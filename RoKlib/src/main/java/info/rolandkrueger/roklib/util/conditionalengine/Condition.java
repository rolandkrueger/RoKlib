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
package info.rolandkrueger.roklib.util.conditionalengine;

import info.rolandkrueger.roklib.util.helper.CheckForNull;

public class Condition extends AbstractCondition
{
  private static final long serialVersionUID = 2843942589476694624L;

  private boolean           mBooleanValue;
  private String            mName;

  public Condition (String name)
  {
    CheckForNull.check (name);
    mName = name;
    mBooleanValue = false;
  }

  public Condition (String name, boolean value)
  {
    this (name);
    mBooleanValue = value;
  }

  public boolean getBooleanValue ()
  {
    return mBooleanValue;
  }

  public void setValue (boolean value)
  {
    boolean previousValue = mBooleanValue;
    mBooleanValue = value;
    if (previousValue != value)
      fireConditionChanged ();
  }

  public String getName ()
  {
    return mName;
  }

  @Override
  public boolean equals (Object obj)
  {
    if (obj == null)
      return false;
    if (obj == this)
      return true;
    if (obj instanceof Condition)
    {
      Condition other = (Condition) obj;
      if (other.mName.equals (mName))
        return true;
    }
    return false;
  }

  @Override
  public int hashCode ()
  {
    return mName.hashCode ();
  }

  @Override
  public String toString ()
  {
    return String.format ("%s=%b", mName, mBooleanValue);
  }
}
