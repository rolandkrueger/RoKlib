/*
 * Copyright (C) 2007 Roland Krueger
 * Created on 21.10.2009
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
package org.roklib.util.conditionalengine;

import org.roklib.util.bool.AndOperation;
import org.roklib.util.bool.IdentityOperation;
import org.roklib.util.bool.NotOperation;
import org.roklib.util.bool.OrOperation;
import org.roklib.util.bool.XOROperation;
import org.roklib.util.helper.CheckForNull;

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
