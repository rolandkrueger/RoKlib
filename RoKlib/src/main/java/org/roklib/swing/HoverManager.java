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

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.LinkedList;

public class HoverManager implements MouseListener {
    protected final LinkedList<HoverListener> mHoverListeners;
    protected final Hoverable mOwner;

    public HoverManager(Hoverable owner) {
        mHoverListeners = new LinkedList<HoverListener>();
        mOwner = owner;
    }

    public boolean addHoverListener(HoverListener listener) {
        return mHoverListeners.add(listener);
    }

    public boolean removeHoverListener(HoverListener listener) {
        return mHoverListeners.remove(listener);
    }

    public void mouseEntered(MouseEvent e) {
        for (HoverListener listener : mHoverListeners) {
            listener.hoverStarted(mOwner);
        }
    }

    public void mouseExited(MouseEvent e) {
        for (HoverListener listener : mHoverListeners) {
            listener.hoverEnded(mOwner);
        }
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }
}
