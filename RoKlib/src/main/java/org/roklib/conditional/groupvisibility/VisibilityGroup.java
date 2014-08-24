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
import org.roklib.conditional.engine.IConditionListener;
import org.roklib.util.helper.CheckForNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class VisibilityGroup implements IVisibilityEnablingConfigurable, IConditionListener {
    private static final long serialVersionUID = -4366993767678121332L;

    private boolean mVisible;
    private boolean mEnabled;
    private final String mGroupName;
    private final Set<IVisibilityEnablingConfigurable> mGroupMembers;
    private BooleanExpression mBooleanExpressionForVisibility;
    private BooleanExpression mBooleanExpressionForEnabling;

    public VisibilityGroup(String name) {
        CheckForNull.check(name);
        mGroupName = name;
        mGroupMembers = Collections.synchronizedSet(new HashSet<IVisibilityEnablingConfigurable>());
        mVisible = true;
        mEnabled = true;
    }

    public void addVisibilityEnablingConfigurable(IVisibilityEnablingConfigurable groupMember) {
        CheckForNull.check(groupMember);
        mGroupMembers.add(groupMember);
        applyExpressionsIfAvailable();
    }

    public boolean removeVisibilityEnablingConfigurable(IVisibilityEnablingConfigurable groupMember) {
        return mGroupMembers.remove(groupMember);
    }

    public void setEnabled(boolean enabled) {
        mEnabled = enabled;
        for (IVisibilityEnablingConfigurable member : mGroupMembers)
            member.setEnabled(enabled);
    }

    public void setVisible(boolean visible) {
        for (IVisibilityEnablingConfigurable member : mGroupMembers)
            member.setVisible(visible);
        mVisible = visible;
    }

    public boolean isEnabled() {
        return mEnabled;
    }

    public boolean isVisible() {
        return mVisible;
    }

    public String getName() {
        return mGroupName;
    }

    public int getSize() {
        return mGroupMembers.size();
    }

    public Set<IVisibilityEnablingConfigurable> getGroupMembers() {
        return Collections.unmodifiableSet(mGroupMembers);
    }

    public void setExpressionForVisibility(BooleanExpression expression) {
        if (mBooleanExpressionForVisibility != null)
            mBooleanExpressionForVisibility.removeConditionListener(this);

        mBooleanExpressionForVisibility = expression;
        if (expression != null) {
            mBooleanExpressionForVisibility.addConditionListener(this);
            applyExpressionsIfAvailable();
        }
    }

    public void setExpressionForEnabling(BooleanExpression expression) {
        if (mBooleanExpressionForEnabling != null)
            mBooleanExpressionForEnabling.removeConditionListener(this);

        mBooleanExpressionForEnabling = expression;
        if (expression != null) {
            mBooleanExpressionForEnabling.addConditionListener(this);
            applyExpressionsIfAvailable();
        }
    }

    private void applyExpressionsIfAvailable() {
        if (mBooleanExpressionForEnabling != null) {
            setEnabled(mBooleanExpressionForEnabling.getBooleanValue());
        }
        if (mBooleanExpressionForVisibility != null) {
            setVisible(mBooleanExpressionForVisibility.getBooleanValue());
        }
    }

    public void conditionChanged(AbstractCondition source) {
        if (source == mBooleanExpressionForVisibility)
            setVisible(mBooleanExpressionForVisibility.getBooleanValue());
        if (source == mBooleanExpressionForEnabling)
            setEnabled(mBooleanExpressionForEnabling.getBooleanValue());
    }
}
