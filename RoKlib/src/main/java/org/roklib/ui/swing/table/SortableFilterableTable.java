/*
 * Copyright (C) 2007-2010 Roland Krueger
 * Created on 08.02.2010
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
package org.roklib.ui.swing.table;


import java.util.Vector;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import org.roklib.ui.swing.table.filtertable.ITableDataColumnHeader;
import org.roklib.ui.swing.table.filtertable.SortableFilterableTableDataModel;
import org.roklib.ui.swing.table.filtertable.SortableFilterableTableDataModel.SearchMode;

public class SortableFilterableTable<T, H extends ITableDataColumnHeader> extends JTable
{
  private static final long                      serialVersionUID = -4117999556318274032L;

  private SortableFilterableTableDataModel<T, H> mTableModel;
  private SearchMode                             mSearchCapability;

  public SortableFilterableTable (SearchMode searchCapability)
  {
    super ();
    mSearchCapability = searchCapability;
  }

  public SortableFilterableTable (SortableFilterableTableDataModel<T, H> dm, SearchMode searchCapability)
  {
    this (searchCapability);
    setModel (dm);
  }

  public SortableFilterableTable (int numRows, int numColumns, SearchMode searchCapability)
  {
    this (searchCapability);
    mTableModel = new SortableFilterableTableDataModel<T, H> (numColumns, searchCapability);
    for (int i = 0; i < numColumns * numRows; ++i)
      mTableModel.addStringData ("");
  }

  public SortableFilterableTable (Object[][] rowData, Object[] columnNames, SearchMode searchCapability)
  {
    this (searchCapability);
    mTableModel = new SortableFilterableTableDataModel<T, H> (columnNames.length, searchCapability);
    // TODO:...
  }

  public SortableFilterableTable (TableModel dm, TableColumnModel cm, ListSelectionModel sm, SearchMode searchCapability)
  {
    this (dm, searchCapability);
    setColumnModel (cm);
    setSelectionModel (sm);
  }

  public SortableFilterableTable (TableModel dm, TableColumnModel cm, SearchMode searchCapability)
  {
    this (dm, searchCapability);
    setColumnModel (cm);
  }

  public SortableFilterableTable (TableModel dm, SearchMode searchCapability)
  {
    this (searchCapability);
    setModel (dm);
  }

  @SuppressWarnings ("unchecked")
  public SortableFilterableTable (Vector rowData, Vector columnNames, SearchMode searchCapability)
  {
    // TODO:...
  }

  @Override
  public void setModel (TableModel dataModel)
  {
    super.setModel (dataModel);
    mTableModel = new SortableFilterableTableDataModel<T, H> (dataModel, mSearchCapability);
    TableColumnSingleInputCellRenderer<T, H> renderer = new TableColumnSingleInputCellRenderer<T, H> (
        mSearchCapability, mTableModel);
    for (int i = 0; i < mTableModel.getColumnCount (); ++i)
    {

      getColumnModel ().getColumn (i).setHeaderRenderer (renderer);
    }

    repaint ();
  }
}
