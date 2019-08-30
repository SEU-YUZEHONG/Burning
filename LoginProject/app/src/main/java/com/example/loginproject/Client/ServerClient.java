package com.example.loginproject.Client;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class ServerClient implements Runnable {

    private Handler handler;
    private Message message=new Message();
    private URL url = null;
    private HttpURLConnection connection = null;
    private BufferedReader reader = null;
    private PrintWriter writer = null;
    //定义了向数据库发送的函数
    private String requestMethod=null;

    public String getRetrunString() {
        return retrunString;
    }

    public void setRetrunString(String retrunString) {
        this.retrunString = retrunString;
    }

    //定义了返回的字符串
    private String retrunString=null;
    private String httpMethod=null;
    public ServerClient(String requsetMethod,String httpMethod,Handler handler)throws MalformedURLException
    {
        try {
            url = new URL("http://49.235.241.216:8089");
            this.requestMethod=requsetMethod;
            this.handler=handler;
            this.httpMethod=httpMethod;
        }catch(MalformedURLException m)
        {
            System.err.println("Don't input right URL");
        }
    }
    public ServerClient(String url,String requestMethod,String httpMethod,Handler handler) throws MalformedURLException
    {
        try {
            this.url=new URL(url);
            this.requestMethod=requestMethod;
            this.httpMethod=httpMethod;
            this.handler=handler;
        }catch(MalformedURLException m)
        {
            System.err.println("Don't input right URL");
        }
    }
    void Post()
    {
        try
        {
            connection=(HttpURLConnection)url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);
            connection.setReadTimeout(5000);
            connection.setConnectTimeout(5000);
            connection.setRequestProperty("Content-type", "application/json");
            connection.setRequestProperty("Connection", "Keep-Alive");// 维持长连接
            connection.setRequestProperty("Charset", "UTF-8");
            connection.setRequestProperty("Cookie",Cookie.cookie);
            writer=new PrintWriter(connection.getOutputStream());
            String pass=requestMethod;

            writer.print(pass);
            writer.flush();
            writer.close();
            reader=new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response=new StringBuilder();
            String line;
            while((line=reader.readLine())!=null)
            {
                response.append(line);
            }
            System.out.println(response.toString());
            setRetrunString(response.toString());
            message.obj=response;
            message.what=1;
            handler.sendMessage(message);
            //System.out.println("Message is "+message.toString());
        }
        catch(ConnectException e)
        {
            message.obj="Error";
            message.what=4;
            handler.sendMessage(message);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }finally {
            if(reader!=null)
            {
                try{reader.close();}catch (Exception e){e.printStackTrace();}
            }
            if(writer!=null)
            {
                try{writer.close();}catch(Exception e){e.printStackTrace();}
            }
            if(connection!=null)
            {
                connection.disconnect();
            }
        }
    }
    void Get()
    {
        try
        {
            connection=(HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            InputStream in=connection.getInputStream();
            Bitmap bitmap= BitmapFactory.decodeStream(in);
            String key=null;
            for(int i=1;(key=connection.getHeaderFieldKey(i))!=null;i++)
            {
                if(key.equalsIgnoreCase("Cookie")) {
                    System.out.println(connection.getHeaderField(i));
                    Cookie.cookie=connection.getHeaderField(i);
                }
            }
            message.obj=bitmap;
            message.what=2;
            handler.sendMessage(message);
        }catch(ConnectException e)
        {
            message.obj="Error";
            message.what=4;
            handler.sendMessage(message);
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        finally {
            if(connection!=null)
            {
                connection.disconnect();
            }
        }
    }
    @Override
    public void run() {
        if(httpMethod.equalsIgnoreCase("GET"))
        {
            Get();
        }else if(httpMethod.equalsIgnoreCase("POST"))
        {
            Post();
        }else
        {
            message.what=3;
            message.obj="You don't input a right http requeset method.";
            handler.sendMessage(message);
        }
    }

}
