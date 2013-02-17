/*
 * Copyright (C) 2007 - 2010 Roland Krueger
 * Created on 31.10.2010
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

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;

import org.roklib.webapps.urldispatching.urlparameters.AbstractURLParameter;
import org.roklib.webapps.urldispatching.urlparameters.Point2DURLParameter;

public class Point2DURLParameterTest extends AbstractURLParameterTest<Point2D.Double>
{
  @Override
  public AbstractURLParameter<Double> getTestURLParameter ()
  {
    return new Point2DURLParameter ("testX", "testY");
  }

  @Override
  public Double getTestValue ()
  {
    return new Double (1.0, 2.0);
  }

  @Override
  public void testGetSingleValueCount ()
  {
    assertEquals (2, getTestURLParameter ().getSingleValueCount ());
  }

  @Override
  public void testGetParameterNames ()
  {
    assertEquals (2, getTestURLParameter ().getParameterNames ().size ());
    assertEquals ("testX", getTestURLParameter ().getParameterNames ().get (0));
    assertEquals ("testY", getTestURLParameter ().getParameterNames ().get (1));
  }
}
