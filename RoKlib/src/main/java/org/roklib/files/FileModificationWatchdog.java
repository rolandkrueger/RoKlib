/*
 * $Id: FileModificationWatchdog.java 178 2010-10-31 18:01:20Z roland $
 * Copyright (C) 2007 Roland Krueger
 * Created on 17.11.2009
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
package org.roklib.files;


import org.roklib.util.helper.CheckForNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class FileModificationWatchdog {
    private final static long DEFAULT_WAIT_INTERVAL = 1000;
    private final File file;
    private long lastModified;
    private List<FileModificationListener> fileModificationListeners;
    private List<DirectoryModificationListener> directoryModificationListeners;
    private long waitInterval;
    private Timer timer;
    private int directoryFileCount;
    private long directoryFileHash;

    public FileModificationWatchdog(File watchedFileOrDirectory, long waitInterval) throws FileNotFoundException {
        CheckForNull.check(watchedFileOrDirectory);
        file = watchedFileOrDirectory;
        if (!file.exists())
            throw new FileNotFoundException(
                    String.format("File %s to be watched does not exist.", file.getAbsolutePath()));
        lastModified = file.lastModified();
        setWaitInterval(waitInterval);
        getDirectoryStatistics();
        startWatching();
    }

    public FileModificationWatchdog(File watchedFileOrDirectory) throws FileNotFoundException {
        this(watchedFileOrDirectory, DEFAULT_WAIT_INTERVAL);
    }

    public FileModificationWatchdog(File watchedDirectory, DirectoryModificationListener listener, long waitInterval)
            throws FileNotFoundException {
        this(watchedDirectory, waitInterval);
        CheckForNull.check(listener);
        addDirectoryModificationListener(listener);
    }

    public FileModificationWatchdog(File watchedDirectory, DirectoryModificationListener listener)
            throws FileNotFoundException {
        this(watchedDirectory, listener, DEFAULT_WAIT_INTERVAL);
    }

    public FileModificationWatchdog(File watchedFile, FileModificationListener listener, long waitInterval)
            throws FileNotFoundException {
        this(watchedFile, waitInterval);
        CheckForNull.check(listener);
        addFileModificationListener(listener);
    }

    public FileModificationWatchdog(File watchedFile, FileModificationListener listener) throws FileNotFoundException {
        this(watchedFile, listener, DEFAULT_WAIT_INTERVAL);
    }

    public void setWaitInterval(long interval) {
        if (interval < 1)
            throw new IllegalArgumentException("Wait interval must be >= 1.");
        waitInterval = interval;
    }

    public void startWatching() {
        if (timer == null)
            timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (!file.exists()) {
                    fireFileDeleted();
                    return;
                }

                if (!file.isDirectory() && file.lastModified() != lastModified)
                    fireFileChanged();

                if (file.isDirectory()) {
                    int fileCount = getDirectoryFileCount();
                    if (directoryFileHash != getDirectoryFileHash()) {
                        fireFileChanged();
                    }

                    if (directoryFileCount > fileCount) {
                        fireFilesRemoved();
                    }

                    if (directoryFileCount < fileCount) {
                        fireFilesAdded();
                    }
                }
                lastModified = file.lastModified();
                getDirectoryStatistics();
            }
        }, waitInterval, waitInterval);
    }

    public void stopWatching() {
        timer.cancel();
        timer = null;
    }

    private void getDirectoryStatistics() {
        if (file.isDirectory()) {
            directoryFileCount = getDirectoryFileCount();
            directoryFileHash = getDirectoryFileHash();
        }
    }

    private int getDirectoryFileCount() {
        return file.list().length;
    }

    private long getDirectoryFileHash() {
        long result = 0;
        for (File file : this.file.listFiles()) {
            result += file.lastModified();
        }
        return result;
    }

    public void addFileModificationListener(FileModificationListener listener) {
        CheckForNull.check(listener);
        getFileModListenerList().add(listener);
    }

    public void addDirectoryModificationListener(DirectoryModificationListener listener) {
        CheckForNull.check(listener);
        if (!isWatchedFileDirectory())
            throw new IllegalStateException("Cannot add directory modification listener. "
                    + "Watched file is not a directory.");
        getDirModListenerList().add(listener);
    }

    public void removeFileModificationListener(FileModificationListener listener) {
        getFileModListenerList().remove(listener);
    }

    public void removeDirectoryModificationListener(DirectoryModificationListener listener) {
        getDirModListenerList().remove(listener);
    }

    private List<DirectoryModificationListener> getDirModListenerList() {
        if (directoryModificationListeners == null)
            directoryModificationListeners = new Vector<DirectoryModificationListener>(1);
        return directoryModificationListeners;
    }

    private List<FileModificationListener> getFileModListenerList() {
        if (fileModificationListeners == null)
            fileModificationListeners = new Vector<FileModificationListener>(1);
        return fileModificationListeners;
    }

    public boolean isWatchedFileDirectory() {
        return file.isDirectory();
    }

    public File getWatchedFile() {
        return file;
    }

    private void fireFilesAdded() {
        for (DirectoryModificationListener listener : new ArrayList<DirectoryModificationListener>(
                getDirModListenerList())) {
            listener.filesAdded(this);
        }
    }

    private void fireFilesRemoved() {
        for (DirectoryModificationListener listener : new ArrayList<DirectoryModificationListener>(
                getDirModListenerList())) {
            listener.filesRemoved(this);
        }
    }

    private void fireFileDeleted() {
        for (DirectoryModificationListener listener : new ArrayList<DirectoryModificationListener>(
                getDirModListenerList())) {
            listener.fileDeleted(this);
        }
        for (FileModificationListener listener : new ArrayList<FileModificationListener>(getDirModListenerList())) {
            listener.fileDeleted(this);
        }
        stopWatching();
    }

    private void fireFileChanged() {
        for (DirectoryModificationListener listener : new ArrayList<DirectoryModificationListener>(
                getDirModListenerList())) {
            listener.fileChanged(this);
        }
        for (FileModificationListener listener : new ArrayList<FileModificationListener>(getDirModListenerList())) {
            listener.fileChanged(this);
        }
    }
}
