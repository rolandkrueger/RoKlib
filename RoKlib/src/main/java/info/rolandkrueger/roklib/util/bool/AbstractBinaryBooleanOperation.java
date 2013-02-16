/*
 * $Id: AbstractBinaryBooleanOperation.java 178 2010-10-31 18:01:20Z roland $
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

public abstract class AbstractBinaryBooleanOperation implements IBooleanOperation
{
  private static final long serialVersionUID = 2820346799434520249L;

  protected boolean mLeft;
  protected boolean mRight;

  public final void setLeftHandOperand (boolean left)
  {
    mLeft = left;
  }

  public final void setRightHandOperand (boolean right)
  {
    mRight = right;
  }

  public final int getMinimumNumberOfOperands ()
  {
    return 2;
  }

  public final boolean isUnaryOperation ()
  {
    return false;
  }
}
