/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zomuhtech.cn.features.advft;

import com.codename1.components.ToastBar;
import com.codename1.ui.Button;
import com.codename1.ui.CheckBox;
import com.codename1.ui.Container;
import com.codename1.ui.Display;
import com.codename1.ui.EncodedImage;
import com.codename1.ui.Form;
import com.codename1.ui.Image;
import com.codename1.ui.Label;
import com.codename1.ui.List;
import com.codename1.ui.URLImage;
import com.codename1.ui.layouts.BorderLayout;
import static com.codename1.ui.layouts.BorderLayout.NORTH;
import static com.codename1.ui.layouts.BorderLayout.WEST;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.FlowLayout;
import com.codename1.ui.list.DefaultListModel;
import com.codename1.ui.list.GenericListCellRenderer;
import com.codename1.ui.list.MultiList;
import com.zomuhtech.cn.features.procs.Proc;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * GUI builder created Form
 *
 * @author Eric
 */
public class ListFt extends Form {

    Form form, prevForm;
    Proc proc;
    ArrayList<Button> btnArr;

    public ListFt(Form form) {
        //this(com.codename1.ui.util.Resources.getGlobalResources());
        this.prevForm = form;
        this.btnArr = new ArrayList<>();
        proc = new Proc();
        Display.getInstance().callSerially(() -> {

            createUI();

        });

    }

    private void createUI() {

        form = proc.getForm("Lists", prevForm);
        form.setLayout(new BorderLayout());

        //form.setTitleComponent(new Label("MultiList"));
        //form.add(CENTER, createMultiList());
        Button btnMultiList = new Button("MultiList", "btnNav");
        btnArr.add(btnMultiList);

        btnMultiList.addActionListener(e -> {
            proc.changeBtnUIID(btnArr, btnMultiList);
            form.setTitleComponent(new Label("MultiList"));
            form.add(CENTER, createMultiList());
            form.revalidate();
        });

        Button btnGenericList = new Button("GenericListCellRenderer(GLCR)",
                "btnNav");
        btnArr.add(btnGenericList);
        btnGenericList.addActionListener(e -> {
            proc.changeBtnUIID(btnArr, btnGenericList);
            form.setTitleComponent(new Label("GenericListCellRenderer"));
            form.add(CENTER, createGenericList());
            form.revalidate();
        });

        Container cnt = new Container(BoxLayout.x());
        cnt.addAll(btnMultiList, btnGenericList);
        form.add(NORTH, cnt);
        form.show();
    }

    private MultiList createMultiList() {

        int mm = Display.getInstance().convertToPixels(3);
        EncodedImage placeholder = EncodedImage.createFromImage(
                Image.createImage(mm * 3, mm * 4, 0), false);
        Image icon1 = URLImage.createToStorage(placeholder, "icon1",
                "https://www.georgerrmartin.com/wp-content/uploads/2013/03/GOTMTI2.jpg");
        Image icon2 = URLImage.createToStorage(placeholder, "icon2",
                "https://www.georgerrmartin.com/wp-content/uploads/2012/08/clashofkings.jpg");
        Image icon3 = URLImage.createToStorage(placeholder, "icon3",
                "https://www.georgerrmartin.com/wp-content/uploads/2013/03/stormswordsMTI.jpg");

        ArrayList<Map<String, Object>> data = new ArrayList<>();
        data.add(createListEntry("A Game of Thrones", "1996", icon1));
        data.add(createListEntry("A Clash of Kings", "1998", icon2));
        data.add(createListEntry("A Storm of Swords", "2000", icon3));
        data.add(createListEntry("A Wind of Winter", "2016(please, "
                + "please, please)", placeholder));

        DefaultListModel<Map<String, Object>> model = new DefaultListModel<>(data);

        /*(model.addSelectionListener((int oldVal, int newVal) -> {

            System.out.println(model.getItemAt(oldVal) + "\n"
                    + model.getItemAt(newVal));
        });*/
        MultiList ml = new MultiList(model);
        ml.addSelectionListener((int oldVal, int newVal) -> {

            ToastBar.showInfoMessage(model.getItemAt(newVal).get("Line1") + "\n"
                    + model.getItemAt(newVal).get("Line2"));
        });

        return ml;
    }

    private Map<String, Object> createListEntry(String name, String date,
            Image icon) {

        Map<String, Object> entry = new HashMap<>();
        entry.put("Line1", name);
        entry.put("Line2", date);
        entry.put("icon", icon);
        return entry;
    }

    private List createGenericList() {
        List list = new List(createGLCRModelData());
        list.setRenderer(new GenericListCellRenderer(getGenericRendererCnt(),
                getGenericRendererCnt()));

        list.addSelectionListener((int oldVal, int newVal) -> {

            //System.out.println(list.getSelectedItem());
            //{Selected=true, Surname=Kimotho, Name=Eric}
            ToastBar.showInfoMessage(list.getSelectedIndex() + "\n"
                    + list.getSelectedItem());
        });
        return list;
    }

    private Container getGenericRendererCnt() {
        Label lblName = new Label("", "menuLbl");
        lblName.setFocusable(true);
        lblName.setName("Name");
        Label lblSurname = new Label("", "menuLbl");
        lblSurname.setFocusable(true);
        lblSurname.setName("Surname");
        Container cntName = new Container(BoxLayout.y());
        cntName.add(FlowLayout.encloseLeftMiddle(lblName))
                .add(FlowLayout.encloseLeftMiddle(lblSurname));
        CheckBox chkSel = new CheckBox();
        chkSel.setFocusable(true);
        chkSel.setName("Selected");

        Container cnt = BorderLayout.center(cntName).add(WEST, chkSel);
        cnt.setUIID("ListRenderer");
        return cnt;
    }

    private Object[] createGLCRModelData() {
        Map<String, Object>[] data = new HashMap[3];
        data[0] = new HashMap<>();
        data[0].put("Name", "Eric");
        data[0].put("Surname", "Kimotho");
        //data[0].put("Selected", Boolean.TRUE);
        data[1] = new HashMap<>();
        data[1].put("Name", "Joy");
        data[1].put("Surname", "Ann");
        // data[1].put("Selected", Boolean.TRUE);
        data[2] = new HashMap<>();
        data[2].put("Name", "Peter");
        data[2].put("Surname", "Ken");

        return data;
    }

    public ListFt(com.codename1.ui.util.Resources resourceObjectInstance) {
        //initGuiBuilderComponents(resourceObjectInstance);
    }

//////////////////////////////////////////////////////////////-- DON'T EDIT BELOW THIS LINE!!!


// <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initGuiBuilderComponents(com.codename1.ui.util.Resources resourceObjectInstance) {
        setLayout(new com.codename1.ui.layouts.FlowLayout());
        setInlineStylesTheme(resourceObjectInstance);
                setInlineStylesTheme(resourceObjectInstance);
        setTitle("ListFt");
        setName("ListFt");
    }// </editor-fold>

//-- DON'T EDIT ABOVE THIS LINE!!!
}
