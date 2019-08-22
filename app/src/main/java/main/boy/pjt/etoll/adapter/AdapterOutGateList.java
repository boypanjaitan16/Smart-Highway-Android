package main.boy.pjt.etoll.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import main.boy.pjt.etoll.R;
import main.boy.pjt.etoll.afterlogin.CoreActivity;
import main.boy.pjt.etoll.afterlogin.fragment.FragmentOutGateList;
import main.boy.pjt.etoll.response.ResponseRoad;

public class AdapterOutGateList extends RecyclerView.Adapter<AdapterOutGateList.ViewHolder>{
    private Context context;
    private List<ResponseRoad.OutGate> values;


    public AdapterOutGateList(Context context, List<ResponseRoad.OutGate> list){
        this.context    = context;
        this.values     = list;

    }

    @Override
    public AdapterOutGateList.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_list_outgate, parent, false);
        return new AdapterOutGateList.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AdapterOutGateList.ViewHolder holder, int position) {
        position    = holder.getAdapterPosition();
        holder.name.setText(values.get(position).getName());
        holder.price.setText("Rp. "+values.get(position).getPrice());
        holder.km.setText(values.get(position).getDistance()+" KM");

        final int finalPosition = position;
        holder.theRow.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FragmentManager fragmentManager = ((CoreActivity)context).getSupportFragmentManager();
                        FragmentOutGateList fragmentOutGateList = (FragmentOutGateList) fragmentManager.findFragmentById(R.id.fragmentContainer);

                        Bundle bundle               = new Bundle();
                        ResponseRoad.OutGate outGate  = values.get(finalPosition);
                        //Fragment fragment           = new FragmentRoadDetail();

                        bundle.putSerializable("gate", outGate);
                        //fragment.setArguments(bundle);

                        fragmentOutGateList.showDetailRoad(bundle);
                    }
                }
        );
    }

    @Override
    public int getItemCount() {
        return values.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        TextView km;
        LinearLayout theRow;
        TextView price;


        ViewHolder(View itemView) {
            super(itemView);
            name            = itemView.findViewById(R.id.name);
            km              = itemView.findViewById(R.id.km);
            theRow          = itemView.findViewById(R.id.the_row);
            price           = itemView.findViewById(R.id.price);


        }
    }
}
