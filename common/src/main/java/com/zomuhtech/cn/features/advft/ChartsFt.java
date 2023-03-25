/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zomuhtech.cn.features.advft;

import com.codename1.charts.ChartComponent;
import com.codename1.ui.Display;
import com.codename1.ui.Form;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.layouts.BorderLayout;
import com.zomuhtech.cn.features.procs.Proc;
import com.codename1.charts.util.ColorUtil;
import com.codename1.charts.renderers.DefaultRenderer;
import com.codename1.charts.renderers.SimpleSeriesRenderer;
import com.codename1.charts.views.PieChart;
import com.codename1.charts.models.CategorySeries;
import com.codename1.ui.CheckBox;
import com.codename1.ui.Command;
import com.codename1.ui.List;
import com.codename1.ui.events.ActionListener;
import com.zomuhtech.cn.features.advft.charts_visual.*;

/**
 *
 * @author Eric
 */
public class ChartsFt extends Form {

    Form form, prevForm, chartForm;
    Proc proc;
    boolean drawOnMutableImages;
    List lstMenus;

    class ListOption {

        Class<AbstractDemoChart> chartClass;
        String name;

        ListOption(Class cls, String name) {
            this.chartClass = cls;
            this.name = name;
        }

        @Override
        public String toString() {
            return this.name;
        }
    }

    ListOption[] options = new ListOption[]{
        new ListOption(AverageCubicTempChart.class, "Avg. Cubic Temperature"),
        new ListOption(BudgetDoughnutChart.class, "Budget Doughnut"),
        new ListOption(BudgetPieChart.class, "Budget Pie Chart"),
        new ListOption(CombinedTempChart.class, "Combined Temperature"),
        new ListOption(MultipleTempChart.class, "Multiple Temperature"),
        new ListOption(SalesBarChart.class, "Sales Bar Chart"),
        new ListOption(SalesComparisonChart.class, "Sales Comparison Chart"),
        new ListOption(SalesGrowthChart.class, "Sales Growth Chart"),
        new ListOption(SalesStackedBarChart.class, "Sales Stacked Bar Chart"),
        new ListOption(SensorValuesChart.class, "Sensor Values Chart"),
        new ListOption(TemperatureChart.class, "Temperature Chart"),
        new ListOption(TrigonometricFunctionsChart.class,
        "Trigonometric Functions Chart"),
        new ListOption(WeightDialChart.class, "Weight Dial Chart"),
        new ListOption(MetricsStackedBarChart.class,
        "Metrics Stacked Bar Chart"),
        new ListOption(ProjectStatusBubbleChart.class,
        "Project Status Bubble Chart"),
        new ListOption(ScatterChartFt.class, "Scatter Chart"),
        new ListOption(ChartsInBoxLayout.class, "Vertical Box Layout")
    };

    public ChartsFt(Form form) {
        this.prevForm = form;
        proc = new Proc();
        lstMenus = new List();
        Display.getInstance().callSerially(() -> {
            createUI();
        });
    }

