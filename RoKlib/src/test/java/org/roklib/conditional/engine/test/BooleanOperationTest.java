/*
 * Copyright (C) 2007 Roland Krueger
 * Created on 21.10.2009
 *
 * Author: Roland Krueger (www.rolandkrueger.info)
 *
 * This file is part of RoKlib.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.roklib.conditional.engine.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.roklib.conditional.engine.AbstractCondition;
import org.roklib.conditional.engine.BoolExpressionBuilder;
import org.roklib.conditional.engine.BooleanExpression;
import org.roklib.conditional.engine.Condition;
import org.roklib.conditional.engine.IConditionListener;

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
        BoolExpressionBuilder.createORExpression (c1, c2), BoolExpressionBuilder.createORExpression (c3, c4));
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
        BoolExpressionBuilder.createORExpression (c1, c2), BoolExpressionBuilder.createORExpression (c3, c4));
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

    private boolean           getValueCalled   = false;

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
