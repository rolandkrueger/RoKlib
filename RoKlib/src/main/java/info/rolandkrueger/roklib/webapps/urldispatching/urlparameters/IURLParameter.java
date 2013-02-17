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
import info.rolandkrueger.roklib.webapps.urldispatching.AbstractURLActionHandler;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface IURLParameter<V extends Serializable> extends Serializable
{
  public abstract boolean consume (Map<String, List<String>> parameters);

  public abstract boolean consumeList (String[] values);

  public abstract V getValue ();

  public abstract void setValue (V value);

  public abstract void clearValue ();

  public abstract AbstractURLActionCommand getErrorCommandIfInvalid ();

  public abstract EnumURLParameterErrors getError ();

  public abstract void parameterizeURLHandler (AbstractURLActionHandler handler);

  public abstract void setValueAndParameterizeURLHandler (V value, AbstractURLActionHandler handler);

  public abstract boolean hasValue ();

  public abstract void setOptional (boolean optional);

  public abstract boolean isOptional ();

  public abstract int getSingleValueCount ();

  public abstract List<String> getParameterNames ();
}
