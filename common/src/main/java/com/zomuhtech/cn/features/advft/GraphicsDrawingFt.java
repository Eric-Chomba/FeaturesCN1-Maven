/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zomuhtech.cn.features.advft;

import com.codename1.components.ScaleImageLabel;
import com.codename1.components.ToastBar;
import com.codename1.io.FileSystemStorage;
import com.codename1.ui.Button;
import com.codename1.ui.Component;
import static com.codename1.ui.Component.CENTER;
import com.codename1.ui.Container;
import com.codename1.ui.Display;
import com.codename1.ui.Font;
import com.codename1.ui.FontImage;
import com.codename1.ui.Form;
import com.codename1.ui.layouts.BorderLayout;
import static com.codename1.ui.layouts.BorderLayout.NORTH;
import com.codename1.ui.layouts.GridLayout;
import com.zomuhtech.cn.features.procs.Proc;
import com.codename1.ui.Graphics;
import com.codename1.ui.Image;
import com.codename1.ui.Stroke;
import com.codename1.ui.TextField;
import com.codename1.ui.Transform;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.plaf.Style;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.geom.GeneralPath;
import com.codename1.ui.geom.Rectangle;
import com.codename1.ui.geom.Shape;
import com.codename1.ui.layouts.FlowLayout;
import com.codename1.ui.util.ImageIO;
import com.javieranton.PhotoCropper;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 *
 * @author Eric
 */
public class GraphicsDrawingFt extends Form {

    Form form, prevForm;
    Proc proc;
    ArrayList<Button> btnArr;

    public GraphicsDrawingFt(Form form) {

        this.prevForm = form;
        this.btnArr = new ArrayList<>();
        proc = new Proc();
        Display.getInstance().callSerially(() -> {
            createUI();
        });
        //super("Rect Rot");
        //this.addComponent(new RectangleComponent());
    }

    public void createUI() {
        form = proc.getForm("Graphics & Drawing", prevForm);
        form.setLayout(new BorderLayout());

        form.add(CENTER, getForm1());
        //form.addComponent(CENTER, new DrawingCanvas());

        Button btnBg = new Button("BG Painting", "btnNav");
        btnArr.add(btnBg);
        btnBg.addActionListener(e -> {
            proc.changeBtnUIID(btnArr, btnBg);
            form.add(CENTER, getForm1());
            form.revalidate();
        });

        Button btnGP = new Button("Glass Pane", "btnNav");
        btnArr.add(btnGP);
        btnGP.addActionListener(ev -> {
            proc.changeBtnUIID(btnArr, btnGP);
            //form.add(CENTER, getForm2());
            //ToastBar.showInfoMessage("Coming soon");
            showForm2();
        });

        Button btn2D = new Button("2D Drawing", "btnNav");
        btnArr.add(btn2D);
        btn2D.addActionListener(ev -> {
            proc.changeBtnUIID(btnArr, btn2D);
            form.add(CENTER, getForm3());
            form.revalidate();
        });

        Button btnAC = new Button("Analog Clock", "btnNav");
        btnArr.add(btnAC);
        btnAC.addActionListener(ev -> {
            proc.changeBtnUIID(btnArr, btnAC);
            form.add(CENTER, FlowLayout.encloseCenterMiddle(getForm4()));
            //form.add(CENTER, getForm4());
            form.revalidate();
        });

        Button btnSC = new Button("Shape Clipping", "btnNav");
        btnArr.add(btnSC);
        btnSC.addActionListener(ev -> {
            proc.changeBtnUIID(btnArr, btnSC);
            //ToastBar.showInfoMessage("Coming soon");
            //form.add(CENTER, getForm5());
            //form.revalidate();
            showForm5();
        });

        Button btnCS = new Button("Coordinate Sys", "btnNav");
        btnArr.add(btnCS);
        btnCS.addActionListener(ev -> {
            proc.changeBtnUIID(btnArr, btnCS);
            form.add(CENTER, getForm6());
            form.revalidate();
        });

        //selected by default
        proc.changeBtnUIID(btnArr, btnBg);

        Container cnt = new Container(new GridLayout(2, 3));
        cnt.add(btnBg).add(btnGP).add(btn2D).add(btnAC).add(btnSC).add(btnCS);

        form.add(NORTH, cnt);
        form.show();
    }

