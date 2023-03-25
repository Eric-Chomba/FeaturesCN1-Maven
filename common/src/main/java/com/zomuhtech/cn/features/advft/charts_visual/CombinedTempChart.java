/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zomuhtech.cn.features.advft.charts_visual;

import com.codename1.charts.ChartComponent;
import com.codename1.charts.models.XYMultipleSeriesDataset;
import com.codename1.charts.models.XYSeries;
import com.codename1.charts.models.XYValueSeries;
import com.codename1.charts.renderers.XYMultipleSeriesRenderer;
import com.codename1.charts.renderers.XYSeriesRenderer;
import com.codename1.charts.util.ColorUtil;
import com.codename1.charts.views.BarChart;
import com.codename1.charts.views.BubbleChart;
import com.codename1.charts.views.CombinedXYChart;
import com.codename1.charts.views.CombinedXYChart.XYCombinedChartDef;
import com.codename1.charts.views.CubicLineChart;
import com.codename1.charts.views.LineChart;
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
public class CombinedTempChart extends AbstractDemoChart {

    Form form;

    public String getDesc() {
        return "Combined Chart - The average temperature in 2 Greek islands, "
                + "water temperature & sunshine hours";
    }

    public CombinedTempChart(Form prevForm) {

        form = new Form();
        //form.getAllStyles().setBgColor(darkBlue); //0xff0000 0X3399ff
        //form.getAllStyles().setBgTransparency(255);
        form.getToolbar().hideToolbar();
        form.setLayout(BoxLayout.y());

        Component cmp = getCombinedChart(prevForm);
       cmp.getAllStyles().setBgColor(black);
        form.addComponent(cmp);
    }

    public Form getForm() {
        return form;
    }

