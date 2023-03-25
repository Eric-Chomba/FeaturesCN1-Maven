/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zomuhtech.cn.features.advft;

import com.codename1.components.ImageViewer;
import com.codename1.components.MultiButton;
import com.codename1.components.SpanLabel;
import com.codename1.gif.GifImage;
import com.codename1.ui.Button;
import static com.codename1.ui.CN.getResourceAsStream;
import com.codename1.ui.Component;
import com.codename1.ui.Container;
import com.codename1.ui.Display;
import com.codename1.ui.Form;
import com.codename1.ui.Label;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.layouts.BorderLayout;
import static com.codename1.ui.layouts.BorderLayout.SOUTH;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.FlowLayout;
import com.codename1.ui.layouts.GridLayout;
import com.codename1.ui.spinner.Picker;
import com.zomuhtech.cn.features.procs.Proc;
import java.io.IOException;
import java.util.ArrayList;
import com.codename1.ui.CommonProgressAnimations.CircleProgress;
import com.codename1.ui.CommonProgressAnimations.LoadingTextAnimation;
import com.codename1.ui.Dialog;
import com.codename1.ui.FontImage;
import com.codename1.ui.Graphics;
import com.codename1.ui.Image;
import com.codename1.ui.RadioButton;
import com.codename1.ui.Slider;
import com.codename1.ui.Stroke;
import com.codename1.ui.geom.GeneralPath;
import com.codename1.ui.layouts.LayeredLayout;
import com.codename1.ui.plaf.DefaultLookAndFeel;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.table.TableLayout;
import com.codename1.ui.util.UITimer;

/**
 *
 * @author Eric
 */
public class Anim extends Form {

    Form form, prevForm;
    Proc proc;
    ArrayList<Button> btnArr;
    Container cntAllCustom;
    int count1 = 0, count1B = 0, count2 = 0, count3 = 0, radCount = 0,
            currentProgress = 10, currentProgress2 = -10,
            fixedCount = 0, dimen, duration;
    int start, end, start2, end2;
    int waveCount, dimenWave, waveCount3, waveCount4, cycle = 1,
            bubbleCycle = 1, scanCount = 0;
    ArrayList<Container> cntWave3Arr;
    boolean topScan = true, showCircleBars = false;

    public Anim(Form form) {

        this.prevForm = form;
        this.btnArr = new ArrayList<>();
        this.cntWave3Arr = new ArrayList<>();
        proc = new Proc();

        String data = "70:170:150"; //"100:170:150"

        String[] dataArr = proc.splitValue(data, ":");
        if (Display.getInstance().isDesktop()) {
            this.dimen = (Integer.parseInt(dataArr[0]) / 2) - 5;
        } else {
            this.dimen = Integer.parseInt(dataArr[0]);
        }
        //this.dimen2 = Integer.parseInt(dataArr[1]);
        this.duration = Integer.parseInt(dataArr[2]);

        Display.getInstance().callSerially(() -> {
            createUI();
        });
    }

    private void createUI() {

        form = proc.getForm("Animations", prevForm);
        form.setLayout(new BorderLayout());

        Container cntPar = new Container(BoxLayout.y());

        Container cnt = new Container(new GridLayout(1, 3));
        Button btnLayAnim = new Button("Layout", "btnNav");
        btnArr.add(btnLayAnim);

        Button btnCircleAnim = new Button("Circle", "btnNav");
        btnArr.add(btnCircleAnim);

        Container cnt2 = new Container(new GridLayout(1, 3));
        Button btnTextAnim = new Button("Text", "btnNav");
        btnArr.add(btnTextAnim);
        Button btnSliderAnim = new Button("Slider", "btnNav");
        btnArr.add(btnSliderAnim);
        Button btnCustom = new Button("Multiple", "btnNav");
        btnArr.add(btnCustom);

        Container cnt3 = new Container(new GridLayout(1, 3));
        Button btnProgress = new Button("Progress", "btnNav");
        btnArr.add(btnProgress);
        Container cnt4 = new Container(new GridLayout(1, 3));

        Button btnWaves = new Button("Waves", "btnNav");
        btnArr.add(btnWaves);
        Button btnScan = new Button("Scan", "btnNav");
        btnArr.add(btnScan);
        btnScan.addActionListener(e -> {
            proc.changeBtnUIID(btnArr, btnScan);
            scan();
        });

        Button btnProgressAnim = new Button("Gif", "btnNav");
        btnArr.add(btnProgressAnim);

        Button btnBubble = new Button("Bubble", "btnNav");
        btnArr.add(btnBubble);
        btnBubble.addActionListener(e -> {
            proc.changeBtnUIID(btnArr, btnBubble);
            bubble();
        });

        btnLayAnim.addActionListener(e -> {
            proc.changeBtnUIID(btnArr, btnLayAnim);
            form.add(CENTER, getLayAnim());
            form.revalidate();
        });

        btnCircleAnim.addActionListener(e -> {
            proc.changeBtnUIID(btnArr, btnCircleAnim);
            form.add(CENTER, FlowLayout.encloseCenterMiddle(getCircleAnim()));
            form.revalidate();

        });

        btnTextAnim.addActionListener(e -> {
            proc.changeBtnUIID(btnArr, btnTextAnim);
            form.add(CENTER, getTextAnim());
            form.revalidate();

        });

        btnSliderAnim.addActionListener(e -> {
            proc.changeBtnUIID(btnArr, btnSliderAnim);
            form.add(CENTER, getSliderAnim());
            form.revalidate();

        });

        btnCustom.addActionListener(e -> {
            proc.changeBtnUIID(btnArr, btnCustom);
            form.add(CENTER, getCustomAnim());
            cntAllCustom.setScrollableY(true);
            form.revalidate();

        });

        btnProgressAnim.addActionListener(e -> {
            proc.changeBtnUIID(btnArr, btnProgressAnim);
            form.add(CENTER, getProgressAnim());
            form.revalidate();

        });

        ArrayList<String> animsArr = new ArrayList<>();
        animsArr.add("Progress 1");
        animsArr.add("Progress 2");
        animsArr.add("Progress 3");
        animsArr.add("Progress 4");
        animsArr.add("Progress 5");

        btnProgress.addActionListener(e -> {

            showCircleBars = false;
            proc.changeBtnUIID(btnArr, btnProgress);

            Dialog d = new Dialog();
            d.setLayout(BoxLayout.y());
            d.getContentPane().setScrollableY(true);

            for (int j = 0; j < animsArr.size(); j++) {

                MultiButton btnM = new MultiButton(animsArr.get(j));
                btnM.setUIIDLine1("popLbl");

                d.add(btnM);
                btnM.addActionListener(evt -> {
                    d.dispose();

                    proc.printLine("Selected " + btnM.getTextLine1());

                    switch (btnM.getTextLine1()) {

                        case "Progress 1":
                            cycle = 1;
                            circleAnim3("A", 10);
                            break;

                        case "Progress 2":
                            cycle = 1;
                            circleAnim3("C", 10);
                            break;

                        case "Progress 3":
                            circleAnim4("A", 10);
                            break;

                        case "Progress 4":
                            circleAnim4("C", 10);
                            break;

                        case "Progress 5":
                            showCircleBars = true;
                            fixedCount = 0;
                            circleAnim2();
                            break;
                    }
                });
            }
            d.showPopupDialog(btnProgress);

        });

        ArrayList<String> wavesArr = new ArrayList<>();
        wavesArr.add("Waves 1");
        wavesArr.add("Waves 2");
        wavesArr.add("Waves 3");
        wavesArr.add("Waves 4");

        btnWaves.addActionListener(e -> {
            showCircleBars = false;
            proc.changeBtnUIID(btnArr, btnWaves);

            Dialog d2 = new Dialog();
            d2.setLayout(BoxLayout.y());
            d2.getContentPane().setScrollableY(true);

            for (int j = 0; j < wavesArr.size(); j++) {

                MultiButton btnM = new MultiButton(wavesArr.get(j));
                btnM.setUIIDLine1("popLbl");

                d2.add(btnM);
                btnM.addActionListener(evt -> {
                    d2.dispose();

                    proc.printLine("Selected " + btnM.getTextLine1());

                    switch (btnM.getTextLine1()) {
                        case "Waves 1":
                            waves1();
                            break;
                        case "Waves 2":
                            dimenWave = 40;
                            showCircleBars = true;
                            waves2();
                            break;
                        case "Waves 3":
                            waves3("bars");
                            break;
                        case "Waves 4":
                            waves3("circles");
                            break;
                    }
                });
            }
            d2.showPopupDialog(btnWaves);
        });

        cnt.addAll(btnLayAnim, btnCircleAnim);
        cnt2.addAll(btnTextAnim, btnSliderAnim, btnCustom);
        //cnt3.addAll(btnProgress1, btnProgress2, btnProgress3);
        cnt3.addAll(btnProgress, btnWaves, btnScan);
        //cnt4.addAll(btnProgress4, btnProgress5, btnWaves);
        cnt4.addAll(btnProgressAnim, btnBubble);
        cntPar.addAll(cnt, cnt2, cnt3, cnt4);

        form.add(SOUTH, cntPar);

        form.show();
    }

