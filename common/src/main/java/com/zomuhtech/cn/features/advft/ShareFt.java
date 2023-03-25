/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zomuhtech.cn.features.advft;

import com.codename1.components.ShareButton;
import com.codename1.components.SpanLabel;
import com.codename1.io.FileSystemStorage;
import com.codename1.ui.Container;
import com.codename1.ui.Display;
import com.codename1.ui.Form;
import com.codename1.ui.Image;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.util.ImageIO;
import com.zomuhtech.cn.features.procs.Proc;
import java.io.IOException;
import java.io.OutputStream;
import com.codename1.ext.filechooser.FileChooser;
import com.codename1.io.Storage;
import com.codename1.io.Util;
import com.codename1.ui.Button;
import static com.codename1.ui.Component.CENTER;
import com.codename1.ui.FontImage;
import com.codename1.ui.TextArea;
import com.codename1.ui.TextField;
import static com.codename1.ui.layouts.BorderLayout.NORTH;
import com.codename1.ui.layouts.GridLayout;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * GUI builder created Form
 *
 * @author Eric
 */
public class ShareFt extends Form {

    Form form, prevForm;
    Proc proc;
    String filePath, selFileExt = null, fileToSharePath = null;
    ArrayList<Button> btnArr;

    public ShareFt(Form form) {
        this.prevForm = form;
        this.btnArr = new ArrayList<>();
        proc = new Proc();
        Display.getInstance().callSerially(() -> {
            createUI();
        });
    }

    private void createUI() {

        form = proc.getForm("Share", prevForm);
        form.setLayout(new BorderLayout());

        form.add(CENTER, getForm1());

        Button btnScreen = new Button("Screenshot", "btnNav");
        btnArr.add(btnScreen);
        btnScreen.addActionListener(ev -> {
            proc.changeBtnUIID(btnArr, btnScreen);
            form.add(CENTER, getForm1());
            form.revalidate();
        });

        Button btnFile = new Button("File", "btnNav");
        btnArr.add(btnFile);
        btnFile.addActionListener(ev -> {
            proc.changeBtnUIID(btnArr, btnFile);
            form.add(CENTER, getForm2());
            form.revalidate();
        });

        Button btnText = new Button("Text", "btnNav");
        btnArr.add(btnText);
        btnText.addActionListener(e -> {
            proc.changeBtnUIID(btnArr, btnText);
            form.add(CENTER, getForm3());
            form.revalidate();
        });

        //selected by default
        proc.changeBtnUIID(btnArr, btnScreen);

        Container cnt = new Container(new GridLayout(1, 3));
        cnt.addAll(btnScreen, btnFile, btnText);

        form.add(NORTH, cnt);

        form.show();
    }

    private Form getForm1() {

        Form form1 = proc.getInputForm();

        ShareButton btnShareImg = new ShareButton();
        btnShareImg.setText("Share Screenshot");
        btnShareImg.setUIID("btnShare");
        form1.add(btnShareImg);

        btnShareImg.addActionListener(e -> {

            Image imgSShot = Image.createImage(form.getWidth(), form.getHeight());
            form.revalidate();
            form.setVisible(true);
            form.paintComponent(imgSShot.getGraphics(), true);

            String imgFile = FileSystemStorage.getInstance().getAppHomePath()
                    + "imgSShot.png";
            proc.printLine("ImgFile " + imgFile);

            try (OutputStream os = FileSystemStorage.getInstance()
                    .openOutputStream(imgFile);) {
                ImageIO.getImageIO().save(imgSShot, os, ImageIO.FORMAT_PNG, 1);

            } catch (IOException ex) {
            }
            btnShareImg.setImageToShare(imgFile, "image/png");

            proc.printLine("Sharing Img " + btnShareImg.getImagePathToShare());
        });

        return form1;
    }

