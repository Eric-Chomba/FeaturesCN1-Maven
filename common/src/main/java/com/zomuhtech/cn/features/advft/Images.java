/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zomuhtech.cn.features.advft;

/**
 * GUI builder created Form
 *
 * @author Eric
 */
import com.zomuhtech.cn.features.procs.Proc;
import com.zomuhtech.cn.features.procs.RRHandler;
import com.codename1.capture.Capture;
import com.codename1.components.ImageViewer;
import com.codename1.components.InfiniteProgress;
import com.codename1.components.InteractionDialog;
import com.codename1.components.ScaleImageLabel;
import com.codename1.io.ConnectionRequest;
import com.codename1.io.FileSystemStorage;
import com.codename1.io.NetworkManager;
import com.codename1.io.Storage;
import com.codename1.ui.Button;
import com.codename1.ui.Container;
import com.codename1.ui.Dialog;
import com.codename1.ui.Display;
import com.codename1.ui.EncodedImage;
import com.codename1.ui.FontImage;
import com.codename1.ui.Form;
import com.codename1.ui.Graphics;
import com.codename1.ui.Image;
import com.codename1.ui.URLImage;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.FlowLayout;
import com.codename1.ui.util.ImageIO;
import com.codename1.util.Base64;
import com.javieranton.PhotoCropper;

import java.io.IOException;
import java.io.OutputStream;

public class Images extends Form implements RRHandler.RespHandler {

    RRHandler rh;
    String imgStr, format;
    Image confirmImg, encImg;
    Form form, prevForm;
    Container cnt;
    Proc proc;
    Button imgDld, btnUpload, btnDld;

    public Images(Form form) {
        //this(com.codename1.ui.util.Resources.getGlobalResources());
        this.prevForm = form;
        rh = new RRHandler();
        proc = new Proc();
        Display.getInstance().callSerially(() -> {
            createUI();
        });
    }

    private void createUI() {

        form = proc.getForm("Camera", prevForm);
        form.setLayout(new BorderLayout());

        Button imgV = new Button();
        //ScaleImageLabel imgV = new ScaleImageLabel();
        btnUpload = new Button("Upload", "btn");
        btnUpload.addActionListener(e -> {
            uploadImg();
        });
        btnDld = new Button("Download", "btn");
        btnDld.addActionListener(e -> {
            downloadImg();
        });

        Container pForm = new Container(BoxLayout.x());
        //Container pForm = new Container(new LayeredLayout());
        pForm.setUIID("imgV");
        pForm.setLeadComponent(imgV);

        pForm.add(imgV);
        //pForm.addAll(new Button("HEY BELOW"), imgV);

        cnt = new Container(BoxLayout.y());

//        cnt.addAll(FlowLayout.encloseCenterMiddle(pForm), 
//                FlowLayout.encloseCenterMiddle(imgV), btnUpload, btnDld);
        cnt.addAll(FlowLayout.encloseCenterMiddle(pForm), btnUpload, btnDld);

        imgDld = new Button();
        cnt.add(imgDld);

        form.getToolbar().addCommandToRightBar("",
                proc.materialIcon(FontImage.MATERIAL_CAMERA_ALT), (ev) -> {

            String path = Capture.capturePhoto();

            if (path == null) {
                proc.showToast("User canceled camera",
                        FontImage.MATERIAL_ERROR).show();
                return;
            } else {
                setCamPhoto(path, imgV, pForm);
            }

        });

        form.getToolbar().addCommandToLeftBar("",
                proc.materialIcon(FontImage.MATERIAL_PHOTO), (ev) -> {

            Display.getInstance().openGallery(e -> {

                if (e == null || e.getSource() == null) {

                    proc.showToast("User canceled gallery",
                            FontImage.MATERIAL_ERROR).show();
                } else {

                    String path = (String) e.getSource();
                    //setImage(path, imgV);
                    setCamPhoto(path, imgV, pForm);
                }

            }, Display.GALLERY_IMAGE);
        });
        form.add(BorderLayout.CENTER, FlowLayout.encloseCenterMiddle(cnt));
        cnt.setScrollableY(true);
        form.show();
    }

