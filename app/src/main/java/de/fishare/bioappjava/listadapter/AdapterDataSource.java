package de.fishare.bioappjava.listadapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

public interface AdapterDataSource {
    void onBindOfRow(RecyclerView.ViewHolder vh, IndexPath indexPath);
    RecyclerView.ViewHolder cellForRow(ViewGroup parent, Integer section);
    Integer numberOfSection();
    Integer numberOfRowIn(Integer section);
}
