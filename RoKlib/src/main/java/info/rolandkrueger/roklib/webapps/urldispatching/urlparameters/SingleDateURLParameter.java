/*
 * $Id: SingleDateURLParameter.java 181 2010-11-01 09:39:13Z roland $
 * Copyright (C) 2007 - 2010 Roland Krueger
 * Created on 12.03.2010
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

import java.util.Date;
import java.util.List;
import java.util.Map;

public class SingleDateURLParameter extends AbstractSingleURLParameter<Date>
{
  private static final long serialVersionUID = 6617369364956822893L;

  public SingleDateURLParameter (String parameterName)
  {
    super (parameterName);
  }

  public SingleDateURLParameter (String parameterName, Date defaultDate)
  {
    super (parameterName);
    setDefaultValue (defaultDate);
  }

  @Override
  protected boolean consumeImpl (Map<String, List<String>> parameters)
  {
    List<String> valueList = parameters.remove (getParameterName ());
    if (valueList == null || valueList.isEmpty ())
    {
      return false;
    }
    return consumeValue (valueList.get (0));
  }

  @Override
  protected boolean consumeListImpl (String[] values)
  {
    if (values == null || values.length == 0) return false;
    
    return consumeValue (values[0]);
  }

  private boolean consumeValue (String valueString)
  {
    try
    {
      setValue (new Date (Long.valueOf (valueString)));
      return true;
    } catch (NumberFormatException nfExc)
    {
      mError = EnumURLParameterErrors.CONVERSION_ERROR;
      return false;
    }
  }

  public AbstractURLActionCommand getErrorCommandIfInvalid ()
  {
    return null;
  }
}
