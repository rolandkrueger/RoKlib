package info.rolandkrueger.roklib.demo.ui.tstdemo;

import info.rolandkrueger.roklib.demo.RoKlibSwingDemo;
import info.rolandkrueger.roklib.ui.swing.StatusBar;
import info.rolandkrueger.roklib.util.helper.StopWatch;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;
import java.awt.GridBagLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;

import javax.swing.JCheckBox;
import javax.swing.border.EtchedBorder;

public class TernarySearchTreeDemoMainPanel extends JPanel implements ActionListener
{
  private static final long serialVersionUID = 681139435259648013L;
  
  private RoKlibSwingDemo parent;
  private JPanel listPanel = null;
  private JScrollPane listScrollPane = null;
  private JTextArea listTextArea = null;
  private JPanel inputPanel = null;
  private JPanel caseSensitiveInputPanel = null;
  private TSTDemoInputPanel caseSensitivePrefixMatchingInputPanel = null;
  private TSTDemoInputPanel caseSensitiveSpellCheckingInputPanel = null;
  private TSTDemoInputPanel caseInsensitivePrefixMatchingInputPanel = null;
  private TSTDemoInputPanel caseInsensitiveSpellCheckingInputPanel = null;
  private JPanel caseInsensitiveInputPanel = null;
  private String statusMessage;
  private String completeDictionary;
  private StatusBar statusBar = null;
  private JPanel switchPanel = null;
  private JCheckBox switchOffCheckBox = null;
  private JPanel lowerPanel = null;
 
  public TernarySearchTreeDemoMainPanel ()
  {
    super ();
    initialize ();
  }

  public void setParentDemo (RoKlibSwingDemo demo)
  {
    parent = demo;
    createStatusMessage (parent.getDictionaryData ().size (), -1);
    StringBuilder buf = new StringBuilder ();
    for (CharSequence string : parent.getDictionaryData ())
    {
      buf.append (string).append ('\n');
    }
    completeDictionary = buf.toString ();
    listTextArea.setText (completeDictionary);
  }
  
  private void initialize ()
  {
    this.setLayout(new BorderLayout());
    this.setSize (356, 251);
    this.add(getInputPanel(), BorderLayout.NORTH);
    this.add(getLowerPanel(), BorderLayout.CENTER);
  }

  private JPanel getListPanel ()
  {
    if (listPanel == null)
    {
      listPanel = new JPanel ();
      listPanel.setLayout(new BorderLayout());
      listPanel.add(getListScrollPane(), BorderLayout.CENTER);
    }
    return listPanel;
  }

  private JScrollPane getListScrollPane ()
  {
    if (listScrollPane == null)
    {
      listScrollPane = new JScrollPane ();
      listScrollPane.setViewportView(getListTextArea());
    }
    return listScrollPane;
  }

  private JTextArea getListTextArea ()
  {
    if (listTextArea == null)
    {
      listTextArea = new JTextArea ();
      listTextArea.setEditable(false);
    }
    return listTextArea;
  }

  private JPanel getInputPanel ()
  {
    if (inputPanel == null)
    {
      GridLayout gridLayout = new GridLayout();
      gridLayout.setRows(1);
      gridLayout.setColumns(2);
      inputPanel = new JPanel ();
      inputPanel.setLayout(gridLayout);
      inputPanel.add(getCaseSensitiveInputPanel(), null);
      inputPanel.add(getCaseInsensitiveInputPanel(), null);
    }
    return inputPanel;
  }

  private JPanel getCaseSensitiveInputPanel ()
  {
    if (caseSensitiveInputPanel == null)
    {
      GridLayout gridLayout1 = new GridLayout();
      gridLayout1.setColumns(1);
      gridLayout1.setRows(0);
      caseSensitiveInputPanel = new JPanel ();
      caseSensitiveInputPanel.setLayout(gridLayout1);
      caseSensitiveInputPanel.setBorder(BorderFactory.createTitledBorder(null, "Case Sensitive Tree", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.ABOVE_TOP, new Font("sansserif", Font.BOLD, 12), new Color(59, 59, 59)));
      caseSensitiveInputPanel.add(getCaseSensitivePrefixMatchingInputPanel(), null);
      caseSensitiveInputPanel.add(getCaseSensitiveSpellCheckingInputPanel(), null);
    }
    return caseSensitiveInputPanel;
  }
  
