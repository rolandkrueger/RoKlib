/*
 * $Id: ApplicationError.java 178 2010-10-31 18:01:20Z roland $
 * Copyright (C) 2007 Roland Krueger
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
package info.rolandkrueger.roklib.util;

/**
 * This class summarizes all information that is necessary to appropriately
 * respond to an error situation. Objects of this class are passed to classes
 * implementing the {@link ApplicationMessageHandler} interface.
 * 
 * @see ApplicationMessageHandler
 * @author Roland Krueger
 */
public class ApplicationError
{
  /** Defines the seriousness of an error. */
  public enum ErrorType
  {
    /** Uncritical errors may be ignored. */
    UNCRITICAL,
    /**
     * Errors with a warning level indicate situations that have to be responded
     * to with appropriate actions.
     */
    WARNING,
    /**
     * Severe errors are types of errors that usually make it impossible for the
     * application to continue running.
     */
    SEVERE
  };

  private String mDescription;
  private Throwable mCause;
  private ErrorType mType;

  /**
   * Default constructor that initializes the error level with
   * {@link ErrorType#UNCRITICAL}.
   */
  protected ApplicationError ()
  {
    this (ErrorType.UNCRITICAL);
  }

  /**
   * Constructor for setting the error level.
   * 
   * @param type
   *          seriousness of the error
   */
  protected ApplicationError (ErrorType type)
  {
    mType = type;
  }

  /**
   * Constructor for setting the description of an error. The error's level
   * defaults to {@link ErrorType#UNCRITICAL}.
   * 
   * @param description
   *          an error description. This can later be used as the error message.
   */
  public ApplicationError (String description)
  {
    this (description, null, ErrorType.UNCRITICAL);
  }

  /**
   * Constructor for setting both the description of an error and its cause. If
   * the error happened due to an exception, this exception can be passed along
   * with the {@link ApplicationError} object. The error's level defaults to
   * {@link ErrorType#UNCRITICAL}.
   * 
   * @param description
   *          an error description. This can later be used as the error message.
   * @param cause
   *          exception that caused this error to be created
   */
  public ApplicationError (String description, Throwable cause)
  {
    this (description, cause, ErrorType.UNCRITICAL);
  }

  /**
   * Constructor for setting both the description of an error and its type.
   * 
   * @param description
   *          an error description. This can later be used as the error message.
   * @param type
   *          seriousness of the error
   */
  public ApplicationError (String description, ErrorType type)
  {
    this (description, null, type);
  }

  /**
   * Constructor for setting the description, the cause and the type of the
   * error.
   * 
   * @see ApplicationError#ApplicationError(String, Throwable)
   * @param description
   *          an error description. This can later be used as the error message.
   * @param cause
   *          exception that caused this error to be created
   * @param type
   *          seriousness of the error
   */
  public ApplicationError (String description, Throwable cause, ErrorType type)
  {
    this (type);
    mDescription = description;
    mCause = cause;
  }

  /**
   * Returns the error message.
   * 
   * @return the error message
   */
  public String getDescription ()
  {
    return mDescription;
  }

  /**
   * Returns the exception that caused this error.
   * 
   * @see ApplicationError#ApplicationError(String, Throwable)
   * @return the cause of this error or <code>null</code> if no such information
   *         was provided
   */
  public Throwable getCause ()
  {
    return mCause;
  }

  /**
   * Returns the error level.
   * 
   * @return the error level
   */
  public ErrorType getType ()
  {
    return mType;
  }
}
