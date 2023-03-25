/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zomuhtech.cn.features.advft.designs;

import com.codename1.ui.Button;
import static com.codename1.ui.Component.CENTER;
import com.codename1.ui.Container;
import com.codename1.ui.Display;
import com.codename1.ui.FontImage;
import com.codename1.ui.Form;
import com.codename1.ui.Graphics;
import com.codename1.ui.Image;
import static com.codename1.ui.Image.createImage;
import com.codename1.ui.Label;
import com.codename1.ui.Stroke;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.layouts.BorderLayout;
import static com.codename1.ui.layouts.BorderLayout.EAST;
import static com.codename1.ui.layouts.BorderLayout.NORTH;
import static com.codename1.ui.layouts.BorderLayout.SOUTH;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.FlowLayout;
import com.codename1.ui.layouts.LayeredLayout;
import com.codename1.ui.plaf.RoundRectBorder;
import com.codename1.ui.plaf.Style;
import com.codename1.ui.table.TableLayout;
import com.codename1.ui.util.UITimer;
import com.codename1.util.StringUtil;
import com.zomuhtech.cn.features.procs.Proc;
import java.io.IOException;

/**
 *
 * @author Eric
 */
public class Design5 extends Form {

    Form form, prevForm;
    Proc proc;
    Container cntFileSel;
    Label /*lblIconSel,*/ lblTitleSel, lblDescSel;
    Container cntIconSel;
    Button btnTime;
    Container cntTimePar, cntTime;
    int currentProgress = 0, nowPlayingMin = 0, nowPlayingSec = 0, remCount = 0,
            songProgress = 0, remDegrees = 0, totalSecCount = 0, progressDelay,
            extraCount = 0;

    String songs = "Taylor Swift:gorgeous:taylor_80.jpeg:3.05#"
            + "Avicii:without you:avicii_80.jpeg:4.53#Hailee Steinfeld:let me go:hailee_80.jpeg:2.34#"
            + "OneRepublic:start again:onerepublic_80.jpg:7.22#Ogoo:you are mine:ogoo_80.jpeg:4.12#"
            + "Dappy:good intention:dappy_80.jpeg:3.33#";

    int picWidth = 80, picHeight = 80;

    public Design5(Form form) {

        this.prevForm = form;
        proc = new Proc();

        Display.getInstance().callSerially(() -> {
            createUI();
        });
    }

