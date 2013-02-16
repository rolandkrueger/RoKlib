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
package info.rolandkrueger.roklib.util.authorization;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class AdmissionTicketContainer implements Serializable
{
  private static final long             serialVersionUID = -297124992997707673L;

  private Map<String, IAdmissionTicket> mAdmissionTickets;

  public boolean hasTicket (IAdmissionTicket ticket)
  {
    if (mAdmissionTickets == null)
      return false;
    return mAdmissionTickets.containsKey (ticket.getName ());
  }

  public void addTicket (IAdmissionTicket ticket)
  {
    if (mAdmissionTickets == null)
      mAdmissionTickets = new HashMap<String, IAdmissionTicket> ();
    mAdmissionTickets.put (ticket.getName (), ticket);
  }

  public boolean removeTicket (IAdmissionTicket ticket)
  {
    if (mAdmissionTickets == null)
      return false;
    return mAdmissionTickets.remove (ticket.getName ()) != null;
  }

  public Map<String, IAdmissionTicket> getTickets ()
  {
    if (mAdmissionTickets == null)
      return Collections.emptyMap ();
    return Collections.unmodifiableMap (mAdmissionTickets);
  }

  public void clear ()
  {
    if (mAdmissionTickets == null)
      return;
    mAdmissionTickets.clear ();
  }
}
