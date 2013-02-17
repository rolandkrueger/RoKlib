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
package org.roklib.util.conditionalengine.test;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.roklib.util.conditionalengine.AbstractCondition;
import org.roklib.util.conditionalengine.Condition;
import org.roklib.util.conditionalengine.IConditionListener;

public class AbstractConditionTest
{
  private Condition mTestObj;

  @Before
  public void setUp ()
  {
    mTestObj = new Condition ("testObj", false);
  }

  @Test
  public void testAddConditionListener ()
  {
    ConditionListener listener = new ConditionListener ();
    mTestObj.addConditionListener (listener);
    mTestObj.setValue (true);
    // call setValue again, this time the value doesn't change. The listener is
    // not expected
    // to be called this time.
    mTestObj.setValue (true);
    mTestObj.setValue (false);
    // listener is expected to have been called two times
    assertEquals (2, listener.callCount);
  }

  @Test
  public void testRemoveConditionListener ()
  {
    ConditionListener listener = new ConditionListener ();
    mTestObj.addConditionListener (listener);
    mTestObj.setValue (true);
    mTestObj.removeConditionListener (listener);
    mTestObj.setValue (false);
    // only the first value change is registered by the listener
    assertEquals (1, listener.callCount);
  }

  private class ConditionListener implements IConditionListener
  {
    private int callCount = 0;

    public void conditionChanged (AbstractCondition source)
    {
      callCount++;
    }
  }
}
