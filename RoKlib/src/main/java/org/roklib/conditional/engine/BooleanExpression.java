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


import org.roklib.conditional.bool.BooleanOperation;
import org.roklib.util.helper.CheckForNull;

import java.util.LinkedList;
import java.util.List;

public class BooleanExpression extends AbstractCondition implements ConditionListener {
    private static final long serialVersionUID = 3479069548966346585L;

    private final BooleanOperation operation;
    private List<AbstractCondition> conditions;
    private Boolean currentValue;

    public BooleanExpression(BooleanOperation operation) {
        CheckForNull.check(operation);
        this.operation = operation;
        currentValue = null;
    }

    public void conditionChanged(AbstractCondition source) {
        if (currentValue == null)
            getBooleanValue();
        boolean previousValue = currentValue;
        boolean value = getBooleanValue();
        if (value != previousValue) {
            fireConditionChanged();
        }
    }

    @Override
    public void addConditionListener(ConditionListener listener) {
        if (getListenerCount() == 0) {
            for (AbstractCondition condition : conditions) {
                condition.addConditionListener(this);
            }
        }
        super.addConditionListener(listener);
    }

    @Override
    public void removeConditionListener(ConditionListener listener) {
        super.removeConditionListener(listener);
        if (getListenerCount() == 0) {
            for (AbstractCondition condition : conditions) {
                condition.removeConditionListener(this);
            }
        }
    }

    public BooleanOperation getBooleanOperation() {
        return operation;
    }

    public boolean getBooleanValue() {
        if (conditions.size() < operation.getMinimumNumberOfOperands())
            throw new IllegalStateException("Not enough operands defined for this expression.");

        if (operation.isUnaryOperation() && conditions.size() > 1)
            throw new IllegalStateException("Too many operands defined for a unary operation.");

        if (operation.isUnaryOperation()) {
            operation.setLeftHandOperand(conditions.get(0).getBooleanValue());
            currentValue = operation.execute();
            return currentValue;
        }

        AbstractCondition firstOperand = conditions.get(0);
        boolean firstOperandValue = firstOperand.getBooleanValue();

        for (int i = 1; i < conditions.size(); ++i) {
            if (operation.canShortCircuit(firstOperandValue)) {
                currentValue = operation.getShortCircuit(firstOperandValue);
                return currentValue;
            }
            firstOperandValue = executeOperation(firstOperandValue, conditions.get(i));
        }

        currentValue = firstOperandValue;
        return firstOperandValue;
    }

    public void addOperand(AbstractCondition operand) {
        CheckForNull.check(operand);
        if (conditions == null)
            conditions = new LinkedList<AbstractCondition>();
        conditions.add(operand);
    }

    private boolean executeOperation(boolean firstOperand, AbstractCondition condition) {
        operation.setLeftHandOperand(firstOperand);
        operation.setRightHandOperand(condition.getBooleanValue());
        return operation.execute();
    }

    @Override
    public String toString() {
        return "[" + operation.getClass().getSimpleName() + ": " + conditions;
    }
}
