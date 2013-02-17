/*
 * Copyright (C) 2003 Roland Krueger
 * Created on Oct 15, 2003
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
package org.roklib.ui.swing.rapidsuggest;


import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import javax.swing.DefaultComboBoxModel;

import org.roklib.util.TernarySearchTreeSet;

/**
 * @author Roland Krueger
 * @version $Id: SuggestionComboBoxModel.java 128 2010-03-05 06:45:32Z roland $
 */
public class SuggestionComboBoxModel extends DefaultComboBoxModel
{
  private static final long    serialVersionUID  = -8908886493531931076L;

  private TernarySearchTreeSet mData;
  private int                  selectedItemIndex = -1;

  public SuggestionComboBoxModel (CharSequence[] items)
  {
    this (items, false);
  }

  public SuggestionComboBoxModel (CharSequence[] items, boolean caseInsensitive)
  {
    this (Arrays.asList (items), caseInsensitive);
  }

  public SuggestionComboBoxModel ()
  {
    this (false);
  }

  public SuggestionComboBoxModel (boolean caseInsensitive)
  {
    mData = new TernarySearchTreeSet (caseInsensitive);
  }

  public SuggestionComboBoxModel (Collection<? extends CharSequence> set)
  {
    this (set, false);
  }

  public SuggestionComboBoxModel (Collection<? extends CharSequence> set, boolean caseInsensitive)
  {
    this (caseInsensitive);
    mData.addAll (set);
  }

  public String getSuggestion (String prefix)
  {
    String match = null;
    Iterator<CharSequence> iterator = mData.getPrefixMatch (prefix).iterator ();

    if (iterator.hasNext ())
    {
      match = iterator.next ().toString ();
    }
    setSelectedItem (match);
    return match;
  }

  public boolean contains (Object o)
  {
    return mData.contains (o);
  }

  public void setSelectedItem (Object anObject)
  {
    if (anObject == null)
    {
      selectedItemIndex = -1;
      return;
    }
    selectedItemIndex = mData.indexOf (anObject.toString ());
  }

  public Object getSelectedItem ()
  {
    return selectedItemIndex == -1 ? null : getElementAt (selectedItemIndex);
  }

  public void removeElementAt (int index)
  {
    mData.remove (mData.getElementAt (index));
  }

  public void addElement (Object obj)
  {
    mData.add (obj.toString ());
  }

  public void removeElement (Object obj)
  {
    mData.remove (obj.toString ());
  }

  public void insertElementAt (Object obj, int index)
  {
    addElement (obj);
  }

  public int getSize ()
  {
    return mData.size ();
  }

  public Object getElementAt (int index)
  {
    return mData.getElementAt (index);
  }
}
