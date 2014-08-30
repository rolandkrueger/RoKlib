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
    private URIActionDispatcher dispatcher;
    private TURIActionHandler testActionHandler;
    private TURIActionCommand testActionCommand;
    private StartsWithURIActionHandler startsWithActionHandler;
    private TURIActionCommand startsWithActionCommand;
    private TURIActionHandler lastActionHandler;
    private TURIActionCommand lastActionCommand;

    @Before
    public void setUp() {
        dispatcher = new URIActionDispatcher(false);

        testActionCommand = new TURIActionCommand();
        testActionHandler = new TURIActionHandler("testhandler", testActionCommand);

        startsWithActionHandler = new StartsWithURIActionHandler("test");
        startsWithActionCommand = new TURIActionCommand();
        startsWithActionHandler.setRootCommand(startsWithActionCommand);

        lastActionCommand = new TURIActionCommand();
        lastActionHandler = new TURIActionHandler("last", lastActionCommand);
        startsWithActionHandler.addSubHandler(lastActionHandler);

        dispatcher.addHandler(startsWithActionHandler);
        dispatcher.addHandler(testActionHandler);
    }

    @Test
    public void testDispatching() {
        dispatcher.handleURIAction("/testvalue/last");
        assertActionCommandWasExecuted(lastActionCommand);
    }

    @Test
    public void testPrecedence() {
        dispatcher.handleURIAction("/testhandler");
        assertActionCommandWasExecuted(testActionCommand);
    }

    @Test
    public void testCaseSensitive() {
        dispatcher.setCaseSensitive(true);
        dispatcher.handleURIAction("/testvalue");
        assertOutcome();
    }

    @Test
    public void testCaseInsensitive() {
        dispatcher.setCaseSensitive(false);
        dispatcher.handleURIAction("/TESTvalue");
        assertOutcome();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_Fail() {
        new StartsWithURIActionHandler("  ");
    }

    private void assertOutcome() {
        assertActionCommandWasExecuted(startsWithActionCommand);
        assertMatchedTokenFragments(startsWithActionHandler, new String[]{"value"});
    }

    private void assertActionCommandWasExecuted(TURIActionCommand command) {
        assertTrue(command.mExecuted);
    }

    private void assertMatchedTokenFragments(RegexURIActionHandler handler, String[] expectedTokenFragments) {
        assertTrue(Arrays.equals(expectedTokenFragments, handler.getMatchedTokenFragments()));
    }
}
