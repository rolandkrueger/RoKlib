/*
 * $Id: RoKlibTests.java 186 2010-11-01 10:12:14Z roland $
 * Copyright (C) 2007 Roland Krueger
 * Created on 10.05.2009
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
package info.rolandkrueger.roklib.test;

import info.rolandkrueger.roklib.util.test.UtilsTests;
import info.rolandkrueger.roklib.webapps.test.WebappsTest;
import info.rolandkrueger.roklib.webapps.urldispatching.URLDispatchingTests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith (Suite.class)
@SuiteClasses ({ UtilsTests.class, WebappsTest.class, URLDispatchingTests.class })
public class RoKlibTests
{
}
