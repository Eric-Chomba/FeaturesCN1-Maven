/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zomuhtech.cn.features.advft;

/**
 * GUI builder created Form
 *
 * @author Eric
 */
import com.codename1.components.ClearableTextField;
import com.codename1.components.FloatingActionButton;
import com.codename1.components.InfiniteProgress;
import com.codename1.components.MultiButton;
import com.codename1.components.ScaleImageLabel;
import com.codename1.components.SliderBridge;
import com.codename1.components.Switch;
import com.codename1.contacts.Contact;
import com.codename1.contacts.ContactsManager;
import com.codename1.io.ConnectionRequest;
import com.codename1.io.NetworkManager;
import com.codename1.io.Util;
import com.codename1.system.NativeLookup;
import com.codename1.ui.AutoCompleteTextField;
import com.codename1.ui.Button;
import com.codename1.ui.ButtonGroup;
import com.codename1.ui.CheckBox;
import com.codename1.ui.ComboBox;
import com.codename1.ui.Component;
import static com.codename1.ui.Component.CENTER;
import com.codename1.ui.Container;
import com.codename1.ui.Dialog;
import com.codename1.ui.Display;
import com.codename1.ui.EncodedImage;
import com.codename1.ui.Font;
import com.codename1.ui.FontImage;
import com.codename1.ui.Form;
import com.codename1.ui.Image;
import com.codename1.ui.Label;
import com.codename1.ui.PickerComponent;
import com.codename1.ui.RadioButton;
import com.codename1.ui.Slider;
import com.codename1.ui.TextArea;
import com.codename1.ui.TextComponent;
import com.codename1.ui.TextField;
import com.codename1.ui.URLImage;
import com.codename1.ui.events.ScrollListener;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.layouts.*;
import com.codename1.ui.list.DefaultListModel;
import com.codename1.ui.list.GenericListCellRenderer;
import com.codename1.ui.list.ListCellRenderer;
import com.codename1.ui.plaf.Border;
import com.codename1.ui.plaf.DefaultLookAndFeel;
import com.codename1.ui.plaf.Style;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.table.TableLayout;
import com.codename1.ui.validation.Constraint;
import com.codename1.ui.validation.LengthConstraint;
import com.codename1.ui.validation.NumericConstraint;
import com.codename1.ui.validation.RegexConstraint;
import com.codename1.ui.validation.Validator;
import com.zomuhtech.cn.features.advft.ml.AndroidBarcode;
import com.zomuhtech.cn.features.procs.Proc;
//import com.zomuhtech.cn.features.procs.TextFieldContainer;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import com.zt.designs.TextFieldContainer;

public class UserInput extends Form {

    Form form, prevForm, contForm;
    StringBuilder sb;
    Proc proc;
    long lastScroll = 0;
    //TextFieldContainer container;
    float tfWidth, tfBoxWidth;

    public UserInput(Form form) {
        //this(com.codename1.ui.util.Resources.getGlobalResources());
        this.prevForm = form;
        if (Display.getInstance().isTablet()) {
            this.tfWidth = 0.2f;
            this.tfBoxWidth = 0.8f;
        } else {
            this.tfWidth = 0.1f;
            this.tfBoxWidth = 0.4f;
        }

        //container = new TextFieldContainer();
        proc = new Proc();

        Display.getInstance().callSerially(() -> {
            createUI();
        });
    }

