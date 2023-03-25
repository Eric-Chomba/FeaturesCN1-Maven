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
import com.codename1.components.SpanLabel;
import com.codename1.components.ToastBar;
import com.codename1.ui.Button;
import com.codename1.ui.ButtonGroup;
import com.codename1.ui.Command;
import com.codename1.ui.Container;
import com.codename1.ui.Display;
import com.codename1.ui.FontImage;
import com.codename1.ui.Form;
import com.codename1.ui.Image;
import static com.codename1.ui.Image.createImage;
import com.codename1.ui.Label;
import com.codename1.ui.RadioButton;
import com.codename1.ui.Tabs;
import com.codename1.ui.Toolbar;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.FlowLayout;
import com.codename1.ui.layouts.GridLayout;
import com.codename1.ui.layouts.LayeredLayout;
import com.codename1.ui.plaf.Border;
import com.zomuhtech.cn.features.procs.Proc;
import java.io.IOException;
import java.util.ArrayList;

public class BottomNavigation extends com.codename1.ui.Form {

    Form prevForm;
    boolean sideMenuOpen;
    Proc proc;
    ArrayList<Button> btnMenuArr = new ArrayList<>();
    ArrayList<Label> lblMenuArr = new ArrayList<>();
    ArrayList<FontImage> iconMenuArr = new ArrayList<>();
    ArrayList<FontImage> selIconMenuArr = new ArrayList<>();
    int btnCount = 0;
    Form form;

    public BottomNavigation(Form form) {
        //this(com.codename1.ui.util.Resources.getGlobalResources
        this.prevForm = form;
        this.sideMenuOpen = false;
        proc = new Proc();
        Display.getInstance().callSerially(() -> {
            createUI();
        });
    }

