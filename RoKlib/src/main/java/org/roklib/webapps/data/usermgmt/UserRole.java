/*
 * Copyright (C) 2007 Roland Krueger
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

import org.roklib.webapps.authorization.AdmissionTicket;
import org.roklib.webapps.data.GenericPersistableObjectImpl;

public class UserRole<KeyClass> extends GenericPersistableObjectImpl<KeyClass> implements AdmissionTicket {
    private static final long serialVersionUID = 467091219897410434L;

    private String roleName;
    private String description;

    public UserRole() {
        super();
    }

    public UserRole(KeyClass key) {
        this(key, null);
    }

    public UserRole(String roleName) {
        setName(roleName);
    }

    public UserRole(KeyClass key, String roleName) {
        this(roleName);
        setKey(key);
    }

    public void setName(String roleName) {
        this.roleName = roleName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return roleName;
    }

    public String getName() {
        return roleName;
    }
}
