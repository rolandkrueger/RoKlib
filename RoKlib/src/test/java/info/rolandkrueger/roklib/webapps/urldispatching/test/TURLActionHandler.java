/*
 * $Id: TURLActionHandler.java 186 2010-11-01 10:12:14Z roland $ Copyright (C) 2007 - 2010 Roland
 * Krueger Created on 17.02.2010
 * 
 * Author: Roland Krueger (www.rolandkrueger.info)
 * 
 * This file is part of RoKlib.
 * 
 * This library is free software; you can redistribute it and/or modify it under the terms of the
 * GNU Lesser General Public License as published by the Free Software Foundation; either version
 * 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with this library;
 * if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA
 * 02111-1307 USA
 */
package info.rolandkrueger.roklib.webapps.urldispatching.test;

import info.rolandkrueger.roklib.webapps.urldispatching.AbstractURLActionCommand;
import info.rolandkrueger.roklib.webapps.urldispatching.DispatchingURLActionHandler;
import info.rolandkrueger.roklib.webapps.urldispatching.urlparameters.IURLParameter;

import java.util.List;
import java.util.Map;

public class TURLActionHandler extends DispatchingURLActionHandler
{
  private static final long serialVersionUID = 6202866717473440168L;

  private TURLActionCommand mCommand;

  public TURLActionHandler (String actionName, TURLActionCommand command)
  {
    super (actionName);
    mCommand = command;
  }

  @Override
  protected AbstractURLActionCommand handleURLImpl (List<String> uriTokens, Map<String, List<String>> parameters,
      ParameterMode parameterMode)
  {
    return mCommand;
  }

  public void registerURLParameterForTest (IURLParameter<?> parameter, boolean optional)
  {
    registerURLParameter (parameter, optional);
  }

  public boolean haveRegisteredURLParametersErrors ()
  {
    return super.haveRegisteredURLParametersErrors ();
  }
}
