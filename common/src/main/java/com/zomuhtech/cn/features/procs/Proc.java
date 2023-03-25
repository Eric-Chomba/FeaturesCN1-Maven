/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zomuhtech.cn.features.procs;

import com.codename1.components.InteractionDialog;
import com.codename1.components.SpanLabel;
import com.codename1.components.ToastBar;
import com.codename1.components.ToastBar.Status;
import com.codename1.io.Log;
import com.codename1.io.Preferences;
import com.codename1.io.Storage;
import com.codename1.l10n.L10NManager;
import com.codename1.ui.Button;
import com.codename1.ui.Command;
import com.codename1.ui.CommonProgressAnimations.CircleProgress;
import com.codename1.ui.Container;
import com.codename1.ui.Display;
import com.codename1.ui.Font;
import com.codename1.ui.FontImage;
import com.codename1.ui.Form;
import com.codename1.ui.Graphics;
import com.codename1.ui.Image;
import com.codename1.ui.Label;
import com.codename1.ui.Slider;
import com.codename1.ui.TextField;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.layouts.*;
import com.codename1.ui.plaf.Border;
import com.codename1.ui.plaf.Style;
import com.codename1.ui.util.SwipeBackSupport;
import com.codename1.util.StringUtil;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.StringTokenizer;

/**
 *
 * @author Eric
 */
public class Proc {

    public Button btnOk;
    int circleSize;
    public int colorBlue = 0x3399ff, darkBlue = 0x0000FF, neonBlue = 0x1F51FF,
            skyBlue = 0x87CEEB, navyBlue = 0x000080, colorTeal = 0x15E7FF,
            blueGray = 0x29293D, white = 0xFFFFFF, ivory = 0xE2DED0,
            lightGray = 0xE2DED0, yellow = 0xffff00,
            pink = 0xff33ff, purple = 0x990099, black = 0x000000,
            oceanBlue = 0x3684C3, brown = 0xCC7722;

    public void printLine(String value) {
        //System.out.println(value);
        Log.p(value);
    }

    public Form getForm(String title, Form prevForm) {

        Form form = new Form(title);
        form.getToolbar().setUIID("tbar");

        form.getToolbar().getTitleComponent().setUIID(getLblTitleUIID());
        form.setBackCommand(new Command("btnBack") {
            @Override
            public void actionPerformed(ActionEvent ev) {
                prevForm.showBack();
            }
        });
        Command backCmd = Command.create("",
                materialIcon(FontImage.MATERIAL_ARROW_BACK),
                evt -> {
                    prevForm.showBack();
                });

        form.getToolbar().addCommandToLeftBar(backCmd);

        //Enable swipe back to previous form
        SwipeBackSupport.bindBack(form, (args) -> {
            form.getToolbar();
            return prevForm.getComponentForm();
        });
        return form;
    }

    public Form getGraphsForm(String title, Form prevForm) {
        Form form = new Form(title);
        form.getAllStyles().setBgColor(0xffffff);
        form.getToolbar().setUIID("tbar");
        form.getToolbar().getTitleComponent().setUIID(getLblTitleUIID());
        form.setBackCommand(new Command("btnBack") {
            @Override
            public void actionPerformed(ActionEvent ev) {
                prevForm.showBack();
            }
        });
        Command backCmd = Command.create("",
                materialIcon(FontImage.MATERIAL_ARROW_BACK),
                evt -> {
                    prevForm.showBack();
                });

        form.getToolbar().addCommandToLeftBar(backCmd);
        return form;
    }

    public Form getDesignForm(String design, String title, Form prevForm,
            int bgColor, int toolbarColor) {

        Form form = new Form(title);
        form.getToolbar().setUIID("tbar");

        switch (design) {
            case "5":
                form.getToolbar().getTitleComponent().setUIID("lblTitle");
                break;
            default:
                form.getToolbar().getTitleComponent().setUIID(getLblTitleUIID());
                break;
        }

        if (getDarkMode().equals("On")) {
            form.getAllStyles().setBgColor(blueGray);
            switch (design) {
                case "1":
                case "3":
                    form.getToolbar().getAllStyles().setBgColor(toolbarColor);
                    break;
                default:
                    form.getToolbar().getAllStyles().setBgColor(blueGray);
                    break;
            }

        } else {
            form.getAllStyles().setBgColor(bgColor);
            form.getToolbar().getAllStyles().setBgColor(toolbarColor);
        }

        form.setBackCommand(new Command("btnBack") {
            @Override
            public void actionPerformed(ActionEvent ev) {
                prevForm.showBack();
            }
        });
        if (!design.equals("2") && !design.equals("2.1")) {
            Command backCmd = Command.create("",
                    materialIcon(FontImage.MATERIAL_ARROW_BACK),
                    evt -> {
                        prevForm.showBack();
                    });
            form.getToolbar().addCommandToLeftBar(backCmd);
        }

        //Enable swipe back to previous form
        SwipeBackSupport.bindBack(form, (args) -> {
            return prevForm.getComponentForm();
        });
        return form;
    }

    /*public Form getDesignForm(String title, Form prevForm) {

        Form form = new Form(title);
        if (!getDarkMode().equals("On")) {
            form.getAllStyles().setBgColor(neonBlue);
        } else {
            form.getAllStyles().setBgColor(0x29293D);
        }
        form.getToolbar().setUIID("design1TBar");
        form.getToolbar().getTitleComponent().setUIID(getD1LblTitleUIID());
        form.setBackCommand(new Command("btnBack") {
            @Override
            public void actionPerformed(ActionEvent ev) {
                prevForm.showBack();
            }
        });
        Command backCmd = Command.create("",
                swipeIcon(FontImage.MATERIAL_ARROW_BACK),
                evt -> {
                    prevForm.showBack();
                });

        form.getToolbar().addCommandToLeftBar(backCmd);

        //Enable swipe back to previous form
        SwipeBackSupport.bindBack(form, (args) -> {
            return prevForm.getComponentForm();
        });
        return form;
    }*/
    public Form getInputForm() {
        Form form = new Form(BoxLayout.y());
        form.getToolbar().hideToolbar();
        //form.getAllStyles().setBgColor(ColorUtil.WHITE);
        return form;
    }

