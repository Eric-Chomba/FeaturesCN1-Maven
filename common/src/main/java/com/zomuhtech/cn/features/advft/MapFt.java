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
import com.codename1.components.InfiniteProgress;
import com.codename1.components.MultiButton;
import com.zomuhtech.cn.features.procs.LocChangeListener;
import com.zomuhtech.cn.features.procs.Proc;
import com.codename1.components.ToastBar;
import com.codename1.googlemaps.MapContainer;
import com.codename1.io.ConnectionRequest;
import com.codename1.io.JSONParser;
import com.codename1.io.NetworkManager;
import com.codename1.location.Geofence;
import com.codename1.location.GeofenceListener;
import com.codename1.location.Location;
import com.codename1.location.LocationManager;
import com.codename1.ui.Button;
import com.codename1.ui.Display;
import com.codename1.ui.Form;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.maps.Coord;
import com.codename1.notifications.LocalNotification;
import com.codename1.processing.Result;
import com.codename1.ui.AutoCompleteTextField;
import com.codename1.ui.CheckBox;
import com.codename1.ui.Container;
import com.codename1.ui.Dialog;
import com.codename1.ui.EncodedImage;
import com.codename1.ui.FontImage;
import com.codename1.ui.Graphics;
import com.codename1.ui.Image;
import com.codename1.ui.Label;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.FlowLayout;
import com.codename1.ui.layouts.GridLayout;
import com.codename1.ui.layouts.LayeredLayout;
import com.codename1.ui.list.DefaultListModel;
import com.codename1.ui.plaf.Style;
import com.codename1.ui.util.UITimer;
import com.codename1.util.MathUtil;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class MapFt extends Form implements LocChangeListener.NewLocResp {

    private static String API_KEY = "";
    MapContainer cntMap;
    FontImage imgMarker;
    Coord searchCoord;
    String address, selVal;

    Label lblDist;
    DefaultListModel<String> fromDefList = new DefaultListModel<>();
    AutoCompleteTextField tfFrom;
    Form form, prevForm;
    Proc proc;
    ArrayList<Button> btnArr;
    Dialog infiniteDialog;
    int waveCount, dimension;
    int zoomLevel = 18;

    public MapFt(Form form) {
        //this(com.codename1.ui.util.Resources.getGlobalResources());
        this.prevForm = form;
        this.btnArr = new ArrayList<>();
        proc = new Proc();
        API_KEY = proc.getMapKey();
        Display.getInstance().callSerially(() -> {
            createUI();
        });
    }

    private void createUI() {
        form = proc.getForm("Map", prevForm);
        form.setLayout(new BorderLayout());

        //cntMap = new MapContainer();
        Container cntMapPar = new Container(new LayeredLayout());
        cntMap = new MapContainer(API_KEY);

        Container cntZoom = new Container(BoxLayout.y());
        Button btnZoomIn = new Button(proc.customIcon(FontImage.MATERIAL_ZOOM_IN, proc.darkBlue,
                3), "btnZoom");
        Button btnZoomOut = new Button(proc.customIcon(FontImage.MATERIAL_ZOOM_OUT, proc.darkBlue,
                3), "btnZoom");
        cntZoom.addAll(FlowLayout.encloseLeftMiddle(btnZoomIn),
                FlowLayout.encloseLeftMiddle(btnZoomOut));
        cntZoom.setVisible(false);

        cntMapPar.addAll(cntMap, cntZoom);

        form.add(BorderLayout.CENTER, cntMapPar);
        form.show();

        Display.getInstance().callSerially(() -> {

            //Get current location
            Location location = LocationManager.getLocationManager()
                    .getCurrentLocationSync();
            /*if (location == null) {
                ToastBar.showErrorMessage("Turn on your GPS Location"
                        + " and retry");
                Display.getInstance().callSerially(() -> {
                    prevForm.showBack();
                });
            } else {*/
            cntMap.setShowMyLocation(true);

            Coord myLoc = new Coord(location.getLatitude(),
                    location.getLongitude());

            proc.printLine("My Location Lat: " + location.getLatitude()
                    + " Lng: " + location.getLongitude());

            Style s = new Style();
            s.setFgColor(0x3399ff);
            s.setBgTransparency(0);
            imgMarker = FontImage.createMaterial(FontImage.MATERIAL_PLACE, s,
                    Display.getInstance().convertToPixels(1));

            /*PointsLayer layer = new PointsLayer();
            layer.setPointIcon(imgMarker);
            PointLayer point = new PointLayer(myLoc, "You are here", imgMarker);
            point.setDisplayName(true);
            layer.addPoint(point);
            cntMap.addLayer(layer);*/
 /*cntMap.addMarker(EncodedImage.createFromImage(imgMarker, false),
                    myLoc, "You", "Your current location",
                    evt -> {
                        ToastBar.showMessage("Your location",
                                FontImage.MATERIAL_PLACE);
                    });*/
            cntMap.addMarker(getMarkerAnim(), myLoc);
            cntMap.setCameraPosition(myLoc);
            cntMap.zoom(myLoc, zoomLevel);

            form.add(BorderLayout.NORTH, getDirectionCnt(myLoc));
            form.add(BorderLayout.SOUTH, getOptCnt());
            form.revalidate();

            cntZoom.setVisible(true);

            btnZoomIn.addActionListener(e -> {
                zoomLevel++;
                cntMap.zoom(myLoc, zoomLevel);
            });

            btnZoomOut.addActionListener(e -> {
                zoomLevel--;
                cntMap.zoom(myLoc, zoomLevel);
            });

        });

    }

    private Container getDirectionCnt(Coord myLoc) {

        Container cntPar = new Container(BoxLayout.y(), "cntDir");

        Container cntBtn = new Container(new GridLayout(1, 3));

        Button btnDir = new Button("Direction", "btnNav");
        btnArr.add(btnDir);
        Button btnGeo = new Button("Geofence", "btnNav");
        btnArr.add(btnGeo);
        Button btnType = new Button("Roadmap", "btnNav");
        btnArr.add(btnType);

        cntBtn.addAll(btnDir, btnGeo, btnType);
        cntPar.add(cntBtn);

        Container cntDir = new Container(BoxLayout.y());
        /*"Your Location "
                + myLoc.getLatitude() + "," + myLoc.getLongitude()*/
//        fromDefList = new DefaultListModel<>();
//        fromDefList.addItem("Your Location");
//        fromDefList.addItem("Others");
//        TableLayout tlFrom = new TableLayout(1, 2);
//        Container cntFrom = new Container(tlFrom);
//        Container cntFromYourLoc = new Container(BoxLayout.y());
//        Button btnFromYourLoc = new Button("Your Location");
//        cntFromYourLoc.add(btnFromYourLoc);
        Image iconFrom = proc.customIcon(FontImage.MATERIAL_COMPARE_ARROWS, proc.colorTeal, 4);
        CheckBox chkFrom = new CheckBox("From your location", iconFrom);
        chkFrom.setOppositeSide(true);

        DefaultListModel<String> optionsFrom = new DefaultListModel<>();

        tfFrom = new AutoCompleteTextField(optionsFrom) {

            @Override
            protected boolean filter(String text) {
                if (text.length() == 0) {
                    return false;
                }

                String[] locArr = searchLocations(text);
                if (locArr == null || locArr.length == 0) {
                    return false;
                }

                optionsFrom.removeAll();
                for (String s : locArr) {
                    optionsFrom.addItem(s);
                }

                return true;
            }

        };
        tfFrom.setHint("Choose starting place");
        tfFrom.setMinimumElementsShownInPopup(5);
        cntDir.addAll(chkFrom, tfFrom);

//        Button btnFrom = new Button("From");
//        cntFrom.setLeadComponent(btnFrom);
//        btnFrom.addActionListener(ev -> {
//            proc.printLine("From selected");
//            if (tfFrom.getText().equals("Your Location")) {
//                tfFrom.setText("");
//            }
//            cntFromYourLoc.setVisible(true);
//        });
//
//        cntFrom.add(tlFrom.createConstraint().widthPercentage(80), tfFrom);
//        cntFrom.add(tlFrom.createConstraint().widthPercentage(20), btnFrom);
//        cntDir.add(cntFrom).add(cntFromYourLoc);
//        //cntDir.add(tfFrom).add(cntFromYourLoc);
//        cntFromYourLoc.setVisible(false);
//        Container cntTo = new Container(BoxLayout.x());
//        Container cntToYourLoc = new Container(BoxLayout.y());
//        Button btnToYourLoc = new Button("Your Location");
//        cntToYourLoc.add(btnToYourLoc);
        Image iconTo = proc.customIcon(FontImage.MATERIAL_COMPASS_CALIBRATION, proc.colorTeal, 4);
        CheckBox chkTo = new CheckBox("To your location", iconTo);
        chkTo.setOppositeSide(true);

        DefaultListModel<String> optionsTo = new DefaultListModel<>();

        AutoCompleteTextField tfTo = new AutoCompleteTextField(optionsTo) {

            @Override
            protected boolean filter(String text) {
                if (text.length() == 0) {
                    return false;
                }

                String[] locArr = searchLocations(text);
                if (locArr == null || locArr.length == 0) {
                    return false;
                }

                optionsTo.removeAll();
                for (String s : locArr) {
                    optionsTo.addItem(s);
                }

                return true;
            }

        };
        tfTo.setHint("Choose Destination");
        tfTo.setMinimumElementsShownInPopup(5);
        cntDir.addAll(chkTo, tfTo);
        cntDir.setHidden(true);

        Container cntGeo = new Container(BoxLayout.y());
        DefaultListModel<String> optionsGeo = new DefaultListModel<>();

        AutoCompleteTextField tfGeo = new AutoCompleteTextField(optionsGeo) {

            @Override
            protected boolean filter(String text) {
                if (text.length() == 0) {
                    return false;
                }

                String[] locArr = searchLocations(text);
                if (locArr == null || locArr.length == 0) {
                    return false;
                }

                optionsGeo.removeAll();
                for (String s : locArr) {
                    optionsGeo.addItem(s);
                }

                return true;
            }

        };
        tfGeo.setHint("Choose Location");
        tfGeo.setMinimumElementsShownInPopup(5);
        cntGeo.add(tfGeo);
        cntGeo.setHidden(true);

        btnDir.addActionListener(e -> {
            zoomLevel = 18;
            proc.changeBtnUIID(btnArr, btnDir);
            cntDir.setHidden(false);
            cntGeo.setHidden(true);
            //cntDir.getParent().animateLayout(200);
            form.revalidate();
        });
        btnGeo.addActionListener(ev -> {
            proc.changeBtnUIID(btnArr, btnGeo);
            cntDir.setHidden(true);
            cntGeo.setHidden(false);
            //cntDir.getParent().animateLayout(200);
            form.revalidate();
        });

        ArrayList<String> typeArr = new ArrayList<>();
        typeArr.add("Roadmap");
        typeArr.add("Terrain");
        typeArr.add("Satellite");

        btnType.addActionListener(e -> {

            proc.changeBtnUIID(btnArr, btnType);

            Dialog d = new Dialog();
            d.setLayout(BoxLayout.y());
            d.getContentPane().setScrollableY(true);

            for (int j = 0; j < typeArr.size(); j++) {

                MultiButton btnM = new MultiButton(typeArr.get(j));
                btnM.setUIIDLine1("popLbl");

                d.add(btnM);
                btnM.addActionListener(evt -> {
                    d.dispose();

                    switch (btnM.getTextLine1()) {

                        case "Roadmap":
                            cntMap.setMapType(0);
                            break;

                        case "Terrain":
                            cntMap.setMapType(1);
                            break;

                        case "Satellite":
                            cntMap.setMapType(2);
                            break;
                    }
                    btnType.setText(btnM.getTextLine1());
                    form.revalidate();
                });
            }
            d.showPopupDialog(btnType);

        });

        chkFrom.addActionListener(e -> {
            if (chkFrom.isSelected()) {
                tfFrom.setText("Your Location");
                tfFrom.setEnabled(false);
                chkTo.setHidden(true);
                //form.revalidate();
            } else {
                tfFrom.setText("");
                tfFrom.setEnabled(true);
                chkTo.setHidden(false);
                //form.revalidate();
            }
            form.revalidate();

        });

        chkTo.addActionListener(e -> {
            if (chkTo.isSelected()) {
                tfTo.setText("Your Location");
                tfTo.setEnabled(false);
                chkFrom.setHidden(true);
                //form.revalidate();
            } else {
                tfTo.setText("");
                tfTo.setEditable(false);
                chkFrom.setHidden(false);
                //form.revalidate();
            }
            form.revalidate();

        });

//        Button btnTo = new Button("To");
//
//        btnTo.addActionListener(ev -> {
//            if (tfTo.getText().equals("Your Location")) {
//                tfTo.setText("");
//            }
//            cntToYourLoc.setVisible(true);
//            cntTo.removeComponent(btnTo);
//            form.revalidate();
//
//        });
//
//        cntTo.addAll(tfTo, btnTo);
        /*disables textfield editability*/
        //cntTo.setLeadComponent(btnTo);
        //cntToYourLoc.setVisible(false);
        optionsFrom.addSelectionListener((int oldVal, int newVal) -> {

            String searchAddr = optionsFrom.getItemAt(newVal);

            if (searchAddr != null) {

                //Display.getInstance().callSerially(() -> {
                if (tfTo.getText().equals("Your Location")) {
                    getCoord(myLoc, searchAddr);
                }
                //});

            }
        });

        optionsTo.addSelectionListener((int oldVal, int newVal) -> {

            String searchAddr = optionsTo.getItemAt(newVal);

            if (searchAddr != null) {

                //Display.getInstance().callSerially(() -> {
                if (tfFrom.getText().equals("Your Location")) {
                    getCoord(myLoc, searchAddr);
                }
                //});
            }
        });

        optionsGeo.addSelectionListener((int oldVal, int newVal) -> {

            String searchAddr = optionsTo.getItemAt(newVal);

            if (searchAddr != null) {

                //Display.getInstance().callSerially(() -> {
                getGeofenceCoord(myLoc, searchAddr);
                //});
            }
        });

//        tfFrom.addDataChangedListener((int type, int index) -> {
//
//            if (tfFrom.getText().length() == 0) {
//                cntFromYourLoc.setVisible(true);
//                form.revalidate();
//            } else {
//                cntFromYourLoc.setVisible(false);
//                form.revalidate();
//            }
//        });
//
//        btnFromYourLoc.addActionListener(e -> {
//            tfFrom.setText("Your Location");
//        });
//        tfTo.addDataChangedListener((int type, int index) -> {
//
//            if (tfTo.getText().length() == 0) {
//                //tfNext.startEditingAsync();
//                cntToYourLoc.setVisible(true);
//                form.revalidate();
//            } else {
//                cntToYourLoc.setVisible(false);
//                form.revalidate();
//            }
//        });
//
//        btnToYourLoc.addActionListener(e -> {
//            tfTo.setText("Your Location");
//
//        });
//        Button btnSearch = new Button("Search");
//        btnSearch.addActionListener(e -> {
//            getCoord(myLoc, tfTo.getText());
//        });
        cntPar.addAll(cntDir, cntGeo);
        return cntPar;
    }

    private Container getOptCnt() {
        lblDist = new Label("", proc.getMenuLblUIID());

        Container cntComp = new Container(new GridLayout(2, 2));
        Button btnNav = new Button("Navigate", "btnMap");
        btnNav.addActionListener(e -> {
            //cntMap.setCameraPosition(myLoc);
            LocationManager.getLocationManager()
                    .setLocationListener(new LocChangeListener(this));

        });

        Button btnClearAll = new Button("Clear", "btnMap");
        btnClearAll.addActionListener(e -> {
            cntMap.clearMapLayers();
        });
        cntComp.add(btnNav).add(btnClearAll).add(lblDist);
        return cntComp;
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

        } catch (IOException | IllegalArgumentException e) {
            ToastBar.showErrorMessage("no suggestions found");
        }

        return null;
    }

    private void getCoord(Coord myLoc, String address) {
        this.address = address;
        cntMap.clearMapLayers();

        infiniteDialog = new InfiniteProgress().showInfiniteBlocking();

        String url = "https://maps.googleapis.com/maps/api/geocode/json";

        try {

            ConnectionRequest request = new ConnectionRequest(url, false);
            request.addArgument("key", API_KEY);
            request.addArgument("address", address);
            request.setFailSilently(true);

            NetworkManager.getInstance().addToQueueAndWait(request);

            proc.printLine("RespCode " + request.getResponseCode());

            switch (request.getResponseCode()) {
                case 0:
                    infiniteDialog.dispose();
                    ToastBar.showErrorMessage("Connection failed");
                    break;
                case 404:
                    infiniteDialog.dispose();
                    ToastBar.showErrorMessage("Services Unavailable");
                    break;

                case 200:

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

                            //Distance
                            long dist = calDistance(
                                    myLoc.getLatitude(),
                                    myLoc.getLongitude(),
                                    searchCoord.getLatitude(),
                                    searchCoord.getLongitude());

                            lblDist.setText(String.valueOf(dist / 1000) + " KM");

                            //route
                            showRoute(myLoc, address, searchCoord.getLatitude(),
                                    searchCoord.getLongitude());

                        }
                    }
                    break;

                default:
                    infiniteDialog.dispose();
                    ToastBar.showErrorMessage("Processing failed");
                    break;
            }

        } catch (IOException e) {
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

    private void showRoute(Coord myLoc, String address, double destLat,
            double destLng) {

        cntMap.setCameraPosition(myLoc);
        cntMap.zoom(myLoc, zoomLevel);

        cntMap.addMarker(EncodedImage.createFromImage(imgMarker, false),
                myLoc, "You", "Your current location",
                evt -> {
                    ToastBar.showMessage("Your location",
                            FontImage.MATERIAL_PLACE);
                });

        cntMap.addMarker(EncodedImage.createFromImage(imgMarker, false),
                new Coord(destLat, destLng), address, "Long description",
                evt -> {
                    ToastBar.showMessage(address + " Loc " + destLat + " "
                            + destLng, FontImage.MATERIAL_PLACE);
                });

        cntMap.setPathStrokeColor(0xff0000);
        cntMap.setPathStrokeWidth(12);
        cntMap.setRotateGestureEnabled(true);
        cntMap.addPath(new Coord[]{myLoc, new Coord(destLat, destLng)});
        Display.getInstance().openNativeNavigationApp(destLng, destLng);

        infiniteDialog.dispose();
    }

    @Override
    public void getNewLoc(Location location) {

        proc.printLine("New Location " + location.getLatitude()
                + " " + location.getLongitude());

        Coord myNewLoc = new Coord(location.getLatitude(),
                location.getLongitude());

        updateRoute(myNewLoc);
    }

    private void updateRoute(Coord myNewLoc) {

        cntMap.clearMapLayers();

        cntMap.setCameraPosition(myNewLoc);
        /*cntMap.addMarker(EncodedImage.createFromImage(imgMarker, false),
                cntMap.getCameraPosition(), "Hi marker", "Long description",
                evt -> {
                    ToastBar.showMessage("Your new location",
                            FontImage.MATERIAL_PLACE);
                });*/
        cntMap.addMarker(getMarkerAnim(), myNewLoc);

        if (searchCoord != null) {
            cntMap.setCameraPosition(new Coord(searchCoord.getLatitude(),
                    searchCoord.getLongitude()));

            cntMap.addMarker(EncodedImage.createFromImage(imgMarker, false),
                    new Coord(searchCoord.getLatitude(),
                            searchCoord.getLongitude()), address, "Long description",
                    evt -> {
                        ToastBar.showMessage(address, FontImage.MATERIAL_PLACE);
                    });

            /*cntMap.addPath(myNewLoc, new Coord(searchCoord.getLatitude(),
                    searchCoord.getLongitude()));*/
        }

    }

    private void getGeofenceCoord(Coord myLoc, String address) {
        this.address = address;
        cntMap.clearMapLayers();

        infiniteDialog = new InfiniteProgress().showInfiniteBlocking();

        String url = "https://maps.googleapis.com/maps/api/geocode/json";

        try {

            ConnectionRequest request = new ConnectionRequest(url, false);
            request.addArgument("key", API_KEY);
            request.addArgument("address", address);
            request.setFailSilently(true);

            NetworkManager.getInstance().addToQueueAndWait(request);

            proc.printLine("RespCode " + request.getResponseCode());

            switch (request.getResponseCode()) {
                case 0:
                    infiniteDialog.dispose();
                    ToastBar.showErrorMessage("Connection failed");
                    break;
                case 404:
                    infiniteDialog.dispose();
                    ToastBar.showErrorMessage("Services Unavailable");
                    break;

                case 200:
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

                            cntMap.clearMapLayers();
                            cntMap.addMarker(EncodedImage.createFromImage(imgMarker, false),
                                    new Coord(searchCoord.getLatitude(),
                                            searchCoord.getLongitude()), address, "Long description",
                                    evt -> {
                                        ToastBar.showMessage(address, FontImage.MATERIAL_PLACE);
                                    });

                            cntMap.setCameraPosition(searchCoord);
                            cntMap.zoom(searchCoord, 30);

                            Location loc = new Location();
                            loc.setLatitude((double) location.get("lat"));
                            loc.setLongitude((double) location.get("lng"));

                            //test - unique identifier, loc - location, 30-radius,
                            //100000ms - expiration  
                            Geofence fence = new Geofence("test", loc, 30, 100000);
                            LocationManager.getLocationManager()
                                    .addGeoFencing(GeofenceListenerImpl.class, fence);

                            infiniteDialog.dispose();

                        }
                    }
                    break;

                default:
                    infiniteDialog.dispose();
                    ToastBar.showErrorMessage("Processing failed");
                    break;
            }

        } catch (IOException e) {
        }

    }

    public class GeofenceListenerImpl implements GeofenceListener {

        @Override
        public void onExit(String id) {

            proc.showToast("Exited geofence, id=" + id, FontImage.MATERIAL_EXIT_TO_APP).show();

        }

        @Override
        public void onEntered(String id) {

            proc.showToast("Entered geofence, id=" + id, FontImage.MATERIAL_INFO_OUTLINE).show();

            if (!Display.getInstance().isMinimized()) {
                Display.getInstance().callSerially(() -> {
                    Dialog.show("Welcome", "Thanks for arriving", "OK", null);
                });
            } else {
                LocalNotification ln = new LocalNotification();
                ln.setId("LnMessage");
                ln.setAlertTitle("Welcome");
                ln.setAlertBody("Thanks for arriving");
                Display.getInstance().scheduleLocalNotification(ln, 10,
                        LocalNotification.REPEAT_NONE);
            }
        }
    }

    private Container getMarkerAnim() {

        Container cntPar = new Container(BoxLayout.x());
        Container cntMask = new Container(new LayeredLayout());

        dimension = 100; //100, 200

        ArrayList<Label> lblArr = new ArrayList<>();

        int max = dimension / 15;
        int blockDimen = 20;

        if (max > 0) {

            for (int w = 1; w <= max; w++) {

                if (dimension - 20 > blockDimen) {

                    //proc.printLine("Dimens=" + dimension+"\nWaves=" + w);
                    if (dimension > 0) {

                        Label lblMarkerAnim = new MarkerAnimCmp(dimension);
                        lblMarkerAnim.setUIID("lblMarkerAnim");

                        cntMask.add(FlowLayout.encloseCenterMiddle(lblMarkerAnim));
                        lblArr.add(lblMarkerAnim);

                        dimension = dimension - 15;
                    }
                }
            }

            Image roundBlock = Image.createImage(blockDimen, blockDimen, 0);
            Graphics gr3 = roundBlock.getGraphics();
            gr3.setColor(0x0000FF); //0x0000FF 0x15E7FF
            gr3.fillArc(0, 0, blockDimen, blockDimen, 0, 360);
            Object mask3 = roundBlock.createMask();
            roundBlock.applyMask(mask3);
            Label lblBlock = new Label(roundBlock);
            cntMask.add(FlowLayout.encloseCenterMiddle(lblBlock));
            lblArr.add(lblBlock);

            cntPar.addAll(cntMask, new Label("Your location", "lblMyLoc"));

            //make fist wave visible by default
            for (int len = 0; len < lblArr.size(); len++) {
                if (len == lblArr.size() - 1) {
                    lblArr.get(lblArr.size() - 1).setVisible(true);
                } else {
                    lblArr.get(len).setVisible(false);
                }
            }

            waveCount = lblArr.size();  // 3

            new UITimer(() -> {

                //proc.printLine("Wavecount=" + waveCount); //3, 2, 1
                for (int c = 0; c < lblArr.size(); c++) {
                    if (c == waveCount - 1) {
                        lblArr.get(waveCount - 1).setVisible(true); //2,1,0
                        //proc.printLine("Cnt=" + lblArr.get(waveCount - 1) + " W=" + lblArr.get(waveCount - 1).getWidth() + " H=" + lblArr.get(waveCount - 1).getHeight());

                    } else {
                        lblArr.get(c).setVisible(false);
                    }
                }

                cntMask.revalidate();
                waveCount--;

                if (waveCount == 0) {
                    waveCount = lblArr.size();
                }

            }).schedule(250, true, form);
        }
        return cntPar;
    }

    private class MarkerAnimCmp extends Label {

        int dimen;

        private MarkerAnimCmp(int dimen) {
            this.dimen = dimen;
        }

        @Override
        public Dimension calcPreferredSize() {
            return new Dimension(dimen, dimen);
        }
    }

    private void suggestFromLoc() {

        //fromDefList = new DefaultListModel<>();
        tfFrom = new AutoCompleteTextField(fromDefList) {

            @Override
            protected boolean filter(String text) {
                if (text.length() == 0) {
                    return false;
                }

                String[] locArr = searchLocations(text);
                if (locArr == null || locArr.length == 0) {
                    return false;
                }

                fromDefList.removeAll();
                for (String s : locArr) {
                    fromDefList.addItem(s);
                }

                return true;
            }

        };
    }

    public MapFt(com.codename1.ui.util.Resources resourceObjectInstance) {
        //initGuiBuilderComponents(resourceObjectInstance);
    }

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////-- DON'T EDIT BELOW THIS LINE!!!


