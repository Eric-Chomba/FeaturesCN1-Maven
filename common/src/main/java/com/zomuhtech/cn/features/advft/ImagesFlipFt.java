/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zomuhtech.cn.features.advft;

import com.codename1.io.ConnectionRequest;
import com.codename1.io.NetworkManager;
import com.codename1.io.Storage;
import com.codename1.io.Util;
import com.codename1.ui.ButtonGroup;
import com.codename1.ui.Container;
import com.codename1.ui.Display;
import com.codename1.ui.EncodedImage;
import com.codename1.ui.FontImage;
import com.codename1.ui.Form;
import com.codename1.ui.Image;
import com.codename1.ui.Label;
import com.codename1.ui.RadioButton;
import com.codename1.ui.Slider;
import com.codename1.ui.Tabs;
import com.codename1.ui.events.DataChangedListener;
import com.codename1.ui.events.SelectionListener;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.FlowLayout;
import com.codename1.ui.layouts.LayeredLayout;
import com.codename1.ui.list.ListModel;
import com.codename1.ui.plaf.DefaultLookAndFeel;
import com.codename1.ui.plaf.Style;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.EventDispatcher;
import com.codename1.ui.util.UITimer;
import com.codename1.util.StringUtil;
import com.zomuhtech.cn.features.procs.Proc;
import java.io.IOException;
import java.util.ArrayList;

/**
 * GUI builder created Form
 *
 * @author Eric
 */
public class ImagesFlipFt extends com.codename1.ui.Form {

    Form form, prevForm;
    Proc proc;
    int tabIndex;
    ArrayList<RadioButton> radArr;

    public ImagesFlipFt(Form form) {
        //this(com.codename1.ui.util.Resources.getGlobalResources());
        this.prevForm = form;
        this.radArr = new ArrayList<>();
        proc = new Proc();
        Display.getInstance().callSerially(() -> {
            createUI();
        });
    }

    private void createUI() {
        form = proc.getForm("Images View Flip", prevForm);
        //form.setLayout(new BorderLayout());
        form.setLayout(new LayeredLayout());

        ArrayList<String> imgUrls = new ArrayList<>();
        imgUrls.add("https://awoiaf.westeros.org/images/thumb/9/93/"
                + "AGameOfThrones.jpg/300px-AGameOfThrones.jpg");
        imgUrls.add("https://awoiaf.westeros.org/images/thumb/3/39/"
                + "AClashOfKings.jpg/300px-AClashOfKings.jpg");
        imgUrls.add("https://awoiaf.westeros.org/images/thumb/2/24/"
                + "AStormOfSwords.jpg/300px-AStormOfSwords.jpg");
        imgUrls.add("https://awoiaf.westeros.org/images/thumb/a/a3/"
                + "AFeastForCrows.jpg/300px-AFeastForCrows.jpg");

//        ImageList imgList = new ImageList(imgUrls);
//        ImageViewer imgV = new ImageViewer(imgList.getItemAt(0));
//        imgV.setImageList(imgList);
//        form.add(CENTER, imgV);
        //form.show();
        showAds();
    }

    class ImageList implements ListModel<Image> {

        ArrayList<String> imgUrls;

        final EncodedImage placeholder = EncodedImage.createFromImage(
                proc.materialIcon(FontImage.MATERIAL_SYNC).scaled(300, 300),
                false);
        private int selection;

        private final Image[] images;
        private final EventDispatcher listeners = new EventDispatcher();

        public ImageList(ArrayList<String> imgUrls) {
            //this.images = new EncodedImage[imgUrls.length];
            this.imgUrls = imgUrls;
            this.images = new EncodedImage[imgUrls.size()];
        }

        @Override
        public Image getItemAt(final int index) {
            if (images[index] == null) {
                images[index] = placeholder;

                /* Util.downloadUrlToStorageInBackground(
                        imgUrls[index], "list" + index, (e) -> {
                            try {
                                images[index] = EncodedImage.create(Storage
                                        .getInstance()
                                        .createInputStream("list" + index));
                                listeners.fireDataChangeEvent(index,
                                        DataChangedListener.CHANGED);
                            } catch (IOException err) {
                                proc.printLine("Util Dwd " + err.toString());
                            }
                        });*/
                // Util.downloadUrlToStorage(imgUrls.get(index), "list" + index, true)
                Util.downloadUrlToStorageInBackground(
                        imgUrls.get(index), "list" + index, (e) -> {
                    try {
                        images[index] = EncodedImage.create(Storage
                                .getInstance()
                                .createInputStream("list" + index));

                        //new UITimer(() -> {
                        listeners.fireDataChangeEvent(index,
                                DataChangedListener.CHANGED);
                        //}).schedule(3000, false, form);

                    } catch (IOException err) {
                        proc.printLine("Util Dwd " + err.toString());
                    }
                });
            }
            return images[index];
        }

        @Override
        public int getSize() {
            //return imgUrls.length;
            return imgUrls.size();
        }

        @Override
        public int getSelectedIndex() {
            return selection;
        }

        @Override
        public void setSelectedIndex(int index) {
            selection = index;
        }

        @Override
        public void addDataChangedListener(DataChangedListener l) {
            listeners.addListener(l);
        }

        @Override
        public void removeDataChangedListener(DataChangedListener l) {
            listeners.removeListener(l);
        }

        @Override
        public void addSelectionListener(SelectionListener l) {
        }

        @Override
        public void removeSelectionListener(SelectionListener l) {
        }

        @Override
        public void addItem(Image item) {
        }

        @Override
        public void removeItem(int index) {
        }

    }

