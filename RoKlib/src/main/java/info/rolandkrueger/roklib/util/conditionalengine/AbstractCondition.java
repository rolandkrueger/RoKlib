/*
 * $Id: AbstractCondition.java 178 2010-10-31 18:01:20Z roland $
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
package info.rolandkrueger.roklib.util.conditionalengine;

import info.rolandkrueger.roklib.util.bool.IBooleanValueProvider;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public abstract class AbstractCondition implements IBooleanValueProvider
{
  private static final long serialVersionUID = 6265626147474145199L;

  private List<IConditionListener> mListeners;

  public void addConditionListener (IConditionListener listener)
  {
    if (mListeners == null) mListeners = new LinkedList<IConditionListener> ();
    mListeners.add (listener);
  }

  public void removeConditionListener (IConditionListener listener)
  {
    if (mListeners == null) return;
    mListeners.remove (listener);
  }

  protected void fireConditionChanged ()
  {
    if (mListeners == null) return;
    for (IConditionListener listener : new ArrayList<IConditionListener> (mListeners))
    {
      listener.conditionChanged (this);
    }
  }

  protected int getListenerCount ()
  {
    if (mListeners == null) return 0;
    return mListeners.size ();
  }

  public void clearConditionListeners ()
  {
    if (mListeners == null) return;
    mListeners.clear ();
  }
}
