/*
 * org.openmicroscopy.shoola.agents.treeviewer.view.PopupMenu
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

package org.openmicroscopy.shoola.agents.treeviewer.view;


//Java imports
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.border.BevelBorder;


//Third-party libraries

//Application-internal dependencies
import org.openmicroscopy.shoola.agents.treeviewer.IconManager;
import org.openmicroscopy.shoola.agents.treeviewer.TreeViewerAgent;
import org.openmicroscopy.shoola.agents.treeviewer.actions.TreeViewerAction;


/** 
 * Pop-up menu for nodes in the browser display.
 *
 * @author  Jean-Marie Burel &nbsp;&nbsp;&nbsp;&nbsp;
 * 				<a href="mailto:j.burel@dundee.ac.uk">j.burel@dundee.ac.uk</a>
 * @version 2.2
 * <small>
 * (<b>Internal version:</b> $Revision$ $Date$)
 * </small>
 * @since OME2.2
 */
class PopupMenu
    extends JPopupMenu
{

    /** 
     * Button to bring up the property sheet of a hierarchy object &#151; 
     * project, dataset, category group, category, or image.
     */
    private JMenuItem           properties;
    
    /** 
     * Button to bring up the property sheet of a hierarchy object &#151; 
     * project, dataset, category group, category, or image.
     */
    private JMenuItem           annotate;
    
    /** Button to browse a container or bring up the Viewer for an image. */
    private JMenuItem           view;
    
    /** Button to add existing element to the specified container. */
    private JMenuItem           existingElement;
    
    /** Button to add element to the specified container. */
    private JMenuItem           newElement;
    
    /** Button to cut the selected elements. */
    private JMenuItem           cutElement;
    
    /** Button to copy the selected elements. */
    private JMenuItem           copyElement;
    
    /** Button to paste the selected elements. */
    private JMenuItem           pasteElement;
    
    /** Button to delete the selected elements. */
    private JMenuItem           deleteElement;
    
    /** Button to classify the selected elements. */
    private JMenuItem          	classifyElement;
    
    /** Button to classify the selected elements. */
    private JMenuItem          	declassifyElement;
    
    /** Button to classify the images contained in the selected element. */
    private JMenuItem          	classifyChildrenElement;
    
    /** Button to annotate the images contained in the selected element. */
    private JMenuItem          	annotateChildrenElement;
    
    /** Button to remove experimenter node from the display. */
    private JMenuItem			removeExperimenterElement;
    
    /** Button to refresh the experimenter data */
    private JMenuItem			refreshExperimenterElement;
    
    /** Reference to the Control. */
    private TreeViewerControl   controller;
    
    /** Font label. */
    private Font				fontLabel;
    
    /**
     * Sets the defaults of the specified menu item.
     * 
     * @param item The menu item.
     * @param name The name of the item.
     */
    private void initMenuItem(JMenuItem item, String name)
    {
        if (name != null) item.setText(name);
        item.setBorder(null);
        item.setFont(fontLabel);
    }
    
    /** Helper method to create the menu items with the given actions. */
    private void createMenuItems()
    {
        TreeViewerAction a = controller.getAction(TreeViewerControl.PROPERTIES);
        properties = new JMenuItem(a);
        initMenuItem(properties, a.getActionName());
        a = controller.getAction(TreeViewerControl.ANNOTATE);
        annotate = new JMenuItem(a);
        initMenuItem(annotate, a.getActionName());
        a = controller.getAction(TreeViewerControl.VIEW);
        view = new JMenuItem(a);
        initMenuItem(view, a.getActionName());
        a = controller.getAction(TreeViewerControl.CREATE_OBJECT);
        newElement = new JMenuItem(a);
        initMenuItem(newElement, a.getActionName());
        a = controller.getAction(TreeViewerControl.CUT_OBJECT);
        cutElement = new JMenuItem(a); 
        initMenuItem(cutElement, a.getActionName());
        a = controller.getAction(TreeViewerControl.COPY_OBJECT);
        copyElement = new JMenuItem(a); 
        initMenuItem(copyElement, a.getActionName());
        a = controller.getAction(TreeViewerControl.PASTE_OBJECT);
        pasteElement = new JMenuItem(a); 
        initMenuItem(pasteElement, a.getActionName());
        a = controller.getAction(TreeViewerControl.DELETE_OBJECT);
        deleteElement = new JMenuItem(a); 
        initMenuItem(deleteElement, a.getActionName());
        a = controller.getAction(TreeViewerControl.ADD_OBJECT);
        existingElement = new JMenuItem(a);
        initMenuItem(existingElement, a.getActionName());
        a = controller.getAction(TreeViewerControl.CLASSIFY);
        classifyElement = new JMenuItem(a);
        initMenuItem(classifyElement, a.getActionName());
        a = controller.getAction(TreeViewerControl.DECLASSIFY);
        declassifyElement = new JMenuItem(a);
        initMenuItem(declassifyElement, a.getActionName());
        a = controller.getAction(TreeViewerControl.ANNOTATE_CHILDREN);
        annotateChildrenElement = new JMenuItem(a);
        initMenuItem(annotateChildrenElement, a.getActionName());
        a = controller.getAction(TreeViewerControl.CLASSIFY_CHILDREN);
        classifyChildrenElement = new JMenuItem(a);
        initMenuItem(classifyChildrenElement, a.getActionName());
        a = controller.getAction(TreeViewerControl.REMOVE_FROM_DISPLAY);
        removeExperimenterElement = new JMenuItem(a);
        initMenuItem(removeExperimenterElement, "Remove from display");
        removeExperimenterElement = new JMenuItem(a);
        initMenuItem(removeExperimenterElement, "Remove from display");
        a = controller.getAction(TreeViewerControl.REFRESH_EXPERIMENTER);
        refreshExperimenterElement = new JMenuItem(a);
        initMenuItem(refreshExperimenterElement, null);
    }
    
    /**
     * Creates the sub-menu to manage the data.
     * 
     * @return See above
     */
    private JMenu createManagementMenu()
    {
        JMenu managementMenu = new JMenu();
        initMenuItem(managementMenu, "Manage");
        IconManager im = IconManager.getInstance();
        managementMenu.setIcon(im.getIcon(IconManager.TRANSPARENT));
        managementMenu.add(newElement);
        managementMenu.add(cutElement);
        managementMenu.add(copyElement);
        managementMenu.add(pasteElement);
        managementMenu.add(deleteElement);
        return managementMenu;
    }
    
    /** Builds and lays out the GUI. */
    private void buildGUI()
    {
        setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        add(view);
        add(createManagementMenu());
        add(new JSeparator(JSeparator.HORIZONTAL));
        add(classifyElement);
        add(declassifyElement);
        add(classifyChildrenElement);
        add(new JSeparator(JSeparator.HORIZONTAL));
        add(annotate);
        add(annotateChildrenElement);
        add(new JSeparator(JSeparator.HORIZONTAL));
        add(properties);
        add(new JSeparator(JSeparator.HORIZONTAL));
        add(refreshExperimenterElement);
        add(removeExperimenterElement);
    }
    
    /** 
     * Creates a new instance.
     *
     * @param controller The Controller. Mustn't be <code>null</code>.
     */
    PopupMenu(TreeViewerControl controller)
    {
        if (controller == null) 
            throw new IllegalArgumentException("No control.");
        this.controller = controller;
        fontLabel = (Font) TreeViewerAgent.getRegistry().lookup(
                					"/resources/fonts/Labels");
        createMenuItems();
        buildGUI() ;
    }
    
}
