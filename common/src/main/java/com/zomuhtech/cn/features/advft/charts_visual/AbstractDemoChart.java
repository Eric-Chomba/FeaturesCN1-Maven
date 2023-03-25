/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zomuhtech.cn.features.advft.charts_visual;

/**
 *
 * @author Eric
 */
import com.codename1.charts.models.CategorySeries;
import com.codename1.charts.models.MultipleCategorySeries;
import com.codename1.charts.models.TimeSeries;
import com.codename1.charts.models.XYMultipleSeriesDataset;
import com.codename1.charts.models.XYSeries;
import com.codename1.charts.renderers.DefaultRenderer;
import com.codename1.charts.renderers.SimpleSeriesRenderer;
import com.codename1.charts.renderers.XYMultipleSeriesRenderer;
import com.codename1.charts.renderers.XYSeriesRenderer;
import com.codename1.charts.views.PointStyle;
import com.codename1.components.SpanLabel;
import com.codename1.ui.Component;
import com.codename1.ui.Display;
import com.codename1.ui.Font;
import com.codename1.ui.Form;
import com.codename1.ui.Graphics;
import com.codename1.ui.Image;
import com.codename1.ui.Label;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.FlowLayout;
import com.zomuhtech.cn.features.procs.Proc;
import java.util.List;
import java.util.Date;

public class AbstractDemoChart {
//public class AbstractDemoChart implements IDemoChart {

    private boolean drawOnMutableImg;
    Font smallFont = Font.createSystemFont(Font.FACE_SYSTEM, Font.SIZE_SMALL,
            Font.STYLE_PLAIN);
    Font medFont = Font.createSystemFont(Font.FACE_SYSTEM, Font.SIZE_MEDIUM,
            Font.STYLE_PLAIN);
    Font largeFont = Font.createSystemFont(Font.FACE_SYSTEM, Font.SIZE_LARGE,
            Font.STYLE_PLAIN);
    Proc proc = new Proc();

    int blue = 0x15E7FF;
    int darkBlue = 0X3399ff;
    int black = 0x0;
    int white = 0xffffff;

    /*Build XY muliple dataset using provided values
    *
    * @param titles the series titles
    * @param xValues the values for x axis
    * @param yValues the values for y axis
    * @return the XY multiple dataset
     */
    protected XYMultipleSeriesDataset buildDataset(String[] titles,
            List<double[]> xValues, List<double[]> yValues) {

        //Dataset or model - contains data displayed in chart
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        addXYSeries(dataset, titles, xValues, yValues, 0);
        return dataset;
    }

    public void addXYSeries(XYMultipleSeriesDataset dataset, String[] titles,
            List<double[]> xValues, List<double[]> yValues, int scale) {

        int length = titles.length;
        for (int j = 0; j < length; j++) { //loop 4 times (title array length)
            //create new XYSeries
            XYSeries series = new XYSeries(titles[j], scale);
            //get x&y values items(double[]) from x&yValues arrays
            double[] xVal = xValues.get(j);
            double[] yVal = yValues.get(j);
            int seriesLen = xVal.length;

            for (int k = 0; k < seriesLen; k++) { //loop 12 times(xArray length)
                //get x&y axis values from x&y axis array & add to series
                series.add(xVal[k], yVal[k]);
            }
            //add series to dataset
            dataset.addSeries(series);
        }
    }

    /**
     * Build an XY multiple series renderer
     *
     * @param colors the series rendering colors
     * @param styles the series point style
     * @return XY multiple series renderers
     *
     */
    protected XYMultipleSeriesRenderer buildRenderer(int[] colors,
            PointStyle[] styles) {
        XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
        setRenderer(renderer, colors, styles);
        return renderer;
    }

    protected void setRenderer(XYMultipleSeriesRenderer renderer, int[] colors,
            PointStyle[] styles) {
        renderer.setAxisTitleTextSize(smallFont.getHeight());
        renderer.setChartTitleTextSize(smallFont.getHeight());
        renderer.setLabelsTextSize(smallFont.getHeight() / 2);
        renderer.setLegendTextSize(smallFont.getHeight() / 2);
        renderer.setPointSize(5f);
        //set margins top, left, bottom, right
        renderer.setMargins(new int[]{medFont.getHeight(), medFont.getHeight(),
            15, medFont.getHeight()});
        int length = colors.length;

        for (int m = 0; m < length; m++) {
            //create XYSeriesRenderer, set lines color & point styles
            XYSeriesRenderer r = new XYSeriesRenderer();
            r.setColor(colors[m]);
            r.setPointStyle(styles[m]);
            //add XYSeriesRenderer to XYMultipleSeriesRenderer
            renderer.addSeriesRenderer(r);
        }
    }