    private Form getForm1() {

        Form form1 = proc.getInputForm();
        form1.setLayout(new BorderLayout());

        form1.add(CENTER, new Component() {

            @Override
            public void paint(Graphics g) {
                g.setColor(0xff0000); //red

                //paint screen in red
                g.fillRect(getX(), getY(), getWidth(), getHeight());

                //Draw text at top in white
                g.setColor(0xffffff);

                int size;
                if (Display.getInstance().isTablet()) {
                    if (Display.getInstance().getDeviceDensity() < 40) {
                        size = 5;
                    } else {
                        size = 8;
                    }
                } else {
                    size = 4;
                }

                Font font = Font.createTrueTypeFont("native:MainLight",
                        "native:MainLight").derive(Display.getInstance()
                                .convertToPixels(size), Font.STYLE_PLAIN);
                
                g.setFont(font);
                
                g.drawString("Codename One Programming - Android, iOS, Windows "
                        + "phone apps etc", getX(), getY());
            }
        });

        return form1;
    }

    private void showForm2() {
        Style s = UIManager.getInstance().getComponentStyle("Label");
        s.setFgColor(0xff0000);
        s.setBgTransparency(0);

        Image imgWarn = FontImage.createMaterial(FontImage.MATERIAL_WARNING, s).
                toImage();

        TextField tf = new TextField("Value Field");
        tf.getAllStyles().setMarginUnit(Style.UNIT_TYPE_DIPS);
        tf.getAllStyles().setMargin(5, 5, 5, 5);
        form.add(CENTER, FlowLayout.encloseCenterMiddle(tf));

        form.setGlassPane((g, rect) -> {
            int x = tf.getAbsoluteX() + tf.getWidth();
            int y = tf.getAbsoluteY();
            x -= imgWarn.getWidth() / 2;
            y += (tf.getHeight() / 2 - imgWarn.getHeight() / 2);
            g.drawImage(imgWarn, x, y);
        });
        form.revalidate();
    }

    private Form getForm2() {

        Form form2 = proc.getInputForm();

        Style s = UIManager.getInstance().getComponentStyle("Label");
        s.setFgColor(0xff0000);
        s.setBgTransparency(0);

        Image imgWarn = FontImage.createMaterial(FontImage.MATERIAL_WARNING, s).
                toImage();

        TextField tf = new TextField("Value Field");
        tf.getAllStyles().setMarginUnit(Style.UNIT_TYPE_DIPS);
        tf.getAllStyles().setMargin(5, 5, 5, 5);
        form2.add(tf);

        form2.setGlassPane((g, rect) -> {
            int x = tf.getAbsoluteX() + tf.getWidth();
            int y = tf.getAbsoluteY();
            x -= imgWarn.getWidth() / 2;
            y += (tf.getHeight() / 2 - imgWarn.getHeight() / 2);
            g.drawImage(imgWarn, x, y);
        });

        return form2;
    }

    private Form getForm3() {

        Form form3 = proc.getInputForm();

        form3.addComponent(new DrawingCanvas());
        Button btn = new Button("Extract", "btn");
        form3.addAll(btn);

        btn.addActionListener(e -> {
            Image screenShotImg = Image.createImage(form3.getWidth(),
                    form3.getHeight());
            form3.revalidate();
            form3.setVisible(true);
            form3.paintComponent(screenShotImg.getGraphics(), true);

            String imgFile = FileSystemStorage.getInstance().getAppHomePath()
                    + "graphics-screenshot.png";
            proc.printLine("ImgFile " + imgFile);

            try (OutputStream os = FileSystemStorage.getInstance()
                    .openOutputStream(imgFile);) {
                ImageIO.getImageIO().save(screenShotImg, os, ImageIO.FORMAT_PNG, 1);

            } catch (IOException ex) {
            }

            PhotoCropper.cropImage(screenShotImg, 192, 500, 500, croppedImg -> {

                form3.add(new ScaleImageLabel(croppedImg));
                form3.revalidate();

            }, "Crop your image", "Warning message image is too small");
        });
        return form3;
    }

