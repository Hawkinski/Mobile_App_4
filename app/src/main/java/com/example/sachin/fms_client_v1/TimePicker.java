package com.example.sachin.fms_client_v1;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.util.Calendar;

/**
 * Created by sachin on 4/10/2016.
 */
public class TimePicker extends DialogFragment implements TimePickerDialog.OnTimeSetListener {


    EditText text;
    @SuppressLint("ValidFragment")

    public TimePicker(View v){

        super();
        text=(EditText)v;
    }

    public TimePicker(){

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstance){
        final Calendar c = Calendar.getInstance();
        int hh= c.get(Calendar.HOUR);
        int mm=c.get(Calendar.MINUTE);

        return new TimePickerDialog(getActivity(),this,hh,mm,false);
    }

    @Override
    public void onTimeSet(android.widget.TimePicker view, int hourOfDay, int minute) {

        String time= hourOfDay+":"+minute;
        text.setText(time);

    }
}