    //private void setCamPhoto(String path, ImageViewer imgV) {
    private void setCamPhoto(String path, Button imgV, Container pForm) {
        //private void setCamPhoto(String path, ScaleImageLabel imgV) {

        proc.printLine("Img Path " + path);

        try {
            /*int width = Display.getInstance().getDisplayWidth();
            int width = 50;
            Image captureImg = Image.createImage(Capture.capturePhoto(width, -1));
            Image roundMask = Image.createImage(width, captureImg.getHeight(),
                    0xff000000);
            Graphics gr = roundMask.getGraphics();
            gr.setColor(0xffffff);
            gr.fillArc(0, 0, width, width, 0, 360);
            Object mask = roundMask.createMask();
            captureImg = captureImg.applyMask(mask);
            imgV.setImage(captureImg);
            imgV.getParent().revalidate();*/

            Image img = Image.createImage(path);

            imgV.setUIID("imgV");

            int size = 250;
//            if (Display.getInstance().isTablet() 
//                    && Display.getInstance().getDeviceDensity() < 40) {
//                size = 200;
//            }
            //crop image
            PhotoCropper.cropImage(img, 192, size, size, croppedImage -> {

                encImg = compressedImg(croppedImage, true);

                int width = encImg.getWidth();
                int height = encImg.getHeight();

                Image roundMask = Image.createImage(width, height, 0xff000000);
                Graphics gr = roundMask.getGraphics();
                gr.setColor(0xffffff);
                gr.fillArc(0, 0, width, width, 0, 360);

                Object mask = roundMask.createMask();
                encImg = encImg.applyMask(mask);
                //imgV.setIcon(encImg);
                //imgV.getParent().revalidate();

                //imgV.getStyle().setBgImage(encImg);
                //pForm.getStyle().setBgImage(encImg);
                //pForm.getParent().revalidate();
                imgV.setIcon(encImg);
                /*imgV.setIcon(proc.getSquareCircleIcon(encImg));

                Container cnt2 = new Container(new LayeredLayout());
                cnt2.addAll(new Button(proc.getSquareCircleIcon(encImg)), new Button("HEY TOP"));
                cnt.add(cnt2);

                Container cnt3 = new Container(new LayeredLayout());
                Button btnBelow = new Button("HEY BELOW 2");
                cnt3.addAll(btnBelow, new Button(proc.getSquareCircleIcon(encImg)));
                btnBelow.setVisible(false);
                cnt.add(cnt3);

                Container cnt4 = new Container(new LayeredLayout());
                Button btnTop = new Button("HEY TOP 2");
                cnt4.addAll(new Button(proc.getSquareCircleIcon(encImg)), btnTop);
                btnTop.setVisible(false);
                cnt.add(cnt4);*/

                form.revalidate();

            }, "Crop your image", "Warning message image is too small");

            /*img = img.scaledWidth(Math.round(Display.getInstance()
                .getDisplayWidth()/10));*/
            //img = img.scaledWidth(500).scaledHeight(500);
            //EncodedImage encImg = compressedImg(img, true);
            /*Image encImg = compressedImg(img, true)
                    .scaledWidth(200).scaledHeight(200);*/
            //Image encImg = compressedImg(img, true);
            // enImg = enImg.scaledLargerRatio(200, 200);
            /*Image encImg = enImg.subImage(0, (int) 
                    (enImg.getHeight() - 1080 / 2), enImg.getWidth(), 
                    enImg.getWidth(), true);*/
 /* Image encImg = enImg.subImage(0, (int) 
                    enImg.getHeight(), enImg.getWidth(), 
                    enImg.getWidth(), true);*/
            //Save keep aspect
            /*String originImgPath = FileSystemStorage.getInstance().getAppHomePath()
                    + "xyz.jpg";

            String imgPath = FileSystemStorage.getInstance().getAppHomePath()
                    + "xyz_prev.jpg";

            ImageIO.getImageIO().saveAndKeepAspect(path, imgPath,
                    ImageIO.FORMAT_PNG, 100, 100, 0.9f, false, true);//scaleDownOnly, scaleToFill

            //read
            Image encImg = Image.createImage(Storage.getInstance()
                    .createInputStream("xyz_prev.jpg"));

            //Image encImg = compressedImg(img, true);
            //Image encImg = encImg2.scaledHeight()
            System.out.println("Width = " + encImg.getWidth()
                    + "\nHeight = " + encImg.getHeight());*/
            //EncodedImage encImg = EncodedImage.createFromImage(img, false);
            imgV.addActionListener(e -> {
                confirmImage();
            });

            /*int width = 100;
            int height = 100;

            EncodedImage placeholder = EncodedImage.createFromImage(
                    Image.createImage(width, height, 0xffff0000), true);

            Image roundMask = Image.createImage(placeholder.getWidth(),
                    placeholder.getHeight(), 0xff000000);

            Graphics gr = roundMask.getGraphics();
            gr.setColor(0xffffff);
            gr.fillArc(0, 0, placeholder.getWidth(),
                    placeholder.getHeight(), 0, 360);

            final URLImage.ImageAdapter ada
                    = URLImage.createMaskAdapter(roundMask);
            Image image = URLImage.createToStorage(placeholder,
                    "image_" , originImgPath, ada);

             imgV.setIcon(image);*/
            //System.out.println(imgStr);
        } catch (IOException e) {
            Dialog.show("Error", "Error loading img: " + e, "OK", null);
        }
    }

