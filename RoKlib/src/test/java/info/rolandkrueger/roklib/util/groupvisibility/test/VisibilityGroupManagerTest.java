/*
 * $Id: VisibilityGroupManagerTest.java 181 2010-11-01 09:39:13Z roland $
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
import info.rolandkrueger.roklib.util.groupvisibility.VisibilityGroupManager;

import org.junit.Before;
import org.junit.Test;

public class VisibilityGroupManagerTest
{
  private VisibilityGroupManager mTestObj;
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
