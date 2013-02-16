package info.rolandkrueger.roklib.demo.ui.tstdemo;

import info.rolandkrueger.roklib.demo.RoKlibSwingDemo;
import info.rolandkrueger.roklib.demo.ui.DemoPanel;
import info.rolandkrueger.roklib.util.helper.CheckForNull;

import java.awt.Dimension;

public class TernarySearchTreeDemoPanel extends DemoPanel
{
  private static final long serialVersionUID = - 5594613580089249041L;
  private TernarySearchTreeDemoMainPanel mainPanel;
  
  public TernarySearchTreeDemoPanel (RoKlibSwingDemo demo)
  {
    super ();
    CheckForNull.check (demo);
    initialize ();
    mainPanel.setParentDemo (demo);
  }

  private void initialize ()
  {
    this.setSize (new Dimension (411, 313));
    mainPanel = new TernarySearchTreeDemoMainPanel ();
    setDemoComponentPanel (mainPanel);
  }
  
}  //  @jve:decl-index=0:visual-constraint="40,28"
