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
public final class AverageCubicTempChart extends AbstractDemoChart {

    /*//return chart name
    @Override
    public String getName() {
        return "Average Temperature";
    }
     */
    //return description
    public String getDesc() {
        return "Cubic Line chart - The average temperature in 4 Greek islands";
    }
    Form form;

    public AverageCubicTempChart(Form prevForm) {
        form = new Form();
        //form.getAllStyles().setBgColor(0X3399ff); //0xff0000 0X3399ff
        //form.getAllStyles().setBgTransparency(255);
        form.getToolbar().hideToolbar();
        form.setLayout(BoxLayout.y());

        Component cmp = getCubicLineChart(prevForm);
        //cmp.setUIID("chartCmp");
        cmp.getStyle().setBgColor(0X0); //0Xffffff
        //cmp.getAllStyles().setBgColor(0X3399ff); //0xff0000 0X3399ff
        //cmp.getAllStyles().setBgTransparency(255);

        form.addComponent(cmp);
        form.setScrollableY(true);
    }

    public Form getForm() {
        return form;
    }

    public Form getCubicLineChart(Form prevForm) {
        //Titles array
        String[] titles = new String[]{"Crete", "Corfu", "Thassos", "Skiathos"};
        //x axis values array
        List<double[]> x = new ArrayList<>();

        /*for (int j = 0; j < titles.length; j++) {
            x.add(new double[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12});
        }*/
        String xVals = "1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12";
        String[] xValsArr = proc.splitValue(xVals, ",");
        double[] xArr = new double[xValsArr.length];

        for (int k = 0; k < xValsArr.length; k++) {
            xArr[k] = Double.parseDouble(xValsArr[k]);
        }

        for (String title : titles) {
            x.add(xArr);
        }

        //y axis values array
        List<double[]> values = new ArrayList<>();
        /*values.add(new double[]{12.3, 12.5, 13.8, 16.8, 20.4, 24.4, 26.4, 26.1,
            23.6, 20.3, 17.2, 13.9});
        values.add(new double[]{10, 10, 12, 15, 20, 24, 26, 26, 23, 18, 14,
            11});
        values.add(new double[]{5, 5.3, 8, 12, 17, 22, 24.2, 24, 19, 15, 9, 6});
        values.add(new double[]{9, 10, 11, 15, 19, 23, 26, 25, 22, 18, 13, 10});*/

        String creteVals = "12.3, 12.5, 13.8, 16.8, 20.4, 24.4, 26.4, 26.1,"
                + "23.6, 20.3, 17.2, 13.9";
        String[] creteValsArr = proc.splitValue(creteVals, ",");
        double[] creteArr = new double[creteValsArr.length];

        String corVals = "10, 10, 12, 15, 20, 24, 26, 26, 23, 18, 14, 11";
        String[] corValsArr = proc.splitValue(corVals, ",");
        double[] corArr = new double[corValsArr.length];

        String thaVals = "5, 5.3, 8, 12, 17, 22, 24.2, 24, 19, 15, 9, 6";
        String[] thaValsArr = proc.splitValue(thaVals, ",");
        double[] thaArr = new double[thaValsArr.length];

        String skiVals = "9, 10, 11, 15, 19, 23, 26, 25, 22, 18, 13, 10";
        String[] skiValsArr = proc.splitValue(skiVals, ",");
        double[] skiArr = new double[skiValsArr.length];

        for (int c = 0; c < creteValsArr.length; c++) {
            creteArr[c] = Double.parseDouble(creteValsArr[c]);
            corArr[c] = Double.parseDouble(corValsArr[c]);
            thaArr[c] = Double.parseDouble(thaValsArr[c]);
            skiArr[c] = Double.parseDouble(skiValsArr[c]);
        }
        values.add(creteArr);
        values.add(corArr);
        values.add(thaArr);
        values.add(skiArr);

        //lines & points color array
        int[] colors = new int[]{ColorUtil.BLUE, ColorUtil.GREEN,
            ColorUtil.CYAN, ColorUtil.YELLOW};
        //point styles array
        PointStyle[] styles = new PointStyle[]{PointStyle.CIRCLE,
            PointStyle.DIAMOND, PointStyle.TRIANGLE, PointStyle.SQUARE};

        //Get renderer - specifies chart's look(color,fonts,styles)
        XYMultipleSeriesRenderer renderer = buildRenderer(colors, styles);
        //set bg color
        //renderer.setApplyBackgroundColor(true);
        //renderer.setBackgroundColor(0X3399ff);
        int len = renderer.getSeriesRendererCount();

        //fill points true - opaque, false - transparent
        for (int k = 0; k < len; k++) {
            ((XYSeriesRenderer) renderer.getSeriesRendererAt(k))
                    .setFillPoints(true);
        }

        setChartSettings(renderer, "Average temperature", "Month",
                "Temperature", 0.5, 12.5, 0, 32, ColorUtil.LTGRAY,
                ColorUtil.LTGRAY);

        renderer.setApplyBackgroundColor(true);
        renderer.setBackgroundColor(black);
        renderer.setXLabels(12);
        renderer.setYLabels(10);
        renderer.setShowGrid(true);
        renderer.setXLabelsAlign(Component.RIGHT);
        renderer.setYLabelsAlign(Component.RIGHT);
        renderer.setZoomButtonsVisible(true);
        //set renderer pan (x&y scrolls) limits
        //-10 xMin 20 yMax -10 yMin 40 yMax
        renderer.setPanLimits(new double[]{-10, 20, -10, 40});
        //set renderer zoom limits
        //-10 min distance btn xMin&Max values
        //20 max distances btn xMin&Max values 
        //-10 min distance btn yMin&Max values
        //40 max distances btn yMin&Max values 
        renderer.setZoomLimits(new double[]{-10, 20, -10, 40});

        //create chart view with dataset, renderer, curve smoothness
        //0.33f curve smoothness(range 0-0.5)
        //closer to 0, most likely to pass though all points
        CubicLineChart chart = new CubicLineChart(buildDataset(titles, x,
                values), renderer, 0.33f);
        //Add chart to Chart Cmp to enable adding to UI
        ChartComponent cmp = new ChartComponent(chart);
        //cmp.setUIID("chartCmp");
       // cmp.getAllStyles().setBgColor(darkBlue);
        // cmp.getAllStyles().setBgTransparency(0);

        return wrap("Avg. Cubic Temperature", getDesc(), cmp, prevForm);
    }
}
