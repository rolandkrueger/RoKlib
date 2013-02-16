/*
 * $Id: Point2DURLParameterTest.java 186 2010-11-01 10:12:14Z roland $
 * Copyright (C) 2007 - 2010 Roland Krueger
 * Created on 31.10.2010
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
import info.rolandkrueger.roklib.webapps.urldispatching.urlparameters.AbstractURLParameter;
import info.rolandkrueger.roklib.webapps.urldispatching.urlparameters.Point2DURLParameter;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;

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
