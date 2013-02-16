/*
 * $Id: AdmissionTicketContainer.java 178 2010-10-31 18:01:20Z roland $
 * Copyright (C) 2007 Roland Krueger
 * Created on 09.02.2010
 *
 * Author: Roland Krueger (www.rolandkrueger.info)
 *
 * This file is part of RoKlib.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307
 * USA
 */
package info.rolandkrueger.roklib.util.authorization;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class AdmissionTicketContainer implements Serializable
{
  private static final long serialVersionUID = - 297124992997707673L;

  private Map<String, IAdmissionTicket> mAdmissionTickets;

  public boolean hasTicket (IAdmissionTicket ticket)
  {
    if (mAdmissionTickets == null) return false;
    return mAdmissionTickets.containsKey (ticket.getName ());
  }

  public void addTicket (IAdmissionTicket ticket)
  {
    if (mAdmissionTickets == null) mAdmissionTickets = new HashMap<String, IAdmissionTicket> ();
    mAdmissionTickets.put (ticket.getName (), ticket);
  }

  public boolean removeTicket (IAdmissionTicket ticket)
  {
    if (mAdmissionTickets == null) return false;
    return mAdmissionTickets.remove (ticket.getName ()) != null;
  }

  public Map<String, IAdmissionTicket> getTickets ()
  {
    if (mAdmissionTickets == null) return Collections.emptyMap ();
    return Collections.unmodifiableMap (mAdmissionTickets);
  }

  public void clear ()
  {
    if (mAdmissionTickets == null) return;
    mAdmissionTickets.clear ();
  }
}
