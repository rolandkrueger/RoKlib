/*
 * $Id: ApplicationMessageHandler.java 178 2010-10-31 18:01:20Z roland $
 * Copyright (C) 2007 Roland Krueger
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
package info.rolandkrueger.roklib.util;

/**
 * This interface provides methods for reporting error and informational
 * messages. Classes that implement {@link ApplicationMessageHandler} have to
 * take care that these messages are delivered to the user in a specific way.
 * This may happen via a command console or by writing such messages into a log
 * file.
 * 
 * @see ApplicationError
 * @author Roland Krueger
 */
public interface ApplicationMessageHandler
{
  /**
   * Gets called in case of an error.
   * 
   * @see ApplicationError
   * @param error
   *          an {@link ApplicationError} object that summarizes the error
   */
  public void reportError (ApplicationError error);

  /**
   * Gets called if an information message has to be delivered.
   * 
   * @param message
   *          the message
   */
  public void infoMessage (String message);
}
