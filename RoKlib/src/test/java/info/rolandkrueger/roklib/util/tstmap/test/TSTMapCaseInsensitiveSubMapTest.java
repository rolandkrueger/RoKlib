/*
 * $Id: TSTMapCaseInsensitiveSubMapTest.java 211 2010-11-22 19:21:26Z roland $
 * Copyright (C) 2007 Roland Krueger
 * Created on 20.11.2010
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

import static org.junit.Assert.*;
import info.rolandkrueger.roklib.util.TernarySearchTreeMapCaseInsensitive;

import java.util.Set;
import java.util.SortedMap;

import org.junit.Before;
import org.junit.Test;

public class TSTMapCaseInsensitiveSubMapTest
{
  private static final String EMPTY = "";
  private static final String PFORZHEIM = "PForzhEIm";
  private static final String MANNHEIM  = "mannHEIM";
  private static final String WEINHEIM  = "wEInhEIm";
  private static final String DRESDEN  = "DREsden";
  private static final String MAGDEBURG  = "maGDEburG";
  private static final String LEIPZIG  = "LeipZig";
  private static final String FREIBURG  = "freiBuRg";
    
  private TernarySearchTreeMapCaseInsensitive<String> mTestObj;
  private SortedMap<CharSequence, String> mSubMap;
  
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
