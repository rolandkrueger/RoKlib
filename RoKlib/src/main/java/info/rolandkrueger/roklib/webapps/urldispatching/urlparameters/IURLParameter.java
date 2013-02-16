/*
 * $Id: $
 * Copyright (C) 2007 - 2010 Roland Krueger
 * Created on 02.03.2010
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
