/*
 * $Id: SingleLongWithIgnoredTextURLParameter.java 198 2010-11-04 17:24:51Z roland $
 * Copyright (C) 2007 Roland Krueger
 * Created on 04.11.2010
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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307
 * USA
 */
package info.rolandkrueger.roklib.webapps.urldispatching.urlparameters;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SingleLongWithIgnoredTextURLParameter extends SingleLongURLParameter
{
  private static final long serialVersionUID = 7990237721421647271L;
  
  private static final Pattern sPattern = Pattern.compile ("^(\\d+).*?"); 
  
  public SingleLongWithIgnoredTextURLParameter (String parameterName, Long defaultValue)
  {
    super (parameterName, defaultValue);
  }

  public SingleLongWithIgnoredTextURLParameter (String parameterName)
  {
    super (parameterName);
  }
 
  @Override
  protected boolean consumeImpl (Map<String, List<String>> parameters)
  {
    List<String> valueList = parameters.get (getParameterName ());
    if (valueList == null || valueList.isEmpty ())
    {
      return false;
    }
    for (int index = 0; index < valueList.size (); ++index)
    {
      String value = convertValue (valueList.get (index));
      valueList.set (index, value);
    }
    
    return super.consumeImpl (parameters);
  }
  
  private String convertValue (String value)
  {
    Matcher m = sPattern.matcher (value);
    if (m.find ())
    {
      return m.group (1);
    }
    return value;
  }
  
  @Override
  public boolean consumeList (String[] values)
  {
    if (values != null && values.length > 0 && values[0] != null)
    {
      values[0] = convertValue (values[0]);
    }
    return super.consumeList (values);
  }
  
}