    public class DrawingCanvas extends Component {

        //Form dForm = new Form();
        GeneralPath path = new GeneralPath();
        int strokeColor = 0x0000ff;
        int strokeWidth = 10;

        /*public DrawingCanvas(Form form) {
            this.dForm = form;
        }*/
        @Override
        protected Dimension calcPreferredSize() {
            return new Dimension(250, 500);
            //return new Dimension(getAbsoluteX(), getAbsoluteY());
        }

        @Override
        protected void paintBackground(Graphics g) {
            super.paintBackground(g);
            Stroke stroke = new Stroke(strokeWidth, Stroke.CAP_BUTT,
                    Stroke.JOIN_ROUND, 1f);

            g.setColor(strokeColor);
            //Draw the shape
            g.drawShape(path, stroke);
            //dForm.add(this);
        }

        @Override
        public void pointerPressed(int x, int y) {
            addPoint(x - getParent().getAbsoluteX(),
                    y - getParent().getAbsoluteY());
        }

        private float lastX = -1;
        private float lastY = -1;
        private boolean odd = true;

        public void addPoint(float x, float y) {
            if (lastX == -1) {
                //this is the first point dont draw line yet
                path.moveTo(x, y);
            } else {
                //path.lineTo(x, y);
                float controlX = odd ? lastX : x;
                float controlY = odd ? y : lastY;
                path.quadTo(controlX, controlY, x, y);
            }
            odd = !odd;
            lastX = x;
            lastY = y;

            repaint();
        }

    }

    private Form getForm4() {

        Form form4 = proc.getInputForm();

//        AnalogClock clock = new AnalogClock();
//        form4.addComponent(clock);
//        clock.start();
        AnalogClock2 clock2 = new AnalogClock2();
        form4.addComponent(clock2);
        clock2.start();

        return form4;
    }

    public class AnalogClock extends Component {

        @Override
        public Dimension calcPreferredSize() {
            return new Dimension(800, 800);
        }

        GeneralPath ticksPath = new GeneralPath();

