/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zomuhtech.cn.features.advft.designs;

import com.codename1.components.SpanLabel;
import com.codename1.ui.Button;
import static com.codename1.ui.Component.CENTER;
import com.codename1.ui.Container;
import com.codename1.ui.Display;
import com.codename1.ui.FontImage;
import com.codename1.ui.Form;
import com.codename1.ui.Graphics;
import com.codename1.ui.Image;
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
import com.codename1.util.StringUtil;
import com.zomuhtech.cn.features.procs.Proc;
import java.io.IOException;

/**
 *
 * @author Eric
 */
public class Design2Statement extends Form {

    Form form, prevForm;
    Proc proc;
    int selCount;
    String statement = "Income#Net Salary#01 Jan 2019#KES 80,000.00!"
            + "Bill#Electricity Bill#02 Jan 2019#KES 500.00!"
            + "Transfer#EFT to Joy Ann#05 Jan 2019#KES 10,000.00!"
            + "Payment#Airtime purchase#21 Feb 2019#KES 10,200.00!"
            + "Bill#Water Bill#06 Jan 2019#KES 11,000.00!"
            + "Transfer#RTGS to Taylor Mac#2 Feb 2019#KES 15,000.00!";

    public Design2Statement(Form form) {
        this.prevForm = form;
        proc = new Proc();

        Display.getInstance().callSerially(() -> {
            createUI();
        });

    }

