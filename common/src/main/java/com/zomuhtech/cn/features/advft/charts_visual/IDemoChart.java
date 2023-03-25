/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zomuhtech.cn.features.advft.charts_visual;

import com.codename1.ui.Form;

/**
 *
 * @author Eric
 */
public interface IDemoChart {

    //name field constant in a list activity
    String NAME = "name";
    //description constant in a list activity
    String DESC = "desc";
    
    //return chart name
    String getName();
    
    //return chart description
    String getDesc();
    
    /**
     * Executes the chart demo
     * @param context the context
     * @return the built intent
     */
    Form execute();
}