    public void createUI() {

        form = proc.getDesignForm("5", "Media Player", prevForm, 0xF251358F, 0xF251358F);
        if (!proc.getDarkMode().equals("On")) {
            form.getAllStyles().setBgColor(0xF251358F);
        } else {
            form.getAllStyles().setBgColor(0x29293D);
        }
        form.setLayout(new BorderLayout());

        try {
            form.getToolbar()
                    .add(EAST, FlowLayout.encloseLeftMiddle(proc
                            .getUserImage(Image.createImage("/eric_40.jpg"))));
        } catch (IOException e) {
        }

        Container cntTop = new Container(BoxLayout.y(), "cntDsg5Top");
        cntTop.setScrollableY(true);
        cntTop.setScrollVisible(false);

        TableLayout tlGenre = new TableLayout(1, 1);
        Container cntGenrePar = new Container(tlGenre, "cntDsg5BorderPar");
        cntGenrePar.add(tlGenre.cc().widthPercentage(30), new Label("Popular Songs", "lblDsg5Files"));
        cntTop.add(cntGenrePar);

        String[] songsArr = proc.splitValue(songs, "#");

        for (String song : songsArr) {

            String[] trackArr = proc.splitValue(song, ":");

            if (trackArr.length > 3) {

                try {
                    TableLayout tlFile = new TableLayout(1, 3);
                    Container cntFile = new Container(tlFile, "cntDsg5File");

                    Container cntLbl = new Container(BoxLayout.y());

                    Image pic = Image.createImage("/" + trackArr[2]);

                    /*Container cntPicPar = new Container(new LayeredLayout());

                    cntPicPar.add(proc.getCustomBg("cntDsg5SqrCircleBg", pic.getWidth(),
                            pic.getHeight(), proc.white, 2, pic.getWidth() / 2));*/
                    //Image icon = proc.getSquareCircleIcon(pic);
                    //proc.printLine("Pic width=" + icon.getWidth() + " height=" + icon.getHeight());
                    Container cntPic = new Container(new BorderLayout(), "cntDsg5SqrCircle") {
                        @Override
                        public Dimension calcPreferredSize() {
                            return new Dimension(picWidth, picHeight);
                        }
                    };

                    Style s = cntPic.getAllStyles();
                    Stroke borderStroke = new Stroke(2, Stroke.CAP_SQUARE, Stroke.JOIN_MITER, 1);
                    int color;
                    if (proc.getDarkMode().equals("On")) {
                        color = proc.blueGray;
                    } else {
                        color = 0xF251358F;
                    }
                    s.setBorder(RoundRectBorder.create().strokeColor(color)
                            .strokeOpacity(255).stroke(borderStroke));

                    s.setBgImage(pic);

                    cntFile.add(tlFile.createConstraint().widthPercentage(30).heightPercentage(100),
                            FlowLayout.encloseCenterMiddle(cntPic));

                    cntLbl.add(new Label(trackArr[1], "lblFilesTitle"));
                    cntLbl.add(new Label(trackArr[0], "lblFiles"));
                    cntFile.add(tlFile.createConstraint().widthPercentage(55),
                            FlowLayout.encloseLeftMiddle(cntLbl));

                    Button btnMore = new Button(StringUtil.replaceAll(trackArr[3], ".", ":"), "lblFiles");
                    btnMore.addActionListener(e -> {

                        try {

//                            lblIconSel.setIcon(proc.getSquareCircleIcon(
//                                    createImage("/" + trackArr[2])).getIcon());
//                            cntIconSel = proc.getSquareCircleIcon(
//                                    createImage("/" + trackArr[2]));
                            /*cntIconSel.getAllStyles().setBgImage(
                                    proc.getSquareCircleIcon(createImage("/" + trackArr[2])));*/
                            Style selectedStyle = cntIconSel.getAllStyles();
                            Stroke selBorderStroke = new Stroke(2, Stroke.CAP_SQUARE, Stroke.JOIN_MITER, 1);
                            selectedStyle.setBorder(RoundRectBorder.create().strokeColor(proc.brown)
                                    .strokeOpacity(255).stroke(selBorderStroke));
                            selectedStyle.setBgImage(createImage("/" + trackArr[2]));

                            lblTitleSel.setText(trackArr[1]);
                            lblDescSel.setText(trackArr[0]);

                            String[] timeArr = proc.splitValue(trackArr[3], ".");
                            int seconds = Integer.parseInt(timeArr[0]) * 60
                                    + Integer.parseInt(timeArr[1]);
                            proc.setNowPlayingSec("" + seconds);
                            proc.printLine(" seconds=" + seconds);
                            int duration = seconds * 1000;
                            proc.printLine("duration=" + duration);

                            currentProgress = 0;
                            nowPlayingMin = 0;
                            nowPlayingSec = 0;

                            proc.setNowPlaying(trackArr[0] + ":" + trackArr[1]);
                            proc.printLine("NowPlaying=" + proc.getNowPlaying());

                            //setSongProgress("A", duration);
                            setSongProgress("C", duration);

                            cntFileSel.setVisible(true);
                            form.revalidate();

                        } catch (IOException ex) {
                        }
                    });
                    cntFile.add(tlFile.createConstraint().widthPercentage(15),
                            FlowLayout.encloseRightMiddle(btnMore));
                    cntFile.setLeadComponent(btnMore);

                    cntTop.add(cntFile);
                    TableLayout tlBorder = new TableLayout(1, 2);
                    Container cntBorderPar = new Container(tlBorder, "cntDsg5BorderPar");
                    Container cntBorder = new Container(BoxLayout.y(), "cntDsg5Border");
                    cntBorderPar.add(tlBorder.cc().widthPercentage(30), new Label("", "lblFiles"));
                    cntBorderPar.add(tlBorder.cc().widthPercentage(70), cntBorder);
                    cntTop.add(cntBorderPar);

                } catch (IOException e) {

                }
            }

        }

        form.add(CENTER, cntTop);

        Container cntBottomPar = new Container(BoxLayout.y(), "cntDsg5BottomPar");

        TableLayout tlFileSel = new TableLayout(1, 3);
        cntFileSel = new Container(tlFileSel, "cntDsg5FileSel");
//        lblIconSel = new Label("", "btnSubMenu");
        cntIconSel = new Container(new BorderLayout(), "cntDsg5SqrCircle") {
            @Override
            public Dimension calcPreferredSize() {
                return new Dimension(picWidth, picHeight);
            }
        };
        cntFileSel.add(tlFileSel.cc().widthPercentage(30).heightPercentage(100),
                FlowLayout.encloseCenterMiddle(cntIconSel));
        Container cntLbl = new Container(BoxLayout.y());
        lblTitleSel = new Label("", "lblFilesTitle");
        lblDescSel = new Label("", "lblFiles");
        cntLbl.addAll(lblTitleSel, lblDescSel);

        cntFileSel.add(tlFileSel.cc().widthPercentage(40),
                FlowLayout.encloseLeftMiddle(cntLbl));

        btnTime = new Button("", "lblFiles");
        cntTimePar = new Container(new BorderLayout());

        btnTime.addActionListener(e -> {
            proc.showToast("More", FontImage.MATERIAL_INFO_OUTLINE).show();
        });
        cntFileSel.add(tlFileSel.cc().widthPercentage(30),
                FlowLayout.encloseRightMiddle(/*btnTime*/cntTimePar));

        cntBottomPar.add(cntFileSel);
        cntFileSel.setVisible(false);

        Container cntBottom = new Container(new LayeredLayout(), "cntDsg5Bottom");

        TableLayout tl = new TableLayout(1, 3);
        Container cntBottomContent = new Container(tl, "cntDsg5BottomContent");

        Container cntMenu1 = new Container(new BorderLayout(), "cntDsg5Menu1");
        Button btnAudio = new Button(proc.customIcon(FontImage.MATERIAL_AUDIOTRACK, proc.darkBlue, 3),
                "btnDsg5Browse");
        btnAudio.addActionListener(e -> {
            proc.showToast("Browsing audio tracks", FontImage.MATERIAL_AUDIOTRACK).show();
        });
        cntMenu1.add(CENTER, FlowLayout.encloseCenterMiddle(btnAudio));
        cntMenu1.setLeadComponent(btnAudio);

        Container cntMenu2Par = new Container(new LayeredLayout(), "cntDsg5Menu2Par");

        TableLayout tlMenuBg = new TableLayout(2, 1);
        Container cntMenu2Bg = new Container(tlMenuBg, "cntDsg5Menu2Bg");
        Container cntMenu2BgTop = new Container(new BorderLayout(), "cntMenu2BgTop");
        cntMenu2Bg.add(tlMenuBg.cc().heightPercentage(40).widthPercentage(100), cntMenu2BgTop);
        cntMenu2Par.add(cntMenu2Bg);

        for (int y = 0; y <= 30; y++) {
            TableLayout tlMenuFg = new TableLayout(2, 1);
            Container cntMenu2Fg = new Container(tlMenuFg, "cntDsg5Menu2Fg");
            //Container cntMenu2 = new Container(new BorderLayout(), "cntDsg5Menu2");
            /*cntDsg5Menu2 {
    background: yellow;
    border-radius: 0mm 0mm 7mm 7mm;
    margin: 0mm;
            padding: 7mm 7mm 7mm 7mm;
    padding: 7mm 0mm 7mm 0mm;
    padding:  0mm 10mm 0mm 10mm;
    border: none;  
}*/

            int width = Display.getInstance().getDisplayWidth() * 20 / 100;
            //0xFFD700 0x8B8000 0xcccc00 0xCC7722 0xCD7F32 0xD27D2D
            Container cntMenu2 = proc.getCustomBg("cntSmoothCurve2", width, width,
                    proc.brown, 1, width);

            cntMenu2Fg.add(tlMenuFg.cc().heightPercentage(70).widthPercentage(100),
                    cntMenu2);
            cntMenu2Par.add(cntMenu2Fg);
        }

        Container cntMenu3 = new Container(new BorderLayout(), "cntDsg5Menu3");
        Button btnVideo = new Button(proc.customIcon(FontImage.MATERIAL_VIDEO_LIBRARY,
                proc.darkBlue, 3), "btnDsg5Browse");
        btnVideo.addActionListener(e -> {
            proc.showToast("Browsing video tracks", FontImage.MATERIAL_VIDEO_LIBRARY).show();
        });
        cntMenu3.add(CENTER, FlowLayout.encloseCenterMiddle(btnVideo));
        cntMenu3.setLeadComponent(btnVideo);

        cntBottomContent.add(tl.cc().widthPercentage(40).heightPercentage(100), cntMenu1);
        cntBottomContent.add(tl.cc().widthPercentage(20).heightPercentage(100), cntMenu2Par);
        cntBottomContent.add(tl.cc().widthPercentage(40).heightPercentage(100), cntMenu3);

        Container cntBottomFloat = new Container(new BorderLayout(), "cntDsg5BottomFloat");
        Button btnFloat = new Button(proc.customIcon(FontImage.MATERIAL_DOWNLOAD,
                proc.darkBlue, 3), "btnDsg5Float");
        btnFloat.addActionListener(e -> {
            proc.showToast("downloading tracks", FontImage.MATERIAL_DOWNLOAD).show();
        });
        cntBottomFloat.add(NORTH, FlowLayout.encloseCenter(btnFloat));

        cntBottom.addAll(cntBottomContent, FlowLayout.encloseCenter(cntBottomFloat));
        cntBottomPar.add(cntBottom);

        form.add(SOUTH, cntBottomPar);
        form.show();

    }

