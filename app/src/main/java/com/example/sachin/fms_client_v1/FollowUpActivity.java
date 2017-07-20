package com.example.sachin.fms_client_v1;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

public class FollowUpActivity extends AppCompatActivity {

    private TextView text_0,text_1,text_2,text_3,text_4,text_5,text_7,text_8,text_9;
    private EditText text_6;
    private Button follow_up,cancel;
    private FloatingActionButton fab,fab1,fab2;
    private Animation show_fab1,show_fab2,hide_fab1,hide_fab2;
    private boolean FAB_STATUS= false;
    private SharedPreferences sp;
    private Bundle b;
    private String previous_call_doc, status, contract_des,location,building,unit,date_1,date_2,date_3,p_hrs,th_user;
    private HashMap<String,String> data= new HashMap<>();
    private ConnectionLanding connectionLanding;
    private ProgressBar pbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow_up);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Follow Up");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab1=(FloatingActionButton) findViewById(R.id.fab_1);
        fab2=(FloatingActionButton) findViewById(R.id.fab_2);
        show_fab1= AnimationUtils.loadAnimation(this,R.anim.show_fab_1);
        hide_fab1= AnimationUtils.loadAnimation(this,R.anim.hide_fab_1);
        show_fab2= AnimationUtils.loadAnimation(this,R.anim.show_fab_2);
        hide_fab2= AnimationUtils.loadAnimation(this,R.anim.hide_fab_2);

        pbar= (ProgressBar)findViewById(R.id.pbar) ;

        follow_up=(Button)findViewById(R.id.follow_up);
        cancel=(Button)findViewById(R.id.cancel);
        sp= this.getSharedPreferences(getString(R.string.preference),MODE_PRIVATE);
        connectionLanding= new ConnectionLanding();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!FAB_STATUS){
                    expandFAB();
                    FAB_STATUS=true;
                }
                else {
                    hideFAB();
                    FAB_STATUS=false;
                }
            }
        });

        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }

        });

        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
        b = getIntent().getExtras();

        contract_des= b.getString("contract_des");
        date_1= b.getString("date_1");
        date_2= b.getString("date_2");
        date_3= b.getString("date_3");
        location= b.getString("location");
        building= b.getString("building");
        unit= b.getString("unit");
        p_hrs= b.getString("phrs");
        previous_call_doc= b.getString("previous_call_doc");

        th_user= b.getString("th_user");

        status= b.getString("status");

        text_0 = (TextView)findViewById(R.id.text0_0);
        text_1 = (TextView)findViewById(R.id.text1_1);
        text_2 = (TextView)findViewById(R.id.text2_2);
        text_3 = (TextView)findViewById(R.id.text3_3);
        text_4 = (TextView)findViewById(R.id.text4_4);
        text_5 = (TextView)findViewById(R.id.text5_5);
        text_6 = (EditText) findViewById(R.id.text6_6);
        text_7 = (TextView)findViewById(R.id.text7_7);
        text_8 = (TextView)findViewById(R.id.text8_8);
        text_9 = (TextView)findViewById(R.id.text9_9);

        fill_data fill= new fill_data();
        fill.execute();


        follow_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FollowUp up= new FollowUp();
                up.execute();

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });




    }




    public class fill_data extends AsyncTask<String,String,String> {


        String z="";
        boolean isSuccess=false;


        public void onPreExecute(){

        }

        public void onPostExecute(String s){


            String str= data.get("report_date");
            String[] r= str.split(" ");
            text_1.setText(r[1]);
            text_0.setText(r[0]);
            text_2.setText(data.get("location_des"));
            text_3.setText(data.get("build_des"));
            text_4.setText(data.get("unit_des"));
            text_5.setText(contract_des);
            text_7.setText(data.get("priority_des"));

            String str2=data.get("sch_date");
            String[] sch=str2.split(" ");
            text_8.setText(sch[0]);
            text_9.setText(sch[1]);




        }

        @Override
        protected String doInBackground(String... params) {

            try{
                Connection con= connectionLanding.landingCONN();

                if(con== null){
                    z="Connection Error!! Please check your connection";

                }
                else{
                    String q="SELECT * FROM dbo.FMCALLH WHERE TH_REPORTER='"+sp.getString(getString(R.string.customer_code),"")+"' AND ISNULL(TH_STATUS, '"+sp.getString(getString(R.string.status_close),"")+"')<> '"+sp.getString(getString(R.string.status_close),"")+"'";
                    Statement stm = con.createStatement();
                    ResultSet rs = stm.executeQuery(q);



                    while (rs.next()) {

                        data.put("report_date",rs.getString("TH_REPORT_DT"));
                        data.put("contract_code",rs.getString("TH_CONTRACT"));
                        data.put("sch_date",rs.getString("TH_SCHED_DT"));
                        data.put("loc_code",rs.getString("TH_LOC"));
                        data.put("build_code",rs.getString("TH_BUILD"));
                        data.put("unit_code",rs.getString("TH_UNIT"));
                        data.put("priority_code",rs.getString("TH_PRIORITY"));
                        data.put("status_code",rs.getString("TH_STATUS"));

                        isSuccess= true;


                    }

                    String q2="SELECT * FROM dbo.FMPRIOR where PRHRS='"+data.get("priority_code")+"'";
                    Statement stm_priority=con.createStatement();
                    ResultSet rs_priority= stm_priority.executeQuery(q2);
                    while(rs_priority.next()){
                        data.put("priority_des",rs_priority.getString("PRDESC"));
                        isSuccess= true;

                    }

                    String q3="SELECT * FROM dbo.FMCONTBUIlDINGS INNER JOIN FMBUILDING ON  BLD_COMPCD =CBLD_COMPCD AND CBLD_BUILDING =BLD_NO AND BLD_INACTIVE_YN=0 INNER JOIN JBLOCN ON LCCOMPCD=CBLD_COMPCD AND LCCD=BLD_LOCATION AND LCINACTIVE=0 WHERE CBLD_COMPCD='001' AND CBLD_CONTRACT='"+data.get("contract_code")+"' AND LCCD='"+data.get("loc_code")+"' AND BLD_NO='"+data.get("build_code")+"'";
                    Statement stm_location=con.createStatement();
                    ResultSet rs_location= stm_location.executeQuery(q3);
                    while (rs_location.next()){
                        data.put("location_des",rs_location.getString("LCDESC"));
                        data.put("build_des",rs_location.getString("BLD_NAME"));
                    }

                    String q4="SELECT UNT_NO,UNT_DESCRIPTION FROM dbo.FMUNIT WHERE UNT_COMPCD ='001' AND UNT_LOCATION='"+data.get("loc_code")+"' AND UNT_BUILDING='"+data.get("build_code")+"'";
                    Statement stm_unit= con.createStatement();
                    ResultSet rs_unit= stm_unit.executeQuery(q4);
                    while(rs_unit.next()){
                        data.put("unit_des",rs_unit.getString("UNT_DESCRIPTION"));

                        isSuccess= true;
                    }




                }
            }

            catch(Exception e){

                isSuccess= false;
                z=e.getLocalizedMessage();

            }
            return z;
        }
    }



    public class FollowUp extends AsyncTask<String,String,String>{

        String z="";
        String doc_number;
        String doc_type;
        String cnt_code;
        String remarks= text_6.getText().toString();

        boolean isSuccess_1;

        public void onPreExecute(){

            pbar.setVisibility(View.VISIBLE);
        }

        public void onPostExecute(String r){

            pbar.setVisibility(View.GONE);

            AlertDialog.Builder builder= new AlertDialog.Builder(FollowUpActivity.this);
            builder.setTitle("Follow Up").setMessage("Your follow up request has been sent.\nPlease Note down your request Number\n"+doc_number)
                    .setCancelable(false)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    Intent i = new Intent(getApplication(),LandingActivity.class);
                    startActivity(i);
                }
            }).create().show();

        }
        @Override
        protected String doInBackground(String... params) {

            try{
                Connection con= connectionLanding.landingCONN();
                if(con==null){
                    z="Connection Error!! Please check your connection ";

                }

                else{

                    String query_doc_number="exec [GEN_FN_GETANDROIDDOCNO1] '001'" ;

                    Statement stm1=con.createStatement();
                    ResultSet rs1= stm1.executeQuery(query_doc_number);
                    if(rs1.next()){
                        doc_number=rs1.getString(1);
                        isSuccess_1=true;
                    }
                    else {
                        z="Connection error! Please check your connection";
                        isSuccess_1=false;

                    }

                    doc_type= doc_number.substring(0,2);
                    String[] s= contract_des.split("-");
                    cnt_code= s[0];


                    String query="INSERT INTO FMCALLH (TH_COMPCD ,TH_DOC_TYPE ,TH_CALL_DOC ,TH_REPORT_DT ,TH_DUE_DT ,TH_SCHED_DT ,TH_TELNO ,TH_REPORTER ,TH_CALL_SOURCE ,TH_LOC ,TH_BUILD ,TH_UNIT ,TH_CONTRACT ,TH_PRIORITY ,TH_REG_MOB ,TH_REG_EMAIL ,TH_REMARKS ,TH_FOLLOW, TH_STATUS, TH_USER,TH_PREV_CALL) values('001','"+doc_type+"','"+doc_number+"','"+date_1+"','"+date_2+"','"+date_3+"','"+sp.getString(getString(R.string.customer_phone),"")+"','"+sp.getString(getString(R.string.customer_name),"")+"','MobileApp','"+location+"','"+building+"','"+unit+"','"+cnt_code+"','"+p_hrs+"','"+sp.getString(getString(R.string.customer_mobile),"")+"','"+sp.getString(getString(R.string.customer_email),"")+"','"+remarks+"',1,'"+status+"','"+th_user+"','"+previous_call_doc+"')";
                    Statement stm=con.createStatement();
                    stm.executeUpdate(query);

                }
            }

            catch (SQLException e) {
                e.printStackTrace();
                z=e.getMessage();
            }
            return z;
        }
    }




    public void expandFAB(){

        FrameLayout.LayoutParams parems= (FrameLayout.LayoutParams)fab1.getLayoutParams();
        parems.rightMargin+=(int)(fab1.getWidth()*1.7);
        parems.leftMargin +=(int)(fab1.getHeight()*0.25);

        fab1.setLayoutParams(parems);
        fab1.startAnimation(show_fab1);
        fab1.setClickable(true);

        FrameLayout.LayoutParams params1=(FrameLayout.LayoutParams)fab1.getLayoutParams();
        params1.rightMargin+=(int)(fab2.getWidth()*1.5);
        params1.leftMargin+=(int) (fab2.getHeight()*1.5);
        fab2.setLayoutParams(params1);
        fab2.startAnimation(show_fab2);
        fab2.setClickable(true);




    }

    public void hideFAB(){
        FrameLayout.LayoutParams params= (FrameLayout.LayoutParams)fab1.getLayoutParams();
        params.rightMargin-= (int)(fab1.getWidth()*1.7 );
        params.leftMargin-=(int) (fab1.getHeight()* 0.25);
        fab1.setLayoutParams(params);
        fab1.startAnimation(hide_fab1);
        fab1.setClickable( true);


        FrameLayout.LayoutParams params1= (FrameLayout.LayoutParams)fab2.getLayoutParams();
        params1.rightMargin-=(int)(fab2.getWidth()*1.5);
        params1.leftMargin-=(int) (fab2.getHeight()*1.5);
        fab2.setLayoutParams(params);
        fab2.startAnimation(hide_fab2);
        fab2.setClickable(true);


    }

}
