/*
 * Copyright (C) 2007 Roland Krueger
 * Created on 11.12.2009
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
package org.roklib.util.data;


import java.io.Serializable;

import org.roklib.util.helper.CheckForNull;

/**
 * Wrapper class for a single data value that monitors the status of and access to this value. A {@link ManagedData}
 * holds one piece of data that can be of an arbitrary type. It provides additional services for this data that is
 * otherwise not available or only in a restricted way to the data object itself.
 * <p>
 * One recurrent problem with data that does not necessarily need to be initialized is for external code to determine
 * whether the value is valid or wasn't initialized yet. When the data is available as an object, one could test if the
 * object reference is <code>null</code>. This isn't possible any longer in the instance that the <code>null</code>
 * value is a valid value for the object. The same applies to primitive data types. It is very clumsy if not impossible
 * to define a specific value, such as <code>0</code>, as a marker for the status of the primitive value.
 * <p>
 * Class {@link ManagedData} alleviates this problem. It encapsulates a single value together with its status without
 * reserving a special value as a status label. Thus, every possible value, such as <code>null</code> or <code>0</code>
 * can be used as valid data for the encapsulated object. Furthermore, {@link ManagedData} lets you query the status of
 * the encapsulated data object. You can determine whether the object has been set, deleted or changed, for instance.
 * <p>
 * Another feature of {@link ManagedData} is that it allows the locking of the underlying data object. That is, the
 * owner of the {@link ManagedData} can hold an exclusive lock on the value, that prevents other objects from changing
 * the data of the {@link ManagedData}.
 * 
 * @author Roland Krueger
 * @param T
 *          the data value that this class is supposed to manage
 */
public class ManagedData<T> implements Serializable
{
  private static final long serialVersionUID = -6712211524960434226L;

  /**
   * State of the encapsulated data object. The data can be in one of four states:
   * <ul>
   * <li>{@link ManagedData.StatusEnum#UNDEFINED} - the value hasn't been initialized yet. Trying to access it will
   * result in an {@link IllegalStateException}.</li>
   * <li>{@link ManagedData.StatusEnum#SET} - the value has been set and can be queried with
   * {@link ManagedData#getValue()}</li>
   * <li>{@link ManagedData.StatusEnum#DELETED} - the value has been deleted. Trying to access it will result in an
   * {@link IllegalStateException}.</li>
   * <li>{@link ManagedData.StatusEnum#CHANGED} - the value has been changed. It can still be accessed.</li>
   * </ul>
   */
  public enum StatusEnum
  {
    UNDEFINED, SET, DELETED, CHANGED
  }

  private T          mValue;
  private StatusEnum mStatus;
  private Object     mWriteLock;

  /**
   * Default constructor. Sets the state of the underlying data object to {@link StatusEnum#UNDEFINED}.
   */
  public ManagedData ()
  {
    mStatus = StatusEnum.UNDEFINED;
  }

  /**
   * Creates a new {@link ManagedData} with an initial value object. The state of the newly created {@link ManagedData}
   * is {@link ManagedData.StatusEnum#SET}.
   * 
   * @param initialValue
   *          the initial value object
   */
  public ManagedData (T initialValue)
  {
    setValueInternal (initialValue);
  }

  /**
   * Returns <code>true</code> if the current state of the {@link ManagedData} is either
   * {@link ManagedData.StatusEnum#SET} or {@link ManagedData.StatusEnum#CHANGED}.
   * 
   * @return <code>true</code> if the current state of the {@link ManagedData} is either
   *         {@link ManagedData.StatusEnum#SET} or {@link ManagedData.StatusEnum#CHANGED}.
   */
  public boolean canRead ()
  {
    return isSet () || mStatus == StatusEnum.CHANGED;
  }

  /**
   * Unsets the data object. The new state of the {@link ManagedData} is {@link ManagedData.StatusEnum#DELETED}.
   */
  public void unset ()
  {
    mStatus = StatusEnum.DELETED;
    mValue = null;
  }

  /**
   * Returns the current state of the data value.
   * 
   * @return the current state of the data value.
   * @see ManagedData.StatusEnum
   */
  public StatusEnum getState ()
  {
    return mStatus;
  }

