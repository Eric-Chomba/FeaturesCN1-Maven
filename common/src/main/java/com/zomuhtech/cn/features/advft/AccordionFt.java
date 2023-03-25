/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zomuhtech.cn.features.advft;

import com.codename1.ui.Display;
import com.codename1.ui.Form;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.components.Accordion;
import com.codename1.components.SpanLabel;
import com.codename1.ui.Button;
import com.codename1.ui.Container;
import com.codename1.ui.Label;
import com.codename1.ui.TextField;
import com.codename1.ui.layouts.BoxLayout;
import com.zomuhtech.cn.features.procs.Proc;
import java.util.ArrayList;

/**
 * GUI builder created Form
 *
 * @author Eric
 */
public class AccordionFt extends Form {

    Form form, prevForm;
    Proc proc;

    public AccordionFt(Form form) {
        // this(com.codename1.ui.util.Resources.getGlobalResources());
        this.prevForm = form;
        proc = new Proc();
        Display.getInstance().callSerially(() -> {
            createUI();
        });
    }

    private void createUI() {
        form = proc.getForm("Accordion", prevForm);
        form.setLayout(new BorderLayout());
        

        ArrayList<String> faqArr = new ArrayList<>();
        faqArr.add("CN1 Feature 1:Build mobile applications \nfor Android from "
                + "one codebase");
        faqArr.add("CN1 Feature 2:Build mobile applications \nfor iOS from "
                + "one codebase");
        faqArr.add("CN1 Feature 3:Build mobile applications \nfor Windows from "
                + "one codebase");

        Accordion accord = new Accordion();

        for (int j = 0; j < faqArr.size(); j++) {

            String[] arr = proc.splitValue(faqArr.get(j), ":");
             accord.addContent(new Label(arr[0], "lblAccordTitle"),
                    new SpanLabel(arr[1], "lblAccord"));
        }
       
        Container cnt = BoxLayout.encloseY(new Label("Email",
                proc.getLblInputUIID()),
                new TextField(), new Button("Subscribe", "btn"));
        cnt.setUIID("cntPar");
        accord.addContent(new Label("More", "lblAccordTitle"), cnt);

        form.add(CENTER, accord);
        form.show();
    }

    public AccordionFt(com.codename1.ui.util.Resources resourceObjectInstance) {
        // initGuiBuilderComponents(resourceObjectInstance);
    }

//////////////////////////////////-- DON'T EDIT BELOW THIS LINE!!!


// <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initGuiBuilderComponents(com.codename1.ui.util.Resources resourceObjectInstance) {
        setLayout(new com.codename1.ui.layouts.FlowLayout());
        setInlineStylesTheme(resourceObjectInstance);
                setInlineStylesTheme(resourceObjectInstance);
        setTitle("AccordionFt");
        setName("AccordionFt");
    }// </editor-fold>

//-- DON'T EDIT ABOVE THIS LINE!!!
}
