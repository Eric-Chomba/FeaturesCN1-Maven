/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zomuhtech.cn.features.advft;

import com.zomuhtech.cn.features.procs.Proc;
import com.codename1.ui.Component;
import com.codename1.ui.Form;
import com.codename1.ui.Display;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.spinner.Picker;
import com.codename1.ui.table.DefaultTableModel;
import com.codename1.ui.table.Table;
import com.codename1.ui.table.TableLayout;
import com.codename1.ui.table.TableModel;
import java.util.ArrayList;

/**
 * GUI builder created Form
 *
 * @author Eric
 */
public class TableFt extends Form {

    Form form, prevForm;
    Proc proc;

    public TableFt(Form form) {
        //this(com.codename1.ui.util.Resources.getGlobalResources());
        this.prevForm = form;
        proc = new Proc();
        Display.getInstance().callSerially(() -> {
            createUI();
        });
    }

    private void createUI() {

        form=proc.getForm("Sales Table", prevForm);
        form.setLayout(new BorderLayout());

        //String[] colArr = new String[]{"Col 1", "Col 2", "Col 3"};
        /*Object[][] rowArr = new Object[][]{
            {"Row 1", "Row A", "Row W"},
            {"Row 2", "Row B can now stretch can now stretch can now "
                + "stretch", "Row X"},
            {"Row 3", "Row C", "Row Y"},
            {"Row 4", "Row D", "Row Z"},};*/
 /*String resp = "Sales, Col 1, Col 2, Col 3;"
                + "Date, Row 1, Row A, Row W;"
                + "na, Row 2, Row B can now stretch can now stretch can now stretch, Row X;"
                + "na, Row 3, Row C, Row Y;"
                + "na, Row 4, Row D, Row Z";*/
        String resp = "Sales/Date, Credit, Debit, Profit;"
                + "1/1/19, 250, 50, 200;"
                + "2/1/19, 300, 30, 270;"
                + "3/1/19, 400, 100, 300;"
                + "4/1/19, 600, 10, 590";

        String[] respArr = proc.splitValue(resp, ";");

        String[] colArr = null;
        ArrayList<String> dataArr = new ArrayList<>();

        for (int m = 0; m < respArr.length; m++) {

            colArr = proc.splitValue(respArr[0], ",");

            if (m > 0) {
                dataArr.add(respArr[m]);
            }
        }

        ArrayList<String[]> arrList = new ArrayList<>();

        for (int k = 0; k < dataArr.size(); k++) {

            String[] dataAr = proc.splitValue(dataArr.get(k), ",");
            arrList.add(dataAr);
        }

        Object[][] rowArr = new Object[arrList.size()][];

        arrList.toArray(rowArr);

        TableModel model = new DefaultTableModel(colArr, rowArr) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return col != 0;
            }
        };

        Table table = new Table(model) {
            //Bind to picker
            @Override
            protected Component createCell(Object value, int row, int column,
                    boolean editable) {

                Component cell;
                if (row == 1 && column == 1) {
                    Picker picker = new Picker();
                    picker.setType(Display.PICKER_TYPE_STRINGS);
                    picker.setStrings("Row B can now stretch",
                            "This is a good value", "So is This",
                            "Better than text field");
                    picker.setSelectedString((String) value);
                    picker.setUIID("TableCell");
                    picker.addActionListener((e) -> {
                        getModel().setValueAt(row, column,
                                picker.getSelectedString());
                    });
                    cell = picker;
                } else {
                    cell = super.createCell(value, row, column, editable);
                }

                //pinstripe effect
                if (row > -1 && row % 2 == 0) {
                    cell.getAllStyles().setBgColor(0xeeeeee);
                    cell.getAllStyles().setBgTransparency(255);
                }
                return cell;
            }

            //Bind table to TextArea
            /*@Override
            protected Component createCell(Object value, int row, int column,
                    boolean editable) {
                TextArea txtArea = new TextArea((String) value);
                txtArea.setUIID("TableCell");
                return txtArea;

            }*/
            //Add table constraints
            @Override
            protected TableLayout.Constraint createCellConstraint(
                    Object value, int row, int column) {
                TableLayout.Constraint con = super.createCellConstraint(
                        value, row, column);

                if (row == 1 && column == 1) {
                    con.setHorizontalSpan(2);
                }
                con.setWidthPercentage(25);
                return con;
            }

        };

        table.setSortSupported(true);
        form.add(CENTER, table);

        form.show();
    }

   
    public TableFt(com.codename1.ui.util.Resources resourceObjectInstance) {
        //initGuiBuilderComponents(resourceObjectInstance);
    }

//////////////////////////////////////////////////////////////////////-- DON'T EDIT BELOW THIS LINE!!!


// <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initGuiBuilderComponents(com.codename1.ui.util.Resources resourceObjectInstance) {
        setLayout(new com.codename1.ui.layouts.FlowLayout());
        setInlineStylesTheme(resourceObjectInstance);
                setInlineStylesTheme(resourceObjectInstance);
        setTitle("TableFt");
        setName("TableFt");
    }// </editor-fold>

//-- DON'T EDIT ABOVE THIS LINE!!!
}
