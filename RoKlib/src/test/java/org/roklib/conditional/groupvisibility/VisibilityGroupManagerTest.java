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

public class VisibilityGroupManagerTest {
    private VisibilityGroupManager visibilityGroupManager;
    private VisibilityEnablingConfigurableTestClass groupMember1 = new VisibilityEnablingConfigurableTestClass();
    private VisibilityEnablingConfigurableTestClass groupMember2 = new VisibilityEnablingConfigurableTestClass();

    @Before
    public void setUp() {
        visibilityGroupManager = new VisibilityGroupManager();
        visibilityGroupManager.addGroupMember("group1", groupMember1);
        visibilityGroupManager.addGroupMember("group2", groupMember2);

    }

    @Test
    public void testAddGroupMember() {
        assertEquals(2, visibilityGroupManager.getGroupCount());
        assertTrue(visibilityGroupManager.getVisibilityGroup("group1").getGroupMembers().contains(groupMember1));
        assertTrue(visibilityGroupManager.getVisibilityGroup("group2").getGroupMembers().contains(groupMember2));
        assertFalse(visibilityGroupManager.getVisibilityGroup("group2").getGroupMembers().contains(groupMember1));
        assertFalse(visibilityGroupManager.getVisibilityGroup("group1").getGroupMembers().contains(groupMember2));
    }

    @Test
    public void testSetGroupVisible() {
        assertTrue(visibilityGroupManager.getVisibilityGroup("group2").isVisible());
        assertTrue(visibilityGroupManager.setGroupVisible("group2", false));
        assertFalse(visibilityGroupManager.getVisibilityGroup("group2").isVisible());
        visibilityGroupManager.setGroupVisible("group2", true);
        assertTrue(visibilityGroupManager.getVisibilityGroup("group2").isVisible());

        assertFalse(visibilityGroupManager.setGroupVisible("non-existing group", false));
    }

    @Test
    public void testSetGroupEnabled() {
        assertTrue(visibilityGroupManager.getVisibilityGroup("group2").isEnabled());
        assertTrue(visibilityGroupManager.setGroupEnabled("group2", false));
        assertFalse(visibilityGroupManager.getVisibilityGroup("group2").isEnabled());
        visibilityGroupManager.setGroupEnabled("group2", true);
        assertTrue(visibilityGroupManager.getVisibilityGroup("group2").isEnabled());

        assertFalse(visibilityGroupManager.setGroupEnabled("non-existing group", false));
    }
}