  private TSTDemoInputPanel getCaseSensitiveSpellCheckingInputPanel ()
  {
    if (caseSensitiveSpellCheckingInputPanel == null)
    {
      caseSensitiveSpellCheckingInputPanel = new TSTDemoInputPanel ();
      caseSensitiveSpellCheckingInputPanel.setParent (this);
      caseSensitiveSpellCheckingInputPanel.setBorderText ("Spell Checking");
      caseSensitiveSpellCheckingInputPanel.setLabelText ("Enter some incorrectly spelled word:");
    }
    return caseSensitiveSpellCheckingInputPanel;
  }
  
  private TSTDemoInputPanel getCaseSensitivePrefixMatchingInputPanel ()
  {
    if (caseSensitivePrefixMatchingInputPanel == null)
    {
      caseSensitivePrefixMatchingInputPanel = new TSTDemoInputPanel ();
      caseSensitivePrefixMatchingInputPanel.setParent (this);
      caseSensitivePrefixMatchingInputPanel.setBorderText ("Prefix Matching");
      caseSensitivePrefixMatchingInputPanel.setLabelText ("Enter some prefix:");
    }
    return caseSensitivePrefixMatchingInputPanel;
  }
  
  private TSTDemoInputPanel getCaseInsensitivePrefixMatchingInputPanel ()
  {
    if (caseInsensitivePrefixMatchingInputPanel == null)
    {
      caseInsensitivePrefixMatchingInputPanel = new TSTDemoInputPanel ();
      caseInsensitivePrefixMatchingInputPanel.setParent (this);
      caseInsensitivePrefixMatchingInputPanel.setBorderText ("Prefix Matching");
//      caseInsensitivePrefixMatchingInputPanel.setLabelText ("Enter some prefix with mixed case:");
      caseInsensitivePrefixMatchingInputPanel.setLabelText ("Use mixed case:");
    }
    return caseInsensitivePrefixMatchingInputPanel;
  }
  
  private TSTDemoInputPanel getCaseInsensitiveSpellCheckingInputPanel ()
  {
    if (caseInsensitiveSpellCheckingInputPanel == null)
    {
      caseInsensitiveSpellCheckingInputPanel = new TSTDemoInputPanel ();
      caseInsensitiveSpellCheckingInputPanel.setParent (this);
      caseInsensitiveSpellCheckingInputPanel.setBorderText ("Spell Checking");
//      caseInsensitiveSpellCheckingInputPanel.setLabelText ("Enter some incorrectly spelled word with mixed case:");
      caseInsensitiveSpellCheckingInputPanel.setLabelText ("Use mixed case:");
    }
    return caseInsensitiveSpellCheckingInputPanel;
  }

  private JPanel getCaseInsensitiveInputPanel ()
  {
    if (caseInsensitiveInputPanel == null)
    {
      GridLayout gridLayout2 = new GridLayout();
      gridLayout2.setColumns(1);
      gridLayout2.setRows(0);
      caseInsensitiveInputPanel = new JPanel ();
      caseInsensitiveInputPanel.setLayout(gridLayout2);
      caseInsensitiveInputPanel.setBorder(BorderFactory.createTitledBorder(null, "Case Insensitive Tree", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.ABOVE_TOP, new Font("sansserif", Font.BOLD, 12), new Color(59, 59, 59)));
      caseInsensitiveInputPanel.add(getCaseInsensitivePrefixMatchingInputPanel(), null);
      caseInsensitiveInputPanel.add(getCaseInsensitiveSpellCheckingInputPanel(), null);
    }
    return caseInsensitiveInputPanel;
  }
  
  private void setListData (Iterable<CharSequence> dataIterable)
  {
    StringBuilder buf = new StringBuilder (10000);
   
    int count = 0;
    StopWatch stopWatch = new StopWatch ();
    stopWatch.start ();    
    for (CharSequence string : dataIterable)
    {
      buf.append (string).append ('\n');
      count++;
    }
    stopWatch.stop ();
    listTextArea.setText (buf.toString ());
    createStatusMessage (count, stopWatch.getTotalTime ());
  }
  
