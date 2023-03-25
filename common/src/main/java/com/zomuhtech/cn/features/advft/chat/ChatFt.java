/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zomuhtech.cn.features.advft.chat;

import com.codename1.components.SpanLabel;
import com.codename1.components.ToastBar;
import com.codename1.io.Storage;
import com.codename1.ui.Button;
import com.codename1.ui.Command;
import com.codename1.ui.Component;
import com.codename1.ui.Container;
import com.codename1.ui.Display;
import com.codename1.ui.FontImage;
import com.codename1.ui.Form;
import com.codename1.ui.Image;
import com.codename1.ui.Label;
import com.codename1.ui.TextArea;
import com.codename1.ui.TextField;
import com.codename1.ui.layouts.BorderLayout;
import static com.codename1.ui.layouts.BorderLayout.SOUTH;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.FlowLayout;
import com.codename1.ui.table.TableLayout;
import com.zomuhtech.cn.features.procs.Proc;
import com.zomuhtech.cn.features.procs.RRHandler;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author Eric
 */
public class ChatFt extends Form implements RRHandler.RespHandler {

    Form form, prevForm;
    Proc proc;
    RRHandler rh;
    Message msgObj;
    Container chatArea, currSentChatArea;
    Label lblProgress, lblSent, lblTime;
    Image imgRecv = null;
    Container cntLast;
    Component cmpLast, sentCmp;
    String currMsgDate = "";
    boolean showMsgDate = false;

    public ChatFt(Form form) {

        this.prevForm = form;
        proc = new Proc();
        rh = new RRHandler();

        Display.getInstance().callSerially(() -> {
            createUI();
        });
    }

    public void createUI() {
        form = proc.getForm("Chat", prevForm);
        form.setLayout(new BorderLayout());
        if (!proc.getDarkMode().equals("On")) {
            form.getAllStyles().setBgColor(0xF0F3F4); //0xF0F3F4 3399ff 
        }
        form.add(CENTER, FlowLayout.encloseCenterMiddle(getChatDetails()));
        form.show();
    }

    private Container getChatDetails() {
        Container cntDetails = new Container(BoxLayout.y());

        Container cntPhone = proc.getInputCnt();
        TextField tfPhone = proc.getInputTf("Enter Phone", TextArea.PHONENUMBER);
        tfPhone.setText("0712345678");
        cntPhone.add(tfPhone);

        Container cntName = proc.getInputCnt();
        TextField tfName = proc.getInputTf("Enter Name", TextArea.ANY);
        tfName.setText("Eric");
        cntName.add(tfName);

        Button btn = proc.getInputBtn("Proceed");
        btn.addActionListener(e -> {
            String phone = tfPhone.getText();
            String name = tfName.getText();

            if (phone.length() == 0) {
                ToastBar.showErrorMessage("Enter Phone");
            } else if (name.length() == 0) {
                ToastBar.showErrorMessage("Enter Name");
            } else {
                showChatForm(prevForm, phone, name);
            }
        });

        cntDetails.add(cntPhone).add(cntName).add(btn);
        return cntDetails;

    }

