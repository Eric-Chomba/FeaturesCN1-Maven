package com.zomuhtech.cn.features.advft.ml;

import com.zomuhtech.ztandnativelib.ml.barcodes.LivePreview;
import com.codename1.impl.android.AndroidNativeUtil;
import android.content.Context;
import android.util.Log;

public class AndroidBarcodeImpl {

    public String getBarcodeData(String param) {
        
        Context context = AndroidNativeUtil.getActivity();
        String resp =  LivePreview.getBarcodeData(context, param);
        Log.d("ScanData",resp);
        
        return resp;
    }

    public boolean isSupported() {
        return true;
    }

}
