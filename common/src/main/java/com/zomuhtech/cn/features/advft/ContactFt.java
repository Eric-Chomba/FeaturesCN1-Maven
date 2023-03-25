/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zomuhtech.cn.features.advft;

import com.codename1.components.InfiniteProgress;
import com.codename1.components.MultiButton;
import com.codename1.components.SpanLabel;
import com.codename1.components.ToastBar;
import com.codename1.ui.Command;
import com.codename1.ui.Display;
import com.codename1.ui.FontImage;
import com.codename1.ui.Form;
import com.codename1.ui.Image;
import com.codename1.ui.layouts.BoxLayout;
import com.zomuhtech.cn.features.procs.Proc;
import com.codename1.contacts.Contact;
import com.codename1.contacts.ContactsManager;
import com.codename1.io.Util;
import com.codename1.ui.Button;
import com.codename1.ui.Component;
import com.codename1.ui.Container;
import com.codename1.ui.Dialog;
import com.codename1.ui.Label;
import com.codename1.ui.Sheet;
import com.codename1.ui.TextArea;
import com.codename1.ui.TextField;
import com.codename1.ui.events.ScrollListener;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.FlowLayout;

/**
 *
 * @author EChomba
 */
public class ContactFt extends Form {

    Form form, prevForm;
    Proc proc;
    long lastScroll = 0;

    public ContactFt(Form form) {

        this.prevForm = form;
        proc = new Proc();
        Display.getInstance().callSerially(() -> {
            createUI();
        });
    }

    private void createUI() {
        form = proc.getForm("Contact Search", prevForm);
        form.setLayout(BoxLayout.y());

        Command addCmd = Command.create("",
                proc.materialIcon(FontImage.MATERIAL_ADD),
                evt -> {
                    proc.printLine("src1 " + evt.getSource());
                    addContact();
                });

        form.getToolbar().addCommandToRightBar(addCmd);

        viewContacts();

        form.show();
    }

    private void viewContacts() {
        //Image img = null;
        /*try {
            img = Image.createImage("/hse_sub_50.png");
        } catch (IOException e) {
        }*/
        Image img = proc.sideMenuIcon(FontImage.MATERIAL_PERSON_SEARCH);
        int size = Display.getInstance().convertToPixels(5);
        final Image conImg = img.scaledWidth(size);

        form.add(FlowLayout.encloseCenterMiddle(new InfiniteProgress()));

        Display.getInstance().scheduleBackgroundTask(() -> {
            //retrieved fields - include id,Fullname,picture,numbers,email,address
            /*(Contact[] cont = Display.getInstance()
                    .getAllContacts(true, true, false, true, false, false);*/
            Contact[] cont = ContactsManager
                    .getContacts(true, true, false, true, false, false);
            Display.getInstance().callSerially(() -> {
                form.removeAll();

                for (Contact c : cont) {

                    MultiButton btn = new MultiButton();
                    btn.setTextLine1(c.getDisplayName());
                    btn.setTextLine2(c.getPrimaryPhoneNumber());
                    btn.setUIIDLine1("lblInput");
                    btn.setUIIDLine2("lblInput");

                    btn.putClientProperty("id", c.getId());

                    btn.addActionListener(e -> {
                        /*ToastBar.showInfoMessage("Name " + btn.getTextLine1()
                                + "\nPhone " + btn.getTextLine2());*/
                        contactsOpt(String.valueOf(btn.getClientProperty("id")),
                                btn.getTextLine1(),
                                btn.getTextLine2());
                    });
                    form.add(btn);


                    /* Image pic = c.getPhoto();

                    if (pic != null) {
                        //btn.setIcon(fill(pic, conImg.getWidth(),
                                //conImg.getHeight()));
                        pic = c.getPhoto().scaled(size, size);
                        btn.setIcon(pic);
                    } else {
                        btn.setIcon(conImg);
                    }*/
                    Display.getInstance().scheduleBackgroundTask(() -> {

                        Contact contImg = ContactsManager
                                .getContactById(c.getId(), false, true, false,
                                        false, false);
                        Display.getInstance().callSerially(() -> {
                            Image pic = contImg.getPhoto();
                            if (pic != null) {
                                btn.setIcon(pic.fill(size, size));
                                btn.revalidate();
                            } else {
                                btn.setIcon(conImg);
                            }
                        });
                    });

                }
                //form.revalidate();
                form.getContentPane().animateLayout(150);
                //form.revalidate();
            });
        });

        /*SCROLL PERFORMANCE ENHANCEMENT*/
        //dont do anything while scrolling/animating in place
        //Sleeps when user interacts with UI & loads images if user hasnt touched UI
        long idle = System.currentTimeMillis() - lastScroll;
        while (idle < 1500 || form.getAnimationManager().isAnimating()) {
            /*|| scrollY != form.getScrollY()*/
            //scrollY = form.getScrollY();
            Util.sleep(Math.min(1500, Math.max(100, 2000 - ((int) idle))));
            idle = System.currentTimeMillis() - lastScroll;
        }

        //update lastScroll variable when user interacts/touches UI
        form.addPointerDraggedListener(e -> lastScroll = System.currentTimeMillis());

        form.addScrollListener(new ScrollListener() {
            int initial = -1;

            @Override
            public void scrollChanged(int scrollX, int scrollY, int oldScrollX,
                    int oldSrollY) {
                //scroll is sensitive on devices
                if (initial < 0) {
                    initial = scrollY;
                }
                lastScroll = System.currentTimeMillis();

            }
        });

        Display.getInstance().callSerially(() -> {
            searchContact();
        });

    }

