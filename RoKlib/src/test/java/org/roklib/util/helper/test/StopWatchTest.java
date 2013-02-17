/*
 * Copyright (C) 2007 Roland Krueger
 * Created on 27.01.2011
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
package org.roklib.util.helper.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.roklib.util.helper.StopWatch;

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

  @Test (expected = IllegalStateException.class)
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

  @Test (expected = IllegalStateException.class)
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

  @Test (expected = IllegalStateException.class)
  public void testStopWhenNotRunning ()
  {
    mTestObj.stop ();
  }
}
