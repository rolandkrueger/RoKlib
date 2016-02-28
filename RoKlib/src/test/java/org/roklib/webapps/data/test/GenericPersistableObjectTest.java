/*
 * Copyright (C) 2007 Roland Krueger
 * Created on 27.01.2010
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
package org.roklib.webapps.data.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.roklib.webapps.data.GenericPersistableObject;

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