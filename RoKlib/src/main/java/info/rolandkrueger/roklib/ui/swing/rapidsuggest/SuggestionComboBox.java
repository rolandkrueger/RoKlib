/*
 * $Id: SuggestionComboBox.java 260 2011-01-27 19:51:26Z roland $
 * Copyright (C) 2003 Roland Krueger
 * Created on Oct 15, 2003
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
package info.rolandkrueger.roklib.ui.swing.rapidsuggest;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.ComboBoxEditor;
import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.PlainDocument;
import javax.swing.text.SimpleAttributeSet;

/*
 * TODO: - ActionEvent ausloesen, wenn vorgeschlagenes Element ausgewaehlt wurde
 */

/**
 * @author Roland Krueger
 * @version $Id: SuggestionComboBox.java 260 2011-01-27 19:51:26Z roland $
 * 
 */
public class SuggestionComboBox extends JComboBox implements ActionListener
{
  private static final long serialVersionUID = - 5641621693220440350L;

  private SuggestionComboBoxModel mModel;
  private SuggestionComboBoxEditor mEditor;
  private Color mHighligerColor;

  public SuggestionComboBox ()
  {
    super (new SuggestionComboBoxModel ());
    init (new SuggestionComboBoxEditor (this), (SuggestionComboBoxModel) getModel ());
  }
  
  public SuggestionComboBox (SuggestionComboBoxModel model)
  {
    super (model);
    init (new SuggestionComboBoxEditor (this), model);
  }

  public SuggestionComboBox (SuggestionComboBoxModel model, Color highlighterColor)
  {
    super (model);
    mHighligerColor = highlighterColor;
    init (new SuggestionComboBoxEditor (this, highlighterColor), model);
  }
  
  private void init (SuggestionComboBoxEditor editor, SuggestionComboBoxModel model)
  {
    mModel = model;
    setEditor (mEditor);
    setEditable (true);
    addActionListener (this);
  }
  
  @Override
  public void setModel (ComboBoxModel aModel)
  {
    setEditor (new SuggestionComboBoxEditor (this, mHighligerColor));
    super.setModel (aModel);
  }
  
  @Override
  public void setEditor (ComboBoxEditor editor)
  {
    super.setEditor (editor);
    mEditor = (SuggestionComboBoxEditor) editor;    
  }
  
  public void actionPerformed (ActionEvent e)
  {
    if (e.getSource () == this) return;
    hidePopup ();
    mEditor.removeHighlights ();
  }

  public boolean containsItem (Object item)
  {
    return mModel.contains (item);
  }

  public SuggestionEditorComponent getEditorComponent ()
  {
    return mEditor.getEditor ();
  }

  /**
   * Editor class for the {@link SuggestionComboBox}.
   * 
   * @author Roland Krueger
   */
  private class SuggestionComboBoxEditor implements ComboBoxEditor
  {
    private SuggestionEditorComponent editor;

    public SuggestionComboBoxEditor (SuggestionComboBox parent)
    {
      editor = new SuggestionEditorComponent (parent);
    }

    public SuggestionComboBoxEditor (SuggestionComboBox parent, Color highlighterColor)
    {
      editor = new SuggestionEditorComponent (parent, highlighterColor);
    }

    /**
     * Selects the entered text.
     * 
     * @see javax.swing.ComboBoxEditor#selectAll()
     */
    public void selectAll ()
    {
      editor.selectAll ();
    }

    /**
     * Returns the text field.
     * 
     * @see javax.swing.ComboBoxEditor#getEditorComponent()
     */
    public Component getEditorComponent ()
    {
      return editor;
    }

    public SuggestionEditorComponent getEditor ()
    {
      return editor;
    }

    /**
     * Delegates the passed ActionListener to the editor component.
     * 
     * @see javax.swing.ComboBoxEditor#addActionListener(java.awt.event.ActionListener)
     */
    public void addActionListener (ActionListener l)
    {
      editor.addActionListener (l);
    }

    /**
     * Removes the passed ActionListener from the editor component.
     * 
     * @see javax.swing.ComboBoxEditor#removeActionListener(java.awt.event.ActionListener)
     */
    public void removeActionListener (ActionListener l)
    {
      editor.removeActionListener (l);
    }

    /**
     * Returns the editor component's text.
     * 
     * @see javax.swing.ComboBoxEditor#getItem()
     */
    public Object getItem ()
    {
      return editor.getText ();
    }

    /**
     * Sets the editor component's text.
     * 
     * @see javax.swing.ComboBoxEditor#setItem(java.lang.Object)
     */
    public void setItem (Object anObject)
    {
      // TODO: implement (?)
    }

    public void removeHighlights ()
    {
      editor.removeAllHighlights ();
    }
  } // End of private class SuggestionComboBoxEditor