    private void createUI() {

        form = proc.getForm("Input Fields", prevForm);
        //form.setLayout(BoxLayout.y());
        form.setLayout(new BorderLayout());

        Container cntPar = new Container(BoxLayout.y());
        TextModeLayout tl = new TextModeLayout(10, 2);
        Form cntInput = new Form(tl);
        cntInput.getToolbar().hideToolbar();
        cntInput.setUIID("cntPar");

        cntInput.add(tl.createConstraint().horizontalSpan(2),
                new Label("Text Components", proc.getLblInputUIID()));
        TextComponent title = new TextComponent().label("Title");
        //title.errorMessage("Enter Title");

        PickerComponent date = PickerComponent.createDate(new Date()).
                label("Date");
        TextComponent price = new TextComponent().label("Price");
        //price.getLabelForComponent().setUIID("tc");
        TextComponent location = new TextComponent().label("Location");
        TextComponent desc = new TextComponent().label("Description")
                .multiline(true);

        Validator val = new Validator();
        val.addConstraint(title, new LengthConstraint(2));
        val.addConstraint(price, new NumericConstraint(true));

        cntInput.add(tl.createConstraint().widthPercentage(60), title);
        cntInput.add(tl.createConstraint().widthPercentage(40), date);
        cntInput.add(tl.createConstraint().widthPercentage(30), price);
        cntInput.add(tl.createConstraint().widthPercentage(70), location);
        cntInput.add(tl.createConstraint().horizontalSpan(2), desc);
        cntInput.setEditOnShow(title.getField());

        TextField tfEmail = new TextField();
        tfEmail.setHint("Email");
        val.addConstraint(tfEmail, RegexConstraint
                .validEmail("Enter valid email address"));
        cntInput.add(tl.createConstraint().horizontalSpan(2),
                new Label("Email Validation", proc.getLblInputUIID()));
        cntInput.add(tl.createConstraint().horizontalSpan(2), tfEmail);
        //form.add(cntInput);
        cntPar.add(cntInput);

        Container cntPhone = new Container(BoxLayout.y(), "cntPar");
        cntPhone.add(new Label("Phone Validation", proc.getLblInputUIID()));
        TableLayout tlPhon = new TableLayout(1, 2);
        Container cntPhon = new Container(tlPhon);
        TextField tfPhone = new TextField();
        tfPhone.setHint("Phone");
        tfPhone.setConstraint(TextArea.PHONENUMBER);
        cntPhon.add(tlPhon.createConstraint().widthPercentage(80), tfPhone);

        val.addConstraint(tfPhone, new Constraint() {
            @Override
            public boolean isValid(Object value) {
                String v = (String) value;
                for (int i = 0; i < v.length(); i++) {
                    char c = v.charAt(i);
                    if ((c >= '0' && c <= '9' || c == '+' || c == '-')
                            && v.length() >= 3) {
                        continue;
                    }
                    return false;
                }
                return true;
            }

            @Override
            public String getDefaultFailMessage() {
                return "Enter valid phone number";
            }
        });

        Button btnBrwCont = new Button(proc.sideMenuIcon(
                FontImage.MATERIAL_CONTACT_PHONE));
        btnBrwCont.addActionListener(evt -> {
            browseCont(tfPhone);
        });
        //cntPhon.add(tlPhon.createConstraint().widthPercentage(20), btnBrwCont);
        cntPhone.add(cntPhon);
        //form.add(cntPhone);
        cntPar.add(cntPhone);

        Container cntInputFields = new Container(BoxLayout.y(), "cntCustomTf");

        cntInputFields.add(new Label("Custom TextFields", proc.getLblInputUIID()));

        TextField tf1 = new TextField();
        //TextField tf1 = proc.getInputTf("First Name", TextArea.ANY);
//        Container cntTf1 = container.getTfContainer(form, tf1, 0x00BFFF, 0x4B0082, tfWidth,
//                TextArea.ANY, "First Name", 0x6493f4);
        Container cntTf1 = TextFieldContainer.getTfContainer(form, tf1, 0x00BFFF, 0x4B0082,
                proc.darkBlue, tfWidth, TextArea.ANY, "First Name", 0x6493f4);

        TextField tf2 = new TextField();
//        Container cntTf2 = container.getTfContainer(form, tf2, 0x00BFFF, 0x4B0082, tfWidth,
//                TextArea.PHONENUMBER, "Phone Number", 0x6493f4);
        Container cntTf2 = TextFieldContainer.getTfContainer(form, tf2, 0x00BFFF, 0x4B0082,
                proc.darkBlue, tfWidth, TextArea.PHONENUMBER, "Phone Number", 0x6493f4);

        TextField tf3 = new TextField();
//        Container cntTf3 = container.getTfContainer(form, tf3, 0x00BFFF, 0x4B0082, tfWidth,
//                TextArea.EMAILADDR, "Email Address", 0x6493f4);
        Container cntTf3 = TextFieldContainer.getTfContainer(form, tf3, 0x00BFFF, 0x4B0082,
                proc.darkBlue, tfWidth, TextArea.EMAILADDR, "Email Address", 0x6493f4);

        int tfBgColor, textColor;
        if (proc.getDarkMode().equals("On")) {
            tfBgColor = proc.blueGray;
            textColor = proc.white;
        } else {
            tfBgColor = proc.white;
            textColor = 0x4B0082;
        }
        TextField tf4 = new TextField();
//        Container cntTf4 = container.getTfBoxContainer(form, tf4, 0x35c1e0, tfBoxWidth, tfBgColor,
//                textColor, TextArea.ANY, "Last Name", 0x6493f4);
        Container cntTf4 = TextFieldContainer.getTfBoxContainer(form, tf4, 0x35c1e0, tfBoxWidth, tfBgColor,
                textColor, TextArea.ANY, "Last Name", 0x6493f4);

        TextField tf5 = new TextField();
//        Container cntTf5 = container.getTfBoxContainer(form, tf5, 0x35c1e0, tfBoxWidth, tfBgColor,
//                textColor, TextArea.PHONENUMBER, "Mobile Number", 0x6493f4);
        Container cntTf5 = TextFieldContainer.getTfBoxContainer(form, tf5, 0x35c1e0, tfBoxWidth, tfBgColor,
                textColor, TextArea.PHONENUMBER, "Mobile Number", 0x6493f4);

        TextField tf6 = new TextField();
//        Container cntTf6 = container.getTfBoxContainer(form, tf6, 0x35c1e0, tfBoxWidth, tfBgColor,
//                textColor, TextArea.EMAILADDR, "Email", 0x6493f4);
        Container cntTf6 = TextFieldContainer.getTfBoxContainer(form, tf6, 0x35c1e0, tfBoxWidth, tfBgColor,
                textColor, TextArea.EMAILADDR, "Email", 0x6493f4);

        cntInputFields.addAll(cntTf1, cntTf2, cntTf3, cntTf4, cntTf5, cntTf6);
        cntPar.add(cntInputFields);

        Container cntBarcode = new Container(BoxLayout.y(), "cntPar");
        cntBarcode.add(new Label("QR/Barcode", proc.getLblInputUIID()));
        TableLayout tlScan = new TableLayout(1, 2);
        Container cntScan = new Container(tlScan); //, "tfLay"
        TextField tfBarcode = new TextField();
        tfBarcode.setHint("QR or Barcode");
        tfBarcode.setConstraint(TextArea.ANY);
        cntScan.add(tlScan.createConstraint().widthPercentage(80), tfBarcode);

        Button btnScan = new Button(proc.sideMenuIcon(
                FontImage.MATERIAL_QR_CODE_SCANNER));
        btnScan.addActionListener(evt -> {

            if (Display.getInstance().getPlatformName()
                    .equals("and")) {
                AndroidBarcode ab = NativeLookup.create(AndroidBarcode.class);
                String resp = ab.getBarcodeData("Barcode:QR:DarkMode:"
                        + proc.getDarkMode());
                tfBarcode.setText(resp);
            } else {
                proc.showToast("Not available for this device yet",
                        FontImage.MATERIAL_INFO).show();
            }

        });
        cntScan.add(tlScan.createConstraint().widthPercentage(20), btnScan);
        cntBarcode.add(cntScan);
        //form.add(cntBarcode);
        cntPar.add(cntBarcode);

        ArrayList<String> arr = new ArrayList<>();
        arr.add("Laptops");
        arr.add("Lenovo");
        arr.add("HP");
        arr.add("Dell");
        arr.add("Mac");

        DefaultListModel<String> defList = new DefaultListModel<>(arr);
        Container cntTf = new Container(BoxLayout.y(), "cntPar");
        cntTf.add(new Label("AutoCompleteTf - Laptop model",
                proc.getLblInputUIID()));
        defList.addSelectionListener((int oldVal, int newVal) -> {
            proc.showToast(defList.getItemAt(newVal), FontImage.MATERIAL_INFO_OUTLINE).show();
        });
        AutoCompleteTextField autoTf = new AutoCompleteTextField(defList);/* {

            @Override
            protected boolean filter(String text) {
                if (text.length() == 0) {
                    return false;
                }

                
                defList.removeAll();
                for (String s : arr) {
                    defList.addItem(s);
                }

                return true;
            }

        };*/
        //autoTf.getAllStyles().setBgColor(0xffffff);
        autoTf.setMinimumElementsShownInPopup(3);
        //autoTf.setUIID("autoTf");
        cntTf.add(autoTf);
        cntTf.add(new Label("AutoCompleteTf - GOT characters",
                proc.getLblInputUIID()));
        cntTf.add(getAutoTf());

        // form.add(cntTf);
        cntPar.add(cntTf);

        Container cntDD = new Container(BoxLayout.y(), "cntPar");
        cntDD.add(new Label("Popup - Laptop model", proc.getLblInputUIID()));
        MultiButton btn = new MultiButton("Select Model");
        if (Display.getInstance().isTablet()) {
            btn.setUIIDLine1("popBtnTab");
        } else {
            btn.setUIIDLine1("popBtn");
        }
        btn.addActionListener(e -> {

            Dialog d = new Dialog();
            d.setLayout(BoxLayout.y());
            d.getContentPane().setScrollableY(true);

            for (int j = 0; j < arr.size(); j++) {

                MultiButton btnM = new MultiButton(arr.get(j));
                btnM.setUIIDLine1("popLbl");
                btnM.setUIIDLine2("popLbl");

                //btnM.setIconUIID("popImg");
                btnM.setTextLine2(arr.get(j));
                // try {
                //btnM.setIcon(createImage("/hse_sub_50.png"));
                btnM.setIcon(proc.customIcon(FontImage.MATERIAL_COMPUTER,
                        proc.colorBlue, 4));
                //} catch (IOException ex) {
                //}
                d.add(btnM);
                btnM.addActionListener(evt -> {
                    d.dispose();
                    btn.setTextLine1(btnM.getTextLine1());
                    btn.revalidate();
                });
            }
            d.showPopupDialog(btn);

        });
        cntDD.add(btn);
        //form.add(tl.createConstraint().horizontalSpan(2), cntDD);
        // form.add(cntDD);
        cntPar.add(cntDD);

        Container cntChk = new Container(BoxLayout.y(), "cntPar");
        CheckBox chk1 = new CheckBox("CheckBox no icon");
        chk1.setUIID(proc.getCheckBoxUIID());
        chk1.setSelected(true);
        Image icon = FontImage.createMaterial(FontImage.MATERIAL_WARNING,
                UIManager.getInstance().getComponentStyle("Title"), 4);

        Style s;
        if (Display.getInstance().isTablet()) {
            s = UIManager.getInstance().getComponentStyle("radTab"); //button
        } else {
            s = UIManager.getInstance().getComponentStyle("rad");
        }
        FontImage chkEmptyImg = FontImage.createMaterial(
                FontImage.MATERIAL_CHECK_BOX_OUTLINE_BLANK, s);
        FontImage chkFullImg = FontImage.createMaterial(
                FontImage.MATERIAL_CHECK_BOX, s);
        ((DefaultLookAndFeel) UIManager.getInstance().getLookAndFeel())
                .setCheckBoxImages(chkFullImg, chkEmptyImg, chkFullImg,
                        chkEmptyImg);

        CheckBox chk2 = new CheckBox("CheckBox with Icon", icon);
        chk2.setUIID(proc.getCheckBoxUIID());
        CheckBox chk3 = new CheckBox("CheckBox Opposite True", icon);
        chk3.setUIID(proc.getCheckBoxUIID());
        chk3.setOppositeSide(true);
        CheckBox chk4 = new CheckBox("CheckBox Opposite False", icon);
        chk4.setUIID(proc.getCheckBoxUIID());
        chk4.setOppositeSide(false);

        cntChk.add(chk1).add(chk2).add(chk3).add(chk4);
        // form.add(cntChk);
        cntPar.add(cntChk);

        //Style s;
        if (Display.getInstance().isTablet()) {
            s = UIManager.getInstance().getComponentStyle("radTab"); //button
        } else {
            s = UIManager.getInstance().getComponentStyle("rad");
        }
        FontImage radEmptyImg = FontImage.createMaterial(
                FontImage.MATERIAL_RADIO_BUTTON_UNCHECKED, s);
        FontImage radFullImg = FontImage.createMaterial(
                FontImage.MATERIAL_RADIO_BUTTON_CHECKED, s);
        ((DefaultLookAndFeel) UIManager.getInstance().getLookAndFeel())
                .setRadioButtonImages(radFullImg, radEmptyImg, radFullImg,
                        radEmptyImg);
        Container cntRad = new Container(new GridLayout(1, 3), "cntPar");
        RadioButton rad1 = new RadioButton("Radio 1");
        rad1.setUIID(proc.getRadioUIID());
        RadioButton rad2 = new RadioButton("Radio 2");
        rad2.setUIID(proc.getRadioUIID());
        RadioButton rad3 = new RadioButton(icon);
        rad2.setUIID(proc.getRadioUIID());
        ButtonGroup bg = new ButtonGroup(rad1, rad2, rad3);
        cntRad.add(rad1).add(rad2).add(rad3);
        bg.addActionListener(e -> {
            if (bg.getSelectedIndex() == 2) {
                proc.showToast("You selected Other", FontImage.MATERIAL_INFO_OUTLINE).show();
            } else {
                RadioButton rad = bg.getRadioButton(bg.getSelectedIndex());
                proc.showToast("You selected " + rad.getText(),
                        FontImage.MATERIAL_INFO_OUTLINE).show();
            }
        });
        cntPar.add(cntRad);

        Container cntToggle = new Container(BoxLayout.y(), "cntPar");
        cntToggle.add(new Label("Toggle Buttons", proc.getLblInputUIID()));
        ButtonGroup bgToggle = new ButtonGroup();
        RadioButton radToggle1 = RadioButton.createToggle("Option 1", bgToggle);
        //radToggle1.setUIID(proc.getRadioUIID());
        RadioButton radToggle2 = RadioButton.createToggle("Option 2", bgToggle);
        //radToggle2.setUIID(proc.getRadioUIID());
        RadioButton radToggle3 = RadioButton.createToggle("Option 3", bgToggle);
        //radToggle3.setUIID(proc.getRadioUIID());
        cntToggle.add(radToggle1).add(radToggle2).add(radToggle3);
        bgToggle.addActionListener(e -> {
            RadioButton rad = bgToggle.getRadioButton(bgToggle.getSelectedIndex());
            proc.showToast("You selected " + rad.getText(), FontImage.MATERIAL_INFO_OUTLINE).show();
        });
        cntPar.add(cntToggle);

        Container cntCard = new Container(BoxLayout.y(), "cntPar");
        cntCard.add(new Label("Enter Card Number", proc.getLblInputUIID()));
        Container cntCreditCard = new Container(new GridLayout(1, 4),
                "cntCreditCard");
        TextField num1 = new TextField(4);
        num1.setConstraint(TextArea.NUMERIC);
        cntCreditCard.addComponent(num1);
        TextField num2 = new TextField(4);
        num2.setConstraint(TextArea.NUMERIC);
        cntCreditCard.addComponent(num2);
        TextField num3 = new TextField(4);
        num3.setConstraint(TextArea.NUMERIC);
        cntCreditCard.addComponent(num3);
        TextField num4 = new TextField(4);
        num4.setConstraint(TextArea.NUMERIC);
        cntCreditCard.addComponent(num4);
        cntCard.add(cntCreditCard);
        cntPar.add(cntCard);

        autoMoveToNext(num1, num2);
        autoMoveToNext(num2, num3);
        autoMoveToNext(num3, num4);

        FloatingActionButton fab = FloatingActionButton
                .createFAB(FontImage.MATERIAL_ADD, "fab");
        fab.setUIID("fab");

        FloatingActionButton fabAnim = fab.createSubFAB(FontImage.MATERIAL_ANIMATION,
                "Anim");
        fabAnim.setUIID("fab1");

        fabAnim.addActionListener(ev -> {
            new Anim(form).show();
        });
        /*FloatingActionButton fabCont = fab
                .createSubFAB(FontImage.MATERIAL_PEOPLE, "Contacts");
        fabCont.setUIID("fab2");
        fabCont.addActionListener(ev -> {
            new ContactFt(form).show();
        });*/
        FloatingActionButton fabImg = fab
                .createSubFAB(FontImage.MATERIAL_IMAGE, "Images");
        fabImg.setUIID("fab2");
        fabImg.addActionListener(ev -> {
            new Images(form).show();
        });
        fab.bindFabToContainer(form.getContentPane());

        Container cntBadge = new Container(BoxLayout.y(), "cntPar");
        cntBadge.add(new Label("Messages Badge", proc.getLblInputUIID()));
        Button btnChat = new Button(proc.materialIcon(FontImage.MATERIAL_CHAT));

        //FontImage.setMaterialIcon(btnChat, FontImage.MATERIAL_CHAT, 7);
        // FontImage.setMaterialIcon(btnChat,  proc.materialIcon(FontImage.MATERIAL_CHAT), 7);
        FloatingActionButton fabBadge = FloatingActionButton.createBadge("33");
        //fabBadge.setUIID("fab");
        cntBadge.add(FlowLayout.encloseCenter(fabBadge.bindFabToContainer(btnChat,
                Component.RIGHT, Component.TOP)));
        TextField tfBadgeVal = new TextField("33");
        tfBadgeVal.addDataChangedListener((i, ii) -> {
            fabBadge.setText(tfBadgeVal.getText());
            fabBadge.getParent().revalidate();
        });
        cntBadge.add(tfBadgeVal);
        cntPar.add(cntBadge);

        Container cntVKeyboard = new Container(BoxLayout.y(), "cntPar");
        cntVKeyboard.add(new Label("Virtual Keyboard & Clearable Tf",
                proc.getLblInputUIID()));
        TextField tfSearch = new TextField();
        tfSearch.putClientProperty("searchField", Boolean.TRUE);
        TextField tfSend = new TextField();
        tfSend.putClientProperty("sendButton", Boolean.TRUE);
        TextField tfGo = new TextField();
        tfGo.putClientProperty("goButton", Boolean.TRUE);
        cntVKeyboard.add(ClearableTextField.wrap(tfSearch, 3))
                .add(ClearableTextField.wrap(tfSend, 3f))
                .add(ClearableTextField.wrap(tfGo, 3));
        cntPar.add(cntVKeyboard);

        Container cntChat = new Container(BoxLayout.y(), "cntChat");
        cntChat.add(new Label("Chat Msg Box", proc.getLblInputUIID()));

        TableLayout tL = new TableLayout(1, 2);
        Container cntMsg = new Container(tL);

        Container cntTfMsg = new Container(BoxLayout.y(), proc.getTfMsgLayUIID());

        TextField tfMsg = new TextField();
        tfMsg.setHint("Type a message");
        tfMsg.setUIID("tfMsg");
        tfMsg.getHintLabel().setUIID("tfMsgHint");
        tfMsg.setMaxSize(5000);
        tfMsg.setSingleLineTextArea(false);
        tfMsg.setGrowByContent(true);
        tfMsg.setGrowLimit(4);
        tfMsg.setScrollVisible(true);
        tfMsg.addDataChangedListener((i1, i2) -> {
            //revalidate message container to resize on text length change
            cntTfMsg.revalidate();
        });
        cntTfMsg.add(FlowLayout.encloseLeftMiddle(tfMsg));

        Button btnSend = new Button(proc.materialIcon(FontImage.MATERIAL_SEND),
                "btnSend");
        btnSend.addActionListener(e -> {
            proc.showToast("Sending " + tfMsg.getText(), FontImage.MATERIAL_INFO_OUTLINE).show();
            tfMsg.setText("");
            cntTfMsg.revalidate();
        });
        /*ntMsg.add(tL.createConstraint().widthPercentage(80),
                FlowLayout.encloseCenterMiddle(cntTfMsg));*/
        cntMsg.add(tL.createConstraint().widthPercentage(80), cntTfMsg);
        cntMsg.add(tL.createConstraint().widthPercentage(20),
                FlowLayout.encloseCenterMiddle(btnSend));

        cntChat.add(cntMsg);
        cntPar.add(cntChat);

        Container cntSwitch = new Container(BoxLayout.y(), "cntPar");
        cntSwitch.add(new Label("Switch", proc.getLblInputUIID()));
        Switch onOff = new Switch();
        onOff.addActionListener(e -> {
            if (onOff.isValue()) {
                proc.showToast("Selected Yes", FontImage.MATERIAL_INFO_OUTLINE).show();
            } else {
                proc.showToast("Selected No", FontImage.MATERIAL_INFO_OUTLINE).show();
            }
        });
        cntSwitch.add(FlowLayout.encloseLeftMiddle(onOff));
        cntPar.add(cntSwitch);

        Container cntComboBox = new Container(BoxLayout.y(), "cntPar");
        cntComboBox.add(new Label("Combo Box", proc.getLblInputUIID()));
        ComboBox<Map<String, Object>> comboBox = new ComboBox<>(
                createListEntry("A Game of Thrones", "1996"),
                createListEntry("A Clash of Kings", "1998"),
                createListEntry("A Storm of Swords", "2000")
        );
        MultiButton btn1 = new MultiButton();
        btn1.setUIIDLine1("popLbl");
        MultiButton btn2 = new MultiButton();
        btn2.setUIIDLine1("popLbl");

        GenericListCellRenderer glcr = new GenericListCellRenderer<>(btn1, btn2);

        comboBox.setRenderer(glcr);

        comboBox.addActionListener(e -> {

            String selStr = comboBox.getSelectedItem().toString();
            String selItem = selStr.substring(1, selStr.length() - 1);
            //Line1=A Game of Thrones, Line2=1996
            String[] selArr1 = proc.splitValue(selItem, ",");
            //[Line1=A Game of Thrones, Line2=1996]
            sb = new StringBuilder();
            for (String item : selArr1) {
                String[] selArr2 = proc.splitValue(item, "=");
                sb.append(selArr2[1]).append(":");
            }
            String[] selArr3 = proc.splitValue(sb.toString(), ":");
            proc.showToast(selArr3[0] + "\n" + selArr3[1], FontImage.MATERIAL_INFO_OUTLINE).show();
            sb = new StringBuilder();
        });
        cntComboBox.add(comboBox);
        cntPar.add(cntComboBox);

        Container cntRate = new Container(BoxLayout.y(), "cntPar");
        cntRate.add(new Label("Rate", proc.getLblInputUIID()));
        cntRate.add(FlowLayout.encloseLeftMiddle(createStarSlider()));
        cntPar.add(cntRate);

        Container cntDownloadProgress = new Container(BoxLayout.y(), "cntPar");
        Button btnDwd = new Button("Download Progress", "btn");
        cntDownloadProgress.add(btnDwd);
        ScaleImageLabel lblImg = new ScaleImageLabel();

        Slider sliderProgress = proc.createDownloadSlider();
        cntDownloadProgress.add(sliderProgress);
        cntDownloadProgress.add(lblImg);
        btnDwd.addActionListener(e -> {

            ConnectionRequest req = new ConnectionRequest(
                    "https://www.codenameone.com/img/blog/new_icon.png", false);
            SliderBridge.bindProgress(req, sliderProgress);
            NetworkManager.getInstance().addToQueueAndWait(req);
            if (req.getResponseCode() == 200) {
                lblImg.setIcon(EncodedImage.create(req.getResponseData()));
                sliderProgress.setVisible(false);
                form.revalidate();
            }
        });

        cntPar.add(cntDownloadProgress);
        form.add(CENTER, cntPar);
        cntPar.setScrollableY(true);

        form.show();
    }

