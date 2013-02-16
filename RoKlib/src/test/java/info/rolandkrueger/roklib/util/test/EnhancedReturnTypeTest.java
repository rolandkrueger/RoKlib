/*
 * $Id: $
 * Copyright (C) 2007 Roland Krueger
 *
 * Author: Roland Krueger (www.rolandkrueger.info)
 *
 * This file is part of RoKlib.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307
 * USA
 */
package info.rolandkrueger.roklib.util.test;

import static junit.framework.Assert.*;

import org.junit.Test;

import info.rolandkrueger.roklib.util.EnhancedReturnType;

public class EnhancedReturnTypeTest
{
  private final static String MESSAGE = "message";
  private final static String FOOBAR = "foobar";
  private final static Exception EXCEPTION = new RuntimeException ();
  
  private EnhancedReturnType<String> mTestObject;
  
  @Test
  public void testVoidedEnhancedReturnType ()
  {
   mTestObject = EnhancedReturnType.Builder
     .createVoided (String.class)
     .withMessage ("foo")
     .appendToMessage ("bar")
     .build ();
   assertIsVoided (mTestObject);
   assertMessage (mTestObject, FOOBAR);
   assertHasNoException (mTestObject);
   
   mTestObject = EnhancedReturnType.Builder.createNew (String.class).voidResult ().build ();
   assertIsVoided (mTestObject);
   assertHasNoException (mTestObject);
  }
  
  @Test
  public void testSuccessfulEnhancedReturnType ()
  {
    mTestObject = EnhancedReturnType.Builder
      .createNew (String.class)
      .successful ()
      .withMessage (MESSAGE)
      .appendToMessage (MESSAGE)
      .withValue (FOOBAR)
      .build ();
    assertCorrect (mTestObject, true, FOOBAR);
    assertMessage (mTestObject, MESSAGE + MESSAGE);
    assertHasNoException (mTestObject);
    
    mTestObject = EnhancedReturnType.Builder
      .createSuccessful (String.class)
      .withMessage (MESSAGE)
      .appendToMessage (MESSAGE)
      .withValue (FOOBAR)
      .build ();
    assertCorrect (mTestObject, true, FOOBAR);
    assertMessage (mTestObject, MESSAGE + MESSAGE);
    assertHasNoException (mTestObject);
    
    mTestObject = EnhancedReturnType.Builder.createSuccessful (FOOBAR);
    assertCorrect (mTestObject, true, FOOBAR);
    assertEquals (mTestObject.getMessage (), "");
    assertHasNoException (mTestObject);
  }
  
  @Test
  public void testFailedEnhancedReturnType ()
  {
    mTestObject = EnhancedReturnType.Builder
      .createNew (String.class)
      .failed ()
      .withMessage (FOOBAR)
      .withException (EXCEPTION)
      .build ();
    assertHasCorrectException (mTestObject, EXCEPTION);
    assertCorrect (mTestObject, false, null);

    mTestObject = EnhancedReturnType.Builder
    .createFailed (String.class)
    .withMessage (FOOBAR)
    .withException (EXCEPTION)
    .build ();
    assertHasCorrectException (mTestObject, EXCEPTION);
    assertCorrect (mTestObject, false, null);
  }
  
  @Test
  public void testSetMessageOverwritesExistingMessage ()
  {
    mTestObject = EnhancedReturnType.Builder
      .createVoided (String.class)
      .appendToMessage ("xxx")
      .withMessage (MESSAGE)
      .build ();
    assertMessage (mTestObject, MESSAGE);

    mTestObject = EnhancedReturnType.Builder
      .createSuccessful (String.class)
      .appendToMessage ("xxx")
      .withMessage (MESSAGE)
      .build ();
    assertMessage (mTestObject, MESSAGE);
    
    mTestObject = EnhancedReturnType.Builder
      .createFailed (String.class)
      .appendToMessage ("xxx")
      .withMessage (MESSAGE)
      .build ();
    assertMessage (mTestObject, MESSAGE);
  }
  
  @Test
  public void testAppendToMessage ()
  {
    mTestObject = EnhancedReturnType.Builder
      .createVoided (String.class)
      .appendToMessage (MESSAGE)
      .appendToMessage (FOOBAR)
      .build ();
    assertMessage (mTestObject, MESSAGE + FOOBAR);
  }

  @Test (expected = IllegalStateException.class)
  public void testGetValueOfVoidedObjectFails ()
  {
    mTestObject = EnhancedReturnType.Builder.createVoided (String.class).build ();
    mTestObject.getValue ();
  }
  
  @Test
  public void testSetNoMessage ()
  {
    mTestObject = EnhancedReturnType.Builder.createVoided (String.class).build ();
    assertFalse (mTestObject.hasMessage ());
  }
  
  @Test
  public void testFinishedBuilderWithMessage ()
  {
    mTestObject = EnhancedReturnType.Builder
      .createSuccessful (String.class)
      .withValue (FOOBAR)
      .appendToMessage (MESSAGE).build ();
    assertMessage (mTestObject, MESSAGE);
  }
  
  @Test
  public void testBuilder ()
  {
    mTestObject = EnhancedReturnType.Builder
      .createNew (String.class)
      .withMessage (MESSAGE)
      .appendToMessage (MESSAGE)
      .withValue (FOOBAR)
      .build ();
    assertIsSuccessful (mTestObject);
    assertMessage (mTestObject, MESSAGE + MESSAGE);
  }
  
  @Test
  public void testBuilderWithException ()
  {
    mTestObject = EnhancedReturnType.Builder
      .createNew (String.class)
      .withException (EXCEPTION)
      .build ();
    
    assertCorrect (mTestObject, false, null);
    assertHasCorrectException (mTestObject, EXCEPTION);
  }
  
  private void assertIsSuccessful (EnhancedReturnType<String> object)
  {
    assertTrue (object.isSuccess ());
    assertNotNull (object.getValue ());
  }
  
  private void assertCorrect (EnhancedReturnType<String> object, boolean success, String value)
  {
    assertEquals ("Success status not met.", success, object.isSuccess ());
    assertEquals ("Unexpected return value", value, object.getValue ());
    if (success)
    {
      assertFalse ("Marked as failure.", object.isFailure ());
      assertTrue ("Not marked as success.", object.isSuccess ());      
    } else
    {
      assertTrue ("Not marked as failure.", object.isFailure ());
      assertFalse ("Marked as success.", object.isSuccess ());
    }
  }
  
  private void assertIsVoided (EnhancedReturnType<String> object)
  {
    assertTrue ("Object not voided.", object.isVoided ());
  }
  
  private void assertMessage (EnhancedReturnType<String> object, String expectedMessage)
  {
    assertTrue ("Object has no message.", object.hasMessage ());
    assertEquals ("Unexpected message", expectedMessage, object.getMessage ());
  }
  
  private void assertHasCorrectException (EnhancedReturnType<String> object, Throwable exception)
  {
    assertTrue ("Object does not contain an exception.", object.hasException ());
    assertEquals ("Unexpected exception.", exception, object.getException ());
  }
  
  private void assertHasNoException (EnhancedReturnType<String> object)
  {
    assertFalse (object.hasException ());
  }
}
