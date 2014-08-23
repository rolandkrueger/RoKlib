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
package org.roklib.swing;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ErrorMessageViewer extends JDialog implements ActionListener {
    private static final long serialVersionUID = -6062453811062792927L;
    private JButton mButton;

    public ErrorMessageViewer(Frame owner, String title, String message) {
        super(owner, "Error!", true);

        setLayout(new BorderLayout());
        JPanel labelPanel = new JPanel();
        labelPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        labelPanel.setBorder(new EmptyBorder(6, 6, 6, 6));
        labelPanel.add(new JLabel(title));
        add(labelPanel, BorderLayout.NORTH);

        JTextArea textArea = new JTextArea(message);
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        JScrollPane scroller = new JScrollPane(textArea);
        scroller.setBorder(new CompoundBorder(new EmptyBorder(6, 6, 6, 6), new EtchedBorder(EtchedBorder.LOWERED)));
        add(scroller, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        mButton = new JButton("  Ok  ");
        buttonPanel.add(mButton);
        mButton.addActionListener(this);

        add(buttonPanel, BorderLayout.SOUTH);

        setSize(400, 300);
    }

    public void actionPerformed(ActionEvent e) {
        setVisible(false);
    }

    public void addActionListener(ActionListener l) {
        mButton.addActionListener(l);
    }

    public void removeActionListener(ActionListener l) {
        mButton.removeActionListener(l);
    }
}
