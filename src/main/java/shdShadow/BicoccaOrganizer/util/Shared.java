package shdShadow.BicoccaOrganizer.util;

import java.io.File;

import shdShadow.BicoccaOrganizer.Status;
import shdShadow.BicoccaOrganizer.Status.ApplicationStatus;

public class Shared {
    private UserCookies sharedCookie;
    private String OS;
    private String baseDataPath;
    private boolean isCrashed;
    public ApplicationStatus status;
    public boolean isCrashed() {
        return isCrashed;
    }
    public void setCrashed(boolean isCrashed) {
        this.isCrashed = isCrashed;
    }
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
        isCrashed = false;
    }
    public UserCookies getCookie(){
        return sharedCookie;

    }
    public void setCookie(UserCookies cookie){
        this.sharedCookie = cookie;
    }

    public synchronized void waitForChange(ApplicationStatus oldStatus){
        while(this.status == oldStatus){
            try {
                wait();
            }catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
    public synchronized void changeStatus(ApplicationStatus newStatus) {
        this.status = newStatus;
        notifyAll(); // wake up waiting thread
    }
}
