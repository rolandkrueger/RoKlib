/*
 * Copyright (C) 2007 Roland Krueger
 * Created on 17.10.2009
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
package org.roklib.util.helper;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class CheckForNullTest {
    @Test
    public void testCheckNoNull() {
        CheckForNull.check();
        CheckForNull.check("not null");
        CheckForNull.check("not null", new Object());
        CheckForNull.check("not null", new Object(), this);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckWithNull() {
        try {
            CheckForNull.check("not null", null, "not null", null);
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("2, 4"));
            throw e;
        }
    }
}
