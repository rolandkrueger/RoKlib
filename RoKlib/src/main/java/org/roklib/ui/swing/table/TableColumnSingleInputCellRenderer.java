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

import org.roklib.ui.swing.table.filtertable.ISortableFilterableTableListener;
import org.roklib.ui.swing.table.filtertable.ITableDataColumnHeader;
import org.roklib.ui.swing.table.filtertable.SortableFilterableTableDataModel;
import org.roklib.ui.swing.table.filtertable.SortableFilterableTableDataModel.SearchMode;

public class TableColumnSingleInputCellRenderer<T, H extends ITableDataColumnHeader> extends
    AbstractTableColumnInputCellRenderer<T, H> implements ISortableFilterableTableListener
{
  private List<TableColumnSingleInputComponent> mComponentList;

  public TableColumnSingleInputCellRenderer (SearchMode mode, SortableFilterableTableDataModel<T, H> dataModel)
  {
    super (mode, dataModel);
    dataModel.addSortableFilterableTableListener (this);
    mComponentList = new ArrayList<TableColumnSingleInputComponent> (dataModel.getColumnCount ());
    for (int i = 0; i < dataModel.getColumnCount (); ++i)
    {
      mComponentList.add (null);
    }
  }

  public Component getTableCellRendererComponent (JTable table, Object value, boolean isSelected, boolean hasFocus,
      int row, int column)
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
    private int               mColumnIndex;
    private JTextField        mTextField;
    private JLabel            mHeaderLabel;

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
            TableColumnSingleInputCellRenderer.this.getDataModel ().setPrefixFilter (mTextField.getText (),
                mColumnIndex, true);
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
