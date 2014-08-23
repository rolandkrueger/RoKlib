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
package org.roklib.util;


import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.roklib.collections.tstmap.TernarySearchTreeMapTests;
import org.roklib.conditional.bool.AndOperationTest;
import org.roklib.conditional.bool.IdentityOperationTest;
import org.roklib.conditional.bool.NotOperationTest;
import org.roklib.conditional.bool.OrOperationTest;
import org.roklib.conditional.bool.XOROperationTest;
import org.roklib.conditional.engine.conditions.HasStateTest;
import org.roklib.conditional.engine.AbstractConditionTest;
import org.roklib.conditional.engine.BoolExpressionBuilderTest;
import org.roklib.conditional.engine.BooleanOperationTest;
import org.roklib.conditional.engine.ConditionTest;
import org.roklib.conditional.engine.ExternalConditionTest;
import org.roklib.conditional.groupvisibility.RadioComponentGroupManagerTest;
import org.roklib.conditional.groupvisibility.VisibilityGroupManagerTest;
import org.roklib.conditional.groupvisibility.VisibilityGroupTest;
import org.roklib.data.ManagedDataTest;
import org.roklib.state.StateTest;
import org.roklib.util.authorization.AdmissionTicketContainerTest;
import org.roklib.util.helper.CheckForNullTest;
import org.roklib.util.helper.StopWatchTest;

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
