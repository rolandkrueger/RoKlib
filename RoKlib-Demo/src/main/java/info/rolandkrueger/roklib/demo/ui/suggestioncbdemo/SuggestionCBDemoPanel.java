package info.rolandkrueger.roklib.demo.ui.suggestioncbdemo;

import info.rolandkrueger.roklib.demo.RoKlibSwingDemo;
import info.rolandkrueger.roklib.demo.ui.DemoPanel;

public class SuggestionCBDemoPanel extends DemoPanel
{
  private static final long serialVersionUID = 6320970106701757854L;

  public SuggestionCBDemoPanel (RoKlibSwingDemo parent)
  {
    SuggestionCBDemoMainPanel mainPanel = new SuggestionCBDemoMainPanel ();
    mainPanel.setParentDemo (parent);
    setDemoComponentPanel (mainPanel);
  }  
}
