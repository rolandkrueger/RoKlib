/*
 * $Id: AbstractHoverableAction.java 178 2010-10-31 18:01:20Z roland $
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

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JToolBar;

/**
 * This subclass of {@link javax.swing.AbstractAction} enhances the action
 * classes with the ability of using hover messages. If the user points at one
 * of the GUI representations of the {@link AbstractHoverableAction}, a
 * predefined message can be displayed by all {@link HoverListener}s that have
 * been registered as listeners with the action object.<br>
 * <br>
 * In order for this feature to work, an {@link AbstractHoverableAction} must be
 * added to a menu bar or a tool bar by using methods
 * {@link AbstractHoverableAction#attachToMenu(JMenu)} and
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

  protected JButton mButton;
  protected JMenuItem mMenuItem;
  private HoverManager mHoverManager;

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
   * Attach this {@link AbstractHoverableAction} to the given toolbar. By using
   * this method, the correct {@link java.awt.event.MouseListener}s will be
   * registered so that hover events will correctly by disseminated.
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
   * Attach this {@link AbstractHoverableAction} to the given menu. By using
   * this method, the correct {@link java.awt.event.MouseListener}s will be
   * registered so that hover events will correctly by disseminated.
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
