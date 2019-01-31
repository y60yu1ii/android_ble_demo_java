package de.fishare.bioappjava;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import de.fishare.bioappjava.listadapter.AdapterDataSource;
import de.fishare.bioappjava.listadapter.EasyListAdapter;
import de.fishare.bioappjava.listadapter.IndexPath;
import de.fishare.bioappjava.viewmodel.CustomVH;
import de.fishare.bioappjava.viewmodel.ViewModel;
import de.fishare.lumosble.AvailObj;
import de.fishare.lumosble.CentralManager;
import de.fishare.lumosble.CentralManagerBuilder;
import de.fishare.lumosble.Event;
import de.fishare.lumosble.PeriObj;

public class MainActivity extends AppCompatActivity implements AvailObj.Listener, PeriObj.Listener {
    CentralManager centralManager;
    NotifCenter notifCenter = NotifCenter.getInstance(this);
    RecyclerView recyclerView;
    EasyListAdapter adapter;
    List<DemoAvail> avails = new ArrayList<>();
    List<DemoPeri> peris = new ArrayList<>();
    private Boolean isRegistered = false;
    private Receiver receiver = new Receiver();
    private ViewModel viewModel = new ViewModel();
    private static final int AVAIL_SECTION = 0;
    private static final int PERI_SECTION = 1;
    Button btnScan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        List<String> uuids = new ArrayList<>();
        centralManager = new CentralManagerBuilder(uuids).build(this);

        setContentView(R.layout.activity_main);
        setUpBLECentralManager();
        addBroadcastReceiver();
        initViews();

