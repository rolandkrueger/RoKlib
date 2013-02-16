/*
 * $Id: JDialogClosableWithESC.java 178 2010-10-31 18:01:20Z roland $
 * Copyright (C) 2007 Roland Krueger
 * Created on 11.12.2009
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

import java.awt.Component;
import java.awt.DefaultKeyboardFocusManager;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import java.awt.KeyEventPostProcessor;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

import javax.swing.JDialog;

public abstract class JDialogClosableWithESC extends JDialog
{
  private static final long serialVersionUID = - 2908436645253742119L;

  private KeyboardFocusManager mSavedKeyboardFocusManager;

  public JDialogClosableWithESC () throws HeadlessException
  {
    super ();
    init ();
  }

  public JDialogClosableWithESC (Dialog owner, boolean modal) throws HeadlessException
  {
    super (owner, modal);
    init ();
  }

  public JDialogClosableWithESC (Dialog owner, String title, boolean modal, GraphicsConfiguration gc)
      throws HeadlessException
  {
    super (owner, title, modal, gc);
    init ();
  }

  public JDialogClosableWithESC (Dialog owner, String title, boolean modal)
      throws HeadlessException
  {
    super (owner, title, modal);
    init ();
  }

  public JDialogClosableWithESC (Dialog owner, String title) throws HeadlessException
  {
    super (owner, title);
    init ();
  }

  public JDialogClosableWithESC (Dialog owner) throws HeadlessException
  {
    super (owner);
    init ();
  }

  public JDialogClosableWithESC (Frame owner, boolean modal) throws HeadlessException
  {
    super (owner, modal);
    init ();
  }

  public JDialogClosableWithESC (Frame owner, String title, boolean modal, GraphicsConfiguration gc)
  {
    super (owner, title, modal, gc);
    init ();
  }

  public JDialogClosableWithESC (Frame owner, String title, boolean modal) throws HeadlessException
  {
    super (owner, title, modal);
    init ();
  }

  public JDialogClosableWithESC (Frame owner, String title) throws HeadlessException
  {
    super (owner, title);
    init ();
  }

  public JDialogClosableWithESC (Frame owner) throws HeadlessException
  {
    super (owner);
    init ();
  }

  private void init ()
  {
    addWindowFocusListener (new WindowFocusListener ()
    {
      public void windowGainedFocus (WindowEvent e)
      {
        installFocusManager ();
      }

      public void windowLostFocus (WindowEvent e)
      {
        deinstallFocusManager ();
      }
    });
  }

  @Override
  public void setVisible (boolean visible)
  {
    if (visible)
    {
      installFocusManager ();
    } else
    {
      deinstallFocusManager ();
    }
    super.setVisible (visible);
  }

  protected abstract void cancelDialog ();

  private class LocalKeyEventPostProcessor implements KeyEventPostProcessor
  {
    public boolean postProcessKeyEvent (KeyEvent e)
    {
      if (e.getSource () instanceof Component && e.getKeyCode () == KeyEvent.VK_ESCAPE
          && ! e.isConsumed () && isAncestorOf ((Component) e.getSource ()))
      {
        e.consume ();
        cancelDialog ();
      }
      return true;
    }
  }

  private void installFocusManager ()
  {
    mSavedKeyboardFocusManager = KeyboardFocusManager.getCurrentKeyboardFocusManager ();
    KeyboardFocusManager newFocusManager = new DefaultKeyboardFocusManager ();
    newFocusManager.addKeyEventPostProcessor (new LocalKeyEventPostProcessor ());
    KeyboardFocusManager.setCurrentKeyboardFocusManager (newFocusManager);
  }

  private void deinstallFocusManager ()
  {
    KeyboardFocusManager.setCurrentKeyboardFocusManager (mSavedKeyboardFocusManager);
  }
}
