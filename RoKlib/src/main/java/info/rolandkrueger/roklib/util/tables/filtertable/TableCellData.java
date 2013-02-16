/*
 * $Id: TableCellData.java 178 2010-10-31 18:01:20Z roland $
 * Copyright (C) 2007-2010 Roland Krueger
 * Created on 02.02.2010
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
package info.rolandkrueger.roklib.util.tables.filtertable;

import java.io.Serializable;
import java.math.BigDecimal;

public class TableCellData<T> implements Serializable
{
  private static final long serialVersionUID = 1794744722319758347L;

  private String mStringValue;
  private BigDecimal mNumericValue;
  private T mAdditionalData;

  public TableCellData (String stringValue, boolean tryToConvertToNumber)
  {
    setStringValue (stringValue, tryToConvertToNumber);
  }

  public TableCellData (int intValue)
  {
    mNumericValue = new BigDecimal (intValue);
    mStringValue = mNumericValue.toPlainString ();
  }

  public TableCellData (long longValue)
  {
    mNumericValue = new BigDecimal (longValue);
    mStringValue = mNumericValue.toPlainString ();
  }

  public TableCellData (float floatValue)
  {
    mNumericValue = new BigDecimal (floatValue);
    mStringValue = mNumericValue.toPlainString ();
  }

  public TableCellData (double doubleValue)
  {
    mNumericValue = new BigDecimal (doubleValue);
    mStringValue = mNumericValue.toPlainString ();
  }

  public TableCellData (byte byteValue)
  {
    mNumericValue = new BigDecimal (byteValue);
    mStringValue = mNumericValue.toPlainString ();
  }

  public TableCellData (char charValue)
  {
    mNumericValue = new BigDecimal (charValue);
    mStringValue = mNumericValue.toPlainString ();
  }

  public TableCellData (short charValue)
  {
    mNumericValue = new BigDecimal (charValue);
    mStringValue = mNumericValue.toPlainString ();
  }

  public TableCellData (Object value, boolean tryToConvertToNumber)
  {
    this (value.toString (), tryToConvertToNumber);
  }

  public void setStringValue (String stringValue, boolean tryToConvertToNumber)
  {
    mStringValue = stringValue;

    if (tryToConvertToNumber)
    {
      try
      {
        mNumericValue = new BigDecimal (stringValue.trim ());
      } catch (NumberFormatException nfExc)
      {
        mNumericValue = null;
      }
    }
  }

  public String getStringValue ()
  {
    return mStringValue;
  }

  public BigDecimal getNumericValue ()
  {
    if (! isNumeric ()) throw new IllegalStateException ("This data object has no numeric value.");
    return mNumericValue;
  }

  public void resetNumericValue ()
  {
    mNumericValue = null;
  }

  public boolean isNumeric ()
  {
    return mNumericValue != null;
  }

  public T getAdditionalData ()
  {
    return mAdditionalData;
  }

  public TableCellData<T> setAdditionalData (T additional)
  {
    mAdditionalData = additional;
    return this;
  }

  @Override
  public String toString ()
  {
    if (mNumericValue != null) return mNumericValue.toPlainString ();
    return mStringValue;
  }
}
