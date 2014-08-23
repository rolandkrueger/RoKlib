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
package org.roklib.webapps.data.access;


import org.roklib.webapps.data.usermgmt.GenericUser;

import java.io.Serializable;

public interface IUserDataAccess<KeyClass, UserData, U extends GenericUser<KeyClass, UserData>> extends Serializable {
    public U getUserWithLogin(String login);

    public void persistUser(U user);

    public void updateUser(U user);

    public U getUserForRegistrationKey(String registrationKey);

    public void startTransaction();

    public void commit();

    public void rollback();
}
