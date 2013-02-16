/*
 * $Id: $ Copyright (C) 2007 Roland Krueger
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
package info.rolandkrueger.roklib.io.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import info.rolandkrueger.roklib.io.StreamUtilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import org.junit.Test;

public class StreamUtilitiesTest
{
  private static String testData = "File needed for unit tests.\r\nDo not delete or change!\r\n"
                                     + "abcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghij\r\n"
                                     + "abcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghij\r\n"
                                     + "abcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghij\r\n"
                                     + "abcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghij\r\n"
                                     + "abcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghij\r\n"
                                     + "abcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghij\r\n"
                                     + "abcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghij\r\n"
                                     + "abcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghij\r\n"
                                     + "abcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghij\r\n"
                                     + "abcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghij";

  @Test
  public void testCopy () throws IOException
  {
    String str = "teststring";
    StringWriter writer = new StringWriter ();
    StreamUtilities.copyReaderToWriter (new StringReader (str), writer);
    assertEquals (str, writer.toString ());
  }

  @Test
  public void testGetStreamAsString () throws IOException
  {
    File file = new File ("target/test-classes/StreamUtilitiesTestFile.txt");
    assertTrue ("Can't read test file " + file.getAbsolutePath ()
        + ". Please make sure that it is stored in the root of the classpath.", file.canRead ());
    assertEquals (testData, StreamUtilities.getStreamAsString (new FileInputStream (file)));
  }

  @Test
  public void testGetFileAsString () throws IOException
  {
    File file = new File ("target/test-classes/StreamUtilitiesTestFile.txt");
    assertTrue (
        "Can't read test file UtilitiesTestFile.txt. Please make sure that it is stored in the root of the classpath.",
        file.canRead ());
    assertEquals (testData, StreamUtilities.getFileAsString (file));
  }
}
