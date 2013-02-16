/*
 * $Id: SortableFilterableTableDataModelFilterAndSortTest.java 181 2010-11-01 09:39:13Z roland $
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
import info.rolandkrueger.roklib.util.tables.filtertable.DefaultTableDataColumnHeader;
import info.rolandkrueger.roklib.util.tables.filtertable.SortableFilterableTableDataModel;
import info.rolandkrueger.roklib.util.tables.filtertable.SortableFilterableTableDataModel.SearchMode;

import java.util.Comparator;

import javax.swing.SortOrder;

import org.junit.Before;
import org.junit.Test;

public class SortableFilterableTableDataModelFilterAndSortTest
{
  private SortableFilterableTableDataModel<String, DefaultTableDataColumnHeader> mTestObj;

  @Before
  public void setUp ()
  {
    mTestObj = new SortableFilterableTableDataModel<String, DefaultTableDataColumnHeader> (4,
        SearchMode.BOTH);
    mTestObj.addStringData ("Belgium").addStringData ("17").addData (17).addData (3.14, "Brussels");
    mTestObj.addStringData ("Belarus").addStringData ("9").addData (9).addData (2.12, "Minsk");
    mTestObj.addStringData ("Australia").addStringData ("23").addData (23)
        .addData (5.17, "Canberra");
    mTestObj.addStringData ("Benin").addStringData ("13").addData (13).addData (1.11, "Porto-Novo");
    mTestObj.addStringData ("Austria").addStringData ("99").addData (99).addData (211.10, "Wien");
    mTestObj.setColumnSortingComparator (new Comparator<String> ()
    {
      public int compare (String o1, String o2)
      {
        // use descending ordering here
        return o2.compareTo (o1);
      }
    }, 3);
  }

  @Test
  public void testTestObject ()
  {
    assertEquals (5, mTestObj.getRowCount ());
    assertEquals (4, mTestObj.getColumnCount ());
    assertEquals ("Porto-Novo", mTestObj.getCellDataAt (3, 3).getAdditionalData ());
  }

  @Test
  public void testPrefixAndInfixFilter ()
  {
    mTestObj.setPrefixFilter ("Be", 0, false);
    mTestObj.setInfixFilter ("1", 2, false);
    mTestObj.applyFilters ();
    checkColumn (0, "Belgium", "Benin");
  }

  @Test
  public void testPrefixFilterAndSort ()
  {
    mTestObj.setPrefixFilter ("Be", 0, true);
    mTestObj.sortColumn (SortOrder.ASCENDING, 0);
    checkColumn (0, "Belarus", "Belgium", "Benin");
  }

  @Test
  public void testSortAndThenFilterWithPrefix ()
  {
    mTestObj.sortColumn (SortOrder.DESCENDING, 0);
    mTestObj.setPrefixFilter ("Be", 0, true);
    checkColumn (0, "Benin", "Belgium", "Belarus");
  }

  @Test
  public void testSortFourthColumn ()
  {
    mTestObj.sortColumn (SortOrder.ASCENDING, 3);
    // the comparator configured for this column defines an inverse ordering
    checkColumnWithAdditionalData (3, "Wien", "Porto-Novo", "Minsk", "Canberra", "Brussels");
    mTestObj.sortColumn (SortOrder.DESCENDING, 3);
    checkColumnWithAdditionalData (3, "Brussels", "Canberra", "Minsk", "Porto-Novo", "Wien");
    mTestObj.sortColumn (SortOrder.UNSORTED, 3);
    checkColumnWithAdditionalData (3, "Brussels", "Minsk", "Canberra", "Porto-Novo", "Wien");
  }

  @Test
  public void testSortFirstColumn ()
  {
    mTestObj.sortColumn (SortOrder.ASCENDING, 0);
    checkColumn (0, "Australia", "Austria", "Belarus", "Belgium", "Benin");
    mTestObj.sortColumn (SortOrder.DESCENDING, 0);
    checkColumn (0, "Benin", "Belgium", "Belarus", "Austria", "Australia");
    mTestObj.sortColumn (SortOrder.UNSORTED, 0);
    checkColumn (0, "Belgium", "Belarus", "Australia", "Benin", "Austria");
  }

  @Test
  public void testSortSecondColumn ()
  {
    // even though the numbers for the second column were added as strings, the
    // column is sorted numerically
    mTestObj.sortColumn (SortOrder.ASCENDING, 1);
    checkColumn (1, "9", "13", "17", "23", "99");
    mTestObj.sortColumn (SortOrder.DESCENDING, 1);
    checkColumn (1, "99", "23", "17", "13", "9");
    mTestObj.sortColumn (SortOrder.UNSORTED, 1);
    checkColumn (1, "17", "9", "23", "13", "99");
  }

  @Test
  public void testInfixFilterFirstColumn ()
  {
    mTestObj.setInfixFilter ("ia", 0, true);
    checkColumn (0, "Australia", "Austria");
  }

  @Test
  public void testInfixFilterThirdColumn ()
  {
    mTestObj.setInfixFilter ("3", 2, true);
    checkColumn (0, "Australia", "Benin");
  }

  @Test
  public void testInfixFilterFourthColumn ()
  {
    mTestObj.setInfixFilter ("11", 3, true);
    checkColumn (0, "Benin", "Austria");
  }

  @Test
  public void testMatchSimilarFilterFirstColumn ()
  {
    mTestObj.setMatchSimilarFilter ("Austrai", 3, 2, 0, true);
    checkColumn (0, "Australia", "Austria");
    mTestObj.resetFilters ();
    mTestObj.setMatchSimilarFilter ("xxxxx", 5, 0, 0, true);
    checkColumn (0, "Benin");
  }

  @Test
  public void testPrefixFilterFirstColumn ()
  {
    mTestObj.setPrefixFilter ("Aus", 0, true);
    assertEquals (2, mTestObj.getRowCount ());
    checkColumn (0, "Australia", "Austria");
    mTestObj.resetFilters ();
    mTestObj.setPrefixFilter ("Bel", 0, true);
    checkColumn (0, "Belgium", "Belarus");
  }

  @Test
  public void testPrefixFilterSecondColumn ()
  {
    mTestObj.setPrefixFilter ("1", 1, true);
    checkColumn (1, "17", "13");
    checkColumn (0, "Belgium", "Benin");
  }

  @Test
  public void testPrefixFilterThirdColumn ()
  {
    mTestObj.setPrefixFilter ("1", 2, true);
    checkColumn (1, "17", "13");
    checkColumn (0, "Belgium", "Benin");
  }

  @Test
  public void testResetFilters ()
  {
    mTestObj.setPrefixFilter ("Be", 0, true);
    assertEquals (3, mTestObj.getRowCount ());
    mTestObj.resetFilters ();
    assertEquals (5, mTestObj.getRowCount ());
    checkColumn (0, "Belgium", "Belarus", "Australia", "Benin", "Austria");
  }

  private void checkColumn (int columnIndex, Object... columnData)
  {
    assertEquals (columnData.length, mTestObj.getRowCount ());
    for (int i = 0; i < mTestObj.getRowCount (); ++i)
    {
      assertEquals (columnData[i], mTestObj.getValueAt (i, columnIndex));
    }
  }

  private void checkColumnWithAdditionalData (int columnIndex, Object... columnData)
  {
    assertEquals (columnData.length, mTestObj.getRowCount ());
    for (int i = 0; i < mTestObj.getRowCount (); ++i)
    {
      assertEquals (columnData[i], mTestObj.getCellDataAt (i, columnIndex).getAdditionalData ());
    }
  }
}
