/*
 * Copyright (C) 2007 Roland Krueger
 * Created on 05.02.2010
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


import org.roklib.util.helper.CheckForNull;
import org.roklib.webapps.actions.interfaces.UserRegistrationMethods;
import org.roklib.webapps.data.access.UserDataAccess;
import org.roklib.webapps.data.usermgmt.GenericUser;
import org.roklib.webapps.data.usermgmt.UserRegistrationStatus;
import org.roklib.webapps.state.GenericUserState;

import java.io.Serializable;

public class UserRegistration<KeyClass, UserData, U extends GenericUser<KeyClass, UserData>> implements Serializable {
    private static final long serialVersionUID = 7913299696050270835L;

    public enum RegistrationOutcome {
        USERNAME_ALREADY_REGISTERED, ERROR_DURING_REGISTRATION, OK
    }

    public enum RegistrationConfirmationOutcome {
        REGISTRATION_KEY_UNKNOWN, OK
    }

    private UserDataAccess<KeyClass, UserData, U> dataAccess;
    private UserRegistrationMethods<KeyClass, UserData, U> registrationMethods;
    private int registrationKeyLength;

    public UserRegistration(UserDataAccess<KeyClass, UserData, U> dataAccess,
                            UserRegistrationMethods<KeyClass, UserData, U> registrationMethods) {
        this(dataAccess, registrationMethods, 30);
    }

    public UserRegistration(UserDataAccess<KeyClass, UserData, U> dataAccess,
                            UserRegistrationMethods<KeyClass, UserData, U> registrationMethods, int registrationKeyLength) {
        CheckForNull.check(dataAccess, registrationMethods);
        if (registrationKeyLength < 5)
            throw new IllegalArgumentException("Registration key length is too small (< 5)");

        this.dataAccess = dataAccess;
        this.registrationMethods = registrationMethods;
        this.registrationKeyLength = registrationKeyLength;
    }

    public RegistrationOutcome registerNewUser(U user) {
        return registerNewUser(user, true);
    }

    public RegistrationOutcome registerNewUser(U user, boolean requireConfirmation) {
        CheckForNull.check(user);
        if (dataAccess.getUserWithLogin(user.getLoginName()) != null)
            return RegistrationOutcome.USERNAME_ALREADY_REGISTERED;

        dataAccess.startTransaction();

        user.setRegistrationStatus(new UserRegistrationStatus(registrationKeyLength));
        if (requireConfirmation) {
            user.setState(GenericUserState.REGISTRATION_CONFIRMATION_PENDING);
        } else {
            user.setState(GenericUserState.REGISTERED);
            user.getRegistrationStatus().setRegistrationKey("");
        }

        dataAccess.persistUser(user);
        if (requireConfirmation && !registrationMethods.sendRegistrationNotification(user)) {
            dataAccess.rollback();
            user.setRegistrationStatus(null);
            return RegistrationOutcome.ERROR_DURING_REGISTRATION;
        }

        dataAccess.commit();
        return RegistrationOutcome.OK;
    }

    public RegistrationConfirmationResult completeRegistration(String registrationKey) {
        U user = dataAccess.getUserForRegistrationKey(registrationKey);
        if (user == null)
            return new RegistrationConfirmationResult(RegistrationConfirmationOutcome.REGISTRATION_KEY_UNKNOWN, null);

        user.setState(GenericUserState.REGISTERED);
        user.getRegistrationStatus().setRegistrationKey("");

        dataAccess.startTransaction();
        dataAccess.updateUser(user);
        dataAccess.commit();
        return new RegistrationConfirmationResult(RegistrationConfirmationOutcome.OK, user);
    }

    public class RegistrationConfirmationResult {
        private final RegistrationConfirmationOutcome mOutcome;
        private final U mUser;

        protected RegistrationConfirmationResult(RegistrationConfirmationOutcome outcome, U user) {
            mOutcome = outcome;
            mUser = user;
        }

        public RegistrationConfirmationOutcome getOutcome() {
            return mOutcome;
        }

        public U getUser() {
            return mUser;
        }
    }
}
