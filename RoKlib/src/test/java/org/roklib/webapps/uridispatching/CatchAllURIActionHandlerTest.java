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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CatchAllURIActionHandlerTest {
    private URIActionDispatcher dispatcher;
    private TURIActionHandler testActionHandler;
    private TURIActionCommand testActionCommand;
    private CatchAllURIActionHandler catchAllActionHandler;
    private TURIActionCommand catchAllActionCommand;
    private TURIActionHandler lastActionHandler;
    private TURIActionCommand lastActionCommand;

    @Before
    public void setUp() {
        dispatcher = new URIActionDispatcher(false);

        testActionCommand = new TURIActionCommand();
        testActionHandler = new TURIActionHandler("test", testActionCommand);

        catchAllActionHandler = new CatchAllURIActionHandler();
        catchAllActionCommand = new TURIActionCommand();
        catchAllActionHandler.setRootCommand(catchAllActionCommand);

        lastActionCommand = new TURIActionCommand();
        lastActionHandler = new TURIActionHandler("last", lastActionCommand);
        catchAllActionHandler.addSubHandler(lastActionHandler);

        dispatcher.addHandler(catchAllActionHandler);
        dispatcher.addHandler(testActionHandler);
    }

    @Test
    public void test() {
        dispatcher.handleURIAction("/test");
        assertActionCommandWasExecuted(testActionCommand);
        resetActionCommands();

        dispatcher.handleURIAction("/someurlfragment");
        assertActionCommandWasExecuted(catchAllActionCommand);
        assertEquals("someurlfragment", catchAllActionHandler.getCurrentURIToken());
        resetActionCommands();

        dispatcher.handleURIAction("/anything/last");
        assertActionCommandWasExecuted(lastActionCommand);
        assertEquals("anything", catchAllActionHandler.getCurrentURIToken());
    }

    private void assertActionCommandWasExecuted(TURIActionCommand command) {
        assertTrue(command.mExecuted);
    }

    private void resetActionCommands() {
        testActionCommand.mExecuted = false;
        catchAllActionCommand.mExecuted = false;
    }
}
