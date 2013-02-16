/*
 * $Id: AbstractMemorySwappingDetectionThread.java 246 2011-01-19 17:03:10Z roland $
 * Copyright (C) 2007 Roland Krueger
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
package info.rolandkrueger.roklib.system;

/**
 * Abstract class for implementations of threads that are able to detect memory
 * paging events on the local system. Subclasses of this class can implement
 * their own specific methods for detecting memory paging by providing an
 * implementation for the abstract method
 * {@link AbstractMemorySwappingDetectionThread#isSwapEventDetected()}. This method is
 * repeatedly called during the lifetime of the detection thread.
 * 
 * @author Roland Krueger
 */
public abstract class AbstractMemorySwappingDetectionThread extends Thread
{
  private MemorySwappingEventListener mListener;
  private boolean mStopRequest = false; // true if thread is supposed to stop

  /**
   * Initializes this thread with the given listener.
   * 
   * @param listener
   *          class implementing the {@link MemorySwappingEventListener}
   *          interface which will be notified of swapping events
   */
  public AbstractMemorySwappingDetectionThread (MemorySwappingEventListener listener)
  {
    mListener = listener;
  }

  /**
   * Returns <code>true</code> if a memory paging event was detected
   */
  protected abstract boolean isSwapEventDetected ();

  /**
   * Implementations of this method define the actions that have to be taken
   * prior to stopping this thread.
   */
  protected abstract void stopThreadImpl ();

  /**
   * During its execution, the swapping detection thread will repeatedly call
   * {@link AbstractMemorySwappingDetectionThread#isSwapEventDetected()} and check if a
   * swapping event was detected. If so, the listener will be notified of this
   * event.<br>
   * <br>
   * <b>Important:</b> Since
   * {@link AbstractMemorySwappingDetectionThread#isSwapEventDetected()} will be called
   * within a loop, you must take care that the implementation of this method
   * will block the thread for a reasonable amount of time (e.g., via
   * {@link Thread#sleep(long)}. Otherwise this thread will consume too much CPU
   * time through the 'busy wait'.
   */
  @Override
  public void run ()
  {
    while (! mStopRequest)
    {
      if (isSwapEventDetected ())
      {
        notifyListenerOfSwappingEvent ();
      }
    }
  }

  /**
   * If a swapping event was detected, this method notifies the
   * {@link MemorySwappingEventListener} of that.
   */
  protected void notifyListenerOfSwappingEvent ()
  {
    mListener.memorySwappingDetected ();
  }

  /**
   * Stop this thread.
   */
  public final void stopThread ()
  {
    mStopRequest = true;
    stopThreadImpl ();
  }
}
