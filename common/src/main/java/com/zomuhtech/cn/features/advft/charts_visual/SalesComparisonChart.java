/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zomuhtech.cn.features.advft.charts_visual;

import com.codename1.charts.ChartComponent;
import com.codename1.charts.util.ColorUtil;
import com.codename1.charts.views.PointStyle;
import com.codename1.charts.renderers.XYMultipleSeriesRenderer;
import com.codename1.charts.renderers.XYSeriesRenderer;
import com.codename1.charts.renderers.XYSeriesRenderer.FillOutsideLine;
import com.codename1.charts.views.CubicLineChart;
import com.codename1.ui.Component;
import com.codename1.ui.Font;
import com.codename1.ui.Form;
import com.codename1.ui.layouts.BoxLayout;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Eric
 */
public class SalesComparisonChart extends AbstractDemoChart {

    Form form;

    public String getDesc() {
        return "Interpolated line & area charts - monthly sales advance for "
                + "2 yrs";
    }

    public SalesComparisonChart(Form prevForm) {

        form = new Form();
        form.getToolbar().hideToolbar();
        form.setLayout(BoxLayout.y());

        Component cmp = getComparisonChart(prevForm);
        cmp.getAllStyles().setBgColor(black);
        form.addComponent(cmp);
    }

    public Form getForm() {
        return form;
    }

    private Form getComparisonChart(Form prevForm) {
        String[] titles = new String[]{"Sales for 2008", "Sales for 2007",
            "Difference between 2008 and 2007 sales"};

        List<double[]> values = new ArrayList<>();

        String vals1 = "14230, 12300, 14240, 15244, 14900, 12200, 11030, 12000,"
                + "12500, 15500, 14600, 15000";
        String[] vals1Array = proc.splitValue(vals1, ",");
        double[] vals1Arr = new double[vals1Array.length];

        for (int c = 0; c < vals1Array.length; c++) {
            vals1Arr[c] = Double.parseDouble(vals1Array[c]);

        }
        values.add(vals1Arr);

        String vals2 = "10230, 10900, 11240, 12540, 13500, 14200, 12530, 11200,"
                + "10500, 12500, 11600, 15000";
        String[] vals2Array = proc.splitValue(vals2, ",");
        double[] vals2Arr = new double[vals2Array.length];

        for (int c = 0; c < vals2Array.length; c++) {
            vals2Arr[c] = Double.parseDouble(vals2Array[c]);

        }
        values.add(vals2Arr);

        int len = values.get(0).length;
        double[] diff = new double[len];
        for (int j = 0; j < len; j++) {
            //compute difference btn first array item & second array item &
            //add to diff array
            diff[j] = values.get(0)[j] - values.get(1)[j];
        }
        //add to values array as 3rd item
        values.add(diff);

        int[] colors = new int[]{ColorUtil.BLUE, ColorUtil.CYAN,
            ColorUtil.GREEN};
        PointStyle[] styles = new PointStyle[]{PointStyle.POINT,
            PointStyle.POINT, PointStyle.POINT};

        XYMultipleSeriesRenderer renderer = buildRenderer(colors, styles);
        setChartSettings(renderer, "Monthly sales in the last 2 years", "Month",
                "Units sold", 0.75, 12.25, -5000, 19000, ColorUtil.GRAY,
                ColorUtil.LTGRAY);
        renderer.setXLabels(12);
        renderer.setYLabels(10);
        renderer.setChartTitleTextFont(smallFont);
        renderer.setTextTypeface(Font.FACE_SYSTEM, Font.STYLE_BOLD);
        renderer.setLabelsTextSize(smallFont.getHeight() / 2);
        renderer.setAxisTitleTextSize(smallFont.getHeight() / 2);
        renderer.setLegendTextSize(smallFont.getHeight() / 2);
        len = renderer.getSeriesRendererCount();

        for (int k = 0; k < len; k++) {
            XYSeriesRenderer seriesRenderer = 
                    (XYSeriesRenderer) renderer.getSeriesRendererAt(k);

            if (k == len - 1) {
                //set comparison fill type & color
                //type BOUNDS_ALL - fills above & below
                //type BOUNDS_ABOVE - fills above only
                 //type BOUNDS_BELOW - fills below only
                 //type NONE - no above/below fill
                
                FillOutsideLine fill = new FillOutsideLine(
                        FillOutsideLine.Type.BOUNDS_ALL);
                fill.setColor(ColorUtil.GREEN);
                //fill.setColor(black);
                seriesRenderer.addFillOutsideLine(fill);
            }
            seriesRenderer.setLineWidth(2.5f);
            seriesRenderer.setDisplayChartValues(true);
            seriesRenderer.setChartValuesTextSize(smallFont.getHeight() / 3);
        }

        CubicLineChart chart = new CubicLineChart(
                buildBarDataset(titles, values), renderer, 0.5f);
     
        return wrap("Sales Comparison Chart", getDesc(), 
                new ChartComponent(chart), prevForm);
    }
}
