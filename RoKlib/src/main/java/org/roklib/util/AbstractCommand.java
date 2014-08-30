/*
 * 
 * Copyright (C) 2007 Roland Krueger 
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

import org.roklib.data.EnhancedReturnType;

public abstract class AbstractCommand<T_ResultType> implements Runnable {
    private EnhancedReturnType<T_ResultType> resultValue;

    private boolean wasExecuted;

    private boolean executionCanceled;

    public AbstractCommand() {
        resultValue = null;
        wasExecuted = false;
        executionCanceled = false;
    }

    protected abstract EnhancedReturnType<T_ResultType> executeImpl();

    public void run() {
        resultValue = null;
        EnhancedReturnType<T_ResultType> result = executeImpl();
        if (result == null) {
            throw new NullPointerException("Return object returned from command implementation object is null.");
        }

        resultValue = result;
        wasExecuted = true;
    }

    public boolean wasExecuted() {
        return wasExecuted;
    }

    public final EnhancedReturnType<T_ResultType> getResult() {
        if (!wasExecuted()) {
            throw new IllegalStateException("Command was not yet executed. Call run() first.");
        }

        if (resultValue == null) {
            throw new IllegalStateException("Invalid result object: Subclass of " + AbstractCommand.class.getName()
                    + " failed to correctly configure command result.");
        }

        return resultValue;
    }

    public final T_ResultType getResultValue() {
        return getResult().getValue();
    }

    public void cancel() {
        executionCanceled = true;
    }

    public boolean wasCanceled() {
        return executionCanceled;
    }
}
