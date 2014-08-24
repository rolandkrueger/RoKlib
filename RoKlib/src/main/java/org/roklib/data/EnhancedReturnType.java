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
package org.roklib.data;

import org.roklib.util.helper.CheckForNull;

import java.io.Serializable;

public class EnhancedReturnType<T_ReturnType> implements Serializable {
    private static final long serialVersionUID = -6438018684249409869L;

    private boolean mSuccessful;

    private StringBuffer mMessage;

    private T_ReturnType mResultValue;

    private Throwable mException;

    private boolean mIsVoided;

    protected EnhancedReturnType() {
        mIsVoided = false;
        mResultValue = null;
    }

    public EnhancedReturnType(Throwable exception, String message) {
        this();
        mException = exception;
        mMessage = new StringBuffer(message);
    }

    public boolean isFailure() {
        return !mSuccessful;
    }

    public String getMessage() {
        return mMessage == null ? "" : mMessage.toString();
    }

    public T_ReturnType getValue() {
        if (isVoided()) {
            throw new IllegalStateException(
                    "Unable to provide value: object was voided. Use isVoid() to check this condition.");
        }

        return mResultValue;
    }

    private void voidResult() {
        setSuccess(true);
        mIsVoided = true;
    }

    public boolean isVoided() {
        return mIsVoided;
    }

    private EnhancedReturnType<T_ReturnType> setValue(T_ReturnType value) {
        CheckForNull.check(value);
        mResultValue = value;
        return this;
    }

    private EnhancedReturnType<T_ReturnType> setSuccess(boolean successful) {
        mSuccessful = successful;
        return this;
    }

    public Throwable getException() {
        return mException;
    }

    private EnhancedReturnType<T_ReturnType> setException(Throwable exception) {
        mException = exception;
        return this;
    }

    public boolean hasException() {
        return mException != null;
    }

    private void appendToMessage(String text) {
        if (mMessage == null) {
            mMessage = new StringBuffer();
        }
        mMessage.append(text);
    }

    public boolean hasMessage() {
        return mMessage != null;
    }

    private EnhancedReturnType<T_ReturnType> setMessage(String pMessage) {
        String message = pMessage == null ? "" : pMessage;
        mMessage = new StringBuffer(message);
        return this;
    }

    private EnhancedReturnType<T_ReturnType> copy(EnhancedReturnType<?> other) {
        mException = other.mException;
        mIsVoided = other.mIsVoided;
        if (other.mMessage != null) {
            mMessage = new StringBuffer(other.mMessage.toString());
        }
        mSuccessful = other.mSuccessful;
        return this;
    }

    public boolean isSuccess() {
        return mSuccessful;
    }

    @Override
    public String toString() {
        return "EnhancedReturnType [successful=" + mSuccessful + ", message=" + mMessage + ", result=" + mResultValue
                + ", exception=" + mException + ", voided=" + mIsVoided + "]";
    }

    private static class BuilderImpl<T_ReturnType> {
        private final EnhancedReturnType<T_ReturnType> mResult;

        private BuilderImpl() {
            mResult = new EnhancedReturnType<T_ReturnType>();
        }

        private void setSuccess(boolean successful) {
            mResult.setSuccess(successful);
        }

        private void voidResult() {
            mResult.voidResult();
        }

        private EnhancedReturnType<T_ReturnType> build() {
            return mResult;
        }

        private void setValue(T_ReturnType result) {
            mResult.setValue(result);
            mResult.setSuccess(true);
        }

        private void setException(Throwable exception) {
            mResult.setException(exception);
            mResult.setSuccess(false);
        }

        private void appendToMessage(String fragment) {
            mResult.appendToMessage(fragment);
        }

        private void setMessage(String message) {
            mResult.setMessage(message);
        }
    }

    public static class Builder<T_ReturnType> {
        private final BuilderImpl<T_ReturnType> mBuilderImpl;

        private Builder() {
            mBuilderImpl = new BuilderImpl<T_ReturnType>();
        }

        public static <T_ReturnType> Builder<T_ReturnType> createNew(Class<T_ReturnType> clazz) {
            return new Builder<T_ReturnType>();
        }

        public static <T_ReturnType> Builder<T_ReturnType> createNew(EnhancedReturnType<T_ReturnType> prototype) {
            return new Builder<T_ReturnType>();
        }

        public static <T_ReturnType> SuccessBuilder<T_ReturnType> createSuccessful(Class<T_ReturnType> clazz) {
            return new SuccessBuilder<T_ReturnType>();
        }

        public static <T_ReturnType> EnhancedReturnType<T_ReturnType> createSuccessful(T_ReturnType value) {
            Builder<T_ReturnType> builder = new Builder<T_ReturnType>();
            builder.mBuilderImpl.setSuccess(true);
            builder.mBuilderImpl.setValue(value);
            return builder.mBuilderImpl.build();
        }

        public static <T_ReturnType> FailureBuilder<T_ReturnType> createFailed(Class<T_ReturnType> clazz) {
            return new FailureBuilder<T_ReturnType>();
        }

        public static <T_ReturnType> VoidBuilder<T_ReturnType> createVoided(Class<T_ReturnType> clazz) {
            return new VoidBuilder<T_ReturnType>();
        }

        public static <T_ReturnType> FinishedBuilder<T_ReturnType> createFromOther(EnhancedReturnType<T_ReturnType> other) {
            FinishedBuilder<T_ReturnType> lBuilder = new FinishedBuilder<T_ReturnType>(new BuilderImpl<T_ReturnType>());
            lBuilder.mBuilderImpl.mResult.copy(other);
            lBuilder.mBuilderImpl.mResult.setValue(other.getValue());
            return lBuilder;
        }