    private void searchContact() {

        form.getToolbar().addSearchCommand(e -> {
            proc.printLine("scr2 " + e.getSource());
            String text = (String) e.getSource();
            if (text == null || text.length() == 0) {
                //clear search
                for (Component cmp : form.getContentPane()) {
                    cmp.setHidden(false);
                    cmp.setVisible(true);
                }
                form.getContentPane().animateLayout(150);
            } else {
                text = text.toLowerCase();
                for (Component cmp : form.getContentPane()) {

                    MultiButton btn = (MultiButton) cmp;
                    String line1 = btn.getTextLine1();
                    String line2 = btn.getTextLine2();
                    btn.setUIIDLine1("lblInput");
                    btn.setUIIDLine2("lblInput");

                    boolean show = line1 != null && line1.toLowerCase()
                            .indexOf(text) > -1 || line2 != null
                            && line2.toLowerCase().indexOf(text) > -1;
                    btn.setHidden(!show);
                    btn.setVisible(show);

                    ((MultiButton) btn).addActionListener(ev -> {
                        ToastBar.showInfoMessage(btn.getTextLine1());
                    });

                }
                form.getContentPane().animateLayout(150);
            }
        }, 4);
    }

    private void addContact() {

        Container pCnt1 = new Container(BoxLayout.y(), "cntProg");
        pCnt1.setScrollableY(true);

        TextField txtProg = new TextField();
        txtProg.setEnabled(false);
        txtProg.setUIID("txtProgMsg");
        //txtProg.setText(msg);
        pCnt1.add(txtProg);

        Label lblFName = new Label("First name", "lblInput");
        TextField tfFName = new TextField();
        pCnt1.add(lblFName).add(tfFName);

        Label lblFamily = new Label("Family Name", "lblInput");
        TextField tfFamily = new TextField();
        pCnt1.add(lblFamily).add(tfFamily);

        Label lblOffPhone = new Label("Office Phone", "lblInput");
        TextField tfOffPhone = new TextField();
        tfOffPhone.setConstraint(TextArea.PHONENUMBER);
        pCnt1.add(lblOffPhone).add(tfOffPhone);

        Label lblHomePhone = new Label("Home Phone", "lblInput");
        TextField tfHomePhone = new TextField();
        tfHomePhone.setConstraint(TextArea.PHONENUMBER);
        pCnt1.add(lblHomePhone).add(tfHomePhone);

        Label lblMobPhone = new Label("Mobile Phone", "lblInput");
        TextField tfMobPhone = new TextField();
        tfMobPhone.setConstraint(TextArea.PHONENUMBER);
        pCnt1.add(lblMobPhone).add(tfMobPhone);

        Label lblEmail = new Label("Email", "lblInput");
        TextField tfEmail = new TextField();
        pCnt1.add(lblEmail).add(tfEmail);

        Container cnt = new Container(new BorderLayout());
        Button btnAdd = new Button("Save", "btn");
        Button btnCancel = new Button("Cancel", "btn");

        cnt.add(BorderLayout.WEST, btnCancel).add(BorderLayout.EAST, btnAdd);
        pCnt1.add(cnt);

        Dialog d = new Dialog(new BorderLayout());

        Form addForm = new Form(BoxLayout.x());
        addForm.setScrollableY(false);
        addForm.setUIID("progDForm");
        addForm.getToolbar().hideToolbar();
        addForm.add(FlowLayout.encloseCenterMiddle(pCnt1));
        d.addComponent(BorderLayout.CENTER, addForm);

        btnAdd.addActionListener(e -> {
            String fName = tfFName.getText();
            String famName = tfFamily.getText();
            String offPhone = tfOffPhone.getText();
            String homePhone = tfHomePhone.getText();
            String mobile = tfMobPhone.getText();
            String email = tfEmail.getText();
            if (fName.length() == 0) {
                ToastBar.showErrorMessage("Enter firstname");
            } else if (mobile.length() == 0) {
                ToastBar.showErrorMessage("Enter Cell phone");
            } else {
                String success = ContactsManager
                        .createContact(fName, famName, mobile, homePhone,
                                offPhone, email);
                if (success != null) {
                    ToastBar.showInfoMessage("Saved successfully");
                    d.dispose();
                    viewContacts();
                }
            }
        });

        btnCancel.addActionListener(e -> {
            d.dispose();
        });
        //d.showPacked(BorderLayout.CENTER, false);
        d.show();
    }

