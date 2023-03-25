/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zomuhtech.cn.features.advft;

import com.codename1.components.ToastBar;
import com.codename1.ui.BrowserComponent;
import com.codename1.ui.Button;
import static com.codename1.ui.Component.CENTER;
import com.codename1.ui.Container;
import com.codename1.ui.Display;
import com.codename1.ui.Form;
import com.codename1.ui.layouts.BorderLayout;
import static com.codename1.ui.layouts.BorderLayout.NORTH;
import com.codename1.ui.layouts.GridLayout;
import com.zomuhtech.cn.features.procs.Proc;
import java.util.ArrayList;

/**
 *
 * @author Eric
 */
public class JSFt extends Form {

    Form form, prevForm;
    Proc proc;
    ArrayList<Button> btnArr;

    public JSFt(Form form) {
        this.prevForm = form;
        this.btnArr = new ArrayList<>();
        proc = new Proc();
        Display.getInstance().callSerially(() -> {
            createUI();
        });
    }

    private void createUI() {
        form = proc.getForm("Javascript Interaction", prevForm);
        form.setLayout(new BorderLayout());

        Container cnt = new Container(new GridLayout(1, 3));
        Button btnNavCallback = new Button("Nav Callback", "btnNav");
        btnArr.add(btnNavCallback);
        btnNavCallback.addActionListener(e -> {
            proc.changeBtnUIID(btnArr, btnNavCallback);
            form.add(CENTER, getNavCallback());
            form.revalidate();
        });
        Button btnAsync = new Button("Async Callback", "btnNav");
        btnArr.add(btnAsync);
        btnAsync.addActionListener(e -> {
            proc.changeBtnUIID(btnArr, btnAsync);
            BrowserComponent browser = new BrowserComponent();
            browser.execute("callback.onSuccess(4+8)",
                    res -> ToastBar.showInfoMessage("Result " + res.getInt())
            );

            /*JSRef res = browser.executeAndWait("callback.onSuccess(4+8)");
            Log.p("Result " + res.getInt());*/
            form.add(CENTER, browser);
            //form.add(CENTER, getAsyncCallback());
            form.revalidate();
        });
        Button btnPassVar = new Button("Pass Variables", "btnNav");
        btnArr.add(btnPassVar);
        btnPassVar.addActionListener(e -> {
            proc.changeBtnUIID(btnArr, btnPassVar);
            BrowserComponent browser = new BrowserComponent();
            browser.execute(
                    "jQuery('#bio').text(${0}; jQuery('#age').text(${1})",
                    new Object[]{
                        "A multi-line\n string with \"quotes\"",
                        27
                    }
            );

        });
        //changeUIID(btnNavCallback);
        cnt.add(btnNavCallback).add(btnAsync).add(btnPassVar);
        form.add(NORTH, cnt);

        form.show();

    }

    private BrowserComponent getNavCallback() {
        BrowserComponent browser = new BrowserComponent();
        browser.setPage("<html lang=\"en\">\n"
                + "           <head>\n"
                + "               <meta charset=\"utf-8\">\n"
                + "               <script>\n"
                + "                   function fnc(message) {\n"
                + "                document.write(message);\n"
                + "                      };\n"
                + "                </script>\n"
                + "            </head>\n"
                + "            <body>\n"
                + "                  <p><a href=\"http://click\">Demo</a></p>\n"
                + "            </body>\n"
                + "      </html>", null);
        browser.addBrowserNavigationCallback((url) -> {
            if (url.startsWith("http://click")) {
                Display.getInstance().callSerially(() -> browser
                        .execute("fnc('<p>You Clicked!</p>')"));
                return false;
            }
            return true;
        });

        return browser;
    }

    private BrowserComponent getAsyncCallback() {
        BrowserComponent browser = new BrowserComponent();
        browser.execute("callback.onSuccess(4+8)",
                res -> ToastBar.showInfoMessage("Result " + res.getInt())
        );
        return browser;
    }
}
