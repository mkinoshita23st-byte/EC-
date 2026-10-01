package JavaBeans;

import java.io.Serializable;

public class LoginResult implements Serializable{
    private boolean isSuccess;
    private JavaBeans.User userInf;
//2つの返却値が欲しくて
    public LoginResult(boolean isSuccess, JavaBeans.User userInf) {
        this.isSuccess = isSuccess;
        this.userInf = userInf;
    }
    
    public boolean getIsSuccess() {
        return isSuccess;
    }

    public JavaBeans.User getUserInf() {
        return userInf;
    }
}