/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zomuhtech.cn.features.advft;

import com.codename1.components.ToastBar;
import com.codename1.ui.Form;
import com.codename1.notifications.LocalNotification;
import com.codename1.notifications.LocalNotificationCallback;
import com.codename1.ui.Button;
import com.codename1.ui.Display;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.FlowLayout;
import com.zomuhtech.cn.features.procs.Proc;

/**
 *
 * @author Eric
 */
public class LocalNotificationFt extends Form 
        implements LocalNotificationCallback {

    Form form, prevForm;
    Proc proc;

    public LocalNotificationFt(Form form) {

        this.prevForm = form;
        proc = new Proc();
        Display.getInstance().callSerially(() -> {
            createUI();
        });

    }

    public void createUI() {
        
        form = proc.getForm("Local Notification", prevForm);
        form.setLayout(new BorderLayout());

        Button btn = new Button("Notify", "btn");
        btn.addActionListener(e -> {
            LocalNotification ln = new LocalNotification();
            ln.setId("demo-note");
            ln.setAlertBody("Local Notification msg test");
            ln.setAlertTitle("Test!");

            ln.setAlertImage("/android.png");
            //ln.setForeground(true);
            ln.setAlertSound("/notification_sound_ln_tone.mp3");
            //ln.setAlertSound("notification_sound_ln_tone.mp3");
            Display.getInstance().scheduleLocalNotification(ln,
                    System.currentTimeMillis() + 10 * 1000,
                    LocalNotification.REPEAT_MINUTE);
        });

        form.add(CENTER, FlowLayout.encloseCenterMiddle(btn));

        form.show();
    }

    @Override
    public void localNotificationReceived(String notificationId) {
        proc.printLine("LocalNotID "+notificationId);
        ToastBar.showInfoMessage("Received local notification "
                + notificationId);
    }
}
