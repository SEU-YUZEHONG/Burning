package com.example.loginproject.data;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;

import com.example.loginproject.Client.ServerClient;
import com.example.loginproject.data.model.Answer;
import com.example.loginproject.data.model.LoggedInUser;

import java.io.IOException;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 * 通过/login凭证处理用户身份认证以及检索用户信息的类
 */
public class LoginDataSource {
    String answer=null;
    /*<>意味这是泛型，即该Result对象中只能放LoggedInUser对象*/
    public Result<LoggedInUser> login(String username, String password) {

        //这里可以开线程返回true Or false 判断能否登录
        try {
            // TODO: handle loggedInUser authentication
            // UUID(Universally Unique Identifier)全局唯一标识符
            LoggedInUser fakeUser =
                    new LoggedInUser(
                            java.util.UUID.randomUUID().toString(),
                            "Jane");
            //前者为用户，后者为展示名
            System.out.println("ReturnString is "+Answer.answer);
            System.out.println(fakeUser.getUserId());
            System.out.println(fakeUser.getDisplayName());
            System.out.println(Answer.answer);
            System.out.println(Boolean.parseBoolean(Answer.answer));
            //不可用Boolean.getBoolean 不区分大小写 True也变成false
            if(Boolean.parseBoolean(Answer.answer)) {
                return new Result.Success<>(fakeUser);
            }else if(Answer.answer.equals("Error"))
            {
                return new Result.Error(new IOException("Error logging in", new Exception()));
            }
            return new Result.Wrong<>(fakeUser);
        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public void logout() {
        // TODO: revoke authentication
    }
}
