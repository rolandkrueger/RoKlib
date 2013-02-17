/*
 * Copyright (C) 2007 - 2010 Roland Krueger
 * Created on 07.03.2010
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
package info.rolandkrueger.roklib.webapps.urldispatching.urlparameters.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import info.rolandkrueger.roklib.webapps.urldispatching.test.TURLActionCommand;
import info.rolandkrueger.roklib.webapps.urldispatching.test.TURLActionHandler;
import info.rolandkrueger.roklib.webapps.urldispatching.urlparameters.AbstractSingleURLParameter;
import info.rolandkrueger.roklib.webapps.urldispatching.urlparameters.AbstractURLParameter;
import info.rolandkrueger.roklib.webapps.urldispatching.urlparameters.EnumURLParameterErrors;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public abstract class AbstractSingleURLParameterTest<V extends Serializable> extends AbstractURLParameterTest<V>
{
  private AbstractSingleURLParameter<V> mTestSingleURLParameter;

  public abstract AbstractSingleURLParameter<V> getTestSingleURLParameter (String parameterName);

  public abstract String getTestValueAsString ();

  @Before
  public void setUp ()
  {
    super.setUp ();
    mTestSingleURLParameter = getTestSingleURLParameter ("test");
  }

  public AbstractURLParameter<V> getTestURLParameter ()
  {
    return getTestSingleURLParameter ("test");
  }

  public void testConsume (String testValue)
  {
    Map<String, List<String>> parameters = new HashMap<String, List<String>> ();
    parameters.put ("test", Arrays.asList (testValue));
    mTestSingleURLParameter.consume (parameters);
    assertEquals (EnumURLParameterErrors.NO_ERROR, mTestSingleURLParameter.getError ());
    assertEquals (getTestValue (), mTestSingleURLParameter.getValue ());
  }

  @Test
  public void testConsume ()
  {
    testConsume (getTestValueAsString ());
  }

  public void testConsumeList (String value)
  {
    mTestSingleURLParameter.consumeList (new String[] { value });
    assertEquals (EnumURLParameterErrors.NO_ERROR, mTestSingleURLParameter.getError ());
    assertEquals (getTestValue (), mTestSingleURLParameter.getValue ());
  }

  @Test
  public void testConsumeList ()
  {
    testConsumeList (getTestValueAsString ());
  }

  @Test
  public void testConsumeFail ()
  {
    Map<String, List<String>> parameters = new HashMap<String, List<String>> ();
    parameters.put ("test", Arrays.asList ("xxx"));
    mTestSingleURLParameter.consume (parameters);
    assertEquals (EnumURLParameterErrors.CONVERSION_ERROR, mTestSingleURLParameter.getError ());

    parameters.clear ();
    mTestSingleURLParameter.consume (parameters);
    assertEquals (EnumURLParameterErrors.PARAMETER_NOT_FOUND, mTestSingleURLParameter.getError ());
  }

  @Test
  public void testParameterizeURLHandler ()
  {
    T2URLActionHandler<V> handler = new T2URLActionHandler<V> ("test", getTestValue ());
    mTestSingleURLParameter.parameterizeURLHandler (handler);
    mTestSingleURLParameter.setValue (getTestValue ());
    handler = new T2URLActionHandler<V> ("test", null);
    mTestSingleURLParameter.clearValue ();
    mTestSingleURLParameter.parameterizeURLHandler (handler);
  }

  @Override
  public void testGetSingleValueCount ()
  {
    assertEquals (1, mTestSingleURLParameter.getSingleValueCount ());
  }

  @Override
  public void testGetParameterNames ()
  {
    assertEquals (1, mTestSingleURLParameter.getParameterNames ().size ());
  }

  @SuppressWarnings ("serial")
  private static class T2URLActionHandler<V extends Serializable> extends TURLActionHandler
  {
    String mExpectedParameterName;
    V      mExpectedValue;

    public T2URLActionHandler (String expectedParameterName, V expectedValue)
    {
      super ("", new TURLActionCommand ());
      mExpectedParameterName = expectedParameterName;
      mExpectedValue = expectedValue;
    }

    @Override
    public void addActionArgument (String argumentName, Serializable... argumentValues)
    {
      super.addActionArgument (argumentName, argumentValues);
      assertEquals (1, argumentValues);
      assertEquals (mExpectedParameterName, argumentName);
      if (mExpectedValue == null)
        assertNull (argumentValues[0]);
      else
        assertEquals (mExpectedValue, argumentValues[0]);
    }
  }
}
