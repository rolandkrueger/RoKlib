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
package info.rolandkrueger.roklib.files;

import info.rolandkrueger.roklib.util.helper.CheckForNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

public class FileModificationWatchdog
{
  private final static long                   DEFAULT_WAIT_INTERVAL = 1000;
  private File                                mFile;
  private long                                mLastModified;
  private List<FileModificationListener>      mFileModificationListeners;
  private List<DirectoryModificationListener> mDirectoryModificationListeners;
  private long                                mWaitInterval;
  private Timer                               mTimer;
  private int                                 mDirectoryFileCount;
  private long                                mDirectoryFileHash;

  public FileModificationWatchdog (File watchedFileOrDirectory, long waitInterval) throws FileNotFoundException
  {
    CheckForNull.check (watchedFileOrDirectory);
    mFile = watchedFileOrDirectory;
    if (!mFile.exists ())
      throw new FileNotFoundException (
          String.format ("File %s to be watched does not exist.", mFile.getAbsolutePath ()));
    mLastModified = mFile.lastModified ();
    setWaitInterval (waitInterval);
    getDirectoryStatistics ();
    startWatching ();
  }

  public FileModificationWatchdog (File watchedFileOrDirectory) throws FileNotFoundException
  {
    this (watchedFileOrDirectory, DEFAULT_WAIT_INTERVAL);
  }

  public FileModificationWatchdog (File watchedDirectory, DirectoryModificationListener listener, long waitInterval)
      throws FileNotFoundException
  {
    this (watchedDirectory, waitInterval);
    CheckForNull.check (listener);
    addDirectoryModificationListener (listener);
  }

  public FileModificationWatchdog (File watchedDirectory, DirectoryModificationListener listener)
      throws FileNotFoundException
  {
    this (watchedDirectory, listener, DEFAULT_WAIT_INTERVAL);
  }

  public FileModificationWatchdog (File watchedFile, FileModificationListener listener, long waitInterval)
      throws FileNotFoundException
  {
    this (watchedFile, waitInterval);
    CheckForNull.check (listener);
    addFileModificationListener (listener);
  }

  public FileModificationWatchdog (File watchedFile, FileModificationListener listener) throws FileNotFoundException
  {
    this (watchedFile, listener, DEFAULT_WAIT_INTERVAL);
  }

  public void setWaitInterval (long interval)
  {
    if (interval < 1)
      throw new IllegalArgumentException ("Wait interval must be >= 1.");
    mWaitInterval = interval;
  }

  public void startWatching ()
  {
    if (mTimer == null)
      mTimer = new Timer (true);
    mTimer.scheduleAtFixedRate (new TimerTask ()
    {
      @Override
      public void run ()
      {
        if (!mFile.exists ())
        {
          fireFileDeleted ();
          return;
        }

        if (!mFile.isDirectory () && mFile.lastModified () != mLastModified)
          fireFileChanged ();

        if (mFile.isDirectory ())
        {
          int fileCount = getDirectoryFileCount ();
          if (mDirectoryFileHash != getDirectoryFileHash ())
          {
            fireFileChanged ();
          }

          if (mDirectoryFileCount > fileCount)
          {
            fireFilesRemoved ();
          }

          if (mDirectoryFileCount < fileCount)
          {
            fireFilesAdded ();
          }
        }
        mLastModified = mFile.lastModified ();
        getDirectoryStatistics ();
      }
    }, mWaitInterval, mWaitInterval);
  }

  public void stopWatching ()
  {
    mTimer.cancel ();
    mTimer = null;
  }

  private void getDirectoryStatistics ()
  {
    if (mFile.isDirectory ())
    {
      mDirectoryFileCount = getDirectoryFileCount ();
      mDirectoryFileHash = getDirectoryFileHash ();
    }
  }

  private int getDirectoryFileCount ()
  {
    return mFile.list ().length;
  }

  private long getDirectoryFileHash ()
  {
    long result = 0;
    for (File file : mFile.listFiles ())
    {
      result += file.lastModified ();
    }
    return result;
  }

  public void addFileModificationListener (FileModificationListener listener)
  {
    CheckForNull.check (listener);
    getFileModListenerList ().add (listener);
  }

  public void addDirectoryModificationListener (DirectoryModificationListener listener)
  {
    CheckForNull.check (listener);
    if (!isWatchedFileDirectory ())
      throw new IllegalStateException ("Cannot add directory modification listener. "
          + "Watched file is not a directory.");
    getDirModListenerList ().add (listener);
  }

  public void removeFileModificationListener (FileModificationListener listener)
  {
    getFileModListenerList ().remove (listener);
  }

  public void removeDirectoryModificationListener (DirectoryModificationListener listener)
  {
    getDirModListenerList ().remove (listener);
  }

  private List<DirectoryModificationListener> getDirModListenerList ()
  {
    if (mDirectoryModificationListeners == null)
      mDirectoryModificationListeners = new Vector<DirectoryModificationListener> (1);
    return mDirectoryModificationListeners;
  }

  private List<FileModificationListener> getFileModListenerList ()
  {
    if (mFileModificationListeners == null)
      mFileModificationListeners = new Vector<FileModificationListener> (1);
    return mFileModificationListeners;
  }

  public boolean isWatchedFileDirectory ()
  {
    return mFile.isDirectory ();
  }

  public File getWatchedFile ()
  {
    return mFile;
  }

  private void fireFilesAdded ()
  {
    for (DirectoryModificationListener listener : new ArrayList<DirectoryModificationListener> (
        getDirModListenerList ()))
    {
      listener.filesAdded (this);
    }
  }

  private void fireFilesRemoved ()
  {
    for (DirectoryModificationListener listener : new ArrayList<DirectoryModificationListener> (
        getDirModListenerList ()))
    {
      listener.filesRemoved (this);
    }
  }

  private void fireFileDeleted ()
  {
    for (DirectoryModificationListener listener : new ArrayList<DirectoryModificationListener> (
        getDirModListenerList ()))
    {
      listener.fileDeleted (this);
    }
    for (FileModificationListener listener : new ArrayList<FileModificationListener> (getDirModListenerList ()))
    {
      listener.fileDeleted (this);
    }
    stopWatching ();
  }

  private void fireFileChanged ()
  {
    for (DirectoryModificationListener listener : new ArrayList<DirectoryModificationListener> (
        getDirModListenerList ()))
    {
      listener.fileChanged (this);
    }
    for (FileModificationListener listener : new ArrayList<FileModificationListener> (getDirModListenerList ()))
    {
      listener.fileChanged (this);
    }
  }
}
