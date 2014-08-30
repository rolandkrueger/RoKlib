/*
 * Copyright (C) 2007 Roland Krueger
 * Created on 09.02.2010
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
package org.roklib.util.authorization;

import org.junit.Before;
import org.junit.Test;
import org.roklib.webapps.authorization.AdmissionTicket;
import org.roklib.webapps.authorization.AdmissionTicketContainer;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AdmissionTicketContainerTest {
    private AdmissionTicketContainer mTestObj;

    private class TTicket implements AdmissionTicket {
        private static final long serialVersionUID = -2680076196174728564L;
        private String mName;

        public TTicket(String name) {
            mName = name;
        }

        public String getName() {
            return mName;
        }
    }

    @Before
    public void setUp() {
        mTestObj = new AdmissionTicketContainer();
        mTestObj.addTicket(new TTicket("admin"));
        mTestObj.addTicket(new TTicket("editor"));
    }

    @Test
    public void testHasTicket() {
        assertTrue(mTestObj.hasTicket(new TTicket("admin")));
        assertFalse(mTestObj.hasTicket(new TTicket("user")));
    }

    @Test
    public void testClear() {
        mTestObj.clear();
        assertTrue(mTestObj.getTickets().isEmpty());
    }

    @Test
    public void testEmptyContainer() {
        mTestObj = new AdmissionTicketContainer();
        assertTrue(mTestObj.getTickets().isEmpty());
        mTestObj.clear();
        assertTrue(mTestObj.getTickets().isEmpty());
        assertFalse(mTestObj.hasTicket(new TTicket("user")));
    }

    @Test
    public void testRemoveTicket() {
        assertTrue(mTestObj.hasTicket(new TTicket("admin")));
        assertTrue(mTestObj.removeTicket(new TTicket("admin")));
        assertFalse(mTestObj.hasTicket(new TTicket("admin")));
    }
}
