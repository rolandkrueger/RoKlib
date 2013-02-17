/*
 * Copyright (C) 2007-2010 Roland Krueger
 * Created on 02.02.2010
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
package org.roklib.util.tables.filtertable;

import java.io.Serializable;
import java.util.Vector;

public class TableDataRow<T> implements Serializable
{
  private static final long        serialVersionUID = 5963741457271921413L;

  private Vector<TableCellData<T>> mRowData;
  private boolean                  mVisible         = true;
  private boolean                  mIsVisibleAfterFilter;
  private Integer                  mOriginalIndexInTable;

  public TableDataRow (int columnCount, int indexInTable)
  {
    if (columnCount < 0)
      throw new IllegalArgumentException ("Column count < 0");
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