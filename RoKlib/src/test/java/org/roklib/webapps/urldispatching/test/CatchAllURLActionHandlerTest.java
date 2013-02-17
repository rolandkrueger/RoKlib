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
package org.roklib.webapps.urldispatching.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Before;
import org.junit.Test;
import org.roklib.net.IURLProvider;
import org.roklib.webapps.urldispatching.CatchAllURLActionHandler;
import org.roklib.webapps.urldispatching.URLActionDispatcher;

public class CatchAllURLActionHandlerTest
{

  private URLActionDispatcher      mDispatcher;

  private TURLActionHandler        mTestActionHandler;

  private TURLActionCommand        mTestActionCommand;

  private CatchAllURLActionHandler mCatchAllActionHandler;

  private TURLActionCommand        mCatchAllActionCommand;

  private TURLActionHandler        mLastActionHandler;

  private TURLActionCommand        mLastActionCommand;

  private URL                      mContext;

  @Before
  public void setUp () throws MalformedURLException
  {
    mContext = new URL ("http://localhost");
    mDispatcher = new URLActionDispatcher (new IURLProvider ()
    {
      public URL getURL ()
      {
        try
        {
          return new URL ("http://localhost:8080");
        } catch (MalformedURLException e)
        {
          throw new RuntimeException (e);
        }
      }
    }, false);

    mTestActionCommand = new TURLActionCommand ();
    mTestActionHandler = new TURLActionHandler ("test", mTestActionCommand);

    mCatchAllActionHandler = new CatchAllURLActionHandler ();
    mCatchAllActionCommand = new TURLActionCommand ();
    mCatchAllActionHandler.setRootCommand (mCatchAllActionCommand);

    mLastActionCommand = new TURLActionCommand ();
    mLastActionHandler = new TURLActionHandler ("last", mLastActionCommand);
    mCatchAllActionHandler.addSubHandler (mLastActionHandler);

    mDispatcher.addHandler (mCatchAllActionHandler);
    mDispatcher.addHandler (mTestActionHandler);
  }

  @Test
  public void test ()
  {
    mDispatcher.handleURIAction (mContext, "/test");
    assertActionCommandWasExecuted (mTestActionCommand);
    resetActionCommands ();

    mDispatcher.handleURIAction (mContext, "/someurlfragment");
    assertActionCommandWasExecuted (mCatchAllActionCommand);
    assertEquals ("someurlfragment", mCatchAllActionHandler.getCurrentURIToken ());
    resetActionCommands ();

    mDispatcher.handleURIAction (mContext, "/anything/last");
    assertActionCommandWasExecuted (mLastActionCommand);
    assertEquals ("anything", mCatchAllActionHandler.getCurrentURIToken ());
  }

  private void assertActionCommandWasExecuted (TURLActionCommand command)
  {
    assertTrue (command.mExecuted);
  }

  private void resetActionCommands ()
  {
    mTestActionCommand.mExecuted = false;
    mCatchAllActionCommand.mExecuted = false;
  }
}
