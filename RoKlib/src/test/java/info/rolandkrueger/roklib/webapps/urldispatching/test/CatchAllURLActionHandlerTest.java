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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import info.rolandkrueger.roklib.util.net.IURLProvider;
import info.rolandkrueger.roklib.webapps.urldispatching.CatchAllURLActionHandler;
import info.rolandkrueger.roklib.webapps.urldispatching.URLActionDispatcher;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Before;
import org.junit.Test;

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
