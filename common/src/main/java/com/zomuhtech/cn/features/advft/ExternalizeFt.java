/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zomuhtech.cn.features.advft;

import com.codename1.components.ToastBar;
import com.codename1.io.Storage;
import com.codename1.ui.Button;
import com.codename1.ui.Container;
import com.codename1.ui.Display;
import com.codename1.ui.Form;
import com.codename1.ui.Label;
import com.codename1.ui.TextArea;
import com.codename1.ui.TextField;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.table.TableLayout;
import com.zomuhtech.cn.features.procs.Proc;
import java.util.ArrayList;

/**
 *
 * @author Eric
 */
public class ExternalizeFt extends Form {

    Form form, prevForm;
    Proc proc;

    Container cntDet;

    public ExternalizeFt(Form form) {
        this.prevForm = form;
        proc = new Proc();
        Display.getInstance().callSerially(() -> {
            createUI();
        });
    }

    public void createUI() {
        form = proc.getForm("Externalization", prevForm);
        form.setLayout(new BorderLayout());

        Container cnt = new Container(BoxLayout.y());

        Container cntName = proc.getInputCnt();
        TextField tfName = proc.getInputTf("Name", TextArea.ANY);
        cntName.add(tfName);

        Container cntAge = proc.getInputCnt();
        TextField tfAge = proc.getInputTf("Age", TextArea.ANY);
        cntAge.add(tfAge);

        Container cntHobby = proc.getInputCnt();
        TextField tfHobby = proc.getInputTf("Hobby", TextArea.ANY);
        cntHobby.add(tfHobby);
        Button btn = new Button("Send", "btn");

        cnt.addAll(cntName, cntAge, cntHobby, btn);

        cntDet = new Container(BoxLayout.y(), "cntPar");

        form.add(BorderLayout.NORTH, cnt);

        readDetails();

        btn.addActionListener(e -> {
            String name = tfName.getText();
            String age = tfAge.getText();
            String hobby = tfHobby.getText();
            if (name.isEmpty()) {
                ToastBar.showErrorMessage("Enter name");
            } else if (age.isEmpty()) {
                ToastBar.showErrorMessage("Enter age");
            } else if (hobby.isEmpty()) {
                ToastBar.showErrorMessage("Enter hobby");
            } else {
                ExternalizeClass object = new ExternalizeClass(name, age, hobby);

                addDetails(object);
            }
        });

        form.show();
    }

    private void addDetails(ExternalizeClass detail) {
        java.util.List details = (java.util.List) Storage.getInstance()
                .readObject("UserDetails");

        if (details == null) {
            details = new ArrayList();
        }
        details.add(detail);

        Storage.getInstance().writeObject("UserDetails", details);

        readDetails();
    }

    private void readDetails() {

        form.removeComponent(cntDet);
        //form.revalidate();

        java.util.List<ExternalizeClass> prevDetails
                = (java.util.List<ExternalizeClass>) Storage.getInstance()
                        .readObject("UserDetails");

        //proc.printLine("Prev Details " + prevDetails);
        TableLayout tl = new TableLayout(1, 3);
        Container cntTitle = new Container(tl);

        cntTitle.add(tl.createConstraint().widthPercentage(40), new Label("Name", "lblInput"))
                .add(tl.createConstraint().widthPercentage(20), new Label("Age", "lblInput"))
                .add(tl.createConstraint().widthPercentage(40), new Label("Hobby", "lblInput"));
        cntDet.add(cntTitle);

        if (prevDetails != null) {
            for (ExternalizeClass detail : prevDetails) {

                TableLayout tl2 = new TableLayout(1, 3);
                Container cntVal = new Container(tl2);

                cntVal.add(tl2.createConstraint().widthPercentage(40),
                        new Label(detail.getName().trim(), "lblInput"))
                        .add(tl2.createConstraint().widthPercentage(20),
                                new Label(detail.getAge().trim(), "lblInput"))
                        .add(tl2.createConstraint().widthPercentage(40),
                                new Label(detail.getHobby().trim(), "lblInput"));

                cntDet.add(cntVal);
            }
        }
        form.add(BorderLayout.CENTER, cntDet);
        cntDet.setScrollableY(true);
        form.revalidate();
    }
}
