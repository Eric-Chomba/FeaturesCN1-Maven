/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zomuhtech.cn.features.advft.charts_visual;

import com.codename1.charts.ChartComponent;
import com.codename1.charts.renderers.XYMultipleSeriesRenderer;
import com.codename1.charts.renderers.XYSeriesRenderer;
import com.codename1.charts.util.ColorUtil;
import com.codename1.charts.views.PointStyle;
import com.codename1.charts.views.ScatterChart;
import com.codename1.ui.Component;
import com.codename1.ui.Form;
import com.codename1.ui.layouts.BoxLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Eric
 */
public class ScatterChartFt extends AbstractDemoChart {

    Form form;

    public String getDesc() {
        return "Scatter chart - randomly generated values";
    }

    public ScatterChartFt(Form prevForm) {

        form = new Form();
        form.getToolbar().hideToolbar();
        form.setLayout(BoxLayout.y());

        Component cmp = getScatterChart(prevForm);
        cmp.getAllStyles().setBgColor(black);
        form.addComponent(cmp);
    }

    public Form getForm() {
        return form;
    }

    private Form getScatterChart(Form prevForm) {
        
        String[] titles = new String[]{"series 1", "series 2", "series 3",
            "series 4", "series 5"};
        
        List<double[]> x = new ArrayList<>();
        List<double[]> values = new ArrayList<>();
        int count = 20;
        int len = titles.length;
        
        //generate random points & add to x&y axis arrays
        Random r = new Random();

        for (int j = 0; j < len; j++) {
         
            double[] xValues = new double[count];
            double[] yValues = new double[count];

            for (int k = 0; k < count; k++) {
                xValues[k] = k + r.nextInt() % 10;
                yValues[k] = k * 2 + r.nextInt() % 10;
            }
            x.add(xValues);
            values.add(yValues);
        }

        int[] colors = new int[]{ColorUtil.BLUE, ColorUtil.CYAN,
            ColorUtil.MAGENTA, ColorUtil.LTGRAY, ColorUtil.GREEN};
        PointStyle[] styles = new PointStyle[]{PointStyle.X, PointStyle.DIAMOND,
            PointStyle.TRIANGLE, PointStyle.SQUARE, PointStyle.CIRCLE};
        XYMultipleSeriesRenderer renderer = buildRenderer(colors, styles);
        setChartSettings(renderer, "Scatter Chart", "X", "Y", -10, 30, -10, 51,
                ColorUtil.GRAY, ColorUtil.LTGRAY);
        renderer.setXLabels(10);
        renderer.setYLabels(10);
        len = renderer.getSeriesRendererCount();

        for (int m = 0; m < len; m++) {
            //fill points with colors
            ((XYSeriesRenderer) renderer.getSeriesRendererAt(m))
                    .setFillPoints(true);
        }

        ScatterChart chart = new ScatterChart(buildDataset(titles, x, values),
                renderer);

        return wrap("Scatter Chart", getDesc(), new ChartComponent(chart),
                prevForm);
    }

}
