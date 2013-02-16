/*
 * $Id: AbstractPlugin.java 178 2010-10-31 18:01:20Z roland $
 * Copyright (C) 2007 Roland Krueger
 * Created on 03.11.2009
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
/**
 * 
 */
package info.rolandkrueger.roklib.plugins;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.InvalidPropertiesFormatException;
import java.util.Map;
import java.util.Properties;

/**
 * 
 * @author Roland Krueger
 */
public abstract class AbstractPlugin
{
  private Properties mProperties;

  public AbstractPlugin ()
  {
    mProperties = new Properties ();
  }

  public String getProperty (String key, String defaultValue)
  {
    return mProperties.getProperty (key, defaultValue);
  }

  public String getProperty (String key)
  {
    return mProperties.getProperty (key);
  }

  public Properties getProperties ()
  {
    return mProperties;
  }

  public void loadProperties (InputStream inStream) throws IOException
  {
    mProperties.load (inStream);
  }

  public void loadProperties (Reader reader) throws IOException
  {
    mProperties.load (reader);
  }

  public void loadPropertiesFromXML (InputStream in) throws IOException,
      InvalidPropertiesFormatException
  {
    mProperties.loadFromXML (in);
  }

  public Object putProperty (Object key, Object value)
  {
    return mProperties.put (key, value);
  }

  public void putAllProperties (Map<? extends Object, ? extends Object> t)
  {
    mProperties.putAll (t);
  }
}
