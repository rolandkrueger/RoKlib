/*
 * $Id: LoggingManager.java 181 2010-11-01 09:39:13Z roland $
 * Copyright (C) 2007 - 2010 Roland Krueger
 * Created on 03.03.2010
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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307
 * USA
 */
package info.rolandkrueger.roklib.util;


import java.io.File;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;


public class LoggingManager implements IThreadLocalContainer
{
  private static final long serialVersionUID = - 5618589916234093856L;
  private static ThreadLocal<LoggingManager> sInstance = new ThreadLocal<LoggingManager> ();
  private static LoggingManager sNullInstance;
  private static boolean sConfigured = false;
  private String mLog4jConfigurationFile;

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
    if (! sConfigured)
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
      if (sNullInstance == null) sNullInstance = new LoggingManager (null);
      result = sNullInstance;
      sInstance.set (sNullInstance);
    }
    return result;
  }
}
