/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zomuhtech.cn.features.advft;

import com.codename1.ui.BrowserComponent;
import com.codename1.ui.Display;
import com.codename1.ui.Form;
import com.codename1.ui.layouts.BorderLayout;
import com.zomuhtech.cn.features.procs.Proc;

/**
 *
 * @author Eric
 */
public class Web extends Form {

    Form form, prevForm;
    Proc proc;

    public Web(Form form) {

        this.prevForm = form;
        proc = new Proc();
        Display.getInstance().callSerially(() -> {
            createUI();
        });
    }

    private void createUI() {
        form = proc.getForm("Web", prevForm);
        form.setLayout(new BorderLayout());
        BrowserComponent browser = new BrowserComponent();
        browser.setURL("https://www.codenameone.com");
        form.add(CENTER, browser);
        form.show();

    }

}
