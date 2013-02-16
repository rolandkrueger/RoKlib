/*
 * $Id: VisibilityGroup.java 178 2010-10-31 18:01:20Z roland $
 * Copyright (C) 2007 Roland Krueger
 * Created on 18.10.2009
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

import info.rolandkrueger.roklib.util.conditionalengine.AbstractCondition;
import info.rolandkrueger.roklib.util.conditionalengine.BooleanExpression;
import info.rolandkrueger.roklib.util.conditionalengine.IConditionListener;
import info.rolandkrueger.roklib.util.helper.CheckForNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class VisibilityGroup implements IVisibilityEnablingConfigurable, IConditionListener
{
  private static final long serialVersionUID = - 4366993767678121332L;

  private boolean mVisible;
  private boolean mEnabled;
  private String mGroupName;
  private Set<IVisibilityEnablingConfigurable> mGroupMembers;
  private BooleanExpression mBooleanExpressionForVisibility;
  private BooleanExpression mBooleanExpressionForEnabling;

  public VisibilityGroup (String name)
  {
    CheckForNull.check (name);
    mGroupName = name;
    mGroupMembers = Collections.synchronizedSet (new HashSet<IVisibilityEnablingConfigurable> ());
    mVisible = true;
    mEnabled = true;
  }

  public void addVisibilityEnablingConfigurable (IVisibilityEnablingConfigurable groupMember)
  {
    CheckForNull.check (groupMember);
    mGroupMembers.add (groupMember);
    applyExpressionsIfAvailable ();
  }

  public boolean removeVisibilityEnablingConfigurable (IVisibilityEnablingConfigurable groupMember)
  {
    return mGroupMembers.remove (groupMember);
  }

  public void setEnabled (boolean enabled)
  {
    mEnabled = enabled;
    for (IVisibilityEnablingConfigurable member : mGroupMembers)
      member.setEnabled (enabled);
  }

  public void setVisible (boolean visible)
  {
    for (IVisibilityEnablingConfigurable member : mGroupMembers)
      member.setVisible (visible);
    mVisible = visible;
  }

  public boolean isEnabled ()
  {
    return mEnabled;
  }

  public boolean isVisible ()
  {
    return mVisible;
  }

  public String getName ()
  {
    return mGroupName;
  }

  public int getSize ()
  {
    return mGroupMembers.size ();
  }

  public Set<IVisibilityEnablingConfigurable> getGroupMembers ()
  {
    return Collections.unmodifiableSet (mGroupMembers);
  }

  public void setExpressionForVisibility (BooleanExpression expression)
  {
    if (mBooleanExpressionForVisibility != null)
      mBooleanExpressionForVisibility.removeConditionListener (this);

    mBooleanExpressionForVisibility = expression;
    if (expression != null)
    {
      mBooleanExpressionForVisibility.addConditionListener (this);
      applyExpressionsIfAvailable ();
    }
  }

  public void setExpressionForEnabling (BooleanExpression expression)
  {
    if (mBooleanExpressionForEnabling != null)
      mBooleanExpressionForEnabling.removeConditionListener (this);

    mBooleanExpressionForEnabling = expression;
    if (expression != null)
    {
      mBooleanExpressionForEnabling.addConditionListener (this);
      applyExpressionsIfAvailable ();
    }
  }

  private void applyExpressionsIfAvailable ()
  {
    if (mBooleanExpressionForEnabling != null)
    {
      setEnabled (mBooleanExpressionForEnabling.getBooleanValue ());
    }
    if (mBooleanExpressionForVisibility != null)
    {
      setVisible (mBooleanExpressionForVisibility.getBooleanValue ());
    }
  }

  public void conditionChanged (AbstractCondition source)
  {
    if (source == mBooleanExpressionForVisibility)
      setVisible (mBooleanExpressionForVisibility.getBooleanValue ());
    if (source == mBooleanExpressionForEnabling)
      setEnabled (mBooleanExpressionForEnabling.getBooleanValue ());
  }
}