    public void createUI() {

        form = proc.getDesignForm("2.1", "Statement", prevForm, proc.navyBlue,
                proc.navyBlue);
        form.setLayout(new BorderLayout());

        Container cntTop = new Container(BoxLayout.y(), "cntTop");

        Container cntUser = new Container(BoxLayout.x(), "cntUser");
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

        //Container cntGraphPar = new Container();
        Container cntGraph = new Container(new GridLayout(1, 2));
        //cntGraph.add(new Design2Chart(form).getForm());
        cntGraph.addAll(FlowLayout.encloseCenterMiddle(getTrxSummary()),
                FlowLayout.encloseCenterMiddle(getTrxKey()));

        //cntGraphPar.add(cntGraph);
        //cntTop.addAll(cntUser, cntGraph);
        cntTop.add(cntGraph);

        form.add(NORTH, cntTop);

        Container cntDesign2Par = new Container(new LayeredLayout(), "cntDesign2Par");
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

        Container cntStmtPar = new Container(BoxLayout.y(), "cntCardPar");
        cntStmtPar.setScrollableY(true);
        cntStmtPar.setScrollVisible(false);

        TableLayout tlExpen = new TableLayout(1, 2);
        Container cntExpen = new Container(tlExpen);
        cntExpen.add(tlExpen.cc().widthPercentage(80), new Label("My Income & Expenses",
                "lblMyCards"));
        Button btnList = new Button(proc.customIcon(FontImage.MATERIAL_LIST,
                proc.white, 2), "btnList");
        cntExpen.add(tlExpen.cc().widthPercentage(20),
                FlowLayout.encloseCenterMiddle(btnList));
        cntStmtPar.add(cntExpen);

        String[] stmtArr = proc.splitValue(statement, "!");

        for (int f = 0; f <= 5; f++) {

            String[] stmtAr = proc.splitValue(stmtArr[f], "#");

            if (stmtAr.length > 1) {

                TableLayout tlStmt = new TableLayout(1, 3);
                Container cntStmt = new Container(tlStmt, "cntFile");
                cntStmt.getAllStyles().setBgColor(proc.white);

                Container cntLbl = new Container(BoxLayout.y());
                String title = stmtAr[0], desc = stmtAr[1], date = stmtAr[2],
                        amt = stmtAr[3];

                Image roundMask = Image.createImage(40, 40, 0);
                Graphics gr = roundMask.getGraphics();

                switch (title) {
                    case "Income":
                        gr.setColor(proc.purple); // income
                        break;
                    case "Bill":
                        gr.setColor(proc.yellow); //bills
                        break;
                    case "Transfer":
                        gr.setColor(proc.pink); //transfers
                        break;
                    case "Payment":
                        gr.setColor(proc.skyBlue);//payments
                        break;
                }
                gr.fillArc(0, 0, 40, 40, 0, 360);

                Object mask = roundMask.createMask();
                roundMask.applyMask(mask);

                //Label lblStmtIcon = new Label(roundMask, "lblStmtIcon");
                Label lblStmtIcon = new Label(roundMask);

                cntStmt.add(tlStmt.createConstraint().widthPercentage(15),
                        FlowLayout.encloseCenterMiddle(lblStmtIcon));
                cntLbl.add(new Label(title, "lblFilesTitle"));
                Label lblDesc = new Label(desc, "lblFiles");
                lblDesc.getAllStyles().setFgColor(proc.navyBlue);
                cntLbl.add(lblDesc);
                cntStmt.add(tlStmt.createConstraint().widthPercentage(50),
                        FlowLayout.encloseLeftMiddle(cntLbl));

                Container cntDateAmt = new Container(BoxLayout.y());
                cntDateAmt.add(new Label(date, "lblDate"));
                Label lblAmt = new Label(amt, "lblDate");
                lblAmt.getAllStyles().setFgColor(proc.black);
                cntDateAmt.add(lblAmt);

                cntStmt.add(tlStmt.createConstraint().widthPercentage(35),
                        FlowLayout.encloseRightMiddle(cntDateAmt));
                cntStmtPar.add(cntStmt);
            }
        }

        cntDesign2BgContent.add(CENTER, cntStmtPar);
        cntDesign2Par.addAll(cntDesign2Bg, cntDesign2BgContent);

        form.add(CENTER, cntDesign2Par);

        Container cntBottomPar = new Container(new LayeredLayout(), "cntBottomPar");

        //add smooth curve bg container - add several times for navy blue color to match
        //graphics reduce navy blue color opacity
        for (int n = 0; n <= 6; n++) {
            cntBottomPar.add(proc.getCustomBg("cntSmoothCurve1",
                    Display.getInstance().getDisplayWidth(), 100, proc.navyBlue, 4, 60));
        }

        Container cntBottomMenus = new Container(new GridLayout(1, 4), "cntBottomMenus");

        if (proc.getDarkMode().equals("On")) {
            //add smooth curve bg container - add several times for blue gray color to match
            //graphics reduce blue gray color opacity
            for (int n = 0; n <= 8; n++) {
                cntBottomPar.add(proc.getCustomBg("cntSmoothCurve1",
                        Display.getInstance().getDisplayWidth(), 100, proc.blueGray, 4, 60));
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

        cntBottomPar.add(cntBottomMenus);

        form.add(SOUTH, cntBottomPar);

        SwipeBackSupport.bindBack(form, (args) -> {
            return prevForm.getComponentForm();
        });

        form.show();
    }

    private Container getTrxSummary() {

        Container cntParTrx = new Container(new BorderLayout());
        int dimen1 = 150, dimen2 = 120;
        Container cntPar = new Container(BoxLayout.y());
        Container cntMask = new Container(new LayeredLayout());

        Image roundMask = Image.createImage(dimen1, dimen1, 0);
        Graphics gr = roundMask.getGraphics();

        int incomeAmt = 0, billsAmt = 0, payAmt = 0, transferAmt = 0,
                totalAmt;
        String[] stmtArr = proc.splitValue(statement, "!");

        for (String stmt : stmtArr) {
            //"Income#Net Salary#01 Jan 2019#KES 100,000.00!"
            String[] stmtAr = proc.splitValue(stmt, "#");

            if (stmtAr.length > 1) {

                String amt = StringUtil.replaceAll(stmtAr[3], "KES ", "");
                amt = StringUtil.replaceAll(amt, ",", "");
                String[] amtArr = proc.splitValue(amt, ".");
                int amtValue = Integer.parseInt(amtArr[0]); // 100000

                switch (stmtAr[0]) {
                    case "Income":
                        incomeAmt = incomeAmt + amtValue;
                        break;
                    case "Bill":
                        billsAmt = billsAmt + amtValue;
                        break;
                    case "Transfer":
                        transferAmt = transferAmt + amtValue;
                        break;
                    case "Payment":
                        payAmt = payAmt + amtValue;
                        break;
                }
            }
        }
        //proc.printLine("Total Income=" + incomeAmt + "\nTotal Bills=" + billsAmt
        //  + "\nTotal Transfers=" + transferAmt + "\nTotal Payments=" + payAmt);

        totalAmt = incomeAmt + billsAmt + transferAmt + payAmt;
        //proc.printLine("Total Amts=" + totalAmt);

        gr.setColor(proc.purple);
        int incomeDegrees = (incomeAmt * 360) / totalAmt; //227
        gr.fillArc(0, 0, dimen1, dimen1, 0, incomeDegrees);

        gr.setColor(proc.yellow);
        int billsDegrees = (billsAmt * 360) / totalAmt; //32
        gr.fillArc(0, 0, dimen1, dimen1, incomeDegrees, billsDegrees);

        gr.setColor(proc.pink);
        int transferDegrees = (transferAmt * 360) / totalAmt;//71
        gr.fillArc(0, 0, dimen1, dimen1, incomeDegrees + billsDegrees, transferDegrees);

        gr.setColor(proc.skyBlue);
        int payDegrees = (payAmt * 360) / totalAmt;//28
        gr.fillArc(0, 0, dimen1, dimen1, incomeDegrees + billsDegrees + transferDegrees, payDegrees);

        Object mask = roundMask.createMask();
        roundMask.applyMask(mask);
        Label lbl = new Label(roundMask);
        cntMask.add(FlowLayout.encloseCenterMiddle(lbl));

        Image roundMask2 = Image.createImage(dimen2, dimen2, 0);
        Graphics gr2 = roundMask2.getGraphics();

        if (proc.getDarkMode().equals("On")) {
            gr2.setColor(proc.blueGray);
        } else {
            gr2.setColor(proc.navyBlue);
        }

        gr2.fillArc(0, 0, dimen2, dimen2, 0, 360);
        Object mask2 = roundMask2.createMask();
        roundMask2.applyMask(mask2);
        Label lbl2 = new Label(roundMask2);

        cntMask.add(FlowLayout.encloseCenterMiddle(lbl2));
        cntMask.add(FlowLayout.encloseCenterMiddle(new SpanLabel("Account\nSummary", "lblSummary")));
        cntPar.add(cntMask);

        cntParTrx.add(CENTER, cntPar);

        return cntParTrx;
    }

    private Container getTrxKey() {

        Container cntParTrx = new Container(new BorderLayout());
        int dimen1 = 30, dimen2 = 15;
        Container cntPar = new Container(BoxLayout.y());

        String key = null;
        for (int k = 0; k <= 3; k++) {

            Container cntKey = new Container(BoxLayout.x());
            Container cntMask = new Container(new LayeredLayout());
            Image roundMask = Image.createImage(dimen1, dimen1, 0);
            Graphics gr = roundMask.getGraphics();

            switch (k) {
                case 0:
                    gr.setColor(proc.purple); // income
                    key = "Incomes";
                    break;
                case 1:
                    gr.setColor(proc.yellow); //bills
                    key = "Bills";
                    break;
                case 2:
                    gr.setColor(proc.pink); //transfers
                    key = "Transfers";
                    break;
                case 3:
                    gr.setColor(proc.skyBlue);//payments
                    key = "Payments";
                    break;
            }
            gr.fillArc(0, 0, dimen1, dimen1, 0, 360);

            Object mask = roundMask.createMask();
            roundMask.applyMask(mask);
            Label lbl = new Label(roundMask);
            cntMask.add(FlowLayout.encloseCenterMiddle(lbl));

            Image roundMask2 = Image.createImage(dimen2, dimen2, 0);
            Graphics gr2 = roundMask2.getGraphics();

            if (proc.getDarkMode().equals("On")) {
                gr2.setColor(proc.blueGray);
            } else {
                gr2.setColor(proc.navyBlue);
            }

            gr2.fillArc(0, 0, dimen2, dimen2, 0, 360);
            Object mask2 = roundMask2.createMask();
            roundMask2.applyMask(mask2);
            Label lbl2 = new Label(roundMask2);

            cntMask.add(FlowLayout.encloseCenterMiddle(lbl2));
            Label lblKey = new Label(key, "lblWelcome");
            lblKey.getAllStyles().setFgColor(proc.white);
            cntKey.addAll(cntMask, lblKey);
            cntPar.add(cntKey);
        }

        cntParTrx.add(CENTER, cntPar);

        return cntParTrx;

    }

}
