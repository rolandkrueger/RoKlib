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

import info.rolandkrueger.roklib.util.helper.CheckForNull;
import info.rolandkrueger.roklib.webapps.urldispatching.AbstractURLActionHandler;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public abstract class AbstractSingleURLParameter<V extends Serializable> extends AbstractURLParameter<V>
{
  private static final long serialVersionUID = -4048110873045678896L;

  private List<String> mParameterName;
  
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
