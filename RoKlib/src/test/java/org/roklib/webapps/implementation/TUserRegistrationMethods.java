/*
 * Copyright (C) 2007 Roland Krueger
 * Created on 08.03.2010
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
package org.roklib.webapps.implementation;

import org.roklib.webapps.actions.interfaces.UserRegistrationMethods;

@SuppressWarnings("serial")
public class TUserRegistrationMethods implements UserRegistrationMethods<Long, String, TUser> {
    public TUser notificationRecipient;
    public boolean registrationSent = false;
    private boolean sendRegistrationSuccessful = true;

    public boolean sendRegistrationNotification(TUser registeredUser) {
        if (sendRegistrationSuccessful)
            registrationSent = true;
        return sendRegistrationSuccessful;
    }

    public void setSendRegistrationSuccessful(boolean successful) {
        sendRegistrationSuccessful = successful;
    }
}
