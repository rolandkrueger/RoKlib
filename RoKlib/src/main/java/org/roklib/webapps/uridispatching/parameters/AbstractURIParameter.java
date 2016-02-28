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
package org.roklib.webapps.uridispatching.parameters;


import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.roklib.webapps.uridispatching.AbstractURIActionHandler;

public abstract class AbstractURIParameter<V extends Serializable> implements IURIParameter<V>
{
  private static final long        serialVersionUID = 2304452724109724238L;

  protected EnumURIParameterErrors mError;
  protected V                      mValue;
  private V                        mDefaultValue    = null;
  private boolean                  mOptional        = false;

  protected abstract boolean consumeImpl (Map<String, List<String>> parameters);

  protected abstract boolean consumeListImpl (String[] values);

  public AbstractURIParameter ()
  {
    mError = EnumURIParameterErrors.NO_ERROR;
  }

  public final boolean consume (Map<String, List<String>> parameters)
  {
    mError = EnumURIParameterErrors.NO_ERROR;
    boolean result = consumeImpl (parameters);
    postConsume ();
    return result;
  }

  public boolean consumeList (String[] values)
  {
    mError = EnumURIParameterErrors.NO_ERROR;
    boolean result = consumeListImpl (values);
    postConsume ();
    return result;
  }

  private void postConsume ()
  {
    if (!hasValue ())
      mValue = mDefaultValue;
    if (!hasValue () && !mOptional && mError == EnumURIParameterErrors.NO_ERROR)
      mError = EnumURIParameterErrors.PARAMETER_NOT_FOUND;
  }

  public void setDefaultValue (V defaultValue)
  {
    mDefaultValue = defaultValue;
  }

  protected void setError (EnumURIParameterErrors error)
  {
    mError = error;
  }

  public EnumURIParameterErrors getError ()
  {
    return mError;
  }

  public V getValue ()
  {
    return mValue;
  }

  public void setValue (V value)
  {
    mValue = value;
  }

  public void setValueAndParameterizeURIHandler (V value, AbstractURIActionHandler handler)
  {
    setValue (value);
    parameterizeURIHandler (handler);
  }

  public void clearValue ()
  {
    mError = EnumURIParameterErrors.NO_ERROR;
    mValue = null;
  }

  public boolean hasValue ()
  {
    return mError == EnumURIParameterErrors.NO_ERROR && mValue != null;
  }

  public void setOptional (boolean optional)
  {
    mOptional = optional;
  }

  public boolean isOptional ()
  {
    return mOptional;
  }
}