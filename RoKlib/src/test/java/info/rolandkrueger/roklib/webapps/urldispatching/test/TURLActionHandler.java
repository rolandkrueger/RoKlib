/*
 * Copyright (C) 2007 - 2010 Roland Krueger 
 * Created on 17.02.2010
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
