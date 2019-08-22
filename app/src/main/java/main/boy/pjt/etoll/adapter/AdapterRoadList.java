package main.boy.pjt.etoll.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
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
import main.boy.pjt.etoll.afterlogin.fragment.FragmentRoadDetail;
import main.boy.pjt.etoll.afterlogin.fragment.FragmentRoadList;
import main.boy.pjt.etoll.interfaces.OnGateItemClickInterface;
import main.boy.pjt.etoll.response.ResponseRoad;

/**
 * Created by Boy Panjaitan on 09/02/2018.
 */

public class AdapterRoadList extends RecyclerView.Adapter<AdapterRoadList.ViewHolder> {
    private Context context;
    private List<ResponseRoad.Values> values;
    private OnGateItemClickInterface gateItemClickInterface;

    public AdapterRoadList(Context context, List<ResponseRoad.Values> values, OnGateItemClickInterface itemClickInterface) {
        this.context    = context;
        this.values      = values;
        this.gateItemClickInterface = itemClickInterface;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_list_road, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.name.setText(values.get(position).getName());
        //holder.roda4.setText("Rp. "+values.get(position).getPrice4());
        //holder.roda6.setText("Rp. "+values.get(position).getPrice6());
        holder.totalOut.setText(values.get(position).getOutGates().size()+" Gerbang keluar");
        //holder.km.setText(values.get(position).getDistance()+" Km");

        final int tmpPosition = position;
        holder.theRow.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        gateItemClickInterface.onItemClick(values.get(tmpPosition));
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
        //TextView roda4;
        //TextView roda6;
        //TextView km;
        LinearLayout theRow;
        TextView totalOut;


        ViewHolder(View itemView) {
            super(itemView);
            name            = itemView.findViewById(R.id.name);
            //roda4           = itemView.findViewById(R.id.roda4);
            //roda6           = itemView.findViewById(R.id.roda6);
            totalOut           = itemView.findViewById(R.id.totalOut);
            //km              = itemView.findViewById(R.id.km);
            theRow          = itemView.findViewById(R.id.the_row);


        }
    }

}
