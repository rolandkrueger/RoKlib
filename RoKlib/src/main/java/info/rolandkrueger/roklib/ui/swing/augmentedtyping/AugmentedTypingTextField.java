/*
 * Copyright (C) 2007 Roland Krueger
 * Created on 02.02.2007
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
package info.rolandkrueger.roklib.ui.swing.augmentedtyping;

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.ComboBoxEditor;
import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

public class AugmentedTypingTextField extends JComboBox implements KeyListener
{
  private static final long                 serialVersionUID = 1637805828266123965L;

  private AugmentedTypingTextFieldDataModel mDataModel;
  private Map<Character, Character[]>       mCharacterMapping;
  private AugmentedTypingTextFieldEditor    mEditor;

  public AugmentedTypingTextField ()
  {
    this (new DefaultIPAKeyMapping ());
  }

  public AugmentedTypingTextField (AugmentedTypingKeyMapping characterMapping)
  {
    super ();
    mEditor = new AugmentedTypingTextFieldEditor ();
    setEditor (mEditor);
    // listen to typing events
    mEditor.getEditorComponent ().addKeyListener (this);
    setEditable (true);
    mDataModel = new AugmentedTypingTextFieldDataModel ();
    setModel (mDataModel);
    setCharacterMapping (characterMapping);
    addActionListener (new ActionListener ()
    {
      public void actionPerformed (ActionEvent e)
      {
        if ((e.getModifiers () & AWTEvent.MOUSE_EVENT_MASK) == AWTEvent.MOUSE_EVENT_MASK
            && mDataModel.mMouseSelectedItem != null)
        {
          mEditor.swapChar (mDataModel.mMouseSelectedItem);
        }
      }
    });
  }

  public void setText (String newText)
  {
    mEditor.mTextField.setText (newText);
    updateEditorComponent ();
  }

  public String getText ()
  {
    return mEditor.mTextField.getText ();
  }

  public void setCharacterMapping (AugmentedTypingKeyMapping newMapping)
  {
    mCharacterMapping = newMapping.getData ();
    newMapping.setInstalled ();
  }

  public void keyTyped (KeyEvent keyEvent)
  {
    if (!keyEvent.isActionKey ())
    {
      // handle the Escape-key
      if (keyEvent.getKeyChar () == KeyEvent.VK_ESCAPE)
      {
        if (mDataModel.getSize () > 0)
        {
          mDataModel.setIPASelectedIndex (0);
          setSelectedIndex (0);
          updateEditorComponent ();
        }
        hidePopup ();
        return;
      }

      Character[] currentChars = mCharacterMapping.get (keyEvent.getKeyChar ());

      if (currentChars != null)
      {
        mDataModel.setData (currentChars);
        hidePopup (); // hide popup first to avoid drawing errors
        showPopup ();
      } else
      {
        mDataModel.setData (new Character[0]);
        hidePopup ();
      }
      if (mDataModel.getSize () > 0)
      {
        mDataModel.setIPASelectedIndex (-1);
        setSelectedIndex (-1);
      }
    }
  }

  public void keyPressed (KeyEvent keyEvent)
  {
    if (mDataModel.getSize () == 0)
      return;
    if (keyEvent.getKeyCode () == KeyEvent.VK_DOWN)
    {
      // Down arrow key pressed
      if (mDataModel.getIPASelectedIndex () == mDataModel.getSize () - 1 || mDataModel.getIPASelectedIndex () == -1)
      {
        mDataModel.setIPASelectedIndex (0);
        setSelectedIndex (0);
      } else
      {
        mDataModel.setIPASelectedIndex (mDataModel.getIPASelectedIndex () + 1);
        setSelectedIndex (mDataModel.getIPASelectedIndex ());
      }
      updateEditorComponent ();
    }

    if (keyEvent.getKeyCode () == KeyEvent.VK_UP)
    {
      // Up arrow key pressed
      showPopup ();
      if (getSelectedIndex () <= 0)
      {
        mDataModel.setIPASelectedIndex (mDataModel.getSize () - 1);
        setSelectedIndex (mDataModel.getSize () - 1);
      } else
      {
        mDataModel.setIPASelectedIndex (mDataModel.getIPASelectedIndex () - 1);
        setSelectedIndex (mDataModel.getIPASelectedIndex ());
      }
      updateEditorComponent ();
    }

    if (keyEvent.getKeyCode () == KeyEvent.VK_LEFT || keyEvent.getKeyCode () == KeyEvent.VK_RIGHT)
    {
      mDataModel.setData (new Character[0]);
      hidePopup ();
    }
  }

  /**
   * Cause an update of the editor component. This will happen each time that an entry of this component's popup menu
   * was selected either by mouse or by the arrow keys.
   */
  private void updateEditorComponent ()
  {
    if (mDataModel.getIPASelectedItem () != null)
      mEditor.swapChar (mDataModel.getIPASelectedItem ());
  }

  /**
   * Not needed.
   */
  public void keyReleased (KeyEvent keyEvent)
  {
  }

  /**
   * Data model for the {@link AugmentedTypingTextField}.
   * 
   * @author Roland Krueger
   */
  private class AugmentedTypingTextFieldDataModel implements ComboBoxModel
  {
    private Character[]            mData;
    private List<ListDataListener> mDataListeners;
    private Character              mSelectedItem;
    private Character              mMouseSelectedItem;
    private int                    mSelectedIndex = -1;

    private AugmentedTypingTextFieldDataModel ()
    {
      mData = new Character[0];
      mDataListeners = new ArrayList<ListDataListener> ();
    }

    public Character getIPASelectedItem ()
    {
      return mSelectedItem;
    }

    private void setData (Character[] newData)
    {
      mData = newData;
      ArrayList<ListDataListener> tmpList = new ArrayList<ListDataListener> (mDataListeners);
      for (ListDataListener listener : tmpList)
      {
        listener.contentsChanged (new ListDataEvent (this, ListDataEvent.CONTENTS_CHANGED, 0, mData.length));
      }
    }

    public Object getSelectedItem ()
    {
      return mSelectedItem;
    }

    public void setSelectedItem (Object selectedItem)
    {
      if (selectedItem != null && selectedItem instanceof Character)
        mMouseSelectedItem = (Character) selectedItem;
      else
        mMouseSelectedItem = null;
    }

    public void setIPASelectedIndex (int index)
    {
      if (mData.length == 0 || index == -1)
      {
        mSelectedItem = null;
        mSelectedIndex = -1;
      } else
      {
        assert index < mData.length && index >= 0 : String.format ("index is %d", index);
        mSelectedItem = mData[index];
        mSelectedIndex = index;
      }
    }

    public int getIPASelectedIndex ()
    {
      return mSelectedIndex;
    }

    public void addListDataListener (ListDataListener listener)
    {
      mDataListeners.add (listener);
    }

    public Object getElementAt (int pos)
    {
      assert pos < mData.length;
      return mData[pos];
    }

    public int getSize ()
    {
      return mData.length;
    }

    public void removeListDataListener (ListDataListener listener)
    {
      mDataListeners.remove (listener);
    }
  }

  private class AugmentedTypingTextFieldEditor implements ComboBoxEditor
  {
    JTextField mTextField;

    public AugmentedTypingTextFieldEditor ()
    {
      mTextField = new JTextField ();
    }

    /**
     * Exchanges the character at the caret position with the provided new character.
     * 
     * @param newChar
     *          a character that is used to replace the character at the caret position
     */
    public void swapChar (Character newChar)
    {
      int caretPos = mTextField.getCaretPosition ();
      if (caretPos == 0)
        return;
      String text = mTextField.getText ();
      mTextField.setText (String.format ("%s%c%s", text.substring (0, caretPos - 1), newChar,
          text.substring (caretPos, text.length ())));
      mTextField.setCaretPosition (caretPos);
    }

    public void addActionListener (ActionListener l)
    {
      mTextField.addActionListener (l);
    }

    public Component getEditorComponent ()
    {
      return mTextField;
    }

    public Object getItem ()
    {
      return mTextField.getText ();
    }

    public void removeActionListener (ActionListener l)
    {
      mTextField.removeActionListener (l);
    }

    public void selectAll ()
    {
      mTextField.selectAll ();
    }

    public void setItem (Object anObject)
    {
      // must be ignored
    }
  }
}