    private Form getForm2() {

        Form form2 = proc.getInputForm();
        ShareButton btnShareFile = new ShareButton();
        btnShareFile.setText("Share File");
        btnShareFile.setUIID("btnShare");

        Container cntShare = new Container(BoxLayout.x());
        SpanLabel lblPath = new SpanLabel("Path", "menuLbl");
        Button btnBrowse = new Button("Browse", "btnNav");
        cntShare.add(btnBrowse).add(lblPath);
        form2.addAll(cntShare, btnShareFile);

        btnBrowse.addActionListener(e -> {

            if (FileChooser.isAvailable()) {

                try {
                    FileChooser.showOpenDialog(proc.getFileExtMime(), ev -> {

                        if (ev == null) {
                            proc.printLine("no file selected");
                            proc.showToast("no file selected", FontImage.MATERIAL_INFO_OUTLINE).show();

                        } else {

                            String allFilePath = (String) ev.getSource();

                            proc.printLine("File Path " + allFilePath);

                            //file:///data/user/0/com.zomuhtech.cn.features/files//9-piece-img-border.txt
//                            String[] fileArr = proc.splitValue(allFilePath, "/");
//                            String[] fileNameArr = proc.splitValue(fileArr[fileArr.length - 1], ".");
//                            String fileName = fileNameArr[0];
//                            proc.printLine("Filename=" + fileName);
                            selFileExt = allFilePath.substring(allFilePath.length() - 3);

                            if (selFileExt.equals("ocx")) {
                                selFileExt = "docx";
                            }
                            if (selFileExt.equals("ptx")) {
                                selFileExt = "pptx";
                            }
                            if (selFileExt.equals("lsx")) {
                                selFileExt = "xlsx";
                            }
                            if (selFileExt.equals("peg")) {
                                selFileExt = "jpeg";
                            }

                            proc.printLine("File Ext " + selFileExt);

                            FileSystemStorage fss = FileSystemStorage.getInstance();

                            try {

                                InputStream is = fss.openInputStream(allFilePath);

                                //Copy from FSS to Storage
                                Util.copy(is, Storage.getInstance()
                                        .createOutputStream("fileToShare." + selFileExt));

                                /*Util.copy(is, Storage.getInstance()
                                        .createOutputStream(fileName + "." + selFileExt));*/
                                //proc.printLine("File Content\n" 
                                //+ Util.readToString(is));
                                fileToSharePath = FileSystemStorage
                                        .getInstance().getAppHomePath() + "fileToShare." + selFileExt;
                                /*fileToSharePath = FileSystemStorage
                                        .getInstance().getAppHomePath() + fileName + "." + selFileExt;*/

                                proc.printLine("FileToSharePath " + fileToSharePath);
                                lblPath.setText(fileToSharePath);
                                form.revalidate();

                            } catch (IOException ex) {
                                proc.printLine("Share Ex " + ex);
                            }
                        }
                    });
                } catch (Exception exc) {
                    proc.printLine("FileChooser: " + exc.getMessage());
                }
            }
        });

        btnShareFile.addActionListener(e -> {

            if (fileToSharePath != null && selFileExt != null) {
                proc.printLine("ShareFilePath " + fileToSharePath);
                String mimeType = proc.getMimeType(selFileExt);
                proc.printLine("ShareFileMimeType " + mimeType);
                //btnShareFile.setImageToShare(fileToSharePath, mimeType);
                Display.getInstance().share(fileToSharePath, null, mimeType);

            }
            /*String msg = " ";
            String to = " ";
            String subject = " ";
            Message message = new Message(msg);
            //message.getAttachments().put(filePath, mimeType);
            message.setAttachment(fileToSharePath);
            message.setAttachmentMimeType(getMimeType(selFileExt));
            //message.setAttachmentMimeType(Message.MIME_IMAGE_PNG);
            Display.getInstance().sendMessage(new String[]{to}, subject, message);*/

        });

        return form2;
    }

    private Form getForm3() {

        Form form3 = proc.getInputForm();

        Container cntText = proc.getInputCnt();
        TextField tfText = proc.getInputTf("Enter Text", TextArea.ANY);
        cntText.add(tfText);

        ShareButton btnShareText = new ShareButton();
        btnShareText.setText("Share Text");
        btnShareText.setUIID("btnShare");

        form3.addAll(cntText, btnShareText);

        btnShareText.addActionListener(e -> {
//            Display.getInstance().share("Attached file",
//                    fileToSharePath,
//                    getMimeType(selFileExt));
            //proc.printLine("Sending " + fileToSharePath);

            if (tfText.getText().length() == 0) {
                proc.showToast("Enter text", FontImage.MATERIAL_ERROR_OUTLINE).show();
            } else {
                //Display.getInstance().share(tfText.getText(), null, null);
                btnShareText.setTextToShare(tfText.getText());
            }
        });

        return form3;
    }

    public ShareFt(com.codename1.ui.util.Resources resourceObjectInstance) {
        //initGuiBuilderComponents(resourceObjectInstance);
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////-- DON'T EDIT BELOW THIS LINE!!!


// <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initGuiBuilderComponents(com.codename1.ui.util.Resources resourceObjectInstance) {
        setLayout(new com.codename1.ui.layouts.FlowLayout());
        setInlineStylesTheme(resourceObjectInstance);
                setInlineStylesTheme(resourceObjectInstance);
        setTitle("ShareFt");
        setName("ShareFt");
    }// </editor-fold>

//-- DON'T EDIT ABOVE THIS LINE!!!
}