    public EncodedImage compressedImg(Image img, boolean jpeg) {

        ImageIO io = ImageIO.getImageIO();

        //Compress
        EncodedImage encImage = EncodedImage.createFromImage(img, false);
        //Convert to byte array
        byte[] imgArr = encImage.getImageData();
        //convert to base64 String
        imgStr = Base64.encode(imgArr);

        //System.out.println(imgStr);
        //Convert base64 String to byte array
        byte[] imgData = Base64.decode(imgStr.getBytes());
        //Create Image
        EncodedImage encImg2 = EncodedImage.create(imgData);
        //confirmImg = encImg2;

        //EncodedImage encImg2 = EncodedImage.create(imgData, 300, 300, jpeg);
        //Image img2  = Image.createImage(imgData, 0, imgData.length);
        if (io != null) {

            if (!io.isFormatSupported(ImageIO.FORMAT_JPEG)) {
                format = ImageIO.FORMAT_PNG;
            } else {
                format = ImageIO.FORMAT_JPEG;
            }

            //store original image in local storage
            try {
                OutputStream out = FileSystemStorage.getInstance()
                        .openOutputStream(FileSystemStorage.getInstance()
                                .getAppHomePath() + "upload-img." + format);

                //compress & save
                io.save(encImg2, out, format, 0.9f);
                // io.save(img2, out, format, 0.9f);
                out.close();

            } catch (IOException e) {
                //System.out.println(e);
            }

        }

        //store 100x100 encoded image in local storage
        //encImg2 = EncodedImage.create(imgData, 100, 100, true);
        encImg2 = EncodedImage.create(imgData, img.getWidth() / 3,
                img.getHeight() / 3, true);
        try {
            OutputStream out = FileSystemStorage.getInstance()
                    .openOutputStream(FileSystemStorage.getInstance()
                            .getAppHomePath() + "upload-img-prv." + format);

            //compress & save
            //Image resizedImg = encImg2.fill(100, 100);
            ImageIO.getImageIO().save(encImg2, out, ImageIO.FORMAT_JPEG, 0.9f);
            // ImageIO.getImageIO().save(resizedImg, out, ImageIO.FORMAT_JPEG, 0.9f);
            // io.save(img2, out, format, 0.9f);
            out.close();

        } catch (IOException e) {
            //System.out.println(e);
        }
        //return encImg2;
        return encImage;
    }

