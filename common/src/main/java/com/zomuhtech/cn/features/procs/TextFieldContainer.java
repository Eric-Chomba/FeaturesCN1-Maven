/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zomuhtech.cn.features.procs;

/**
 *
 * @author Eric
 */
import com.codename1.ui.Button;
import com.codename1.ui.Container;
import com.codename1.ui.Display;
import com.codename1.ui.Form;
import com.codename1.ui.Label;
import com.codename1.ui.TextField;
import com.codename1.ui.layouts.BorderLayout;
import static com.codename1.ui.layouts.BorderLayout.CENTER;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.FlowLayout;
import com.codename1.ui.layouts.LayeredLayout;
import com.codename1.ui.plaf.Style;
import com.codename1.ui.util.UITimer;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class TextFieldContainer {

    ArrayList<Label> lblArr = new ArrayList<>();
    ArrayList<String> hintArr = new ArrayList<>();
    ArrayList<Container> cntTfArr = new ArrayList<>();
    ArrayList<Label> activeHintArr = new ArrayList<>();
    ArrayList<Label> dormantHintArr = new ArrayList<>();
    ArrayList<TextField> tfArr = new ArrayList<>();
    ArrayList<String> hintsArr = new ArrayList<>();
    ArrayList<Container> cntTfBorderArr = new ArrayList<>();
    int selBorderColor;
    int hintCount = 0, btnCount = 0, lblCount = 0;
    boolean animateHint;
    Form form;
    int activeBtnIndex = 0;

    ArrayList<Label> lblBoxArr = new ArrayList<>();
    ArrayList<String> hintBoxArr = new ArrayList<>();
    ArrayList<Container> cntBoxTfArr = new ArrayList<>();
    ArrayList<Label> activeBoxHintArr = new ArrayList<>();
    ArrayList<Label> dormantBoxHintArr = new ArrayList<>();
    ArrayList<TextField> tfBoxArr = new ArrayList<>();
    int hintBoxCount = 0, btnBoxCount = 0, lblBoxCount = 0, activeBoxBtnIndex = 0;
    boolean animateBoxHint;

    /**
     * @param currentForm - current form with textFields
     * @param tf - TextField
     * @param textColor - TextField text color
     * @param tfBorderColor - TextField border color
     * @param tfBorderWidth - TextField border width
     * @param inputType - TextField input type - any, phone, password etc
     * @param hint - TextField hint
     * @param hintColor- hint color
     * @return - returns Container with TexField & hints with animation
     */
    public Container getTfContainer(Form currentForm, TextField tf, int textColor,
            int tfBorderColor, float tfBorderWidth, int inputType, String hint,
            int hintColor) {

        this.form = currentForm;
        this.selBorderColor = hintColor;

        tf.setName("" + btnCount);
        tf.setConstraint(inputType);
        tf.setUIID("tfCustom");
        tf.getAllStyles().setFgColor(textColor);
        tf.setHint(hint);
        tf.getHintLabel().setUIID("tfHint");
        tf.getHintLabel().getAllStyles().setFgColor(hintColor);
        tfArr.add(tf);

        hintsArr.add(hint);

        Container cntTfPar = new Container(BoxLayout.y());

        Container cntTf = new Container(new LayeredLayout());

        Button btnTf = new Button();
        btnTf.setUIID("btnTf");
        btnTf.setName("" + btnCount);

        btnCount++;

        btnTf.addActionListener(e -> {

            tf.startEditingAsync();
            //Log.p("Editing " + btnTf.getName());
            hintCount = 0;
            animateHint = true;
            activeBtnIndex = Integer.parseInt(btnTf.getName());

            //hide hint of selected tf
            for (int t = 0; t < tfArr.size(); t++) {
                if (t == activeBtnIndex) {
                    if (tfArr.get(activeBtnIndex).getText().length() == 0) {
                        tfArr.get(activeBtnIndex).setHint("");
                    }
                } else {
                    tfArr.get(t).setHint(hintsArr.get(t));
                }
                cntTfPar.revalidate();
            }

            //highlight border of selected tf
            for (int h = 0; h < cntTfBorderArr.size(); h++) {
                if (h == activeBtnIndex) {
                    cntTfBorderArr.get(activeBtnIndex).getAllStyles().setBgColor(hintColor);
                } else {
                    cntTfBorderArr.get(h).getAllStyles().setBgColor(tfBorderColor);
                }
                form.revalidate();
            }

            hideEmptyTfHint();
        });

        cntTf.setLeadComponent(btnTf);
        cntTf.addAll(btnTf, tf);
        btnTf.setVisible(false);

        Label lblHint1 = new Label(hint, "lblHint");
        lblHint1.getAllStyles().setFgColor(hintColor);
        lblHint1.setVisible(false);
        Label lblHint2 = new Label(hint, "tfHint");
        lblHint2.getAllStyles().setFgColor(hintColor);
        lblHint2.setVisible(false);

        lblHint2.setName("lbl" + lblCount); //0 2
        lblArr.add(lblHint2);
        lblCount++;
        lblHint1.setName("lbl" + lblCount); //1 3
        lblArr.add(lblHint1);
        lblCount++;

        Container cntHint = new Container(new LayeredLayout());
        cntHint.addAll(lblHint1, lblHint2);

        Container cntTfBorder = new Container(BoxLayout.y(), "cntTfBorder");
        cntTfBorder.getAllStyles().setBgColor(tfBorderColor);
        cntTfBorder.getAllStyles().setPadding(tfBorderWidth, tfBorderWidth, tfBorderWidth, tfBorderWidth);
        //cntTfBorder.getAllStyles().setPaddingUnit(Style.UNIT_TYPE_PIXELS);
        cntTfBorder.getAllStyles().setPaddingUnit(Style.UNIT_TYPE_DIPS);
        cntTfBorderArr.add(cntTfBorder);

        cntTfPar.addAll(/*lblHint1, lblHint2*/cntHint, cntTf, cntTfBorder);

        //hide/show hints on edit
        tf.addDataChangedListener((i1, i2) -> {

            int selTfIndex = Integer.parseInt(tf.getName());
            new Proc().printLine("editing " + selTfIndex + " textLen= " + tf.getText().length());

            if (tf.getText().length() == 0) {
                tf.setHint("");
                activeHintArr.get(1).setVisible(true);
            }

        });

//        //set focus to next tf when next keyboard arrow is pressed
//        if (Integer.parseInt(tf.getName()) + 1 < tfArr.size()) {
//            tf.setNextFocusDown(tfArr.get(Integer.parseInt(tf.getName()) + 1));
//        }
        cntTfArr.add(cntTfPar);

        return cntTfPar;
    }

    private void hideEmptyTfHint() {

        //group label hints into twos
        hintArr.clear();
        int grpHintCount = 1;

        for (int h = 0; h < lblArr.size(); h++) {

            if (grpHintCount == 2) {
                String hintStr = h - 1 + ":" + h;
                hintArr.add(hintStr);
                grpHintCount = 1;
            } else {
                grpHintCount++;
            }
        }

        for (int t = 0; t < tfArr.size(); t++) {

            if (t != activeBtnIndex) {

                String otherHints = hintArr.get(t);
                //Log.p("otherHints=" + otherHints); //0:1 , 2:3
                String[] otherHintArr = splitValue(otherHints, ":"); //[0, 1] [2, 3]

                dormantHintArr.clear();

                //create array of other tf hints - not currently being edited
                for (String otherHint : otherHintArr) {
                    //Log.p("otherHint=" + otherHint); //hint@v=0 hint@v=1   hint@v=2 hint@v=3
                    dormantHintArr.add(lblArr.get(Integer.parseInt(otherHint)));
                }

                //hide tf hints if tf text length = 0
                for (int h = 0; h < dormantHintArr.size(); h++) {
                    if (tfArr.get(t).getText().length() == 0) {
                        dormantHintArr.get(h).setVisible(false);
                    }
                }
            } else {
                //for currently edited tf
                animateTfHint();
            }
        }
    }

    private void animateTfHint() {

        //group label hints into twos
        hintArr.clear();
        int grpHintCount = 1;

        for (int h = 0; h < lblArr.size(); h++) {

            if (grpHintCount == 2) {
                String hintStr = h - 1 + ":" + h;
                hintArr.add(hintStr);
                grpHintCount = 1;
            } else {
                grpHintCount++;
            }
        }

        String currHints = hintArr.get(activeBtnIndex);
        //Log.p("currHints=" + currHints); //0:1 , 2:3
        String[] currHintArr = splitValue(currHints, ":"); //[0, 1] [2, 3]

        //create array of selected(active) tf hints
        activeHintArr.clear();

        for (String currHint : currHintArr) {
            //Log.p("hint@v=" + currHint); //hint@v=0 hint@v=1   hint@v=2 hint@v=3
            activeHintArr.add(lblArr.get(Integer.parseInt(currHint)));
        }

        new UITimer(() -> {

            if (animateHint) {
                animHints();

                //update hint count
                hintCount++;

                if (hintCount == activeHintArr.size()) {
                    hintCount = 0;
                    animateHint = false;
                }
            }
        }).schedule(100, false, form);

    }

    private void animHints() {

        //loop hints of selected tf
        for (int a = 0; a < activeHintArr.size(); a++) {

            //animate all current tf's hints if tf has no text
            if (tfArr.get(activeBtnIndex).getText().length() == 0) {
                if (a == hintCount) {
                    activeHintArr.get(hintCount).setVisible(true);
                } else {

                    for (int h = 0; h < activeHintArr.size(); h++) {

                        if (!(activeHintArr.get(h).getName())
                                .equals(activeHintArr.get(hintCount).getName())) {
                            activeHintArr.get(h).setVisible(false);
                        }
                    }
                }
            } else {
                //show 1 hint only(without animating) if tf has text
                activeHintArr.get(1).setVisible(true);
            }
        }
        Display.getInstance().callSerially(() -> {
            cntTfArr.get(activeBtnIndex).revalidateWithAnimationSafety();
            animateTfHint();
        });

    }

    /**
     * @param currentForm - current form with textFields
     * @param tf - TextField
     * @param tfBorderColor - TextField border colour
     * @param tfBorderWidth - border width
     * @param tfBgColor - TextField background colour
     * @param textColor - TextField text colour
     * @param inputType - TextField input type - any, phone, password etc
     * @param hint - TextField hint
     * @param hintColor- hint colour
     * @return - returns Container with TexField & hints with animation
     */
    public Container getTfBoxContainer(Form currentForm, TextField tf, int tfBorderColor,
            float tfBorderWidth,
            int tfBgColor, int textColor, int inputType, String hint, int hintColor) {

        this.form = currentForm;

        Container cntTfPar = new Container(new LayeredLayout(), "cntBoxTfPar");
        cntTfPar.getAllStyles().setBgColor(tfBgColor);

        Container cntTf = new Container(new LayeredLayout(), "cntBoxTf");
        cntTf.getAllStyles().setBgColor(tfBorderColor);

        Container cntBoxTfBg = new Container(new BorderLayout(), "cntBoxTfBg");
        cntBoxTfBg.getAllStyles().setBgColor(tfBgColor);
        cntBoxTfBg.getAllStyles().setMargin(tfBorderWidth, tfBorderWidth, tfBorderWidth, tfBorderWidth);
        cntBoxTfBg.getAllStyles().setMarginUnit(Style.UNIT_TYPE_DIPS);
        tf.setConstraint(inputType);
        tf.setUIID("tfBoxCustom");
        tf.getAllStyles().setBgColor(tfBgColor);
        tf.getAllStyles().setFgColor(textColor);
        tf.setHint(hint);
        tf.getHintLabel().setUIID("tfBoxHint");
        tf.getHintLabel().getAllStyles().setFgColor(hintColor);
        tfBoxArr.add(tf);
        cntBoxTfBg.add(CENTER, tf);

        Button btnTf = new Button();
        btnTf.setUIID("btnBoxTf");
        btnTf.setName("" + btnBoxCount);

        btnBoxCount++;

        btnTf.addActionListener(e -> {
            tf.startEditingAsync();
            //Log.p("Editing " + btnTf.getName());
            hintBoxCount = 0;
            animateBoxHint = true;
            activeBoxBtnIndex = Integer.parseInt(btnTf.getName());
            new Proc().printLine("activeBoxBtnIndex=" + activeBoxBtnIndex);
            hideEmptyBoxTfHint();
        });

        Label lblHint1 = new Label(hint, "lblBoxHint");
        lblHint1.getAllStyles().setBgColor(tfBgColor);
        lblHint1.getAllStyles().setFgColor(hintColor);
        lblHint1.setVisible(false);

        lblHint1.setName("lbl" + lblBoxCount); //1 3
        lblBoxArr.add(lblHint1);
        lblBoxCount++;

        cntTf.setLeadComponent(btnTf);
        cntTf.addAll(btnTf, cntBoxTfBg);

        cntTfPar.addAll(cntTf, FlowLayout.encloseIn(lblHint1));
        btnTf.setVisible(false);

        cntBoxTfArr.add(cntTf);

        return cntTfPar;
    }

    private void animateBoxTfHint() {

        //group label hints into twos
        hintBoxArr.clear();
        //int grpHintCount = 1;

        for (int h = 0; h < lblBoxArr.size(); h++) {

            /*if (grpHintCount == 2) {
                String hintStr = h - 1 + ":" + h;
                hintBoxArr.add(hintStr);
                grpHintCount = 1;
            } else {
                grpHintCount++;
            }*/
            String hintStr = "" + h;
            hintBoxArr.add(hintStr);
        }

        String currHints = hintBoxArr.get(activeBoxBtnIndex);
        //Log.p("currHints=" + currHints); //0:1 , 2:3
        String[] currHintArr = splitValue(currHints, ":"); //[0, 1] [2, 3]

        //create array of selected(active) tf hints
        activeBoxHintArr.clear();

        for (String currHint : currHintArr) {
            //Log.p("hint@v=" + currHint); //hint@v=0 hint@v=1   hint@v=2 hint@v=3
            activeBoxHintArr.add(lblBoxArr.get(Integer.parseInt(currHint)));
        }

        //Display.getInstance().callSerially(() -> {
        new UITimer(() -> {

            if (animateBoxHint) {

                //loop hints of selected tf
                for (int a = 0; a < activeBoxHintArr.size(); a++) {
                    if (a == hintBoxCount) {
                        activeBoxHintArr.get(hintBoxCount).setVisible(true);
                    } else {

                        for (int h = 0; h < activeBoxHintArr.size(); h++) {

                            if (!(activeBoxHintArr.get(h).getName())
                                    .equals(activeBoxHintArr.get(hintBoxCount).getName())) {
                                activeBoxHintArr.get(h).setVisible(false);
                            }
                        }
                    }
                }
                Display.getInstance().callSerially(() -> {
                    //form.revalidateWithAnimationSafety();
                    cntBoxTfArr.get(activeBoxBtnIndex).revalidateWithAnimationSafety();
                });

                //update hint count
                hintBoxCount++;

                if (hintBoxCount == activeBoxHintArr.size()) {
                    hintBoxCount = 0;
                    animateBoxHint = false;
                }
            }
        }).schedule(100, animateBoxHint, form);
        //});
    }

//    private void hideBoxHint() {
//
//        //group label hints into twos
//        hintBoxArr.clear();
//        int grpHintCount = 1;
//
//        for (int h = 0; h < lblArr.size(); h++) {
//
//            if (grpHintCount == 2) {
//                String hintStr = h - 1 + ":" + h;
//                hintArr.add(hintStr);
//                grpHintCount = 1;
//            } else {
//                grpHintCount++;
//            }
//        }
//
//        //Log.p("DataChanged activeBtnIndex=" + activeBtnIndex);
//        String currHints = hintArr.get(activeBtnIndex);
//        //Log.p("currHints=" + currHints); //0:1 , 2:3
//        String[] currHintArr = splitValue(currHints, ":"); //[0, 1] [2, 3]
//
//        activeHintArr.clear();
//
//        for (String currHint : currHintArr) {
//            //Log.p("hint@v=" + currHint); //hint@v=0 hint@v=1   hint@v=2 hint@v=3
//            activeHintArr.add(lblArr.get(Integer.parseInt(currHint)));
//        }
//
//        for (int h = 0; h < activeHintArr.size(); h++) {
//            activeHintArr.get(h).setVisible(false);
//        }
//    }
    private void hideEmptyBoxTfHint() {

        //group label hints into twos
        hintBoxArr.clear();
        //int grpHintCount = 1;

        for (int h = 0; h < lblBoxArr.size(); h++) {

            String hintStr = "" + h;
            hintBoxArr.add(hintStr);
        }

        for (int t = 0; t < tfBoxArr.size(); t++) {

            if (t != activeBoxBtnIndex) {

                String otherHints = hintBoxArr.get(t);
                //Log.p("otherHints=" + otherHints); //0:1 , 2:3
                String[] otherHintArr = splitValue(otherHints, ":"); //[0, 1] [2, 3]

                dormantBoxHintArr.clear();

                //create array of other tf hints - not currently being edited
                for (String otherHint : otherHintArr) {
                    //Log.p("otherHint=" + otherHint); //hint@v=0 hint@v=1   hint@v=2 hint@v=3
                    dormantBoxHintArr.add(lblBoxArr.get(Integer.parseInt(otherHint)));
                }

                //hide tf hints if tf text length = 0
                for (int h = 0; h < dormantBoxHintArr.size(); h++) {
                    if (tfBoxArr.get(t).getText().length() == 0) {
                        dormantBoxHintArr.get(h).setVisible(false);
                    }
                }
            } else {
                //for currently edited tf
                animateBoxTfHint();
            }
        }
    }

    public String[] splitValue(String value, String del) {
        ArrayList<String> splitArr = new ArrayList<>();
        StringTokenizer arr = new StringTokenizer(value, del);
        while (arr.hasMoreElements()) {
            splitArr.add(arr.nextToken());
        }
        return splitArr.toArray(new String[splitArr.size()]);
    }

//    ArrayList<Label> lblArr = new ArrayList<>();
//    ArrayList<String> hintArr = new ArrayList<>();
//    ArrayList<Container> cntTfArr = new ArrayList<>();
//    ArrayList<Label> activeHintArr = new ArrayList<>();
//    ArrayList<Label> dormantHintArr = new ArrayList<>();
//    ArrayList<TextField> tfArr = new ArrayList<>();
//    int hintCount = 0, btnCount = 0, lblCount = 0;
//    boolean animateHint;
//    Form form;
//    int activeBtnIndex = 0;
//
//    ArrayList<Label> lblBoxArr = new ArrayList<>();
//    ArrayList<String> hintBoxArr = new ArrayList<>();
//    ArrayList<Container> cntBoxTfArr = new ArrayList<>();
//    ArrayList<Label> activeBoxHintArr = new ArrayList<>();
//    ArrayList<Label> dormantBoxHintArr = new ArrayList<>();
//    ArrayList<TextField> tfBoxArr = new ArrayList<>();
//    int hintBoxCount = 0, btnBoxCount = 0, lblBoxCount = 0, activeBoxBtnIndex = 0;
//    boolean animateBoxHint;
//
//    /**
//     * @param currentForm - current form with textFields
//     * @param tf - TextField
//     * @param textColor - TextField text color
//     * @param tfBorderColor - TextField border color
//     * @param tfBorderWidth - TextField border width
//     * @param inputType - TextField input type - any, phone, password etc
//     * @param hint - TextField hint
//     * @param hintColor- hint color
//     * @return - returns Container with TexField & hints with animation
//     */
//    public Container getTfContainer(Form currentForm, TextField tf, int textColor,
//            int tfBorderColor, float tfBorderWidth, int inputType, String hint,
//            int hintColor) {
//
//        this.form = currentForm;
//
//        tf.setConstraint(inputType);
//        tf.setUIID("tfCustom");
//        tf.getAllStyles().setFgColor(textColor);
//        tf.setHint(hint);
//        tf.getHintLabel().setUIID("tfHint");
//        tf.getHintLabel().getAllStyles().setFgColor(hintColor);
//        tfArr.add(tf);
//
//        Container cntTfPar = new Container(BoxLayout.y());
//
//        Container cntTf = new Container(new LayeredLayout());
//
//        Button btnTf = new Button();
//        btnTf.setUIID("btnTf");
//        btnTf.setName("" + btnCount);
//
//        btnCount++;
//
//        btnTf.addActionListener(e -> {
//            tf.startEditingAsync();
//            //Log.p("Editing " + btnTf.getName());
//            hintCount = 0;
//            animateHint = true;
//            activeBtnIndex = Integer.parseInt(btnTf.getName());
//            hideEmptyTfHint();
//        });
//
//        cntTf.setLeadComponent(btnTf);
//        cntTf.addAll(btnTf, tf);
//        btnTf.setVisible(false);
//
//        Label lblHint1 = new Label(hint, "lblHint");
//        lblHint1.getAllStyles().setFgColor(hintColor);
//        lblHint1.setVisible(false);
//        Label lblHint2 = new Label(hint, "tfHint");
//        lblHint2.getAllStyles().setFgColor(hintColor);
//        lblHint2.setVisible(false);
//
//        lblHint2.setName("lbl" + lblCount); //0 2
//        lblArr.add(lblHint2);
//        lblCount++;
//        lblHint1.setName("lbl" + lblCount); //1 3
//        lblArr.add(lblHint1);
//        lblCount++;
//
//        Container cntTfBorder = new Container(BoxLayout.y(), "cntTfBorder");
//        cntTfBorder.getAllStyles().setBgColor(tfBorderColor);
//        cntTfBorder.getAllStyles().setPadding(tfBorderWidth, tfBorderWidth, tfBorderWidth, tfBorderWidth);
//        //cntTfBorder.getAllStyles().setPaddingUnit(Style.UNIT_TYPE_PIXELS);
//        cntTfBorder.getAllStyles().setPaddingUnit(Style.UNIT_TYPE_DIPS);
//
//        cntTfPar.addAll(lblHint1, lblHint2, cntTf, cntTfBorder);
//
//        /* tf.addDataChangedListener((i1, i2) -> {
//
//            if (tf.getText().length() == 0) {
//                hideHint();
//            }
//        });*/
//        cntTfArr.add(cntTfPar);
//
//        return cntTfPar;
//    }
//
//    private void animateTfHint() {
//
//        //group label hints into twos
//        hintArr.clear();
//        int grpHintCount = 1;
//
//        for (int h = 0; h < lblArr.size(); h++) {
//
//            if (grpHintCount == 2) {
//                String hintStr = h - 1 + ":" + h;
//                hintArr.add(hintStr);
//                grpHintCount = 1;
//            } else {
//                grpHintCount++;
//            }
//        }
//
//        String currHints = hintArr.get(activeBtnIndex);
//        //Log.p("currHints=" + currHints); //0:1 , 2:3
//        String[] currHintArr = splitValue(currHints, ":"); //[0, 1] [2, 3]
//
//        //create array of selected(active) tf hints
//        activeHintArr.clear();
//
//        for (String currHint : currHintArr) {
//            //Log.p("hint@v=" + currHint); //hint@v=0 hint@v=1   hint@v=2 hint@v=3
//            activeHintArr.add(lblArr.get(Integer.parseInt(currHint)));
//        }
//
//        new UITimer(() -> {
//
//            if (animateHint) {
//                animHints();
//                
//                //update hint count
//                hintCount++;
//
//                if (hintCount == activeHintArr.size()) {
//                    hintCount = 0;
//                    animateHint = false;
//                }
//            }
//        }).schedule(100, false, form);
//
//    }
//
//    private void animHints() {
//
//        //loop hints of selected tf
//        for (int a = 0; a < activeHintArr.size(); a++) {
//
//            //animate all current tf's hints if tf has no text
//            if (tfArr.get(activeBtnIndex).getText().length() == 0) {
//                if (a == hintCount) {
//                    activeHintArr.get(hintCount).setVisible(true);
//                } else {
//
//                    for (int h = 0; h < activeHintArr.size(); h++) {
//
//                        if (!(activeHintArr.get(h).getName())
//                                .equals(activeHintArr.get(hintCount).getName())) {
//                            activeHintArr.get(h).setVisible(false);
//                        }
//                    }
//                }
//            } else {
//                //show 1 hint only(without animating) if tf has text
//                activeHintArr.get(1).setVisible(true);
//            }
//        }
//        Display.getInstance().callSerially(() -> {
//            cntTfArr.get(activeBtnIndex).revalidateWithAnimationSafety();
//            animateTfHint();
//        });
//
//    }
//
//    /*private void hideHint() {
//
//        //group label hints into twos
//        hintArr.clear();
//        int grpHintCount = 1;
//
//        for (int h = 0; h < lblArr.size(); h++) {
//
//            if (grpHintCount == 2) {
//                String hintStr = h - 1 + ":" + h;
//                hintArr.add(hintStr);
//                grpHintCount = 1;
//            } else {
//                grpHintCount++;
//            }
//        }
//
//        //Log.p("DataChanged activeBtnIndex=" + activeBtnIndex);
//        String currHints = hintArr.get(activeBtnIndex);
//        //Log.p("currHints=" + currHints); //0:1 , 2:3
//        String[] currHintArr = splitValue(currHints, ":"); //[0, 1] [2, 3]
//
//        activeHintArr.clear();
//
//        for (String currHint : currHintArr) {
//            //Log.p("hint@v=" + currHint); //hint@v=0 hint@v=1   hint@v=2 hint@v=3
//            activeHintArr.add(lblArr.get(Integer.parseInt(currHint)));
//        }
//
//        for (int h = 0; h < activeHintArr.size(); h++) {
//            activeHintArr.get(h).setVisible(false);
//        }
//    }*/
//    private void hideEmptyTfHint() {
//
//        //group label hints into twos
//        hintArr.clear();
//        int grpHintCount = 1;
//
//        for (int h = 0; h < lblArr.size(); h++) {
//
//            if (grpHintCount == 2) {
//                String hintStr = h - 1 + ":" + h;
//                hintArr.add(hintStr);
//                grpHintCount = 1;
//            } else {
//                grpHintCount++;
//            }
//        }
//
//        for (int t = 0; t < tfArr.size(); t++) {
//
//            if (t != activeBtnIndex) {
//
//                String otherHints = hintArr.get(t);
//                //Log.p("otherHints=" + otherHints); //0:1 , 2:3
//                String[] otherHintArr = splitValue(otherHints, ":"); //[0, 1] [2, 3]
//
//                dormantHintArr.clear();
//
//                //create array of other tf hints - not currently being edited
//                for (String otherHint : otherHintArr) {
//                    //Log.p("otherHint=" + otherHint); //hint@v=0 hint@v=1   hint@v=2 hint@v=3
//                    dormantHintArr.add(lblArr.get(Integer.parseInt(otherHint)));
//                }
//
//                //hide tf hints if tf text length = 0
//                for (int h = 0; h < dormantHintArr.size(); h++) {
//                    if (tfArr.get(t).getText().length() == 0) {
//                        dormantHintArr.get(h).setVisible(false);
//                    }
//                }
//            } else {
//                //for currently edited tf
//                animateTfHint();
//            }
//        }
//    }
//
//    /**
//     * @param currentForm - current form with textFields
//     * @param tf - TextField
//     * @param tfBorderColor - TextField border color
//     * @param tfBorderWidth - border width
//     * @param tfBgColor - TextField background color
//     * @param textColor - TextField text color
//     * @param inputType - TextField input type - any, phone, password etc
//     * @param hint - TextField hint
//     * @param hintColor- hint color
//     * @return - returns Container with TexField & hints with animation
//     */
//    public Container getTfBoxContainer(Form currentForm, TextField tf, int tfBorderColor,
//            float tfBorderWidth,
//            int tfBgColor, int textColor, int inputType, String hint, int hintColor) {
//
//        this.form = currentForm;
//
//        Container cntTfPar = new Container(new LayeredLayout(), "cntBoxTfPar");
//        cntTfPar.getAllStyles().setBgColor(tfBgColor);
//
//        Container cntTf = new Container(new LayeredLayout(), "cntBoxTf");
//        cntTf.getAllStyles().setBgColor(tfBorderColor);
//
//        Container cntBoxTfBg = new Container(new BorderLayout(), "cntBoxTfBg");
//        cntBoxTfBg.getAllStyles().setBgColor(tfBgColor);
//        cntBoxTfBg.getAllStyles().setMargin(tfBorderWidth, tfBorderWidth, tfBorderWidth, tfBorderWidth);
//        cntBoxTfBg.getAllStyles().setMarginUnit(Style.UNIT_TYPE_DIPS);
//        tf.setConstraint(inputType);
//        tf.setUIID("tfBoxCustom");
//        tf.getAllStyles().setBgColor(tfBgColor);
//        tf.getAllStyles().setFgColor(textColor);
//        tf.setHint(hint);
//        tf.getHintLabel().setUIID("tfBoxHint");
//        tf.getHintLabel().getAllStyles().setFgColor(hintColor);
//        tfBoxArr.add(tf);
//        cntBoxTfBg.add(CENTER, tf);
//
//        Button btnTf = new Button();
//        btnTf.setUIID("btnBoxTf");
//        btnTf.setName("" + btnBoxCount);
//
//        btnBoxCount++;
//
//        btnTf.addActionListener(e -> {
//            tf.startEditingAsync();
//            //Log.p("Editing " + btnTf.getName());
//            hintBoxCount = 0;
//            animateBoxHint = true;
//            activeBoxBtnIndex = Integer.parseInt(btnTf.getName());
//            //Log.p("activeBoxBtnIndex=" + activeBoxBtnIndex);
//            hideEmptyBoxTfHint();
//        });
//
//        Label lblHint1 = new Label(hint, "lblBoxHint");
//        lblHint1.getAllStyles().setBgColor(tfBgColor);
//        lblHint1.getAllStyles().setFgColor(hintColor);
//        lblHint1.setVisible(false);
//
//        lblHint1.setName("lbl" + lblBoxCount); //1 3
//        lblBoxArr.add(lblHint1);
//        lblBoxCount++;
//
//        cntTf.setLeadComponent(btnTf);
//        cntTf.addAll(btnTf, cntBoxTfBg);
//
//        cntTfPar.addAll(cntTf, FlowLayout.encloseIn(lblHint1));
//        btnTf.setVisible(false);
//
//        cntBoxTfArr.add(cntTf);
//
//        return cntTfPar;
//    }
//
//    private void animateBoxTfHint() {
//
//        //group label hints into twos
//        hintBoxArr.clear();
//        //int grpHintCount = 1;
//
//        for (int h = 0; h < lblBoxArr.size(); h++) {
//
//            /*if (grpHintCount == 2) {
//                String hintStr = h - 1 + ":" + h;
//                hintBoxArr.add(hintStr);
//                grpHintCount = 1;
//            } else {
//                grpHintCount++;
//            }*/
//            String hintStr = "" + h;
//            hintBoxArr.add(hintStr);
//        }
//
//        String currHints = hintBoxArr.get(activeBoxBtnIndex);
//        //Log.p("currHints=" + currHints); //0:1 , 2:3
//        String[] currHintArr = splitValue(currHints, ":"); //[0, 1] [2, 3]
//
//        //create array of selected(active) tf hints
//        activeBoxHintArr.clear();
//
//        for (String currHint : currHintArr) {
//            //Log.p("hint@v=" + currHint); //hint@v=0 hint@v=1   hint@v=2 hint@v=3
//            activeBoxHintArr.add(lblBoxArr.get(Integer.parseInt(currHint)));
//        }
//
//        //Display.getInstance().callSerially(() -> {
//        new UITimer(() -> {
//
//            if (animateBoxHint) {
//
//                //loop hints of selected tf
//                for (int a = 0; a < activeBoxHintArr.size(); a++) {
//                    if (a == hintBoxCount) {
//                        activeBoxHintArr.get(hintBoxCount).setVisible(true);
//                    } else {
//
//                        for (int h = 0; h < activeBoxHintArr.size(); h++) {
//
//                            if (!(activeBoxHintArr.get(h).getName())
//                                    .equals(activeBoxHintArr.get(hintBoxCount).getName())) {
//                                activeBoxHintArr.get(h).setVisible(false);
//                            }
//                        }
//                    }
//                }
//                Display.getInstance().callSerially(() -> {
//                    //form.revalidateWithAnimationSafety();
//                    cntBoxTfArr.get(activeBoxBtnIndex).revalidateWithAnimationSafety();
//                });
//
//                //update hint count
//                hintBoxCount++;
//
//                if (hintBoxCount == activeBoxHintArr.size()) {
//                    hintBoxCount = 0;
//                    animateBoxHint = false;
//                }
//            }
//        }).schedule(100, animateBoxHint, form);
//        //});
//    }
//
////    private void hideBoxHint() {
////
////        //group label hints into twos
////        hintBoxArr.clear();
////        int grpHintCount = 1;
////
////        for (int h = 0; h < lblArr.size(); h++) {
////
////            if (grpHintCount == 2) {
////                String hintStr = h - 1 + ":" + h;
////                hintArr.add(hintStr);
////                grpHintCount = 1;
////            } else {
////                grpHintCount++;
////            }
////        }
////
////        //Log.p("DataChanged activeBtnIndex=" + activeBtnIndex);
////        String currHints = hintArr.get(activeBtnIndex);
////        //Log.p("currHints=" + currHints); //0:1 , 2:3
////        String[] currHintArr = splitValue(currHints, ":"); //[0, 1] [2, 3]
////
////        activeHintArr.clear();
////
////        for (String currHint : currHintArr) {
////            //Log.p("hint@v=" + currHint); //hint@v=0 hint@v=1   hint@v=2 hint@v=3
////            activeHintArr.add(lblArr.get(Integer.parseInt(currHint)));
////        }
////
////        for (int h = 0; h < activeHintArr.size(); h++) {
////            activeHintArr.get(h).setVisible(false);
////        }
////    }
//    private void hideEmptyBoxTfHint() {
//
//        //group label hints into twos
//        hintBoxArr.clear();
//        //int grpHintCount = 1;
//
//        for (int h = 0; h < lblBoxArr.size(); h++) {
//
//            String hintStr = "" + h;
//            hintBoxArr.add(hintStr);
//        }
//
//        for (int t = 0; t < tfBoxArr.size(); t++) {
//
//            if (t != activeBoxBtnIndex) {
//
//                String otherHints = hintBoxArr.get(t);
//                //Log.p("otherHints=" + otherHints); //0:1 , 2:3
//                String[] otherHintArr = splitValue(otherHints, ":"); //[0, 1] [2, 3]
//
//                dormantBoxHintArr.clear();
//
//                //create array of other tf hints - not currently being edited
//                for (String otherHint : otherHintArr) {
//                    //Log.p("otherHint=" + otherHint); //hint@v=0 hint@v=1   hint@v=2 hint@v=3
//                    dormantBoxHintArr.add(lblBoxArr.get(Integer.parseInt(otherHint)));
//                }
//
//                //hide tf hints if tf text length = 0
//                for (int h = 0; h < dormantBoxHintArr.size(); h++) {
//                    if (tfBoxArr.get(t).getText().length() == 0) {
//                        dormantBoxHintArr.get(h).setVisible(false);
//                    }
//                }
//            } else {
//                //for currently edited tf
//                animateBoxTfHint();
//            }
//        }
//    }
//
//    public String[] splitValue(String value, String del) {
//        ArrayList<String> splitArr = new ArrayList<>();
//        StringTokenizer arr = new StringTokenizer(value, del);
//        while (arr.hasMoreElements()) {
//            splitArr.add(arr.nextToken());
//        }
//        return splitArr.toArray(new String[splitArr.size()]);
//    }
}
