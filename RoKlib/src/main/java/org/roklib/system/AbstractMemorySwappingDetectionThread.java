/*
 * Copyright (C) 2007 Roland Krueger
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
package org.roklib.system;

/**
 * Abstract class for implementations of threads that are able to detect memory paging events on the local system.
 * Subclasses of this class can implement their own specific methods for detecting memory paging by providing an
 * implementation for the abstract method {@link AbstractMemorySwappingDetectionThread#isSwapEventDetected()}. This
 * method is repeatedly called during the lifetime of the detection thread.
 *
 * @author Roland Krueger
 */
public abstract class AbstractMemorySwappingDetectionThread extends Thread {
    private final MemorySwappingEventListener listener;
    private boolean stopRequest = false; // true if thread is supposed to stop

    /**
     * Initializes this thread with the given listener.
     *
     * @param listener class implementing the {@link MemorySwappingEventListener} interface which will be notified of swapping
     *                 events
     */
    public AbstractMemorySwappingDetectionThread(MemorySwappingEventListener listener) {
        this.listener = listener;
    }

    /**
     * Returns <code>true</code> if a memory paging event was detected
     */
    protected abstract boolean isSwapEventDetected();

    /**
     * Implementations of this method define the actions that have to be taken prior to stopping this thread.
     */
    protected abstract void stopThreadImpl();

    /**
     * During its execution, the swapping detection thread will repeatedly call
     * {@link AbstractMemorySwappingDetectionThread#isSwapEventDetected()} and check if a swapping event was detected. If
     * so, the listener will be notified of this event.<br>
     * <br>
     * <b>Important:</b> Since {@link AbstractMemorySwappingDetectionThread#isSwapEventDetected()} will be called within a
     * loop, you must take care that the implementation of this method will block the thread for a reasonable amount of
     * time (e.g., via {@link Thread#sleep(long)}. Otherwise this thread will consume too much CPU time through the 'busy
     * wait'.
     */
    @Override
    public void run() {
        while (!stopRequest) {
            if (isSwapEventDetected()) {
                notifyListenerOfSwappingEvent();
            }
        }
    }

    /**
     * If a swapping event was detected, this method notifies the {@link MemorySwappingEventListener} of that.
     */
    protected void notifyListenerOfSwappingEvent() {
        listener.memorySwappingDetected();
    }

    /**
     * Stop this thread.
     */
    public final void stopThread() {
        stopRequest = true;
        stopThreadImpl();
    }
}
