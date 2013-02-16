/*
 * $Id: BooleanExpression.java 181 2010-11-01 09:39:13Z roland $
 * Copyright (C) 2007 Roland Krueger
 * Created on 20.10.2009
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
package info.rolandkrueger.roklib.util.conditionalengine;

import info.rolandkrueger.roklib.util.bool.IBooleanOperation;
import info.rolandkrueger.roklib.util.helper.CheckForNull;

import java.util.LinkedList;
import java.util.List;

public class BooleanExpression extends AbstractCondition implements IConditionListener
{
  private static final long serialVersionUID = 3479069548966346585L;

  private IBooleanOperation mOperation;
  private List<AbstractCondition> mConditions;
  private Boolean mCurrentValue;

  public BooleanExpression (IBooleanOperation operation)
  {
    CheckForNull.check (operation);
    mOperation = operation;
    mCurrentValue = null;
  }

  public void conditionChanged (AbstractCondition source)
  {
    if (mCurrentValue == null) getBooleanValue ();
    boolean previousValue = mCurrentValue;
    boolean value = getBooleanValue ();
    if (value != previousValue)
    {
      fireConditionChanged ();
    }
  }

  @Override
  public void addConditionListener (IConditionListener listener)
  {
    if (getListenerCount () == 0)
    {
      for (AbstractCondition condition : mConditions)
      {
        condition.addConditionListener (this);
      }
    }
    super.addConditionListener (listener);
  }

  @Override
  public void removeConditionListener (IConditionListener listener)
  {
    super.removeConditionListener (listener);
    if (getListenerCount () == 0)
    {
      for (AbstractCondition condition : mConditions)
      {
        condition.removeConditionListener (this);
      }
    }
  }

  public IBooleanOperation getBooleanOperation ()
  {
    return mOperation;
  }

  public boolean getBooleanValue ()
  {
    if (mConditions.size () < mOperation.getMinimumNumberOfOperands ())
      throw new IllegalStateException ("Not enough operands defined for this expression.");

    if (mOperation.isUnaryOperation () && mConditions.size () > 1)
      throw new IllegalStateException ("Too many operands defined for a unary operation.");

    if (mOperation.isUnaryOperation ())
    {
      mOperation.setLeftHandOperand (mConditions.get (0).getBooleanValue ());
      mCurrentValue = mOperation.execute ();
      return mCurrentValue;
    }

    AbstractCondition firstOperand = mConditions.get (0);
    boolean firstOperandValue = firstOperand.getBooleanValue ();

    for (int i = 1; i < mConditions.size (); ++i)
    {
      if (mOperation.canShortCircuit (firstOperandValue))
      {
        mCurrentValue = mOperation.getShortCircuit (firstOperandValue);
        return mCurrentValue;
      }
      firstOperandValue = executeOperation (firstOperandValue, mConditions.get (i));
    }

    mCurrentValue = firstOperandValue;
    return firstOperandValue;
  }

  public void addOperand (AbstractCondition operand)
  {
    CheckForNull.check (operand);
    if (mConditions == null) mConditions = new LinkedList<AbstractCondition> ();
    mConditions.add (operand);
  }

  private boolean executeOperation (boolean firstOperand, AbstractCondition condition)
  {
    mOperation.setLeftHandOperand (firstOperand);
    mOperation.setRightHandOperand (condition.getBooleanValue ());
    return mOperation.execute ();
  }

  @Override
  public String toString ()
  {
    StringBuilder buf = new StringBuilder ();
    buf.append ("[").append (mOperation.getClass ().getSimpleName ());
    buf.append (": ").append (mConditions);
    return buf.toString ();
  }
}
