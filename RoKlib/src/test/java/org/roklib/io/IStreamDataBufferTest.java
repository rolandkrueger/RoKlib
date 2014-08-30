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
package org.roklib.io;

import org.junit.Before;
import org.junit.Test;
import org.roklib.data.EnhancedReturnType;

import java.io.*;

import static junit.framework.Assert.*;

public abstract class IStreamDataBufferTest {
    private StreamDataBuffer mTestObj;

    public abstract StreamDataBuffer createObjectUnderTest() throws IOException;

    @Before
    public void setUp() throws IOException {
        mTestObj = createObjectUnderTest();
    }

    @Test
    public void testGetOutputStream() throws IOException {
        byte[] testData = new byte[]{-1, 2, -3, 4, -5};

        EnhancedReturnType<OutputStream> outputStream = mTestObj.getOutputStream();
        assertNotNull(outputStream.getValue());

        StreamUtilities.copyStreams(new ByteArrayInputStream(testData), outputStream.getValue());
        EnhancedReturnType<InputStream> inputStreamResultObj = mTestObj.getInputStream();
        assertNotNull(inputStreamResultObj.getValue());
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        StreamUtilities.copyStreams(inputStreamResultObj.getValue(), byteArrayOutputStream);
        byte[] arrayUnderTest = byteArrayOutputStream.toByteArray();
        for (int i = 0; i < testData.length; ++i) {
            assertEquals(testData[i], arrayUnderTest[i]);
        }

        inputStreamResultObj = mTestObj.getInputStream();
        assertNotNull(inputStreamResultObj.getValue());
        byteArrayOutputStream = new ByteArrayOutputStream();
        StreamUtilities.copyStreams(inputStreamResultObj.getValue(), byteArrayOutputStream);
        arrayUnderTest = byteArrayOutputStream.toByteArray();
        for (int i = 0; i < testData.length; ++i) {
            assertEquals(testData[i], arrayUnderTest[i]);
        }
    }

    @Test
    public void testGetInputStream() throws IOException {
        EnhancedReturnType<InputStream> result = mTestObj.getInputStream();
        assertNotNull(result.getValue());
        assertEquals("", StreamUtilities.getStreamAsString(result.getValue()));

        result = mTestObj.getInputStream();
        assertNotNull(result.getValue());
        assertEquals("", StreamUtilities.getStreamAsString(result.getValue()));
    }

    @Test
    public void testReset() throws IOException {
        StreamUtilities.copyStreams(new ByteArrayInputStream("testReset test data".getBytes()), mTestObj
                .getOutputStream().getValue());
        assertTrue(mTestObj.reset());
        assertEquals("", StreamUtilities.getStreamAsString(mTestObj.getInputStream().getValue()));
    }
}
