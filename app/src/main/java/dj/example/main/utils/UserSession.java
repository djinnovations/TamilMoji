package dj.example.main.utils;

import dj.example.main.model.UserInfo;

/**
 * Created by DJphy on 31-07-2017.
 */

public class UserSession {

    private static UserSession ourInstance;
    private String fcmToken;

    public static UserSession getInstance(){
        if (ourInstance == null){
            ourInstance = new UserSession();
        }
        return ourInstance;
    }

    private UserSession(){

    }

    public void clearInstance(){
        ourInstance = null;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    private UserInfo userInfo;

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }
}
