/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zomuhtech.cn.features.advft;

import com.codename1.components.SpanLabel;
import com.codename1.components.ToastBar;
import com.codename1.io.Log;
import com.codename1.io.Storage;
import com.codename1.io.Util;
import com.codename1.ui.Button;
import com.codename1.ui.Command;
import com.codename1.ui.Container;
import com.codename1.ui.Dialog;
import com.codename1.ui.Display;
import com.codename1.ui.FontImage;
import com.codename1.ui.Form;
import com.codename1.ui.Label;
import com.codename1.ui.TextArea;
import com.codename1.ui.TextField;
import com.codename1.ui.layouts.BorderLayout;
import static com.codename1.ui.layouts.BorderLayout.EAST;
import static com.codename1.ui.layouts.BorderLayout.NORTH;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.FlowLayout;
import com.codename1.ui.table.TableLayout;
import com.zomuhtech.cn.features.procs.Proc;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import com.codename1.io.Preferences;
import com.codename1.ui.layouts.GridLayout;
import java.util.ArrayList;

/**
 *
 * @author Eric
 */
public class StorageFt extends Form {

    Form form, prevForm;
    Proc proc;
    Command cmdAdd;
    TextField tfName, tfAge, tfW, tfSal;
    ArrayList<Button> btnArr;

    public StorageFt(Form form) {
        this.prevForm = form;
        this.btnArr = new ArrayList<>();
        proc = new Proc();
        Display.getInstance().callSerially(() -> {
            createUI();
        });
    }

    private void createUI() {

        form = proc.getForm("Local Storage", prevForm);
        form.setLayout(new BorderLayout());

        form.add(CENTER, getForm1());

        Button btnFile = new Button("Files", "btnNav");
        btnArr.add(btnFile);
        btnFile.addActionListener(e -> {
            proc.changeBtnUIID(btnArr, btnFile);
            form.add(CENTER, getForm1());
            form.revalidate();
        });

        Button btnPref = new Button("Preferences", "btnNav");
        btnArr.add(btnPref);
        btnPref.addActionListener(ev -> {
            proc.changeBtnUIID(btnArr, btnPref);
            //changeKey();
            //Change Preferences encryption key - replace with user new password
            //EncryptedStorage.install("321qriE");
            form.add(CENTER, getForm2());
            form.revalidate();
        });
        //selected by default
        proc.changeBtnUIID(btnArr, btnFile);
        
        Container cnt = new Container(new GridLayout(1, 2));
        cnt.add(btnFile).add(btnPref);

        form.add(NORTH, cnt);

        form.show();
    }

    private Form getForm1() {

        Form form1 = proc.getInputForm();
        form1.setUIID("cntPar");

        cmdAdd = Command.create("",
                proc.materialIcon(FontImage.MATERIAL_ADD), evt -> {
            TextField tf = new TextField("", "File Name", 20, TextField.ANY);
            TextArea body = new TextArea(5, 20);
            body.setHint("File Body");

            Command ok = new Command("OK");
            Command cancel = new Command("Cancel");
            Command result = Dialog.show("File Name",
                    BorderLayout.north(tf).add(CENTER, body), ok, cancel);
            if (ok == result) {
                try (OutputStream os = Storage.getInstance()
                        .createOutputStream(tf.getText());) {
                    os.write(body.getText().getBytes("UTF-8"));
                    createFileEntry(form1, tf.getText());
                    form1.getContentPane().animateLayout(250);
                } catch (IOException err) {
                    Log.e(err);
                }
            }
        });

        form.getToolbar().addCommandToRightBar(cmdAdd);

        for (String file : Storage.getInstance().listEntries()) {
            createFileEntry(form1, file);
        }
        return form1;
    }

    private void createFileEntry(Form f, String file) {

        Label lblFile = new Label(file, "lblInput");
        Button btnDel = new Button(proc.customIcon(FontImage.MATERIAL_DELETE, proc.colorBlue, 4));
        //btnDel.getAllStyles().setFgColor(0x15E7FF);
        Button btnView = new Button(proc.customIcon(FontImage.MATERIAL_OPEN_IN_NEW, proc.colorBlue, 4));
        //btnView.getAllStyles().setFgColor(0x15E7FF);
        //FontImage.setMaterialIcon(btnDel, FontImage.MATERIAL_DELETE);
        //FontImage.setMaterialIcon(btnView, FontImage.MATERIAL_OPEN_IN_NEW);

        Container cnt = BorderLayout.center(lblFile);
        int size = Storage.getInstance().entrySize(file);
        cnt.add(EAST, BoxLayout.encloseX(new Label(size + "bytes", "lblInput"),
                btnDel, btnView));

        btnDel.addActionListener(e -> {
            Storage.getInstance().deleteStorageFile(file);
            cnt.setY(f.getWidth());
            f.getContentPane().animateUnlayoutAndWait(150, 255);
            f.removeComponent(cnt);
            f.getContentPane().animateLayout(150);
        });

        btnView.addActionListener(e -> {
            try (InputStream is = Storage.getInstance().createInputStream(file);) {
                String s = Util.readToString(is, "UTF-8");
                Dialog.show(file, s, "OK", null);
            } catch (IOException err) {
                Log.e(err);
            }
        });

        f.add(cnt);
    }

