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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;

/**
 * @author Roland Krueger
 */
public final class StreamUtilities
{
  private StreamUtilities ()
  {
  }

  /**
   * <p>
   * Copies the data from the given {@link Reader} to the given {@link Writer}. Note that the Reader
   * and Writer classes solely process character data and not byte data. Copying byte data must be
   * done with {@link StreamUtilities#copyStreams(InputStream, OutputStream)}.
   * <p>
   * Both source reader and destination writer will be closed after the copy process.
   * 
   * @param source
   *          a character data source
   * @param destination
   *          the target of the copy operation
   * @throws IOException
   *           if an error occurs while copying
   */
  public static void copyReaderToWriter (Reader source, Writer destination) throws IOException
  {
    copyReaderToWriter (source, destination, true, true);
  }

  /**
   * <p>
   * Copies the data from the given {@link Reader} to the given {@link Writer}. Note that the Reader
   * and Writer classes solely process character data and not byte data. Copying byte data must be
   * done with {@link StreamUtilities#copyStreams(InputStream, OutputStream)}. The two boolean
   * parameters let you define whether you want the source reader and destination writer to be
   * closed after the operation.
   * 
   * @param source
   *          a character data source
   * @param destination
   *          the target of the copy operation
   * @param closeSource
   *          source reader will be closed if <code>true</code>
   * @param closeDestination
   *          destination writer will be closed if <code>true</code>
   * @throws IOException
   *           if an error occurs while copying
   */
  public static void copyReaderToWriter (Reader source, Writer destination, boolean closeSource,
      boolean closeDestination) throws IOException
  {
    char[] buffer = new char[4096];
    int read;
    while ((read = source.read (buffer)) != -1)
    {
      destination.write (buffer, 0, read);
    }
    destination.flush ();
    if (closeDestination) destination.close ();
    if (closeSource) source.close ();
  }

  /**
   * <p>
   * Copies the data from the given {@link InputStream} to the given {@link OutputStream}.
   * <p>
   * Both source and destination stream will be closed after the copy process.
   * 
   * @param source
   *          a byte data source
   * @param destination
   *          the target of the copy operation
   * @throws IOException
   *           if an error occurs while copying
   */
  public static void copyStreams (InputStream source, OutputStream destination) throws IOException
  {
    copyStreams (source, destination, true, true);
  }

  /**
   * <p>
   * Copies the data from the given {@link InputStream} to the given {@link OutputStream}. The two
   * boolean parameters let you define whether you want the source and destination streams to be
   * closed after the operation.
   * 
   * @param source
   *          a byte data source
   * @param destination
   *          the target of the copy operation
   * @param closeSource
   *          source stream will be closed if <code>true</code>
   * @param closeDestination
   *          destination stream will be closed if <code>true</code>
   * @throws IOException
   *           if an error occurs while copying
   */
  public static void copyStreams (InputStream source, OutputStream destination, boolean closeSource,
      boolean closeDestination) throws IOException
  {
    byte[] buffer = new byte[4096];
    int read;
    while ((read = source.read (buffer)) != -1)
    {
      destination.write (buffer, 0, read);
    }
    destination.flush ();
    if (closeDestination) destination.close ();
    if (closeSource) source.close ();
  }

  /**
   * Copies the contents of one file to another.
   * 
   * @param source
   *          the source file
   * @param destination
   *          the destination file
   * @throws IOException
   *           if an error occurs while copying
   */
  public static void copyFiles (File source, File destination) throws IOException
  {
    copyStreams (new FileInputStream (source), new FileOutputStream (destination));
  }

  /**
   * Transforms the contents of the given {@link InputStream} into a String object.
   * 
   * @param source
   *          an {@link InputStream} the contents of which shall be transformed into a String
   * @return a String containing the contents of the given {@link InputStream}
   * @throws IOException
   *           if an error occurs during the transformation
   */
  public static String getStreamAsString (InputStream source) throws IOException
  {
    String result = getReaderAsString (new InputStreamReader (source));
    source.close ();
    return result;
  }

  public static String getStreamAsString (InputStream source, Charset charset) throws IOException
  {
    String result = getReaderAsString (new InputStreamReader (source, charset));
    source.close ();
    return result;
  }

  public static String getStreamAsString (InputStream source, String charsetName) throws IOException
  {
    String result = getReaderAsString (new InputStreamReader (source, charsetName));
    source.close ();
    return result;
  }

  /**
   * Transforms the contents of the given {@link Reader} into a String object.
   * 
   * @param source
   *          an {@link Reader} the contents of which shall be transformed into a String
   * @return a String containing the contents of the given {@link Reader}
   * @throws IOException
   *           if an error occurs during the transformation
   */
  public static String getReaderAsString (Reader source) throws IOException
  {
    StringBuilder buffer = new StringBuilder ();

    char[] charBuffer = new char[1024];
    int read;
    while ((read = source.read (charBuffer)) != -1)
    {
      buffer.append (charBuffer, 0, read);
    }

    source.close ();
    return buffer.toString ();
  }

  /**
   * Reads the contents of a file into a String.
   * 
   * @param file
   *          some file
   * @return the file's contents as a String
   * @throws IOException
   *           if an error occurs while reading the file
   */
  public static String getFileAsString (File file) throws IOException
  {
    return getStreamAsString (new FileInputStream (file));
  }

  /**
   * Appends the given String to the end of a file.
   * 
   * @param targetFile
   *          the file to append the data String to
   * @param text
   *          some String
   * @throws IOException
   *           if an error occurred while appending the data
   */
  public static void appendFile (File targetFile, String text) throws IOException
  {
    BufferedWriter writer = new BufferedWriter (new FileWriter (targetFile));
    writer.append (text);
    writer.flush ();
    writer.close ();
  }
}
