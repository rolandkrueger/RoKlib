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
package org.roklib.webapps.uridispatching.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.roklib.webapps.uridispatching.DispatchingURIActionHandler;
import org.roklib.webapps.uridispatching.RegexURIActionHandler;
import org.roklib.webapps.uridispatching.URIActionDispatcher;

public class RegexURIActionHandlerTest
{
  private URIActionDispatcher         mDispatcher;
  private TURIActionHandler           mTtestActionHandler;
  private TURIActionCommand           mTestActionCommand;
  private TURIActionHandler           mLastActionHandler;
  private TURIActionCommand           mLastActionCommand;
  private DispatchingURIActionHandler mMiddleActionHandler;
  private TURIActionCommand           mMiddleActionCommand;
  private TURIActionCommand           mRegexActionCommand1;
  private RegexURIActionHandler       mRegexActionHandler1;
  private TURIActionCommand           mRegexActionCommand2;
  private RegexURIActionHandler       mRegexActionHandler2;

  @Before
  public void setUp ()
  {
    mDispatcher = new URIActionDispatcher (false);

    mTestActionCommand = new TURIActionCommand ();
    mTtestActionHandler = new TURIActionHandler ("1test_x", mTestActionCommand);

    mRegexActionCommand1 = new TURIActionCommand ();
    mRegexActionCommand2 = new TURIActionCommand ();

    // first regex action handler is responsible for URIs like '1test_abc' or '2test_123test'
    mRegexActionHandler1 = new RegexURIActionHandler ("(\\d)test_(.*)");
    mRegexActionHandler1.setRootCommand (mRegexActionCommand1);

    // second regex action handler is responsible for URIs like '3test_5xxx' or '12test_9yyy'
    mRegexActionHandler2 = new RegexURIActionHandler ("(\\d{1,2})test_(\\d\\w+)");
    mRegexActionHandler2.setRootCommand (mRegexActionCommand2);

    mLastActionCommand = new TURIActionCommand ();
    mLastActionHandler = new TURIActionHandler ("last", mLastActionCommand);

    mMiddleActionCommand = new TURIActionCommand ();
    mMiddleActionHandler = new DispatchingURIActionHandler ("middle");
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
    mDispatcher.handleURIAction ("/23test_123/middle/last");
    assertActionCommandWasExecuted (mLastActionCommand);
  }

  @Test
  public void testURLDecoding ()
  {
    mDispatcher.handleURIAction ("/3test_%22xx+xx%22");
    assertActionCommandWasExecuted (mRegexActionCommand1);
    assertMatchedTokenFragments (mRegexActionHandler1, new String[] { "3", "\"xx xx\"" });
  }

  @Test
  public void testCaseInsensitive ()
  {
    mDispatcher.setCaseSensitive (false);
    mDispatcher.handleURIAction ("/1TEST_x");

    // the dispatching action handler is added second to the dispatcher, but it has highest
    // precedence
    assertActionCommandWasExecuted (mTestActionCommand);
    resetActionCommands ();

    mDispatcher.handleURIAction ("/2TEST_2x");
    assertActionCommandWasExecuted (mRegexActionCommand1);
    assertMatchedTokenFragments (mRegexActionHandler1, new String[] { "2", "2x" });
    resetActionCommands ();

    mDispatcher.handleURIAction ("12TEST_2xxx");
    assertActionCommandWasExecuted (mRegexActionCommand2);
    assertMatchedTokenFragments (mRegexActionHandler2, new String[] { "12", "2xxx" });
  }

  @Test
  public void testCaseSensitive ()
  {
    mDispatcher.setCaseSensitive (true);

    mDispatcher.handleURIAction ("/1test_x");

    // the dispatching action handler is added second to the dispatcher, but it has highest
    // precedence
    assertActionCommandWasExecuted (mTestActionCommand);
    resetActionCommands ();

    mDispatcher.handleURIAction ("/2test_abc");
    assertActionCommandWasExecuted (mRegexActionCommand1);
    assertMatchedTokenFragments (mRegexActionHandler1, new String[] { "2", "abc" });
    resetActionCommands ();

    mDispatcher.handleURIAction ("12test_2xxx");
    assertActionCommandWasExecuted (mRegexActionCommand2);
    assertMatchedTokenFragments (mRegexActionHandler2, new String[] { "12", "2xxx" });
  }

  @Test
  public void testGetParameterizedActionURI ()
  {
    mRegexActionHandler2.setURIToken ("17test_23some_value");
    assertEquals ("/17test_23some_value/middle/last", mLastActionHandler.getParameterizedActionURI (true).toString ());
    mRegexActionHandler2.setURIToken ("99test_9999");
    assertEquals ("/99test_9999/middle/last", mLastActionHandler.getParameterizedActionURI (true).toString ());
  }

  @Test (expected = IllegalArgumentException.class)
  public void testSetActionURI_Failure ()
  {
    mRegexActionHandler2.setURIToken ("does_not_match_with_regex");
  }

  @Test (expected = IllegalArgumentException.class)
  public void testConstructor_Fail ()
  {
    new RegexURIActionHandler ("  ");
  }

  @Test
  public void testGetMatchedTokenFragmentCount ()
  {
    // if a regex action handler has not been evaluated yet, its matched token fragment count is 0,
    // even if the underlying array is still null
    assertEquals (0, mRegexActionHandler2.getMatchedTokenFragmentCount ());
    mDispatcher.handleURIAction ("12test_2xxx");
    assertEquals (2, mRegexActionHandler2.getMatchedTokenFragmentCount ());
  }

  private void assertActionCommandWasExecuted (TURIActionCommand command)
  {
    assertTrue (command.mExecuted);
  }

  private void assertMatchedTokenFragments (RegexURIActionHandler handler, String[] expectedTokenFragments)
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
