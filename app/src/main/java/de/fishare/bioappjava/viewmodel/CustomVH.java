package de.fishare.bioappjava.viewmodel;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import de.fishare.bioappjava.R;

public class CustomVH extends RecyclerView.ViewHolder {
    public CustomVH(View itemView) {
        super(itemView);
    }

    public TextView lblName = (TextView) itemView.findViewById(R.id.lblName);
    public TextView lblMac  = (TextView) itemView.findViewById(R.id.lblMac);
    public TextView lblData = (TextView) itemView.findViewById(R.id.lblData);
    public TextView lblRSSI = (TextView) itemView.findViewById(R.id.lblRSSI);
    public Button btnConnect = (Button) itemView.findViewById(R.id.btnConnect);
    public Button btnAction  = (Button) itemView.findViewById(R.id.btnAction);
    public Button btnScan    = (Button) itemView.findViewById(R.id.btnScan);
}