    private Map<String, Object> createListEntry(String name, String date) {

        Map<String, Object> entry = new HashMap<>();
        entry.put("Line1", name);
        entry.put("Line2", date);
        return entry;
    }

    /*private void createToast(String text) {
        
        Image icon = FontImage.createMaterial(FontImage.MATERIAL_ERROR,
                UIManager.getInstance().getComponentStyle("Title"), 4);
        ToastBar.Status status = ToastBar.getInstance().createStatus();
        status.setUiid("customToastBar");
        status.setMessageUIID(proc.getLblInputUIID());
        status.setMessage(text);
        status.setIcon(icon);
        status.setExpires(2000);
        status.show();
    }*/
    private void autoMoveToNext(final TextField tfCurrent, final TextField tfNext) {
        /* tfCurrent.addDataChangedListener(new DataChangedListener() {

            public void dataChanged(int type, int index) {*/

        tfCurrent.addDataChangedListener((int type, int index) -> {

            if (tfCurrent.getText().length() == 5) {

                Display.getInstance().stopEditing(tfCurrent);

                String val = tfCurrent.getText();

                tfCurrent.setText(val.substring(0, 4));

                tfNext.setText(val.substring(4));

                /*Display.getInstance().editString(tfNext, 5,
                        tfCurrent.getConstraint(), tfNext.getText());*/
                tfNext.startEditingAsync();
            }
        });

        autoMoveToPrev(tfNext, tfCurrent);
    }

