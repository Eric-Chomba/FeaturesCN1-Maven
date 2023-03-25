package com.zomuhtech.cn.features.advft.route;

import android.util.Log;
import com.codename1.impl.android.AndroidNativeUtil;

//import ztroute on map lib's MainActivity
//import com.ztroute.MainActivity;
//import com.ztroute.MapsActivity;
import com.zomuhtech.ztandnativelib.map.MapsActivity;

import android.os.Handler;
import android.os.Looper;
import android.content.Context;

public class AndNativeRouteImpl {

    //Launch android native activity & pass context(currently open CN1 form) & data
    public void showRoute(final String param) {
        //create looper to enable showing UI since invoking native is done by Worker Thread which runs in background
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Context context = AndroidNativeUtil.getActivity();
                new MapsActivity().launchMap(context, param);
            }
        });

        //or create UI Thread to enable showing UI since invoking native is done by Worker Thread which runs in background
        /*AndroidNativeUtil.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Context context = AndroidNativeUtil.getActivity();
                new MainActivity().launchForm(context, "This is message from CN1");
            }
        });*/
    }

    public boolean isSupported() {
        return true;
    }

}
