package com.example.sachin.fms_client_v1;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

/**
 * Created by sachin on 4/6/2016.
 */
public class StatusAdapter extends RecyclerView.Adapter<StatusAdapter.Status_View> {


    List<StatusData> list= Collections.emptyList();
    Context context;

    int count=0;
    public StatusAdapter(Context context,List<StatusData> list){
        this.context=context;
        this.list=list;
    }

    itemClick click;
    @Override
    public Status_View onCreateViewHolder(ViewGroup parent, int viewType) {

        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.status_list_view,parent,false);

        Status_View holder= new Status_View(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(Status_View holder, int position) {

        holder.title.setText(list.get(position).title);
        //holder.date.setText(list.get(position).date);
        holder.location.setText(list.get(position).location);
        holder.complaint.setText(list.get(position).complaint);
        holder.priority.setText(list.get(position).priority);
        holder.status.setText(list.get(position).status);
        holder.date.setText(list.get(position).date);
        holder.unit.setText(list.get(position).unit);
        if(count%2==0 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            //holder.r1.setBackground(new ColorDrawable(Color.parseColor("#EEEEEE")));
        }
        else if(count%2!=0 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN ) {
           // holder.r1.setBackground(new ColorDrawable(Color.parseColor("#BDBDBD")));

        }
        count++;

    }

    public void SetOnClick(itemClick click){
        this.click=click;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Status_View extends RecyclerView.ViewHolder implements View.OnClickListener{

        CardView cv;
        RelativeLayout r1;

        TextView title,location,complaint,priority,status,date,unit;
        public Status_View(View itemView) {
            super(itemView);
            cv=(CardView) itemView.findViewById(R.id.status_card_view);
            title=(TextView)itemView.findViewById(R.id.title);

            date=(TextView)itemView.findViewById(R.id.sdate);
            unit=(TextView)itemView.findViewById(R.id.unit);
            location=(TextView)itemView.findViewById(R.id.location);
            complaint=(TextView)itemView.findViewById(R.id.complaint);
            priority=(TextView)itemView.findViewById(R.id.priority);
            status=(TextView)itemView.findViewById(R.id.status);
            r1=(RelativeLayout) itemView.findViewById(R.id.r_1);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

            if(click!=null){
                click.itemClick(v,getAdapterPosition());
            }

        }
    }




    public  interface itemClick{
          void itemClick(View v, int position);
    }
}
