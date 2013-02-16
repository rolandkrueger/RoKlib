/*
 * $Id: RandomStringIDGeneratorTest.java 246 2011-01-19 17:03:10Z roland $
 * Copyright (C) 2007 Roland Krueger
 * Created on 19.01.2011
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
package info.rolandkrueger.roklib.util.test;

import java.util.Random;

import info.rolandkrueger.roklib.util.RandomStringIDGenerator;

import org.junit.Test;
import static org.junit.Assert.*;

public class RandomStringIDGeneratorTest
{
  @Test
  public void testZeroLengthYieldsEmptyString ()
  {
    String randomString = RandomStringIDGenerator.getUniqueID (0);
    assertStringIsEmpty (randomString);
  }

  @Test
  public void testZeroLengthAndRandomYieldsEmptyString ()
  {
    String randomString = RandomStringIDGenerator.getUniqueID (new Random (), 0);
    assertStringIsEmpty (randomString);
  }

  @Test
  public void testGenerateStringWithOwnCharacters ()
  {
    String randomString = RandomStringIDGenerator.getUniqueID (5, "x");
    assertEquals ("xxxxx", randomString);
  }

  public void testGenerateStringWithOwnCharactersAndRandom ()
  {
    String randomString = RandomStringIDGenerator.getUniqueID (new Random (), 5, "x");
    assertEquals ("xxxxx", randomString);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testNegativeLengthThrowsException ()
  {
    RandomStringIDGenerator.getUniqueID (-1);
  }
  
  @Test (expected=IllegalArgumentException.class)
  public void testEmptyCharacterStringThrowsAnException ()
  {
    RandomStringIDGenerator.getUniqueID (5, " \t ");
  }
  
  @Test
  public void testWithOwnRandom ()
  {
    String randomString = RandomStringIDGenerator.getUniqueID (new RandomThatCountsUp (), 5);
    assertEquals ("abcde", randomString);
  }
  
  private void assertStringIsEmpty (String string)
  {
    assertEquals ("", string);
  }
  
  private static class RandomThatCountsUp extends Random
  {
    private static final long serialVersionUID = - 1239820927930277037L;
    
    private int mNext = 0;
    
    @Override
    public int nextInt (int n)
    {
      return nextInt ();
    }
    
    @Override
    public int nextInt ()
    {
      return mNext++;
    }
  }
}
