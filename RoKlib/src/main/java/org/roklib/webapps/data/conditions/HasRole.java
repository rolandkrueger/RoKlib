/*
 * Copyright (C) 2007 Roland Krueger
 * Created on 09.02.2010
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
package org.roklib.webapps.data.conditions;

import org.roklib.conditional.bool.BooleanValueProvider;
import org.roklib.webapps.data.usermgmt.GenericUser;
import org.roklib.webapps.data.usermgmt.UserRole;

public class HasRole<KeyClass, UserData> implements BooleanValueProvider {
    private static final long serialVersionUID = -3841669716191027492L;

    private GenericUser<KeyClass, UserData> user;
    private final UserRole<KeyClass> role;

    public HasRole(GenericUser<KeyClass, UserData> user, UserRole<KeyClass> role) {
        this.user = user;
        this.role = role;
    }

    public void setUser(GenericUser<KeyClass, UserData> user) {
        this.user = user;
    }

    public boolean getBooleanValue() {
        return !(user == null || role == null) && user.hasRole(role);
    }
}
