/*
 * $Id: RandomStringIDGenerator.java 246 2011-01-19 17:03:10Z roland $
 * Copyright (C) 2007 Roland Krueger
 * Created on 24.07.2009
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
package info.rolandkrueger.roklib.util;

import info.rolandkrueger.roklib.util.helper.CheckForNull;

import java.util.Random;

/**
 * Utility class for creating fixed-length Strings which contain a sequence of
 * random characters. The set of characters from which the String is generated
 * can be set explicitly. By default, this set contains all 26 letters from the
 * alphabet in upper and lower case and all 10 digits.
 * 
 * An example String which may be generated with this class is
 * 'oSi6B3hnG2Ge6SuhPAa'.
 * 
 * @author Roland Krueger
 * @version $Id: RandomStringIDGenerator.java 246 2011-01-19 17:03:10Z roland $
 */
public final class RandomStringIDGenerator
{
  private static String sCharList = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

  private RandomStringIDGenerator () {}
  
  /**
   * Generates a random String with the specified length. The characters are
   * randomly chosen from the provided String.
   * 
   * The {@link Random} object which is used internally is seeded with the
   * current system time.
   * 
   * @param length
   *          number of characters in the resulting String
   * @param fromCharacters
   *          String from which the characters are randomly chosen
   * @throws IllegalArgumentException
   *           if the provided length is a negative number or if the given
   *           character String is either empty or does only consist of
   *           whitespaces
   */
  public static String getUniqueID (int length, String fromCharacters)
  {
    return getUniqueID (new Random (System.currentTimeMillis ()), length, fromCharacters);
  }

  /**
   * @see RandomStringIDGenerator#getUniqueID(int, String)
   * 
   * @param random
   *          {@link Random} object to be used for creating the result String
   * @param length
   *          number of characters in the resulting String
   * @param fromCharacters
   *          String from which the characters are randomly chosen
   * @throws IllegalArgumentException
   *           if the provided length is a negative number or if the given
   *           character String is either empty or does only consist of
   *           whitespaces
   */
  public static String getUniqueID (Random random, int length, String fromCharacters)
  {
    if (length < 0)
    {
      throw new IllegalArgumentException ("Length parameter < 0 not allowed.");
    }
    if (fromCharacters.trim ().isEmpty ())
    {
      throw new IllegalArgumentException ("Charlist is empty.");
    }
    CheckForNull.check (fromCharacters);
    if (length == 0)
    {
      return "";
    }

    char[] buffer = new char[length];
    int fromCharactersLength = fromCharacters.length ();
    for (int i = 0; i < buffer.length; i++)
    {
      buffer[i] = fromCharacters.charAt (random.nextInt (fromCharactersLength));
    }
    return String.valueOf (buffer);
  }

  /**
   * Generates a random String with the specified length. The characters are
   * randomly chosen from all upper and lower case characters of the English
   * alphabet and the numbers 0-9.
   * 
   * The {@link Random} object which is used internally is seeded with the
   * current system time.
   * 
   * @param length
   *          number of characters in the resulting String
   * @throws IllegalArgumentException
   *           if the provided length is a negative number
   */
  public static String getUniqueID (int length)
  {
    return getUniqueID (new Random (System.currentTimeMillis ()), length);
  }

  /**
   * @see RandomStringIDGenerator#getUniqueID(int)
   * 
   * @param random
   *          {@link Random} object to be used for creating the result String
   * @param length
   *          number of characters in the resulting String
   * @throws IllegalArgumentException
   *           if the provided length is a negative number
   */
  public static String getUniqueID (Random random, int length)
  {
    return getUniqueID (random, length, sCharList);
  }
}
