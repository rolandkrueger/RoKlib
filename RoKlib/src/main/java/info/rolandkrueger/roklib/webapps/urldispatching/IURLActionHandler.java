/*
 * $Id: IURLActionHandler.java 181 2010-11-01 09:39:13Z roland $
 * Copyright (C) 2007 - 2010 Roland Krueger
 * Created on 08.04.2010
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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307
 * USA
 */
package info.rolandkrueger.roklib.webapps.urldispatching;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface IURLActionHandler extends Serializable
{
  public enum ParameterMode {QUERY, DIRECTORY, DIRECTORY_WITH_NAMES}
  
  public abstract AbstractURLActionCommand handleURL (List<String> uriTokens,
      Map<String, List<String>> parameters, ParameterMode parameterMode);
}