    public Container getInputCnt() {
        return new Container(BoxLayout.x(), "tfLay");
    }

    public TextField getInputTf(String hint, int constraint) {

        TextField tf = new TextField();
        tf.setHint(hint);
        tf.setUIID("tf");
        tf.getHintLabel().setUIID("tfHint");
        tf.setConstraint(constraint);
        return tf;
    }

    public TextField getSearchTf(String hint, int constraint) {

        int size = 3;
        if (Display.getInstance().isTablet()
                && Display.getInstance().getDeviceDensity() < 40) {
            size = 2;
        }

        TextField tf = new TextField();
        tf.setHint(hint, customIcon(FontImage.MATERIAL_SEARCH, colorBlue, size));
        tf.setUIID("tfSearch");
        tf.getHintLabel().setUIID("tfSearchHint");
        tf.setConstraint(constraint);
        return tf;
    }

    public Button getInputBtn(String text) {
        return new Button(text, "btn");
    }

    public String[] splitValue(String value, String dmt) {
        ArrayList<String> splitArr = new ArrayList<>();
        StringTokenizer arr = new StringTokenizer(value, dmt);
        while (arr.hasMoreElements()) {
            splitArr.add(arr.nextToken());
        }
        String[] respArr = splitArr.toArray(new String[splitArr.size()]);
        return respArr;
    }

    public String[] splitSms(String value, String dmt) {
        ArrayList<String> splitArr = new ArrayList<>();
        StringTokenizer arr = new StringTokenizer(value, dmt);
        while (arr.hasMoreElements()) {
            splitArr.add(arr.nextToken());
        }
        String[] respArr = splitArr.toArray(new String[splitArr.size()]);
        return respArr;
    }

    public String getMenuLblUIID() {
        /*String uiid;
        //printLine("Device Density="+Display.getInstance().getDeviceDensity());
        if (Display.getInstance().isTablet()) {
            //printLine("1Plt=" + Display.getInstance().isTablet());
            uiid = "menuLblTab";
        } else if (Display.getInstance().isDesktop()) {
            //printLine("2Plt=" + Display.getInstance().isDesktop());
            uiid = "menuLblDesktop";
        } else {
            uiid = "menuLbl";
        }
        return uiid;*/
        return "menuLbl";
    }

    public String getMenuLblBtnUIID() {
        String uiid;
        if (Display.getInstance().isTablet()) {
            uiid = "menuLblBtnTab";
        } else if (Display.getInstance().isDesktop()) {
            uiid = "menuLblBtnDesktop";
        } else {
            uiid = "menuLblBtn";
        }
        return uiid;
    }

    public String getMenuImgBgUIID() {
        String uiid;
        if (Display.getInstance().isTablet()) {
            uiid = "menuImgRoundTab";
        } else {
            uiid = "menuImgRound";
        }
        return uiid;
    }

    public String getLblInputUIID() {
        /* String uiid;
        if (Display.getInstance().isTablet()) {
            uiid = "lblInputTab";
        } else {
            uiid = "lblInput";
        }
        return uiid;*/
        return "lblInput";
    }

    public String getLblTitleUIID() {
        String titleUiid = null;
        if (Display.getInstance().isTablet()) {
            titleUiid = "lblTitleTab";
        } else {
            //uiid = "lblTitle";

            //String platName = Display.getInstance().getPlatformName();
            switch (Display.getInstance().getPlatformName()) {
                case "and":
                    titleUiid = "lblATitle";
                    break;
                case "ios":
                    titleUiid = "lblITitle";
                    break;
                case "win":
                    titleUiid = "lblWTitle";
                    break;
            }

        }
        return titleUiid;
    }

    public String getD1LblTitleUIID() {
        String titleUiid = null;
        if (Display.getInstance().isTablet()) {
            titleUiid = "lblD1TitleTab";
        } else {
            //uiid = "lblTitle";

            //String platName = Display.getInstance().getPlatformName();
            switch (Display.getInstance().getPlatformName()) {
                case "and":
                    titleUiid = "lblD1ATitle";
                    break;
                case "ios":
                    titleUiid = "lblD1ITitle";
                    break;
                case "win":
                    titleUiid = "lblD1WTitle";
                    break;
            }

        }
        return titleUiid;
    }

    public String getTfMsgLayUIID() {
        String uiid;
        switch (Display.getInstance().getPlatformName()) {
            case "and":
                uiid = "tfMsgLayAnd";
                break;
            default:
                uiid = "tfMsgLay";
                break;
        }
        return uiid;
    }

    public FontImage materialIcon(char charCode) {
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
        Font font = Font.createTrueTypeFont("native:MainLight",
                "native:MainLight").derive(Display.getInstance()
                        .convertToPixels(size), Font.STYLE_PLAIN);

        Style s = new Style(0xffffff, 0x3399ff, font, (byte) 0);
        FontImage fontImg = FontImage.createMaterial(charCode, s);

        fontImg.setPadding(0);
        //fontImg.setBgTransparency(255);
        return fontImg;
    }

    public FontImage materialPrintIcon(char charCode) {
        int size;
        if (Display.getInstance().isTablet()) {
            size = 14;
        } else {
            size = 10;
        }
        Font font = Font.createTrueTypeFont("native:MainLight",
                "native:MainLight").derive(Display.getInstance()
                        .convertToPixels(size), Font.STYLE_PLAIN);

        Style s = new Style(0x3399ff, 0xffffff, font, (byte) 0);
        FontImage fontImg = FontImage.createMaterial(charCode, s);

        fontImg.setPadding(0);
        //fontImg.setBgTransparency(255);
        return fontImg;
    }

    public FontImage swipeIcon(char charCode) {
        int size;
        if (Display.getInstance().isTablet()) {
            size = 8;
        } else {
            size = 4;
        }
        Font font = Font.createTrueTypeFont("native:MainLight",
                "native:MainLight").derive(Display.getInstance()
                        .convertToPixels(size), Font.STYLE_PLAIN);

        Style s = new Style(0x3399ff, 0xffffff, font, (byte) 0);
        FontImage fontImg = FontImage.createMaterial(charCode, s);

        fontImg.setPadding(0);
        //fontImg.setBgTransparency(255);
        return fontImg;
    }

