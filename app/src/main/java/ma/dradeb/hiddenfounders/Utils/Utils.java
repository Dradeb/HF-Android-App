package ma.dradeb.hiddenfounders.Utils;

import android.app.Service;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ServiceInfo;
import android.icu.text.DateFormat;
import android.icu.text.RelativeDateTimeFormatter;
import android.icu.text.SimpleDateFormat;
import android.net.ConnectivityManager;
import android.net.ParseException;
import android.support.v7.app.AppCompatActivity;

import java.util.Date;

/**
 * Created by Youness on 09/07/2017.
 */

public class Utils extends AppCompatActivity {


    public String fbDateToSimpleFormat(String fbDate){

        fbDate = fbDate.substring(0,fbDate.indexOf("T"));

        System.out.println(""+fbDate);
        SimpleDateFormat format2 = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat format1 = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();
        try {
            date = format1.parse(fbDate);
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        return  format2.format(date);
    }

    public static boolean isOnline(Context ctx)
    {
        ConnectivityManager manager = (ConnectivityManager)ctx.getSystemService(CONNECTIVITY_SERVICE);
        return manager.getActiveNetworkInfo() == null?false:true  ;

    }

    public static void saveDownloadAction(boolean state , Context ctx)
    {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(Constants.SHAREDPREF_DOWNLOAD_STATE,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("delete_after_download",state);
        editor.commit();

    }
    public static boolean getDownloadAction(Context ctx)
    {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(Constants.SHAREDPREF_DOWNLOAD_STATE,MODE_PRIVATE);
        return sharedPreferences.getBoolean("delete_after_download",false);

    }

}
