package com.volmit.react.configuration;

import com.google.gson.Gson;
import com.volmit.react.React;
import com.volmit.react.api.monitor.configuration.MonitorConfiguration;
import com.volmit.react.util.IO;
import com.volmit.react.util.JSONObject;
import lombok.Data;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Data
public class PlayerSettings {
    private MonitorConfiguration monitorConfiguration;
    private boolean actionBarMonitoring = false;

    public static PlayerSettings get(UUID player) {
        PlayerSettings dummy = new PlayerSettings();
        PlayerSettings configuration = null;
        File l = React.instance.getDataFile("player-settings", player.toString() + ".json");

        if (!l.exists()) {
            try {
                IO.writeAll(l, new JSONObject(new Gson().toJson(dummy)).toString(4));
            } catch (IOException e) {
                e.printStackTrace();
                configuration = dummy;
                return dummy;
            }
        }

        try {
            configuration = new Gson().fromJson(IO.readAll(l), PlayerSettings.class);
            IO.writeAll(l, new JSONObject(new Gson().toJson(configuration)).toString(4));
        } catch (IOException e) {
            e.printStackTrace();
            configuration = new PlayerSettings();
        }

        return configuration;
    }
}