        public static <T_ReturnType> FinishedBuilder<T_ReturnType> createFromOtherIgnoreResult(Class<T_ReturnType> clazz,
                                                                                               EnhancedReturnType<?> other) {
            FinishedBuilder<T_ReturnType> lBuilder = new FinishedBuilder<T_ReturnType>(new BuilderImpl<T_ReturnType>());
            lBuilder.mBuilderImpl.mResult.copy(other);
            return lBuilder;
        }

        public Builder<T_ReturnType> withMessage(String message) {
            mBuilderImpl.setMessage(message);
            return this;
        }

        public FailureBuilder<T_ReturnType> withException(Throwable exception) {
            mBuilderImpl.setException(exception);
            return new FailureBuilder<T_ReturnType>(mBuilderImpl);
        }

        public Builder<T_ReturnType> appendToMessage(String fragment) {
            mBuilderImpl.appendToMessage(fragment);
            return this;
        }

        public FinishedBuilder<T_ReturnType> withValue(T_ReturnType value) {
            mBuilderImpl.setValue(value);
            return new FinishedBuilder<T_ReturnType>(mBuilderImpl);
        }

        public SuccessBuilder<T_ReturnType> successful() {
            mBuilderImpl.setSuccess(true);
            return new SuccessBuilder<T_ReturnType>(mBuilderImpl);
        }

        public FailureBuilder<T_ReturnType> failed() {
            mBuilderImpl.setSuccess(false);
            return new FailureBuilder<T_ReturnType>(mBuilderImpl);
        }

        public VoidBuilder<T_ReturnType> voidResult() {
            mBuilderImpl.voidResult();
            return new VoidBuilder<T_ReturnType>(mBuilderImpl);
        }
    }

    public static class SuccessBuilder<T_ReturnType> {
        private BuilderImpl<T_ReturnType> mBuilderImpl;

        private SuccessBuilder() {
            mBuilderImpl = new BuilderImpl<T_ReturnType>();
            mBuilderImpl.setSuccess(true);
        }

        private SuccessBuilder(BuilderImpl<T_ReturnType> pBuilderImpl) {
            assert pBuilderImpl != null;
            mBuilderImpl = pBuilderImpl;
        }

        public SuccessBuilder<T_ReturnType> withMessage(String message) {
            mBuilderImpl.setMessage(message);
            return this;
        }

        public SuccessBuilder<T_ReturnType> appendToMessage(String fragment) {
            mBuilderImpl.appendToMessage(fragment);
            return this;
        }

        public FinishedBuilder<T_ReturnType> withValue(T_ReturnType value) {
            mBuilderImpl.setValue(value);
            return new FinishedBuilder<T_ReturnType>(mBuilderImpl);
        }

        public EnhancedReturnType<T_ReturnType> build() {
            return mBuilderImpl.build();
        }
    }

    public static class FailureBuilder<T_ReturnType> {
        private BuilderImpl<T_ReturnType> mBuilderImpl;

        private FailureBuilder() {
            mBuilderImpl = new BuilderImpl<T_ReturnType>();
            mBuilderImpl.setSuccess(false);
        }

        private FailureBuilder(BuilderImpl<T_ReturnType> pBuilderImpl) {
            assert pBuilderImpl != null;
            mBuilderImpl = pBuilderImpl;
        }

        public FailureBuilder<T_ReturnType> appendToMessage(String fragment) {
            mBuilderImpl.appendToMessage(fragment);
            return this;
        }

        public FailureBuilder<T_ReturnType> withException(Throwable exception) {
            mBuilderImpl.setException(exception);
            return this;
        }

        public FailureBuilder<T_ReturnType> withMessage(String message) {
            mBuilderImpl.setMessage(message);
            return this;
        }

        public EnhancedReturnType<T_ReturnType> build() {
            return mBuilderImpl.build();
        }
    }

    public static class VoidBuilder<T_ReturnType> {
        private BuilderImpl<T_ReturnType> mBuilderImpl;

        private VoidBuilder() {
            mBuilderImpl = new BuilderImpl<T_ReturnType>();
            mBuilderImpl.voidResult();
        }

        private VoidBuilder(BuilderImpl<T_ReturnType> pBuilderImpl) {
            assert pBuilderImpl != null;
            mBuilderImpl = pBuilderImpl;
        }

        public VoidBuilder<T_ReturnType> appendToMessage(String fragment) {
            mBuilderImpl.appendToMessage(fragment);
            return this;
        }

        public VoidBuilder<T_ReturnType> withMessage(String message) {
            mBuilderImpl.setMessage(message);
            return this;
        }

        public EnhancedReturnType<T_ReturnType> build() {
            return mBuilderImpl.build();
        }
    }

    public static class FinishedBuilder<T_ReturnType> {
        private BuilderImpl<T_ReturnType> mBuilderImpl;

        private FinishedBuilder(BuilderImpl<T_ReturnType> pBuilderImpl) {
            assert pBuilderImpl != null;
            mBuilderImpl = pBuilderImpl;
        }

        public EnhancedReturnType<T_ReturnType> build() {
            return mBuilderImpl.build();
        }

        public FinishedBuilder<T_ReturnType> appendToMessage(String fragment) {
            mBuilderImpl.appendToMessage(fragment);
            return this;
        }
    }
}