    private void showAds() {
        Tabs tabs = new Tabs();
        tabs.getAllStyles().setBgTransparency(0);
        tabs.hideTabs();

        Style s = UIManager.getInstance().getComponentStyle("radBtn");//btn radOnBoard
        FontImage radEmptyImg = FontImage.createMaterial(
                FontImage.MATERIAL_RADIO_BUTTON_UNCHECKED, s);
        FontImage radFullImg = FontImage.createMaterial(
                FontImage.MATERIAL_RADIO_BUTTON_CHECKED, s);
        ((DefaultLookAndFeel) UIManager.getInstance().getLookAndFeel())
                .setRadioButtonImages(radFullImg, radEmptyImg, radFullImg,
                        radEmptyImg);

        ArrayList<String> adsArr = new ArrayList<>();
//        adsArr.add("Codename One:http#//127.0.0.1/AdvanceFt/ads/android.png:"
//                + "Using one codebase build apps for Android phones");
//        adsArr.add("Codename One:http#//127.0.0.1/AdvanceFt/ads/ios.jpeg:"
//                + "Using one codebase build apps for iPhones");
//        adsArr.add("Codename One:http#//127.0.0.1/AdvanceFt/ads/windows.jpeg:"
//                + "Using one codebase build apps for Windows phones");

        adsArr.add("A Game of Thrones:https#//awoiaf.westeros.org/images/thumb/9/93/"
                + "AGameOfThrones.jpg/300px-AGameOfThrones.jpg:Series");
        adsArr.add("A Clash Of Kings:https#//awoiaf.westeros.org/images/thumb/3/39/"
                + "AClashOfKings.jpg/300px-AClashOfKings.jpg:Series");
        adsArr.add("A Storm Of Swords:https#//awoiaf.westeros.org/images/thumb/2/24/"
                + "AStormOfSwords.jpg/300px-AStormOfSwords.jpg:Series");
        adsArr.add("A Feast For Crows:https#//awoiaf.westeros.org/images/thumb/a/a3/"
                + "AFeastForCrows.jpg/300px-AFeastForCrows.jpg:Series");
        RadioButton rad;
        ButtonGroup bg = new ButtonGroup();
        Container tabsFlow = new Container();

        for (int j = 0; j < adsArr.size(); j++) {

            String[] adArr = proc.splitValue(adsArr.get(j), ":");
            Container cnt1 = new Container(BoxLayout.y());

//            cnt1.add(FlowLayout.encloseCenter(new Label(adArr[0],
//                    "lblAdsTitle")));
            Slider sliderProgress = proc.createDownloadSlider();
            sliderProgress.setInfinite(true);
            cnt1.add(sliderProgress);
            Label lblImg = new Label();

//            cnt1.add(FlowLayout.encloseCenter(lblImg))
//                    .add(FlowLayout.encloseCenter(
//                            new SpanLabel(adArr[2], "lblAdsMsg")));
            
            //tabs.addTab("Tab", FlowLayout.encloseCenterMiddle(cnt1));
            tabs.addTab("Tab", cnt1);

            rad = new RadioButton("");
            radArr.add(rad);
            bg.add(rad);
            tabsFlow.add(FlowLayout.encloseCenter(rad));

            Display.getInstance().callSerially(() -> {
                downloadImg(cnt1, lblImg, StringUtil.replaceAll(adArr[1], "#", ":"),
                        sliderProgress);
            });

        }

        radArr.get(0).setSelected(true);
        form.add(tabs);
        form.add(BorderLayout.south(FlowLayout.encloseCenterMiddle(tabsFlow)));

        tabs.addSelectionListener((i1, i2) -> {

            for (int j = 0; j < radArr.size(); j++) {
                if (i2 == j) {
                    if (!radArr.get(j).isSelected()) {
                        radArr.get(j).setSelected(true);
                        tabIndex = j;
                    }
                }
            }
        });

        bg.addActionListener(e -> {
            tabs.setSelectedIndex(bg.getSelectedIndex(), true);
            tabIndex = bg.getSelectedIndex();
        });

        updateTab(tabs);

        form.show();
    }

    private void downloadImg(Container cnt, Label lblImg, String url, Slider sliderProgress) {

        ConnectionRequest req = new ConnectionRequest(url, false);
        //SliderBridge.bindProgress(req, sliderProgress);
        NetworkManager.getInstance().addToQueueAndWait(req);
        if (req.getResponseCode() == 200) {
            // proc.printLine("Resp=" + new String(req.getResponseData()));
            lblImg.setIcon(EncodedImage.create(req.getResponseData()));
            cnt.getStyle().setBgImage(EncodedImage.create(req.getResponseData()));
            sliderProgress.setVisible(false);
            form.revalidate();

        } else {
            proc.showToast("Image download failed", FontImage.MATERIAL_ERROR).show();
        }

    }

    private void updateTab(Tabs tabs) {

        Display.getInstance().callSerially(() -> {

            new UITimer(() -> {

                if (tabIndex == radArr.size() - 1) {
                    tabIndex = 0;
                    proc.printLine("tabIndex " + tabIndex);
                    tabs.setSelectedIndex(tabIndex);
                } else {
                    tabIndex = tabIndex + 1;
                    proc.printLine("tabIndex " + tabIndex);
                    tabs.setSelectedIndex(tabIndex);
                }

                updateTab(tabs);
            }).schedule(3000, false, form);
        });
    }

    public ImagesFlipFt(com.codename1.ui.util.Resources resourceObjectInstance) {
        //initGuiBuilderComponents(resourceObjectInstance);
    }

//-- DON'T EDIT BELOW THIS LINE!!!
    private void initGuiBuilderComponents(com.codename1.ui.util.Resources resourceObjectInstance) {
    }
//-- DON'T EDIT ABOVE THIS LINE!!!
}