    //Change Preferences encryption key
    /*private void changeKey() {
         EncryptedStorage.install("321qriE");
        /*try {
            proc.printLine("currPass "+Preferences.get("CurrPwd", null));
            EncryptedStorage.install(Preferences.get("CurrPwd", null)); //old key
            InputStream is = Storage.getInstance()
                    .createInputStream("CN1Preferences");
            byte[] data = Util.readInputStream(is);
            EncryptedStorage.install("321qirE"); //new key
            OutputStream out = Storage.getInstance()
                    .createOutputStream("CN1Preferences");
            out.write(data);
            out.close();
            
             Preferences.set("CurrPwd", "321qirE");

        } catch (IOException e) {
        }      
    }*/
    private Form getForm2() {

        form.getToolbar().removeCommand(cmdAdd);

        Form form2 = proc.getInputForm();
        form2.setUIID("cntPar");

        TableLayout tLName = new TableLayout(1, 2);
        Container cntName = new Container(tLName);
        Container cntName_ = proc.getInputCnt();
        tfName = proc.getInputTf("", TextArea.ANY);
        cntName_.add(tfName);
        cntName.add(tLName.createConstraint().widthPercentage(20),
                FlowLayout.encloseLeftMiddle(new Label("Name", "lblInput")));
        cntName.add(tLName.createConstraint().widthPercentage(80),
                FlowLayout.encloseCenterMiddle(cntName_));

        TableLayout tLAge = new TableLayout(1, 2);
        Container cntAge = new Container(tLAge);
        Container cntAge_ = proc.getInputCnt();
        tfAge = proc.getInputTf("40", TextArea.NUMERIC);
        cntAge_.add(tfAge);
        cntAge.add(tLAge.createConstraint().widthPercentage(20),
                FlowLayout.encloseLeftMiddle(new Label("Age", "lblInput")));
        cntAge.add(tLAge.createConstraint().widthPercentage(80),
                FlowLayout.encloseCenterMiddle(cntAge_));

        TableLayout tLW = new TableLayout(1, 2);
        Container cntW = new Container(tLW);
        Container cntW_ = proc.getInputCnt();
        tfW = proc.getInputTf("0.00", TextArea.DECIMAL);
        cntW_.add(tfW);
        cntW.add(tLW.createConstraint().widthPercentage(20),
                FlowLayout.encloseLeftMiddle(new Label("Weight", "lblInput")));
        cntW.add(tLW.createConstraint().widthPercentage(80),
                FlowLayout.encloseCenterMiddle(cntW_));

        TableLayout tLSal = new TableLayout(1, 2);
        Container cntSal = new Container(tLSal);
        Container cntSal_ = proc.getInputCnt();
        tfSal = proc.getInputTf("1000000", TextArea.NUMERIC);
        cntSal_.add(tfSal);
        cntSal.add(tLSal.createConstraint().widthPercentage(20),
                FlowLayout.encloseLeftMiddle(new SpanLabel("Annual\nSalary",
                        "lblInput")));
        cntSal.add(tLSal.createConstraint().widthPercentage(80),
                FlowLayout.encloseCenterMiddle(cntSal_));

        Button btn = new Button("Save", "btn");
        btn.addActionListener(e -> {
            String name = tfName.getText();
            String age = tfAge.getText();
            String weight = tfW.getText();
            String salary = tfSal.getText();

            if (name.length() == 0) {
                ToastBar.showErrorMessage("Enter Name");
            } else if (age.length() == 0) {
                ToastBar.showErrorMessage("Enter Age");
            } else if (weight.length() == 0) {
                ToastBar.showErrorMessage("Enter Weight");
            } else if (salary.length() == 0) {
                ToastBar.showErrorMessage("Enter Annual Salary");
            } else {
                try {
                    int age_ = Integer.valueOf(age);
                    double weight_ = Double.valueOf(weight);
                    long salary_ = Long.parseLong(salary);

                    saveData(form2, name, age_, weight_, salary_);
                } catch (NumberFormatException err) {
                    ToastBar.showErrorMessage("Enter valid details");
                }
            }
        });

        form2.add(cntName).add(cntAge).add(cntW).add(cntSal).add(btn);
        viewData(form2);

        return form2;
    }

    private void saveData(Form form2, String name, int age, double weight,
            long salary) {

        Preferences.set("name_", name);
        Preferences.set("age_", age);
        Preferences.set("weight_", weight);
        Preferences.set("salary_", salary);

        if (Preferences.get("name_", null) != null) {
            viewData(form2);
        }
    }

    private void viewData(Form form2) {

        //if (Preferences.get("name_", null) != null) {
        form2.add(new Label("Name " + Preferences.get("name_", null),"lblInput"));
        form2.add(new Label("Age " + Preferences.get("age_", 0),"lblInput"));
        form2.add(new Label("Weight " + Preferences.get("weight_", 0.0),"lblInput"));
        form2.add(new Label("Salary " + Preferences.get("salary_", (long) 0),"lblInput"));

        Button btnEdit = new Button("Edit", "btnNav");
        btnEdit.addActionListener(e -> {

            tfName.setText(Preferences.get("name_", null));
            tfAge.setText(String.valueOf(Preferences.get("age_", 0)));
            tfW.setText(String.valueOf(Preferences.get("weight_", 0.0)));
            tfSal.setText(String.valueOf(Preferences.get("salary_", (long) 0)));
        });

        Container cnt = new Container(new BorderLayout());
        cnt.add(BorderLayout.WEST, btnEdit);
        form2.add(cnt);
        form2.revalidate();
        //}

    }
}
