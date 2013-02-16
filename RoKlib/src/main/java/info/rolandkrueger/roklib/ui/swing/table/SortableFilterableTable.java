/*
 * $Id: SortableFilterableTable.java 178 2010-10-31 18:01:20Z roland $
 * Copyright (C) 2007-2010 Roland Krueger
 * Created on 08.02.2010
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
package info.rolandkrueger.roklib.ui.swing.table;

import info.rolandkrueger.roklib.util.tables.filtertable.ITableDataColumnHeader;
import info.rolandkrueger.roklib.util.tables.filtertable.SortableFilterableTableDataModel;
import info.rolandkrueger.roklib.util.tables.filtertable.SortableFilterableTableDataModel.SearchMode;

import java.util.Vector;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

public class SortableFilterableTable<T, H extends ITableDataColumnHeader> extends JTable
{
  private static final long serialVersionUID = - 4117999556318274032L;

  private SortableFilterableTableDataModel<T, H> mTableModel;
  private SearchMode mSearchCapability;

  public SortableFilterableTable (SearchMode searchCapability)
  {
    super ();
    mSearchCapability = searchCapability;
  }

  public SortableFilterableTable (SortableFilterableTableDataModel<T, H> dm,
      SearchMode searchCapability)
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

  public SortableFilterableTable (Object[][] rowData, Object[] columnNames,
      SearchMode searchCapability)
  {
    this (searchCapability);
    mTableModel = new SortableFilterableTableDataModel<T, H> (columnNames.length, searchCapability);
    // TODO:...
  }

  public SortableFilterableTable (TableModel dm, TableColumnModel cm, ListSelectionModel sm,
      SearchMode searchCapability)
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
