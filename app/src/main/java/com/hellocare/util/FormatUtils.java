package com.hellocare.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import hirondelle.date4j.DateTime;

/**
 * Created by admin on 21/11/2016.
 */

public class FormatUtils {

public  static String PATTERN_DATE_TIME = "yyyy-MM-dd HH:mm:ss";
    public  static String PATTERN_DATE = "yyyy-MM-dd";
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
        return output.format(d);}


}
