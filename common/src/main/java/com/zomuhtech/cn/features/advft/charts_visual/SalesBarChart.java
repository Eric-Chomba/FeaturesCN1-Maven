/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zomuhtech.cn.features.advft.charts_visual;

import com.codename1.charts.ChartComponent;
import com.codename1.charts.renderers.XYMultipleSeriesRenderer;
import com.codename1.charts.renderers.XYMultipleSeriesRenderer.Orientation;
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
public class SalesBarChart extends AbstractDemoChart {

    Form form;

    public String getDesc() {
        return "Horizontal bar chart -The monthly sales for the last 2 yrs";
    }

    public SalesBarChart(Form prevForm) {

        form = new Form();
        form.getToolbar().hideToolbar();
        form.setLayout(BoxLayout.y());

        Component cmp = getSalesBarChart(prevForm);
        cmp.getAllStyles().setBgColor(black);
        form.addComponent(cmp);
    }

    public Form getForm() {
        return form;
    }

    private Form getSalesBarChart(Form prevForm) {
        String[] titles = new String[]{"2007", "2008"};

        List<double[]> values = new ArrayList<>();

        String vals1 = "5230, 7300, 9240, 10540, 7900, 9200, 12030, 11200, "
                + "9500, 10500, 11600, 13500";
        String[] vals1Array = proc.splitValue(vals1, ",");
        double[] vals1Arr = new double[vals1Array.length];

        for (int c = 0; c < vals1Array.length; c++) {
            vals1Arr[c] = Double.parseDouble(vals1Array[c]);

        }
        values.add(vals1Arr);

        String vals2 = "14230, 12300, 14240, 15244, 15900, 19200, 22030, 21200,"
                + "19500, 15500, 12600, 14000";
        String[] vals2Array = proc.splitValue(vals2, ",");
        double[] vals2Arr = new double[vals2Array.length];

        for (int c = 0; c < vals2Array.length; c++) {
            vals2Arr[c] = Double.parseDouble(vals2Array[c]);

        }
        values.add(vals2Arr);

        int[] colors = new int[]{ColorUtil.CYAN, ColorUtil.BLUE};
        XYMultipleSeriesRenderer renderer = buildBarRenderer(colors);
        renderer.setOrientation(Orientation.HORIZONTAL);
        setChartSettings(renderer, "Montly sales in the last 2 years", "Month",
                "Units sold", 0.5, 12.5, 0, 24000, ColorUtil.GRAY,
                ColorUtil.LTGRAY);
        renderer.setXLabels(1);
        renderer.setYLabels(10);
        renderer.addXTextLabel(1, "Jan");
        renderer.addXTextLabel(3, "Mar");
        renderer.addXTextLabel(5, "May");
        renderer.addXTextLabel(7, "Jul");
        renderer.addXTextLabel(10, "Oct");
        renderer.addXTextLabel(12, "Dec");

        int len = renderer.getSeriesRendererCount();
        for (int j = 0; j < len; j++) {
            XYSeriesRenderer seriesRenderer = (XYSeriesRenderer) renderer
                    .getSeriesRendererAt(j);
            seriesRenderer.setDisplayChartValues(true);
        }

        BarChart chart = new BarChart(buildBarDataset(titles, values), renderer,
                Type.DEFAULT);

        return wrap("Monthly Sales", getDesc(), new ChartComponent(chart),
                prevForm);
    }
}
