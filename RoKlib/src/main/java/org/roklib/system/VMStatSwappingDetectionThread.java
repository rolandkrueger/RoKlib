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

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class is a concrete implementation of the abstract class {@link AbstractMemorySwappingDetectionThread} where the
 * program <code>vmstat</code> is used for detecting memory swapping. When the thread is started, <code>vmstat</code> is
 * invoked with an update delay of a configurable number of seconds. The thread will then evaluate the output of
 * <code>vmstat</code> upon each update. If the swap in/out column of <code>vmstat</code>'s output differs from 0 the
 * object registered as {@link MemorySwappingEventListener} will be notified of that event. <br>
 * <br>
 * Note: Detecting memory paging with an external, platform-dependent tool such as <code>vmstat</code> is of course
 * detrimental to the application's portability. This strategy was chosen, though, since it offered an easy to implement
 * method for swapping detection. Unfortunately, the Java language as yet doesn't offer any general machanisms to detect
 * these memory paging situations in a portable way. If there are any better ways to cope with that in the future, an
 * appropriate method can be implemented as a subclass of {@link AbstractMemorySwappingDetectionThread}.
 *
 * @author Roland Krueger
 */
public class VMStatSwappingDetectionThread extends AbstractMemorySwappingDetectionThread {
    protected static final Logger LOG = Logger.getLogger(VMStatSwappingDetectionThread.class.getPackage()
            .getName());
    private final static String CMD_STRING = "%s %d";

    /**
     * Regular expression that extracts the swap in and swap out columns of the <code>vmstat</code> output.
     */
    private final static Pattern SWAP_PATTERN = Pattern.compile("^(?:\\s*\\d+){6}\\s*(\\d+)\\s*(\\d+).*$");

    private final String vmStatBinaryLocation;
    private int samplingRate;
    private Process vmStatProcess;
    private BufferedReader processReader;

    /**
     * Constructor.
     *
     * @param listener             object that implements the {@link MemorySwappingEventListener} interface
     * @param vmstatBinaryLocation the location of the <code>vmstat</code> command. This is usually <code>/usr/bin/vmstat</code>.
     * @param samplingRate         the update delay for <code>vmstat</code>
     */
    public VMStatSwappingDetectionThread(MemorySwappingEventListener listener, String vmstatBinaryLocation,
                                         int samplingRate) {
        super(listener);
        vmStatBinaryLocation = vmstatBinaryLocation;
        if (vmStatBinaryLocation.matches("\\s"))
            throw new IllegalArgumentException(
                    "Using a whitespace character in the vmstat binary location string is not allowed.");

        File vmstatBinary = new File(vmStatBinaryLocation);
        if (!vmstatBinary.canRead())
            throw new IllegalArgumentException("Unable to read the given file for the vmstat binary.");

        this.samplingRate = samplingRate;
        if (samplingRate <= 0)
            throw new IllegalArgumentException("Sampling rate must be at least 1.");
    }

    /**
     * Starts the <code>vmstat</code> command in an own process.
     *
     * @return <code>true</code> if the process was successfully started
     */
    public synchronized boolean startProcess() {
        // start vmstat and parse its output
        ProcessBuilder procBuilder = new ProcessBuilder(String.format(CMD_STRING, vmStatBinaryLocation, samplingRate)
                .split(" "));
        procBuilder.redirectErrorStream(true);

        try {
            vmStatProcess = procBuilder.start();
            processReader = new BufferedReader(new InputStreamReader(vmStatProcess.getInputStream()));
        } catch (IOException ioExc) {
            LOG.warning("Unable to start vmstat process with binary :" + vmStatBinaryLocation);
            return false;
        }
        super.start();
        return true;
    }

    @Override
    protected boolean isSwapEventDetected() {
        if (processReader == null)
            throw new IllegalStateException("Thread must be started with startProcess().");
        final String mLine;
        try {
            mLine = processReader.readLine();
            if (mLine == null) {
                LOG.info("vmstat process has finished.");
                stopThread();
                return false;
            }
        } catch (IOException e) {
            stopThread();
            return false;
        }

        final Matcher mMatcher = SWAP_PATTERN.matcher(mLine);
        if (mMatcher.matches()) {
            if (!mMatcher.group(1).equals("0") || !mMatcher.group(2).equals("0"))
                return true;
        }

        return false;
    }

    @Override
    protected void stopThreadImpl() {
        if (vmStatProcess != null) {
            LOG.info("Stopping vmstat process.");
            vmStatProcess.destroy();
        }
    }

}
