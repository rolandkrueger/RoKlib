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
package org.roklib.ui.swing;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JToolBar;

/**
 * This subclass of {@link javax.swing.AbstractAction} enhances the action classes with the ability of using hover
 * messages. If the user points at one of the GUI representations of the {@link AbstractHoverableAction}, a predefined
 * message can be displayed by all {@link HoverListener}s that have been registered as listeners with the action object.<br>
 * <br>
 * In order for this feature to work, an {@link AbstractHoverableAction} must be added to a menu bar or a tool bar by
 * using methods {@link AbstractHoverableAction#attachToMenu(JMenu)} and
 * {@link AbstractHoverableAction#attachToToolbar(JToolBar)}, respectively.
 * 
 * @see Hoverable
 * @see HoverManager
 * @see HoverListener
 * @author Roland Krueger
 */
public abstract class AbstractHoverableAction extends AbstractAction implements Hoverable
{
  private static final long serialVersionUID = 2709720096565522081L;

  protected JButton         mButton;
  protected JMenuItem       mMenuItem;
  private HoverManager      mHoverManager;

  /**
   * Constructs a new {@link AbstractHoverableAction}.
   * 
   * @see AbstractAction#AbstractAction(java.lang.String, javax.swing.Icon)
   */
  public AbstractHoverableAction (String name, Icon icon)
  {
    super (name, icon);
    mHoverManager = new HoverManager (this);
  }

  /**
   * Attach this {@link AbstractHoverableAction} to the given toolbar. By using this method, the correct
   * {@link java.awt.event.MouseListener}s will be registered so that hover events will correctly by disseminated.
   * 
   * @param toolbar
   *          target toolbar that this action will be added to
   */
  public void attachToToolbar (JToolBar toolbar)
  {
    mButton = toolbar.add (this);
    mButton.addMouseListener (mHoverManager);
  }

  /**
   * Attach this {@link AbstractHoverableAction} to the given menu. By using this method, the correct
   * {@link java.awt.event.MouseListener}s will be registered so that hover events will correctly by disseminated.
   * 
   * @param menu
   *          target menu that this action will be added to
   */
  public void attachToMenu (JMenu menu)
  {
    mMenuItem = menu.add (this);
    mMenuItem.addMouseListener (mHoverManager);
  }

  /**
   * Gets the hover text for this action.
   * 
   * @return the hover text for this action
   */
  public String getHoverText ()
  {
    return getValue (Action.SHORT_DESCRIPTION).toString ();
  }

  /**
   * Add a {@link HoverListener} to the list of listeners.
   * 
   * @param listener
   *          a {@link HoverListener} registering for hover events
   * @return <code>true</code>
   */
  public boolean addHoverListener (HoverListener listener)
  {
    return mHoverManager.addHoverListener (listener);
  }

  /**
   * Sets the message that can be displayed during hover events.
   * 
   * @param hoverMessage
   *          a help message for this action
   */
  public void setHoverMessage (String hoverMessage)
  {
    putValue (Action.SHORT_DESCRIPTION, hoverMessage);
  }

  /**
   * Sets the short key for this action.
   * 
   * @param mnemonicKey
   *          one of the key constants as defined in {@link Action}.
   */
  public void setMnemonicKey (int mnemonicKey)
  {
    putValue (Action.MNEMONIC_KEY, new Integer (mnemonicKey));
  }
}
