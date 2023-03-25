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
import com.codename1.ui.Button;
import com.codename1.ui.Container;
import com.codename1.ui.Display;
import com.codename1.ui.Form;
import com.codename1.ui.Image;
import com.codename1.ui.Label;
import com.codename1.ui.Tabs;
import com.codename1.ui.layouts.BorderLayout;
import static com.codename1.ui.layouts.BorderLayout.NORTH;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.FlowLayout;
import com.codename1.ui.layouts.GridLayout;
import com.zomuhtech.cn.features.procs.Proc;
import java.io.IOException;
import java.util.ArrayList;

public class Fragments extends com.codename1.ui.Form {

    Form form, prevForm;
    Proc proc;
    ArrayList<Button> btnArr;
    ArrayList<Container> cntBorderArr;
    int tabIndex;

    public Fragments(Form form) {
        this.prevForm = form;
        this.btnArr = new ArrayList<>();
        this.cntBorderArr = new ArrayList<>();

        proc = new Proc();
        Display.getInstance().callSerially(() -> {
            createUI();
        });
    }

    private void createUI() {
        form = proc.getForm("Tabs/Fragments", prevForm);
        form.setLayout(new BorderLayout());

        Container cntTabs = new Container(BoxLayout.y());
        Tabs tabs = new Tabs();
        tabs.hideTabs();

        Container cntAnd = new Container(BoxLayout.y());
        Button btnAnd = new Button("Android", "btnTabsNav");
        btnArr.add(btnAnd);
        Container cntAndBorder = new Container(BoxLayout.y(), "cntBorder");
        cntBorderArr.add(cntAndBorder);
        cntAnd.addAll(btnAnd, cntAndBorder);
        cntAnd.setLeadComponent(btnAnd);
        btnAnd.addActionListener(e -> {
            proc.changeTabNav(btnArr, btnAnd, cntBorderArr, cntAndBorder);
            form.revalidate();
            tabIndex = 0;
            tabs.setSelectedIndex(tabIndex);
        });

        Container cntIOS = new Container(BoxLayout.y());
        Button btnIOS = new Button("iOS", "btnTabsNav");
        btnArr.add(btnIOS);
        Container cntIOSBorder = new Container(BoxLayout.y(), "cntBorder");
        cntBorderArr.add(cntIOSBorder);
        cntIOS.addAll(btnIOS, cntIOSBorder);
        cntIOS.setLeadComponent(btnIOS);
        btnIOS.addActionListener(ev -> {
            proc.changeTabNav(btnArr, btnIOS, cntBorderArr, cntIOSBorder);
            form.revalidate();
            tabIndex = 1;
            tabs.setSelectedIndex(tabIndex);
        });

        Container cntWin = new Container(BoxLayout.y());
        Button btnWin = new Button("Windows", "btnTabsNav");
        btnArr.add(btnWin);
        Container cntWinBorder = new Container(BoxLayout.y(), "cntBorder");
        cntBorderArr.add(cntWinBorder);
        cntWin.addAll(btnWin, cntWinBorder);
        cntWin.setLeadComponent(btnWin);
        btnWin.addActionListener(ev -> {
            proc.changeTabNav(btnArr, btnWin, cntBorderArr, cntWinBorder);
            form.revalidate();
            tabIndex = 2;
            tabs.setSelectedIndex(tabIndex);
        });

        Container cntAnd2 = new Container(BoxLayout.y());
        Button btnAnd2 = new Button("Android", "btnTabsNav");
        btnArr.add(btnAnd2);
        Container cntAndBorder2 = new Container(BoxLayout.y(), "cntBorder");
        cntBorderArr.add(cntAndBorder2);
        cntAnd2.addAll(btnAnd2, cntAndBorder2);
        cntAnd2.setLeadComponent(btnAnd2);

        btnAnd2.addActionListener(e -> {
            proc.changeTabNav(btnArr, btnAnd2, cntBorderArr, cntAndBorder2);
            form.revalidate();
            tabIndex = 3;
            tabs.setSelectedIndex(tabIndex);
        });

        Container cntIOS2 = new Container(BoxLayout.y());
        Button btnIOS2 = new Button("iOS", "btnTabsNav");
        btnArr.add(btnIOS2);
        Container cntIOSBorder2 = new Container(BoxLayout.y(), "cntBorder");
        cntBorderArr.add(cntIOSBorder2);
        cntIOS2.addAll(btnIOS2, cntIOSBorder2);
        cntIOS2.setLeadComponent(btnIOS2);
        btnIOS2.addActionListener(ev -> {
            proc.changeTabNav(btnArr, btnIOS2, cntBorderArr, cntIOSBorder2);
            form.revalidate();
            tabIndex = 4;
            tabs.setSelectedIndex(tabIndex);
        });

        Container cntWin2 = new Container(BoxLayout.y());
        Button btnWin2 = new Button("Windows", "btnTabsNav");
        btnArr.add(btnWin2);
        Container cntWinBorder2 = new Container(BoxLayout.y(), "cntBorder");
        cntBorderArr.add(cntWinBorder2);
        cntWin2.addAll(btnWin2, cntWinBorder2);
        cntWin2.setLeadComponent(btnWin2);
        btnWin2.addActionListener(ev -> {
            proc.changeTabNav(btnArr, btnWin2, cntBorderArr, cntWinBorder2);
            form.revalidate();
            tabIndex = 5;
            tabs.setSelectedIndex(tabIndex);
        });

        Container cnt = new Container(new GridLayout(1, 6), "cntTabBtns");
        cnt.addAll(cntAnd, cntIOS, cntWin, cntAnd2, cntIOS2, cntWin2);
        cnt.setScrollableX(true);
        cnt.setScrollVisible(false);

        form.add(NORTH, cnt);

        Container cnt1 = new Container(BoxLayout.y(), proc.getCntTabParUiid());
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
        tabs.addTab("Android", FlowLayout.encloseCenterMiddle(cnt1));

        Container cnt2 = new Container(BoxLayout.y(), proc.getCntTabParUiid());
        try {
            cnt2.add(FlowLayout.encloseCenter(new Label("Codename One",
                    "lblAdsTitle")));

            Container cntAdsImg = proc.getAdsImageContainer();
            cntAdsImg.getAllStyles().setBgImage(Image.createImage("/ios.jpeg"));
            cnt2.addAll(cntAdsImg, FlowLayout.encloseCenter(
                    new SpanLabel("Using one codebase build apps for iPhones", "lblAdsMsg")));

        } catch (IOException e) {
        }
        tabs.addTab("iOS", FlowLayout.encloseCenterMiddle(cnt2));

        Container cnt3 = new Container(BoxLayout.y(), proc.getCntTabParUiid());
        try {
            cnt3.add(FlowLayout.encloseCenter(new Label("Codename One",
                    "lblAdsTitle")));
            
            Container cntAdsImg = proc.getAdsImageContainer();
            cntAdsImg.getAllStyles().setBgImage(Image.createImage("/windows.jpeg"));
            cnt3.addAll(cntAdsImg, FlowLayout.encloseCenter(
                    new SpanLabel("Using one codebase build apps for Windows phones", "lblAdsMsg")));
        } catch (IOException e) {
        }
        tabs.addTab("Windows", FlowLayout.encloseCenterMiddle(cnt3));

        Container cnt4 = new Container(BoxLayout.y(), proc.getCntTabParUiid());
        try {
            cnt4.add(FlowLayout.encloseCenter(new Label("Codename One",
                    "lblAdsTitle")));
            
            Container cntAdsImg = proc.getAdsImageContainer();
            cntAdsImg.getAllStyles().setBgImage(Image.createImage("/android.png"));
            cnt4.addAll(cntAdsImg, FlowLayout.encloseCenter(
                    new SpanLabel("Using one codebase build apps for Android phones", "lblAdsMsg")));
        } catch (IOException e) {
        }
        tabs.addTab("Android", FlowLayout.encloseCenterMiddle(cnt4));

        Container cnt5 = new Container(BoxLayout.y(), proc.getCntTabParUiid());
        try {
            cnt5.add(FlowLayout.encloseCenter(new Label("Codename One",
                    "lblAdsTitle")));
            
            Container cntAdsImg = proc.getAdsImageContainer();
            cntAdsImg.getAllStyles().setBgImage(Image.createImage("/ios.jpeg"));
            cnt5.addAll(cntAdsImg, FlowLayout.encloseCenter(
                    new SpanLabel("Using one codebase build apps for iPhones", "lblAdsMsg")));
        } catch (IOException e) {
        }
        tabs.addTab("iOS", FlowLayout.encloseCenterMiddle(cnt5));

        Container cnt6 = new Container(BoxLayout.y(), proc.getCntTabParUiid());
        try {
            cnt6.add(FlowLayout.encloseCenter(new Label("Codename One",
                    "lblAdsTitle")));
            
            Container cntAdsImg = proc.getAdsImageContainer();
            cntAdsImg.getAllStyles().setBgImage(Image.createImage("/windows.jpeg"));
            cnt6.addAll(cntAdsImg, FlowLayout.encloseCenter(
                    new SpanLabel("Using one codebase build apps for Windows phones", "lblAdsMsg")));
        } catch (IOException e) {
        }
        tabs.addTab("Windows", FlowLayout.encloseCenterMiddle(cnt6));
        
        cntTabs.add(tabs);
        form.add(CENTER, cntTabs);
        cntTabs.setScrollableY(true);

        //selected by default   
        proc.changeTabNav(btnArr, btnAnd, cntBorderArr, cntAndBorder);
        tabIndex = 0;
        tabs.setSelectedIndex(tabIndex);

        tabs.addSelectionListener((i1, i2) -> {

            for (int j = 0; j < btnArr.size(); j++) {
                if (i2 == j) {

                    proc.changeTabNav(btnArr, btnArr.get(j),
                            cntBorderArr, cntBorderArr.get(j));
                    cnt.scrollComponentToVisible(btnArr.get(j));
                    form.revalidate();
                }
            }
        });

        form.show();
    }

    public Fragments(com.codename1.ui.util.Resources resourceObjectInstance) {
        initGuiBuilderComponents(resourceObjectInstance);
    }

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////-- DON'T EDIT BELOW THIS LINE!!!


// <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initGuiBuilderComponents(com.codename1.ui.util.Resources resourceObjectInstance) {
        setLayout(new com.codename1.ui.layouts.FlowLayout());
        setInlineStylesTheme(resourceObjectInstance);
                setInlineStylesTheme(resourceObjectInstance);
        setTitle("Fragments");
        setName("Fragments");
    }// </editor-fold>

//-- DON'T EDIT ABOVE THIS LINE!!!
}
