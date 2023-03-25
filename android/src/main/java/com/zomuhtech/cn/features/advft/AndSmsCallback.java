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
import com.codename1.components.ToastBar;
import com.codename1.util.FailureCallback;
import com.codename1.util.SuccessCallback;
import static com.codename1.ui.CN.*;
import com.codename1.ui.Display;

//should be internal class not public
class AndSmsCallback {

    static SuccessCallback<String> onSuccess;
    static FailureCallback onFail;

    public static void smsReceived(final String sms) {

        //ToastBar.showInfoMessage("Msg " + sms);
        com.zomuhtech.cn.features.advft.ReceiveSMSFt
                .smsCallBack("SUCCESS:" + sms);


        /*if (onSuccess != null) {
            final SuccessCallback<String> s = onSuccess;
            onSuccess = null;
            onFail = null;
            // AndSmsInterceptor.unbindSmsListener();
            
            Display.getInstance().callSerially(new Runnable() {
                @Override
                public void run() {
                    s.onSucess(sms);
                    com.zomuhtech.cn.features.advft.ReadSMSFt
                            .smsCallBack("SUCCESS:" + sms);
                }
            });
        }*/
    }

//public static void smsReceiveError(Exception err) {
    public void smsReceiveError(final Exception err) {

        //ToastBar.showInfoMessage("Err " + err);
        com.zomuhtech.cn.features.advft.ReceiveSMSFt
                .smsCallBack("FAIL:" + err.toString());


        /*if (onFail != null) {
            final FailureCallback f = onFail;
            onFail = null;
            //AndSmsInterceptor.unbindSmsListener();
            onSuccess = null;
            Display.getInstance().callSerially(new Runnable() {
                @Override
                public void run() {
                    f.onError(null, err, 1, err.toString());
                    com.zomuhtech.cn.features.advft.ReadSMSFt
                            .smsCallBack("FAIL:" + err.toString());
                }

            });
        } else {
            if (onSuccess != null) {
                // AndSmsInterceptor.unbindSmsListener();
                onSuccess = null;
            }
        }*/
    }
}
