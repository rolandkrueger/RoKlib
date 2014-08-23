/*
 * Copyright (C) 2007 Roland Krueger
 * Created on 03.11.2009
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
package org.roklib.plugins;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.jar.Attributes;
import java.util.jar.JarInputStream;
import java.util.jar.Manifest;

/**
 * @author Roland Krueger
 */
public class PluginLoader<P extends AbstractPlugin> {
    public final static String PLUGIN_MAINCLASS_PROPERTY = "PluginMainClass";

    private Set<String> mPropertiesToLoad;

    public PluginLoader() {
        mPropertiesToLoad = new HashSet<String>();
        mPropertiesToLoad.add(PLUGIN_MAINCLASS_PROPERTY);
    }

    public void getPropertyFromManifest(String propertyName) {
        mPropertiesToLoad.add(propertyName);
    }

    public void getPropertiesFromManifest(Collection<String> propertyNames) {
        for (String propertyName : propertyNames) {
            getPropertyFromManifest(propertyName);
        }
    }

    public P loadPlugin(File fromFile) throws IOException, InvalidManifestException, ClassNotFoundException,
            InstantiationException, IllegalAccessException {
        if (!fromFile.exists())
            throw new FileNotFoundException();
        if (!fromFile.canRead())
            throw new IOException("Cannot read from file.");

        try {
            return loadPlugin(fromFile.toURI().toURL());
        } catch (MalformedURLException e) {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public P loadPlugin(URL fromURL) throws IOException, InvalidManifestException, ClassNotFoundException,
            InstantiationException, IllegalAccessException {
        JarInputStream jarInputStream = new JarInputStream(fromURL.openStream());
        Manifest manifest = jarInputStream.getManifest();
        if (manifest == null) {
            throw new InvalidManifestException(String.format("Plugin loaded from URL %s doesn't contain a manifest file.",
                    fromURL.toString()));
        }

        Attributes attributes = manifest.getMainAttributes();
        Map<String, String> properties = new HashMap<String, String>();
        for (String propertyName : mPropertiesToLoad) {
            String value = attributes.getValue(propertyName);
            if (value != null) {
                properties.put(propertyName, value);
            }
        }

        if (!properties.containsKey(PLUGIN_MAINCLASS_PROPERTY)) {
            throw new InvalidManifestException(String.format("Manifest attribute '%s' not found in plugin at URL %s.",
                    PLUGIN_MAINCLASS_PROPERTY, fromURL.toString()));
        }

        URLClassLoader classLoader = new URLClassLoader(new URL[]{fromURL});
        Class<?> pluginClass;

        pluginClass = classLoader.loadClass(properties.get(PLUGIN_MAINCLASS_PROPERTY));
        P plugin = (P) pluginClass.newInstance();
        plugin.putAllProperties(properties);

        return plugin;
    }
}