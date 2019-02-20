package com.mygdx.bomberman.dto;

import com.google.gson.Gson;
import com.mygdx.bomberman.utils.FileUtils;

// TODO: This does not look like a very brilliant idea, remake?
public class Constants {
    private static Constants instance = null;

    public int port;
    public String userIdString;
    public String url;
    public String keyClientString;
    public String keyServerString;

    private Constants() {
    }

    public static Constants getInstance() {
        if (instance == null) {
            String json = FileUtils.readAllFile(System.getProperty("user.dir") + "\\jsons\\constants.json");
            instance = new Gson().fromJson(json, Constants.class);
        }
        return instance;
    }
}
