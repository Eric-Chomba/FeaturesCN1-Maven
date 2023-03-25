/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zomuhtech.cn.features.advft;

import com.codename1.ui.Button;
import com.codename1.ui.Container;
import com.codename1.ui.Form;
import com.codename1.ui.TextArea;
import com.codename1.ui.TextField;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.zomuhtech.cn.features.procs.Proc;
import com.codename1.fingerprint.Fingerprint;
import com.codename1.ui.Display;
import com.codename1.ui.FontImage;
import com.codename1.ui.Image;
import com.codename1.ui.layouts.FlowLayout;
import com.codename1.ui.layouts.LayeredLayout;
import com.codename1.util.EasyThread;
import com.zomuhtech.cn.features.procs.CustomSizedCmp;

/**
 *
 * @author Eric
 */
public class FingerprintAuthFt extends Form {

    Form form, prevForm;
    Proc proc;

    public FingerprintAuthFt(Form form) {

        this.prevForm = form;
        proc = new Proc();
        /*Display.getInstance().callSerially(() -> {
            createUI();
        });*/
        EasyThread e = EasyThread.start("UIThread");
        e.run(() -> {
            createUI();
        });
        //e.run((success)->success.onSuccess(getResp()),(res)->createUI());

    }

    private String getResp() {
        return null;
    }

    public void createUI() {
        form = proc.getForm("Fingerprint Authentication", prevForm);
        form.setLayout(new BorderLayout());

        Container cntFields = new Container(BoxLayout.y());

        Container cntUser = proc.getInputCnt();
        TextField tfUser = proc.getInputTf("Enter Username", TextArea.ANY);
        cntUser.add(tfUser);

        Container cntPwd = proc.getInputCnt();
        TextField tfPwd = proc.getInputTf("Enter Password", TextArea.PASSWORD);
        cntPwd.add(tfPwd);

        Container cntBtn = new Container(new LayeredLayout());

        Button btn = proc.getInputBtn("Proceed");

        Container cntFillWidth = new CustomSizedCmp(Display.getInstance().getDisplayWidth(), 1);
        cntFillWidth.setUIID("cntFillWidth");
        cntBtn.addAll(cntFillWidth, btn);

        cntFields.addAll(cntUser, cntPwd, cntBtn);

        Image img = proc.materialPrintIcon(FontImage.MATERIAL_FINGERPRINT)
                .toImage();
        Container cntPrint = new Container(BoxLayout.x());
        cntPrint.add(img);

        Container cnt = new Container(BoxLayout.y());
        cnt.add(cntFields).add(FlowLayout.encloseCenterMiddle(cntPrint));
        form.add(CENTER, FlowLayout.encloseCenterMiddle(cnt));

        cntPrint.setVisible(false);

        btn.addActionListener(e -> {
            String user = tfUser.getText();
            String pwd = tfPwd.getText();

            if (user.length() == 0) {
                proc.showToast("Enter Username", FontImage.MATERIAL_ERROR).show();
            } else if (pwd.length() == 0) {
                proc.showToast("Enter Password", FontImage.MATERIAL_ERROR).show();
            } else {

                //Check hardware availability
                if (Fingerprint.isAvailable()) {
                    cntFields.setVisible(false);
                    cntPrint.setVisible(true);
                    printAuth(user, pwd);
                } else {
                    proc.showToast("Finger print authentication not supported",
                            FontImage.MATERIAL_ERROR).show();
                }
            }
        });

        form.show();
    }

    private void printAuth(String user, String pwd) {

        Fingerprint.scanFingerprint("User your finger print to verify", value -> {

            proc.showToast("Authentication Successful", FontImage.MATERIAL_INFO_OUTLINE).show();

            savePassword(user, pwd);

        }, (sender, err, errCode, errMsg) -> {
            proc.showToast("Authentication failed", FontImage.MATERIAL_ERROR).show();
        });
    }

    private void savePassword(String user, String pwd) {
        Fingerprint.addPassword("Saving account details to keystore", user, pwd)
                .onResult((success, err) -> {

                    if (err != null) {
                        proc.showToast("Failed to save details to keystore " + err.getMessage(),
                                FontImage.MATERIAL_ERROR).show();
                    } else {
                        proc.showToast("Details saved successfully to keystore",
                                FontImage.MATERIAL_INFO_OUTLINE).show();

                        retrievePwd(user);
                    }
                });
    }

    private void retrievePwd(String user) {
        Fingerprint.getPassword("Getting account details", user)
                .onResult((password, err) -> {
                    if (err != null) {
                        proc.showToast("Failed to get details " + err.getMessage(),
                                FontImage.MATERIAL_ERROR).show();
                    } else {
                        proc.showToast("Password = " + password,
                                FontImage.MATERIAL_INFO_OUTLINE).show();
                    }

                });
    }

    private void deletePwd(String user) {
        Fingerprint.getPassword("Getting account details", user)
                .onResult((res, err) -> {
                    if (err != null) {
                        proc.showToast("Failed to delete details " + err.getMessage(),
                                FontImage.MATERIAL_ERROR).show();
                    } else {
                        proc.showToast("Delete successful", FontImage.MATERIAL_ERROR).show();
                    }

                });
    }
}
