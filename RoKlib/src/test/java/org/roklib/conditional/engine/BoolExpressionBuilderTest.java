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
package org.roklib.conditional.engine;

import org.junit.Test;
import org.roklib.conditional.bool.*;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BoolExpressionBuilderTest {
    @Test
    public void testCreateANDExpression() {
        Condition c1 = new Condition("c1", false);
        Condition c2 = new Condition("c2", false);
        Condition c3 = new Condition("c3", false);
        BooleanExpression expression = BoolExpressionBuilder.createANDExpression(c1, c2, c3);
        assertTrue(expression.getBooleanOperation() instanceof AndOperation);
        assertFalse(expression.getBooleanValue());
        c1.setValue(true);
        assertFalse(expression.getBooleanValue());
        c2.setValue(true);
        assertFalse(expression.getBooleanValue());
        c3.setValue(true);
        assertTrue(expression.getBooleanValue());
    }

    @Test
    public void testCreateORExpression() {
        Condition c1 = new Condition("c1", true);
        Condition c2 = new Condition("c2", true);
        Condition c3 = new Condition("c3", true);
        BooleanExpression expression = BoolExpressionBuilder.createORExpression(c1, c2, c3);
        assertTrue(expression.getBooleanOperation() instanceof OrOperation);
        assertTrue(expression.getBooleanValue());
        c1.setValue(false);
        assertTrue(expression.getBooleanValue());
        c2.setValue(false);
        assertTrue(expression.getBooleanValue());
        c3.setValue(false);
        assertFalse(expression.getBooleanValue());
    }

    @Test
    public void testCreateXORExpression() {
        Condition c1 = new Condition("c1", true);
        Condition c2 = new Condition("c2", true);
        BooleanExpression expression = BoolExpressionBuilder.createXORExpression(c1, c2);
        assertTrue(expression.getBooleanOperation() instanceof XOROperation);
        assertFalse(expression.getBooleanValue());
        c1.setValue(false);
        assertTrue(expression.getBooleanValue());
        c2.setValue(false);
        assertFalse(expression.getBooleanValue());
    }

    @Test
    public void testCreateNOTExpression() {
        Condition c1 = new Condition("c1", true);
        BooleanExpression expression = BoolExpressionBuilder.createNOTExpression(c1);
        assertTrue(expression.getBooleanOperation() instanceof NotOperation);
        assertFalse(expression.getBooleanValue());
        c1.setValue(false);
        assertTrue(expression.getBooleanValue());
    }

    @Test
    public void testCreateIDENTITYExpression() {
        Condition c1 = new Condition("c1", true);
        BooleanExpression expression = BoolExpressionBuilder.createIDENTITYExpression(c1);
        assertTrue(expression.getBooleanOperation() instanceof IdentityOperation);
        assertTrue(expression.getBooleanValue());
        c1.setValue(false);
        assertFalse(expression.getBooleanValue());
    }
}
