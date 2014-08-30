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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * <p>
 * An {@link StreamDataBuffer} implementation that keeps its buffered data in a byte array. By that, this class
 * implements an in-memory data buffer which can be accessed with {@link InputStream}s and {@link OutputStream}s.
 * </p>
 * <p>
 * If the default constructor is used to create a new {@link ByteArrayStreamDataBuffer} the initial capacity of the byte
 * array is defined by the constant {@link ByteArrayStreamDataBuffer#DEFAULT_CAPACITY_IN_BYTES}. The capacity of this
 * buffer automatically grows with the data that is written to it.
 * </p>
 *
 * @author Roland Krueger
 * @see ByteArrayOutputStream
 */
public class ByteArrayStreamDataBuffer implements StreamDataBuffer {
    private static final int DEFAULT_CAPACITY_IN_BYTES = 8192;
    private ByteArrayOutputStream data;
    private final int capacity;

    /**
     * Creates a new {@link ByteArrayStreamDataBuffer} with an initial capacity as defined by
     * {@link ByteArrayStreamDataBuffer#DEFAULT_CAPACITY_IN_BYTES}.
     */
    public ByteArrayStreamDataBuffer() {
        this(DEFAULT_CAPACITY_IN_BYTES);
    }

    /**
     * Creates a new {@link ByteArrayStreamDataBuffer} with an initial capacity as defined by the capacity parameter.
     */
    public ByteArrayStreamDataBuffer(int capacity) {
        this.capacity = capacity;
        reset();
    }

    @Override
    public boolean reset() {
        data = new ByteArrayOutputStream(capacity);
        return true;
    }

    /**
     * {@inheritDoc} The contents of the returned {@link InputStream} represent the data which has been written to the
     * {@link ByteArrayStreamDataBuffer}'s {@link OutputStream} up until the point the {@link InputStream} was requested.
     * Any data that is written to the {@link OutputStream} at a later time cannot be read with this {@link InputStream}.
     * <p>
     * Calling this method more than once will yield a new {@link InputStream} instance pointing to the same data each
     * time.
     * </p>
     */
    @Override
    public EnhancedReturnType<InputStream> getInputStream() {
        return EnhancedReturnType.Builder.createSuccessful((InputStream) new ByteArrayInputStream(data.toByteArray()));
    }

    /**
     * {@inheritDoc} Calling this method more than once will always return the same {@link OutputStream} instance. Closing
     * this stream will have no effect (see {@link ByteArrayOutputStream#close()}. You will still be able to write data to
     * this buffer after calling the stream's close method.
     */
    @Override
    public EnhancedReturnType<OutputStream> getOutputStream() {
        return EnhancedReturnType.Builder.createSuccessful((OutputStream) data);
    }

    /**
     * Returns the current size of this buffer.
     *
     * @see ByteArrayOutputStream#size()
     */
    public int getSize() {
        return data.size();
    }

    /**
     * Creates a new {@link ByteArrayStreamDataBuffer} object. The result object has always a successful state.
     */
    @Override
    public EnhancedReturnType<StreamDataBuffer> create() {
        return EnhancedReturnType.Builder.createSuccessful((StreamDataBuffer) new ByteArrayStreamDataBuffer());
    }
}