        @Override
        public void paintBackground(Graphics g) {
            double padding = 5;

            //clock radius
            double r = Math.min(getWidth(), getHeight()) / 2 - padding;

            //center point
            double cX = getX() + getWidth() / 2;
            double cY = getY() + getHeight() / 2;

            //Tick styles
            int tickLen = 10; //short tick
            int medTickLen = 30; //at 5 min intervals
            int longTickLen = 50; //at the quarters
            int tickColor = 0xCCCCCC;
            Stroke tickStroke = new Stroke(2f, Stroke.CAP_BUTT,
                    Stroke.JOIN_ROUND, 1f);

            //Draw tick for each sec 1-60
            for (int j = 1; j <= 60; j++) {
                //default tick len is short
                int len = tickLen;
                if (j % 15 == 0) {
                    //Longest tick on quarters - every 15 ticks
                    len = longTickLen;
                } else if (j % 5 == 0) {
                    //Medium ticks on 5's - every 5 ticks
                    len = medTickLen;
                }

                double dj = (double) j; //convert tick num to double

                //Get angle from 12 0'clock to this tick (radians)
                double angleFrom12 = dj / 60.0 * 2.0 * Math.PI;

                //Get angle from 3 0'clock to this tick
                double angleFrom3 = Math.PI / 2.0 - angleFrom12;

                //Move to the outer edge of the circle
                ticksPath.moveTo((float) (cX + Math.cos(angleFrom3) * r),
                        (float) (cY - Math.sin(angleFrom3) * r));

                //Draw line inward along radius for length of the tick mark
                ticksPath.lineTo((float) (cX + Math.cos(angleFrom3) * (r - len)),
                        (float) (cY - Math.sin(angleFrom3) * (r - len)));
            }

            g.setColor(tickColor);
            g.drawShape(ticksPath, tickStroke);

            //Draw numbers
            for (int k = 1; k <= 12; k++) {
                //Cal width & height strings for proper entry
                String numStr = "" + k;
                int charWidth = g.getFont().stringWidth(numStr);
                int charHeight = g.getFont().getHeight();

                double dk = (double) k;

                //Cal position along clock edge where numbers are drawn
                //Get angle from 12'0 clock to this tick(radian)
                double angleFrom12 = dk / 12.0 * 2.0 * Math.PI;

                //Get the angle from 3'0 clock to this tick
                double angleFrom3 = Math.PI / 2.0 - angleFrom12;

                //Get difference btn no. position & clock center
                int tx = (int) (Math.cos(angleFrom3) * (r - longTickLen));
                int ty = (int) (-Math.sin(angleFrom3) * (r - longTickLen));

                //For 6 & 12 shift no. slightly to be more even
                if (k == 6) {
                    ty -= charHeight / 2;
                } else if (k == 12) {
                    ty += charHeight / 2;
                }

                //Translate graphics context by delta btn center & no. position
                g.translate(tx, ty);

                //Draw number at clock center
                g.drawString(numStr, (int) cX - charWidth / 2,
                        (int) cY - charHeight / 2);

                //undo translation
                g.translate(-tx, -ty);
            }

            //Drawing hands
            GeneralPath secPath = new GeneralPath();
            secPath.moveTo((float) cX, (float) cY);
            secPath.lineTo((float) cX, (float) cY - (r - medTickLen));

            //Translate slightly to overlap the center
            Shape tranSecHand = secPath.createTransformedShape(
                    Transform.makeTranslation(0f, 5));

            //Cal angle of sec hand
            Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
            double sec = (double) (calendar.get(Calendar.SECOND));
            double secAngle = sec / 60.0 * 2.0 * Math.PI;

            //Get absolute center position of the clock
            double absCX = getAbsoluteX() + cX - getX();
            double absCY = getAbsoluteY() + cY - getY();

            //rotate sec hand
            g.rotate((float) secAngle, (int) absCX, (int) absCY);

            g.setColor(0xff0000);
            g.drawShape(tranSecHand,
                    new Stroke(2f, Stroke.CAP_BUTT, Stroke.JOIN_BEVEL, 1f));
            g.resetAffine();

            //Draw min hand
            GeneralPath minPath = new GeneralPath();
            minPath.moveTo((float) cX, (float) cY);
            minPath.lineTo((float) cX + 6, (float) cY);
            minPath.lineTo((float) cX + 2, (float) (cY - (r - tickLen)));
            minPath.lineTo((float) cX - 2, (float) (cY - (r - tickLen)));
            minPath.lineTo((float) cX - 6, (float) cY);
            minPath.closePath();

            //Translate min hand slightly down to overlap center
            Shape tranMinHand = minPath.createTransformedShape(
                    Transform.makeTranslation(0f, 5));

            double min = (double) (calendar.get(Calendar.MINUTE))
                    + (double) (calendar.get(Calendar.SECOND)) / 60.0;
            double minAngle = min / 60.0 * 2.0 * Math.PI;

            //Rotate & draw min hand
            g.rotate((float) minAngle, (int) absCX, (int) absCY);
            g.setColor(0x3399ff);
            g.fillShape(tranMinHand);
            g.resetAffine();

            //Draw hr hand
            GeneralPath hrPath = new GeneralPath();
            hrPath.moveTo((float) cX, (float) cY);
            hrPath.lineTo((float) cX + 4, (float) cY);
            hrPath.lineTo((float) cX + 1, (float) (cY - (r - longTickLen) * 0.75));
            hrPath.lineTo((float) cX - 1, (float) (cY - (r - longTickLen) * 0.75));
            hrPath.lineTo((float) cX - 4, (float) cY);
            hrPath.closePath();

            //Translate min hand slightly down to overlap center
            Shape tranHrHand = hrPath.createTransformedShape(
                    Transform.makeTranslation(0f, 5));

            double hr = (double) (calendar.get(Calendar.HOUR_OF_DAY) % 12)
                    + (double) (calendar.get(Calendar.MINUTE)) / 60.0;
            double hrAngle = hr / 12.0 * 2.0 * Math.PI;

            //Rotate & draw hr hand
            g.rotate((float) hrAngle, (int) absCX, (int) absCY);
            g.setColor(0x3399ff);
            g.fillShape(tranHrHand);
            g.resetAffine();
        }

