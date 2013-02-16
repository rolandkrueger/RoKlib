/*
 * $Id: PluginLoader.java 178 2010-10-31 18:01:20Z roland $
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.jar.Attributes;
import java.util.jar.JarInputStream;
import java.util.jar.Manifest;

/**
 * 
 * @author Roland Krueger
 */
public class PluginLoader<P extends AbstractPlugin>
{
  public final static String PLUGIN_MAINCLASS_PROPERTY = "PluginMainClass";

  private Set<String> mPropertiesToLoad;

  public PluginLoader ()
  {
    mPropertiesToLoad = new HashSet<String> ();
    mPropertiesToLoad.add (PLUGIN_MAINCLASS_PROPERTY);
  }

  public void getPropertyFromManifest (String propertyName)
  {
    mPropertiesToLoad.add (propertyName);
  }

  public void getPropertiesFromManifest (Collection<String> propertyNames)
  {
    for (String propertyName : propertyNames)
    {
      getPropertyFromManifest (propertyName);
    }
  }

  public P loadPlugin (File fromFile) throws IOException, InvalidManifestException,
      ClassNotFoundException, InstantiationException, IllegalAccessException
  {
    if (! fromFile.exists ()) throw new FileNotFoundException ();
    if (! fromFile.canRead ()) throw new IOException ("Cannot read from file.");

    try
    {
      return loadPlugin (fromFile.toURI ().toURL ());
    } catch (MalformedURLException e)
    {
      return null;
    }
  }

  @SuppressWarnings ("unchecked")
  public P loadPlugin (URL fromURL) throws IOException, InvalidManifestException,
      ClassNotFoundException, InstantiationException, IllegalAccessException
  {
    JarInputStream jarInputStream = new JarInputStream (fromURL.openStream ());
    Manifest manifest = jarInputStream.getManifest ();
    if (manifest == null)
    {
      throw new InvalidManifestException (String.format (
          "Plugin loaded from URL %s doesn't contain a manifest file.", fromURL.toString ()));
    }

    Attributes attributes = manifest.getMainAttributes ();
    Map<String, String> properties = new HashMap<String, String> ();
    for (String propertyName : mPropertiesToLoad)
    {
      String value = attributes.getValue (propertyName);
      if (value != null)
      {
        properties.put (propertyName, value);
      }
    }

    if (! properties.containsKey (PLUGIN_MAINCLASS_PROPERTY))
    {
      throw new InvalidManifestException (String.format (
          "Manifest attribute '%s' not found in plugin at URL %s.", PLUGIN_MAINCLASS_PROPERTY,
          fromURL.toString ()));
    }

    URLClassLoader classLoader = new URLClassLoader (new URL[] { fromURL });
    Class<?> pluginClass;

    pluginClass = classLoader.loadClass (properties.get (PLUGIN_MAINCLASS_PROPERTY));
    P plugin = (P) pluginClass.newInstance ();
    plugin.putAllProperties (properties);

    return plugin;
  }
}