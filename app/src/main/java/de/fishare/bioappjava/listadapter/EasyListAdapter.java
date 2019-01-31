package de.fishare.bioappjava.listadapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;


public class EasyListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public AdapterDataSource dataSource;
    public ItemEvent listener;

    public interface ItemEvent{
        void onItemClick(View view, IndexPath indexPath );
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return dataSource.cellForRow(viewGroup, i);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if(dataSource != null){
            dataSource.onBindOfRow(viewHolder, getIndexPath(i));
        }
    }

    @Override
    public int getItemCount() {
        Integer secCap = 0;
        Integer sum = 0;
        if(dataSource != null){
            secCap = dataSource.numberOfSection();
            for(int i = 0; i < secCap ; ++i){
                sum += dataSource.numberOfRowIn(i);
            }
        }
        return sum;
    }

    IndexPath getIndexPath(Integer position){
       Integer secCap = 0;
       if(dataSource != null){
           dataSource.numberOfSection();
       }
       return getRecursiveIdxPath(position, new IndexPath(0, position), secCap);
    }

    private IndexPath getRecursiveIdxPath(Integer pos, IndexPath indexPath, Integer cap){
        Integer section = indexPath.section;
        Integer numberOfRow = 0;
        if(dataSource != null){
            dataSource.numberOfRowIn(section);
        }

        if(pos < numberOfRow || section >= cap){
            indexPath.row = pos;
            return  indexPath;
        }else {
            indexPath.section += 1;
            return getRecursiveIdxPath(pos - numberOfRow, indexPath, pos);
        }
    }

    public void reload(){
       notifyDataSetChanged();
    }

    public Integer getPositionOf(IndexPath indexPath){
       Integer position = indexPath.row;
       Integer section  = indexPath.section - 1;
       if(section < 0) return position;
       for(int idx =0; idx <= section; idx ++){
           int numberOfRow = 0;
           if(dataSource != null){
               numberOfRow = dataSource.numberOfRowIn(idx);
           }
           position += numberOfRow;
       }
       return position;
    }

//    public static class CustomVH extends RecyclerView.ViewHolder {
//        public CustomVH(View itemView) {
//            super(itemView);
//        }
//
//        public TextView lblName = (TextView) itemView.findViewById(R.id.lblName);
//        public TextView lblMac  = (TextView) itemView.findViewById(R.id.lblMac);
//        public TextView lblData = itemView.findViewById(R.id.lblData);
//        public TextView lblRSSI = itemView.findViewById(R.id.lblRSSI);
//        public Button btnConnect = itemView.findViewById(R.id.btnConnect);
//        public Button btnAction  = itemView.findViewById(R.id.btnAction);
//        public Button btnScan    = itemView.findViewById(R.id.btnScan);
//    }

}





