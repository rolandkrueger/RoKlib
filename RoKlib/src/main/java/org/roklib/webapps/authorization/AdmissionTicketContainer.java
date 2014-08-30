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
package org.roklib.webapps.authorization;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class AdmissionTicketContainer implements Serializable {
    private static final long serialVersionUID = -297124992997707673L;

    private Map<String, AdmissionTicket> admissionTickets;

    public boolean hasTicket(AdmissionTicket ticket) {
        return admissionTickets != null && admissionTickets.containsKey(ticket.getName());
    }

    public void addTicket(AdmissionTicket ticket) {
        if (admissionTickets == null)
            admissionTickets = new HashMap<String, AdmissionTicket>();
        admissionTickets.put(ticket.getName(), ticket);
    }

    public boolean removeTicket(AdmissionTicket ticket) {
        return admissionTickets != null && admissionTickets.remove(ticket.getName()) != null;
    }

    public Map<String, AdmissionTicket> getTickets() {
        if (admissionTickets == null)
            return Collections.emptyMap();
        return Collections.unmodifiableMap(admissionTickets);
    }

    public void clear() {
        if (admissionTickets == null)
            return;
        admissionTickets.clear();
    }
}
