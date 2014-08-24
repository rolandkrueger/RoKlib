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
package org.roklib.util;

/**
 * This class summarizes all information that is necessary to appropriately respond to an error situation. Objects of
 * this class are passed to classes implementing the {@link ApplicationMessageHandler} interface.
 *
 * @author Roland Krueger
 * @see ApplicationMessageHandler
 */
public class ApplicationError {
    /**
     * Defines the seriousness of an error.
     */
    public enum ErrorType {
        /**
         * Uncritical errors may be ignored.
         */
        UNCRITICAL,
        /**
         * Errors with a warning level indicate situations that have to be responded to with appropriate actions.
         */
        WARNING,
        /**
         * Severe errors are types of errors that usually make it impossible for the application to continue running.
         */
        SEVERE
    }

    private String mDescription;
    private Throwable mCause;
    private final ErrorType mType;

    /**
     * Default constructor that initializes the error level with {@link ErrorType#UNCRITICAL}.
     */
    protected ApplicationError() {
        this(ErrorType.UNCRITICAL);
    }

    /**
     * Constructor for setting the error level.
     *
     * @param type seriousness of the error
     */
    protected ApplicationError(ErrorType type) {
        mType = type;
    }

    /**
     * Constructor for setting the description of an error. The error's level defaults to {@link ErrorType#UNCRITICAL}.
     *
     * @param description an error description. This can later be used as the error message.
     */
    public ApplicationError(String description) {
        this(description, null, ErrorType.UNCRITICAL);
    }

    /**
     * Constructor for setting both the description of an error and its cause. If the error happened due to an exception,
     * this exception can be passed along with the {@link ApplicationError} object. The error's level defaults to
     * {@link ErrorType#UNCRITICAL}.
     *
     * @param description an error description. This can later be used as the error message.
     * @param cause       exception that caused this error to be created
     */
    public ApplicationError(String description, Throwable cause) {
        this(description, cause, ErrorType.UNCRITICAL);
    }

    /**
     * Constructor for setting both the description of an error and its type.
     *
     * @param description an error description. This can later be used as the error message.
     * @param type        seriousness of the error
     */
    public ApplicationError(String description, ErrorType type) {
        this(description, null, type);
    }

    /**
     * Constructor for setting the description, the cause and the type of the error.
     *
     * @param description an error description. This can later be used as the error message.
     * @param cause       exception that caused this error to be created
     * @param type        seriousness of the error
     * @see ApplicationError#ApplicationError(String, Throwable)
     */
    public ApplicationError(String description, Throwable cause, ErrorType type) {
        this(type);
        mDescription = description;
        mCause = cause;
    }

    /**
     * Returns the error message.
     *
     * @return the error message
     */
    public String getDescription() {
        return mDescription;
    }

    /**
     * Returns the exception that caused this error.
     *
     * @return the cause of this error or <code>null</code> if no such information was provided
     * @see ApplicationError#ApplicationError(String, Throwable)
     */
    public Throwable getCause() {
        return mCause;
    }

    /**
     * Returns the error level.
     *
     * @return the error level
     */
    public ErrorType getType() {
        return mType;
    }
}
