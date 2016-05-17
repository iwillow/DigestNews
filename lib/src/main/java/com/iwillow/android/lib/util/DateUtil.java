package com.iwillow.android.lib.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by https://www.github.com/iwillow on 2016/5/12.
 */
public class DateUtil {


    public static Date getPreDay(Date presentDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(presentDate);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        presentDate = calendar.getTime();
        return presentDate;
    }


    public static Date getPreNDay(Date presentDate, int n) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(presentDate);
        calendar.add(Calendar.DAY_OF_MONTH, -n);
        presentDate = calendar.getTime();
        return presentDate;
    }

    public static Date getNextDay(Date presentDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(presentDate);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        presentDate = calendar.getTime();
        return presentDate;
    }

    public static String format(Date date, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }

    public static Date getPresentDate() {
        GregorianCalendar g = new GregorianCalendar();
        return g.getTime();
    }

}
