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
    private InputStream dataStream;
    private String filename;
    private String contentType;

    public DownloadInfo() {
    }

    public DownloadInfo(InputStream pDataStream, String pFilename, String pContentType) {
        dataStream = pDataStream;
        filename = pFilename;
        contentType = pContentType;
    }

    public InputStream getDataStream() {
        return dataStream;
    }

    public void setDataStream(InputStream pDataStream) {
        dataStream = pDataStream;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String pFilename) {
        filename = pFilename;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String pContentType) {
        contentType = pContentType;
    }
}