    private Form getLayAnim() {

        Form animForm = proc.getInputForm();

        Button btn = new Button("Fall", "mmenuLbl");

        btn.addActionListener(e -> {

            if (animForm.getContentPane().getComponentCount() == 1) {
                btn.setText("Rise");
                for (int j = 0; j < 10; j++) {
                    Label lbl2 = new Label("Label " + j, "btn");
                    lbl2.setWidth(btn.getWidth());
                    lbl2.setHeight(btn.getHeight());
                    lbl2.setY(-btn.getHeight());
                    animForm.add(lbl2);
                }
                animForm.getContentPane().animateLayout(5000);
            } else {
                btn.setText("Fall");
                for (int k = 1; k < animForm.getContentPane().getComponentCount();
                        k++) {
                    Component cmp = animForm.getContentPane().getComponentAt(k);
                    cmp.setY(-btn.getHeight());
                }
                animForm.getContentPane().animateUnlayoutAndWait(5000, 255);
                animForm.removeAll();
                animForm.add(btn);
                animForm.revalidate();

            }
        });

        animForm.add(btn);

        return animForm;
    }

    private Form getProgressAnim() {

        Form form3 = proc.getInputForm();
        form3.setLayout(new BorderLayout());

        Picker picker = new Picker();
        picker.setType(Display.PICKER_TYPE_STRINGS);
        picker.setStrings("Acquire", "Wave", "Circle", "Bool", "Circular",
                "Fluid", "Fragmented Square", "Holographic Radar", "Infinity",
                "Plane", "Page Loading", "Pastel Bead", "Pay Loader",
                "Playoffs", "Rotating Circles");
        picker.setSelectedString("Acquire");
        Button btnPlay = new Button("Play Anim", "btn");
        Container cnt = new Container(BoxLayout.y());
        cnt.addAll(picker, FlowLayout.encloseCenterMiddle(btnPlay));
        form3.add(BorderLayout.SOUTH, cnt);

        btnPlay.addActionListener(ev -> {
            try {
                /*InputStream input = Display.getInstance()
                    .getResourceAsStream(getClass(), "/circle_anim.gif");
            form3.add(CENTER, new ScaleImageLabel(GifImage
                    .decode(input, 1177720)));*/
                //[circle_anim.gif 171400] [acquire_anim 20480] [wave 12288]
                String anim = null;
                int len = 0;
                switch (picker.getText()) {

                    case "Acquire":
                        anim = "/acquire_anim.gif";
                        len = 20480;
                        break;

                    case "Wave":
                        anim = "/wave.gif";
                        len = 12288;
                        break;

                    case "Circle":
                        anim = "/circle_anim.gif";
                        len = 171400;
                        //len=1177720;
                        //len=167380;
                        break;

                    case "Bool":
                        anim = "/bool.gif";
                        len = 61440;
                        break;

                    case "Circular":
                        anim = "/circular.gif";
                        len = 548864;
                        break;

                    case "Fluid":
                        anim = "/fluid_load.gif";
                        len = 1011712;
                        break;

                    case "Fragmented Square":
                        anim = "/fragmented_square.gif";
                        len = 90112;
                        break;

                    case "Holographic Radar":
                        anim = "/holographic_radar.gif";
                        len = 1118208;
                        break;

                    case "Infinity":
                        anim = "/infinity.gif";
                        len = 65536;
                        break;

                    case "Plane":
                        anim = "/plane.gif";
                        len = 819200;
                        break;

                    case "Page Loading":
                        anim = "/page_loading.gif";
                        len = 86016;
                        break;

                    case "Pastel Bead":
                        anim = "/pastel_bead_loader.gif";
                        len = 180224;
                        break;

                    case "Pay Loader":
                        anim = "/pay_loader.gif";
                        len = 208896;
                        break;

                    case "Playoffs":
                        anim = "/playoffs.gif";
                        len = 57344;
                        break;

                    case "Rotating Circles":
                        anim = "/rotating_circles.gif";
                        len = 176128;
                        break;
                }
                //len - length of inputstream that is decoded when opening gif

                GifImage img = GifImage
                        .decode(getResourceAsStream(anim), len);

                //form3.add(CENTER, new ScaleImageLabel(img));
                form3.add(CENTER, new ImageViewer(img));

                form3.revalidate();

            } catch (IOException e) {
                proc.printLine("Err " + e);
            }
        });

        return form3;
    }

    private Form getCircleAnim() {

        Form form4 = proc.getInputForm();

        SpanLabel lblName = new SpanLabel("placeholder", "CenterAlignmentLabel");
        //final int circleSize=convertToPixels(10);
        final int circleSize = 80;
        Container cntName = new Container(new BorderLayout()) {
            @Override
            protected Dimension calcPreferredSize() {
                return new Dimension(circleSize, circleSize);
            }
        };

        cntName.add(BorderLayout.CENTER, lblName);
        Container cntDemo = BorderLayout.centerCenter(cntName);
        //replace the label with a CircleProgress to indicate that it is loading
        CircleProgress.markComponentLoading(lblName).setUIID("CircleAnim");

        /*EasyThread.start("").run(()->{
            sleep(3000);
            Response<Map> jsonData=Rest.
                    get("https://anapioficeandfire.com/api/characters/583")
                    .acceptJson()
                    .getAsJsonMap();
            
            callSerially(()->{
                lblName.setText(((Map<String,String>)jsonData.getResponseData()).get("name"));
                //replace process with lblName now that it's ready, using a 
                //fade transition
                CircleProgress.markComponentReady(lblName,CommonTransitions.createFade(300));
            });
           
        });*/
        form4.add(new SpanLabel("Processing, please wait", "lblStatus"));
        form4.add(cntDemo);

        return form4;

    }

    private Container getTextAnim() {

        // Form form5 = proc.getInputForm();
        SpanLabel profileText = new SpanLabel("placeholder", "DemoLabel");
        Container cntDemo = BorderLayout.center(profileText);

        //replace the label with a TextProgress to indicate that it is loading
        LoadingTextAnimation.markComponentLoading(profileText).setUIID("textAnim");

//        EasyThread.start("").run(() -> {
//            sleep(3000);
//            Response<Map> response = Rest.
//                    get("https://anapioficeandfire.com/api/characters/583")
//                    .acceptJson()
//                    .getAsJsonMap();
//
//            Map<String, Object> data = response.getResponseData();
//            StringBuilder sb = new StringBuilder();
//            sb.append("name: " + data.get("name") + "\n")
//                    .append("gender: " + data.get("gender") + "\n")
//                    .append("culture: " + data.get("culture") + "\n")
//                    .append("born: " + data.get("born") + "\n");
//
//            List<String> aliases = (ArrayList<String>) data.get("aliases");
//            sb.append("aliases: \n");
//            for (String alias : aliases) {
//                sb.append(alias + "\n");
//            }
//            callSerially(() -> {
//                cntDemo.removeAll();
//                cntDemo.add(BorderLayout.NORTH, profileText);
//                profileText.setText(sb.toString());
//                //profileText.setText("");
//                //replace process with profileText now that it's ready, using a 
//                //fade transition
//                LoadingTextAnimation.markComponentReady(profileText,
//                        CommonTransitions.createFade(300));
//            });
        //       });
        //form5.add(cntDemo);
        //return form5;
        return cntDemo;

    }

