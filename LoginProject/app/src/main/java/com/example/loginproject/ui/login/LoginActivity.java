package com.example.loginproject.ui.login;


import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.loginproject.Client.ServerClient;
import com.example.loginproject.R;
import com.example.loginproject.data.model.Answer;

import java.net.MalformedURLException;

public class LoginActivity extends AppCompatActivity {
    String username;
    String password;
    /*验证码*/
    private ImageView idCode;
    private LoginViewModel loginViewModel;
    private Handler handler=new Handler()
    {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            /*Answer.answer=msg.obj.toString();
            System.out.println(Answer.answer);
            System.out.println("Get From subThread"+msg.toString());
            loginViewModel.login(username,
                    password);*/
            switch (msg.what)
            {
                case 1: Answer.answer=msg.obj.toString();System.out.println(Answer.answer);
                    System.out.println("Get From subThread"+msg.toString());
                    loginViewModel.login(username,
                            password);break;
                case 2:
                    Bitmap bitmap=(Bitmap) msg.obj;idCode.setImageBitmap(bitmap);break;
                case 3: System.out.println(msg.obj.toString()+" Please input again.");break;
                case 4: Answer.answer=msg.obj.toString();System.out.println(Answer.answer);
                    System.out.println("Get From subThread"+msg.toString());
                    loginViewModel.login(username,
                            password);break;
                default:break;
            }
        }
    };
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ServerClient s=null;
        try {
            s=new ServerClient("","GET",handler);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        new Thread(s).start();
        loginViewModel = ViewModelProviders.of(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
        /*用户名文本编辑*/
        final EditText usernameEditText = findViewById(R.id.username);
        /*密码文本编辑*/
        final EditText passwordEditText = findViewById(R.id.password);
        /*验证码文本编辑*/
         final EditText idcodeEditText=findViewById(R.id.identification);
        /*登录按钮*/
        final Button loginButton = findViewById(R.id.login);
        /*进度条*/
        final ProgressBar loadingProgressBar = findViewById(R.id.loading);
        idCode=findViewById(R.id.idCode);
        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                /*setEnabled()函数设置控件是否可以点击 true：可点击 false：不可点击*/
                //如果登录格式正确便可以点击登录按钮
                loginButton.setEnabled(loginFormState.isDataValid());
                //loginButton.setEnabled(false);
                if (loginFormState.getUsernameError() != null) {
                    //输出登录名格式问题
                    usernameEditText.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    //输出登录密码格式问题
                    passwordEditText.setError(getString(loginFormState.getPasswordError()));
                }

            }
        });

        loginViewModel.getLoginResult().observe(this, new Observer<LoginResult>() {
            @Override
            public void onChanged(@Nullable LoginResult loginResult) {
                if (loginResult == null) {
                    return;
                }
                //这里更改进度条
                loadingProgressBar.setVisibility(View.GONE);
                if (loginResult.getError() != null) {
                    showLoginFailed(loginResult.getError());
                    ServerClient s=null;
                    try {
                        s=new ServerClient("","GET",handler);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                    new Thread(s).start();
                }
                if (loginResult.getSuccess() != null) {
                    updateUiWithUser(loginResult.getSuccess());
                    finish();
                }
                setResult(Activity.RESULT_OK);

                //Complete and destroy login activity once successful
                //登录界面一旦成果就取消
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        /*该控件并不是在我们编辑TextView时触发，也不是在我们点击TextView时触发，而是我们编辑完TextView时触发
        * 即按下完成按钮之后，撤回软键盘*/
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    /*loginViewModel.login(usernameEditText.getText().toString(),
                            passwordEditText.getText().toString());*/
                    //return true;
                }
                return false;
            }
        });
        usernameEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    /*loginViewModel.login(usernameEditText.getText().toString(),
                            passwordEditText.getText().toString());*/
                    //return true;
                }
                return false;
            }
        });
        idcodeEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    /*loginViewModel.login(usernameEditText.getText().toString(),
                            passwordEditText.getText().toString());*/
                    //return true;
                }
                return false;
            }
        });
        loginButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                username=usernameEditText.getText().toString();
                password=passwordEditText.getText().toString();
                System.out.println("use");
                String method = "\"{\\\"method\\\":\\\"userLogin\\\",\\\"data\\\":[{\\\"userAccount\\\": \\\"" + username+ "\\\", " +
                        "\\\"userPassword\\\": \\\"" + password + "\\\",\\\"verCode\\\": \\\""+idcodeEditText.getText().toString()+"\\\",\\\"userType\\\":\\\"normal\\\"}]}\"";
                ServerClient s = null;
                try {
                    s = new ServerClient(method,"POST",handler);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                new Thread(s).start();
                //判断登录请求
            }
        });
    }

    private void updateUiWithUser(LoggedInUserView model) {
        String welcome = getString(R.string.welcome) + model.getDisplayName();
        // TODO : initiate successful logged in experience
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }
}
