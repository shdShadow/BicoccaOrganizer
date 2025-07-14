package shdShadow.BicoccaOrganizer.util;

import java.io.File;

public class Shared {
    private UserCookies sharedCookie;
    private String OS;
    private String baseDataPath;
    public String getBaseDataPath() {
        return baseDataPath;
    }
    public Shared(){
        String temp = System.getProperty("os.name").toLowerCase();
        if (temp.contains("win")){
            baseDataPath = System.getenv("APPDATA");
            OS = "WINDOWS";
        }else if(temp.contains("mac")){
            baseDataPath = System.getProperty("user.home") + "Library/Application Support";
            OS = "MACOS";
        }else{
            baseDataPath = System.getProperty("user.home") + ".config/";
            OS = "LINUX";
        }

        baseDataPath += File.separator + "BicoccaOrganizer" + File.separator;
    }
    public UserCookies getCookie(){
        return sharedCookie;

    }
    public void setCookie(UserCookies cookie){
        this.sharedCookie = cookie;
    }
}