  private class SuggestionEditorComponent extends JTextField implements MouseMotionListener,
      MouseListener, KeyListener
  {
    private static final long serialVersionUID = 8515002528978775937L;

    private AutoCompletionDocument doc;

    public SuggestionEditorComponent (SuggestionComboBox parent)
    {
      super ();
      this.addMouseMotionListener (this);
      this.addMouseListener (this);
      this.addKeyListener (this);
      doc = new AutoCompletionDocument (parent, this);
      setDocument (doc);
    }

    public SuggestionEditorComponent (SuggestionComboBox parent, Color highlighterColor)
    {
      super ();
      this.addMouseMotionListener (this);
      this.addMouseListener (this);
      this.addKeyListener (this);
      doc = new AutoCompletionDocument (parent, this, highlighterColor);
      setDocument (doc);
    }

    public void removeAllHighlights ()
    {
      doc.removeAllHighlights ();
    }

    public void mouseDragged (MouseEvent e)
    {
      doc.removeSuggestionHighlight ();
    }

    public void mouseClicked (MouseEvent e)
    {
      doc.removeSuggestionHighlight ();
    }

    public void keyPressed (KeyEvent e)
    {
      switch (e.getKeyCode ())
      {
      case KeyEvent.VK_LEFT:
      case KeyEvent.VK_RIGHT:
      case KeyEvent.VK_KP_LEFT:
      case KeyEvent.VK_KP_RIGHT:
      case KeyEvent.VK_HOME:
      case KeyEvent.VK_END:
        doc.removeSuggestionHighlight ();
      default:
        // ignore all other keys
      }
    }

    /** * UNUSED ** */
    public void keyTyped (KeyEvent e)
    {
    }

    /** * UNUSED ** */
    public void keyReleased (KeyEvent e)
    {
    }

    /** * UNUSED ** */
    public void mouseEntered (MouseEvent e)
    {
    }

    /** * UNUSED ** */
    public void mouseExited (MouseEvent e)
    {
    }

    /** * UNUSED ** */
    public void mousePressed (MouseEvent e)
    {
    }

    /** * UNUSED ** */
    public void mouseReleased (MouseEvent e)
    {
    }

    /** * UNUSED ** */
    public void mouseMoved (MouseEvent e)
    {
    }

    private class AutoCompletionDocument extends PlainDocument implements ItemListener
    {
      private static final long serialVersionUID = - 2716231755948898477L;

      private SuggestionComboBox mParent;
      private SuggestionComboBoxModel model;
      private SuggestionEditorComponent component;
      private int selectionStartIndex;
      private Object highlight;
      private Color highlighterColor;

      public AutoCompletionDocument (SuggestionComboBox mother, SuggestionEditorComponent component)
      {
        this (mother, component, new Color (153, 153, 204));
      }

      public AutoCompletionDocument (SuggestionComboBox parent,
          SuggestionEditorComponent component, Color highlighterColor)
      {
        this.mParent = parent;
        model = (SuggestionComboBoxModel) parent.getModel ();
        this.component = component;
        parent.addItemListener (this);
        selectionStartIndex = 0;
        this.highlighterColor = highlighterColor;
      }

      public void insertString (int offs, String str, AttributeSet a) throws BadLocationException
      {
        // System.out.println("insert: "+str);
        int caretPos = getCaretPosition () + str.length ();
        if (model == null)
          super.insertString (offs, str, a);
        else
        {
          super.insertString (offs, str, a);
          selectionStartIndex += str.length ();
          // System.out.println("prefix:"+getContent().getString(0,
          // selectionStartIndex));
          fetchSuggestion (getContent ().getString (0, selectionStartIndex), a, caretPos);
        }
      }

      public void remove (int offs, int len) throws BadLocationException
      {
        // System.out.println("remove! offs: "+offs+" len: "+len+" index: "+selectionStartIndex);
        super.remove (offs, len);
        if (offs < selectionStartIndex)
        {
          selectionStartIndex -= len;
          fetchSuggestion (getContent ().getString (0, selectionStartIndex),
              new SimpleAttributeSet (), getCaretPosition ());
        } else
        {
          component.getHighlighter ().removeAllHighlights ();
          super.remove (offs, getLength () - offs);
        }
        if (offs > selectionStartIndex) selectionStartIndex = getLength ();
      }

      private void fetchSuggestion (String prefix, AttributeSet a, int caretPos)
          throws BadLocationException
      {
        // System.out.println("prefix: "+prefix);
        String sugg = model.getSuggestion (getContent ().getString (0, selectionStartIndex));
        if (sugg != null)
        {
          super.remove (0, getLength ());
          super.insertString (0, prefix, a);
          super.insertString (selectionStartIndex, sugg.substring (selectionStartIndex), a);
          component.getHighlighter ().removeAllHighlights ();
          if (selectionStartIndex < getLength ())
            highlight = component.getHighlighter ().addHighlight (selectionStartIndex,
                getLength (), new DefaultHighlighter.DefaultHighlightPainter (highlighterColor));
          component.setCaretPosition (caretPos);
          mParent.setPopupVisible (true);
        } else
        {
          super.remove (0, getLength ());
          super.insertString (0, prefix, a);
          component.getHighlighter ().removeAllHighlights ();
          component.setCaretPosition (caretPos);
        }
        // System.out.println("suggest:"+sugg);
      }

      public void itemStateChanged (ItemEvent e)
      {
        if (e.getStateChange () == ItemEvent.DESELECTED) return;
        String selItem = (String) mParent.getSelectedItem ();
        // System.out.println("itemStateChanged: "+selItem);
        try
        {
          super.remove (0, getLength ());
          // System.out.println(selItem);
          super.insertString (0, selItem, new SimpleAttributeSet ());
          // System.out.println("after insert: "+getContent().getString(0,
          // getLength()));
        } catch (BadLocationException blExc)
        {
        }
        selectionStartIndex = selItem.length ();
      }

      public void removeAllHighlights ()
      {
        component.getHighlighter ().removeAllHighlights ();
        component.setCaretPosition (getLength ());
        selectionStartIndex = getLength ();
      }

      public void removeSuggestionHighlight ()
      {
        if (highlight == null) return;
        component.getHighlighter ().removeHighlight (highlight);
        selectionStartIndex = getLength ();
      }

    } // End of private class AutoCompletionDocument
  } // End of private class SuggestionEditorComponent
} // End of class SuggestionComboBox

