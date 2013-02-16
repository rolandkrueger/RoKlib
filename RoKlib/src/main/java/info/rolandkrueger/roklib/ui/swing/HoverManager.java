/*
 * $Id: HoverManager.java 178 2010-10-31 18:01:20Z roland $
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
package info.rolandkrueger.roklib.ui.swing;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.LinkedList;

public class HoverManager implements MouseListener
{
  protected LinkedList<HoverListener> mHoverListeners;
  protected Hoverable mOwner;

  public HoverManager (Hoverable owner)
  {
    mHoverListeners = new LinkedList<HoverListener> ();
    mOwner = owner;
  }

  public boolean addHoverListener (HoverListener listener)
  {
    return mHoverListeners.add (listener);
  }

  public boolean removeHoverListener (HoverListener listener)
  {
    return mHoverListeners.remove (listener);
  }

  public void mouseEntered (MouseEvent e)
  {
    for (HoverListener listener : mHoverListeners)
    {
      listener.hoverStarted (mOwner);
    }
  }

  public void mouseExited (MouseEvent e)
  {
    for (HoverListener listener : mHoverListeners)
    {
      listener.hoverEnded (mOwner);
    }
  }

  public void mouseClicked (MouseEvent e)
  {
  }

  public void mousePressed (MouseEvent e)
  {
  }

  public void mouseReleased (MouseEvent e)
  {
  }
}
