/*
 * $Id: $
 * Copyright (C) 2007 Roland Krueger
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
package info.rolandkrueger.roklib.util.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import info.rolandkrueger.roklib.util.AbstractCommand;
import info.rolandkrueger.roklib.util.EnhancedReturnType;

import org.junit.Before;
import org.junit.Test;

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
