package com.example.sachin.fms_client_v1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class StatusActivity extends AppCompatActivity implements StatusAdapter.itemClick {

    private Spinner searchView;
    private StatusAdapter adapter;
    private RecyclerView recyclerView;
    private ConnectionLanding connectionLanding;
    private List<String> priority;
    private List<String> priority_des;
    private List<String> buliding_name;
    private List<String> status;

    private List<String> location_des;
    private List<String> taskId;
    private List<String> des;
    private Bundle b,b1;
    private List<String> contract;
    private List<String> items;
    private SharedPreferences sp;
    private List<String> location;
    private List<String> unit;
    private List<String> unit_des;

    private List<String> building;
    private List<String> date_1;
    private List<String> date_2;
    private List<String> date_3;
    private List<String> th_user;
    private StatusAdapter.itemClick click;

    private List<StatusData> list;
    private String flag,contract_des;
    private FloatingActionButton fab,fab1,fab2;
    private Animation show_fab1,hide_fab1,show_fab2,hide_fab2;

    private boolean FAB_status= false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Request List");

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


        b= new Bundle();
        b=getIntent().getExtras();


        connectionLanding = new ConnectionLanding();

        recyclerView= (RecyclerView)findViewById(R.id.recyclerView);
        searchView = (Spinner)
                findViewById(R.id.searchView);
        String[] items= new String[]{"Display All","Emergency","Normal","Low Priority","Very Low Priority","sample priority"};
        ArrayAdapter<String> arrayadapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,items);

        arrayadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        searchView.setAdapter(arrayadapter);
        /*final String[] COUNTRIES = new String[]{
                "0000000104", "0000000105", "0000000110", "Germany", "Spain"
        };
        ArrayAdapter<String> arrayadapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, COUNTRIES);*/


        flag = b.getString("flag");
        if(flag.equals("P")){

            //Toast.makeText(getApplication(),Integer.toString(b.size()),Toast.LENGTH_LONG).show();

            list = new ArrayList<>(fill_list_pending());


        }
        else if(flag.equals("S")){

            list = new ArrayList<>(fill_list_status());
        }



        adapter = new StatusAdapter(this, list);
        recyclerView.setAdapter(adapter);
        adapter.SetOnClick(StatusActivity.this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));




        /*searchView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(),"Parents"+parent.getItemAtPosition(position).toString(),Toast.LENGTH_LONG).show();

                if(position == 0) {
                    list = fill_list();


                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplication()));
                    adapter = new StatusAdapter(getApplication(), list);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                }

                else if(position >0) {

                    final List<StatusData> filteredList= new ArrayList<>();

                    for (int i =0;i<list.size();i++){
                        if(list.get(i).priority.equals(parent.getItemAtPosition(position).toString())){
                            filteredList.add(list.get(i));
                        }

                        else {
                            Toast.makeText(getApplicationContext(),"No data found",Toast.LENGTH_LONG).show();

                        }
                    }
                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    adapter= new StatusAdapter(getApplicationContext(),filteredList);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();



                }




            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/



    }



    public List<StatusData> fill_list_status(){


        priority = new ArrayList<>();
        priority_des = new ArrayList<>();
        unit= new ArrayList<>();
        date_1= new ArrayList<>();
        date_2= new ArrayList<>();
        date_3 = new ArrayList<>();
        building= new ArrayList<>();
        List<String> build_code= new ArrayList<>();
        unit_des= new ArrayList<>();

        status= new ArrayList<>();
        location_des= new ArrayList<>();
        taskId = new ArrayList<>();
        des = new ArrayList<>();
        th_user= new ArrayList<>();
        contract= new ArrayList<>();
        sp = this.getSharedPreferences(getString(R.string.preference), MODE_PRIVATE);

        String s_des;
        location = new ArrayList<>();

        items= new ArrayList<>();



        //  int b= Integer.parseInt(sp.getString(getString(R.string.assigned_count), "0"));
        List<StatusData> data = new ArrayList<>();
        if (b.size()== 0) {
            Log.e("ERROR", "There is no new task");
        } else {
            for (int i = 0; i < b.size()-1; i++) {
                items.add(b.getString("taskId" + i));
                try {
                    Connection con = connectionLanding.landingCONN();
                    if (con == null) {
                        Toast.makeText(this, "Connection error!! Please check your Connection", Toast.LENGTH_LONG).show();

                    } else {

                        String q = "SELECT * FROM dbo.FMCALLH WHERE TH_COMPCD='001' AND TH_REPORTER='" + sp.getString(getString(R.string.customer_code), "") + "' AND ISNULL(TH_SUBMITTED,'')<>'' AND TH_FOLLOW <> 1 AND TH_HID='" + items.get(i) + "' AND ISNULL(TH_STATUS,'" + sp.getString(getString(R.string.status_close), "") + "') <>'" + sp.getString(getString(R.string.status_close), "") + "'";
                        Statement stm = con.createStatement();
                        ResultSet rs = stm.executeQuery(q);


                        while (rs.next()) {
                            taskId.add(rs.getString("TH_CALL_DOC"));
                            status.add(rs.getString("TH_STATUS"));
                            location.add(rs.getString("TH_LOC"));
                            unit.add(rs.getString("TH_UNIT"));
                            building.add(rs.getString("TH_BUILD"));

                            th_user.add(rs.getString("TH_USER"));
                            date_1.add(rs.getString("TH_REPORT_DT"));
                            date_2.add(rs.getString("TH_DUE_DT"));
                            date_3.add(rs.getString("TH_SCHED_DT"));

                            des.add(rs.getString("TH_REMARKS"));
                            contract.add(rs.getString("TH_CONTRACT"));
                            priority.add(rs.getString("TH_PRIORITY"));
                        }

                        String q2 = "SELECT * FROM dbo.FMPRIOR where PRHRS='" + priority.get(i) + "'";
                        Statement stm_priority = con.createStatement();
                        ResultSet rs_priority = stm_priority.executeQuery(q2);
                        while (rs_priority.next()) {
                            priority_des.add(rs_priority.getString("PRDESC"));

                        }

                        String q3 = "SELECT BLD_NO,BLD_NAME,LCCD,LCDESC FROM dbo.FMCONTBUIlDINGS INNER JOIN FMBUILDING ON  BLD_COMPCD =CBLD_COMPCD AND CBLD_BUILDING =BLD_NO AND BLD_INACTIVE_YN=0 INNER JOIN JBLOCN ON LCCOMPCD=CBLD_COMPCD AND LCCD=BLD_LOCATION AND LCINACTIVE=0 WHERE CBLD_COMPCD='001' AND CBLD_CONTRACT='" + contract.get(i) + "' AND LCCD='" + location.get(i) + "'";
                        Statement stm_location = con.createStatement();
                        ResultSet rs_location = stm_location.executeQuery(q3);
                        while (rs_location.next()) {
                            location_des.add(rs_location.getString("LCDESC"));
                            build_code.add(rs_location.getString("BLD_NO"));


                        }

                        String query = "SELECT UNT_NO,UNT_DESCRIPTION FROM dbo.FMUNIT WHERE UNT_COMPCD ='001' AND UNT_LOCATION='" + location.get(i) + "' AND UNT_BUILDING='" + build_code.get(i) + "'";
                        Statement stm_unit = con.createStatement();
                        ResultSet rs_unit = stm_unit.executeQuery(query);
                        while (rs_unit.next()) {
                            unit_des.add(rs_unit.getString("UNT_DESCRIPTION"));

                        }

                    }

                } catch (SQLException q) {

                    Log.e("ERROR", q.getMessage());
                } catch (Exception e) {
                    Log.e("ERROR", e.getMessage());
                }


                if(status.get(i).equals(sp.getString(getString(R.string.status_close),""))){

                    s_des=(sp.getString(getString(R.string.status_close_des),""));
                }
                else if(status.get(i).equals(sp.getString(getString(R.string.status_on_going),""))) {
                    s_des= sp.getString(getString(R.string.status_on_going_des),"");

                }

                else if(status.get(i).equals(sp.getString(getString(R.string.status_assigned),""))){

                    s_des= sp.getString(getString(R.string.status_assigned_des),"");
                }
                else if(status.get(i).equals(sp.getString(getString(R.string.status_unassigned),""))) {
                    s_des= sp.getString(getString(R.string.status_unassigned_des),"");

                }
                else if(status.get(i).equals(sp.getString(getString(R.string.status_enquiry),""))){

                    s_des= sp.getString(getString(R.string.status_enquiry_des),"");
                }
                else if(status.get(i).equals(sp.getString(getString(R.string.status_interim),""))) {
                    s_des= sp.getString(getString(R.string.status_interim_des),"");

                }

                else if(status.get(i).equals(sp.getString(getString(R.string.status_work_completed),""))){

                    s_des= sp.getString(getString(R.string.status_work_completed_des),"");
                }
                else if(status.get(i).equals(sp.getString(getString(R.string.status_work_in_progress),""))) {
                    s_des= sp.getString(getString(R.string.status_work_in_progress_des),"");

                }
                else {
                    s_des="nothing";
                }

                data.add(new StatusData(taskId.get(i),date_1.get(i),location_des.get(i),des.get(i),priority_des.get(i),s_des,unit_des.get(i)));


            }

        }

        return data;

    }

    public List<StatusData> fill_list_pending(){


        priority = new ArrayList<>();
        priority_des = new ArrayList<>();
        unit= new ArrayList<>();
        date_1= new ArrayList<>();
        date_2= new ArrayList<>();
        date_3 = new ArrayList<>();
        building= new ArrayList<>();

        status= new ArrayList<>();
        location_des= new ArrayList<>();
        taskId = new ArrayList<>();
        des = new ArrayList<>();
        th_user= new ArrayList<>();
        unit_des= new ArrayList<>();

        contract= new ArrayList<>();
        sp = this.getSharedPreferences(getString(R.string.preference), MODE_PRIVATE);

        String s_des;

        List<String> build_code= new ArrayList<>();
        items = new ArrayList<>();
        location = new ArrayList<>();



        //  int b= Integer.parseInt(sp.getString(getString(R.string.assigned_count), "0"));
        List<StatusData> data = new ArrayList<>();
      //  Toast.makeText(getApplication(),Integer.toString(b.size()),Toast.LENGTH_LONG).show();
        if (b.size()== 0) {
            Log.e("ERROR", "There is no new task");
        } else {

            for (int i = 0; i < b.size()-1; i++) {
                items.add(b.getString("pendingTask" + i));
                try {
                    Connection con = connectionLanding.landingCONN();
                    if (con == null) {
                        Toast.makeText(this, "Connection error!! Please check your Connection", Toast.LENGTH_LONG).show();

                    } else {



                        String q="SELECT * FROM dbo.FMCALLH WHERE TH_COMPCD='001' AND TH_REPORTER='"+sp.getString(getString(R.string.customer_code),"")+"' AND TH_HID='"+items.get(i)+"' AND TH_FOLLOW <> 1 AND ISNULL(TH_SUBMITTED,'')=''";
                        Statement stm = con.createStatement();
                        ResultSet rs = stm.executeQuery(q);



                        while (rs.next()) {
                            taskId.add(rs.getString("TH_CALL_DOC"));
                            status.add(rs.getString("TH_STATUS"));
                            location.add(rs.getString("TH_LOC"));
                            unit.add(rs.getString("TH_UNIT"));
                            building.add(rs.getString("TH_BUILD"));

                            th_user.add(rs.getString("TH_USER"));
                            date_1.add(rs.getString("TH_REPORT_DT"));
                            date_2.add(rs.getString("TH_DUE_DT"));
                            date_3.add(rs.getString("TH_SCHED_DT"));

                            des.add(rs.getString("TH_REMARKS"));
                            contract.add(rs.getString("TH_CONTRACT"));
                            priority.add(rs.getString("TH_PRIORITY"));

                        }

                        String q2="SELECT * FROM dbo.FMPRIOR where PRHRS='"+priority.get(i)+"'";
                        Statement stm_priority=con.createStatement();
                        ResultSet rs_priority= stm_priority.executeQuery(q2);
                        while(rs_priority.next()){
                            priority_des.add(rs_priority.getString("PRDESC"));

                        }

                        String q3="SELECT BLD_NO,BLD_NAME,LCCD,LCDESC FROM dbo.FMCONTBUIlDINGS INNER JOIN FMBUILDING ON  BLD_COMPCD =CBLD_COMPCD AND CBLD_BUILDING =BLD_NO AND BLD_INACTIVE_YN=0 INNER JOIN JBLOCN ON LCCOMPCD=CBLD_COMPCD AND LCCD=BLD_LOCATION AND LCINACTIVE=0 WHERE CBLD_COMPCD='001' AND CBLD_CONTRACT='"+contract.get(i)+"' AND LCCD='"+location.get(i)+"'";
                        Statement stm_location=con.createStatement();
                        ResultSet rs_location= stm_location.executeQuery(q3);
                        while (rs_location.next()){
                            location_des.add(rs_location.getString("LCDESC"));
                            build_code.add(rs_location.getString("BLD_NO"));


                        }

                        String query="SELECT UNT_NO,UNT_DESCRIPTION FROM dbo.FMUNIT WHERE UNT_COMPCD ='001' AND UNT_LOCATION='"+location.get(i)+"' AND UNT_BUILDING='"+build_code.get(i)+"'";
                        Statement stm_unit= con.createStatement();
                        ResultSet rs_unit= stm_unit.executeQuery(query);
                        while (rs_unit.next()){
                            unit_des.add(rs_unit.getString("UNT_DESCRIPTION"));

                        }

                    }


                } catch (SQLException q) {

                    Log.e("ERROR", q.getMessage());
                } catch (Exception e) {
                    Log.e("ERROR", e.getMessage());
                }


              //  Toast.makeText(getApplication(),sp.getString(getString(R.string.status_work_completed),""),Toast.LENGTH_LONG).show();
                if(status.get(i).equals(sp.getString(getString(R.string.status_close),""))){

                    s_des= sp.getString(getString(R.string.status_close_des),"");
                }
                else if(status.get(i).equals(sp.getString(getString(R.string.status_on_going),""))) {
                    s_des= sp.getString(getString(R.string.status_on_going_des),"");

                }

                else if(status.get(i).equals(sp.getString(getString(R.string.status_assigned),""))){

                    s_des= sp.getString(getString(R.string.status_assigned_des),"");
                }
                else if(status.get(i).equals(sp.getString(getString(R.string.status_unassigned),""))) {
                    s_des= sp.getString(getString(R.string.status_unassigned_des),"");

                }
                else if(status.get(i).equals(sp.getString(getString(R.string.status_enquiry),""))){

                    s_des= sp.getString(getString(R.string.status_enquiry_des),"");
                }
                else if(status.get(i).equals(sp.getString(getString(R.string.status_interim),""))) {
                    s_des= sp.getString(getString(R.string.status_interim_des),"");

                }

                else if(status.get(i).equals(sp.getString(getString(R.string.status_work_completed),""))){

                    s_des= sp.getString(getString(R.string.status_work_completed_des),"");
                }
                else if(status.get(i).equals(sp.getString(getString(R.string.status_work_in_progress),""))) {
                    s_des= sp.getString(getString(R.string.status_work_in_progress_des),"");

                }
                else{
                    s_des="nothing";
                }


                data.add(new StatusData(taskId.get(i),date_1.get(i),location_des.get(i),des.get(i),priority_des.get(i),s_des,unit_des.get(i)));


            }

        }

        return data;

    }



    /*@Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        Toast.makeText(getApplicationContext(),"Parents"+parent.getItemAtPosition(position).toString(),Toast.LENGTH_LONG).show();

        switch (position){

            case 0:

                final List<StatusData> filteredList= new ArrayList<>();

                for (int i =0;i<list.size();i++){
                    if(list.get(i).status.equals(parent.getItemAtPosition(position).toString())){
                        filteredList.add(list.get(i));
                    }
                }
                Toast.makeText(getApplicationContext(),"Parents"+parent.getItemAtPosition(position).toString(),Toast.LENGTH_LONG).show();
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                adapter= new StatusAdapter(getApplicationContext(),filteredList);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();




                break;

            case 1:

                final List<StatusData> filteredList1= new ArrayList<>();
                Toast.makeText(getApplicationContext(),"Parents"+parent.getItemAtPosition(position).toString(),Toast.LENGTH_LONG).show();

                for (int i =0;i<list.size();i++){
                    if(list.get(i).status.equals(parent.getItemAtPosition(position).toString())){
                        filteredList1.add(list.get(i));
                    }
                }
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                adapter= new StatusAdapter(getApplicationContext(),filteredList1);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                break;
            case 2:
                final List<StatusData> filteredList2= new ArrayList<>();
                Toast.makeText(getApplicationContext(),"Parents"+parent.getItemAtPosition(position).toString(),Toast.LENGTH_LONG).show();

                for (int i =0;i<list.size();i++){
                    if(list.get(i).status.equals(parent.getItemAtPosition(position).toString())){
                        filteredList2.add(list.get(i));
                    }
                }
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                adapter= new StatusAdapter(getApplicationContext(),filteredList2);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }*/

    @Override
    public void itemClick(View v, int position) {

        boolean isSuccess=false;

        if(flag.equals("P")){



            try{
                Connection con= connectionLanding.landingCONN();

                if(con== null){
                    Toast.makeText(getApplication(),"Connection Error!! Please Check your connection",Toast.LENGTH_LONG).show();

                }

                else{
                    String query="SELECT CUSTD_CNT_NO,DESCRIPTION=(CUSTD_CNT_NO + '-' + CNT_DESCRIPTION) FROM HRZNET..GLUSER_CUSTD INNER JOIN  dbo.FMCONTRACT ON custd_cnt_no=CNT_NO WHERE CUSTD_CD ='"+sp.getString(getString(R.string.customer_code),"")+"'and CUSTD_CNT_NO='"+contract.get(position)+"'";

                    Statement stm =con.createStatement();
                    ResultSet rs= stm.executeQuery(query);
                    if(rs.next()){
                        contract_des= rs.getString("DESCRIPTION");
                        isSuccess=true;

                    }
                    else {
                        Toast.makeText(getApplication(),"There are no data",Toast.LENGTH_LONG).show();
                    }

                }

            }
            catch (Exception e){
                Toast.makeText(getApplication(),e.getMessage(),Toast.LENGTH_LONG).show();

            }

            if(isSuccess){

                Intent i = new Intent(getApplication(),FollowUpActivity.class);

                i.putExtra("contract_des",contract_des);
                i.putExtra("date_1",date_1.get(position));
                i.putExtra("date_2",date_2.get(position));
                i.putExtra("date_3",date_3.get(position));
                i.putExtra("unit",unit.get(position));
                i.putExtra("building",building.get(position));
                i.putExtra("location",location.get(position));
                i.putExtra("phrs",priority.get(position));
                i.putExtra("contract",contract.get(position));
                i.putExtra("th_user",th_user.get(position));
                i.putExtra("previous_call_doc",taskId.get(position));
                i.putExtra("status",status.get(position));


                startActivity(i);



            }

        }


        else {

        }

    }

    @Override
    public void onStart(){

        super.onStart();

        searchView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0) {

                    if(flag.equals("P")){
                        list = fill_list_pending();
                    }
                    else if(flag.equals("S")){

                        list = fill_list_status();
                    }


                    adapter = new StatusAdapter(getApplication(), list);
                    recyclerView.setAdapter(adapter);
                    adapter.SetOnClick(StatusActivity.this);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplication()));

                    adapter.notifyDataSetChanged();

                }

                else if(position >0) {

                    final List<StatusData> filteredList= new ArrayList<>();

                    for (int i =0;i<list.size();i++){
                        if(list.get(i).priority.equals(parent.getItemAtPosition(position).toString())){
                            filteredList.add(list.get(i));

                        }
                        else {

                        }
                    }

                    if(!filteredList.isEmpty()){
                        adapter= new StatusAdapter(getApplicationContext(),filteredList);
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        adapter.SetOnClick(StatusActivity.this);

                        adapter.notifyDataSetChanged();

                    }
                    else {
                        adapter= new StatusAdapter(getApplicationContext(),filteredList);
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        adapter.SetOnClick(StatusActivity.this);
                        adapter.notifyDataSetChanged();
                        Toast.makeText(getApplication(),"No Data Found",Toast.LENGTH_LONG).show();
                    }



                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

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
