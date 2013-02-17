/*
 * Copyright (C) 2007 Roland Krueger
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
package info.rolandkrueger.roklib.util.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.roklib.util.AbstractCommand;
import org.roklib.util.EnhancedReturnType;

public class AbstractCommandTest
{
  private CVoidTestCommand mTestObject;

  @Before
  public void setUp ()
  {
    mTestObject = new CVoidTestCommand ();
  }

  @Test
  public void testResultDoesNotRemainVoidAfterSecondCommandExecution ()
  {
    mTestObject.setVoid (true);
    mTestObject.run ();
    assertTrue (mTestObject.getResult ().isVoided ());
    mTestObject.setVoid (false);
    // the void status should be reset for the second execution of the same command object
    mTestObject.run ();
    assertFalse (mTestObject.getResult ().isVoided ());
  }

  private static class CVoidTestCommand extends AbstractCommand<Integer>
  {
    private boolean mSetVoid = false;

    public void setVoid (boolean setVoid)
    {
      mSetVoid = setVoid;
    }

    @Override
    protected EnhancedReturnType<Integer> executeImpl ()
    {
      if (mSetVoid)
      {
        return EnhancedReturnType.Builder.createVoided (Integer.class).build ();
      } else
      {
        return EnhancedReturnType.Builder.createSuccessful (Integer.class).withValue (1).build ();
      }
    }
  }
}
