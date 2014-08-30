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
package org.roklib.webapps.data.usermgmt;


import org.roklib.util.RandomStringIDGenerator;
import org.roklib.util.helper.CheckForNull;

import java.io.Serializable;
import java.util.Date;

public class UserRegistrationStatus implements Serializable {
    private static final long serialVersionUID = 325123813003034741L;

    private Date registeredSince;
    private String registrationKey;

    public UserRegistrationStatus() {
    }

    public UserRegistrationStatus(Date registeredSince, String registrationKey) {
        CheckForNull.check(registeredSince, registrationKey);
        this.registeredSince = registeredSince;
        this.registrationKey = registrationKey;
    }

    public UserRegistrationStatus(int registrationKeyLength) {
        this(new Date(), RandomStringIDGenerator.getUniqueID(registrationKeyLength));
    }

    public String getRegistrationKey() {
        return registrationKey;
    }

    public void setRegistrationKey(String registrationKey) {
        this.registrationKey = registrationKey;
    }

    public Date getRegisteredSince() {
        return registeredSince;
    }

    public void setRegisteredSince(Date registeredSince) {
        this.registeredSince = registeredSince;
    }
}
