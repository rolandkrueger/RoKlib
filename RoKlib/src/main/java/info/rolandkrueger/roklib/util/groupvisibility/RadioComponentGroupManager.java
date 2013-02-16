/*
 * $Id: RadioComponentGroupManager.java 178 2010-10-31 18:01:20Z roland $
 * Copyright (C) 2007 Roland Krueger
 * Created on 01.07.2010
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

import info.rolandkrueger.roklib.util.bool.AndOperation;
import info.rolandkrueger.roklib.util.conditionalengine.AbstractCondition;
import info.rolandkrueger.roklib.util.conditionalengine.BoolExpressionBuilder;
import info.rolandkrueger.roklib.util.conditionalengine.BooleanExpression;
import info.rolandkrueger.roklib.util.conditionalengine.Condition;
import info.rolandkrueger.roklib.util.conditionalengine.IConditionListener;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.Map;

/**
 * <p>
 * This class manages a set of components which can be switched on or off in the
 * same way as a group of radio buttons. Component in this context means any
 * object which implements the {@link IVisibilityEnablingConfigurable}
 * interface. This may be a UI component or any other object that can be
 * switched on or off.
 * </p>
 * <p>
 * An example use case for this class is the navigation bar in a web
 * application. Depending on which section the user is visiting, the links for
 * the individual sections in the navigation bar are either selected or not
 * selected. If the user clicks on such a link, its background image may change
 * from an unselected graphic to a selected graphic. All other link images have
 * to show the unselected image accordingly. This behavior is controlled
 * automatically by the {@link RadioComponentGroupManager}.
 * </p>
 * 
 * @author Roland Krueger
 * @see VisibilityGroupManager
 */
public class RadioComponentGroupManager implements Serializable
{
  private static final long serialVersionUID = 3934091376239028966L;

  public enum Mode
  {
    ENABLING, VISIBILITY
  };

  private VisibilityGroupManager mManager;
  private Map<String, RadioComponentSwitch> mConditions;
  private Mode mMode;

  public RadioComponentGroupManager (Mode mode)
  {
    this (7, mode);
  }

  public RadioComponentGroupManager ()
  {
    this (7);
  }

  public RadioComponentGroupManager (int numberOfManagedComponents, Mode mode)
  {
    mMode = mode;
    if (mMode == null)
    {
      mMode = Mode.ENABLING;
    }
    mManager = new VisibilityGroupManager (numberOfManagedComponents);
    mConditions = new Hashtable<String, RadioComponentSwitch> (numberOfManagedComponents);
  }

  public RadioComponentGroupManager (int numberOfManagedComponents)
  {
    this (numberOfManagedComponents, Mode.ENABLING);
  }

  public RadioComponentSwitch addComponent (String groupId,
      IVisibilityEnablingConfigurable component)
  {
    RadioComponentSwitch result = new RadioComponentSwitch (groupId + "_switch");
    mConditions.put (groupId, result);
    mManager.addGroupMember (groupId, component);
    recalculateExpressions ();
    return result;
  }

  private void recalculateExpressions ()
  {
    if (mConditions.size () < 2) return;

    for (Map.Entry<String, RadioComponentSwitch> currentEntry : mConditions.entrySet ())
    {
      currentEntry.getValue ().clearConditionListeners ();
      BooleanExpression expression = new BooleanExpression (new AndOperation ());

      for (Map.Entry<String, RadioComponentSwitch> entry : mConditions.entrySet ())
      {
        if (currentEntry == entry) continue;
        expression.addOperand (entry.getValue ());
        currentEntry.getValue ().addConditionListener (entry.getValue ());
      }

      expression = BoolExpressionBuilder.createANDExpression (currentEntry.getValue (),
          BoolExpressionBuilder.createNOTExpression (expression));

      if (mMode == Mode.ENABLING)
      {
        mManager.setExpressionForEnabling (currentEntry.getKey (), expression);
      } else
      {
        mManager.setExpressionForVisibility (currentEntry.getKey (), expression);
      }
    }
  }

  public static class RadioComponentSwitch extends Condition implements IConditionListener
  {
    private static final long serialVersionUID = 4106071527518882170L;

    public RadioComponentSwitch (String name)
    {
      super (name);
    }

    public void select ()
    {
      setValue (true);
    }

    public void conditionChanged (AbstractCondition source)
    {
      if (source.getBooleanValue () == true)
      {
        setValue (false);
      }
    }
  }
}
