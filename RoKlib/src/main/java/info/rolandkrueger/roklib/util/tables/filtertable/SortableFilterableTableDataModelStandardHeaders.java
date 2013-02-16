/*
 * $Id: SortableFilterableTableDataModelStandardHeaders.java 178 2010-10-31 18:01:20Z roland $
 * Copyright (C) 2007-2010 Roland Krueger
 * Created on 10.02.2010
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

import java.util.Collection;

import javax.swing.table.TableModel;

public class SortableFilterableTableDataModelStandardHeaders<T> extends
    SortableFilterableTableDataModel<T, DefaultTableDataColumnHeader>
{
  private static final long serialVersionUID = - 5996526646666011364L;

  public SortableFilterableTableDataModelStandardHeaders (int columnCount,
      SearchMode searchCapability)
  {
    super (columnCount, searchCapability);
  }

  public SortableFilterableTableDataModelStandardHeaders (TableModel tableModel,
      SearchMode searchCapability)
  {
    super (tableModel, searchCapability);
  }

  public void setHeaderNames (String... names)
  {
    if (names.length > getColumnCount ())
      throw new IllegalArgumentException (String.format (
          "Too many column names: %d names > %d columns", names.length, getColumnCount ()));

    int i = 0;
    for (String name : names)
    {
      setColumnHeader (new DefaultTableDataColumnHeader (name), i);
      ++i;
    }
  }

  public void setHeaderNames (Collection<String> names)
  {
    setHeaderNames (names.toArray (new String[] {}));
  }
}
