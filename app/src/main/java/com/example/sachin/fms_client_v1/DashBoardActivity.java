package com.example.sachin.fms_client_v1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class DashBoardActivity extends AppCompatActivity {

    private TextView text_0,text_1,text_2,text_3,text_4,text_5,text_6;
    private SharedPreferences sp;
    private FloatingActionButton fab,fab1,fab2;
    private Animation show_fab1,hide_fab1,show_fab2,hide_fab2;
    private PieChart pieChart;

    private boolean FAB_status= false;
    private ArrayList<Entry> entires;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("DashBoard");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        sp= this.getSharedPreferences(getString(R.string.preference),MODE_PRIVATE);
        fab=(FloatingActionButton) findViewById(R.id.fab);

        fab1=(FloatingActionButton) findViewById(R.id.fab_1);
        fab2=(FloatingActionButton) findViewById(R.id.fab_2);
        pieChart=(PieChart)findViewById(R.id.pie_chart);
        show_fab1= AnimationUtils.loadAnimation(getApplicationContext(), R.anim.show_fab_1);
        hide_fab1= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.hide_fab_1);
        show_fab2= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.show_fab_2);
        hide_fab2= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.hide_fab_2);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(!FAB_status){
                    expandFAB();
                    FAB_status=true;


                }
                else{
                    hideFAB();
                    FAB_status=false;
                }

            }
        });

        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sp = getApplication().getSharedPreferences(getString(R.string.preference), MODE_APPEND);
                SharedPreferences.Editor editor = sp.edit();
                editor.clear();
                editor.apply();

                Intent i = new Intent(getApplication(), LoginActivity.class);
                i.putExtra("finish", true); // if you are checking for this in your other Activities
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK |
                        Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();

            }
        });
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(getApplication(), LandingActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });



        text_0 = (TextView)findViewById(R.id.text0_0);
        text_1 = (TextView)findViewById(R.id.text1_1);
        text_2 = (TextView)findViewById(R.id.text2_2);
        text_3 = (TextView)findViewById(R.id.text3_3);
        text_4 = (TextView)findViewById(R.id.text4_4);
        text_5 = (TextView)findViewById(R.id.text5_5);
        text_6 = (TextView)findViewById(R.id.text6_6);


        String total=Integer.toString(sp.getInt(getString(R.string.total),1));
        String open=Integer.toString(sp.getInt(getString(R.string.open),1));
        String completed=Integer.toString(sp.getInt(getString(R.string.completed),1));
        String assigned=Integer.toString(sp.getInt(getString(R.string.assigned),1));
        String unassigned=Integer.toString(sp.getInt(getString(R.string.unassigned),1));

        String not_completed=Integer.toString(sp.getInt(getString(R.string.not_completed),1));
        text_0.setText(total);
        text_1.setText(open);
        text_2.setText(assigned);
        text_3.setText(unassigned);

        text_4.setText("1");

        text_5.setText(completed);
        text_6.setText(not_completed);
       // Fill_data data= new Fill_data();
       // data.execute();

        entires = new ArrayList<>();

        entires.add(new Entry(sp.getInt(getString(R.string.total),1),0));
        entires.add(new Entry(sp.getInt(getString(R.string.open),1),1));
        entires.add(new Entry(sp.getInt(getString(R.string.assigned),1),2));
        entires.add(new Entry(sp.getInt(getString(R.string.unassigned),1),3));
        entires.add(new Entry(sp.getInt(getString(R.string.completed),1),4));
        entires.add(new Entry(sp.getInt(getString(R.string.not_completed),1),5));


        PieDataSet pieDataSet= new PieDataSet(entires,"");
        pieDataSet.setValueTextSize(6);

        ArrayList<String> str=new ArrayList<>();
        str.add("Call Logged");
        str.add("Open");
        str.add("Assigned");
        str.add("Unassigned");
        str.add("Completed");
        str.add("not completed");

        final int[] COLORFUL_COLORS = {
                Color.rgb(193, 37, 82), Color.rgb(255, 102, 0), Color.rgb(245, 199, 0),
                Color.rgb(106, 150, 31), Color.rgb(179, 100, 53),Color.rgb(191, 134, 134)
        };

        pieChart.setTransparentCircleRadius(50f);
        pieChart.setHoleRadius(40f);
        PieData data = new PieData(str,pieDataSet);
        data.setValueTextSize(11);
        pieChart.setData(data);
        pieChart.setDescription("Description");
       // pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieDataSet.setColors(ColorTemplate.createColors(COLORFUL_COLORS));
        pieChart.animateY(5000);







    }

    public class Fill_data extends AsyncTask<String,String,String>{

        public void onPostExecute(String s){

        }
        public void onPreExecute(){

        }
        @Override
        protected String doInBackground(String... params) {
            return null;
        }
    }

    public void expandFAB(){
        FrameLayout.LayoutParams params=(FrameLayout.LayoutParams) fab1.getLayoutParams();
        params.rightMargin+=(int)(fab1.getWidth()*1.7);
        params.bottomMargin+=(int) (fab1.getHeight()*0.25);
        fab1.setLayoutParams(params);
        fab1.startAnimation(show_fab1);
        fab1.setClickable(true);


        FrameLayout.LayoutParams params1=(FrameLayout.LayoutParams) fab2.getLayoutParams();
        params1.rightMargin+=(int) (fab2.getWidth()*1.5);
        params1.bottomMargin+= (int) (fab2.getHeight()* 1.5);
        fab2.setLayoutParams(params1);
        fab2.setAnimation(show_fab2);
        fab2.setClickable(true);

    }

    public void hideFAB(){
        FrameLayout.LayoutParams params=(FrameLayout.LayoutParams) fab1.getLayoutParams();
        params.rightMargin -=(int) (fab1.getWidth()* 1.7);
        params.bottomMargin -=(int) (fab1.getHeight() * 0.25);
        fab1.setLayoutParams(params);
        fab1.setAnimation(hide_fab1);
        fab1.setClickable(false);

        FrameLayout.LayoutParams params1= (FrameLayout.LayoutParams) fab2.getLayoutParams();
        params1.rightMargin -=(int) (fab2.getWidth() * 1.5);
        params1.bottomMargin -=(int) (fab2.getHeight() * 1.5);
        fab2.setLayoutParams(params1);
        fab2.setAnimation(hide_fab2);
        fab2.setClickable(false);

    }
}
