/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zomuhtech.cn.features.advft;

import com.codename1.ui.Component;
import com.codename1.ui.Container;
import com.codename1.ui.Display;
import com.codename1.ui.Form;
import com.codename1.ui.Label;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.Layout;
import com.codename1.ui.plaf.Style;
import com.zomuhtech.cn.features.procs.Proc;

/**
 *
 * @author Eric
 */
public class LayoutManagerFt extends Form {

    Form form, prevForm;
    Proc proc;

    public LayoutManagerFt(Form form) {

        this.prevForm = form;
        proc = new Proc();
        Display.getInstance().callSerially(() -> {
            createUI();
        });

    }

    public void createUI() {
        form = proc.getForm("Center Layout", prevForm);
        form.setLayout(new CenterLayout());

        for (int m = 1; m < 10; m++) {
            Label lbl = new Label("Label: " + m, "lblInput");
            lbl.getUnselectedStyle().setMarginLeft(m * 3);
            lbl.getUnselectedStyle().setMarginRight(0);
            form.add(lbl);
        }
        form.add(new Label("Wide Label text","lblInput"));

        form.show();
    }

    class CenterLayout extends Layout {

        @Override
        public void layoutContainer(Container parent) {
            int components = parent.getComponentCount();
            Style parStyle = parent.getStyle();
            int centerPos = parent.getLayoutWidth() / 2
                    + parStyle.getMargin(Component.LEFT);
            int y = parStyle.getMargin(Component.TOP);
            boolean rtl = parent.isRTL();

            for (int j = 0; j < components; j++) {
                Component current = parent.getComponentAt(j);
                Dimension d = current.getPreferredSize();
                Style currStyle = current.getStyle();
                int marginR = currStyle.getMarginRight(rtl);
                int marginL = currStyle.getMarginLeft(rtl);
                int marginT = currStyle.getMarginTop();
                int marginB = currStyle.getMarginBottom();
                current.setSize(d);

                int actualW = d.getWidth() + marginL + marginR;
                current.setX(centerPos - actualW / 2 + marginL);
                y += marginT;
                current.setY(y);
                y += d.getHeight() + marginB;
            }
        }

        @Override
        public Dimension getPreferredSize(Container parent) {
            int components = parent.getComponentCount();
            Style parStyle = parent.getStyle();
            int height = parStyle.getMargin(Component.TOP)
                    + parStyle.getMargin(Component.BOTTOM);
            int marginX = parStyle.getMargin(Component.RIGHT)
                    + parStyle.getMargin(Component.LEFT);
            int width = marginX;

            for (int k = 0; k < components; k++) {
                Component current = parent.getComponentAt(k);
                Dimension d = current.getPreferredSize();
                Style currStyle = current.getStyle();

                width = Math.max(d.getWidth() + marginX
                        + currStyle.getMargin(Component.RIGHT)
                        + currStyle.getMargin(Component.LEFT), width);
                height += currStyle.getMargin(Component.TOP) + d.getHeight()
                        + currStyle.getMargin(Component.BOTTOM);
            }
            Dimension size = new Dimension(width, height);
            return size;
        }
    }
}
