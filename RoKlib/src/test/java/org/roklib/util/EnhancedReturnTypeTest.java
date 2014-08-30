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
package org.roklib.util;

import org.junit.Test;
import org.roklib.data.EnhancedReturnType;

import static junit.framework.Assert.*;

public class EnhancedReturnTypeTest {
    private final static String MESSAGE = "message";
    private final static String FOOBAR = "foobar";
    private final static Exception EXCEPTION = new RuntimeException();

    private EnhancedReturnType<String> testObject;

    @Test
    public void testVoidedEnhancedReturnType() {
        testObject = EnhancedReturnType.Builder.createVoided(String.class).withMessage("foo").appendToMessage("bar")
                .build();
        assertIsVoided(testObject);
        assertMessage(testObject, FOOBAR);
        assertHasNoException(testObject);

        testObject = EnhancedReturnType.Builder.createNew(String.class).voidResult().build();
        assertIsVoided(testObject);
        assertHasNoException(testObject);
    }

    @Test
    public void testSuccessfulEnhancedReturnType() {
        testObject = EnhancedReturnType.Builder.createNew(String.class).successful().withMessage(MESSAGE)
                .appendToMessage(MESSAGE).withValue(FOOBAR).build();
        assertCorrect(testObject, true, FOOBAR);
        assertMessage(testObject, MESSAGE + MESSAGE);
        assertHasNoException(testObject);

        testObject = EnhancedReturnType.Builder.createSuccessful(String.class).withMessage(MESSAGE)
                .appendToMessage(MESSAGE).withValue(FOOBAR).build();
        assertCorrect(testObject, true, FOOBAR);
        assertMessage(testObject, MESSAGE + MESSAGE);
        assertHasNoException(testObject);

        testObject = EnhancedReturnType.Builder.createSuccessful(FOOBAR);
        assertCorrect(testObject, true, FOOBAR);
        assertEquals(testObject.getMessage(), "");
        assertHasNoException(testObject);
    }

    @Test
    public void testFailedEnhancedReturnType() {
        testObject = EnhancedReturnType.Builder.createNew(String.class).failed().withMessage(FOOBAR)
                .withException(EXCEPTION).build();
        assertHasCorrectException(testObject, EXCEPTION);
        assertCorrect(testObject, false, null);

        testObject = EnhancedReturnType.Builder.createFailed(String.class).withMessage(FOOBAR).withException(EXCEPTION)
                .build();
        assertHasCorrectException(testObject, EXCEPTION);
        assertCorrect(testObject, false, null);
    }

    @Test
    public void testSetMessageOverwritesExistingMessage() {
        testObject = EnhancedReturnType.Builder.createVoided(String.class).appendToMessage("xxx").withMessage(MESSAGE)
                .build();
        assertMessage(testObject, MESSAGE);

        testObject = EnhancedReturnType.Builder.createSuccessful(String.class).appendToMessage("xxx")
                .withMessage(MESSAGE).build();
        assertMessage(testObject, MESSAGE);

        testObject = EnhancedReturnType.Builder.createFailed(String.class).appendToMessage("xxx").withMessage(MESSAGE)
                .build();
        assertMessage(testObject, MESSAGE);
    }

    @Test
    public void testAppendToMessage() {
        testObject = EnhancedReturnType.Builder.createVoided(String.class).appendToMessage(MESSAGE)
                .appendToMessage(FOOBAR).build();
        assertMessage(testObject, MESSAGE + FOOBAR);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetValueOfVoidedObjectFails() {
        testObject = EnhancedReturnType.Builder.createVoided(String.class).build();
        testObject.getValue();
    }

    @Test
    public void testSetNoMessage() {
        testObject = EnhancedReturnType.Builder.createVoided(String.class).build();
        assertFalse(testObject.hasMessage());
    }

    @Test
    public void testFinishedBuilderWithMessage() {
        testObject = EnhancedReturnType.Builder.createSuccessful(String.class).withValue(FOOBAR)
                .appendToMessage(MESSAGE).build();
        assertMessage(testObject, MESSAGE);
    }

    @Test
    public void testBuilder() {
        testObject = EnhancedReturnType.Builder.createNew(String.class).withMessage(MESSAGE).appendToMessage(MESSAGE)
                .withValue(FOOBAR).build();
        assertIsSuccessful(testObject);
        assertMessage(testObject, MESSAGE + MESSAGE);
    }

    @Test
    public void testBuilderWithException() {
        testObject = EnhancedReturnType.Builder.createNew(String.class).withException(EXCEPTION).build();

        assertCorrect(testObject, false, null);
        assertHasCorrectException(testObject, EXCEPTION);
    }

    private void assertIsSuccessful(EnhancedReturnType<String> object) {
        assertTrue(object.isSuccess());
        assertNotNull(object.getValue());
    }

    private void assertCorrect(EnhancedReturnType<String> object, boolean success, String value) {
        assertEquals("Success status not met.", success, object.isSuccess());
        assertEquals("Unexpected return value", value, object.getValue());
        if (success) {
            assertFalse("Marked as failure.", object.isFailure());
            assertTrue("Not marked as success.", object.isSuccess());
        } else {
            assertTrue("Not marked as failure.", object.isFailure());
            assertFalse("Marked as success.", object.isSuccess());
        }
    }

    private void assertIsVoided(EnhancedReturnType<String> object) {
        assertTrue("Object not voided.", object.isVoided());
    }

    private void assertMessage(EnhancedReturnType<String> object, String expectedMessage) {
        assertTrue("Object has no message.", object.hasMessage());
        assertEquals("Unexpected message", expectedMessage, object.getMessage());
    }

    private void assertHasCorrectException(EnhancedReturnType<String> object, Throwable exception) {
        assertTrue("Object does not contain an exception.", object.hasException());
        assertEquals("Unexpected exception.", exception, object.getException());
    }

    private void assertHasNoException(EnhancedReturnType<String> object) {
        assertFalse(object.hasException());
    }
}
