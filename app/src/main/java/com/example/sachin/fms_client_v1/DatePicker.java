package com.example.sachin.fms_client_v1;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.util.Calendar;

/**
 * Created by sachin on 4/10/2016.
 */
public class DatePicker extends DialogFragment implements DatePickerDialog.OnDateSetListener{
    EditText text;

    @SuppressLint("ValidFragment")
    public DatePicker (View v){

        super();
        text=(EditText)v;
    }

    public DatePicker(){

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstance){

        final Calendar c= Calendar.getInstance();
        int dd=c.get(Calendar.DAY_OF_MONTH);
        int yy = c.get(Calendar.YEAR);
        int mm= c.get(Calendar.MONTH);

        return new DatePickerDialog(getActivity(),this,yy,mm,dd);

    }
    @Override
    public void onDateSet(android.widget.DatePicker view, int year, int monthOfYear, int dayOfMonth) {

        String date= year+"-"+(monthOfYear+1)+"-"+dayOfMonth;
        text.setText(date);
    }
}
