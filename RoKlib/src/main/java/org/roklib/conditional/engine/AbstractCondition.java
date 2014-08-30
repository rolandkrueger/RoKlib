/*
 * Copyright (C) 2007 Roland Krueger
 * Created on 20.10.2009
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


import org.roklib.conditional.bool.BooleanValueProvider;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public abstract class AbstractCondition implements BooleanValueProvider {
    private static final long serialVersionUID = 6265626147474145199L;

    private List<ConditionListener> listeners;

    public void addConditionListener(ConditionListener listener) {
        if (listeners == null)
            listeners = new LinkedList<ConditionListener>();
        listeners.add(listener);
    }

    public void removeConditionListener(ConditionListener listener) {
        if (listeners == null)
            return;
        listeners.remove(listener);
    }

    protected void fireConditionChanged() {
        if (listeners == null)
            return;
        for (ConditionListener listener : new ArrayList<ConditionListener>(listeners)) {
            listener.conditionChanged(this);
        }
    }

    protected int getListenerCount() {
        if (listeners == null)
            return 0;
        return listeners.size();
    }

    public void clearConditionListeners() {
        if (listeners == null)
            return;
        listeners.clear();
    }
}