    public FontImage menuIcon(char charCode) {
        int size;
        if (Display.getInstance().isTablet()) {
            if (Display.getInstance().getDeviceDensity() < 40) {
                size = 7;
            } else {
                size = 10;
            }

        } else {
            size = 5;
        }
        Font font = Font.createTrueTypeFont("native:MainLight",
                "native:MainLight").derive(Display.getInstance()
                        .convertToPixels(size), Font.STYLE_PLAIN);

        Style s = new Style(0xffffff, 0x15E7FF, font, (byte) 0);
        FontImage fontImg = FontImage.createMaterial(charCode, s);

        fontImg.setPadding(0);
        //fontImg.setBgTransparency(255);
        return fontImg;
    }

    public FontImage sideMenuIcon(char charCode) {
        int size;
        if (Display.getInstance().isTablet()) {
            if (Display.getInstance().getDeviceDensity() < 40) {
                size = 4;
            } else {
                size = 8;
            }
        } else {
            size = 4;
        }
        Font font = Font.createTrueTypeFont("native:MainLight",
                "native:MainLight").derive(Display.getInstance()
                        .convertToPixels(size), Font.STYLE_PLAIN);

        Style s = new Style(0x15E7FF, 0x3399ff, font, (byte) 0);
        FontImage fontImg = FontImage.createMaterial(charCode, s);

        fontImg.setPadding(0);
        //fontImg.setBgTransparency(255);
        return fontImg;
    }

    public FontImage chatDeliveredIcon(char charCode) {
        int size;
        if (Display.getInstance().isTablet()) {
            size = 4;
        } else {
            size = 2;
        }
        Font font = Font.createTrueTypeFont("native:MainLight",
                "native:MainLight").derive(Display.getInstance()
                        .convertToPixels(size), Font.STYLE_PLAIN);

        Style s = new Style(0x15E7FF, 0x3399ff, font, (byte) 0);
        FontImage fontImg = FontImage.createMaterial(charCode, s);

        fontImg.setPadding(0);
        //fontImg.setBgTransparency(255);
        return fontImg;
    }

    public FontImage chatFailIcon(char charCode) {
        int size;
        if (Display.getInstance().isTablet()) {
            size = 4;
        } else {
            size = 2;
        }
        Font font = Font.createTrueTypeFont("native:MainLight",
                "native:MainLight").derive(Display.getInstance()
                        .convertToPixels(size), Font.STYLE_PLAIN);

        Style s = new Style(0xFF0000, 0x3399ff, font, (byte) 0);
        FontImage fontImg = FontImage.createMaterial(charCode, s);

        fontImg.setPadding(0);
        //fontImg.setBgTransparency(255);
        return fontImg;
    }

    public FontImage statusIcon(char charCode, int iconColor) {
        int size;
        if (Display.getInstance().isTablet()) {
            size = 10;
        } else {
            size = 6;
        }
        Font font = Font.createTrueTypeFont("native:MainLight",
                "native:MainLight").derive(Display.getInstance()
                        .convertToPixels(size), Font.STYLE_PLAIN);

        Style s = new Style(iconColor, 0x3399ff, font, (byte) 0);
        FontImage fontImg = FontImage.createMaterial(charCode, s);

        fontImg.setPadding(0);
        //fontImg.setBgTransparency(255);
        return fontImg;
    }

    public FontImage customIcon(char charCode, int iconColor, int size) {
        //int size;
        if (Display.getInstance().isTablet()) {

            if (Display.getInstance().getDeviceDensity() >= 40) {
                size = size * 2;
            }
        }
        Font font = Font.createTrueTypeFont("native:MainLight",
                "native:MainLight").derive(Display.getInstance()
                        .convertToPixels(size), Font.STYLE_PLAIN);

        Style s = new Style(iconColor, 0x3399ff, font, (byte) 0);
        FontImage fontImg = FontImage.createMaterial(charCode, s);

        fontImg.setPadding(0);
        //fontImg.setBgTransparency(255);
        return fontImg;
    }

//    public FontImage customKeyboardIcon(char charCode, int iconColor) {
//        int size;
//        if (Display.getInstance().isTablet()) {
//            size = 4;
//        } else {
//            size = 2;
//        }
//        Font font = Font.createTrueTypeFont("native:MainLight",
//                "native:MainLight").derive(Display.getInstance()
//                        .convertToPixels(size), Font.STYLE_PLAIN);
//
//        Style s = new Style(iconColor, 0x3399ff, font, (byte) 0);
//        FontImage fontImg = FontImage.createMaterial(charCode, s);
//
//        fontImg.setPadding(0);
//        //fontImg.setBgTransparency(255);
//        return fontImg;
//    }
    public FontImage horizontalAnimIcon(char charCode) {
        int size;
        if (Display.getInstance().isTablet()) {
            size = 6;
        } else {
            size = 2;
        }
        Font font = Font.createTrueTypeFont("native:MainLight",
                "native:MainLight").derive(Display.getInstance()
                        .convertToPixels(size), Font.STYLE_PLAIN);

        //Style s = new Style(0x15E7FF, 0x15E7FF, font, (byte) 0);
        Style s = new Style(0x15E7FF, 0xffffff, font, (byte) 0);
        FontImage fontImg = FontImage.createMaterial(charCode, s);

        fontImg.setPadding(0);
        //fontImg.setBgTransparency(255);
        return fontImg;
    }

    public FontImage circleAnimIcon(char charCode) {
        int size;
        if (Display.getInstance().isTablet()) {
            size = 3;
        } else {
            size = 1;
        }
        Font font = Font.createTrueTypeFont("native:MainLight",
                "native:MainLight").derive(Display.getInstance()
                        .convertToPixels(size), Font.STYLE_PLAIN);

        //Style s = new Style(0x15E7FF, 0x15E7FF, font, (byte) 0); //0xffffff, 0x15E7FF
        Style s = new Style(0x15E7FF, 0xffffff, font, (byte) 0);
        FontImage fontImg = FontImage.createMaterial(charCode, s);

        fontImg.setPadding(0);
        //fontImg.setBgTransparency(255);
        return fontImg;
    }