        Date currentTime = new Date();
        long lastRenderedTime = 0;

        @Override
        public boolean animate() {
            if (System.currentTimeMillis() / 1000 != lastRenderedTime / 1000) {
                currentTime.setTime(System.currentTimeMillis());
                return true;
            }
            return false;
        }

        public void start() {
            getComponentForm().registerAnimated(this);
        }

        public void stop() {
            getComponentForm().deregisterAnimated(this);
        }
    }

    public class AnalogClock2 extends Component {

        @Override
        public Dimension calcPreferredSize() {
            return new Dimension(800, 800);
        }

        Date currentTime = new Date();

        @Override
        public void paintBackground(Graphics g) {
            super.paintBackground(g);

            boolean oldAntialised = g.isAntiAliased();
            g.setAntiAliased(true);
            double padding = 10;

            //clock radius
            double r = Math.min(getWidth(), getHeight()) / 2 - padding;

            //center point
            double cX = getX() + getWidth() / 2;
            double cY = getY() + getHeight() / 2;

            //Tick styles
            int tickLen = 10; //short tick
            int medTickLen = 30; //at 5 min intervals
            int longTickLen = 50; //at the quarters
            int tickColor = 0x0000FF; //0xCCCCCC 0x0000FF 0x3399ff
            Stroke tickStroke = new Stroke(2f, Stroke.CAP_BUTT,
                    Stroke.JOIN_ROUND, 1f);

            GeneralPath ticksPath = new GeneralPath();

            //Draw tick for each sec 1-60
            for (int j = 1; j <= 60; j++) {
                //default tick len is short
                int len = tickLen;
                if (j % 15 == 0) {
                    //Longest tick on quarters - every 15 ticks
                    len = longTickLen;
                } else if (j % 5 == 0) {
                    //Medium ticks on 5's - every 5 ticks
                    len = medTickLen;
                }

                double dj = (double) j; //convert tick num to double

                //Get angle from 12 0'clock to this tick (radians)
                double angleFrom12 = dj / 60.0 * 2.0 * Math.PI;

                //Get angle from 3 0'clock to this tick
                double angleFrom3 = Math.PI / 2.0 - angleFrom12;

                //Move to the outer edge of the circle
                ticksPath.moveTo(
                        (float) (cX + Math.cos(angleFrom3) * r),
                        (float) (cY - Math.sin(angleFrom3) * r)
                );

                //Draw line inward along radius for length of the tick mark
                ticksPath.lineTo(
                        (float) (cX + Math.cos(angleFrom3) * (r - len)),
                        (float) (cY - Math.sin(angleFrom3) * (r - len))
                );
            }

            g.setColor(tickColor);
            g.drawShape(ticksPath, tickStroke);

            g.setColor(0x000000);
            //Draw numbers
            for (int k = 1; k <= 12; k++) {
                //Cal width & height strings for proper entry
                String numStr = "" + k;
                int charWidth = g.getFont().stringWidth(numStr);
                int charHeight = g.getFont().getHeight();

                double dk = (double) k;

                //Cal position along clock edge where numbers are drawn
                //Get angle from 12'0 clock to this tick(radian)
                double angleFrom12 = dk / 12.0 * 2.0 * Math.PI;

                //Get the angle from 3'0 clock to this tick
                double angleFrom3 = Math.PI / 2.0 - angleFrom12;

                //Get difference btn no. position & clock center
                int tx = (int) (Math.cos(angleFrom3) * (r - longTickLen));
                int ty = (int) (-Math.sin(angleFrom3) * (r - longTickLen));

                //For 6 & 12 shift no. slightly to be more even
                if (k == 6) {
                    ty -= charHeight / 2;
                } else if (k == 12) {
                    ty += charHeight / 2;
                }

                //Translate graphics context by delta btn center & no. position
                g.translate(tx, ty);

                //Draw number at clock center
                g.drawString(numStr, (int) cX - charWidth / 2,
                        (int) cY - charHeight / 2);

                //undo translation
                g.translate(-tx, -ty);
            }

            //Drawing hands
            Calendar calendar = Calendar.getInstance(TimeZone.getDefault());

            GeneralPath secPath = new GeneralPath();
            secPath.moveTo((float) cX, (float) cY);
            secPath.lineTo((float) cX, (float) (cY - (r - medTickLen)));

            //Translate slightly to overlap the center
            Shape tranSecHand = secPath.createTransformedShape(
                    Transform.makeTranslation(0f, 5, 0));

            //Cal angle of sec hand
            double sec = (double) (calendar.get(Calendar.SECOND));
            double secAngle = sec / 60.0 * 2.0 * Math.PI;

            //Get absolute center position of the clock
            double absCX = getAbsoluteX() + cX - getX();
            double absCY = getAbsoluteY() + cY - getY();

            //rotate sec hand
            g.rotate((float) secAngle, (int) absCX, (int) absCY);

            g.setColor(0xff0000);
            g.drawShape(tranSecHand,
                    new Stroke(2f, Stroke.CAP_BUTT, Stroke.JOIN_BEVEL, 1f));
            g.resetAffine();

            //Draw min hand
            GeneralPath minPath = new GeneralPath();
            minPath.moveTo((float) cX, (float) cY);
            minPath.lineTo((float) cX + 6, (float) cY);
            minPath.lineTo((float) cX + 2, (float) (cY - (r - tickLen)));
            minPath.lineTo((float) cX - 2, (float) (cY - (r - tickLen)));
            minPath.lineTo((float) cX - 6, (float) cY);
            minPath.closePath();

            //Translate min hand slightly down to overlap center
            Shape tranMinHand = minPath.createTransformedShape(
                    Transform.makeTranslation(0f, 5, 0));

            double min = (double) (calendar.get(Calendar.MINUTE))
                    + (double) (calendar.get(Calendar.SECOND)) / 60.0;
            double minAngle = min / 60.0 * 2.0 * Math.PI;

            //Rotate & draw min hand
            g.rotate((float) minAngle, (int) absCX, (int) absCY);
            g.setColor(0x3399ff);
            g.fillShape(tranMinHand);
            g.resetAffine();

            //Draw hr hand
            GeneralPath hrPath = new GeneralPath();
            hrPath.moveTo((float) cX, (float) cY);
            hrPath.lineTo((float) cX + 4, (float) cY);
            hrPath.lineTo((float) cX + 1, (float) (cY - (r - longTickLen) * 0.75));
            hrPath.lineTo((float) cX - 1, (float) (cY - (r - longTickLen) * 0.75));
            hrPath.lineTo((float) cX - 4, (float) cY);
            hrPath.closePath();

            //Translate min hand slightly down to overlap center
            Shape tranHrHand = hrPath.createTransformedShape(
                    Transform.makeTranslation(0f, 5, 0));

            double hr = (double) (calendar.get(Calendar.HOUR_OF_DAY) % 12)
                    + (double) (calendar.get(Calendar.MINUTE)) / 60.0;
            double hrAngle = hr / 12.0 * 2.0 * Math.PI;

            //Rotate & draw hr hand
            g.rotate((float) hrAngle, (int) absCX, (int) absCY);
            g.setColor(0x3399ff);
            g.fillShape(tranHrHand);
            g.resetAffine();

            g.setAntiAliased(oldAntialised);
        }

