/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zomuhtech.cn.features.advft;

import ca.weblite.codename1.json.JSONException;
import ca.weblite.codename1.json.JSONObject;
import com.codename1.components.SpanLabel;
import com.codename1.components.ToastBar;
import com.codename1.ext.filechooser.FileChooser;
import com.codename1.io.MultipartRequest;
import com.codename1.io.NetworkManager;
import com.codename1.io.Socket;
import com.codename1.io.SocketConnection;
import com.codename1.ui.Button;
import com.codename1.ui.ButtonGroup;
import com.codename1.ui.Container;
import com.codename1.ui.Display;
import com.codename1.ui.Form;
import com.codename1.ui.Label;
import com.codename1.ui.RadioButton;
import com.codename1.ui.TextArea;
import com.codename1.ui.TextField;
import com.codename1.ui.layouts.BorderLayout;
import static com.codename1.ui.layouts.BorderLayout.NORTH;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.GridLayout;
import com.zomuhtech.cn.features.procs.Proc;
import com.zomuhtech.cn.features.procs.RRHandler;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import com.codename1.io.Util;
import com.codename1.ui.FontImage;
import com.codename1.ui.layouts.FlowLayout;
import com.codename1.ui.plaf.DefaultLookAndFeel;
import com.codename1.ui.plaf.Style;
import com.codename1.ui.plaf.UIManager;
import com.zomuhtech.cn.features.procs.UnplainT;
import java.util.ArrayList;

/**
 *
 * @author Eric
 */
public class NetworkingFt extends Form implements RRHandler.RespHandler {

    Form form, prevForm;
    static Form form2;
    Proc proc;
    RRHandler rh;
    String group, filePath, selFileExt;
    static InputStream inputStream;
    static OutputStream outputStream;
    static InputStream inputStream2;
    static OutputStream outputStream2;
    SpanLabel lblGZ;
    ArrayList<Button> btnArr;
    UnplainT unPlainT = new UnplainT();

    public NetworkingFt(Form form) {
        this.prevForm = form;
        this.btnArr = new ArrayList<>();
        proc = new Proc();
        rh = new RRHandler();
        Display.getInstance().callSerially(() -> {
            createUI();
        });

    }

    public void createUI() {
        form = proc.getForm("Networking", prevForm);
        form.setLayout(new BorderLayout());

        Button btnForm1 = new Button("HTTP GET,POST,HEADER", "btnNav");
        btnArr.add(btnForm1);

        form.add(CENTER, getForm1());

        btnForm1.addActionListener(e -> {
            proc.changeBtnUIID(btnArr, btnForm1);
            form.add(CENTER, getForm1());
            form.revalidate();
        });

        Button btnForm2 = new Button("SOCKETS", "btnNav");
        btnArr.add(btnForm2);
        btnForm2.addActionListener(e -> {
            proc.changeBtnUIID(btnArr, btnForm2);
            form.add(CENTER, getForm2());
            form.revalidate();
        });

        Container cnt = new Container(BoxLayout.x());
        cnt.addAll(btnForm1, btnForm2);

        Button btnForm3 = new Button("MULTIPART", "btnNav");
        btnArr.add(btnForm3);
        btnForm3.addActionListener(e -> {
            proc.changeBtnUIID(btnArr, btnForm3);
            form.add(CENTER, getForm3());
            form.revalidate();
        });

        Button btnForm4 = new Button("ENCODE DECODE", "btnNav");
        btnArr.add(btnForm4);
        btnForm4.addActionListener(e -> {
            proc.changeBtnUIID(btnArr, btnForm4);
            form.add(CENTER, getForm4());
            form.revalidate();
        });

        Button btnForm5 = new Button("GZIP", "btnNav");
        btnArr.add(btnForm5);
        btnForm5.addActionListener(e -> {
            proc.changeBtnUIID(btnArr, btnForm5);
            form.add(CENTER, getForm5());
            form.revalidate();
            //ToastBar.showInfoMessage("Coming soon");
        });

        Button btnForm6 = new Button("JSON", "btnNav");
        btnArr.add(btnForm6);
        btnForm6.addActionListener(e -> {
            proc.changeBtnUIID(btnArr, btnForm6);
            form.add(CENTER, getForm6());
            form.revalidate();
        });

        Button btnForm7 = new Button("REST", "btnNav");
        btnArr.add(btnForm7);
        btnForm7.addActionListener(e -> {
            proc.changeBtnUIID(btnArr, btnForm7);
            form.add(CENTER, getForm7());
            form.revalidate();
        });

        Button btnForm8 = new Button("PDF Doc", "btnNav");
        btnArr.add(btnForm8);
        btnForm8.addActionListener(e -> {
            proc.changeBtnUIID(btnArr, btnForm8);
            //form.add(CENTER, getForm8());
            ToastBar.showInfoMessage("Coming soon");
            form.revalidate();
        });

        //selected by default
        proc.changeBtnUIID(btnArr, btnForm1);

        Container cnt2 = new Container(BoxLayout.x());
        cnt2.addAll(btnForm3, btnForm4, btnForm5);

        Container cnt3 = new Container(BoxLayout.x());
        cnt3.addAll(btnForm6, FlowLayout.encloseLeftMiddle(btnForm7), btnForm8);

        Container cntPar = new Container(BoxLayout.y());
        cntPar.addAll(cnt, cnt2, cnt3);

        form.add(NORTH, cntPar);
        form.show();
    }

