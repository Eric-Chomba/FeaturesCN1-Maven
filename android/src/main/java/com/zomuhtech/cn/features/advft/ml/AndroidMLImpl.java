package com.zomuhtech.cn.features.advft.ml;

import com.zomuhtech.ztandnativelib.MainActivity;

import android.os.Handler;
import android.os.Looper;
import android.content.Context;
import android.util.Log;
import com.codename1.impl.android.AndroidNativeUtil;

public class AndroidMLImpl {
    
    public void machineLearning(final String param) {
        
         new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Context context = AndroidNativeUtil.getActivity();
                new MainActivity().launchMlKit(context, param);
            }
        });
    }

    public boolean isSupported() {
        return true;
    }

}
