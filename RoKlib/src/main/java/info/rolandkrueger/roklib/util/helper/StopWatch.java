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
package info.rolandkrueger.roklib.util.helper;

public class StopWatch
{
  private long    mTotalTime;
  private long    mStartTime;
  private boolean mIsRunning;

  public StopWatch ()
  {
    mTotalTime = 0;
    mIsRunning = false;
  }

  public long getTotalTime ()
  {
    return mTotalTime;
  }

  public void start ()
  {
    if (mIsRunning)
    {
      throw new IllegalStateException ("Stop watch is already running.");
    }
    mStartTime = System.currentTimeMillis ();
    mIsRunning = true;
  }

  public void stop ()
  {
    if (!mIsRunning)
    {
      throw new IllegalStateException ("Stop watch is currently not running.");
    }
    mIsRunning = false;
    calculateTotalTime ();
  }

  public long getStartTime ()
  {
    if (mStartTime == 0L)
    {
      throw new IllegalStateException ("Stop watch has not yet been started.");
    }
    return mStartTime;
  }

  public boolean isRunning ()
  {
    return mIsRunning;
  }

  public void reset ()
  {
    mIsRunning = false;
    mStartTime = 0;
    mTotalTime = 0;
  }

  private void calculateTotalTime ()
  {
    mTotalTime += System.currentTimeMillis () - mStartTime;
  }

  public long getSplitTime ()
  {
    return System.currentTimeMillis () - mStartTime;
  }
}
