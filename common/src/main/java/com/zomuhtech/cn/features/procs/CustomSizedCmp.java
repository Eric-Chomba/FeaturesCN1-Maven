/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zomuhtech.cn.features.procs;

import com.codename1.ui.Container;
import com.codename1.ui.geom.Dimension;

/**
 *
 * @author Eric
 */
public class CustomSizedCmp extends Container {

    int width, height;

    public CustomSizedCmp(int w, int h) {
        this.width = w;
        this.height = h;
    }

    @Override
    public Dimension calcPreferredSize() {
        return new Dimension(width, height);
    }
}
