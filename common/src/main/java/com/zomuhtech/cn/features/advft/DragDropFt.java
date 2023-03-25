/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zomuhtech.cn.features.advft;

import com.codename1.ui.Button;
import com.codename1.ui.Container;
import com.codename1.ui.Display;
import com.codename1.ui.Form;
import com.codename1.ui.Label;
import com.codename1.ui.layouts.BorderLayout;
import static com.codename1.ui.layouts.BorderLayout.NORTH;
import com.codename1.ui.layouts.BoxLayout;
import com.zomuhtech.cn.features.procs.Proc;
import java.util.Arrays;
import java.util.Collections;

/**
 *
 * @author Eric
 */
public class DragDropFt extends Form {

    Form form, prevForm;
    Proc proc;

    public DragDropFt(Form form) {

        this.prevForm = form;
        proc = new Proc();
        Display.getInstance().callSerially(() -> {
            createUI();
        });

    }

    public void createUI() {
        form = proc.getForm("Rearrangeable Items", prevForm);
        form.setLayout(new BorderLayout());

        String[] buttons = {"A Game of Thrones", "A Clash of Kings",
            "A Storm of Swords", "A Dance with Dragons", "The Wind of Winter",
            "A Dream of Spring"};

        Container cnt = new Container(BoxLayout.y());
        cnt.setScrollableY(true);
        cnt.setDropTarget(true);

        java.util.List<String> list = Arrays.asList(buttons);
        Collections.shuffle(list);

        for (String current : list) {
            //MultiButton btn = new MultiButton(current, "btn");
            Button btn = new Button(current, "btn");
            cnt.add(btn);
            btn.setDraggable(true);
        }
        form.add(NORTH, new Label("Drag Drop to Arrange the Titles","lblInput")).add(CENTER, cnt);

        form.show();
    }
}
