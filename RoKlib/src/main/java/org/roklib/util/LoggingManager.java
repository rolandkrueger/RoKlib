/*
 * Copyright (C) 2007 - 2010 Roland Krueger
 * Created on 03.03.2010
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
package org.roklib.util;

import java.io.File;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.roklib.collections.IThreadLocalContainer;

public class LoggingManager implements IThreadLocalContainer
{
  private static final long                  serialVersionUID = -5618589916234093856L;
  private static ThreadLocal<LoggingManager> sInstance        = new ThreadLocal<LoggingManager> ();
  private static LoggingManager              sNullInstance;
  private static boolean                     sConfigured      = false;
  private String                             mLog4jConfigurationFile;

  public LoggingManager ()
  {
    this ("log4j.properties");
  }

  public LoggingManager (String log4jConfigurationFile)
  {
    mLog4jConfigurationFile = log4jConfigurationFile;
    configureLog4J ();
  }

  public Logger getLogger (Class<?> clazz)
  {
    return Logger.getLogger (clazz);
  }

  public void configureLog4J ()
  {
    if (!sConfigured)
    {
      if (mLog4jConfigurationFile != null)
      {
        PropertyConfigurator.configureAndWatch (mLog4jConfigurationFile);
        Logger log = getLogger (getClass ());
        if (log.isDebugEnabled ())
          log.debug ("Configured Log4J with logging properties from "
              + new File (mLog4jConfigurationFile).getAbsolutePath ());
      } else
      {
        PropertyConfigurator.configure (new Properties ());
      }
      sConfigured = true;
    }
  }

  public void resetCurrentInstance ()
  {
    sInstance.set (null);
    sInstance.remove ();
  }

  public void setCurrentInstance ()
  {
    sInstance.set (LoggingManager.this);
  }

  /**
   * This method is guaranteed to return a non-null result.
   * 
   * @return
   */
  public static LoggingManager getInstance ()
  {
    LoggingManager result = sInstance.get ();
    if (result == null)
    {
      if (sNullInstance == null)
        sNullInstance = new LoggingManager (null);
      result = sNullInstance;
      sInstance.set (sNullInstance);
    }
    return result;
  }
}