    //void showChatForm(ContactData d, Component source) {
    private void showChatForm(Component source, String senderPhone,
            String senderName) {
        //form = proc.getForm(d.name, prevForm);
        //identify person chatting with, so incoming message know this is the 
        //right person

        //form.putClientProperty("cid", "senderName");//tokenPrefix+d.uniqueId
        // Toolbar tb = new Toolbar();
        chatArea = new Container(BoxLayout.y());
        chatArea.setScrollableY(true);
        chatArea.setName("ChatArea");

        //Get round pic of friends & cache it
        //Image roundImg = getRoundedImg(d.uniqueId, d.imageUrl);
        //Image roundImg = getRoundedImg(null, null);
        Image userImg = proc.materialIcon(FontImage.MATERIAL_ACCOUNT_CIRCLE);
//        try {
//            userImg = Image.createImage("/hse_sub_50.png");
//        } catch (IOException err) {
//
//        }

        //Load stored msgs & add them to the form
        /*java.util.List<Message> messages = (java.util.List<Message>) Storage
                .getInstance().readObject("chatAdmin"); //senderName recepientName tokenPrefix+d.uniqueId
         */
        java.util.List<Message> messages = (java.util.List<Message>) Storage
                .getInstance().readObject("ChatStore");

        if (messages != null) {

            for (Message m : messages) {
                /*proc.printLine("Msg = " + m.getMessage().trim());
                 */

                compareMsgDate(m.getMsgDate());

                //query sent msgs
                if ((m.getSenderPhone().trim().equals(senderPhone)
                        && m.getRecepientId().trim().equals("chatAdmin"))
                        //query received msgs
                        || (m.getSenderPhone().trim().equals("chatAdmin")
                        && m.getRecepientId().trim().equals(senderPhone))) {

                    if (m.getStatus().equals("recv")) {
                        recvLayout(chatArea, m.getMessage().trim(), userImg,
                                m.getMsgDate().trim(), m.getMsgTime().trim());
                    } else if (m.getStatus().equals("sent")) {
                        sentLayout(chatArea, m.getMessage().trim(), "sent",
                                m.getMsgDate().trim(), m.getMsgTime().trim());
                    }
                }
            }
        }

        form.revalidateLater();

        //Place image to right side of toolbar
        Command cmdImg = new Command("", userImg);
        form.getToolbar().addCommandToRightBar(cmdImg);

        //TextField tfMsg = new TextField(30);
        //tfMsg.setHint("Write to " + d.name);
        // tfMsg.setHint("Write to Msg");
        form.add(BorderLayout.CENTER, chatArea);
        //form.add(BorderLayout.SOUTH, tfMsg);

        //action listener for textfield creates msg object,convert it to json
        /*tfMsg.addActionListener((e) -> {
            String text = tfMsg.getText();
            final Component t = say(chatArea, text, "sending");

            //make outgoing msg translucent to indicate no received yet
            t.getUnselectedStyle().setOpacity(120);
            tfMsg.setText("");

            msgObj = new Message("sent", senderPhone, senderName, "chatAdmin",
                    "imgUrl", text);

            //JSONObject obj = msgObj.toJSON();
            //send & receive msg here
            sendMsg(senderPhone + "#" + senderName + "#" + "chatAdmin" + "#"
                    + "imgUrl" + "#" + text);
            //make received msg opaque & add it to storage
            t.getUnselectedStyle().setOpacity(255);
            //addMessage(msgObj);

        });*/
        Container cntChat = new Container(BoxLayout.y(), "cntChat");

        TableLayout tL = new TableLayout(1, 2);
        Container cntMsg = new Container(tL);

        Container cntTfMsg = new Container(BoxLayout.x(), proc.getTfMsgLayUIID());

        //TextComponent tfMsg = new TextComponent().multiline(true);
        //TextArea tfMsg  = new TextArea("message", 1,80);
        //tfMsg.setMaxSize(5000);
        TextField tfMsg = new TextField();
        tfMsg.setHint("Type a message");
        tfMsg.setUIID("tfMsg");
        tfMsg.getHintLabel().setUIID("tfMsgHint");
        tfMsg.setMaxSize(5000);
        tfMsg.setSingleLineTextArea(false);
        //tfMsg.setRows(2);
        tfMsg.setGrowByContent(true);
        tfMsg.setGrowLimit(4);
        tfMsg.setScrollVisible(true);
        tfMsg.addDataChangedListener((i1, i2) -> {
            //revalidate message container to resize on text length change
            cntTfMsg.revalidate();
        });
        cntTfMsg.add(FlowLayout.encloseLeftMiddle(tfMsg));

        //tfMsg.errorMessage("Text Message");

        /*Container cntTfMsg = new Container(BoxLayout.x(), "tfChatLay");
        cntTfMsg.add(tfMsg);*/
        Button btnSend = new Button(proc.materialIcon(FontImage.MATERIAL_SEND),
                "btnSend");
        btnSend.addActionListener(e -> {
            //String emoji = " 😋💪";
            String text = tfMsg.getText();

            compareMsgDate(proc.getCurrDate());
            sentCmp = say(chatArea, text, "sending",
                    proc.getCurrDate(), proc.getCurrTime());

            //make outgoing msg translucent to indicate no received yet
            sentCmp.getUnselectedStyle().setOpacity(120);
            tfMsg.setText("");
            cntTfMsg.revalidate();

            msgObj = new Message("sent", senderPhone, senderName, "chatAdmin",
                    "imgUrl", text, proc.getCurrDate(), proc.getCurrTime());

            //JSONObject obj = msgObj.toJSON();
            //send & receive msg here
            String data = "VAL1>" + senderPhone
                    + ">VAL2>" + senderName
                    + ">VAL3>" + "chatAdmin"
                    + ">VAL4>" + "imgUrl"
                    + ">VAL5>" + text
                    + ">VAL6>" + proc.getCurrDate()
                    + ">VAL7>" + proc.getCurrTime();
            sendMsg(senderPhone, data);
        });
        /*cntMsg.add(tL.createConstraint().widthPercentage(80),
                FlowLayout.encloseCenterMiddle(cntTfMsg));*/
        cntMsg.add(tL.createConstraint().widthPercentage(80),cntTfMsg);
        cntMsg.add(tL.createConstraint().widthPercentage(20),
                FlowLayout.encloseCenterMiddle(btnSend));

        cntChat.add(cntMsg);

        form.add(BorderLayout.SOUTH, cntChat);
        form.revalidate();

        //cntLast.animateLayoutAndWait(300);
        //cntLast.scrollComponentToVisible(cmpLast);
        chatArea.animateLayoutAndWait(300);
        chatArea.scrollComponentToVisible(cmpLast);

    }

