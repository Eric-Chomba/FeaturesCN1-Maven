/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zomuhtech.cn.features.advft;

import ca.weblite.codename1.json.JSONArray;
import ca.weblite.codename1.json.JSONException;
import ca.weblite.codename1.json.JSONObject;
import com.codename1.components.InfiniteScrollAdapter;
import com.codename1.components.MultiButton;
import com.codename1.components.ToastBar;
import com.codename1.io.ConnectionRequest;
import com.codename1.io.Log;
import com.codename1.io.NetworkManager;
import com.codename1.ui.Button;
import com.codename1.ui.Component;
import static com.codename1.ui.Component.CENTER;
import com.codename1.ui.Container;
import com.codename1.ui.Display;
import com.codename1.ui.Form;
import com.codename1.ui.InfiniteContainer;
import com.codename1.ui.layouts.BorderLayout;
import static com.codename1.ui.layouts.BorderLayout.NORTH;
import com.codename1.ui.layouts.FlowLayout;
import com.codename1.ui.layouts.GridLayout;
import com.zomuhtech.cn.features.procs.Proc;
import java.util.ArrayList;

/**
 * GUI builder created Form
 *
 * @author Eric
 */
public class InfiniteScrollAdapterFt extends Form {

    Form form, prevForm;
    int pageNo;
    Proc proc;
    // Map<String, Object> response;
    String response;
    ArrayList<Button> btnArr;

    public InfiniteScrollAdapterFt(Form form) {
        //this(com.codename1.ui.util.Resources.getGlobalResources());
        this.prevForm = form;
        this.btnArr = new ArrayList<>();
        this.pageNo = 1;
        proc = new Proc();
        Display.getInstance().callSerially(() -> {

            createUI();
            //createUI2();
        });
    }

    //InfiniteScrollAdapter
    private void createUI() {

        form = proc.getForm("Infinite(Container)ScrollAdapter", prevForm);
        form.setLayout(new BorderLayout());

        form.add(CENTER, getForm1());

        Button btnISA = new Button("InfiniteScrollAdapter", "btnNav");
        btnArr.add(btnISA);

        btnISA.addActionListener(ev -> {
            proc.changeBtnUIID(btnArr, btnISA);
            form.add(CENTER, getForm1());
            form.revalidate();
        });

        Button btnIC = new Button("InfiniteContainer", "btnNav");
        btnArr.add(btnIC);
        btnIC.addActionListener(ev -> {
            proc.changeBtnUIID(btnArr, btnIC);
            form.add(CENTER, getForm2());
            form.revalidate();

        });

        //selected by default
        proc.changeBtnUIID(btnArr, btnISA);

        Container cnt = new Container(new GridLayout(1, 2));
        cnt.addAll(FlowLayout.encloseLeftMiddle(btnISA), FlowLayout.encloseLeftMiddle(btnIC));

        form.add(NORTH, cnt);
        form.show();
    }

    //Infinite Scroll Adapter
    private Form getForm1() {

        Form form1 = proc.getInputForm();

        /*Style s = UIManager.getInstance().getComponentStyle("MultiLine1");
        FontImage fontImg = FontImage.createMaterial(FontImage.MATERIAL_PORTRAIT,
                s);
        EncodedImage placeholder = EncodedImage.createFromImage(
                fontImg.scaled(fontImg.getWidth() * 3, fontImg.getHeight() * 3),
                false);*/
        InfiniteScrollAdapter.createInfiniteScroll(form1.getContentPane(), ()
                -> { //Accept runnable invoked when scrolling end is reached

            String resp = fetchPropertyData(); //blocks until all data is downloaded
            //String resp = proc.getResp().trim();
            if (resp != null) {
                try {

                    //proc.printLine("Resp "+resp);
                    JSONArray respArr = new JSONArray(resp);

                    // MultiButton[] mBtn = new MultiButton[respArr.length()];
                    Container[] cntArr = new Container[respArr.length()];

                    //for (int j = 0; j < mBtn.length; j++) {
                    for (int j = 0; j < respArr.length(); j++) {
                        /*Map<String, Object> currentList = data.get(j);
                    
                    //if no more data to fetch
                    if (currentList == null) {

                        InfiniteScrollAdapter.addMoreComponents(form.getContentPane(),
                                new Component[0], false);
                        return;
                    }*/
                        JSONObject posts = respArr.getJSONObject(j);

                        /*mBtn[j] = new MultiButton(posts.getString("id")
                            + " " + posts.getString("name"));
                    mBtn[j].setTextLine2(posts.getString("email"));
                    mBtn[j].setTextLine3(posts.getString("body"));*/
                        //mBtn[j].setLayout(BoxLayout.y());
                        MultiButton btn = new MultiButton(posts.getString("id")
                                + " " + posts.getString("name"));
                        btn.setTextLine2(posts.getString("email"));
                        btn.setTextLine3(posts.getString("body"));
                        cntArr[j] = new Container();
                        cntArr[j].add(btn);
                        cntArr[j].setLeadComponent(btn);

                        btn.addActionListener(e -> {
                            ToastBar.showInfoMessage(btn.getTextLine1()
                                    + "\n" + btn.getTextLine2());
                        });


                        /*mBtn[j].setIcon(URLImage
                            .createToStorage(placeholder, guid, thumbUrl));*/
                    }
                    //Add actual components to the end of form
                    InfiniteScrollAdapter.addMoreComponents(form1.getContentPane(),
                            cntArr,
                            true); //true - indicate data isnt prefilled & hence method 
                    //invoke immediately for is shown
                } catch (JSONException err) {
                    proc.printLine("Err " + err);
                }
            }
        }, true);

        return form1;

    }

