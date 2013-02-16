/*
 * Copyright (C) 2007 Roland Krueger
 * 
 * Created on 26.03.2010
 * 
 * Author: Roland Krueger (www.rolandkrueger.info)
 * 
 * This file is part of RoKlib.
 * 
 * This library is free software; you can redistribute it and/or modify it under the terms of the
 * GNU Lesser General Public License as published by the Free Software Foundation; either version
 * 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with this library;
 * if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA
 * 02111-1307 USA
 */
package info.rolandkrueger.roklib.io;

import info.rolandkrueger.roklib.util.EnhancedReturnType;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * <p>
 * An {@link IStreamDataBuffer} implementation that keeps its buffered data in a byte array. By
 * that, this class implements an in-memory data buffer which can be accessed with
 * {@link InputStream}s and {@link OutputStream}s.
 * </p>
 * <p>
 * If the default constructor is used to create a new {@link ByteArrayStreamDataBuffer} the initial
 * capacity of the byte array is defined by the constant
 * {@link ByteArrayStreamDataBuffer#DEFAULT_CAPACITY_IN_BYTES}. The capacity of this buffer
 * automatically grows with the data that is written to it.
 * </p>
 * 
 * @see ByteArrayOutputStream
 * @author Roland Krueger
 */
public class ByteArrayStreamDataBuffer implements IStreamDataBuffer
{
  private static final int      DEFAULT_CAPACITY_IN_BYTES = 8192;

  private ByteArrayOutputStream mData;

  private int                   mCapacity;

  /**
   * Creates a new {@link ByteArrayStreamDataBuffer} with an initial capacity as defined by
   * {@link ByteArrayStreamDataBuffer#DEFAULT_CAPACITY_IN_BYTES}.
   */
  public ByteArrayStreamDataBuffer ()
  {
    this (DEFAULT_CAPACITY_IN_BYTES);
  }

  /**
   * Creates a new {@link ByteArrayStreamDataBuffer} with an initial capacity as defined by the
   * capacity parameter.
   */
  public ByteArrayStreamDataBuffer (int capacity)
  {
    mCapacity = capacity;
    reset ();
  }

  @Override
  public boolean reset ()
  {
    mData = new ByteArrayOutputStream (mCapacity);
    return true;
  }

  /**
   * {@inheritDoc} The contents of the returned {@link InputStream} represent the data which has
   * been written to the {@link ByteArrayStreamDataBuffer}'s {@link OutputStream} up until the point
   * the {@link InputStream} was requested. Any data that is written to the {@link OutputStream} at
   * a later time cannot be read with this {@link InputStream}.
   * <p>
   * Calling this method more than once will yield a new {@link InputStream} instance pointing to
   * the same data each time.
   * </p>
   */
  @Override
  public EnhancedReturnType<InputStream> getInputStream ()
  {
    return EnhancedReturnType.Builder.createSuccessful ((InputStream) new ByteArrayInputStream (mData.toByteArray ()));
  }

  /**
   * {@inheritDoc} Calling this method more than once will always return the same
   * {@link OutputStream} instance. Closing this stream will have no effect (see
   * {@link ByteArrayOutputStream#close()}. You will still be able to write data to this buffer
   * after calling the stream's close method.
   */
  @Override
  public EnhancedReturnType<OutputStream> getOutputStream ()
  {
    return EnhancedReturnType.Builder.createSuccessful ((OutputStream) mData);
  }

  /**
   * Returns the current size of this buffer.
   * 
   * @see ByteArrayOutputStream#size()
   */
  public int getSize ()
  {
    return mData.size ();
  }

  /**
   * Creates a new {@link ByteArrayStreamDataBuffer} object. The result object has always a
   * successful state.
   */
  @Override
  public EnhancedReturnType<IStreamDataBuffer> create ()
  {
    return EnhancedReturnType.Builder.createSuccessful ((IStreamDataBuffer) new ByteArrayStreamDataBuffer ());
  }
}
