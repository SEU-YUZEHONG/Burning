package com.example.loginproject.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.util.Patterns;

import com.example.loginproject.data.LoginRepository;
import com.example.loginproject.data.Result;
import com.example.loginproject.data.model.Answer;
import com.example.loginproject.data.model.LoggedInUser;
import com.example.loginproject.R;

/*LiveData是一个数据持有类。它具有以下特点：

数据可以被观察者订阅；

能够感知组件（Fragment、Activity、Service）的生命周期；

只有在组件出于激活状态（STARTED、RESUMED）才会通知观察者有数据更新；*/
public class LoginViewModel extends ViewModel {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private LoginRepository loginRepository;

    /*构造函数*/
    LoginViewModel(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }
    /*获取登录界面格式的状态*/
    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }
    /*获取登录结果*/
    LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    public void login(String username, String password) {
        // can be launched in a separate asynchronous job
        /*可以被加载进一个分离的异步工作*/
        //导入的确实是用户名以及密码
        Result<LoggedInUser> result = loginRepository.login(username, password);
        /*判断result是否为Result.Success的一个实例*/
        //System.out.println("Use it");
        if (result instanceof Result.Success) {
            LoggedInUser data = ((Result.Success<LoggedInUser>) result).getData();
            loginResult.setValue(new LoginResult(new LoggedInUserView("Right")));
        }else if(result instanceof  Result.Wrong)
        {
            if(Answer.answer.equalsIgnoreCase("vercodeError"))
                loginResult.setValue((new LoginResult(R.string.login_verWrong)));
            else
                loginResult.setValue(new LoginResult(R.string.login_wrong));
        }
        else {
            /*如果无法创建实例，那么就是登录失败*/
            loginResult.setValue(new LoginResult(R.string.login_failed));
        }
    }

    public void loginDataChanged(String username, String password) {
        if (!isUserNameValid(username)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        if (username.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            return !username.trim().isEmpty();
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }
}
