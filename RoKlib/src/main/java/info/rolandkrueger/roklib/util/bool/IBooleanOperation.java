/*
 * $Id: IBooleanOperation.java 178 2010-10-31 18:01:20Z roland $
 * Copyright (C) 2007 Roland Krueger
 * Created on 20.10.2009
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
package info.rolandkrueger.roklib.util.bool;

import java.io.Serializable;

public interface IBooleanOperation extends Serializable
{
  public boolean execute ();

  public void setLeftHandOperand (boolean left);

  public void setRightHandOperand (boolean right);

  /**
   * <p>
   * Returns <code>true</code> if this Boolean operation could be short-circuited if the given
   * parameter was used as the operation's first operand.
   * </p>
   * <p>
   * For example, if this Boolean operation represents the <code>AND</code> operation, using
   * <code>false</code> as the first operand would make short-circuiting possible. The
   * short-circuited outcome of the operation would then also be <code>false</code>.
   * </p>
   * 
   * @param firstOperand
   *          value for the operation's first operand
   */
  public boolean canShortCircuit (boolean firstOperand);

  /**
   * <p>
   * Returns the final result of this Boolean operation for the given first operand value if the
   * operation is short-circuited. For example, passing <code>true</code> as the first operand value
   * for the <code>OR</code> operation will yield <code>true</code> as the operation's result.
   * </p>
   * <p>
   * Implementations of this method should check if short-circuiting is possible at all for the
   * given operand value. If that is not the case, an {@link IllegalStateException} should be
   * thrown.
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