    private void setSongProgress(String direction, int maxDuration) {

        //Form formProgress = new Form(new BorderLayout());
        Form formProgress = new Form(new LayeredLayout());
        formProgress.getToolbar().hideToolbar();
        formProgress.setUIID("formDsgn5Progress");
        cntTime = createSongProgress(formProgress, direction, maxDuration);

        String sec = "" + nowPlayingSec;
        if (sec.length() == 1) {
            sec = "0" + sec;
        }
        String time = nowPlayingMin + ":" + sec;
        proc.printLine("NowPlayingTime=" + time);
        Label lblTime = new Label(time, "lblSummary");
        cntTime.add(lblTime);

        cntTimePar.add(CENTER, cntTime);
        cntTimePar.revalidate();
    }

    private Form createSongProgress(Form formProgress, String direction, int maxDuration) {

        Container cntPar = new Container(BoxLayout.y());
        Container cntMask = new Container(new LayeredLayout());

        Image roundMask = Image.createImage(70, 70, 0);
        Graphics gr = roundMask.getGraphics();
        gr.setColor(0xF251358F);

        if (direction.equals("A")) {
            gr.fillArc(0, 0, 70, 70, 120, currentProgress);
        } else if (direction.equals("C")) {
            gr.fillArc(0, 0, 70, 70, 60, currentProgress);
        }
        Object mask = roundMask.createMask();
        roundMask.applyMask(mask);
        Label lbl = new Label(roundMask);
        cntMask.add(FlowLayout.encloseCenterMiddle(lbl));

        Image roundMask2 = Image.createImage(60, 60, 0);
        Graphics gr2 = roundMask2.getGraphics();
        gr2.setColor(proc.brown);
        gr2.fillArc(0, 0, 60, 60, 0, 360);
        Object mask2 = roundMask2.createMask();
        roundMask2.applyMask(mask2);
        Label lbl2 = new Label(roundMask2);
        cntMask.addAll(FlowLayout.encloseCenterMiddle(lbl2));
        cntPar.add(cntMask);

        //formProgress.add(CENTER, cntPar);
        formProgress.add(cntPar);

        //int songProgress = 360 * 1000 / maxDuration; //2.34
        int totalSec = Integer.parseInt(proc.getNowPlayingSec());
        //proc.printLine("Total Seconds=" + totalSec);
        if (totalSec > 360) {
            songProgress = 1;
            progressDelay = totalSec / 360;

        } else {
            songProgress = 360 / totalSec; //2.34 ~ 2 0.7929...
            progressDelay = songProgress;
        }

        if (direction.equals("A")) {
            if (totalSec > 360) {
                remDegrees = totalSec - 360;
            } else {
                remDegrees = 360 - (totalSec * songProgress); // 52
            }

        } else if (direction.equals("C")) {
            if (totalSec > 360) {
                remDegrees = -totalSec + 360;
            } else {
                remDegrees = -360 + (totalSec * songProgress); // -52
            }
        }

        proc.printLine("SongProgressDelay=" + progressDelay);
        proc.printLine("Progress " + currentProgress);

        new UITimer(() -> {

            nowPlayingSec++;
            if (nowPlayingSec == 60) {
                nowPlayingMin++;
                nowPlayingSec = 0;
            }

            if (direction.equals("A")) {

                if (Integer.parseInt(proc.getNowPlayingSec()) <= 360) { //361
                    if (currentProgress < 360) { //361

                        proc.printLine("remCount------------------------" + remCount);

                        if (remCount < remDegrees) {
                            currentProgress = currentProgress + songProgress + songProgress;
                        } else {
                            currentProgress = currentProgress + songProgress;
                        }
                        remCount = remCount + songProgress;

                    } else { //360 + songProgress
                        currentProgress = 0;
                        nowPlayingMin = 0;
                        nowPlayingSec = 0;
                        remCount = 0;
                        getNextSong();
                    }

                } else {

                    int remSec = Integer.parseInt(proc.getNowPlayingSec()) - totalSecCount;
                    proc.printLine("totalSecCount= " + totalSecCount + " remSec= " + remSec);

                    if (remSec > 360 - currentProgress) {
                        //remSec= 454,453... progress = 0,0,1,1... remDegrees = 360,359...
                        //remaining Sec greater than 360 - currentProgress
                        proc.printLine("extraCount~~~~~~~~~~~~~~~~~~~~~~~~" + extraCount);
                        if (extraCount == progressDelay) {
                            currentProgress = currentProgress + songProgress;
                            extraCount = 0;
                        } else {
                            extraCount++;
                        }
                    } else if (remSec == 360 - currentProgress) {
                        //remSec= 267,266... progress = 93,94... remDegrees = 267,266...
                        //remaining Sec equals 360 -currentProgress
                        if (currentProgress < 360) {
                            currentProgress = currentProgress + songProgress;
                        } else {
                            currentProgress = 0;
                            nowPlayingMin = 0;
                            nowPlayingSec = 0;
                            totalSecCount = 0;
                            getNextSong();
                        }
                    }
                    totalSecCount++;

                }

            } else if (direction.equals("C")) {

                if (Integer.parseInt(proc.getNowPlayingSec()) <= 360) {
                    if (currentProgress > -360) {
                        //currentProgress = currentProgress - songProgress;
                        proc.printLine("remCount------------------------" + remCount);

                        if (remCount > remDegrees) { //0,-1, -2 .... > -52
                            currentProgress = currentProgress - songProgress - songProgress; //0 - 2 - 2 = -4
                        } else {
                            currentProgress = currentProgress - songProgress;
                        }
                        remCount = remCount - songProgress;
                    } else {
                        currentProgress = 0;
                        nowPlayingMin = 0;
                        nowPlayingSec = 0;
                        remCount = 0;
                        getNextSong();
                    }

                } else {

                    int remSec = -(Integer.parseInt(proc.getNowPlayingSec())) + totalSecCount;
                    proc.printLine("totalSecCount= " + totalSecCount + " remSec= " + remSec);

                    if (remSec < -360 - currentProgress) {
                        //remSec= -454,-453... progress = 0,0,-1,1... remDegrees = -360,-359...
                        //remaining Sec less than -360 - (-currentProgress)
                        proc.printLine("extraCount~~~~~~~~~~~~~~~~~~~~~~~~" + extraCount);
                        if (extraCount == progressDelay) {
                            currentProgress = currentProgress - songProgress;
                            extraCount = 0;
                        } else {
                            extraCount++;
                        }
                    } else if (remSec == -360 - currentProgress) {
                        //remSec= -267,-266... progress = -93,-94... remDegrees = -267,-266...
                        //remaining Sec equals -360 - (-currentProgress)
                        if (currentProgress > - 360) {
                            currentProgress = currentProgress - songProgress;
                        } else {
                            currentProgress = 0;
                            nowPlayingMin = 0;
                            nowPlayingSec = 0;
                            totalSecCount = 0;
                            getNextSong();
                        }
                    }
                    totalSecCount++;
                }
            }
            setSongProgress(direction, maxDuration);

        }).schedule(50, true, formProgress);
        //}).schedule(1000, true, formProgress);

        return formProgress;
    }

