package edu.fjnu.cse.lostandfound.tools;

/**
 * Created by zspmh on 2017-6-15.
 */

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Md5 {

    public static String digest(byte[] data){
        StringBuffer sb=new StringBuffer();
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(data);
            byte[] result=digest.digest();
            for (int i = 0; i < result.length; i++) {
                int hi=((result[i]>>4)&0x0f);
                int lo=(result[i]&0x0f);
                sb.append(hi>9?(char)((hi-10)+'a'):(char)(hi+'0'));
                sb.append(lo>9?(char)((lo-10)+'a'):(char)(lo+'0'));
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return sb.toString().toUpperCase();
    }

    public static void main(String[] args) {
        //String a = "123456";
        //System.out.println(Md5.digest(a.getBytes()));

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String dateStr = sdf.format(date);
        System.out.println(dateStr);
        try {
            Date myDate = sdf.parse("2016-02-15");
            Calendar cal = Calendar.getInstance();
            cal.setTime(myDate);
            System.out.println(cal.getTime().toString());
            System.out.println(myDate.getYear()+1990);
            System.out.println(myDate.getMonth()+1);
            System.out.println(myDate.getDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        /*String str="2010-5-27";
        SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = sdf.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        System.out.println(calendar.getTime());*/
    }

}

