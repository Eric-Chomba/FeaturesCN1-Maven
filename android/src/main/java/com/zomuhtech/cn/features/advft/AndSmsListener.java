/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zomuhtech.cn.features.advft;

/**
 *
 * @author Eric
 */
import android.content.*;
import android.os.Bundle;
import android.telephony.*;

public class AndSmsListener extends BroadcastReceiver {
    @Override 
    public void onReceive(Context cxt, Intent intent){
        if(intent.getAction()
                .equals("android.provider.Telephony.SMS_RECEIVED")){
            
            Bundle bundle = intent.getExtras();
            SmsMessage[] msgs = null;
            
            if(bundle != null){
                try{
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    msgs = new SmsMessage[pdus.length];
                    
                    for(int j=0; j<msgs.length;j++){
                        msgs[j] = SmsMessage.createFromPdu((byte[])pdus[j]);
                        String msgBody = msgs[j].getMessageBody();
                        AndSmsCallback.smsReceived(msgBody);
                    }
                    
                }catch(Exception e){
                    //Log.e(e);
                    AndSmsCallback.smsReceived(""+e);
                }
            }
        }
    }
}
