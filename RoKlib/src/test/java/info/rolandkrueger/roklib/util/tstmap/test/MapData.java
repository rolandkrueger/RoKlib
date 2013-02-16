/*
 * $Id: MapData.java 193 2010-11-02 14:34:08Z roland $
 * Copyright (C) 2007 Roland Krueger
 * Created on 10.07.2007
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapData
{
  public static TestDataFixtureMapMap getFiveLowerCaseEntriesOrderingExpected ()
  {
    return new FiveLowerCaseEntriesOrderingExpected ();
  }

  public static class TestDataFixture
  {
    protected Map<CharSequence, String> mData = new HashMap<CharSequence, String> ();

    public Map<CharSequence, String> getData ()
    {
      return mData;
    }
  }

  public static class TestDataFixtureMapMap extends TestDataFixture
  {
    protected Map<CharSequence, String> mExpected = new HashMap<CharSequence, String> ();

    public Map<CharSequence, String> getExpected ()
    {
      return mExpected;
    }
  }

  public static class TestDataFixtureMapList extends TestDataFixture
  {
    protected List<CharSequence> mExpected = new ArrayList<CharSequence> ();

    public List<CharSequence> getExpected ()
    {
      return mExpected;
    }
  }

  public static class FiveLowerCaseEntriesOrderingExpected extends TestDataFixtureMapMap
  {
    public FiveLowerCaseEntriesOrderingExpected ()
    {
      mData.put ("sverige", "smorgasbord");
      mData.put ("deutschland", "sauerkraut");
      mData.put ("england", "fishnchips");
      mData.put ("suomi", "salmiakki");
      mData.put ("france", "fromage");
      mData.put ("espana", "paella");

      mExpected.put ("deutschland", "sauerkraut");
      mExpected.put ("england", "fishnchips");
      mExpected.put ("espana", "paella");
      mExpected.put ("france", "fromage");
      mExpected.put ("suomi", "salmiakki");
      mExpected.put ("sverige", "smorgasbord");
    }
  }
}
