/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zomuhtech.cn.features.advft;

import com.codename1.components.SpanLabel;
import com.codename1.components.ToastBar;
import com.codename1.system.NativeLookup;
import com.codename1.ui.Button;
import com.codename1.ui.Container;
import com.codename1.ui.Display;
import com.codename1.ui.Form;
import com.codename1.ui.TextField;
import com.codename1.ui.layouts.BorderLayout;
import static com.codename1.ui.layouts.BorderLayout.NORTH;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.FlowLayout;
import com.zomuhtech.cn.features.procs.Proc;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * GUI builder created Form
 *
 * @author Eric
 */
/*Expected BroadcastReceiver SMS format(<hash tag>Description message:6 numberscode .hashcode)
    hashcode - generated based on application id(main package name)
    sample

    <#>Code:123456 .Y7Swm0o0gkI 

 */
public class ReceiveSMSFt extends com.codename1.ui.Form {

    Form form, prevForm;
    Proc proc;
    public static TextField tfCode;

    public ReceiveSMSFt(Form form) {
        //this(com.codename1.ui.util.Resources.getGlobalResources());
        this.prevForm = form;
        proc = new Proc();

        Display.getInstance().callSerially(() -> {
            createUI();
        });
    }

    private void createUI() {
        form = proc.getForm("Receive SMS", prevForm);
        form.setLayout(new BorderLayout());

        form.add(NORTH, new SpanLabel("Expected BroadcastReceiver SMS "
                + "format(<hash tag>Description message:6 numbers code .hashcode)\n"
                + "    hashcode - generated based on application id(main package name)\n"
                + "    sample\n"
                + "\n"
                + "    <#>Verification Code:123456 .Y7Swm0o0gkI", "lblInput"));

        Container cnt = new Container(BoxLayout.y());
        tfCode = new TextField("", "Enter verification code");
        tfCode.setUIID("tf");
        tfCode.setConstraint(2);
        Button btnVerify = new Button("Verify", "btn");
        btnVerify.addActionListener(e -> {
            ToastBar.showInfoMessage("Sending verification code "
                    + tfCode.getText());
        });
        cnt.add(tfCode).add(btnVerify);
        form.add(CENTER, FlowLayout.encloseCenterMiddle(cnt));
        form.show();

        AndSmsInterceptor interceptor = NativeLookup
                .create(AndSmsInterceptor.class);
        interceptor.bindSmsListener();
    }

    public static void smsCallBack(String smsResp) {

        ArrayList<String> splitArr = new ArrayList<>();
        StringTokenizer arr = new StringTokenizer(smsResp, ":");
        while (arr.hasMoreElements()) {
            splitArr.add(arr.nextToken());
        }
        String[] smsArr = splitArr.toArray(new String[splitArr.size()]);

        switch (smsArr[0]) {
            case "SUCCESS":
                /*if (smsArr.length > 2) {

                    try {
                        int code = Integer.parseInt(smsArr[2].trim());
                        tfCode.setText("" + code);

                        ToastBar.showInfoMessage("Sending verification code "
                                + tfCode.getText());

                    } catch (NumberFormatException e) {
                        ToastBar.showErrorMessage("Unexpected SMS format received ");
                        //ToastBar.showErrorMessage("" + e);
                    }
                    Display.getInstance().callSerially(() -> {
                        AndSmsInterceptor interceptor = NativeLookup
                                .create(AndSmsInterceptor.class);
                        interceptor.unbindSmsListener();
                    });

                } else {
                    ToastBar.showErrorMessage("Unexpected SMS format received");
                }*/
                tfCode.setText("" + smsArr[2]);

                ToastBar.showInfoMessage("Sending verification code "
                        + tfCode.getText());
                break;

            case "FAIL":
                ToastBar.showErrorMessage(smsArr[1]);
                break;

        }
    }

    public ReceiveSMSFt(com.codename1.ui.util.Resources resourceObjectInstance) {
        //initGuiBuilderComponents(resourceObjectInstance);
    }

//////////////////////////////////////////////////////-- DON'T EDIT BELOW THIS LINE!!!


// <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initGuiBuilderComponents(com.codename1.ui.util.Resources resourceObjectInstance) {
        setLayout(new com.codename1.ui.layouts.FlowLayout());
        setInlineStylesTheme(resourceObjectInstance);
                setInlineStylesTheme(resourceObjectInstance);
        setTitle("ReadSMSFt");
        setName("ReadSMSFt");
    }// </editor-fold>

//-- DON'T EDIT ABOVE THIS LINE!!!
}
