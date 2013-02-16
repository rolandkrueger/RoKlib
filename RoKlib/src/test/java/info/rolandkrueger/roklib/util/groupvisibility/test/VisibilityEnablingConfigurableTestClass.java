/*
 * $Id: VisibilityEnablingConfigurableTestClass.java 178 2010-10-31 18:01:20Z roland $
 * Copyright (C) 2007 Roland Krueger
 * Created on 19.10.2009
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

import info.rolandkrueger.roklib.util.groupvisibility.IVisibilityEnablingConfigurable;

public class VisibilityEnablingConfigurableTestClass implements IVisibilityEnablingConfigurable
{
  private boolean mEnabled = true;
  private boolean mVisible = true;

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
