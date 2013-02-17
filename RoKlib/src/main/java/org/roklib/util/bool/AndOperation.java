/*
 * Copyright (C) 2007 Roland Krueger
 * Created on 20.10.2009
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
package org.roklib.util.bool;

public class AndOperation extends AbstractBinaryBooleanOperation
{
  private static final long serialVersionUID = -1716064083548567207L;

  public boolean execute ()
  {
    return mLeft && mRight;
  }

  public boolean canShortCircuit (boolean firstOperand)
  {
    return !firstOperand;
  }

  public boolean getShortCircuit (boolean firstOperand)
  {
    if (!canShortCircuit (firstOperand))
      throw new IllegalStateException ("Cannot short-circuit.");
    return false;
  }
}