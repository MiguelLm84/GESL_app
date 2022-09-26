package com.example.geslapp.core.clases;

import android.content.Context;
import android.content.SharedPreferences;
import com.example.geslapp.BuildConfig;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class ConfigPreferences {

    public void createPreferences(Context context, String IP, String RESOURCES, String PROJECT) {

        SharedPreferences sharedpref = context.getSharedPreferences("CONFIG",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpref.edit();
        editor.putString("ip",IP);
        editor.putString("resources",RESOURCES);
        editor.putString("project",PROJECT);

        editor.apply();
    }

    public int getFirstTimeRun(Context context) {

        SharedPreferences sp = context.getSharedPreferences("CONFIG", Context.MODE_PRIVATE);
        int result, currentVersionCode = BuildConfig.VERSION_CODE;
        int lastVersionCode = sp.getInt("inicio", -1);
        if (lastVersionCode == -1) result = 0; else
            result = (lastVersionCode == currentVersionCode) ? 1 : 2;
        sp.edit().putInt("inicio", currentVersionCode).apply();
        return result;
    }

    public String getIP(Context context)
    {
        SharedPreferences sp = context.getSharedPreferences("CONFIG",Context.MODE_PRIVATE);
        return sp.getString("ip",null);
    }

    public String getRec(Context context) {

        SharedPreferences sp = context.getSharedPreferences("CONFIG",Context.MODE_PRIVATE);
        return sp.getString("resources",null);
    }

    public String getProject(Context context) {

        SharedPreferences sp = context.getSharedPreferences("CONFIG",Context.MODE_PRIVATE);
        return sp.getString("project",null);
    }

    public void setVTables(Context context) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd 'a las' HH:mm:ss z");
        String currentDateandTime = sdf.format(new Date());
        SharedPreferences sp = context.getSharedPreferences("CONFIG",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("vtables",currentDateandTime);
        editor.apply();
    }

    public String getVTables(Context context) {

        SharedPreferences sp = context.getSharedPreferences("CONFIG",Context.MODE_PRIVATE);
        return "Última acutalización de tablas: "+sp.getString("vtables","no hay actualizaciones");
    }

    public void setVApp(Context context, String vapp) {

        SharedPreferences sp = context.getSharedPreferences("CONFIG",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("vapp", vapp);
        editor.apply();
    }

    public String getVApp(Context context) {

        SharedPreferences sp = context.getSharedPreferences("CONFIG",Context.MODE_PRIVATE);
        return sp.getString("vapp","");
    }

    public void setCon(Context context,boolean online) {

        SharedPreferences sp = context.getSharedPreferences("CONFIG",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("online",online);
        editor.apply();
    }

    public boolean getCon(Context  context) {

        SharedPreferences sp = context.getSharedPreferences("CONFIG",Context.MODE_PRIVATE);
        return sp.getBoolean("online",false);
    }

    public String getLastCon(Context context) {

        SharedPreferences sp = context.getSharedPreferences("CONFIG",Context.MODE_PRIVATE);
        return sp.getString("lastcon","");
    }
    public void setLastCon(Context context) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd 'a las' HH:mm:ss z");
        String currentDateandTime = sdf.format(new Date());
        SharedPreferences sp = context.getSharedPreferences("CONFIG",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("lastcon",currentDateandTime);
        editor.apply();
    }

    public void setLastUpdate(Context context, long update) {

        SharedPreferences sp = context.getSharedPreferences("CONFIG",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putLong("lastupdate",update);
        editor.apply();
    }

    public long getLastUpdate(Context context) {

        SharedPreferences sp = context.getSharedPreferences("CONFIG",Context.MODE_PRIVATE);
        return sp.getLong("lastupdate",0);
    }

    public void setLastAppUpdate(Context context) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd 'a las' HH:mm:ss z");
        String currentDateandTime = sdf.format(new Date());
        SharedPreferences sp = context.getSharedPreferences("CONFIG",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("lastappupdate",currentDateandTime);
        editor.apply();
    }

    public String getLastAppUpdate(Context context) {

        SharedPreferences sp = context.getSharedPreferences("CONFIG",Context.MODE_PRIVATE);
        return "Última acutalización de la app: "+sp.getString("lastappupdate","primera versión");
    }

    public void setUserip(Context context,String user) {

        SharedPreferences sp = context.getSharedPreferences("CONFIG",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("user",user);
        editor.putString("Uip",getIPAddress(true));
        editor.apply();
    }

    public String getUsername(Context context) {

        String data;
        SharedPreferences sp = context.getSharedPreferences("CONFIG",Context.MODE_PRIVATE);
        data = sp.getString("user","");

        return data;
    }

    public String getUip(Context context) {

        SharedPreferences sp = context.getSharedPreferences("CONFIG",Context.MODE_PRIVATE);
        return sp.getString("Uip","");
    }

    private static String getIPAddress(boolean useIPv4) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress();
                        //boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                        boolean isIPv4 = sAddr.indexOf(':')<0;

                        if (useIPv4) {
                            if (isIPv4)
                                return sAddr;
                        } else {
                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%'); // drop ip6 zone suffix
                                return delim<0 ? sAddr.toUpperCase() : sAddr.substring(0, delim).toUpperCase();
                            }
                        }
                    }
                }
            }
        } catch (Exception ignored) { } // for now eat exceptions
        return "";
    }
}
