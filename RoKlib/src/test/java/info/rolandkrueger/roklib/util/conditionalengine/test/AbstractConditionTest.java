/*
 * $Id: AbstractConditionTest.java 181 2010-11-01 09:39:13Z roland $
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

import static org.junit.Assert.assertEquals;
import info.rolandkrueger.roklib.util.conditionalengine.AbstractCondition;
import info.rolandkrueger.roklib.util.conditionalengine.Condition;
import info.rolandkrueger.roklib.util.conditionalengine.IConditionListener;

import org.junit.Before;
import org.junit.Test;

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
