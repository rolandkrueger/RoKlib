/*
 * $Id: Point2DURLParameter.java 181 2010-11-01 09:39:13Z roland $
 * Copyright (C) 2007 - 2010 Roland Krueger
 * Created on 05.10.2010
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
package info.rolandkrueger.roklib.webapps.urldispatching.urlparameters;

import info.rolandkrueger.roklib.util.helper.CheckForNull;
import info.rolandkrueger.roklib.webapps.urldispatching.AbstractURLActionCommand;
import info.rolandkrueger.roklib.webapps.urldispatching.AbstractURLActionHandler;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Point2DURLParameter extends AbstractURLParameter<Point2D.Double>
{
  private static final long serialVersionUID = - 8452255745085323681L;

  private List<String> mParameterNames;
  private SingleDoubleURLParameter mXURLParameter;
  private SingleDoubleURLParameter mYURLParameter;
  
  public Point2DURLParameter (String xParamName, String yParamName)
  {
    CheckForNull.check (xParamName, yParamName);
    mParameterNames = new ArrayList<String> (2);
    mParameterNames.add (xParamName);
    mParameterNames.add (yParamName);
    mXURLParameter = new SingleDoubleURLParameter (xParamName);
    mYURLParameter = new SingleDoubleURLParameter (yParamName);
  }
  
  public AbstractURLActionCommand getErrorCommandIfInvalid ()
  {
    return null;
  }

  public void parameterizeURLHandler (AbstractURLActionHandler handler)
  {
    mXURLParameter.setValue (getValue ().getX ());
    mYURLParameter.setValue (getValue ().getY ());
    mXURLParameter.parameterizeURLHandler (handler);
    mYURLParameter.parameterizeURLHandler (handler);
  }

  public int getSingleValueCount ()
  {
    return 2;
  }

  public List<String> getParameterNames ()
  {
    return mParameterNames;
  }

  @Override
  protected boolean consumeImpl (Map<String, List<String>> parameters)
  {
    boolean result = mXURLParameter.consume (parameters);
    result &= mYURLParameter.consume (parameters);
    if (result)
    {
      setValue (new Point2D.Double (mXURLParameter.getValue (), mYURLParameter.getValue ()));
    }
    return result;
  }

  @Override
  protected boolean consumeListImpl (String[] values)
  {
    if (values == null || values.length != 2)
      return false;
    boolean result = mXURLParameter.consumeList (Arrays.copyOfRange (values, 0, 1));
    result &= mYURLParameter.consumeList (Arrays.copyOfRange (values, 1, 2));
    if (result)
    {
      setValue (new Point2D.Double (mXURLParameter.getValue (), mYURLParameter.getValue ()));
    }
    return result;
  }

}
