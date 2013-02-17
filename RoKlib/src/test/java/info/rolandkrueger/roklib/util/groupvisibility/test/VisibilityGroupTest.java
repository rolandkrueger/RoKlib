/*
 * Copyright (C) 2007 Roland Krueger
 * Created on 19.10.2009
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
package info.rolandkrueger.roklib.util.groupvisibility.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.roklib.util.groupvisibility.VisibilityGroup;

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
