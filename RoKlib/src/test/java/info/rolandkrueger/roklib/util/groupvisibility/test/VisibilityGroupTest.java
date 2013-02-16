/*
 * $Id: VisibilityGroupTest.java 181 2010-11-01 09:39:13Z roland $
 * Copyright (C) 2007 Roland Krueger
 * Created on 19.10.2009
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
package info.rolandkrueger.roklib.util.groupvisibility.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import info.rolandkrueger.roklib.util.groupvisibility.VisibilityGroup;

import org.junit.Before;
import org.junit.Test;

public class VisibilityGroupTest
{
  private VisibilityGroup mTestObj;

  @Before
  public void setUp ()
  {
    mTestObj = new VisibilityGroup ("group");
  }

  @Test
  public void testAddVisibilityEnablingConfigurable ()
  {
    VisibilityEnablingConfigurableTestClass testObj = new VisibilityEnablingConfigurableTestClass ();
    mTestObj.addVisibilityEnablingConfigurable (new VisibilityEnablingConfigurableTestClass ());
    mTestObj.addVisibilityEnablingConfigurable (testObj);
    mTestObj.addVisibilityEnablingConfigurable (testObj);

    assertEquals (2, mTestObj.getSize ());
  }

  @Test
  public void testRemoveVisibilityEnablingConfigurable ()
  {
    VisibilityEnablingConfigurableTestClass testObj = new VisibilityEnablingConfigurableTestClass ();
    mTestObj.addVisibilityEnablingConfigurable (testObj);
    assertEquals (1, mTestObj.getSize ());
    boolean result = mTestObj.removeVisibilityEnablingConfigurable (testObj);
    assertEquals (0, mTestObj.getSize ());
    assertTrue (result);
  }

  @Test
  public void testSetVisible ()
  {
    VisibilityEnablingConfigurableTestClass testObj1 = new VisibilityEnablingConfigurableTestClass ();
    VisibilityEnablingConfigurableTestClass testObj2 = new VisibilityEnablingConfigurableTestClass ();
    mTestObj.addVisibilityEnablingConfigurable (testObj1);
    mTestObj.addVisibilityEnablingConfigurable (testObj2);
    assertTrue (testObj1.isEnabled ());
    assertTrue (testObj2.isEnabled ());
    assertTrue (testObj1.isVisible ());
    assertTrue (testObj2.isVisible ());
    assertTrue (mTestObj.isEnabled ());
    assertTrue (mTestObj.isVisible ());

    mTestObj.setVisible (false);
    assertTrue (testObj1.isEnabled ());
    assertTrue (testObj2.isEnabled ());
    assertFalse (testObj1.isVisible ());
    assertFalse (testObj2.isVisible ());
    assertTrue (mTestObj.isEnabled ());
    assertFalse (mTestObj.isVisible ());
  }

  @Test
  public void testSetEnabled ()
  {
    VisibilityEnablingConfigurableTestClass testObj1 = new VisibilityEnablingConfigurableTestClass ();
    VisibilityEnablingConfigurableTestClass testObj2 = new VisibilityEnablingConfigurableTestClass ();
    mTestObj.addVisibilityEnablingConfigurable (testObj1);
    mTestObj.addVisibilityEnablingConfigurable (testObj2);
    assertTrue (testObj1.isEnabled ());
    assertTrue (testObj2.isEnabled ());
    assertTrue (testObj1.isVisible ());
    assertTrue (testObj2.isVisible ());
    assertTrue (mTestObj.isEnabled ());
    assertTrue (mTestObj.isVisible ());

    mTestObj.setEnabled (false);
    assertFalse (testObj1.isEnabled ());
    assertFalse (testObj2.isEnabled ());
    assertTrue (testObj1.isVisible ());
    assertTrue (testObj2.isVisible ());
    assertFalse (mTestObj.isEnabled ());
    assertTrue (mTestObj.isVisible ());
  }

  @Test
  public void testIsEnabled ()
  {
    assertTrue (mTestObj.isEnabled ());
    mTestObj.setEnabled (false);
    assertFalse (mTestObj.isEnabled ());
    mTestObj.setEnabled (true);
    assertTrue (mTestObj.isEnabled ());
  }

  @Test
  public void testIsVisible ()
  {
    assertTrue (mTestObj.isVisible ());
    mTestObj.setVisible (false);
    assertFalse (mTestObj.isVisible ());
    mTestObj.setVisible (true);
    assertTrue (mTestObj.isVisible ());
  }

  @Test
  public void testGetName ()
  {
    assertEquals ("group", mTestObj.getName ());
  }
}
