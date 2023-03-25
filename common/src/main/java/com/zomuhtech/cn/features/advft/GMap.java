/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zomuhtech.cn.features.advft;

/**
 * GUI builder created Form
 *
 * @author Eric
 */
import com.zomuhtech.cn.features.procs.LocChangeListener;
import com.codename1.components.InteractionDialog;
import com.codename1.components.ToastBar;
import com.codename1.googlemaps.MapContainer;
import com.codename1.io.ConnectionRequest;
import com.codename1.io.JSONParser;
import com.codename1.io.Log;
import com.codename1.io.NetworkManager;
import com.codename1.location.Location;
import com.codename1.location.LocationManager;
import com.codename1.ui.Button;
import com.codename1.ui.Display;
import com.codename1.ui.Form;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.maps.Coord;
import com.codename1.processing.Result;
import com.codename1.ui.AutoCompleteTextField;
import com.codename1.ui.Container;
import com.codename1.ui.EncodedImage;
import com.codename1.ui.FontImage;
import com.codename1.ui.Label;
import com.codename1.ui.TextField;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.FlowLayout;
import com.codename1.ui.layouts.LayeredLayout;
import com.codename1.ui.list.DefaultListModel;
import com.codename1.ui.plaf.Style;
import com.codename1.util.MathUtil;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class GMap extends com.codename1.ui.Form 
        implements LocChangeListener.NewLocResp {

    private static final String API_KEY = "AIzaSyDNljQCtWhdtsNYMmU7de3o7-XAZoQ1jlk";
    MapContainer cntMap;
    FontImage imgMarker;
    Coord searchCoord;
    String address;

    public GMap() {
       // this(com.codename1.ui.util.Resources.getGlobalResources());
        Display.getInstance().callSerially(() -> {
            createUI();
        });
    }
    
     private void createUI() {
        Form form = new Form("Native Maps", new BorderLayout());
        //final MapContainer cntMap = new MapContainer(HTML_API_KEY);

        Style s = new Style();
        s.setFgColor(0x3399ff);
        s.setBgTransparency(0);
        imgMarker = FontImage.createMaterial(
                FontImage.MATERIAL_PLACE, s,
                Display.getInstance().convertToPixels(1));

        // cntMap = new MapContainer();
        cntMap = new MapContainer(API_KEY);
        cntMap.setPathStrokeColor(0xff0000);
        cntMap.setShowMyLocation(true);
        cntMap.setPathStrokeWidth(2);
        cntMap.setRotateGestureEnabled(true);

        //Get current location
        Location location = LocationManager.getLocationManager()
                .getCurrentLocationSync();

        Coord myLoc = new Coord(location.getLatitude(),
                location.getLongitude());

        /*System.out.println("My Location Lat: " + location.getLatitude()
                + " Lng: " + location.getLongitude());*/

        /* cntMap.setCameraPosition(myLoc);
        cntMap.zoom(myLoc, 10);

        cntMap.addMarker(EncodedImage.createFromImage(imgMarker, false),
                cntMap.getCameraPosition(), "Hi marker", "Long description",
                evt -> {
                    ToastBar.showMessage("your location",
                            FontImage.MATERIAL_PLACE);
                });*/
        //To stop update
        //LocationManager.getLocationManager().setLocationListener(null);
        //To get location updates
        /*LocationManager.getLocationManager()
                .setLocationListener(new LocChangeListener(this));*/
        Button btnMoveCam = new Button("Move Camera");
        btnMoveCam.addActionListener(e -> {
            cntMap.setCameraPosition(new Coord(-1.2674735, 36.7476624));

        });

        Button btnAddMarker = new Button("Add Marker");
        btnAddMarker.addActionListener(e -> {

            cntMap.setCameraPosition(new Coord(-2.2674735, 36.7476624));

            cntMap.addMarker(EncodedImage.createFromImage(imgMarker, false),
                    cntMap.getCameraPosition(), "Hi marker", "Long description",
                    evt -> {
                        ToastBar.showMessage("You",
                                FontImage.MATERIAL_PLACE);
                    });
        });

        String[] coordArr = {"-1.2574735:40.7476624:Town A", "-2.2574735:30.7476624:Town B"};

        Button btnAddPath = new Button("Add Path");
        btnAddPath.addActionListener(e -> {

            for (String coord : coordArr) {

                String[] latLng = splitCoord(coord, ":");

                ///cntMap.setCameraPosition(new Coord(-2.2674735, 36.7476624));
                cntMap.setCameraPosition(myLoc);
                cntMap.addMarker(EncodedImage.createFromImage(imgMarker, false),
                        cntMap.getCameraPosition(), "Hi marker", "Long description",
                        evt -> {
                            ToastBar.showMessage("Your location",
                                    FontImage.MATERIAL_PLACE);
                        });

                cntMap.setCameraPosition(new Coord(Double.valueOf(latLng[0]),
                        Double.valueOf(latLng[1])));
                cntMap.zoom(new Coord(Double.valueOf(latLng[0]),
                        Double.valueOf(latLng[1])), 10);

                cntMap.addMarker(EncodedImage.createFromImage(imgMarker, false),
                        cntMap.getCameraPosition(), "Hi marker", "Long description",
                        evt -> {
                            ToastBar.showMessage(latLng[2] + " Loc " + latLng[0] + " " + latLng[1],
                                    FontImage.MATERIAL_PLACE);
                        });


                /* cntMap.setCameraPosition(new Coord(-2.2574735, 30.7476624));
                cntMap.addMarker(EncodedImage.createFromImage(imgMarker, false),
                        cntMap.getCameraPosition(), "Hi marker", "Long description",
                        evt -> {
                            ToastBar.showMessage("Center 1",
                                    FontImage.MATERIAL_PLACE);
                        });*/

 /*cntMap.addPath(cntMap.getCameraPosition(),
                    new Coord(-1.2674735,35.7476624),
                    new Coord(-1.2574735, 34.7476624),
                    new Coord(-1.2474735, 33.7476624),
                    new Coord(-1.2374735, 33.7476624));*/
 /* cntMap.addPath(new Coord(-1.2674735, 35.7476624),
                    new Coord(-1.2574735, 34.7476624));*/
 /*cntMap.addPath(myLoc, new Coord(-1.2574735, 34.7476624),
                        myLoc, new Coord(-2.2574735, 30.7476624));*/
                cntMap.addPath(myLoc, new Coord(Double.valueOf(latLng[0]),
                        Double.valueOf(latLng[1])));
            }
        });

        Button btnClearAll = new Button("Clear All");
        btnClearAll.addActionListener(e -> {
            cntMap.clearMapLayers();
        });

        cntMap.addTapListener(e -> {
            TextField tfName = new TextField();
            Container cnt = BoxLayout.encloseY(new Label("Name"), tfName);
            InteractionDialog dlg = new InteractionDialog("Add Marker");
            dlg.getContentPane().add(cnt);
            tfName.setDoneListener(e2 -> {

                String txt = tfName.getText();
                cntMap.addMarker(EncodedImage.createFromImage(imgMarker, false),
                        cntMap.getCoordAtPosition(e.getX(), e.getY()), txt, "",
                        e3 -> {

                            ToastBar.showMessage("You clicked",
                                    FontImage.MATERIAL_PLACE);
                        });
                dlg.dispose();
            });
            //dlg.showPopupDialog(new Rectangle(e.getX(), e.getY(), 10, 10));
            //tfName.startEditingAsync();
        });

        final DefaultListModel<String> options = new DefaultListModel<>();
        AutoCompleteTextField acTf = new AutoCompleteTextField(options) {

            @Override
            protected boolean filter(String text) {
                if (text.length() == 0) {
                    return false;
                }

                String[] locArr = searchLocations(text);
                if (locArr == null || locArr.length == 0) {
                    return false;
                }

                options.removeAll();
                for (String s : locArr) {
                    options.addItem(s);
                }

                return true;
            }
        };

        acTf.setMinimumElementsShownInPopup(5);
        Container cntSearch = new Container(BoxLayout.x());
        Button btnSearch = new Button("Search");
        btnSearch.addActionListener(e -> {

            String text = acTf.getText();
            
            getCoord(text);

            /*String [] textArr = splitCoord(text,",");
            
            System.out.println(textArr[0]+" "+textArr[1]);
           
            getAddress(new Coord(Double.parseDouble(textArr[0]),
                    Double.parseDouble(textArr[1])));*/
        });
        cntSearch.add(acTf).add(btnSearch);

        Container cntAll = new Container(BoxLayout.y());
        cntAll.add(cntSearch);
        Container root = LayeredLayout.encloseIn(BorderLayout.center(cntMap),
                BorderLayout.south(FlowLayout.encloseBottom(btnMoveCam,
                        btnAddMarker, btnAddPath, btnClearAll)));

        cntAll.add(root);
        form.add(BorderLayout.CENTER, cntAll);
        //form.add(BorderLayout.NORTH, acTf);

        form.show();

    }

    public String[] splitCoord(String coordStr, String dmt) {
        ArrayList<String> splitArr = new ArrayList<>();
        StringTokenizer arr = new StringTokenizer(coordStr, dmt);
        while (arr.hasMoreElements()) {
            splitArr.add(arr.nextToken());
        }
        String[] coordArr = splitArr.toArray(new String[splitArr.size()]);
        return coordArr;
    }

    @Override
    public void getNewLoc(Location location) {

        /*System.out.println("New Location2 " + location.getLatitude()
                + " " + location.getLongitude());*/

        Coord myNewLoc = new Coord(location.getLatitude(),
                location.getLongitude());

        /*cntMap.setCameraPosition(myNewLoc);
        cntMap.zoom(myNewLoc, 10);

        cntMap.addMarker(EncodedImage.createFromImage(imgMarker, false),
                cntMap.getCameraPosition(), "Hi marker", "Long description",
                evt -> {
                    ToastBar.showMessage("your new location",
                            FontImage.MATERIAL_PLACE);
                });*/
        //updateRoute(myNewLoc);
    }

    private void updateRoute(Coord myNewLoc) {
        String[] coordArr = {"-1.2574735:40.7476624:Town A",
            "-2.2574735:30.7476624:Town B"};

        for (String coord : coordArr) {

            String[] latLng = splitCoord(coord, ":");

            cntMap.setCameraPosition(myNewLoc);
            cntMap.addMarker(EncodedImage.createFromImage(imgMarker, false),
                    cntMap.getCameraPosition(), "Hi marker", "Long description",
                    evt -> {
                        ToastBar.showMessage("Your new location",
                                FontImage.MATERIAL_PLACE);
                    });

            cntMap.setCameraPosition(new Coord(Double.valueOf(latLng[0]),
                    Double.valueOf(latLng[1])));
            cntMap.zoom(new Coord(
                    Double.valueOf(latLng[0]), Double.valueOf(latLng[1])), 10);

            cntMap.addMarker(EncodedImage.createFromImage(imgMarker, false),
                    cntMap.getCameraPosition(), "Hi marker", "Long description",
                    evt -> {
                        ToastBar.showMessage(latLng[2] + " Loc " + latLng[0] + " " + latLng[1],
                                FontImage.MATERIAL_PLACE);
                    });

            cntMap.addPath(myNewLoc, new Coord(Double.valueOf(latLng[0]),
                    Double.valueOf(latLng[1])));

            long dist = calDistance(myNewLoc.getLatitude(), myNewLoc.getLongitude(),
                    Double.valueOf(latLng[0]), Double.valueOf(latLng[1]));
            //System.out.println("New Distance " + dist + " M " + dist / 1000 + " KM");

        }

    }

    public static long calDistance(double lat1, double lng1,
            double lat2, double lng2) {

        double latSin = Math.sin(Math.toRadians(lat2 - lat1) / 2);
        double lngSin = Math.sin(Math.toRadians(lng2 - lng2) / 2);
        double a = latSin * latSin
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * lngSin * lngSin;

        double c = 2 * MathUtil.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return (long) (6378137 * c);
    }

    String[] searchLocations(String text) {
        String url = "https://maps.googleapis.com/maps/api/place/autocomplete/json";
        try {

            if (text.length() > 0) {

                ConnectionRequest r = new ConnectionRequest();
                r.setPost(false);
                r.setUrl(url);
                r.addArgument("key", API_KEY);
                r.addArgument("input", text);
                NetworkManager.getInstance().addToQueueAndWait(r);
                Map<String, Object> result = new JSONParser()
                        .parseJSON(new InputStreamReader(
                                new ByteArrayInputStream(r.getResponseData()),
                                "UTF-8"));
                String[] res = Result.fromContent(result).getAsStringArray("//description");
                return res;
            }

        } catch (Exception e) {
            Log.e(e);
        }

        return null;
    }

    private void getCoord(String address) {
        
        String url = "https://maps.googleapis.com/maps/api/geocode/json";

        try {

            ConnectionRequest request = new ConnectionRequest(url, false);
            request.addArgument("key", API_KEY);
            request.addArgument("address", address);

            NetworkManager.getInstance().addToQueueAndWait(request);
            
            Map<String, Object> resp = new JSONParser()
                    .parseJSON(new InputStreamReader(
                            new ByteArrayInputStream(
                                    request.getResponseData()), "UTF-8"));

            if (resp.get("results") != null) {

                ArrayList resArr = (ArrayList) resp.get("results");
                if (resArr.size() > 0) {

                    LinkedHashMap location = (LinkedHashMap) ((LinkedHashMap) ((LinkedHashMap) resArr.get(0)).get("geometry"))
                            .get("location");

                    searchCoord = new Coord((double) location.get("lat"),
                            (double) location.get("lng"));

                    cntMap.setCameraPosition(searchCoord);
                    cntMap.zoom(searchCoord, 10);

                    cntMap.addMarker(EncodedImage.createFromImage(imgMarker, false),
                            cntMap.getCameraPosition(), "Hi marker", "Long description",
                            evt -> {
                                
                                String res = address
                                + "\nLoc " + searchCoord.getLatitude() + " "
                                + searchCoord.getLongitude();
                                
                                //System.out.println(res);
                                
                                ToastBar.showMessage(res, FontImage.MATERIAL_PLACE);
                            });
                }
            }

        } catch (IOException e) {
        }

    }
    
    private void getAddress(Coord coord) {

        String url = "https://maps.googleapis.com/maps/api/geocode/json";

        try {

            ConnectionRequest req = new ConnectionRequest(url, false);
            req.addArgument("key", API_KEY);
            req.addArgument("latlng", coord.getLatitude() + "," + coord.getLongitude());

            NetworkManager.getInstance().addToQueueAndWait(req);
            Map<String, Object> response = new JSONParser()
                    .parseJSON(new InputStreamReader(
                            new ByteArrayInputStream(
                                    req.getResponseData()), "UTF-8"));

            if (response.get("results") != null) {

                ArrayList results = (ArrayList) response.get("results");
                if (results.size() > 0) {

                    address = (String) ((LinkedHashMap) results.get(0))
                            .get("formatted_address");

                    //System.out.println("Address: " + address);

                }
            }

            ConnectionRequest request = new ConnectionRequest(url, false);
            request.addArgument("key", API_KEY);
            request.addArgument("address", address);

            NetworkManager.getInstance().addToQueueAndWait(request);
            Map<String, Object> resp = new JSONParser()
                    .parseJSON(new InputStreamReader(
                            new ByteArrayInputStream(
                                    request.getResponseData()), "UTF-8"));

            if (resp.get("results") != null) {

                ArrayList resArr = (ArrayList) resp.get("results");
                if (resArr.size() > 0) {

                    LinkedHashMap location = (LinkedHashMap) ((LinkedHashMap) ((LinkedHashMap) resArr.get(0)).get("geometry"))
                            .get("location");

                    coord = new Coord((double) location.get("lat"),
                            (double) location.get("lng"));

                    cntMap.setCameraPosition(coord);
                    cntMap.zoom(coord, 10);

                    cntMap.addMarker(EncodedImage.createFromImage(imgMarker, false),
                            cntMap.getCameraPosition(), "Hi marker", "Long description",
                            evt -> {
                                String res = address
                                + "\nLoc " + searchCoord.getLatitude() + " "
                                + searchCoord.getLongitude();
                                
                                /*System.out.println(res);*/
                                
                                ToastBar.showMessage(res,
                                        FontImage.MATERIAL_PLACE);
                            });
                }
            }

        } catch (IOException e) {
        }

    }
    
    public GMap(com.codename1.ui.util.Resources resourceObjectInstance) {
        //initGuiBuilderComponents(resourceObjectInstance);
    }

//////-- DON'T EDIT BELOW THIS LINE!!!


// <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initGuiBuilderComponents(com.codename1.ui.util.Resources resourceObjectInstance) {
        setLayout(new com.codename1.ui.layouts.FlowLayout());
        setInlineStylesTheme(resourceObjectInstance);
                setInlineStylesTheme(resourceObjectInstance);
        setTitle("GMap");
        setName("GMap");
    }// </editor-fold>

//-- DON'T EDIT ABOVE THIS LINE!!!
}
