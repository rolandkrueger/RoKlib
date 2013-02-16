package info.rolandkrueger.roklib.demo.ui.augmentedtypingdemo;

import info.rolandkrueger.roklib.demo.ui.DemoPanel;

public class AugmentedTypingDemoPanel extends DemoPanel
{
  private static final long serialVersionUID = - 3507687667891631245L;

  private AugmentedTypingDemoMainPanel mMainPanel;
  
  public AugmentedTypingDemoPanel() {
  	super();
  	mMainPanel = new AugmentedTypingDemoMainPanel ();
  	setDemoComponentPanel (mMainPanel);
  }
}
