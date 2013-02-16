/*
 * $Id: TernarySearchTreeMapTests.java 254 2011-01-25 18:48:50Z roland $
 * Copyright (C) 2007 Roland Krueger
 * Created on 02.11.2010
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
package info.rolandkrueger.roklib.util.tstmap;

import info.rolandkrueger.roklib.util.tstmap.test.CompareTSTMapToTreeMapTest;
import info.rolandkrueger.roklib.util.tstmap.test.PrefixSubtreeIteratorTest;
import info.rolandkrueger.roklib.util.tstmap.test.TSTKeySetTest;
import info.rolandkrueger.roklib.util.tstmap.test.TSTMapCaseInsensitiveMapEntrySetTest;
import info.rolandkrueger.roklib.util.tstmap.test.TSTMapCaseInsensitiveSubMapTest;
import info.rolandkrueger.roklib.util.tstmap.test.TSTMapCaseInsensitiveTest;
import info.rolandkrueger.roklib.util.tstmap.test.TSTMapEntrySetTest;
import info.rolandkrueger.roklib.util.tstmap.test.TSTMapSortedMapInterfaceTest;
import info.rolandkrueger.roklib.util.tstmap.test.TSTMapValuesTest;
import info.rolandkrueger.roklib.util.tstmap.test.TSTMapWithEmptyStringKeyTest;
import info.rolandkrueger.roklib.util.tstmap.test.TSTSetCaseInsensitiveTest;
import info.rolandkrueger.roklib.util.tstmap.test.TSTSetSubSetTest;
import info.rolandkrueger.roklib.util.tstmap.test.TSTSetTest;
import info.rolandkrueger.roklib.util.tstmap.test.TSTSubMapTest;
import info.rolandkrueger.roklib.util.tstmap.test.TernarySearchTreeMapTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith (Suite.class)
@SuiteClasses ({
  CompareTSTMapToTreeMapTest.class,
  PrefixSubtreeIteratorTest.class,
  TernarySearchTreeMapTest.class,
  TSTKeySetTest.class,
  TSTMapEntrySetTest.class,
  TSTMapSortedMapInterfaceTest.class,
  TSTMapValuesTest.class,
  TSTMapWithEmptyStringKeyTest.class,
  TSTSubMapTest.class,
  TSTMapCaseInsensitiveTest.class,
  TSTMapCaseInsensitiveMapEntrySetTest.class,
  TSTMapCaseInsensitiveSubMapTest.class,
  TSTSetTest.class,
  TSTSetSubSetTest.class,
  TSTSetCaseInsensitiveTest.class
})
public class TernarySearchTreeMapTests
{
}
