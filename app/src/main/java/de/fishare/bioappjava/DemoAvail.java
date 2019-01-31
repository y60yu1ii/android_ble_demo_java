package de.fishare.bioappjava;

import android.bluetooth.BluetoothDevice;
import android.support.annotation.NonNull;

import de.fishare.lumosble.AvailObj;

public class DemoAvail extends AvailObj {
    String tag = "DemoAvail";
    Integer lumenData = 0;
    public DemoAvail(BluetoothDevice device) {
        super(device);
    }

    @Override
    public void onRawUpdate(@NonNull byte[] data) {

        super.onRawUpdate(data);
//        Map<Integer, byte[]> dataMap = parseScanRecord(data);
//        try {
//            byte[] rData = Arrays.copyOfRange(dataMap.get(-1), 2, 3);
//            lumenData = UtilsKt.to2unsignedInt(rData);
//            if(getListener() != null){
//                getListener().onUpdated("lumenData", lumenData, this);
//            }
//        }catch (Exception e){
//           print(tag , "exception " + e.getMessage());
//        }
    }
}
