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

public class StopWatch {
    private long totalTime;
    private long startTime;
    private boolean isRunning;

    public StopWatch() {
        totalTime = 0;
        isRunning = false;
    }

    public long getTotalTime() {
        return totalTime;
    }

    public void start() {
        if (isRunning) {
            throw new IllegalStateException("Stop watch is already running.");
        }
        startTime = System.currentTimeMillis();
        isRunning = true;
    }

    public void stop() {
        if (!isRunning) {
            throw new IllegalStateException("Stop watch is currently not running.");
        }
        isRunning = false;
        calculateTotalTime();
    }

    public long getStartTime() {
        if (startTime == 0L) {
            throw new IllegalStateException("Stop watch has not yet been started.");
        }
        return startTime;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void reset() {
        isRunning = false;
        startTime = 0;
        totalTime = 0;
    }

    private void calculateTotalTime() {
        totalTime += System.currentTimeMillis() - startTime;
    }

    public long getSplitTime() {
        return System.currentTimeMillis() - startTime;
    }
}
