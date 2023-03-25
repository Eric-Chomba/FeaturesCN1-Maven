/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zomuhtech.cn.features;

/**
 *
 * @author Eric
 */
import com.codename1.components.SpanLabel;
import com.zomuhtech.cn.features.advft.chat.*;
import com.codename1.components.Switch;
import com.zomuhtech.cn.features.advft.*;
import com.zomuhtech.cn.features.procs.Proc;
import static com.codename1.ui.CN.*;
import com.codename1.ui.Form;
import com.codename1.ui.Label;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.Resources;
import com.codename1.ui.Toolbar;
import com.codename1.ui.layouts.*;
import com.codename1.ui.Button;
import com.codename1.ui.Container;
import com.codename1.ui.Command;
import com.codename1.ui.Display;
import com.codename1.ui.events.ActionEvent;
import com.codename1.system.NativeLookup;
import com.codename1.ui.Component;
import com.codename1.ui.FontImage;
import com.codename1.ui.Image;
import com.codename1.ui.TextArea;
import com.codename1.ui.TextField;
import com.codename1.ui.plaf.Border;
import com.codename1.ui.util.SwipeBackSupport;
import com.zomuhtech.cn.features.advft.designs.DesignDashboard;
import com.zomuhtech.cn.features.advft.ml.AndroidML;
import com.zomuhtech.cn.features.advft.route.*;
import java.io.IOException;
import java.util.ArrayList;

public class Dashboard extends Form {

    Form form;
    Proc proc = new Proc();
    boolean /*sideMenuOpen,*/ darkMode;
    String version;
    Button btnLang;
    Switch onOff;
    ArrayList<Container> cntArr;
    ArrayList<String> cntDetailsArr, searchResArr, searchNameResArr;
    ArrayList<Image> cntIconArr, searchIconArr;
    ArrayList<Component> menuSearchArr;
    Container cntCenterMenus, cntSearch, cntSearchBox;

    public Dashboard() {

        this.darkMode = false;
        this.cntArr = new ArrayList<>();
        this.cntDetailsArr = new ArrayList<>();
        this.cntIconArr = new ArrayList<>();

        Display.getInstance().callSerially(() -> {
            createUI();
        });

    }

