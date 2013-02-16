/*
 * Copyright (C) 2007-2010 Roland Krueger
 * Created on 03.02.2010
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
package info.rolandkrueger.roklib.util.tables.filtertable;

import info.rolandkrueger.roklib.util.helper.CheckForNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.SortOrder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

public class SortableFilterableTableDataModel<T, H extends ITableDataColumnHeader> extends AbstractTableModel implements
    TableModelListener
{
  private static final long serialVersionUID = 2016102798175233234L;

  public enum SearchMode
  {
    NONE, PREFIX_SIMILARITY, INFIX, BOTH
  };

  private List<TableDataRow<T>>                  mTableRows;
  private List<TableDataColumn<T, H>>            mTableColumns;
  private List<TableDataRow<T>>                  mVisibleRows;
  private int                                    mColumnCount;
  private List<Comparator<T>>                    mColumnComparators;
  private List<Class<?>>                         mColumnClasses;
  private int                                    mCurrentAddColumnIndex = 0;
  private TableDataRow<T>                        mCurrentRow;
  private int                                    mSortColumnIndex       = -1;
  private SortOrder                              mSortOrder;
  private final StringComparator                 mStringComparator      = new StringComparator ();
  private final NumberComparator                 mnNumberComparator     = new NumberComparator ();
  private TableModel                             mDataFromTableModel;
  private List<ISortableFilterableTableListener> mListeners;

  public SortableFilterableTableDataModel (TableModel tableModel, SearchMode searchCapability)
  {
    this (tableModel.getColumnCount (), searchCapability);
    mDataFromTableModel = tableModel;
    mDataFromTableModel.addTableModelListener (this);
    buildFromTableModel ();
  }

  public SortableFilterableTableDataModel (int columnCount, SearchMode searchCapability)
  {
    if (columnCount < 0)
      throw new IllegalArgumentException ("Column count must be a positive integer.");
    mTableColumns = new ArrayList<TableDataColumn<T, H>> (columnCount);
    mColumnClasses = new ArrayList<Class<?>> (mColumnCount);
    for (int i = 0; i < columnCount; ++i)
    {
      mTableColumns.add (new TableDataColumn<T, H> (searchCapability));
      mColumnClasses.add (null);
    }
    mTableRows = new ArrayList<TableDataRow<T>> ();
    mVisibleRows = new ArrayList<TableDataRow<T>> ();
    mListeners = new ArrayList<ISortableFilterableTableListener> ();
    mColumnCount = columnCount;
  }

  public void addSortableFilterableTableListener (ISortableFilterableTableListener listener)
  {
    mListeners.add (listener);
  }

  public void removeSortableFilterableTableListener (ISortableFilterableTableListener listener)
  {
    mListeners.remove (listener);
  }

  public synchronized SortableFilterableTableDataModel<T, H> addStringData (String data)
  {
    updateInsertData (addCellLabel (data));
    return this;
  }

  public synchronized SortableFilterableTableDataModel<T, H> addData (String data, T additional)
  {
    updateInsertData (addCellLabel (data).setAdditionalData (additional));
    return this;
  }

  public synchronized SortableFilterableTableDataModel<T, H> addData (T data)
  {
    if (data != null)
      addCellLabel (data.toString ());
    else
      addCellLabel ("");
    updateInsertData (mCurrentRow.getRowData (mCurrentAddColumnIndex).setAdditionalData (data));
    return this;
  }

  public synchronized SortableFilterableTableDataModel<T, H> addData (int data)
  {
    addData (data, null);
    return this;
  }

  public synchronized SortableFilterableTableDataModel<T, H> addData (long data)
  {
    addData (data, null);
    return this;
  }

  public synchronized SortableFilterableTableDataModel<T, H> addData (float data)
  {
    addData (data, null);
    return this;
  }

  public synchronized SortableFilterableTableDataModel<T, H> addData (double data)
  {
    addData (data, null);
    return this;
  }

  public synchronized SortableFilterableTableDataModel<T, H> addData (byte data)
  {
    addData (data, null);
    return this;
  }

  public synchronized SortableFilterableTableDataModel<T, H> addData (short data)
  {
    addData (data, null);
    return this;
  }

  public synchronized SortableFilterableTableDataModel<T, H> addData (char data)
  {
    addData (data, null);
    return this;
  }

  public synchronized SortableFilterableTableDataModel<T, H> addData (int data, T additional)
  {
    updateInsertData (getCurrentAddRow ().addCellData (mCurrentAddColumnIndex, data).setAdditionalData (additional));
    return this;
  }

  public synchronized SortableFilterableTableDataModel<T, H> addData (long data, T additional)
  {
    updateInsertData (getCurrentAddRow ().addCellData (mCurrentAddColumnIndex, data).setAdditionalData (additional));
    return this;
  }

  public synchronized SortableFilterableTableDataModel<T, H> addData (double data, T additional)
  {
    updateInsertData (getCurrentAddRow ().addCellData (mCurrentAddColumnIndex, data).setAdditionalData (additional));
    return this;
  }

  public synchronized SortableFilterableTableDataModel<T, H> addData (float data, T additional)
  {
    updateInsertData (getCurrentAddRow ().addCellData (mCurrentAddColumnIndex, data).setAdditionalData (additional));
    return this;
  }

  public synchronized SortableFilterableTableDataModel<T, H> addData (byte data, T additional)
  {
    updateInsertData (getCurrentAddRow ().addCellData (mCurrentAddColumnIndex, data).setAdditionalData (additional));
    return this;
  }

  public synchronized SortableFilterableTableDataModel<T, H> addData (char data, T additional)
  {
    updateInsertData (getCurrentAddRow ().addCellData (mCurrentAddColumnIndex, data).setAdditionalData (additional));
    return this;
  }

  private TableCellData<T> addCellLabel (String label)
  {
    TableDataRow<T> row = getCurrentAddRow ();
    TableDataColumn<T, H> col = mTableColumns.get (mCurrentAddColumnIndex);
    TableCellData<T> cellData = row.addCellData (mCurrentAddColumnIndex, label, col.isNumeric ());
    if (col.isNumeric () && !cellData.isNumeric ())
      col.setNumeric (false);
    return cellData;
  }

  private TableDataRow<T> getCurrentAddRow ()
  {
    if (mDataFromTableModel != null)
      throw new IllegalStateException ("Cannot add data. Data is already defined through a TableModel.");
    if (mCurrentRow == null)
    {
      mCurrentRow = new TableDataRow<T> (mColumnCount, mTableRows.size ());
      mTableRows.add (mCurrentRow);
      mVisibleRows.add (mCurrentRow);
      // fireTableRowsInserted (mTableRows.size () - 1, mTableRows.size () - 1);
      updateVisibleRows ();
    }
    return mCurrentRow;
  }

  private void updateInsertData (TableCellData<T> cellData)
  {
    mTableColumns.get (mCurrentAddColumnIndex).addToSearchTree (cellData.getStringValue (), mCurrentRow);
    if (mCurrentAddColumnIndex == mColumnCount - 1)
    {
      mCurrentAddColumnIndex = 0;
      mCurrentRow = null;
    } else
      ++mCurrentAddColumnIndex;
  }

  public synchronized void refreshSearchIndices ()
  {
    int index = 0;
    for (TableDataColumn<T, H> column : mTableColumns)
    {
      column.clearSearchIndices ();
      for (TableDataRow<T> row : mTableRows)
      {
        column.addToSearchTree (row.getRowData (index).getStringValue (), row);
      }
    }
    ++index;
  }

  public synchronized void applyFilters ()
  {
    for (TableDataRow<T> row : mTableRows)
    {
      row.setVisible (true);
    }
    for (TableDataColumn<T, H> column : mTableColumns)
    {
      column.filterWithPrefix ();
      column.filterWithInfix ();
      column.filterWithSimilarContent ();
    }
    updateVisibleRows ();
  }

  private void updateVisibleRows ()
  {
    mVisibleRows.clear ();
    for (TableDataRow<T> row : mTableRows)
    {
      if (row.isVisible ())
      {
        mVisibleRows.add (row);
      }
    }
    // fireTableStructureChanged ();
    // fireTableDataChanged ();
  }

  public String getInfixFilter (int columnIndex)
  {
    checkColumnIndex (columnIndex);
    return mTableColumns.get (columnIndex).getCurrentInfixFilter ();
  }

  public String getPrefixFilter (int columnIndex)
  {
    checkColumnIndex (columnIndex);
    return mTableColumns.get (columnIndex).getCurrentPrefixFilter ();
  }

  public String getMatchSimilarFilter (int columnIndex)
  {
    checkColumnIndex (columnIndex);
    return mTableColumns.get (columnIndex).getCurrentMatchSimilarFilter ();
  }

  public synchronized void setInfixFilter (String infix, int columnIndex, boolean applyFilter)
  {
    checkColumnIndex (columnIndex);
    mTableColumns.get (columnIndex).setInfixFilter (infix);
    if (applyFilter)
    {
      mTableColumns.get (columnIndex).filterWithInfix ();
      updateVisibleRows ();
    }
  }

  public synchronized void setPrefixFilter (String prefix, int columnIndex, boolean applyFilter)
  {
    checkColumnIndex (columnIndex);
    mTableColumns.get (columnIndex).setPrefixFilter (prefix);
    if (applyFilter)
    {
      mTableColumns.get (columnIndex).filterWithPrefix ();
      updateVisibleRows ();
    }
  }

  public synchronized void setMatchSimilarFilter (String filter, int distance, int lengthTolerance, int columnIndex,
      boolean applyFilter)
  {
    checkColumnIndex (columnIndex);
    mTableColumns.get (columnIndex).setMatchSimilarFilter (filter, distance, lengthTolerance);
    if (applyFilter)
    {
      mTableColumns.get (columnIndex).filterWithSimilarContent ();
      updateVisibleRows ();
    }
  }

  public synchronized void clear ()
  {
    mTableColumns.clear ();
    mTableRows.clear ();
    mVisibleRows.clear ();
    mCurrentAddColumnIndex = 0;
    mCurrentRow = null;
    fireTableDataChanged ();
  }

  public synchronized void setColumnHeader (H header, int columnIndex)
  {
    checkColumnIndex (columnIndex);
    mTableColumns.get (columnIndex).setHeader (header);
  }

  public H getColumnHeader (int columnIndex)
  {
    checkColumnIndex (columnIndex);
    return mTableColumns.get (columnIndex).getHeader ();
  }

  public synchronized Comparator<T> setColumnSortingComparator (Comparator<T> comparator, int columnIndex)
  {
    checkColumnIndex (columnIndex);
    if (mColumnComparators == null)
    {
      mColumnComparators = new ArrayList<Comparator<T>> (mColumnCount);
      for (int i = 0; i < mColumnCount; ++i)
        mColumnComparators.add (null);
    }
    Comparator<T> old = mColumnComparators.get (columnIndex);
    mColumnComparators.set (columnIndex, comparator);
    return old;
  }

  public synchronized void setColumnClass (Class<?> clazz, int columnIndex)
  {
    checkColumnIndex (columnIndex);
    mColumnClasses.set (columnIndex, clazz);
  }

  public synchronized void setCaseSensitiveFiltering (boolean caseSensitive, int columnIndex)
  {
    checkColumnIndex (columnIndex);
    mTableColumns.get (columnIndex).setCaseSensitiveFiltering (caseSensitive);
    refreshSearchIndices ();
  }

  public synchronized void resetFilters ()
  {
    for (TableDataColumn<T, H> column : mTableColumns)
    {
      column.resetFilters ();
    }
    for (TableDataRow<T> row : mTableRows)
    {
      row.setVisible (true);
    }
    mVisibleRows.clear ();
    mVisibleRows.addAll (mTableRows);
    fireResetAllFilters ();
  }

  public Class<?> getColumnClass (int columnIndex)
  {
    Class<?> result = mColumnClasses.get (columnIndex);
    if (result == null)
      return String.class;
    return result;
  }

  public int getColumnCount ()
  {
    return mColumnCount;
  }

  public String getColumnName (int columnIndex)
  {
    checkColumnIndex (columnIndex);
    H header = mTableColumns.get (columnIndex).getHeader ();
    if (header == null)
      return "";
    return header.getHeadline ();
  }

  public int getRowCount ()
  {
    return mVisibleRows.size ();
  }

  public Object getValueAt (int rowIndex, int columnIndex)
  {
    return mVisibleRows.get (rowIndex).getRowData (columnIndex).getStringValue ();
  }

  public TableCellData<T> getCellDataAt (int rowIndex, int columnIndex)
  {
    return mVisibleRows.get (rowIndex).getRowData (columnIndex);
  }

  public boolean isCellEditable (int rowIndex, int columnIndex)
  {
    return false;
  }

  public synchronized void setValueAt (Object aValue, int rowIndex, int columnIndex)
  {
    TableDataColumn<T, H> column = mTableColumns.get (columnIndex);
    TableDataRow<T> row = mVisibleRows.get (rowIndex);
    TableCellData<T> cellData = row.getRowData (columnIndex);
    String oldValue = cellData.getStringValue ();
    String newValue = aValue.toString ();
    cellData.setStringValue (newValue, column.isNumeric ());
    if (column.isNumeric () && !cellData.isNumeric ())
      column.setNumeric (false);
    column.updateSearchTree (oldValue, newValue, row);
    fireTableCellUpdated (rowIndex, columnIndex);
  }

  public int getMaximumCellValueLength (int forColumnIndex)
  {
    checkColumnIndex (forColumnIndex);
    return mTableColumns.get (forColumnIndex).getMaximumCellValueLength ();
  }

  private void checkColumnIndex (int index)
  {
    if (index < 0)
      throw new ArrayIndexOutOfBoundsException (String.format ("index %d < 0", index));
    if (index >= mColumnCount)
      throw new ArrayIndexOutOfBoundsException (String.format ("index %d >= %d", index, mColumnCount));
  }

  public synchronized void restoreOriginalOrdering ()
  {
    mSortOrder = SortOrder.UNSORTED;
    mSortColumnIndex = -1;
    Collections.sort (mVisibleRows, mStringComparator); // it doesn't matter
                                                        // which comparator is
                                                        // used to restore the
                                                        // original ordering
  }

  public synchronized void sortColumn (SortOrder order, int columnIndex)
  {
    checkColumnIndex (columnIndex);
    mSortOrder = order;
    TableDataColumn<T, H> column = mTableColumns.get (columnIndex);
    Comparator<TableDataRow<T>> comparator = mStringComparator;
    if (column.isNumeric ())
      comparator = mnNumberComparator;
    Comparator<T> tComparator = mColumnComparators.get (columnIndex);
    if (tComparator != null)
      comparator = new TComparator (tComparator);

    mSortColumnIndex = columnIndex;
    Collections.sort (mVisibleRows, comparator);
    Collections.sort (mTableRows, comparator);
    fireTableDataChanged ();
  }

  private class TComparator implements Comparator<TableDataRow<T>>
  {
    private Comparator<T> mComparator;

    public TComparator (Comparator<T> comparator)
    {
      assert comparator != null;
      mComparator = comparator;
    }

    public int compare (TableDataRow<T> o1, TableDataRow<T> o2)
    {
      if (mSortOrder == SortOrder.UNSORTED)
        return o1.getOriginalIndexInTable ().compareTo (o2.getOriginalIndexInTable ());
      if (mSortOrder == SortOrder.ASCENDING)
        return mComparator.compare (o1.getRowData (mSortColumnIndex).getAdditionalData (),
            o2.getRowData (mSortColumnIndex).getAdditionalData ());
      else if (mSortOrder == SortOrder.DESCENDING)
        return mComparator.compare (o2.getRowData (mSortColumnIndex).getAdditionalData (),
            o1.getRowData (mSortColumnIndex).getAdditionalData ());
      return 0;
    }
  }

  private class StringComparator implements Comparator<TableDataRow<T>>
  {
    public int compare (TableDataRow<T> o1, TableDataRow<T> o2)
    {
      if (mSortOrder == SortOrder.UNSORTED)
        return o1.getOriginalIndexInTable ().compareTo (o2.getOriginalIndexInTable ());
      if (mSortOrder == SortOrder.ASCENDING)
        return o1.getRowData (mSortColumnIndex).getStringValue ()
            .compareTo (o2.getRowData (mSortColumnIndex).getStringValue ());
      else if (mSortOrder == SortOrder.DESCENDING)
        return o2.getRowData (mSortColumnIndex).getStringValue ()
            .compareTo (o1.getRowData (mSortColumnIndex).getStringValue ());
      return 0;
    }
  }

  private class NumberComparator implements Comparator<TableDataRow<T>>
  {
    public int compare (TableDataRow<T> o1, TableDataRow<T> o2)
    {
      if (mSortOrder == SortOrder.UNSORTED)
        return o1.getOriginalIndexInTable ().compareTo (o2.getOriginalIndexInTable ());
      if (mSortOrder == SortOrder.ASCENDING)
        return o1.getRowData (mSortColumnIndex).getNumericValue ()
            .compareTo (o2.getRowData (mSortColumnIndex).getNumericValue ());
      else if (mSortOrder == SortOrder.DESCENDING)
        return o2.getRowData (mSortColumnIndex).getNumericValue ()
            .compareTo (o1.getRowData (mSortColumnIndex).getNumericValue ());
      return 0;
    }
  }

  public void setTableModel (TableModel model)
  {
    CheckForNull.check (model);
    mDataFromTableModel = model;
    clear ();
    buildFromTableModel ();
  }

  private void buildFromTableModel ()
  {
    TableModel model = mDataFromTableModel;
    mDataFromTableModel = null;
    for (int r = 0; r < model.getRowCount (); ++r)
    {
      for (int c = 0; c < model.getColumnCount (); ++c)
      {
        addStringData (model.getValueAt (r, c).toString ());
      }
    }
    mDataFromTableModel = model;
  }

  public void tableChanged (TableModelEvent e)
  {
    if (mDataFromTableModel == null)
      return;
    // TODO: make this more performant by evaluating the TableModelEvent
    clear ();
    buildFromTableModel ();
  }

  private void fireResetAllFilters ()
  {
    for (ISortableFilterableTableListener l : new ArrayList<ISortableFilterableTableListener> (mListeners))
    {
      l.resetAllFilters ();
    }
  }
}
