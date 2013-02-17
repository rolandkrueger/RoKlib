/*
 * Copyright (C) 2007 Roland Krueger
 * Created on 01.07.2010
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
package info.rolandkrueger.roklib.util.groupvisibility.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.roklib.util.groupvisibility.IVisibilityEnablingConfigurable;
import org.roklib.util.groupvisibility.RadioComponentGroupManager;
import org.roklib.util.groupvisibility.RadioComponentGroupManager.Mode;

public class RadioComponentGroupManagerTest
{
  private TestRadioComponent                              mComponent1;
  private TestRadioComponent                              mComponent2;
  private TestRadioComponent                              mComponent3;
  private RadioComponentGroupManager.RadioComponentSwitch mSwitch1;
  private RadioComponentGroupManager.RadioComponentSwitch mSwitch2;
  private RadioComponentGroupManager.RadioComponentSwitch mSwitch3;
  private RadioComponentGroupManager                      mTestObj;

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
    private static final long serialVersionUID = -1878281356099704091L;

    private boolean           mEnabled;
    private boolean           mVisible;

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
