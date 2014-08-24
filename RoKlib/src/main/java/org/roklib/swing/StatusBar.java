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
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * This class represents a status bar that can be used to display status messages in a windowed application. The area of
 * a status bar is divided into two parts. The bar's left half is used as display for general messages. Here, all types
 * of messages can be displayed by the application. If desired, the right half of a status bar can be filled with
 * specialized message fields. Such fields can be used to show the status of a some special part of the application. In
 * a client-server application, it is conceivable to show the server address a client is connected to in such a field,
 * for example. These specialized message fields are added to a status bar with
 * {@link StatusBar#addElement(String, String)}.<br>
 * <br>
 * It is possible to display messages in the message area of a status bar that will be shown for a certain predefined
 * period of time. This is done with {@link StatusBar#setTimedMessage(String)}. If the delay of a timed message - which
 * can be defined through the constructor {@link StatusBar#StatusBar(long)} or with
 * {@link StatusBar#setMessageDelay(long)} - has elapsed, the timed message is replaced by the ready message as provided
 * by {@link StatusBar#setReadyMessage(String)}.<br>
 * <br>
 * <b>Example:</b> The following code will create a status bar as sketched below: <blockquote>
 * <p/>
 * <pre>
 * StatusBar bar = new StatusBar ();
 * bar.setReadyMessage ("Ready.");
 * bar.addElement      ("Msg. field 1",     "Help text for message field 1.");
 * bar.addElement      ("Other msg. field", "Help text for the other message field.");
 *
 * +-------------------------------+--------------+------------------+
 * | Ready.                        | Msg. field 1 | Other msg. field |
 * +-------------------------------+--------------+------------------+
 * </pre>
 * <p/>
 * </blockquote>
 *
 * @author Roland Krueger
 */
public class StatusBar extends JPanel implements HoverListener {
    private static final long serialVersionUID = 1777229355563005791L;

    private final JLabel mInfoLabel;
    private final JPanel mElementsPanel;
    private final Timer mTimer;
    private long mDelay;
    private String mReadyText = "";
    private String mCurrentMessage = "";
    private final ArrayList<BarElement> mElements;

    /**
     * Default constructor. The duration of displaying a timed message is set to 2 seconds.
     *
     * @see StatusBar#setTimedMessage(String)
     */
    public StatusBar() {
        this(2000);
    }

    /**
     * Constructs a {@link StatusBar} with the specified delay for timed messages.
     *
     * @param messageDelay delay in milliseconds for timed messages
     * @see StatusBar#setTimedMessage(String)
     */
    public StatusBar(long messageDelay) {
        mInfoLabel = new JLabel();
        setLayout(new GridLayout());
        mInfoLabel.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
        add(mInfoLabel);
        mDelay = messageDelay;
        mTimer = new Timer();
        mElementsPanel = new JPanel();
        mElements = new ArrayList<BarElement>();
        mElementsPanel.setLayout(new GridLayout());
        setBorder(new EmptyBorder(1, 1, 1, 1));
    }

    public void hoverStarted(Hoverable source) {
        if (source.getHoverText() == null)
            return;
        mInfoLabel.setText(source.getHoverText());
    }

    public void hoverEnded(Hoverable source) {
        mInfoLabel.setText(mCurrentMessage);
    }

    /**
     * Sets the ready message for this status bar. This message will be displayed in the message area if no other message
     * is currently being shown.
     *
     * @param msg the ready message for this status bar.
     */
    public void setReadyMessage(String msg) {
        mReadyText = msg;
        mCurrentMessage = msg;
        mInfoLabel.setText(msg);
    }

    /**
     * A timed message will be displayed in the message area for a certain amount of time. This time can be configured
     * either through the constructor {@link StatusBar#StatusBar(long)} or through method
     * {@link StatusBar#setMessageDelay(long)}. After the delay has elapsed, the status bar's ready message is displayed
     * again.
     *
     * @param msg the message to be displayed for some time
     */
    public void setTimedMessage(String msg) {
        mCurrentMessage = msg;
        mInfoLabel.setText(msg);

        mTimer.schedule(new TimerTask() {
            public void run() {
                mCurrentMessage = mReadyText;
                mInfoLabel.setText(mCurrentMessage);
            }
        }, mDelay);
    }

    /**
     * Sets the amount of time in milliseconds of how long a timed message will be displayed.
     *
     * @param delay the amount of time in milliseconds of how long a timed message will be displayed
     * @see StatusBar#setTimedMessage(String)
     */
    public void setMessageDelay(long delay) {
        mDelay = delay;
    }

    /**
     * Adds a specialized message field to the right half of the status bar. The specified <code>text</code> will be
     * displayed in that field. The given <code>helpText</code> will be displayed in the general message area while the
     * mouse cursor hovers over the specialized message field. Both of these text messages can be changed with
     * {@link StatusBar#setElementText(int, String)} and {@link StatusBar#setElementHelpText(int, String)}, respectively.
     * The <code>int</code> value that is returned by this method is the identification number of the new specialized
     * message field. This number is needed for the first parameter of the <code>setElement*</code> methods and indicates
     * the field that is meant to be changed.
     *
     * @param text     the text that will be displayed in the text area
     * @param helpText help text for the text area that will be displayed in the general text area when the mouse hovers over the
     *                 new text area
     * @return an identification number that is needed to refer to the newly added text area
     * @see StatusBar#setElementHelpText(int, String)
     * @see StatusBar#setElementText(int, String)
     */
    public int addElement(String text, String helpText) {
        BarElement element = new BarElement(text, helpText);
        element.addHoverListener(this);
        if (mElements.size() == 0) {
            // rearrange layout if the first separate element is added
            removeAll();
            setLayout(new GridLayout(1, 2));
            add(mInfoLabel);
            add(mElementsPanel);
        }
        mElementsPanel.add(element);
        mElements.add(element);
        return mElements.indexOf(element);
    }

    /**
     * Sets the text displayed by the spezialized message area with the given identification number.
     *
     * @param elementNr identification number as provided by {@link StatusBar#addElement(String, String)} which refers to the
     *                  message field that is to be changed by this method
     * @param text      the text to be displayed by the message field
     * @throws ArrayIndexOutOfBoundsException if the message field denoted by <code>elementNr</code> does not exist
     */
    public void setElementText(int elementNr, String text) {
        if (elementNr > mElements.size() || elementNr < 0)
            throw new ArrayIndexOutOfBoundsException();
        mElements.get(elementNr).setText(text);
    }

    /**
     * Sets the help text that belongs to the spezialized message area with the given identification number.
     *
     * @param elementNr identification number as provided by {@link StatusBar#addElement(String, String)} which refers to the
     *                  message field that is to be changed by this method
     * @param helpText  the text to be displayed by the message field
     * @throws ArrayIndexOutOfBoundsException if the message field denoted by <code>elementNr</code> does not exist
     */
    public void setElementHelpText(int elementNr, String helpText) {
        if (elementNr >= mElements.size() || elementNr < 0)
            throw new ArrayIndexOutOfBoundsException();
        mElements.get(elementNr).setHelpText(helpText);
    }

    /**
     * This class represents a single display area of a status bar.
     *
     * @author Roland Krueger
     */
    private class BarElement extends JLabel implements Hoverable {
        private static final long serialVersionUID = 9159375387417999283L;

        private String mHelpText;
        private final HoverManager mHoverManager;

        public BarElement(String text, String helpText) {
            super(text);
            setHelpText(helpText);
            mHoverManager = new HoverManager(this);
            addMouseListener(mHoverManager);
            setBorder(new EtchedBorder(EtchedBorder.LOWERED));
        }

        public BarElement(String text) {
            this(text, "");
        }

        public void setHelpText(String helpText) {
            mHelpText = helpText;
            setToolTipText(helpText);
        }

        public String getHoverText() {
            return mHelpText;
        }

        public boolean addHoverListener(HoverListener listener) {
            return mHoverManager.addHoverListener(listener);
        }
    }
}
