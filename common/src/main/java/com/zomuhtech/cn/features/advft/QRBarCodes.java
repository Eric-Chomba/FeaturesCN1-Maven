/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zomuhtech.cn.features.advft;

import com.codename1.components.SpanLabel;
import com.codename1.ui.Button;
import com.codename1.ui.Display;
import com.codename1.ui.Form;
import com.codename1.ui.layouts.BorderLayout;
import com.zomuhtech.cn.features.procs.Proc;
import com.codename1.ext.codescan.CodeScanner;
import com.codename1.ext.codescan.ScanResult;
import com.codename1.ui.Container;
import com.codename1.ui.FontImage;
import static com.codename1.ui.layouts.BorderLayout.SOUTH;
import com.codename1.ui.layouts.GridLayout;
import java.util.ArrayList;
//import org.littlemonkey.qrscanner.QRScanner;

/**
 *
 * @author Eric
 */
public class QRBarCodes extends Form {

    Form form, prevForm;
    Proc proc;
    ArrayList<Button> btnArr;

    public QRBarCodes(Form form) {
        this.prevForm = form;
        this.btnArr = new ArrayList<>();
        proc = new Proc();
        Display.getInstance().callSerially(() -> {
            createUI();
        });

    }

    public void createUI() {
        form = proc.getForm("QR&Bar Codes", prevForm);
        form.setLayout(new BorderLayout());

        Button btnQR = new Button("QR Code", "btnNav");
        btnArr.add(btnQR);
        btnQR.addActionListener(e -> {
            proc.changeBtnUIID(btnArr, btnQR);
            form.add(CENTER, getForm1());
            form.revalidate();
        });

        Button btnBar = new Button("Bar Code", "btnNav");
        btnArr.add(btnBar);
        btnBar.addActionListener(ev -> {
            proc.changeBtnUIID(btnArr, btnBar);
            form.add(CENTER, getForm2());
            form.revalidate();
        });

        Container cnt = new Container(new GridLayout(1, 2));
        cnt.addAll(btnQR, btnBar);

        form.add(SOUTH, cnt);
        form.show();
    }

    private Form getForm1() {

        Form form1 = proc.getInputForm();

        if (CodeScanner.isSupported() && CodeScanner.getInstance() != null) {

            CodeScanner.getInstance().scanQRCode(new ScanResult() {
                @Override
                public void scanCompleted(String contents,
                        String formatName, byte[] rawBytes) {
                    form1.add(new SpanLabel("Content\n" + contents
                            + "\n\nFormat Name\n" + formatName
                            + "\n\nRaw bytes\n" + rawBytes));
                }

                @Override
                public void scanCanceled() {
                    proc.showToast("Scanning Cancelled", FontImage.MATERIAL_INFO_OUTLINE).show();
                }

                @Override
                public void scanError(int errCode, String msg) {
                    proc.showToast("Error " + msg, FontImage.MATERIAL_INFO_OUTLINE).show();
                }

            });

            /*switch (Display.getInstance().getPlatformName()) {
                //Use CodeScanner in iOS - requires Barcode Scanner
                //3rd party app installed
                case "ios":
                    CodeScanner.getInstance().scanQRCode(new ScanResult() {
                        @Override
                        public void scanCompleted(String contents,
                                                  String formatName, byte[] rawBytes) {
                            form1.add(new SpanLabel("Content\n" + contents
                                    + "\n\nFormat Name\n" + formatName
                                    + "\n\nRaw bytes\n" + rawBytes));
                        }

                        @Override
                        public void scanCanceled() {
                            ToastBar.showErrorMessage("Scanning Cancelled");
                        }

                        @Override
                        public void scanError(int errCode, String msg) {
                            ToastBar.showErrorMessage("Error " + msg);
                        }

                    });
                    break;

                //Use QRScanner in android - no third party app required but
                //api level 21 required
                case "and":
                    QRScanner.scanQRCode(new ScanResult() {
                        @Override
                        public void scanCompleted(String contents,
                                                  String formatName, byte[] rawBytes) {
                            form1.add(new SpanLabel("Content\n" + contents
                                    + "\n\nFormat Name\n" + formatName
                                    + "\n\nRaw bytes\n" + rawBytes));
                        }

                        @Override
                        public void scanCanceled() {
                            ToastBar.showErrorMessage("Scanning Cancelled");
                        }

                        @Override
                        public void scanError(int errCode, String msg) {
                            ToastBar.showErrorMessage("Error " + msg);
                        }

                    });
                    break;
            }*/
        } else {
            proc.showToast("QR Code scanning not supported on this device",
                    FontImage.MATERIAL_INFO_OUTLINE).show();
        }
        return form1;
    }

    private Form getForm2() {

        Form form2 = proc.getInputForm();

        if (CodeScanner.isSupported()) {

            CodeScanner.getInstance().scanBarCode(new ScanResult() {
                @Override
                public void scanCompleted(String contents, String formatName,
                        byte[] rawBytes) {
                    form2.add(new SpanLabel("Content\n" + contents
                            + "\n\nformatName\n" + formatName
                            + "\n\nRaw bytes\n" + rawBytes));
                }

                @Override
                public void scanCanceled() {
                    proc.showToast("Scanning Cancelled", FontImage.MATERIAL_INFO_OUTLINE).show();
                }

                @Override
                public void scanError(int errCode, String msg) {
                    proc.showToast("Error " + msg, FontImage.MATERIAL_INFO_OUTLINE).show();
                }

            });
        } else {
            proc.showToast("Bar Code scanning not supported on this device",
                    FontImage.MATERIAL_INFO_OUTLINE).show();
        }
        return form2;
    }
}
