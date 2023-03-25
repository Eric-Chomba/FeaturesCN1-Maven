/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zomuhtech.cn.features.advft.chat;

import com.codename1.rad.models.Entity;
import com.codename1.rad.models.EntityType;
import static com.codename1.rad.models.EntityType.tags;
import com.codename1.rad.models.StringProperty;
import com.codename1.rad.schemas.Person;
import com.codename1.rad.schemas.Thing;

/**
 *
 * @author Eric
 */
//encapsulate profiles for participants in the chat room
public class ChatAccount extends Entity {

    //name & thumbnailurl properties
    public static StringProperty name, thumbnailUrl, phone;

    //define entity type for ChatAccount entity
    public static final EntityType TYPE = new EntityType() {
        {
            //generate name & thumbnailurl property as a string
            name = string(tags(Thing.name));
            thumbnailUrl = string(tags(Thing.thumbnailUrl));
            phone = string(tags(Person.telephone));
        }
    };

    {
        setEntityType(TYPE);
    }

    public ChatAccount(String nm, String thumb, String phoneNo) {
        set(name, nm);
        set(thumbnailUrl, thumb);
        set(phone, phoneNo);
    }
}
