package shdShadow.BicoccaOrganizer.util;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

public class UserCookies {
    @SerializedName("Elearning-SAMLSessionID")
    private String ElearningSamlSessionID;
    private String MoodleSession;
    @SerializedName("Elearning-SAMLAuthToken")
    private String ElearningSamlAuthToken;

    public String getElearningSamlSessionID() {
        return ElearningSamlSessionID;
    }

    public void setElearningSamlSessionID(String elearningSamlSessionID) {
        ElearningSamlSessionID = elearningSamlSessionID;
    }

    public String getMoodleSession() {
        return MoodleSession;
    }

    public void setMoodleSession(String moodleSession) {
        MoodleSession = moodleSession;
    }

    public String getElearningSamlAuthToken() {
        return ElearningSamlAuthToken;
    }

    public void setElearningSamlAuthToken(String elearningSamlAuthToken) {
        ElearningSamlAuthToken = elearningSamlAuthToken;
    }

    public UserCookies() {
        this.ElearningSamlSessionID = "";
        this.MoodleSession = "";
        this.ElearningSamlAuthToken = "";
    }

    public void SerializaCookie() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(this);
        System.out.println(json);
    }

    private Map<String, String> buildCookieMap(UserCookies cookies) {
        Map<String, String> cookieMap = new HashMap<>();

        if (cookies.getElearningSamlSessionID() != null) {
            cookieMap.put("Elearning-SAMLSessionID", cookies.getElearningSamlSessionID());
        }
        if (cookies.getMoodleSession() != null) {
            cookieMap.put("MoodleSession", cookies.getMoodleSession());
        }
        if (cookies.getElearningSamlAuthToken() != null) {
            cookieMap.put("Elearning-SAMLAuthToken", cookies.getElearningSamlAuthToken());
        }
        
        return cookieMap;
    }

    public String buildCookieHeader() {
        return String.format(
        "Elearning-SAMLSessionID=%s; MoodleSession=%s; Elearning-SAMLAuthToken=%s",
        ElearningSamlSessionID,
        MoodleSession,
        ElearningSamlAuthToken
    );
    }

}
