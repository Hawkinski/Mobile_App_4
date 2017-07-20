package com.example.sachin.fms_client_v1;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class LandingActivity extends AppCompatActivity implements RVAdapter.onItemClick {

    RecyclerView recyclerView;
    RVAdapter adapter;
    private FloatingActionButton fab1;
    private ConnectionLanding connectionLanding;
    private HashMap<String,String> task= new HashMap<>();

    private SharedPreferences sp;
    private Animation hide_fab_1,show_fab_1;
    private boolean FB_status=false;
    private FloatingActionButton fab;
    private ProgressBar pbar;
    private boolean isSuccess=false;
    private TextView username,date,notification,notification1;
    private List<String> id_list;
    private List<String> th_status;
    private List<String> status_des;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //nview=(NavigationView)findViewById(R.id.navigation_view);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab1=(FloatingActionButton) findViewById(R.id.fab_1);
        fab.hide();
        pbar= (ProgressBar) findViewById(R.id.progress);
        pbar.setVisibility(View.GONE);
        sp= this.getSharedPreferences(getString(R.string.preference),MODE_PRIVATE);

        notification=(TextView)findViewById(R.id.notification);
        notification1=(TextView)findViewById(R.id.notification1);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                fab.show();
                fab.setAnimation(AnimationUtils.loadAnimation(LandingActivity.this, R.anim.show_from_bottom));

            }
        }, 300);
        show_fab_1 = AnimationUtils.loadAnimation(getApplication(), R.anim.fab1_show);
        hide_fab_1= AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab1_hide);

        connectionLanding= new ConnectionLanding();
        username=(TextView)findViewById(R.id.usernameText);
        date= (TextView)findViewById(R.id.date);

        final Calendar c = Calendar.getInstance();
        int yy=c.get(Calendar.YEAR);
        String mm= c.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
        int dd= c.get(Calendar.DAY_OF_MONTH);

        date.setText(new StringBuilder().append(mm).append(" ").append(" ").append(dd).append(", ").append(yy));
        username.setText("Welcome "+sp.getString(getString(R.string.customer_name),""));







       /*nview.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {

                if (item.isChecked()) item.setChecked(false);
                else item.setChecked(true);

                drawerLayout.closeDrawers();

                switch (item.getItemId()) {

                    case R.id.home_menu:

                        Toast.makeText(getApplicationContext(), "Home Selected", Toast.LENGTH_LONG).show();
                        ContentFragment fragment = new ContentFragment();
                        FragmentTransaction f = getFragmentManager().beginTransaction();
                        f.replace(R.id.frame, fragment);
                        f.commit();
                        return true;
                    case R.id.setting_menu:
                        Toast.makeText(getApplicationContext(), "setting Selected", Toast.LENGTH_LONG).show();

                        return true;


                    case R.id.logout_menu:
                        Toast.makeText(getApplicationContext(), "logout Selected", Toast.LENGTH_LONG).show();

                        return true;

                    default:
                        return true;


                }


            }
        });*/

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FB_status == false) {
                    expandFAB();
                    FB_status = true;
                } else {
                    hideFAB();
                    FB_status = false;

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



        // drawerLayout=(DrawerLayout)findViewById(R.id.drawer_layout);
        //ActionBarDrawerToggle toggle= new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.close_drawer,R.string.open_drawer){


            /*@Override
       public void onDrawerClosed(View closeView){

                super.onDrawerClosed(closeView);
            }
            @Override
        public void onDrawerOpened(View openView){
                super.onDrawerOpened(openView);
            }
        };
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();*/


        List<Data> data=fill_data();


        recyclerView=(RecyclerView)findViewById(R.id.recyclerView);
        adapter= new RVAdapter(this,data);
        recyclerView.setAdapter(adapter);
        adapter.setOnClick(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));




        Connection conn=connectionLanding.landingCONN();

        try {


            if(conn== null){
                Toast.makeText(LandingActivity.this,"Connection Error please check connection",Toast.LENGTH_SHORT).show();
            }
            else {



                String q="SELECT COUNT(*) FROM dbo.FMCALLH WHERE TH_COMPCD='001' AND TH_REPORTER='CUSTOMER1'  AND TH_FOLLOW <> 1 AND ISNULL(TH_SUBMITTED,'')=''";
                String q3="SELECT COUNT(*) FROM dbo.FMCALLH WHERE TH_COMPCD='001' AND TH_REPORTER='CUSTOMER1'  AND TH_FOLLOW <> 1 AND ISNULL(TH_SUBMITTED,'')<>'' AND ISNULL(TH_STATUS,'S1') <>'S1'";

                Statement stm3= conn.createStatement();

                ResultSet rs3= stm3.executeQuery(q);
                if(rs3.next()){

                    task.put("count_1",rs3.getString(1));
                    isSuccess=true;
                }
                else {
                    isSuccess=false;
                }

                ResultSet rs4= stm3.executeQuery(q3);
                if (rs4.next()){
                    task.put("count_2",rs4.getString(1));
                    isSuccess=true;
                }
                else {
                    isSuccess=false;
                }

                String q4="SELECT COUNT(*) FROM dbo.FMTASKH WHERE TH_COMPCD='001' AND TH_STATUS ='"+sp.getString(getString(R.string.status_close),"")+"'";
                Statement stm4=conn.createStatement();
                ResultSet rs5= stm4.executeQuery(q4);
                if (rs5.next()){
                    task.put("completed",rs5.getString(1));
                    isSuccess=true;
                }
                else {
                    isSuccess=false;
                }
                String q5="SELECT COUNT(*) FROM dbo.FMTASKH WHERE TH_COMPCD='001' AND TH_STATUS <> '"+sp.getString(getString(R.string.status_close),"")+"'";
                Statement stm5=conn.createStatement();
                ResultSet rs6= stm5.executeQuery(q5);
                if (rs6.next()){
                    task.put("not_completed",rs6.getString(1));
                    isSuccess=true;
                }
                else {
                    isSuccess=false;
                }

                String assigned="SELECT COUNT(*) FROM dbo.FMTASKH WHERE TH_COMPCD='001' AND TH_STATUS in ('"+sp.getString(getString(R.string.status_assigned),"")+"','"+sp.getString(getString(R.string.status_close),"")+"')";
                Statement stm_assigned=conn.createStatement();
                ResultSet rs_assigned= stm_assigned.executeQuery(assigned);
                if (rs_assigned.next()){
                    task.put("assigned",rs_assigned.getString(1));
                    isSuccess=true;
                }
                else {
                    isSuccess=false;
                }

                String not_assigned="SELECT COUNT(*) FROM dbo.FMTASKH WHERE TH_COMPCD='001' AND TH_STATUS not in ('"+sp.getString(getString(R.string.status_assigned),"")+"','"+sp.getString(getString(R.string.status_close),"")+"')";
                Statement stm_not_assigned=conn.createStatement();
                ResultSet rs_not_assigned= stm_not_assigned.executeQuery(not_assigned);
                if (rs_not_assigned.next()){
                    task.put("unassigned",rs_not_assigned.getString(1));
                    isSuccess=true;
                }
                else {
                    isSuccess=false;
                }


            }

        }
        catch (SQLException e){
            Toast.makeText(LandingActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
            isSuccess=false;
        }
        catch (Exception e){
            Toast.makeText(LandingActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
            isSuccess=false;
        }

        if(isSuccess){
            //SharedPreferences.Editor editor=sp.edit();
            // editor.remove(getString(R.string.assigned_count));
            // editor.remove(getString(R.string.status_count));
            // editor.putString(getString(R.string.assigned_count), data.get("count_1"));
            // editor.putString(getString(R.string.status_count),data.get("count_2"));
            // editor.commit();

            SharedPreferences.Editor edit=sp.edit();
            edit.putInt(getString(R.string.total),(Integer.parseInt(task.get("count_1"))+Integer.parseInt(task.get("count_2"))));
            edit.putInt(getString(R.string.open),(Integer.parseInt(task.get("count_1"))));
            edit.putInt(getString(R.string.completed),(Integer.parseInt(task.get("completed"))));
            edit.putInt(getString(R.string.not_completed),(Integer.parseInt(task.get("not_completed"))));
            edit.putInt(getString(R.string.assigned),(Integer.parseInt(task.get("assigned"))));
            edit.putInt(getString(R.string.unassigned),(Integer.parseInt(task.get("unassigned"))));


            edit.apply();




            notification.setText(task.get("count_1") + "  Pending Request");
            notification1.setText(task.get("count_2")+"  Status request");

        }



    }

    public List<Data> fill_data(){
        List<Data> list= new ArrayList<>();


        list.add(new Data("Book Complaint/Service Request "));
        list.add(new Data("Pending Complaint"));
        list.add(new Data("Status of Request"));
        list.add(new Data("DashBoard"));





        return list;
    }


    @Override
    public void onItemClick(View v, int position) {


        switch (position)
        {
            case 0:


                Intent intent= new Intent(this,BookActivity.class);
                startActivity(intent);
                break;

            case 1:


                PendingTaskList pending= new PendingTaskList();
                pending.execute();





                break;

            case 2:



                TaskAssigned task= new TaskAssigned();
                task.execute();
                break;

            case 3:

                //Toast.makeText(this,sp.getInt(getString(R.string.total),1),Toast.LENGTH_LONG).show();
                Intent i= new Intent(getApplication(),DashBoardActivity.class);
                startActivity(i);

                break;

        }

        /*PendingIntent pendingIntent=PendingIntent.getActivity(this,0,intent,0);



        String title= "Notification ";
        String body="this is a notification demo";
        String subject ="you got a new notification";
        Notification notification=new Notification.Builder(LandingActivity.this)
                .setContentTitle(title)
                .setContentInfo(subject)
                .setContentIntent(pendingIntent)
                .setContentText(body)
                .setSmallIcon(R.drawable.logo)
                .setWhen(System.currentTimeMillis())
                .build();
        NotificationManager manager= (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        notification.flags|=Notification.FLAG_AUTO_CANCEL;
        manager.notify(0,notification);

        // Toast.makeText(this,list.get(position).toString(),Toast.LENGTH_LONG).show();
*/
    }

    private class TaskAssigned extends AsyncTask<String,String,String> {

        String z="";
        boolean isSuccess=false;


        protected void onPreExecute(){
            pbar.setVisibility(View.VISIBLE);
        }
        protected void onPostExecute(String r){

            pbar.setVisibility(View.GONE);

            if(isSuccess){

                Intent i= new Intent(LandingActivity.this,StatusActivity.class);
                //Toast.makeText(getApplication(),Integer.toString(id_list.size()), Toast.LENGTH_LONG).show();

                for(int j=0;j<id_list.size();j++){
                    i.putExtra("taskId"+j,id_list.get(j).toString());
                }
                i.putExtra("flag","S");
                startActivity(i);





            }
            else {

                Toast.makeText(getApplication(),"SELECT * FROM dbo.FMCALLH WHERE TH_REPORTER='"+sp.getString(getString(R.string.customer_code),"")+"' AND ISNULL(TH_STATUS,'"+sp.getString(getString(R.string.status_close),"")+"')  not in (' ','"+sp.getString(getString(R.string.status_close),"")+"')",Toast.LENGTH_LONG).show();
                Toast.makeText(LandingActivity.this, "ERRORRRRRRR!!!", Toast.LENGTH_SHORT).show();

            }



        }


        @Override
        protected String doInBackground(String... params) {

            try{
                Connection con= connectionLanding.landingCONN();
                if(con==null){
                    z="Connection Error Please check your connection!!";

                }
                else{
                    String query="SELECT * FROM dbo.FMCALLH WHERE TH_COMPCD='001' AND TH_REPORTER='"+sp.getString(getString(R.string.customer_code),"")+"' AND ISNULL(TH_SUBMITTED,'')<>'' AND TH_FOLLOW <> 1 AND ISNULL(TH_STATUS,'"+sp.getString(getString(R.string.status_close),"")+"') <>'"+sp.getString(getString(R.string.status_close),"")+"'";
                    Statement stm= con.createStatement();
                    ResultSet rs= stm.executeQuery(query);
                    id_list=new ArrayList<>();
                    while(rs.next()){
                        id_list.add(rs.getString("TH_HID"));

                        isSuccess=true;
                    }




                }


            }
            catch (SQLException q){
                Log.e("ERRO", q.getMessage());
                isSuccess=false;
                         }
            catch (Exception e){
                Log.e("ERRO",e.getMessage());
                isSuccess=false;

            }

            return z;
        }
    }


    private void expandFAB() {

        //Floating Action Button 1
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) fab1.getLayoutParams();
        layoutParams.rightMargin += (int) (fab1.getWidth() * 1.7);
        layoutParams.bottomMargin += (int) (fab1.getHeight() * 0.25);
        fab1.setLayoutParams(layoutParams);
        fab1.startAnimation(show_fab_1);
        fab1.setClickable(true);

        //Floating Action Button 2

        //Floating Action Button 3

    }


    private void hideFAB() {

        //Floating Action Button 1
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) fab1.getLayoutParams();
        layoutParams.rightMargin -= (int) (fab1.getWidth() * 1.7);
        layoutParams.bottomMargin -= (int) (fab1.getHeight() * 0.25);
        fab1.setLayoutParams(layoutParams);
        fab1.startAnimation(hide_fab_1);
        fab1.setClickable(false);

        //Floating Action Button 2


    }

    @Override
    public void onRestart(){
        super.onRestart();
        finish();
        startActivity(getIntent());
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        Intent i= new Intent(Intent.ACTION_MAIN);
        i.addCategory(Intent.CATEGORY_HOME);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }




    public class PendingTaskList extends AsyncTask<String,String,String>{

        String z="";
        boolean isSuccess=false;



        protected void onPreExecute(){
            pbar.setVisibility(View.VISIBLE);
        }
        protected void onPostExecute(String r){

            pbar.setVisibility(View.GONE);

            if(isSuccess){



                //Toast.makeText(getApplication(),Integer.toString(id_list.size()), Toast.LENGTH_LONG).show();

                Intent i= new Intent(LandingActivity.this,StatusActivity.class);
                for(int j=0;j<id_list.size();j++){
                    i.putExtra("pendingTask"+j,id_list.get(j).toString());
                }
                i.putExtra("flag","P");
                startActivity(i);


            }
            else {

                Toast.makeText(getApplication(),"SELECT * FROM dbo.FMCALLH WHERE TH_REPORTER='"+sp.getString(getString(R.string.customer_code),"")+"' AND ISNULL(TH_STATUS,'"+sp.getString(getString(R.string.status_close),"")+"')  not in (' ','"+sp.getString(getString(R.string.status_close),"")+"')",Toast.LENGTH_LONG).show();
                Toast.makeText(LandingActivity.this, "ERRORRRRRRR!!!", Toast.LENGTH_SHORT).show();

            }



        }


        @Override
        protected String doInBackground(String... params) {

            try{
                Connection con= connectionLanding.landingCONN();
                if(con==null){
                    z="Connection Error Please check your connection!!";

                }
                else{
                    String query="SELECT * FROM dbo.FMCALLH WHERE TH_COMPCD='001' AND TH_REPORTER='"+sp.getString(getString(R.string.customer_code),"")+"' AND TH_FOLLOW <> 1 AND ISNULL(TH_SUBMITTED,'')=''";
                    Statement stm= con.createStatement();
                    ResultSet rs= stm.executeQuery(query);
                    id_list=new ArrayList<>();
                    while(rs.next()){
                        id_list.add(rs.getString("TH_HID"));

                        isSuccess=true;
                    }



                }


            }
            catch (SQLException q){
                Log.e("ERRO", q.getMessage());
                isSuccess=false;
                        }
            catch (Exception e){
                Log.e("ERRO",e.getMessage());
                isSuccess=false;

            }

            return z;
        }
    }











}
