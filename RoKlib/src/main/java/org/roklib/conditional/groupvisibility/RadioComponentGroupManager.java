/*
 * Copyright (C) 2007 Roland Krueger
 * Created on 01.07.2010
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
package org.roklib.conditional.groupvisibility;


import org.roklib.conditional.bool.AndOperation;
import org.roklib.conditional.engine.*;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.Map;

/**
 * <p>
 * This class manages a set of components which can be switched on or off in the same way as a group of radio buttons.
 * Component in this context means any object which implements the {@link VisibilityEnablingConfigurable} interface.
 * This may be a UI component or any other object that can be switched on or off.
 * </p>
 * <p>
 * An example use case for this class is the navigation bar in a web application. Depending on which section the user is
 * visiting, the links for the individual sections in the navigation bar are either selected or not selected. If the
 * user clicks on such a link, its background image may change from an unselected graphic to a selected graphic. All
 * other link images have to show the unselected image accordingly. This behavior is controlled automatically by the
 * {@link RadioComponentGroupManager}.
 * </p>
 *
 * @author Roland Krueger
 * @see VisibilityGroupManager
 */
public class RadioComponentGroupManager implements Serializable {
    private static final long serialVersionUID = 3934091376239028966L;

    public enum Mode {
        ENABLING, VISIBILITY
    }

    private final VisibilityGroupManager manager;
    private final Map<String, RadioComponentSwitch> conditions;
    private Mode mode;

    public RadioComponentGroupManager(Mode mode) {
        this(7, mode);
    }

    public RadioComponentGroupManager() {
        this(7);
    }

    public RadioComponentGroupManager(int numberOfManagedComponents, Mode mode) {
        this.mode = mode;
        if (this.mode == null) {
            this.mode = Mode.ENABLING;
        }
        manager = new VisibilityGroupManager(numberOfManagedComponents);
        conditions = new Hashtable<String, RadioComponentSwitch>(numberOfManagedComponents);
    }

    public RadioComponentGroupManager(int numberOfManagedComponents) {
        this(numberOfManagedComponents, Mode.ENABLING);
    }

    public RadioComponentSwitch addComponent(String groupId, VisibilityEnablingConfigurable component) {
        RadioComponentSwitch result = new RadioComponentSwitch(groupId + "_switch");
        conditions.put(groupId, result);
        manager.addGroupMember(groupId, component);
        recalculateExpressions();
        return result;
    }

    private void recalculateExpressions() {
        if (conditions.size() < 2)
            return;

        for (Map.Entry<String, RadioComponentSwitch> currentEntry : conditions.entrySet()) {
            currentEntry.getValue().clearConditionListeners();
            BooleanExpression expression = new BooleanExpression(new AndOperation());

            for (Map.Entry<String, RadioComponentSwitch> entry : conditions.entrySet()) {
                if (currentEntry == entry)
                    continue;
                expression.addOperand(entry.getValue());
                currentEntry.getValue().addConditionListener(entry.getValue());
            }

            expression = BoolExpressionBuilder.createANDExpression(currentEntry.getValue(),
                    BoolExpressionBuilder.createNOTExpression(expression));

            if (mode == Mode.ENABLING) {
                manager.setExpressionForEnabling(currentEntry.getKey(), expression);
            } else {
                manager.setExpressionForVisibility(currentEntry.getKey(), expression);
            }
        }
    }

    public static class RadioComponentSwitch extends Condition implements ConditionListener {
        private static final long serialVersionUID = 4106071527518882170L;

        public RadioComponentSwitch(String name) {
            super(name);
        }

        public void select() {
            setValue(true);
        }

        public void conditionChanged(AbstractCondition source) {
            if (source.getBooleanValue()) {
                setValue(false);
            }
        }
    }
}
