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


import javax.swing.table.TableCellRenderer;

import org.roklib.util.helper.CheckForNull;
import org.roklib.util.tables.filtertable.ITableDataColumnHeader;
import org.roklib.util.tables.filtertable.SortableFilterableTableDataModel;
import org.roklib.util.tables.filtertable.SortableFilterableTableDataModel.SearchMode;

public abstract class AbstractTableColumnInputCellRenderer<T, H extends ITableDataColumnHeader> implements
    TableCellRenderer
{
  private SearchMode                             mSearchMode;
  private SortableFilterableTableDataModel<T, H> mDataModel;

  public AbstractTableColumnInputCellRenderer (SearchMode mode, SortableFilterableTableDataModel<T, H> dataModel)
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