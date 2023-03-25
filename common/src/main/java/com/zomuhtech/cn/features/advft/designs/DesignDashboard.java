/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zomuhtech.cn.features.advft.designs;

import com.codename1.ui.Button;
import com.codename1.ui.Display;
import com.codename1.ui.Form;
import com.codename1.ui.Label;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.util.SwipeBackSupport;
import com.zomuhtech.cn.features.procs.Proc;
import java.util.ArrayList;

/**
 *
 * @author Eric
 */
public class DesignDashboard extends Form {

    Form form, prevForm;
    Proc proc;
    String filePath, mimeType;
    Label lblPath;
    ArrayList<Button> btnArr;

    public DesignDashboard(Form form) {
        this.prevForm = form;
        this.btnArr = new ArrayList<>();
        proc = new Proc();
        Display.getInstance().callSerially(() -> {
            createUI();
        });

    }

    public void createUI() {
        form = proc.getDesignForm("D", "Designs", prevForm, proc.colorTeal, proc.colorTeal);
        form.setLayout(new BorderLayout());
        //Container cntDesignBg = new Container(BoxLayout.y(), "cntDesignBg");
        Form cntDesignBg = new Form();
        cntDesignBg.getToolbar().hideToolbar();
        cntDesignBg.setUIID("cntDesignBg");
        cntDesignBg.setLayout(BoxLayout.y());

        Button btnDsg1 = proc.getInputBtn("Storage Drives");
        btnDsg1.addActionListener(e -> {
            new Design1(form).show();
        });

        Button btnDsg2 = proc.getInputBtn("Account");
        btnDsg2.addActionListener(e -> {
            new Design2(form).show();
        });

        Button btnDsg3 = proc.getInputBtn("Home Screen");
        btnDsg3.addActionListener(e -> {
            new Design3(form).show();
        });

        Button btnDsg5 = proc.getInputBtn("Media Player");
        btnDsg5.addActionListener(e -> {
            new Design5(form).show();
        });
        cntDesignBg.addAll(btnDsg1, btnDsg2, btnDsg3, btnDsg5);

        SwipeBackSupport.bindBack(cntDesignBg, (args) -> {
            return prevForm.getComponentForm();
        });
        form.add(CENTER, cntDesignBg);
        form.show();
    }

}
