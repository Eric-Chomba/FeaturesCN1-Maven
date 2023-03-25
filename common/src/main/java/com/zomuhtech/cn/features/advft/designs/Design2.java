/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zomuhtech.cn.features.advft.designs;

import com.codename1.ui.Button;
import static com.codename1.ui.Component.CENTER;
import com.codename1.ui.Container;
import com.codename1.ui.Display;
import com.codename1.ui.FontImage;
import com.codename1.ui.Form;
import com.codename1.ui.Image;
import static com.codename1.ui.Image.createImage;
import com.codename1.ui.Label;
import com.codename1.ui.layouts.BorderLayout;
import static com.codename1.ui.layouts.BorderLayout.EAST;
import static com.codename1.ui.layouts.BorderLayout.NORTH;
import static com.codename1.ui.layouts.BorderLayout.SOUTH;
import static com.codename1.ui.layouts.BorderLayout.WEST;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.FlowLayout;
import com.codename1.ui.layouts.GridLayout;
import com.codename1.ui.layouts.LayeredLayout;
import com.codename1.ui.table.TableLayout;
import com.codename1.ui.util.SwipeBackSupport;
import com.zomuhtech.cn.features.procs.Proc;
import java.io.IOException;

/**
 *
 * @author Eric
 */
public class Design2 extends Form {

    Form form, prevForm;
    Proc proc;
    int selCount;

    public Design2(Form form) {
        this.prevForm = form;
        proc = new Proc();

        Display.getInstance().callSerially(() -> {
            createUI();
        });

    }

