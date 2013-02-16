/*
 * $Id: GenericUserTest.java 181 2010-11-01 09:39:13Z roland $
 * Copyright (C) 2007 Roland Krueger
 * Created on 27.01.2010
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
package info.rolandkrueger.roklib.webapps.data.usermgmt.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import info.rolandkrueger.roklib.webapps.data.usermgmt.GenericUser;
import info.rolandkrueger.roklib.webapps.data.usermgmt.UserRole;
import info.rolandkrueger.roklib.webapps.state.GenericUserState;

import org.junit.Before;
import org.junit.Test;

public class GenericUserTest
{
  private GenericUser<Long, String> mTestObj;

  @Before
  public void setUp ()
  {
    mTestObj = new GenericUser<Long, String> ();
  }

  @Test
  public void testGetUserRoles ()
  {
    assertTrue (mTestObj.getUserRoles ().isEmpty ());
    mTestObj.addRole (new UserRole<Long> (17L, "17"));
    assertEquals (1, mTestObj.getUserRoles ().size ());
  }

  @Test
  public void testAddRole ()
  {
    UserRole<Long> role1 = new UserRole<Long> (17L, "17");
    UserRole<Long> role2 = new UserRole<Long> (23L, "23");
    mTestObj.addRole (role1);
    mTestObj.addRole (role1);
    mTestObj.addRole (role2);
    assertEquals (2, mTestObj.getUserRoles ().size ());
  }

  @Test
  public void testHasRole ()
  {
    UserRole<Long> role1 = new UserRole<Long> (17L, "17");
    UserRole<Long> role2 = new UserRole<Long> (23L, "23");
    UserRole<Long> role3 = new UserRole<Long> (42L, "42");
    mTestObj.addRole (role1);
    mTestObj.addRole (role2);
    assertTrue (mTestObj.hasRole (role1));
    assertTrue (mTestObj.hasRole (role2));
    assertFalse (mTestObj.hasRole (role3));
  }

  @Test
  public void testSetState ()
  {
    assertFalse (mTestObj.getState ().hasState (GenericUserState.DEACTIVATED));
    mTestObj.setState (GenericUserState.DEACTIVATED);
    assertTrue (mTestObj.getState ().hasState (GenericUserState.DEACTIVATED));
  }

  @Test (expected = IllegalStateException.class)
  public void testSetStateExternal_Fail ()
  {
    mTestObj.getState ().setStateValue (GenericUserState.DEACTIVATED);
  }
}
