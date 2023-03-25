/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zomuhtech.cn.features.advft;

import com.codename1.ui.Display;
import com.codename1.ui.Form;
import com.codename1.ui.Label;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.zt.designs.ZTDesigns;

/**
 *
 * @author Eric
 */
public class DevCN1LibFt extends Form{

    Form form, prevForm;

    public DevCN1LibFt(Form form) {

        this.prevForm = form;

        Display.getInstance().callSerially(() -> {
            createUI();
        });

    }

    public void createUI() {

        form = ZTDesigns.getForm("CN1 Lib Dev", prevForm);
        //form.setLayout(BoxLayout.y());
        form.add(new Label("Codename One library dev"));
        form.show();
    }

}