    private Container getSliderAnim() {

        Container cnt = new Container(BoxLayout.y(), "cntSlider");
        Slider sliderProgress = proc.createDownloadSlider();
        //Slider sliderProgress = new Slider();
        //sliderProgress.setUIID("sliderProgress");
        sliderProgress.setInfinite(true);
        cnt.add(sliderProgress);
        return cnt;
    }

    private Container getCustomAnim() {
        cntAllCustom = new Container(BoxLayout.y(), "cntAll");
        horizontalAnim1();
        return cntAllCustom;
    }

    private void horizontalAnim1() {
        Form formParAnim = new Form(BoxLayout.x());
        formParAnim.getToolbar().hideToolbar();
        formParAnim.setUIID("formParAnim");
        ArrayList<Container> cntArr = new ArrayList<>();

        for (int k = 0; k < 11; k++) {
            Container cntAnim = new Container(BoxLayout.y(), "cntHorizontalAnim1");
            formParAnim.add(cntAnim);
            cntArr.add(cntAnim);
        }
        cntAllCustom.add(FlowLayout.encloseCenterMiddle(formParAnim));

        new UITimer(() -> {

            for (int c = 0; c < cntArr.size(); c++) {
                if (c + 1 == count1 || c + 2 == count1 || c + 3 == count1
                        || c + 4 == count1 || c + 5 == count1 || c + 6 == count1
                        || c + 7 == count1 || c + 8 == count1 || c + 9 == count1
                        || c + 10 == count1) {
                    cntArr.get(c).setUIID("cntHorizontalAnim1Sel");
                } else {
                    cntArr.get(c).setUIID("cntHorizontalAnim1");
                }
                formParAnim.revalidate();
            }
            count1++;
            if (count1 == 12) {
                count1 = 0;
            }
            //300
        }).schedule(100, true, formParAnim);
        horizontalAnim1B();
    }

    private void horizontalAnim1B() {
        Form formParAnim = new Form(BoxLayout.x());
        formParAnim.getToolbar().hideToolbar();
        formParAnim.setUIID("formParAnim");
        ArrayList<Container> cntArr = new ArrayList<>();

        for (int k = 0; k < 13; k++) {
            Container cntAnim = new Container(BoxLayout.y(), "cntHorizontalAnim1");
            formParAnim.add(cntAnim);
            cntArr.add(cntAnim);
        }

        //Log.p("ArrSize="+cntArr.size()); //13
        cntAllCustom.add(FlowLayout.encloseCenterMiddle(formParAnim));

        new UITimer(() -> {

            for (int c = 0; c < cntArr.size(); c++) { //12x

                //Log.p("c=" + c);
                //Log.p("Count1B=" + count1B);
                if (c + 1 == count1B || c + 2 == count1B || c + 3 == count1B) {

                    cntArr.get(c).setUIID("cntHorizontalAnim1Sel");

                } else {
                    cntArr.get(c).setUIID("cntHorizontalAnim1");

                }

                if (c + 4 == count1B || c + 5 == count1B || c + 6 == count1B) {
                    cntArr.get(c).setUIID("cntHorizontalAnim1Sel");
                } else {
                    cntArr.get(c).setUIID("cntHorizontalAnim1");
                }

//                if (c + 7 == count1B || c + 8 == count1B || c + 9 == count1B) {
//                    cntArr.get(c).setUIID("cntHorizontalAnim1Sel");
//                } else {
//                    cntArr.get(c).setUIID("cntHorizontalAnim1");
//                }
//
//                if (c + 10 == count1B || c + 11 == count1B || c + 12 == count1B) {
//                    //cntArr.get(c).setUIID("cntHorizontalAnim1Sel");
//                    //cntArr.get(c).setUIID("cntHorizontalAnim1Sel");
//                    cntArr.get(9).setUIID("cntHorizontalAnim1Sel");
//                    cntArr.get(10).setUIID("cntHorizontalAnim1Sel");
//                    cntArr.get(11).setUIID("cntHorizontalAnim1Sel");
//                } else {
//                    cntArr.get(c).setUIID("cntHorizontalAnim1");
//                }
            }
            formParAnim.revalidate();
            count1B++;
            // count1B = count1B + 1;

            if (count1B == 19) {
                count1B = 0;
            }

            //250
        }).schedule(100, true, formParAnim);

        horizontalAnim2();
    }

    private void horizontalAnim2() {
        Form formParAnim = new Form(BoxLayout.x());
        formParAnim.getToolbar().hideToolbar();
        formParAnim.setUIID("formParAnim");
        ArrayList<Container> cntArr = new ArrayList<>();

        for (int k = 0; k < 11; k++) {
            Container cntAnim = new Container(BoxLayout.y(), "cntHorizontalAnim2");
            formParAnim.add(cntAnim);
            cntArr.add(cntAnim);

        }
        cntAllCustom.add(FlowLayout.encloseCenterMiddle(formParAnim));

        new UITimer(() -> {

            for (int c = 0; c < cntArr.size(); c++) {
                if (c + 1 == count2 || c + 2 == count2 || c + 3 == count2
                        || c + 4 == count2 || c + 5 == count2 || c + 6 == count2
                        || c + 7 == count2 || c + 8 == count2 || c + 9 == count2
                        || c + 10 == count2) {
                    cntArr.get(c).setUIID("cntHorizontalAnim2Sel");
                } else {
                    cntArr.get(c).setUIID("cntHorizontalAnim2");
                }
                formParAnim.revalidate();
            }
            count2++;
            if (count2 == 12) {
                count2 = 0;
            }
            //300
        }).schedule(100, true, formParAnim);
        horizontalAnim3();
    }

    private void horizontalAnim3() {
        Form formParAnim = new Form(BoxLayout.x());
        formParAnim.getToolbar().hideToolbar();
        formParAnim.setUIID("formParAnim");
        ArrayList<RadioButton> radArr = new ArrayList<>();

        for (int k = 0; k < 11; k++) {
            RadioButton cntAnim = new RadioButton();
            formParAnim.add(cntAnim);
            radArr.add(cntAnim);
        }

        cntAllCustom.add(FlowLayout.encloseCenterMiddle(formParAnim));

        new UITimer(() -> {

            for (int c = 0; c < radArr.size(); c++) {
                if (c + 1 == count3 || c + 2 == count3 || c + 3 == count3
                        || c + 4 == count3 || c + 5 == count3 || c + 6 == count3
                        || c + 7 == count3 || c + 8 == count3 || c + 9 == count3
                        || c + 10 == count3) {
                    radArr.get(c).setSelected(true);
                } else {
                    radArr.get(c).setSelected(false);
                }
                formParAnim.revalidate();
            }
            count3++;
            if (count3 == 12) {
                count3 = 0;
            }
        }).schedule(300, true, formParAnim);
        circleAnim1();
    }

