/*
 * $Id: StopWatchTest.java 260 2011-01-27 19:51:26Z roland $
 * Copyright (C) 2007 Roland Krueger
 * Created on 27.01.2011
 *
 * Author: Roland Krueger (www.rolandkrueger.info)
 *
 * This file is part of RoKlib.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307
 * USA
 */
package info.rolandkrueger.roklib.util.helper.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import info.rolandkrueger.roklib.util.helper.StopWatch;

public class StopWatchTest
{
  private StopWatch mTestObj;
  
  @Before
  public void setUp ()
  {
    mTestObj = new StopWatch ();
  }
  
  @Test
  public void testGetTotalTimeForNewInstance ()
  {
    assertEquals (0L, mTestObj.getTotalTime ());
  }
  
  @Test
  public void testGetTotalTime () throws InterruptedException
  {
    mTestObj.start ();
    Thread.sleep (30);
    mTestObj.stop ();
    long totalTime1 = mTestObj.getTotalTime ();
    Thread.sleep (150);
    mTestObj.start ();
    Thread.sleep (20);
    mTestObj.stop ();
    long totalTime2 = mTestObj.getTotalTime ();
    assertTrue (totalTime1 < totalTime2);
    assertTrue (totalTime2 < 100);
  }
  
  @Test (expected=IllegalStateException.class)
  public void testStartFailsWhenRunning ()
  {
    mTestObj.start ();
    mTestObj.start ();
  }
  
  @Test
  public void testIsRunning ()
  {
    assertFalse (mTestObj.isRunning ());
    mTestObj.start ();
    assertTrue (mTestObj.isRunning ());
    mTestObj.stop ();
    assertFalse (mTestObj.isRunning ());    
  }
  
  @Test
  public void testGetSplitTime () throws InterruptedException
  {
    mTestObj.start ();
    Thread.sleep (100);
    long splitTime1 = mTestObj.getSplitTime ();
    assertTrue (mTestObj.isRunning ());
    Thread.sleep (50);
    long splitTime2 = mTestObj.getSplitTime ();
    Thread.sleep (50);
    mTestObj.stop ();
    long totalTime = mTestObj.getTotalTime ();
    assertTrue (splitTime1 < splitTime2);
    assertTrue (splitTime1 < totalTime);
    assertTrue (splitTime2 < totalTime);
  }
  
  @Test
  public void testReset () throws InterruptedException
  {
    mTestObj.start ();
    Thread.sleep (10);
    mTestObj.stop ();
    assertTrue (mTestObj.getTotalTime () > 0L);
    mTestObj.reset ();
    assertEquals (0L, mTestObj.getTotalTime ());
    assertFalse (mTestObj.isRunning ());
  }
  
  @Test (expected=IllegalStateException.class)
  public void testGetStartTimeFailsWhenNotStarted ()
  {
    mTestObj.getStartTime ();
  }
  
  @Test
  public void testGetStartTimeAfterStart ()
  {
    mTestObj.start ();
    assertTrue (mTestObj.getStartTime () > 0L);
  }
  
  @Test
  public void testStart () throws InterruptedException
  {
    mTestObj.start ();
    long startTime = mTestObj.getStartTime ();
    Thread.sleep (50);
    mTestObj.stop ();
    assertEquals (startTime, mTestObj.getStartTime ());
    assertTrue (mTestObj.getTotalTime () > 10L);
  }
  
  @Test (expected=IllegalStateException.class)
  public void testStopWhenNotRunning ()
  {
    mTestObj.stop ();    
  }
}
