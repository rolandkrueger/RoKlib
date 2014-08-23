/*
 * Copyright (C) 2007 Roland Krueger Created on 22.09.2012
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
package org.roklib.webapps.uridispatching;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertTrue;

public class StartsWithURIActionHandlerTest {
    private URIActionDispatcher mDispatcher;
    private TURIActionHandler mTestActionHandler;
    private TURIActionCommand mTestActionCommand;
    private StartsWithURIActionHandler mStartsWithActionHandler;
    private TURIActionCommand mStartsWithActionCommand;
    private TURIActionHandler mLastActionHandler;
    private TURIActionCommand mLastActionCommand;

    @Before
    public void setUp() {
        mDispatcher = new URIActionDispatcher(false);

        mTestActionCommand = new TURIActionCommand();
        mTestActionHandler = new TURIActionHandler("testhandler", mTestActionCommand);

        mStartsWithActionHandler = new StartsWithURIActionHandler("test");
        mStartsWithActionCommand = new TURIActionCommand();
        mStartsWithActionHandler.setRootCommand(mStartsWithActionCommand);

        mLastActionCommand = new TURIActionCommand();
        mLastActionHandler = new TURIActionHandler("last", mLastActionCommand);
        mStartsWithActionHandler.addSubHandler(mLastActionHandler);

        mDispatcher.addHandler(mStartsWithActionHandler);
        mDispatcher.addHandler(mTestActionHandler);
    }

    @Test
    public void testDispatching() {
        mDispatcher.handleURIAction("/testvalue/last");
        assertActionCommandWasExecuted(mLastActionCommand);
    }

    @Test
    public void testPrecedence() {
        mDispatcher.handleURIAction("/testhandler");
        assertActionCommandWasExecuted(mTestActionCommand);
    }

    @Test
    public void testCaseSensitive() {
        mDispatcher.setCaseSensitive(true);
        mDispatcher.handleURIAction("/testvalue");
        assertOutcome();
    }

    @Test
    public void testCaseInsensitive() {
        mDispatcher.setCaseSensitive(false);
        mDispatcher.handleURIAction("/TESTvalue");
        assertOutcome();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_Fail() {
        new StartsWithURIActionHandler("  ");
    }

    private void assertOutcome() {
        assertActionCommandWasExecuted(mStartsWithActionCommand);
        assertMatchedTokenFragments(mStartsWithActionHandler, new String[]{"value"});
    }

    private void assertActionCommandWasExecuted(TURIActionCommand command) {
        assertTrue(command.mExecuted);
    }

    private void assertMatchedTokenFragments(RegexURIActionHandler handler, String[] expectedTokenFragments) {
        assertTrue(Arrays.equals(expectedTokenFragments, handler.getMatchedTokenFragments()));
    }
}
