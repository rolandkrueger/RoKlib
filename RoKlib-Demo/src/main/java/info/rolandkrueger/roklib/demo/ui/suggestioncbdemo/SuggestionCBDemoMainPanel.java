package info.rolandkrueger.roklib.demo.ui.suggestioncbdemo;

import info.rolandkrueger.roklib.demo.RoKlibSwingDemo;

import java.awt.GridBagLayout;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import javax.swing.BorderFactory;
import javax.swing.border.TitledBorder;
import java.awt.Font;
import java.awt.Color;
import info.rolandkrueger.roklib.ui.swing.rapidsuggest.SuggestionComboBox;
import info.rolandkrueger.roklib.ui.swing.rapidsuggest.SuggestionComboBoxModel;

public class SuggestionCBDemoMainPanel extends JPanel
{
  private static final long serialVersionUID = - 8582957742074841200L;
  private RoKlibSwingDemo parent;
  private JPanel upperPanel = null;
  private JPanel suggestionCBPanel = null;
  private SuggestionComboBox suggestionComboBox = null;

  public SuggestionCBDemoMainPanel ()
  {
    super ();
    initialize ();
  }

  public void setParentDemo (RoKlibSwingDemo parent)
  {
    this.parent = parent;
//    getSuggestionComboBox ().setModel (new SuggestionComboBoxModel (parent.getDictionaryDataCaseInsensitive ()));
  }
  
  private void initialize ()
  {
    this.setSize (300, 200);
    this.setLayout (new BorderLayout());
    this.add(getUpperPanel(), BorderLayout.NORTH);
  }

  private JPanel getUpperPanel ()
  {
    if (upperPanel == null)
    {
      GridLayout gridLayout = new GridLayout();
      gridLayout.setColumns(1);
      gridLayout.setRows(0);
      upperPanel = new JPanel ();
      upperPanel.setLayout(gridLayout);
      upperPanel.add(getSuggestionCBPanel(), null);
    }
    return upperPanel;
  }

  private JPanel getSuggestionCBPanel ()
  {
    if (suggestionCBPanel == null)
    {
      GridLayout gridLayout1 = new GridLayout();
      gridLayout1.setRows(1);
      suggestionCBPanel = new JPanel ();
      suggestionCBPanel.setLayout(gridLayout1);
      suggestionCBPanel.setBorder(BorderFactory.createTitledBorder(null, "Enter some text:", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.ABOVE_TOP, new Font("sansserif", Font.BOLD, 12), new Color(59, 59, 59)));
      suggestionCBPanel.add(getSuggestionComboBox(), null);
    }
    return suggestionCBPanel;
  }

  private SuggestionComboBox getSuggestionComboBox ()
  {
    if (suggestionComboBox == null)
    {
      suggestionComboBox = new SuggestionComboBox ();
    }
    return suggestionComboBox;
  }

}
