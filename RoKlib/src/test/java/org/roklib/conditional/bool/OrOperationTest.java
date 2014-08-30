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

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class OrOperationTest {
    private OrOperation orOperation;

    @Before
    public void setUp() {
        orOperation = new OrOperation();
        orOperation.setLeftHandOperand(false);
        orOperation.setRightHandOperand(false);
    }

    @Test
    public void test() {
        assertFalse(orOperation.execute());
        orOperation.setLeftHandOperand(true);
        assertTrue(orOperation.execute());
        orOperation.setRightHandOperand(true);
        assertTrue(orOperation.execute());
        orOperation.setLeftHandOperand(false);
        assertTrue(orOperation.execute());
    }

    @Test
    public void testCanShortCircuit() {
        assertFalse(orOperation.canShortCircuit(false));
        assertTrue(orOperation.canShortCircuit(true));
    }

    @Test
    public void testGetShortCircuit() {
        assertTrue(orOperation.getShortCircuit(true));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetShortCircuit_Fail() {
        orOperation.getShortCircuit(false);
    }

    @Test
    public void testIsUnaryOperation() {
        assertFalse(orOperation.isUnaryOperation());
    }

    @Test
    public void testGetMinimumNumberOfOperands() {
        assertEquals(2, orOperation.getMinimumNumberOfOperands());
    }
}
