/*
 * $Id: TableDataColumn.java 253 2011-01-25 16:16:31Z roland $
 * Copyright (C) 2007-2010 Roland Krueger
 * Created on 02.02.2010
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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307
 * USA
 */
package info.rolandkrueger.roklib.util.tables.filtertable;

import info.rolandkrueger.roklib.util.TernarySearchTreeMap;
import info.rolandkrueger.roklib.util.helper.CheckForNull;
import info.rolandkrueger.roklib.util.tables.filtertable.SortableFilterableTableDataModel.SearchMode;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.SortedSet;

public class TableDataColumn<T, H extends ITableDataColumnHeader> implements Serializable
{
  private static final long serialVersionUID = - 3866991508484425058L;

  private boolean mIsNumeric = true;
  private H mHeader;
  private SearchMode mSearchCapability;
  private boolean mCaseSensitive = false;
  private TernarySearchTreeMap<Collection<TableDataRow<T>>> mPrefixSearchTree;
  private TernarySearchTreeMap<Collection<TableDataRow<T>>> mInfixSearchTree;
  private int mDataStringMaxWidth = 0;

  // filter data
  private String mCurrentPrefixFilter;
  private String mCurrentInfixFilter;
  private String mCurrentSimilarityFilter;
  private int mSimilarityDistance;
  private int mSimilarityLengthTolerance;

  public TableDataColumn (SearchMode searchCapability)
  {
    this (null, searchCapability, false);
  }

  public TableDataColumn (SearchMode searchCapability, boolean caseSensitive)
  {
    this (null, searchCapability, caseSensitive);
  }

  public TableDataColumn (H header, SearchMode searchCapability, boolean caseSensitive)
  {
    CheckForNull.check (searchCapability);
    mHeader = header;
    mCaseSensitive = caseSensitive;

    if (searchCapability != SearchMode.NONE)
    {
      if (searchCapability == SearchMode.BOTH || searchCapability == SearchMode.PREFIX_SIMILARITY)
      {
        mPrefixSearchTree = new TernarySearchTreeMap<Collection<TableDataRow<T>>> ();
      }
      if (searchCapability == SearchMode.BOTH || searchCapability == SearchMode.INFIX)
      {
        mInfixSearchTree = new TernarySearchTreeMap<Collection<TableDataRow<T>>> ();
      }
    }
    mSearchCapability = searchCapability;
  }

  public void resetPrefixFilter ()
  {
    mCurrentPrefixFilter = null;
  }

  public void resetInfixFilter ()
  {
    mCurrentInfixFilter = null;
  }

  public void resetMatchSimilarFilter ()
  {
    mCurrentSimilarityFilter = null;
    mSimilarityDistance = 0;
    mSimilarityLengthTolerance = 0;
  }

  public String getCurrentPrefixFilter ()
  {
    return mCurrentPrefixFilter == null ? "" : mCurrentPrefixFilter;
  }

  public String getCurrentInfixFilter ()
  {
    return mCurrentInfixFilter == null ? "" : mCurrentInfixFilter;
  }

  public String getCurrentMatchSimilarFilter ()
  {
    return mCurrentSimilarityFilter == null ? "" : mCurrentSimilarityFilter;
  }

  public void setPrefixFilter (String prefix)
  {
    if (mPrefixSearchTree == null)
      throw new IllegalStateException ("Column is not configured for prefix matching.");
    if (! mCaseSensitive) prefix = prefix.toLowerCase ();
    mCurrentPrefixFilter = prefix;
  }

  public void setInfixFilter (String infix)
  {
    if (mInfixSearchTree == null)
      throw new IllegalStateException ("Column is not configured for infix matching.");
    if (! mCaseSensitive) infix = infix.toLowerCase ();
    mCurrentInfixFilter = infix;
  }

  public void setMatchSimilarFilter (String filter, int distance, int lengthTolerance)
  {
    if (mPrefixSearchTree == null)
      throw new IllegalStateException ("Column is not configured for similarity matching.");
    if (! mCaseSensitive) filter = filter.toLowerCase ();
    mCurrentSimilarityFilter = filter;
    mSimilarityDistance = distance;
    mSimilarityLengthTolerance = lengthTolerance;
  }

  public void resetFilters ()
  {
    resetPrefixFilter ();
    resetInfixFilter ();
    resetMatchSimilarFilter ();
  }

