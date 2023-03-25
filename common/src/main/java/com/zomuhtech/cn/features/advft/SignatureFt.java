/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zomuhtech.cn.features.advft;

import com.codename1.components.SignatureComponent;
import com.codename1.components.ToastBar;
import com.codename1.ui.Display;
import com.codename1.ui.Form;
import com.codename1.ui.Image;
import com.codename1.ui.Label;
import com.codename1.ui.TextField;
import com.codename1.ui.layouts.BoxLayout;
import com.zomuhtech.cn.features.procs.Proc;

/**
 *
 * @author Eric
 */
public class SignatureFt extends Form {

    Form form, prevForm;
    Proc proc;

    public SignatureFt(Form form) {
        this.prevForm = form;
        proc = new Proc();
        Display.getInstance().callSerially(() -> {
            createUI();
        });
    }

    private void createUI() {
        
        form = proc.getForm("Signature", prevForm);
        form.setLayout(BoxLayout.y());

        form.add(new Label("Name", proc.getLblInputUIID()));
        form.add(new TextField());
        form.add(new Label("Sign here", proc.getLblInputUIID()));
        SignatureComponent sign = new SignatureComponent();
        form.add(sign);

        Label lblSign = new Label();
        form.add(lblSign);
        sign.addActionListener(e -> {
            ToastBar.showInfoMessage("Signature changed");
            Image img = sign.getSignatureImage();
            lblSign.setIcon(img);

            form.revalidate();
        });
        form.show();
    }
}
