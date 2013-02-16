/*
 * $Id: BoolExpressionBuilder.java 178 2010-10-31 18:01:20Z roland $
 * Copyright (C) 2007 Roland Krueger
 * Created on 21.10.2009
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
package info.rolandkrueger.roklib.util.conditionalengine;

import info.rolandkrueger.roklib.util.bool.AndOperation;
import info.rolandkrueger.roklib.util.bool.IdentityOperation;
import info.rolandkrueger.roklib.util.bool.NotOperation;
import info.rolandkrueger.roklib.util.bool.OrOperation;
import info.rolandkrueger.roklib.util.bool.XOROperation;
import info.rolandkrueger.roklib.util.helper.CheckForNull;

public class BoolExpressionBuilder
{
  private BoolExpressionBuilder ()
  {
  }

  public static BooleanExpression createANDExpression (AbstractCondition... abstractConditions)
  {
    CheckForNull.check ((Object[]) abstractConditions);
    BooleanExpression result = new BooleanExpression (new AndOperation ());
    for (AbstractCondition condition : abstractConditions)
      result.addOperand (condition);
    return result;
  }

  public static BooleanExpression createORExpression (AbstractCondition... abstractConditions)
  {
    CheckForNull.check ((Object[]) abstractConditions);
    BooleanExpression result = new BooleanExpression (new OrOperation ());
    for (AbstractCondition condition : abstractConditions)
      result.addOperand (condition);
    return result;
  }

  public static BooleanExpression createXORExpression (AbstractCondition... abstractConditions)
  {
    CheckForNull.check ((Object[]) abstractConditions);
    BooleanExpression result = new BooleanExpression (new XOROperation ());
    for (AbstractCondition condition : abstractConditions)
      result.addOperand (condition);
    return result;
  }

  public static BooleanExpression createNOTExpression (AbstractCondition abstractCondition)
  {
    CheckForNull.check (abstractCondition);
    BooleanExpression result = new BooleanExpression (new NotOperation ());
    result.addOperand (abstractCondition);
    return result;

  }

  public static BooleanExpression createIDENTITYExpression (AbstractCondition abstractCondition)
  {
    CheckForNull.check (abstractCondition);
    BooleanExpression result = new BooleanExpression (new IdentityOperation ());
    result.addOperand (abstractCondition);
    return result;
  }
}
