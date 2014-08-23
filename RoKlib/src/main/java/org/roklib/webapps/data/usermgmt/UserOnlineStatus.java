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

import java.io.Serializable;
import java.util.Date;

public class UserOnlineStatus implements Serializable {
    private static final long serialVersionUID = -4054866399984707095L;

    private boolean mOnline;
    private Date mOnlineSince;

    public void setOnline(boolean online) {
        mOnline = online;
        if (online)
            mOnlineSince = new Date();
        else
            mOnlineSince = null;
    }

    public boolean isOnline() {
        return mOnline;
    }

    public Date getOnlineSince() {
        return mOnlineSince;
    }
}