    private Form getForm1() {

        Form form1 = proc.getInputForm();

        Container cntPhone = proc.getInputCnt();
        TextField tfPhone = proc.getInputTf("Enter Phone", TextArea.PHONENUMBER);
        cntPhone.add(tfPhone);

        Container cntName = proc.getInputCnt();
        TextField tfName = proc.getInputTf("Name", TextArea.ANY);
        cntName.add(tfName);

        Container cntAge = proc.getInputCnt();
        TextField tfAge = proc.getInputTf("Age", TextArea.NUMERIC);
        cntAge.add(tfAge);

        Container cntGrp = new Container(new GridLayout(1, 2), "cntPar");

        Style s;
        if (Display.getInstance().isTablet()) {
            s = UIManager.getInstance().getComponentStyle("radTab"); //button
        } else {
            s = UIManager.getInstance().getComponentStyle("rad");
        }
        FontImage radEmptyImg = FontImage.createMaterial(
                FontImage.MATERIAL_RADIO_BUTTON_UNCHECKED, s);
        FontImage radFullImg = FontImage.createMaterial(
                FontImage.MATERIAL_RADIO_BUTTON_CHECKED, s);
        ((DefaultLookAndFeel) UIManager.getInstance().getLookAndFeel())
                .setRadioButtonImages(radFullImg, radEmptyImg, radFullImg,
                        radEmptyImg);

        RadioButton radG1 = new RadioButton("Group 1");
        radG1.setUIID(proc.getRadioUIID());
        RadioButton radG2 = new RadioButton("Group 2");
        radG2.setUIID(proc.getRadioUIID());
        ButtonGroup bg = new ButtonGroup(radG1, radG2);
        bg.addActionListener(e -> {
            RadioButton rad = bg.getRadioButton(bg.getSelectedIndex());
            if (rad.getText().equals("Group 1")) {
                group = "Grp1";
            } else if (rad.getText().equals("Group 2")) {
                group = "Grp2";
            }
        });
        cntGrp.add(radG1).add(radG2);

        Button btnSignUp = proc.getInputBtn("Sign Up");
        btnSignUp.addActionListener(e -> {

            String phone = tfPhone.getText();
            String name = tfName.getText();
            String age = tfAge.getText();

            sendData("SignUp", name, age, phone, "Signing Up");
        });

        Button btnSignIn = proc.getInputBtn("Sign In");
        btnSignIn.addActionListener(e -> {

            String phone = tfPhone.getText();
            String name = tfName.getText();
            String age = tfAge.getText();

            sendData("SignIn", name, age, phone, "Signing In");
        });

        form1.addAll(cntPhone, cntName, cntAge, cntGrp, btnSignUp, btnSignIn);
        return form1;
    }

    //Send GET,POST requests with header
    private void sendData(String opt, String name, String age, String phone,
            String msg) {

        String data = group + ">" + opt
                + ">VAL1>" + name
                + ">VAL2>" + age
                + ">VAL3>" + phone;

        proc.progressForm(msg).show();
        rh.processNetTkn(this, phone, data, "networking");

    }

