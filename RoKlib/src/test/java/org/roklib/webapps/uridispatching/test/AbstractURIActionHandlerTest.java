/*
 * $Id: AbstractURIActionHandlerTest.java 186 2010-11-01 10:12:14Z roland $
 * Copyright (C) 2007 - 2010 Roland Krueger
 * Created on 17.02.2010 
 * 
 * Author: Roland Krueger (www.rolandkrueger.info) 
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
package org.roklib.webapps.uridispatching.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.roklib.webapps.uridispatching.DispatchingURIActionHandler;
import org.roklib.webapps.uridispatching.IURIActionHandler.ParameterMode;
import org.roklib.webapps.uridispatching.URIActionDispatcher;
import org.roklib.webapps.uridispatching.parameters.EnumURIParameterErrors;
import org.roklib.webapps.uridispatching.parameters.SingleBooleanURIParameter;
import org.roklib.webapps.uridispatching.parameters.SingleIntegerURIParameter;
import org.roklib.webapps.uridispatching.parameters.SingleStringURIParameter;

public class AbstractURIActionHandlerTest
{
  private URIActionDispatcher         mDispatcher;

  private TURIActionHandler           mTestHandler1;

  private TURIActionHandler           mTestHandler2;

  private TURIActionHandler           mTestHandler3;

  private DispatchingURIActionHandler mDispatchingHandler;

  private TURIActionCommand           mTestCommand1;

  private TURIActionCommand           mTestCommand2;

  private SingleStringURIParameter    mURLParameter;

  private SingleBooleanURIParameter   mURLParameter2;

  private DispatchingURIActionHandler mCaseSensitiveDispatchingHandler;

  private URIActionDispatcher         mCaseSensitiveDispatcher;

  private TURIActionHandler           mCaseSensitiveTestHandler1;

  @Before
  public void setUp ()
  {
    mDispatcher = new URIActionDispatcher (false);
    mCaseSensitiveDispatcher = new URIActionDispatcher (true);

    mURLParameter = new SingleStringURIParameter ("PARAmeter");
    mURLParameter2 = new SingleBooleanURIParameter ("bool");
    mTestCommand1 = new TURIActionCommand ();
    mTestCommand2 = new TURIActionCommand ();
    mTestHandler1 = new TURIActionHandler ("abc", mTestCommand1);
    mTestHandler1.registerURLParameterForTest (mURLParameter, true);
    mTestHandler1.registerURLParameterForTest (mURLParameter2, true);
    mTestHandler2 = new TURIActionHandler ("123", mTestCommand2);
    mTestHandler3 = new TURIActionHandler ("cmd", mTestCommand1);
    mDispatchingHandler = new DispatchingURIActionHandler ("test");
    mDispatcher.addHandler (mDispatchingHandler);
    mDispatchingHandler.addSubHandler (mTestHandler1);
    mDispatchingHandler.addSubHandler (mTestHandler2);
    mDispatchingHandler.addSubHandler (mTestHandler3);

    mCaseSensitiveTestHandler1 = new TURIActionHandler ("ABC", mTestCommand1);
    mCaseSensitiveTestHandler1.registerURLParameterForTest (mURLParameter, true);
    mCaseSensitiveDispatchingHandler = new DispatchingURIActionHandler ("TEST");
    mCaseSensitiveDispatcher.getRootActionHandler ().addSubHandler (mCaseSensitiveDispatchingHandler);
    mCaseSensitiveDispatchingHandler.addSubHandler (mCaseSensitiveTestHandler1);
  }

  @Test
  public void testAddDefaultCommandForCondition () throws MalformedURLException
  {
    TURIActionCommand defaultCommand = new TURIActionCommand ();
    TCondition condition = new TCondition (true);
    mTestHandler1.addDefaultCommandForCondition (defaultCommand, condition);
    // use different case to test case insensitivity
    mDispatcher.handleURIAction (new URL ("http://localhost"), "Test/ABC");
    assertTrue (defaultCommand.mExecuted);
    condition = new TCondition (false);
    defaultCommand = new TURIActionCommand ();
    mTestHandler2.addDefaultCommandForCondition (defaultCommand, condition);
    mDispatcher.handleURIAction (new URL ("http://localhost"), "test/123");
    assertFalse (defaultCommand.mExecuted);
  }

  @Test
  public void testClearDefaultCommands () throws MalformedURLException
  {
    TURIActionCommand defaultCommand = new TURIActionCommand ();
    TCondition condition = new TCondition (true);
    mTestHandler1.addDefaultCommandForCondition (defaultCommand, condition);
    mTestHandler1.clearDefaultCommands ();
    // use different case to test case insensitivity
    mDispatcher.handleURIAction (new URL ("http://localhost"), "TEST/Abc");
    assertFalse (defaultCommand.mExecuted);
  }

  @Test
  public void test404CommandExecution () throws MalformedURLException
  {
    TURIActionCommand test404ActionCommand = new TURIActionCommand ();
    mDispatcher.set404FileNotFoundCommand (test404ActionCommand);
    mDispatcher.handleURIAction (new URL ("http://localhost"), "test/123");
    assertFalse (test404ActionCommand.mExecuted);
    mDispatcher.handleURIAction (new URL ("http://localhost"), "no/actionhandler/registered");
    assertTrue (test404ActionCommand.mExecuted);
  }

  @Test
  public void testDirectoryModeParameterEvaluation () throws MalformedURLException
  {
    mDispatcher.handleURIAction (new URL ("http://localhost"), "TEST/ABC/parameterValue/true", ParameterMode.DIRECTORY);
    assertEquals ("parameterValue", mURLParameter.getValue ());
    assertEquals (true, mURLParameter2.getValue ());
  }

  @Test
  public void testDirectoryModeWithNamesParameterEvaluation () throws MalformedURLException
  {
    mDispatcher.handleURIAction (new URL ("http://localhost"), "TEST/ABC/bool/true/PARAmeter/parameterValue",
        ParameterMode.DIRECTORY_WITH_NAMES);
    assertEquals ("parameterValue", mURLParameter.getValue ());
    assertEquals (true, mURLParameter2.getValue ());
  }

  @Test
  public void testDirectoryModeWithMissingValuesParameterEvaluation () throws MalformedURLException
  {
    mDispatcher.handleURIAction (new URL ("http://localhost"), "TEST/ABC/bool/true/",
        ParameterMode.DIRECTORY_WITH_NAMES);
    assertFalse (mURLParameter.hasValue ());
    assertEquals (true, mURLParameter2.getValue ());
  }

  @Test
  public void testCaseInsensitiveParameterHandling () throws MalformedURLException
  {
    Map<String, String[]> parameters = new HashMap<String, String[]> ();
    parameters.put ("parameter", new String[] { "parameterValue" });
    mDispatcher.handleParameters (parameters);
    mDispatcher.handleURIAction (new URL ("http://localhost"), "TEST/ABC");
    assertEquals ("parameterValue", mURLParameter.getValue ());
  }

  @Test
  public void testCaseSensitiveParameterHandling () throws MalformedURLException
  {
    Map<String, String[]> parameters = new HashMap<String, String[]> ();
    parameters.put ("PARAmeter", new String[] { "parameterValue" });
    mCaseSensitiveDispatcher.handleParameters (parameters);
    mCaseSensitiveDispatcher.handleURIAction (new URL ("http://localhost"), "TEST/ABC");
    assertEquals ("parameterValue", mURLParameter.getValue ());
  }

  @Test
  public void testCaseSensitiveParameterHandlingFail () throws MalformedURLException
  {
    Map<String, String[]> parameters = new HashMap<String, String[]> ();
    parameters.put ("parameter", new String[] { "parameterValue" });
    mCaseSensitiveDispatcher.handleParameters (parameters);
    mCaseSensitiveDispatcher.handleURIAction (new URL ("http://localhost"), "TEST/ABC");
    assertNotSame ("parameterValue", mURLParameter.getValue ());
  }

  @Test
  public void testCaseSensitiveActionHandling () throws MalformedURLException
  {
    mCaseSensitiveDispatcher.handleURIAction (new URL ("http://localhost"), "TEST/ABC");
    assertTrue (mTestCommand1.mExecuted);
  }

  @Test
  public void testCaseSensitiveActionHandlingFails () throws MalformedURLException
  {
    mCaseSensitiveDispatcher.handleURIAction (new URL ("http://localhost"), "test/ABC");
    assertFalse (mTestCommand1.mExecuted);
  }

  @Test
  public void testAddActionArgument ()
  {
    assertEquals ("/test/abc", mTestHandler1.getParameterizedActionURI (true).toString ());
    mTestHandler1.addActionArgument ("id", 1234);
    assertEquals ("/test/abc?id=1234", mTestHandler1.getParameterizedActionURI (false).toString ());
    mTestHandler1.addActionArgument ("id", 9999);
    assertEquals ("/test/abc?id=1234&id=9999", mTestHandler1.getParameterizedActionURI (true).toString ());

    mTestHandler2.addActionArgument ("v", 1, 2, 3);
    assertEquals ("/test/123?v=1&v=2&v=3", mTestHandler2.getParameterizedActionURI (false).toString ());
    mTestHandler2.addActionArgument ("test", true);
    assertEquals ("/test/123?v=1&v=2&v=3&test=true", mTestHandler2.getParameterizedActionURI (true).toString ());

    // test DIRECTORY_WITH_NAMES parameter mode
    mTestHandler1.addActionArgument ("id", 1234);
    mTestHandler1.addActionArgument ("param", "value_a", "value_b");
    assertEquals ("/test/abc/id/1234/param/value_a/param/value_b",
        mTestHandler1.getParameterizedActionURI (true, ParameterMode.DIRECTORY_WITH_NAMES).toString ());

    // test DIRECTORY parameter mode (parameter names are not contained in URL)
    mTestHandler2.addActionArgument ("id", 1234);
    mTestHandler2.addActionArgument ("param", "value");
    assertEquals ("/test/123/1234/value", mTestHandler2.getParameterizedActionURI (true, ParameterMode.DIRECTORY)
        .toString ());

    // test that parameters appear in the order they were added in the URL
    mTestHandler1.clearActionArguments ();
    mTestHandler1.addActionArgument ("first", "1");
    mTestHandler1.addActionArgument ("second", "2");
    mTestHandler1.addActionArgument ("third", "3");
    assertEquals ("/test/abc?first=1&second=2&third=3",
        mTestHandler1.getParameterizedActionURI (true, ParameterMode.QUERY).toString ());
  }

  @Test
  public void testClearActionArguments ()
  {
    mTestHandler1.addActionArgument ("v", 1, 2, 3);
    mTestHandler1.addActionArgument ("test", true);
    assertEquals ("/test/abc?v=1&v=2&v=3&test=true", mTestHandler1.getParameterizedActionURI (true).toString ());
    mTestHandler1.clearActionArguments ();
    assertEquals ("/test/abc", mTestHandler1.getParameterizedActionURI (true).toString ());
  }

  @Test
  public void testMandatoryParameters () throws MalformedURLException
  {
    SingleStringURIParameter parameter1 = new SingleStringURIParameter ("param");
    parameter1.setOptional (false);
    SingleStringURIParameter parameter2 = new SingleStringURIParameter ("arg");
    parameter2.setOptional (false);
    mTestHandler3.registerURLParameterForTest (parameter1, false);
    mTestHandler3.registerURLParameterForTest (parameter2, false);
    Map<String, String[]> parameters = new HashMap<String, String[]> ();
    parameters.put ("param", new String[] { "parameterValue" });

    // set only one parameter, but do not set the second, mandatory parameter
    mDispatcher.handleParameters (parameters);
    mDispatcher.handleURIAction (new URL ("http://localhost"), "test/cmd");
    assertTrue (mTestHandler3.haveRegisteredURIParametersErrors ());
    assertEquals (parameter1.getError (), EnumURIParameterErrors.NO_ERROR);
    assertEquals (parameter2.getError (), EnumURIParameterErrors.PARAMETER_NOT_FOUND);

    // now also set the second parameter
    parameters.put ("arg", new String[] { "argumentValue" });
    mDispatcher.handleParameters (parameters);
    mDispatcher.handleURIAction (new URL ("http://localhost"), "test/cmd");
    assertFalse (mTestHandler3.haveRegisteredURIParametersErrors ());
    assertEquals (parameter1.getError (), EnumURIParameterErrors.NO_ERROR);
    assertEquals (parameter2.getError (), EnumURIParameterErrors.NO_ERROR);
  }

  @Test
  public void testOptionalParameters () throws MalformedURLException
  {
    SingleStringURIParameter parameter1 = new SingleStringURIParameter ("param");
    parameter1.setOptional (false);
    SingleStringURIParameter parameter2 = new SingleStringURIParameter ("arg");
    parameter2.setOptional (true);
    mTestHandler3.registerURLParameterForTest (parameter1, false);
    mTestHandler3.registerURLParameterForTest (parameter2, true);
    Map<String, String[]> parameters = new HashMap<String, String[]> ();
    parameters.put ("param", new String[] { "parameterValue" });

    // set only one parameter, but do not set the second, optional parameter
    mDispatcher.handleParameters (parameters);
    mDispatcher.handleURIAction (new URL ("http://localhost"), "test/cmd");
    assertFalse (mTestHandler3.haveRegisteredURIParametersErrors ());
    assertEquals (parameter1.getError (), EnumURIParameterErrors.NO_ERROR);
    assertEquals (parameter2.getError (), EnumURIParameterErrors.NO_ERROR);
    assertTrue (parameter1.hasValue ());
    assertFalse (parameter2.hasValue ());
  }

  @Test
  public void testMandatoryParametersWithDefaultValue () throws MalformedURLException
  {
    SingleStringURIParameter parameter1 = new SingleStringURIParameter ("param");
    parameter1.setOptional (false);
    SingleStringURIParameter parameter2 = new SingleStringURIParameter ("arg");
    parameter2.setOptional (false);
    parameter2.setDefaultValue ("DEFAULT");
    mTestHandler3.registerURLParameterForTest (parameter1, false);
    mTestHandler3.registerURLParameterForTest (parameter2, false);
    Map<String, String[]> parameters = new HashMap<String, String[]> ();
    parameters.put ("param", new String[] { "parameterValue" });

    // set only one parameter, but do not set the second, mandatory parameter
    mDispatcher.handleParameters (parameters);
    mDispatcher.handleURIAction (new URL ("http://localhost"), "test/cmd");
    assertFalse (mTestHandler3.haveRegisteredURIParametersErrors ());
    assertEquals (parameter1.getError (), EnumURIParameterErrors.NO_ERROR);
    assertEquals (parameter2.getError (), EnumURIParameterErrors.NO_ERROR);
    assertTrue (parameter1.hasValue ());
    assertTrue (parameter2.hasValue ());
    assertEquals (parameter1.getValue (), "parameterValue");
    // the second parameter contains the default value
    assertEquals (parameter2.getValue (), "DEFAULT");

    // now also set the second parameter
    parameters.put ("arg", new String[] { "argumentValue" });
    mDispatcher.handleParameters (parameters);
    mDispatcher.handleURIAction (new URL ("http://localhost"), "test/cmd");
    assertFalse (mTestHandler3.haveRegisteredURIParametersErrors ());
    assertEquals (parameter1.getValue (), "parameterValue");
    assertEquals (parameter2.getValue (), "argumentValue");
  }

  @Test
  public void testOptionalParameterWithConversionError () throws MalformedURLException
  {
    SingleIntegerURIParameter parameter = new SingleIntegerURIParameter ("int");
    parameter.setOptional (true);
    mTestHandler3.registerURLParameterForTest (parameter, true);

    Map<String, String[]> parameters = new HashMap<String, String[]> ();
    parameters.put ("int", new String[] { "one" });
    mDispatcher.handleParameters (parameters);
    mDispatcher.handleURIAction (new URL ("http://localhost"), "test/cmd");
    assertTrue (mTestHandler3.haveRegisteredURIParametersErrors ());
    assertEquals (parameter.getError (), EnumURIParameterErrors.CONVERSION_ERROR);
  }

  @Test
  public void testGetActionName ()
  {
    assertEquals ("abc", mTestHandler1.getActionName ());
  }

  @Test
  public void testGetCaseInsensitiveActionName ()
  {
    assertEquals ("test", mCaseSensitiveDispatchingHandler.getCaseInsensitiveActionName ());
  }

  @Test (expected = IllegalArgumentException.class)
  public void testAddSubHandlerTwice ()
  {
    mTestHandler1.addSubHandler (mTestHandler2);
  }

  @Test
  public void testGetActionURI ()
  {
    assertEquals ("/test/abc", mTestHandler1.getActionURI ());
    assertEquals ("/test/123", mTestHandler2.getActionURI ());
    assertEquals ("/test", mDispatchingHandler.getActionURI ());
  }

  @Test
  public void testIgnoreExclamationMark () throws MalformedURLException
  {
    mCaseSensitiveDispatcher.handleURIAction (new URL ("http://localhost"), "!TEST/ABC");
    assertFalse (mTestCommand1.mExecuted);
    mCaseSensitiveDispatcher.setIgnoreExclamationMark (true);
    mCaseSensitiveDispatcher.handleURIAction (new URL ("http://localhost"), "!TEST/ABC");
    assertTrue (mTestCommand1.mExecuted);
  }
}