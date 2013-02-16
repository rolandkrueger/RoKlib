/*
 * $Id: RadioComponentGroupManagerTest.java 181 2010-11-01 09:39:13Z roland $
 * Copyright (C) 2007 Roland Krueger
 * Created on 01.07.2010
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
package info.rolandkrueger.roklib.util.groupvisibility.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import info.rolandkrueger.roklib.util.groupvisibility.IVisibilityEnablingConfigurable;
import info.rolandkrueger.roklib.util.groupvisibility.RadioComponentGroupManager;
import info.rolandkrueger.roklib.util.groupvisibility.RadioComponentGroupManager.Mode;

import org.junit.Before;
import org.junit.Test;

public class RadioComponentGroupManagerTest
{
  private TestRadioComponent mComponent1;
  private TestRadioComponent mComponent2;
  private TestRadioComponent mComponent3;
  private RadioComponentGroupManager.RadioComponentSwitch mSwitch1;
  private RadioComponentGroupManager.RadioComponentSwitch mSwitch2;
  private RadioComponentGroupManager.RadioComponentSwitch mSwitch3;
  private RadioComponentGroupManager mTestObj;

  @Before
  public void setUp ()
  {
    mComponent1 = new TestRadioComponent ();
    mComponent2 = new TestRadioComponent ();
    mComponent3 = new TestRadioComponent ();
    mComponent1.setEnabled (false);
    mComponent1.setVisible (false);
    mComponent2.setEnabled (false);
    mComponent2.setVisible (false);
    mComponent3.setEnabled (false);
    mComponent3.setVisible (false);
  }

  private void setUpTestObj (Mode mode)
  {
    mTestObj = new RadioComponentGroupManager (mode);
    mSwitch1 = mTestObj.addComponent ("group1", mComponent1);
    mSwitch2 = mTestObj.addComponent ("group2", mComponent2);
    mSwitch3 = mTestObj.addComponent ("group3", mComponent3);
  }

  @Test
  public void testEnablingMode ()
  {
    setUpTestObj (Mode.ENABLING);

    mSwitch1.select ();
    assertTrue (mComponent1.isEnabled ());
    assertFalse (mComponent1.isVisible ());
    assertFalse (mComponent2.isEnabled ());
    assertFalse (mComponent2.isVisible ());
    assertFalse (mComponent3.isEnabled ());
    assertFalse (mComponent3.isVisible ());

    mSwitch2.select ();
    assertTrue (mComponent2.isEnabled ());
    assertFalse (mComponent2.isVisible ());
    assertFalse (mComponent1.isEnabled ());
    assertFalse (mComponent1.isVisible ());
    assertFalse (mComponent3.isEnabled ());
    assertFalse (mComponent3.isVisible ());

    mSwitch3.select ();
    assertTrue (mComponent3.isEnabled ());
    assertFalse (mComponent3.isVisible ());
    assertFalse (mComponent2.isEnabled ());
    assertFalse (mComponent2.isVisible ());
    assertFalse (mComponent1.isEnabled ());
    assertFalse (mComponent1.isVisible ());
  }

  @Test
  public void testVisibilityMode ()
  {
    setUpTestObj (Mode.VISIBILITY);

    mSwitch1.select ();
    assertTrue (mComponent1.isVisible ());
    assertFalse (mComponent1.isEnabled ());
    assertFalse (mComponent2.isVisible ());
    assertFalse (mComponent2.isEnabled ());
    assertFalse (mComponent3.isVisible ());
    assertFalse (mComponent3.isEnabled ());

    mSwitch2.select ();
    assertTrue (mComponent2.isVisible ());
    assertFalse (mComponent2.isEnabled ());
    assertFalse (mComponent1.isVisible ());
    assertFalse (mComponent1.isEnabled ());
    assertFalse (mComponent3.isVisible ());
    assertFalse (mComponent3.isEnabled ());

    mSwitch3.select ();
    assertTrue (mComponent3.isVisible ());
    assertFalse (mComponent3.isEnabled ());
    assertFalse (mComponent1.isVisible ());
    assertFalse (mComponent1.isEnabled ());
    assertFalse (mComponent2.isVisible ());
    assertFalse (mComponent2.isEnabled ());
  }

  private class TestRadioComponent implements IVisibilityEnablingConfigurable
  {
    private static final long serialVersionUID = - 1878281356099704091L;

    private boolean mEnabled;
    private boolean mVisible;

    public boolean isEnabled ()
    {
      return mEnabled;
    }

    public boolean isVisible ()
    {
      return mVisible;
    }

    public void setEnabled (boolean enabled)
    {
      mEnabled = enabled;
    }

    public void setVisible (boolean visible)
    {
      mVisible = visible;
    }
  }
}