    private void createUI() {
        form = new Form("CN1 Features", new BorderLayout());
        //Get app version
        version = Display.getInstance().getProperty("AppVersion", "1.0");

        Toolbar tb = form.getToolbar();
        tb.setUIID("tbar");
        tb.getTitleComponent().setUIID(proc.getLblTitleUIID());

        form.setBackCommand(new Command("exit") {
            @Override
            public void actionPerformed(ActionEvent ev) {

                Display.getInstance().callSerially(() -> {
                    if (/*sideMenuOpen ||*/proc.getSideMenuOpen().equals("Yes")) {
                        tb.closeSideMenu();
                        //sideMenuOpen = false;
                        proc.setSideMenuOpen("No");
                    } else {
                        //System.exit(0);
                        Display.getInstance().exitApplication();
                    }
                });
            }
        });

        //disable side menu swipe
        tb.setEnableSideMenuSwipe(false);

        //Enable swipe back to open & detect sidemenu
        SwipeBackSupport.bindBack(form, (args) -> {
            tb.openSideMenu();
            //sideMenuOpen = true;
            proc.setSideMenuOpen("Yes");
            return form.getComponentForm();
        });

        Image profilePic = proc.menuIcon(FontImage.MATERIAL_ACCOUNT_CIRCLE);
        tb.addComponentToSideMenu(sideMenuItems(form, tb, profilePic));

        Command menuCmd = Command.create("",
                proc.materialIcon(FontImage.MATERIAL_MENU), evt -> {
            //checkRTLLang();
            tb.openSideMenu();
            //sideMenuOpen = true;
            proc.setSideMenuOpen("Yes");
        });

        tb.addCommandToLeftBar(menuCmd);

        cntCenterMenus = new Container(new GridLayout(18, 3),
                "cntMenus");
        cntCenterMenus.setScrollableY(true);
        cntCenterMenus.setScrollVisible(false);

        cntCenterMenus.add(getMenu("1", "Input", "Fields",
                proc.menuIcon(FontImage.MATERIAL_INPUT), form));
        cntCenterMenus.add(getMenu("1.1", "Custom", "Keyboard",
                proc.menuIcon(FontImage.MATERIAL_KEYBOARD), form));
        cntCenterMenus.add(getMenu("1.2", "na", "SQLite",
                proc.menuIcon(FontImage.MATERIAL_STORE), form));
        cntCenterMenus.add(getMenu("1.3", "na", "Pickers",
                proc.menuIcon(FontImage.MATERIAL_SELECT_ALL), form));
        cntCenterMenus.add(getMenu("1.4", "na", "Signature",
                proc.menuIcon(FontImage.MATERIAL_DRAW), form));
        cntCenterMenus.add(getMenu("1.5", "Fingerprint", "Auth",
                proc.menuIcon(FontImage.MATERIAL_FINGERPRINT), form));
        cntCenterMenus.add(getMenu("1.6", "na", "Sign In",
                proc.menuIcon(FontImage.MATERIAL_LOCK_OPEN), form));
        cntCenterMenus.add(getMenu("2", "na", "Images",
                proc.menuIcon(FontImage.MATERIAL_IMAGE), form));
        cntCenterMenus.add(getMenu("2.1", "Crop", "Image",
                proc.menuIcon(FontImage.MATERIAL_CAMERA), form));
        //cntCenterMenus.add(getMenu("3", "na", "Maps",
                //proc.menuIcon(FontImage.MATERIAL_MAP), form));
        //cntCenterMenus.add(getMenu("3.1", "Route on", "Map",
               // proc.menuIcon(FontImage.MATERIAL_DIRECTIONS), form));
        cntCenterMenus.add(getMenu("5", "Tabs&", "Fragments",
                proc.menuIcon(FontImage.MATERIAL_TAB), form));
        cntCenterMenus.add(getMenu("6", "Dync OnBoard", "&Carousel",
                proc.menuIcon(FontImage.MATERIAL_COMPARE), form));
        cntCenterMenus.add(getMenu("7", "Bottom", "Navigation",
                proc.menuIcon(FontImage.MATERIAL_NAVIGATION), form));
        cntCenterMenus.add(getMenu("8", "Infinite Adapter", "&Container",
                proc.menuIcon(FontImage.MATERIAL_LIST_ALT), form));
        cntCenterMenus.add(getMenu("9", "na", "Lists",
                proc.menuIcon(FontImage.MATERIAL_LIST), form));
        cntCenterMenus.add(getMenu("10", "Dynamic", "Table",
                proc.menuIcon(FontImage.MATERIAL_TABLE_VIEW), form));
        cntCenterMenus.add(getMenu("11", "na", "Trees",
                proc.menuIcon(FontImage.MATERIAL_TRENDING_DOWN), form));
        cntCenterMenus.add(getMenu("12", "Dynamic", "Accordion",
                proc.menuIcon(FontImage.MATERIAL_EXPAND_MORE), form));
        cntCenterMenus.add(getMenu("13", "na", "Share",
                proc.menuIcon(FontImage.MATERIAL_SHARE), form));
        cntCenterMenus.add(getMenu("14", "Media", "Player",
                proc.menuIcon(FontImage.MATERIAL_MULTITRACK_AUDIO), form));
        cntCenterMenus.add(getMenu("15", "Pull to", "Refresh",
                proc.menuIcon(FontImage.MATERIAL_REFRESH), form));
        cntCenterMenus.add(getMenu("16", "Images", "View Flip",
                proc.menuIcon(FontImage.MATERIAL_FLIP), form));
        //cntCenterMenus.add(getMenu("17", "Receive", "SMS",
               // proc.menuIcon(FontImage.MATERIAL_SMS), form));
        cntCenterMenus.add(getMenu("18", "Toolbar", "Search",
                proc.menuIcon(FontImage.MATERIAL_SEARCH), form));
        /*cntCenterMenus.add(getMenu("19", "na", "Contacts",
                proc.menuIcon(FontImage.MATERIAL_CONTACTS), form));*/
        cntCenterMenus.add(getMenu("20", "Collapsing", "Toolbar",
                proc.menuIcon(FontImage.MATERIAL_EXPAND_LESS), form));
        cntCenterMenus.add(getMenu("21", "na", "Web",
                proc.menuIcon(FontImage.MATERIAL_WEB), form));
        cntCenterMenus.add(getMenu("22", "JavaScript", "Interation",
                proc.menuIcon(FontImage.MATERIAL_WEB_STORIES), form));
        cntCenterMenus.add(getMenu("24", "Swipe", "Container",
                proc.menuIcon(FontImage.MATERIAL_SWIPE), form));
        cntCenterMenus.add(getMenu("25", "Charts &", "Visualization",
                proc.menuIcon(FontImage.MATERIAL_PIE_CHART), form));
        cntCenterMenus.add(getMenu("27", "na", "Animations",
                proc.menuIcon(FontImage.MATERIAL_ANIMATION), form));
        cntCenterMenus.add(getMenu("28", "na", "Transition",
                proc.menuIcon(FontImage.MATERIAL_ANALYTICS), form));
        cntCenterMenus.add(getMenu("29", "Local", "Storage",
                proc.menuIcon(FontImage.MATERIAL_STORAGE), form));
        cntCenterMenus.add(getMenu("30", "Networking", "& Encryption",
                proc.menuIcon(FontImage.MATERIAL_ENHANCED_ENCRYPTION),
                form));
        cntCenterMenus.add(getMenu("31", "na", "Externalization",
                proc.menuIcon(FontImage.MATERIAL_STOREFRONT), form));
        cntCenterMenus.add(getMenu("32", "Files", "Parsing",
                proc.menuIcon(FontImage.MATERIAL_TRANSFORM), form));
        cntCenterMenus.add(getMenu("33", "na", "Properties",
                proc.menuIcon(FontImage.MATERIAL_HIDE_SOURCE), form));
        cntCenterMenus.add(getMenu("34", "na", "Phone",
                proc.menuIcon(FontImage.MATERIAL_PHONE), form));
        cntCenterMenus.add(getMenu("35", "na", "Localization",
                proc.menuIcon(FontImage.MATERIAL_TRANSLATE), form));
        cntCenterMenus.add(getMenu("36", "Localization", "Manager",
                proc.menuIcon(FontImage.MATERIAL_LOCAL_LIBRARY), form));
        cntCenterMenus.add(getMenu("37", "Graphics &", "Drawing",
                proc.menuIcon(FontImage.MATERIAL_BRUSH), form));
        cntCenterMenus.add(getMenu("39", "CN1Lib", "Dev",
                proc.menuIcon(FontImage.MATERIAL_CODE), form));
        cntCenterMenus.add(getMenu("40", "Drag", "Drop",
                proc.menuIcon(FontImage.MATERIAL_DRAG_HANDLE), form));
        cntCenterMenus.add(getMenu("41", "Layout", "Manager",
                proc.menuIcon(FontImage.MATERIAL_LAYERS), form));
        cntCenterMenus.add(getMenu("42", "Local", "Notification",
                proc.menuIcon(FontImage.MATERIAL_NOTIFICATIONS), form));
        cntCenterMenus.add(getMenu("43", "na", "Chat",
                proc.menuIcon(FontImage.MATERIAL_CHAT), form));
//        cntCenterMenus.add(getMenu("43.1", "RADChat", "Room",
//                proc.menuIcon(FontImage.MATERIAL_CHAT), form));
        cntCenterMenus.add(getMenu("44", "QR&Bar", "Codes",
                proc.menuIcon(FontImage.MATERIAL_QR_CODE), form));
        cntCenterMenus.add(getMenu("45", "na", "Sensors",
                proc.menuIcon(FontImage.MATERIAL_SENSORS), form));
        cntCenterMenus.add(getMenu("46", "Machine", "Learning",
                proc.menuIcon(FontImage.MATERIAL_SCANNER), form));
        /*cntCenterMenus.add(getMenu("47", "Mobile", "Payment",
                proc.menuIcon(FontImage.MATERIAL_PAYMENTS), form));*/
        cntCenterMenus.add(getMenu("48", "na", "Designs",
                proc.menuIcon(FontImage.MATERIAL_DRAW), form));

        cntSearch = new Container(new GridLayout(18, 3),
                "cntMenus");
        cntSearch.setScrollableY(true);
        //form.add(NORTH, cntSearch);
        form.add(CENTER, cntCenterMenus);
        form.revalidate();
        //form.addAll(cntSearch, cntCenterMenus);
        //cntSearch.setHidden(true);
        //cntSearch.setVisible(false);

        //searchMenu(form, cntSearch, cntCenterMenus);
        searchMenu();

        form.show();

    }

