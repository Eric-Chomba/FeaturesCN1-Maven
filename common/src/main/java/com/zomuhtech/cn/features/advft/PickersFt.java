/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zomuhtech.cn.features.advft;

import com.codename1.components.ToastBar;
import com.codename1.ui.Button;
import com.codename1.ui.Display;
import com.codename1.ui.Form;
import com.codename1.ui.Label;
import com.codename1.ui.layouts.BoxLayout;
import com.zomuhtech.cn.features.procs.Proc;
import com.codename1.ui.spinner.Picker;
import java.util.Date;

/**
 *
 * @author Eric
 */
public class PickersFt extends Form {
    
    Form form, prevForm;
    Proc proc;
    
    public PickersFt(Form form) {
        this.prevForm = form;
        proc = new Proc();
        Display.getInstance().callSerially(() -> {
            createUI();
        });
    }
    
    private void createUI() {
        form = proc.getForm("Pickers",prevForm);
        form.setLayout( BoxLayout.y());
        
        
        form.add(new Label("Date", proc.getLblInputUIID()));
        Picker datePicker = new Picker();
        datePicker.setType(Display.PICKER_TYPE_DATE);
        datePicker.setDate(new Date());
        form.add(datePicker);
        
        form.add(new Label("Time", proc.getLblInputUIID()));
        Picker timePicker = new Picker();
        timePicker.setType(Display.PICKER_TYPE_TIME);
        timePicker.setTime(10 * 60);
        form.add(timePicker);
        
        form.add(new Label("Date & Time", proc.getLblInputUIID()));
        Picker dateTimePicker = new Picker();
        dateTimePicker.setType(Display.PICKER_TYPE_DATE_AND_TIME);
        dateTimePicker.setDate(new Date());
        form.add(dateTimePicker);
        
        form.add(new Label("Strings", proc.getLblInputUIID()));
        Picker strPicker = new Picker();
        strPicker.setType(Display.PICKER_TYPE_STRINGS);
        strPicker.setStrings("A Game of Thrones", "A Clash of Kings",
                "A Dream of Spring");
        strPicker.setSelectedString("A Game of Thrones");
        form.add(strPicker);
        
        Button btn = new Button("Send", "btn");
        btn.addActionListener(e -> {
            String values = "Date " + datePicker.getText()
                    + "\nTime " + timePicker.getText() + "\nDate&Time "
                    + dateTimePicker.getText() + "\nString " + 
                    strPicker.getText();
            ToastBar.showInfoMessage(values);
        });
        form.add(btn);
        
        form.show();
    }
    
}
