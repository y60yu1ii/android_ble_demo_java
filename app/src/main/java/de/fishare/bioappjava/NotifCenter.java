package de.fishare.bioappjava;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Build;
import android.os.Handler;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

class NotifCenter implements Response.ErrorListener, Response.Listener<String> {
    private Context context;
    private static NotifCenter instance;

    static synchronized NotifCenter getInstance(Context context) {
        if (instance == null) {
            instance = new NotifCenter(context);
        }
        return instance;
    }

    private NotifCenter(Context context) {
        this.context = context;
    }

    private String chanelID = "chanelID";
    private int notifID = 9627;

    public void send(Map<String, String> dict) {
        Resources r = context.getResources();
        Bitmap icon = BitmapFactory.decodeResource(r, R.mipmap.ic_launcher);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(context);
        builder.setContentTitle(dict.get("title"))
                .setContentText(dict.get("body"))
                .setLargeIcon(icon)
                .setAutoCancel(true)
                .setSmallIcon(R.mipmap.ic_launcher_round);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(chanelID);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(chanelID, r.getString(R.string.app_name), importance);
            notificationManager.createNotificationChannel(mChannel);
        }

        notificationManager.notify(notifID, builder.build());
    }

    public void beep() {
        ToneGenerator tokenGen = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
        tokenGen.startTone(ToneGenerator.TONE_CDMA_ABBR_INTERCEPT, 250);
    }

    public void sendMail() {
        String urlAPI = "https://script.google.com/macros/s/AKfycbxHBZqYqsF84kffNpgZMTEhFSP-MeXaXmK55kbk3y_sahl2kAYS/exec";
        String to = "?to=" + "aa@mail.com";
        Map<String, Double> gpsMap = getLocation();
        String lat = "&lat=" + gpsMap.get("lat");
        String lng = "&lng=" + gpsMap.get("lng");
        String url = urlAPI + to + lat + lng;
        final RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, this, this);
        stringRequest.setTag("mail");
        queue.add(stringRequest);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                queue.cancelAll("mail");
            }
        }, 15000);
    }

    private Map<String, Double> getLocation() {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        @SuppressLint("MissingPermission")
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        Map<String, Double> map = new HashMap<>();
        if(location != null){
            map.put("lat", location.getLatitude());
            map.put("lng", location.getLongitude());
        }else {
            map.put("lat", 0.0);
            map.put("lng", 0.0);
        }

        return map;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Log.e("queue", "error is " + error);
    }

    @Override
    public void onResponse(String response) {
        Log.e("queue",  response);
    }
}
