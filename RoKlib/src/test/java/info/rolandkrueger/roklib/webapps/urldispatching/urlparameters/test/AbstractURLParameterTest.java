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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import info.rolandkrueger.roklib.webapps.urldispatching.AbstractURLActionCommand;
import info.rolandkrueger.roklib.webapps.urldispatching.AbstractURLActionHandler;
import info.rolandkrueger.roklib.webapps.urldispatching.test.TURLActionCommand;
import info.rolandkrueger.roklib.webapps.urldispatching.test.TURLActionHandler;
import info.rolandkrueger.roklib.webapps.urldispatching.urlparameters.AbstractURLParameter;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public abstract class AbstractURLParameterTest<V extends Serializable>
{
  private AbstractURLParameter<V> mTestObj;

  public abstract AbstractURLParameter<V> getTestURLParameter ();

  public abstract V getTestValue ();

  @Test
  public abstract void testGetSingleValueCount ();

  @Test
  public abstract void testGetParameterNames ();

  @Before
  public void setUp ()
  {
    mTestObj = getTestURLParameter ();
  }

  @Test
  public void testSetGetValue ()
  {
    V value = getTestValue ();
    mTestObj.setValue (value);
    assertEquals (value, mTestObj.getValue ());
  }

  @Test
  public void testHasValue ()
  {
    V value = getTestValue ();
    assertFalse (mTestObj.hasValue ());
    mTestObj.setValue (value);
    assertTrue (mTestObj.hasValue ());
  }

  @Test
  public void testClearValue ()
  {
    mTestObj.setValue (getTestValue ());
    mTestObj.clearValue ();
    assertNull (mTestObj.getValue ());
  }

  @Test
  public void testSetValueAndParameterizeURLHandler ()
  {
    TURLParameter testObj = new TURLParameter ();

    testObj.setValueAndParameterizeURLHandler ("value", new TURLActionHandler ("action", new TURLActionCommand ()));
    assertEquals ("value", testObj.getValue ());
    assertTrue (testObj.parameterized ());
  }

  @Test
  public void testSetOptional ()
  {
    mTestObj.setOptional (true);
    assertTrue (mTestObj.isOptional ());
    mTestObj.setOptional (false);
    assertFalse (mTestObj.isOptional ());
  }

  @SuppressWarnings ("serial")
  private static class TURLParameter extends AbstractURLParameter<String>
  {
    boolean parameterized = false;

    public boolean parameterized ()
    {
      return parameterized;
    }

    @Override
    protected boolean consumeImpl (Map<String, List<String>> parameters)
    {
      return true;
    }

    public AbstractURLActionCommand getErrorCommandIfInvalid ()
    {
      return null;
    }

    public void parameterizeURLHandler (AbstractURLActionHandler handler)
    {
      parameterized = true;
    }

    @Override
    protected boolean consumeListImpl (String[] values)
    {
      return true;
    }

    public int getSingleValueCount ()
    {
      return 1;
    }

    public List<String> getParameterNames ()
    {
      return null;
    }
  }
}