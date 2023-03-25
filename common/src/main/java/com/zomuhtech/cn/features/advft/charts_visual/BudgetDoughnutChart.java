/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zomuhtech.cn.features.advft.charts_visual;

import com.codename1.charts.ChartComponent;
import com.codename1.charts.renderers.DefaultRenderer;
import com.codename1.charts.util.ColorUtil;
import com.codename1.charts.views.DoughnutChart;
import com.codename1.ui.Component;
import com.codename1.ui.Form;
import com.codename1.ui.layouts.BoxLayout;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Eric
 */
public class BudgetDoughnutChart extends AbstractDemoChart {

    Form form;

    public String getDesc() {
        return "Pie Chart - The budget per project for several years";
    }

    public BudgetDoughnutChart(Form prevForm) {
        form = new Form();
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
        String vals1 = "12, 14, 11, 10, 19";
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
        values.add(val2Arr);

        List<String[]> titles = new ArrayList<>();
        //add outer titles
        titles.add(new String[]{"P1", "P2", "P3", "P4", "P5"});
        //add inner titles
        titles.add(new String[]{"Project1", "Project2", "Project3", "Project4",
            "Project5"});

        int[] colors = new int[]{ColorUtil.BLUE, ColorUtil.GREEN,
            ColorUtil.MAGENTA, ColorUtil.YELLOW, ColorUtil.CYAN};

        //get & set default renderer
        DefaultRenderer renderer = buildCategoryRenderer(colors);
        renderer.setApplyBackgroundColor(true);
        //renderer.setBackgroundColor(ColorUtil.rgb(222, 222, 200));
        renderer.setBackgroundColor(white);
        renderer.setLabelsColor(darkBlue);

        //create chart view with dataset(multiple category series) & renderer
        DoughnutChart chart = new DoughnutChart(
                buildMultipleCategorySeries("Project Budget", titles, values),
                renderer);

        //add chart to chart cmp
        ChartComponent cmp = new ChartComponent(chart);

        return wrap("Doughnut Chart", getDesc(), cmp, prevForm);

    }
}
