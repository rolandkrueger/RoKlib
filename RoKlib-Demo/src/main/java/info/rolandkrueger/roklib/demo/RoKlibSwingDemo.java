package info.rolandkrueger.roklib.demo;

import info.rolandkrueger.roklib.demo.ui.RoKlibDemoMainWindow;
import info.rolandkrueger.roklib.demo.ui.augmentedtypingdemo.AugmentedTypingDemo;
import info.rolandkrueger.roklib.demo.ui.suggestioncbdemo.SuggestionCBDemo;
import info.rolandkrueger.roklib.demo.ui.tstdemo.TernarySearchTreeDemo;
import info.rolandkrueger.roklib.io.StreamUtilities;
import info.rolandkrueger.roklib.util.TernarySearchTreeSet;
import info.rolandkrueger.roklib.util.resources.ResourceHandle;
import info.rolandkrueger.roklib.util.resources.ResourceHandlingManager;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

import javax.swing.UIManager;

public class RoKlibSwingDemo
{
  private RoKlibDemoMainWindow mMainWindow;
  private List<AbstractDemo> mDemos;
  private TernarySearchTreeSet mDictionaryData;
  private TernarySearchTreeSet mDictionaryDataCaseInsensitive;
  
  public static void main (String[] args) throws Exception
  {
    try
    {
      UIManager.setLookAndFeel ("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
    } catch (Exception e)
    {
      UIManager.setLookAndFeel (UIManager.getCrossPlatformLookAndFeelClassName ());
    } 
    RoKlibSwingDemo demo = new RoKlibSwingDemo ();
    try
    {
      demo.run ();
    } catch (Exception e)
    {
      e.printStackTrace ();
      System.exit (1);
    }    
  }
  
  private void loadDemo (AbstractDemo demo) throws IOException
  {
    String resourceName = "html/" + demo.getClass ().getSimpleName () + ".html";
    ResourceHandle descriptionHandle = ResourceHandlingManager.instance ().registerResource (RoKlibSwingDemo.class, resourceName);
    if (ResourceHandlingManager.instance ().areResourcesMissing ())
    {
      throw new IllegalStateException (
          String.format ("Unable to load demo %s. Description resource %s/%s not found.", 
              demo.getClass ().getSimpleName (),
              ResourceHandlingManager.instance ().getResourceLocation (),
              resourceName));
    }
    String descriptionText = StreamUtilities.getStreamAsString (descriptionHandle.getResourceData ());
    mMainWindow.addDemo (demo);
    demo.setDescriptionText (descriptionText);
  }
  
  private void run () throws Exception
  {
    mMainWindow = new RoKlibDemoMainWindow ();
    mMainWindow.addWindowListener (new WindowAdapter()
    {
      @Override
      public void windowClosing (WindowEvent e)
      {
        System.exit (0);
      }
    });
    loadEnglishDictionary ();
    mDemos = new LinkedList<AbstractDemo> ();
    mDemos.add (new TernarySearchTreeDemo (this));
//    mDemos.add (new SuggestionCBDemo (this));
    mDemos.add (new AugmentedTypingDemo ());
    
    for (AbstractDemo demo : mDemos)
    {      
      loadDemo (demo);
    }
    mMainWindow.setVisible (true);
  }
  
  private void loadEnglishDictionary () throws Exception
  {
    mDictionaryData = new TernarySearchTreeSet ();
    ResourceHandle dictionaryHandle = ResourceHandlingManager.instance ().registerResource (RoKlibSwingDemo.class, "en-GB.dic");
    if (dictionaryHandle == null)
    {
      throw new IllegalStateException ("Unable to load English dictionary.");
    }
    int dicionaryLength = 115500;
    mMainWindow.startProgressMonitor (dicionaryLength, "Loading dictionary data...");
    BufferedReader reader = new BufferedReader (new InputStreamReader (dictionaryHandle.getResourceData ()));
    String line = reader.readLine ();
    int count = 0;
    while (line != null)
    {
      mDictionaryData.add (line);
      count++;
      if (count % 100 == 0)
      {
        mMainWindow.setProgress (count);
      }
      line = reader.readLine ();
    }
    mMainWindow.closeProgressMonitor ();
    mDictionaryDataCaseInsensitive = new TernarySearchTreeSet (mDictionaryData, true);
  }
  
  public TernarySearchTreeSet getDictionaryData ()
  {
    return mDictionaryData;
  }
  
  public TernarySearchTreeSet getDictionaryDataCaseInsensitive ()
  {
    return mDictionaryDataCaseInsensitive;
  }
}




