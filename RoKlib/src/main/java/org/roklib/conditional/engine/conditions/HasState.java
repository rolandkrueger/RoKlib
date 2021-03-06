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
package org.roklib.conditional.engine.conditions;

import org.roklib.conditional.bool.BooleanValueProvider;
import org.roklib.state.State;
import org.roklib.state.State.StateValue;
import org.roklib.util.helper.CheckForNull;

public class HasState<S extends State<?>> implements BooleanValueProvider {
    private static final long serialVersionUID = -232946455411913695L;

    private final State<S> state;
    private final StateValue<S> stateValue;

    public HasState(State<S> state, StateValue<S> stateValue) {
        CheckForNull.check(state, stateValue);
        this.state = state;
        this.stateValue = stateValue;
    }

    public boolean getBooleanValue() {
        return state.hasState(stateValue);
    }
}
