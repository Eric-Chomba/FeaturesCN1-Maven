/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zomuhtech.cn.features.advft.route;

import com.codename1.system.NativeLookup;
import com.codename1.ui.Display;
import com.codename1.ui.Form;
import com.codename1.ui.layouts.BorderLayout;
import com.zomuhtech.cn.features.procs.Proc;

/**
 *
 * @author EChomba
 */
public class RouteOnMapFt extends com.codename1.ui.Form {

    Form form, prevForm;
    Proc proc;

    public RouteOnMapFt(Form form) {

        this.prevForm = form;
        proc = new Proc();
        Display.getInstance().callSerially(() -> {
            createUI();
        });
    }

    private void createUI() {
        form = proc.getForm("Route on Map", prevForm);
        form.setLayout(new BorderLayout());

        //initialize Android Native interface
        /*AndNativeRoute and = NativeLookup.create(AndNativeRoute.class);
        
        Command cmdRoute = Command.create("",
                proc.materialIcon(FontImage.MATERIAL_MAP),
                evt -> {
                     and.showRoute("-1.2832207,36.8198298,");
                });

        form.getToolbar().addCommandToRightBar(cmdRoute);

        //launch MainActivity of ztroute lib
        Button btnAnd = new Button("Android", "btn");
        btnAnd.addActionListener(e -> {
            and.showRoute("Nairobi CBD:-1.2832207:36.8198298,");
        });
        form.add(CENTER, FlowLayout.encloseCenterMiddle(btnAnd));

        form.show();*/
        AndNativeRoute and = NativeLookup.create(AndNativeRoute.class);
        and.showRoute("Nairobi CBD:-1.2832207:36.8198298,");

    }

}
