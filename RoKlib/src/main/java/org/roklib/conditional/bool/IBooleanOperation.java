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
package org.roklib.conditional.bool;

import java.io.Serializable;

public interface IBooleanOperation extends Serializable
{
  public boolean execute ();

  public void setLeftHandOperand (boolean left);

  public void setRightHandOperand (boolean right);

  /**
   * <p>
   * Returns <code>true</code> if this Boolean operation could be short-circuited if the given parameter was used as the
   * operation's first operand.
   * </p>
   * <p>
   * For example, if this Boolean operation represents the <code>AND</code> operation, using <code>false</code> as the
   * first operand would make short-circuiting possible. The short-circuited outcome of the operation would then also be
   * <code>false</code>.
   * </p>
   * 
   * @param firstOperand
   *          value for the operation's first operand
   */
  public boolean canShortCircuit (boolean firstOperand);

  /**
   * <p>
   * Returns the final result of this Boolean operation for the given first operand value if the operation is
   * short-circuited. For example, passing <code>true</code> as the first operand value for the <code>OR</code>
   * operation will yield <code>true</code> as the operation's result.
   * </p>
   * <p>
   * Implementations of this method should check if short-circuiting is possible at all for the given operand value. If
   * that is not the case, an {@link IllegalStateException} should be thrown.
   * </p>
   * 
   * @param firstOperand
   *          value for the operation's first operand
   * @throws IllegalStateException
   *           if short-circuiting is not possible for the given operand value
   */
  public boolean getShortCircuit (boolean firstOperand);

  public int getMinimumNumberOfOperands ();

  /**
   * Returns <code>true</code> if this Boolean operation takes only one operand.
   */
  public boolean isUnaryOperation ();
}