    /**
     * Set few of series renderer settings
     *
     * @param renderer the renderer to set the properties to
     * @param title the chart title
     * @param xTitle title for X axis
     * @param yTitle title for Y axis
     * @param xMin the minimum value on X axis
     * @param xMax the maximum value in X axis
     * @param yMin the minimum value on Y axis
     * @param yMax the maximum value in Y axis
     * @param axesColor the axes color
     * @param lblColors the labels color *
     */
    protected void setChartSettings(XYMultipleSeriesRenderer renderer,
            String title, String xTitle, String yTitle, double xMin, double xMax,
            double yMin, double yMax, int axesColor, int lblColors) {

        renderer.setChartTitle(title);
        renderer.setXTitle(xTitle);
        renderer.setYTitle(yTitle);
        renderer.setXAxisMin(xMin);
        renderer.setXAxisMax(xMax);
        renderer.setYAxisMin(yMin);
        renderer.setYAxisMax(yMax);
        renderer.setAxesColor(axesColor);
        renderer.setLabelsColor(lblColors);
    }

    /**
     * Build an XY multiple time dataset using provided values
     *
     * @param titles the series titles
     * @param xValues values for x axis
     * @param yValues values for y axis
     * @return the XY multiple time dataset
     */
    protected XYMultipleSeriesDataset buildDateDataset(String[] titles,
            List<Date[]> xValues, List<double[]> yValues) {

        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        int len = titles.length;

        for (int j = 0; j < len; j++) { //loop 2 times
            //create time series
            TimeSeries series = new TimeSeries(titles[j]);
            //get x&y array items from x&y values arrays
            Date[] xV = xValues.get(j);
            double[] yV = yValues.get(j);
            int seriesLen = xV.length;

            for (int k = 0; k < seriesLen; k++) { // loop 24 times(each array's len)
                //add x & y values to series
                series.add(xV[k], yV[k]);
            }

            //add series to dataset
            dataset.addSeries(series);
        }
        return dataset;
    }

    /**
     * Build category series using provided values
     *
     * @param title the series titles
     * @param values the values
     * @return the category series
     *
     */
    protected CategorySeries buildCategoryDataset(String title,
            double[] values) {

        //create category series
        CategorySeries series = new CategorySeries(title);
        int k = 0;
        for (double value : values) {
            //add labels & values to series
            series.add("Project " + ++k, value);
        }
        return series;
    }

    /**
     * Build multiple category series using provided values
     *
     * @param title the series title
     * @param titles the series titles
     * @param values the values
     * @return category series
     */
    protected MultipleCategorySeries buildMultipleCategorySeries(String title,
            List<String[]> titles, List<double[]> values) {

        //create multiple category series
        MultipleCategorySeries series = new MultipleCategorySeries(title);
        int k = 0;
        for (double[] value : values) {
            //add year, titles array & value array
            series.add(2007 + k + "", titles.get(k), value);
            k++;
        }
        return series;
    }

    protected MultipleCategorySeries buildStatementMultipleCategorySeries(String title,
            List<String[]> titles, List<double[]> values) {

        //create multiple category series
        MultipleCategorySeries series = new MultipleCategorySeries(title);
        int k = 0;
        for (double[] value : values) {
            //add year, titles array & value array
            //series.add("", titles.get(k), value);
            series.add(titles.get(k), value);
            k++;
        }
        return series;
    }

    /**
     * Build category renderer using provided colors
     *
     * @param colors the colors
     * @return the category renderer
     */
    protected DefaultRenderer buildCategoryRenderer(int[] colors) {
        //create & set default renderer
        DefaultRenderer renderer = new DefaultRenderer();
        renderer.setLabelsTextSize(smallFont.getHeight() / 2);
        renderer.setLegendTextSize(smallFont.getHeight() / 2);
        renderer.setMargins(new int[]{medFont.getHeight(), medFont.getHeight(),
            medFont.getHeight(), medFont.getHeight()});

        for (int color : colors) {
            //create simple series renderer & add to default renderer
            SimpleSeriesRenderer r = new SimpleSeriesRenderer();
            r.setColor(color);
            renderer.addSeriesRenderer(r);
        }
        return renderer;
    }

    /**
     * Build a multiple series dataset using provided values
     *
     * @param titles the series titles
     * @param values the values
     * @return the XY multiple bar dataset
     */
    protected XYMultipleSeriesDataset buildBarDataset(String[] titles,
            List<double[]> values) {

        //create xy multiple series dataset
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        int len = titles.length;

        for (int j = 0; j < len; j++) { // loop 2 times (titles array length)
            //create category series
            CategorySeries series = new CategorySeries(titles[j]);
            //get array items from values array 
            double[] v = values.get(j);
            int seriesLen = v.length;

            for (int k = 0; k < seriesLen; k++) { //loop 12 times(array item len)
                //add items from array item to series
                series.add(v[k]);
            }
            //add series to dataset
            dataset.addSeries(series.toXYSeries());
        }
        return dataset;
    }

