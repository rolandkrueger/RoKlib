/*
 * $Id: $ Copyright (C) 2007 Roland Krueger Author: Roland Krueger (www.rolandkrueger.info) This
 * file is part of RoKlib. This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the Free Software
 * Foundation; either version 2.1 of the License, or (at your option) any later version. This
 * library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser
 * General Public License for more details. You should have received a copy of the GNU Lesser
 * General Public License along with this library; if not, write to the Free Software Foundation,
 * Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package info.rolandkrueger.roklib.util;

public abstract class AbstractCommand<T_ResultType> implements Runnable
{
  private EnhancedReturnType<T_ResultType> mResultValue;

  private boolean                          mWasExecuted;

  private boolean                          mExecutionCanceled;

  public AbstractCommand ()
  {
    mResultValue = null;
    mWasExecuted = false;
    mExecutionCanceled = false;
  }

  protected abstract EnhancedReturnType<T_ResultType> executeImpl ();

  public void run ()
  {
    mResultValue = null;
    EnhancedReturnType<T_ResultType> result = executeImpl ();
    if (result == null) 
    {
      throw new NullPointerException ("Return object returned from command implementation object is null."); 
    }

    mResultValue = result;
    mWasExecuted = true;
  }

  public boolean wasExecuted ()
  {
    return mWasExecuted;
  }

  public final EnhancedReturnType<T_ResultType> getResult ()
  {
    if (!wasExecuted ()) { throw new IllegalStateException ("Command was not yet executed. Call run() first."); }

    if (mResultValue == null) { throw new IllegalStateException ("Invalid result object: Subclass of "
        + AbstractCommand.class.getName () + " failed to correctly configure command result."); }

    return mResultValue;
  }

  public final T_ResultType getResultValue ()
  {
    return getResult ().getValue ();
  }

  public void cancel ()
  {
    mExecutionCanceled = true;
  }

  public boolean wasCanceled ()
  {
    return mExecutionCanceled;
  }
}