        //Date currentTime = new Date();
        long lastRenderedTime = 0;

        @Override
        public boolean animate() {
            if (System.currentTimeMillis() / 1000 != lastRenderedTime / 1000) {
                currentTime.setTime(System.currentTimeMillis());
                return true;
            }
            return false;
        }

        public void start() {
            getComponentForm().registerAnimated(this);
        }

        public void stop() {
            getComponentForm().deregisterAnimated(this);
        }
    }

    private void showForm5() {
        Image img = null;
        try {
            img = Image.createImage("/android.png");
        } catch (IOException e) {
            proc.printLine(e.getMessage());
        }

        final Image finalImg = img;

        //create 50 x 100 shape for easy scaling
        GeneralPath path = new GeneralPath();
        path.moveTo(20, 0);
        path.lineTo(30, 0);
        path.lineTo(30, 100);
        path.lineTo(20, 100);
        path.lineTo(20, 15);
        path.lineTo(5, 40);
        path.lineTo(5, 25);
        path.lineTo(20, 0);

        Stroke stroke = new Stroke(0.5f, Stroke.CAP_ROUND, Stroke.JOIN_ROUND, 4);

        form.getContentPane().getUnselectedStyle()
                .setBgPainter((Graphics g, Rectangle rect) -> {
                    g.setColor(0xff);
                    float widthRatio = ((float) rect.getWidth()) / 50f;
                    float heightRatio = ((float) rect.getHeight()) / 100f;
                    g.scale(widthRatio, heightRatio);
                    g.translate((int) (((float) rect.getX()) / widthRatio),
                            (int) (((float) rect.getY()) / heightRatio));
                    g.setClip(path);
                    g.setAntiAliased(true);
                    g.drawImage(finalImg, 0, 0, 50, 100);
                    g.setClip(path.getBounds());
                    g.drawShape(path, stroke);
                    g.translate(-(int) (((float) rect.getX()) / widthRatio),
                            -(int) (((float) rect.getY()) / heightRatio));
                    g.resetAffine();

                });
        form.revalidate();
    }

