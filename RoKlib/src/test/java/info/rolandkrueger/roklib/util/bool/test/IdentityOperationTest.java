/*
 * $Id: IdentityOperationTest.java 178 2010-10-31 18:01:20Z roland $
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
package info.rolandkrueger.roklib.util.bool.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import info.rolandkrueger.roklib.util.bool.IdentityOperation;

import org.junit.Before;
import org.junit.Test;

public class IdentityOperationTest
{
  private IdentityOperation mTestObj;

  @Before
  public void setUp ()
  {
    mTestObj = new IdentityOperation ();
    mTestObj.setLeftHandOperand (false);
  }

  @Test
  public void test ()
  {
    assertFalse (mTestObj.execute ());
    mTestObj.setLeftHandOperand (true);
    assertTrue (mTestObj.execute ());
  }

  @Test
  public void testCanShortCircuit ()
  {
    assertFalse (mTestObj.canShortCircuit (true));
    assertFalse (mTestObj.canShortCircuit (false));
  }

  @Test (expected = IllegalStateException.class)
  public void testGetShortCircuit ()
  {
    mTestObj.getShortCircuit (true);
  }

  @Test
  public void testIsUnaryOperation ()
  {
    assertTrue (mTestObj.isUnaryOperation ());
  }

  @Test
  public void testGetMinimumNumberOfOperands ()
  {
    assertEquals (1, mTestObj.getMinimumNumberOfOperands ());
  }
}
