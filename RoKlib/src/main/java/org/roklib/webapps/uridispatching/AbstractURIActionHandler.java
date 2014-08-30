/*
 * Copyright (C) 2007 - 2010 Roland Krueger 
 * Created on 11.02.2010 
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

import org.roklib.conditional.engine.AbstractCondition;
import org.roklib.util.helper.CheckForNull;
import org.roklib.webapps.uridispatching.parameters.EnumURIParameterErrors;
import org.roklib.webapps.uridispatching.parameters.URIParameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.*;

public abstract class AbstractURIActionHandler implements URIActionHandler {
    private static final long serialVersionUID = 8450975393827044559L;

    private static final String[] STRING_ARRAY_PROTOTYPE = new String[]{};
    private static final Logger LOG = LoggerFactory
            .getLogger(AbstractURIActionHandler.class);
    private List<CommandForCondition> commandsForCondition;
    private List<URIParameter<?>> uriParameters;
    private List<String> actionArgumentOrder;
    protected List<URIActionHandler> handlerChain;
    private Map<String, List<Serializable>> actionArgumentMap;
    protected AbstractURIActionHandler parentHandler;
    private AbstractURIActionCommand defaultCommand;

    /**
     * The name of the URI portion for which this action handler is responsible.
     */
    protected String actionName;
    private String actionURI;
    private boolean caseSensitive = false;
    private boolean useHashExclamationMarkNotation = false;
    private Locale locale;

    /**
     * Creates a new action handler with the given action name. The action name must not be <code>null</code>. This name
     * identifies the fragment of a URI which is handled by this action handler. For example, if this action handler is
     * responsible for the <code>admin</code> part in the following URI
     * <p/>
     * <pre>
     * http://www.example.com/admin/settings
     * </pre>
     * <p/>
     * then the action name for this handler has to be set to <code>admin</code> as well.
     *
     * @param actionName the name of the URI portion for which this action handler is responsible. Must not be <code>null</code>.
     */
    public AbstractURIActionHandler(String actionName) {
        CheckForNull.check(actionName);
        this.actionName = actionName;
        actionURI = actionName;
        defaultCommand = null;
    }

    protected void setUseHashExclamationMarkNotation(boolean useHashExclamationMarkNotation) {
        this.useHashExclamationMarkNotation = useHashExclamationMarkNotation;
    }

    /**
     * <p>
     * Sets the case sensitivity of this action handler. A case insentitive action handler will match a URI token without
     * regarding the token's case. You have to be careful with case insensitive action handlers if you have more than one
     * action handler with action names differing only in case. You might get unexpected results since one action handler
     * might shadow the other.
     * </p>
     */
    protected void setCaseSensitive(boolean caseSensitive) {
        this.caseSensitive = caseSensitive;
    }

    public boolean isCaseSensitive() {
        return caseSensitive;
    }

    public String getActionName() {
        return actionName;
    }

    public String getCaseInsensitiveActionName() {
        return actionName.toLowerCase(getLocale());
    }

    public void setDefaultActionCommand(AbstractURIActionCommand command) {
        defaultCommand = command;
    }

    protected AbstractURIActionCommand getDefaultCommand() {
        return defaultCommand;
    }

    protected void registerURIParameter(URIParameter<?> parameter) {
        if (parameter == null)
            return;
        if (uriParameters == null)
            uriParameters = new LinkedList<URIParameter<?>>();
        if (!uriParameters.contains(parameter))
            uriParameters.add(parameter);
    }

    protected void registerURIParameter(URIParameter<?> parameter, boolean isOptional) {
        registerURIParameter(parameter);
        parameter.setOptional(isOptional);
    }

    protected boolean haveRegisteredURIParametersErrors() {
        if (uriParameters == null)
            return false;
        boolean result = false;

        for (URIParameter<?> parameter : uriParameters) {
            result |= parameter.getError() != EnumURIParameterErrors.NO_ERROR;
        }

        return result;
    }

    public final AbstractURIActionCommand handleURI(List<String> pUriTokens, Map<String, List<String>> pParameters,
                                                    ParameterMode pParameterMode) {
        if (commandsForCondition != null) {
            for (CommandForCondition cfc : commandsForCondition) {
                if (cfc.mCondition.getBooleanValue())
                    return cfc.mDefaultCommandForCondition;
            }
        }
        if (uriParameters != null) {
            if (pParameterMode == ParameterMode.QUERY) {
                for (URIParameter<?> parameter : uriParameters) {
                    parameter.clearValue();
                    parameter.consume(pParameters);
                }
            } else {
                List<String> parameterNames = new LinkedList<String>();
                for (URIParameter<?> parameter : uriParameters) {
                    parameterNames.addAll(parameter.getParameterNames());
                }
                if (pParameterMode == ParameterMode.DIRECTORY_WITH_NAMES) {
                    Map<String, List<String>> parameters = new HashMap<String, List<String>>(4);
                    String parameterName;
                    String value;
                    for (Iterator<String> it = pUriTokens.iterator(); it.hasNext(); ) {
                        parameterName = it.next();
                        try {
                            parameterName = URLDecoder.decode(parameterName, "UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            // nothing to do, parameterName stays encoded
                        }
                        value = "";
                        if (parameterNames.contains(parameterName)) {
                            it.remove();
                            if (it.hasNext()) {
                                value = it.next();
                                try {
                                    value = URLDecoder.decode(value, "UTF-8");
                                } catch (UnsupportedEncodingException e) {
                                    // nothing to do, value stays encoded
                                }
                                it.remove();
                            }
                            List<String> values = parameters.get(parameterName);
                            if (values == null) {
                                values = new LinkedList<String>();
                                parameters.put(parameterName, values);
                            }
                            values.add(value);
                        }
                    }
                    for (URIParameter<?> parameter : uriParameters) {
                        parameter.clearValue();
                        parameter.consume(parameters);
                    }
                } else {
                    List<String> valueList = new LinkedList<String>();
                    for (URIParameter<?> parameter : uriParameters) {
                        parameter.clearValue();
                        if (pUriTokens.isEmpty())
                            continue;
                        valueList.clear();
                        int singleValueCount = parameter.getSingleValueCount();
                        int i = 0;
                        while (!pUriTokens.isEmpty() && i < singleValueCount) {
                            String token = pUriTokens.remove(0);
                            try {
                                token = URLDecoder.decode(token, "UTF-8");
                            } catch (UnsupportedEncodingException e) {
                                // nothing to do, token stays encoded
                            }
                            valueList.add(token);
                            ++i;
                        }
                        parameter.consumeList(valueList.toArray(new String[valueList.size()]));
                    }
                }
            }
        }

        if (handlerChain != null) {
            for (URIActionHandler chainedHandler : handlerChain) {
                if (LOG.isTraceEnabled()) {
                    LOG.trace("Executing chained handler " + chainedHandler + " (" + handlerChain.size()
                            + " chained handler(s) in list)");
                }
                AbstractURIActionCommand commandFromChain = chainedHandler.handleURI(pUriTokens, pParameters, pParameterMode);
                if (commandFromChain != null)
                    return commandFromChain;
            }
        }

        return handleURIImpl(pUriTokens, pParameters, pParameterMode);
    }

    protected abstract AbstractURIActionCommand handleURIImpl(List<String> uriTokens,
                                                              Map<String, List<String>> parameters, ParameterMode parameterMode);

    protected boolean isResponsibleForToken(String uriToken) {
        if (isCaseSensitive()) {
            return actionName.equals(uriToken);
        } else {
            return actionName.equalsIgnoreCase(uriToken);
        }
    }

    protected String urlEncode(String term) {
        try {
            return URLEncoder.encode(term, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // this should not happen
            return term;
        }
    }

    /**
     * Returns the full relative action URI for this action handler. This is the concatenation of all parent handler
     * action names going back to the handler root separated by a slash. For example, if this action handler's action name
     * is <code>languageSettings</code>, with its parent's action name <code>configuration</code> and the next parent's
     * action name <code>admin</code> then the action URI for this handler evaluates to
     * <p/>
     * <pre>
     * /admin/configuration/languageSettings.
     * </pre>
     * <p/>
     * This String is needed for generating fully configured URIs (this URI together with the corresponding parameter
     * values) which can be used for rendering links pointing to this action handler.
     *
     * @return the action URI for this action handler (such as <code>/admin/configuration/languageSettings</code> if this
     * action handler's action name is <code>languageSettings</code>).
     */
    public String getActionURI() {
        return actionURI;
    }

    /**
     * Sets the parent action handler for this object. An action handler can only be added as sub-handler to one action
     * handler. In other words, an action handler can only have one parent.
     *
     * @param parent the parent handler for this action handler
     */
    protected final void setParent(AbstractURIActionHandler parent) {
        parentHandler = parent;
    }

    public URI getParameterizedHashbangActionURI(boolean clearParametersAfterwards) {
        return getParameterizedHashbangActionURI(clearParametersAfterwards, ParameterMode.DIRECTORY_WITH_NAMES);
    }

    public URI getParameterizedHashbangActionURI(boolean clearParametersAfterwards, ParameterMode parameterMode) {
        return getParameterizedActionURI(clearParametersAfterwards, parameterMode, true, true);
    }

    public URI getParameterizedActionURI(boolean clearParametersAfterwards) {
        return getParameterizedActionURI(clearParametersAfterwards, ParameterMode.QUERY);
    }

    public URI getParameterizedActionURI(boolean clearParametersAfterwards, ParameterMode parameterMode) {
        return getParameterizedActionURI(clearParametersAfterwards, parameterMode, false);
    }

    public URI getParameterizedActionURI(boolean clearParametersAfterwards, ParameterMode parameterMode,
                                         boolean addHashMark) {
        return getParameterizedActionURI(clearParametersAfterwards, parameterMode, addHashMark,
                useHashExclamationMarkNotation);
    }

    private URI getParameterizedActionURI(boolean clearParametersAfterwards, ParameterMode parameterMode,
                                          boolean addHashMark, boolean addExclamationMark) {
        StringBuilder buf = new StringBuilder();
        if (addHashMark) {
            buf.append('#');
            if (addExclamationMark) {
                buf.append('!');
            }
            buf.append(getActionURI().substring(1));
        } else {
            buf.append(getActionURI());
        }

        boolean removeLastCharacter = false;
        if (actionArgumentMap != null && !actionArgumentMap.isEmpty()) {
            if (parameterMode == ParameterMode.QUERY) {
                buf.append('?');
                for (String argument : actionArgumentOrder) {
                    for (Serializable value : actionArgumentMap.get(argument)) {
                        buf.append(urlEncode(argument)).append('=').append(urlEncode(value.toString()));
                        buf.append('&');
                        removeLastCharacter = true;
                    }
                }
            } else {
                buf.append('/');
                for (String argument : actionArgumentOrder) {
                    for (Serializable value : actionArgumentMap.get(argument)) {
                        if (parameterMode == ParameterMode.DIRECTORY_WITH_NAMES) {
                            buf.append(urlEncode(argument)).append('/');
                        }
                        buf.append(urlEncode(value.toString()));
                        buf.append('/');
                        removeLastCharacter = true;
                    }
                }
            }
        }

        if (removeLastCharacter)
            buf.setLength(buf.length() - 1);

        try {
            return new URI(buf.toString());
        } catch (URISyntaxException e) {
            throw new RuntimeException("Unable to create URL object.", e);
        } finally {
            if (clearParametersAfterwards) {
                clearActionArguments();
            }
        }
    }

    public final void addDefaultCommandForCondition(AbstractURIActionCommand command, AbstractCondition condition) {
        CheckForNull.check(command, condition);
        if (commandsForCondition == null)
            commandsForCondition = new LinkedList<CommandForCondition>();
        CommandForCondition cfc = new CommandForCondition();
        cfc.mDefaultCommandForCondition = command;
        cfc.mCondition = condition;
        commandsForCondition.add(cfc);
    }

    public void addToHandlerChain(URIActionHandler handler) {
        CheckForNull.check(handler);
        if (handlerChain == null) {
            handlerChain = new LinkedList<URIActionHandler>();
        }
        handlerChain.add(handler);
    }

    /**
     * <code>null</code> argument values are ignored.
     *
     * @param argumentName
     * @param argumentValues
     */
    public void addActionArgument(String argumentName, Serializable... argumentValues) {
        CheckForNull.check(argumentName);
        if (actionArgumentMap == null) {
            actionArgumentMap = new HashMap<String, List<Serializable>>(4);
            actionArgumentOrder = new LinkedList<String>();
        }

        List<Serializable> valueList = actionArgumentMap.get(argumentName);
        if (valueList == null) {
            valueList = new LinkedList<Serializable>();
            actionArgumentMap.put(argumentName, valueList);
        }
        for (Serializable value : argumentValues) {
            if (value != null)
                valueList.add(value);
        }
        if (valueList.isEmpty()) {
            actionArgumentMap.remove(argumentName);
        } else if (!actionArgumentOrder.contains(argumentName)) {
            actionArgumentOrder.add(argumentName);
        }
    }

    public void clearActionArguments() {
        if (actionArgumentMap != null) {
            actionArgumentMap.clear();
            actionArgumentOrder.clear();
        }
    }

    public final void clearDefaultCommands() {
        commandsForCondition.clear();
    }

    private static class CommandForCondition implements Serializable {
        private static final long serialVersionUID = 2090692709855753816L;

        private AbstractURIActionCommand mDefaultCommandForCondition;

        private AbstractCondition mCondition;
    }

    public void getActionURIOverview(List<String> targetList) {
        StringBuilder buf = new StringBuilder();
        buf.append(getActionURI());

        if (uriParameters != null && uriParameters.size() > 0) {
            buf.append(" ? ");
            for (URIParameter<?> parameter : uriParameters) {
                buf.append(parameter).append(", ");
            }
            buf.setLength(buf.length() - 2);
        }
        if (buf.length() > 0)
            targetList.add(buf.toString());
        for (AbstractURIActionHandler subHandler : getSubHandlerMap().values()) {
            subHandler.getActionURIOverview(targetList);
        }
    }

    /**
     * Returns a map of all registered sub-handlers for this URI action handler. This method is only implemented by
     * {@link DispatchingURIActionHandler} since this is the only URI action handler implementation in the framework which
     * can have sub-handlers. All other subclasses of {@link AbstractURIActionHandler} return an empty map.
     *
     * @return map containing a mapping of URI tokens on the corresponding sub-handlers that handle these tokens.
     */
    protected Map<String, AbstractURIActionHandler> getSubHandlerMap() {
        return Collections.emptyMap();
    }

    public boolean hasSubHandlers() {
        return !getSubHandlerMap().isEmpty();
    }

    protected void setSubhandlersActionURI(AbstractURIActionHandler subHandler) {
        subHandler.setActionURI(String.format("%s%s%s", getActionURI(), "/", urlEncode(subHandler.actionName)));
        if (subHandler.hasSubHandlers()) {
            subHandler.updateActionURIs();
        }
    }

    protected void updateActionURIs() {
        setActionURI(parentHandler.getActionURI() + "/" + actionName);
        for (AbstractURIActionHandler subHandler : getSubHandlerMap().values()) {
            setSubhandlersActionURI(subHandler);
        }
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public Locale getLocale() {
        return locale == null ? Locale.getDefault() : locale;
    }

    protected void setActionURI(String actionURI) {
        this.actionURI = actionURI;
    }

    @Override
    public String toString() {
        return String.format("%s='%s'", getClass().getSimpleName(), actionName);
    }
}
