/*
 * Copyright (C) 2007 - 2010 Roland Krueger
 * Created on 05.10.2010
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
  private static final long        serialVersionUID = -8452255745085323681L;

  private List<String>             mParameterNames;
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
