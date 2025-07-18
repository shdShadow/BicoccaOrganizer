package shdShadow.BicoccaOrganizer.DataManager;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JOptionPane;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import shdShadow.BicoccaOrganizer.Interfaces.IJsonIO;
import shdShadow.BicoccaOrganizer.util.Constants;
import shdShadow.BicoccaOrganizer.util.Shared;
import shdShadow.BicoccaOrganizer.util.UserCookies;

public class CookieManager implements IJsonIO<Void, UserCookies> {
    private Shared cond;
    
    public CookieManager(Shared condivisa){
        this.cond = condivisa;
    }
    @Override
    public Void saveToJson(UserCookies cookies) throws IOException{
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String fullpath = cond.getBaseDataPath();
        File cookieDirectory = new File(fullpath);
        if (!cookieDirectory.exists()){
            cookieDirectory.canWrite();
            cookieDirectory.mkdirs();
        }
        FileWriter writer = new FileWriter(fullpath + Constants.COOKIES_FILE);
            gson.toJson(cond.getCookie(), writer);
            //if i cant write the file, then something is wrong, i propagate the exception to the caller
            //i dont have permits
            //there's no more space available
            //i dont know ...
            //show the error window and close the application
            writer.close();
        return null;
        }
    
    @Override
    public UserCookies loadFromJson(){
        Gson gson = new Gson();
        try (FileReader reader = new FileReader(cond.getBaseDataPath() + Constants.COOKIES_FILE)){
            return gson.fromJson(reader, UserCookies.class);
        }catch (IOException ex){
            System.out.println(ex.getMessage());
            return null;
        }
    }
    
}
