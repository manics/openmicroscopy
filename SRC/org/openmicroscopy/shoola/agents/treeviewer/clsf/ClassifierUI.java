/*
 * org.openmicroscopy.shoola.agents.treeviewer.clsf.ClassifierUI
 *
 *------------------------------------------------------------------------------
 *  Copyright (C) 2006 University of Dundee. All rights reserved.
 *
 *
 * 	This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *  
 *  You should have received a copy of the GNU General Public License along
 *  with this program; if not, write to the Free Software Foundation, Inc.,
 *  51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 *------------------------------------------------------------------------------
 */

package org.openmicroscopy.shoola.agents.treeviewer.clsf;


//Java imports
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

//Third-party libraries

//Application-internal dependencies
import org.openmicroscopy.shoola.agents.treeviewer.IconManager;
import org.openmicroscopy.shoola.agents.treeviewer.TreeViewerAgent;
import org.openmicroscopy.shoola.agents.util.ViewerSorter;
import org.openmicroscopy.shoola.env.ui.UserNotifier;
import org.openmicroscopy.shoola.util.ui.TitlePanel;
import org.openmicroscopy.shoola.util.ui.UIUtilities;
import org.openmicroscopy.shoola.util.ui.clsf.TreeCheck;
import org.openmicroscopy.shoola.util.ui.clsf.TreeCheckNode;
import pojos.CategoryData;
import pojos.DataObject;


/** 
 * The {@link Classifier}'s view.
 *
 * @author  Jean-Marie Burel &nbsp;&nbsp;&nbsp;&nbsp;
 * 				<a href="mailto:j.burel@dundee.ac.uk">j.burel@dundee.ac.uk</a>
 * @version 2.2
 * <small>
 * (<b>Internal version:</b> $Revision$ $Date$)
 * </small>
 * @since OME2.2
 */
