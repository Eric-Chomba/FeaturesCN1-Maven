/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zomuhtech.cn.features.advft.charts_visual;

import com.codename1.charts.ChartComponent;
import com.codename1.charts.models.CategorySeries;
import com.codename1.charts.renderers.DialRenderer;
import com.codename1.charts.renderers.DialRenderer.Type;
import com.codename1.charts.renderers.SimpleSeriesRenderer;
import com.codename1.charts.util.ColorUtil;
import com.codename1.charts.views.DialChart;
import com.codename1.ui.Component;
import com.codename1.ui.Form;
import com.codename1.ui.layouts.BoxLayout;

/**
 *
 * @author Eric
 */
public class WeightDialChart extends AbstractDemoChart {

    Form form;

    public String getDesc() {
        return "DialChart - weight indicator";
    }

    public WeightDialChart(Form prevForm) {

        form = new Form();
        form.getToolbar().hideToolbar();
        form.setLayout(BoxLayout.y());

        Component cmp = getDialChart(prevForm);
        cmp.getAllStyles().setBgColor(black);
        form.addComponent(cmp);
    }

    public Form getForm() {
        return form;
    }

    private Form getDialChart(Form prevForm) {
        
        //create category series & add (title, values)
        CategorySeries category = new CategorySeries("Weight indic");
        category.add("Current", 75);
        category.add("Minimum", 65);
        category.add("Maximum", 90);

        DialRenderer renderer = new DialRenderer();
        renderer.setChartTitleTextFont(largeFont);
        renderer.setLabelsTextFont(medFont);
        //set font using Font instead of pointer size
        renderer.setLegendTextFont(medFont);
        renderer.setMargins(new int[]{20, 30, 15, 0});

        //create simple series renderer & set colors (current, min, max)
        SimpleSeriesRenderer r = new SimpleSeriesRenderer();
        r.setColor(ColorUtil.BLUE);
        renderer.addSeriesRenderer(r);
        r = new SimpleSeriesRenderer();
        r.setColor(ColorUtil.rgb(0, 150, 0));
        renderer.addSeriesRenderer(r);
        r = new SimpleSeriesRenderer();
        r.setColor(ColorUtil.GREEN);
        renderer.addSeriesRenderer(r);
        renderer.setLabelsTextSize(smallFont.getHeight() / 2);
        renderer.setLabelsColor(ColorUtil.WHITE);
        renderer.setShowLabels(true);
        //set indicators (current, min, max)
        renderer.setVisualTypes(new DialRenderer.Type[]{Type.ARROW,
            Type.NEEDLE, Type.NEEDLE});
        renderer.setMinValue(0);
        renderer.setMaxValue(150);

        //create chart view with category series & renderer
        DialChart chart = new DialChart(category, renderer);
        return wrap("Weight indicator", getDesc(), new ChartComponent(chart),
                prevForm);

    }

}