    public void createUI() {

        form = proc.getDesignForm("2", "Account", prevForm, proc.navyBlue,
                proc.navyBlue);
        form.setLayout(new BorderLayout());

        Container cntTop = new Container(BoxLayout.y(), "cntTop");

        /*Container cntUser = new Container(BoxLayout.x(), "cntUser");
        Button btnUserImg = new Button(proc.menuIcon(
                FontImage.MATERIAL_ACCOUNT_CIRCLE), "btnUserImg");
        Container cntUserLbl = new Container(BoxLayout.y());
        Label lbl = new Label("Welcome", "lblWelcome");
        lbl.getAllStyles().setFgColor(proc.white);
        Label lblName = new Label("John Doe", "lblUserName");
        lblName.getAllStyles().setFgColor(proc.colorTeal);
        cntUserLbl.addAll(lbl, lblName);
        cntUser.addAll(FlowLayout.encloseLeftMiddle(btnUserImg),
                FlowLayout.encloseLeftMiddle(cntUserLbl));
        //form.getToolbar().add(CENTER, FlowLayout.encloseLeftMiddle(cntUser));*/
        Container cntBack = new Container(BoxLayout.x());
        Button btnBack = new Button(proc.materialIcon(FontImage.MATERIAL_ARROW_BACK),
                "btnToolbar");
        btnBack.addActionListener(e -> {
            prevForm.showBack();
        });

        try {
            cntBack.addAll(FlowLayout.encloseCenterMiddle(btnBack),
                    FlowLayout.encloseCenterMiddle(proc.getUserImage(
                            Image.createImage("/eric_40.jpg"))));
        } catch (IOException e) {
        }

        form.getToolbar().add(WEST, FlowLayout.encloseLeftMiddle(cntBack));
        Button btnNotify = new Button(proc.materialIcon(FontImage.MATERIAL_NOTIFICATIONS),
                "btnToolbar");
        btnNotify.addActionListener(e -> {
            proc.showToast("Notifications", FontImage.MATERIAL_NOTIFICATIONS).show();
        });
        form.getToolbar().add(EAST, FlowLayout.encloseRightMiddle(btnNotify));

        Label lblBal = new Label("Available Balance", "lblBal");
        Label lblAmt = new Label("KES 72,782.78", "lblAmt");

        TableLayout tlBtn = new TableLayout(1, 2);
        Container cntBtn = new Container(tlBtn);
        Button btnStmt = new Button("Statement", "cntMenuBtn");
        btnStmt.getAllStyles().setBgColor(proc.colorTeal);
        btnStmt.getAllStyles().setFgColor(proc.white);
        btnStmt.addActionListener(e -> {
            new Design2Statement(form).show();
        });
        cntBtn.add(tlBtn.cc().widthPercentage(50), btnStmt);
        Button btnMore = new Button("More", "cntMenuBtn");
        cntBtn.add(tlBtn.cc().widthPercentage(50), btnMore);

        //cntTop.addAll(cntUser, lblBal, lblAmt, cntBtn);
        cntTop.addAll(lblBal, lblAmt, cntBtn);

        form.add(NORTH, cntTop);

        // Form cntDesignBg = new Form();
        //cntDesignBg.getToolbar().hideToolbar();
        //cntDesignBg.setUIID("cntDesign2Bg");
        //cntDesignBg.setLayout(new BorderLayout());
        Container cntDesign2Par = new Container(new LayeredLayout(), "cntDesign2Par");
        //Container cntDesign2Bg = new Container(BoxLayout.y(), "cntDesign2Bg");

//        Container cntDesign2Bg = proc.getCustomBg("cntSmoothCurve1",
//                Display.getInstance().getDisplayWidth(),
//                Display.getInstance().getDisplayWidth(), proc.white, 4, 60);
        Container cntDesign2Bg;
        if (Display.getInstance().isPortrait()) {
            cntDesign2Bg = proc.getCustomBg("cntSmoothCurve1",
                    Display.getInstance().getDisplayWidth(),
                    Display.getInstance().getDisplayHeight(), proc.white, 4,
                    Display.getInstance().getDisplayHeight() / 14);
        } else {
            cntDesign2Bg = proc.getCustomBg("cntSmoothCurve1",
                    Display.getInstance().getDisplayWidth(),
                    Display.getInstance().getDisplayHeight(), proc.white, 4,
                    Display.getInstance().getDisplayHeight() / 7);
        }

        Container cntDesign2BgContent = new Container(new BorderLayout(), "cntDesign2BgContent");
        //cntDesign2BgContent.setScrollableY(true);

        Container cntCardPar = new Container(BoxLayout.y(), "cntCardPar");
        cntCardPar.setScrollableY(true);
        cntCardPar.setScrollVisible(false);

        TableLayout tlCards = new TableLayout(1, 2);
        Container cntCards = new Container(tlCards);
        cntCards.add(tlCards.cc().widthPercentage(80), new Label("My Cards",
                "lblMyCards"));
        Button btnAdd = new Button(proc.customIcon(
                FontImage.MATERIAL_ADD, proc.white, 2), "btnList");
        cntCards.add(tlCards.cc().widthPercentage(20),
                FlowLayout.encloseCenterMiddle(btnAdd));
        cntCardPar.add(cntCards);

        int cardCount = 0;
        for (int f = 0; f < 5; f++) {
            try {

                Container cntCard = new Container(BoxLayout.y(), "cntCard");
                TableLayout tlLogo = new TableLayout(1, 2);
                Container cntLogo = new Container(tlLogo, "cntLogo");
                if (cardCount == 0) {
                    cntCard.getAllStyles().setBgColor(0x1F51FF);
                    /*cntLogo.add(tlLogo.createConstraint().widthPercentage(50),
                            new Label(proc.scaleIcon(createImage("/mastercard_183_122.png")), "btnSubMenu"));*/
                    cntLogo.add(tlLogo.createConstraint().widthPercentage(50),
                            new Label(createImage("/mastercard_60_40.png"), "btnSubMenu"));
                    cardCount = 1;
                } else if (cardCount == 1) {
                    cntCard.getAllStyles().setBgColor(0x3399ff);
                    cntLogo.add(tlLogo.createConstraint().widthPercentage(50),
                            new Label(createImage("/visa_107_40.jpeg"), "btnSubMenu"));
                    cardCount = 0;
                }
                cntLogo.add(tlLogo.createConstraint().widthPercentage(50),
                        FlowLayout.encloseRightMiddle(new Label("1234 5678 9876 1255", "lblFiles")));
                cntCard.add(cntLogo);

                Container cntLbl = new Container(BoxLayout.y(), "cntLbl");
                cntLbl.add(new Label("John Doe", "lblFiles"));
                cntLbl.add(new Label("8927727727722323", "lblFiles"));
                cntCard.add(cntLbl);

                cntCardPar.add(cntCard);

            } catch (IOException e) {

            }
        }

        cntDesign2BgContent.add(CENTER, cntCardPar);
        cntDesign2Par.addAll(cntDesign2Bg, cntDesign2BgContent);

        form.add(CENTER, cntDesign2Par);

        Container cntBottomPar = new Container(new LayeredLayout(), "cntBottomPar");

        //add smooth curve bg container - add several times for navy blue color to match
        //graphics reduce navy blue color opacity
        for (int n = 0; n <= 6; n++) {
            cntBottomPar.add(proc.getCustomBg("cntSmoothCurve1",
                    Display.getInstance().getDisplayWidth(), 100, proc.navyBlue, 6, 40));
        }

        Container cntBottomMenus = new Container(new GridLayout(1, 4), "cntBottomMenus");
        //TableLayout tlMenus = new TableLayout(1, 4);
        //Container cntBottomMenus = new Container(tlMenus, "cntBottomMenus");

        if (proc.getDarkMode().equals("On")) {
            //add smooth curve bg container - add several times for blue gray color to match
            //graphics reduce blue gray color opacity
            for (int n = 0; n <= 15; n++) {
                cntBottomPar.add(proc.getCustomBg("cntSmoothCurve1",
                        Display.getInstance().getDisplayWidth(), 100, proc.blueGray, 6, 40));
            }
            cntBottomMenus.getAllStyles().setBgColor(proc.blueGray);
        }

        Button btnMenu1 = new Button(proc.customIcon(FontImage.MATERIAL_HOME, proc.white, 3),
                "btnBottomMenu");
        btnMenu1.addActionListener(e -> {
            proc.showToast("Home", FontImage.MATERIAL_INFO).show();
        });
        Button btnMenu2 = new Button(proc.customIcon(FontImage.MATERIAL_PAYMENT, proc.white, 3),
                "btnBottomMenu");
        btnMenu2.addActionListener(e -> {
            proc.showToast("Payment", FontImage.MATERIAL_INFO).show();
        });
        Button btnMenu3 = new Button(proc.customIcon(FontImage.MATERIAL_HISTORY, proc.white, 3),
                "btnBottomMenu");
        btnMenu3.addActionListener(e -> {
            proc.showToast("Statement", FontImage.MATERIAL_INFO).show();
        });
        Button btnMenu4 = new Button(proc.customIcon(FontImage.MATERIAL_MORE, proc.white, 3),
                "btnBottomMenu");
        btnMenu4.addActionListener(e -> {
            proc.showToast("More", FontImage.MATERIAL_INFO).show();
        });

        cntBottomMenus.addAll(FlowLayout.encloseCenterMiddle(btnMenu1),
                FlowLayout.encloseCenterMiddle(btnMenu2),
                FlowLayout.encloseCenterMiddle(btnMenu3),
                FlowLayout.encloseCenterMiddle(btnMenu4));
        /*cntBottomMenus
                .add(tlMenus.cc().widthPercentage(25).heightPercentage(20),
                        FlowLayout.encloseCenterMiddle(btnMenu1))
                .add(tlMenus.cc().widthPercentage(25).heightPercentage(20),
                        FlowLayout.encloseCenterMiddle(btnMenu2))
                .add(tlMenus.cc().widthPercentage(25).heightPercentage(20),
                        FlowLayout.encloseCenterMiddle(btnMenu3))
                .add(tlMenus.cc().widthPercentage(25).heightPercentage(20),
                        FlowLayout.encloseCenterMiddle(btnMenu4));*/

        //cntBottomPar.addAll(cntBottom, cntBottomMenus);
        cntBottomPar.add(cntBottomMenus);

        form.add(SOUTH, cntBottomPar);

        SwipeBackSupport.bindBack(form, (args) -> {
            return prevForm.getComponentForm();
        });

        form.show();
    }

}
