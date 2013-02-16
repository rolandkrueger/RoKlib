/*
 * $Id: OrOperation.java 178 2010-10-31 18:01:20Z roland $
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

public class OrOperation extends AbstractBinaryBooleanOperation
{
  private static final long serialVersionUID = 2060306880093108085L;

  public boolean execute ()
  {
    return mLeft || mRight;
  }

  public boolean canShortCircuit (boolean firstOperand)
  {
    return firstOperand;
  }

  public boolean getShortCircuit (boolean firstOperand)
  {
    if (! canShortCircuit (firstOperand))
      throw new IllegalStateException ("Cannot short-circuit.");
    return true;
  }
}
