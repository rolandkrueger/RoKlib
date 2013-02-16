package info.rolandkrueger.roklib.demo.ui.tstdemo;

import info.rolandkrueger.roklib.demo.AbstractDemo;
import info.rolandkrueger.roklib.demo.RoKlibSwingDemo;
import info.rolandkrueger.roklib.demo.ui.DemoPanel;
import info.rolandkrueger.roklib.util.helper.CheckForNull;

public class TernarySearchTreeDemo extends AbstractDemo
{
  private TernarySearchTreeDemoPanel mPanel;
  private RoKlibSwingDemo mParent;
  
  public TernarySearchTreeDemo (RoKlibSwingDemo parent)
  {
    CheckForNull.check (parent);
    mParent = parent;
  }
  
  @Override
  protected DemoPanel getPanelImpl ()
  {
    mPanel = new TernarySearchTreeDemoPanel (mParent);
    return mPanel;    
  }

  @Override
  public String getName ()
  {
    return "Ternary Search Tree";
  }
}