    private Container getMenu(String menu, String lbl1, String lbl2,
            Image iconImg, Form form) {

        Container cntMenuPar1 = new Container(BoxLayout.y());
        cntMenuPar1.setUIID("menuImgBg");
        if (!lbl1.equals("na")) {
            cntMenuPar1.setName(lbl1.toLowerCase() + lbl2.toLowerCase());
        } else {
            cntMenuPar1.setName(lbl2.toLowerCase());
        }
        cntArr.add(cntMenuPar1);
        cntDetailsArr.add(menu + "#" + lbl1 + "#" + lbl2);
        cntIconArr.add(iconImg);

        try {
            Container cntMenuLbl1 = new Container(BoxLayout.y());
            if (!lbl1.equals("na")) {
                cntMenuLbl1.add(new Label(lbl1, proc.getMenuLblUIID()));
            }
            Button btnMenu1 = new Button(lbl2, proc.getMenuLblBtnUIID());
            btnMenu1.addActionListener(e -> {

                switch (menu) {
                    case "1":
                        new UserInput(form).show();
                        break;
                    case "1.1":
                        new CustomKeyboard(form).show();
                        break;
                    case "1.2":
                        new SQLiteStore(form).show();
                        break;
                    case "1.3":
                        new PickersFt(form).show();
                        break;
                    case "1.4":
                        new SignatureFt(form).show();
                        break;
                    case "1.5":
                        new FingerprintAuthFt(form).show();
                        break;
                    case "1.6":
                        new SignIn(form).show();
                        break;
                    case "2":
                        new Images(form).show();
                        break;
                    case "2.1":
                        new CropImg(form).show();
                        break;
                    case "3":
                        new MapFt(form).show();
                        break;
                    case "3.1":
                        if (Display.getInstance().getPlatformName().equals("and")) {
                            //new RouteOnMapFt(form).show();
                            AndNativeRoute and = NativeLookup.create(AndNativeRoute.class);
                            and.showRoute("Nairobi CBD:-1.2832207:36.8198298:DarkMode:"
                                    + proc.getDarkMode() + ":MapKey:" + proc.getMapKey() + ",");
                        } else {
                            proc.showToast("Not available for this device yet",
                                    FontImage.MATERIAL_INFO).show();
                        }
                        break;
                    case "5":
                        new Fragments(form).show();
                        break;
                    case "6":
                        new OnBoard(form).show();
                        break;
                    case "7":
                        new BottomNavigation(form).show();
                        break;
                    case "8":
                        new InfiniteScrollAdapterFt(form).show();
                        break;
                    case "9":
                        new ListFt(form).show();
                        break;
                    case "10":
                        new TableFt(form).show();
                        break;
                    case "11":
                        new TreeFt(form).show();
                        break;
                    case "12":
                        new AccordionFt(form).show();
                        break;
                    case "13":
                        new ShareFt(form).show();
                        break;
                    case "14":
                        new MediaPlayerFt(form).show();
                        break;
                    case "15":
                        new PullToRefresh(form).show();
                        break;
                    case "16":
                        new ImagesFlipFt(form).show();
                        break;
                    case "17":
                        if (Display.getInstance().getPlatformName().equals("and")) {
                            new ReceiveSMSFt(form).show();
                        } else {
                            proc.showToast("Not available for this device yet",
                                    FontImage.MATERIAL_INFO).show();
                        }
                        break;
                    case "18":
                        new SearchFt(form).show();
                        break;
                    case "19":
                        //new ContactFt(form).show();
                        break;
                    case "20":
                        new CollapsingToolbar(form).show();
                        break;
                    case "21":
                        new Web(form).show();
                        break;
                    case "22":
                        new JSFt(form).show();
                        break;
                    case "24":
                        new SwipeCnt(form).show();
                        break;
                    case "25":
                        new ChartsFt(form).show();
                        break;
                    case "27":
                        new Anim(form).show();
                        break;
                    case "28":
                        new TransitionFt(form).show();
                        break;
                    case "29":
                        //Encrypt Preferences & Storage with login password as key
                        //EncryptedStorage.install("321qriE");
                        new StorageFt(form).show();
                        break;
                    case "30":
                        new NetworkingFt(form).show();
                        break;
                    case "31":
                        //Encrypt Preferences & Storage with login password as key
                        //EncryptedStorage.install("321qriE");
                        new ExternalizeFt(form).show();
                        break;
                    case "32":
                        new ParsingFt(form).show();
                        break;
                    case "33":
                        new PropertiesFt(form).show();
                        break;
                    case "34":
                        new PhoneFt(form).show();
                        break;
                    case "35":
                        new LocalizationFt(form).show();
                        break;
                    case "36":
                        new LocalizationMgrFt(form).show();
                        break;
                    case "37":
                        new GraphicsDrawingFt(form).show();
                        break;
                    case "39":
                        new DevCN1LibFt(form).show();
                        break;
                    case "40":
                        new DragDropFt(form).show();
                        break;
                    case "41":
                        new LayoutManagerFt(form).show();
                        break;
                    case "42":
                        new LocalNotificationFt(form).show();
                        break;
                    case "43":
                        //Encrypt Preferences & Storage with login password as key
                        //EncryptedStorage.install("321qriE");
                        new ChatFt(form).show();
                        break;

                    case "43.1":
                        //Encrypt Preferences & Storage with login password as key
                        //EncryptedStorage.install("321qriE");
                        //new ChatFt(form).show();

                        //new ChatController(this, form).getView().show();
                        break;
                    case "44":
                        new QRBarCodes(form).show();
                        break;
                    case "45":
                        new SensorsFt(form).show();
                        break;
                    case "46":
                        if (Display.getInstance().getPlatformName().equals("and")) {
                            AndroidML andML = NativeLookup.create(AndroidML.class);
                            andML.machineLearning("Barcode Scanning:OCR:DarkMode:"
                                    + proc.getDarkMode() + ",");
                        } else {
                            proc.showToast("Not available for this device yet",
                                    FontImage.MATERIAL_INFO).show();
                        }
                        break;
                    case "47":
                        //new PaymentFt(form).show();
                        break;
                    case "48":
                        new DesignDashboard(form).show();
                        break;
                }

            });
            cntMenuLbl1.add(btnMenu1);
            Container imgLay = new Container(new FlowLayout(CENTER));

            Container cntParRound = new Container(BoxLayout.y(), "menuParRound");

            //proc.materialIcon(FontImage.MATERIAL_CAMERA);
            Label lblMenuIcon = new Label(iconImg, proc.getMenuImgBgUIID());

            imgLay.add(lblMenuIcon);

            cntParRound.add(imgLay);

            cntMenuPar1.add(cntParRound).add(FlowLayout.encloseCenter(cntMenuLbl1));
            cntMenuPar1.setLeadComponent(btnMenu1);

        } catch (Exception e) {
        }

        return cntMenuPar1;
    }

