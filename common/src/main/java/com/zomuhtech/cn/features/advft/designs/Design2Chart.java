/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zomuhtech.cn.features.advft.designs;

import com.codename1.charts.ChartComponent;
import com.codename1.charts.renderers.DefaultRenderer;
import com.codename1.charts.util.ColorUtil;
import com.codename1.charts.views.DoughnutChart;
import com.codename1.ui.Component;
import com.codename1.ui.Form;
import com.codename1.ui.layouts.BoxLayout;
import com.zomuhtech.cn.features.advft.charts_visual.AbstractDemoChart;
import com.zomuhtech.cn.features.procs.Proc;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Eric
 */
public class Design2Chart extends AbstractDemoChart {

    Form form;
    Proc proc;

    public String getDesc() {
        return "Pie Chart - The budget per project for several years";
    }

    public Design2Chart(Form prevForm) {
        form = new Form();
        proc = new Proc();
        //form.getAllStyles().setBgColor(0X3399ff); //0xff0000 0X3399ff
        //form.getAllStyles().setBgTransparency(255);
        form.getToolbar().hideToolbar();
        form.setLayout(BoxLayout.y());

        Component cmp = getDoughnutChart(prevForm);
        cmp.getAllStyles().setBgColor(0Xffffff); //0x0 - black
        form.addComponent(cmp);
    }

    public Form getForm() {
        return form;
    }

    private Form getDoughnutChart(Form prevForm) {

        List<double[]> values = new ArrayList<>();

        //Outer values
        //String vals1 = "12, 14, 11, 10, 19";
         String vals1 = "20, 5, 7, 6";
        String[] vals1Arr = proc.splitValue(vals1, ",");
        double[] val1Arr = new double[vals1Arr.length];

        //Inner values
        String vals2 = "10, 9, 14, 20, 11";
        String[] vals2Arr = proc.splitValue(vals2, ",");
        double[] val2Arr = new double[vals2Arr.length];

        for (int j = 0; j < vals1Arr.length; j++) {
            val1Arr[j] = Double.parseDouble(vals1Arr[j]);
            val2Arr[j] = Double.parseDouble(vals2Arr[j]);
        }
        values.add(val1Arr);
        //values.add(val2Arr);

        List<String[]> titles = new ArrayList<>();
        //add outer titles
        titles.add(new String[]{"Income", "Bills", "Payments", "Transfers"});
        //add inner titles
        //titles.add(new String[]{"Project1", "Project2", "Project3", "Project4",
            //"Project5"});

        int[] colors = new int[]{ColorUtil.BLUE,
            ColorUtil.MAGENTA, ColorUtil.YELLOW, ColorUtil.CYAN};

        //get & set default renderer
        DefaultRenderer renderer = buildCategoryRenderer(colors);
        renderer.setApplyBackgroundColor(true);
        //renderer.setBackgroundColor(ColorUtil.rgb(222, 222, 200));
        renderer.setBackgroundColor(proc.white);
        renderer.setLabelsColor(proc.darkBlue);

        //create chart view with dataset(multiple category series) & renderer
        DoughnutChart chart = new DoughnutChart(
                buildStatementMultipleCategorySeries("Statement", titles, values),
                renderer);

        //add chart to chart cmp
        ChartComponent cmp = new ChartComponent(chart);

        return wrapStatement("Doughnut Chart", getDesc(), cmp, prevForm);

    }
}
