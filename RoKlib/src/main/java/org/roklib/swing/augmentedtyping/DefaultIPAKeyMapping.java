/*
 * Copyright (C) 2007 Roland Krueger
 * Created on 23.10.2009
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
package org.roklib.swing.augmentedtyping;

public class DefaultIPAKeyMapping extends AugmentedTypingKeyMapping {
    // a , '\ u',
    final Character[] aChars = {'\u0251', '\u0250', '\u0252', '\u00E6', '\u028C'};
    // ae
    final Character[] aeChars = {'\u00E6'};
    // b
    final Character[] bChars = {'\u0253', '\u0299', '\u03B2'};
    // c
    final Character[] cChars = {'\u0254', '\u0255', '\u00E7'};
    // d
    final Character[] dChars = {'\u0257', '\u0256', '\u00F0', '\u02A4'};
    // e
    final Character[] eChars = {'\u0259', '\u0258', '\u025A', '\u025B', '\u025C', '\u025D', '\u025E', '\u0292'};
    // f
    final Character[] fChars = {'\u025F', '\u0284'};
    // g
    final Character[] gChars = {'\u0261', '\u0260', '\u0262', '\u029B'};
    // h
    final Character[] hChars = {'\u0266', '\u0267', '\u0127', '\u0265', '\u029C', '\u02B0', '\u02B1'};
    // i
    final Character[] iChars = {'\u0268', '\u026A'};
    // j
    final Character[] jChars = {'\u025F', '\u029D', '\u02B2'};
    // l
    final Character[] lChars = {'\u026D', '\u026C', '\u026B', '\u026E', '\u029F'};
    // m
    final Character[] mChars = {'\u0271', '\u026F', '\u0270'};
    // n
    final Character[] nChars = {'\u014B', '\u0273', '\u0272', '\u0274'};
    // o
    final Character[] oChars = {'\u00F8', '\u0275', '\u0278', '\u03B8', '\u0153', '\u0276', '\u0298', '\u0254'};
    // oe
    final Character[] oeChars = {'\u00F8', '\u0153', '\u0276'};
    // r
    final Character[] rChars = {'\u0279', '\u027A', '\u027E', '\u027B', '\u0280', '\u0281', '\u027D', '\u02B4'};
    // s
    final Character[] sChars = {'\u0282', '\u0283'};
    // t
    final Character[] tChars = {'\u0288', '\u02A7'};
    // u
    final Character[] uChars = {'\u0289', '\u028B', '\u028A'};
    // v
    final Character[] vChars = {'\u028B', '\u028A', '\u028C', '\u0263', '\u0264', '\u02E0', '\u02C7'};
    // w
    final Character[] wChars = {'\u028D', '\u02B7'};
    // x
    final Character[] xChars = {'\u03C7'};
    // y
    final Character[] yChars = {'\u028E', '\u028F', '\u0263', '\u0264', '\u02E0'};
    // z
    final Character[] zChars = {'\u0291', '\u0290', '\u0292'};
    // sz
    final Character[] szChars = {'\u03B2'};
    // ?
    final Character[] qmChars = {'\u0294', '\u02A1', '\u0295', '\u02A2', '\u02E4'};
    // |
    final Character[] barChars = {'\u01C0', '\u01C1', '\u01C2', '\u01C3'};
    // !
    final Character[] emChars = {'\u01C3'};
    // :
    final Character[] colonChars = {'\u02D0'};
    // ,
    final Character[] commaChars = {'\u02CC'};
    // '
    final Character[] apoChars = {'\u02D1', '\u02BC', '\u02DE'};

    public DefaultIPAKeyMapping() {
        addMapping('a', aChars);
        addMapping('\u00E4', aeChars);
        addMapping('b', bChars);
        addMapping('c', cChars);
        addMapping('d', dChars);
        addMapping('e', eChars);
        addMapping('f', fChars);
        addMapping('g', gChars);
        addMapping('h', hChars);
        addMapping('i', iChars);
        addMapping('j', jChars);
        addMapping('l', lChars);
        addMapping('m', mChars);
        addMapping('n', nChars);
        addMapping('o', oChars);
        addMapping('\u00F6', oeChars);
        addMapping('r', rChars);
        addMapping('s', sChars);
        addMapping('t', tChars);
        addMapping('u', uChars);
        addMapping('v', vChars);
        addMapping('w', wChars);
        addMapping('x', xChars);
        addMapping('y', yChars);
        addMapping('z', zChars);

        addMapping('A', aChars);
        addMapping('\u00C4', aeChars);
        addMapping('B', bChars);
        addMapping('C', cChars);
        addMapping('D', dChars);
        addMapping('E', eChars);
        addMapping('F', fChars);
        addMapping('G', gChars);
        addMapping('H', hChars);
        addMapping('I', iChars);
        addMapping('J', jChars);
        addMapping('L', lChars);
        addMapping('M', mChars);
        addMapping('N', nChars);
        addMapping('O', oChars);
        addMapping('\u00D6', oeChars);
        addMapping('R', rChars);
        addMapping('S', sChars);
        addMapping('T', tChars);
        addMapping('U', uChars);
        addMapping('V', vChars);
        addMapping('W', wChars);
        addMapping('X', xChars);
        addMapping('Y', yChars);
        addMapping('Z', zChars);

        addMapping('\u00DF', szChars);
        addMapping('?', qmChars);
        addMapping('|', barChars);
        addMapping('!', emChars);
        addMapping(':', colonChars);
        addMapping(',', commaChars);
        addMapping('\'', apoChars);
    }
}
