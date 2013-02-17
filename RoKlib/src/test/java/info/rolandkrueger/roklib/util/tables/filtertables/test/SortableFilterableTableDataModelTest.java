/*
 * Copyright (C) 2007-2010 Roland Krueger
 * Created on 04.02.2010
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
package info.rolandkrueger.roklib.util.tables.filtertables.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Comparator;

import javax.swing.table.DefaultTableModel;

import org.junit.Before;
import org.junit.Test;
import org.roklib.ui.swing.table.filtertable.DefaultTableDataColumnHeader;
import org.roklib.ui.swing.table.filtertable.SortableFilterableTableDataModel;
import org.roklib.ui.swing.table.filtertable.SortableFilterableTableDataModel.SearchMode;

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
    mTestModel = new SortableFilterableTableDataModel<String, DefaultTableDataColumnHeader> (3, SearchMode.NONE);
  }

  @Test (expected = IllegalStateException.class)
  public void testAddDataWithTableModelFail ()
  {
    mTestModel = new SortableFilterableTableDataModel<String, DefaultTableDataColumnHeader> (new DefaultTableModel (),
        SearchMode.BOTH);
    mTestModel.addData (3.141);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testConstructor_Fail ()
  {
    mTestModel = new SortableFilterableTableDataModel<String, DefaultTableDataColumnHeader> (-23, SearchMode.NONE);
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
