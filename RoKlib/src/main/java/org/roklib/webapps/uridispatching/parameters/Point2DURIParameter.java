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
package org.roklib.webapps.uridispatching.parameters;


import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.roklib.util.helper.CheckForNull;
import org.roklib.webapps.uridispatching.AbstractURIActionCommand;
import org.roklib.webapps.uridispatching.AbstractURIActionHandler;

public class Point2DURIParameter extends AbstractURIParameter<Point2D.Double>
{
  private static final long        serialVersionUID = -8452255745085323681L;

  private List<String>             mParameterNames;
  private SingleDoubleURIParameter mXURIParameter;
  private SingleDoubleURIParameter mYURIParameter;

  public Point2DURIParameter (String xParamName, String yParamName)
  {
    CheckForNull.check (xParamName, yParamName);
    mParameterNames = new ArrayList<String> (2);
    mParameterNames.add (xParamName);
    mParameterNames.add (yParamName);
    mXURIParameter = new SingleDoubleURIParameter (xParamName);
    mYURIParameter = new SingleDoubleURIParameter (yParamName);
  }

  public AbstractURIActionCommand getErrorCommandIfInvalid ()
  {
    return null;
  }

  public void parameterizeURIHandler (AbstractURIActionHandler handler)
  {
    mXURIParameter.setValue (getValue ().getX ());
    mYURIParameter.setValue (getValue ().getY ());
    mXURIParameter.parameterizeURIHandler (handler);
    mYURIParameter.parameterizeURIHandler (handler);
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
    boolean result = mXURIParameter.consume (parameters);
    result &= mYURIParameter.consume (parameters);
    if (result)
    {
      setValue (new Point2D.Double (mXURIParameter.getValue (), mYURIParameter.getValue ()));
    }
    return result;
  }

  @Override
  protected boolean consumeListImpl (String[] values)
  {
    if (values == null || values.length != 2)
      return false;
    boolean result = mXURIParameter.consumeList (Arrays.copyOfRange (values, 0, 1));
    result &= mYURIParameter.consumeList (Arrays.copyOfRange (values, 1, 2));
    if (result)
    {
      setValue (new Point2D.Double (mXURIParameter.getValue (), mYURIParameter.getValue ()));
    }
    return result;
  }

}