    private void compareMsgDate(String msgDate) {
        //1/2   1/2    1/2   2/2
        if (!msgDate.equals(currMsgDate)) {
            currMsgDate = msgDate;
            showMsgDate = true;
        } else {
            currMsgDate = msgDate;
            showMsgDate = false;
        }
    }

    private void sendMsg(String userId, String msg) {

        String data = "Chat>" + msg.trim();
        //String data = "Chat>" + msg.trim() + emoji;
        //data = "'" + data + "'";
        rh.processNetTkn(this, userId, data, "chat");
    }

    @Override
    public void getResp(String resp, String taskTag) {

        proc.printLine(resp);

        String[] resArr = proc.splitValue(resp, ">");
        switch (resArr[0]) {
            case "SUCCESS":
                //Store sent
                addMessage(msgObj);

                //Store received
                //chatAdmin#chatAdmin#0712345678#Received hello
                String[] respArr = proc.splitValue(resArr[1], "#");

                Message recvMsgObj = new Message("recv", respArr[0], respArr[1],
                        respArr[2], "imgUrl", respArr[3], proc.getCurrDate(),
                        proc.getCurrTime());

                addMessage(recvMsgObj);

                compareMsgDate(recvMsgObj.getMsgDate());

                recvLayout(chatArea, recvMsgObj.getMessage(), null,
                        recvMsgObj.getMsgDate(), recvMsgObj.getMsgTime());

                lblProgress.setText("");
                lblSent.setIcon(imgRecv);
                lblSent.setVisible(true);
                lblTime.setVisible(true);
                sentCmp.getUnselectedStyle().setOpacity(255);
                form.revalidateLater();
                break;
            case "FAIL":
                lblProgress.setText("not sent");
                lblSent.setIcon(proc.chatFailIcon(FontImage.MATERIAL_WARNING));
                lblSent.setVisible(true);
                lblTime.setVisible(true);
                sentCmp.getUnselectedStyle().setOpacity(255);
                form.revalidateLater();

                //proc.showToast(resArr[1], FontImage.MATERIAL_ERROR).show();
                break;
        }

    }

    private void addMessage(Message m) {

        /*msgObj = new Message("sent", senderPhone, senderName, "chatAdmin",
                    "imgUrl", text);*/
        java.util.List messages = (java.util.List) Storage.getInstance()
                .readObject("ChatStore");

        if (messages == null) {
            messages = new ArrayList();
        }
        messages.add(m);

        Storage.getInstance().writeObject("ChatStore", messages);
    }

    private void scrollToLast(Container cnt, Component cmp) {

        cnt.animateLayoutAndWait(300);
        cnt.scrollComponentToVisible(cmp);

        this.cntLast = cnt;
        this.cmpLast = cmp;
    }

    private Container getDateCnt(String msgDate) {
        Container cnt = new Container(new BorderLayout());
        Label lbl = new Label(msgDate, "lblMsgDate");
        cnt.add(CENTER, FlowLayout.encloseCenterMiddle(lbl));
        return cnt;
    }