    public String getCntTabsUiid() {
        String uiid = null;
        switch (Display.getInstance().getPlatformName()) {
            case "and":
                uiid = "cntTabsAnd";
                break;
            case "win":
                uiid = "cntTabsWin";
                break;
            case "ios":
                uiid = "cntTabsIOS";
                break;
        }
        return uiid;
    }

    public String getCntTabParUiid() {
        String uiid = null;
        switch (Display.getInstance().getPlatformName()) {
            case "and":
                uiid = "cntTabParAnd";
                break;
            case "win":
                uiid = "cntTabParWin";
                break;
            case "ios":
                uiid = "cntTabParIOS";
                break;
        }
        return uiid;
    }

    public void changeBtnUIID(ArrayList<Button> btnArr, Button selBtn) {
        for (int k = 0; k < btnArr.size(); k++) {
            if (btnArr.get(k).equals(selBtn)) {
                btnArr.get(k).setUIID("btnNavSel");
            } else {
                btnArr.get(k).setUIID("btnNav");
            }
        }
    }

    public void changeTabNavUIID(ArrayList<Button> btnArr, Button selBtn) {
        for (int k = 0; k < btnArr.size(); k++) {
            if (btnArr.get(k).equals(selBtn)) {
                btnArr.get(k).setUIID("btnTabsNavSel");
            } else {
                btnArr.get(k).setUIID("btnTabsNav");
            }
        }
    }

    public void changeTabNav(ArrayList<Button> btnArr, Button selBtn,
            ArrayList<Container> cntBorderArr, Container cnt) {
        for (int k = 0; k < btnArr.size(); k++) {
            if (btnArr.get(k).equals(selBtn)) {
                btnArr.get(k).setUIID("btnTabsNavSel");
            } else {
                btnArr.get(k).setUIID("btnTabsNav");
            }

            if (cntBorderArr.get(k).equals(cnt)) {
                cntBorderArr.get(k).setVisible(true);
                cntBorderArr.get(k).setHidden(false);
            } else {
                cntBorderArr.get(k).setVisible(false);
                cntBorderArr.get(k).setHidden(true);
            }
        }

    }

    public Status showToast(String text, char icon) {
//        Image img = FontImage.createMaterial(icon,
//                UIManager.getInstance().getComponentStyle("Title"), 4)
        int size;

        if (Display.getInstance().isTablet()) {
            if (Display.getInstance().getDeviceDensity() < 40) {
                size = 2;
            } else {
                size = 4;
            }
        } else {
            size = 4;
        }

        Image img = customIcon(icon, colorBlue, size);
        ToastBar.Status status = ToastBar.getInstance().createStatus();
        status.setMessage(text);
        status.setUiid("customToastBar");
        status.setMessageUIID("lblToastBar");
        status.setIcon(img);
        status.setExpires(2000);
        //status.show();
        return status;
    }

    public String[] getL10N() {
        String[] arr = {"Change Language>English>Change Language>Swahili>Badili Lugha>Sheng>Chuja Lugha",
            "Name>English>Name>Swahili>Jina>Sheng>Naji",
            "Age>English>Age>Swahili>Miaka>Sheng>Kamia"
        };
        return arr;
    }

    public HashMap changeLang(String lang) {

        HashMap<String, String> resBundle = new HashMap<>();

        for (String tsl : getL10N()) {
            //Name>English>Name>Swahili>Jina>Sheng>Naji
            String[] tslArr = splitValue(tsl, ">");
            switch (lang) {
                case "English":
                    resBundle.put(tslArr[0], tslArr[2]);
                    break;
                case "Swahili":
                    resBundle.put(tslArr[0], tslArr[4]);
                    break;
                case "Sheng":
                    resBundle.put(tslArr[0], tslArr[6]);
                    break;
            }
        }

        return resBundle;
    }

    private void store(String key, String value) {
        Preferences.set(key, value);
    }

    private String getValue(String key) {
        return Preferences.get(key, null);
    }

    public void setCurrentLang(String value) {
        store("CurrentLang", value);
    }

    public String getCurrentLang() {
        return getValue("CurrentLang");
    }

    public void setIsRTL(String value) {
        store("IsRTL", value);
    }

    public String getIsRTL() {
        return getValue("IsRTL");
    }

    public void setSenderPhone(String value) {
        store("SenderPhone", value);
    }

    public String getSenderPhone() {
        return getValue("SenderPhone");
    }

    public void setSenderName(String value) {
        store("SenderName", value);
    }

    public String getSenderName() {
        return getValue("SenderName");
    }

    public String getCurrDate() {
        //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm:ss a");
        /*DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
        LocalDateTime now = LocalDateTime.now();
        return formatter.format(now);*/

        DateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
        return dateFormat.format(new Date());
    }

    public String getCurrTime() {
        /*DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalDateTime now = LocalDateTime.now();
        return formatter.format(now);*/

        DateFormat dateFormat = new SimpleDateFormat("HH:mm");
        return dateFormat.format(new Date());
    }

    public String checkDate(String msgDate) {

        String date;
        String[] dateArr = splitValue(msgDate, " ");

        DateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
        String currDate = dateFormat.format(new Date());
        String[] currDateArr = splitValue(currDate, " ");

        //Check same month & year
        if (dateArr[1].trim().equals(currDateArr[1].trim())
                && dateArr[2].trim().equals(currDateArr[2].trim())) {

            //09 Aug 2021
            //TODAY
            if (dateArr[0].trim().equals(currDateArr[0].trim())) {
                date = "TODAY";

                //YESTERDAY
                //08 Aug 2021 09 Aug 2021
            } else if (Integer.parseInt(dateArr[0].trim())
                    == Integer.parseInt(currDateArr[0].trim()) - 1) {
                date = "YESTERDAY";
            } else {
                date = msgDate;
            }

        } else {
            date = msgDate;
        }

        return date;
    }

    public void setGenSKey(String value) {
        store("GenSKey", value);
    }

