/*
 * Copyright (C) 2007 Roland Krueger 
 * Created on 22.09.2012
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
package org.roklib.webapps.uridispatching;

/**
 * This action handler is used to invariably interpret all URI tokens that are passed into this handler during the URI
 * interpretation process. The value of this token can be obtained with {@link #getCurrentURIToken()}. As this action
 * handler class is a particularly configured {@link RegexURIActionHandler}, all of the description of
 * {@link RegexURIActionHandler} also applies to this class.
 * 
 * @author Roland Krüger
 * @since 1.1.0
 */
public class CatchAllURIActionHandler extends RegexURIActionHandler
{
  private static final long serialVersionUID = -5033766191211958005L;

  public CatchAllURIActionHandler ()
  {
    super ("(.*)");
  }

  /**
   * Returns the value of the URI token that has been interpreted by this action handler.
   * 
   * @return the URI token captured by this action handler
   */
  public String getCurrentURIToken ()
  {
    String[] matchedTokenFragments = getMatchedTokenFragments ();
    if (matchedTokenFragments != null)
    {
      return matchedTokenFragments[0];
    }
    return null;
  }

  /**
   * <p>
   * {@inheritDoc}
   * </p>
   * <p>
   * Invariably returns <code>true</code> for this {@link CatchAllURIActionHandler}.
   * </p>
   */
  @Override
  protected boolean isResponsibleForToken (String uriToken)
  {
    mMatchedTokenFragments = new String[] { uriToken };
    return true;
  }
}
