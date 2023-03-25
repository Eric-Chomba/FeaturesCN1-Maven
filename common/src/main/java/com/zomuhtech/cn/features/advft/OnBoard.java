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
import com.codename1.ui.ButtonGroup;
import com.codename1.ui.Container;
import com.codename1.ui.Display;
import com.codename1.ui.FontImage;
import com.codename1.ui.Form;
import com.codename1.ui.Image;
import com.codename1.ui.Label;
import com.codename1.ui.RadioButton;
import com.codename1.ui.Tabs;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.FlowLayout;
import com.codename1.ui.layouts.LayeredLayout;
import com.codename1.ui.plaf.Style;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.plaf.DefaultLookAndFeel;
import com.codename1.ui.util.UITimer;
import com.zomuhtech.cn.features.procs.Proc;
import java.io.IOException;
import java.util.ArrayList;

public class OnBoard extends com.codename1.ui.Form {

    Form prevForm;
    Proc proc;
    int tabIndex;

    public OnBoard(Form form) {
        //this(com.codename1.ui.util.Resources.getGlobalResources());
        this.prevForm = form;
        this.tabIndex = 0;
        proc = new Proc();
        Display.getInstance().callSerially(() -> {
            createUI();
        });
    }

    private void createUI() {
        Form form = proc.getForm("OnBoard", prevForm);
        form.setLayout(new LayeredLayout());

        Tabs tabs = new Tabs();
        tabs.getAllStyles().setBgTransparency(0);
        tabs.hideTabs();

        Style s;
        if (Display.getInstance().isTablet()) {
            s = UIManager.getInstance().getComponentStyle("radTab");
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

        ArrayList<String> adsArr = new ArrayList<>();
        adsArr.add("Codename One:android.png:Using one codebase build apps for"
                + " Android phones");
        adsArr.add("Codename One:ios.jpeg:Using one codebase build apps for"
                + " iPhones");
        adsArr.add("Codename One:windows.jpeg:Using one codebase build apps for"
                + " Windows phones");

        for (int j = 0; j < adsArr.size(); j++) {

            String[] adArr = proc.splitValue(adsArr.get(j), ":");
            Container cnt1 = new Container(BoxLayout.y());

            try {
                cnt1.add(FlowLayout.encloseCenter(new Label(adArr[0],
                        "lblAdsTitle")));
                Container cntAdsImg = proc.getAdsImageContainer();
                cntAdsImg.getAllStyles().setBgImage(Image.createImage("/" + adArr[1]));
                
                cnt1.addAll(cntAdsImg, FlowLayout.encloseCenter(new SpanLabel(adArr[2], "lblAdsMsg")));
            } catch (IOException e) {
            }
            tabs.addTab("Tab", FlowLayout.encloseCenterMiddle(cnt1));
        }

        RadioButton firstTab = new RadioButton("");
        RadioButton secondTab = new RadioButton("");
        RadioButton thirdTab = new RadioButton("");

        ButtonGroup bg = new ButtonGroup(firstTab, secondTab, thirdTab);
        firstTab.setSelected(true);
        Container tabsFlow = FlowLayout.encloseCenter(firstTab, secondTab,
                thirdTab);

        form.add(tabs);
        form.add(BorderLayout.south(tabsFlow));

        tabs.addSelectionListener((i1, i2) -> {

            switch (i2) {
                case 0:
                    if (!firstTab.isSelected()) {
                        firstTab.setSelected(true);
                        tabIndex = 0;
                    }
                    break;
                case 1:
                    if (!secondTab.isSelected()) {
                        secondTab.setSelected(true);
                        tabIndex = 1;
                    }
                    break;
                case 2:
                    if (!thirdTab.isSelected()) {
                        thirdTab.setSelected(true);
                        tabIndex = 2;
                    }
                    break;
            }
        });

        bg.addActionListener(e -> {
            tabs.setSelectedIndex(bg.getSelectedIndex(), true);
            tabIndex = bg.getSelectedIndex();
        });
        updateTab(tabs, form);

        form.show();
    }

    private void updateTab(Tabs tabs, Form form) {

        Display.getInstance().callSerially(() -> {

            new UITimer(() -> {
                if (tabIndex == 2) {
                    tabIndex = 0;
                    //proc.printLine("tabIndex " + tabIndex);
                    tabs.setSelectedIndex(tabIndex);
                } else {
                    tabIndex = tabIndex + 1;
                    //proc.printLine("tabIndex " + tabIndex);
                    tabs.setSelectedIndex(tabIndex);
                }

                updateTab(tabs, form);
            }).schedule(3000, false, form);
        });
    }

    public OnBoard(com.codename1.ui.util.Resources resourceObjectInstance) {
        //initGuiBuilderComponents(resourceObjectInstance);
    }

//////////////////////////////////////////////////////////////////////////////////////////////////////-- DON'T EDIT BELOW THIS LINE!!!


// <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initGuiBuilderComponents(com.codename1.ui.util.Resources resourceObjectInstance) {
        setLayout(new com.codename1.ui.layouts.FlowLayout());
        setInlineStylesTheme(resourceObjectInstance);
                setInlineStylesTheme(resourceObjectInstance);
        setTitle("OnBoard");
        setName("OnBoard");
    }// </editor-fold>

//-- DON'T EDIT ABOVE THIS LINE!!!
}