  /**
   * Returns the value object that is managed by this {@link ManagedData}.
   * 
   * @return the value object that is managed by this {@link ManagedData}.
   * @throws IllegalStateException
   *           if the value either hasn't been initialized yet or was deleted with {@link ManagedData#unset()}
   */
  public T getValue ()
  {
    if (mStatus == StatusEnum.UNDEFINED)
    {
      throw new IllegalStateException ("Unable to get value: value is still undefined.");
    }
    if (mStatus == StatusEnum.DELETED)
    {
      throw new IllegalStateException ("Unable to get value: value has been deleted.");
    }

    return mValue;
  }

  /**
   * Copy constructor. Copies the exact state of the other {@link ManagedData} into this object.
   * 
   * @param other
   *          an other {@link ManagedData}
   */
  public ManagedData (ManagedData<T> other)
  {
    this ();
    mValue = other.mValue;
    mStatus = other.mStatus;
    mWriteLock = other.mWriteLock;
  }

  /**
   * Locks a {@link ManagedData}. By setting a lock, an owner of the {@link ManagedData} can assure that no other
   * external process can alter the {@link ManagedData}'s data object or change its state. If that is being attempted by
   * an object that doesn't own the lock key, an {@link IllegalStateException} will be raised. Unlocking the
   * {@link ManagedData} is only possible by passing the key object to the respective unlock methods.
   * 
   * @param writeLock
   *          an arbitrary object instance that functions as the key for this lock
   * @throws NullPointerException
   *           if the key object is <code>null</code>
   * @see ManagedData#setValue(Object)
   * @see ManagedData#setValue(Object, Object)
   */
  public void lock (Object writeLock)
  {
    CheckForNull.check (writeLock);
    mWriteLock = writeLock;
  }

  /**
   * Returns <code>true</code> if the {@link ManagedData} is currently locked.
   */
  public boolean isLocked ()
  {
    return mWriteLock != null;
  }

  /**
   * Resets the state of the data value. This will only have any effect if the current status is
   * {@link ManagedData.StatusEnum#CHANGED}. If that is the case the state is reset to
   * {@link ManagedData.StatusEnum#SET}. Thus, {@link ManagedData#reset()} can be used to acknowledge that the
   * {@link ManagedData}'s data has been changed.
   */
  public void reset ()
  {
    if (mStatus == StatusEnum.CHANGED)
    {
      mStatus = StatusEnum.SET;
    }
  }

  /**
   * Sets the value of the data object. The {@link ManagedData} must not be locked when this method is called. The new
   * state of the value object is either {@link ManagedData.StatusEnum#SET} if it was undefined or deleted before this
   * operation or {@link ManagedData.StatusEnum#CHANGED} if it was already set.
   * 
   * @param value
   * @throws IllegalStateException
   *           if this {@link ManagedData} is locked
   */
  public void setValue (T value)
  {
    CheckForNull.check (value);
    if (isLocked ())
    {
      throw new IllegalStateException ("Value object is currently locked.");
    }
    setValueInternal (value);
  }

  /**
   * Sets the value of the data object. This method can be used if and only if the {@link ManagedData} is currently
   * locked. The lock's key is provided as the second parameter. The new state of the value object is either
   * {@link ManagedData.StatusEnum#SET} if it was undefined or deleted before this operation or
   * {@link ManagedData.StatusEnum#CHANGED} if it was already set.
   * 
   * @param value
   *          a new value
   * @param lockKey
   *          the object that was used to lock the {@link ManagedData}
   * @throws IllegalStateException
   *           if the {@link ManagedData} isn't currently locked
   * @throws IllegalArgumentException
   *           if the passed lock key is not the same object instance as the key object that was used to set the lock
   * @see ManagedData#unlock(Object)
   */
  public void setValue (T value, Object lockKey)
  {
    if (!isLocked ())
    {
      throw new IllegalStateException ("Cannot unlock value: object is not locked.");
    }
    if (mWriteLock != lockKey)
    {
      throw new IllegalArgumentException ("Cannot set value: incorrect lock key.");
    }
    setValueInternal (value);
  }