    private void circleAnim1() {

        TableLayout tlPar = new TableLayout(3, 1);
        Form formParAnim = new Form(tlPar);
        formParAnim.getToolbar().hideToolbar();
        formParAnim.setUIID("formParCircleAnim");
        ArrayList<RadioButton> radArr = new ArrayList<>();

        FontImage radEmptyImg = proc.circleAnimIcon(FontImage.MATERIAL_RADIO_BUTTON_UNCHECKED);
        FontImage radFullImg = proc.circleAnimIcon(FontImage.MATERIAL_RADIO_BUTTON_CHECKED);
        ((DefaultLookAndFeel) UIManager.getInstance().getLookAndFeel())
                .setRadioButtonImages(radFullImg, radEmptyImg, radFullImg,
                        radEmptyImg);

        Container cnt1CircleAnim1 = new Container(BoxLayout.x(), "cnt1CircleAnim1");
        RadioButton rad1CircleAnim1 = new RadioButton();
        rad1CircleAnim1.setUIID("rad1CircleAnim1");
        RadioButton rad2CircleAnim1 = new RadioButton();
        rad2CircleAnim1.setUIID("rad2CircleAnim1");
        RadioButton rad3CircleAnim1 = new RadioButton();
        rad3CircleAnim1.setUIID("rad3CircleAnim1");
        cnt1CircleAnim1.addAll(rad1CircleAnim1, rad2CircleAnim1, rad3CircleAnim1);

        formParAnim.add(tlPar.createConstraint().heightPercentage(20),
                FlowLayout.encloseCenterMiddle(cnt1CircleAnim1));

        Container cnt2And4 = new Container(BoxLayout.x(), "cnt2And4");

        Container cnt2CircleAnim1 = new Container(BoxLayout.y(), "cnt2CircleAnim1");
        RadioButton rad4CircleAnim1 = new RadioButton();

        RadioButton rad5CircleAnim1 = new RadioButton();

        RadioButton rad6CircleAnim1 = new RadioButton();

        rad4CircleAnim1.setUIID("rad4CircleAnim1");
        rad5CircleAnim1.setUIID("rad5CircleAnim1");
        rad6CircleAnim1.setUIID("rad6CircleAnim1");

        cnt2CircleAnim1.addAll(FlowLayout.encloseRightMiddle(rad4CircleAnim1),
                FlowLayout.encloseRightMiddle(rad5CircleAnim1),
                FlowLayout.encloseRightMiddle(rad6CircleAnim1));

        Container cnt4CircleAnim1 = new Container(BoxLayout.y(), "cnt4CircleAnim1");
        RadioButton rad10CircleAnim1 = new RadioButton();
        rad10CircleAnim1.setUIID("rad10CircleAnim1");
        RadioButton rad11CircleAnim1 = new RadioButton();
        rad11CircleAnim1.setUIID("rad11CircleAnim1");
        RadioButton rad12CircleAnim1 = new RadioButton();
        rad12CircleAnim1.setUIID("rad12CircleAnim1");
        cnt4CircleAnim1.addAll(FlowLayout.encloseRightMiddle(rad10CircleAnim1),
                FlowLayout.encloseRightMiddle(rad11CircleAnim1),
                FlowLayout.encloseRightMiddle(rad12CircleAnim1));

        cnt2And4.addAll(cnt4CircleAnim1, FlowLayout.encloseRightMiddle(cnt2CircleAnim1));

        formParAnim.add(tlPar.createConstraint().heightPercentage(57), cnt2And4);

        Container cnt3CircleAnim1 = new Container(BoxLayout.x(), "cnt3CircleAnim1");
        RadioButton rad7CircleAnim1 = new RadioButton();
        rad7CircleAnim1.setUIID("rad7CircleAnim1");
        RadioButton rad8CircleAnim1 = new RadioButton();
        rad8CircleAnim1.setUIID("rad8CircleAnim1");
        RadioButton rad9CircleAnim1 = new RadioButton();
        rad9CircleAnim1.setUIID("rad9CircleAnim1");
        cnt3CircleAnim1.addAll(FlowLayout.encloseIn(rad7CircleAnim1),
                FlowLayout.encloseIn(rad8CircleAnim1),
                FlowLayout.encloseIn(rad9CircleAnim1));

        formParAnim.add(tlPar.createConstraint().heightPercentage(25),
                FlowLayout.encloseCenterMiddle(cnt3CircleAnim1));

        radArr.add(rad1CircleAnim1);
        radArr.add(rad2CircleAnim1);
        radArr.add(rad3CircleAnim1);

        radArr.add(rad4CircleAnim1);
        radArr.add(rad5CircleAnim1);
        radArr.add(rad6CircleAnim1);

        radArr.add(rad9CircleAnim1);
        radArr.add(rad8CircleAnim1);
        radArr.add(rad7CircleAnim1);

        radArr.add(rad12CircleAnim1);
        radArr.add(rad11CircleAnim1);
        radArr.add(rad10CircleAnim1);

        cntAllCustom.add(FlowLayout.encloseCenterMiddle(formParAnim));

        new UITimer(() -> {

            for (int c = 0; c < radArr.size(); c++) {

                // Log.p("Count=" + radCount);
                if (/*c == radCount ||*/c + 1 == radCount || c + 2 == radCount || c + 3 == radCount
                        || c + 4 == radCount || c + 5 == radCount || c + 6 == radCount
                        || c + 7 == radCount || c + 8 == radCount || c + 9 == radCount
                        || c + 10 == radCount || c + 11 == radCount || c + 12 == radCount) {
                    //radArr.get(c).setUIID("radSel");
                    radArr.get(c).setSelected(true);
                } else {
                    //radArr.get(c).setUIID("radDefault");
                    radArr.get(c).setSelected(false);
                }
                formParAnim.revalidate();

            }

            radCount++;

            if (radCount == 13) {
                radCount = 0;
            }

            //150, 100, 50
        }).schedule(50, true, formParAnim);

    }

    private void circleAnim2() {
        Container cnt = new Container(BoxLayout.y());
        cnt.add(new SpanLabel("Processing request please\n wait",
                "lblStatus"));

        Container cntAnim = new Container(new LayeredLayout());

        CircleBars bars = new CircleBars(form);
        bars.setUIID("CircleBars");
        cntAnim.add(bars);

        cnt.add(FlowLayout.encloseCenterMiddle(cntAnim));

        form.add(CENTER, FlowLayout.encloseCenterMiddle(cnt));
        form.revalidate();
        cntAnim.revalidate();
    }

    public class CircleBars extends Component {

        Form formParAnim;

        public CircleBars(Form formParAnim) {
            this.formParAnim = formParAnim;
        }

        @Override
        public Dimension calcPreferredSize() {
            return new Dimension(dimen, dimen);
        }

