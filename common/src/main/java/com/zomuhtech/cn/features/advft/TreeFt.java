/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zomuhtech.cn.features.advft;

import com.zomuhtech.cn.features.procs.Proc;
import com.codename1.components.ToastBar;
import com.codename1.io.FileSystemStorage;
import com.codename1.ui.Button;
import com.codename1.ui.Component;
import static com.codename1.ui.Component.CENTER;
import com.codename1.ui.Container;
import com.codename1.ui.Display;
import com.codename1.ui.FontImage;
import com.codename1.ui.Form;
import com.codename1.ui.layouts.BorderLayout;
import static com.codename1.ui.layouts.BorderLayout.NORTH;
import com.codename1.ui.layouts.GridLayout;
import com.codename1.ui.tree.Tree;
import com.codename1.ui.tree.TreeModel;
import com.codename1.util.StringUtil;
import com.codename1.xml.Element;
import com.codename1.xml.XMLParser;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

/**
 * GUI builder created Form
 *
 * @author Eric
 */
public class TreeFt extends com.codename1.ui.Form {

    Form form, prevForm;
    Proc proc;
    ArrayList<Button> btnArr;

    public TreeFt(Form form) {
        //this(com.codename1.ui.util.Resources.getGlobalResources());
        this.prevForm = form;
        this.btnArr = new ArrayList<>();
        proc = new Proc();
        Display.getInstance().callSerially(() -> {
            createUI();
        });
    }

    private void createUI() {
        form = proc.getForm("Trees", prevForm);
        form.setLayout(new BorderLayout());

        form.add(CENTER, getForm1());

        Button btnTree = new Button("Dynamic Array Tree", "btnNav");
        btnArr.add(btnTree);
        btnTree.addActionListener(e -> {
            proc.changeBtnUIID(btnArr, btnTree);
            form.add(CENTER, getForm1());
            form.revalidate();
        });

        Button btnXmlTree = new Button("XML Tree", "btnNav");
        btnArr.add(btnXmlTree);
        btnXmlTree.addActionListener(ev -> {
            proc.changeBtnUIID(btnArr, btnXmlTree);
            form.add(CENTER, getForm2());
            form.revalidate();

        });

        Button btnFSTree = new Button("FileStorage", "btnNav");
        btnArr.add(btnFSTree);
        btnFSTree.addActionListener(ev -> {
            proc.changeBtnUIID(btnArr, btnFSTree);
            form.add(CENTER, getForm3());
            form.revalidate();

        });

        //selected by default
        proc.changeBtnUIID(btnArr, btnTree);

        Container cnt = new Container(new GridLayout(1, 3));
        cnt.add(btnTree).add(btnXmlTree).add(btnFSTree);
        form.add(NORTH, cnt);

        form.show();
    }

    private Form getForm1() {
        Form form1 = new Form(new BorderLayout());
        form1.getToolbar().setUIID("tbar");

        String resp = "Colors, Letters, Numbers;"
                + "Red, Green, Blue;"
                + "A, B, C;"
                + "1, 2, 3;";

        Tree tree = new Tree(new TreeModelClass(resp)) {
            @Override
            protected Component createNode(Object node, int depth) {
                Component c = super.createNode(node, depth);
                if (!getModel().isLeaf(node) && c instanceof Button) {
                    Button btn = (Button) c;
                    btn.addActionListener(e -> {
                        ToastBar.showMessage("Expanded " + node,
                                FontImage.MATERIAL_INFO);
                    });
                }
                return c;
            }
        };
        form1.add(CENTER, tree);
        tree.addLeafListener(ev -> {
            ToastBar.showMessage("Selected " + ev.getSource(),
                    FontImage.MATERIAL_INFO);
        });

        return form1;
    }

    class TreeModelClass implements TreeModel {

        String[][] arr;
        ArrayList<String[]> arrList;

        /*
        String resp = "Colors, Letters, Numbers;"
                + "Red, Green, Blue;"
                + "A, B, C;"
                + "1, 2, 3;";
       String[][] arr = new String[][]{
            {"Colors", "Letters", "Numbers"},
            {"Red", "Green", "Blue"},
            {"A", "B", "C"},
            {"1", "2", "3"}
        };*/
        TreeModelClass(String resp) {

            String[] respArr = proc.splitValue(resp, ";");

            arrList = new ArrayList<>();

            for (String item : respArr) {
                String[] dataAr = proc.splitValue(item, ",");
                arrList.add(dataAr);
            }

            arr = new String[arrList.size()][];
            arrList.toArray(arr);
        }

        @Override
        public Vector getChildren(Object parent) {

            if (parent == null) {

                Vector v = new Vector();

                for (int j = 0; j < arr[0].length; j++) {
                    v.addElement(arr[0][j]);
                }
                return v;

            }

            Vector v = new Vector();
            for (int j = 0; j < arr[0].length; j++) {
                if (parent == arr[0][j]) {
                    if (arr.length > j + 1 && arr[j + 1] != null) {
                        for (int k = 0; k < arr[j + 1].length; k++) {
                            v.addElement(arr[j + 1][k]);
                        }
                    }
                }
            }

            return v;
        }

