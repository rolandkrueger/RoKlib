/*
 * Copyright (C) 2007 - 2010 Roland Krueger
 * Created on 07.03.2010
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
package info.rolandkrueger.roklib.webapps.urldispatching.urlparameters;

import info.rolandkrueger.roklib.webapps.urldispatching.urlparameters.test.Point2DURLParameterTest;
import info.rolandkrueger.roklib.webapps.urldispatching.urlparameters.test.SingleBooleanURLParameterTest;
import info.rolandkrueger.roklib.webapps.urldispatching.urlparameters.test.SingleDoubleURLParameterTest;
import info.rolandkrueger.roklib.webapps.urldispatching.urlparameters.test.SingleFloatURLParameterTest;
import info.rolandkrueger.roklib.webapps.urldispatching.urlparameters.test.SingleIntegerURLParameterTest;
import info.rolandkrueger.roklib.webapps.urldispatching.urlparameters.test.SingleLongURLParameterTest;
import info.rolandkrueger.roklib.webapps.urldispatching.urlparameters.test.SingleStringURLParameterTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith (Suite.class)
@SuiteClasses ({ SingleBooleanURLParameterTest.class, SingleDoubleURLParameterTest.class,
    SingleFloatURLParameterTest.class, SingleIntegerURLParameterTest.class, SingleLongURLParameterTest.class,
    SingleStringURLParameterTest.class, Point2DURLParameterTest.class })
public class URLParametersTests
{
}