    private Component say(Container chatArea, String text, String sendStatus,
            String msgDate, String msgTime) {
        Component t = sentLayout(chatArea, text, sendStatus, msgDate, msgTime);
        //t.setY(chatArea.getHeight());
        //t.setWidth(chatArea.getWidth());
        //t.setHeight(40);
        //chatArea.animateLayoutAndWait(300);
        //chatArea.scrollComponentToVisible(t);
        //scrollToLast(chatArea, t);
        return t;
    }

    private Component sentLayout(Container chatArea, String text,
            String sendStatus, String msgDate, String msgTime) {

        Container cntPar = new Container(new BorderLayout(), "cntParSent");
//         Container cntPar = new Container(new BorderLayout(), 
//                 "ChatBubbleSpanLabelOwn");

        Container cntPar_ = new Container(BoxLayout.y());
        //Container cntBg = new Container(new BorderLayout(), "cntBg");
        Container cntBg = new Container(new BorderLayout(),
                "ChatBubbleSpanLabelOwn");
        Image img, imgSent = null;
        try {
            img = Image.createImage("/chat_bubble_sent.png");
            imgSent = Image.createImage("/msg_sent.png");
            //imgRecv = Image.createImage("/msg_delivered.png");
            imgRecv = proc.chatDeliveredIcon(FontImage.MATERIAL_CHECK);
            //cntBg.getAllStyles().setBgImage(img);

        } catch (IOException ex) {

        }

        SpanLabel lblMsg = new SpanLabel(text, "lblSentMsg");
        //SpanLabel lblMsg = new SpanLabel(text, "ChatBubbleSpanLabelOwn");
        //Label lblMsg = new Label(text, "lblSentMsg");

        Container cntStatus = new Container(BoxLayout.x(), "cntStatus");
        lblProgress = new Label("sending...", "lblSentStatus");
        lblTime = new Label(msgTime, "lblSentStatus");
        lblSent = new Label(imgRecv, "lblSentStatus");
        //Label lblRead = new Label(imgRecv, "lblSentStatus");
        //cntStatus.addAll(lblProgress, lblTime, lblSent, lblRead);
        cntStatus.addAll(lblProgress, lblTime, lblSent);

        if (sendStatus.equals("sent")) {
            lblProgress.setVisible(false);
            //lblTime.setText(msgTime);
            //lblTime.setVisible(true);
            lblSent.setVisible(true);
            //lblRead.setVisible(true);
        } else {
            lblProgress.setVisible(true);
            currSentChatArea = chatArea;
            //lblTime.setVisible(false);
            lblSent.setVisible(false);
            //lblRead.setVisible(false);
        }

        cntBg.add(CENTER, FlowLayout.encloseLeftMiddle(lblMsg));
        cntBg.add(SOUTH, FlowLayout.encloseRightMiddle(cntStatus));

        //cntPar.add(EAST, FlowLayout.encloseRightMiddle(cntBg));
        cntPar_.add(cntBg);
        //cntPar.add(EAST, cntPar_);
        cntPar.add(CENTER, FlowLayout.encloseRightMiddle(cntPar_));

        //cmpLast = cntPar;
        if (showMsgDate) {
            chatArea.add(getDateCnt(proc.checkDate(msgDate)));
        }
        chatArea.addComponent(cntPar);
        //chatArea.scrollComponentToVisible(cntPar);
        scrollToLast(chatArea, cntPar);
        return cntPar;
    }

