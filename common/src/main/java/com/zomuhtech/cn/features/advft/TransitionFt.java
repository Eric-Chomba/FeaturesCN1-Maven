/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zomuhtech.cn.features.advft;

import com.codename1.components.SpanLabel;
import com.codename1.ui.Button;
import com.codename1.ui.CheckBox;
import com.codename1.ui.Container;
import com.codename1.ui.Dialog;
import com.codename1.ui.Display;
import com.codename1.ui.Form;
import com.codename1.ui.Label;
import com.codename1.ui.TextArea;
import com.codename1.ui.TextField;
import com.codename1.ui.animations.BubbleTransition;
import com.codename1.ui.animations.CommonTransitions;
import com.codename1.ui.animations.FlipTransition;
import com.codename1.ui.layouts.BorderLayout;
import static com.codename1.ui.layouts.BorderLayout.SOUTH;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.plaf.Border;
import com.codename1.ui.plaf.Style;
import com.codename1.ui.spinner.Picker;
import com.zomuhtech.cn.features.procs.Proc;

/**
 *
 * @author Eric
 */
public class TransitionFt extends Form {

    Form form, prevForm;
    Proc proc;

    public TransitionFt(Form form) {
        this.prevForm = form;
        proc = new Proc();
        Display.getInstance().callSerially(() -> {
            createUI();
        });
    }

    private void createUI() {

        form = proc.getForm("Transition", prevForm);
        form.setLayout(new BorderLayout());

        Style style = form.getContentPane().getUnselectedStyle();
        style.setBgTransparency(255);
        style.setBgColor(0Xff0000);

        Container cnt = new Container(BoxLayout.y());

        Button btnReplace = new Button("Replace Pending");
        cnt.add(btnReplace);

        Picker p = new Picker();
        p.setStrings("Select", "Replace", "Slide", "SlideFade", "Cover",
                "Uncover", "Fade", "Flip", "Bubble");
        p.setSelectedString("Select");
        cnt.add(p);

        TextField tfDuration = new TextField("10000", "Duration", 6,
                TextArea.NUMERIC);
        cnt.add(tfDuration);
        CheckBox chk = CheckBox.createToggle("Horizontal");
        cnt.add(chk);

        Button btn = new Button("Show", "btn");
        cnt.add(btn);
        form.add(SOUTH, cnt);

        p.addActionListener(e -> {
            String s = p.getSelectedString().toLowerCase();
            chk.setEnabled(s.equals("slide") || s.contains("cover"));
        });
        chk.setSelected(true);

        Form dest = new Form("Destination");
        style = dest.getContentPane().getUnselectedStyle();
        style.setBgTransparency(255);
        style.setBgColor(0xff);
        dest.setBackCommand(dest.getToolbar().addCommandToLeftBar("Back", null,
                e -> {
                    form.showBack();
                }));

        btn.addActionListener(e -> {
            int h = CommonTransitions.SLIDE_HORIZONTAL;
            if (chk.isSelected()) {
                h = CommonTransitions.SLIDE_VERTICAL;
            }

            switch (p.getSelectedString()) {
                case "Replace":
                    Label lbl = new Label("Destination Replace");
                    btnReplace.getParent().replaceAndWait(btnReplace, lbl,
                            CommonTransitions.createCover(
                                    CommonTransitions.SLIDE_VERTICAL,
                                    true, 1000));
                    lbl.getParent().replaceAndWait(lbl, btnReplace,
                            CommonTransitions.createUncover(
                                    CommonTransitions.SLIDE_VERTICAL,
                                    true, 1000));
                    break;

                case "Slide":
                    form.setTransitionOutAnimator(CommonTransitions
                            .createSlide(h, true, tfDuration.getAsInt(3000)));
                    dest.setTransitionOutAnimator(CommonTransitions
                            .createSlide(h, true, tfDuration.getAsInt(3000)));
                    dest.show();
                    break;

                case "SlideFade":
                    form.setTransitionOutAnimator(CommonTransitions
                            .createSlideFadeTitle(true,
                                    tfDuration.getAsInt(3000)));
                    dest.setTransitionOutAnimator(CommonTransitions
                            .createSlideFadeTitle(true,
                                    tfDuration.getAsInt(3000)));
                    dest.show();
                    break;

                case "Cover":
                    form.setTransitionOutAnimator(CommonTransitions
                            .createCover(h, true, tfDuration.getAsInt(3000)));
                    dest.setTransitionOutAnimator(CommonTransitions
                            .createCover(h, true, tfDuration.getAsInt(3000)));
                    dest.show();
                    break;

                case "Uncover":
                    form.setTransitionOutAnimator(CommonTransitions
                            .createUncover(h, true, tfDuration.getAsInt(3000)));
                    dest.setTransitionOutAnimator(CommonTransitions
                            .createUncover(h, true, tfDuration.getAsInt(3000)));
                    dest.show();
                    break;

                case "Fade":
                    form.setTransitionOutAnimator(CommonTransitions
                            .createFade(tfDuration.getAsInt(3000)));
                    dest.setTransitionOutAnimator(CommonTransitions
                            .createFade(tfDuration.getAsInt(3000)));
                    dest.show();
                    break;

                case "Flip":
                    form.setTransitionOutAnimator(new FlipTransition(-1,
                            tfDuration.getAsInt(3000)));
                    dest.setTransitionOutAnimator(new FlipTransition(-1,
                            tfDuration.getAsInt(3000)));
                    dest.show();
                    break;

                case "Bubble":
                    bubble();
                    break;
            }

        });

        form.show();
    }

    private void bubble() {
        Dialog d = new Dialog("Bubbled");
        d.setLayout(new BorderLayout());
        SpanLabel lbl = new SpanLabel("Dialog appearing with bubble transition");
        lbl.getUnselectedStyle().setFgColor(0xffffff);
        d.add(CENTER, lbl);

        d.setTransitionInAnimator(new BubbleTransition(500, "BubbleButton"));
        d.setTransitionOutAnimator(new BubbleTransition(500, "BubbleButton"));
        d.setDisposeWhenPointerOutOfBounds(true);
        d.getTitleStyle().setFgColor(0xffffff);

        Style dStyle = d.getDialogStyle();
        dStyle.setBorder(Border.createEmpty());
        dStyle.setBgColor(0xff);
        dStyle.setBgTransparency(0xff);
        d.showPacked(BorderLayout.CENTER, true);

    }
}