    public String getGenSKey() {
        return getValue("GenSKey");
    }

    public void setGenIV(String value) {
        store("GenIV", value);
    }

    public String getGenIV() {
        return getValue("GenIV");
    }

    public void setMapKey(String value) {
        store("MapKey", value);
    }

    public String getMapKey() {
        
         String mapKey;
        try {
            if (getValue("MapKey") != null || !getValue("MapKey").equals("")) {
                mapKey = getValue("MapKey");
            } else {
                mapKey = "None";
            }
        } catch (Exception e) {
            mapKey = "None";
        }
        return mapKey;
    }
    
    public void setDarkMode(String value) {
        store("DarkMode", value);
    }

    public String getDarkMode() {
        String mode;
        try {
            if (getValue("DarkMode") != null || !getValue("DarkMode").equals("")) {
                mode = getValue("DarkMode");
            } else {
                mode = "No";
            }
        } catch (Exception e) {
            mode = "No";
        }
        return mode;
    }

    public void setSideMenuOpen(String value) {
        store("SideMenuOpen", value);
    }

    public String getSideMenuOpen() {
        String open;
        try {
            if (getValue("SideMenuOpen") != null || !getValue("SideMenuOpen").equals("")) {
                open = getValue("SideMenuOpen");
            } else {
                open = "No";
            }
        } catch (Exception e) {
            open = "No";
        }
        return open;
    }

    public boolean clearStore(String fileName, String tempFileName) {
        boolean clear = false;

        Storage store = Storage.getInstance();

        printLine("\n\nStore " + store);
        printLine("StoreLen1 " + store.listEntries().length);

        //store.clearStorage();
        // showException("Store Len2 " + store.listEntries().length);
//        if (store.listEntries().length == 0) {
//            printLine("All cleared ");
//            clear = true;
//        } else 
        if (store.listEntries().length > 0) {
            for (String str : store.listEntries()) {
                printLine("Store Len2 " + Storage.getInstance().listEntries().length);

                if (str.equals(fileName)) {
                    store.deleteStorageFile(str);
                    printLine("Del Local Item " + str);
                }
                if (str.equals(tempFileName)) {
                    store.deleteStorageFile(str);
                    printLine("Del Local Item " + str);
                }

            }
            /*Storage.getInstance().clearStorage();
            Storage store2 = Storage.getInstance();
            //store2.clearStorage();
            showException("StoreLen2 " + store2.listEntries().length);*/

            //printLine("All cleared ");
            clear = true;
        }

        return clear;
    }

    public String addCCode(String phone) {
        phone = StringUtil.replaceAll(phone, "+", "");
        if (phone.startsWith("0")) {
            phone = phone.substring(1);
            phone = "254".concat(phone);
        }
        return phone;
    }

    public Form confirmForm(String msg, Form curr) {

        Form form = new Form("Confirmation", new BorderLayout());
        //form.getAllStyles().setBgColor(0xffffff);
        form.getToolbar().setUIID("tbar");
        form.getToolbar().getTitleComponent().setUIID(getLblTitleUIID());

        final InteractionDialog dialog
                = new InteractionDialog(new BorderLayout());
        dialog.getTitleComponent().remove();
        dialog.setDialogUIID("statusDialog");

        btnOk = new Button("OK", "btnDialog");
        Button btnCancel = new Button("Cancel", "btnCancel");

        Container cntBtn = new Container(new GridLayout(1, 3));
        cntBtn.addAll(btnCancel, new Label(""), btnOk);

        Container cntSuccess = new Container(BoxLayout.y(), "cntSuccess");

        cntSuccess.add(FlowLayout.encloseCenterMiddle(new SpanLabel(msg,
                "lblStatus")));
        cntSuccess.add(FlowLayout.encloseCenterBottom(cntBtn));
        dialog.addComponent(BorderLayout.CENTER, FlowLayout.encloseCenterMiddle(cntSuccess));

        form.add(BorderLayout.CENTER, FlowLayout.encloseCenterMiddle(dialog));

        btnCancel.addActionListener(e -> {
            dialog.dispose();
            curr.showBack();

        });
        return form;
    }

    public Form progressForm(String msg) {

        String[] msgArr = splitValue(msg, " ");
        Form form = new Form(msgArr[0], new BorderLayout());
        //form.getAllStyles().setBgColor(0xffffff);
        form.getToolbar().setUIID("tbar");
        form.getToolbar().getTitleComponent().setUIID(getLblTitleUIID());

        Container cnt = new Container(BoxLayout.y());
        SpanLabel lbl = new SpanLabel(msg, "lblStatus");
        cnt.add(FlowLayout.encloseCenterMiddle(lbl));

//        GifImage img = null;
//        try {
//            img = GifImage.decode(getResourceAsStream("/acquire_anim.gif"),
//                    20480);
//        } catch (IOException e) {
//
//        }
//        ScaleImageLabel lblAnim = new ScaleImageLabel(img);
//        lblAnim.setUIID("lblAnim");
        //cnt.add(FlowLayout.encloseCenterMiddle(lblAnim));
        SpanLabel lblName = new SpanLabel("placeholder", "CenterAlignmentLabel");

        switch (Display.getInstance().getPlatformName()) {
            case "and":
            case "ios":
                circleSize = 50;
                break;
            case "win":
                circleSize = 200;
                break;
        }

        Container cntName = new Container(new BorderLayout()) {
            @Override
            protected Dimension calcPreferredSize() {
                return new Dimension(circleSize, circleSize);
            }
        };

        cntName.add(BorderLayout.CENTER, lblName);
        Container cntAnim = BorderLayout.centerCenter(cntName);
        //replace the label with a CircleProgress to indicate that it is loading
        CircleProgress.markComponentLoading(lblName).setUIID("CircleAnim");

        cnt.add(FlowLayout.encloseCenterMiddle(cntAnim));

        form.add(BorderLayout.CENTER, FlowLayout.encloseCenterMiddle(cnt));

        return form;
    }

