/*
 * $Id: BooleanOperationTest.java 181 2010-11-01 09:39:13Z roland $
 * Copyright (C) 2007 Roland Krueger
 * Created on 21.10.2009
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
package info.rolandkrueger.roklib.util.conditionalengine.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import info.rolandkrueger.roklib.util.conditionalengine.AbstractCondition;
import info.rolandkrueger.roklib.util.conditionalengine.BoolExpressionBuilder;
import info.rolandkrueger.roklib.util.conditionalengine.BooleanExpression;
import info.rolandkrueger.roklib.util.conditionalengine.Condition;
import info.rolandkrueger.roklib.util.conditionalengine.IConditionListener;

import org.junit.Before;
import org.junit.Test;

public class BooleanOperationTest
{
  SingleConditionTestClass c1;
  SingleConditionTestClass c2;
  SingleConditionTestClass c3;
  SingleConditionTestClass c4;

  @Before
  public void setUp ()
  {
    c1 = new SingleConditionTestClass ("c1", false);
    c2 = new SingleConditionTestClass ("c2", false);
    c3 = new SingleConditionTestClass ("c3", false);
    c4 = new SingleConditionTestClass ("c4", false);
  }

  @Test
  public void testNestedChangeNotfication ()
  {
    BooleanExpression nested = BoolExpressionBuilder.createANDExpression (
        BoolExpressionBuilder.createORExpression (c1, c2),
        BoolExpressionBuilder.createORExpression (c3, c4));
    ConditionListenerTestClass listener = new ConditionListenerTestClass ();
    nested.addConditionListener (listener);
    assertFalse (nested.getBooleanValue ());
    c1.setValue (true);
    assertFalse (nested.getBooleanValue ());
    assertFalse (listener.conditionChanged);
    c3.setValue (true);
    assertTrue (nested.getBooleanValue ());
    assertTrue (listener.conditionChanged);
    listener.conditionChanged = false;
    c2.setValue (true);
    assertTrue (nested.getBooleanValue ());
    assertFalse (listener.conditionChanged);
    c3.setValue (false);
    assertFalse (nested.getBooleanValue ());
    assertTrue (listener.conditionChanged);
  }

  private class ConditionListenerTestClass implements IConditionListener
  {
    private boolean conditionChanged = false;

    public void conditionChanged (AbstractCondition source)
    {
      conditionChanged = true;
    }
  }

  @Test
  public void testNestedOperation ()
  {
    BooleanExpression nested = BoolExpressionBuilder.createANDExpression (
        BoolExpressionBuilder.createORExpression (c1, c2),
        BoolExpressionBuilder.createORExpression (c3, c4));
    assertFalse (nested.getBooleanValue ());
    c1.setValue (true);
    assertFalse (nested.getBooleanValue ());
    c2.setValue (true);
    assertFalse (nested.getBooleanValue ());
    c3.setValue (true);
    assertTrue (nested.getBooleanValue ());
    c1.setValue (false);
    assertTrue (nested.getBooleanValue ());
  }

  @Test
  public void testShortCircuitFunctionality ()
  {
    BooleanExpression expression = BoolExpressionBuilder.createANDExpression (c1, c2, c3, c4);
    c1.setValue (true);
    c2.setValue (true);
    assertFalse (expression.getBooleanValue ());
    assertTrue (c1.getValueCalled);
    assertTrue (c2.getValueCalled);
    assertTrue (c3.getValueCalled);
    assertFalse (c4.getValueCalled);
  }

  private class SingleConditionTestClass extends Condition
  {
    private static final long serialVersionUID = 9063400667739940285L;

    private boolean getValueCalled = false;

    public SingleConditionTestClass (String name, boolean value)
    {
      super (name, value);
    }

    @Override
    public boolean getBooleanValue ()
    {
      getValueCalled = true;
      return super.getBooleanValue ();
    }
  }
}