        @Override
        public void paintBackground(Graphics graphics) {
            super.paintBackground(graphics);
            graphics.setAntiAliased(true);

            double padding = 10;

            //clock radius
            double r = Math.min(getWidth(), getHeight()) / 2 - padding;

            //center point
            double cX = getX() + getWidth() / 2;
            double cY = getY() + getHeight() / 2;

            int tickLen; //at the quarters //0xCCCCCC 0x0000FF 0x3399ff

            ArrayList<Stroke> strokeArr = new ArrayList<>();
            ArrayList<GeneralPath> pathArr = new ArrayList<>();

            int max = 10;
            //Draw tick for each sec 1-10
            for (int j = 1; j <= max; j++) {

                Stroke tickStroke;
                if (Display.getInstance().isDesktop()) {
                    tickLen = 5;//5 10
                    //strokeWidth, strokeStyle, strokeJoinStyle, miterLimit
                    tickStroke = new Stroke(3f, Stroke.CAP_SQUARE,
                            Stroke.JOIN_ROUND, 10f); //5f 1f 10f
                } else {
                    tickLen = 8;
                    tickStroke = new Stroke(7f, Stroke.CAP_SQUARE,
                            Stroke.JOIN_ROUND, 3f);
                }
                strokeArr.add(tickStroke);

                GeneralPath ticksPath = new GeneralPath();
                //convert tick num to double
                double dj = (double) j;

                //Get angle from 12 0'clock to this tick (radians)
                double angleFrom12 = dj / 10.0 * 2.0 * Math.PI;

                //Get angle from 3 0'clock to this tick
                double angleFrom3 = Math.PI / 2.0 - angleFrom12;

                //Move to the outer edge of the circle
                ticksPath.moveTo(
                        (float) (cX + Math.cos(angleFrom3) * r),
                        (float) (cY - Math.sin(angleFrom3) * r)
                );

                //Draw line inward along radius for length of the tick mark
                ticksPath.lineTo(
                        (float) (cX + Math.cos(angleFrom3) * (r - tickLen)),
                        (float) (cY - Math.sin(angleFrom3) * (r - tickLen))
                );

                pathArr.add(ticksPath);
            }

            //proc.printLine("PathArrSize=" + pathArr.size() + " StrokeArrSize="
            // + strokeArr.size());
            for (int g = 0; g < pathArr.size(); g++) {

                if (g == fixedCount) {
                    //proc.printLine("FixedCountHgh=" + fixedCount);
                    if (proc.getDarkMode().equals("On")) {
                        graphics.setColor(0x0000FF); //0x0000FF 0x15E7FF
                    } else {
                        graphics.setColor(0x15E7FF);
                    }
                    graphics.drawShape(pathArr.get(fixedCount), strokeArr.get(fixedCount));
                } else {
                    //proc.printLine("FixedCount=" + fixedCount);
                    if (proc.getDarkMode().equals("On")) {
                        graphics.setColor(0x15E7FF); //0xFF0000
                    } else {
                        graphics.setColor(0x0000FF);
                    }
                    graphics.drawShape(pathArr.get(g), strokeArr.get(g));
                }
            }

            new UITimer(() -> {
                fixedCount = fixedCount + 1;
                if (fixedCount == max) {
                    fixedCount = 0;
                }

                if (showCircleBars) {
                    circleAnim2();
                }

            }).schedule(duration, false, formParAnim);
        }
    }

    private void circleAnim3(String direction, int duration) {
        // Display.getInstance().callSerially(() -> {
        Container cnt = new Container(BoxLayout.y());
        cnt.add(new SpanLabel("Processing request please\n wait",
                "lblStatus"));
        Form formParAnim3 = new Form(new BorderLayout());
        formParAnim3.getToolbar().hideToolbar();
        formParAnim3.setUIID("formProgressBar");

        cnt.add(FlowLayout.encloseCenterMiddle(paintAnim3(formParAnim3,
                direction, duration)));
        form.add(CENTER, FlowLayout.encloseCenterMiddle(cnt));
        form.revalidate();
        //form.revalidateLater();
        //form.revalidateWithAnimationSafety();
        //});
    }

    public Form paintAnim3(Form formParAnim, String direction, int duration) {

        Container cntPar = new Container(BoxLayout.y());
        Container cntMask = new Container(new LayeredLayout());

        // 0xff000000 0x0000FF 0x15E7FF 0x3399ff
        Image roundMask = Image.createImage(70, 70, 0);
        Graphics gr = roundMask.getGraphics();
        //gr.setColor(0x0000FF);
        if (proc.getDarkMode().equals("On")) {
            gr.setColor(0x15E7FF);
        } else {
            gr.setColor(0x0000FF);
        }
        //gr.fillArc(0, 0, 200, 200, 0, currentProgress);
        if (direction.equals("A")) {
            //gr.fillArc(0, 0, 80, 80, 120, currentProgress);
            switch (cycle) {
                case 1:
                    gr.fillArc(0, 0, 70, 70, 120, currentProgress);
                    break;
                case 2:
                    gr.fillArc(0, 0, 70, 70, 240, currentProgress);
                    break;
                case 3:
                    gr.fillArc(0, 0, 70, 70, 345, currentProgress);
                    break;
            }
        } else if (direction.equals("C")) {
            //gr.fillArc(0, 0, 80, 80, 60, currentProgress);
            switch (cycle) {
                case 1:
                    gr.fillArc(0, 0, 70, 70, 60, currentProgress);
                    break;
                case 2:
                    gr.fillArc(0, 0, 70, 70, 245, currentProgress);
                    break;
                case 3:
                    gr.fillArc(0, 0, 70, 70, 120, currentProgress);
                    break;
            }
        }
        Object mask = roundMask.createMask();
        roundMask.applyMask(mask);
        Label lbl = new Label(roundMask);
        cntMask.add(FlowLayout.encloseCenterMiddle(lbl));

        Image roundMask2 = Image.createImage(60, 60, 0);
        Graphics gr2 = roundMask2.getGraphics();
        if (proc.getDarkMode().equals("On")) {
            gr2.setColor(0x29293D);
        } else {
            gr2.setColor(0xffffff);
        }

        gr2.fillArc(0, 0, 60, 60, 0, 360);

        Object mask2 = roundMask2.createMask();
        roundMask2.applyMask(mask2);
        Label lbl2 = new Label(roundMask2);
        cntMask.addAll(FlowLayout.encloseCenterMiddle(lbl2));
        cntPar.add(cntMask);

        formParAnim.add(CENTER, cntPar);

        //proc.printLine("Progress " + currentProgress);
        new UITimer(() -> {

            if (direction.equals("A")) {
                if (currentProgress < 361) {
                    currentProgress = currentProgress + 10;
                } else if (currentProgress == 370) {
                    currentProgress = 10;
                    proc.printLine("A Cycle=" + cycle);
                    switch (cycle) {
                        case 1:
                            cycle = 2;
                            break;
                        case 2:
                            cycle = 3;
                            break;
                        case 3:
                            cycle = 1;
                            break;
                    }
                }
            } else if (direction.equals("C")) {

                if (currentProgress > -361) {
                    currentProgress = currentProgress - 10;
                } else if (currentProgress == -370) {
                    currentProgress = 10;
                    proc.printLine("C Cycle=" + cycle);
                    switch (cycle) {
                        case 1:
                            cycle = 2;
                            break;
                        case 2:
                            cycle = 3;
                            break;
                        case 3:
                            cycle = 1;
                            break;
                    }
                }
            }

            circleAnim3(direction, duration);

            //250 150 50 10
        }).schedule(duration, true, formParAnim);

        return formParAnim;
    }

    private void circleAnim4(String direction, int duration) {
        //Display.getInstance().callSerially(() -> {
        Container cnt = new Container(BoxLayout.y());
        cnt.add(new SpanLabel("Processing request please\n wait",
                "lblStatus"));
        Form formParAnim3 = new Form(new BorderLayout());
        formParAnim3.getToolbar().hideToolbar();
        formParAnim3.setUIID("formProgressBar");

        cnt.add(FlowLayout.encloseCenterMiddle(paintAnim4(formParAnim3, direction, duration)));
        form.add(CENTER, FlowLayout.encloseCenterMiddle(cnt));
        form.revalidate();
        //form.revalidateLater();
        //form.revalidateWithAnimationSafety();
        //});
    }