    private Component recvLayout(Container chatArea, String text,
            Image roundImg, String msgDate, String msgTime) {
        /*SpanLabel lbl = new SpanLabel(text);
        lbl.setIcon(roundImg);
        try {
            lbl.getAllStyles().setBgImage(Image.createImage("/chat_bubble_recv.png"));
        } catch (IOException ex) {

        }
        lbl.setIconPosition(EAST);
        lbl.setTextUIID("BubbleThem");
        lbl.setTextBlockAlign(Component.RIGHT);
        chatArea.addComponent(lbl);
        chatArea.scrollComponentToVisible(lbl);
        return lbl;*/

        Container cntPar = new Container(new BorderLayout(), "cntParSent");
//        Container cntPar = new Container(new BorderLayout(), 
//                "ChatBubbleSpanLabelOther");

        Container cntPar_ = new Container(BoxLayout.y());
        //Container cntBg = new Container(new BorderLayout(), "cntBg");
        Container cntBg = new Container(new BorderLayout(),
                "ChatBubbleSpanLabelOther");
       
        /*try {
            Image img = Image.createImage("/chat_bubble_recv.png");
            //img = img.scaledLargerRatio(100, 100)
            //cntBg.getAllStyles() .setBgImage(img);
        } catch (IOException ex) {

        }*/

        SpanLabel lblMsg = new SpanLabel(text, "lblRecvMsg");
        //SpanLabel lblMsg = new SpanLabel(text, "ChatBubbleSpanLabelOther");

        Container cntStatus = new Container(BoxLayout.x(), "cntStatus");
        lblTime = new Label(msgTime, "lblRecvStatus");
        cntStatus.addAll(lblTime);

        cntBg.add(BorderLayout.CENTER, FlowLayout.encloseLeftMiddle(lblMsg));
        cntBg.add(BorderLayout.SOUTH, FlowLayout.encloseRightMiddle(cntStatus));

        //cntPar.add(EAST, FlowLayout.encloseRightMiddle(cntBg));
        cntPar_.add(cntBg);
        cntPar.add(BorderLayout.WEST, cntPar_);

        //cmpLast = cntPar;
        if (showMsgDate) {
            chatArea.add(getDateCnt(proc.checkDate(msgDate)));
        }
        chatArea.addComponent(cntPar);
        //chatArea.scrollComponentToVisible(cntPar);
        scrollToLast(chatArea, cntPar);
        return cntPar;
    }

    /*private EncodedImage getRoundedImg(String uid, String imgUrl) {
        EncodedImage roundImg = roundedImagesOfFriends.get(uid);
        if (roundImg == null) {
            roundImg = URLImage.createToStorage(roundPlaceHolder,
                    "rounded" + uid, imgUrl, URLImage.createMaskAdapter(mask));
            roundedImagesOfFriends.put(uid, roundImg);
        }
        return roundImg;
        return null;
    }*/

 /*private void respond(Container chatArea, String text, Image roundImg) {
        Component ans = recvLayout(chatArea, text, roundImg);
        ans.setX(chatArea.getWidth());
        ans.setWidth(chatArea.getWidth());
        ans.setHeight(40);
        chatArea.animateLayoutAndWait(300);
        chatArea.scrollComponentToVisible(ans);
    }*/
 /*private void respond(Message m) {
        String clientId = (String) Display.getInstance().getCurrent()
                .getClientProperty("cid");
        addMessage(m);

        EncodedImage rounded = getRoundedImg(m.getSenderPhone(), m.getPicture());
        if (clientId == null || clientId.equals(m.getSenderPhone())) {
            //show toast we arent in chat form
            InteractionDialog toast = new InteractionDialog();
            toast.setUIID("Container");
            toast.setLayout(new BorderLayout());

            SpanButton btnMsg = new SpanButton(m.getMessage());
            btnMsg.setIcon(rounded);

            toast.addComponent(CENTER, btnMsg);
            int h = toast.getPreferredH();
            toast.show(Display.getInstance().getDisplayHeight() - h - 10, 10,
                    10, 10);

            UITimer timer = new UITimer(() -> {
                toast.dispose();
            });
            timer.schedule(3000, false, Display.getInstance().getCurrent());

            btnMsg.addActionListener((e) -> {
                timer.cancel();
                toast.dispose();
                //showChatForm(getContactId(m.getSenderId()),
                        //Display.getInstance().getCurrent());
            });
        } else {
            Container chatArea = getChatArea(Display.getInstance().getCurrent()
                    .getContentPane());
            respond(chatArea, m.getMessage(), rounded);
        }
    }*/

 /*private Container getChatArea(Container cnt) {
        String n = cnt.getName();
        if (n != null && n.equals("ChatArea")) {
            return cnt;
        }

        for (Component cmp : cnt) {
            if (cmp instanceof Container) {
                Container cur = getChatArea((Container) cmp);
                if (cur != null) {
                    return cur;
                }
            }
        }
        return null;
    }*/
}
