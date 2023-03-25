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
import com.codename1.components.ToastBar;
import com.codename1.db.Cursor;
import com.codename1.db.Database;
import com.codename1.db.Row;
import com.codename1.ui.Button;
import com.codename1.ui.Container;
import com.codename1.ui.Display;
import com.codename1.ui.Form;
import com.codename1.ui.TextArea;
import com.codename1.ui.TextField;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.FlowLayout;
import com.codename1.ui.layouts.GridLayout;
import com.codename1.ui.table.DefaultTableModel;
import com.codename1.ui.table.Table;
import com.codename1.ui.table.TableLayout;
import com.zomuhtech.cn.features.procs.Proc;
import java.io.IOException;
import java.util.ArrayList;

public class SQLiteStore extends Form {

    Form form, prevForm;
    Database db;
    TextField tfName, tfAge, tfHobby, tfSearch;
    Proc proc;

    public SQLiteStore(Form form) {
        //this(com.codename1.ui.util.Resources.getGlobalResources());
        this.prevForm = form;
        proc = new Proc();
        Display.getInstance().callSerially(() -> {
            createUI();
        });
    }

    private void createUI() {
        try {
            db = Display.getInstance().openOrCreate("Sports.db");
            db.execute("CREATE TABLE IF NOT EXISTS 'players' "
                    + "('Name' VARCHAR(250) NOT NULL, "
                    + "'Age' VARCHAR(20) NOT NULL, "
                    + "'Hobby' VARCHAR(20) NOT NULL); ");
        } catch (IOException e) {
        }

        form = proc.getForm("SQLite", prevForm);
        form.setLayout(BoxLayout.y());
        
        // Container cntTf = new Container(BoxLayout.y(), "cntTf");
        Container cntSearch = proc.getInputCnt();
        tfSearch = proc.getInputTf("Search Name", TextArea.ANY);
        cntSearch.add(tfSearch);

        Button btnSearch = proc.getInputBtn("Search");
        btnSearch.addActionListener(e -> {
            searchRec(tfSearch.getText());
        });
        Button btnDel = proc.getInputBtn("Delete");
        btnDel.addActionListener(e -> {
            delRecord(tfSearch.getText());
        });

        TableLayout tl = new TableLayout(1, 3);
        Container cntS = new Container(tl);
        cntS.add(tl.createConstraint().widthPercentage(60), cntSearch)
                .add(tl.createConstraint().widthPercentage(20),
                        FlowLayout.encloseCenterMiddle(btnSearch))
                .add(tl.createConstraint().widthPercentage(20),
                        FlowLayout.encloseCenterMiddle(btnDel));
        /*Container cntS = new Container(new GridLayout(1, 3));
        cntS.add(cntSearch)
                .add(FlowLayout.encloseCenterMiddle(btnSearch))
                .add(FlowLayout.encloseCenterMiddle(btnDel));*/

        Container cntName = proc.getInputCnt();
        tfName = proc.getInputTf("Name", TextArea.ANY);
        cntName.add(tfName);

        Container cntAge = proc.getInputCnt();
        tfAge =proc.getInputTf("Age",TextArea.NUMERIC);
        cntAge.add(tfAge);

        Container cntHobby = proc.getInputCnt();
        tfHobby = proc.getInputTf("Hobby", TextArea.ANY);
        cntHobby.add(tfHobby);

        Button btnAdd = proc.getInputBtn("Add");
        btnAdd.addActionListener(e -> {

            String name = tfName.getText();
            String age = tfAge.getText();
            String hobby = tfHobby.getText();
            addRecord(name, age, hobby);
        });

        Button btnView = new Button("View", "btn");
        btnView.addActionListener(e -> {
            viewRecords();
        });

        Button btnUpd = new Button("Update", "btn");
        btnUpd.addActionListener(e -> {
            updRecord(tfSearch.getText(), tfName.getText(), tfAge.getText(),
                    tfHobby.getText());
        });

        Container cntBtn = new Container(new GridLayout(1, 3));
        cntBtn.add(btnAdd).add(btnView).add(btnUpd);

        form.add(cntS).add(cntName).add(cntAge).add(cntHobby).add(cntBtn);

        //Component.setSameWidth(cntBtn,tfHobby);
        form.show();
    }

    private void clearText() {
        tfName.setText("");
        tfAge.setText("");
        tfHobby.setText("");
        tfSearch.setText("");
    }

