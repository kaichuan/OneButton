package org.piscatory.onebutton;

import android.app.ActivityManager;
import android.content.Context;
import android.net.ConnectivityManager;

import org.apache.http.conn.util.InetAddressUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.zip.GZIPInputStream;

public class Utils {

    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");

    public static String getLocalIpAddress() {

        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface interFace = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = interFace
                        .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && InetAddressUtils.isIPv4Address(inetAddress.getHostAddress())) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            // TODO: handle exception
        }

        return null;
    }

    public static boolean isMyServiceRunning(Class<?> serviceClass, Context ctx) {
        ActivityManager manager = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public static void setMobileDataStatus(Context context, boolean enabled){
        ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        //ConnectivityManager类
        Class<?> conMgrClass;
        //ConnectivityManager类中的字段
        Field iConMgrField;
        //IConnectivityManager类的引用
        Object iConMgr;
        //IConnectivityManager类
        Class<?> iConMgrClass;
        //setMobileDataEnabled方法
        Method setMobileDataEnabledMethod;
        try {
            //取得ConnectivityManager类
            conMgrClass = Class.forName(conMgr.getClass().getName());
            //取得ConnectivityManager类中的对象Mservice
            iConMgrField = conMgrClass.getDeclaredField("mService");
            //设置mService可访问
            iConMgrField.setAccessible(true);
            //取得mService的实例化类IConnectivityManager
            iConMgr = iConMgrField.get(conMgr);
            //取得IConnectivityManager类
            iConMgrClass = Class.forName(iConMgr.getClass().getName());
            //取得IConnectivityManager类中的setMobileDataEnabled(boolean)方法
            setMobileDataEnabledMethod = iConMgrClass.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
            //设置setMobileDataEnabled方法是否可访问
            setMobileDataEnabledMethod.setAccessible(true);
            //调用setMobileDataEnabled方法
            setMobileDataEnabledMethod.invoke(iConMgr, enabled);
        } catch (ClassNotFoundException e) {

            e.printStackTrace();
        } catch (NoSuchFieldException e) {

            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();

        } catch (NoSuchMethodException e)

        {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {

            e.printStackTrace();
        } catch (IllegalAccessException e) {

            e.printStackTrace();
        } catch (InvocationTargetException e)

        {

            e.printStackTrace();

        }

    }
    public static boolean getMobileDataStatus(Context context)

    {

        ConnectivityManager cm;

        cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        Class cmClass = cm.getClass();
        Boolean isOpen = false;
        try
        {

            Method method = cmClass.getMethod("getMobileDataEnabled", null);

            isOpen = (Boolean)method.invoke(cm, null);
        }catch(Exception e)
        {
            e.printStackTrace();
        }

        return isOpen;

    }
    public static void getIpAddress() {
        new Thread(new Runnable() {
            @Override
            public void run() {

            }
        }).start();
    }
    public static String formatDate(Date date){
        return simpleDateFormat.format(date);
    }

    public static InputStream decompressStream(InputStream input) throws IOException {
        PushbackInputStream pb = new PushbackInputStream( input, 2 ); //we need a pushbackstream to look ahead
        byte [] signature = new byte[2];
        pb.read( signature ); //read the signature
        pb.unread( signature ); //push back the signature to the stream
        if( signature[ 0 ] == (byte) 0x1f && signature[ 1 ] == (byte) 0x8b ) //check if matches standard gzip magic number
            return new GZIPInputStream( pb );
        else
            return pb;
    }
}
