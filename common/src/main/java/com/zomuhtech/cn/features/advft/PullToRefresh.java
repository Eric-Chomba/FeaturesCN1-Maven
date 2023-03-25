/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zomuhtech.cn.features.advft;

import com.codename1.components.ToastBar;
import com.codename1.l10n.L10NManager;
import com.codename1.ui.Display;
import com.codename1.ui.FontImage;
import com.codename1.ui.Form;
import com.codename1.ui.Label;
import com.codename1.ui.layouts.BoxLayout;
import com.zomuhtech.cn.features.procs.Proc;
import java.util.Date;

/**
 * GUI builder created Form
 *
 * @author Eric
 */
public class PullToRefresh extends Form {

    Form form, prevForm;
    Proc proc;

    public PullToRefresh(Form form) {
        //this(com.codename1.ui.util.Resources.getGlobalResources());
        this.prevForm = form;
        proc = new Proc();

        Display.getInstance().callSerially(() -> {
            createUI();
        });
    }

    private void createUI() {
        form = proc.getForm("Pull To Refresh", prevForm);
        form.setLayout(BoxLayout.y());

        form.getContentPane().addPullToRefresh(() -> {
            Label lbl = new Label("Pulled at " + L10NManager.getInstance()
                    .formatDateTimeShort(new Date()), "menuLbl");
            form.add(lbl);
            proc.showToast("Re-Sending", FontImage.MATERIAL_INFO_OUTLINE).show();
        });

        form.show();
    }

    public PullToRefresh(com.codename1.ui.util.Resources resourceObjectInstance) {
        // initGuiBuilderComponents(resourceObjectInstance);
    }

////////////////-- DON'T EDIT BELOW THIS LINE!!!


// <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initGuiBuilderComponents(com.codename1.ui.util.Resources resourceObjectInstance) {
        setLayout(new com.codename1.ui.layouts.FlowLayout());
        setInlineStylesTheme(resourceObjectInstance);
                setInlineStylesTheme(resourceObjectInstance);
        setTitle("PullToRefresh");
        setName("PullToRefresh");
    }// </editor-fold>

//-- DON'T EDIT ABOVE THIS LINE!!!
}
