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
/**
 *
 */
package org.roklib.plugins;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Map;
import java.util.Properties;

/**
 * @author Roland Krueger
 */
public abstract class AbstractPlugin {
    private final Properties properties;

    public AbstractPlugin() {
        properties = new Properties();
    }

    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    public Properties getProperties() {
        return properties;
    }

    public void loadProperties(InputStream inStream) throws IOException {
        properties.load(inStream);
    }

    public void loadProperties(Reader reader) throws IOException {
        properties.load(reader);
    }

    public void loadPropertiesFromXML(InputStream in) throws IOException {
        properties.loadFromXML(in);
    }

    public Object putProperty(Object key, Object value) {
        return properties.put(key, value);
    }

    public void putAllProperties(Map<?, ?> t) {
        properties.putAll(t);
    }
}
