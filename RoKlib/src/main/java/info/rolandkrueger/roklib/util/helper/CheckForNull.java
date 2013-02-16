/*
 * $Id: CheckForNull.java 178 2010-10-31 18:01:20Z roland $
 * Copyright (C) 2007 Roland Krueger
 * Created on 17.10.2009
 *
 * Author: Roland Krueger (www.rolandkrueger.info)
 *
 * This file is part of RoKlib.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307
 * USA
 */
package info.rolandkrueger.roklib.util.helper;

public class CheckForNull
{
  public static void check (Object... objects)
  {
    StringBuilder buf = null;
    int count = 0;
    for (Object o : objects)
    {
      count++;
      if (o == null)
      {
        if (buf == null) buf = new StringBuilder ("Argument at position ");
        buf.append (count).append (", ");
      }
    }
    if (buf != null)
    {
      buf.setLength (buf.length () - 2); // remove last comma
      StringBuilder buf2 = new StringBuilder ();
      for (Object o : objects)
      {
        buf2.append (o == null ? "null, " : "set, ");
      }
      buf2.setLength (buf2.length () - 2);
      buf.append (" is null (").append (buf2.toString ()).append (").");
      throw new IllegalArgumentException (buf.toString ());
    }
  }
}