        @Override
        public boolean isLeaf(Object node) {
            Vector v = getChildren(node);
            return v == null || v.size() == 0;
        }
    }

    private Form getForm2() {
        Form form2 = new Form(new BorderLayout());
        form2.getToolbar().setUIID("tbar");

        /*InputStream input = Display.getInstance()
                .getResourceAsStream(getClass(), "/treetest.xml");*/
 /*InputStream input = Display.getInstance()
                .getResourceAsStream(getClass(), "/xml_data.xml");*/
        try {
            InputStream input = Display.getInstance()
                    .getResourceAsStream(getClass(), "/treetest.xml");

            /*Result result = Result.fromContent(input, Result.XML);

            String btnId = result.getAsString("Forms/Form1/Container1/Button1");
            proc.printLine("Button " + btnId);*/
            Reader reader = new InputStreamReader(input, "UTF-8");
            Element element = new XMLParser().parse(reader);

            Tree xmlTree = new Tree(new XMLTreeModel(element)) {

                @Override
                protected String childToDisplayLabel(Object child) {

                    if (child instanceof Element) {
                        return ((Element) child).getTagName();
                    }

                    return child.toString();
                }

                @Override
                protected Component createNode(Object node, int depth) {
                    Component c = super.createNode(node, depth);
                    if (!getModel().isLeaf(node) && c instanceof Button) {
                        Button btn = (Button) c;
                        btn.addActionListener(e -> {
                            String[] nodeArr = proc.splitValue("" + node, "<");
                            ToastBar.showMessage("Expanded\n\n" +
                                    StringUtil.replaceAll(nodeArr[0],">","").trim(),
                                    FontImage.MATERIAL_INFO);
                        });
                    }
                    return c;
                }
            };

            form2.add(CENTER, xmlTree);
            xmlTree.addLeafListener(e -> {
                String[] nodeArr = proc.splitValue("" + e.getSource(), "<");
                ToastBar.showMessage("Selected " + 
                        StringUtil.replaceAll(nodeArr[0],">","").trim(),
                        FontImage.MATERIAL_INFO);

            });
        } catch (IOException e) {

        }
        return form2;
    }

    class XMLTreeModel implements TreeModel {

        private Element root;

        public XMLTreeModel(Element e) {
            root = e;
        }

        @Override
        public Vector getChildren(Object parentObj) {

            if (parentObj == null) {
                Vector vector = new Vector();
                vector.addElement(root);
                return vector;
            }

            Vector result = new Vector();
            Element element = (Element) parentObj;

            for (int j = 0; j < element.getNumChildren(); j++) {
                result.addElement(element.getChildAt(j));
            }
            return result;
        }

        @Override
        public boolean isLeaf(Object node) {
            Element e = (Element) node;
            return e.getNumChildren() == 0;
        }
    }

    private Form getForm3() {
        Form form3 = new Form(new BorderLayout());
        form3.getToolbar().setUIID("tbar");

        TreeModel treeModel = new TreeModel() {

            @Override
            public Vector getChildren(Object parent) {
                String[] files;
                if (parent == null) {
                    files = FileSystemStorage.getInstance().getRoots();
                    return new Vector<Object>(Arrays.asList(files));
                } else {
                    try {
                        files = FileSystemStorage.getInstance()
                                .listFiles((String) parent);
                    } catch (IOException e) {
                        proc.printLine(e.getMessage());
                        files = new String[0];
                    }
                }
                String p = (String) parent;
                Vector result = new Vector();
                for (String s : files) {
                    result.add(p + s);
                }
                return result;
            }

            @Override
            public boolean isLeaf(Object node) {
                return !FileSystemStorage.getInstance()
                        .isDirectory((String) node);
            }
        };

        Tree tree = new Tree(treeModel) {

            @Override
            protected String childToDisplayLabel(Object child) {
                String n = (String) child;
                int pos = n.lastIndexOf("/");
                if (pos < 0) {
                    return n;
                }
                return n.substring(pos);
            }

            @Override
            protected Component createNode(Object node, int depth) {
                Component c = super.createNode(node, depth);
                if (!getModel().isLeaf(node) && c instanceof Button) {
                    Button btn = (Button) c;
                    btn.addActionListener(e -> {
                        ToastBar.showInfoMessage("Expanded " + node);
                    });
                }
                return c;
            }
        };

        tree.addLeafListener(e -> {
            ToastBar.showInfoMessage("Selected " + e.getSource());
        });
        form3.add(CENTER, tree);
        return form3;
    }

    public TreeFt(com.codename1.ui.util.Resources resourceObjectInstance) {
        //initGuiBuilderComponents(resourceObjectInstance);
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////-- DON'T EDIT BELOW THIS LINE!!!


// <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initGuiBuilderComponents(com.codename1.ui.util.Resources resourceObjectInstance) {
        setLayout(new com.codename1.ui.layouts.LayeredLayout());
        setInlineStylesTheme(resourceObjectInstance);
                setInlineStylesTheme(resourceObjectInstance);
        setTitle("TreeFt");
        setName("TreeFt");
    }// </editor-fold>

//-- DON'T EDIT ABOVE THIS LINE!!!
}
