/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zomuhtech.cn.features.advft.charts_visual;

import com.codename1.charts.ChartComponent;
import com.codename1.charts.renderers.XYMultipleSeriesRenderer;
import com.codename1.charts.util.ColorUtil;
import com.codename1.charts.views.LineChart;
import com.codename1.charts.views.PointStyle;
import com.codename1.ui.Component;
import com.codename1.ui.Display;
import com.codename1.ui.Form;
import com.codename1.ui.Graphics;
import com.codename1.ui.animations.Animation;
import com.codename1.ui.animations.Motion;
import com.codename1.ui.layouts.BoxLayout;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Eric
 */
public class TrigonometricFunctionsChart extends AbstractDemoChart {

    Form form;

    public String getDesc() {
        return "LineChart - graphical representation of sin & cos functions";
    }

    public TrigonometricFunctionsChart(Form prevForm) {

        form = new Form();
        form.getToolbar().hideToolbar();
        form.setLayout(BoxLayout.y());

        Component cmp = getTrigChart(prevForm);
        cmp.getAllStyles().setBgColor(black);
        form.addComponent(cmp);
    }

    public Form getForm() {
        return form;
    }

    private Form getTrigChart(Form prevForm) {
        
        String[] titles = new String[]{"sin", "cos"};
        List<double[]> x = new ArrayList<>();
        List<double[]> values = new ArrayList<>();
        int step = 4;
        int count = 360 / step + 1;
        
        //add x (sin) array
        x.add(new double[count]);
        //add x (cos) array
        x.add(new double[count]);
        
        double[] sinValues = new double[count];
        double[] cosValues = new double[count];
       

        for (int j = 0; j < count; j++) {
            int angle = j * step;
            //get x(sin) item & add value
            x.get(0)[j] = angle;
            //get x(cos) item & add value
            x.get(1)[j] = angle;
            
            double rAngle = Math.toRadians(angle);
            //add y(sin) value to y(sin) array
            sinValues[j] = Math.sin(rAngle);
            //add y(cos) value to y(cos) array
            cosValues[j] = Math.cos(rAngle);
        }
        //add y (sin) array
        values.add(sinValues);
        //add y (cos) array
        values.add(cosValues);
        
        int[] colors = new int[]{ColorUtil.BLUE, ColorUtil.CYAN};
        PointStyle[] styles = new PointStyle[]{PointStyle.POINT,
            PointStyle.POINT};
        final XYMultipleSeriesRenderer renderer = buildRenderer(colors, styles);
        setChartSettings(renderer, "Trigonometric functions", "X (in degrees)",
                "Y", 0, 360, -1, 1, ColorUtil.GRAY, ColorUtil.LTGRAY);

        int strWidth = smallFont.stringWidth("360") / 2;
        int numXLabels = Display.getInstance()
                .getDisplayWidth() / (strWidth + 20);
        renderer.setXLabels(numXLabels);
        renderer.setYLabels(10);
        renderer.setYAxisMin(0);
        renderer.setXAxisMax(50);

        //create motion (from x value, to x value, duration(seconds))
        final Motion m = Motion.createLinearMotion(0, 310, 10000);
        final LineChart chart = new LineChart(buildDataset(titles, x, values),
                renderer);
        final ChartComponent cmp = new ChartComponent(chart);
        Form out = wrap("Trigonometric Functions", getDesc(), cmp, prevForm);

        //register form animation
        out.registerAnimated(new Animation() {
            @Override
            public boolean animate() {
                if (m.isFinished()) {
                    return false;
                } else {
                    renderer.setXAxisMin(m.getValue());
                    renderer.setXAxisMax(m.getValue() + 50);
                    cmp.repaint();
                    return true;
                }
            }

            @Override
            public void paint(Graphics g) {
            }
        });
        m.start();
        return out;
    }

}
