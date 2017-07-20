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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

/**
 * Created by sachin on 3/27/2016.
 */
public class RVAdapter extends RecyclerView.Adapter<RVAdapter.View_Holder>{

    onItemClick click;
    List<Data> list= Collections.emptyList();
    Context context;
    public RVAdapter(Context context,List<Data> list){
        this.list=list;
        this.context=context;

    }
    @Override
    public View_Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview,parent,false);
        View_Holder holder= new View_Holder(view);




        return holder;
    }

    @Override
    public void onBindViewHolder(View_Holder holder, int position) {


        holder.text.setText(list.get(position).title);

        animate(holder);


    }

    public void setOnClick(onItemClick click){
        this.click=click;

    }

    public void animate(RecyclerView.ViewHolder viewHolder){
        final Animation anim= AnimationUtils.loadAnimation(context,R.anim.bounce_interpolator);
        viewHolder.itemView.setAnimation(anim);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }



    public class View_Holder extends RecyclerView.ViewHolder implements View.OnClickListener{


        CardView cardView;
        TextView text;
        ImageView image;


        public View_Holder(View itemView) {
            super(itemView);

            cardView=(CardView)itemView.findViewById(R.id.cardView);
            text=(TextView)itemView.findViewById(R.id.text);

            itemView.setOnClickListener(this);




        }

        @Override
        public void onClick(View v) {

            if(click!=null){
                click.onItemClick(v,getPosition());

            }


        }
    }

    public interface onItemClick{
        void onItemClick(View v, int position);
    }
}
