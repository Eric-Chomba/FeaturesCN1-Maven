/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zomuhtech.cn.features.advft;

import com.codename1.ui.ButtonGroup;
import com.codename1.ui.Container;
import com.codename1.ui.Display;
import com.codename1.ui.FontImage;
import com.codename1.ui.Form;
import com.codename1.ui.Label;
import com.codename1.ui.RadioButton;
import com.codename1.ui.TextArea;
import com.codename1.ui.TextField;
import com.codename1.ui.layouts.BorderLayout;
import static com.codename1.ui.layouts.BorderLayout.NORTH;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.GridLayout;
import com.codename1.ui.plaf.DefaultLookAndFeel;
import com.codename1.ui.plaf.Style;
import com.codename1.ui.plaf.UIManager;
import com.zomuhtech.cn.features.procs.Proc;

/**
 *
 * @author Eric
 */
public class LocalizationFt extends Form {

    Form form, prevForm;
    Proc proc;

    String[] arr = {"Change Language>English>Change Language>Swahili>Badili Lugha>Sheng>Chuja Lugha",
        "Name>English>Name>Swahili>Jina>Sheng>Naji",
        "Age>English>Age>Swahili>Miaka>Sheng>Kamia"
    };

    public LocalizationFt(Form form) {

        this.prevForm = form;
        proc = new Proc();
        Display.getInstance().callSerially(() -> {
            createUI();
        });

    }

    public void createUI() {
        form = proc.getForm("L10N & I18N", prevForm);
        form.setLayout(new BorderLayout());

        Container cnt = new Container(BoxLayout.y(), "cntPar");
        Label lblLang = new Label("Change Language", "lblInput");

        Container cntRad = new Container(new GridLayout(1, 3), "cntPar");
        
        Style s;
        if (Display.getInstance().isTablet()) {
            s = UIManager.getInstance().getComponentStyle("radTab"); //button
        } else {
            s = UIManager.getInstance().getComponentStyle("rad");
        }
        FontImage radEmptyImg = FontImage.createMaterial(
                FontImage.MATERIAL_RADIO_BUTTON_UNCHECKED, s);
        FontImage radFullImg = FontImage.createMaterial(
                FontImage.MATERIAL_RADIO_BUTTON_CHECKED, s);
        ((DefaultLookAndFeel) UIManager.getInstance().getLookAndFeel())
                .setRadioButtonImages(radFullImg, radEmptyImg, radFullImg,
                        radEmptyImg);
        RadioButton rad1 = new RadioButton("English");
        rad1.setUIID(proc.getRadioUIID());
        //rad1.setSelected(true);
        //changeLang("English");
        

        RadioButton rad2 = new RadioButton("Swahili");
        rad2.setUIID(proc.getRadioUIID());
        RadioButton rad3 = new RadioButton("Sheng");
        rad3.setUIID(proc.getRadioUIID());
        ButtonGroup bg = new ButtonGroup(rad1, rad2, rad3);
        cntRad.add(rad1).add(rad2).add(rad3);
        bg.addActionListener(e -> {
            RadioButton rad = bg.getRadioButton(bg.getSelectedIndex());
            changeLang(rad.getText());

        });

        cnt.add(lblLang).add(cntRad);

        form.add(NORTH, cnt);
        form.add(CENTER, getForm1());
        //form.revalidateWithAnimationSafety();
        form.show();

    }

    private void changeLang(String lang) {

        /*HashMap<String, String> resBundle = new HashMap<>();

        for (String tsl : arr) {
            //Name>English>Name>Swahili>Jina>Sheng>Naji
            String[] tslArr = proc.splitValue(tsl, ">");
            switch (lang) {
                case "English":
                    resBundle.put(tslArr[0], tslArr[2]);
                    break;
                case "Swahili":
                    resBundle.put(tslArr[0], tslArr[4]);
                    break;
                case "Sheng":
                    resBundle.put(tslArr[0], tslArr[6]);
                    break;
            }
        }*/
        UIManager.getInstance().setBundle(proc.changeLang(lang));

        proc.setCurrentLang(lang);

        form.add(CENTER, getForm1());
        form.revalidateWithAnimationSafety();
    }

    private Form getForm1() {

        Form form1 = proc.getInputForm();
        form1.setUIID("cntPar");

        Label lblName = new Label("Name", "lblInput");
        Container cntName = proc.getInputCnt();
        TextField tfName = proc.getInputTf("", TextArea.ANY);
        cntName.add(tfName);

        Label lblAge = new Label("Age", "lblInput");
        Container cntAge = proc.getInputCnt();
        TextField tfAge = proc.getInputTf("", TextArea.NUMERIC);
        cntAge.add(tfAge);

        form1.add(lblName).add(cntName).add(lblAge).add(cntAge);

        return form1;
    }
}
