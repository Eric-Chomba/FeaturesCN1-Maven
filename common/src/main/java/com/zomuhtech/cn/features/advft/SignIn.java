/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zomuhtech.cn.features.advft;

import com.codename1.social.FacebookConnect;
import com.codename1.social.Login;
import com.codename1.ui.Button;
import com.codename1.ui.Container;
import com.codename1.ui.Display;
import com.codename1.ui.Form;
import com.codename1.ui.Label;
import com.codename1.ui.layouts.BorderLayout;
import static com.codename1.ui.layouts.BorderLayout.NORTH;
import com.codename1.ui.layouts.GridLayout;
import com.zomuhtech.cn.features.procs.Proc;
import java.util.ArrayList;
import com.codename1.social.GoogleConnect;
import com.codename1.social.LoginCallback;
import com.codename1.ui.FontImage;
import static com.codename1.ui.Image.createImage;
import com.codename1.ui.layouts.BoxLayout;
import java.io.IOException;

/**
 *
 * @author Eric
 */
public class SignIn extends Form {

    Form form, prevForm;
    Proc proc;
    ArrayList<Container> cntArr;

    public SignIn(Form form) {
        this.prevForm = form;
        this.cntArr = new ArrayList<>();
        proc = new Proc();
        Display.getInstance().callSerially(() -> {
            createUI();
        });

    }

    public void createUI() {
        form = proc.getForm("Sign In", prevForm);
        form.setLayout(new BorderLayout());

        Container cntGoogle = new Container(BoxLayout.x(), "cntSocial");
        Container cntFB = new Container(BoxLayout.x(), "cntSocial");
        try {
            Button btnGoogle = new Button("Google", "btnSocial");
            cntArr.add(cntGoogle);
            btnGoogle.addActionListener(e -> {
                changeCntUIID(cntArr, cntGoogle);
                googleSignIn();
                form.revalidate();
            });
            cntGoogle.addAll(new Label(createImage("/google_23_25.png"),
                    "lblSocial"), btnGoogle);
            cntGoogle.setLeadComponent(btnGoogle);

            Button btnFB = new Button("Facebook", "btnSocial");
            cntArr.add(cntFB);
            btnFB.addActionListener(ev -> {
                changeCntUIID(cntArr, cntFB);
                fbSignIn();
                form.revalidate();
            });
            cntFB.addAll(new Label(createImage("/fb_25_25.png"),"lblSocial"),
                    btnFB);
            cntFB.setLeadComponent(btnFB);
        } catch (IOException e) {

        }

        Container cnt = new Container(new GridLayout(1, 3));
        cnt.addAll(cntGoogle, cntFB);

        form.add(NORTH, cnt);
        form.show();
    }

    public void changeCntUIID(ArrayList<Container> cntArr, Container selCnt) {
        for (int k = 0; k < cntArr.size(); k++) {
            if (cntArr.get(k).equals(selCnt)) {
                cntArr.get(k).setUIID("cntSocialSel");
            } else {
                cntArr.get(k).setUIID("cntSocial");
            }
        }
    }

    private void googleSignIn() {
        
        Login gc = GoogleConnect.getInstance();

        //required to work on simulator&android
        gc.setClientId("");
        //gc.setRedirectURI("https://www.codenameone.com");
        gc.setRedirectURI("");
        gc.setClientSecret("");

        //trigger login if not already logged in
        if (!gc.isUserLoggedIn()) {
            gc.doLogin();
        }

        if (gc.isUserLoggedIn()) {
            gc.doLogout();
        }

        //set a login callback listener
        gc.setCallback(new LoginCallback() {

            @Override
            public void loginSuccessful() {
                String token = gc.getAccessToken().getToken();
                proc.printLine("GmailToken:" + token);
                //proc.showToast("GmailToken:" + token, FontImage.MATERIAL_INFO).show();
                proc.showToast("Login via google successful \n" + token, 
                        FontImage.MATERIAL_INFO).show();
            }

            @Override
            public void loginFailed(String error) {
                proc.printLine(error);
                proc.showToast("Login via google failed " + error, 
                        FontImage.MATERIAL_WARNING).show();
            }
        });

//        //trigger login if not already logged in
//        if (!gc.isUserLoggedIn()) {
//            gc.doLogin();
//        } else {
//            //get the token & can now query google api
//            String token = gc.getAccessToken().getToken();
//            proc.printLine("GmailToken:" + token);
//            proc.showToast("GmailToken:" + token, FontImage.MATERIAL_INFO).show();
//        }
    }

    private void fbSignIn() {

        Login fb = FacebookConnect.getInstance();

        fb.setClientId("");
        fb.setRedirectURI("https://www.codenameone.com");
        fb.setClientSecret("");

        //trigger login if not already logged in
        if (!fb.isUserLoggedIn()) {
            fb.doLogin();
        }

        if (fb.isUserLoggedIn()) {
            fb.doLogout();
        }

        //set a LoginCallback listener
        fb.setCallback(new LoginCallback() {

            @Override
            public void loginSuccessful() {
                String token = fb.getAccessToken().getToken();
                proc.printLine("FBToken:" + token);
                proc.showToast("Login via facebook successful \n" + token,
                        FontImage.MATERIAL_INFO).show();
            }

            @Override
            public void loginFailed(String error) {
                proc.printLine(error);
                proc.showToast("Login via facebook failed " + error, 
                        FontImage.MATERIAL_WARNING).show();
            }

        });
    }
}
