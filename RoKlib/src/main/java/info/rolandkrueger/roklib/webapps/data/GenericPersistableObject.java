/*
 * $Id: GenericPersistableObject.java 178 2010-10-31 18:01:20Z roland $
 * Copyright (C) 2007 Roland Krueger
 * Created on 27.01.2010
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
package info.rolandkrueger.roklib.webapps.data;

public class GenericPersistableObject<KeyClass> implements IGenericPersistableObject<KeyClass>
{
  private static final long serialVersionUID = 1820908905463077361L;

  private KeyClass mKey;

  public KeyClass getKey ()
  {
    return mKey;
  }

  public void setKey (KeyClass key)
  {
    mKey = key;
  }

  @SuppressWarnings ("unchecked")
  @Override
  public boolean equals (Object obj)
  {
    if (mKey == null) return false;
    if (obj == null) return false;
    if (obj == this) return true;

    if (obj instanceof GenericPersistableObject)
    {
      GenericPersistableObject other = (GenericPersistableObject) obj;
      return mKey.equals (other.mKey);
    }
    return false;
  }

  @Override
  public int hashCode ()
  {
    if (mKey == null) return - 1;
    return mKey.hashCode ();
  }
}