    private void confirmImage() {

        //read original image from storage
        try {
            confirmImg = Image.createImage(Storage.getInstance()
                    .createInputStream("upload-img." + format));
        } catch (IOException e) {
        }

        ScaleImageLabel lblImg = new ScaleImageLabel();
        lblImg.setIcon(confirmImg);
        //Label lblImg = new Label();
        //lblImg.setIcon(confirmImg);
        //ImageViewer lblImg = new ImageViewer();
        //lblImg.setImage(confirmImg);
        //lblImg.setImageInitialPosition(IMAGE_FIT);
        //lblImg.setImageInitialPosition(IMAGE_FILL);

        InteractionDialog interDialog = new InteractionDialog(new BorderLayout());
        interDialog.getTitleComponent().remove();
        interDialog.setDialogUIID("statusDialog");
        // Dialog interDialog = new Dialog(new BorderLayout());

        Button btnClose = new Button("OK", "btnDialog");
        btnClose.addActionListener(e -> {
            interDialog.dispose();
        });
        Container cntConfirm = new Container(BoxLayout.y(), "cntSuccess");
        cntConfirm.add(FlowLayout.encloseCenterMiddle(lblImg));
        cntConfirm.add(FlowLayout.encloseCenterBottom(btnClose));
        interDialog.addComponent(BorderLayout.CENTER,
                FlowLayout.encloseCenterMiddle(cntConfirm));

        interDialog.show(0, 0, 0, 0);
        //interDialog.show();

        //break;
        //}
    }

    private void uploadImg() {

        //if (proc.clearStore("dwld-img", "dwld-imgImageURLTMP")) {
        btnUpload.setText("Uploading, please wait");
        btnUpload.setEnabled(false);
        String data = "Images>Upload>" + imgStr;
        rh.processReq(this, data, "upload");
        //}
    }

    private void downloadImg() {

        // first clear previous masked local stored image file
        if (proc.clearStore("dwld-img", "dwld-imgImageURLTMP")) {
            //Storage.getInstance().deleteStorageFile("dwld-img");
            //Storage.getInstance().deleteStorageFile("dwld-imgImageURLTMP");
            //Display.getInstance().callSerially(() -> {
            btnDld.setText("Downloading, please wait");
            btnDld.setEnabled(false);
            String data = "Images>Download>";
            rh.processReq(this, data, "download");
            // });
        }
    }

    /*public static EncodedImage compressedImg(Image img, boolean jpeg) {
        if (img instanceof EncodedImage) {
            return ((EncodedImage) img);
        }

        ImageIO io = ImageIO.getImageIO();
        if (io != null) {
            String format;
            if (jpeg) {
                if (!io.isFormatSupported(ImageIO.FORMAT_JPEG)) {
                    format = ImageIO.FORMAT_PNG;
                } else {
                    format = ImageIO.FORMAT_JPEG;
                }
            } else {
                if (!io.isFormatSupported(ImageIO.FORMAT_PNG)) {
                    format = ImageIO.FORMAT_JPEG;
                } else {
                    format = ImageIO.FORMAT_PNG;
                }
            }

            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                io.save(img, baos, format, 0.9f);
                EncodedImage encImg = EncodedImage.create(baos.toByteArray());
                Util.cleanup(baos);
                
                encImg.width = img.getWidth();
                encImg.height = img.getHeight();

                if (format == ImageIO.FORMAT_JPEG) {
                    
                    encImg.opaque = true;
                    encImg.opaqueChecked = true;
                }
                encImg.cache = Display.getInstance().createSoftWeakRef(img);

                return encImg;

            } catch (IOException err) {
                Log.e(err);
            }
        }
        return null;
    }*/
    private void setImage(String path, ImageViewer imgV) {

        try {
            Image img = Image.createImage(path);
            imgV.setImage(img);
            imgV.getParent().revalidate();

        } catch (IOException e) {
            Dialog.show("Error", "Error loading img: " + e, "OK", null);
        }
    }

