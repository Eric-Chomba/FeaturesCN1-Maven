/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zomuhtech.cn.features.procs;

/**
 *
 * @author Zomuh Tech
 */
import com.codename1.location.Location;
import com.codename1.location.LocationListener;
import com.codename1.location.LocationManager;
import com.codename1.ui.Display;

public class LocChangeListener implements LocationListener {

    NewLocResp newL;
    Proc proc = new Proc();

    public LocChangeListener(NewLocResp newLoc) {
        this.newL = newLoc;
    }

    @Override
    public void locationUpdated(Location location) {

        //Update only when app in foreground (not in background)
        if (!Display.getInstance().isMinimized()) {
            //proc.printLine("New Location " + location.getLatitude()
            //+ " " + location.getLongitude());

            newL.getNewLoc(location);
        } else {
            
            //Stop location update when app in background
            LocationManager.getLocationManager().setLocationListener(null);
        }
    }

    @Override
    public void providerStateChanged(int newState) {
        proc.printLine("Prov state " + newState);
    }

    public interface NewLocResp {

        void getNewLoc(Location location);
    }
}
