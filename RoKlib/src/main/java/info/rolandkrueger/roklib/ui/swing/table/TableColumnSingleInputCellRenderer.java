/*
 * $Id: TableColumnSingleInputCellRenderer.java 178 2010-10-31 18:01:20Z roland $
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

import info.rolandkrueger.roklib.util.tables.filtertable.ISortableFilterableTableListener;
import info.rolandkrueger.roklib.util.tables.filtertable.ITableDataColumnHeader;
import info.rolandkrueger.roklib.util.tables.filtertable.SortableFilterableTableDataModel;
import info.rolandkrueger.roklib.util.tables.filtertable.SortableFilterableTableDataModel.SearchMode;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

public class TableColumnSingleInputCellRenderer<T, H extends ITableDataColumnHeader> extends
    AbstractTableColumnInputCellRenderer<T, H> implements ISortableFilterableTableListener
{
  private List<TableColumnSingleInputComponent> mComponentList;

  public TableColumnSingleInputCellRenderer (SearchMode mode,
      SortableFilterableTableDataModel<T, H> dataModel)
  {
    super (mode, dataModel);
    dataModel.addSortableFilterableTableListener (this);
    mComponentList = new ArrayList<TableColumnSingleInputComponent> (dataModel.getColumnCount ());
    for (int i = 0; i < dataModel.getColumnCount (); ++i)
    {
      mComponentList.add (null);
    }
  }

  public Component getTableCellRendererComponent (JTable table, Object value, boolean isSelected,
      boolean hasFocus, int row, int column)
  {
    TableColumnSingleInputComponent component = mComponentList.get (column);
    if (component == null)
    {
      component = new TableColumnSingleInputComponent (column);
      mComponentList.set (column, component);
    }
    return component;
  }

  private class TableColumnSingleInputComponent extends JPanel implements MouseListener
  {
    private static final long serialVersionUID = 6266420577278257939L;
    private int mColumnIndex;
    private JTextField mTextField;
    private JLabel mHeaderLabel;

    public TableColumnSingleInputComponent (int columnIndex)
    {
      mColumnIndex = columnIndex;
      initialize ();
    }

    private void initialize ()
    {
      GridBagConstraints gridBagConstraints1 = new GridBagConstraints ();
      gridBagConstraints1.fill = GridBagConstraints.BOTH;
      gridBagConstraints1.gridy = 1;
      gridBagConstraints1.weightx = 1.0;
      gridBagConstraints1.anchor = GridBagConstraints.CENTER;
      gridBagConstraints1.insets = new Insets (2, 5, 2, 5);
      gridBagConstraints1.gridx = 0;
      GridBagConstraints gridBagConstraints = new GridBagConstraints ();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 0;
      mHeaderLabel = new JLabel ();
      this.setSize (300, 80);
      this.setLayout (new GridBagLayout ());
      this.add (mHeaderLabel, gridBagConstraints);
      this.add (getTextField (), gridBagConstraints1);
      this.setBorder (BorderFactory.createEtchedBorder (EtchedBorder.LOWERED));
    }

    public void reset ()
    {
      mTextField.setText ("");
    }

    private JTextField getTextField ()
    {
      if (mTextField == null)
      {
        mTextField = new JTextField ();
        mTextField.addCaretListener (new CaretListener ()
        {
          public void caretUpdate (CaretEvent e)
          {
            TableColumnSingleInputCellRenderer.this.getDataModel ().setPrefixFilter (
                mTextField.getText (), mColumnIndex, true);
          }
        });
      }
      return mTextField;
    }

    public void mouseClicked (MouseEvent e)
    {
      System.out.println ("Click!");
    }

    public void mouseEntered (MouseEvent e)
    {
      System.out.println ("Entered!");
    }

    public void mouseExited (MouseEvent e)
    {
      System.out.println ("Exited!");
    }

    public void mousePressed (MouseEvent e)
    {
      // TODO Auto-generated method stub

    }

    public void mouseReleased (MouseEvent e)
    {
      // TODO Auto-generated method stub

    }
  }

  public void resetAllFilters ()
  {
    for (TableColumnSingleInputComponent c : mComponentList)
    {
      c.reset ();
    }
  }
}