    private Container sideMenuItems(Form form, Toolbar tb, Image profilePic) {

        Container nav = new Container(BoxLayout.y(), "cntNavPar");

        Border border = Border.createCompoundBorder(null,
                Border.createLineBorder(2, 0x15E7FF), null, null);

        Container topBar = BorderLayout.west(new Label(profilePic));
        topBar.add(BorderLayout.SOUTH, new Label("Username", "lblUser"));
        topBar.setUIID("topBar");
        topBar.getAllStyles().setBorder(border);
        nav.add(topBar);

        try {
            Container cntItem = new Container(new GridLayout(1, 2), "navCnt");
            Container cntRTL = new Container(BoxLayout.x());
            Button btnItem1 = new Button("RTL", proc.getMenuLblUIID());
            cntRTL.addAll(new Label(proc.sideMenuIcon(FontImage.MATERIAL_ALIGN_HORIZONTAL_RIGHT),
                    "navIcon"), btnItem1);
            onOff = new Switch();
            cntItem.addAll(cntRTL, FlowLayout.encloseRightMiddle(onOff));
            cntItem.setLeadComponent(btnItem1);

            btnItem1.addActionListener(e -> {
                //Enable Right to Left(RTL)/Bi Directional(BIDI)
                //proc.printLine("RTL set " + proc.getIsRTL());
                if (proc.getIsRTL() != null) {
                    //UIManager.getInstance().getLookAndFeel().setRTL(false);
                    if (proc.getIsRTL().equals("Yes")) {
                        proc.setIsRTL("No");
                        onOff.setOff();
                    } else if (proc.getIsRTL().equals("No")) {
                        //UIManager.getInstance().getLookAndFeel().setRTL(true);
                        proc.setIsRTL("Yes");
                        onOff.setOn();
                    }
                } else if (proc.getIsRTL() == null) {
                    //UIManager.getInstance().getLookAndFeel().setRTL(true);
                    proc.setIsRTL("Yes");
                    onOff.setOn();
                }

                //checkRTLLang();
                tb.closeSideMenu();
                proc.setSideMenuOpen("No");
                //sideMenuOpen = false;

                proc.showToast("Restart app to apply changes",
                        FontImage.MATERIAL_INFO).show();

            });

            nav.add(cntItem);
            cntItem.getAllStyles().setBorder(border);

        } catch (Exception e) {
        }

        try {
            Container cntItem = new Container(BoxLayout.x(), "navCnt");
            cntItem.add(new Label(proc.sideMenuIcon(FontImage.MATERIAL_TRANSLATE),
                    "navIcon"));

            Button btnItem1 = new Button("Change Language", proc.getMenuLblUIID());
            btnItem1.addActionListener(e -> {
                tb.closeSideMenu();
                //sideMenuOpen = false;
                proc.setSideMenuOpen("No");
                new LocalizationFt(form).show();
            });
            cntItem.add(btnItem1);
            cntItem.setLeadComponent(btnItem1);
            nav.add(cntItem);
            cntItem.getAllStyles().setBorder(border);

        } catch (Exception e) {
        }

        try {
            Container cntItem = new Container(BoxLayout.x(), "navCnt");
            cntItem.add(new Label(proc.sideMenuIcon(FontImage.MATERIAL_LANGUAGE),
                    "navIcon"));
            btnLang = new Button("Language " + proc.getCurrentLang(),
                    proc.getMenuLblUIID());
            btnLang.addActionListener(e -> {
                tb.closeSideMenu();
                //sideMenuOpen = false;
                proc.setSideMenuOpen("No");
            });
            cntItem.add(btnLang);
            cntItem.setLeadComponent(btnLang);
            nav.add(cntItem);
            cntItem.getAllStyles().setBorder(border);
        } catch (Exception e) {
        }

        try {
            Container cntItem = new Container(BoxLayout.x(), "navCnt");
            cntItem.add(new Label(proc.sideMenuIcon(FontImage.MATERIAL_APPS),
                    "navIcon"));

            Button btnItem1 = new Button("Version " + version, proc.getMenuLblUIID());
            btnItem1.addActionListener(e -> {
                tb.closeSideMenu();
                //sideMenuOpen = false;
                proc.setSideMenuOpen("No");

            });
            cntItem.add(btnItem1);
            cntItem.setLeadComponent(btnItem1);
            nav.add(cntItem);
            cntItem.getAllStyles().setBorder(border);
        } catch (Exception e) {
        }

        try {
            Container cntItem = new Container(new GridLayout(1, 2), "navCnt");
            Container cntRTL = new Container(BoxLayout.x());
            Button btnItem1 = new Button("Dark mode", proc.getMenuLblUIID());
            cntRTL.addAll(new Label(proc.sideMenuIcon(FontImage.MATERIAL_BRIGHTNESS_MEDIUM),
                    "navIcon"), btnItem1);
            Switch swMode = new Switch();
            cntItem.addAll(cntRTL, FlowLayout.encloseRightMiddle(swMode));
            cntItem.setLeadComponent(btnItem1);

            if (proc.getDarkMode().equals("On")) {
                swMode.setOn();
            } else {
                swMode.setOff();
            }

            btnItem1.addActionListener(e -> {
                //tb.closeSideMenu();
                //sideMenuOpen = false;
                if (proc.getDarkMode().equals("On")) {
                    form.getAllStyles().setBgColor(proc.blueGray);
                } else {
                    form.getAllStyles().setBgColor(proc.white);
                }
                changeTheme(swMode, tb);

            });

            nav.add(cntItem);
            cntItem.getAllStyles().setBorder(border);

        } catch (Exception e) {
        }

        try {
            Container cntItem = new Container(BoxLayout.x(), "navCnt");
            cntItem.add(new Label(proc.sideMenuIcon(FontImage.MATERIAL_CLOSE),
                    "navIcon"));

            Button btnItem1 = new Button("Close", proc.getMenuLblUIID());
            btnItem1.addActionListener(e -> {
                tb.closeSideMenu();
                //sideMenuOpen = false;
                proc.setSideMenuOpen("No");

            });
            cntItem.add(btnItem1);
            cntItem.setLeadComponent(btnItem1);
            nav.add(cntItem);
            cntItem.getAllStyles().setBorder(border);
        } catch (Exception e) {
        }
        //parSideNav.add(CENTER, nav);

        return nav;
        //return parSideNav;
    }