    private void contactsOpt(String contId, String name, String phone) {

        proc.printLine("ID " + contId);
        Container pCnt1 = new Container(BoxLayout.y(), "cntProg");
        pCnt1.setScrollableY(true);

        //TextField txtProg = new TextField();
        ///txtProg.setEnabled(false);
        //txtProg.setUIID("txtProgMsg");
        //txtProg.setText(msg);
        //pCnt1.add(txtProg);
        String details = name + "\n" + phone;
        SpanLabel lbl = new SpanLabel(details, "lblDetail");
        pCnt1.add(FlowLayout.encloseCenterMiddle(lbl));

        //Container cnt = new Container(new BorderLayout());
        Container cnt = new Container(BoxLayout.x());
        //Button btnDel = new Button("Delete", "btn");
        Button btnDel = new Button(proc.customIcon(FontImage.MATERIAL_DELETE, 0xff0000, 4));
        Button btnCall = new Button(proc.customIcon(FontImage.MATERIAL_CALL, 0x3399ff, 4));
        Button btnCancel = new Button("Cancel", "btn");

        //cnt.add(BorderLayout.WEST, btnDel).add(BorderLayout.EAST, btnCancel);
        cnt.addAll(btnDel, btnCall);
        pCnt1.add(FlowLayout.encloseCenterMiddle(cnt));

        //Dialog d = new Dialog(new BorderLayout());
        Sheet sheet = new Sheet(null, name);
        sheet.setLayout(BoxLayout.y());

        Form addForm = new Form(BoxLayout.x());
        addForm.setScrollableY(false);
        addForm.setUIID("progDForm");
        addForm.getToolbar().hideToolbar();
        addForm.add(FlowLayout.encloseCenterMiddle(pCnt1));
        //d.addComponent(BorderLayout.CENTER, addForm);
        sheet.add(FlowLayout.encloseCenterMiddle(addForm));

        btnCall.addActionListener(e -> {
            Display.getInstance().dial(phone);
        });
        btnDel.addActionListener(e -> {

            String msg = "Please confirm\nDelete contact " + name + "?"
                    + "\n\nNote it will be deleted permanently from your phone contacts\n";
            Form confirm = proc.confirmForm(msg, form);
            proc.btnOk.addActionListener(ev -> {
                if (ContactsManager.deleteContact(contId)) {
                    form.show();
                    //d.dispose();
                    sheet.setVisible(false);
                    viewContacts();

                }
            });
            confirm.show();

        });
        btnCancel.addActionListener(e -> {
            //d.dispose();
        });
        //d.show();
        sheet.show();
    }
}
