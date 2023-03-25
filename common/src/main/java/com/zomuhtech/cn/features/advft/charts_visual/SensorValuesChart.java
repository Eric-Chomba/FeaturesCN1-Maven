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
import com.codename1.charts.util.MathHelper;
import com.codename1.charts.views.PointStyle;
import com.codename1.charts.views.TimeChart;
import com.codename1.ui.Component;
import com.codename1.ui.Display;
import com.codename1.ui.Form;
import com.codename1.ui.layouts.BoxLayout;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Eric
 */
public class SensorValuesChart extends AbstractDemoChart {

    Form form;
    private static final long HOUR = 3600 * 1000;
    private static final long DAY = HOUR * 24;
    private static final int HOURS = 24;

    public String getDesc() {
        return "Temperature read from outside & inside sensor";
    }

    public SensorValuesChart(Form prevForm) {

        form = new Form();
        form.getToolbar().hideToolbar();
        form.setLayout(BoxLayout.y());

        Component cmp = getTimeChart(prevForm);
        cmp.getAllStyles().setBgColor(black);
        form.addComponent(cmp);
    }

    public Form getForm() {
        return form;
    }

    private Form getTimeChart(Form prevForm) {

        String[] titles = new String[]{"Inside", "Outside"};
        long now = Math.round(new Date().getTime() / DAY) * DAY;
        List<Date[]> x = new ArrayList<>();

        //create array of x axis values
        for (int j = 0; j < titles.length; j++) {
            Date[] dates = new Date[HOURS];

            for (int k = 0; k < HOURS; k++) {
                dates[k] = new Date(now - (HOURS - k) * HOUR);
            }
            x.add(dates);
        }

        List<double[]> values = new ArrayList<>();

        String vals1 = "21.2, 21.5, 21.7, 21.5, 21.4, 21.4, 21.3, 21.1, 20.6, "
                + "20.3, 20.2, 19.9, 19.7, 19.6, 19.9, 20.3, 20.6, 20.9, 21.2, "
                + "21.6, 21.9, 22.1, 21.7, 21.5";
        String[] vals1Array = proc.splitValue(vals1, ",");
        double[] vals1Arr = new double[vals1Array.length];

        String vals2 = "1.9, 1.2, 0.9, 0.5, 0.1, -0.5, -0.6, NULL, NULL, -1.8, "
                + "-0.3, 1.4, 3.4, 4.9, 7.0, 6.4, 3.4, 2.0, 1.5, 0.9, -0.5, "
                + "NULL, -1.9, -2.5, -4.3";
        String[] vals2Array = proc.splitValue(vals2, ",");
        double[] vals2Arr = new double[vals2Array.length];

        for (int c = 0; c < vals1Array.length; c++) {
            vals1Arr[c] = Double.parseDouble(vals1Array[c]);

        }
        values.add(vals1Arr);
        for (int c = 0; c < vals2Array.length; c++) {

            if (vals2Array[c].trim().equals("NULL")) {
                vals2Arr[c] = MathHelper.NULL_VALUE;
            } else {
                vals2Arr[c] = Double.parseDouble(vals2Array[c]);
            }
        }
        values.add(vals2Arr);

        int[] colors = new int[]{ColorUtil.GREEN, ColorUtil.BLUE};
        PointStyle[] styles = new PointStyle[]{PointStyle.CIRCLE,
            PointStyle.DIAMOND,};
        XYMultipleSeriesRenderer renderer = buildRenderer(colors, styles);
        int len = renderer.getSeriesRendererCount();

        for (int m = 0; m < len; m++) {
            ((XYSeriesRenderer) renderer.getSeriesRendererAt(m))
                    .setFillPoints(true);
        }
        setChartSettings(renderer, "Sensor temperature", "Hour",
                "Celcius degrees", x.get(0)[0].getTime(),
                x.get(0)[HOURS - 1].getTime(), -5, 30, ColorUtil.LTGRAY,
                ColorUtil.LTGRAY);

        int strWidth = smallFont.stringWidth("00:00:00 PM") / 2;
        int numXLabels = Display.getInstance()
                .getDisplayWidth() / (strWidth + 20);
        renderer.setXLabels(numXLabels);
        renderer.setYLabels(20);
        renderer.setShowGrid(true);
        renderer.setXLabelsAlign(Component.CENTER);
        renderer.setYLabelsAlign(Component.RIGHT);
        
        //create chart view with dataset & renderer
        TimeChart chart = new TimeChart(buildDateDataset(titles, x, values),
        renderer);
        
        return wrap("h:mm:a", getDesc(), new ChartComponent(chart), prevForm);
    }

}
