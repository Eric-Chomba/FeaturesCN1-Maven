/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zomuhtech.cn.features.advft;

import com.codename1.components.SpanButton;
import com.codename1.components.SpanLabel;
import com.codename1.ui.Display;
import com.codename1.ui.EncodedImage;
import com.codename1.ui.Form;
import com.codename1.ui.Image;
import com.codename1.ui.Label;
import com.codename1.ui.URLImage;
import com.codename1.ui.animations.ComponentAnimation;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.plaf.Style;
import com.zomuhtech.cn.features.procs.Proc;

/**
 *
 * @author EChomba
 */
public class CollapsingToolbar extends Form {

    Form form, prevForm;
    Proc proc;

    public CollapsingToolbar(Form form) {
        this.prevForm = form;
        proc = new Proc();
        Display.getInstance().callSerially(() -> {
            createUI();
        });
    }

    private void createUI() {
        form = proc.getForm("Collapsing Toolbar", prevForm);
        form.setLayout(BoxLayout.y());

        EncodedImage placeholder = EncodedImage.createFromImage(
                Image.createImage(form.getWidth(), form.getWidth() / 5,
                        0x3399ff), true); //0x3399ff 0x15E7FF

        URLImage bgImg = URLImage.createToStorage(placeholder, "BgImg.jpg",
                "https://awoiaf.westeros.org/images/thumb/9/93/"
                + "AGameOfThrones.jpg/300px-AGameOfThrones.jpg");
        bgImg.fetch();

        Style style = form.getToolbar().getTitleComponent().getUnselectedStyle();
        //Style style = form.getToolbar().getUnselectedStyle();
        style.setBgImage(bgImg);
        style.setBackgroundType(Style.BACKGROUND_IMAGE_SCALED_FILL);
        style.setPaddingUnit(Style.UNIT_TYPE_DIPS, Style.UNIT_TYPE_DIPS,
                Style.UNIT_TYPE_DIPS, Style.UNIT_TYPE_DIPS);
        style.setPaddingTop(15);
        style.setMargin(0, 0, 0, 0);

        SpanButton btn = new SpanButton("This excerpt is from A Wiki Of Ice And"
                + " Fire. Please check it out by clicking here!","collapsingToolbarTitle");
        btn.addActionListener(e -> {
            Display.getInstance()
                    .execute("https://awoiaf.westeros.org/index.php/A_Game_of_Thrones");
        });
        form.add(new SpanLabel("A Game of Thrones is the first of seven "
                + "planned novels in A Song of Ice and Fire, an epic fantasy"
                + " series by American author George R. R. Martin. It was "
                + "first published on 6 August 1996. The novel was nominated"
                + " for the 1998 Nebula Award and the 1997 World Fantasy "
                + "Award,[1] and won the 1997 Locus Award.[2] The novella "
                + "Blood of the Dragon, comprising the Daenerys Targaryen "
                + "chapters from the novel, won the 1997 Hugo Award for Best"
                + " Novella","lblInput"))
                .add(new Label("Plot introduction", "collapsingToolbarTitle"))
                .add(new SpanLabel("\"A Game of Thrones is set in the Seven"
                        + " Kingdoms of Westeros, a land reminiscent of "
                        + "Medieval Europe. In Westeros the seasons last for"
                        + " years, sometimes decades, at a time.\\n\\n\" + "
                        + "            \"Fifteen years prior to the novel, "
                        + "the Seven Kingdoms were torn apart by a civil "
                        + "war, known alternately as \\\"Robert's "
                        + "Rebellion\\\" and the \\\"War of the Usurper.\\\""
                        + " Prince Rhaegar Targaryen kidnapped Lyanna Stark,"
                        + " arousing the ire of her family and of her "
                        + "betrothed, Lord Robert Baratheon (the war's "
                        + "titular rebel). The Mad King, Aerys II Targaryen,"
                        + " had Lyanna's father and eldest brother executed "
                        + "when they demanded her safe return. ","lblInput"))
                .add(btn);

        ComponentAnimation cmpAnim = form.getToolbar().getTitleComponent()
                .createStyleAnimation("Title", 200);
        form.getAnimationManager().onTitleScrollAnimation(cmpAnim);

        form.show();

    }
}