  protected synchronized void addToSearchTree (String key, TableDataRow<T> row)
  {
    if (mSearchCapability == SearchMode.NONE || key == null || key.equals ("")) return;
    int keyLength = key.length ();
    if (keyLength > mDataStringMaxWidth) mDataStringMaxWidth = keyLength;

    if (! mCaseSensitive) key = key.toLowerCase ();

    if (mSearchCapability == SearchMode.BOTH || mSearchCapability == SearchMode.PREFIX_SIMILARITY)
    {
      assert mPrefixSearchTree != null;
      Collection<TableDataRow<T>> rows = mPrefixSearchTree.get (key);

      if (rows == null)
      {
        rows = new LinkedList<TableDataRow<T>> ();
        mPrefixSearchTree.put (key, rows);
      }
      rows.add (row);
    }

    if (mSearchCapability == SearchMode.BOTH || mSearchCapability == SearchMode.INFIX)
    {
      assert mInfixSearchTree != null;
      for (int i = 0; i < key.length (); ++i)
      {
        String infixKey = key.substring (i);
        Collection<TableDataRow<T>> rows = mInfixSearchTree.get (infixKey);

        if (rows == null)
        {
          rows = new LinkedList<TableDataRow<T>> ();
          mInfixSearchTree.put (infixKey, rows);
        }
        rows.add (row);
      }
    }
  }

  protected synchronized void updateSearchTree (String oldKey, String newKey, TableDataRow<T> row)
  {
    if (mSearchCapability == SearchMode.NONE) return;
    if (oldKey != null)
    {
      if (mPrefixSearchTree != null)
      {
        Collection<TableDataRow<T>> col = mPrefixSearchTree.get (oldKey);
        if (col != null) col.remove (row);
      }
      if (mInfixSearchTree != null)
      {
        Collection<TableDataRow<T>> col = mInfixSearchTree.get (oldKey);
        if (col != null) col.remove (row);
      }
    }
    addToSearchTree (newKey, row);
  }

  public synchronized void filterWithPrefix ()
  {
    if (mCurrentPrefixFilter == null || mCurrentPrefixFilter.equals ("")) return;
    prepareFilter ();

    for (Iterator<Entry<CharSequence, Collection<TableDataRow<T>>>> it = mPrefixSearchTree
        .getPrefixSubtreeIterator (mCurrentPrefixFilter).iterator (); it.hasNext ();)
    {
      for (TableDataRow<T> row : it.next ().getValue ())
      {
        row.setVisibleAfterFilter (true);
      }
    }
    finishFilter ();
  }

  public synchronized void filterWithSimilarContent ()
  {
    if (mCurrentSimilarityFilter == null || mCurrentSimilarityFilter.equals ("")) return;
    prepareFilter ();
    SortedSet<CharSequence> set = mPrefixSearchTree.matchAlmost (mCurrentSimilarityFilter,
        mSimilarityDistance, mSimilarityLengthTolerance);

    for (CharSequence key : set)
    {
      for (TableDataRow<T> row : mPrefixSearchTree.get (key))
      {
        row.setVisibleAfterFilter (true);
      }
    }
    finishFilter ();
  }

  public synchronized void filterWithInfix ()
  {
    if (mCurrentInfixFilter == null || mCurrentInfixFilter.equals ("")) return;
    prepareFilter ();

    for (Iterator<Entry<CharSequence, Collection<TableDataRow<T>>>> it = mInfixSearchTree
        .getPrefixSubtreeIterator (mCurrentInfixFilter).iterator (); it.hasNext ();)
    {
      for (TableDataRow<T> row : it.next ().getValue ())
      {
        row.setVisibleAfterFilter (true);
      }
    }
    finishFilter ();
  }

  private void prepareFilter ()
  {
    TernarySearchTreeMap<Collection<TableDataRow<T>>> map = mPrefixSearchTree;
    if (map == null) map = mInfixSearchTree;

    for (CharSequence key : map.keySet ())
    {
      for (TableDataRow<T> row : map.get (key))
      {
        row.setVisibleAfterFilter (false);
      }
    }
  }

  private void finishFilter ()
  {
    TernarySearchTreeMap<Collection<TableDataRow<T>>> map = mPrefixSearchTree;
    if (map == null) map = mInfixSearchTree;

    for (CharSequence key : map.keySet ())
    {
      for (TableDataRow<T> row : map.get (key))
      {
        if (! (row.isVisible () && row.isVisibleAfterFilter ())) row.setVisible (false);
      }
    }
  }

  protected void clearSearchIndices ()
  {
    if (mPrefixSearchTree != null) mPrefixSearchTree.clear ();
    if (mInfixSearchTree != null) mInfixSearchTree.clear ();
  }

  public int getMaximumCellValueLength ()
  {
    return mDataStringMaxWidth;
  }

  public boolean isNumeric ()
  {
    return mIsNumeric;
  }

  public void setNumeric (boolean isNumeric)
  {
    mIsNumeric = isNumeric;
  }

  public H getHeader ()
  {
    return mHeader;
  }

  public void setHeader (H header)
  {
    CheckForNull.check (header);
    mHeader = header;
  }

  public void setCaseSensitiveFiltering (boolean caseSensitive)
  {
    mCaseSensitive = caseSensitive;
  }
}