    @Override
    public void getResp(String resp, String taskTag) {

        int width = 100;
        int height = 100;

        EncodedImage placeholder = EncodedImage.createFromImage(
                Image.createImage(width, height, 0x3399ff), true);

        Image roundMask = Image.createImage(placeholder.getWidth(),
                placeholder.getHeight(), 0x3399ff);

        Graphics gr = roundMask.getGraphics();
        gr.setColor(0xffffff);
        gr.fillArc(0, 0, placeholder.getWidth(),
                placeholder.getHeight(), 0, 360);

        String[] respArr = proc.splitValue(resp, ">");

        switch (taskTag) {
            case "upload":
                btnUpload.setText("Upload");
                btnUpload.setEnabled(true);
                //showToast(respArr[1]);
                proc.showToast(respArr[1], FontImage.MATERIAL_ERROR).show();
                break;

            case "download":

                // first clear previous masked local stored image file
                //if (proc.clearStore("dwld-img")) { 
                btnDld.setText("Download");
                btnDld.setEnabled(true);
                final URLImage.ImageAdapter ada
                        = URLImage.createMaskAdapter(roundMask);

                Image image = URLImage.createToStorage(placeholder,
                        "dwld-img", respArr[1], ada);

                imgDld.setIcon(image);
                form.revalidate();
                imgDld.addActionListener(e -> {
                    //confirmImg = imgDld.getIcon();
                    confirmDwldImg(respArr[1]);
                });

                //Delete image
                /*Display.getInstance().callSerially(() -> {
                    delImg(respArr[1]);
                });*/
                //}
                break;

            case "delImg":
                //showToast(respArr[1]);
                proc.showToast(respArr[1], FontImage.MATERIAL_ERROR).show();
                break;
        }

    }

    private void delImg(String imgPath) {

        String data = "Images>DelImg>" + imgPath;
        rh.processReq(this, data, "delImg");
    }

    public void confirmDwldImg(String imgUrl) {

        Dialog d = new InfiniteProgress().showInfiniteBlocking();

        //InfiniteProgress ip = new InfiniteProgress();
        //ip.setUIID("infiniteP");
//        Image img, dImg = null;
//        try {
//            img = Image.createImage("/progress.png");
//            ip.setAnimation(img);
//        } catch (IOException e) {
//
//            proc.printLine(e.getMessage());
//        }
        //d.addComponent(ip);
        //d.show();
        ConnectionRequest request = new ConnectionRequest(imgUrl, false);
        request.setTimeout(15000);
        request.setReadTimeout(20000);
        request.setFailSilently(true);

        NetworkManager.getInstance().addToQueueAndWait(request);

        proc.printLine("\nRespCode1 " + request.getResponseCode());

        ScaleImageLabel lblImg = new ScaleImageLabel();

        InteractionDialog interDialog = new InteractionDialog(new BorderLayout());
        interDialog.getTitleComponent().remove();
        interDialog.setDialogUIID("statusDialog");

        Button btnClose = new Button("OK", "btnDialog");
        btnClose.addActionListener(e -> {
            interDialog.dispose();
        });
        Container cntDialog = new Container(BoxLayout.y());
        cntDialog.add(FlowLayout.encloseCenterMiddle(lblImg));
        cntDialog.add(FlowLayout.encloseCenterBottom(btnClose));
        interDialog.addComponent(BorderLayout.CENTER,
                FlowLayout.encloseCenterMiddle(cntDialog));

        switch (request.getResponseCode()) {

            case 0:
                d.dispose();
                proc.showToast("Connection failed", FontImage.MATERIAL_ERROR);
                break;

            case 404:
                d.dispose();
                proc.showToast("Services Unavailable", FontImage.MATERIAL_ERROR);
                break;

            case 200:
                d.dispose();
                byte[] result = request.getResponseData();
                EncodedImage encodedImg = EncodedImage.create(result);

                lblImg.setIcon(encodedImg);

                interDialog.show(0, 0, 0, 0);

                break;
        }

    }

    public Images(com.codename1.ui.util.Resources resourceObjectInstance) {
        //initGuiBuilderComponents(resourceObjectInstance);
    }

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////-- DON'T EDIT BELOW THIS LINE!!!


// <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initGuiBuilderComponents(com.codename1.ui.util.Resources resourceObjectInstance) {
        setLayout(new com.codename1.ui.layouts.FlowLayout());
        setInlineStylesTheme(resourceObjectInstance);
                setInlineStylesTheme(resourceObjectInstance);
        setTitle("Images");
        setName("Images");
    }// </editor-fold>

//-- DON'T EDIT ABOVE THIS LINE!!!
}
