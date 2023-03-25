/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zomuhtech.cn.features.advft;

import com.codename1.ui.Button;
import static com.codename1.ui.Component.CENTER;
import com.codename1.ui.Container;
import com.codename1.ui.Display;
import com.codename1.ui.FontImage;
import com.codename1.ui.Form;
import com.codename1.ui.TextArea;
import com.codename1.ui.TextField;
import com.codename1.ui.layouts.BorderLayout;
import static com.codename1.ui.layouts.BorderLayout.NORTH;
import com.codename1.ui.layouts.GridLayout;
import com.codename1.util.StringUtil;
import com.zomuhtech.cn.features.procs.Proc;
import com.zomuhtech.cn.features.procs.RRHandler;
import java.util.ArrayList;

/**
 *
 * @author Eric
 */
public class PaymentFt extends Form implements RRHandler.RespHandler {
    
    Form form, prevForm;
    Proc proc;
    RRHandler rh;
    ArrayList<Button> btnArr;
    String phone, acc, desc, amt;
    
    public PaymentFt(Form form) {
        
        this.prevForm = form;
        this.btnArr = new ArrayList<>();
        proc = new Proc();
        rh = new RRHandler();
        
        Display.getInstance().callSerially(() -> {
            createUI();
        });
        
    }
    
    public void createUI() {
        form = proc.getForm("Mobile Payment", prevForm);
        form.setLayout(new BorderLayout());
        
        form.add(CENTER, getForm1());
        
        Button btnMpesa = new Button("M-Pesa", "btnNav");
        btnArr.add(btnMpesa);
        btnMpesa.addActionListener(e -> {
            changeUIID(btnMpesa);
            form.add(CENTER, getForm1());
            form.revalidate();
        });
        
        Button btnPaypal = new Button("Paypal", "btnNav");
        btnArr.add(btnPaypal);
        btnPaypal.addActionListener(ev -> {
            changeUIID(btnPaypal);
            //form.add(CENTER, getForm2());
            //form.revalidate();
            proc.showToast("Coming soon", FontImage.MATERIAL_INFO).show();
        });

        //selected by default
        changeUIID(btnMpesa);
        
        Container cnt = new Container(new GridLayout(1, 3));
        cnt.addAll(btnMpesa, btnPaypal);
        
        form.add(NORTH, cnt);
        form.show();
    }
    
    private void changeUIID(Button selBtn) {
        for (int k = 0; k < btnArr.size(); k++) {
            if (btnArr.get(k).equals(selBtn)) {
                btnArr.get(k).setUIID("btnNavSel");
            } else {
                btnArr.get(k).setUIID("btnNav");
            }
        }
    }
    
    private Form getForm1() {
        
        Form form1 = proc.getInputForm();
        
        Container cntPhone = proc.getInputCnt();
        TextField tfPhone = proc.getInputTf("M-Pesa Phone", TextArea.PHONENUMBER);
        cntPhone.add(tfPhone);
        
        Container cntAcc = proc.getInputCnt();
        TextField tfAcc = proc.getInputTf("Account No.", TextArea.ANY);
        cntAcc.add(tfAcc);
        
        Container cntDesc = proc.getInputCnt();
        TextField tfDesc = proc.getInputTf("Description", TextArea.ANY);
        cntDesc.add(tfDesc);
        
        Container cntAmt = proc.getInputCnt();
        TextField tfAmt = proc.getInputTf("Amount", TextArea.DECIMAL);
        cntAmt.add(tfAmt);
        
        Button btn = proc.getInputBtn("Pay");
        btn.addActionListener(e -> {
            phone = tfPhone.getText();
            acc = tfAcc.getText();
            desc = tfDesc.getText();
            amt = tfAmt.getText();
            
            if (phone.length() == 0) {
                proc.showToast("Enter M-Pesa Registered Phone",
                        FontImage.MATERIAL_ERROR).show();
            } else if (acc.isEmpty()) {
                proc.showToast("Enter Account Number",
                        FontImage.MATERIAL_ERROR).show();
            } else if (desc.isEmpty()) {
                proc.showToast("Enter Description",
                        FontImage.MATERIAL_ERROR).show();
            } else if (amt.isEmpty()) {
                proc.showToast("Enter Amount",
                        FontImage.MATERIAL_ERROR).show();
            } else {
                
                phone = proc.addCCode(phone);
                amt = proc.formatAmt(amt);
                
                String msg = "Please confirm\n\n" + "Pay KES " + amt 
                        + "\nFrom M-Pesa phone " + phone
                        + "\nFor account " + acc + "\n";
                
                Form confirm = proc.confirmForm(msg, form);
                proc.btnOk.addActionListener(ev -> {
                    mpesaPayment(phone, acc, desc, amt);
                });
                confirm.show();
                
            }
        });
        
        form1.addAll(cntPhone, cntAcc, cntDesc, cntAmt, btn);
        
        return form1;
    }
    
    private void mpesaPayment(String phone, String acc, String desc, String amt) {
        
        String data = "Mpesa"
                + ">Phone>" + phone
                + ">AccNo>" + acc
                + ">Desc>" + desc
                + ">Amt>" + amt;
        
        proc.progressForm("Processing payment, please wait").show();
        rh.processNetTkn(this, phone, data, "mpesa");
    }
    
    @Override
    public void getResp(String resp, String taskTag) {
        
        resp = StringUtil.replaceAll(resp, ">", ">>");
        
        switch (taskTag) {
            case "mpesa":
                String[] respArr = proc.splitValue(resp, ">>");
                switch (respArr[0]) {
                    case "SUCCESS":
                        proc.statusForm(respArr[1], "S", form, prevForm).show();
                        break;
                    case "FAIL":
                        proc.statusForm(respArr[1], "F", form, prevForm).show();
                        break;
                }
                break;
        }
    }
    
}
