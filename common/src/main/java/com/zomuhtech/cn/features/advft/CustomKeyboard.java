/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zomuhtech.cn.features.advft;

import com.codename1.components.SpanLabel;
import com.codename1.ui.Button;
import com.codename1.ui.Container;
import com.codename1.ui.Display;
import com.codename1.ui.FontImage;
import com.codename1.ui.Form;
import com.codename1.ui.Label;
import com.codename1.ui.TextField;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.FlowLayout;
import com.codename1.ui.layouts.GridLayout;
import com.codename1.ui.util.UITimer;
import com.zomuhtech.cn.features.procs.Proc;
import java.util.ArrayList;

/**
 *
 * @author Eric
 */
public class CustomKeyboard extends Form {

    Form form, prevForm;
    Proc proc;
    ArrayList<Button> btnKeyArr;
    String input;

    ArrayList<Label> lblArr;
    int inputCount = 0;

    ArrayList<String> inputArr;
    StringBuilder sbInput;

    public CustomKeyboard(Form form) {
        this.prevForm = form;
        proc = new Proc();
        this.btnKeyArr = new ArrayList<>();
        this.lblArr = new ArrayList<>();
        this.inputArr = new ArrayList<>();
        this.sbInput = new StringBuilder();

        Display.getInstance().callSerially(() -> {
            createUI();
        });

    }

    public void createUI() {
        form = proc.getForm("Custom Keyboard", prevForm);
        form.setLayout(new BorderLayout());

        Container cntKeyboardPar = new Container(new GridLayout(1, 1));
        cntKeyboardPar.setScrollableY(true);

        Container cntKeyboard = new Container(BoxLayout.y());

        cntKeyboard.add(FlowLayout.encloseCenterMiddle(
                new SpanLabel("Please enter your secure pin\nto proceed",
                        "lblPinHint")));
        Container cntPin = new Container(new GridLayout(1, 4), "cntPin");

        for (int l = 1; l < 5; l++) {
            Container cntLblNum = new Container(BoxLayout.y(), "cntLblNum");
            Label lblNum = new Label("0", "lblNum");
            cntLblNum.add(lblNum);
            lblArr.add(lblNum);
            cntPin.add(cntLblNum);
        }

        cntKeyboard.add(FlowLayout.encloseCenterMiddle(cntPin));

        Container cntKeys = new Container(new GridLayout(4, 3));

        int size = 3;
        if (Display.getInstance().isTablet()
                && Display.getInstance().getDeviceDensity() < 40) {
            size = 2;
        }
        for (int k = 1; k < 13; k++) {

            Container cntBtnKey = new Container(BoxLayout.y(), "cntBtnKey");
            Button btnKey = new Button("", "btnKey");
            switch (k) {
                case 10:
                    btnKey.setIcon(proc.customIcon(FontImage.MATERIAL_DELETE_SWEEP,
                            proc.colorBlue, size));
                    btnKey.setName("btnClear");
                    break;
                case 11:
                    btnKey.setText("0");
                    btnKey.setName("btn0");
                    break;
                case 12:
                    btnKey.setIcon(proc.customIcon(FontImage.MATERIAL_BACKSPACE,
                            proc.colorBlue, size));
                    btnKey.setName("btnDel");

                    break;
                default:
                    btnKey.setText("" + k);
                    btnKey.setName("btn" + k);
                    break;
            }
            btnKeyArr.add(btnKey);
            readInput(btnKey);

            if (btnKey.getName().equals("btnDel")
                    || btnKey.getName().equals("btnClear")) {
                btnKey.setUIID("btnKeyDel");
            }

            cntBtnKey.add(FlowLayout.encloseCenterMiddle(btnKey));
            cntBtnKey.setLeadComponent(btnKey);
            cntKeys.add(FlowLayout.encloseCenterMiddle(cntBtnKey));
        }

        cntKeyboard.add(cntKeys);
        TextField tfFill = new TextField();
        tfFill.setUIID("tfFill");
        cntKeyboard.add(tfFill);
        tfFill.setVisible(false);
        cntKeyboardPar.add(FlowLayout.encloseCenterMiddle(cntKeyboard));

        //form.add(NORTH, FlowLayout.encloseCenterMiddle(new Label("Top Label")));
        form.add(CENTER, cntKeyboardPar);
        //form.add(SOUTH, FlowLayout.encloseCenterMiddle(new Label("Bottom Label")));

        form.show();
    }

    private void readInput(Button btnKey) {

        btnKey.addActionListener(evt -> {

            for (int b = 0; b < btnKeyArr.size(); b++) {

                if (btnKeyArr.get(b).getName().equals(btnKey.getName())) {

                    //proc.printLine("btnName=" + btnKey.getName());
                    switch (btnKey.getName()) {
                        case "btn0":
                            input = "0";
                            break;
                        case "btnClear":
                            clearInput();
                            break;
                        case "btnDel":
                            delInput();
                            break;
                        default:
                            input = btnKey.getText();
                            break;
                    }

                    if (!btnKey.getName().equals("btnDel")
                            && !btnKey.getName().equals("btnClear")
                            && inputCount < 4) {
                        //set inputs
                        inputArr.add(input);
                        //proc.printLine("Input=" + input);

                        for (int n = 0; n < lblArr.size(); n++) {
                            lblArr.get(inputCount).setText(input);
                            lblArr.get(inputCount).setUIID("lblNumSel");
                            form.revalidate();
                        }
                        inputCount++;
                        //proc.printLine("InputCount=" + inputCount);
                        if (inputCount == 4) {
                            //set 1 second delay to display last input
                            new UITimer(() -> {
                                for (String val : inputArr) {
                                    sbInput.append(val);
                                }
                                proc.showToast("Sending..." + sbInput.toString(),
                                        FontImage.MATERIAL_INFO_OUTLINE).show();
                                //clear inputs
                                for (int n = 0; n < lblArr.size(); n++) {
                                    lblArr.get(n).setText("0");
                                    lblArr.get(n).setUIID("lblNum");
                                    form.revalidate();
                                }

                                inputCount = 0;
                                sbInput = new StringBuilder();
                                inputArr.clear();
                            }).schedule(1000, false, form);
                        }
                    }
                }
            }
        });
    }

    private void clearInput() {

        inputCount = 0;
        inputArr.clear();

        for (int n = 0; n < lblArr.size(); n++) {
            lblArr.get(n).setText("0");
            lblArr.get(n).setUIID("lblNum");
            form.revalidate();
        }
    }

    private void delInput() {

        if (inputCount > 0) {
            inputCount--;
        }
        if (inputCount >= 0) {
            //proc.printLine("InputCountDel1=" + inputCount);
            if (inputArr.size() > 0) {
                inputArr.remove(inputCount);
            }
            lblArr.get(inputCount).setText("0");
            lblArr.get(inputCount).setUIID("lblNum");

            form.revalidate();
            //proc.printLine("InputCountDel2=" + inputCount);
        }
    }
}
