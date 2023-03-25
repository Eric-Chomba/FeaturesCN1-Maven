/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zomuhtech.cn.features.advft.charts_visual;

import com.codename1.charts.ChartComponent;
import com.codename1.charts.models.XYMultipleSeriesDataset;
import com.codename1.charts.models.XYValueSeries;
import com.codename1.charts.renderers.XYMultipleSeriesRenderer;
import com.codename1.charts.renderers.XYSeriesRenderer;
import com.codename1.charts.util.ColorUtil;
import com.codename1.charts.views.BubbleChart;
import com.codename1.ui.Component;
import com.codename1.ui.Form;
import com.codename1.ui.layouts.BoxLayout;

/**
 *
 * @author Eric
 */
public class ProjectStatusBubbleChart extends AbstractDemoChart {

    Form form;

    public String getDesc() {
        return "Bubble chart - the open & closed tickets";
    }

    public ProjectStatusBubbleChart(Form prevForm) {

        form = new Form();
        //form.getAllStyles().setBgColor(darkBlue); //0xff0000 0X3399ff
        //form.getAllStyles().setBgTransparency(255);
        form.getToolbar().hideToolbar();
        form.setLayout(BoxLayout.y());

        Component cmp = getProjectChart(prevForm);
        cmp.getAllStyles().setBgColor(black);
        form.addComponent(cmp);
    }

    public Form getForm() {
        return form;
    }

    private Form getProjectChart(Form prevForm) {

        XYMultipleSeriesDataset series = new XYMultipleSeriesDataset();

        XYValueSeries newTicketSeries = new XYValueSeries("New Tickets");
        
        /*newTicketSeries.add(1f, 2, 14);
        newTicketSeries.add(2f, 2, 12);
        newTicketSeries.add(3f, 2, 18);
        newTicketSeries.add(4f, 2, 5);
        newTicketSeries.add(5f, 2, 1);*/
        
        String newVals = "1f,2,14: 2f,2,12: 3f,2,18: 4f,2,5: 5f,2,1";
        String[] newValsArray = proc.splitValue(newVals, ":");

        for (String newVal : newValsArray) {
            String[] newValsArr = proc.splitValue(newVal, ",");
            newTicketSeries.add(Double.parseDouble(newValsArr[0]),
                    Double.parseDouble(newValsArr[1]),
                    Double.parseDouble(newValsArr[2]));
        }

        series.addSeries(newTicketSeries);

        XYValueSeries fixedTicketSeries = new XYValueSeries("Fixed Tickets");
        
        /*fixedTicketSeries.add(1f, 1, 7);
        fixedTicketSeries.add(2f, 1, 4);
        fixedTicketSeries.add(3f, 1, 18);
        fixedTicketSeries.add(4f, 1, 3);
        fixedTicketSeries.add(5f, 1, 1);*/
        
        String fixedVals = "1f,1,7: 2f,1,4: 3f,1,18: 4f,1,3: 5f,1,1";
        String[] fixedValsArray = proc.splitValue(fixedVals, ":");

        
        for (String fixedVal : fixedValsArray) {
           
            String[] fixedValsArr = proc.splitValue(fixedVal, ",");
            proc.printLine("" + Float.parseFloat(fixedValsArr[0]));

            fixedTicketSeries.add(Float.parseFloat(fixedValsArr[0]),
                    Double.parseDouble(fixedValsArr[1]),
                    Double.parseDouble(fixedValsArr[2]));
        }
        

        series.addSeries(fixedTicketSeries);

        XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer(2);
        renderer.setAxisTitleTextFont(medFont);
        renderer.setChartTitleTextFont(largeFont);
        renderer.setLabelsTextFont(medFont);
        renderer.setLegendTextFont(medFont);
        renderer.setMargins(new int[]{20, 30, 15, 0});

        XYSeriesRenderer newTicketRenderer = new XYSeriesRenderer();
        newTicketRenderer.setColor(ColorUtil.BLUE);
        renderer.addSeriesRenderer(newTicketRenderer);

        XYSeriesRenderer fixedTicketRenderer = new XYSeriesRenderer();
        fixedTicketRenderer.setColor(ColorUtil.GREEN);
        renderer.addSeriesRenderer(fixedTicketRenderer);

        setChartSettings(renderer, "Project work status", "Priority", "",
                0.5, 5.5, 0, 5, ColorUtil.GRAY, ColorUtil.LTGRAY);
        renderer.setXLabels(7);
        renderer.setYLabels(0);
        renderer.setShowGrid(false);

        BubbleChart chart = new BubbleChart(series, renderer);

        return wrap("Project tickets", getDesc(), new ChartComponent(chart),
                prevForm);

    }
}
