/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zomuhtech.cn.features.advft;

import com.codename1.io.Externalizable;
import com.codename1.io.Util;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Date;

/**
 *
 * @author Eric
 */
public class ExternalizeClass implements Externalizable {

    private static final int VERSION = 2;
    private String name, age, hobby;
    private Date date;
    private long expires = 1;

    //empty constructor required for initialization & read already stored values
    public ExternalizeClass(){
        
    }
    public ExternalizeClass(String name, String age, String hobby) {

        this.name = name;
        this.age = age;
        this.hobby = hobby;
    }

    @Override
    public int getVersion() {
        return VERSION;
    }

    @Override
    public String getObjectId() {
        return "ExternalizeClass";
    }

    @Override
    public void externalize(DataOutputStream out) throws IOException {

        Util.writeUTF(name, out);
        Util.writeUTF(age, out);
        Util.writeUTF(hobby, out);
        if (date != null) {
            out.writeBoolean(true);
            out.writeLong(date.getTime());
        } else {
            out.writeBoolean(false);
        }
        out.writeLong(expires);
    }

    @Override
    public void internalize(int version, DataInputStream in) throws IOException {

        name = Util.readUTF(in);
        age = Util.readUTF(in);
        hobby = Util.readUTF(in);

        if (version > 1) {
            boolean hasDate = in.readBoolean();
            if (hasDate) {
                date = new Date(in.readLong());
            }
        }
        expires = in.readLong();
    }

    public String getName() {
        return name;
    }

    public String getAge() {
        return age;
    }

    public String getHobby() {
        return hobby;
    }
}
