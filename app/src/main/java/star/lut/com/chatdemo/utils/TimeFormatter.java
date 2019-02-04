package star.lut.com.chatdemo.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import star.lut.com.chatdemo.constants.ValueConstants;

public class TimeFormatter {
    private static  final SimpleDateFormat AM_PM_FORMAT = new SimpleDateFormat("HH:mm");

    public Long timeinmillies(String time){
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(AM_PM_FORMAT.parse(time));
            calendar.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR));
            calendar.set(Calendar.MONTH, Calendar.getInstance().get(Calendar.MONTH));
            calendar.set(Calendar.DAY_OF_MONTH, Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        }catch (ParseException e){
            e.printStackTrace();
        }

        if (calendar.getTimeInMillis() > System.currentTimeMillis())
            return calendar.getTimeInMillis();
        else
            return calendar.getTimeInMillis()+ValueConstants.TIME_ONE_DAY;
    }

    public Long timeinmilliesAccurate(String time){
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(AM_PM_FORMAT.parse(time));
            calendar.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR));
            calendar.set(Calendar.MONTH, Calendar.getInstance().get(Calendar.MONTH));
            calendar.set(Calendar.DAY_OF_MONTH, Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        }catch (ParseException e){
            e.printStackTrace();
        }

        return calendar.getTimeInMillis();
    }
}
