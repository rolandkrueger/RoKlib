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
package org.roklib.util.test;


import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.roklib.collections.tstmap.TernarySearchTreeMapTests;
import org.roklib.conditional.bool.test.AndOperationTest;
import org.roklib.conditional.bool.test.IdentityOperationTest;
import org.roklib.conditional.bool.test.NotOperationTest;
import org.roklib.conditional.bool.test.OrOperationTest;
import org.roklib.conditional.bool.test.XOROperationTest;
import org.roklib.conditional.engine.conditions.test.HasStateTest;
import org.roklib.conditional.engine.test.AbstractConditionTest;
import org.roklib.conditional.engine.test.BoolExpressionBuilderTest;
import org.roklib.conditional.engine.test.BooleanOperationTest;
import org.roklib.conditional.engine.test.ConditionTest;
import org.roklib.conditional.engine.test.ExternalConditionTest;
import org.roklib.conditional.groupvisibility.test.RadioComponentGroupManagerTest;
import org.roklib.conditional.groupvisibility.test.VisibilityGroupManagerTest;
import org.roklib.conditional.groupvisibility.test.VisibilityGroupTest;
import org.roklib.data.test.ManagedDataTest;
import org.roklib.util.authorization.test.AdmissionTicketContainerTest;
import org.roklib.util.helper.test.CheckForNullTest;
import org.roklib.util.helper.test.StopWatchTest;
import org.roklib.util.state.test.StateTest;

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
