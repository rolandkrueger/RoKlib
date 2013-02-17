/*
 * Copyright (C) 2007 Roland Krueger
 * Created on 19.01.2011
 *
 * Author: Roland Krueger (www.rolandkrueger.info)
 *
 * This file is part of RoKlib.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package info.rolandkrueger.roklib.util.test;

import static org.junit.Assert.assertEquals;
import info.rolandkrueger.roklib.util.RandomStringIDGenerator;

import java.util.Random;

import org.junit.Test;

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

  @Test (expected = IllegalArgumentException.class)
  public void testNegativeLengthThrowsException ()
  {
    RandomStringIDGenerator.getUniqueID (-1);
  }

  @Test (expected = IllegalArgumentException.class)
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
    private static final long serialVersionUID = -1239820927930277037L;

    private int               mNext            = 0;

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