    private void showMsg(String msg) {
        ToastBar.showInfoMessage(msg);
    }

    private void addRecord(String name, String age, String hobby) {

        try {

            String[] values = new String[]{name, age, hobby};

            db.execute("INSERT INTO players"
                    + "('Name', 'Age', 'Hobby') VALUES(?, ?, ?)", values);

            clearText();
            showMsg("Added successfully");
            viewRecords();

        } catch (IOException e) {
        }
    }

    private void viewRecords() {

        try {
            Cursor cursor = db.executeQuery("SELECT * FROM players");
            int columns = cursor.getColumnCount();
            //DataEvent.removeAll();
            if (columns > 0) {

                boolean next = cursor.next();
                if (next) {

                    ArrayList<String[]> data = new ArrayList<>();
                    String[] columnNames = new String[columns];

                    for (int j = 0; j < columns; j++) {
                        columnNames[j] = cursor.getColumnName(j);
                    }

                    while (next) {

                        Row currRow = cursor.getRow();
                        String[] currRowArr = new String[columns];

                        for (int k = 0; k < columns; k++) {
                            currRowArr[k] = currRow.getString(k);
                        }

                        data.add(currRowArr);
                        next = cursor.next();
                    }

                    Object[][] arr = new Object[data.size()][];
                    data.toArray(arr);
                    form.add(new Table(new DefaultTableModel(columnNames, arr)));
                } else {
                    form.add("1 Query returned no results");
                }
            } else {
                form.add("2 Query returned no results");
            }

            form.revalidate();
            //Util.cleanup(db);
            //Util.cleanup(cursor);

        } catch (IOException e) {

        }
    }

    private void searchRec(String search) {

        try {
            Cursor cursor = db.executeQuery("SELECT * FROM players "
                    + "WHERE Name = ?", search);
            int columns = cursor.getColumnCount();

            if (columns > 0) {

                boolean next = cursor.next();
                if (next) {

                    ArrayList<String[]> data = new ArrayList<>();
                    String[] columnNames = new String[columns];

                    for (int j = 0; j < columns; j++) {
                        columnNames[j] = cursor.getColumnName(j);
                    }

                    while (next) {

                        Row currRow = cursor.getRow();
                        String[] currRowArr = new String[columns];

                        for (int k = 0; k < columns; k++) {
                            currRowArr[k] = currRow.getString(k);
                        }

                        data.add(currRowArr);
                        next = cursor.next();
                    }

                    Object[][] arr = new Object[data.size()][];
                    data.toArray(arr);
                    form.add(new Table(new DefaultTableModel(columnNames, arr)));
                } else {
                    //form.add("1 '" + search + "' not found");
                    showMsg(search + "' not found");
                }
            } else {
                //form.add("2 '" + search + "' not found");
                showMsg(search + "' not found");
            }

            form.revalidate();
            //Util.cleanup(db);
            //Util.cleanup(cursor);

        } catch (IOException e) {
        }
    }

    private void delRecord(String name) {

        try {

            db.execute("DELETE FROM players WHERE Name = ?", name);
            clearText();
            showMsg("Deleted successfully");
            viewRecords();

        } catch (IOException e) {
        }
    }

    private void updRecord(String search, String name, String age, String hobby) {
        try {

            String[] values = new String[]{name, age, hobby, search};

            db.execute("UPDATE players SET Name = ? , Age = ? , Hobby = ? "
                    + "WHERE Name = ? ", values);

            clearText();
            showMsg("Update successfully");
            viewRecords();

        } catch (IOException e) {
        }
    }

    public SQLiteStore(com.codename1.ui.util.Resources resourceObjectInstance) {
        //initGuiBuilderComponents(resourceObjectInstance);
    }

//////////////////////////////////////////////////////////////-- DON'T EDIT BELOW THIS LINE!!!


// <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initGuiBuilderComponents(com.codename1.ui.util.Resources resourceObjectInstance) {
        setLayout(new com.codename1.ui.layouts.FlowLayout());
        setInlineStylesTheme(resourceObjectInstance);
                setInlineStylesTheme(resourceObjectInstance);
        setTitle("SQLiteStore");
        setName("SQLiteStore");
    }// </editor-fold>

//-- DON'T EDIT ABOVE THIS LINE!!!
}
