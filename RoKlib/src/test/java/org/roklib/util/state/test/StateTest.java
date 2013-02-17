/*
 * Copyright (C) 2007 Roland Krueger
 * Created on 29.01.2010
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
package org.roklib.util.state.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.roklib.util.state.State;

public class StateTest
{
  private static class TestState extends State<TestState>
  {
    private static final long                 serialVersionUID = -3126104638124546290L;
    public final static StateValue<TestState> TEST_STATE_ON    = new StateValue<TestState> ("TEST_STATE_ON");
    public final static StateValue<TestState> TEST_STATE_OFF   = new StateValue<TestState> ("TEST_STATE_OFF");

    public TestState ()
    {
      super ();
    }

    public TestState (StateValue<TestState> defaultState)
    {
      super (defaultState);
    }
  };

  private static class TestStateDerived extends TestState
  {
    private static final long                 serialVersionUID = 3136674823264291904L;
    public final static StateValue<TestState> TEST_STATE_NONE  = new StateValue<TestState> ("TEST_STATE_NONE");
  }

  private TestState mTestObj;

  @Before
  public void setUp ()
  {
    mTestObj = new TestState ();
  }

  @Test
  public void testHasState ()
  {
    assertTrue (mTestObj.hasState (null));
    mTestObj.setStateValue (TestState.TEST_STATE_ON);
    assertTrue (mTestObj.hasState (TestState.TEST_STATE_ON));
    assertFalse (mTestObj.hasState (TestState.TEST_STATE_OFF));
    mTestObj.setStateValue (TestStateDerived.TEST_STATE_NONE);
    assertTrue (mTestObj.hasState (TestStateDerived.TEST_STATE_NONE));
    assertFalse (mTestObj.hasState (TestStateDerived.TEST_STATE_ON));
  }

  @Test
  public void testGetState ()
  {
    mTestObj.setStateValue (TestState.TEST_STATE_ON);
    assertEquals (TestState.TEST_STATE_ON, mTestObj.getStateValue ());
    assertEquals (TestStateDerived.TEST_STATE_ON, mTestObj.getStateValue ());
  }

  @Test
  public void testReset ()
  {
    mTestObj.setStateValue (TestState.TEST_STATE_ON);
    mTestObj.reset ();
    assertNull (mTestObj.getStateValue ());
    mTestObj = new TestState (TestState.TEST_STATE_OFF);
    assertEquals (TestState.TEST_STATE_OFF, mTestObj.getStateValue ());
    mTestObj.setStateValue (TestState.TEST_STATE_ON);
    assertEquals (TestState.TEST_STATE_ON, mTestObj.getStateValue ());
    mTestObj.reset ();
    assertEquals (TestState.TEST_STATE_OFF, mTestObj.getStateValue ());
  }

  public void testLock ()
  {
    String lockKey = new String ();
    mTestObj.setStateValue (TestState.TEST_STATE_OFF);
    mTestObj.lock (lockKey);
    // locking again with the same key is silently ignored
    mTestObj.lock (lockKey);
    mTestObj.setStateValue (TestState.TEST_STATE_ON);
  }

  @Test (expected = IllegalStateException.class)
  public void testLock_Fail ()
  {
    mTestObj.setStateValue (TestState.TEST_STATE_OFF);
    mTestObj.lock (new String ());
    // try to lock again with a different key, this will fail
    mTestObj.lock (new String ());
  }

  @Test (expected = IllegalArgumentException.class)
  public void testSetStateWithWrongKey ()
  {
    mTestObj.setStateValue (TestState.TEST_STATE_OFF);
    mTestObj.lock (new String ());
    mTestObj.setStateValue (TestState.TEST_STATE_ON, new Object ());
  }

  @Test
  public void testSetStateWithLock ()
  {
    String lockKey = new String ();
    mTestObj.setStateValue (TestState.TEST_STATE_OFF);
    mTestObj.lock (lockKey);
    mTestObj.setStateValue (TestState.TEST_STATE_ON, lockKey);
    assertEquals (TestState.TEST_STATE_ON, mTestObj.getStateValue ());
  }

  @Test (expected = IllegalStateException.class)
  public void testSetStateWithLock_Fail ()
  {
    mTestObj.setStateValue (TestState.TEST_STATE_OFF);
    mTestObj.lock (new String ());
    mTestObj.setStateValue (TestState.TEST_STATE_ON);
  }

  @Test
  public void testUnlock ()
  {
    String lockKey = new String ();
    mTestObj.setStateValue (TestState.TEST_STATE_OFF);
    mTestObj.lock (lockKey);
    assertTrue (mTestObj.isLocked ());
    mTestObj.unlock (lockKey);
    assertFalse (mTestObj.isLocked ());
  }

  @Test (expected = IllegalArgumentException.class)
  public void testUnlock_Fail ()
  {
    mTestObj.setStateValue (TestState.TEST_STATE_OFF);
    mTestObj.lock (new String ());
    // try to unlock with a different key, this will fail
    mTestObj.unlock (new String ());
  }

  @Test
  public void testToString ()
  {
    assertEquals ("null", mTestObj.toString ());
    mTestObj.setStateValue (TestState.TEST_STATE_OFF);
    assertEquals ("TEST_STATE_OFF", mTestObj.toString ());
  }
}