    private Form getForm2() {
        form2 = proc.getInputForm();

        TextField tfHost = new TextField("127.0.0.1");
        Button btnCreate = new Button("Create Server", "btnNav");
        btnCreate.addActionListener(e -> {
            form2.addComponent(new Label("Listening: " + Socket.getHostOrIP()));
            form2.revalidate();
            Socket.listen(54321, SocketListenerCallback.class);

        });

        Button btnConnect = new Button("Connect", "btnNav");
        btnConnect.addActionListener(e -> {
            Socket.connect(tfHost.getText(), 54321, new SocketConnection() {

                @Override
                public void connectionError(int errCode, String msg) {
                    proc.printLine("Connect Error " + msg);
                }

                @Override
                public void connectionEstablished(InputStream is,
                        OutputStream os) {
                    inputStream2 = is;
                    outputStream2 = os;

                    try {
                        /*int count = 1;
                        while (isConnected()) {
                            os.write(("Hi: " + count).getBytes());
                            count++;
                            Thread.sleep(2000);
                        }*/

                        byte[] b = ("Hi Server\n").getBytes();
                        os.write(b);

                        byte[] buffer = new byte[8192];
                        while (isConnected()) {
                            int pending = is.available();
                            if (pending > 0) {
                                int size = is.read(buffer, 0, 8192);
                                if (size == -1) {
                                    return;
                                }
                                if (size > 0) {
                                    ToastBar.showInfoMessage(new String(buffer, 0, size));
                                }
                            } else {
                                Thread.sleep(50);
                            }
                        }
                    } catch (Exception err) {
                        proc.printLine(err.getMessage());
                    }

                }
            });
        });
        form2.setLayout(BoxLayout.y());

        Button btnStop = new Button("Disconnect", "btnNav");
        btnStop.addActionListener(e -> {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
                if (inputStream2 != null) {
                    inputStream2.close();
                }
                if (outputStream2 != null) {
                    outputStream2.close();
                }
            } catch (IOException err) {

            }

            ToastBar.showInfoMessage("Disconnected");
        });

        Container cnt = new Container(BoxLayout.x());
        cnt.add(btnCreate).add(btnConnect).add(btnStop);
        form2.add(cnt).add(tfHost);