    private void changeTheme(Switch swMode, Toolbar tb) {

        darkMode = !darkMode;

        String themeFile = darkMode ? "/theme-dark" : "/theme";

        try {
            Resources themeRes = Resources.openLayered(themeFile);
            UIManager.getInstance().addThemeProps(themeRes.getTheme(themeRes.getThemeResourceNames()[0]));
            Display.getInstance().getCurrent().refreshTheme();
        } catch (IOException e) {
            proc.printLine("Theme Error " + e);
        }

        proc.printLine("DarkMode " + darkMode);
        //proc.printLine("SideMenuOpenBool " + sideMenuOpen);
        proc.printLine("SideMenuOpenStr " + proc.getSideMenuOpen());

        //Display.getInstance().callSerially(() -> {
        //UIManager.initFirstTheme(themeFile);
        //Display.getInstance().getCurrent().refreshTheme();
        // });
        //Display.getInstance().callSerially(() -> {
        if (darkMode) {
            proc.setDarkMode("On");
            swMode.setOn();
            //sideMenuOpen = false;
            // tb.closeSideMenu();

        } else {
            proc.setDarkMode("Off");
            swMode.setOff();
            //sideMenuOpen = false;
            //tb.closeSideMenu();
        }

        tb.closeSideMenu();
        proc.setSideMenuOpen("No");
        //sideMenuOpen = false;

        proc.printLine("DarkMode2 " + darkMode);
        //proc.printLine("SideMenuOpenBool2 " + sideMenuOpen);
        proc.printLine("SideMenuOpenStr2 " + proc.getSideMenuOpen());

        proc.showToast("Theme changed", FontImage.MATERIAL_INFO_OUTLINE).show();
        //});
        form.revalidate();

    }

