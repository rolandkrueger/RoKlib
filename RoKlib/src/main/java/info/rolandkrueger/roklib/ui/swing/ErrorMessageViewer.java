/*
 * $Id: ErrorMessageViewer.java 178 2010-10-31 18:01:20Z roland $
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

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

public class ErrorMessageViewer extends JDialog implements ActionListener
{
  private static final long serialVersionUID = - 6062453811062792927L;
  private JButton mButton;

  public ErrorMessageViewer (Frame owner, String title, String message)
  {
    super (owner, "Error!", true);

    setLayout (new BorderLayout ());
    JPanel labelPanel = new JPanel ();
    labelPanel.setLayout (new FlowLayout (FlowLayout.LEFT));
    labelPanel.setBorder (new EmptyBorder (6, 6, 6, 6));
    labelPanel.add (new JLabel (title));
    add (labelPanel, BorderLayout.NORTH);

    JTextArea textArea = new JTextArea (message);
    textArea.setEditable (false);
    textArea.setLineWrap (true);
    textArea.setWrapStyleWord (true);
    JScrollPane scroller = new JScrollPane (textArea);
    scroller.setBorder (new CompoundBorder (new EmptyBorder (6, 6, 6, 6), new EtchedBorder (
        EtchedBorder.LOWERED)));
    add (scroller, BorderLayout.CENTER);

    JPanel buttonPanel = new JPanel ();
    mButton = new JButton ("  Ok  ");
    buttonPanel.add (mButton);
    mButton.addActionListener (this);

    add (buttonPanel, BorderLayout.SOUTH);

    setSize (400, 300);
  }

  public void actionPerformed (ActionEvent e)
  {
    setVisible (false);
  }

  public void addActionListener (ActionListener l)
  {
    mButton.addActionListener (l);
  }

  public void removeActionListener (ActionListener l)
  {
    mButton.removeActionListener (l);
  }
}
