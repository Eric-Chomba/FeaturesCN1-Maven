/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zomuhtech.cn.features.advft;

import com.codename1.capture.Capture;
import com.codename1.components.MediaPlayer;
import com.codename1.components.MultiButton;
import com.codename1.components.ToastBar;
import com.codename1.ext.filechooser.FileChooser;
import com.codename1.io.FileSystemStorage;
import com.codename1.io.Storage;
import com.codename1.io.Util;
import com.codename1.l10n.SimpleDateFormat;
import com.codename1.media.Media;
import com.codename1.media.MediaManager;
import com.codename1.ui.Display;
import com.codename1.ui.FontImage;
import com.codename1.ui.Form;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.layouts.BorderLayout;
import static com.codename1.ui.layouts.BorderLayout.NORTH;
import com.zomuhtech.cn.features.procs.Proc;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

/**
 * GUI builder created Form
 *
 * @author Eric
 */
public class MediaPlayerFt extends com.codename1.ui.Form {

    Form form, prevForm;
    Proc proc;
    String mp4Path;
    int size;

    public MediaPlayerFt(Form form) {
        //this(com.codename1.ui.util.Resources.getGlobalResources());
        this.prevForm = form;
        proc = new Proc();
        this.size = Display.getInstance().getDisplayWidth();
        Display.getInstance().callSerially(() -> {
            Storage.getInstance().clearStorage();
            createUI();
        });
    }

