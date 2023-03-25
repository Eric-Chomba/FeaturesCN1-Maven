/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zomuhtech.cn.features.advft;

/**
 *
 * @author Eric
 */
import com.codename1.system.NativeInterface;

public interface AndSmsInterceptor extends NativeInterface {
    public void bindSmsListener();
    public void unbindSmsListener();
    public void showRouteActivity();
}
