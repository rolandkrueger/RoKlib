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
package org.roklib.webapps.urldispatching.urlparameters;


import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import org.roklib.util.helper.CheckForNull;
import org.roklib.webapps.urldispatching.AbstractURLActionHandler;

public abstract class AbstractSingleURLParameter<V extends Serializable> extends AbstractURLParameter<V>
{
  private static final long serialVersionUID = -4048110873045678896L;

  private List<String>      mParameterName;

  public AbstractSingleURLParameter (String parameterName, boolean optional)
  {
    this (parameterName);
    setOptional (optional);
  }

  public AbstractSingleURLParameter (String parameterName)
  {
    CheckForNull.check (parameterName);
    mParameterName = new LinkedList<String> ();
    mParameterName.add (parameterName);
  }

  protected String getParameterName ()
  {
    return mParameterName.get (0);
  }

  public int getSingleValueCount ()
  {
    return 1;
  }

  public List<String> getParameterNames ()
  {
    return mParameterName;
  }

  public void parameterizeURLHandler (AbstractURLActionHandler handler)
  {
    if (mValue != null)
    {
      handler.addActionArgument (mParameterName.get (0), mValue);
    }
  }

  @Override
  public String toString ()
  {
    StringBuilder buf = new StringBuilder ();
    buf.append ("{").append (getClass ().getSimpleName ()).append (": ");
    buf.append (mParameterName.get (0)).append ("}");
    return buf.toString ();
  }
}