    private Form getCombinedChart(Form prevForm) {
        /*Air temp titles Crete(LineChart - straight lines btn points)
            Skiathos(CubicLineChart - smooth curves btn points)
         */
        String[] titles = new String[]{"Crete Air Temperature", "Skiathos Air "
            + "Temperature"};
        List<double[]> x = new ArrayList<>();

        String xVals = "1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12";
        String[] xValsArr = proc.splitValue(xVals, ",");
        double[] xArr = new double[xValsArr.length];

        for (int k = 0; k < xValsArr.length; k++) {
            xArr[k] = Double.parseDouble(xValsArr[k]);
        }

        for (String title : titles) {
            x.add(xArr);
        }

        List<double[]> values = new ArrayList<>();

        String creteVals = "12.3, 12.5, 13.8, 16.8, 20.4, 24.4, 26.4, 26.1,"
                + "23.6, 20.3, 17.2, 13.9";
        String[] creteValsArr = proc.splitValue(creteVals, ",");
        double[] creteArr = new double[creteValsArr.length];

        String skiVals = "9, 10, 11, 15, 19, 23, 26, 25, 22, 18, 13, 10";
        String[] skiValsArr = proc.splitValue(skiVals, ",");
        double[] skiArr = new double[skiValsArr.length];

        for (int c = 0; c < creteValsArr.length; c++) {
            creteArr[c] = Double.parseDouble(creteValsArr[c]);
            skiArr[c] = Double.parseDouble(skiValsArr[c]);
        }
        //add Crete(LineChart) & Skiathos(CubicLineChart) air temp values
        values.add(creteArr);
        values.add(skiArr);

        int[] colors = new int[]{ColorUtil.GREEN, ColorUtil.rgb(200, 150, 0)};
        PointStyle[] styles = new PointStyle[]{PointStyle.CIRCLE,
            PointStyle.DIAMOND};
        //get & set LineChart & CubicLineChart renderer
        XYMultipleSeriesRenderer renderer = buildRenderer(colors, styles);
        renderer.setPointSize(5.5f);
        int len = renderer.getSeriesRendererCount();

        for (int k = 0; k < len; k++) {
            XYSeriesRenderer r = (XYSeriesRenderer) renderer
                    .getSeriesRendererAt(k);
            r.setLineWidth(2);
            r.setFillPoints(true);
        }

        setChartSettings(renderer, "Weather data", "Month", "Temperature", 0.5,
                12.5, 0, 40, ColorUtil.LTGRAY, ColorUtil.LTGRAY);

        renderer.setXLabels(12);
        renderer.setYLabels(10);
        renderer.setShowGrid(true);
        renderer.setXLabelsAlign(Component.RIGHT);
        renderer.setYLabelsAlign(Component.RIGHT);
        renderer.setZoomButtonsVisible(true);
        renderer.setPanLimits(new double[]{-10, 20, -10, 40});
        renderer.setZoomLimits(new double[]{-10, 20, -10, 40});

        /*XYValueSeries - XY extension with 3rd dimension. applies in bubble 
            charts
        Create XYValueSeries & add sunshine(BubbleChart) series values 
                (x, y, bubble size)*/
        XYValueSeries sunSeries = new XYValueSeries("Sunshine hours");
        /*sunSeries.add(1f, 35, 4.3);
        sunSeries.add(2f, 35, 4.9);
        sunSeries.add(3f, 35, 5.9);
        sunSeries.add(4f, 35, 8.8);
        sunSeries.add(5f, 35, 10.8);
        sunSeries.add(6f, 35, 11.9);
        sunSeries.add(7f, 35, 13.6);
        sunSeries.add(8f, 35, 12.8);
        sunSeries.add(9f, 35, 11.4);
        sunSeries.add(10f, 35, 9.5);
        sunSeries.add(11f, 35, 7.5);
        sunSeries.add(12f, 35, 5.5);*/

        String sunSeriesVals = "1f, 35, 4.3: 2f, 35, 4.9: 3f, 35, 5.9: "
                + "4f, 35, 8.8: 5f, 35, 10.8: 6f, 35, 11.9: 7f, 35, 13.6: "
                + "8f, 35, 12.8: 9f, 35, 11.4: 10f, 35, 9.5: 11f, 35, 7.5: "
                + "12f, 35, 5.5";
        String[] sunSeriesArr = proc.splitValue(sunSeriesVals, ":");

        for (String val : sunSeriesArr) {
            String[] sunSeriessAr = proc.splitValue(val, ",");
            sunSeries.add(Double.parseDouble(sunSeriessAr[0]),
                    Double.parseDouble(sunSeriessAr[1]),
                    Double.parseDouble(sunSeriessAr[2]));
        }

        //create sunshine renderer & set bubbles color
        XYSeriesRenderer lightRenderer = new XYSeriesRenderer();
        lightRenderer.setColor(ColorUtil.YELLOW);

        //Create Crete(BarChart) XYSeries & add water temp series values
        XYSeries waterSeries = new XYSeries("Crete Water Temperature");
        /*waterSeries.add(1, 16);
        waterSeries.add(2, 15);
        waterSeries.add(3, 16);
        waterSeries.add(4, 17);
        waterSeries.add(5, 20);
        waterSeries.add(6, 23);
        waterSeries.add(7, 25);
        waterSeries.add(8, 25.5);
        waterSeries.add(9, 26.5);
        waterSeries.add(10, 24);
        waterSeries.add(11, 22);
        waterSeries.add(12, 18);*/

        String waterSeriesVals = "1, 16: 2, 15: 3, 16: 4, 17: 5, 20: 6, 23: "
                + "7, 25: 8, 25.5: 9, 26.5: 10, 24: 11, 22: 12, 18";

        String[] waterSeriesArr = proc.splitValue(waterSeriesVals, ":");

        for (String val : waterSeriesArr) {
            String[] waterSeriesAr = proc.splitValue(val, ",");
            waterSeries.add(Double.parseDouble(waterSeriesAr[0]),
                    Double.parseDouble(waterSeriesAr[1]));
        }

        //create crete renderer & set color & alignment
        XYSeriesRenderer waterRenderer1 = new XYSeriesRenderer();
        waterRenderer1.setColor(0xff0099cc);
        waterRenderer1.setChartValuesTextAlign(Component.CENTER);

        //Create Skiathos(BarChart) XYSeries & add water temp series values
        XYSeries waterSeries2 = new XYSeries("Skiathos Water Temperature");
        /*waterSeries2.add(1, 15);
        waterSeries2.add(2, 14);
        waterSeries2.add(3, 14);
        waterSeries2.add(4, 15);
        waterSeries2.add(5, 18);
        waterSeries2.add(6, 22);
        waterSeries2.add(7, 24);
        waterSeries2.add(8, 25);
        waterSeries2.add(9, 24);
        waterSeries2.add(10, 21);
        waterSeries2.add(11, 18);
        waterSeries2.add(12, 16);*/

        String waterSeries2Vals = "1, 15: 2, 14: 3, 14: 4, 15: 5, 18: 6, 22: "
                + "7, 24: 8, 25: 9, 24: 10, 21: 11, 18: 12, 16";

        String[] waterSeries2Arr = proc.splitValue(waterSeries2Vals, ":");

        for (String val : waterSeries2Arr) {
            String[] waterSeries2Ar = proc.splitValue(val, ",");
            waterSeries2.add(Double.parseDouble(waterSeries2Ar[0]),
                    Double.parseDouble(waterSeries2Ar[1]));
        }

        //create skiathos renderer & set color & alignment
        XYSeriesRenderer waterRenderer2 = new XYSeriesRenderer();
        waterRenderer2.setColor(0xff9933cc);
        waterRenderer2.setChartValuesTextAlign(Component.RIGHT);

        renderer.setBarSpacing(0.3);

        /*create & get Crete(LineChart - series index 3) & 
            Skiathos(CubicLineChart - series indes 4) air temp dataset*/
        XYMultipleSeriesDataset dataset = buildDataset(titles, x, values);
        //add sunshine(BubbleChart - series index 2) data/series set
        dataset.addSeries(0, sunSeries);
        //add crete(BarChart - series index 1) water temp  data/series set
        dataset.addSeries(0, waterSeries);
        //add skiathos (BarChart - series index 1) water temp  data/series set
        dataset.addSeries(0, waterSeries2);

        //add sunshine & water temp renderers
        renderer.addSeriesRenderer(0, lightRenderer);
        renderer.addSeriesRenderer(0, waterRenderer1);
        renderer.addSeriesRenderer(0, waterRenderer2);

        waterRenderer1.setDisplayChartValues(true);
        waterRenderer1.setChartValuesTextSize(smallFont.getHeight() / 2);
        waterRenderer2.setDisplayChartValues(true);
        waterRenderer2.setChartValuesTextSize(smallFont.getHeight() / 2);

        //Define charts in CombinedXYChart
        XYCombinedChartDef[] types = new XYCombinedChartDef[]{
            //Construct charts definitions (type, series index)
            new XYCombinedChartDef(BarChart.TYPE, 0, 1),
            new XYCombinedChartDef(BubbleChart.TYPE, 2),
            new XYCombinedChartDef(LineChart.TYPE, 3),
            new XYCombinedChartDef(CubicLineChart.TYPE, 4)
        };

        //create combined chart view with dataset, renderer & types
        CombinedXYChart chart = new CombinedXYChart(dataset, renderer, types);
        return wrap("Weather parameters", getDesc(), new ChartComponent(chart),
                prevForm);

    }

}
