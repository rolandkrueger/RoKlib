/*
 * Copyright (C) 2007 - 2010 Roland Krueger
 * Created on 02.03.2010
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

import info.rolandkrueger.roklib.webapps.urldispatching.AbstractURLActionCommand;

import java.util.List;
import java.util.Map;

public class SingleBooleanURLParameter extends AbstractSingleURLParameter<Boolean>
{
  private static final long serialVersionUID = 1181515935142386380L;

  public SingleBooleanURLParameter (String parameterName)
  {
    super (parameterName);
  }

  public SingleBooleanURLParameter (String parameterName, Boolean defaultValue)
  {
    super (parameterName);
    setDefaultValue (defaultValue);
  }

  protected boolean consumeImpl (Map<String, List<String>> parameters)
  {
    List<String> valueList = parameters.remove (getParameterName ());
    if (valueList == null || valueList.isEmpty ())
    {
      return false;
    }
    String value = valueList.get (0).toLowerCase ();
    return consumeValue (value);
  }

  @Override
  protected boolean consumeListImpl (String[] values)
  {
    if (values.length == 0)
      return false;
    return consumeValue (values[0]);
  }

  private boolean consumeValue (String stringValue)
  {
    if (stringValue == null)
      return false;
    if (!(stringValue.equals ("1") || stringValue.equals ("0") || stringValue.equals ("false") || stringValue
        .equals ("true")))
    {
      mError = EnumURLParameterErrors.CONVERSION_ERROR;
      return false;
    }

    if (stringValue.equals ("1"))
    {
      setValue (true);
      return true;
    }

    if (stringValue.equals ("0"))
    {
      setValue (false);
      return true;
    }

    setValue (Boolean.valueOf (stringValue));
    return true;
  }

  public AbstractURLActionCommand getErrorCommandIfInvalid ()
  {
    return null;
  }
}