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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class CatchAllURIActionHandlerTest
{
  private URIActionDispatcher      mDispatcher;
  private TURIActionHandler        mTestActionHandler;
  private TURIActionCommand        mTestActionCommand;
  private CatchAllURIActionHandler mCatchAllActionHandler;
  private TURIActionCommand        mCatchAllActionCommand;
  private TURIActionHandler        mLastActionHandler;
  private TURIActionCommand        mLastActionCommand;

  @Before
  public void setUp ()
  {
    mDispatcher = new URIActionDispatcher (false);

    mTestActionCommand = new TURIActionCommand ();
    mTestActionHandler = new TURIActionHandler ("test", mTestActionCommand);

    mCatchAllActionHandler = new CatchAllURIActionHandler ();
    mCatchAllActionCommand = new TURIActionCommand ();
    mCatchAllActionHandler.setRootCommand (mCatchAllActionCommand);

    mLastActionCommand = new TURIActionCommand ();
    mLastActionHandler = new TURIActionHandler ("last", mLastActionCommand);
    mCatchAllActionHandler.addSubHandler (mLastActionHandler);

    mDispatcher.addHandler (mCatchAllActionHandler);
    mDispatcher.addHandler (mTestActionHandler);
  }

  @Test
  public void test ()
  {
    mDispatcher.handleURIAction ("/test");
    assertActionCommandWasExecuted (mTestActionCommand);
    resetActionCommands ();

    mDispatcher.handleURIAction ("/someurlfragment");
    assertActionCommandWasExecuted (mCatchAllActionCommand);
    assertEquals ("someurlfragment", mCatchAllActionHandler.getCurrentURIToken ());
    resetActionCommands ();

    mDispatcher.handleURIAction ("/anything/last");
    assertActionCommandWasExecuted (mLastActionCommand);
    assertEquals ("anything", mCatchAllActionHandler.getCurrentURIToken ());
  }

  private void assertActionCommandWasExecuted (TURIActionCommand command)
  {
    assertTrue (command.mExecuted);
  }

  private void resetActionCommands ()
  {
    mTestActionCommand.mExecuted = false;
    mCatchAllActionCommand.mExecuted = false;
  }
}
