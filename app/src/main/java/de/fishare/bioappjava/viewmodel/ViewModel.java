package de.fishare.bioappjava.viewmodel;

import android.support.annotation.NonNull;
import android.view.View;

import de.fishare.bioappjava.DemoAvail;
import de.fishare.bioappjava.DemoPeri;
import de.fishare.bioappjava.listadapter.EasyListAdapter;
import de.fishare.bioappjava.listadapter.IndexPath;

public class ViewModel {
    public void setUpViewFor(@NonNull final DemoAvail avl, @NonNull final CustomVH cv, @NonNull final EasyListAdapter adapter, @NonNull final IndexPath indexPath){
        final View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adapter.listener != null){
                    adapter.listener.onItemClick(v, indexPath);
                }
            }
        };

        cv.btnConnect.setOnClickListener(listener);
        cv.itemView.post(new Runnable() {
            @Override
            public void run() {
                cv.lblName.setText(avl.getName());
                cv.lblMac.setText(avl.getMac());
                cv.btnAction.setVisibility(View.GONE);
            }
        });

    }

    public void updateViewFor(@NonNull final DemoAvail avl, @NonNull final CustomVH cv){
        final String rssi = String.format("%d", avl.getRssi());
        cv.itemView.post(new Runnable() {
            @Override
            public void run() {
                cv.lblRSSI.setText(rssi);
            }
        });
    }

    public void setUpViewFor(final DemoPeri peri, final CustomVH cv, final EasyListAdapter adapter, final IndexPath indexPath){
        cv.itemView.post(new Runnable() {
            @Override
            public void run() {
                cv.lblName.setText(peri.getName());
                cv.lblMac.setText(peri.getMac());
                cv.btnAction.setVisibility(View.VISIBLE);
            }
        });
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adapter.listener != null){
                    adapter.listener.onItemClick(v, indexPath);
                }
            }
        };
        cv.btnConnect.setOnClickListener(listener);
        cv.btnAction.setOnClickListener(listener);

    }

    public void updateViewFor(final DemoPeri peri, final CustomVH cv){
        final String rssi = String.format("%d", peri.getRssi());
        cv.itemView.post(new Runnable() {
            @Override
            public void run() {
                cv.lblRSSI.setText(rssi);
            }
        });
    }

}

