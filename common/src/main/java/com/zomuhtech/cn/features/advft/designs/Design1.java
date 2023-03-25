/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zomuhtech.cn.features.advft.designs;

import com.codename1.ui.Button;
import com.codename1.ui.Container;
import com.codename1.ui.Display;
import com.codename1.ui.FontImage;
import com.codename1.ui.Form;
import com.codename1.ui.Image;
import static com.codename1.ui.Image.createImage;
import com.codename1.ui.Label;
import com.codename1.ui.layouts.BorderLayout;
import static com.codename1.ui.layouts.BorderLayout.NORTH;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.FlowLayout;
import com.codename1.ui.layouts.GridLayout;
import com.codename1.ui.table.TableLayout;
import com.codename1.ui.util.SwipeBackSupport;
import com.zomuhtech.cn.features.procs.Proc;
import java.io.IOException;

/**
 *
 * @author Eric
 */
public class Design1 extends Form {

    Form form, prevForm;
    Proc proc;
    int selCount;

    public Design1(Form form) {
        this.prevForm = form;
        proc = new Proc();

        Display.getInstance().callSerially(() -> {
            createUI();
        });

    }

    public void createUI() {

        form = proc.getDesignForm("1", "Storage Drives", prevForm, proc.neonBlue, proc.ivory);
        form.setLayout(new BorderLayout());

        Form cntDesignBg = new Form();
        cntDesignBg.getToolbar().hideToolbar();
        cntDesignBg.setUIID("cntDesign1Bg");
        cntDesignBg.setLayout(BoxLayout.y());

        Container cntUser = new Container(BoxLayout.x());
        Button btnUserImg = null;
        try {
            btnUserImg = proc.getUserImage(Image.createImage("/eric_40.jpg"));
        } catch (IOException e) {

        }
        Container cntUserLbl = new Container(BoxLayout.y());
        Label lbl = new Label("Welcome", "lblWelcome");
        Label lblName = new Label("John Doe", "lblUserName");
        cntUserLbl.addAll(lbl, lblName);
        cntUser.addAll(FlowLayout.encloseLeftMiddle(btnUserImg),
                FlowLayout.encloseLeftMiddle(cntUserLbl));
        cntDesignBg.add(cntUser);

        //Container cntMenuPar = new Container(BoxLayout.x());
        Container cntMenuPar = new Container(new GridLayout(1, 4));
        cntMenuPar.setScrollableX(true);
        cntMenuPar.setScrollVisible(false);

        int count = 0, titleCount = 0;

        for (int m = 0; m <= 3; m++) {
            selCount = m;
            try {
                Container cntMenu = new Container(BoxLayout.y(), "cntMenu");
                if (count == 0) {
                    //Button btnDropbox = new Button(proc.scaleDriveIcon(createImage("/dropbox_122_113.png")),
                    //"btnDrive");
                    Button btnDropbox = new Button(createImage("/dropbox_25_23.png"),
                            "btnDrive");
                    btnDropbox.addActionListener(e -> {
                        proc.showToast("Dropbox ", FontImage.MATERIAL_INFO_OUTLINE).show();
                    });

                    /*ScaleImageButton btnDropbox = new ScaleImageButton(createImage("/dropbox.png")) {
                        @Override
                        protected Dimension calcPreferredSize() {

                            //Dimension preferredSize =  super.calcPreferredSize();
                    //preferredSize.setHeight(Display.getInstance().getDisplayHeight() / 10);
                    //return preferredSize;
                            //return new Dimension(40, 40);
                        }
                    };*/
                    cntMenu.add(FlowLayout.encloseCenterMiddle(btnDropbox));
                    count = 1;
                } else if (count == 1) {
                    //Button btnGoogleDrive = new Button(proc.scaleDriveIcon(createImage("/google_drive_122_109.png")),
                    //"btnDrive");
                    Button btnGoogleDrive = new Button(createImage("/google_drive_25_22.png"),
                            "btnDrive");
                    btnGoogleDrive.addActionListener(e -> {
                        proc.showToast("Google Drive ", FontImage.MATERIAL_INFO_OUTLINE).show();
                    });
                    cntMenu.add(FlowLayout.encloseCenterMiddle(btnGoogleDrive));
                    count = 0;
                }

                Container cntSubMenu = new Container(BoxLayout.x());
                Button btnFolder = new Button(createImage("/folder_25_22.png"), "btnSubMenu");
                Button btnFile = new Button(createImage("/file_18_25.jpeg"), "btnSubMenu");
                cntSubMenu.addAll(btnFolder, btnFile);
                cntMenu.add(FlowLayout.encloseCenterMiddle(cntSubMenu));

                if (titleCount == 0) {
                    cntMenu.add(new Label("Dropbox", "lblDrive"));
                    titleCount = 1;
                } else if (titleCount == 1) {
                    cntMenu.add(new Label("Google Drive", "lblDrive"));
                    titleCount = 0;
                }

                cntMenuPar.add(cntMenu);

            } catch (IOException e) {
            }
        }

        cntDesignBg.add(cntMenuPar);
        form.add(NORTH, cntDesignBg);

        Container cntFilePar = new Container(BoxLayout.y(), "cntFilePar");
        cntFilePar.setScrollableY(true);
        cntFilePar.setScrollVisible(false);
        cntFilePar.add(new Label("Last Files", "lblFiles"));

        for (int f = 0; f <= 6; f++) {
            try {
                TableLayout tlFile = new TableLayout(1, 3);
                Container cntFile = new Container(tlFile, "cntFile");

                Container cntLbl = new Container(BoxLayout.y());
                String icon = null, title = null, desc = null;
                switch (f) {
                    case 0:
                    case 3:
                        //icon = "/cn1_logo_122_122.png";
                        icon = "/cn1_logo_40_40.png";
                        title = "Codename One";
                        desc = "Codename One projects";
                        break;
                    case 1:
                    case 4:
                        //icon = "/android_logo_122_101.png";
                        icon = "/android_logo_48_40.png";
                        title = "Android";
                        desc = "Android gradle projects";
                        break;
                    case 2:
                    case 5:
                        //icon = "/ios_logo_122_122.png";
                        icon = "/ios_logo_40_40.png";
                        title = "iOS";
                        desc = "Xcode projects";
                        break;
                    case 6:
                        //icon = "/windows_logo.png";
                        icon = "/windows_logo_40_40.png";
                        title = "Windows";
                        desc = "Visual studio projects";
                        break;
                }
                /*cntFile.add(tlFile.createConstraint().widthPercentage(15),
                        FlowLayout.encloseCenterMiddle(
                                new Label(proc.scaleIcon(createImage(icon)),
                                        "btnSubMenu")));*/
                cntFile.add(tlFile.createConstraint().widthPercentage(15),
                        FlowLayout.encloseCenterMiddle(
                                new Label(createImage(icon), "btnSubMenu")));
                cntLbl.add(new Label(title, "lblFilesTitle"));
                cntLbl.add(new Label(desc, "lblFiles"));
                cntFile.add(tlFile.createConstraint().widthPercentage(70),
                        FlowLayout.encloseLeftMiddle(cntLbl));

                Button btnMore = new Button("...", "lblFiles");
                btnMore.addActionListener(e -> {
                    proc.showToast("More", FontImage.MATERIAL_INFO_OUTLINE).show();
                });
                cntFile.add(tlFile.createConstraint().widthPercentage(15),
                        FlowLayout.encloseRightMiddle(btnMore));
                cntFilePar.add(cntFile);

            } catch (IOException e) {

            }
        }
        form.add(CENTER, cntFilePar);

        SwipeBackSupport.bindBack(cntDesignBg, (args) -> {
            return prevForm.getComponentForm();
        });

        form.show();
    }
}
