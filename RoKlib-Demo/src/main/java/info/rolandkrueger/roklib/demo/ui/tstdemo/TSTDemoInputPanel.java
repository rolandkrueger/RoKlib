package info.rolandkrueger.roklib.demo.ui.tstdemo;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

public class TSTDemoInputPanel extends JPanel implements CaretListener
{
  private static final long serialVersionUID = 650568530896999485L;
  private TernarySearchTreeDemoMainPanel parent;
  private TitledBorder border;
  private String currentInput = "";
  private JTextField textField = null;
  private JLabel label = null;
  public TSTDemoInputPanel ()
  {
    super ();
    initialize ();
  }

  private void initialize ()
  {

    GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
    gridBagConstraints1.fill = GridBagConstraints.BOTH;
    gridBagConstraints1.gridy = 2;
    gridBagConstraints1.weightx = 1.0;
    GridBagConstraints gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.anchor = GridBagConstraints.WEST;
    gridBagConstraints.gridy = 1;
    label = new JLabel();
    label.setText("JLabel");
    this.setLayout(new GridBagLayout());
    this.setSize (new Dimension (224, 197));
    this.add(label, gridBagConstraints);
    this.add(getTextField(), gridBagConstraints1);
  }
  
  public void setEnabled (boolean enabled)
  {
    textField.setEnabled (enabled);
  }

  public void setParent (TernarySearchTreeDemoMainPanel parent)
  {
    this.parent = parent;
  }

  public void setBorderText (String title)
  {
    this.setBorder (BorderFactory.createTitledBorder (null, title,
        TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.ABOVE_TOP, new Font ("sansserif",
            Font.BOLD, 12), new Color (59, 59, 59)));
  }

  public void setLabelText (String text)
  {
     label.setText (text);
  }
  
  private JTextField getTextField ()
  {
    if (textField == null)
    {
      textField = new JTextField ();
      textField.addCaretListener (this);
    }
    return textField;
  }

  @Override
  public void caretUpdate (CaretEvent e)
  {
    if (textField.getText ().equals (currentInput))
    {
      return;
    }
    currentInput = textField.getText ();
    parent.userInputChanged (currentInput, this);
  }

} // @jve:decl-index=0:visual-constraint="73,53"