    public Form statusForm(String msg, String status, Form curr, Form prev) {

        Form form = new Form("Response", new BorderLayout());
        //form.getAllStyles().setBgColor(0xffffff);
        form.getToolbar().setUIID("tbar");
        form.getToolbar().getTitleComponent().setUIID(getLblTitleUIID());

        final InteractionDialog dialog
                = new InteractionDialog(new BorderLayout());
        dialog.getTitleComponent().remove();
        dialog.setDialogUIID("statusDialog");

        Button btnClose = new Button("OK", "btnDialog");

        Container cntSuccess = new Container(BoxLayout.y(), "cntSuccess");

        Image icon = null;
        if (status.equals("S")) {

            //icon = proc.statusIcon(FontImage.MATERIAL_CHECK_CIRCLE, 0x15E7FF);
            icon = statusIcon(FontImage.MATERIAL_CHECK_CIRCLE_OUTLINE, 0x15E7FF);
        } else if (status.equals("F")) {
            icon = statusIcon(FontImage.MATERIAL_CANCEL, 0xFF0000);
        }

        cntSuccess.add(FlowLayout.encloseCenterMiddle(new Label(icon)));
        cntSuccess.add(FlowLayout.encloseCenterMiddle(new SpanLabel(msg,
                "lblStatus")));
        cntSuccess.add(FlowLayout.encloseCenterBottom(btnClose));
        dialog.addComponent(BorderLayout.CENTER, FlowLayout.encloseCenterMiddle(cntSuccess));

        form.add(BorderLayout.CENTER, FlowLayout.encloseCenterMiddle(dialog));

        btnClose.addActionListener(e -> {
            dialog.dispose();

            if (status.equals("F")) {
                curr.showBack();
            } else if (status.equals("S")) {
                prev.show();
            }

        });
        return form;

    }

    public String formatAmt(String amt) {
        L10NManager lm = L10NManager.getInstance();
        lm.setLocale("KE", "EN");
        amt = lm.format(Double.parseDouble(amt), 2);
        return amt;
    }

    public Slider createDownloadSlider() {
        Slider slider = new Slider();
        slider.setEditable(true);
        slider.setMinValue(0);
        slider.setMaxValue(1000);
        Font font = Font.createTrueTypeFont("native:MainLight",
                "native:MainLight").derive(Display.getInstance()
                        .convertToPixels(1, true), Font.STYLE_PLAIN);
        //fgcolor, bgcolor, font, transparency
        Style s = new Style(0x15E7FF, 0, font, (byte) 0); //0x00FBEB 0x15E7FF
        Image fullImg = FontImage.createMaterial(
                FontImage.MATERIAL_HORIZONTAL_RULE, s).toImage();
        s.setOpacity(255);
        s.setFgColor(0X0000FF); //0X3399ff 0X0000FF 0X0047AB
        Image emptyImg = FontImage.createMaterial(
                FontImage.MATERIAL_HORIZONTAL_RULE, s).toImage();

        initDwdSliderStyle(slider.getSliderEmptySelectedStyle(), emptyImg);
        initDwdSliderStyle(slider.getSliderEmptyUnselectedStyle(), emptyImg);
        initDwdSliderStyle(slider.getSliderFullSelectedStyle(), fullImg);
        initDwdSliderStyle(slider.getSliderFullUnselectedStyle(), fullImg);

        slider.setPreferredSize(new Dimension(fullImg.getWidth() * 5,
                fullImg.getHeight()));

        return slider;
    }

    private void initDwdSliderStyle(Style s, Image img) {
        s.setBackgroundType(Style.BACKGROUND_IMAGE_SCALED_FILL);
        s.setBorder(Border.createEmpty());
        s.setBgImage(img);
        s.setBgTransparency(0);
    }

    public String getFileExtMime() {
        return ".pdf, application/pdf, "
                + ".docx, application/vnd.openxmlformats-officedocument.wordprocessingml.document,"
                + ".pptx, application/vnd.openxmlformats-officedocument.presentationml.presentation,"
                + ".pub, application/x-mspublisher, "
                + ".xlsx, application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                + ".txt, text/plain,.gif, image/gif,.png, image/png, .jpg, image/jpg, .tif, image/tif,"
                + ".jpeg, image/jpeg .apk, application/vnd.android.package-archive,"
                + " .mp3, audio/mpeg, .mp4, video/mp4, .mkv, video/x-, */*,"
                + ".xls, application/vnd.ms-excel,"
                + ".xlsb, application/vnd.ms-excel.sheet.binary.macroEnabled.12,"
                + ".xlsm, application/vnd.ms-excel.sheet.macroEnabled.12,"
                + "application/msexcel, application/x-msexcel, application/x-ms-excel,"
                + "application/x-excel, application/x-dos_ms_excel, application/xls,"
                + "application/x-xls";
    }

    public String getMimeType(String ext) {

        String mime = null;

        switch (ext) {
            case "png":
            case "PNG":
            case "jpg":
            case "JPG":
            case "jpeg":
            case "JPEG":
            case "gif":
            case "GIF":
            case "tif":
            case "TIF":
                mime = "image/" + ext;
                break;

            case "txt":
                mime = "plain/text";
                break;

            case "pdf":
                mime = "application/pdf";
                break;

            case "docx":
                mime = "application/vnd.openxmlformats-"
                        + "officedocument.wordprocessingml.document";
                break;

            case "pptx":
                mime = "application/vnd.openxmlformats-"
                        + "officedocument.presentationml.presentation";
                break;

            case "xlsx":
                mime = "application/vnd.openxmlformats-"
                        + "officedocument.spreadsheetml.sheet";
                break;

            case "xls":
                mime = "application/vnd.ms-excel";
                break;

            case "pub":
                mime = "application/x-mspublisher";
                break;

            case "apk":
                mime = "application/vnd.android.package-archive";
                break;

            case "mp3":
            case "MP3":
                mime = "audio/mpeg";
                break;

            case "mp4":
            case "MP4":
                mime = "video/mp4";
                break;

            case "mkv":
            case "MKV":
                mime = "video/x-";
                break;
        }
        return mime;
    }

