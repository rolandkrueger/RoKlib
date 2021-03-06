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
package org.roklib.conditional.bool;

public abstract class AbstractUnaryBooleanOperation implements BooleanOperation {
    private static final long serialVersionUID = 3671470052740592370L;

    protected boolean value;

    public final void setLeftHandOperand(boolean left) {
        value = left;
    }

    public final void setRightHandOperand(boolean right) {
        setLeftHandOperand(right);
    }

    public final boolean canShortCircuit(boolean firstOperand) {
        return false;
    }

    public final boolean getShortCircuit(boolean firstOperand) {
        throw new IllegalStateException("Cannot short-circuit.");
    }

    public final int getMinimumNumberOfOperands() {
        return 1;
    }

    public final boolean isUnaryOperation() {
        return true;
    }
}
