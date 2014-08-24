/*
 * Copyright (C) 2007 Roland Krueger
 * Created on 24.07.2009
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
package org.roklib.util;


import org.roklib.util.helper.CheckForNull;

import java.util.Random;

/**
 * Utility class for creating fixed-length Strings which contain a sequence of random characters. The set of characters
 * from which the String is generated can be set explicitly. By default, this set contains all 26 letters from the
 * alphabet in upper and lower case and all 10 digits.
 * <p/>
 * An example String which may be generated with this class is 'oSi6B3hnG2Ge6SuhPAa'.
 *
 * @author Roland Krueger
 * @version $Id: RandomStringIDGenerator.java 246 2011-01-19 17:03:10Z roland $
 */
public final class RandomStringIDGenerator {

    private RandomStringIDGenerator() {
    }

    /**
     * Generates a random String with the specified length. The characters are randomly chosen from the provided String.
     * <p/>
     * The {@link Random} object which is used internally is seeded with the current system time.
     *
     * @param length         number of characters in the resulting String
     * @param fromCharacters String from which the characters are randomly chosen
     * @throws IllegalArgumentException if the provided length is a negative number or if the given character String is either empty or does only
     *                                  consist of whitespaces
     */
    public static String getUniqueID(int length, String fromCharacters) {
        return getUniqueID(new Random(System.currentTimeMillis()), length, fromCharacters);
    }

    /**
     * @param random         {@link Random} object to be used for creating the result String
     * @param length         number of characters in the resulting String
     * @param fromCharacters String from which the characters are randomly chosen
     * @throws IllegalArgumentException if the provided length is a negative number or if the given character String is either empty or does only
     *                                  consist of whitespaces
     * @see RandomStringIDGenerator#getUniqueID(int, String)
     */
    public static String getUniqueID(Random random, int length, String fromCharacters) {
        if (length < 0) {
            throw new IllegalArgumentException("Length parameter < 0 not allowed.");
        }
        if (fromCharacters.trim().isEmpty()) {
            throw new IllegalArgumentException("Charlist is empty.");
        }
        CheckForNull.check(fromCharacters);
        if (length == 0) {
            return "";
        }

        char[] buffer = new char[length];
        int fromCharactersLength = fromCharacters.length();
        for (int i = 0; i < buffer.length; i++) {
            buffer[i] = fromCharacters.charAt(random.nextInt(fromCharactersLength));
        }
        return String.valueOf(buffer);
    }

    /**
     * Generates a random String with the specified length. The characters are randomly chosen from all upper and lower
     * case characters of the English alphabet and the numbers 0-9.
     * <p/>
     * The {@link Random} object which is used internally is seeded with the current system time.
     *
     * @param length number of characters in the resulting String
     * @throws IllegalArgumentException if the provided length is a negative number
     */
    public static String getUniqueID(int length) {
        return getUniqueID(new Random(System.currentTimeMillis()), length);
    }

    /**
     * @param random {@link Random} object to be used for creating the result String
     * @param length number of characters in the resulting String
     * @throws IllegalArgumentException if the provided length is a negative number
     * @see RandomStringIDGenerator#getUniqueID(int)
     */
    public static String getUniqueID(Random random, int length) {
        final String sCharList = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        return getUniqueID(random, length, sCharList);
    }
}
