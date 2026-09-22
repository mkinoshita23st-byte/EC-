package JavaBeans;

import java.io.Serializable;

public class LoginResult implements Serializable{
    private boolean success;
    private JavaBeans.User userInf;
//2つの返却値が欲しくて
    public LoginResult(boolean success, JavaBeans.User userInf) {
        this.success = success;
        this.userInf = userInf;
    }
    
    public boolean isSuccess() {
        return success;
    }

    public JavaBeans.User getUserInf() {
        return userInf;
    }
}