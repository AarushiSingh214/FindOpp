package com.example.findopp;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class GeoLocation {

//    private static MapCallBack mapCallBack;
//    //MapCallBack mapCallBack;
//
//    public interface MapCallBack{
//        void getLatLongCallBack();
//    }
//
//    public void setMapCallBack(MapCallBack mapCallBack){
//        this.mapCallBack = mapCallBack;
//    }

    public static void getAddress(final String locationAddress, final Context context, Handler handler){
        Thread thread = new Thread(){
            @Override
            public void run() {
                Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                String result = null;
                try {
                    List addressList = geocoder.getFromLocationName(locationAddress, 1);
                    if(addressList != null && addressList.size() > 0){
                        Address address = (Address) addressList.get(0);
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append(address.getLatitude()).append("\n");
                        stringBuilder.append(address.getLongitude()).append("\n");
                        result = stringBuilder.toString();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }finally{
                    Message message = Message.obtain();
                    message.setTarget(handler);
                    if(result != null){
                        message.what = 1;
                        Bundle bundle = new Bundle();
                        Log.i("Geohandler", "lat/long: " + result.substring(result.indexOf("\n")));
                        bundle.putString("address", result);
                        message.setData(bundle);

//                        if(mapCallBack != null){
//                            mapCallBack.getLatLongCallBack();
//                        }
                    }
                    message.sendToTarget();
                }
            }
        };
        thread.start();
    }


}