    private Form paintAnim4(Form formParAnim, String direction, int duration) {

        int dimen1 = 70, dimen2 = 55;
        Container cntPar = new Container(BoxLayout.y());
        Container cntMask = new Container(new LayeredLayout());

        // 0xff000000 0x0000FF 0x15E7FF 0x3399ff
        Image roundMask = Image.createImage(dimen1, dimen1, 0);
        Graphics gr = roundMask.getGraphics();

        if (direction.equals("A")) {
            start = currentProgress; //10 20
            end = currentProgress + 50; //60 70
            start2 = end; //60 70
            end2 = (360 - end) + currentProgress; //310 310
            //end2 = 360 + start; //350 340
            //end2=360;

            /*#15E7FF  #BDE2EF  #C8E8F3  #D1EBF4*/
            gr.setColor(0x15E7FF);
            gr.fillArc(0, 0, dimen1, dimen1, start, end);
            gr.setColor(0x0000FF);
            gr.fillArc(0, 0, dimen1, dimen1, start2, end2);

            //proc.printLine("Progress=" + currentProgress + " Start=" + start + " End=" + end
            // + " Start2=" + start2 + " End2=" + end2);
        } else if (direction.equals("C")) {
            start = currentProgress2; //-10 -20
            end = currentProgress2 - 50; //-60 -70
            start2 = end; //-60 -70
            //end2 = (-360 - end) - currentProgress2; //-310 -310
            end2 = -310;

            gr.setColor(0x15E7FF);
            gr.fillArc(0, 0, dimen1, dimen1, start, end);
            gr.setColor(0x0000FF);
            gr.fillArc(0, 0, dimen1, dimen1, start2, end2);

            //proc.printLine("Progress=" + currentProgress2 + " Start=" + start + " End=" + end
            // + " Start2=" + start2 + " End2=" + end2);
        }

        //if (direction.equals("A")) {
        // if (start == 10 && end == 60) {
        /* gr.setColor(0xFF0000);
            gr.fillArc(0, 0, dimen, dimen, start, end);
            gr.setColor(0x0000FF);
            gr.fillArc(0, 0, dimen, dimen, start2, end2);*/
        //}
//            if (start == 60 && end == 110) {
//                gr.setColor(0xFF0000);
//                gr.fillArc(0, 0, dimen, dimen, start, end);
//                start = end;
//                end = end + 50;
//            } else {
//                gr.setColor(0x0000FF);
//                gr.fillArc(0, 0, dimen, dimen, 110, 360);
//            }
        //}
        Object mask = roundMask.createMask();
        roundMask.applyMask(mask);
        Label lbl = new Label(roundMask);
        cntMask.add(FlowLayout.encloseCenterMiddle(lbl));

        Image roundMask2 = Image.createImage(dimen2, dimen2, 0);
        Graphics gr2 = roundMask2.getGraphics();
        if (proc.getDarkMode().equals("On")) {
            gr2.setColor(0x29293D);
        } else {
            gr2.setColor(0xffffff);
        }
        //gr2.setColor(0xff000000); //0xffffff 0x15E7FF 0xff000000

        //gr2.fillArc(0, 0, 175, 175, 0, 360);
        //if (direction.equals("A")) {
        //gr2.fillArc(0, 0, dimen2, dimen2, 120, currentProgress);
        gr2.fillArc(0, 0, dimen2, dimen2, 0, 360);
        //} else if (direction.equals("C")) {
        //gr2.fillArc(0, 0, dimen2, dimen2, 60, currentProgress);
        //}

        //gr2.fillArc(0, 0, 175, 175, -10, -360);
        Object mask2 = roundMask2.createMask();
        roundMask2.applyMask(mask2);
        Label lbl2 = new Label(roundMask2);
        cntMask.addAll(FlowLayout.encloseCenterMiddle(lbl2));
        cntPar.add(cntMask);

        formParAnim.add(CENTER, cntPar);

        new UITimer(() -> {

            if (direction.equals("A")) {
                if (currentProgress < 361) {
                    currentProgress = currentProgress + 10;
                } else if (currentProgress == 370) {
                    currentProgress = 10;
                    //start = 10;
                    //end = 60;
                }
            } else if (direction.equals("C")) {
//             if (currentProgress > -281) {
//                currentProgress = currentProgress - 10;
//            } else if (currentProgress == -290) {
//                currentProgress = 60;
//            }
                if (currentProgress2 > -361) {
                    currentProgress2 = currentProgress2 - 10;
                } else if (currentProgress2 == -370) {
                    currentProgress2 = -10;
                    // start = -10;
                    //end = -60;
                }
            }

            //Display.getInstance().callSerially(() -> {
            circleAnim4(direction, duration);
            //});

            //250 150 50 10
        }).schedule(duration, true, formParAnim);

        return formParAnim;

    }

    private void waves1() {
        Display.getInstance().callSerially(() -> {
            Container cnt = new Container(BoxLayout.y());
            cnt.add(new SpanLabel("Processing request please\n wait",
                    "lblStatus"));
            Form formParAnim3 = new Form(new BorderLayout());
            formParAnim3.getToolbar().hideToolbar();
            formParAnim3.setUIID("formProgressBar");

            cnt.add(FlowLayout.encloseCenterMiddle(paintWave1(formParAnim3)));
            form.add(CENTER, FlowLayout.encloseCenterMiddle(cnt));
            form.revalidateLater();
        });
    }

    private Form paintWave1(Form formParAnim) {

        Container cntPar = new Container(BoxLayout.y());
        Container cntMask = new Container(new LayeredLayout());

        //int dimension = dimen; //300
        int dimension = 100; //100, 200

        ArrayList<Label> lblArr = new ArrayList<>();

        int max = dimension / 15;
        int blockDimen = 20;

        if (max > 0) {

            for (int w = 1; w <= max; w++) {

                if (dimension - 20 > blockDimen) {

                    if (dimension > 0) {
                        Image roundMask = Image.createImage(dimension, dimension, 0);
                        Graphics gr = roundMask.getGraphics();
                        if (proc.getDarkMode().equals("On")) {
                            gr.setColor(proc.colorTeal);
                        } else {
                            gr.setColor(proc.darkBlue);
                        }
                        gr.fillArc(0, 0, dimension, dimension, 0, 360);
                        Object mask = roundMask.createMask();
                        roundMask.applyMask(mask);
                        Label lbl = new Label(roundMask);
                        cntMask.add(FlowLayout.encloseCenterMiddle(lbl));
                        lblArr.add(lbl);

                        //dimension = dimension - 15;
                        dimension = dimension - 5;
                    }
                    if (dimension > 0) {
                        Image roundMask2 = Image.createImage(dimension, dimension, 0);
                        Graphics gr2 = roundMask2.getGraphics();
                        if (proc.getDarkMode().equals("On")) {
                            gr2.setColor(proc.blueGray);
                        } else {
                            gr2.setColor(proc.white);
                        }
                        gr2.fillArc(0, 0, dimension, dimension, 0, 360);
                        Object mask2 = roundMask2.createMask();
                        roundMask2.applyMask(mask2);
                        Label lbl2 = new Label(roundMask2);
                        cntMask.add(FlowLayout.encloseCenterMiddle(lbl2));

                        //dimension = dimension - 15;
                    }
                }

            }

            Image roundBlock = Image.createImage(blockDimen, blockDimen, 0);
            Graphics gr3 = roundBlock.getGraphics();
            if (proc.getDarkMode().equals("On")) {
                gr3.setColor(proc.colorTeal);
            } else {
                gr3.setColor(proc.darkBlue);
            }
            gr3.fillArc(0, 0, blockDimen, blockDimen, 0, 360);
            Object mask3 = roundBlock.createMask();
            roundBlock.applyMask(mask3);
            Label lblBlock = new Label(roundBlock);
            cntMask.add(FlowLayout.encloseCenterMiddle(lblBlock));
            lblArr.add(lblBlock);

            cntPar.add(cntMask);

            formParAnim.add(CENTER, cntPar);

            //make fist wave visible by default
            for (int len = 0; len < lblArr.size(); len++) {
                if (len == lblArr.size() - 1) {
                    lblArr.get(lblArr.size() - 1).setVisible(true);
                } else {
                    lblArr.get(len).setVisible(false);
                }
            }

            waveCount = lblArr.size();  // 3

            new UITimer(() -> {

                //proc.printLine("Wavecount=" + waveCount); //3, 2, 1
                for (int c = 0; c < lblArr.size(); c++) {
                    if (c == waveCount - 1) {
                        lblArr.get(waveCount - 1).setVisible(true); //2,1,0
                    } else {
                        lblArr.get(c).setVisible(false);
                    }
                }

                cntMask.revalidate();

                waveCount--;
                //Log.p("Wavecount2=" + waveCount);

                if (waveCount == 0) {
                    waveCount = lblArr.size();
                }

                //250
            }).schedule(165, true, formParAnim);
        }
        /* {
            Log.p("Dimension must be atleast >= 75");
        }*/

        return formParAnim;
    }

