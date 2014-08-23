/*
 * Copyright (C) 2007 - 2010
 * Roland Krueger Created on 10.03.2010
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
package org.roklib.webapps.uridispatching;

import java.util.List;
import java.util.Map;

/**
 * A simple URI action handler that directly returns a predefined action command when the URI interpretation process
 * reaches this handler. By that, {@link SimpleURIActionHandler}s always represent the last token of an interpreted URI
 * as they cannot dispatch to any sub-handlers.
 *
 * @author Roland Krüger
 */
public class SimpleURIActionHandler extends AbstractURIActionHandler {
    private static final long serialVersionUID = 8203362201388037000L;

    private AbstractURIActionCommand mCommand;

    /**
     * Create a new {@link SimpleURIActionHandler} with the specified action name and action command.
     *
     * @param actionName the action name for this handler
     * @param command    action command to be returned when this action handler is being evaluated
     */
    public SimpleURIActionHandler(String actionName, AbstractURIActionCommand command) {
        super(actionName);
        mCommand = command;
    }

    /**
     * Directly returns the URI action command passed in through the constructor. All method arguments are ignored.
     */
    @Override
    protected AbstractURIActionCommand handleURIImpl(List<String> uriTokens, Map<String, List<String>> parameters,
                                                     ParameterMode parameterMode) {
        return mCommand;
    }
}
