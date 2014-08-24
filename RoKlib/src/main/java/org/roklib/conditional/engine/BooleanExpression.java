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


import org.roklib.conditional.bool.IBooleanOperation;
import org.roklib.util.helper.CheckForNull;

import java.util.LinkedList;
import java.util.List;

public class BooleanExpression extends AbstractCondition implements IConditionListener {
    private static final long serialVersionUID = 3479069548966346585L;

    private IBooleanOperation mOperation;
    private List<AbstractCondition> mConditions;
    private Boolean mCurrentValue;

    public BooleanExpression(IBooleanOperation operation) {
        CheckForNull.check(operation);
        mOperation = operation;
        mCurrentValue = null;
    }

    public void conditionChanged(AbstractCondition source) {
        if (mCurrentValue == null)
            getBooleanValue();
        boolean previousValue = mCurrentValue;
        boolean value = getBooleanValue();
        if (value != previousValue) {
            fireConditionChanged();
        }
    }

    @Override
    public void addConditionListener(IConditionListener listener) {
        if (getListenerCount() == 0) {
            for (AbstractCondition condition : mConditions) {
                condition.addConditionListener(this);
            }
        }
        super.addConditionListener(listener);
    }

    @Override
    public void removeConditionListener(IConditionListener listener) {
        super.removeConditionListener(listener);
        if (getListenerCount() == 0) {
            for (AbstractCondition condition : mConditions) {
                condition.removeConditionListener(this);
            }
        }
    }

    public IBooleanOperation getBooleanOperation() {
        return mOperation;
    }

    public boolean getBooleanValue() {
        if (mConditions.size() < mOperation.getMinimumNumberOfOperands())
            throw new IllegalStateException("Not enough operands defined for this expression.");

        if (mOperation.isUnaryOperation() && mConditions.size() > 1)
            throw new IllegalStateException("Too many operands defined for a unary operation.");

        if (mOperation.isUnaryOperation()) {
            mOperation.setLeftHandOperand(mConditions.get(0).getBooleanValue());
            mCurrentValue = mOperation.execute();
            return mCurrentValue;
        }

        AbstractCondition firstOperand = mConditions.get(0);
        boolean firstOperandValue = firstOperand.getBooleanValue();

        for (int i = 1; i < mConditions.size(); ++i) {
            if (mOperation.canShortCircuit(firstOperandValue)) {
                mCurrentValue = mOperation.getShortCircuit(firstOperandValue);
                return mCurrentValue;
            }
            firstOperandValue = executeOperation(firstOperandValue, mConditions.get(i));
        }

        mCurrentValue = firstOperandValue;
        return firstOperandValue;
    }

    public void addOperand(AbstractCondition operand) {
        CheckForNull.check(operand);
        if (mConditions == null)
            mConditions = new LinkedList<AbstractCondition>();
        mConditions.add(operand);
    }

    private boolean executeOperation(boolean firstOperand, AbstractCondition condition) {
        mOperation.setLeftHandOperand(firstOperand);
        mOperation.setRightHandOperand(condition.getBooleanValue());
        return mOperation.execute();
    }

    @Override
    public String toString() {
        return "[" + mOperation.getClass().getSimpleName() + ": " + mConditions;
    }
}
