package com.zomuhtech.cn.features.advft;

import android.Manifest;
import android.content.IntentFilter;
import android.content.Intent;
import android.content.BroadcastReceiver;
import android.content.Context;
import com.codename1.impl.android.AndroidNativeUtil;
import android.util.Log;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
//import android.widget.Toast;

public class AndSmsInterceptorImpl {

    //AndSmsListener listener;

    public void showRouteActivity() {
        /*startActivity(new Intent(AndroidNativeUtil.getActivity(), 
                MainActivity.class));*/

    }

    public void unbindSmsListener() {
        //AndSmsListener listener = new AndSmsListener();
        /*Log.d("Receiver ", "Unregistering receiver...");
        AndroidNativeUtil.getActivity().unregisterReceiver(listener);
        Log.d("Receiver ", "Receiver unregistered");*/
    }

    public void bindSmsListener() {
        /*if (AndroidNativeUtil.checkForPermission(Manifest.permission.RECEIVE_SMS,
                "We can automatically enter the SMS code for you")) {
            listener = new AndSmsListener();
            IntentFilter filter = new IntentFilter();
            filter.addAction("android.provider.Telephony.SMS_RECEIVED");
            //get current activity & register receiver
            AndroidNativeUtil.getActivity().registerReceiver(listener, filter);
            Log.d("Receiver ", "Receiver registered");
        }*/

        registerNewListener();

        //TODO del
        AppSignHelper appSignHelper = new AppSignHelper(AndroidNativeUtil.getActivity());
        //Toast.makeText(Activate.this, ""+appSignHelper.getAppSignatues(), Toast.LENGTH_SHORT).show();
        Log.d("Debug hashcode ", "" + appSignHelper.getAppSignatues());
    }

    private void registerNewListener() {
        SmsRetrieverClient smsRetriever = SmsRetriever.getClient(AndroidNativeUtil.getActivity());

        Task<Void> task = smsRetriever.startSmsRetriever();

        /*task.addOnSuccessListener(aVoid -> registerReceiver(receiver,
                new IntentFilter("SMSReceived")));*/
        task.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                AndroidNativeUtil.getActivity().registerReceiver(receiver,
                        new IntentFilter("SMSReceived"));
            }
        });

        /*task.addOnFailureListener(e -> {
            String error = e.toString();
            Log.e("Error ", error);
        });*/
        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                String error = e.toString();
                Log.e("Error ", error);
            }
        });

    }

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String smsBody = intent.getStringExtra("SMSBody");
            String status = intent.getStringExtra("STATUS");

            switch (status) {
                case "000":
                    AndroidNativeUtil.getActivity().unregisterReceiver(receiver);
                    Log.d("SMS Body 000 ", smsBody.trim());
                    com.zomuhtech.cn.features.advft.ReceiveSMSFt
                            .smsCallBack("SUCCESS:" + smsBody.trim());

                    break;
                case "091":
                    AndroidNativeUtil.getActivity().unregisterReceiver(receiver);
                    Log.d("SMS Body 091 ", smsBody);
                    break;
                case "092":
                    Log.d("SMS Body 092 ", smsBody);
                    break;
            }
        }
    };

    public boolean isSupported() {
        return true;
    }

}
