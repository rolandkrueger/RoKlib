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

import static org.junit.Assert.assertTrue;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.roklib.net.IURLProvider;
import org.roklib.webapps.urldispatching.RegexURLActionHandler;
import org.roklib.webapps.urldispatching.StartsWithURLActionHandler;
import org.roklib.webapps.urldispatching.URLActionDispatcher;

public class StartsWithURLActionHandlerTest
{
  private URLActionDispatcher        mDispatcher;

  private TURLActionHandler          mTestActionHandler;

  private TURLActionCommand          mTestActionCommand;

  private StartsWithURLActionHandler mStartsWithActionHandler;

  private TURLActionCommand          mStartsWithActionCommand;

  private TURLActionHandler          mLastActionHandler;

  private TURLActionCommand          mLastActionCommand;

  private URL                        mContext;

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
    mTestActionHandler = new TURLActionHandler ("testhandler", mTestActionCommand);

    mStartsWithActionHandler = new StartsWithURLActionHandler ("test");
    mStartsWithActionCommand = new TURLActionCommand ();
    mStartsWithActionHandler.setRootCommand (mStartsWithActionCommand);

    mLastActionCommand = new TURLActionCommand ();
    mLastActionHandler = new TURLActionHandler ("last", mLastActionCommand);
    mStartsWithActionHandler.addSubHandler (mLastActionHandler);

    mDispatcher.addHandler (mStartsWithActionHandler);
    mDispatcher.addHandler (mTestActionHandler);
  }

  @Test
  public void testDispatching ()
  {
    mDispatcher.handleURIAction (mContext, "/testvalue/last");
    assertActionCommandWasExecuted (mLastActionCommand);
  }

  @Test
  public void testPrecedence ()
  {
    mDispatcher.handleURIAction (mContext, "/testhandler");
    assertActionCommandWasExecuted (mTestActionCommand);
  }

  @Test
  public void testCaseSensitive ()
  {
    mDispatcher.setCaseSensitive (true);
    mDispatcher.handleURIAction (mContext, "/testvalue");
    assertOutcome ();
  }

  @Test
  public void testCaseInsensitive ()
  {
    mDispatcher.setCaseSensitive (false);
    mDispatcher.handleURIAction (mContext, "/TESTvalue");
    assertOutcome ();
  }

  @Test (expected = IllegalArgumentException.class)
  public void testConstructor_Fail ()
  {
    new StartsWithURLActionHandler ("  ");
  }

  private void assertOutcome ()
  {
    assertActionCommandWasExecuted (mStartsWithActionCommand);
    assertMatchedTokenFragments (mStartsWithActionHandler, new String[] { "value" });
  }

  private void assertActionCommandWasExecuted (TURLActionCommand command)
  {
    assertTrue (command.mExecuted);
  }

  private void assertMatchedTokenFragments (RegexURLActionHandler handler, String[] expectedTokenFragments)
  {
    assertTrue (Arrays.equals (expectedTokenFragments, handler.getMatchedTokenFragments ()));
  }
}
