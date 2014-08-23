/*
 * Copyright (C) 2007 Roland Krueger
 * Created on 21.10.2009
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
package org.roklib.conditional.bool;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.roklib.conditional.bool.OrOperation;

public class OrOperationTest
{
  private OrOperation mTestObj;

  @Before
  public void setUp ()
  {
    mTestObj = new OrOperation ();
    mTestObj.setLeftHandOperand (false);
    mTestObj.setRightHandOperand (false);
  }

  @Test
  public void test ()
  {
    assertFalse (mTestObj.execute ());
    mTestObj.setLeftHandOperand (true);
    assertTrue (mTestObj.execute ());
    mTestObj.setRightHandOperand (true);
    assertTrue (mTestObj.execute ());
    mTestObj.setLeftHandOperand (false);
    assertTrue (mTestObj.execute ());
  }

  @Test
  public void testCanShortCircuit ()
  {
    assertFalse (mTestObj.canShortCircuit (false));
    assertTrue (mTestObj.canShortCircuit (true));
  }

  @Test
  public void testGetShortCircuit ()
  {
    assertTrue (mTestObj.getShortCircuit (true));
  }

  @Test (expected = IllegalStateException.class)
  public void testGetShortCircuit_Fail ()
  {
    mTestObj.getShortCircuit (false);
  }

  @Test
  public void testIsUnaryOperation ()
  {
    assertFalse (mTestObj.isUnaryOperation ());
  }

  @Test
  public void testGetMinimumNumberOfOperands ()
  {
    assertEquals (2, mTestObj.getMinimumNumberOfOperands ());
  }
}
