/*
 * $Id: SuggestionComboBoxModel.java 260 2011-01-27 19:51:26Z roland $
 * Copyright (C) 2003 Roland Krueger
 * Created on Oct 15, 2003
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
package info.rolandkrueger.roklib.ui.swing.rapidsuggest;

import info.rolandkrueger.roklib.util.TernarySearchTreeSet;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import javax.swing.DefaultComboBoxModel;

/**
 * @author Roland Krueger
 * @version $Id: SuggestionComboBoxModel.java 128 2010-03-05 06:45:32Z
 *          roland $
 */
public class SuggestionComboBoxModel extends DefaultComboBoxModel
{
  private static final long serialVersionUID = - 8908886493531931076L;
  
  private TernarySearchTreeSet mData;
  private int selectedItemIndex = - 1;

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
