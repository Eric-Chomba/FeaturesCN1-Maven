/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zomuhtech.cn.features.advft;

import com.codename1.components.ToastBar;
import com.codename1.ext.filechooser.FileChooser;
import com.codename1.ui.Button;
import static com.codename1.ui.Component.CENTER;
import com.codename1.ui.Container;
import com.codename1.ui.Display;
import com.codename1.ui.Form;
import com.codename1.ui.Label;
import com.codename1.ui.TextArea;
import com.codename1.ui.TextField;
import com.codename1.ui.layouts.BorderLayout;
import static com.codename1.ui.layouts.BorderLayout.NORTH;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.FlowLayout;
import com.codename1.ui.table.TableLayout;
import com.zomuhtech.cn.features.procs.Proc;
import java.io.IOException;
import com.codename1.messaging.Message;
import com.codename1.ui.FontImage;
import com.codename1.ui.layouts.GridLayout;
import java.util.ArrayList;

/**
 *
 * @author Eric
 */
public class PhoneFt extends Form {

    Form form, prevForm;
    Proc proc;
    String filePath, mimeType;
    Label lblPath;
    ArrayList<Button> btnArr;

    public PhoneFt(Form form) {
        this.prevForm = form;
        this.btnArr = new ArrayList<>();
        proc = new Proc();
        Display.getInstance().callSerially(() -> {
            createUI();
        });

    }

    public void createUI() {
        form = proc.getForm("Phone", prevForm);
        form.setLayout(new BorderLayout());

        form.add(CENTER, getForm2());

        Button btnDial = new Button("DIAL", "btnNav");
        btnArr.add(btnDial);
        btnDial.addActionListener(ev -> {
            proc.changeBtnUIID(btnArr, btnDial);
            form.add(CENTER, getForm2());
            form.revalidate();
        });

        Button btnEmail = new Button("EMAIL", "btnNav");
        btnArr.add(btnEmail);
        btnEmail.addActionListener(ev -> {
            proc.changeBtnUIID(btnArr, btnEmail);
            form.add(CENTER, getForm3());
            form.revalidate();
        });

        Button btnSMS = new Button("SMS", "btnNav");
        btnArr.add(btnSMS);
        btnSMS.addActionListener(e -> {
            proc.changeBtnUIID(btnArr, btnSMS);
            form.add(CENTER, getForm1());
            form.revalidate();
        });

        //selected by default
        proc.changeBtnUIID(btnArr, btnDial);

        Container cnt = new Container(new GridLayout(1, 3));
        cnt.addAll(btnDial, btnEmail, btnSMS);

        form.add(NORTH, cnt);
        form.show();
    }

    private Form getForm1() {

        Form form1 = proc.getInputForm();

        Container cntPhone = proc.getInputCnt();
        TextField tfPhone = proc.getInputTf("Enter Phone", TextArea.PHONENUMBER);
        cntPhone.add(tfPhone);

        Container cntMsg = proc.getInputCnt();
        TextField tfMsg = proc.getInputTf("Enter Message", TextArea.ANY);
        cntMsg.add(tfMsg);

        Button btn = new Button("Send", "btn");
        btn.addActionListener(e -> {
            String phone = tfPhone.getText();
            String msg = tfMsg.getText();

            if (phone.length() == 0) {
                ToastBar.showErrorMessage("Enter Phone");
            } else if (msg.length() == 0) {
                ToastBar.showErrorMessage("Enter Message");
            } else {
                sendSMS(phone, msg);
            }
        });

        form1.add(cntPhone).add(cntMsg).add(btn);

        return form1;
    }

    private void sendSMS(String phone, String msg) {

        try {
            Display.getInstance().sendSMS(phone, msg, true);
            proc.showToast("Supports" + Display.getInstance().getSMSSupport(),
                    FontImage.MATERIAL_INFO_OUTLINE).show();

            switch (Display.getInstance().getSMSSupport()) {
                //Desktop & tablet
                case Display.SMS_NOT_SUPPORTED:
                    ToastBar.showErrorMessage("Unsupported on this device");
                    break;

                //Send in background - android false -> not to show SMS UI
                case Display.SMS_SEAMLESS:
                    Display.getInstance().sendSMS(phone, msg, false);
                    break;
                //Show SMS sending UI - Android, iOS, Windows
                case Display.SMS_INTERACTIVE:
                    Display.getInstance().sendSMS(phone, msg, true);
                    break;
                //Send in background & show SMS UI
                case Display.SMS_BOTH:
                    Display.getInstance().sendSMS(phone, msg, false);
                    break;
                default:
                    ToastBar.showInfoMessage(Display.getInstance().getSMSSupport() + " supported");
                    Display.getInstance().sendSMS(phone, msg, true);
                    break;
            }
        } catch (IOException e) {
            ToastBar.showErrorMessage(e.getMessage());
        }
    }

