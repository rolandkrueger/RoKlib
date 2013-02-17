/*
 * Copyright (C) 2007 Roland Krueger
 * Created on 26.03.2010
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
package info.rolandkrueger.roklib.io;

import info.rolandkrueger.roklib.io.test.ByteArrayStreamDataBufferTest;
import info.rolandkrueger.roklib.io.test.IStreamDataBufferTest;
import info.rolandkrueger.roklib.io.test.StreamUtilitiesTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith (Suite.class)
@SuiteClasses ({ StreamUtilitiesTest.class, IStreamDataBufferTest.class, ByteArrayStreamDataBufferTest.class })
public class IOTests
{
}
