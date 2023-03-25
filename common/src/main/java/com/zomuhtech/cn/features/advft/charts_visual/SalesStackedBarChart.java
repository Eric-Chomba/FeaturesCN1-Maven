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
public class SalesStackedBarChart extends AbstractDemoChart {

    Form form;

    public String getDesc() {
        return "Stacked bar chart - monthly sales for the last 2 yrs";
    }

    public SalesStackedBarChart(Form prevForm) {

        form = new Form();
        form.getToolbar().hideToolbar();
        form.setLayout(BoxLayout.y());

        Component cmp = getStackedChart(prevForm);
        cmp.getAllStyles().setBgColor(black);
        form.addComponent(cmp);
    }

    public Form getForm() {
        return form;
    }

    private Form getStackedChart(Form prevForm) {

        String[] titles = new String[]{"2008", "2007"};

        List<double[]> values = new ArrayList<>();

        String vals1 = "14230, 12300, 14240, 300, 15900, 19200, 22030, 21200, "
                + "19500, 15500, 12600, 14000";
        String[] vals1Array = proc.splitValue(vals1, ",");
        double[] vals1Arr = new double[vals1Array.length];

        String vals2 = "5230, 7300, 9240, 10540, 7900, 9200, 12030, 11200, "
                + "9500, 10500, 11600, 13500";
        String[] vals2Array = proc.splitValue(vals2, ",");
        double[] vals2Arr = new double[vals2Array.length];

        for (int c = 0; c < vals1Array.length; c++) {
            vals1Arr[c] = Double.parseDouble(vals1Array[c]);
            vals2Arr[c] = Double.parseDouble(vals2Array[c]);
        }
        values.add(vals1Arr);
        values.add(vals2Arr);

        int[] colors = new int[]{ColorUtil.BLUE, ColorUtil.CYAN};
        //get bars renderer
        XYMultipleSeriesRenderer renderer = buildBarRenderer(colors);
        setChartSettings(renderer, "Monthly sales in the last 2 years", "Month",
                "Units sold", 0.5, 12.5, 0, 24000, ColorUtil.GRAY,
                ColorUtil.LTGRAY);
        //show/hide chart values
        ((XYSeriesRenderer) renderer.getSeriesRendererAt(0))
                .setDisplayChartValues(true);
        ((XYSeriesRenderer) renderer.getSeriesRendererAt(1))
                .setDisplayChartValues(true);
        renderer.setXLabels(12);
        renderer.setYLabels(10);
        renderer.setXLabelsAlign(Component.LEFT);
        renderer.setYLabelsAlign(Component.LEFT);
        renderer.setPanEnabled(true, false);
        renderer.setZoomRate(1.1f);
        renderer.setBarSpacing(0.5f);

        //build chart view with dataset, renderer & bar chart type
        BarChart chart = new BarChart(buildBarDataset(titles, values), renderer,
                Type.STACKED);

        return wrap("Sales Stacked Bar Chart", getDesc(),
                new ChartComponent(chart), prevForm);

    }
}