        return form2;
    }

    public static class SocketListenerCallback extends SocketConnection {

        private Label lblConn;

        @Override
        public void connectionError(int errCode, String msg) {
            // proc.printLine("SLC Error " + msg);
        }

        @Override
        public void connectionEstablished(InputStream is, OutputStream os) {
            inputStream = is;
            outputStream = os;
            try {
                byte[] buffer = new byte[8192];
                while (isConnected()) {
                    int pending = is.available();
                    if (pending > 0) {
                        int size = is.read(buffer, 0, 8192);
                        if (size == -1) {
                            return;
                        }
                        if (size > 0) {
                            updateLabel(new String(buffer, 0, size));

                            /*Display.getInstance().callSerially(() -> {
                                try {
                                    os.write(("Server says hi too\n").getBytes());
                                } catch (IOException ex) {

                                }
                            });*/
                        }
                    } else {
                        Thread.sleep(50);
                    }

                }

                //os.write(("Server says hi too\n").getBytes());
            } catch (Exception err) {
                //proc.printLine("SLC Exc" + err.getMessage());
            }
        }

        private void updateLabel(String value) {
            Display.getInstance().callSerially(() -> {

                ToastBar.showInfoMessage("Server received " + value);
                try {
                    outputStream.write(("Server says hi too\n").getBytes());
                } catch (IOException ex) {

                }
                /*if (lblConn == null) {
                    lblConn = new Label(value);
                    Display.getInstance().getCurrent()
                            .addComponent(CENTER, lblConn);
                } else {
                    lblConn.setText(value);
                }
                Display.getInstance().getCurrent().revalidate();*/
            });

        }
    }

    private Form getForm3() {
        Form form3 = proc.getInputForm();

        Button btnBrw = new Button("Browse File", "btnNav");
        Label lblPath = new Label("path", "lblInput");

        btnBrw.addActionListener(e -> {

            if (FileChooser.isAvailable()) {

                FileChooser.showOpenDialog(proc.getFileExtMime(), ev -> {

                    if (ev == null) {
                        ToastBar.showErrorMessage("no file selected");
                    } else {
                        filePath = (String) ev.getSource();
                        selFileExt = filePath.substring(filePath.length() - 3);
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
                        proc.printLine("Path " + filePath);
                        proc.printLine("Ext " + selFileExt);
                        lblPath.setText(filePath);
                    }
                });
            }
        });

        Container cnt = new Container(BoxLayout.x());
        cnt.add(btnBrw).add(lblPath);

        TextField tfName = proc.getInputTf("Enter file name", TextArea.ANY);
        Container cntBorder = new Container(BoxLayout.y(), "cntBorder");

        Button btnUpload = proc.getInputBtn("Upload");
        btnUpload.addActionListener(e -> {
            String fileName = tfName.getText();

            if (fileName.isEmpty()) {
                ToastBar.showErrorMessage("Enter file name");
            } else {

                String reqData = unPlainT.unPlain(fileName);
                proc.printLine("EncReq " + reqData);
                try {
                    btnUpload.setText("Uploading...");
                    btnUpload.setEnabled(false);

                    MultipartRequest request = new MultipartRequest();

                    if (rh.prod) {
                        request.setUrl("https://cn.adv.ft.zomuh-tech.com/all_file_upload");
                        //request.setUrl("http://192.168.43.182/AdvanceFt/all_file_upload");

                    } else {
                        request.setUrl("http://127.0.0.1/AdvanceFt/all_file_upload");
                    }

                    //request.addArgument("reqData", fileName);
                    request.addArgumentNoEncoding("reqData", reqData);
                    request.addData("file", filePath, selFileExt);
                    request.setFilename("file", fileName + "." + selFileExt);

                    NetworkManager.getInstance().addToQueueAndWait(request);

                    proc.printLine("RespCode " + request.getResponseCode());
                    byte[] respArr = request.getResponseData();
                    String response = new String(respArr).trim();
                    proc.printLine("EncResp " + response);

                    //String resp = response;
                    String resp = unPlainT.plainT(response);
                    if (request.getResponseCode() == 200) {

                        ToastBar.showInfoMessage("Resp " + resp);
                        btnUpload.setText("Upload");
                        btnUpload.setEnabled(true);
                        form3.revalidate();
                    } else {
                        ToastBar.showInfoMessage("Resp " + resp);
                        btnUpload.setText("Upload");
                        btnUpload.setEnabled(true);
                        form3.revalidate();
                    }

                } catch (IOException err) {
                    proc.printLine("Upload Err " + err);
                }
            }
        });
        form3.addAll(cnt, tfName, cntBorder, btnUpload);
        return form3;
    }

    private Form getForm4() {

        Form form4 = proc.getInputForm();

        Container cntContent = proc.getInputCnt();
        TextField tfContent = proc.getInputTf("Type text here", TextArea.ANY);
        cntContent.add(tfContent);

        Container cntEncoded = proc.getInputCnt();
        TextArea taEncoded = proc.getInputTf("", TextArea.ANY);
        cntEncoded.add(taEncoded);

        SpanLabel lblDecoded = new SpanLabel();
        form4.addAll(cntContent, cntEncoded, lblDecoded);

        tfContent.addDataChangedListener((a, b) -> {
            String content = tfContent.getText();
            String enc = Util.xorEncode(content);
            taEncoded.setText(enc);
            lblDecoded.setText(Util.xorDecode(enc));
            form4.getContentPane().animateLayout(100);
        });

        return form4;
    }

    private Form getForm5() {

        Form form5 = proc.getInputForm();

        Container cntName = proc.getInputCnt();
        TextField tfName = proc.getInputTf("Name", TextArea.ANY);
        cntName.add(tfName);

        Container cntAge = proc.getInputCnt();
        TextField tfAge = proc.getInputTf("Age", TextArea.NUMERIC);
        cntAge.add(tfAge);

        Button btnSend = proc.getInputBtn("Send");
        btnSend.addActionListener(e -> {

            String name = tfName.getText();
            String age = tfAge.getText();

            String data = "GZ>" + name + "#" + age;

            rh.processGZReq(this, data, "GZ");
        });

        lblGZ = new SpanLabel("", "lblInput");
        //.addAll(cntName, cntAge, btnSend, new Label("Response "), lblGZ);
        form5.add(FlowLayout.encloseCenterMiddle(new Label("Coming soon",
                "lblInput")));

        return form5;
    }

    private Form getForm6() {

        Form form6 = proc.getInputForm();

        Container cntPhone = proc.getInputCnt();
        TextField tfPhone = proc.getInputTf("Enter Phone", TextArea.PHONENUMBER);
        cntPhone.add(tfPhone);

        Container cntName = proc.getInputCnt();
        TextField tfName = proc.getInputTf("Name", TextArea.ANY);
        cntName.add(tfName);

        Container cntHobby = proc.getInputCnt();
        TextField tfHobby = proc.getInputTf("Hobby", TextArea.ANY);
        cntHobby.add(tfHobby);

        Button btnSend = proc.getInputBtn("Send");
        btnSend.addActionListener(e -> {

            String phone = tfPhone.getText();
            String name = tfName.getText();
            String hobby = tfHobby.getText();

            try {

                JSONObject obj = new JSONObject();
                obj.put("VAL1", phone);
                obj.put("VAL2", name);
                obj.put("VAL3", hobby);

                String data = "JsonObj>" + obj.toString();

                proc.progressForm("Processing JSON data, please wait").show();
                rh.processNetTkn(this, phone, data, "json");

            } catch (JSONException err) {

            }
        });

        form6.addAll(cntPhone, cntName, cntHobby, btnSend);
        return form6;
    }

    private Form getForm7() {

        Form form7 = proc.getInputForm();

        Container cntPhone = proc.getInputCnt();
        TextField tfPhone = proc.getInputTf("Enter Phone", TextArea.PHONENUMBER);
        cntPhone.add(tfPhone);

        Container cntName = proc.getInputCnt();
        TextField tfName = proc.getInputTf("Name", TextArea.ANY);
        cntName.add(tfName);

        Button btnSend = proc.getInputBtn("Send");
        btnSend.addActionListener(e -> {

            String phone = tfPhone.getText();
            String name = tfName.getText();

            try {

                JSONObject obj = new JSONObject();
                obj.put("VAL1", phone);
                obj.put("VAL2", name);

                String data = "Rest>" + obj.toString();

                proc.progressForm("Processing via REST API").show();
                rh.processNetTkn(this, phone, data, "rest");

            } catch (JSONException err) {

            }
        });

        form7.addAll(cntPhone, cntName, btnSend);
        return form7;
    }

    private Form getForm8() {

        Form form8 = proc.getInputForm();

        Container cntPhone = proc.getInputCnt();
        TextField tfPhone = proc.getInputTf("Enter Phone", TextArea.PHONENUMBER);
        cntPhone.add(tfPhone);

        Container cntName = proc.getInputCnt();
        TextField tfName = proc.getInputTf("Name", TextArea.ANY);
        cntName.add(tfName);

        Button btnSend = proc.getInputBtn("Send");
        btnSend.addActionListener(e -> {

            String phone = tfPhone.getText();
            String name = tfName.getText();

            String data = "Pdf"
                    + ">VAL1>" + phone
                    + ">VAL2>" + name;

            proc.progressForm("Generating, please wait").show();
            rh.processNetTkn(this, phone, data, "Pdf");

        });

        form8.addAll(cntPhone, cntName, btnSend);
        return form8;
    }

    @Override
    public void getResp(String resp, String taskTag) {
        switch (taskTag) {
            case "networking":
            case "json":
            case "rest":
            case "Pdf":
                String[] respArr = proc.splitValue(resp, ">");
                switch (respArr[0]) {
                    case "SUCCESS":
                        proc.statusForm(respArr[1], "S", form, prevForm).show();
                        break;
                    case "FAIL":
                        proc.statusForm(respArr[1], "F", form, prevForm).show();
                        break;
                }
                break;

            case "GZ":
                lblGZ.setText(resp);
                break;

//            case "Pdf":
//                ToastBar.showInfoMessage("Resp " + resp);
//                break;
            default:
                ToastBar.showInfoMessage("Resp " + resp);
                break;
        }
    }
}
