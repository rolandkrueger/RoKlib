/*
 * Copyright (C) 2007 Roland Krueger
 * Created on 20.11.2010
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
package info.rolandkrueger.roklib.util.tstmap.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Set;
import java.util.SortedMap;

import org.junit.Before;
import org.junit.Test;
import org.roklib.util.TernarySearchTreeMapCaseInsensitive;

public class TSTMapCaseInsensitiveSubMapTest
{
  private static final String                         EMPTY     = "";
  private static final String                         PFORZHEIM = "PForzhEIm";
  private static final String                         MANNHEIM  = "mannHEIM";
  private static final String                         WEINHEIM  = "wEInhEIm";
  private static final String                         DRESDEN   = "DREsden";
  private static final String                         MAGDEBURG = "maGDEburG";
  private static final String                         LEIPZIG   = "LeipZig";
  private static final String                         FREIBURG  = "freiBuRg";

  private TernarySearchTreeMapCaseInsensitive<String> mTestObj;
  private SortedMap<CharSequence, String>             mSubMap;

  @Before
  public void setUp ()
  {
    mTestObj = new TernarySearchTreeMapCaseInsensitive<String> ();
    mTestObj.put (EMPTY, EMPTY);
    mTestObj.put (DRESDEN, DRESDEN);
    mTestObj.put (FREIBURG, FREIBURG);
    mTestObj.put (LEIPZIG, LEIPZIG);
    mTestObj.put (MAGDEBURG, MAGDEBURG);
    mTestObj.put (MANNHEIM, MANNHEIM);
    mTestObj.put (PFORZHEIM, PFORZHEIM);
    mTestObj.put (WEINHEIM, WEINHEIM);
    mSubMap = mTestObj.subMap ("ERFURT", "marburg");
  }

  @Test
  public void testSubMap ()
  {
    assertEquals (4, mSubMap.size ());
    assertTrue (mSubMap.containsKey (FREIBURG));
    assertTrue (mSubMap.containsKey (LEIPZIG));
    assertTrue (mSubMap.containsKey (MAGDEBURG));
    assertTrue (mSubMap.containsKey (MANNHEIM));
  }

  @Test
  public void testKeySet ()
  {
    Set<CharSequence> keySet = mSubMap.keySet ();
    assertEquals (4, keySet.size ());
    assertTrue (keySet.contains (MANNHEIM));
    assertTrue (keySet.contains ("mannheim"));
    assertTrue (keySet.contains ("MANNHEIM"));
  }

  @Test
  public void testFirstKey ()
  {
    assertEquals (FREIBURG, mSubMap.firstKey ());
  }

  @Test
  public void testLastKey ()
  {
    assertEquals (MANNHEIM, mSubMap.lastKey ());
  }

  @Test
  public void testPut ()
  {
    mSubMap.put ("GerA", "GERA");
    assertTrue (mTestObj.containsKey ("GERA"));
  }

}
