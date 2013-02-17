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
package org.roklib.util;

/**
 * Default implementation for the interface {@link ApplicationMessageHandler}. Error and information messages are simple
 * printed to the console.
 * 
 * @author Roland Krueger
 */
public class DefaultErrorHandler implements ApplicationMessageHandler
{
  public void reportError (ApplicationError error)
  {
    System.err.println (String.format ("ERROR: %s %s; ERROR CLASS: %s", error.getDescription (),
        error.getCause () == null ? "" : "(" + error.getCause () + ")", error.getType ()));
  }

  public void infoMessage (String message)
  {
    System.out.println (String.format ("MESSAGE: %s", message));
  }
}
