package info.rolandkrueger.roklib.demo;

import info.rolandkrueger.roklib.demo.ui.DemoPanel;

public abstract class AbstractDemo
{
  private DemoPanel mDemoPanel;
  
  public AbstractDemo ()
  {
  }
  
  public DemoPanel getPanel ()
  {
    if (mDemoPanel == null)
    {
      mDemoPanel = getPanelImpl ();
      if (mDemoPanel == null)
      {
        throw new NullPointerException ("getPanel() returned null");
      }      
    }
    return mDemoPanel;
  }
  
  public void setDescriptionText (String text)
  {
    mDemoPanel.setViewerText (text);
  }
  
  protected abstract DemoPanel getPanelImpl ();
  public abstract String getName ();
}