    private void searchMenu() {

        cntSearchBox = proc.getInputCnt();
        TextField tfSearch = proc.getSearchTf("Search menu", TextArea.ANY);
        cntSearchBox.add(tfSearch);
        form.add(NORTH, cntSearchBox);
        cntSearchBox.setHidden(true);
        cntSearchBox.setVisible(false);

        /*TextField tfSearch = new TextField("", "CN1 Features")
        form.getToolbar().setTitleComponent(tfSearch);
        form.getToolbar().addCommandToRightBar("",
                proc.materialIcon(FontImage.MATERIAL_SEARCH), (e) -> {
            tfSearch.startEditingAsync();
        });*/
        cntCenterMenus.addScrollListener((int scrollX, int scrollY,
                int oldScrollX, int oldScrollY) -> {

            //proc.printLine("ScrollX = " + scrollX + " ScrollY = " + scrollY
            //  + " OldScrollX = " + oldScrollX + " OldScrollY = " + oldScrollY);
            /*for (int c = 0; c <= cntArr.size(); c++) {
                proc.printLine("cntcount=" + c);
                if (c <= cntArr.size()) {*/
            if (scrollY > 0 && oldScrollY > 0) {
                if (scrollY > oldScrollY) {
                    //proc.printLine("\nScrolling up....");
                    tfSearch.setEnabled(true);
                    cntSearchBox.setHidden(false);
                    cntSearchBox.setVisible(true);

                } else if (scrollY < oldScrollY) {
                    //proc.printLine("\nScrolling down....");
                    tfSearch.stopEditing();
                    tfSearch.setEnabled(false);
                    cntSearchBox.setHidden(true);
                    cntSearchBox.setVisible(false);
                }
                Display.getInstance().callSerially(() -> {
                    form.revalidate();
                });
            }
            // }
            /*}else{
                proc.printLine("last cnt is visible");
            }*/
        });

        tfSearch.addDataChangedListener((i1, i2) -> {

            //stores unique menu names matching search
            this.searchNameResArr = new ArrayList<>();
            //stores menus matching search
            this.menuSearchArr = new ArrayList<>();
            //stores menu details(no, label1&2) matching search
            this.searchResArr = new ArrayList<>();
            //stores menu icons matching search
            this.searchIconArr = new ArrayList<>();
            //remove all menus in search container so as to add newly 
            //searched menus
            cntSearch.removeAll();
            //tfSearch.startEditingAsync();

            String search = tfSearch.getText();

            if (search.length() < 1) {
                cntSearchBox.setHidden(true);
                cntSearchBox.setVisible(false);
                cntSearch.removeAll();
                form.add(CENTER, cntCenterMenus);
                form.revalidate();
                //scroll to first menu
                Display.getInstance().callSerially(() -> {
                    for (Container cnt : cntArr) {
                        if (cnt.getName().equals("inputfields")) {
                            form.scrollComponentToVisible(cnt);
                            break;
                        }
                    }
                });

            } else {
                search = search.toLowerCase();

                for (int j = 0; j < cntArr.size(); j++) {
                    Component cmp = cntArr.get(j);

                    String searchMenu = cntDetailsArr.get(j);

                    if (cmp.getName().startsWith(search)
                            || cmp.getName().contains(search)) {

                        //proc.printLine("close match found " + search);
                        //only add unique values to arrays
                        if (!searchNameResArr.contains(cmp.getName())) {
                            searchNameResArr.add(cmp.getName());
                            menuSearchArr.add(cmp);
                            searchResArr.add(searchMenu);
                            searchIconArr.add(cntIconArr.get(j));
                        }
                    }
                }

                //check is atleast 1 match is found
                if (menuSearchArr.size() > 0) {
                    for (int s = 0; s < menuSearchArr.size(); s++) {
                        //proc.printLine("searchNameRes=" + searchNameResArr.get(s));
                        String[] searchMenuArr = proc.splitValue(searchResArr.get(s), "#");
                        cntSearch.add(getMenu(searchMenuArr[0], searchMenuArr[1],
                                searchMenuArr[2], searchIconArr.get(s), form));
                    }
                } else {
                    cntSearch.addAll(new Label(""), FlowLayout.encloseCenter(
                            new SpanLabel("no menu matching " + search + " found")));
                }

                /*form.removeAll();
                form.add(CENTER, cntSearch);
                form.revalidate();*/
                //form.removeAll();
                form.removeComponent(cntSearch);
                form.add(CENTER, cntSearch);
                tfSearch.startEditingAsync();
                //form.revalidate();

            }
            //form.getContentPane().animateLayout(250);
            form.getContentPane().animateLayout(100);
        });

    }
}
