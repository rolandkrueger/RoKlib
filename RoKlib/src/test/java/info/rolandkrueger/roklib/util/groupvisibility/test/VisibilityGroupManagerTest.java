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
import info.rolandkrueger.roklib.util.groupvisibility.VisibilityGroupManager;

import org.junit.Before;
import org.junit.Test;

public class VisibilityGroupManagerTest
{
  private VisibilityGroupManager                  mTestObj;
  private VisibilityEnablingConfigurableTestClass mGroupMember1 = new VisibilityEnablingConfigurableTestClass ();
  private VisibilityEnablingConfigurableTestClass mGroupMember2 = new VisibilityEnablingConfigurableTestClass ();

  @Before
  public void setUp ()
  {
    mTestObj = new VisibilityGroupManager ();
    mTestObj.addGroupMember ("group1", mGroupMember1);
    mTestObj.addGroupMember ("group2", mGroupMember2);

  }

  @Test
  public void testAddGroupMember ()
  {
    assertEquals (2, mTestObj.getGroupCount ());
    assertTrue (mTestObj.getVisibilityGroup ("group1").getGroupMembers ().contains (mGroupMember1));
    assertTrue (mTestObj.getVisibilityGroup ("group2").getGroupMembers ().contains (mGroupMember2));
    assertFalse (mTestObj.getVisibilityGroup ("group2").getGroupMembers ().contains (mGroupMember1));
    assertFalse (mTestObj.getVisibilityGroup ("group1").getGroupMembers ().contains (mGroupMember2));
  }

  @Test
  public void testSetGroupVisible ()
  {
    assertTrue (mTestObj.getVisibilityGroup ("group2").isVisible ());
    assertTrue (mTestObj.setGroupVisible ("group2", false));
    assertFalse (mTestObj.getVisibilityGroup ("group2").isVisible ());
    mTestObj.setGroupVisible ("group2", true);
    assertTrue (mTestObj.getVisibilityGroup ("group2").isVisible ());

    assertFalse (mTestObj.setGroupVisible ("non-existing group", false));
  }

  @Test
  public void testSetGroupEnabled ()
  {
    assertTrue (mTestObj.getVisibilityGroup ("group2").isEnabled ());
    assertTrue (mTestObj.setGroupEnabled ("group2", false));
    assertFalse (mTestObj.getVisibilityGroup ("group2").isEnabled ());
    mTestObj.setGroupEnabled ("group2", true);
    assertTrue (mTestObj.getVisibilityGroup ("group2").isEnabled ());

    assertFalse (mTestObj.setGroupEnabled ("non-existing group", false));
  }
}