        btnScan = (Button) findViewById(R.id.btnScan);
        btnScan.setTag(true);
        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               boolean tick = (boolean) v.getTag();
               v.setTag(!tick);
               if((boolean) v.getTag()){
                  btnScan.post(new Runnable() {
                      @Override
                      public void run() {
                          btnScan.setText("STOP");
                          centralManager.scan();
                      }
                  });
               }else {
                   btnScan.post(new Runnable() {
                       @Override
                       public void run() {
                           btnScan.setText("SCAN");
                           centralManager.stopScan();
                       }
                   });
               }
            }
        });

    }

    private void setUpBLECentralManager(){
       centralManager.setEvent(new CentralManager.EventListener() {
           @Override
           public void didDiscover(AvailObj availObj) {
               Log.e("main", " found mac is " + availObj.getMac());
               refreshAll();
           }
       });

       centralManager.setSetting(new CentralManager.Setting() {
           @NonNull
           @Override
           public String getNameRule() {
               //for all name rules
               return ".*?";
           }

           @NonNull
           @Override
           public PeriObj getCustomObj(String mac, String name) {
               return new DemoPeri(mac);
           }

           @NonNull
           @Override
           public AvailObj getCustomAvl(BluetoothDevice bluetoothDevice) {
               return new DemoAvail(bluetoothDevice);
           }
       });
       centralManager.loadHistory();
       centralManager.checkPermit(this);
    }

    private void refreshAll(){
        List<AvailObj> rawAvails =  centralManager.getAvails();
        List<DemoAvail> demoAvails = new ArrayList<>();
        for(AvailObj availObj : rawAvails ){
            DemoAvail a = (DemoAvail) availObj;
            a.setListener(this);
           demoAvails.add(a);
        }
        synchronized (avails){ avails = demoAvails; }

        List<PeriObj> rawPeris =  centralManager.getPeris();
        List<DemoPeri> demoPeris = new ArrayList<>();
        for(PeriObj PeriObj : rawPeris ){
            DemoPeri a = (DemoPeri) PeriObj;
            a.setListener(this);
            demoPeris.add(a);
        }
        synchronized (peris){ peris = demoPeris; }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
               adapter.reload();
            }
        });
    }

    void initViews(){
        adapter = new EasyListAdapter();
        adapter.dataSource = new AdapterDataSource(){
            @Override
            public void onBindOfRow(final RecyclerView.ViewHolder vh, final IndexPath indexPath) {
                if(indexPath.getSection() == AVAIL_SECTION && indexPath.getRow() < avails.size()){
                    viewModel.setUpViewFor(avails.get(indexPath.getRow()), (CustomVH) vh , adapter, indexPath);
                }else if(indexPath.getSection() == PERI_SECTION && indexPath.getRow() < peris.size()){
                    viewModel.setUpViewFor(peris.get(indexPath.getRow()), (CustomVH) vh , adapter, indexPath);
                }
            }

            @Override
            public RecyclerView.ViewHolder cellForRow(ViewGroup parent, Integer section) {
                View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.cell_device, parent, false);
                return new CustomVH(view);
            }

            @Override
            public Integer numberOfSection() {
                return 2;
            }

            @Override
            public Integer numberOfRowIn(Integer section) {
                switch (section){
                    case AVAIL_SECTION :
                        return avails.size();
                    case PERI_SECTION :
                        return peris.size();
                    default:
                        return 0;
                }

            }
        };

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private CustomVH getCustomItemOf(int type, String mac){
        int idx = getIdxOf(type, mac);
        if(idx != -1){
            int position = adapter.getPositionOf(new IndexPath(type, idx));
            RecyclerView.ViewHolder vh = recyclerView.findViewHolderForAdapterPosition(position);
            if(vh != null){
               return (CustomVH) vh;
            }
        }
        return null;
    }

    private int getIdxOf(int type, String mac){
        if(type == AVAIL_SECTION){
            synchronized (avails){
                for(AvailObj availObj : avails){
                    if(availObj.getMac().equals(mac)){
                        return avails.indexOf(availObj);
                    }
                }
            }
        }else if(type == PERI_SECTION){
            synchronized (peris){
                for(PeriObj periObj : peris){
                    if(periObj.getMac().equals(mac)){
                        return peris.indexOf(periObj);
                    }
                }
            }
        }

        return -1;
    }

    /**
     * Broadcast handler
     **/
    //on RSSI changed of avail
    @Override
    public void onRSSIChanged(int i, final AvailObj availObj) {
//        Log.e("main", "onrssi update rssi " + i + "mac is " + availObj.getMac());
        final CustomVH vh = getCustomItemOf(AVAIL_SECTION, availObj.getMac());
        if(vh != null){
            viewModel.updateViewFor((DemoAvail) availObj, vh);
        }
    }

    @Override
    public void onUpdated(String label, Object value, final AvailObj availObj) {
        final CustomVH vh = getCustomItemOf(AVAIL_SECTION, availObj.getMac());
        if(vh != null){
            viewModel.updateViewFor((DemoAvail) availObj, vh);
        }
        if(label.equals("lumenData") && ((int) value < 50)){
            Map<String, String> payload = new HashMap<>();
            payload.put("title", "Alert");
            payload.put("body", "Light is Dimmed!!1");
            notifCenter.send(payload);
            notifCenter.beep();
        }
    }

    /**
     * notify handler
     **/
    @Override
    public void onRSSIChanged(int i, PeriObj periObj) {
        CustomVH vh = getCustomItemOf(AVAIL_SECTION, periObj.getMac());
        if(vh != null){ viewModel.updateViewFor((DemoPeri) periObj, vh); }
    }

    @Override
    public void onUpdated(String label, Object value, PeriObj periObj) {
        CustomVH vh = getCustomItemOf(AVAIL_SECTION, periObj.getMac());
        if(vh != null){ viewModel.updateViewFor((DemoPeri) periObj, vh); }
    }

    private void addBroadcastReceiver(){
        IntentFilter filter = new IntentFilter();
        filter.addAction(Event.CONNECTION);
        filter.addAction(Event.REFRESH);
        registerReceiver(receiver, filter);
        isRegistered = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(isRegistered){
           unregisterReceiver(receiver);
        }
    }

    /**
     *  Broadcast relative
     *
     **/
    class Receiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            //null safety
            if(Objects.equals(intent.getAction(), Event.CONNECTION)){
                String mac = intent.getStringExtra("mac");
                boolean isConnected = intent.getBooleanExtra("connected", false);
                if(mac != null){
                    Log.e("MainActivity", mac + "is " + ((isConnected)?"CONNECTED":"DISCONNECTED") );
                }

                refreshAll();
            }else if(Objects.equals(intent.getAction(), Event.REFRESH)) {
                refreshAll();
                centralManager.clearOutdateAvl();
            }else {
                Log.e("MainActivity", "event" + intent.getAction());
            }
        }
    }
}
