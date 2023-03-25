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
import com.codename1.capture.Capture;
import com.codename1.components.ImageViewer;
import com.codename1.components.ScaleImageLabel;
import com.codename1.io.FileSystemStorage;
import com.codename1.io.Log;
import static com.codename1.ui.CN.getCurrentForm;
import com.codename1.ui.Container;
import com.codename1.ui.Display;
import com.codename1.ui.FontImage;
import com.codename1.ui.Form;
import com.codename1.ui.Graphics;
import com.codename1.ui.Image;
import com.codename1.ui.Label;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.geom.GeneralPath;
import com.codename1.ui.geom.Rectangle;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.FlowLayout;
import com.codename1.ui.layouts.LayeredLayout;
import com.codename1.ui.util.ImageIO;
import com.codename1.util.OnComplete;
import com.zomuhtech.cn.features.procs.Proc;
import java.io.IOException;
import com.javieranton.PhotoCropper;
import java.io.OutputStream;

public class CropImg extends com.codename1.ui.Form {

    Form form, prevForm;
    Proc proc;

    public CropImg(Form form) {
        //this(com.codename1.ui.util.Resources.getGlobalResources());
        this.prevForm = form;
        proc = new Proc();
        Display.getInstance().callSerially(() -> {
            createUI();
        });
    }

    private void createUI() {
        form = proc.getForm("Crop Image", prevForm);
        form.setLayout(new BorderLayout());

        form.getToolbar().addCommandToLeftBar("",
                proc.materialIcon(FontImage.MATERIAL_PHOTO), (ev) -> {

            Display.getInstance().openGallery(e -> {

                if (e == null || e.getSource() == null) {
                    proc.showToast("User canceled gallery",
                            FontImage.MATERIAL_ERROR).show();
                } else {
                    try {
                        String path = (String) e.getSource();
                        Image img = Image.createImage(path);
                        useCropImg(img);
                    } catch (IOException err) {

                    }
                }

            }, Display.GALLERY_IMAGE);
        });

        form.getToolbar().addCommandToLeftBar("",
                proc.materialIcon(FontImage.MATERIAL_PHOTO_ALBUM), (ev) -> {

            Display.getInstance().openGallery(e -> {

                if (e == null || e.getSource() == null) {
                    proc.showToast("User canceled gallery",
                            FontImage.MATERIAL_ERROR).show();
                } else {

                    try {
                        String path = (String) e.getSource();
                        Image img = Image.createImage(path);
                        usePhotoCropper(img);
                    } catch (IOException err) {

                    }
                }

            }, Display.GALLERY_IMAGE);
        });

        form.getToolbar().addCommandToRightBar("",
                proc.materialIcon(FontImage.MATERIAL_CAMERA), e -> {

            Capture.capturePhoto((ActionListener) (ActionEvent evt) -> {

                if (evt == null || evt.getSource() == null) {
                    //camera cancelled
                    return;
                }

                String file = (String) evt.getSource();

                try {
                    Image img = Image.createImage(file);

                    useCropImg(img);

                } catch (IOException ex) {
                    Log.p("Error loading captured image from camera",
                            Log.ERROR);
                }
            });
        });

        form.getToolbar().addCommandToRightBar("",
                proc.materialIcon(FontImage.MATERIAL_CAMERA_ALT), e -> {

            Capture.capturePhoto((ActionListener) (ActionEvent evt) -> {

                if (evt == null || evt.getSource() == null) {
                    //camera cancelled
                    return;
                }

                String file = (String) evt.getSource();

                try {
                    Image img = Image.createImage(file);
                    usePhotoCropper(img);

                } catch (IOException ex) {
                    Log.p("Error loading captured image from camera",
                            Log.ERROR);
                }
            });
        });

        form.show();
    }

    private void useCropImg(Image img) {
        cropImage(img, 256, 256, i -> {

            form.removeAll();
            ScaleImageLabel lblImg = new ScaleImageLabel(i);
            lblImg.setUIID("lblImg");
            //form.add(CENTER, new ScaleImageLabel(i));
            form.add(CENTER, lblImg);
            form.revalidate();

            writeToStorage(i);
        });
    }

    private void usePhotoCropper(Image img) {
        PhotoCropper.cropImage(img, 192, 500, 500, croppedImg -> {
            form.removeAll();
            form.add(CENTER, new ScaleImageLabel(croppedImg));
            form.revalidate();

            writeToStorage(croppedImg);

        }, "Crop your image", "Warning message image is too small");
    }

