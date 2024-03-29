package main.boy.pjt.etoll.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import main.boy.pjt.etoll.R;
import main.boy.pjt.etoll.afterlogin.ChooseDeviceActivity;
import main.boy.pjt.etoll.response.ResponseDevices;

/**
 * Created by Boy Panjaitan on 10/06/2018.
 */

public class AdapterBluetoothList extends RecyclerView.Adapter<AdapterBluetoothList.ViewHolder> {
    private List<ResponseDevices> responseDevices;
    private Context context;

    public AdapterBluetoothList(Context context,List<ResponseDevices> devices){
        this.responseDevices = devices;
        this.context        = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_list_bluetooth, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.name.setText(responseDevices.get(position).getName());
        holder.address.setText(responseDevices.get(position).getAddress());

        holder.row.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ((ChooseDeviceActivity)context).setResult(responseDevices.get(position).getAddress());
                    }
                }
        );
    }

    @Override
    public int getItemCount() {
        return responseDevices.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        TextView address;
        LinearLayout row;

        ViewHolder(View itemView) {
            super(itemView);
            name        = itemView.findViewById(R.id.name);
            address     = itemView.findViewById(R.id.address);
            row         = itemView.findViewById(R.id.row);

        }
    }
}