    public Image scaleIcon(Image img) {
        /*img = img.scaledWidth(Math.round(Display.getInstance()
                .getDisplayWidth()/10));*/
        String platName = Display.getInstance().getPlatformName();
        //showException("Platform = " + platName);

        switch (platName) {
            case "and":
                img = img.scaledWidth(40).scaledHeight(40);
                break;
            case "ios":
                img = img.scaledWidth(50).scaledHeight(50);
                break;
            case "win":
                img = img.scaledWidth(80).scaledHeight(80);
                break;
        }
        return img;
    }

    public Image scaleDriveIcon(Image img) {
        /*img = img.scaledWidth(Math.round(Display.getInstance()
                .getDisplayWidth()/10));*/
        String platName = Display.getInstance().getPlatformName();
        //showException("Platform = " + platName);

        switch (platName) {
            case "and":
                img = img.scaledWidth(20).scaledHeight(20);
                break;
            case "ios":
                img = img.scaledWidth(25).scaledHeight(25);
                break;
            case "win":
                img = img.scaledWidth(40).scaledHeight(40);
                break;
        }

        return img;
    }

    public Button getUserImage(Image user) {

        int width = user.getWidth();
        Image roundMask = Image.createImage(width, width, yellow);
        Graphics gr = roundMask.getGraphics();
        gr.setColor(0xffffff);
        gr.fillArc(0, 0, width, width, 0, 360);

        Object mask = roundMask.createMask();
        user = user.applyMask(mask);
        return new Button(user, "btnToolbar");
    }

    //public Button getSquareCircleIcon(Image pic) {
    //public Container getSquareCircleIcon(Image pic) {
    public Image getSquareCircleIcon(Image pic) {

        Container cntSqrCircle = new Container(new BorderLayout(), "cntDsg5SqrCircle");
        int width = pic.getWidth();

        Image roundMask = Image.createImage(width, width, 0);
        Graphics gr = roundMask.getGraphics();
        gr.setAlpha(255);
        //gr.setColor(0xF251358F);
        /**
         * Fills a rounded rectangle in the same way as drawRoundRect
         *
         * @param x the x coordinate of the rectangle to be filled.
         * @param y the y coordinate of the rectangle to be filled.
         * @param width the width of the rectangle to be filled.
         * @param height the height of the rectangle to be filled.
         * @param arcWidth the horizontal diameter of the arc at the four
         * corners.
         * @param arcHeight the vertical diameter of the arc at the four
         * corners.
         */
        gr.fillRoundRect(0, 0, width, width, width / 2, width / 2);
        Object mask = roundMask.createMask();
        //pic = pic.applyMask(mask);
        pic = pic.applyMaskAutoScale(mask);
        //roundMask = roundMask.applyMask(mask);

        //return new Button(pic, "cntDsg5Icon");
        //cntSqrCircle.getAllStyles().setBgImage(pic);
        //return cntSqrCircle;
        return pic;
    }

    public Container getCustomBg(String uiid, int width, int height, int bgColor,
            int divideBy, int arcHeight) {

        Container cntCurve = new Container(new BorderLayout(), uiid);

        Image cntImg = Image.createImage(width, height, bgColor);
        Graphics cntGr = cntImg.getGraphics();
        cntGr.setColor(bgColor);
        //cntGr.setAlpha(255);

        cntGr.fillRoundRect(0, 0, width, height, width / divideBy, arcHeight);

        Object cntMask = cntImg.createMask();
        cntImg = cntImg.applyMask(cntMask);
        cntCurve.getAllStyles().setBgImage(cntImg);
        return cntCurve;
    }

    
    public void setNowPlaying(String value) {
        store("NowPlaying", value);
    }

    public String getNowPlaying() {
        return getValue("NowPlaying");
    }

    public void setNowPlayingSec(String value) {
        store("NowPlayingSec", value);
    }

    public String getNowPlayingSec() {
        return getValue("NowPlayingSec");
    }

    public String getCheckBoxUIID() {
        String uiid;
        if (Display.getInstance().isTablet()) {
            uiid = "chkTab";
        } else {
            uiid = "chk";
        }
        return uiid;
    }

    public String getRadioUIID() {
        String uiid;
        if (Display.getInstance().isTablet()) {
            uiid = "radTab";
        } else {
            uiid = "rad";
        }
        return uiid;
    }

    public Container getAdsImageContainer() {
        Container cntAdsImg = new Container(new BorderLayout(), "cntAdsImg") {
            @Override
            public Dimension calcPreferredSize() {
                return new Dimension(Display.getInstance().getDisplayWidth(),
                        Display.getInstance().getDisplayWidth());
            }
        };
        return cntAdsImg;
    }

