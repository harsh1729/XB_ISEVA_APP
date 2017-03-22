package com.iseva.app.source.travel;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.EditText;

import com.iseva.app.source.R;

import java.util.Calendar;

/**
 * Created by xb_sushil on 1/19/2017.
 */

public class Datepicker extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datepickerdialog = new DatePickerDialog(getActivity(),this,year,month,day);



        return datepickerdialog;
    }



    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {



        EditText et = (EditText)getActivity().findViewById(R.id.journey_date);
        String date ="";
        if(i1 > 9 && i2 > 9)
        {

            Search_Buses_Key.Selected_date = i+"-"+(i1+1)+"-"+i2;
            date = i+"-"+(i1+1)+"-"+i2;
            et.setText(changeDateForm(date));
        }
        else if(i1 < 10 && i2 < 10)
        {

            Search_Buses_Key.Selected_date = ""+i+"-"+"0"+(i1+1)+"-"+"0"+i2;
            date = ""+i+"-"+"0"+(i1+1)+"-"+"0"+i2;
            et.setText(changeDateForm(date));
        }
        else if(i1 < 10 && i2 >9)
        {

            Search_Buses_Key.Selected_date = i+"-"+"0"+(i1+1)+"-"+i2;
            date = i+"-"+"0"+(i1+1)+"-"+i2;
            et.setText(changeDateForm(date));
        }
        else if(i1 > 9 && i2 < 10)
        {

            Search_Buses_Key.Selected_date = i+"-"+(i1+1)+"-"+"0"+i2;
            date = i+"-"+(i1+1)+"-"+"0"+i2;
            et.setText(changeDateForm(date));
        }


    }

    public String changeDateForm(String date)
    {

        String[] days ={"Sunday","Monday","Tuesday","Wednesday","Thirsday","Friday","Saturday"};
        String[] months={"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};

        String day = date.substring(8,10);
        String month = date.substring(5,7);
        String year = date.substring(0,4);

        int day_int = Integer.parseInt(day);
        int month_int =Integer.parseInt(month);
        int year_int = Integer.parseInt(year);

        Calendar calendar = Calendar.getInstance();
        calendar.set(year_int,month_int-1,day_int);

        int day_of_weak_int = calendar.get(calendar.DAY_OF_WEEK);
        String day_of_weak = days[day_of_weak_int-1];


        String final_date = day+"-"+months[month_int-1]+"-"+year+","+day_of_weak;


        return final_date;
    }


}