    private Form getForm2() {

        Form form2 = proc.getInputForm();

        Container cntPhone = proc.getInputCnt();
        TextField tfPhone = proc.getInputTf("Enter Phone", TextArea.PHONENUMBER);
        cntPhone.add(tfPhone);

        Button btn = new Button("Call", "btn");
        btn.addActionListener(e -> {
            String phone = tfPhone.getText();

            if (phone.length() == 0) {
                ToastBar.showErrorMessage("Enter Phone");
            } else {
                Display.getInstance().dial(phone);
            }
        });

        form2.add(cntPhone).add(btn);

        return form2;
    }

    private Form getForm3() {

        Form form3 = proc.getInputForm();

        TableLayout tLTo = new TableLayout(1, 2);
        Container cntTo = new Container(tLTo);
        Label lblTo = new Label("To", "lblInput");

        Container cntRecv = proc.getInputCnt();
        TextField tfRecv = proc.getInputTf("", TextArea.EMAILADDR);
        cntRecv.add(tfRecv);
        cntTo.add(tLTo.createConstraint().widthPercentage(20),
                FlowLayout.encloseCenterMiddle(lblTo));
        cntTo.add(tLTo.createConstraint().widthPercentage(80),
                FlowLayout.encloseCenterMiddle(cntRecv));

        TableLayout tLSubj = new TableLayout(1, 2);
        Container cntSubj = new Container(tLSubj);
        Label lblSubj = new Label("Subject", "lblInput");
        Container cntSubj_ = proc.getInputCnt();
        TextField tfSubj = proc.getInputTf("", TextArea.ANY);
        cntSubj_.add(tfSubj);
        cntSubj.add(tLSubj.createConstraint().widthPercentage(20),
                FlowLayout.encloseCenterMiddle(lblSubj));
        cntSubj.add(tLSubj.createConstraint().widthPercentage(80),
                FlowLayout.encloseCenterMiddle(cntSubj_));

        Container cntMsg = proc.getInputCnt();
        TextArea tfMsg = proc.getInputTf("Enter Message", TextArea.ANY);
        tfMsg.setSingleLineTextArea(false);
        tfMsg.setMaxSize(5000);
        tfMsg.setGrowByContent(true);
        tfMsg.setGrowLimit(4);
        cntMsg.add(tfMsg);

        tfMsg.addDataChangedListener((i1, i2) -> {
            cntMsg.revalidate();
        });

        Button btnBrw = new Button("Attach", "btnNav");
        lblPath = new Label("path", "lblInput");

        attachFile(btnBrw);

        Container cnt = new Container(BoxLayout.x());
        cnt.add(btnBrw).add(lblPath);

        Button btn = new Button("Send", "btn");
        btn.addActionListener(e -> {
            String to = tfRecv.getText();
            String subj = tfSubj.getText();
            String msg = tfMsg.getText();

            if (to.length() == 0) {
                ToastBar.showErrorMessage("Enter Recipient");
            } else if (subj.length() == 0) {
                ToastBar.showErrorMessage("Enter Subject");
            } else if (msg.length() == 0) {
                ToastBar.showErrorMessage("Enter Message");
            } else {
                sendEmail(to, subj, msg);
            }
        });

        form3.add(cntTo).add(cntSubj).add(cntMsg).add(cnt).add(btn);

        return form3;
    }

    private void attachFile(Button btnBrw) {

        btnBrw.addActionListener(e -> {

            if (FileChooser.isAvailable()) {
                //proc.getFileExtMime() */*
                FileChooser.showOpenDialog(proc.getFileExtMime(), ev -> {

                    if (ev == null) {
                        ToastBar.showErrorMessage("no file selected");
                    } else {
                        filePath = (String) ev.getSource();
                        String selFileExt = filePath.substring(filePath.length() - 3);
                        if (selFileExt.equals("ocx")) {
                            selFileExt = "docx";
                        }
                        if (selFileExt.equals("ptx")) {
                            selFileExt = "pptx";
                        }
                        if (selFileExt.equals("lsx")) {
                            selFileExt = "xlsx";
                        }
                        if (selFileExt.equals("peg")) {
                            selFileExt = "jpeg";
                        }

                        mimeType = proc.getMimeType(selFileExt);
                        proc.printLine("Path " + filePath);
                        proc.printLine("Ext " + selFileExt);
                        proc.printLine("Mime " + mimeType);

                        lblPath.setText(filePath);

                    }
                });
            }
        });
    }

    private void sendEmail(String to, String subject, String msg) {

        Message message = new Message(msg);
        //message.getAttachments().put(filePath, mimeType);
        message.setAttachment(filePath);
        message.setAttachmentMimeType(mimeType);
        //message.setAttachmentMimeType(Message.MIME_IMAGE_PNG);
        Display.getInstance().sendMessage(new String[]{to}, subject, message);
    }

}
