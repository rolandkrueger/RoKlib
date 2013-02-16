/*
 * Copyright (C) 2007-2010 Roland Krueger
 * Created on 02.02.2010
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
package info.rolandkrueger.roklib.util.tables.filtertable;

import java.io.Serializable;
import java.math.BigDecimal;

public class TableCellData<T> implements Serializable
{
  private static final long serialVersionUID = 1794744722319758347L;

  private String            mStringValue;
  private BigDecimal        mNumericValue;
  private T                 mAdditionalData;

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
    if (!isNumeric ())
      throw new IllegalStateException ("This data object has no numeric value.");
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
    if (mNumericValue != null)
      return mNumericValue.toPlainString ();
    return mStringValue;
  }
}
