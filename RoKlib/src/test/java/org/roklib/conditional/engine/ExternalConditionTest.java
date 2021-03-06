/*
 * Copyright (C) 2007 Roland Krueger
 * Created on 09.02.2010
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
import org.roklib.conditional.engine.conditions.HasState;
import org.roklib.webapps.state.GenericUserState;

import static org.junit.Assert.*;

public class ExternalConditionTest {

    @Test
    public void testGetBooleanValue() {
        GenericUserState state = new GenericUserState();
        state.setStateValue(GenericUserState.DEACTIVATED);
        HasState<GenericUserState> hasState = new HasState<GenericUserState>(state, GenericUserState.REGISTERED);
        ExternalCondition condition = new ExternalCondition(hasState);
        assertFalse(condition.getBooleanValue());
        state.setStateValue(GenericUserState.REGISTERED);
        assertTrue(condition.getBooleanValue());
    }

}
