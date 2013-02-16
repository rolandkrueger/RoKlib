/*
 * $Id: VisibilityGroupManager.java 178 2010-10-31 18:01:20Z roland $
 * Copyright (C) 2007 Roland Krueger
 * Created on 19.10.2009
 *
 * Author: Roland Krueger (www.rolandkrueger.info)
 *
 * This file is part of RoKlib.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307
 * USA
 */
package info.rolandkrueger.roklib.util.groupvisibility;

import info.rolandkrueger.roklib.util.conditionalengine.BooleanExpression;
import info.rolandkrueger.roklib.util.helper.CheckForNull;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.Map;

public class VisibilityGroupManager implements Serializable
{
  private static final long serialVersionUID = 4836663644491428361L;

  private Map<String, VisibilityGroup> mGroups;

  public VisibilityGroupManager (int numberOfManagedObjects)
  {
    mGroups = new Hashtable<String, VisibilityGroup> (numberOfManagedObjects);
  }

  public VisibilityGroupManager ()
  {
    mGroups = new Hashtable<String, VisibilityGroup> ();
  }

  /**
   * Returns the number of {@link VisibilityGroup}s which are managed by this
   * {@link VisibilityGroupManager}.
   */
  public int getGroupCount ()
  {
    return mGroups.size ();
  }

  /**
   * Adds a component to the {@link VisibilityGroup} specified by the given
   * name. If a group with this name does not yet exist, it is created by the
   * manager.
   * 
   * @param groupName
   * @param groupMember
   * @return the {@link VisibilityGroup} object for the given group name. The
   *         provided component has beed added to this group.
   */
  public VisibilityGroup addGroupMember (String groupName,
      IVisibilityEnablingConfigurable groupMember)
  {
    CheckForNull.check (groupName, groupMember);
    VisibilityGroup group = createGroupIfNecessary (groupName);
    group.addVisibilityEnablingConfigurable (groupMember);
    return group;
  }

  private VisibilityGroup createGroupIfNecessary (String groupName)
  {
    VisibilityGroup group = mGroups.get (groupName);
    if (group == null)
    {
      group = new VisibilityGroup (groupName);
      mGroups.put (groupName, group);
    }
    return group;
  }

  /**
   * Provides the {@link VisibilityGroup} object for the given group name. If
   * there is no group available for this name, this method returns
   * <code>null</code>. With method {@link #doesGroupExist(String)} you can
   * check if a {@link VisibilityGroup} object exists with a given name.
   * 
   * @param groupName
   *          identifier name for a {@link VisibilityGroup} object managed by
   *          this {@link VisibilityGroupManager}
   * @return the {@link VisibilityGroup} object for the given group name or
   *         <code>null</code> it such a group does is not exist
   */
  public VisibilityGroup getVisibilityGroup (String groupName)
  {
    return mGroups.get (groupName);
  }

  /**
   * Checks if a {@link VisibilityGroup} object for the given name is managed by
   * this {@link VisibilityGroupManager}.
   */
  public boolean doesGroupExist (String groupName)
  {
    if (groupName == null) return false;
    return mGroups.containsKey (groupName);
  }

  /**
   * Sets the {@link BooleanExpression} object which will be responsible for
   * managing the visibility of the specified {@link VisibilityGroup}. If the
   * expression's value changes its state from <code>false</code> to
   * <code>true</code> all components in the group are made visible and vice
   * versa.
   * 
   * @param groupName
   *          name of the {@link VisibilityGroup} for which the given
   *          {@link BooleanExpression} is set
   * @param expression
   *          {@link BooleanExpression} which is responsible for making the
   *          components of the specified {@link VisibilityGroup} visible or
   *          invisible
   */
  public void setExpressionForVisibility (String groupName, BooleanExpression expression)
  {
    CheckForNull.check (groupName);
    VisibilityGroup group = createGroupIfNecessary (groupName);
    group.setExpressionForVisibility (expression);
  }

  /**
   * Sets the {@link BooleanExpression} object which will be responsible for
   * managing the enablement of the specified {@link VisibilityGroup}. If the
   * expression's value changes its state from <code>false</code> to
   * <code>true</code> all components in the group are enabled and vice versa.
   * 
   * @param groupName
   *          name of the {@link VisibilityGroup} for which the given
   *          {@link BooleanExpression} is set
   * @param expression
   *          {@link BooleanExpression} which is responsible for enabling or
   *          disabling the components of the specified {@link VisibilityGroup}
   */
  public void setExpressionForEnabling (String groupName, BooleanExpression expression)
  {
    CheckForNull.check (groupName);
    VisibilityGroup group = createGroupIfNecessary (groupName);
    group.setExpressionForEnabling (expression);
  }

  /**
   * Makes all components which are members of the given component group visible
   * or invisible by calling their
   * {@link IVisibilityEnablingConfigurable#setVisible(boolean)} method.
   * 
   * @param groupName
   *          name of the {@link VisibilityGroup}
   * @param visible
   *          <code>true</code> if the components should be made visible,
   *          <code>false</code> otherwise
   * @return <code>false</code> if a {@link VisibilityGroup} with the given name
   *         does not exist in this {@link VisibilityGroupManager}
   */
  public boolean setGroupVisible (String groupName, boolean visible)
  {
    CheckForNull.check (groupName);

    VisibilityGroup group = mGroups.get (groupName);
    if (group == null) return false;
    group.setVisible (visible);
    return true;
  }

  /**
   * Enables or disabled all components which are members of the given component
   * group by calling their
   * {@link IVisibilityEnablingConfigurable#setEnabled(boolean)} method.
   * 
   * @param groupName
   *          name of the {@link VisibilityGroup}
   * @param enabled
   *          <code>true</code> if all components of the specified group shall
   *          be enabled. This argument is passed through to the
   *          {@link IVisibilityEnablingConfigurable#setEnabled(boolean)} method
   *          of each component in the specified group.
   * @return <code>false</code> if the specified {@link VisibilityGroup} does
   *         not exist in this {@link VisibilityGroupManager}
   */
  public boolean setGroupEnabled (String groupName, boolean enabled)
  {
    CheckForNull.check (groupName);

    VisibilityGroup group = mGroups.get (groupName);
    if (group == null) return false;
    group.setEnabled (enabled);
    return true;
  }
}