    private void cropImage(Image img, int destWidth, int destHeight,
            OnComplete<Image> s) {

        Form previous = getCurrentForm();
        Form cropForm = new Form("Crop your Avatar", new LayeredLayout());
        cropForm.getToolbar().setUIID("tbar");
        cropForm.getToolbar().getTitleComponent()
                .setUIID(proc.getLblTitleUIID());
        Label moveAndZoom = new Label("Move and zoom photo to crop it");
        moveAndZoom.getUnselectedStyle().setFgColor(0x15E7FF);
        moveAndZoom.getUnselectedStyle().setAlignment(CENTER);
        moveAndZoom.setCellRenderer(true);

        cropForm.setGlassPane((Graphics g, Rectangle rect) -> {

            g.setColor(0x0000ff);
            g.setAlpha(150);
            Container cropCp = cropForm.getContentPane();
            int posY = cropForm.getContentPane().getAbsoluteY();

            GeneralPath p = new GeneralPath();
            p.setRect(new Rectangle(0, posY, cropCp.getWidth(),
                    cropCp.getHeight()), null);

            if (Display.getInstance().isPortrait()) {

                p.arc(0, posY + cropCp.getHeight() / 2 - cropCp.getWidth() / 2,
                        cropCp.getWidth() - 1, cropCp.getWidth() - 1, 0,
                        Math.PI * 2);
            } else {

                p.arc(cropCp.getWidth() / 2 - cropCp.getHeight() / 2, posY,
                        cropCp.getHeight() - 1, cropCp.getHeight() - 1, 0,
                        Math.PI * 2);
            }
            g.fillShape(p);
            g.setAlpha(255);
            g.setColor(0xffffff);
            moveAndZoom.setX(0);
            moveAndZoom.setY(posY);
            moveAndZoom.setWidth(cropCp.getWidth());
            moveAndZoom.setHeight(moveAndZoom.getPreferredH());
            moveAndZoom.paint(g);
        });

        ImageViewer viewer = new ImageViewer();
        viewer.setImage(img);

        cropForm.add(viewer);
        cropForm.getToolbar().addCommandToRightBar("",
                proc.materialIcon(FontImage.MATERIAL_CROP), e -> {
            previous.showBack();

            s.completed(viewer.getCroppedImage(0).fill(destWidth, destHeight));
            /*s.completed(viewer.getCroppedImage(0)
                    .fill(viewer.getCroppedImage(0).getWidth(),
                            viewer.getCroppedImage(0).getHeight()));*/

        });

        cropForm.getToolbar().addCommandToLeftBar("",
                proc.materialIcon(FontImage.MATERIAL_CANCEL), e -> {
            previous.showBack();
        });

        cropForm.show();
    }

    private void writeToStorage(Image croppedImg) {
        ImageIO io = ImageIO.getImageIO();
        if (io != null) {
            String format;

            if (!io.isFormatSupported(ImageIO.FORMAT_JPEG)) {
                format = ImageIO.FORMAT_PNG;
            } else {
                format = ImageIO.FORMAT_JPEG;
            }

            try {
                OutputStream out = FileSystemStorage.getInstance()
                        .openOutputStream(FileSystemStorage.getInstance()
                                .getAppHomePath() + "croppedImage.jpg");

                //compress & save
                io.save(croppedImg, out, format, 0.9f);
                // io.save(img2, out, format, 0.9f);
                out.close();

            } catch (IOException e) {
                //System.out.println(e);
            }

        }
    }

    public CropImg(com.codename1.ui.util.Resources resourceObjectInstance) {
        // initGuiBuilderComponents(resourceObjectInstance);
    }

//////////////////////////////////////////////-- DON'T EDIT BELOW THIS LINE!!!


// <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initGuiBuilderComponents(com.codename1.ui.util.Resources resourceObjectInstance) {
        setLayout(new com.codename1.ui.layouts.FlowLayout());
        setInlineStylesTheme(resourceObjectInstance);
                setInlineStylesTheme(resourceObjectInstance);
        setTitle("CropImg");
        setName("CropImg");
    }// </editor-fold>

//-- DON'T EDIT ABOVE THIS LINE!!!
}
