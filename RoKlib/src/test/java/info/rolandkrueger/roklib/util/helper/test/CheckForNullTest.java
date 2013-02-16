/*
 * $Id: CheckForNullTest.java 181 2010-11-01 09:39:13Z roland $
 * Copyright (C) 2007 Roland Krueger
 * Created on 17.10.2009
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
package info.rolandkrueger.roklib.util.helper.test;

import static org.junit.Assert.assertTrue;
import info.rolandkrueger.roklib.util.helper.CheckForNull;

import org.junit.Test;

public class CheckForNullTest
{
  @Test
  public void testCheckNoNull ()
  {
    CheckForNull.check ();
    CheckForNull.check ("not null");
    CheckForNull.check ("not null", new Object ());
    CheckForNull.check ("not null", new Object (), this);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testCheckWithNull ()
  {
    try
    {
      CheckForNull.check ("not null", null, "not null", null);
    } catch (IllegalArgumentException e)
    {
      assertTrue (e.getMessage ().contains ("2, 4"));
      throw e;
    }
  }
}