    private void createUI() {
        form = new Form("Bottom Navigation", new LayeredLayout());
        Toolbar tb = form.getToolbar();
        //tb.setVisible(false);
        tb.hideToolbar();
        tb.setEnableSideMenuSwipe(false);
        tb.setUIID("tbar");
        tb.getTitleComponent().setUIID(proc.getLblTitleUIID());

        Image profilePic = null;
        try {
            profilePic = Image.createImage("/cn1_logo_40_40.png");
        } catch (IOException e) {
        }

        tb.addComponentToSideMenu(sideMenuItems(form, tb, profilePic));

        form.setBackCommand(new Command("btnBack") {
            @Override
            public void actionPerformed(ActionEvent ev) {
                prevForm.showBack();
            }
        });
        Command backCmd = Command.create("",
                proc.materialIcon(FontImage.MATERIAL_ARROW_BACK),
                evt -> {
                    prevForm.showBack();
                });

        form.getToolbar().addCommandToLeftBar(backCmd);

        Tabs tabs = new Tabs();
        tabs.hideTabs();

        Container cntHome = FlowLayout.encloseCenterMiddle(new Label("Home", "lblBottomNav"));
        cntHome.setUIID("cntBottomNav");
        tabs.addTab("Tab1", cntHome);

        Container cnt1 = new Container(BoxLayout.y(), "cntBottomNav");

        try {
            cnt1.add(FlowLayout.encloseCenter(new Label("Codename One",
                    "lblAdsTitle")));

            Container cntAdsImg = proc.getAdsImageContainer();
            cntAdsImg.getAllStyles().setBgImage(Image.createImage("/android.png"));
            cnt1.addAll(cntAdsImg, FlowLayout.encloseCenter(
                    new SpanLabel("Using one codebase build apps for  Android phones",
                            "lblAdsMsg")));
        } catch (IOException e) {
        }
        tabs.addTab("Tab2", FlowLayout.encloseCenterMiddle(cnt1));

        Container cnt2 = new Container(BoxLayout.y(), "cntBottomNav");
        try {
            cnt2.add(FlowLayout.encloseCenter(new Label("Codename One",
                    "lblAdsTitle")));

            Container cntAdsImg = proc.getAdsImageContainer();
            cntAdsImg.getAllStyles().setBgImage(Image.createImage("/ios.jpeg"));
            cnt2.addAll(cntAdsImg, FlowLayout.encloseCenter(
                    new SpanLabel("Using one codebase build apps for iPhones", "lblAdsMsg")));
        } catch (IOException e) {
        }
        tabs.addTab("Tab3", FlowLayout.encloseCenterMiddle(cnt2));

        Container cnt3 = new Container(BoxLayout.y(), "cntBottomNav");
        try {
            cnt3.add(FlowLayout.encloseCenter(new Label("Codename One",
                    "lblAdsTitle")));

            Container cntAdsImg = proc.getAdsImageContainer();
            cntAdsImg.getAllStyles().setBgImage(Image.createImage("/windows.jpeg"));
            cnt3.addAll(cntAdsImg, FlowLayout.encloseCenter(
                    new SpanLabel("Using one codebase build apps for Windows phones", "lblAdsMsg")));
        } catch (IOException e) {
        }
        tabs.addTab("Tab4", FlowLayout.encloseCenterMiddle(cnt3));

        RadioButton firstTab = new RadioButton();
        RadioButton secondTab = new RadioButton();
        RadioButton thirdTab = new RadioButton();
        RadioButton fouthTab = new RadioButton();

        ButtonGroup bg = new ButtonGroup(firstTab, secondTab, thirdTab,
                fouthTab);

        firstTab.setSelected(true);

        form.add(tabs);

        //select first menu by default
        changeIcon(0);

        tabs.addSelectionListener((i1, i2) -> {

            switch (i2) {
                case 0:
                    if (!firstTab.isSelected()) {
                        firstTab.setSelected(true);
                        changeIcon(0);
                    }
                    break;
                case 1:
                    if (!secondTab.isSelected()) {
                        secondTab.setSelected(true);
                        changeIcon(1);
                    }
                    break;
                case 2:
                    if (!thirdTab.isSelected()) {
                        thirdTab.setSelected(true);
                        changeIcon(2);
                    }
                    break;
                case 3:
                    if (!fouthTab.isSelected()) {
                        fouthTab.setSelected(true);
                        changeIcon(3);
                    }
                    break;
            }
        });

        bg.addActionListener(e -> {
            tabs.setSelectedIndex(bg.getSelectedIndex(), true);
        });

        int iconColor, selIconColor;
        if (proc.getDarkMode().equals("On")) {
            iconColor = proc.white;
            selIconColor = proc.colorTeal;
        } else {
            iconColor = proc.colorTeal;
            selIconColor = proc.blueGray;
        }

        Container cntMenu = new Container(BoxLayout.y());
        FontImage iconMenu = proc.customIcon(FontImage.MATERIAL_MENU, iconColor, 4);
        iconMenuArr.add(iconMenu);
        selIconMenuArr.add(proc.customIcon(FontImage.MATERIAL_MENU, selIconColor, 4));
        Button btnMenu = new Button("Menu", "lblBottomNav");
        btnMenu.setName("" + btnCount);
        btnMenuArr.add(btnMenu);
        btnCount++;
        btnMenu.addActionListener((e) -> {
            if (tabs.getSelectedIndex() != 0) {
                tabs.setSelectedIndex(0, true);
                changeIcon(Integer.parseInt(btnMenu.getName()));
            } else {
                tb.openSideMenu();
                sideMenuOpen = true;
            }
        });
        Label lblIconMenu = new Label(iconMenu);
        lblMenuArr.add(lblIconMenu);

        cntMenu.addAll(FlowLayout.encloseCenterMiddle(lblIconMenu), btnMenu);
        cntMenu.setLeadComponent(btnMenu);

        Container cntAnd = new Container(BoxLayout.y());
        FontImage iconAnd = proc.customIcon(FontImage.MATERIAL_PHONE_ANDROID, iconColor, 4);
        iconMenuArr.add(iconAnd);
        selIconMenuArr.add(proc.customIcon(FontImage.MATERIAL_PHONE_ANDROID, selIconColor, 4));
        Button btnAnd = new Button("Android", "lblBottomNav");
        btnAnd.setName("" + btnCount);
        btnMenuArr.add(btnAnd);
        btnCount++;
        btnAnd.addActionListener((e) -> {
            tabs.setSelectedIndex(1, true);
            changeIcon(Integer.parseInt(btnAnd.getName()));
        });
        Label lblIconAnd = new Label(iconAnd);
        lblMenuArr.add(lblIconAnd);
        cntAnd.addAll(FlowLayout.encloseCenterMiddle(lblIconAnd), btnAnd);
        cntAnd.setLeadComponent(btnAnd);

        Container cntIOS = new Container(BoxLayout.y());
        FontImage iconIOS = proc.customIcon(FontImage.MATERIAL_PHONE_IPHONE, iconColor, 4);
        iconMenuArr.add(iconIOS);
        selIconMenuArr.add(proc.customIcon(FontImage.MATERIAL_PHONE_IPHONE, selIconColor, 4));
        Button btnIOS = new Button("iOS", "lblBottomNav");
        btnIOS.setName("" + btnCount);
        btnMenuArr.add(btnIOS);
        btnCount++;
        btnIOS.addActionListener((e) -> {
            tabs.setSelectedIndex(2, true);
            changeIcon(Integer.parseInt(btnIOS.getName()));
        });
        Label lblIconIOS = new Label(iconIOS);
        lblMenuArr.add(lblIconIOS);
        cntIOS.addAll(FlowLayout.encloseCenterMiddle(lblIconIOS), btnIOS);
        cntIOS.setLeadComponent(btnIOS);

        Container cntWin = new Container(BoxLayout.y());
        FontImage iconWin = proc.customIcon(FontImage.MATERIAL_WINDOW, iconColor, 4);
        iconMenuArr.add(iconWin);
        selIconMenuArr.add(proc.customIcon(FontImage.MATERIAL_WINDOW, selIconColor, 4));
        Button btnWin = new Button("Windows", "lblBottomNav");
        btnWin.setName("" + btnCount);
        btnMenuArr.add(btnWin);
        btnWin.addActionListener((e) -> {
            tabs.setSelectedIndex(3, true);
            changeIcon(Integer.parseInt(btnWin.getName()));
        });
        Label lblIconWin = new Label(iconWin);
        lblMenuArr.add(lblIconWin);
        cntWin.addAll(FlowLayout.encloseCenterMiddle(lblIconWin), btnWin);
        cntWin.setLeadComponent(btnWin);

        Container cntBtn = new Container(new GridLayout(1, 4));
        cntBtn.addAll(cntMenu, cntAnd, cntIOS, cntWin);

        form.add(BorderLayout.south(cntBtn));

        form.show();

        //select first menu by default
        changeIcon(0);
    }