    private void autoMoveToPrev(TextField tfCurrent, TextField tfNext) {

        tfCurrent.addDataChangedListener((int type, int index) -> {

            if (tfCurrent.getText().length() == 0) {
                tfNext.startEditingAsync();
            }
        });
    }

    private Slider createStarSlider() {

        int size;
        if (Display.getInstance().isTablet()) {
            if (Display.getInstance().getDeviceDensity() < 40) {
                size = 5;
            } else {
                size = 8;
            }
        } else {
            size = 4;
        }

        Slider slider = new Slider();
        slider.setEditable(true);
        slider.setMinValue(0);
        slider.setMaxValue(10);
        Font font = Font.createTrueTypeFont("native:MainLight",
                "native:MainLight").derive(Display.getInstance()
                        .convertToPixels(size, true), Font.STYLE_PLAIN);
        Style s = new Style(0x00FBEB, 0, font, (byte) 0);
        Image fullStar = FontImage.createMaterial(FontImage.MATERIAL_STAR, s).toImage();
        s.setOpacity(100);
        s.setFgColor(0);
        Image emptyStar = FontImage.createMaterial(FontImage.MATERIAL_STAR, s).toImage();

        initSliderStyle(slider.getSliderEmptySelectedStyle(), emptyStar);
        initSliderStyle(slider.getSliderEmptyUnselectedStyle(), emptyStar);
        initSliderStyle(slider.getSliderFullSelectedStyle(), fullStar);
        initSliderStyle(slider.getSliderFullUnselectedStyle(), fullStar);

        slider.setPreferredSize(new Dimension(fullStar.getWidth() * 5,
                fullStar.getHeight()));

        slider.addActionListener(e -> {
            proc.showToast("Rate " + slider.getProgress() + "/10",
                    FontImage.MATERIAL_INFO_OUTLINE).show();
        });
        return slider;
    }

