/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zomuhtech.cn.features.advft;

import com.codename1.components.SpanLabel;
import com.codename1.sensors.SensorListener;
import com.codename1.sensors.SensorsManager;
import com.codename1.ui.Button;
import static com.codename1.ui.Component.CENTER;
import com.codename1.ui.Container;
import com.codename1.ui.Display;
import com.codename1.ui.Form;
import com.codename1.ui.layouts.BorderLayout;
import static com.codename1.ui.layouts.BorderLayout.SOUTH;
import com.codename1.ui.layouts.GridLayout;
import com.codename1.util.MathUtil;
import com.zomuhtech.cn.features.procs.Proc;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author Eric
 */
public class SensorsFt extends Form {

    Form form, prevForm;
    Proc proc;
    ArrayList<Button> btnArr;
    double calibration = Double.NaN;
   // private final long lastUpdate;
    float appliedAcceleration = 0, currentAcceleration = 0, velocity = 0;
    Date lastUpdateDate;

    public SensorsFt(Form form) {

        this.prevForm = form;
        this.btnArr = new ArrayList<>();
        this.lastUpdateDate = new Date(System.currentTimeMillis());
        //this.lastUpdate = System.currentTimeMillis();
        proc = new Proc();

        Display.getInstance().callSerially(() -> {
            createUI();
        });

    }

    public void createUI() {

        form = proc.getForm("Sensors", prevForm);
        form.setLayout(new BorderLayout());

        Button btnAcc = new Button("Accelerometer", "btnNav");
        btnArr.add(btnAcc);
        btnAcc.addActionListener(e -> {
            proc.changeBtnUIID(btnArr, btnAcc);
            form.add(CENTER, getForm1());
            form.revalidate();
        });

        Button btnGyro = new Button("Gyroscope", "btnNav");
        btnArr.add(btnGyro);
        btnGyro.addActionListener(ev -> {
            proc.changeBtnUIID(btnArr, btnGyro);
            form.add(CENTER, getForm2());
            form.revalidate();
        });

        Button btnMagnet = new Button("Magnetism", "btnNav");
        btnArr.add(btnMagnet);
        btnMagnet.addActionListener(ev -> {
            proc.changeBtnUIID(btnArr, btnMagnet);
            form.add(CENTER, getForm3());
            form.revalidate();
        });

        Container cnt = new Container(new GridLayout(1, 3));
        cnt.addAll(btnAcc, btnGyro, btnMagnet);

        form.add(SOUTH, cnt);
        form.show();
    }

    private Form getForm1() {

        Form form1 = proc.getInputForm();

        SpanLabel lbl = new SpanLabel();
        form1.add(lbl);

        try {
            //default sensor speed
            SensorsManager accelerometer = SensorsManager
                    .getSensorsManager(SensorsManager.TYPE_ACCELEROMETER);
            //custom sensor speed (receive 10 samples/sec(10hz)=100000ms=1000000/10)
//        SensorsManager accelerometer = SensorsManager
//                .getSensorsManager(SensorsManager.TYPE_ACCELEROMETER, 100000);

            //register listener
            if (accelerometer != null) {
                accelerometer.registerListener(new SensorListener() {
                    
                    @Override
                    public void onSensorChanged(long timeStamp, float x, float y,
                            float z) {
                         /**
                     * x - acceleration force on x axis including gravity in
                     * m/seconds squared y - acceleration force on y axis
                     * including gravity in m/seconds squared z - acceleration
                     * force on z axis including gravity in m/seconds squared
                     */
                        
                        String data = "Timestamp: " + timeStamp + "\nX: " + x
                                + "\nY: " + y + "\nZ: " + z;

                        //double a=0.0;
                       double a = Math.sqrt(MathUtil.pow(x, 2) + MathUtil.pow(y, 2)
                               + MathUtil.pow(z, 2));


                        if (calibration == Double.NaN) {
                            calibration = a;
                        } else {
                            //updateVelocity();
                            //calculate how long this acceleration has been 
                            //applied
                            Date timeNow = new Date(System.currentTimeMillis());
                            long timeDelta = timeNow.getTime() - lastUpdateDate.getTime();
                            lastUpdateDate.setTime(timeNow.getTime());

                            //calculate the change in velocity at the current 
                            //acceleration since the last update
                            float deltaVelocity = appliedAcceleration * (timeDelta / 1000);
                            appliedAcceleration = currentAcceleration;

                            //Add the velocity change to the current velocity
                            velocity += deltaVelocity;

                            final double mph = (Math.round(100 * velocity / 1.6 * 3.6)) / 100;

                            data = data + "\n\nSpeed: " + velocity + " m/s  " + mph + " m/h";
                            proc.printLine(data);
                            lbl.setText(data);

                            currentAcceleration = (float) a;
                        }
                        form1.revalidate();

                    }

                });

            }
        } catch (Exception e) {
            lbl.setText(e.getMessage());
            form1.revalidate();
        }
        return form1;
    }

    private Form getForm2() {

        Form form2 = proc.getInputForm();

        SpanLabel lbl = new SpanLabel();
        form2.add(lbl);

        try {
            //measure angular velocity
            SensorsManager gyroscope = SensorsManager
                    .getSensorsManager(SensorsManager.TYPE_GYROSCOPE);
            //custom sensor speed (receive 10 samples/sec(10hz)=100000ms=1000000/10)
//        SensorsManager accelerometer = SensorsManager
//                .getSensorsManager(SensorsManager.TYPE_ACCELEROMETER, 100000);

            //register listener
            if (gyroscope != null) {
                gyroscope.registerListener(new SensorListener() {
                    @Override
                    /**
                     * x - rate of rotation along x axis in radians/second
                     * (rad/s) y - rate of rotation along y axis in
                     * radians/second (rad/s) z - rate of rotation along z axis
                     * in radians/second (rad/s)
                     */
                    public void onSensorChanged(long timeStamp, float x, float y,
                            float z) {
                        String data = "Timestamp: " + timeStamp + "\nX: " + x
                                + "\nY: " + y + "\nZ:" + z;
                        proc.printLine(data);
                        lbl.setText(data);
                        
                        form2.revalidate();
                    }

                });

            }
        } catch (Exception e) {
            lbl.setText(e.getMessage());
            form2.revalidate();
        }
        return form2;
    }

    private Form getForm3() {

        Form form3 = proc.getInputForm();

        SpanLabel lbl = new SpanLabel();
        form3.add(lbl);

        try {
            //default sensor speed
            SensorsManager magnet = SensorsManager
                    .getSensorsManager(SensorsManager.TYPE_MAGNETIC);
            //custom sensor speed (receive 10 samples/sec(10hz)=100000ms=1000000/10)
//        SensorsManager accelerometer = SensorsManager
//                .getSensorsManager(SensorsManager.TYPE_ACCELEROMETER, 100000);

            //register listener
            if (magnet != null) {
                magnet.registerListener(new SensorListener() {
                    @Override
                    public void onSensorChanged(long timeStamp, float x, float y,
                            float z) {
                        String data = "Timestamp: " + timeStamp + "\nX: " + x
                                + "\nY: " + y + "\nZ:" + z;
                        proc.printLine(data);
                        lbl.setText(data);
                        form3.revalidate();
                    }

                });

            }
        } catch (Exception e) {
            lbl.setText(e.getMessage());
            form3.revalidate();
        }
        return form3;
    }
}
