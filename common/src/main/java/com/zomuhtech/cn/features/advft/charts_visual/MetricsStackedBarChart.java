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
import com.codename1.charts.views.BarChart;
import com.codename1.charts.views.BarChart.Type;
import com.codename1.ui.Component;
import com.codename1.ui.Form;
import com.codename1.ui.layouts.BoxLayout;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Eric
 */
public class MetricsStackedBarChart extends AbstractDemoChart {

    Form form;

    public String getDesc() {
        return "Stacked Bar Chart - System Health and Compliance";
    }

    public MetricsStackedBarChart(Form prevForm) {

        form = new Form();
        //form.getAllStyles().setBgColor(darkBlue); //0xff0000 0X3399ff
        //form.getAllStyles().setBgTransparency(255);
        form.getToolbar().hideToolbar();
        form.setLayout(BoxLayout.y());

        Component cmp = getStackedBarChart(prevForm);
        cmp.getAllStyles().setBgColor(black);
        form.addComponent(cmp);
    }

    public Form getForm() {
        return form;
    }

    private Form getStackedBarChart(Form prevForm) {
        String[] titles = new String[]{"Gnr1", "Gnr Pvt", "Conc.", "Pvt", "VA",
            "S3"};
        List<double[]> values = new ArrayList<>();

        values.add(new double[]{33, 22, 30, 15}); //Gnr1
        values.add(new double[]{12, 32, 10, 35}); //Gnr Pvt
        values.add(new double[]{12, 21, 20, 10}); //Conc
        values.add(new double[]{6, 12, 20, 10}); //Pvt
        values.add(new double[]{21, 12, 22, 23}); //VA
        values.add(new double[]{11, 12, 20, 21});//S3

        /*String vals = "33,22,30,15: ";
        String[] valsArray = proc.splitValue(vals, ":");
        double[] valsArr = new double[valsArray.length];

        for (int c = 0; c < valsArray.length; c++) {
            String[] valAr = proc.splitValue(valsArray[c].trim(), ",");
            valsArr[c] = new double[]{Double.parseDouble(valAr[0])};
        }

        values.add(valsArr);*/
        int[] colors = new int[]{ColorUtil.rgb(0, 76, 153),
            ColorUtil.rgb(0, 102, 204), ColorUtil.rgb(0, 128, 255),
            ColorUtil.rgb(51, 153, 255), ColorUtil.rgb(102, 178, 255),
            ColorUtil.rgb(153, 204, 255)};
        XYMultipleSeriesRenderer renderer = buildBarRenderer(colors);
        setChartSettings(renderer, "Dispensary Revenue By Day", "",
                "Prescription Type Revenue", 0.5, 4.5, 0, 50, ColorUtil.GRAY,
                ColorUtil.LTGRAY);
        renderer.setXLabels(0);
        renderer.setYLabels(0);
        renderer.setXLabelsAlign(Component.LEFT);
        renderer.setYLabelsAlign(Component.LEFT);
        //Enable X scrolling & disable Y scrolling
        renderer.setPanEnabled(true, false);
        renderer.setZoomRate(1.1f);
        renderer.setBarSpacing(0.5f);
        //apply & set bg color
        renderer.setApplyBackgroundColor(true);
        renderer.setBackgroundColor(ColorUtil.WHITE);
        //set bars width
        renderer.setBarWidth(70);
        //set x axis bars labels
        renderer.addXTextLabel(1, "TODAY");
        renderer.addXTextLabel(2, "YEST.");
        renderer.addXTextLabel(3, "PREV DAY");
        renderer.addXTextLabel(4, "7 DAYS AVG");

        int len = renderer.getSeriesRendererCount();

        for (int j = 0; j < len; j++) {
            XYSeriesRenderer seriesRenderer = (XYSeriesRenderer) renderer
                    .getSeriesRendererAt(j);
            seriesRenderer.setDisplayChartValues(true);
            seriesRenderer.setChartValuesTextFont(largeFont);
        }

        BarChart chart = new BarChart(buildBarDataset(titles, values), renderer,
                Type.STACKED);

        return wrap("Dispensary Revenue", getDesc(), new ChartComponent(chart),
                prevForm);
    }

}
