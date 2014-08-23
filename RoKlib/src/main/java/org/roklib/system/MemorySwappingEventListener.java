/*
 * Copyright (C) 2007 Roland Krueger
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
package org.roklib.system;

/**
 * This interface defines a callback that is invoked by a {@link AbstractMemorySwappingDetectionThread} if a memory
 * paging event is detected by it.
 *
 * @author Roland Krueger
 * @see AbstractMemorySwappingDetectionThread
 */
public interface MemorySwappingEventListener {
    /**
     * Callback for memory swapping events. If memory paging was detected, this method will be called. A class
     * implementing this interface can then react accordingly.
     */
    public void memorySwappingDetected();
}
