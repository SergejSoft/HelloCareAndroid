package com.hellocare.util;

import android.content.Context;
import android.text.format.DateUtils;

import com.hellocare.R;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;

import hirondelle.date4j.DateTime;

/**
 * Created by admin on 21/11/2016.
 */

public class FormatUtils {

public  static String PATTERN_DATE_TIME = "yyyy-MM-dd HH:mm:ss";
    public  static String PATTERN_DATE = "dd.MM.yyyy";
    public  static String PATTERN_TIME =  "HH:mm";
    public static String convertTimestamp(String currentTimestamp, String outputPattern){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        SimpleDateFormat output = new SimpleDateFormat(outputPattern);
        Date d = null;
        try {
            d = sdf.parse(currentTimestamp);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return output.format(d);
    }
    public static String timestampToProperString(Context c, String currentTimestamp){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

        Date d = null;
        try {
            d = sdf.parse(currentTimestamp);
          // if (DateUtils.isToday(d.getTime())){return  c.getString(R.string.today);}else {
            // return  DateUtils.formatDateTime(c, d.getTime(),
            //         DateUtils.FORMAT_ABBREV_ALL|DateUtils.FORMAT_SHOW_WEEKDAY|DateUtils.FORMAT_SHOW_DATE);
               return  DateUtils.getRelativeTimeSpanString(d.getTime(), System.currentTimeMillis(), DateUtils.DAY_IN_MILLIS,
                       DateUtils.FORMAT_NO_YEAR|DateUtils.FORMAT_ABBREV_ALL|DateUtils.FORMAT_SHOW_WEEKDAY|DateUtils.FORMAT_SHOW_DATE).toString();
         //  }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
       // return output.format(d);
    }
    public static String formatDecimal(double d)
    {
        if(d == (int) d)
            return String.format("%d",(int)d);
        else
            return String.format("%s",d);
    }
    public static String formatCurrency(double d, String currency)
    {
        NumberFormat format = NumberFormat.getCurrencyInstance(Locale.getDefault());
        format.setCurrency(Currency.getInstance(currency));
        return format.format(d);
    }


}
