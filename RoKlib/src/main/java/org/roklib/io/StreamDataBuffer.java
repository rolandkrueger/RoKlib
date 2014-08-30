/*
 * Copyright (C) 2007 Roland Krueger
 * 
 * Created on 26.03.2010
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
package org.roklib.io;


import org.roklib.data.EnhancedReturnType;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * <p>
 * Interface for some kind of data buffer which can be accessed via {@link InputStream}s and {@link OutputStream}s.
 * Implementing classes must provide a storage for the targeted data which has to be accessible with {@link InputStream}
 * s and {@link OutputStream}s.
 * </p>
 * <p>
 * Most of this interface's methods return an {@link EnhancedReturnType} carrying the requested object as a result. This
 * is to give implementation classes a chance to report errors without the need to pollute the method signatures with
 * implementation-dependent throws clauses.
 * </p>
 * <p>
 * Classes implementing this interface can be used, for example, to provide a data buffer which stores its contents
 * either in a temporary file or in memory.
 * </p>
 *
 * @author Roland Krueger
 */
public interface StreamDataBuffer {
    /**
     * Provides an {@link OutputStream} pointing to the buffered data. Writing to this {@link OutputStream} will write to
     * the buffer.
     *
     * @return a return object containing the requested {@link OutputStream} if this operation was successful. Otherwise,
     * this return object contains an error description and/or an exception object.
     */
    EnhancedReturnType<OutputStream> getOutputStream();

    /**
     * Provides an {@link InputStream} pointing to the buffered data. Reading from this {@link InputStream} will read the
     * buffered data.
     *
     * @return a return object containing the requested {@link InputStream} if this operation was successful. Otherwise,
     * this return object contains an error description and/or an exception object.
     */
    EnhancedReturnType<InputStream> getInputStream();

    /**
     * Resets the data buffer. Resetting the buffer will clear all data from it.
     *
     * @return <code>true</code> if the operation succeeded
     * @throws IOException if an error occurred while clearing the data
     */
    boolean reset() throws IOException;

    /**
     * Factory method that creates a new {@link StreamDataBuffer} object. Implementing classes should create a new
     * instance of themself and return this instance.
     *
     * @return a return object containing a new instance of a {@link StreamDataBuffer} if this operation was successful.
     * Otherwise, this return object contains an error description and/or an exception object.
     */
    EnhancedReturnType<StreamDataBuffer> create();
}
