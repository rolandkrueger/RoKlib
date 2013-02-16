/*
 * $Id: BoolExpressionBuilderTest.java 181 2010-11-01 09:39:13Z roland $
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
package info.rolandkrueger.roklib.util.conditionalengine.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import info.rolandkrueger.roklib.util.bool.AndOperation;
import info.rolandkrueger.roklib.util.bool.IdentityOperation;
import info.rolandkrueger.roklib.util.bool.NotOperation;
import info.rolandkrueger.roklib.util.bool.OrOperation;
import info.rolandkrueger.roklib.util.bool.XOROperation;
import info.rolandkrueger.roklib.util.conditionalengine.BoolExpressionBuilder;
import info.rolandkrueger.roklib.util.conditionalengine.BooleanExpression;
import info.rolandkrueger.roklib.util.conditionalengine.Condition;

import org.junit.Test;

public class BoolExpressionBuilderTest
{
  @Test
  public void testCreateANDExpression ()
  {
    Condition c1 = new Condition ("c1", false);
    Condition c2 = new Condition ("c2", false);
    Condition c3 = new Condition ("c3", false);
    BooleanExpression expression = BoolExpressionBuilder.createANDExpression (c1, c2, c3);
    assertTrue (expression.getBooleanOperation () instanceof AndOperation);
    assertFalse (expression.getBooleanValue ());
    c1.setValue (true);
    assertFalse (expression.getBooleanValue ());
    c2.setValue (true);
    assertFalse (expression.getBooleanValue ());
    c3.setValue (true);
    assertTrue (expression.getBooleanValue ());
  }

  @Test
  public void testCreateORExpression ()
  {
    Condition c1 = new Condition ("c1", true);
    Condition c2 = new Condition ("c2", true);
    Condition c3 = new Condition ("c3", true);
    BooleanExpression expression = BoolExpressionBuilder.createORExpression (c1, c2, c3);
    assertTrue (expression.getBooleanOperation () instanceof OrOperation);
    assertTrue (expression.getBooleanValue ());
    c1.setValue (false);
    assertTrue (expression.getBooleanValue ());
    c2.setValue (false);
    assertTrue (expression.getBooleanValue ());
    c3.setValue (false);
    assertFalse (expression.getBooleanValue ());
  }

  @Test
  public void testCreateXORExpression ()
  {
    Condition c1 = new Condition ("c1", true);
    Condition c2 = new Condition ("c2", true);
    BooleanExpression expression = BoolExpressionBuilder.createXORExpression (c1, c2);
    assertTrue (expression.getBooleanOperation () instanceof XOROperation);
    assertFalse (expression.getBooleanValue ());
    c1.setValue (false);
    assertTrue (expression.getBooleanValue ());
    c2.setValue (false);
    assertFalse (expression.getBooleanValue ());
  }

  @Test
  public void testCreateNOTExpression ()
  {
    Condition c1 = new Condition ("c1", true);
    BooleanExpression expression = BoolExpressionBuilder.createNOTExpression (c1);
    assertTrue (expression.getBooleanOperation () instanceof NotOperation);
    assertFalse (expression.getBooleanValue ());
    c1.setValue (false);
    assertTrue (expression.getBooleanValue ());
  }

  @Test
  public void testCreateIDENTITYExpression ()
  {
    Condition c1 = new Condition ("c1", true);
    BooleanExpression expression = BoolExpressionBuilder.createIDENTITYExpression (c1);
    assertTrue (expression.getBooleanOperation () instanceof IdentityOperation);
    assertTrue (expression.getBooleanValue ());
    c1.setValue (false);
    assertFalse (expression.getBooleanValue ());
  }
}
