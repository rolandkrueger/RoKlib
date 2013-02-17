/*
 * Copyright (C) 2007 Roland Krueger
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
package org.roklib.io.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import org.junit.Test;
import org.roklib.io.StreamUtilities;

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
