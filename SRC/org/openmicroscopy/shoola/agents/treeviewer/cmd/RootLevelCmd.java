/*
 * org.openmicroscopy.shoola.agents.treeviewer.cmd.HiViewerWin
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

package org.openmicroscopy.shoola.agents.treeviewer.cmd;




//Java imports

//Third-party libraries

//Application-internal dependencies
import org.openmicroscopy.shoola.agents.treeviewer.view.TreeViewer;


/** 
 * Commands to set the root of the hierarchy. It can either be
 * {@link TreeViewer#USER_ROOT} or {@link TreeViewer#GROUP_ROOT}.
 * 
 * @author  Jean-Marie Burel &nbsp;&nbsp;&nbsp;&nbsp;
 * 				<a href="mailto:j.burel@dundee.ac.uk">j.burel@dundee.ac.uk</a>
 * @version 2.2
 * <small>
 * (<b>Internal version:</b> $Revision$Date: )
 * </small>
 * @since OME2.2
 */
public class RootLevelCmd
	implements ActionCmd
{

    /** Reference to the model. */
    private TreeViewer	model;
    
    /** 
     * The root of the hierarchy. One of the following constants:
     * {@link TreeViewer#USER_ROOT} and {@link TreeViewer#GROUP_ROOT}.
     */
    private int 		rootLevel;
    
    /** 
     * The id of the root node. This field is only used if the 
     * {@link #rootLevel} is {@link TreeViewer#GROUP_ROOT}.
     */
    private long        rootID;
    
    /**
     * Checks if the specified level is supported.
     * 
     * @param level The level to control.
     */
    private void checkLevel(int level)
    {
        switch (level) {
	        case TreeViewer.USER_ROOT:
	        case TreeViewer.GROUP_ROOT:    
	            return;
	        default:
	            throw new IllegalArgumentException("Root level not supported");
        }
    }
    
    /**
     * Creates a new instance.
     * 
     * @param model     Reference to the model. Mustn't be <code>null</code>.
     * @param rootLevel The root of the hierarchy.
     * 					One of the following constants:
     * 					{@link TreeViewer#USER_ROOT},
     * 					{@link TreeViewer#GROUP_ROOT}.
     * @param rootID    The id of the root node.
     */
    public RootLevelCmd(TreeViewer model, int rootLevel, long rootID)
    {
        if (model == null) throw new IllegalArgumentException("No model.");
        checkLevel(rootLevel);
        this.model = model;
        this.rootLevel = rootLevel;
        this.rootID = rootID;
    }
    
    /** Implemented as specified by {@link ActionCmd}. */
    public void execute()
    {
        model.setHierarchyRoot(rootLevel, rootID);
    }

}
