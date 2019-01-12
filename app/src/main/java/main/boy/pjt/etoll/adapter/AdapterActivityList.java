package main.boy.pjt.etoll.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import main.boy.pjt.etoll.R;
import main.boy.pjt.etoll.values.ValueActivity;

/**
 * Created by Boy Panjaitan on 15/06/2018.
 */

public class AdapterActivityList extends RecyclerView.Adapter<AdapterActivityList.ViewHolder>{
    private List<ValueActivity.Values> values;

    public AdapterActivityList(List<ValueActivity.Values> valuesList){
        this.values = valuesList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_list_activity, parent, false);
        return new AdapterActivityList.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.name.setText(values.get(position).getRoad().getName());
        holder.date.setText(values.get(position).getDate());
        holder.type.setText("("+values.get(position).getType()+")");
    }

    @Override
    public int getItemCount() {
        return values.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        TextView date;
        TextView type;

        ViewHolder(View itemView) {
            super(itemView);
            name            = itemView.findViewById(R.id.name);
            date            = itemView.findViewById(R.id.date);
            type            = itemView.findViewById(R.id.type);

        }
    }

}