    private Form getForm5() {

        Form form5 = proc.getInputForm();
        //form5.addComponent(new ShapeClip(form5));

        Image img = null;
        try {
            img = Image.createImage("/android.png");
        } catch (IOException e) {
            proc.printLine(e.getMessage());
        }

        final Image finalImg = img;

        //create 50 x 100 shape for easy scaling
        GeneralPath path = new GeneralPath();
        path.moveTo(20, 0);
        path.lineTo(30, 0);
        path.lineTo(30, 100);
        path.lineTo(20, 100);
        path.lineTo(20, 15);
        path.lineTo(5, 40);
        path.lineTo(5, 25);
        path.lineTo(20, 0);

        Stroke stroke = new Stroke(0.5f, Stroke.CAP_ROUND, Stroke.JOIN_ROUND, 4);

        form5.getContentPane().getUnselectedStyle()
                .setBgPainter((Graphics g, Rectangle rect) -> {
                    g.setColor(0xff);
                    float widthRatio = ((float) rect.getWidth()) / 50f;
                    float heightRatio = ((float) rect.getHeight()) / 100f;
                    g.scale(widthRatio, heightRatio);
                    g.translate((int) (((float) rect.getX()) / widthRatio),
                            (int) (((float) rect.getY()) / heightRatio));
                    g.setClip(path);
                    g.setAntiAliased(true);
                    g.drawImage(finalImg, 0, 0, 50, 100);
                    g.setClip(path.getBounds());
                    g.drawShape(path, stroke);
                    g.translate(-(int) (((float) rect.getX()) / widthRatio),
                            -(int) (((float) rect.getY()) / heightRatio));
                    g.resetAffine();

                });
        return form5;
    }

