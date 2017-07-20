package com.example.sachin.fms_client_v1;

import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BookActivity extends AppCompatActivity implements View.OnClickListener {

    private Spinner spinner_1 , spinner_2,spinner_3,spinner_4,spinner_5,spinner_6;
    EditText complaint,date,time;
    private String str;
    private ProgressBar pbar;
    private ConnectionLanding connectionLanding;
    private SharedPreferences sp;
    private List<String> contract= new ArrayList<>();
    private List<String> location= new ArrayList<>();
    private List<String> building= new ArrayList<>();
    private List<String> unit= new ArrayList<>();
    private List<String> priority= new ArrayList<>();
    private List<String> asset= new ArrayList<>();
    private HashMap<String,String> priority_hrs= new HashMap<>();
    private HashMap<String,String> priority_code= new HashMap<>();

    private EditText content_text,format_text, txt_complaint,asset_code;

    private Button send_request;
    private ImageButton scan_barcode;

    private HashMap<String,String> location_code= new HashMap<>();
    private HashMap<String,String> building_code= new HashMap<>();
    private HashMap<String,String> unit_code= new HashMap<>();
    private boolean canClick=false;
    private View focusView=null;
    private FloatingActionButton fab,fab1,fab2;
    private Animation show_fab1,hide_fab1,show_fab2,hide_fab2;
    private ImageButton add;

    private boolean FAB_status= false;
    private int count=0;
    private LinearLayout root_view;
    private HashMap<String,EditText> editText= new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Book Complaint");

        fab = (FloatingActionButton) findViewById(R.id.fab);

        fab1=(FloatingActionButton) findViewById(R.id.fab_1);
        fab2=(FloatingActionButton) findViewById(R.id.fab_2);
        scan_barcode=(ImageButton)findViewById(R.id.qr_scanner);
        root_view = (LinearLayout) findViewById(R.id.root_view);

        add=(ImageButton)findViewById(R.id.add);

        show_fab1= AnimationUtils.loadAnimation(getApplicationContext(), R.anim.show_fab_1);
        hide_fab1= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.hide_fab_1);
        show_fab2= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.show_fab_2);
        hide_fab2= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.hide_fab_2);

        send_request=(Button)findViewById(R.id.request_send);

        content_text=(EditText)findViewById(R.id.content);
        format_text=(EditText)findViewById(R.id.format);
        date=(EditText)findViewById(R.id.date);
        time=(EditText)findViewById(R.id.time);

        spinner_1=(Spinner) findViewById(R.id.spinner_1);
        complaint=(EditText)findViewById(R.id.complaint);
        spinner_2=(Spinner) findViewById(R.id.spinner_2);
        spinner_3=(Spinner) findViewById(R.id.spinner_3);
        spinner_4=(Spinner) findViewById(R.id.spinner_4);
        spinner_5=(Spinner) findViewById(R.id.spinner_5);
        //spinner_6=(Spinner) findViewById(R.id.spinner_6);

        pbar= (ProgressBar)findViewById(R.id.progress);
        connectionLanding= new ConnectionLanding();

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count++;


                LinearLayout child_1 = new LinearLayout(BookActivity.this);
                child_1.setOrientation(LinearLayout.HORIZONTAL);
                txt_complaint=new EditText(BookActivity.this);
                txt_complaint.setId(count);

                txt_complaint.setHint("Complaint");

                txt_complaint.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));

                String s="EditTextComplaint"+Integer.toString(count);
                editText.put(s,txt_complaint);

                LinearLayout child_2 = new LinearLayout(BookActivity.this);
                child_2.setOrientation(LinearLayout.HORIZONTAL);
                asset_code=new EditText(BookActivity.this);
                asset_code.setHint("Asset Code");
                asset_code.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT,10f));


                asset_code.setId(count);
                String s2="EditTextAsset"+Integer.toString(count);
                editText.put("EditTextAsset0",format_text);
                editText.put(s2,asset_code);
                ImageButton scan=new ImageButton(BookActivity.this);

                LinearLayout.LayoutParams parms= new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT,0.5f);
                scan.setLayoutParams(parms);
                //scan.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT,0.5f));
                scan.setImageResource(R.drawable.ic_barcode_black_36dp);
                scan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        IntentIntegrator integratort= new IntentIntegrator(BookActivity.this);
                        integratort.initiateScan();
                    }
                });



                child_1.addView(txt_complaint);
                child_2.addView(asset_code);
                child_2.addView(scan);

                root_view.addView(child_1);
                root_view.addView(child_2);




            }
        });

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
                editor.commit();

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

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });







        time.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.setFocusable(true);
                v.setFocusableInTouchMode(true);
                return false;
            }
        });
        time.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    TimePicker timePicker = new TimePicker(v);
                    FragmentTransaction F = getFragmentManager().beginTransaction();
                    timePicker.show(F, "TimePicker");

                }
            }
        });


        date.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.setFocusable(true);
                v.setFocusableInTouchMode(true);
                return false;
            }
        });
        date.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    DatePicker datePicker = new DatePicker(v);
                    FragmentTransaction f = getFragmentManager().beginTransaction();
                    datePicker.show(f, "DatePicker");
                }
            }
        });

        scan_barcode.setOnClickListener(this);



        fill_list fill= new fill_list();
        fill.execute();


        //Set<String> temp = sp.getStringSet(getString(R.string.customer_contract_number), new HashSet<String>());




        contract.add("Select Contract Number");
        location.add("Select Location");
        building.add("Select Building");
        unit.add("Select Unit");
        priority.add("Select Priority");
        asset.add("Select Asset");


        sp=this.getSharedPreferences(getString(R.string.preference),MODE_PRIVATE);

        /**
         * Array adapter to Populate the contract spinner
         */

        ArrayAdapter<String> adapter=new ArrayAdapter<String>(BookActivity.this,android.R.layout.simple_spinner_item,contract){
            @Override
        public boolean isEnabled(int position){
                if(position==0){
                    return  false;
                }
                else{
                    return true;
                }
            }
            @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent){
                View view= super.getDropDownView(position,convertView,parent);

                TextView tv= (TextView)view;
                if(position==0){
                    tv.setTextColor(Color.GRAY);

                }
                else{
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };

        adapter.setDropDownViewResource(R.layout.spinner_item);

        spinner_1.setAdapter(adapter);

        ArrayAdapter<String> adapter_2=new ArrayAdapter<String>(BookActivity.this,android.R.layout.simple_spinner_item,location){
            @Override
            public boolean isEnabled(int position){
                if(position==0){
                    return  false;
                }
                else{
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent){
                View view= super.getDropDownView(position,convertView,parent);

                TextView tv= (TextView)view;
                if(position==0){
                    tv.setTextColor(Color.GRAY);

                }
                else{
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };

        adapter_2.setDropDownViewResource(R.layout.spinner_item);
        spinner_2.setAdapter(adapter_2);

        ArrayAdapter<String> adapter_3=new ArrayAdapter<String>(BookActivity.this,android.R.layout.simple_spinner_item,building){
            @Override
            public boolean isEnabled(int position){
                if(position==0){
                    return  false;
                }
                else{
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent){
                View view= super.getDropDownView(position,convertView,parent);

                TextView tv= (TextView)view;
                if(position==0){
                    tv.setTextColor(Color.GRAY);

                }
                else{
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };

        adapter_3.setDropDownViewResource(R.layout.spinner_item);
        spinner_3.setAdapter(adapter_3);

        ArrayAdapter<String> adapter_4=new ArrayAdapter<String>(BookActivity.this,android.R.layout.simple_spinner_item,unit){
            @Override
            public boolean isEnabled(int position){
                if(position==0){
                    return  false;
                }
                else{
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent){
                View view= super.getDropDownView(position,convertView,parent);

                TextView tv= (TextView)view;
                if(position==0){
                    tv.setTextColor(Color.GRAY);

                }
                else{
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        adapter_4.setDropDownViewResource(R.layout.spinner_item);
        spinner_4.setAdapter(adapter_4);







        /**
         * TO check whether the previous spinners are selcted or not
         */



       /*spinner_3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!spinner_2.isSelected()) {
                    spinner_2.setPrompt("Please Select Contract Number First");
                    focusView = spinner_2;
                }
            }
        });
        spinner_4.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!spinner_3.isSelected()) {
                    spinner_3.setPrompt("Please Select Contract Number First");
                    focusView = spinner_3;
                }
            }
        });
*/


        /**
         * Array adapter to Populate the priority spinner
         */
        fill_priority();

        ArrayAdapter<String> parray=new ArrayAdapter<String>(BookActivity.this,android.R.layout.simple_spinner_item,priority){
            @Override
            public boolean isEnabled(int position){
                if(position==0){
                    return  false;
                }
                else{
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent){
                View view= super.getDropDownView(position,convertView,parent);

                TextView tv= (TextView)view;
                if(position==0){
                    tv.setTextColor(Color.GRAY);

                }
                else{
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };

        parray.setDropDownViewResource(R.layout.spinner_item);
        spinner_5.setAdapter(parray);


        /*ArrayAdapter<String> asset_adapter= new ArrayAdapter<String>(BookActivity.this,android.R.layout.simple_spinner_item,asset){
            @Override
            public boolean isEnabled(int position){
                if(position==0){
                    return false;
                }
                else{
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent){
                View view= super.getDropDownView(position,convertView,parent);
                TextView text= (TextView)view;
                if(position==0){
                    text.setTextColor(Color.GRAY);

                }
                else{
                    text.setTextColor(Color.BLACK);
                }
                return view;
            }
        };

        asset_adapter.setDropDownViewResource(R.layout.spinner_item);
        spinner_6.setAdapter(asset_adapter);*/







        /**
         *  contract spinner Item Selected listener
         */



        spinner_1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            private boolean isSuccess= false;
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                location= new ArrayList<String>();
                location_code= new HashMap<String, String>();
                location.add("Select Location");

                if(position>0){
                    try{
                        Connection con= connectionLanding.landingCONN();
                        if(con== null){
                            Toast.makeText(getApplicationContext(),"Connection Error! Please check your connection",Toast.LENGTH_LONG).show();

                        }
                        else{
                            String s=parent.getItemAtPosition(position).toString();
                            String[] temp=s.split("-");
                            str= temp[0];

                            Toast.makeText(getApplicationContext(),str,Toast.LENGTH_LONG).show();
                            String query="SELECT BLD_NO,BLD_NAME,LCCD,LCDESC FROM dbo.FMCONTBUIlDINGS INNER JOIN FMBUILDING ON  BLD_COMPCD =CBLD_COMPCD AND CBLD_BUILDING =BLD_NO AND BLD_INACTIVE_YN=0 INNER JOIN JBLOCN ON LCCOMPCD=CBLD_COMPCD AND LCCD=BLD_LOCATION AND LCINACTIVE=0 WHERE CBLD_COMPCD='001' AND CBLD_CONTRACT='"+str+"'";

                            Statement stm= con.createStatement();
                            ResultSet rs= stm.executeQuery(query);
                            while (rs.next()){
                                location.add(rs.getString("LCDESC"));
                                location_code.put(rs.getString("LCDESC"),rs.getString("LCCD"));
                                isSuccess= true;

                            }


                        }

                    }
                    catch (SQLException e) {
                        isSuccess = false;
                        e.printStackTrace();
                    }

                    if(isSuccess){

                        canClick= true;


                        ArrayAdapter<String> adapter=new ArrayAdapter<String>(BookActivity.this,android.R.layout.simple_spinner_item,location){
                            @Override
                            public boolean isEnabled(int position){
                                if(position==0){
                                    return  false;
                                }
                                else{
                                    return true;
                                }
                            }
                            @Override
                            public View getDropDownView(int position, View convertView, ViewGroup parent){
                                View view= super.getDropDownView(position,convertView,parent);

                                TextView tv= (TextView)view;
                                if(position==0){
                                    tv.setTextColor(Color.GRAY);

                                }
                                else{
                                    tv.setTextColor(Color.BLACK);
                                }
                                return view;
                            }
                        };

                        adapter.setDropDownViewResource(R.layout.spinner_item);
                        spinner_2.setAdapter(adapter);
                    }
                    else {
                        canClick= false;
                        Toast.makeText(getApplicationContext(),"No data found",Toast.LENGTH_LONG).show();
                    }


                }



            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        /**
         *  location spinner Item Selected listener
         */

        spinner_2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            private boolean isSuccess= false;

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                building=new ArrayList<String>();
                building_code= new HashMap<String, String>();
                building.add("Select Building");
                if(position>0){
                        try{
                            Connection con= connectionLanding.landingCONN();
                            if(con== null){
                                Toast.makeText(getApplicationContext(),"Connection Error! Please check your connection",Toast.LENGTH_LONG).show();

                            }
                            else{


                                String query="SELECT BLD_NO,BLD_NAME,LCCD,LCDESC FROM dbo.FMCONTBUIlDINGS INNER JOIN FMBUILDING ON  BLD_COMPCD =CBLD_COMPCD AND CBLD_BUILDING =BLD_NO AND BLD_INACTIVE_YN=0 INNER JOIN JBLOCN ON LCCOMPCD=CBLD_COMPCD AND LCCD=BLD_LOCATION AND LCINACTIVE=0 WHERE CBLD_COMPCD='001' AND CBLD_CONTRACT='"+str+"'";

                                Statement stm= con.createStatement();
                                ResultSet rs= stm.executeQuery(query);
                                while (rs.next()){
                                    building.add(rs.getString("BLD_NAME"));
                                    building_code.put(rs.getString("BLD_NAME"),rs.getString("BLD_NO"));
                                    isSuccess= true;

                                }


                            }

                        }
                        catch (SQLException e) {
                            isSuccess = false;
                            e.printStackTrace();
                        }



                    if(isSuccess){

                        ArrayAdapter<String> adapter=new ArrayAdapter<String>(BookActivity.this,android.R.layout.simple_spinner_item,building){
                            @Override
                            public boolean isEnabled(int position){
                                if(position==0){
                                    return  false;
                                }
                                else{
                                    return true;
                                }
                            }
                            @Override
                            public View getDropDownView(int position, View convertView, ViewGroup parent){
                                View view= super.getDropDownView(position,convertView,parent);

                                TextView tv= (TextView)view;
                                if(position==0){
                                    tv.setTextColor(Color.GRAY);

                                }
                                else{
                                    tv.setTextColor(Color.BLACK);
                                }
                                return view;
                            }
                        };

                        adapter.setDropDownViewResource(R.layout.spinner_item);
                        spinner_3.setAdapter(adapter);
                    }
                    else {
                        Toast.makeText(getApplicationContext(),"No data found",Toast.LENGTH_LONG).show();
                    }


                }
                else {
                    spinner_1.setPrompt("Please Select Contract Number First");
                    focusView = spinner_1;
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {



                    TextView errorText = (TextView)spinner_2.getSelectedView();
                    errorText.setError("Please seletc this");
                    errorText.setTextColor(Color.RED);//just to highlight that this is an error
                    errorText.setText("Please select contact Number");//changes the selected item text to this


            }
        });


        /**
         *  Building spinner Item Selected listener
         */
        spinner_3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            private boolean isSuccess= false;

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                unit= new ArrayList<String>();
                unit_code= new HashMap<String, String>();
                unit.add("Select Unit");

                if(position>0){

                    List<String> loc_code= new ArrayList<String>(location_code.values());
                    List<String> build_code= new ArrayList<String>(building_code.values());
                    try{
                        Connection con= connectionLanding.landingCONN();
                        if(con== null){
                            Toast.makeText(getApplicationContext(),"Connection Error! Please check your connection",Toast.LENGTH_LONG).show();

                        }
                        else{


                            String query="SELECT UNT_NO,UNT_DESCRIPTION FROM dbo.FMUNIT WHERE UNT_COMPCD ='001' AND UNT_LOCATION='"+loc_code.get(position-1)+"' AND UNT_BUILDING='"+build_code.get(position-1)+"'";
                            Statement stm= con.createStatement();
                            ResultSet rs= stm.executeQuery(query);
                            while (rs.next()){
                                unit.add(rs.getString("UNT_DESCRIPTION"));
                                unit_code.put(rs.getString("UNT_DESCRIPTION"),rs.getString("UNT_NO"));
                                isSuccess= true;

                            }


                        }

                    }
                    catch (SQLException e) {
                        isSuccess = false;
                        e.printStackTrace();
                    }

                    if(isSuccess){

                        ArrayAdapter<String> adapter=new ArrayAdapter<String>(BookActivity.this,android.R.layout.simple_spinner_item,unit){
                            @Override
                            public boolean isEnabled(int position){
                                if(position==0){
                                    return  false;
                                }
                                else{
                                    return true;
                                }
                            }
                            @Override
                            public View getDropDownView(int position, View convertView, ViewGroup parent){
                                View view= super.getDropDownView(position,convertView,parent);

                                TextView tv= (TextView)view;
                                if(position==0){
                                    tv.setTextColor(Color.GRAY);

                                }
                                else{
                                    tv.setTextColor(Color.BLACK);
                                }
                                return view;
                            }
                        };

                        adapter.setDropDownViewResource(R.layout.spinner_item);
                        spinner_4.setAdapter(adapter);
                    }
                    else {
                        Toast.makeText(getApplicationContext(),"No data found",Toast.LENGTH_LONG).show();
                    }


                }
                else {
                    spinner_2.setPrompt("Please Select Location First");
                    focusView = spinner_2;
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        spinner_4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (spinner_3.isSelected() && position > 0) {
                    Toast.makeText(getApplicationContext(), parent.getItemAtPosition(position).toString(), Toast.LENGTH_LONG).show();
                } else {
                    spinner_3.setPrompt("Please Select Building First");
                    focusView = spinner_3;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        send_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 attemptSave();
            }
        });





    }

    public void attemptSave(){

        String str1= date.getText().toString();
        String str2=time.getText().toString();
        boolean cancel = false;

        if(spinner_1.getSelectedItem()== "Select Contract Number"){
            TextView errorText = (TextView)spinner_1.getSelectedView();
            errorText.setError("Please seletc this");
            errorText.setTextColor(Color.RED);//just to highlight that this is an error
            errorText.setText("Please select contact Number");//changes the selected item text to this
            cancel = true;

        }
        if(spinner_2.getSelectedItem()==  "Select Location"){
            TextView errorText = (TextView)spinner_2.getSelectedView();
            errorText.setError("Please seletc this");
            errorText.setTextColor(Color.RED);//just to highlight that this is an error
            errorText.setText("Please select Location");//changes the selected item text to this
            cancel = true;

        }

        if(spinner_3.getSelectedItem()== "Select Building"){
            TextView errorText = (TextView)spinner_3.getSelectedView();
            errorText.setError("Please seletc this");
            errorText.setTextColor(Color.RED);//just to highlight that this is an error
            errorText.setText("Please select building");//changes the selected item text to this
            cancel = true;

        }

        if(spinner_4.getSelectedItem()== "Select Unit"){
            TextView errorText = (TextView)spinner_4.getSelectedView();
            errorText.setError("Please seletc this");
            errorText.setTextColor(Color.RED);//just to highlight that this is an error
            errorText.setText("Please select unit");//changes the selected item text to this
            cancel = true;


        }

        if(spinner_5.getSelectedItem()== "Select Priority"){
            TextView errorText = (TextView)spinner_5.getSelectedView();
            errorText.setError("Please seletc this");
            errorText.setTextColor(Color.RED);//just to highlight that this is an error
            errorText.setText("Please select priority");//changes the selected item text to this
            cancel = true;

        }

        if (TextUtils.isEmpty(str1)) {
            date.setError(getString(R.string.error_field_required));
            cancel = true;
        }


        if (TextUtils.isEmpty(str2)) {
            time.setError(getString(R.string.error_field_required));
            cancel = true;
        }


        if(!cancel){
            Save save = new Save();
            save.execute();
        }





    }

    @Override
    public void onClick(View v) {
        IntentIntegrator integratort= new IntentIntegrator(BookActivity.this);
        integratort.initiateScan();
    }


    /**
     *  Async method to fill the contract spinner
     */


    public class Save extends AsyncTask<String,String,String>{

        boolean isSuccess=false;
        boolean isSuccess_1=false;
        boolean isSuccess_2=false;
        boolean isSuccess_3=false;
        String z="";
        String doc_type;
        String doc_number;
        String loc_code;
        String b_code;
        String u_code;
        String cnt_code;
        String hid;
        String complaint_code;
        String p_hrs;
        String p_code;
        String remarks= complaint.getText().toString();
        String datetime= (date.getText()+" "+time.getText()).toString();

        String th_user;
        String location= spinner_2.getSelectedItem().toString();
        String building= spinner_3.getSelectedItem().toString();
        String unit= spinner_4.getSelectedItem().toString();
        String cnt= spinner_1.getSelectedItem().toString();
        String p= spinner_5.getSelectedItem().toString();
        String pcode=spinner_5.getSelectedItem().toString();

        List<String> asset_complaint= new ArrayList<>();

        public void onPreExecute(){
            pbar.setVisibility(View.VISIBLE);

            for(int i =1;i<count;i++) {


                EditText e = editText.get("EditTextComplaint" + Integer.toString(i));
                asset_complaint.add(e.getText().toString());
            }

        }




        public void onPostExecute(String s){

            pbar.setVisibility(View.GONE);



            if(isSuccess && isSuccess_1 && isSuccess_2 && isSuccess_3){



                AlertDialog.Builder build = new AlertDialog.Builder(BookActivity.this);
                build.setTitle("Booking Reference Number")
                        .setMessage("Your complaint has been submitted, Please note the complaint reference number\n" +doc_number)
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent i = new Intent(getApplication(), LandingActivity.class);
                                startActivity(i);
                            }
                        });
                AlertDialog alert = build.create();
                alert.show();

                //Toast.makeText(getApplication(),"Request has been sent, Your request number is"+doc_number,Toast.LENGTH_LONG).show();


            }
            else {
                Toast.makeText(getApplication(),s,Toast.LENGTH_LONG).show();


            }
        }

        @Override
        protected String doInBackground(String... params) {


            p_code= priority_code.get(pcode);
            u_code= unit_code.get(unit);
            b_code= building_code.get(building);
            loc_code = location_code.get(location);
            String[] temp=cnt.split("-");
            cnt_code= temp[0];
            p_hrs=priority_hrs.get(p);
            try{

                Connection con = connectionLanding.landingCONN();
                if(con==null){
                    z="Connection error! Please check your connection";

                }

                else{

                    String query_doc_type="SELECT TTCODE=ISNULL(JFMS_MOBAUTOTT,'') FROM dbo.JBCONT";
                    Statement stm= con.createStatement();
                    ResultSet rs= stm.executeQuery(query_doc_type);
                    if(rs.next()){
                        doc_type=rs.getString("TTCODE");

                        isSuccess=true;
                    }
                    else {
                        z="Connection error! Please check your connection";

                        isSuccess=false;

                    }
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


                    String user_name="SELECT JFMS_DFTVIEWUSER FROM dbo.JBCONT WHERE JJCOMPCD='001'";
                    Statement stm_user_name= con.createStatement();
                    ResultSet rs_user_name= stm_user_name.executeQuery(user_name);
                    if (rs_user_name.next()){
                        th_user=rs_user_name.getString(1);
                        isSuccess_1=true;

                    }
                    else {
                        z="Connection error! Please check your connection";
                        isSuccess_1=false;

                    }

                    String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());

                    String insert_fmcllh="INSERT INTO FMCALLH (TH_COMPCD ,TH_DOC_TYPE ,TH_CALL_DOC ,TH_REPORT_DT ,TH_DUE_DT ,TH_SCHED_DT ,TH_TELNO ,TH_REPORTER ,TH_CALL_SOURCE ,TH_LOC ,TH_BUILD ,TH_UNIT ,TH_CONTRACT ,TH_PRIORITY ,TH_REG_MOB ,TH_REG_EMAIL ,TH_FOLLOW, TH_STATUS, TH_USER) values('001','"+doc_type+"','"+doc_number+"','"+currentTime+"','"+currentTime+"','"+datetime+"','"+sp.getString(getString(R.string.customer_phone),"")+"','"+sp.getString(getString(R.string.customer_name),"")+"','MobileApp','"+loc_code+"','"+b_code+"','"+u_code+"','"+cnt_code+"','"+p_hrs+"','"+sp.getString(getString(R.string.customer_mobile),"")+"','"+sp.getString(getString(R.string.customer_email),"")+"',0,'"+sp.getString(getString(R.string.status_on_going),"")+"','"+th_user+"')";

                    Statement stm_fmcllh=con.createStatement();
                    stm_fmcllh.executeUpdate(insert_fmcllh);


                    String get_hid="SELECT TH_HID FROM FMCALLH WHERE TH_CALL_DOC='"+doc_number+"'";
                    Statement stm_hid= con.createStatement();
                    ResultSet rs_hid=stm_hid.executeQuery(get_hid);
                    if(rs_hid.next()){
                        hid=rs_hid.getString("TH_HID");
                        isSuccess_2=true;
                    }
                    else {
                        z = "Connection error! Please check your connection";
                        isSuccess_2 = false;
                    }

                    String get_complaint_code="SELECT JFMS_DFTCOMPLAINTCD FROM dbo.JBCONT ";
                    Statement stm_complaint_code=con.createStatement();
                    ResultSet rs_complaint_code=stm_complaint_code.executeQuery(get_complaint_code);
                    if(rs_complaint_code.next()){
                        complaint_code=rs_complaint_code.getString(1);
                        isSuccess_3=true;
                    }
                    else {
                        z = "Connection error! Please check your connection";
                        isSuccess_3 = false;
                    }


                    String insert_fmcalld="INSERT INTO dbo.FMCALLD (TH_HID ,TD_COMPCD ,TD_DOC_TYPE ,TD_CALL_DOC ,TD_LNO ,TD_COMPLAINT ,TD_DETAILS ,TD_SEVERITY,TH_ASSET) values('"+hid+"','001','"+doc_type+"','"+doc_number+"',1,'"+complaint_code+"','"+remarks+"','"+p_code+"')";
                    Statement stm_fmcalld=con.createStatement();
                    stm_fmcalld.executeUpdate(insert_fmcalld);

                    for(int i =0;i<count;i++){
                        String insert_fmcalld2="INSERT INTO dbo.FMCALLD (TH_HID ,TD_COMPCD ,TD_DOC_TYPE ,TD_CALL_DOC ,TD_LNO ,TD_COMPLAINT ,TD_DETAILS ,TD_SEVERITY,TH_ASSET) values('"+hid+"','001','"+doc_type+"','"+doc_number+"',1,'"+complaint_code+"','"+asset_complaint.get(i)+"','"+p_code+"')";
                        Statement stm_fmcalld2=con.createStatement();
                        stm_fmcalld2.executeUpdate(insert_fmcalld2);

                    }



                    }









            }
            catch (Exception e){

                z=e.getMessage();
            }
            return z;
        }
    }



    /**
     *  Async method to fill the contract spinner
     */

    public class fill_list extends AsyncTask<String,String,String>{

        private boolean isSuccess= false;
        private String z="";


        @Override
        protected String doInBackground(String... params) {

            try{
                Connection con= connectionLanding.landingCONN();
                if(con== null){
                    z="Connection Error! Please check your connection";

                }
                else{
                    String query="SELECT CUSTD_CNT_NO,DESCRIPTION=(CUSTD_CNT_NO + '-' + CNT_DESCRIPTION) FROM HRZNET..GLUSER_CUSTD INNER JOIN  dbo.FMCONTRACT ON custd_cnt_no=CNT_NO WHERE CUSTD_CD ='"+sp.getString(getString(R.string.customer_code),"")+"'";


                    Statement stm= con.createStatement();
                    ResultSet rs= stm.executeQuery(query);
                    while (rs.next()){
                        contract.add(rs.getString("DESCRIPTION"));
                        isSuccess= true;

                    }

                   // String query1="SELECT BLD_NO,BLD_NAME,LCCD,LCDESC FROM dbo.FMCONTBUIlDINGS " +
                          //  "INNER JOIN FMBUILDING ON  BLD_COMPCD =CBLD_COMPCD AND CBLD_BUILDING =BLD_NO AND BLD_INACTIVE_YN=0" +
                          //  "INNER JOIN JBLOCN ON LCCOMPCD=CBLD_COMPCD AND LCCD=BLD_LOCATION AND LCINACTIVE=0" +
                        //    "WHERE CBLD_COMPCD='001' AND CBLD_CONTRACT='C002'";
                }

            }
            catch (SQLException e) {
                isSuccess = false;
                e.printStackTrace();
            }


            return z;
        }

        public void onPreExecute(){

            pbar.setVisibility(View.VISIBLE);
        }

        public void onPostExecute(String s){

            pbar.setVisibility(View.GONE);

            if (isSuccess){

                SharedPreferences.Editor edit= sp.edit();
                Set<String> temp = new HashSet<String>(contract);
                edit.putStringSet(getString(R.string.customer_contract_number),temp);
                edit.apply();


            }
            else {


                Toast.makeText(BookActivity.this,"Connection error",Toast.LENGTH_LONG).show();
            }
        }

    }


    /**
     *  method to add new Row programmatically
     */










    /**
     *  method to fill the priority spinner
     */
    public void fill_priority(){

        boolean isSuccess= false;

        try{
            Connection con= connectionLanding.landingCONN();
            if(con== null){
                Toast.makeText(getApplicationContext(),"Connection Error! Please check your connection",Toast.LENGTH_LONG).show();

            }
            else{
                String query="SELECT * FROM dbo.FMPRIOR ";
                Statement stm= con.createStatement();
                ResultSet rs= stm.executeQuery(query);
                while (rs.next()){
                    priority.add(rs.getString("PRDESC"));
                    priority_hrs.put(rs.getString("PRDESC"), rs.getString("PRHRS"));
                    priority_code.put(rs.getString("PRDESC"), rs.getString("PRCODE"));
                    isSuccess= true;

                }


            }

        }
        catch (SQLException e) {
            isSuccess = false;
            e.printStackTrace();
        }

        if(isSuccess){


        }
        else {
            Toast.makeText(getApplicationContext(),"No data found",Toast.LENGTH_LONG).show();
        }

    }




    public void onActivityResult(int requestCode,int resultCode,Intent i){

        IntentResult result= IntentIntegrator.parseActivityResult(requestCode,resultCode,i);
        if(result!=null){
            String scanContent= result.getContents();
            format_text.setText(scanContent);



        }

        if(result!=null){
            for(int j=0;j<count;j++){
                EditText e=editText.get("EditTextAsset"+j);
                String scanContent= result.getContents();
                e.setText(scanContent);
            }

        }
        else{
            Toast.makeText(getApplication(),"No data received",Toast.LENGTH_LONG).show();
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
