package info.rolandkrueger.roklib.demo.ui.suggestioncbdemo;

import info.rolandkrueger.roklib.demo.AbstractDemo;
import info.rolandkrueger.roklib.demo.RoKlibSwingDemo;
import info.rolandkrueger.roklib.demo.ui.DemoPanel;

public class SuggestionCBDemo extends AbstractDemo
{
  RoKlibSwingDemo mParent;
  
  public SuggestionCBDemo (RoKlibSwingDemo parent)
  {
    mParent = parent;
  }
  
  @Override
  protected DemoPanel getPanelImpl ()
  {
    return new SuggestionCBDemoPanel (mParent);
  }

  @Override
  public String getName ()
  {
    return "Suggestion ComboBox";
  }
}