    public class ShapeClip extends Component {

        Form form5;

        public ShapeClip(Form form5) {
            this.form5 = form5;
        }

        @Override
        public Dimension calcPreferredSize() {
            return new Dimension(800, 800);
        }

        GeneralPath ticksPath = new GeneralPath();

        @Override
        public void paintBackground(Graphics g_) {
            Image img = null;
            try {
                img = Image.createImage("/android.png");
            } catch (IOException e) {
                proc.printLine(e.getMessage());
            }

            final Image finalImg = img;

            //create 50 x 100 shape for easy scaling
            GeneralPath path = new GeneralPath();
            path.moveTo(20, 0);
            path.lineTo(30, 0);
            path.lineTo(30, 100);
            path.lineTo(20, 100);
            path.lineTo(20, 15);
            path.lineTo(5, 40);
            path.lineTo(5, 25);
            path.lineTo(20, 0);

            Stroke stroke = new Stroke(0.5f, Stroke.CAP_ROUND, Stroke.JOIN_ROUND, 4);

            form5.getContentPane().getUnselectedStyle()
                    .setBgPainter((Graphics g, Rectangle rect) -> {
                        g.setColor(0xff);
                        float widthRatio = ((float) rect.getWidth()) / 50f;
                        float heightRatio = ((float) rect.getHeight()) / 100f;
                        g.scale(widthRatio, heightRatio);
                        g.translate((int) (((float) rect.getX()) / widthRatio),
                                (int) (((float) rect.getY()) / heightRatio));
                        g.setClip(path);
                        g.setAntiAliased(true);
                        g.drawImage(finalImg, 0, 0, 50, 100);
                        g.setClip(path.getBounds());
                        g.drawShape(path, stroke);
                        g.translate(-(int) (((float) rect.getX()) / widthRatio),
                                -(int) (((float) rect.getY()) / heightRatio));
                        g.resetAffine();

                    });

        }
    }

    private Form getForm6() {

        Form form6 = proc.getInputForm();

        //form6.addComponent(new RectangleComponent());
        for (int i = 0; i < 10; i++) {
            form6.addComponent(new RectangleComponent());
        }

        return form6;
    }

    public class RectangleComponent extends Component {

        @Override
        protected Dimension calcPreferredSize() {
            return new Dimension(250, 250);
            //return new Dimension(getAbsoluteX(), getAbsoluteY());
        }

        @Override
        public void paint(Graphics g) {
            g.setColor(0x0000ff);

            //g.rotate((float) (Math.PI / 40));
            //g.rotate((float) (Math.PI / 4.0), getAbsoluteX(), getAbsoluteY());
            g.rotate((float) (Math.PI / 4.0), getAbsoluteX() + getWidth() / 2,
                    getAbsoluteY() + getHeight() / 2);

            g.drawRect(getX() + 5, getY() + 5, getWidth() - 10, getHeight() - 10);

            //g.rotate(-(float) (Math.PI / 4.0));
            //g.rotate(-(float) (Math.PI / 4.0), getAbsoluteX(), getAbsoluteY());
            g.rotate(-(float) (Math.PI / 4.0), getAbsoluteX() + getWidth() / 2,
                    getAbsoluteY() + getHeight() / 2);
        }

        //get coordinates of pressed points on screen
        @Override
        public void pointerPressed(int x, int y) {
            String point = (x - getParent().getAbsoluteX()) + " "
                    + (y - getParent().getAbsoluteY());
            ToastBar.showInfoMessage("Point " + point);
        }
    }

}
