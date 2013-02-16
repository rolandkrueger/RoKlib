package info.rolandkrueger.roklib.demo.ui.augmentedtypingdemo;

import java.awt.Font;

import info.rolandkrueger.roklib.demo.AbstractDemo;
import info.rolandkrueger.roklib.demo.ui.DemoPanel;

public class AugmentedTypingDemo extends AbstractDemo
{
  private AugmentedTypingDemoPanel mPanel;
  
  @Override
  protected DemoPanel getPanelImpl ()
  {
    mPanel = new AugmentedTypingDemoPanel ();
    return mPanel;
  }

  @Override
  public String getName ()
  {
    return "Augmented Typing";
  }
  
  public AugmentedTypingDemo setFont (Font font)
  {
    mPanel.setFont (font);
    return this;
  }
}
