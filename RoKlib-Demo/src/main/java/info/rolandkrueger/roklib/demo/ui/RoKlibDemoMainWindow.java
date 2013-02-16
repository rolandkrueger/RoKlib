package info.rolandkrueger.roklib.demo.ui;

import info.rolandkrueger.roklib.demo.AbstractDemo;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.ProgressMonitor;

public class RoKlibDemoMainWindow extends JFrame
{
  private static final long serialVersionUID = - 278979597178687034L;
  
  private JPanel jContentPane = null;
  private JTabbedPane tabbedPane = null;
  private ProgressMonitor progressMonitor;

  public RoKlibDemoMainWindow ()
  {
    super ();
    initialize ();
  }

  private void initialize ()
  {
    this.setSize (821, 733);
    this.setContentPane (getJContentPane ());
    this.setTitle ("RoKlib Library Demonstration");
  }
  
  public void startProgressMonitor (int maximum, String message)
  {
    progressMonitor = new ProgressMonitor (this, message, "", 0, maximum);
    progressMonitor.setMillisToPopup (0);
  }
  
  public void setProgress (int progress)
  {
    if (progressMonitor == null)
    {
      throw new IllegalStateException ("Progress monitor hasn't been started yet.");
    }
    progressMonitor.setProgress (progress);
  }

  public void closeProgressMonitor ()
  {
    if (progressMonitor == null)
    {
      throw new IllegalStateException ("Progress monitor hasn't been started yet.");
    }
    progressMonitor.close ();
    progressMonitor = null;
  }
  
  private JPanel getJContentPane ()
  {
    if (jContentPane == null)
    {
      jContentPane = new JPanel ();
      jContentPane.setLayout (new BorderLayout ());
      jContentPane.add(getTabbedPane(), BorderLayout.CENTER);
    }
    return jContentPane;
  }

  private JTabbedPane getTabbedPane ()
  {
    if (tabbedPane == null)
    {
      tabbedPane = new JTabbedPane ();
    }
    return tabbedPane;
  }
  
  public void addDemo (AbstractDemo demo)
  {
    getTabbedPane ().addTab (demo.getName (), demo.getPanel ());
  }
}  //  @jve:decl-index=0:visual-constraint="10,10"
