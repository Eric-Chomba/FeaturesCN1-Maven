/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zomuhtech.cn.features.advft.chat;

import com.codename1.capture.Capture;
import com.codename1.components.ToastBar;
import com.codename1.io.File;
import com.codename1.io.FileSystemStorage;
import com.codename1.io.Storage;
import com.codename1.io.Util;
import com.codename1.rad.controllers.Controller;
import com.codename1.rad.controllers.FormController;
import com.codename1.rad.models.Entity;
import com.codename1.rad.models.EntityList;
import com.codename1.rad.nodes.ActionNode;
import com.codename1.rad.nodes.ViewNode;
import com.codename1.rad.schemas.ChatMessage;
import com.codename1.rad.schemas.ChatRoom;
import com.codename1.rad.schemas.Person;
import com.codename1.rad.ui.UI;
import static com.codename1.rad.ui.UI.action;
import static com.codename1.rad.ui.UI.actions;
import static com.codename1.rad.ui.UI.condition;
import static com.codename1.rad.ui.UI.enabledCondition;
import static com.codename1.rad.ui.UI.icon;
import static com.codename1.rad.ui.UI.selected;
import static com.codename1.rad.ui.UI.selectedCondition;
import static com.codename1.rad.ui.UI.uiid;
import com.codename1.rad.ui.chatroom.ChatBubbleView;
import com.codename1.rad.ui.chatroom.ChatRoomView;
import com.codename1.rad.ui.entityviews.ProfileAvatarView;
import com.codename1.ui.Button;
import com.codename1.ui.CN;
import static com.codename1.ui.CN.CENTER;
import com.codename1.ui.Command;
import com.codename1.ui.Component;
import com.codename1.ui.Container;
import com.codename1.ui.Dialog;
import com.codename1.ui.Display;
import com.codename1.ui.FontImage;
import com.codename1.ui.Form;
import com.codename1.ui.Image;
import com.codename1.ui.Label;
import com.codename1.ui.TextArea;
import com.codename1.ui.TextField;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.FlowLayout;
import com.codename1.ui.table.TableLayout;
import com.codename1.ui.util.SwipeBackSupport;
import com.zomuhtech.cn.features.procs.Proc;
import com.zomuhtech.cn.features.procs.RRHandler;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author Eric
 */
