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

import org.junit.Before;
import org.junit.Test;
import org.roklib.conditional.groupvisibility.RadioComponentGroupManager.Mode;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RadioComponentGroupManagerTest {
    private TestRadioComponent component1;
    private TestRadioComponent component2;
    private TestRadioComponent component3;
    private RadioComponentGroupManager.RadioComponentSwitch switch1;
    private RadioComponentGroupManager.RadioComponentSwitch switch2;
    private RadioComponentGroupManager.RadioComponentSwitch switch3;
    private RadioComponentGroupManager rcgManager;

    @Before
    public void setUp() {
        component1 = new TestRadioComponent();
        component2 = new TestRadioComponent();
        component3 = new TestRadioComponent();
        component1.setEnabled(false);
        component1.setVisible(false);
        component2.setEnabled(false);
        component2.setVisible(false);
        component3.setEnabled(false);
        component3.setVisible(false);
    }

    private void setUpTestObj(Mode mode) {
        rcgManager = new RadioComponentGroupManager(mode);
        switch1 = rcgManager.addComponent("group1", component1);
        switch2 = rcgManager.addComponent("group2", component2);
        switch3 = rcgManager.addComponent("group3", component3);
    }

    @Test
    public void testEnablingMode() {
        setUpTestObj(Mode.ENABLING);

        switch1.select();
        assertTrue(component1.isEnabled());
        assertFalse(component1.isVisible());
        assertFalse(component2.isEnabled());
        assertFalse(component2.isVisible());
        assertFalse(component3.isEnabled());
        assertFalse(component3.isVisible());

        switch2.select();
        assertTrue(component2.isEnabled());
        assertFalse(component2.isVisible());
        assertFalse(component1.isEnabled());
        assertFalse(component1.isVisible());
        assertFalse(component3.isEnabled());
        assertFalse(component3.isVisible());

        switch3.select();
        assertTrue(component3.isEnabled());
        assertFalse(component3.isVisible());
        assertFalse(component2.isEnabled());
        assertFalse(component2.isVisible());
        assertFalse(component1.isEnabled());
        assertFalse(component1.isVisible());
    }

    @Test
    public void testVisibilityMode() {
        setUpTestObj(Mode.VISIBILITY);

        switch1.select();
        assertTrue(component1.isVisible());
        assertFalse(component1.isEnabled());
        assertFalse(component2.isVisible());
        assertFalse(component2.isEnabled());
        assertFalse(component3.isVisible());
        assertFalse(component3.isEnabled());

        switch2.select();
        assertTrue(component2.isVisible());
        assertFalse(component2.isEnabled());
        assertFalse(component1.isVisible());
        assertFalse(component1.isEnabled());
        assertFalse(component3.isVisible());
        assertFalse(component3.isEnabled());

        switch3.select();
        assertTrue(component3.isVisible());
        assertFalse(component3.isEnabled());
        assertFalse(component1.isVisible());
        assertFalse(component1.isEnabled());
        assertFalse(component2.isVisible());
        assertFalse(component2.isEnabled());
    }

    private class TestRadioComponent implements VisibilityEnablingConfigurable {
        private static final long serialVersionUID = -1878281356099704091L;

        private boolean mEnabled;
        private boolean mVisible;

        public boolean isEnabled() {
            return mEnabled;
        }

        public boolean isVisible() {
            return mVisible;
        }

        public void setEnabled(boolean enabled) {
            mEnabled = enabled;
        }

        public void setVisible(boolean visible) {
            mVisible = visible;
        }
    }
}
