/*
 * Copyright (C) 2007 Roland Krueger
 * Created on 18.10.2009
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


import org.roklib.conditional.engine.AbstractCondition;
import org.roklib.conditional.engine.BooleanExpression;
import org.roklib.conditional.engine.ConditionListener;
import org.roklib.util.helper.CheckForNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class VisibilityGroup implements VisibilityEnablingConfigurable, ConditionListener {
    private static final long serialVersionUID = -4366993767678121332L;

    private boolean visible;
    private boolean enabled;
    private final String groupName;
    private final Set<VisibilityEnablingConfigurable> groupMembers;
    private BooleanExpression booleanExpressionForVisibility;
    private BooleanExpression booleanExpressionForEnabling;

    public VisibilityGroup(String name) {
        CheckForNull.check(name);
        groupName = name;
        groupMembers = Collections.synchronizedSet(new HashSet<VisibilityEnablingConfigurable>());
        visible = true;
        enabled = true;
    }

    public void addVisibilityEnablingConfigurable(VisibilityEnablingConfigurable groupMember) {
        CheckForNull.check(groupMember);
        groupMembers.add(groupMember);
        applyExpressionsIfAvailable();
    }

    public boolean removeVisibilityEnablingConfigurable(VisibilityEnablingConfigurable groupMember) {
        return groupMembers.remove(groupMember);
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        for (VisibilityEnablingConfigurable member : groupMembers)
            member.setEnabled(enabled);
    }

    public void setVisible(boolean visible) {
        for (VisibilityEnablingConfigurable member : groupMembers)
            member.setVisible(visible);
        this.visible = visible;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public boolean isVisible() {
        return visible;
    }

    public String getName() {
        return groupName;
    }

    public int getSize() {
        return groupMembers.size();
    }

    public Set<VisibilityEnablingConfigurable> getGroupMembers() {
        return Collections.unmodifiableSet(groupMembers);
    }

    public void setExpressionForVisibility(BooleanExpression expression) {
        if (booleanExpressionForVisibility != null)
            booleanExpressionForVisibility.removeConditionListener(this);

        booleanExpressionForVisibility = expression;
        if (expression != null) {
            booleanExpressionForVisibility.addConditionListener(this);
            applyExpressionsIfAvailable();
        }
    }

    public void setExpressionForEnabling(BooleanExpression expression) {
        if (booleanExpressionForEnabling != null)
            booleanExpressionForEnabling.removeConditionListener(this);

        booleanExpressionForEnabling = expression;
        if (expression != null) {
            booleanExpressionForEnabling.addConditionListener(this);
            applyExpressionsIfAvailable();
        }
    }

    private void applyExpressionsIfAvailable() {
        if (booleanExpressionForEnabling != null) {
            setEnabled(booleanExpressionForEnabling.getBooleanValue());
        }
        if (booleanExpressionForVisibility != null) {
            setVisible(booleanExpressionForVisibility.getBooleanValue());
        }
    }

    public void conditionChanged(AbstractCondition source) {
        if (source == booleanExpressionForVisibility)
            setVisible(booleanExpressionForVisibility.getBooleanValue());
        if (source == booleanExpressionForEnabling)
            setEnabled(booleanExpressionForEnabling.getBooleanValue());
    }
}
