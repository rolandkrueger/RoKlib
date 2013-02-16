/*
 * $Id: $ Copyright (C) 2007 Roland Krueger Created on 22.09.2012
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
package info.rolandkrueger.roklib.webapps.urldispatching;

/**
 * This action handler is used to invariably interpret all URI tokens that are passed into this
 * handler during the URL interpretation process. The value of this token can be obtained with
 * {@link #getCurrentURIToken()}. As this action handler class is a particularly configured
 * {@link RegexURLActionHandler}, all of the description of {@link RegexURLActionHandler} also
 * applies to this class.
 * 
 * @author Roland Krüger
 * @since 1.1.0
 */
public class CatchAllURLActionHandler extends RegexURLActionHandler
{
  private static final long serialVersionUID = -5033766191211958005L;

  public CatchAllURLActionHandler ()
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
    if (matchedTokenFragments != null) { return matchedTokenFragments[0]; }
    return null;
  }

  /**
   * <p>
   * {@inheritDoc}
   * </p>
   * <p>
   * Invariably returns <code>true</code> for this {@link CatchAllURLActionHandler}.
   * </p>
   */
  @Override
  protected boolean isResponsibleForToken (String uriToken)
  {
    mMatchedTokenFragments = new String[] { uriToken };
    return true;
  }
}