    private void getNextSong() {

        String nextSong;
        String[] songsArr = proc.splitValue(songs, "#");

        for (int s = 0; s < songsArr.length; s++) {

            String[] trackArr = proc.splitValue(songsArr[s], ":");

            if (trackArr.length > 3) {

                if ((trackArr[0] + ":" + trackArr[1]).equals(proc.getNowPlaying())) {

                    //when last song in the array is reached, get first song in the array 
                    if (s == songsArr.length - 1) {
                        nextSong = songsArr[0];
                    } else {
                        //get next song in the array
                        nextSong = songsArr[s + 1];
                    }

                    String[] nextTrackArr = proc.splitValue(nextSong, ":");

                    try {
//                        lblIconSel.setIcon(proc.getSquareCircleIcon(
//                                createImage("/" + nextTrackArr[2])).getIcon());
//                        cntIconSel = proc.getSquareCircleIcon(
//                                createImage("/" + nextTrackArr[2]));
                        /*cntIconSel.getAllStyles().setBgImage(
                                proc.getSquareCircleIcon(createImage("/" + nextTrackArr[2])));*/

                        Style nextStyle = cntIconSel.getAllStyles();
                        Stroke nextBorderStroke = new Stroke(2, Stroke.CAP_SQUARE, Stroke.JOIN_MITER, 1);
                        nextStyle.setBorder(RoundRectBorder.create().strokeColor(proc.brown)
                                .strokeOpacity(255).stroke(nextBorderStroke));
                        nextStyle.setBgImage(createImage("/" + nextTrackArr[2]));

                        lblTitleSel.setText(nextTrackArr[1]);
                        lblDescSel.setText(nextTrackArr[0]);

                        String[] timeArr = proc.splitValue(nextTrackArr[3], ".");
                        int seconds = Integer.parseInt(timeArr[0]) * 60
                                + Integer.parseInt(timeArr[1]);
                        proc.setNowPlayingSec("" + seconds);
                        proc.printLine(" seconds=" + seconds);
                        int duration = seconds * 1000;
                        proc.printLine("duration=" + duration);

//                        currentProgress = 0;
//                        nowPlayingMin = 0;
//                        nowPlayingSec = 0;
                        proc.setNowPlaying(nextTrackArr[0] + ":" + nextTrackArr[1]);
                        proc.printLine("NextSong=" + proc.getNowPlaying());
                        setSongProgress("A", duration);
                        cntFileSel.setVisible(true);
                        form.revalidate();

                    } catch (IOException e) {

                    }

                    break;
                }
            }
        }
    }
}
