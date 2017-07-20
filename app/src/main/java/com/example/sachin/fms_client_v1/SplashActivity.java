package com.example.sachin.fms_client_v1;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class SplashActivity extends AppCompatActivity {
    private ImageView rotate;
    private SharedPreferences sp;

    private Timer timer;
    private MyTimerTask myTimerTask;
    private String cd;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);


        sp=this.getSharedPreferences(getString(R.string.preference),MODE_PRIVATE);

        cd= sp.getString(getString(R.string.customer_code),null);





        //sp=this.getSharedPreferences(getString(R.string.preferences), MODE_PRIVATE);
        // cd = sp.getString(getString(R.string.user_cd),null);

        rotate=(ImageView)findViewById(R.id.rotate);
        rotate.post(
                new Runnable(){

                    @Override
                    public void run() {
                        rotate.startAnimation(AnimationUtils.loadAnimation(SplashActivity.this, R.anim.rotate));
                    }
                });

        //Calculate the total duration
        int duration = 0;


        timer = new Timer();
        myTimerTask = new MyTimerTask();
        timer.schedule(myTimerTask, 1600);

    }

    class MyTimerTask extends TimerTask {

        @Override
        public void run() {

            timer.cancel();

            if(cd == null){
                Intent intent = new Intent(
                        SplashActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                startActivity(intent);
                finish();
            }
            else {
                Intent intent = new Intent(
                        SplashActivity.this, LandingActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                startActivity(intent);
                finish();
            }




        }
    }
}
