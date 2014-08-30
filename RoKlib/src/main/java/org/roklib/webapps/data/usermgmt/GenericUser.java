/*
 * Copyright (C) 2007 - 2010 Roland Krueger
 * Created on 27.01.2010
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


import org.roklib.state.State.StateValue;
import org.roklib.util.helper.CheckForNull;
import org.roklib.webapps.actions.DefaultPasswordMD5HashGenerator;
import org.roklib.webapps.actions.interfaces.PasswordHashGenerator;
import org.roklib.webapps.authorization.AdmissionTicketContainer;
import org.roklib.webapps.data.GenericPersistableObjectImpl;
import org.roklib.webapps.state.GenericUserState;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class GenericUser<KeyClass, UserData> extends GenericPersistableObjectImpl<KeyClass> {
    private static final long serialVersionUID = -8767507542975979387L;

    private final Serializable STATE_LOCK = new Serializable() {
        private static final long serialVersionUID = 1098724371435032386L;
    };

    private String loginName;
    private String passwordHash;
    protected UserData userData;
    private final Set<UserRole<KeyClass>> userRoles;
    private final AdmissionTicketContainer roleHandler;
    private final GenericUserState state;
    private UserOnlineStatus onlineStatus;
    private UserRegistrationStatus registrationStatus;
    private PasswordHashGenerator passwordHashGenerator;

    public GenericUser() {
        userRoles = new HashSet<UserRole<KeyClass>>();
        state = new GenericUserState(GenericUserState.UNKNOWN);
        state.lock(STATE_LOCK);
        onlineStatus = new UserOnlineStatus();
        passwordHashGenerator = new DefaultPasswordMD5HashGenerator();
        roleHandler = new AdmissionTicketContainer();
    }

    public void setPasswordHashGenerator(PasswordHashGenerator generator) {
        CheckForNull.check(generator);
        passwordHashGenerator = generator;
    }

    public PasswordHashGenerator getPasswordHashGenerator() {
        return passwordHashGenerator;
    }

    public GenericUser(UserData userData) {
        this();
        this.userData = userData;
    }

    public Set<? extends UserRole<KeyClass>> getUserRoles() {
        return userRoles;
    }

    protected void setUserRoles(Set<? extends UserRole<KeyClass>> userRoles) {
        CheckForNull.check(userRoles);
        this.userRoles.clear();
        this.userRoles.addAll(userRoles);
        for (UserRole<KeyClass> role : userRoles) {
            roleHandler.addTicket(role);
        }
    }

    public <R extends UserRole<KeyClass>> void addRole(R newRole) {
        CheckForNull.check(newRole);
        userRoles.add(newRole);
        roleHandler.addTicket(newRole);
    }

    public <R extends UserRole<KeyClass>> void removeRole(R role) {
        userRoles.remove(role);
        roleHandler.removeTicket(role);
    }

    public <R extends UserRole<KeyClass>> boolean hasRole(R role) {
        return roleHandler.hasTicket(role);
    }

    public GenericUserState getState() {
        return state;
    }

    public boolean hasState(StateValue<GenericUserState> state) {
        return this.state.hasState(state);
    }

    public void setState(StateValue<GenericUserState> newState) {
        state.setStateValue(newState, STATE_LOCK);
    }

    public UserOnlineStatus getOnlineStatus() {
        return onlineStatus;
    }

    public void setOnlineStatus(UserOnlineStatus onlineStatus) {
        CheckForNull.check(onlineStatus);
        this.onlineStatus = onlineStatus;
    }

    public UserRegistrationStatus getRegistrationStatus() {
        return registrationStatus;
    }

    public void setRegistrationStatus(UserRegistrationStatus registrationStatus) {
        this.registrationStatus = registrationStatus;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPassword(String password) {
        setPasswordHash(passwordHashGenerator.createPasswordHash(password));
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public UserData getUserData() {
        return userData;
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
    }

    /**
     * @return
     * @see org.roklib.webapps.data.usermgmt.UserRegistrationStatus#getRegisteredSince()
     */
    public Date getRegisteredSince() {
        if (registrationStatus == null)
            return null;
        return registrationStatus.getRegisteredSince();
    }

    /**
     * @return
     * @see org.roklib.webapps.data.usermgmt.UserRegistrationStatus#getRegistrationKey()
     */
    public String getRegistrationKey() {
        if (registrationStatus == null)
            return null;
        return registrationStatus.getRegistrationKey();
    }

    /**
     * @param registeredSince
     * @see org.roklib.webapps.data.usermgmt.UserRegistrationStatus#setRegisteredSince(java.util.Date)
     */
    public void setRegisteredSince(Date registeredSince) {
        if (registeredSince == null)
            return;
        if (registrationStatus == null)
            registrationStatus = new UserRegistrationStatus();
        registrationStatus.setRegisteredSince(registeredSince);
    }

    /**
     * @param registrationKey
     * @see org.roklib.webapps.data.usermgmt.UserRegistrationStatus#setRegistrationKey(java.lang.String)
     */
    public void setRegistrationKey(String registrationKey) {
        if (registrationKey == null)
            return;
        if (registrationStatus == null)
            registrationStatus = new UserRegistrationStatus();
        registrationStatus.setRegistrationKey(registrationKey);
    }
}
