package info.rolandkrueger.roklib.util;

import java.io.Serializable;

public interface IThreadLocalContainer extends Serializable
{
  public void setCurrentInstance (); 
  public void resetCurrentInstance ();
}
