/*
 * $Id: TableDataRow.java 178 2010-10-31 18:01:20Z roland $
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

import java.io.Serializable;
import java.util.Vector;

public class TableDataRow<T> implements Serializable
{
  private static final long serialVersionUID = 5963741457271921413L;

  private Vector<TableCellData<T>> mRowData;
  private boolean mVisible = true;
  private boolean mIsVisibleAfterFilter;
  private Integer mOriginalIndexInTable;

  public TableDataRow (int columnCount, int indexInTable)
  {
    if (columnCount < 0) throw new IllegalArgumentException ("Column count < 0");
    mRowData = new Vector<TableCellData<T>> (columnCount);
    for (int i = 0; i < columnCount; ++i)
      mRowData.add (null);
    mOriginalIndexInTable = indexInTable;
  }

  public TableCellData<T> addCellData (int columnIndex, String data, boolean tryToConvertToNumber)
  {
    return setData (columnIndex, new TableCellData<T> (data, tryToConvertToNumber));
  }

  public TableCellData<T> addCellData (int columnIndex, int data)
  {
    return setData (columnIndex, new TableCellData<T> (data));
  }

  public TableCellData<T> addCellData (int columnIndex, long data)
  {
    return setData (columnIndex, new TableCellData<T> (data));
  }

  public TableCellData<T> addCellData (int columnIndex, double data)
  {
    return setData (columnIndex, new TableCellData<T> (data));
  }

  public TableCellData<T> addCellData (int columnIndex, float data)
  {
    return setData (columnIndex, new TableCellData<T> (data));
  }

  public TableCellData<T> addCellData (int columnIndex, byte data)
  {
    return setData (columnIndex, new TableCellData<T> (data));
  }

  public TableCellData<T> addCellData (int columnIndex, char data)
  {
    return setData (columnIndex, new TableCellData<T> (data));
  }

  public TableCellData<T> addCellData (int columnIndex, short data)
  {
    return setData (columnIndex, new TableCellData<T> (data));
  }

  private TableCellData<T> setData (int columnIndex, TableCellData<T> data)
  {
    mRowData.set (columnIndex, data);
    return data;
  }

  public TableCellData<T> getRowData (int columnIndex)
  {
    return mRowData.get (columnIndex);
  }

  public boolean isVisible ()
  {
    return mVisible;
  }

  public void setVisible (boolean visible)
  {
    mVisible = visible;
  }

  public Integer getOriginalIndexInTable ()
  {
    return mOriginalIndexInTable;
  }

  @Override
  public String toString ()
  {
    return mRowData.toString ();
  }

  public boolean isVisibleAfterFilter ()
  {
    return mIsVisibleAfterFilter;
  }

  public void setVisibleAfterFilter (boolean isVisibleAfterFilter)
  {
    mIsVisibleAfterFilter = isVisibleAfterFilter;
  }
}