/*
 * org.openmicroscopy.shoola.agents.treeviewer.actions.PropertiesAction
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

package org.openmicroscopy.shoola.agents.treeviewer.actions;

//Java imports
import java.awt.event.ActionEvent;
import javax.swing.Action;

//Third-party libraries

//Application-internal dependencies
import org.openmicroscopy.shoola.agents.treeviewer.IconManager;
import org.openmicroscopy.shoola.agents.treeviewer.browser.Browser;
import org.openmicroscopy.shoola.agents.treeviewer.browser.TreeImageDisplay;
import org.openmicroscopy.shoola.agents.treeviewer.cmd.PropertiesCmd;
import org.openmicroscopy.shoola.agents.treeviewer.view.TreeViewer;
import org.openmicroscopy.shoola.util.ui.UIUtilities;
import pojos.DataObject;

/** 
 * Brings up the property widget.
 * This action is enabled on all display nodes, expected on the root node.
 * 
 * @author  Jean-Marie Burel &nbsp;&nbsp;&nbsp;&nbsp;
 * 				<a href="mailto:j.burel@dundee.ac.uk">j.burel@dundee.ac.uk</a>
 * @version 2.2
 * <small>
 * (<b>Internal version:</b> $Revision$ $Date$)
 * </small>
 * @since OME2.2
 */
public class PropertiesAction
    extends TreeViewerAction
{
    
    /** The name of the action. */
    private static final String NAME = "Properties";
    
    /** The description of the action. */
    private static final String DESCRIPTION = "Display properties.";
    
    /**
     * Callback to notify of a change in the currently selected display
     * in the currently selected 
     * {@link org.openmicroscopy.shoola.agents.treeviewer.browser.Browser}.
     * 
     * @param selectedDisplay The newly selected display node.
     */
    protected void onDisplayChange(TreeImageDisplay selectedDisplay)
    {
        if (selectedDisplay == null) {
            setEnabled(false);
            return;
        }
        Browser browser = model.getSelectedBrowser();
        if (browser != null) {
            if (browser.getSelectedDisplays().length > 1) {
                setEnabled(false);
                return;
            }
        }
        Object ho = selectedDisplay.getUserObject();
        setEnabled(!((ho == null) || !(ho instanceof DataObject)));
    }
    
    /**
     * Creates a new instance.
     * 
     * @param model Reference to the Model. Mustn't be <code>null</code>.
     */
    public PropertiesAction(TreeViewer model)
    {
        super(model);
        name = NAME;
        //putValue(Action.NAME, NAME);
        putValue(Action.SHORT_DESCRIPTION, 
                UIUtilities.formatToolTipText(DESCRIPTION));
        IconManager im = IconManager.getInstance();
        putValue(Action.SMALL_ICON, im.getIcon(IconManager.PROPERTIES));
    } 

    /** 
     * Creates a {@link PropertiesCmd} command to execute the action.
     * @see java.awt.event.ActionListener#actionPerformed(ActionEvent)
     */
    public void actionPerformed(ActionEvent e)
    {
       PropertiesCmd cmd = new PropertiesCmd(model);
       cmd.execute();
    }

}
