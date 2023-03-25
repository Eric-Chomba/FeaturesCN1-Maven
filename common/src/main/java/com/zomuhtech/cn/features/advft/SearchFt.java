/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zomuhtech.cn.features.advft;

import com.codename1.components.ToastBar;
import com.codename1.ui.Button;
import com.codename1.ui.Component;
import com.codename1.ui.Display;
import com.codename1.ui.FontImage;
import com.codename1.ui.Form;
import com.codename1.ui.TextArea;
import com.codename1.ui.TextField;
import com.codename1.ui.layouts.BoxLayout;
import com.zomuhtech.cn.features.procs.Proc;
import java.util.ArrayList;

/**
 *
 * @author EChomba
 */
public class SearchFt extends Form {

    Form form, prevForm;
    Proc proc;

    public SearchFt(Form form) {
        this.prevForm = form;
        proc = new Proc();
        Display.getInstance().callSerially(() -> {
            createUI();
        });
    }

    private void createUI() {
        form = proc.getForm("Toolbar Search", prevForm);
        form.setLayout(BoxLayout.y());

        TextField tfSearch = new TextField("", "Search");
        tfSearch.getHintLabel().setUIID("lblSearch");
        tfSearch.setUIID("lblSearch");
        tfSearch.getAllStyles().setAlignment(Component.LEFT);
        form.getToolbar().setTitleComponent(tfSearch);

        tfSearch.addDataChangedListener((i1, i2) -> {

            String search = tfSearch.getText();

            if (search.length() < 1) {
                for (Component cmp : form.getContentPane()) {
                    cmp.setHidden(false);
                    cmp.setVisible(true);
                }
            } else {
                search = search.toLowerCase();

                for (Component cmp : form.getContentPane()) {

                    String value;
                    /*if (cmp instanceof Label) {
                        value = ((Label) cmp).getText();
                    } */
                    if (cmp instanceof Button) {
                        value = ((Button) cmp).getText();
                        ((Button) cmp).addActionListener(e -> {
                            //ToastBar.showInfoMessage(value);
                            /*ToastBar.showMessage("Delete, Click here to delete",
                                    FontImage.MATERIAL_DELETE, ev -> {
                                        ToastBar.showInfoMessage("deleting "
                                                + value);
                                    });*/
                            ToastBar.showMessage(value,
                                    FontImage.MATERIAL_DELETE, ev -> {
                                        ToastBar.showInfoMessage("deleting "
                                                + value);
                                    });

                            tfSearch.setText(value);
                            form.revalidate();
                        });

                    } else {
                        if (cmp instanceof TextArea) {
                            value = ((TextArea) cmp).getText();
                        } else {
                            value = (String) cmp.getPropertyValue("text");
                        }
                    }

                    boolean show = value != null && value.toLowerCase()
                            .indexOf(search) > -1;
                    cmp.setHidden(!show);
                    cmp.setVisible(show);
                }
            }
            form.getContentPane().animateLayout(250);
        });
        form.getToolbar().addCommandToRightBar("",
                proc.materialIcon(FontImage.MATERIAL_SEARCH), (e) -> {
            tfSearch.startEditingAsync();
        });

        ArrayList<String> arr = new ArrayList<>();
        arr.add("A Game of Thrones");
        arr.add("A Clash of Kings");
        arr.add("Feast for Crows");
        arr.add("A Dance with Dragons");
        arr.add("The Winds and Winters");
        arr.add("A Dream of Spring");

        for (int k = 0; k < arr.size(); k++) {
            //form.add(new Label(arr.get(k)));
            form.add(new Button(arr.get(k), "menuLbl"));
        }

        form.show();
    }
}
