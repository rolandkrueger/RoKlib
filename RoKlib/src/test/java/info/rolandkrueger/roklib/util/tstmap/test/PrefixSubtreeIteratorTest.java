/*
 * $Id: PrefixSubtreeIteratorTest.java 253 2011-01-25 16:16:31Z roland $
 * Copyright (C) 2007 Roland Krueger
 * Created on 23.09.2007
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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307
 * USA
 */
package info.rolandkrueger.roklib.util.tstmap.test;

import static org.junit.Assert.assertEquals;
import info.rolandkrueger.roklib.util.TernarySearchTreeMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map.Entry;

import org.junit.Before;
import org.junit.Test;

public class PrefixSubtreeIteratorTest
{
  private final static String ABIBOR = "Abigor";
  private final static String ABORTED = "Aborted";
  private final static String AMON_AMARTH = "Amon Amarth";
  private final static String AMORPHIS = "Amorphis";
  private final static String BLACK_DESTINY = "Black Destiny";
  private final static String BLACKFIELD = "Blackfield";
  private final static String OPERA_IX = "Opera IX";
  private final static String OPETH = "Opeth";
  private final static String PORCUPINE_TREE = "Porcupine Tree";
  private final static String PORTISHEAD = "Portishead";

  private TernarySearchTreeMap<String> map;

  @Before
  public void setUp ()
  {
    map = new TernarySearchTreeMap<String> ();
    map.put (ABIBOR, ABIBOR);
    map.put (ABORTED, ABORTED);
    map.put (AMON_AMARTH, AMON_AMARTH);
    map.put (AMORPHIS, AMORPHIS);
    map.put (BLACK_DESTINY, BLACK_DESTINY);
    map.put (BLACKFIELD, BLACKFIELD);
    map.put (OPERA_IX, OPERA_IX);
    map.put (OPETH, OPETH);
    map.put (PORCUPINE_TREE, PORCUPINE_TREE);
    map.put (PORTISHEAD, PORTISHEAD);
  }

  @Test
  public void testInversePrefixSearchWithEmptyString ()
  {
    String[] result = {};
    compareResults (map.getPrefixSubtreeIterator ("", true), result);
  }

  @Test
  public void testInversePrefixSearchWithExactPrefix ()
  {
    String[] result = { ABIBOR, ABORTED, AMON_AMARTH, AMORPHIS, BLACK_DESTINY, BLACKFIELD,
        OPERA_IX, PORCUPINE_TREE, PORTISHEAD };
    compareResults (map.getPrefixSubtreeIterator (OPETH, true), result);
  }

  @Test
  public void testInversePrefixSearchWithShortPrefix ()
  {
    map.put ("Op", "");
    String[] result = { ABIBOR, ABORTED, AMON_AMARTH, AMORPHIS, BLACK_DESTINY, BLACKFIELD, "Op",
        PORCUPINE_TREE, PORTISHEAD };
    compareResults (map.getPrefixSubtreeIterator ("Ope", true), result);
  }

  @Test
  public void testInversePrefixSearchWithEmptyStringInMap ()
  {
    map.put ("", "");
    String[] result = { "", ABIBOR, ABORTED, AMORPHIS, BLACK_DESTINY, BLACKFIELD, OPERA_IX, OPETH,
        PORCUPINE_TREE, PORTISHEAD };
    compareResults (map.getPrefixSubtreeIterator ("Amon", true), result);
  }

  @Test
  public void testInversePrefixSearchWithKeyNotInMapWithEmptyStringInMap ()
  {
    map.put ("", "");
    String[] result = { "", ABIBOR, ABORTED, AMON_AMARTH, AMORPHIS, BLACK_DESTINY, BLACKFIELD,
        OPERA_IX, OPETH, PORCUPINE_TREE, PORTISHEAD };
    compareResults (map.getPrefixSubtreeIterator ("xxx", true), result);
  }

  @Test
  public void testInversePrefixSearch ()
  {
    String[] result = { ABIBOR, ABORTED, BLACK_DESTINY, BLACKFIELD, OPERA_IX, OPETH,
        PORCUPINE_TREE, PORTISHEAD };
    compareResults (map.getPrefixSubtreeIterator ("Amo", true), result);
  }

  @Test
  public void testInversePrefixSearchWithKeyNotInMap ()
  {
    String[] result = { ABIBOR, ABORTED, AMON_AMARTH, AMORPHIS, BLACK_DESTINY, BLACKFIELD,
        OPERA_IX, OPETH, PORCUPINE_TREE, PORTISHEAD };
    compareResults (map.getPrefixSubtreeIterator ("xxx", true), result);
  }

  @Test
  public void testOneResultInPrefixSubtree ()
  {
    String[] result = { OPERA_IX };
    compareResults (map.getPrefixSubtreeIterator ("Opera"), result);
  }

  @Test
  public void testExactMatch ()
  {
    String[] result = { PORCUPINE_TREE };
    compareResults (map.getPrefixSubtreeIterator (PORCUPINE_TREE), result);
  }

  @Test
  public void testNoMatch ()
  {
    String[] result = {};
    compareResults (map.getPrefixSubtreeIterator ("prefix_not_contained"), result);
  }

  @Test
  public void testMatchAtMapsBeginning ()
  {
    String[] result = { ABIBOR, ABORTED };
    compareResults (map.getPrefixSubtreeIterator ("Ab"), result);
  }

  @Test
  public void testMatchAtMapsEnd ()
  {
    String[] result = { PORCUPINE_TREE, PORTISHEAD };
    compareResults (map.getPrefixSubtreeIterator ("Por"), result);
  }

  @Test
  public void testMatchEmptyPrefix ()
  {
    String[] result = { ABIBOR, ABORTED, AMON_AMARTH, AMORPHIS, BLACK_DESTINY, BLACKFIELD,
        OPERA_IX, OPETH, PORCUPINE_TREE, PORTISHEAD };
    compareResults (map.getPrefixSubtreeIterator (""), result);
  }

  private void compareResults (Iterable<Entry<CharSequence, String>> iterable, String[] values)
  {
    ArrayList<CharSequence> valueList = new ArrayList<CharSequence> (Arrays.asList (values));
    ArrayList<CharSequence> iteratorValues = new ArrayList<CharSequence> ();
    while (iterable.iterator ().hasNext ())
    {
      Entry<CharSequence, String> entry = iterable.iterator ().next ();
      iteratorValues.add (entry.getKey ());
    }

    assertEquals (valueList, iteratorValues);
  }
}
