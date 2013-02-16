/*
 * $Id: $ Copyright (C) 2007 Roland Krueger Created on 22.09.2012
 * 
 * Author: Roland Krueger (www.rolandkrueger.info)
 * 
 * This file is part of RoKlib.
 * 
 * This library is free software; you can redistribute it and/or modify it under the terms of the
 * GNU Lesser General Public License as published by the Free Software Foundation; either version
 * 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with this library;
 * if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA
 * 02111-1307 USA
 */
package info.rolandkrueger.roklib.webapps.urldispatching.test;

import static org.junit.Assert.assertTrue;
import info.rolandkrueger.roklib.util.net.IURLProvider;
import info.rolandkrueger.roklib.webapps.urldispatching.RegexURLActionHandler;
import info.rolandkrueger.roklib.webapps.urldispatching.StartsWithURLActionHandler;
import info.rolandkrueger.roklib.webapps.urldispatching.URLActionDispatcher;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

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
