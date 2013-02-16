/*
 * $Id: UtilsTests.java 260 2011-01-27 19:51:26Z roland $
 * Copyright (C) 2007 Roland Krueger
 * Created on 16.07.2007
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
package info.rolandkrueger.roklib.util.test;

import info.rolandkrueger.roklib.util.authorization.test.AdmissionTicketContainerTest;
import info.rolandkrueger.roklib.util.bool.test.AndOperationTest;
import info.rolandkrueger.roklib.util.bool.test.IdentityOperationTest;
import info.rolandkrueger.roklib.util.bool.test.NotOperationTest;
import info.rolandkrueger.roklib.util.bool.test.OrOperationTest;
import info.rolandkrueger.roklib.util.bool.test.XOROperationTest;
import info.rolandkrueger.roklib.util.conditionalengine.conditions.test.HasStateTest;
import info.rolandkrueger.roklib.util.conditionalengine.test.AbstractConditionTest;
import info.rolandkrueger.roklib.util.conditionalengine.test.BoolExpressionBuilderTest;
import info.rolandkrueger.roklib.util.conditionalengine.test.BooleanOperationTest;
import info.rolandkrueger.roklib.util.conditionalengine.test.ConditionTest;
import info.rolandkrueger.roklib.util.conditionalengine.test.ExternalConditionTest;
import info.rolandkrueger.roklib.util.data.test.ManagedDataTest;
import info.rolandkrueger.roklib.util.groupvisibility.test.RadioComponentGroupManagerTest;
import info.rolandkrueger.roklib.util.groupvisibility.test.VisibilityGroupManagerTest;
import info.rolandkrueger.roklib.util.groupvisibility.test.VisibilityGroupTest;
import info.rolandkrueger.roklib.util.helper.test.CheckForNullTest;
import info.rolandkrueger.roklib.util.helper.test.StopWatchTest;
import info.rolandkrueger.roklib.util.state.test.StateTest;
import info.rolandkrueger.roklib.util.tstmap.TernarySearchTreeMapTests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith (Suite.class)
@SuiteClasses ({ TernarySearchTreeMapTests.class, StopWatchTest.class, CheckForNullTest.class,
    VisibilityGroupManagerTest.class, VisibilityGroupTest.class, AbstractConditionTest.class, ConditionTest.class,
    BooleanOperationTest.class, BoolExpressionBuilderTest.class, AndOperationTest.class, OrOperationTest.class,
    XOROperationTest.class, NotOperationTest.class, IdentityOperationTest.class, ManagedDataTest.class,
    StateTest.class, HasStateTest.class, ExternalConditionTest.class, AdmissionTicketContainerTest.class,
    RadioComponentGroupManagerTest.class, EnhancedReturnTypeTest.class, AbstractCommandTest.class })
public class UtilsTests
{
}