    private void waves2() {

        Container cnt = new Container(new LayeredLayout(), "cntWaveBarsPar");

        cnt.add(FlowLayout.encloseCenterMiddle(new SpanLabel("Processing request please\nwait",
                "lblStatus")));

        Container cntAnim = new Container(new BorderLayout(), "cntWaveBars");

        dimenWave = 40;

        CircleWaves waves = new CircleWaves(form, dimenWave);
        waves.setUIID("waveBars");
        cntAnim.add(CENTER, waves);

        dimenWave = dimenWave + 20;

        cnt.add(FlowLayout.encloseCenterBottom(cntAnim));

        form.add(CENTER, FlowLayout.encloseCenterMiddle(cnt));
        form.revalidate();

        new UITimer(() -> {

            if (showCircleBars) {
                CircleWaves waves2 = new CircleWaves(form, dimenWave);
                waves2.setUIID("waveBars");
                cntAnim.add(CENTER, waves2);
                cntAnim.revalidate();

                dimenWave = dimenWave + 20;

                if (dimenWave >= 240) {
                    dimenWave = 40;
                }
            }
            //200
        }).schedule(165, true, form);
    }

    public class CircleWaves extends Component {

        Form formParAnim;
        int dimension;

        public CircleWaves(Form formParAnim, int dimension) {
            this.formParAnim = formParAnim;
            this.dimension = dimension;
        }

        @Override
        public Dimension calcPreferredSize() {
            return new Dimension(this.dimension, this.dimension);
        }

        @Override
        public void paintBackground(Graphics graphics) {

            super.paintBackground(graphics);
            graphics.setAntiAliased(true);

            double padding = 10;

            //clock radius
            double r = Math.min(getWidth(), getHeight()) / 2 - padding;

            //center point
            double cX = getX() + getWidth() / 2;
            double cY = getY() + getHeight() / 2;

            int tickLen; //at the quarters //0xCCCCCC 0x0000FF 0x3399ff

            ArrayList<Stroke> strokeArr = new ArrayList<>();
            ArrayList<GeneralPath> pathArr = new ArrayList<>();

            int max = 10;
            //Draw tick for each sec 1-10
            for (int j = 1; j <= max; j++) {

                Stroke tickStroke;
                if (Display.getInstance().isDesktop()) {
                    tickLen = 5;//5 10
                    //strokeWidth, strokeStyle, strokeJoinStyle, miterLimit
                    tickStroke = new Stroke(3f, Stroke.CAP_SQUARE,
                            Stroke.JOIN_ROUND, 10f); //5f 1f 10f
                } else {
                    tickLen = 15;
                    tickStroke = new Stroke(10f, Stroke.CAP_SQUARE,
                            Stroke.JOIN_ROUND, 3f);
                }
                strokeArr.add(tickStroke);

                GeneralPath ticksPath = new GeneralPath();
                //convert tick num to double
                double dj = (double) j;

                //Get angle from 12 0'clock to this tick (radians)
                double angleFrom12 = dj / 10.0 * 2.0 * Math.PI;

                //Get angle from 3 0'clock to this tick
                double angleFrom3 = Math.PI / 2.0 - angleFrom12;

                //Move to the outer edge of the circle
                ticksPath.moveTo(
                        (float) (cX + Math.cos(angleFrom3) * r),
                        (float) (cY - Math.sin(angleFrom3) * r)
                );
                //Draw line inward along radius for length of the tick mark
                ticksPath.lineTo(
                        (float) (cX + Math.cos(angleFrom3) * (r - tickLen)),
                        (float) (cY - Math.sin(angleFrom3) * (r - tickLen))
                );
                pathArr.add(ticksPath);
            }

            for (int g = 0; g < pathArr.size(); g++) {

                if (g == fixedCount) {
                    if (proc.getDarkMode().equals("On")) {
                        graphics.setColor(proc.darkBlue);
                    } else {
                        graphics.setColor(proc.colorTeal);
                    }
                    graphics.drawShape(pathArr.get(fixedCount), strokeArr.get(fixedCount));
                } else {
                    if (proc.getDarkMode().equals("On")) {
                        graphics.setColor(proc.colorTeal);
                    } else {
                        graphics.setColor(proc.darkBlue);
                    }
                    graphics.drawShape(pathArr.get(g), strokeArr.get(g));
                }
            }
        }
    }

    private Container getWave3Container(String uiid) {
        Container cntBar = new Container(BoxLayout.y(), uiid);
        cntWave3Arr.add(cntBar);
        return cntBar;
    }

    private void waves3(String type) {

        cntWave3Arr.clear();
        //if(type.equals("bars")){
        duration = 60;
        //}

        Container cnt = new Container(BoxLayout.y());
        cnt.add(new SpanLabel("Processing request please\n wait",
                "lblStatus"));

        Form formParAnim = new Form(new GridLayout(2, 17));
        formParAnim.getToolbar().hideToolbar();
        formParAnim.setUIID("formParWave3Anim");

        for (int k = 0; k <= 16; k++) {
            Container cntWave = null;
            switch (k) {
                case 0:
                case 4:
                case 8:
                case 12:
                case 16:
                    if (type.equals("bars")) {
                        cntWave = getWave3Container("cntHorizontalWaveBar0");
                    } else if (type.equals("circles")) {
                        cntWave = getWave3Container("cntHorizontalWaveCircle0");
                    }
                    break;
                case 1:
                case 3:
                case 9:
                case 11:
                    if (type.equals("bars")) {
                        cntWave = getWave3Container("cntHorizontalWaveBar1");
                    } else if (type.equals("circles")) {
                        cntWave = getWave3Container("cntHorizontalWaveCircle1");
                    }
                    break;
                case 2:
                case 10:
                    if (type.equals("bars")) {
                        cntWave = getWave3Container("cntHorizontalWaveBar2");
                    } else if (type.equals("circles")) {
                        cntWave = getWave3Container("cntHorizontalWaveCircle2");
                    }
                    break;
                case 5:
                case 7:
                case 13:
                case 15:
                    if (type.equals("bars")) {
                        cntWave = getWave3Container("cntHorizontalWaveBar5");
                    } else if (type.equals("circles")) {
                        cntWave = getWave3Container("cntHorizontalWaveCircle5");
                    }
                    break;
                case 6:
                case 14:
                    if (type.equals("bars")) {
                        cntWave = getWave3Container("cntHorizontalWaveBar6");
                    } else if (type.equals("circles")) {
                        cntWave = getWave3Container("cntHorizontalWaveCircle6");
                    }
                    break;
            }

            formParAnim.add(FlowLayout.encloseCenterMiddle(cntWave));
        }

        cnt.add(FlowLayout.encloseCenterMiddle(formParAnim));

        form.add(CENTER, FlowLayout.encloseCenterMiddle(cnt));
        form.revalidate();

        new UITimer(() -> {

            for (int c = 0; c < cntWave3Arr.size(); c++) {

                //Log.p("Count=" + count);
                if (c + 1 == waveCount3 || c + 2 == waveCount3 || c + 3 == waveCount3
                        || c + 4 == waveCount3 || c + 5 == waveCount3 || c + 6 == waveCount3
                        || c + 7 == waveCount3 || c + 8 == waveCount3 || c + 9 == waveCount3
                        || c + 10 == waveCount3 || c + 11 == waveCount3 || c + 12 == waveCount3
                        || c + 13 == waveCount3 || c + 14 == waveCount3 || c + 15 == waveCount3
                        || c + 16 == waveCount3 || c + 17 == waveCount3 || c + 18 == waveCount3) {

                    cntWave3Arr.get(c).setVisible(true);

                } else {
                    cntWave3Arr.get(c).setVisible(false);
                }
                formParAnim.revalidate();

            }

            waveCount3++;

            if (waveCount3 == 18) {
                waveCount3 = 0;
            }

        }).schedule(duration, true, formParAnim);

    }

