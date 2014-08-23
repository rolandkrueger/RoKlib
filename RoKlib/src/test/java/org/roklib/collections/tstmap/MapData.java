/*
 * Copyright (C) 2007 Roland Krueger
 * Created on 10.07.2007
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
package org.roklib.collections.tstmap;

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
