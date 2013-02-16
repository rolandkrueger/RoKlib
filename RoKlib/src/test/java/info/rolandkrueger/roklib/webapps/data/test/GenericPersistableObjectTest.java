/*
 * $Id: GenericPersistableObjectTest.java 181 2010-11-01 09:39:13Z roland $
 * Copyright (C) 2007 Roland Krueger
 * Created on 27.01.2010
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
package info.rolandkrueger.roklib.webapps.data.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import info.rolandkrueger.roklib.webapps.data.GenericPersistableObject;

import org.junit.Test;

public class GenericPersistableObjectTest
{
  @Test
  public void testSetID ()
  {
    GenericPersistableObject<Long> testObj = new GenericPersistableObject<Long> ();
    testObj.setKey (12345L);
    assertEquals (12345L, testObj.getKey ());
    Long id = new Long (23L);
    testObj.setKey (id);
    assertTrue (testObj.getKey () == id);
  }
}
