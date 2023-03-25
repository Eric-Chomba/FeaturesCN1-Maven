/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zomuhtech.cn.features.advft;

import com.codename1.components.MultiButton;
import com.codename1.ui.Button;
import static com.codename1.ui.Component.CENTER;
import com.codename1.ui.Container;
import com.codename1.ui.Display;
import com.codename1.ui.Form;
import com.codename1.ui.layouts.BorderLayout;
import static com.codename1.ui.layouts.BorderLayout.NORTH;
import com.zomuhtech.cn.features.procs.Proc;
import com.codename1.io.CSVParser;
import com.codename1.io.JSONParser;
import com.codename1.processing.Result;
import com.codename1.ui.Label;
import com.codename1.ui.layouts.GridLayout;
import com.codename1.ui.table.DefaultTableModel;
import com.codename1.ui.table.Table;
import com.codename1.ui.table.TableModel;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Map;

/**
 *
 * @author Eric
 */
public class ParsingFt extends Form {

    Form form, prevForm;
    Proc proc;
    ArrayList<Button> btnArr;

    public ParsingFt(Form form) {
        this.prevForm = form;
        this.btnArr = new ArrayList<>();
        proc = new Proc();
        Display.getInstance().callSerially(() -> {
            createUI();
        });

    }

    public void createUI() {
        form = proc.getForm("Files Parsing", prevForm);
        form.setLayout(new BorderLayout());

        Button btnForm1 = new Button("CSV", "btnNav");
        btnArr.add(btnForm1);

        form.add(CENTER, getForm1());

        btnForm1.addActionListener(e -> {
            proc.changeBtnUIID(btnArr, btnForm1);
            form.add(CENTER, getForm1());
            form.revalidate();
        });

        Button btnForm2 = new Button("JSON", "btnNav");
        btnArr.add(btnForm2);
        btnForm2.addActionListener(e -> {
            proc.changeBtnUIID(btnArr, btnForm2);
            form.add(CENTER, getForm2());
            form.revalidate();
        });

        Button btnForm3 = new Button("XML", "btnNav");
        btnArr.add(btnForm3);
        btnForm3.addActionListener(e -> {
            proc.changeBtnUIID(btnArr, btnForm3);
            form.add(CENTER, getForm3());
            form.revalidate();
        });
        //selected by default
        proc.changeBtnUIID(btnArr, btnForm1);

        Container cnt = new Container(new GridLayout(1, 3));
        cnt.add(btnForm1).add(btnForm2).add(btnForm3);
        form.add(NORTH, cnt);
        form.show();
    }

    private Form getForm1() {
        Form form1 = new Form(new BorderLayout());
        form1.getToolbar().setUIID("tbar");
        CSVParser parser = new CSVParser();

        try {
            //String csvData = "1997,Ford,E350,\"Super, \"\"luxurious\"\" truck\"";
            //Reader reader = new CharArrayReader(csvData.toCharArray());
            Reader reader = new InputStreamReader(Display.getInstance()
                    .getResourceAsStream(getClass(), "/csv_data.csv"), "UTF-8");
            String[][] data = parser.parse(reader);
            String[] columnNames = new String[data[0].length];

            for (int j = 0; j < columnNames.length; j++) {
                columnNames[j] = "Col " + (j + 1);
            }

            TableModel model = new DefaultTableModel(columnNames, data);
            form1.add(CENTER, new Table(model));

        } catch (IOException e) {
            proc.printLine(e.getMessage());
        }
        return form1;
    }

    private Form getForm2() {
        Form form2 = proc.getInputForm();

        JSONParser parser = new JSONParser();
        try {
            Reader reader = new InputStreamReader(Display.getInstance()
                    .getResourceAsStream(getClass(), "/json_data.json"), "UTF-8");

            Map<String, Object> data = parser.parseJSON(reader);

            java.util.List<Map<String, Object>> content
                    = (java.util.List<Map<String, Object>>) data.get("root");

            for (Map<String, Object> object : content) {
                String url = (String) object.get("url");
                String name = (String) object.get("name");
                java.util.List<String> titles
                        = (java.util.List<String>) object.get("titles");

                if (name == null || name.length() == 0) {
                    java.util.List<String> aliases
                            = (java.util.List<String>) object.get("aliases");
                    if (aliases != null && aliases.size() > 0) {
                        name = aliases.get(0);
                    }
                }

                MultiButton btn = new MultiButton(name);
                //btn.setUIID("multiBtn");
                btn.setUIIDLine1("collapsingToolbarTitle");
                btn.setUIIDLine2("lblInput");
                if (titles != null && titles.size() > 0) {
                    btn.setTextLine2(titles.get(0));
                }
                btn.addActionListener(ev -> {
                    Display.getInstance().execute(url);
                });
                form2.add(btn);
            }

        } catch (IOException e) {
            proc.printLine(e.getMessage());
        }

        return form2;
    }

    private Form getForm3() {
        Form form3 = proc.getInputForm();

        try {
            InputStream input = Display.getInstance()
                    .getResourceAsStream(getClass(), "/xml_data.xml");

            Result result = Result.fromContent(input, Result.XML);

            String country = result.getAsString("GeocodeResponse/result/address_component[type='country']/long_name");
            //java.util.List<String> country = result.getAsArray("GeocodeResponse/result/address_component[type='country']/long_name");

            String region = result.getAsString("GeocodeResponse/result/address_component[type='administrative_area_level_1']/long_name");
            String city = result.getAsString("GeocodeResponse/result/address_component[type='locality']/long_name");

            proc.printLine("Country = " + country + " Region = " + region + " City " + city);
            form3.add(new Label("County : " + country, "lblInput"));
            form3.add(new Label("Region : " + region, "lblInput"));
            form3.add(new Label("City : " + city, "lblInput"));

        } catch (IOException e) {

        }
        return form3;
    }
}
