/*
 * $Id: DefaultPasswordMD5HashGenerator.java 181 2010-11-01 09:39:13Z roland $
 * Copyright (C) 2007 Roland Krueger
 * Created on 29.01.2010
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
package info.rolandkrueger.roklib.webapps.actions;

import info.rolandkrueger.roklib.webapps.actions.interfaces.IPasswordHashGenerator;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class DefaultPasswordMD5HashGenerator implements IPasswordHashGenerator
{
  private static final long serialVersionUID = - 4098113757797776880L;

  public String createPasswordHash (String password)
  {
    MessageDigest md5;
    try
    {
      md5 = MessageDigest.getInstance ("MD5");
    } catch (NoSuchAlgorithmException e)
    {
      throw new RuntimeException ("Unable to create MD5 hash of password.", e);
    }
    md5.reset ();
    md5.update (password.getBytes ());
    byte[] result = md5.digest ();

    StringBuilder hexString = new StringBuilder ();
    for (int i = 0; i < result.length; ++i)
    {
      hexString.append (Integer.toHexString (0xFF & result[i]));
    }

    return hexString.toString ();
  }
}