    /**
     * Build a bar multiple series renderer using provided colors
     *
     * @param colors the series renderer colors
     * @return the bar multiple series renderer
     */
    protected XYMultipleSeriesRenderer buildBarRenderer(int[] colors) {
        XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
        renderer.setAxisTitleTextSize(smallFont.getHeight() / 2);
        renderer.setChartTitleTextFont(smallFont);
        renderer.setLabelsTextSize(smallFont.getHeight() / 2);
        renderer.setLegendTextSize(smallFont.getHeight() / 2);
        int len = colors.length;

        for (int j = 0; j < len; j++) {
            XYSeriesRenderer r = new XYSeriesRenderer();
            //set bars colors
            r.setColor(colors[j]);
            renderer.addSeriesRenderer(r);
        }
        return renderer;
    }

    protected Form wrap(String title, String desc, Component cmp, Form prevForm) {
        //customize chart cmp color
        //cmp.getAllStyles().setBgColor(0xff0000); //0xff0000 0X3399ff
        //cmp.getAllStyles().setBgTransparency(255);
        //Form f = new Form(title);
        Form f = proc.getForm(title, prevForm);
        //f.setLayout(new BorderLayout());
        f.setLayout(BoxLayout.y());
        f.setScrollableY(true);
        SpanLabel lblDesc = new SpanLabel(desc, "lblInput");
        lblDesc.setTextBlockAlign(Component.CENTER);
        //f.add(BorderLayout.NORTH, lblDesc);
        f.add(lblDesc);

        if (isDrawOnMutableImg()) {
            int width = Display.getInstance().getDisplayWidth();
            int height = Display.getInstance().getDisplayHeight();
            Image img = Image.createImage((int) (width * 0.8),
                    (int) (height * 0.8), 0x0);
            Graphics g = img.getGraphics();
            cmp.setWidth((int) (width * 0.8));
            cmp.setHeight((int) (height * 0.8));
            cmp.paint(g);
            //f.addComponent(BorderLayout.CENTER, new Label(img));
            f.addComponent(new Label(img));
        } else {
            //f.addComponent(BorderLayout.CENTER, cmp);
            f.addComponent(cmp);
        }
        return f;
    }

    protected Form wrapStatement(String title, String desc, Component cmp, Form prevForm) {
        //customize chart cmp color
        Form f = proc.getForm(title, prevForm);
        f.getToolbar().hideToolbar();
        f.setLayout(BoxLayout.y());
        f.setScrollableY(true);
        //SpanLabel lblDesc = new SpanLabel(desc, "lblInput");
        //lblDesc.setTextBlockAlign(Component.CENTER);
        //f.add(BorderLayout.NORTH, lblDesc);
        //f.add(lblDesc);

        //if (isDrawOnMutableImg()) {
        //int width = Display.getInstance().getDisplayWidth();
        int width = 500;
        //int height = Display.getInstance().getDisplayHeight();
        int height = 500;
        Image img = Image.createImage((int) (width * 0.8),
                (int) (height * 0.8), 0x0);
        Graphics g = img.getGraphics();
        cmp.setWidth((int) (width * 0.8));
        cmp.setHeight((int) (height * 0.8));
        cmp.paint(g);
        //f.addComponent(BorderLayout.CENTER, new Label(img));
        f.addComponent(FlowLayout.encloseCenterMiddle(new Label(img)));
        /*} else {
            //f.addComponent(BorderLayout.CENTER, cmp);
            f.addComponent(FlowLayout.encloseCenter(cmp));
        }*/
        return f;
    }

    public void setDrawOnMutableImg(boolean b) {
        this.drawOnMutableImg = b;
    }

    public boolean isDrawOnMutableImg() {
        return this.drawOnMutableImg;
    }

    /**
     * Build a multiple category series using provided values
     *
     * @param title the series title
     * @param titles the series titles
     * @param values the values
     * @return the category series
     *
     * protected MultipleCategorySeries buildMultipleCategoryDataset(String
     * title, List<String[]> titles, List<double[]> values) {
     *
     * //create multiple category MultipleCategorySeries series = new
     * MultipleCategorySeries(title); int k = 0; for (double[] value : values) {
     * series.add(2007 + k + "", titles.get(k), value); k++; } return series; }
     */
    /*@Override
    public String getName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getDesc() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Form execute() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }*/
}
