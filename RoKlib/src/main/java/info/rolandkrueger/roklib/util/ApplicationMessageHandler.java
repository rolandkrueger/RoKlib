/*
 * Copyright (C) 2007 Roland Krueger
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
package info.rolandkrueger.roklib.util;

/**
 * This interface provides methods for reporting error and informational messages. Classes that implement
 * {@link ApplicationMessageHandler} have to take care that these messages are delivered to the user in a specific way.
 * This may happen via a command console or by writing such messages into a log file.
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
