/*
 * $Id: SingleBooleanURLParameterTest.java 186 2010-11-01 10:12:14Z roland $
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
import info.rolandkrueger.roklib.webapps.urldispatching.urlparameters.AbstractSingleURLParameter;
import info.rolandkrueger.roklib.webapps.urldispatching.urlparameters.EnumURLParameterErrors;
import info.rolandkrueger.roklib.webapps.urldispatching.urlparameters.SingleBooleanURLParameter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SingleBooleanURLParameterTest extends AbstractSingleURLParameterTest<Boolean>
{
  @Override
  public AbstractSingleURLParameter<Boolean> getTestSingleURLParameter (String parameterName)
  {
    return new SingleBooleanURLParameter ("test");
  }
  
  @Override
  public String getTestValueAsString ()
  {
    return "true";
  }
  
  @Override
  public Boolean getTestValue ()
  {
    return Boolean.TRUE;
  }
  
  @Override
  public void testConsume ()
  {
    super.testConsume ();
    AbstractSingleURLParameter<Boolean> testObj = getTestSingleURLParameter ("test");
    
    Map<String, List<String>> parameters = new HashMap<String, List<String>> ();
    
    parameters.put ("test", Arrays.asList ("false"));
    testObj.consume (parameters);
    assertEquals (EnumURLParameterErrors.NO_ERROR, testObj.getError ());
    assertEquals (Boolean.FALSE, testObj.getValue ());
    
    
    parameters.put ("test", Arrays.asList ("0"));
    testObj.consume (parameters);
    assertEquals (EnumURLParameterErrors.NO_ERROR, testObj.getError ());
    assertEquals (Boolean.FALSE, testObj.getValue ());
    
    
    parameters.put ("test", Arrays.asList ("1"));
    testObj.consume (parameters);
    assertEquals (EnumURLParameterErrors.NO_ERROR, testObj.getError ());
    assertEquals (Boolean.TRUE, testObj.getValue ());
  }
}