    private Container sideMenuItems(Form form, Toolbar tb, Image profilePic) {

        Container nav = new Container(BoxLayout.y());

        Border border = Border.createCompoundBorder(null,
                Border.createLineBorder(2, 0x15E7FF), null, null);

        Container topBar = BorderLayout.west(new Label(profilePic));
        topBar.add(BorderLayout.SOUTH, new Label("Codename One", "lblUser"));
        topBar.setUIID("topBar");
        topBar.getAllStyles().setBorder(border);
        nav.add(topBar);

        try {
            Container cntItem = new Container(BoxLayout.x(), "navCnt");
            cntItem.add(new Label(createImage("/android_logo_48_40.png"), "navIcon"));

            Button btnItem1 = new Button("Android", "menuLbl");
            btnItem1.addActionListener(e -> {
                tb.closeSideMenu();
                sideMenuOpen = false;
                ToastBar.showInfoMessage("Android apps");
            });
            cntItem.add(btnItem1);
            nav.add(cntItem);
            cntItem.getAllStyles().setBorder(border);
        } catch (IOException e) {
        }

        try {
            Container cntItem = new Container(BoxLayout.x(), "navCnt");
            cntItem.add(new Label(createImage("/ios_logo_40_40.png"), "navIcon"));
            Button btnItem1 = new Button("iOS", "menuLbl");
            btnItem1.addActionListener(e -> {
                tb.closeSideMenu();
                sideMenuOpen = false;
                ToastBar.showInfoMessage("iOS apps");
            });
            cntItem.add(btnItem1);
            nav.add(cntItem);
            cntItem.getAllStyles().setBorder(border);
        } catch (IOException e) {
        }

        try {
            Container cntItem = new Container(BoxLayout.x(), "navCnt");
            cntItem.add(new Label(createImage("/windows_logo_40_40.png"), "navIcon"));

            Button btnItem1 = new Button("Windows", "menuLbl");
            btnItem1.addActionListener(e -> {
                tb.closeSideMenu();
                sideMenuOpen = false;
                ToastBar.showInfoMessage("Windows apps");
            });
            cntItem.add(btnItem1);
            nav.add(cntItem);
            cntItem.getAllStyles().setBorder(border);
        } catch (IOException e) {
        }

        try {
            Container cntItem = new Container(BoxLayout.x(), "navCnt");
            cntItem.add(new Label(proc.sideMenuIcon(FontImage.MATERIAL_ARROW_BACK)));

            Button btnItem1 = new Button("Back", "menuLbl");
            btnItem1.addActionListener(e -> {
                tb.closeSideMenu();
                sideMenuOpen = false;
                prevForm.showBack();
            });
            cntItem.add(btnItem1);
            cntItem.setLeadComponent(btnItem1);
            nav.add(cntItem);
            cntItem.getAllStyles().setBorder(border);
        } catch (Exception e) {
        }

        return nav;
    }

    public Image scaleSideMenuImg(Image img) {

        String platName = Display.getInstance().getPlatformName();

        switch (platName) {
            case "and":
                img = img.scaledWidth(50).scaledHeight(50);
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

    private void changeIcon(int index) {

        for (int m = 0; m < btnMenuArr.size(); m++) {
            if (m == index) {
                lblMenuArr.get(index).setIcon(selIconMenuArr.get(index));
                btnMenuArr.get(index).setUIID("lblBottomNavSel");
            } else {
                lblMenuArr.get(m).setIcon(iconMenuArr.get(m));
                btnMenuArr.get(m).setUIID("lblBottomNav");
            }
            form.revalidate();
        }
    }

    public BottomNavigation(com.codename1.ui.util.Resources resourceObjectInstance) {
        //initGuiBuilderComponents(resourceObjectInstance);
    }

////////////////////////////////////////////////////////////////////////////////////-- DON'T EDIT BELOW THIS LINE!!!


// <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initGuiBuilderComponents(com.codename1.ui.util.Resources resourceObjectInstance) {
        setLayout(new com.codename1.ui.layouts.FlowLayout());
        setInlineStylesTheme(resourceObjectInstance);
                setInlineStylesTheme(resourceObjectInstance);
        setTitle("BottomNavigation");
        setName("BottomNavigation");
    }// </editor-fold>

//-- DON'T EDIT ABOVE THIS LINE!!!
}
