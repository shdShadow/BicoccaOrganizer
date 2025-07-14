package shdShadow.BicoccaOrganizer.DataManager;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import shdShadow.BicoccaOrganizer.util.Shared;
import shdShadow.BicoccaOrganizer.util.UserCookies;

public class CookieManager {
    private Shared cond;
    
    public CookieManager(Shared condivisa){
        this.cond = condivisa;
    }

    public void saveToJson(){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String fullpath = cond.getBaseDataPath();
        File cookieDirectory = new File(fullpath);
        if (!cookieDirectory.exists()){
            cookieDirectory.canWrite();
            cookieDirectory.mkdirs();
        }
        try(FileWriter writer = new FileWriter(fullpath + "cookies.json")){
            gson.toJson(cond.getCookie(), writer);
        }catch (IOException e){
            System.out.println(e.getMessage());
            System.exit(-1);
        }
    }

    public void readCookiesFromJson(){
        Gson gson = new Gson();
        try (FileReader reader = new FileReader(cond.getBaseDataPath() + "cookies.json")){
            cond.setCookie(gson.fromJson(reader, UserCookies.class));
        }catch (IOException ex){
            cond.setCookie(null);
        }
    }
    
}