    //Infinite Container
    private Form getForm2() {
        //private void getForm2() {
        Form form2 = new Form(new BorderLayout());
        form2.getToolbar().hideToolbar();

        /*Style s = UIManager.getInstance().getComponentStyle("MultiLine1");
        FontImage fontImg = FontImage.createMaterial(FontImage.MATERIAL_PORTRAIT, s);
        EncodedImage placeholder = EncodedImage.createFromImage(
                fontImg.scaled(fontImg.getWidth() * 3, fontImg.getHeight() * 3), false);
         */
        InfiniteContainer infiniteCnt = new InfiniteContainer(100) {

            @Override
            public Component[] fetchComponents(int index, int amount) {

                proc.printLine("Index " + index + "\nAmount " + amount);

                String resp = fetchPropertyData(); //blocks until all data is downloaded
                //String resp = proc.getResp().trim();
                Container[] cntArr = null;

                if (resp != null) {

                    try {

                        JSONArray respArr = new JSONArray(resp);
                        //MultiButton[] mBtn = new MultiButton[respArr.length()];
                        cntArr = new Container[respArr.length()];

                        for (int j = 0; j < respArr.length(); j++) {

                            JSONObject posts = respArr.getJSONObject(j);

                            /* mBtn[j] = new MultiButton(posts.getString("id")
                                + " " + posts.getString("name"));
                        mBtn[j].setTextLine2(posts.getString("email"));
                        mBtn[j].setTextLine3(posts.getString("body"));*/
                            MultiButton btn = new MultiButton(posts.getString("id")
                                    + " " + posts.getString("name"));
                            btn.setTextLine2(posts.getString("email"));
                            btn.setTextLine3(posts.getString("body"));
                            cntArr[j] = new Container();
                            cntArr[j].add(btn);
                            cntArr[j].setLeadComponent(btn);

                            btn.addActionListener(e -> {
                                ToastBar.showInfoMessage(btn.getTextLine1()
                                        + "\n" + btn.getTextLine2());
                            });

                        }

                    } catch (JSONException err) {
                    }
                }

                //return mBtn;
                return cntArr;
            }
        };

        /*infiniteCnt.addPullToRefresh(() -> {

            //refresh content
            ToastBar.showInfoMessage("Reloading");
        });*/
        form2.add(CENTER, infiniteCnt);
        return form2;

    }

    //List<Map<String, Object>> fetchPropertyData(String text) {
    String fetchPropertyData() {
        try {

            ConnectionRequest req = new ConnectionRequest();
            req.setPost(false);
            //req.setUrl("https://api.nestoria.co.uk/api");
            req.setUrl("https://jsonplaceholder.typicode.com/comments");
            /*req.addArgument("pretty", "0");
            req.addArgument("action", "search_listings");
            req.addArgument("encoding", "json");
            req.addArgument("listing_type", "buy");
            req.addArgument("page", "" + pageNo);
            pageNo++;
            req.addArgument("country", "uk");
            req.addArgument("place_name", text);*/
            req.setFailSilently(true);

            NetworkManager.getInstance().addToQueueAndWait(req);

            // System.out.println("RespCode:" + req.getResponseCode());
            switch (req.getResponseCode()) {

                case 0:
                    ToastBar.showInfoMessage("Connection failed");
                    break;

                case 404:
                    ToastBar.showInfoMessage("Services Unavailable");
                    break;

                case 200:
                    byte[] result = req.getResponseData();
                    response = new String(result).trim();
                    //proc.printLine("Resp = " + response);

                    /*Map<String, Object> result2 = new JSONParser().parseJSON(
                            new InputStreamReader(new ByteArrayInputStream(
                                    req.getResponseData()), "UTF-8"));*/
                    //Log.p("Result = "+result);
                    //response = (Map<String, Object>) result2.get("root");
                    //Log.p("Resp = "+response);
                    break;
            }

            //return (List<Map<String, Object>>) response.get("listings");
            return response;
        } catch (Exception e) {
            Log.e(e);
            return null;
        }
    }

    public InfiniteScrollAdapterFt(com.codename1.ui.util.Resources resourceObjectInstance) {
        //initGuiBuilderComponents(resourceObjectInstance);
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////-- DON'T EDIT BELOW THIS LINE!!!


// <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initGuiBuilderComponents(com.codename1.ui.util.Resources resourceObjectInstance) {
        setLayout(new com.codename1.ui.layouts.FlowLayout());
        setInlineStylesTheme(resourceObjectInstance);
                setInlineStylesTheme(resourceObjectInstance);
        setTitle("InfiniteScrollAdapterFt");
        setName("InfiniteScrollAdapterFt");
    }// </editor-fold>

//-- DON'T EDIT ABOVE THIS LINE!!!
}
