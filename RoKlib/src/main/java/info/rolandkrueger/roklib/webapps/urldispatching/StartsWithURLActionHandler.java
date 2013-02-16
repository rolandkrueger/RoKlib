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
 * <p>
 * URL action handler for matching all URI tokens which start with some particular character string.
 * As this action handler class is a particularly configured {@link RegexURLActionHandler}, all of
 * the description of {@link RegexURLActionHandler} also applies to this class.
 * </p>
 * <p>
 * This action handler is initialized with some prefix string which must not be all whitespaces or
 * the empty string. By default, there is one capturing group in the regular expression that
 * underlies this class. This group captures any substring that comes after the given prefix string
 * in the currently evaluated URL token.
 * </p>
 * 
 * @author Roland Krüger
 * @since 1.1.0
 * @see RegexURLActionHandler
 */
public class StartsWithURLActionHandler extends RegexURLActionHandler
{
  private static final long serialVersionUID = -8311620063509162064L;

  /**
   * Creates a new {@link StartsWithURLActionHandler} with the given prefix string.
   * 
   * @param prefix
   *          prefix string to be used for interpreting URI tokens.
   * @throws IllegalArgumentException
   *           if the prefix is the empty string or all whitespaces
   */
  public StartsWithURLActionHandler (String prefix)
  {
    super (prefix + "(.*)");
    if ("".equals (prefix.trim ())) { throw new IllegalArgumentException (
        "prefix must not be the empty string or all whitespaces"); }
  }
}