    private void createUI() {
        form = proc.getForm("Media Player", prevForm);
        form.setLayout(new BorderLayout());

        FileSystemStorage fss = FileSystemStorage.getInstance();

        //Browse & Play Video from Gallery
        form.getToolbar().addCommandToLeftBar("",
                proc.materialIcon(FontImage.MATERIAL_VIDEO_LIBRARY), (ev) -> {
            Display.getInstance().openGallery((e) -> {
                if (ev == null) {
                    proc.printLine("no video selected");
                    ToastBar.showErrorMessage("no video selected");

                } else {
                    String filePath = (String) e.getSource();
                    try {
                        Media video = MediaManager.createMedia(filePath, true);
                        //form.removeAll();

                        MediaPlayer player = new MediaPlayer(video) {
                            @Override
                            public Dimension calcPreferredSize() {
                                return new Dimension(size, size);
                            }
                        };
                        form.add(CENTER, player);
                        form.revalidate();
                    } catch (IOException ex) {
                    }
                }
            }, Display.GALLERY_VIDEO);
        });

        //Browse & Play Video from file storage
        form.getToolbar().addCommandToLeftBar("",
                proc.materialIcon(FontImage.MATERIAL_VIDEO_SETTINGS), (ev) -> {

            Storage.getInstance().clearStorage();

            if (FileChooser.isAvailable()) {

                FileChooser.showOpenDialog(".mp4", event -> {

                    if (event == null) {
                        proc.printLine("no video selected");
                        ToastBar.showErrorMessage("no video selected");

                    } else {
                        String filePath = (String) event.getSource();

                        proc.printLine("File Path " + filePath);
                        String selFileExt = filePath
                                .substring(filePath.length() - 3);

                        proc.printLine("File Ext " + selFileExt);

                        try {

                            InputStream is = fss.openInputStream(filePath);

                            //Copy from FSS to Storage
                            Util.copy(is, Storage.getInstance()
                                    .createOutputStream(
                                            "videoFile." + selFileExt));

                            mp4Path = FileSystemStorage
                                    .getInstance().getAppHomePath()
                                    + "videoFile." + selFileExt;

                            proc.printLine("Mp4Path " + mp4Path);

                            MultiButton btn = new MultiButton(mp4Path.substring(mp4Path
                                    .lastIndexOf("/") + 1));
                            btn.setUIIDLine1("menuLbl");
                            form.add(NORTH, btn);
                            form.revalidate();
                            btn.addActionListener(e -> {
                                try {
                                    Media video = MediaManager
                                            .createMedia(mp4Path, true);
                                    MediaPlayer player = new MediaPlayer(video) {
                                        @Override
                                        public Dimension calcPreferredSize() {
                                            return new Dimension(size, size);
                                        }
                                    };
                                    form.add(CENTER, player);
                                    //form.add(CENTER, new MediaPlayer(video));
                                    form.revalidate();
                                } catch (IOException ex) {
                                }
                            });

                        } catch (IOException ex) {
                        }
                    }
                });
            }

        });

        //Record & play sound 
        String recordingsDir = fss.getAppHomePath();
        fss.mkdir(recordingsDir);

        try {

            for (String file : fss.listFiles(recordingsDir)) {
                MultiButton mb = new MultiButton(
                        file.substring(file.lastIndexOf("/") + 1));
                mb.setUIIDLine1("menuLbl");
                mb.addActionListener((e) -> {

                    try {
                        Media m = MediaManager.createMedia(recordingsDir + file,
                                false);
                        m.play();
                    } catch (IOException ex) {
                    }

                });
                form.add(NORTH, mb);
                form.revalidate();
            }
            form.getToolbar().addCommandToRightBar("",
                    proc.materialIcon(FontImage.MATERIAL_MIC), (ev) -> {
                Storage.getInstance().clearStorage();
                try {
                    String file = Capture.captureAudio();
                    if (file != null) {
                        SimpleDateFormat sdf
                                = new SimpleDateFormat("yyyy-MM-dd-kk-mm");
                        String fileName = sdf.format(new Date());
                        String filePath = recordingsDir + fileName;
                        Util.copy(fss.openInputStream(file),
                                fss.openOutputStream(filePath));

                        MultiButton btn = new MultiButton(fileName);
                        btn.setUIIDLine1("menuLbl");
                        btn.addActionListener(e -> {
                            try {
                                Media media = MediaManager.createMedia(filePath,
                                        false);
                                media.play();
                            } catch (IOException ex) {
                            }
                        });
                        form.add(NORTH, btn);
                        form.revalidate();
                    }
                } catch (IOException ex) {
                }
            });

            //Browse & Play Audio from file storage
            form.getToolbar().addCommandToRightBar("",
                    proc.materialIcon(FontImage.MATERIAL_AUDIOTRACK), (ev) -> {

                Storage.getInstance().clearStorage();

                if (FileChooser.isAvailable()) {

                    FileChooser.showOpenDialog(".mp3", event -> {

                        if (event == null) {
                            proc.printLine("no audio selected");
                            ToastBar.showErrorMessage("no audio selected");

                        } else {
                            String filePath = (String) event.getSource();

                            proc.printLine("File Path " + filePath);
                            String selFileExt = filePath
                                    .substring(filePath.length() - 3);

                            proc.printLine("File Ext " + selFileExt);

                            try {

                                InputStream is = fss.openInputStream(filePath);

                                //Copy from FSS to Storage
                                Util.copy(is, Storage.getInstance()
                                        .createOutputStream(
                                                "audioFile." + selFileExt));

                                String mp3Path = FileSystemStorage
                                        .getInstance().getAppHomePath()
                                        + "audioFile." + selFileExt;

                                proc.printLine("Mp3Path " + mp3Path);

                                MultiButton btn = new MultiButton(mp3Path.substring(mp3Path
                                        .lastIndexOf("/") + 1));
                                btn.setUIIDLine1("menuLbl");
                                btn.addActionListener(e -> {
                                    try {
                                        Media media = MediaManager
                                                .createMedia(mp3Path, false);
                                        media.play();
                                    } catch (IOException ex) {
                                    }
                                });
                                form.add(NORTH, btn);
                                form.revalidate();

                            } catch (IOException ex) {
                            }
                        }
                    });
                }

            });

        } catch (IOException e) {
        }

        form.show();
    }

    public MediaPlayerFt(com.codename1.ui.util.Resources resourceObjectInstance) {
        //initGuiBuilderComponents(resourceObjectInstance);
    }

//////////////////////////////////////////////////////////////////-- DON'T EDIT BELOW THIS LINE!!!


// <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initGuiBuilderComponents(com.codename1.ui.util.Resources resourceObjectInstance) {
        setLayout(new com.codename1.ui.layouts.FlowLayout());
        setInlineStylesTheme(resourceObjectInstance);
                setInlineStylesTheme(resourceObjectInstance);
        setTitle("MediaPlayerFt");
        setName("MediaPlayerFt");
    }// </editor-fold>

//-- DON'T EDIT ABOVE THIS LINE!!!
}
