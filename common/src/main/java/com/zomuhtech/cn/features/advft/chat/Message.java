/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zomuhtech.cn.features.advft.chat;

import ca.weblite.codename1.json.JSONException;
import ca.weblite.codename1.json.JSONObject;
import com.codename1.io.Externalizable;
import com.codename1.io.Util;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 *
 * @author Eric
 */
public class Message implements Externalizable {

    private long time;
    private String status, senderPhone, senderName, picture, recepientId, msg,
            msgDate, msgTime;

    //empty constructor required for initialization & read already stored values
    public Message() {

    }

    public Message(String status, String senderPhone, String senderName,
            String recepientId, String picture, String msg, String msgDate,
            String msgTime) {
        this.status = status;
        this.senderPhone = senderPhone;
        this.senderName = senderName;
        this.recepientId = recepientId;
        this.picture = picture;
        this.msg = msg;
        this.msgDate = msgDate;
        this.msgTime = msgTime;
    }

    public Message(JSONObject obj) {
        try {
            time = Long.parseLong(obj.getString("time"));
            senderPhone = obj.getString("senderPhone");
            senderName = obj.getString("senderName");
            recepientId = obj.getString("recepientId");
            msg = obj.getString("message");
            msgDate = obj.getString("msgdate");
            msgTime = obj.getString("msgtime");
            picture = obj.getString("pic");

        } catch (JSONException ex) {

        }
    }

    public JSONObject toJSON() {
        JSONObject obj = createJSONObject("senderPhone", senderPhone,
                "senderName", senderName,
                "recepientId", recepientId,
                "pic", picture,
                "time", Long.toString(System.currentTimeMillis()),
                "message", msg,
                "msgdate", msgDate,
                "msgtime", msgTime
        );

        return obj;
    }

    JSONObject createJSONObject(String... keyValues) {
        try {
            JSONObject object = new JSONObject();
            for (int j = 0; j < keyValues.length; j++) {
                object.put(keyValues[j], keyValues[j + 1]);
            }
            return object;
        } catch (JSONException err) {

        }
        return null;
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public void externalize(DataOutputStream out) throws IOException {
        out.writeLong(time);
        Util.writeUTF(status, out);
        Util.writeUTF(senderPhone, out);
        Util.writeUTF(senderName, out);
        Util.writeUTF(recepientId, out);
        Util.writeUTF(picture, out);
        Util.writeUTF(msg, out);
        Util.writeUTF(msgDate, out);
        Util.writeUTF(msgTime, out);
    }

    @Override
    public void internalize(int version, DataInputStream in) throws IOException {
        time = in.readLong();
        status = Util.readUTF(in);
        senderPhone = Util.readUTF(in);
        senderName = Util.readUTF(in);
        recepientId = Util.readUTF(in);
        picture = Util.readUTF(in);
        msg = Util.readUTF(in);
        msgDate = Util.readUTF(in);
        msgTime = Util.readUTF(in);
    }

    @Override
    public String getObjectId() {
        return "Message";
    }

    public long getTime() {
        return time;
    }

    public String getStatus() {
        return status;
    }

    public String getSenderPhone() {
        return senderPhone;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getRecepientId() {
        return recepientId;
    }

    public String getPicture() {
        return picture;
    }

    public String getMessage() {
        return msg;
    }

    public String getMsgDate() {
        return msgDate;
    }

    public String getMsgTime() {
        return msgTime;
    }

}