class ClassifierUI
    extends JPanel
{
    
    /** 
     * The size of the invisible components used to separate buttons
     * horizontally.
     */
    private static final Dimension  H_SPACER_SIZE = new Dimension(5, 10);
    
    /** Text displayed in the title panel. */
    private static final String     ADD_PANEL_TITLE = "Categorise";
    
    /** Text displayed in the text panel. */
    private static final String     ADD_PANEL_TEXT = "Categorise the " +
    										"following image: "; 
    
    /** The default note. */
    private static final String     PANEL_NOTE = 
    	"Expand list to select the categories to add the image to. "+
    	"Double-click on the name to browse the CategoryGroup or the " +
    	"Category.";
    
    /** Text displayed in the note panel. */
    private static final String     ADD_PANEL_NOTE = PANEL_NOTE;
    
    /** Message displayed if the image is unclassified. */
    private static final String     ADD_UNCLASSIFIED_TEXT = "The image " +
            "cannot be categorised. Please first create a category.";
    
    /** Text displayed in the title panel. */
    private static final String     REMOVE_PANEL_TITLE = "Remove From Category";
    
    /** Text displayed in the text panel. */
    private static final String     REMOVE_PANEL_TEXT = "Decategorises the " +
                                                        "following image: ";
    
    /** Text displayed in the note panel. */
    private static final String     REMOVE_PANEL_NOTE = "The image is " +
            "currently classified under the following categories. "+PANEL_NOTE;
    
    /** Message displayed if the image is unclassified. */
    private static final String     REMOVE_UNCLASSIFIED_TEXT = "The selected " +
                                    "image hasn't been categorised.";
    
    
    /** Reference to the Model. */
    private ClassifierModel     model;
    
    /** Reference to the Control. */
    private ClassifierControl   controller;
    
    /** Button to finish the operation. */
    private JButton             finishButton;
    
    /** Button to cancel the object creation. */
    private JButton             cancelButton;
    
    /** The UI component hosting the title. */
    private TitlePanel          titlePanel;
    
    /** Component used to sort the nodes. */
    private ViewerSorter        sorter;
    
    /** The tree hosting the hierarchical structure. */
    private TreeCheck           tree;
    
    /** The panel hosting the classifications. */
    private JPanel              centerPanel;
    
    /** Classifies or declassifies the image depending on the mode. */
    private void finish()
    {
        Set nodes = tree.getSelectedNodes(); 
        if (nodes == null || nodes.size() == 0) { 
            UserNotifier un = TreeViewerAgent.getRegistry().getUserNotifier();
            un.notifyInfo("Categorisation", "No category selected."); 
            return; 
        }
        Set paths = new HashSet(nodes.size()); 
        Iterator i = nodes.iterator();
        Object object; 
        while (i.hasNext()) { 
            object = ((TreeCheckNode) i.next()).getUserObject(); 
            if (object instanceof CategoryData) paths.add(object); 
        } 
        controller.classifyImages(paths);
    }
    
    /** Initializes the GUI components. */
    private void initComponents()
    {
        sorter = new ViewerSorter();
        IconManager im = IconManager.getInstance();
        titlePanel = new TitlePanel(getPanelTitle(), getPanelText(), 
                getPanelNote(), im.getIcon(IconManager.CATEGORY_BIG));
        tree = new TreeCheck("", im.getIcon(IconManager.ROOT)); 
        tree.setRootVisible(false);
        //if (model.getMode() == Classifier.CLASSIFY_MODE)
         //   tree.setSingleSelectionInParent(true);
        //Add Listeners
        tree.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) { onClick(e); }
            public void mouseReleased(MouseEvent e) { onClick(e); }
        });
        finishButton = new JButton("Save");
        finishButton.setEnabled(false);
        finishButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { finish(); }
        });
        cancelButton = new JButton("Close");
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {  
                controller.closeClassifier(true);
            }
        });
        tree.addPropertyChangeListener(TreeCheck.NODE_SELECTED_PROPERTY, 
        								controller);
    }
    
    /** 
     * Browses the selected <code>CategoryGroup</code> or <code>Category</code>.
     * 
     * @param me The mouse event.
     */
    private void onClick(MouseEvent me)
    {
        Point p = me.getPoint();
        int row = tree.getRowForLocation(p.x, p.y);
        if (row != -1) {
            tree.setSelectionRow(row);
            if (me.getClickCount() != 2) return;
            Object node = tree.getLastSelectedPathComponent();
            if (!(node instanceof TreeCheckNode)) return;
            Object object = ((TreeCheckNode) node).getUserObject();
            if (object instanceof DataObject)
                model.browse((DataObject) object);
        }
    }
    
    /**
     * Builds the tool bar hosting the {@link #cancelButton} and
     * {@link #finishButton}.
     * 
     * @return See above;
     */
    private JPanel buildToolBar()
    {
    	JPanel bar = new JPanel();
        bar.setBorder(null);
        bar.add(finishButton);
        bar.add(Box.createRigidArea(H_SPACER_SIZE));
        bar.add(cancelButton);
        return bar;
    }
    
    /** Builds and lays out the UI. */
    private void buildGUI()
    {
        setLayout(new BorderLayout(0, 0));
        setOpaque(true);
        add(titlePanel, BorderLayout.NORTH);
        centerPanel = new JPanel();
        centerPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        add(centerPanel, BorderLayout.CENTER);
        JPanel p = UIUtilities.buildComponentPanelRight(buildToolBar());
        p.setBorder(BorderFactory.createEtchedBorder());
        add(p, BorderLayout.SOUTH);
    }
    
    /**
     * Adds the nodes to the specified parent.
     * 
     * @param parent    The parent node.
     * @param nodes     The list of nodes to add.
     */
    private void buildTreeNode(TreeCheckNode parent, List nodes)
    {
        DefaultTreeModel tm = (DefaultTreeModel) tree.getModel();
        Iterator i = nodes.iterator();
        TreeCheckNode display;
        Set children;
        while (i.hasNext()) {
            display = (TreeCheckNode) i.next();
            tm.insertNodeInto(display, parent, parent.getChildCount());
            tree.expandPath(new TreePath(display.getPath()));
            children = display.getChildrenDisplay();
            if (children.size() != 0)
                buildTreeNode(display, sorter.sort(children));
        }  
    }
    
    /**
     * Returns the component hosting the display.
     * 
     * @return See above.
     */
    private JComponent getClassificationComponent()
    {
        Set paths = model.getPaths();
        if (paths.size() == 0) {
            finishButton.setEnabled(false);
            JPanel p = new JPanel();
            p.setBorder(BorderFactory.createEmptyBorder(5, 5, 10, 10));
            p.add(UIUtilities.setTextFont(getUnclassifiedNote()), 
                   BorderLayout.CENTER);
            return p;
        }
        //populates the tree
        DefaultTreeModel dtm = (DefaultTreeModel) tree.getModel();
        TreeCheckNode root = (TreeCheckNode) dtm.getRoot();
        Iterator i = paths.iterator();
        while (i.hasNext())
            root.addChildDisplay((TreeCheckNode) i.next()) ;
        buildTreeNode(root, sorter.sort(paths));
        //tree.expandPath(new TreePath(root.getPath()));
        dtm.reload();
        return new JScrollPane(tree);
    }
    
    /** 
     * Returns the title displayed in the titlePanel.
     * 
     * @return See above.
     */
    private String getPanelTitle()
    {
        switch (model.getMode()) {
            case Classifier.CLASSIFY_MODE: return ADD_PANEL_TITLE;
            case Classifier.DECLASSIFY_MODE: return REMOVE_PANEL_TITLE;
        }
        return "";
    }
    
    /** 
     * Returns the text displayed in the titlePanel.
     * 
     * @return See above.
     */
    private String getPanelText()
    {
        switch (model.getMode()) {
            case Classifier.CLASSIFY_MODE:
                return ADD_PANEL_TEXT+" "+model.getDataObject().getName();
            case Classifier.DECLASSIFY_MODE:
                return REMOVE_PANEL_TEXT+" "+model.getDataObject().getName();
        }
        return "";
    }
    
    /**
     * Returns the note displayed in the titlePanel.
     * 
     * @return See above.
     */
    private String getPanelNote()
    {
        switch (model.getMode()) {
            case Classifier.CLASSIFY_MODE: return ADD_PANEL_NOTE;
            case Classifier.DECLASSIFY_MODE: return REMOVE_PANEL_NOTE;
        }
        return "";
    }
    
    /**
     * Returns the note displaying the unclassified message.
     * 
     * @return See above.
     */
    private String getUnclassifiedNote()
    {
        switch (model.getMode()) {
            case Classifier.CLASSIFY_MODE: return ADD_UNCLASSIFIED_TEXT;
            case Classifier.DECLASSIFY_MODE: return REMOVE_UNCLASSIFIED_TEXT;
        }
        return "";
    }
    
    /** Creates a new instance. */
    ClassifierUI() {}
    
    /**
     * Links the View with its Model and Control.
     * 
     * @param controller    Reference to the Control.
     *                      Mustn't be <code>null</code>.
     * @param model         Reference to the Model.
     *                      Mustn't be <code>null</code>.
     */
    void initialize(ClassifierControl controller, ClassifierModel model)
    {
        if (model == null) throw new IllegalArgumentException("No model.");
        if (controller == null) 
            throw new IllegalArgumentException("No control.");
        this.model = model;
        this.controller = controller;
        initComponents();
        buildGUI();
    }
    
    /**
     * Sets the specified thumbnail 
     * 
     * @param thumbnail The thumbnail to set.
     */
    void setThumbnail(BufferedImage thumbnail)
    {
        JLabel label = new JLabel(new ImageIcon(thumbnail));
        label.addMouseListener(new MouseAdapter() {
            
            /**
             * Views the image if the user double-clicks on the thumbnail.
             */
            public void mouseClicked(MouseEvent e)
            {
                if (e.getClickCount() == 2) 
                    model.browse(model.getDataObject());
            }
        });
        titlePanel.setIconComponent(label);
    }
    
    /** Displays the classifications. */
    void showClassifications()
    {
        remove(centerPanel);
        add(getClassificationComponent(), BorderLayout.CENTER);
        validate();
        repaint();
    }

    /**
     * Notifies the user that a data loading/saving is happening.
     * 
     * @param b         Pass <code>true</code> to display the cancel button 
     *                  used to cancel any ongoing data loading/saving,
     *                  <code>false</code> otherwise.
     * @param text      The message displayed.
     * @param hide      Pass <code>true</code> to hide the progress bar,
     *                  <code>false</code> otherwise.
     */
    void notify(boolean b, String text, boolean hide)
    {
        model.getParentModel().setStatus(b, text, hide);
    }

    /**
     * Enables the {@link #finishButton} or not depending on specified value.
     * 
     * @param b Pass <code>true</code> to enable the button, 
     * 			<code>false</code> otherwise.
     */
	void handleButton(boolean b) { finishButton.setEnabled(b); }
    
}
