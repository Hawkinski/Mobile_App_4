package com.example.sachin.fms_client_v1;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {


    private ConnectionLogin connectionLogin;
    private ConnectionLanding connectionLanding;
    private SharedPreferences sp,sp2;
    private String status_close,status_open,status_assigned,status_unassigned,status_enquiry,status_interim,status_work_in_progress,status_work_completed;
    private HashMap<String,String> status_description=new HashMap<>();
    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private EditText mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private HashMap<String,String> loginData= new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mEmailView = (EditText) findViewById(R.id.user_code);
       // populateAutoComplete();

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        sp=this.getSharedPreferences(getString(R.string.preference),MODE_PRIVATE);

        sp2=this.getSharedPreferences("temp",MODE_PRIVATE);

        Button mEmailSignInButton = (Button) findViewById(R.id.user_login_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        connectionLanding= new ConnectionLanding();


        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        connectionLogin= new ConnectionLogin();
        String str=sp2.getString("current_text",null);

        if(str!=null){
            mEmailView.setText(str);
        }



    }


   /* private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }*/

    /*private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }*/

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //populateAutoComplete();
            }
        }
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
       /* if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }*/

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        }
        /*else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }*/

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute();
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        //addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

   /* private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }*/


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<String, String, String> {

        private final String mEmail;
        private boolean isSuccess=false;
        private boolean isSuccess_1=false;
        private String z="";
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO: attempt authentication against a network service.

            try {

                Connection con = connectionLogin.CONN();
                Connection conn = connectionLanding.landingCONN();
                if (con == null || conn== null) {
                    z = "Connection Error! Please check your internet connection";
                } else {

                    String query = "SELECT * FROM HRZ_CUSTOMER where (CUSTH_UNAME='"+mEmail+"' or CUSTH_UEMAIL='"+mEmail+"') and CUSTH_PWD='"+mPassword+"'";
                    Statement stm = con.createStatement();
                    ResultSet rs = stm.executeQuery(query);
                    if (rs.next()) {

                        loginData.put("customer_name", rs.getString("CUSTH_UNAME"));
                        loginData.put("customer_code", rs.getString("CUSTH_UCD"));
                        loginData.put("customer_pwd", rs.getString("CUSTH_PWD"));
                        loginData.put("customer_mobile", rs.getString("CUSTH_UMOBILE"));
                        loginData.put("customer_phone", rs.getString("CUSTH_UTELPHONE"));
                        loginData.put("customer_email", rs.getString("CUSTH_UEMAIL"));
                        isSuccess = true;

                        z = "Login successful";

                    }
                    else {
                        isSuccess = false;
                        z ="Wrong user name or Password ";
                    }

                    String task_status_code="SELECT JFMS_CLOSEST, JFMS_OPENST , JFMS_ASSGST, JFMS_UNASSGST, JFMS_ENQST, JFMS_INTERIMST, JFMS_WIP, JFMS_WORKCOMP FROM JBCONT WHERE JJCOMPCD = '001'";
                    Statement stm_status_code=conn.createStatement();
                    ResultSet rs_status_code= stm_status_code.executeQuery(task_status_code);
                    if(rs_status_code.next()){

                        status_close= rs_status_code.getString(1);
                        status_open=rs_status_code.getString(2);
                        status_assigned=rs_status_code.getString(3);
                        status_unassigned=rs_status_code.getString(4);
                        status_enquiry=rs_status_code.getString(5);
                        status_interim=rs_status_code.getString(6);
                        status_work_in_progress=rs_status_code.getString(7);
                        status_work_completed=rs_status_code.getString(8);

                        isSuccess_1=true;
                    }
                    else {
                        isSuccess_1=false;
                        z="Connection Error please check connection";

                    }

                    String status_des="select STCODE,STDESC from dbo.FMSTATUS ";
                    Statement stm_status_des=conn.createStatement();
                    ResultSet rs_status_des=stm_status_des.executeQuery(status_des);
                    while(rs_status_des.next()){
                        status_description.put(rs_status_des.getString("STCODE"),rs_status_des.getString("STDESC"));
                        isSuccess_1=true;

                    }







                }


                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                isSuccess = false;
                isSuccess_1=false;


            } catch (SQLException e) {
                isSuccess = false;
                isSuccess_1=false;

                e.printStackTrace();
            }



            return z;
        }

        @Override
        protected void onPostExecute(final String success) {
            mAuthTask = null;
            showProgress(false);

            if (isSuccess && isSuccess_1) {



                SharedPreferences.Editor editor= sp2.edit();
                editor.putString("current_text",mEmail);
                editor.apply();


                SharedPreferences.Editor edit= sp.edit();

                edit.putString(getString(R.string.customer_code),loginData.get("customer_code"));
                edit.putString(getString(R.string.customer_name), loginData.get("customer_name"));
                edit.putString(getString(R.string.customer_phone),loginData.get("customer_phone"));
                edit.putString(getString(R.string.customer_mobile), loginData.get("customer_mobile"));
                edit.putString(getString(R.string.customer_email), loginData.get("customer_email"));
                edit.putString(getString(R.string.status_on_going),status_open );
                edit.putString(getString(R.string.status_close),status_close);
                edit.putString(getString(R.string.status_assigned),status_assigned);

                edit.putString(getString(R.string.status_unassigned),status_unassigned);
                edit.putString(getString(R.string.status_enquiry),status_enquiry);
                edit.putString(getString(R.string.status_interim),status_interim);
                edit.putString(getString(R.string.status_work_completed),status_work_completed);
                edit.putString(getString(R.string.status_work_in_progress),status_work_in_progress);


                edit.putString(getString(R.string.status_unassigned_des),status_description.get(status_unassigned));
                edit.putString(getString(R.string.status_assigned_des),status_description.get(status_assigned));

                edit.putString(getString(R.string.status_on_going_des),status_description.get(status_open) );
                edit.putString(getString(R.string.status_close_des),status_description.get(status_close));

                edit.putString(getString(R.string.status_enquiry_des),status_description.get(status_enquiry));
                edit.putString(getString(R.string.status_interim_des),status_description.get(status_interim));
                edit.putString(getString(R.string.status_work_completed_des),status_description.get(status_work_completed));
                edit.putString(getString(R.string.status_work_in_progress_des),status_description.get(status_work_in_progress));



                edit.apply();

                Intent i = new Intent(getApplication(),LandingActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK |
                        Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();




            } else {
                mPasswordView.setError(z);
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }


    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setTitle("Exit").setMessage("Are you Sure you want to exit?")
                .setNegativeButton("No" ,null)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        LoginActivity.super.onBackPressed();
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                }).create().show();




    }
}

