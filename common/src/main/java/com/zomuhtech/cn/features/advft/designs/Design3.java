/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zomuhtech.cn.features.advft.designs;

import com.codename1.components.SpanLabel;
import com.codename1.ui.Button;
import com.codename1.ui.Container;
import com.codename1.ui.Display;
import com.codename1.ui.FontImage;
import com.codename1.ui.Form;
import com.codename1.ui.Image;
import com.codename1.ui.Label;
import com.codename1.ui.layouts.BorderLayout;
import static com.codename1.ui.layouts.BorderLayout.SOUTH;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.FlowLayout;
import com.codename1.ui.layouts.GridLayout;
import com.codename1.ui.layouts.LayeredLayout;
import com.codename1.ui.table.TableLayout;
import com.zomuhtech.cn.features.procs.CustomSizedCmp;
import com.zomuhtech.cn.features.procs.Proc;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author Eric
 */
public class Design3 extends Form {

    Form form, prevForm;
    Proc proc;
    ArrayList<Button> btnArr;

    public Design3(Form form) {
        this.prevForm = form;
        proc = new Proc();
        this.btnArr = new ArrayList<>();

        Display.getInstance().callSerially(() -> {
            createUI();
        });

    }

    public void createUI() {

        form = proc.getDesignForm("3", "Home Screen", prevForm, proc.white, proc.oceanBlue);
        form.setLayout(new LayeredLayout());
        Container cntBgPar = new Container(new GridLayout(2, 1));

        Container cntBgImg = new Container(new BorderLayout(), "cntBgImg");
        try {
            cntBgImg.getAllStyles().setBgImage(Image.createImage("/bridge.jpeg"));
        } catch (IOException e) {
        }

        Container cntBottom = new Container(new BorderLayout());

        Container cntContent = new Container(BoxLayout.y(), "cntContent");

        Container cntDsg3MenuPar = new Container(new GridLayout(1, 4));

        for (int m = 0; m <= 3; m++) {
            // Container cntDsg3Menu = new Container(BoxLayout.y(), "cntDsg3Menu");

            FontImage menuIcon = null;
            switch (m) {
                case 0:
                    menuIcon = proc.customIcon(FontImage.MATERIAL_NOTES, proc.colorBlue, 3);
                    break;
                case 1:
                    menuIcon = proc.customIcon(FontImage.MATERIAL_CALENDAR_VIEW_MONTH, proc.colorBlue, 3);
                    break;
                case 2:
                    menuIcon = proc.customIcon(FontImage.MATERIAL_MUSIC_NOTE, proc.colorBlue, 3);
                    break;
                case 3:
                    menuIcon = proc.customIcon(FontImage.MATERIAL_GAMES, proc.colorBlue, 3);
                    break;
            }

            Button btnMenu = new Button(menuIcon, "btnDsg3Menu");
            btnMenu.setName("" + m);
            btnMenu.addActionListener(e -> {
                //switch (e.getActualComponent().getName()) {
                switch (btnMenu.getName()) {
                    case "0":
                        proc.showToast("Notes", FontImage.MATERIAL_NOTES).show();
                        break;
                    case "1":
                        proc.showToast("Calendar", FontImage.MATERIAL_CALENDAR_VIEW_MONTH).show();
                        break;
                    case "2":
                        proc.showToast("Music", FontImage.MATERIAL_MUSIC_NOTE).show();
                        break;
                    case "3":
                        proc.showToast("Games", FontImage.MATERIAL_GAMES).show();
                        break;
                }
            });
            cntDsg3MenuPar.add(FlowLayout.encloseCenterMiddle(btnMenu));
        }

        cntContent.addAll(cntDsg3MenuPar, new Label("Tuesday Jan 2019", "lblDsg3Date"),
                new SpanLabel("Quicky access your notes, calendar, music, games\n"
                        + "messages, emails, calls etc", "lblDsg3Desc"));

        cntBottom.add(CENTER, cntContent);

        Container cntDsg3BottomMenus = new Container(new GridLayout(1, 3), "cntDsg3BottomMenus");

        Button btnCall = new Button("CALL", "btnDsg3Bottom");
        btnArr.add(btnCall);
        btnCall.addActionListener(e -> {
            changeBtnUIID(btnArr, btnCall);
            proc.showToast("Calls", FontImage.MATERIAL_INFO_OUTLINE).show();
        });

        Button btnSms = new Button("SMS", "btnDsg3Bottom");
        btnArr.add(btnSms);
        btnSms.addActionListener(e -> {
            changeBtnUIID(btnArr, btnSms);
            proc.showToast("Sms", FontImage.MATERIAL_INFO_OUTLINE).show();
        });

        Button btnEmail = new Button("EMAIL", "btnDsg3Bottom");
        btnArr.add(btnEmail);
        btnEmail.addActionListener(e -> {
            changeBtnUIID(btnArr, btnEmail);
            proc.showToast("Email", FontImage.MATERIAL_INFO_OUTLINE).show();
        });
        cntDsg3BottomMenus.addAll(FlowLayout.encloseCenterMiddle(btnCall),
                FlowLayout.encloseCenterMiddle(btnSms),
                FlowLayout.encloseCenterMiddle(btnEmail));

        cntBottom.add(SOUTH, cntDsg3BottomMenus);

        cntBgPar.addAll(cntBgImg, cntBottom);

        form.add(cntBgPar);

        Container cntMidPar = new Container(new LayeredLayout());
        Container cntFillWidth = new CustomSizedCmp(Display.getInstance().getDisplayWidth(), 1);
        cntFillWidth.setUIID("cntFillWidth");
        cntMidPar.add(cntFillWidth);

        TableLayout tlMid = new TableLayout(1, 3);
        Container cntMid = new Container(tlMid, "cntMid");

        Button btnMid1 = new Button(proc.customIcon(FontImage.MATERIAL_FAVORITE_OUTLINE,
                proc.white, 3), "btnMidMenu");
        btnMid1.addActionListener(e -> {
            proc.showToast("Favourite apps", FontImage.MATERIAL_FAVORITE_OUTLINE).show();
        });
        Container cntMidContent = new Container(BoxLayout.y(), "cntMidContent");
        cntMidContent.addAll(new Label("Home Screen", "lblMid1"),
                new Label("apps", "lblMid2"));
        Button btnMid2 = new Button(proc.customIcon(FontImage.MATERIAL_RECENT_ACTORS,
                proc.white, 3), "btnMidMenuBlue");
        btnMid2.addActionListener(e -> {
            proc.showToast("Recent apps", FontImage.MATERIAL_RECENT_ACTORS).show();
        });
        btnMid2.getAllStyles().setBgColor(proc.colorBlue);

        cntMid.add(tlMid.cc().widthPercentage(15).heightPercentage(100), FlowLayout.encloseCenterMiddle(btnMid1));
        cntMid.add(tlMid.cc().widthPercentage(70).heightPercentage(100), FlowLayout.encloseLeftMiddle(cntMidContent));
        cntMid.add(tlMid.cc().widthPercentage(15).heightPercentage(100), FlowLayout.encloseCenterMiddle(btnMid2));

        cntMidPar.add(cntMid);
        form.add(FlowLayout.encloseCenterMiddle(cntMidPar));

        form.show();
    }

    private void changeBtnUIID(ArrayList<Button> btnArr, Button selBtn) {
        for (int k = 0; k < btnArr.size(); k++) {
            if (btnArr.get(k).equals(selBtn)) {
                btnArr.get(k).setUIID("btnDsg3BottomSel");
            } else {
                btnArr.get(k).setUIID("btnDsg3Bottom");
            }
        }
    }

}
