package de.fishare.bioappjava;

import android.support.annotation.NonNull;

import java.util.logging.Handler;

import de.fishare.lumosble.GattController;
import de.fishare.lumosble.PeriObj;

import static de.fishare.lumosble.UtilsKt.hex4Human;
import static de.fishare.lumosble.UtilsKt.print;
import static de.fishare.lumosble.UtilsKt.to2Int;

public class DemoPeri extends PeriObj {
    DemoPeri(String mac) { super(mac); }
    public Integer lumenData = 0 ;
    String tx = "2a05";
    String rx = "2a04";
    String tag = "DemoPeri";

    @Override
    public void authAndSubscribe() {
        super.authAndSubscribe();
        print(tag, "auth and start");
    }

    public void write(String cmd){
        if(getController() != null){
           getController().writeTo(rx, cmd.getBytes(), true);
        }
    }

    @Override
    public void getUpdated(@NonNull String uuidStr, @NonNull byte[] value, @NonNull GattController.UpdateKind kind) {
        super.getUpdated(uuidStr, value, kind);
        if(kind == GattController.UpdateKind.Notify){
            print(tag, "[Notify] is "+ uuidStr + " has "+ hex4Human(value) +" int is "+ to2Int(value));
        }
    }
}