public class ChatController extends FormController
        implements RRHandler.RespHandler {

    Form form, prevForm;
    Proc proc;
    RRHandler rh;
    Message msgObj;
    Container chatArea, currSentChatArea;
    Label lblProgress, lblSent, lblTime;
    Image imgRecv = null;
    Component cmpLast;
    String senderPhone, senderName;

    public static final ActionNode send = action(
            //disable send button when text is empty
            enabledCondition(entity -> {
                return !entity.isEmpty(ChatRoom.inputBuffer);
            }),
            icon(FontImage.MATERIAL_SEND));

    public static final ActionNode phone = action(
            icon(FontImage.MATERIAL_PHONE),
            //disable popup menu call option if has no phone
            /*enabledCondition(entity -> {
                return CN.canDial() && !entity.isEmpty(Person.telephone);
            })*/
            //hide popup menu call option if has no phone
            condition(entity -> {
                return CN.canDial() && !entity.isEmpty(Person.telephone);
            })
    );

    public static final ActionNode video = action(
            icon(FontImage.MATERIAL_VIDEOCAM));

    public static final ActionNode likedBadge = UI.action(
            UI.uiid("ChatBubbleLikedBadge"),
            icon(FontImage.MATERIAL_FAVORITE),
            //only show badge if msg is liked
            condition(entity -> {
                //determine if msg is liked, falsey returns null,"", 0 or false
                return !entity.isFalsey(ChatMessage.isFavorite);
            })
    );

    public static final ActionNode likeAction = UI.action(
            icon(FontImage.MATERIAL_FAVORITE_OUTLINE),
            uiid("LikeButton"),
            //set different icon when action is selected
            selected(icon(FontImage.MATERIAL_FAVORITE)),
            //set action selected only when message is liked
            selectedCondition(entity -> {
                return !entity.isFalsey(ChatMessage.isFavorite);
            })
    );

    public static final ActionNode capturePhoto = action(
            icon(FontImage.MATERIAL_CAMERA));

    public ChatController(Controller parent, Form form) {
        super(parent);

        this.prevForm = form;
        proc = new Proc();
        rh = new RRHandler();

        //Display.getInstance().callSerially(() -> {
        createUI();
        //});
    }

    private void createUI() {

        form = new Form("My Chat Room", new BorderLayout());
        form.getAllStyles().setBgColor(0xF0F3F4);
        form.getAllStyles().setBgTransparency(255);

        form.setBackCommand(new Command("btnBack") {
            @Override
            public void actionPerformed(ActionEvent ev) {
                prevForm.showBack();
            }
        });
        Command backCmd = Command.create("",
                proc.sideMenuIcon(FontImage.MATERIAL_ARROW_BACK),
                evt -> {
                    prevForm.showBack();
                });

        form.getToolbar().addCommandToLeftBar(backCmd);

        form.add(CENTER, FlowLayout.encloseCenterMiddle(getChatDetails()));
        setView(form);

        //showChatForm(prevForm, "0712345678", "Eric");
        //Create a view model as UI descriptor for the chat room - to allow
        //customizing & extending chat room
//        ViewNode viewNode = new ViewNode(
//                actions(ChatRoomView.SEND_ACTION, send),
//                actions(ProfileAvatarView.PROFILE_AVATAR_CLICKED_MENU,
//                        phone, video),
//                actions(ChatBubbleView.CHAT_BUBBLE_LONG_PRESS_MENU, likeAction),
//                actions(ChatBubbleView.CHAT_BUBBLE_BADGES, likedBadge),
//                //add capture action to TEXT_ACTIONS category to as it appear 
//                //as button beside textfield
//                actions(ChatRoomView.TEXT_ACTIONS, capturePhoto)
//        );
//        String senderPhone = "0712345678";
//        String senderName="Eric";
//        
//        ChatRoomView view = new ChatRoomView(createViewModel(senderPhone), 
//                viewNode, form);
//        form.add(CENTER, view);
//        setView(form);
        //form.revalidate();
//        addActionListener(send, evt -> {
//
//            //Consume the event indicating that it was handled thus preventing 
//            //other action listeners from handling/receiving the event
//            evt.consume();
//            ChatRoomView.ViewModel room = (ChatRoomView.ViewModel) evt.getEntity();
//            String text = room.getInputBuffer();
//            if (text != null && !text.isEmpty()) {
//                ChatBubbleView.ViewModel message = new ChatBubbleView.ViewModel();
//                message.messageText(text);
//                message.date(new Date());
//                message.isOwn(true); //indicates its sent by user - right aligned
//
//                //add the message
//                room.addMessages(message);
//                //clear text
//                room.inputBuffer("");
//
//                //sendMsg(text);
//                msgObj = new Message("sent", senderPhone, senderName, "chatAdmin",
//                        "imgUrl", text);
//
//                //send & receive msg here
//                sendMsg(senderPhone + "#" + senderName + "#" + "chatAdmin" + "#"
//                        + "imgUrl" + "#" + text);
//            }
//        });
//        addActionListener(phone, evt -> {
//            evt.consume();
//            if (!CN.canDial()) {
//                Dialog.show("Not supported", "Phone calls not supported on this "
//                        + "device", "OK", null);
//                return;
//            }
//
//            if (evt.getEntity().isEmpty(Person.telephone)) {
//                Dialog.show("No Phone", "This user has no phone number", "OK",
//                        null);
//                return;
//            }
//            String phoneNo = evt.getEntity().getText(Person.telephone);
//            CN.dial(phoneNo);
//        });
//
//        //handle like action to toggle property on and off
//        addActionListener(likeAction, evt -> {
//            evt.consume();
//            Entity chatMsg = evt.getEntity();
//            chatMsg.setBoolean(
//                    ChatMessage.isFavorite,
//                    chatMsg.isFalsey(ChatMessage.isFavorite));
//        });
//
//        addActionListener(capturePhoto, evt -> {
//            evt.consume();
//            String path = Capture.capturePhoto();
//            if (path == null) {
//                //user cancelled photo capture
//                return;
//            }
//
//            //create directory to store photos
//            File photos = new File("photos");
//            photos.mkdir();
//
//            Entity entity = evt.getEntity();
//            File photo = new File(photos, System.currentTimeMillis() + ".png");
//
//            try (InputStream input = FileSystemStorage.getInstance()
//                    .openInputStream(path);
//                    OutputStream output = FileSystemStorage.getInstance()
//                            .openOutputStream(photo.getAbsolutePath())) {
//
//                Util.copy(input, output);
//                ChatBubbleView.ViewModel message = new ChatBubbleView.ViewModel();
//                //set photo path - can be http,https&file urls & storage keys
//                message.attachmentImageUrl(photo.getAbsolutePath());
//                message.isOwn(true);
//                message.date(new Date());
//
//                //use ChatRoom.messages to access messages list 
//                EntityList list = entity.getEntityList(ChatRoom.messages);
//
//                if (list == null) {
//                    throw new IllegalStateException("This chat room has no "
//                            + "messages list set up");
//                }
//                //add messages to entity list - triggers change event & will be 
//                //rendered automatically in the chat room
//                list.add(message);
//
//            } catch (IOException e) {
//                ToastBar.showErrorMessage(e.getMessage());
//            }
//        });
        //Enable swipe back to previous form
        /*SwipeBackSupport.bindBack(form, (args) -> {
            return source.getComponentForm();
        });*/
        Image userImg = null;
        try {
            userImg = Image.createImage("/hse_sub_50.png");
        } catch (IOException err) {

        }

        //Place image to right side of toolbar
        Command cmdImg = new Command("", userImg);
        form.getToolbar().addCommandToRightBar(cmdImg);

    }

    private Container getChatDetails() {
        Container cntDetails = new Container(BoxLayout.y());

        Container cntPhone = new Container(BoxLayout.x(), "tfLay");
        TextField tfPhone = new TextField();
        tfPhone.setHint("Enter Phone");
        tfPhone.setText("0712345678");
        tfPhone.setUIID("tf");
        tfPhone.setConstraint(TextArea.PHONENUMBER);
        cntPhone.add(tfPhone);

        Container cntName = new Container(BoxLayout.x(), "tfLay");
        TextField tfName = new TextField();
        tfName.setHint("Enter Name");
        tfName.setText("Eric");
        tfName.setConstraint(TextArea.ANY);
        tfName.setUIID("tf");
        cntName.add(tfName);

        Button btn = new Button("Proceed", "btn");
        btn.addActionListener(e -> {
            senderPhone = tfPhone.getText();
            senderName = tfName.getText();

            if (senderPhone.length() == 0) {
                ToastBar.showErrorMessage("Enter Phone");
            } else if (senderName.length() == 0) {
                ToastBar.showErrorMessage("Enter Name");
            } else {
                proc.setSenderPhone(senderPhone);
                proc.setSenderName(senderName);
                showChatForm();
            }
        });

        cntDetails.add(cntPhone).add(cntName).add(btn);
        return cntDetails;

    }

    private void showChatForm() {

        //Create a view model as UI descriptor for the chat room - to allow
        //customizing & extending chat room
        ViewNode viewNode = new ViewNode(
                actions(ChatRoomView.SEND_ACTION, send),
                actions(ProfileAvatarView.PROFILE_AVATAR_CLICKED_MENU,
                        phone, video),
                actions(ChatBubbleView.CHAT_BUBBLE_LONG_PRESS_MENU, likeAction),
                actions(ChatBubbleView.CHAT_BUBBLE_BADGES, likedBadge),
                //add capture action to TEXT_ACTIONS category to as it appear 
                //as button beside textfield
                actions(ChatRoomView.TEXT_ACTIONS, capturePhoto)
        );

        //use ChatRoom.messages to access messages list 
        //Entity list = createViewModel().getEntityList(ChatRoom.messages);
        int msgSize = createViewModel().getEntityList(ChatRoom.messages).size();
        proc.printLine("Size " + msgSize);
        if (msgSize > 0) {

            ChatRoomView view = new ChatRoomView(createViewModel(), viewNode, form);
            form.add(CENTER, view);
            setView(form);

        } else {
            ChatRoomView view = new ChatRoomView(viewNode, form);
            form.add(CENTER, view);
            setView(form);
        }

        form.revalidateLater();

        addActionListener(send, evt -> {

            //Consume the event indicating that it was handled thus preventing 
            //other action listeners from handling/receiving the event
            evt.consume();
            ChatRoomView.ViewModel room = (ChatRoomView.ViewModel) evt.getEntity();
            String text = room.getInputBuffer();

            if (text != null && !text.isEmpty()) {
                ChatBubbleView.ViewModel message = new ChatBubbleView.ViewModel();
                message.messageText(text);
                message.date(new Date());
                message.isOwn(true); //indicates its sent by user - right aligned

                //add the message
                room.addMessages(message);
                //clear text
                room.inputBuffer("");

                //sendMsg(text);
                msgObj = new Message("sent", proc.getSenderPhone(),
                        proc.getSenderName(), "chatAdmin",
                        "imgUrl", text, proc.getCurrDate(), proc.getCurrTime());

                String data = "VAL1>" + proc.getSenderPhone()
                        + ">VAL2>" + proc.getSenderName()
                        + ">VAL3>" + "chatAdmin"
                        + ">VAL4>" + "imgUrl"
                        + ">VAL5>" + text
                        + ">VAL6>" + proc.getCurrDate()
                        + ">VAL7>" + proc.getCurrTime();

                sendMsg(proc.getSenderPhone(), data);

            }
        });

        addActionListener(phone, evt -> {
            evt.consume();
            if (!CN.canDial()) {
                Dialog.show("Not supported", "Phone calls not supported on this "
                        + "device", "OK", null);
                return;
            }

            if (evt.getEntity().isEmpty(Person.telephone)) {
                Dialog.show("No Phone", "This user has no phone number", "OK",
                        null);
                return;
            }
            String phoneNo = evt.getEntity().getText(Person.telephone);
            CN.dial(phoneNo);
        });

        //handle like action to toggle property on and off
        addActionListener(likeAction, evt -> {
            evt.consume();
            Entity chatMsg = evt.getEntity();
            chatMsg.setBoolean(
                    ChatMessage.isFavorite,
                    chatMsg.isFalsey(ChatMessage.isFavorite));
        });

        addActionListener(capturePhoto, evt -> {
            evt.consume();
            String path = Capture.capturePhoto();
            if (path == null) {
                //user cancelled photo capture
                return;
            }

            //create directory to store photos
            File photos = new File("photos");
            photos.mkdir();

            Entity entity = evt.getEntity();
            File photo = new File(photos, System.currentTimeMillis() + ".png");

            try (InputStream input = FileSystemStorage.getInstance()
                    .openInputStream(path);
                    OutputStream output = FileSystemStorage.getInstance()
                            .openOutputStream(photo.getAbsolutePath())) {

                Util.copy(input, output);
                ChatBubbleView.ViewModel message = new ChatBubbleView.ViewModel();
                //set photo path - can be http,https&file urls & storage keys
                message.attachmentImageUrl(photo.getAbsolutePath());
                message.isOwn(true);
                message.date(new Date());

                //use ChatRoom.messages to access messages list 
                EntityList list = entity.getEntityList(ChatRoom.messages);

                if (list == null) {
                    throw new IllegalStateException("This chat room has no "
                            + "messages list set up");
                }
                //add messages to entity list - triggers change event & will be 
                //rendered automatically in the chat room
                list.add(message);

            } catch (IOException e) {
                ToastBar.showErrorMessage(e.getMessage());
            }
        });

    }

    private Entity createViewModel(String msg, boolean own) {
        ChatRoomView.ViewModel room = new ChatRoomView.ViewModel();
        ChatBubbleView.ViewModel message = new ChatBubbleView.ViewModel();
        message.messageText(msg);
        message.date(new Date());
        message.isOwn(own);
        room.addMessages(message);

        return room;

    }

    private void showRecv(String msg) {

        /* ChatRoomView.ViewModel room = (ChatRoomView.ViewModel) evt.getEntity();
            String text = room.getInputBuffer();
            if (text != null && !text.isEmpty()) {
                ChatBubbleView.ViewModel message = new ChatBubbleView.ViewModel();
                message.messageText(text);
                message.date(new Date());
                message.isOwn(true); //indicates its sent by user - right aligned

                //add the message
                room.addMessages(message);*/
 /*ChatRoomView.ViewModel room = new ChatRoomView.ViewModel();
        ChatBubbleView.ViewModel message = new ChatBubbleView.ViewModel();
        message.messageText(msg);
        message.date(new Date());
        message.isOwn(false);
        room.addMessages(message);*/
        //Entity entity = new Entity();
        //use ChatRoom.messages to access messages list 
        //EntityList list = entity.getEntityList(ChatRoom.messages);
//        if (list == null) {
//            throw new IllegalStateException("This chat room has no "
//                    + "messages list set up");
//        }
        //add messages to entity list - triggers change event & will be 
        //rendered automatically in the chat room
        //list.add(message);
        //form.add(CENTER,room);
        /*ChatRoomView view = new ChatRoomView(createViewModel(msg, false), form);
        form.add(CENTER, view);*/
        //setView(form);
        //form.revalidate();
    }

    private void showSent(String msg) {
        /*ChatRoomView.ViewModel room = new ChatRoomView.ViewModel();
        ChatBubbleView.ViewModel message = new ChatBubbleView.ViewModel();
        message.messageText(msg);
        message.date(new Date());
        message.isOwn(true);
        room.addMessages(message);*/

 /*Entity entity = new Entity();
        //use ChatRoom.messages to access messages list 
        EntityList list = entity.getEntityList(ChatRoom.messages);

//        if (list == null) {
//            throw new IllegalStateException("This chat room has no "
//                    + "messages list set up");
//        }
        //add messages to entity list - triggers change event & will be 
        //rendered automatically in the chat room
        list.add(message);*/
        //ChatRoomView view = new ChatRoomView(createViewModel(msg, true), form);
        //form.add(CENTER, view);
        //setView(form);
        //form.revalidate();
    }

    private void sendMsg(String userId, String msg) {

        String data = "Chat>" + msg.trim();
        //String data = "Chat>" + msg.trim() + emoji;
        //data = "'" + data + "'";
        rh.processNetTkn(this, userId, data, "chat");
    }

    @Override
    public void getResp(String resp, String taskTag) {

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
                //recvLayout(chatArea, recvMsgObj.getMessage(), null);
                //showRecv(recvMsgObj.getMessage());
                //showMsg(recvMsgObj.getMessage(), false);

                /*lblProgress.setText("");
                lblSent.setIcon(imgRecv);
                lblSent.setVisible(true);
                lblTime.setVisible(true);

                form.revalidate();*/
                showChatForm();
                break;
            case "FAIL":
                proc.showToast(resArr[1], FontImage.MATERIAL_ERROR);
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

    /**
     * Create a view model for the chat room
     *
     * @return
     */
    private Entity createViewModel() {
        ChatRoomView.ViewModel room = new ChatRoomView.ViewModel();

        //dummy times for chat messages
        long SECOND = 1000l;
        long MINUTE = SECOND * 60;
        long HOUR = MINUTE * 60;
        long DAY = HOUR * 24;

        //make 1st msg 2days ago
        long t = System.currentTimeMillis() - 2 * DAY;
        //participants avatars
        /*String gThumb = "https://weblite.ca/cn1test/radchat/george.jpg";
        String kThumb = "https://weblite.ca/cn1test/radchat/kramer.jpg";*/
        String gThumb = null, kThumb = null;

//        room.addMessages(createDemoMessage("Why couldn't you have made me an "
//                + "architect? You know I always wanted to pretend that I was "
//                + "an architect. Well I'm supposed to see her tommorrow, I'm "
//                + "gonna tell her what's going on. Maybe she likes me for me.",
//                new Date(t), "George", gThumb));
//        t += HOUR;
//        room.addMessages(createDemoMessage("Hey", new Date(t), "Kramer", kThumb));
//        t += MINUTE;
//        room.addMessages(createDemoMessage("Hey", new Date(t), null, null));

        /*ChatBubbleView.ViewModel message = new ChatBubbleView.ViewModel();
        message.messageText("Hello world");
        room.addMessages(message);*/
        //Load stored msgs & add them to the form
        java.util.List<Message> messages = (java.util.List<Message>) Storage
                .getInstance().readObject("ChatStore");

        //proc.printLine("Msgs = " + messages);
        if (messages != null) {
            for (Message m : messages) {
                /*proc.printLine("Msg = " + m.getMessage().trim());
                 */

                //query sent msgs
                if ((m.getSenderPhone().trim().equals(proc.getSenderPhone())
                        && m.getRecepientId().trim().equals("chatAdmin"))
                        //query received msgs
                        || (m.getSenderPhone().trim().equals("chatAdmin")
                        && m.getRecepientId().trim().equals(proc.getSenderPhone()))) {

                    if (m.getStatus().equals("recv")) {
                        //recvLayout(chatArea, m.getMessage().trim(), userImg);
                        //showRecv(m.getMessage().trim());
                        room.addMessages(createDemoMessage(m.getMessage().trim(),
                                new Date(t), "Kramer", kThumb));

                        //room = showMsg(m.getMessage().trim(),false);
                    } else if (m.getStatus().equals("sent")) {
                        //sentLayout(chatArea, m.getMessage().trim(), "sent");
                        //showSent(m.getMessage().trim());
                        room.addMessages(createDemoMessage(m.getMessage().trim(),
                                new Date(t), null, gThumb));
                    }

                }
            }

        }

        room.addParticipants(new ChatAccount("George", gThumb, "712-555-1234"),
                new ChatAccount("Kramer", kThumb, null));

        return room;
    }

    private Entity createDemoMessage(String text, Date datePosted,
            String participant, String iconUrl) {

        ChatBubbleView.ViewModel msg = new ChatBubbleView.ViewModel();
        msg.messageText(text)
                .date(datePosted)
                .iconUrl(iconUrl)
                .isOwn(participant == null);
        if (participant != null) {
            msg.postedBy(participant);
        }
        return msg;
    }

    private Entity showMsg(String msg, boolean own) {
        ChatRoomView.ViewModel room = new ChatRoomView.ViewModel();

        //dummy times for chat messages
        long SECOND = 1000l;
        long MINUTE = SECOND * 60;
        long HOUR = MINUTE * 60;
        long DAY = HOUR * 24;

        //make 1st msg 2days ago
        long t = System.currentTimeMillis() - 2 * DAY;
        //participants avatars
        /*String gThumb = "https://weblite.ca/cn1test/radchat/george.jpg";
        String kThumb = "https://weblite.ca/cn1test/radchat/kramer.jpg";*/
        String gThumb = null, kThumb = null;

        t += HOUR;
        room.addMessages(createMsg(msg, new Date(t), own, null));

        room.addParticipants(new ChatAccount("George", gThumb, "712-555-1234"),
                new ChatAccount("Kramer", kThumb, null));
        return room;
    }

    private Entity createMsg(String text, Date datePosted,
            boolean own, String iconUrl) {

        ChatBubbleView.ViewModel msg = new ChatBubbleView.ViewModel();
        msg.messageText(text)
                .date(datePosted)
                .iconUrl(iconUrl)
                .isOwn(own);
        if (own) {
            msg.postedBy(iconUrl);
        }
        return msg;
    }

}