// <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initGuiBuilderComponents(com.codename1.ui.util.Resources resourceObjectInstance) {
        setLayout(new com.codename1.ui.layouts.FlowLayout());
        setInlineStylesTheme(resourceObjectInstance);
                setInlineStylesTheme(resourceObjectInstance);
        setTitle("MapFt");
        setName("MapFt");
    }// </editor-fold>

//-- DON'T EDIT ABOVE THIS LINE!!!
}


/* tfFrom.addDataChangedListener((int type, int index) -> {
            if (tfFrom.getText().length() == 0) {
                tfFrom.setText("Your Location");
                acTf.startEditingAsync();
            }
        });
 */
 /* fromDefList.addSelectionListener((int oldVal, int newVal) -> {

            //System.out.println(fromDefList.getItemAt(newVal).trim());
            System.out.println("Nval " + newVal);
            //if (fromDefList.getItemAt(newVal).trim().equals("Your Location")) {
            if (newVal == 0) {
                //Display.getInstance().callSerially(() -> {
                tfFrom.setText(fromDefList.getItemAt(newVal).trim());
                acTf.startEditingAsync();

                //});
            } else {
                tfFrom.setText("");
                suggestFromLoc();
               
            }
            
            if (fromDefList.getItemAt(newVal).trim().equals("Other")) {
               // tfFrom.setText("");
                suggestFromLoc();
            }
        });*/
        //Container cntSearch = new Container(new GridLayout(1, 2));