    private void initSliderStyle(Style s, Image star) {
        s.setBackgroundType(Style.BACKGROUND_IMAGE_TILE_BOTH);
        s.setBorder(Border.createEmpty());
        s.setBgImage(star);
        s.setBgTransparency(0);
    }

    private AutoCompleteTextField getAutoTf() {
        final String[] characters = {"Tyrion Lannister", "Jamie Lannister",
            "Cersei Lannister"
        };
        AutoCompleteTextField acTf = new AutoCompleteTextField(characters);

        final int size = Display.getInstance().convertToPixels(7);
        final EncodedImage placeholder = EncodedImage.createFromImage(
                Image.createImage(size, size, 0xffcccccc), true);
        final String[] actors = {"Peter Dinklage", "Nikolaj Coster",
            "Lena Headey"};
        /*final Image[] pictures = {
            URLImage.createToStorage(placeholder, "tyrion",
            "https://i.lv3.hbo.com/assets/images/series/game-of-thrones/character/s5/tyrion-lannister-512x512.jpg"),
            URLImage.createToStorage(placeholder, "jamie",
            "https://i.lv3.hbo.com/assets/images/series/game-of-thrones/character/s5/jamie-lannister-512x512.jpg"),
            URLImage.createToStorage(placeholder, "cersei",
            "https://i.lv3.hbo.com/assets/images/series/game-of-thrones/character/s5/cersei-lannister-512x512.jpg")
        };*/
        final Image[] pictures = {
            URLImage.createToStorage(placeholder, "tyrion",
            "https://awoiaf.westeros.org/images/thumb/9/93/"
            + "AGameOfThrones.jpg/300px-AGameOfThrones.jpg"),
            URLImage.createToStorage(placeholder, "jamie",
            "https://awoiaf.westeros.org/images/thumb/3/39/"
            + "AClashOfKings.jpg/300px-AClashOfKings.jpg"),
            URLImage.createToStorage(placeholder, "cersei",
            "https://awoiaf.westeros.org/images/thumb/2/24/"
            + "AStormOfSwords.jpg/300px-AStormOfSwords.jpg")
        };

        acTf.setCompletionRenderer(new ListCellRenderer() {
            private final Label focus = new Label();
            private final Label line1 = new Label(characters[0], "popLbl");
            private final Label line2 = new Label(actors[0], "popLbl");
            private final Label icon = new Label(pictures[0]);

            private final Container cnt = BorderLayout.center(
                    BoxLayout.encloseY(line1, line2)).add(BorderLayout.EAST, icon);

            @Override
            public Component getListCellRendererComponent(
                    com.codename1.ui.List list, Object value, int index,
                    boolean isSelected) {

                for (int j = 0; j < characters.length; j++) {
                    if (characters[j].equals(value)) {
                        line1.setText(characters[j]);
                        if (actors.length > j) {
                            line2.setText(actors[j]);
                            icon.setIcon(pictures[j]);
                        } else {
                            line2.setText("");
                            icon.setIcon(placeholder);
                        }
                        break;
                    }
                }

                return cnt;
            }

            @Override
            public Component getListFocusComponent(com.codename1.ui.List list) {
                return focus;
            }

        });

        return acTf;
    }

