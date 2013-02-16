/*
 * $Id: StopWatch.java 260 2011-01-27 19:51:26Z roland $
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
package info.rolandkrueger.roklib.util.helper;

public class StopWatch
{
  private long mTotalTime;
  private long mStartTime;
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
