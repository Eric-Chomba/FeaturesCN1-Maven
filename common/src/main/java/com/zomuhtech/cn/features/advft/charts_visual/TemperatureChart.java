/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zomuhtech.cn.features.advft.charts_visual;

import com.codename1.charts.ChartComponent;
import com.codename1.charts.models.RangeCategorySeries;
import com.codename1.charts.models.XYMultipleSeriesDataset;
import com.codename1.charts.renderers.XYMultipleSeriesRenderer;
import com.codename1.charts.renderers.XYSeriesRenderer;
import com.codename1.charts.util.ColorUtil;
import com.codename1.charts.views.BarChart.Type;
import com.codename1.charts.views.RangeBarChart;
import com.codename1.ui.Component;
import com.codename1.ui.Form;
import com.codename1.ui.layouts.BoxLayout;

/**
 *
 * @author Eric
 */
public class TemperatureChart extends AbstractDemoChart {

    Form form;

    public String getDesc() {
        return "RangeBarChart(vertical) - the monthly temperature";
    }

    public TemperatureChart(Form prevForm) {

        form = new Form();
        form.getToolbar().hideToolbar();
        form.setLayout(BoxLayout.y());

        Component cmp = getRangeChart(prevForm);
        cmp.getAllStyles().setBgColor(black);
        form.addComponent(cmp);
    }

    public Form getForm() {
        return form;
    }

    private Form getRangeChart(Form prevForm) {

        String vals1 = "-24, -19, -10, -1, 7, 12, 15 ,14, 9, 1, -11, -16";
        String[] vals1Array = proc.splitValue(vals1, ",");
        double[] minValues = new double[vals1Array.length];

        String vals2 = "7, 12, 24, 28, 33, 35, 37, 36, 28, 19, 11, 4";
        String[] vals2Array = proc.splitValue(vals2, ",");
        double[] maxValues = new double[vals2Array.length];

        for (int c = 0; c < vals1Array.length; c++) {
            minValues[c] = Double.parseDouble(vals1Array[c]);
            maxValues[c] = Double.parseDouble(vals2Array[c]);
        }
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        //create range category series
        RangeCategorySeries series = new RangeCategorySeries("Temperature");
        int len = minValues.length;

        for (int j = 0; j < len; j++) {
            //add min&max values to series
            series.add(minValues[j], maxValues[j]);
        }
        //add series to dataset
        dataset.addSeries(series.toXYSeries());
        int[] colors = new int[]{ColorUtil.CYAN};
        XYMultipleSeriesRenderer renderer = buildBarRenderer(colors);
        setChartSettings(renderer, "Monthly temperature range", "Month",
                "Celcius Degrees", 0.5, 12.5, -30, 45, ColorUtil.GRAY,
                ColorUtil.LTGRAY);
        renderer.setBarSpacing(0.5);
        renderer.setXLabels(0);
        renderer.setYLabels(10);
        //add months on x axis
        renderer.addXTextLabel(1, "Jan");
        renderer.addXTextLabel(3, "Mar");
        renderer.addXTextLabel(5, "May");
        renderer.addXTextLabel(7, "July");
        renderer.addXTextLabel(10, "Oct");
        renderer.addXTextLabel(12, "Dec");
        //add temp comment on y axis
        renderer.addYTextLabel(-25, "Very cold");
        renderer.addYTextLabel(-10, "Cold");
        renderer.addYTextLabel(5, "OK");
        renderer.addYTextLabel(20, "Nice");

        renderer.setMargins(new int[]{30, 70, 10, 0});
        renderer.setYLabelsAlign(Component.RIGHT);

        XYSeriesRenderer r = (XYSeriesRenderer) renderer.getSeriesRendererAt(0);
        r.setDisplayChartValues(true);
        r.setChartValuesTextSize(smallFont.getHeight() / 2);
        r.setChartValuesSpacing(3);
        //set gradient
        r.setGradientEnabled(true);
        r.setGradientStart(-20, ColorUtil.BLUE);
        r.setGradientStop(20, ColorUtil.GREEN);

        //create chart view with dataset, renderer & bar chart type
        RangeBarChart chart = new RangeBarChart(dataset, renderer,
                Type.DEFAULT);
        
        return wrap("Temperature range", getDesc(), new ChartComponent(chart),
                prevForm);

    }

}
