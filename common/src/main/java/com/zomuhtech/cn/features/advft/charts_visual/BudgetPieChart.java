/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zomuhtech.cn.features.advft.charts_visual;

import com.codename1.charts.ChartComponent;
import com.codename1.charts.models.CategorySeries;
import com.codename1.charts.models.SeriesSelection;
import com.codename1.charts.renderers.DefaultRenderer;
import com.codename1.charts.renderers.SimpleSeriesRenderer;
import com.codename1.charts.util.ColorUtil;
import com.codename1.charts.views.PieChart;
import com.codename1.ui.Component;
import com.codename1.ui.Form;
import com.codename1.ui.geom.Rectangle;
import com.codename1.ui.geom.Shape;
import com.codename1.ui.layouts.BoxLayout;

/**
 *
 * @author Eric
 */
public class BudgetPieChart extends AbstractDemoChart {

    Form form;

    public String getDesc() {
        return "Pie Chart - The budget per project for this year";
    }

    public BudgetPieChart(Form prevForm) {

        form = new Form();
       
        form.getToolbar().hideToolbar();
        form.setLayout(BoxLayout.y());

        Component cmp = getPieChart(prevForm);
        form.addComponent(cmp);
    }

    public Form getForm() {
        return form;
    }

    private Form getPieChart(Form prevForm) {

        String vals1 = "12, 14, 11, 10, 19";
        String[] vals1Arr = proc.splitValue(vals1, ",");
        double[] valArr = new double[vals1Arr.length];
        for (int j = 0; j < vals1Arr.length; j++) {
            valArr[j] = Double.parseDouble(vals1Arr[j]);
        }

        int[] colors = new int[]{ColorUtil.BLUE, ColorUtil.GREEN,
            ColorUtil.MAGENTA, ColorUtil.YELLOW, ColorUtil.CYAN};

        //build & set default renderer
        DefaultRenderer renderer = buildCategoryRenderer(colors);
        renderer.setZoomButtonsVisible(true);
        renderer.setZoomEnabled(true);
        renderer.setChartTitleTextFont(largeFont);
        //show or hide values in the chart true/false
        renderer.setDisplayValues(true);
        //show or hide labels in the chart true/false
        renderer.setShowLabels(true);
        renderer.setApplyBackgroundColor(true);
        renderer.setBackgroundColor(white);
        renderer.setLabelsColor(darkBlue);

        final CategorySeries seriesSet = buildCategoryDataset("Project Budget",
                valArr);
        //create chart view with seriessSet & renderer
        final PieChart chart = new PieChart(seriesSet, renderer);

        //implement pointer actions
        ChartComponent cmp = new ChartComponent(chart) {
            private boolean inDrag = false;

            //pointer Pressed - zooms the chart
            @Override
            public void pointerPressed(int x, int y) {
                inDrag = false;
                super.pointerPressed(x, y);
                //proc.printLine("pointer Pressed");
            }

            //drag the chart
            @Override
            public void pointerDragged(int x, int y) {
                inDrag = true;
                super.pointerDragged(x, y);
                //proc.printLine("pointer Dragged");
            }

            //release after zooming
            @Override
            protected void seriesReleased(SeriesSelection sel) {
                if (inDrag) {
                    //dont do this if it was a drag operation
                    //proc.printLine("dont do this");
                    return;
                }

                //proc.printLine("series Released");
                for (SimpleSeriesRenderer r : renderer.getSeriesRenderers()) {
                    //unhighlighted simple series renderer
                    r.setHighlighted(false);
                    //proc.printLine("SimpleSeriesRenderer unhighlighted");
                }

                SimpleSeriesRenderer r = renderer
                        .getSeriesRendererAt(sel.getPointIndex());
                //highlighted simple series renderer
                r.setHighlighted(true);
                //proc.printLine("SimpleSeriesRenderer highlighted");

                Shape shapeSeg = chart.getSegmentShape(sel.getPointIndex());
                Rectangle bounds = shapeSeg.getBounds();
                bounds = new Rectangle(bounds.getX() - 40,
                        bounds.getY() - 40,
                        bounds.getWidth() + 80,
                        bounds.getHeight() + 80
                );
                //zoom in
                this.zoomToShapeInChartCoords(bounds, 500);
                //proc.printLine("Zooming");
            }
        };

        cmp.setZoomEnabled(true);
        cmp.setPanEnabled(true);
        //cmp.getStyle().setBgColor(0xff0000);
        //cmp.getStyle().setBgTransparency(255);

        return wrap("Project Budget", getDesc(), cmp, prevForm);
    }
}
