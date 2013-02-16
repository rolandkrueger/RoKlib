/*
 * $Id: AbstractURLParameter.java 181 2010-11-01 09:39:13Z roland $
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
package info.rolandkrueger.roklib.webapps.urldispatching.urlparameters;

import info.rolandkrueger.roklib.webapps.urldispatching.AbstractURLActionHandler;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public abstract class AbstractURLParameter<V extends Serializable> implements IURLParameter<V>
{
  private static final long serialVersionUID = 2304452724109724238L;
  
  protected EnumURLParameterErrors mError;
  protected V mValue;
  private V mDefaultValue = null;
  private boolean mOptional = false;
  
  protected abstract boolean consumeImpl (Map<String, List<String>> parameters);
  protected abstract boolean consumeListImpl (String[] values);
  
  public AbstractURLParameter ()
  {    
    mError = EnumURLParameterErrors.NO_ERROR;
  }
  
  public final boolean consume (Map<String, List<String>> parameters)
  {
    mError = EnumURLParameterErrors.NO_ERROR;
    boolean result = consumeImpl (parameters);    
    postConsume ();
    return result;
  }
  
  public boolean consumeList (String[] values)
  {
    mError = EnumURLParameterErrors.NO_ERROR;
    boolean result = consumeListImpl (values);
    postConsume ();
    return result;
  }
  
  private void postConsume ()
  {
    if ( ! hasValue ())
      mValue = mDefaultValue;
    if ( ! hasValue () && ! mOptional && mError == EnumURLParameterErrors.NO_ERROR) 
      mError = EnumURLParameterErrors.PARAMETER_NOT_FOUND;
  }
  
  public void setDefaultValue (V defaultValue)
  {
    mDefaultValue = defaultValue;
  }
  
  protected void setError (EnumURLParameterErrors error)
  {
    mError = error;
  }

  public EnumURLParameterErrors getError ()
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
  
  public void setValueAndParameterizeURLHandler(V value, AbstractURLActionHandler handler) 
  {
    setValue (value);
    parameterizeURLHandler (handler);    
  }
  
  public void clearValue ()
  {
    mError = EnumURLParameterErrors.NO_ERROR;
    mValue = null;
  }
  
  public boolean hasValue ()
  {
    return mError == EnumURLParameterErrors.NO_ERROR && mValue != null;
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