    private void scan() {

        Container cntScanPar = new Container(new LayeredLayout(), "cntScanPar");

        cntScanPar.add(FlowLayout.encloseCenterMiddle(new Label(
                proc.customIcon(FontImage.MATERIAL_FINGERPRINT,
                        proc.colorTeal, 12))));
        Form formScan = new Form(BoxLayout.y());
        formScan.getToolbar().hideToolbar();
        formScan.setUIID("formScan");
        ArrayList<Container> cntScanArr = new ArrayList<>();

        for (int k = 0; k <= 15; k++) {
            Container cntAnim = new Container(BoxLayout.y(), "cntScanAnim");
            formScan.add(cntAnim);
            cntScanArr.add(cntAnim);
        }
        cntScanPar.add(formScan);

        form.add(CENTER, FlowLayout.encloseCenterMiddle(cntScanPar));
        form.revalidate();

        for (int c = 0; c < cntScanArr.size(); c++) {
            cntScanArr.get(c).setVisible(false);
        }

        topScan = true;
        scanCount = 15;

        new UITimer(() -> {
            //proc.printLine("ScanCount=" + scanCount);
            for (int c = 0; c < cntScanArr.size(); c++) {

                if (c == scanCount) {
                    cntScanArr.get(c).setVisible(true);
                } else {
                    cntScanArr.get(c).setVisible(false);
                }
                formScan.revalidate();
            }

            if (topScan) {
                scanCount--;
                if (scanCount == 0) {
                    topScan = false;
                }

            } else {
                scanCount++;
                if (scanCount == 15) {
                    topScan = true;
                }
            }

            //100 duration
        }).schedule(80, true, formScan);

    }

    private void bubble() {
        Form formParAnim = new Form(BoxLayout.x());
        formParAnim.getToolbar().hideToolbar();
        formParAnim.setUIID("formParAnim");
        ArrayList<Container> cntArr = new ArrayList<>();

        for (int k = 0; k <= 2; k++) {
            Container cntBubble = new Container(BoxLayout.y(), "cntBubbleSmall");
            formParAnim.add(cntBubble);
            cntArr.add(cntBubble);
        }
        form.add(CENTER, FlowLayout.encloseCenterMiddle(formParAnim));
        form.revalidate();

        new UITimer(() -> {

            //Log.p("BubbleCount=" + bubbleCount);
            //for (int c = 0; c < cntArr.size(); c++) {
            //Log.p("Count=" + count);
            /*if (c == bubbleCount) {
                    cntArr.get(c).setUIID("cntBubbleSel");
                } else {
                    cntArr.get(c).setUIID("cntBubble");
                }*/
            switch (bubbleCycle) {
                case 1:
                    cntArr.get(0).setUIID("cntBubbleSmall");
                    cntArr.get(1).setUIID("cntBubbleMedium");
                    cntArr.get(2).setUIID("cntBubbleLarge");
                    break;
                case 2:
                    cntArr.get(0).setUIID("cntBubbleMedium");
                    cntArr.get(1).setUIID("cntBubbleSmall");
                    cntArr.get(2).setUIID("cntBubbleMedium");
                    break;
                case 3:
                    cntArr.get(0).setUIID("cntBubbleLarge");
                    cntArr.get(1).setUIID("cntBubbleMedium");
                    cntArr.get(2).setUIID("cntBubbleSmall");
                    break;
            }
            formParAnim.revalidate();

            //}
            //bubbleCount++;
            //bubbleCycle++;

            /*if (bubbleCount == 2) {
                bubbleCount = 0;*/
            proc.printLine("BubbleCycle=" + bubbleCycle);

            switch (bubbleCycle) {
                case 1:
                    //bubbleCycle = 2;
                    bubbleCycle = 3;
                    break;
                case 2:
                    bubbleCycle = 3;
                    break;
                case 3:
                    bubbleCycle = 1;
                    break;

            }
            //duration

        }).schedule(250, true, formParAnim);

    }

    private void waves4() {
        Display.getInstance().callSerially(() -> {
            Container cnt = new Container(BoxLayout.y());
            cnt.add(new SpanLabel("Processing request please\n wait",
                    "lblStatus"));
            Form formParAnim3 = new Form(new BorderLayout());
            formParAnim3.getToolbar().hideToolbar();
            formParAnim3.setUIID("formProgressBar");

            cnt.add(FlowLayout.encloseCenterMiddle(paintWave4(formParAnim3)));
            form.add(CENTER, FlowLayout.encloseCenterMiddle(cnt));
            form.revalidate();
            //form.revalidateLater();
        });
    }

    private Form paintWave4(Form formParAnim) {

        Container cntPar = new Container(BoxLayout.x());
        //Container cntPar = new Container(new GridLayout(1, 5));

        //int dimension = dimen; //300
        int dimension = dimen; //100, 200

        ArrayList<Label> lblArr = new ArrayList<>();

        for (int w = 0; w <= 4; w++) {

            Container cntMask = new Container(new LayeredLayout(), "cntMaskWave4");

            Image roundMask = Image.createImage(dimension, dimension, 0);
            Graphics gr = roundMask.getGraphics();
            if (proc.getDarkMode().equals("On")) {
                gr.setColor(proc.colorTeal);
            } else {
                gr.setColor(proc.darkBlue);
            }

            // gr.fillArc(0, 0, dimension, dimension, 0, 360);
            switch (w) {
                case 0:
                    gr.fillArc(0, 0, dimension, dimension, 5, -220);
                    break;
                case 1:
                    gr.fillArc(0, 0, dimension, dimension, -5, 190);
                    break;
                case 2:
                    gr.fillArc(0, 0, dimension, dimension, 5, -190);
                    break;
                case 3:
                    gr.fillArc(0, 0, dimension, dimension, -40, 230);
                    break;
                case 4:
                    gr.fillArc(0, 0, dimension, dimension, 5, -220);
                    break;
                default:
                    break;
            }

            Object mask = roundMask.createMask();
            roundMask.applyMask(mask);
            Label lbl = new Label(roundMask, "lblMaskWave4");
            cntMask.add(FlowLayout.encloseCenterMiddle(lbl));
            lblArr.add(lbl);

            dimension = dimension - 20;

            Image roundMask2 = Image.createImage(dimension, dimension, 0);
            Graphics gr2 = roundMask2.getGraphics();
            if (proc.getDarkMode().equals("On")) {
                gr2.setColor(proc.blueGray);
            } else {
                gr2.setColor(proc.white);
            }

            gr2.fillArc(0, 0, dimension, dimension, 0, 360);
            Object mask2 = roundMask2.createMask();
            roundMask2.applyMask(mask2);
            Label lbl2 = new Label(roundMask2, "lblMaskWave4");
            cntMask.add(FlowLayout.encloseCenterMiddle(lbl2));

            dimension = dimension + 20;

            cntPar.add(cntMask);

        }

        formParAnim.add(CENTER, cntPar);

        //hide last wave
        lblArr.get(lblArr.size() - 1).setVisible(false);

        new UITimer(() -> {

            //proc.printLine("WaveCount4=" + waveCount4); //3, 2, 1
            for (int c = 0; c < lblArr.size(); c++) {
                if (c + 1 == waveCount4 || c + 2 == waveCount4
                        || c + 3 == waveCount4 || c + 4 == waveCount4 || c + 5 == waveCount4) {
                    lblArr.get(c).setVisible(true); //2,1,0
                } else {
                    lblArr.get(c).setVisible(false);
                }

            }

            cntPar.revalidate();

            waveCount4++;

            if (waveCount4 == 5) {
                waveCount4 = 0;
            }

        }).schedule(duration, true, formParAnim);

        return formParAnim;
    }
}