    private void browseCont(TextField tfPhone) {
        /*final InteractionDialog dialog
                = new InteractionDialog(new BorderLayout());
        dialog.getTitleComponent().remove();
        dialog.setDialogUIID("statusDialog");*/

        contForm = proc.getForm("Contacts", form);
        contForm.setLayout(BoxLayout.y());

        Image img = proc.sideMenuIcon(FontImage.MATERIAL_PERSON_SEARCH);
        int size = Display.getInstance().convertToPixels(5);
        final Image conImg = img.scaledWidth(size);

        contForm.add(FlowLayout.encloseCenterMiddle(new InfiniteProgress()));

        Display.getInstance().scheduleBackgroundTask(() -> {
            //retrieved fields - include id,Fullname,picture,numbers,email,address
            /*(Contact[] cont = Display.getInstance()
                    .getAllContacts(true, true, false, true, false, false);*/
            Contact[] cont = ContactsManager
                    .getContacts(true, true, false, true, false, false);
            Display.getInstance().callSerially(() -> {
                contForm.removeAll();

                for (Contact c : cont) {

                    MultiButton btn = new MultiButton();
                    btn.setTextLine1(c.getDisplayName());
                    btn.setTextLine2(c.getPrimaryPhoneNumber());
                    btn.setUIIDLine1("lblMultiBtn");
                    btn.setUIIDLine2("lblMultiBtn");

                    btn.putClientProperty("id", c.getId());

                    btn.addActionListener(e -> {
                        tfPhone.setText(btn.getTextLine2());
                        form.showBack();
                        //dialog.dispose();
                    });
                    contForm.add(btn);

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
                contForm.getContentPane().animateLayout(150);
            });
        });

        /*SCROLL PERFORMANCE ENHANCEMENT*/
        //dont do anything while scrolling/animating in place
        //Sleeps when user interacts with UI & loads images if user hasnt touched UI
        long idle = System.currentTimeMillis() - lastScroll;
        while (idle < 1500 || contForm.getAnimationManager().isAnimating()) {
            /*|| scrollY != form.getScrollY()*/
            //scrollY = form.getScrollY();
            Util.sleep(Math.min(1500, Math.max(100, 2000 - ((int) idle))));
            idle = System.currentTimeMillis() - lastScroll;
        }

        //update lastScroll variable when user interacts/touches UI
        contForm.addPointerDraggedListener(e -> lastScroll = System.currentTimeMillis());

        contForm.addScrollListener(new ScrollListener() {
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

        contForm.getToolbar().addSearchCommand(e -> {
            proc.printLine("scr2 " + e.getSource());
            String text = (String) e.getSource();
            if (text == null || text.length() == 0) {
                //clear search
                for (Component cmp : contForm.getContentPane()) {
                    cmp.setHidden(false);
                    cmp.setVisible(true);
                }
                contForm.getContentPane().animateLayout(150);
            } else {
                text = text.toLowerCase();
                for (Component cmp : contForm.getContentPane()) {

                    MultiButton btn = (MultiButton) cmp;
                    String line1 = btn.getTextLine1();
                    String line2 = btn.getTextLine2();
                    btn.setUIIDLine1("lblMultiBtn");
                    btn.setUIIDLine2("lblMultiBtn");

                    boolean show = line1 != null && line1.toLowerCase()
                            .indexOf(text) > -1 || line2 != null
                            && line2.toLowerCase().indexOf(text) > -1;
                    btn.setHidden(!show);
                    btn.setVisible(show);

                    ((MultiButton) btn).addActionListener(ev -> {
                        //contForm.setVisible(false);
                        tfPhone.setText(btn.getTextLine2());
                        form.showBack();
                        //dialog.dispose();
                        //form.getToolbar().removeSearchCommand();
                        //form.revalidate();
                        //form.revalidate();
                        //ToastBar.showInfoMessage(btn.getTextLine1());

                    });

                }
                contForm.getContentPane().animateLayout(150);
            }
        }, 4);

        //dialog.add(CENTER, contForm);
        //dialog.show(10, 10, 10, 10);
        contForm.show();
    }

//    private class CustomFAB extends FloatingActionButton {
//
//        int size;
//
//        private CustomFAB(int size) {
//            this.size = size;
//        }
//
//        @Override
//        public Dimension calcPreferredSize() {
//            return new Dimension(size, size);
//        }
//    }
    public UserInput(com.codename1.ui.util.Resources resourceObjectInstance) {
        //initGuiBuilderComponents(resourceObjectInstance);
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////-- DON'T EDIT BELOW THIS LINE!!!


// <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initGuiBuilderComponents(com.codename1.ui.util.Resources resourceObjectInstance) {
        setLayout(new com.codename1.ui.layouts.FlowLayout());
        setInlineStylesTheme(resourceObjectInstance);
                setInlineStylesTheme(resourceObjectInstance);
        setTitle("UserInput");
        setName("UserInput");
    }// </editor-fold>

//-- DON'T EDIT ABOVE THIS LINE!!!
}
