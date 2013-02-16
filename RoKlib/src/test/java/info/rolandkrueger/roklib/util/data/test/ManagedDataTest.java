/*
 * $Id: ManagedDataTest.java 246 2011-01-19 17:03:10Z roland $
 * Copyright (C) 2007 Roland Krueger
 * Created on 11.12.2009
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
package info.rolandkrueger.roklib.util.data.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import info.rolandkrueger.roklib.util.data.ManagedData;
import info.rolandkrueger.roklib.util.data.ManagedData.StatusEnum;

import org.junit.Before;
import org.junit.Test;

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
    assertEquals (17, obj.getValue ());
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

  @Test (expected=IllegalArgumentException.class)
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