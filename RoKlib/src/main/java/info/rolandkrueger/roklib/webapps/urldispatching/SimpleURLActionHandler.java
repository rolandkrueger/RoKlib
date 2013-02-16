/*
 * $Id: SimpleURLActionHandler.java 181 2010-11-01 09:39:13Z roland $ Copyright (C) 2007 - 2010
 * Roland Krueger Created on 10.03.2010
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

import java.util.List;
import java.util.Map;

/**
 * A simple URL action handler that directly returns a predefined action command when the URL
 * interpretation process reaches this handler. By that, {@link SimpleURLActionHandler}s always
 * represent the last token of an interpreted URL as they cannot dispatch to any sub-handlers.
 * 
 * @author Roland Krüger
 */
public class SimpleURLActionHandler extends AbstractURLActionHandler
{
  private static final long        serialVersionUID = 8203362201388037000L;

  private AbstractURLActionCommand mCommand;

  /**
   * Create a new {@link SimpleURLActionHandler} with the specified action name and action command.
   * 
   * @param actionName
   *          the action name for this handler
   * @param command
   *          action command to be returned when this action handler is being evaluated
   */
  public SimpleURLActionHandler (String actionName, AbstractURLActionCommand command)
  {
    super (actionName);
    mCommand = command;
  }

  /**
   * Directly returns the URL action command passed in through the constructor. All method arguments
   * are ignored.
   */
  @Override
  protected AbstractURLActionCommand handleURLImpl (List<String> uriTokens, Map<String, List<String>> parameters,
      ParameterMode parameterMode)
  {
    return mCommand;
  }
}
