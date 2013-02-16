/*
 * Copyright (C) 2007 Roland Krueger
 * Created on 11.12.2009
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
  private static final long    serialVersionUID = -2908436645253742119L;

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

  public JDialogClosableWithESC (Dialog owner, String title, boolean modal) throws HeadlessException
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
      if (e.getSource () instanceof Component && e.getKeyCode () == KeyEvent.VK_ESCAPE && !e.isConsumed ()
          && isAncestorOf ((Component) e.getSource ()))
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
