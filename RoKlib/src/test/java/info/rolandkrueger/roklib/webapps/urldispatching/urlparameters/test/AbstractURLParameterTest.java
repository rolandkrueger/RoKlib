/*
 * $Id: AbstractURLParameterTest.java 186 2010-11-01 10:12:14Z roland $
 * Copyright (C) 2007 - 2010 Roland Krueger
 * Created on 07.03.2010
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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307
 * USA
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
    mTestObj.setValue (getTestValue());
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