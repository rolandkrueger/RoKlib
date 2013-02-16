/*
 * $Id: AbstractTableColumnInputCellRenderer.java 178 2010-10-31 18:01:20Z roland $
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

import info.rolandkrueger.roklib.util.helper.CheckForNull;
import info.rolandkrueger.roklib.util.tables.filtertable.ITableDataColumnHeader;
import info.rolandkrueger.roklib.util.tables.filtertable.SortableFilterableTableDataModel;
import info.rolandkrueger.roklib.util.tables.filtertable.SortableFilterableTableDataModel.SearchMode;

import javax.swing.table.TableCellRenderer;

public abstract class AbstractTableColumnInputCellRenderer<T, H extends ITableDataColumnHeader>
    implements TableCellRenderer
{
  private SearchMode mSearchMode;
  private SortableFilterableTableDataModel<T, H> mDataModel;

  public AbstractTableColumnInputCellRenderer (SearchMode mode,
      SortableFilterableTableDataModel<T, H> dataModel)
  {
    CheckForNull.check (dataModel);
    mSearchMode = mode;
    mDataModel = dataModel;
  }

  public SearchMode getSearchMode ()
  {
    return mSearchMode;
  }

  public SortableFilterableTableDataModel<T, H> getDataModel ()
  {
    return mDataModel;
  }
}