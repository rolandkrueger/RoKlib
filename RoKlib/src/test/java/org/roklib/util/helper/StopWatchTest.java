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
package org.roklib.util.helper;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class StopWatchTest {
    private StopWatch testObj;

    @Before
    public void setUp() {
        testObj = new StopWatch();
    }

    @Test
    public void testGetTotalTimeForNewInstance() {
        assertEquals(0L, testObj.getTotalTime());
    }

    @Test
    public void testGetTotalTime() throws InterruptedException {
        testObj.start();
        Thread.sleep(30);
        testObj.stop();
        long totalTime1 = testObj.getTotalTime();
        Thread.sleep(150);
        testObj.start();
        Thread.sleep(20);
        testObj.stop();
        long totalTime2 = testObj.getTotalTime();
        assertTrue(totalTime1 < totalTime2);
        assertTrue(totalTime2 < 100);
    }

    @Test(expected = IllegalStateException.class)
    public void testStartFailsWhenRunning() {
        testObj.start();
        testObj.start();
    }

    @Test
    public void testIsRunning() {
        assertFalse(testObj.isRunning());
        testObj.start();
        assertTrue(testObj.isRunning());
        testObj.stop();
        assertFalse(testObj.isRunning());
    }

    @Test
    public void testGetSplitTime() throws InterruptedException {
        testObj.start();
        Thread.sleep(100);
        long splitTime1 = testObj.getSplitTime();
        assertTrue(testObj.isRunning());
        Thread.sleep(50);
        long splitTime2 = testObj.getSplitTime();
        Thread.sleep(50);
        testObj.stop();
        long totalTime = testObj.getTotalTime();
        assertTrue(splitTime1 < splitTime2);
        assertTrue(splitTime1 < totalTime);
        assertTrue(splitTime2 < totalTime);
    }

    @Test
    public void testReset() throws InterruptedException {
        testObj.start();
        Thread.sleep(10);
        testObj.stop();
        assertTrue(testObj.getTotalTime() > 0L);
        testObj.reset();
        assertEquals(0L, testObj.getTotalTime());
        assertFalse(testObj.isRunning());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetStartTimeFailsWhenNotStarted() {
        testObj.getStartTime();
    }

    @Test
    public void testGetStartTimeAfterStart() {
        testObj.start();
        assertTrue(testObj.getStartTime() > 0L);
    }

    @Test
    public void testStart() throws InterruptedException {
        testObj.start();
        long startTime = testObj.getStartTime();
        Thread.sleep(50);
        testObj.stop();
        assertEquals(startTime, testObj.getStartTime());
        assertTrue(testObj.getTotalTime() > 10L);
    }

    @Test(expected = IllegalStateException.class)
    public void testStopWhenNotRunning() {
        testObj.stop();
    }
}
