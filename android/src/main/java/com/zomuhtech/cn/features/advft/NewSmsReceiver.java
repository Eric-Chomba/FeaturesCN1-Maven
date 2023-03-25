package com.zomuhtech.cn.features.advft;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.Status;

import java.util.Objects;

public class NewSmsReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent){
        if(SmsRetriever.SMS_RETRIEVED_ACTION.equals(intent.getAction())){

            try {
                Bundle bundle = intent.getExtras();
                Status status = (Status) Objects.requireNonNull(bundle).get(SmsRetriever.EXTRA_STATUS);

                switch (Objects.requireNonNull(status).getStatusCode()) {
                    case CommonStatusCodes.SUCCESS:
                        String message = (String) bundle.get(SmsRetriever.EXTRA_SMS_MESSAGE);

                        String[] msgArray = Objects.requireNonNull(message).split(":");
                        String msgBody;
                        if (msgArray.length > 1) {
                            msgBody = msgArray[1].substring(0, 6);

                            //TODO del
                            //Log.e("MsgBody ", msgBody);

                            Intent k = new Intent("SMSReceived");
                            k.putExtra("STATUS", "000");
                            k.putExtra("SMSBody", msgBody);

                            context.sendBroadcast(k);
                        }
                        break;

                    case CommonStatusCodes.TIMEOUT:
                        //TODO del
                        //Log.e("Exc ", "Sorry. The SMS retrieval service timed out.");

                        Intent k = new Intent("SMSReceived");
                        k.putExtra("STATUS", "091");
                        k.putExtra("SMSBody", "Sorry. The SMS retrieval service timed out. " +
                                "Attempting to restart it.");

                        context.sendBroadcast(k);
                        break;
                }
            }catch (Exception e){
                //TODO del
                //Log.e("Exc ", "Sorry. Unable to retrieve the SMS.");
                Intent k = new Intent("SMSReceived");
                k.putExtra("STATUS", "092");
                k.putExtra("SMSBody", "Sorry. Unable to retrieve the SMS.");

                context.sendBroadcast(k);
            }
        }
    }
}