  private void setValueInternal (T value)
  {
    if (value == null)
    {
      throw new IllegalArgumentException ("Can't overwrite value with null. Use unset() instead.");
    }

    mValue = value;
    if (mStatus == StatusEnum.SET || mStatus == StatusEnum.CHANGED)
    {
      mStatus = StatusEnum.CHANGED;
    } else
    {
      mStatus = StatusEnum.SET;
    }
  }

  /**
   * Returns <code>true</code> if the current state of the {@link ManagedData} is {@link ManagedData.StatusEnum#SET}.
   * 
   * @return <code>true</code> if the current state of the {@link ManagedData} is {@link ManagedData.StatusEnum#SET}.
   */
  public boolean isSet ()
  {
    return mStatus == StatusEnum.SET;
  }

  /**
   * Returns <code>true</code> if the current state of the {@link ManagedData} is {@link ManagedData.StatusEnum#CHANGED}
   * .
   * 
   * @return <code>true</code> if the current state of the {@link ManagedData} is {@link ManagedData.StatusEnum#CHANGED}
   *         .
   */
  public boolean isChanged ()
  {
    return mStatus == StatusEnum.CHANGED;
  }

  /**
   * Returns <code>true</code> if the current state of the {@link ManagedData} is
   * {@link ManagedData.StatusEnum#UNDEFINED}.
   * 
   * @return <code>true</code> if the current state of the {@link ManagedData} is
   *         {@link ManagedData.StatusEnum#UNDEFINED}.
   */
  public boolean isUndefined ()
  {
    return mStatus == StatusEnum.UNDEFINED;
  }

  /**
   * Returns <code>true</code> if the current state of the {@link ManagedData} is {@link ManagedData.StatusEnum#DELETED}
   * .
   * 
   * @return <code>true</code> if the current state of the {@link ManagedData} is {@link ManagedData.StatusEnum#DELETED}
   *         .
   */
  public boolean isDeleted ()
  {
    return mStatus == StatusEnum.DELETED;
  }

  @Override
  @SuppressWarnings (value = "all")
  public boolean equals (Object obj)
  {
    if (obj == null)
    {
      return false;
    }
    if (obj == this)
    {
      return true;
    }
    if (obj instanceof ManagedData)
    {
      ManagedData other = (ManagedData) obj;
      if (!(mStatus == StatusEnum.SET || mStatus == StatusEnum.CHANGED))
        return false;
      if (!(other.mStatus == StatusEnum.SET || other.mStatus == StatusEnum.CHANGED))
        return false;

      return mValue.equals (other.mValue);
    }
    return false;
  }

  @Override
  public int hashCode ()
  {
    if (mStatus == StatusEnum.SET || mStatus == StatusEnum.CHANGED)
    {
      return mValue.hashCode ();
    } else
    {
      return super.hashCode ();
    }
  }

  @Override
  public String toString ()
  {
    if (canRead ())
    {
      return mValue.toString ();
    }
    return String.format ("%s:%s", super.toString (), mStatus.toString ());
  }

  /**
   * Unlocks a previously locked {@link ManagedData}. Unlock will only succeed if the given key has the same object
   * reference as the key object that was used to lock the {@link ManagedData} with {@link ManagedData#lock(Object)}.
   * The given key is compared with the lock key with the <code>==</code> operator.
   * 
   * @param writeLock
   *          the object that was used to lock the {@link ManagedData}
   * @throws IllegalStateException
   *           if the {@link ManagedData} isn't currently locked
   * @throws IllegalArgumentException
   *           if the passed lock key is not the same object instance as the key object that was used to set the lock
   * @see ManagedData#lock(Object)
   */
  public void unlock (Object writeLock)
  {
    if (!isLocked ())
    {
      throw new IllegalStateException ("Cannot unlock value: object is not locked.");
    }
    if (mWriteLock != writeLock)
    {
      throw new IllegalArgumentException ("Cannot unlock value: wrong key.");
    }
    mWriteLock = null;
  }
}
