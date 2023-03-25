/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zomuhtech.cn.features.advft.charts_visual;

import com.codename1.charts.ChartComponent;
import com.codename1.charts.renderers.XYMultipleSeriesRenderer;
import com.codename1.charts.renderers.XYSeriesRenderer;
import com.codename1.charts.renderers.XYSeriesRenderer.FillOutsideLine;
import com.codename1.charts.util.ColorUtil;
import com.codename1.charts.views.PointStyle;
import com.codename1.charts.views.TimeChart;
import com.codename1.ui.Component;
import com.codename1.ui.Form;
import com.codename1.ui.layouts.BoxLayout;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Eric
 */
public class SalesGrowthChart extends AbstractDemoChart {

    Form form;

    public String getDesc() {
        return "Time chart - The sales growth across several years";
    }

    public SalesGrowthChart(Form prevForm) {

        form = new Form();
        form.getToolbar().hideToolbar();
        form.setLayout(BoxLayout.y());

        Component cmp = getSalesGrowthChart(prevForm);
        cmp.getAllStyles().setBgColor(black);
        form.addComponent(cmp);
    }

    public Form getForm() {
        return form;
    }

    private Form getSalesGrowthChart(Form prevForm) {
        String[] titles = new String[]{"Sales growth January 1995 to "
            + "December 2000"};

        //create x values(dates)
        List<Date[]> dates = new ArrayList<>();

        /*Date[] dateValues = new Date[]{DateUtil.date(95, 0, 1), DateUtil.date(95, 3, 1),
            DateUtil.date(95, 6, 1), DateUtil.date(95, 9, 1), DateUtil.date(96, 0, 1),
            DateUtil.date(96, 3, 1), DateUtil.date(96, 6, 1), DateUtil.date(96, 9, 1),
            DateUtil.date(97, 0, 1), DateUtil.date(97, 3, 1), DateUtil.date(97, 6, 1),
            DateUtil.date(97, 9, 1), DateUtil.date(98, 0, 1), DateUtil.date(98, 3, 1),
            DateUtil.date(98, 6, 1), DateUtil.date(98, 9, 1), DateUtil.date(99, 0, 1),
            DateUtil.date(99, 3, 1), DateUtil.date(99, 6, 1), DateUtil.date(99, 9, 1),
            DateUtil.date(100, 0, 1), DateUtil.date(100, 3, 1), DateUtil.date(100, 6, 1),
            DateUtil.date(100, 9, 1), DateUtil.date(100, 11, 1)};*/
        
        String dateVals = "95,0,1: 95,3,1: 95,6,1: 95,9,1: 96,0,1: 96,3,1: 96,6,1:"
                + "96,9,1: 97,0,1: 97,3,1: 97,6,1: 97,9,1: 98,0,1: 98,3,1:"
                + "98,6,1: 98,9,1: 99,0,1: 99,3,1: 99,6,1: 99,9,1: 100,0,1:"
                + "100,3,1: 100,6,1: 100,9,1: 100,11,1";
        String[] datesArray = proc.splitValue(dateVals, ":");
        Date[] dateArr = new Date[datesArray.length];

        for (int c = 0; c < datesArray.length; c++) {
            String[] datesArr = proc.splitValue(datesArray[c].trim(), ",");
            dateArr[c] = DateUtil.date(Integer.parseInt(datesArr[0]),
                    Integer.parseInt(datesArr[1]),
                    Integer.parseInt(datesArr[2]));
        }

        dates.add(dateArr);

        //create y values in percentages 
        List<double[]> values = new ArrayList<>();

        String vals1 = "4.9, 5.3, 3.2, 4.5, 6.5, 4.7, 5.8, 4.3, 4, 2.3, -0.5, "
                + "-2.9, 3.2, 5.5, 4.6, 9.4, 4.3, 1.2, 0, 0.4, 4.5, 3.4, 4.5, "
                + "4.3, 4";
        String[] vals1Array = proc.splitValue(vals1, ",");
        double[] vals1Arr = new double[vals1Array.length];

        for (int c = 0; c < vals1Array.length; c++) {
            vals1Arr[c] = Double.parseDouble(vals1Array[c]);

        }
        values.add(vals1Arr);

        int[] colors = new int[]{ColorUtil.BLUE};
        PointStyle[] styles = new PointStyle[]{PointStyle.POINT};
        XYMultipleSeriesRenderer renderer = buildRenderer(colors, styles);
        setChartSettings(renderer, "Sales growth", "Date", "%",
                dateArr[0].getTime(), dateArr[dateArr.length - 1].getTime(),
                -4, 11, ColorUtil.GRAY, ColorUtil.LTGRAY);
        renderer.setYLabels(10);
        renderer.setXRoundedLabels(false);

        XYSeriesRenderer xyRenderer
                = (XYSeriesRenderer) renderer.getSeriesRendererAt(0);
        //set values above 0 fill color
        FillOutsideLine fill = new FillOutsideLine(FillOutsideLine.Type.BOUNDS_ABOVE);
        fill.setColor(ColorUtil.GREEN);
        xyRenderer.addFillOutsideLine(fill);
        //set values below 0 fill color
        fill = new FillOutsideLine(FillOutsideLine.Type.BOUNDS_BELOW);
        fill.setColor(ColorUtil.MAGENTA);
        xyRenderer.addFillOutsideLine(fill);
        //set values above 0 fill color
        fill = new FillOutsideLine(FillOutsideLine.Type.BOUNDS_ABOVE);
        fill.setColor(ColorUtil.argb(255, 0, 200, 100));
        fill.setFillRange(new int[]{10, 19});
        xyRenderer.addFillOutsideLine(fill);

        //create view with dataset & renderer
        TimeChart chart = new TimeChart(buildDateDataset(titles, dates, values),
                renderer);

        return wrap("Sales Growth", getDesc(), new ChartComponent(chart),
                prevForm);
    }

    public static class DateUtil {

        public static int getTimezoneOffset(Date date) {
            return 0;
        }

        public static Date date(int y, int m, int d) {
            Calendar c = Calendar.getInstance();
            c.set(Calendar.YEAR, y + 1900);
            c.set(Calendar.MONTH, m);
            c.set(Calendar.DAY_OF_MONTH, d);
            return c.getTime();
        }
    }
}
