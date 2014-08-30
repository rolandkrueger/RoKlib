/*
 * Copyright (C) 2007 Roland Krueger
 * Created on 29.01.2010
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
package org.roklib.webapps.actions;


import org.roklib.webapps.actions.interfaces.PasswordHashGenerator;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class DefaultPasswordMD5HashGenerator implements PasswordHashGenerator {
    private static final long serialVersionUID = -4098113757797776880L;

    public String createPasswordHash(String password) {
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Unable to create MD5 hash of password.", e);
        }
        md5.reset();
        md5.update(password.getBytes());
        byte[] result = md5.digest();

        StringBuilder hexString = new StringBuilder();
        for (final byte aResult : result) {
            hexString.append(Integer.toHexString(0xFF & aResult));
        }

        return hexString.toString();
    }
}
