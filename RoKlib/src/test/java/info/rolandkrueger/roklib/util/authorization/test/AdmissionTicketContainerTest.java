/*
 * $Id: AdmissionTicketContainerTest.java 181 2010-11-01 09:39:13Z roland $
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
package info.rolandkrueger.roklib.util.authorization.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import info.rolandkrueger.roklib.util.authorization.AdmissionTicketContainer;
import info.rolandkrueger.roklib.util.authorization.IAdmissionTicket;

import org.junit.Before;
import org.junit.Test;

public class AdmissionTicketContainerTest
{
  private AdmissionTicketContainer mTestObj;

  private class TTicket implements IAdmissionTicket
  {
    private static final long serialVersionUID = - 2680076196174728564L;
    private String mName;

    public TTicket (String name)
    {
      mName = name;
    }

    public String getName ()
    {
      return mName;
    }
  }

  @Before
  public void setUp ()
  {
    mTestObj = new AdmissionTicketContainer ();
    mTestObj.addTicket (new TTicket ("admin"));
    mTestObj.addTicket (new TTicket ("editor"));
  }

  @Test
  public void testHasTicket ()
  {
    assertTrue (mTestObj.hasTicket (new TTicket ("admin")));
    assertFalse (mTestObj.hasTicket (new TTicket ("user")));
  }

  @Test
  public void testClear ()
  {
    mTestObj.clear ();
    assertTrue (mTestObj.getTickets ().isEmpty ());
  }

  @Test
  public void testEmptyContainer ()
  {
    mTestObj = new AdmissionTicketContainer ();
    assertTrue (mTestObj.getTickets ().isEmpty ());
    mTestObj.clear ();
    assertTrue (mTestObj.getTickets ().isEmpty ());
    assertFalse (mTestObj.hasTicket (new TTicket ("user")));
  }

  @Test
  public void testRemoveTicket ()
  {
    assertTrue (mTestObj.hasTicket (new TTicket ("admin")));
    assertTrue (mTestObj.removeTicket (new TTicket ("admin")));
    assertFalse (mTestObj.hasTicket (new TTicket ("admin")));
  }
}
