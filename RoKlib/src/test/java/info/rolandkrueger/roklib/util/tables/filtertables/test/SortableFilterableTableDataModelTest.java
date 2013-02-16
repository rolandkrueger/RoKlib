/*
 * $Id: SortableFilterableTableDataModelTest.java 181 2010-11-01 09:39:13Z roland $
 * Copyright (C) 2007-2010 Roland Krueger
 * Created on 04.02.2010
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
package info.rolandkrueger.roklib.util.tables.filtertables.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import info.rolandkrueger.roklib.util.tables.filtertable.DefaultTableDataColumnHeader;
import info.rolandkrueger.roklib.util.tables.filtertable.SortableFilterableTableDataModel;
import info.rolandkrueger.roklib.util.tables.filtertable.SortableFilterableTableDataModel.SearchMode;

import java.util.Comparator;

import javax.swing.table.DefaultTableModel;

import org.junit.Before;
import org.junit.Test;

public class SortableFilterableTableDataModelTest
{
  private SortableFilterableTableDataModel<String, DefaultTableDataColumnHeader> mTestModel;

  private static class TestComparator implements Comparator<String>
  {
    public int compare (String o1, String o2)
    {
      return 0;
    }
  }

  @Before
  public void setUp ()
  {
    mTestModel = new SortableFilterableTableDataModel<String, DefaultTableDataColumnHeader> (3,
        SearchMode.NONE);
  }

  @Test (expected = IllegalStateException.class)
  public void testAddDataWithTableModelFail ()
  {
    mTestModel = new SortableFilterableTableDataModel<String, DefaultTableDataColumnHeader> (
        new DefaultTableModel (), SearchMode.BOTH);
    mTestModel.addData (3.141);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testConstructor_Fail ()
  {
    mTestModel = new SortableFilterableTableDataModel<String, DefaultTableDataColumnHeader> (- 23,
        SearchMode.NONE);
  }

  @Test (expected = ArrayIndexOutOfBoundsException.class)
  public void testSetColumnHeader_Fail ()
  {
    mTestModel.setColumnHeader (new DefaultTableDataColumnHeader (""), 17);
  }

  @Test
  public void testSetGetColumnHeader ()
  {
    DefaultTableDataColumnHeader header = new DefaultTableDataColumnHeader ("headline");
    mTestModel.setColumnHeader (header, 0);
    assertEquals ("headline", mTestModel.getColumnHeader (0).getHeadline ());
    assertNull (mTestModel.getColumnHeader (1));
  }

  @Test (expected = ArrayIndexOutOfBoundsException.class)
  public void testGetColumnHeader_Fail ()
  {
    mTestModel.getColumnHeader (17);
  }

  @Test
  public void testSetComparator ()
  {
    Comparator<String> comparator1 = new TestComparator ();
    Comparator<String> comparator2 = new TestComparator ();
    mTestModel.setColumnSortingComparator (comparator1, 1);
    assertEquals (comparator1, mTestModel.setColumnSortingComparator (comparator2, 1));
    assertEquals (comparator2, mTestModel.setColumnSortingComparator (null, 1));
    assertEquals (null, mTestModel.setColumnSortingComparator (comparator1, 1));
  }

  @Test (expected = ArrayIndexOutOfBoundsException.class)
  public void testSetComparator_WrongIndex ()
  {
    Comparator<String> comparator = new TestComparator ();
    mTestModel.setColumnSortingComparator (comparator, 17);
  }
}
