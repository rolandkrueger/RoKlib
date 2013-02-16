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

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import info.rolandkrueger.roklib.io.IStreamDataBuffer;
import info.rolandkrueger.roklib.io.StreamUtilities;
import info.rolandkrueger.roklib.util.EnhancedReturnType;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.junit.Before;
import org.junit.Test;

public abstract class IStreamDataBufferTest
{
  private IStreamDataBuffer mTestObj;

  public abstract IStreamDataBuffer createObjectUnderTest () throws IOException;

  @Before
  public void setUp () throws IOException
  {
    mTestObj = createObjectUnderTest ();
  }

  @Test
  public void testGetOutputStream () throws IOException
  {
    byte[] testData = new byte[] { -1, 2, -3, 4, -5 };

    EnhancedReturnType<OutputStream> outputStream = mTestObj.getOutputStream ();
    assertNotNull (outputStream.getValue ());

    StreamUtilities.copyStreams (new ByteArrayInputStream (testData), outputStream.getValue ());
    EnhancedReturnType<InputStream> inputStreamResultObj = mTestObj.getInputStream ();
    assertNotNull (inputStreamResultObj.getValue ());
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream ();
    StreamUtilities.copyStreams (inputStreamResultObj.getValue (), byteArrayOutputStream);
    byte[] arrayUnderTest = byteArrayOutputStream.toByteArray ();
    for (int i = 0; i < testData.length; ++i)
    {
      assertEquals (testData[i], arrayUnderTest[i]);
    }

    inputStreamResultObj = mTestObj.getInputStream ();
    assertNotNull (inputStreamResultObj.getValue ());
    byteArrayOutputStream = new ByteArrayOutputStream ();
    StreamUtilities.copyStreams (inputStreamResultObj.getValue (), byteArrayOutputStream);
    arrayUnderTest = byteArrayOutputStream.toByteArray ();
    for (int i = 0; i < testData.length; ++i)
    {
      assertEquals (testData[i], arrayUnderTest[i]);
    }
  }

  @Test
  public void testGetInputStream () throws IOException
  {
    EnhancedReturnType<InputStream> result = mTestObj.getInputStream ();
    assertNotNull (result.getValue ());
    assertEquals ("", StreamUtilities.getStreamAsString (result.getValue ()));

    result = mTestObj.getInputStream ();
    assertNotNull (result.getValue ());
    assertEquals ("", StreamUtilities.getStreamAsString (result.getValue ()));
  }

  @Test
  public void testReset () throws IOException
  {
    StreamUtilities.copyStreams (new ByteArrayInputStream ("testReset test data".getBytes ()), mTestObj
        .getOutputStream ().getValue ());
    assertTrue (mTestObj.reset ());
    assertEquals ("", StreamUtilities.getStreamAsString (mTestObj.getInputStream ().getValue ()));
  }
}