  private void prefixMatchWithStartsWith (boolean caseSensitive, String prefix)
  {
    StringBuilder buf = new StringBuilder (10000);
    int count = 0;
    StopWatch stopWatch = new StopWatch ();
    stopWatch.start ();
    if (! caseSensitive)
    {
      prefix = prefix.toLowerCase ();
    }
    for (CharSequence charSeq : parent.getDictionaryData ())
    {
      String string = charSeq.toString ();
      if ( ! caseSensitive)
      {
        string = string.toLowerCase ();
      }
      if (string.startsWith (prefix))
      {
        buf.append (string).append ('\n');
        count++;
      } else if (buf.length () > 0)
      {
        break;
      }
    }    
    stopWatch.stop ();
    listTextArea.setText (buf.toString ());
    createStatusMessage (count, stopWatch.getTotalTime ());
  }
  
  public void userInputChanged (String input, TSTDemoInputPanel source)
  {
    if ("".equals (input.trim ())) 
    {
      listTextArea.setText (completeDictionary);
      createStatusMessage (parent.getDictionaryData ().size (), -1);
      return;
    }
    if (source == caseSensitivePrefixMatchingInputPanel)
    {
      if (switchOffCheckBox.isSelected ())
      {
        prefixMatchWithStartsWith (true, input.trim ()); 
      } else
      {        
        setListData (parent.getDictionaryData ().getPrefixMatch (input));
      }
    }
    if (source == caseSensitiveSpellCheckingInputPanel)
    {
      setListData (parent.getDictionaryData ().matchAlmost (input, 2, 2));
    }
    if (source == caseInsensitivePrefixMatchingInputPanel)
    {
      if (switchOffCheckBox.isSelected ()) 
      {
        prefixMatchWithStartsWith (false, input.trim ());
      } else
      {
        setListData (parent.getDictionaryDataCaseInsensitive ().getPrefixMatch (input));
      }
    }
    if (source == caseInsensitiveSpellCheckingInputPanel)
    {
      setListData (parent.getDictionaryDataCaseInsensitive ().matchAlmost (input, 2, 2));
    }
  }
  
  private void createStatusMessage (int numberOfItemsInList, long filterTime)
  {
    statusMessage = String.format ("%d items in list. %s", numberOfItemsInList,
        filterTime < 0 ? "" : "List filtered in " + filterTime + " ms.");    
    statusBar.setReadyMessage (statusMessage);
  }

  private StatusBar getStatusBar ()
  {
    if (statusBar == null)
    {
      statusBar = new StatusBar ();
      statusBar.setReadyMessage ("Loading...");
    }
    return statusBar;
  }

  private JPanel getSwitchPanel ()
  {
    if (switchPanel == null)
    {
      FlowLayout flowLayout = new FlowLayout();
      flowLayout.setAlignment(FlowLayout.LEFT);
      switchPanel = new JPanel ();
      switchPanel.setLayout(flowLayout);
      switchPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
      switchPanel.add(getSwitchOffCheckBox(), null);
    }
    return switchPanel;
  }

  private JCheckBox getSwitchOffCheckBox ()
  {
    if (switchOffCheckBox == null)
    {
      switchOffCheckBox = new JCheckBox ();
      switchOffCheckBox.setText("Use startsWith() prefix matching");
      switchOffCheckBox.addActionListener (this);
    }
    return switchOffCheckBox;
  }

  /**
   * This method initializes lowerPanel	
   * 	
   * @return javax.swing.JPanel	
   */
  private JPanel getLowerPanel ()
  {
    if (lowerPanel == null)
    {
      lowerPanel = new JPanel ();
      lowerPanel.setLayout(new BorderLayout());
      lowerPanel.add(getSwitchPanel(), BorderLayout.NORTH);
      lowerPanel.add(getStatusBar(), BorderLayout.SOUTH);
      lowerPanel.add(getListPanel(), BorderLayout.CENTER);
    }
    return lowerPanel;
  }

  @Override
  public void actionPerformed (ActionEvent event)
  {
    caseSensitiveSpellCheckingInputPanel.setEnabled (!switchOffCheckBox.isSelected ());
    caseInsensitiveSpellCheckingInputPanel.setEnabled (!switchOffCheckBox.isSelected ());
  }

}  //  @jve:decl-index=0:visual-constraint="10,10"
