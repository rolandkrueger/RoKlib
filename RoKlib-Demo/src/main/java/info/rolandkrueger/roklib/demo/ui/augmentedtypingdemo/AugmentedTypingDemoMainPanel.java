package info.rolandkrueger.roklib.demo.ui.augmentedtypingdemo;

import info.rolandkrueger.roklib.demo.RoKlibSwingDemo;
import info.rolandkrueger.roklib.ui.swing.augmentedtyping.AugmentedTypingTextField;
import info.rolandkrueger.roklib.util.resources.ResourceHandle;
import info.rolandkrueger.roklib.util.resources.ResourceHandlingManager;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

public class AugmentedTypingDemoMainPanel extends JPanel
{
  private static final long serialVersionUID = 1032156176864212670L;
  private JPanel panel1 = null;
  private AugmentedTypingTextField augmentedTypingTextField = null;
  private JPanel upperPanel = null;
  private JPanel panel2 = null;
  private AugmentedTypingTextField germanAugmentedTypingTextField = null;
  private Font font;
  private JPanel panel3 = null;
  private AugmentedTypingTextField frenchAugmentedTypingTextField = null;

  public AugmentedTypingDemoMainPanel ()
  {
    super ();
    initialize ();
    try
    {
      font = loadDejaVuFont ();
      augmentedTypingTextField.setFont (font);
      germanAugmentedTypingTextField.setFont (font);
      frenchAugmentedTypingTextField.setFont (font);
    } catch (Exception e)
    {
      // nothing to do here, if font loading fails, use the default
    }
  }

  private void initialize ()
  {
    this.setLayout(new BorderLayout());
    this.setSize (300, 200);
    this.add(getUpperPanel(), BorderLayout.NORTH);
  }

  private JPanel getPanel1 ()
  {
    if (panel1 == null)
    {
      GridLayout gridLayout1 = new GridLayout();
      gridLayout1.setRows(1);
      panel1 = new JPanel ();
      panel1.setBorder(BorderFactory.createTitledBorder(null, "Type IPA phonetic characters:", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.ABOVE_TOP, new Font("sansserif", Font.BOLD, 12), new Color(59, 59, 59)));
      panel1.setLayout(gridLayout1);
      panel1.add(getAugmentedTypingTextField(), null);
    }
    return panel1;
  }

  private AugmentedTypingTextField getAugmentedTypingTextField ()
  {
    if (augmentedTypingTextField == null)
    {
      augmentedTypingTextField = new AugmentedTypingTextField ();
    }
    return augmentedTypingTextField;
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
      upperPanel.add(getPanel1(), null);
      upperPanel.add(getPanel2(), null);
      upperPanel.add(getPanel3(), null);
    }
    return upperPanel;
  }
  
  private Font loadDejaVuFont () throws Exception
  {
    ResourceHandle fontHandle = ResourceHandlingManager.instance ().registerResource (RoKlibSwingDemo.class, "DejaVuSans.ttf");
    if (fontHandle == null)
    {
      throw new IllegalStateException ("Unable to load DejaVu TTF font.");
    }
    return Font.createFont (Font.TRUETYPE_FONT, fontHandle.getResourceData ()).deriveFont (20.0f);
  }

  private JPanel getPanel2 ()
  {
    if (panel2 == null)
    {
      GridLayout gridLayout2 = new GridLayout();
      gridLayout2.setRows(1);
      panel2 = new JPanel ();
      panel2.setLayout(gridLayout2);
      panel2.setBorder(BorderFactory.createTitledBorder(null, "Type German Umlauts (try using one of [aoubsAOUB]):", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.ABOVE_TOP, new Font("sansserif", Font.BOLD, 12), new Color(59, 59, 59)));
      panel2.add(getGermanAugmentedTypingTextField(), null);
    }
    return panel2;
  }

  private AugmentedTypingTextField getGermanAugmentedTypingTextField ()
  {
    if (germanAugmentedTypingTextField == null)
    {
      germanAugmentedTypingTextField = new AugmentedTypingTextField (new GermanKeyMapping ());
    }
    return germanAugmentedTypingTextField;
  }

  private JPanel getPanel3 ()
  {
    if (panel3 == null)
    {
      GridLayout gridLayout3 = new GridLayout();
      gridLayout3.setRows(1);
      panel3 = new JPanel ();
      panel3.setLayout(gridLayout3);
      panel3.setBorder(BorderFactory.createTitledBorder(null, "Type French special characters (try [aeiuoc><yAEIOUYC])", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.ABOVE_TOP, new Font("sansserif", Font.BOLD, 12), new Color(59, 59, 59)));
      panel3.add(getFrenchAugmentedTypingTextField(), null);
    }
    return panel3;
  }

  private AugmentedTypingTextField getFrenchAugmentedTypingTextField ()
  {
    if (frenchAugmentedTypingTextField == null)
    {
      frenchAugmentedTypingTextField = new AugmentedTypingTextField (new FrenchKeyMappings ());
    }
    return frenchAugmentedTypingTextField;
  }
}
