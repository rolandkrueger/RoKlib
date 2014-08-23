/*
 * Copyright (C) 2007 Roland Krueger
 * Created on 11.12.2009
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
package org.roklib.data;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.roklib.data.ManagedData;
import org.roklib.data.ManagedData.StatusEnum;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

public class ManagedDataTest
{
  private ManagedData<String> mValue;

  @Before
  public void setUp ()
  {
    mValue = new ManagedData<String> ();
  }

  @Test
  public void testDefaultConstructor ()
  {
    assertTrue (mValue.getState () == StatusEnum.UNDEFINED);
    assertFalse (mValue.canRead ());
    assertTrue (mValue.isUndefined ());
  }

  @Test
  public void testUnset ()
  {
    mValue.setValue ("test");
    mValue.unset ();
    assertTrue (mValue.isDeleted ());
    assertFalse (mValue.canRead ());
  }

  @Test
  public void testEquals ()
  {
    mValue.setValue ("test");
    assertTrue (mValue.equals (mValue));
    ManagedData<String> testValue = new ManagedData<String> ();
    testValue.setValue ("test");
    assertTrue (mValue.equals (testValue));
    assertTrue (testValue.equals (mValue));

    ManagedData<Integer> intTestValue1 = new ManagedData<Integer> (17);
    ManagedData<Integer> intTestValue2 = new ManagedData<Integer> (17);

    assertTrue (intTestValue1.equals (intTestValue2));

    ManagedData<String> otherValue = new ManagedData<String> ();
    otherValue.setValue ("---");
    assertFalse (mValue.equals (otherValue));
    assertFalse (otherValue.equals (mValue));
  }

  @Test (expected = IllegalStateException.class)
  public void testGetValueFail ()
  {
    mValue.getValue ();
  }

  @Test (expected = IllegalStateException.class)
  public void testGetValueFailsAfterDeletingIt ()
  {
    mValue.setValue ("...");
    mValue.unset ();
    mValue.getValue ();
  }

  @Test
  public void testValueConstructor ()
  {
    ManagedData<Integer> obj = new ManagedData<Integer> (17);
    assertTrue (obj.getState () == StatusEnum.SET);
    assertTrue (obj.isSet ());
    assertThat(obj.getValue(), equalTo(17));
  }

  @Test
  public void testReset ()
  {
    mValue.setValue ("firstValue");
    mValue.setValue ("secondValue");
    assertTrue (mValue.isChanged ());
    mValue.reset ();
    assertTrue (mValue.isSet ());
    assertEquals ("secondValue", mValue.getValue ());
  }

  @Test
  public void testResetWithDelete ()
  {
    mValue.setValue ("firstValue");
    mValue.setValue ("secondValue");
    mValue.unset ();
    mValue.reset (); // reset() is without effect if state is 'deleted'
    assertTrue (mValue.isDeleted ());
  }

  @Test
  public void testSetValue ()
  {
    assertTrue (mValue.isUndefined ());
    mValue.setValue ("firstValue");
    assertTrue (mValue.canRead ());
    assertTrue (mValue.isSet ());
    mValue.setValue ("secondValue");
    assertTrue (mValue.isChanged ());
    assertTrue (mValue.canRead ());
  }

  @Test (expected = IllegalArgumentException.class)
  public void testSetValueWithNull ()
  {
    mValue.setValue ("firstValue");
    mValue.setValue (null);
  }

  @Test
  public void testSetLockedValue ()
  {
    Object lockKey = new Object ();
    mValue.lock (lockKey);
    mValue.setValue ("", lockKey);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testSetLockedValueFail2 ()
  {
    Object lockKey = new Object ();
    mValue.lock (lockKey);
    mValue.setValue ("", this);
  }

  @Test
  public void testLock ()
  {
    assertFalse (mValue.isLocked ());
    mValue.lock (this);
    assertTrue (mValue.isLocked ());
  }

  @Test
  public void testUnlock ()
  {
    assertFalse (mValue.isLocked ());
    mValue.lock (this);
    assertTrue (mValue.isLocked ());
    mValue.unlock (this);
    assertFalse (mValue.isLocked ());
  }

  @Test (expected = IllegalStateException.class)
  public void testSetLockedValueFail ()
  {
    mValue.lock (this);
    mValue.setValue ("");
  }

  @Test (expected = IllegalArgumentException.class)
  public void testLockWithNullKey ()
  {
    mValue.lock (null);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testUnlockFail ()
  {
    mValue.lock (this);
    mValue.unlock (new Object ());
  }

  @Test (expected = IllegalStateException.class)
  public void testUnlock_NotLocked ()
  {
    mValue.unlock (new Object ());
  }

  @Test (expected = IllegalStateException.class)
  public void testSetValue_NotLocked ()
  {
    mValue.setValue ("", new Object ());
  }

  @Test
  public void testHashCode ()
  {
    String value = "someValue";
    mValue.setValue (value);
    assertEquals (value.hashCode (), mValue.hashCode ());
  }
}