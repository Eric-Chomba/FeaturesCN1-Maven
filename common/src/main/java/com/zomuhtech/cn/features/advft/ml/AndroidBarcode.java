/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zomuhtech.cn.features.advft.ml;

import com.codename1.system.NativeInterface;

/**
 *
 * @author Eric
 */
public interface AndroidBarcode extends NativeInterface {

    String getBarcodeData(String data);
}
