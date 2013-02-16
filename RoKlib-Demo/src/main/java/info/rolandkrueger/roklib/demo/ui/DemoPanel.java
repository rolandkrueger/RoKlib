package info.rolandkrueger.roklib.demo.ui;

import info.rolandkrueger.roklib.util.helper.CheckForNull;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

public class DemoPanel extends JPanel
{
  private static final long serialVersionUID = - 7686054141734620094L;
  
  private JSplitPane splitPane = null;
  private JScrollPane leftScrollPane = null;
  private JEditorPane textViewer = null;

  public DemoPanel ()
  {
    super ();
    initialize ();
  }
  
  public void setDemoComponentPanel (JPanel demoComponentPanel)
  {
    CheckForNull.check (demoComponentPanel);
    splitPane.setRightComponent(demoComponentPanel);    
  }
  
  public void setViewerText (String text)
  {
    getTextViewer ().setText (text);
  }

  private void initialize ()
  {
    this.setLayout (new BorderLayout());
    this.setSize(new Dimension(409, 308));
    this.add(getSplitPane(), BorderLayout.CENTER);
  }

  private JSplitPane getSplitPane ()
  {
    if (splitPane == null)
    {
      splitPane = new JSplitPane ();
      splitPane.setDividerLocation(300);
      splitPane.setResizeWeight(0.0D);
      splitPane.setLeftComponent(getLeftScrollPane());
    }
    return splitPane;
  }

  private JScrollPane getLeftScrollPane ()
  {
    if (leftScrollPane == null)
    {
      leftScrollPane = new JScrollPane ();
      leftScrollPane.setViewportView(getTextViewer());
    }
    return leftScrollPane;
  }

  private JEditorPane getTextViewer ()
  {
    if (textViewer == null)
    {
      textViewer = new JEditorPane ();
      textViewer.setEditable(false);
      textViewer.setContentType("text/html");
    }
    return textViewer;
  }


}  //  @jve:decl-index=0:visual-constraint="10,10"
