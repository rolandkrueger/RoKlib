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
package info.rolandkrueger.roklib.webapps.urldispatching.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import info.rolandkrueger.roklib.util.net.IURLProvider;
import info.rolandkrueger.roklib.webapps.urldispatching.DispatchingURLActionHandler;
import info.rolandkrueger.roklib.webapps.urldispatching.RegexURLActionHandler;
import info.rolandkrueger.roklib.webapps.urldispatching.URLActionDispatcher;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

public class RegexURLActionHandlerTest
{
  private URLActionDispatcher         mDispatcher;

  private TURLActionHandler           mTtestActionHandler;

  private TURLActionCommand           mTestActionCommand;

  private TURLActionHandler           mLastActionHandler;

  private TURLActionCommand           mLastActionCommand;

  private DispatchingURLActionHandler mMiddleActionHandler;

  private TURLActionCommand           mMiddleActionCommand;

  private TURLActionCommand           mRegexActionCommand1;

  private RegexURLActionHandler       mRegexActionHandler1;

  private TURLActionCommand           mRegexActionCommand2;

  private RegexURLActionHandler       mRegexActionHandler2;

  private URL                         mContext;

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
    mTtestActionHandler = new TURLActionHandler ("1test_x", mTestActionCommand);

    mRegexActionCommand1 = new TURLActionCommand ();
    mRegexActionCommand2 = new TURLActionCommand ();

    // first regex action handler is responsible for URLs like '1test_abc' or '2test_123test'
    mRegexActionHandler1 = new RegexURLActionHandler ("(\\d)test_(.*)");
    mRegexActionHandler1.setRootCommand (mRegexActionCommand1);

    // second regex action handler is responsible for URLs like '3test_5xxx' or '12test_9yyy'
    mRegexActionHandler2 = new RegexURLActionHandler ("(\\d{1,2})test_(\\d\\w+)");
    mRegexActionHandler2.setRootCommand (mRegexActionCommand2);

    mLastActionCommand = new TURLActionCommand ();
    mLastActionHandler = new TURLActionHandler ("last", mLastActionCommand);

    mMiddleActionCommand = new TURLActionCommand ();
    mMiddleActionHandler = new DispatchingURLActionHandler ("middle");
    mMiddleActionHandler.setRootCommand (mMiddleActionCommand);

    mRegexActionHandler2.addSubHandler (mMiddleActionHandler);
    mMiddleActionHandler.addSubHandler (mLastActionHandler);

    mDispatcher.addHandler (mRegexActionHandler1);
    mDispatcher.addHandler (mTtestActionHandler);
    mDispatcher.addHandler (mRegexActionHandler2); // add second regex handler last, so that it has
                                                   // least precedence
  }

  @Test
  public void testDispatching ()
  {
    mDispatcher.handleURIAction (mContext, "/23test_123/middle/last");
    assertActionCommandWasExecuted (mLastActionCommand);
  }

  @Test
  public void testURLDecoding ()
  {
    mDispatcher.handleURIAction (mContext, "/3test_%22xx+xx%22");
    assertActionCommandWasExecuted (mRegexActionCommand1);
    assertMatchedTokenFragments (mRegexActionHandler1, new String[] { "3", "\"xx xx\"" });
  }

  @Test
  public void testCaseInsensitive ()
  {
    mDispatcher.setCaseSensitive (false);
    mDispatcher.handleURIAction (mContext, "/1TEST_x");

    // the dispatching action handler is added second to the dispatcher, but it has highest
    // precedence
    assertActionCommandWasExecuted (mTestActionCommand);
    resetActionCommands ();

    mDispatcher.handleURIAction (mContext, "/2TEST_2x");
    assertActionCommandWasExecuted (mRegexActionCommand1);
    assertMatchedTokenFragments (mRegexActionHandler1, new String[] { "2", "2x" });
    resetActionCommands ();

    mDispatcher.handleURIAction (mContext, "12TEST_2xxx");
    assertActionCommandWasExecuted (mRegexActionCommand2);
    assertMatchedTokenFragments (mRegexActionHandler2, new String[] { "12", "2xxx" });
  }

  @Test
  public void testCaseSensitive ()
  {
    mDispatcher.setCaseSensitive (true);

    mDispatcher.handleURIAction (mContext, "/1test_x");

    // the dispatching action handler is added second to the dispatcher, but it has highest
    // precedence
    assertActionCommandWasExecuted (mTestActionCommand);
    resetActionCommands ();

    mDispatcher.handleURIAction (mContext, "/2test_abc");
    assertActionCommandWasExecuted (mRegexActionCommand1);
    assertMatchedTokenFragments (mRegexActionHandler1, new String[] { "2", "abc" });
    resetActionCommands ();

    mDispatcher.handleURIAction (mContext, "12test_2xxx");
    assertActionCommandWasExecuted (mRegexActionCommand2);
    assertMatchedTokenFragments (mRegexActionHandler2, new String[] { "12", "2xxx" });
  }

  @Test
  public void testGetParameterizedActionURI ()
  {
    mRegexActionHandler2.setURIToken ("17test_23some_value");
    assertEquals ("http://localhost:8080/17test_23some_value/middle/last", mLastActionHandler
        .getParameterizedActionURL (true).toString ());
    mRegexActionHandler2.setURIToken ("99test_9999");
    assertEquals ("http://localhost:8080/99test_9999/middle/last", mLastActionHandler.getParameterizedActionURL (true)
        .toString ());
  }

  @Test (expected = IllegalArgumentException.class)
  public void testSetActionURI_Failure ()
  {
    mRegexActionHandler2.setURIToken ("does_not_match_with_regex");
  }

  @Test (expected = IllegalArgumentException.class)
  public void testConstructor_Fail ()
  {
    new RegexURLActionHandler ("  ");
  }

  @Test
  public void testGetMatchedTokenFragmentCount ()
  {
    // if a regex action handler has not been evaluated yet, its matched token fragment count is 0,
    // even if the underlying array is still null
    assertEquals (0, mRegexActionHandler2.getMatchedTokenFragmentCount ());
    mDispatcher.handleURIAction (mContext, "12test_2xxx");
    assertEquals (2, mRegexActionHandler2.getMatchedTokenFragmentCount ());
  }

  private void assertActionCommandWasExecuted (TURLActionCommand command)
  {
    assertTrue (command.mExecuted);
  }

  private void assertMatchedTokenFragments (RegexURLActionHandler handler, String[] expectedTokenFragments)
  {
    assertEquals (expectedTokenFragments.length, handler.getMatchedTokenFragmentCount ());
    assertTrue (Arrays.equals (expectedTokenFragments, handler.getMatchedTokenFragments ()));
  }

  private void resetActionCommands ()
  {
    mTestActionCommand.mExecuted = false;
    mRegexActionCommand1.mExecuted = false;
    mRegexActionCommand2.mExecuted = false;
  }

}
