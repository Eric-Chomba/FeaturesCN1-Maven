/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zomuhtech.cn.features.advft.charts_visual;

import com.codename1.charts.ChartComponent;
import com.codename1.charts.models.XYMultipleSeriesDataset;
import com.codename1.charts.renderers.XYMultipleSeriesRenderer;
import com.codename1.charts.renderers.XYSeriesRenderer;
import com.codename1.charts.util.ColorUtil;
import com.codename1.charts.views.CubicLineChart;
import com.codename1.charts.views.PointStyle;
import com.codename1.ui.Component;
import com.codename1.ui.Form;
import com.codename1.ui.layouts.BoxLayout;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Eric
 */
public class MultipleTempChart extends AbstractDemoChart {

    Form form;

    public String getDesc() {
        return "Line chart with multiple Y scales & axis - The average "
                + "temperature and hours of sunshine in Crete";
    }

    public MultipleTempChart(Form prevForm) {

        form = new Form();
        form.getAllStyles().setBgColor(white); //0xff0000 0X3399ff
        form.getAllStyles().setBgTransparency(255);
        form.getToolbar().hideToolbar();
        form.setLayout(BoxLayout.y());

        Component cmp = getMultipleTempChart(prevForm);
        cmp.getAllStyles().setBgColor(white);
        form.addComponent(cmp);
    }

    public Form getForm() {
        return form;
    }

    private Form getMultipleTempChart(Form prevForm) {
        String[] titles = new String[]{"Air temperature"};
        List<double[]> x = new ArrayList<>();

        x.add(new double[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12});

        List<double[]> values = new ArrayList<>();

        String vals1 = "12.3, 12.5, 13.8, 16.8, 20.4, 24.4, 26.4, 26.1, 23.6,"
                + " 20.3, 17.2, 13.9";
        String[] vals1Array = proc.splitValue(vals1, ",");
        double[] vals1Arr = new double[vals1Array.length];

        for (int c = 0; c < vals1Array.length; c++) {
            vals1Arr[c] = Double.parseDouble(vals1Array[c]);

        }
        values.add(vals1Arr);

        int[] colors = new int[]{ColorUtil.BLUE, ColorUtil.YELLOW};
        PointStyle[] styles = new PointStyle[]{PointStyle.POINT,
            PointStyle.POINT};
        XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer(2);
        setRenderer(renderer, colors, styles);
        int len = renderer.getSeriesRendererCount();

        for (int k = 0; k < len; k++) {
            XYSeriesRenderer r = (XYSeriesRenderer) renderer.getSeriesRendererAt(k);
            r.setLineWidth(3f);
        }

        setChartSettings(renderer, "Average Temperature", "Month", "Temperature",
                0.5, 12.5, 0, 32, blue, ColorUtil.LTGRAY);

        renderer.setXLabels(12);
        renderer.setYLabels(10);
        renderer.setShowGrid(true);
        renderer.setXLabelsAlign(Component.RIGHT);
        renderer.setYLabelsAlign(Component.RIGHT);
        renderer.setZoomButtonsVisible(true);
        renderer.setPanLimits(new double[]{-10, 20, 10, 40});
        renderer.setZoomLimits(new double[]{-10, 20, 10, 40});
        renderer.setZoomRate(1.05f);
        //set margins background color
        renderer.setMarginsColor(white);
        //set chart title, x&y axis titles color
        renderer.setLabelsColor(darkBlue);
        //set x axis labels color
        renderer.setXLabelsColor(ColorUtil.GREEN);
        //set y axis(air temp) labels color
        renderer.setYLabelsColor(0, colors[0]);
        //set y axis(sunshine hours) labels color
        renderer.setYLabelsColor(1, colors[1]);

        //set y axis(sunshine hours) 
        renderer.setYTitle("Hours", 1);
        //set y axis(sunshine) on the right of chart
        renderer.setYAxisAlign(Component.RIGHT, 1);
        //set y labels(sunshine) on the right of chart
        renderer.setYLabelsAlign(Component.LEFT, 1);
        //set grid colors, blue & yellow
        renderer.setGridColor(colors[0], 0);
        renderer.setGridColor(colors[1], 1);

        //create & get air temp dataset
        XYMultipleSeriesDataset dataset = buildDataset(titles, x, values);
        //clear x axis(air temp) values & add x axis(sunshine temp) values
        x.clear();
        x.add(new double[]{-1, 0, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12});
        
        //clear y axis(air temp) values & add y axis(sunshine temp) values
        values.clear();

        String vals2 = "4.3, 4.9, 5.9, 8.8, 10.8, 11.9, 13.6, 12.8, 11.4, 9.5,"
                + " 7.5, 5.5";
        String[] vals2Array = proc.splitValue(vals2, ",");
        double[] vals2Arr = new double[vals2Array.length];

        for (int c = 0; c < vals2Array.length; c++) {
            vals2Arr[c] = Double.parseDouble(vals2Array[c]);

        }
        values.add(vals2Arr);

        //add sunshine x&y values to dataset
        addXYSeries(dataset, new String[]{"Sunshine hours"}, x, values, 1);

        CubicLineChart chart = new CubicLineChart(dataset, renderer, 0.3f);
        
        return wrap("Average Temperature", getDesc(), new ChartComponent(chart),
                prevForm);
    }
}
