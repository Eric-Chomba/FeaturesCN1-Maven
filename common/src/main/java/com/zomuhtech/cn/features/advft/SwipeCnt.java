/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zomuhtech.cn.features.advft;

import com.codename1.components.MultiButton;
import com.codename1.components.ToastBar;
import com.codename1.ui.Button;
import static com.codename1.ui.Component.CENTER;
import com.codename1.ui.Container;
import com.codename1.ui.Display;
import com.codename1.ui.Font;
import com.codename1.ui.FontImage;
import com.codename1.ui.Form;
import com.codename1.ui.Image;
import com.codename1.ui.Slider;
import com.codename1.ui.SwipeableContainer;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.layouts.BorderLayout;
import static com.codename1.ui.layouts.BorderLayout.NORTH;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.FlowLayout;
import com.codename1.ui.layouts.GridLayout;
import com.codename1.ui.plaf.Border;
import com.codename1.ui.plaf.Style;
import com.zomuhtech.cn.features.procs.Proc;
import java.util.ArrayList;

/**
 *
 * @author Eric
 */
public class SwipeCnt extends Form {

    Form form, prevForm;
    Proc proc;
    ArrayList<Button> btnArr;

    public SwipeCnt(Form form) {
        this.prevForm = form;
        this.btnArr = new ArrayList<>();
        proc = new Proc();
        Display.getInstance().callSerially(() -> {
            createUI();
        });
    }

    private void createUI() {
        form = proc.getForm("Swipe Container", prevForm);
        form.setLayout(new BorderLayout());

        form.add(CENTER, getForm1());

        Button btnRate = new Button("Rate", "btnNav");
        btnArr.add(btnRate);
        btnRate.addActionListener(ev -> {
            proc.changeBtnUIID(btnArr, btnRate);
            form.add(CENTER, getForm1());
            form.revalidate();
        });

        Button btnManage = new Button("Manage", "btnNav");
        btnArr.add(btnManage);
        btnManage.addActionListener(ev -> {
            proc.changeBtnUIID(btnArr, btnManage);
            form.add(CENTER, getForm2());
            form.revalidate();
        });
        //selected by default
        proc.changeBtnUIID(btnArr, btnRate);

        Container cnt = new Container(new GridLayout(1, 2));
        cnt.addAll(btnRate,btnManage);

        form.add(NORTH, cnt);

        form.show();
    }

    private Form getForm1() {

        Form form1 = proc.getInputForm();

        ArrayList<String> arr = new ArrayList<>();
        arr.add("A Game of Thrones:1996");
        arr.add("A Clash of Kings:1998");
        arr.add("A Storm of Swords:2000");

        for (int j = 0; j < arr.size(); j++) {
            String[] itemArr = proc.splitValue(arr.get(j), ":");
            form1.add(createRank(itemArr[0], itemArr[1]));
        }
        return form1;
    }

    private SwipeableContainer createRank(String title, String year) {
        MultiButton btn = new MultiButton(title);
        btn.setUIIDLine1("collapsingToolbarTitle");
        btn.setUIIDLine2("lblInput");
        btn.setTextLine2(year);
        return new SwipeableContainer(FlowLayout
                .encloseCenterMiddle(createStarSlider(btn)), btn);

    }

    private Slider createStarSlider(MultiButton btn) {
        Slider slider = new Slider();
        slider.setEditable(true);
        slider.setMinValue(0);
        slider.setMaxValue(10);
        Font font = Font.createTrueTypeFont("native:MainLight",
                "native:MainLight").derive(Display.getInstance()
                        .convertToPixels(5, true), Font.STYLE_PLAIN);
        Style s = new Style(0x00FBEB, 0, font, (byte) 0);
        Image fullStar = FontImage.createMaterial(FontImage.MATERIAL_STAR, s)
                .toImage();
        s.setOpacity(100);
        s.setFgColor(0);
        Image emptyStar = FontImage.createMaterial(FontImage.MATERIAL_STAR, s)
                .toImage();

        initSliderStyle(slider.getSliderEmptySelectedStyle(), emptyStar);
        initSliderStyle(slider.getSliderEmptyUnselectedStyle(), emptyStar);
        initSliderStyle(slider.getSliderFullSelectedStyle(), fullStar);
        initSliderStyle(slider.getSliderFullUnselectedStyle(), fullStar);

        slider.setPreferredSize(new Dimension(fullStar.getWidth() * 5,
                fullStar.getHeight()));

        slider.addActionListener(e -> {
            String value = btn.getTextLine1() + " " + "Rate "
                    + slider.getProgress() + "/10";
            ToastBar.showInfoMessage(value);
        });
        return slider;
    }

    private void initSliderStyle(Style s, Image star) {
        s.setBackgroundType(Style.BACKGROUND_IMAGE_TILE_BOTH);
        s.setBorder(Border.createEmpty());
        s.setBgImage(star);
        s.setBgTransparency(0);
    }

    private Form getForm2() {

        Form form2 = proc.getInputForm();
//        if (!Display.getInstance().getPlatformName().equals("win")) {
//            form2.getAllStyles().setBgColor(0xffffff);
//        }
//        form2.getToolbar().hideToolbar();

        ArrayList<String> arr = new ArrayList<>();
        arr.add("The Arrow:2013");
        arr.add("Transporter Series:2015");
        arr.add("The Last Ship:2005");
        arr.add("Queen Sono:2014");
        arr.add("MacGyver:2005");
        arr.add("Treadstone:2006");
        arr.add("Blacklist Redemption:2007");
        arr.add("Hanna:2017");
        arr.add("Cobra Kai:2016");
        arr.add("Blindspot:2005");

        for (int j = 0; j < arr.size(); j++) {
            String[] itemArr = proc.splitValue(arr.get(j), ":");
            form2.add(createMng(itemArr[0], itemArr[1]));
        }
        return form2;
    }

    private SwipeableContainer createMng(String title, String year) {
        MultiButton btn = new MultiButton(title);
        btn.setUIIDLine1("collapsingToolbarTitle");
        btn.setTextLine2(year);
        btn.setUIIDLine2("lblInput");
        return new SwipeableContainer(FlowLayout
                .encloseCenterMiddle(createMngButons(btn)), btn);

    }

    private Container createMngButons(MultiButton btn) {

        Container cnt = new Container(BoxLayout.x());
        Button btnView = new Button(proc.swipeIcon(FontImage.MATERIAL_INFO),
                "btnSwipe");
        btnView.addActionListener(e -> {
            ToastBar.showInfoMessage("Viewing " + btn.getTextLine1());
        });
        Button btnEdit = new Button(proc.swipeIcon(FontImage.MATERIAL_EDIT),
                "btnSwipe");
        btnEdit.addActionListener(e -> {
            ToastBar.showInfoMessage("Editing " + btn.getTextLine1());
        });

        Button btnDel = new Button(proc.swipeIcon(FontImage.MATERIAL_DELETE),
                "btnSwipe");
        btnDel.addActionListener(e -> {
            ToastBar.showErrorMessage("Deleting " + btn.getTextLine1());
        });
        cnt.addAll(btnView, btnEdit, btnDel);

        return cnt;
    }

}
