/*
 * Copyright (C) 2007 Roland Krueger
 * Created on 29.01.2010
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
package org.roklib.state;

import org.roklib.util.helper.CheckForNull;

import java.io.Serializable;

public class State<S extends State<?>> implements Serializable {
    private static final long serialVersionUID = 6243348683850423328L;

    private Serializable lockKey;
    private StateValue<S> currentState;
    private StateValue<S> defaultState;

    public static class StateValue<S extends State<?>> implements Serializable {
        private static final long serialVersionUID = -1916548888416932116L;
        private final String mName;

        public StateValue(String name) {
            mName = name;
        }

        public String getName() {
            return mName;
        }

        @SuppressWarnings("rawtypes")
        @Override
        public boolean equals(Object obj) {
            if (obj == null)
                return false;
            if (obj == this)
                return true;
            if (obj instanceof StateValue) {
                StateValue other = (StateValue) obj;
                return other.mName.equals(mName);
            }
            return false;
        }

        @Override
        public int hashCode() {
            return mName.hashCode();
        }
    }

    public State() {
    }

    public State(StateValue<S> defaultState) {
        currentState = defaultState;
        this.defaultState = defaultState;
    }

    public boolean hasState(StateValue<S> state) {
        return state == null && currentState == null || state != null && currentState.equals(state);

    }

    public void setStateValue(StateValue<S> state) {
        if (lockKey != null)
            throw new IllegalStateException(
                    "Cannot set status: object is locked. Use setState with the correct key instead.");
        currentState = state;
    }

    public void setStateValue(StateValue<S> state, Object lockKey) {
        if (this.lockKey != null && this.lockKey != lockKey)
            throw new IllegalArgumentException("Unlock failed: wrong key.");
        currentState = state;
    }

    public StateValue<S> getStateValue() {
        return currentState;
    }

    public void reset() {
        currentState = defaultState;
    }

    public void lock(Serializable lockKey) {
        CheckForNull.check(lockKey);
        if (this.lockKey != null && this.lockKey != lockKey)
            throw new IllegalStateException("Already locked. Cannot lock again with a different key.");
        this.lockKey = lockKey;
    }

    public void unlock(Object lockKey) {
        if (this.lockKey == null)
            return;
        if (lockKey != this.lockKey)
            throw new IllegalArgumentException("Unlock failed: wrong key.");
        this.lockKey = null;
    }

    public boolean isLocked() {
        return lockKey != null;
    }

    @Override
    public String toString() {
        if (currentState == null)
            return "null";
        return currentState.mName;
    }

    @Override
    public int hashCode() {
        if (currentState == null)
            return -1;
        return currentState.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (obj == this)
            return true;
        if (currentState == null)
            return false;
        if (obj instanceof State) {
            @SuppressWarnings("rawtypes")
            State other = (State) obj;
            return other.currentState != null && other.currentState.equals(currentState);
        }

        return false;
    }
}