    private void createUI() {
        form = proc.getForm("Charts & Visualization", prevForm);
        form.setLayout(new BorderLayout());

        for (ListOption option : options) {
            lstMenus.addItem(option);
        }

        lstMenus.addActionListener((ActionListener) (ActionEvent evt) -> {
            int selected = lstMenus.getCurrentSelected();
            ListOption opt = options[selected];
            Class cls = opt.chartClass;

            if (cls.equals(AverageCubicTempChart.class)) {
                chartForm = new AverageCubicTempChart(form).getForm();
            } else if (cls.equals(BudgetDoughnutChart.class)) {
                chartForm = new BudgetDoughnutChart(form).getForm();
            } else if (cls.equals(BudgetPieChart.class)) {
                chartForm = new BudgetPieChart(form).getForm();
            } else if (cls.equals(CombinedTempChart.class)) {
                chartForm = new CombinedTempChart(form).getForm();
            } else if (cls.equals(MultipleTempChart.class)) {
                chartForm = new MultipleTempChart(form).getForm();
            } else if (cls.equals(SalesBarChart.class)) {
                chartForm = new SalesBarChart(form).getForm();
            } else if (cls.equals(SalesComparisonChart.class)) {
                chartForm = new SalesComparisonChart(form).getForm();
            } else if (cls.equals(SalesGrowthChart.class)) {
                chartForm = new SalesGrowthChart(form).getForm();
            } else if (cls.equals(SalesStackedBarChart.class)) {
                chartForm = new SalesStackedBarChart(form).getForm();
            } else if (cls.equals(SensorValuesChart.class)) {
                chartForm = new SensorValuesChart(form).getForm();
            } else if (cls.equals(TemperatureChart.class)) {
                chartForm = new TemperatureChart(form).getForm();
            } else if (cls.equals(TrigonometricFunctionsChart.class)) {
                chartForm = new TrigonometricFunctionsChart(form).getForm();
            } else if (cls.equals(WeightDialChart.class)) {
                chartForm = new WeightDialChart(form).getForm();
            } else if (cls.equals(MetricsStackedBarChart.class)) {
                chartForm = new MetricsStackedBarChart(form).getForm();
            } /*pending*/ else if (cls.equals(ScatterChartFt.class)) {
                chartForm = new ScatterChartFt(form).getForm();
            } else if (cls.equals(ProjectStatusBubbleChart.class)) {
                chartForm = new ProjectStatusBubbleChart(form).getForm();
            } else if (cls.equals(ChartsInBoxLayout.class)) {
                chartForm = new ChartsInBoxLayout(form).getForm();
            }

            int cmpCount = chartForm.getComponentCount();

            for (int k = 0; k < cmpCount; k++) {
                if (cls.equals(MultipleTempChart.class)
                        || cls.equals(BudgetDoughnutChart.class)
                        || cls.equals(BudgetPieChart.class)) {
                    chartForm.getComponentAt(k).getStyle().setBgColor(0xffffff);
                    chartForm.getComponentAt(k).getStyle().setBgTransparency(255);
                } else {
                    chartForm.getComponentAt(k).getStyle().setBgColor(0x0);
                    chartForm.getComponentAt(k).getStyle().setBgTransparency(0xff);
                }
            }

            chartForm.setBackCommand(new Command("btnBack") {
                @Override
                public void actionPerformed(ActionEvent ev) {
                    form.showBack();
                }
            });
            chartForm.show();

            /* try {
            AbstractDemoChart demo = (AbstractDemoChart) cls.newInstance();
            demo.setDrawOnMutableImg(drawOnMutableImages);
            
            Form intent = demo.execute();
            if ("".equals(intent.getTitle())) {
            intent.setTitle("Hey " + demo.getName());
            } else {
            proc.printLine("Title =" + intent.getTitle());
            }
            
            Command cmd = new Command("Menu") {
            @Override
            public void actionPerformed(ActionEvent evt) {
            ChartsFt.this.showBack();
            }
            };
            
            intent.setBackCommand(cmd);
            intent.getStyle().setBgColor(0xfff);
            intent.getStyle().setBgTransparency(0xff);
            int cmpCount = intent.getComponentCount();
            
            for (int m = 0; m < cmpCount; m++) {
            intent.getComponentAt(m).getStyle().setBgColor(0x0);
            intent.getComponentAt(m).getStyle().setBgTransparency(0xff);
            }
            
            intent.show();
            
            } catch (InstantiationException e) {
            proc.printLine(e.getMessage());
            } catch (IllegalAccessException e) {
            proc.printLine(e.getMessage());
            }*/
        });

        final CheckBox chkMutableImg = new CheckBox("Render on Mutable Image");
        chkMutableImg.addActionListener((ActionListener) (ActionEvent evt) -> {
            drawOnMutableImages = chkMutableImg.isSelected();
        });

        form.addComponent(BorderLayout.NORTH, chkMutableImg);
        form.add(BorderLayout.CENTER, lstMenus);

        /*Container cnt = new Container(BoxLayout.x());

        Button btnPie = new Button("Pie", "btn");
        btnPie.addActionListener(e -> {
            form.add(CENTER, getPie());
            form.revalidate();
        });
        cnt.add(btnPie);

        Button btnLineG = new Button("Line graph", "btn");
        btnLineG.addActionListener(e -> {
            ToastBar.showErrorMessage("Unavalable");
            //form.add(CENTER, getPie());
        });
        cnt.add(btnLineG);

        form.add(SOUTH, cnt);*/
        form.show();
    }

    //Create renderer for specified colors
    private DefaultRenderer buildCategoryRenderer(int[] colors) {

        DefaultRenderer renderer = new DefaultRenderer();
        renderer.setLabelsTextSize(15);
        renderer.setLegendTextSize(15);
        renderer.setMargins(new int[]{20, 30, 15, 0});

        for (int color : colors) {
            SimpleSeriesRenderer series = new SimpleSeriesRenderer();
            series.setColor(color);
            renderer.addSeriesRenderer(series);
        }
        return renderer;
    }

    //Build category series
    protected CategorySeries buildCategoryDataset(String title,
            double[] values) {
        CategorySeries category = new CategorySeries(title);
        int k = 0;
        for (double value : values) {
            category.add("Project " + ++k, value);
        }
        return category;
    }

    private Form getPie() {

        double[] values = new double[]{12, 14, 11, 10, 19};
        //set up renderer
        int[] colors = new int[]{ColorUtil.BLUE, ColorUtil.GREEN,
            ColorUtil.MAGENTA, ColorUtil.YELLOW, ColorUtil.CYAN};
        DefaultRenderer renderer = buildCategoryRenderer(colors);
        renderer.setZoomButtonsVisible(true);
        renderer.setZoomEnabled(true);
        renderer.setChartTitleTextSize(20);
        renderer.setDisplayValues(true);
        renderer.setShowLabels(true);

        SimpleSeriesRenderer series = renderer.getSeriesRendererAt(0);
        series.setGradientEnabled(true);
        series.setGradientStart(0, ColorUtil.BLUE);
        series.setGradientStop(0, ColorUtil.GREEN);
        series.setHighlighted(true);

        //create chart
        PieChart chart = new PieChart(buildCategoryDataset("Budget", values),
                renderer);

        //Wrap chart in a component
        ChartComponent cmp = new ChartComponent(chart);
        Form f = new Form("Budget");
        f.setLayout(new BorderLayout());
        f.addComponent(CENTER, cmp);
        return f;
    }

}
