/*
 * Copyright (C) 2007 Roland Krueger
 * Created on 31.10.2010
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
package org.roklib.webapps.data;

import java.io.InputStream;
import java.io.Serializable;

public class DownloadInfo implements Serializable {
    private InputStream mDataStream;
    private String mFilename;
    private String mContentType;

    public DownloadInfo() {
    }

    public DownloadInfo(InputStream pDataStream, String pFilename, String pContentType) {
        mDataStream = pDataStream;
        mFilename = pFilename;
        mContentType = pContentType;
    }

    public InputStream getDataStream() {
        return mDataStream;
    }

    public void setDataStream(InputStream pDataStream) {
        mDataStream = pDataStream;
    }

    public String getFilename() {
        return mFilename;
    }

    public void setFilename(String pFilename) {
        mFilename = pFilename;
    }

    public String getContentType() {
        return mContentType;
    }

    public void setContentType(String pContentType) {
        mContentType = pContentType;
    }
}