    public String getResp() {
        return "[\n"
                + "  {\n"
                + "    \"postId\": 1,\n"
                + "    \"id\": 1,\n"
                + "    \"name\": \"id labore ex et quam laborum\",\n"
                + "    \"email\": \"Eliseo@gardner.biz\",\n"
                + "    \"body\": \"laudantium enim quasi est quidem magnam voluptate ipsam eos\\ntempora quo necessitatibus\\ndolor quam autem quasi\\nreiciendis et nam sapiente accusantium\"\n"
                + "  },\n"
                + "  {\n"
                + "    \"postId\": 1,\n"
                + "    \"id\": 2,\n"
                + "    \"name\": \"quo vero reiciendis velit similique earum\",\n"
                + "    \"email\": \"Jayne_Kuhic@sydney.com\",\n"
                + "    \"body\": \"est natus enim nihil est dolore omnis voluptatem numquam\\net omnis occaecati quod ullam at\\nvoluptatem error expedita pariatur\\nnihil sint nostrum voluptatem reiciendis et\"\n"
                + "  },\n"
                + "  {\n"
                + "    \"postId\": 1,\n"
                + "    \"id\": 3,\n"
                + "    \"name\": \"odio adipisci rerum aut animi\",\n"
                + "    \"email\": \"Nikita@garfield.biz\",\n"
                + "    \"body\": \"quia molestiae reprehenderit quasi aspernatur\\naut expedita occaecati aliquam eveniet laudantium\\nomnis quibusdam delectus saepe quia accusamus maiores nam est\\ncum et ducimus et vero voluptates excepturi deleniti ratione\"\n"
                + "  },\n"
                + "  {\n"
                + "    \"postId\": 1,\n"
                + "    \"id\": 4,\n"
                + "    \"name\": \"alias odio sit\",\n"
                + "    \"email\": \"Lew@alysha.tv\",\n"
                + "    \"body\": \"non et atque\\noccaecati deserunt quas accusantium unde odit nobis qui voluptatem\\nquia voluptas consequuntur itaque dolor\\net qui rerum deleniti ut occaecati\"\n"
                + "  },\n"
                + "  {\n"
                + "    \"postId\": 1,\n"
                + "    \"id\": 5,\n"
                + "    \"name\": \"vero eaque aliquid doloribus et culpa\",\n"
                + "    \"email\": \"Hayden@althea.biz\",\n"
                + "    \"body\": \"harum non quasi et ratione\\ntempore iure ex voluptates in ratione\\nharum architecto fugit inventore cupiditate\\nvoluptates magni quo et\"\n"
                + "  },\n"
                + "  {\n"
                + "    \"postId\": 2,\n"
                + "    \"id\": 6,\n"
                + "    \"name\": \"et fugit eligendi deleniti quidem qui sint nihil autem\",\n"
                + "    \"email\": \"Presley.Mueller@myrl.com\",\n"
                + "    \"body\": \"doloribus at sed quis culpa deserunt consectetur qui praesentium\\naccusamus fugiat dicta\\nvoluptatem rerum ut voluptate autem\\nvoluptatem repellendus aspernatur dolorem in\"\n"
                + "  },\n"
                + "  {\n"
                + "    \"postId\": 2,\n"
                + "    \"id\": 7,\n"
                + "    \"name\": \"repellat consequatur praesentium vel minus molestias voluptatum\",\n"
                + "    \"email\": \"Dallas@ole.me\",\n"
                + "    \"body\": \"maiores sed dolores similique labore et inventore et\\nquasi temporibus esse sunt id et\\neos voluptatem aliquam\\naliquid ratione corporis molestiae mollitia quia et magnam dolor\"\n"
                + "  },\n"
                + "  {\n"
                + "    \"postId\": 2,\n"
                + "    \"id\": 8,\n"
                + "    \"name\": \"et omnis dolorem\",\n"
                + "    \"email\": \"Mallory_Kunze@marie.org\",\n"
                + "    \"body\": \"ut voluptatem corrupti velit\\nad voluptatem maiores\\net nisi velit vero accusamus maiores\\nvoluptates quia aliquid ullam eaque\"\n"
                + "  },\n"
                + "  {\n"
                + "    \"postId\": 2,\n"
                + "    \"id\": 9,\n"
                + "    \"name\": \"provident id voluptas\",\n"
                + "    \"email\": \"Meghan_Littel@rene.us\",\n"
                + "    \"body\": \"sapiente assumenda molestiae atque\\nadipisci laborum distinctio aperiam et ab ut omnis\\net occaecati aspernatur odit sit rem expedita\\nquas enim ipsam minus\"\n"
                + "  },\n"
                + "  {\n"
                + "    \"postId\": 2,\n"
                + "    \"id\": 10,\n"
                + "    \"name\": \"eaque et deleniti atque tenetur ut quo ut\",\n"
                + "    \"email\": \"Carmen_Keeling@caroline.name\",\n"
                + "    \"body\": \"voluptate iusto quis nobis reprehenderit ipsum amet nulla\\nquia quas dolores velit et non\\naut quia necessitatibus\\nnostrum quaerat nulla et accusamus nisi facilis\"\n"
                + "  },\n"
                + "  {\n"
                + "    \"postId\": 3,\n"
                + "    \"id\": 11,\n"
                + "    \"name\": \"fugit labore quia mollitia quas deserunt nostrum sunt\",\n"
                + "    \"email\": \"Veronica_Goodwin@timmothy.net\",\n"
                + "    \"body\": \"ut dolorum nostrum id quia aut est\\nfuga est inventore vel eligendi explicabo quis consectetur\\naut occaecati repellat id natus quo est\\nut blanditiis quia ut vel ut maiores ea\"\n"
                + "  },\n"
                + "  {\n"
                + "    \"postId\": 3,\n"
                + "    \"id\": 12,\n"
                + "    \"name\": \"modi ut eos dolores illum nam dolor\",\n"
                + "    \"email\": \"Oswald.Vandervort@leanne.org\",\n"
                + "    \"body\": \"expedita maiores dignissimos facilis\\nipsum est rem est fugit velit sequi\\neum odio dolores dolor totam\\noccaecati ratione eius rem velit\"\n"
                + "  },\n"
                + "  {\n"
                + "    \"postId\": 3,\n"
                + "    \"id\": 13,\n"
                + "    \"name\": \"aut inventore non pariatur sit vitae voluptatem sapiente\",\n"
                + "    \"email\": \"Kariane@jadyn.tv\",\n"
                + "    \"body\": \"fuga eos qui dolor rerum\\ninventore corporis exercitationem\\ncorporis cupiditate et deserunt recusandae est sed quis culpa\\neum maiores corporis et\"\n"
                + "  },\n"
                + "  {\n"
                + "    \"postId\": 3,\n"
                + "    \"id\": 14,\n"
                + "    \"name\": \"et officiis id praesentium hic aut ipsa dolorem repudiandae\",\n"
                + "    \"email\": \"Nathan@solon.io\",\n"
                + "    \"body\": \"vel quae voluptas qui exercitationem\\nvoluptatibus unde sed\\nminima et qui ipsam aspernatur\\nexpedita magnam laudantium et et quaerat ut qui dolorum\"\n"
                + "  },\n"
                + "  {\n"
                + "    \"postId\": 3,\n"
                + "    \"id\": 15,\n"
                + "    \"name\": \"debitis magnam hic odit aut ullam nostrum tenetur\",\n"
                + "    \"email\": \"Maynard.Hodkiewicz@roberta.com\",\n"
                + "    \"body\": \"nihil ut voluptates blanditiis autem odio dicta rerum\\nquisquam saepe et est\\nsunt quasi nemo laudantium deserunt\\nmolestias tempora quo quia\"\n"
                + "  }\n"
                + "]";
    }
}
