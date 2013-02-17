/*
 * Copyright (C) 2007 Roland Krueger
 * Created on 16.07.2007
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
