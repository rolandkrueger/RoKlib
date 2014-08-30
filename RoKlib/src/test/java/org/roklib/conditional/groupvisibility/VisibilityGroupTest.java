/*
 * Copyright (C) 2007 Roland Krueger
 * Created on 19.10.2009
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

import static org.junit.Assert.*;

public class VisibilityGroupTest {
    private VisibilityGroup visibilityGroup;

    @Before
    public void setUp() {
        visibilityGroup = new VisibilityGroup("group");
    }

    @Test
    public void testAddVisibilityEnablingConfigurable() {
        VisibilityEnablingConfigurableTestClass testObj = new VisibilityEnablingConfigurableTestClass();
        visibilityGroup.addVisibilityEnablingConfigurable(new VisibilityEnablingConfigurableTestClass());
        visibilityGroup.addVisibilityEnablingConfigurable(testObj);
        visibilityGroup.addVisibilityEnablingConfigurable(testObj);

        assertEquals(2, visibilityGroup.getSize());
    }

    @Test
    public void testRemoveVisibilityEnablingConfigurable() {
        VisibilityEnablingConfigurableTestClass testObj = new VisibilityEnablingConfigurableTestClass();
        visibilityGroup.addVisibilityEnablingConfigurable(testObj);
        assertEquals(1, visibilityGroup.getSize());
        boolean result = visibilityGroup.removeVisibilityEnablingConfigurable(testObj);
        assertEquals(0, visibilityGroup.getSize());
        assertTrue(result);
    }

    @Test
    public void testSetVisible() {
        VisibilityEnablingConfigurableTestClass testObj1 = new VisibilityEnablingConfigurableTestClass();
        VisibilityEnablingConfigurableTestClass testObj2 = new VisibilityEnablingConfigurableTestClass();
        visibilityGroup.addVisibilityEnablingConfigurable(testObj1);
        visibilityGroup.addVisibilityEnablingConfigurable(testObj2);
        assertTrue(testObj1.isEnabled());
        assertTrue(testObj2.isEnabled());
        assertTrue(testObj1.isVisible());
        assertTrue(testObj2.isVisible());
        assertTrue(visibilityGroup.isEnabled());
        assertTrue(visibilityGroup.isVisible());

        visibilityGroup.setVisible(false);
        assertTrue(testObj1.isEnabled());
        assertTrue(testObj2.isEnabled());
        assertFalse(testObj1.isVisible());
        assertFalse(testObj2.isVisible());
        assertTrue(visibilityGroup.isEnabled());
        assertFalse(visibilityGroup.isVisible());
    }

    @Test
    public void testSetEnabled() {
        VisibilityEnablingConfigurableTestClass testObj1 = new VisibilityEnablingConfigurableTestClass();
        VisibilityEnablingConfigurableTestClass testObj2 = new VisibilityEnablingConfigurableTestClass();
        visibilityGroup.addVisibilityEnablingConfigurable(testObj1);
        visibilityGroup.addVisibilityEnablingConfigurable(testObj2);
        assertTrue(testObj1.isEnabled());
        assertTrue(testObj2.isEnabled());
        assertTrue(testObj1.isVisible());
        assertTrue(testObj2.isVisible());
        assertTrue(visibilityGroup.isEnabled());
        assertTrue(visibilityGroup.isVisible());

        visibilityGroup.setEnabled(false);
        assertFalse(testObj1.isEnabled());
        assertFalse(testObj2.isEnabled());
        assertTrue(testObj1.isVisible());
        assertTrue(testObj2.isVisible());
        assertFalse(visibilityGroup.isEnabled());
        assertTrue(visibilityGroup.isVisible());
    }

    @Test
    public void testIsEnabled() {
        assertTrue(visibilityGroup.isEnabled());
        visibilityGroup.setEnabled(false);
        assertFalse(visibilityGroup.isEnabled());
        visibilityGroup.setEnabled(true);
        assertTrue(visibilityGroup.isEnabled());
    }

    @Test
    public void testIsVisible() {
        assertTrue(visibilityGroup.isVisible());
        visibilityGroup.setVisible(false);
        assertFalse(visibilityGroup.isVisible());
        visibilityGroup.setVisible(true);
        assertTrue(visibilityGroup.isVisible());
    }

    @Test
    public void testGetName() {
        assertEquals("group", visibilityGroup.getName());
    }
}
