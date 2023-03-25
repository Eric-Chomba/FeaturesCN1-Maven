/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zomuhtech.cn.features.advft;

import com.codename1.components.ToastBar;
import com.codename1.properties.Property;
import com.codename1.ui.Button;
import static com.codename1.ui.Component.CENTER;
import com.codename1.ui.Container;
import com.codename1.ui.Display;
import com.codename1.ui.Form;
import com.codename1.ui.Label;
import com.codename1.ui.TextField;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.spinner.Picker;
import com.zomuhtech.cn.features.procs.Proc;
import java.util.Date;
import com.codename1.properties.PropertyBusinessObject;
import com.codename1.properties.PropertyIndex;
import com.codename1.ui.TextArea;

/**
 *
 * @author Eric
 */
public class PropertiesFt extends Form {

    Form form, prevForm;
    Proc proc;

    public PropertiesFt(Form form) {
        this.prevForm = form;

        proc = new Proc();
        Display.getInstance().callSerially(() -> {
            createUI();
        });

    }

    public void createUI() {
        form = proc.getForm("Properties", prevForm);
        form.setLayout(new BorderLayout());

        form.add(CENTER, getForm1());

        /*
        Button btnForm1 = new Button("Encapsulation", "btn");
        btnForm1.addActionListener(e -> {
            form.add(CENTER, getForm1());
            form.revalidate();
        });

        Button btnForm2 = new Button("UI Binding", "btn");
        btnForm2.addActionListener(e -> {
            form.add(CENTER, getForm2());
            form.revalidate();
        });

        Container cnt = new Container(BoxLayout.x());
        cnt.add(btnForm1).add(btnForm2);
        form.add(NORTH, cnt);*/
        form.show();
    }

    private Form getForm1() {
        Form form1 = proc.getInputForm();

        Container cntPic = proc.getInputCnt();
        form1.add(new Label("Date", proc.getLblInputUIID()));
        Picker datePicker = new Picker();
        datePicker.setType(Display.PICKER_TYPE_DATE);
        datePicker.setDate(new Date());
        cntPic.add(datePicker);

        Container cntSubj = proc.getInputCnt();
        TextField tfSubj = proc.getInputTf("Subject", TextArea.ANY);
        cntSubj.add(tfSubj);

        Container cntAtt = proc.getInputCnt();
        TextField tfAtt = proc.getInputTf("Attendance", TextArea.NUMERIC);
        cntAtt.add(tfAtt);

        Button btn = new Button("Submit", "btn");
        btn.addActionListener(e -> {
            Meeting meet = new Meeting();

            meet.when.set(datePicker.getDate());
            meet.subject.set(tfSubj.getText());
            if (tfAtt.getText().length() > 0) {
                meet.attendance.set(Integer.valueOf(tfAtt.getText()));
            }

            String resp = "Date : " + meet.when.get()
                    + "\nSubject : " + meet.subject.get()
                    + "\nAttendance : " + meet.attendance.get();

            ToastBar.showInfoMessage(resp);
        });

        form1.add(cntPic).add(cntSubj).add(cntAtt).add(btn);

        return form1;
    }

    public class Meeting implements PropertyBusinessObject {

        public final Property<Date, Meeting> when = new Property<>("when");
        public final Property<String, Meeting> subject
                = new Property<>("subject");
        public final Property<Integer, Meeting> attendance
                = new Property<>("attendance");

        private final PropertyIndex index = new PropertyIndex(this,
                "Meeting", when, subject, attendance);

        @Override
        public PropertyIndex getPropertyIndex() {
            return index;
        }

        @Override
        public String toString() {
            return index.toString();
        }

        /*@Override
        public boolean equals(Object obj){
            return object.getClass()=getClass()&&index.equals(((com.zomuhtech.cn.features.advft).obj).getPropertyIndex());
        }
        
        @Override
        public int hasCode(){
            return index.hashCode();
        }*/
    }

    private Form getForm2() {
        Form form2 = new Form(BoxLayout.y());
        form2.getToolbar().setUIID("tbar");
        return form2;
    }

}
