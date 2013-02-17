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
package org.roklib.webapps.urldispatching;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.log4j.Logger;
import org.roklib.util.conditionalengine.AbstractCondition;
import org.roklib.util.helper.CheckForNull;
import org.roklib.util.net.IURLProvider;
import org.roklib.webapps.urldispatching.urlparameters.EnumURLParameterErrors;
import org.roklib.webapps.urldispatching.urlparameters.IURLParameter;

public abstract class AbstractURLActionHandler implements IURLActionHandler
{
  private static final long               serialVersionUID                = 8450975393827044559L;
  private static final String[]           STRING_ARRAY_PROTOTYPE          = new String[] {};
  private static final Logger             LOG                             = null;                // FIXME
  private List<CommandForCondition>       mCommandsForCondition;
  private List<IURLParameter<?>>          mURLParameters;
  private List<String>                    mActionArgumentOrder;
  protected List<IURLActionHandler>       mHandlerChain;
  private Map<String, List<Serializable>> mActionArgumentMap;
  protected AbstractURLActionHandler      mParentHandler;
  private AbstractURLActionCommand        mDefaultCommand;

  /**
   * The name of the URL portion for which this action handler is responsible.
   */
  protected String                        mActionName;
  private String                          mActionURI;
  private boolean                         mCaseSensitive                  = false;
  private boolean                         mUseHashExclamationMarkNotation = false;
  private Locale                          mLocale;

  /**
   * Creates a new action handler with the given action name. The action name must not be <code>null</code>. This name
   * identifies the fragment of a URL which is handled by this action handler. For example, if this action handler is
   * responsible for the <code>admin</code> part in the following URL
   * 
   * <pre>
   * http://www.example.com/admin/settings
   * </pre>
   * 
   * then the action name for this handler has to be set to <code>admin</code> as well.
   * 
   * @param actionName
   *          the name of the URL portion for which this action handler is responsible. Must not be <code>null</code>.
   */
  public AbstractURLActionHandler (String actionName)
  {
    CheckForNull.check (actionName);
    mActionName = actionName;
    mActionURI = actionName;
    mDefaultCommand = null;
  }

  protected void setUseHashExclamationMarkNotation (boolean useHashExclamationMarkNotation)
  {
    mUseHashExclamationMarkNotation = useHashExclamationMarkNotation;
  }

  /**
   * <p>
   * Sets the case sensitivity of this action handler. A case insentitive action handler will match a URI token without
   * regarding the token's case. You have to be careful with case insensitive action handlers if you have more than one
   * action handler with action names differing only in case. You might get unexpected results since one action handler
   * might shadow the other.
   * </p>
   */
  protected void setCaseSensitive (boolean caseSensitive)
  {
    mCaseSensitive = caseSensitive;
  }

  public boolean isCaseSensitive ()
  {
    return mCaseSensitive;
  }

  public String getActionName ()
  {
    return mActionName;
  }

  public String getCaseInsensitiveActionName ()
  {
    return mActionName.toLowerCase (getLocale ());
  }

  public void setDefaultActionCommand (AbstractURLActionCommand command)
  {
    mDefaultCommand = command;
  }

  protected AbstractURLActionCommand getDefaultCommand ()
  {
    return mDefaultCommand;
  }

  protected void registerURLParameter (IURLParameter<?> parameter)
  {
    if (parameter == null)
      return;
    if (mURLParameters == null)
      mURLParameters = new LinkedList<IURLParameter<?>> ();
    if (!mURLParameters.contains (parameter))
      mURLParameters.add (parameter);
  }

  protected void registerURLParameter (IURLParameter<?> parameter, boolean isOptional)
  {
    registerURLParameter (parameter);
    parameter.setOptional (isOptional);
  }

  protected boolean haveRegisteredURLParametersErrors ()
  {
    if (mURLParameters == null)
      return false;
    boolean result = false;

    for (IURLParameter<?> parameter : mURLParameters)
    {
      result |= parameter.getError () != EnumURLParameterErrors.NO_ERROR;
    }

    return result;
  }

  public final AbstractURLActionCommand handleURL (List<String> pUriTokens, Map<String, List<String>> pParameters,
      ParameterMode pParameterMode)
  {
    if (mCommandsForCondition != null)
    {
      for (CommandForCondition cfc : mCommandsForCondition)
      {
        if (cfc.mCondition.getBooleanValue () == true)
          return cfc.mDefaultCommandForCondition;
      }
    }
    if (mURLParameters != null)
    {
      if (pParameterMode == ParameterMode.QUERY)
      {
        for (IURLParameter<?> parameter : mURLParameters)
        {
          parameter.clearValue ();
          parameter.consume (pParameters);
        }
      } else
      {
        List<String> parameterNames = new LinkedList<String> ();
        for (IURLParameter<?> parameter : mURLParameters)
        {
          parameterNames.addAll (parameter.getParameterNames ());
        }
        if (pParameterMode == ParameterMode.DIRECTORY_WITH_NAMES)
        {
          Map<String, List<String>> parameters = new HashMap<String, List<String>> (4);
          String parameterName;
          String value;
          for (Iterator<String> it = pUriTokens.iterator (); it.hasNext ();)
          {
            parameterName = it.next ();
            try
            {
              parameterName = URLDecoder.decode (parameterName, "UTF-8");
            } catch (UnsupportedEncodingException e)
            {
              // nothing to do, parameterName stays encoded
            }
            value = "";
            if (parameterNames.contains (parameterName))
            {
              it.remove ();
              if (it.hasNext ())
              {
                value = it.next ();
                try
                {
                  value = URLDecoder.decode (value, "UTF-8");
                } catch (UnsupportedEncodingException e)
                {
                  // nothing to do, value stays encoded
                }
                it.remove ();
              }
              List<String> values = parameters.get (parameterName);
              if (values == null)
              {
                values = new LinkedList<String> ();
                parameters.put (parameterName, values);
              }
              values.add (value);
            }
          }
          for (IURLParameter<?> parameter : mURLParameters)
          {
            parameter.clearValue ();
            parameter.consume (parameters);
          }
        } else
        {
          List<String> valueList = new LinkedList<String> ();
          for (IURLParameter<?> parameter : mURLParameters)
          {
            parameter.clearValue ();
            if (pUriTokens.isEmpty ())
              continue;
            valueList.clear ();
            int singleValueCount = parameter.getSingleValueCount ();
            int i = 0;
            while (!pUriTokens.isEmpty () && i < singleValueCount)
            {
              String token = pUriTokens.remove (0);
              try
              {
                token = URLDecoder.decode (token, "UTF-8");
              } catch (UnsupportedEncodingException e)
              {
                // nothing to do, token stays encoded
              }
              valueList.add (token);
              ++i;
            }
            parameter.consumeList (valueList.toArray (STRING_ARRAY_PROTOTYPE));
          }
        }
      }
    }

    if (mHandlerChain != null)
    {
      for (IURLActionHandler chainedHandler : mHandlerChain)
      {
        if (LOG.isTraceEnabled ())
        {
          LOG.trace ("Executing chained handler " + chainedHandler + " (" + mHandlerChain.size ()
              + " chained handler(s) in list)");
        }
        AbstractURLActionCommand commandFromChain = chainedHandler.handleURL (pUriTokens, pParameters, pParameterMode);
        if (commandFromChain != null)
          return commandFromChain;
      }
    }

    return handleURLImpl (pUriTokens, pParameters, pParameterMode);
  }

  protected abstract AbstractURLActionCommand handleURLImpl (List<String> uriTokens,
      Map<String, List<String>> parameters, ParameterMode parameterMode);

  protected boolean isResponsibleForToken (String uriToken)
  {
    if (isCaseSensitive ())
    {
      return mActionName.equals (uriToken);
    } else
    {
      return mActionName.equalsIgnoreCase (uriToken);
    }
  }

  protected String urlEncode (String term)
  {
    try
    {
      return URLEncoder.encode (term, "UTF-8");
    } catch (UnsupportedEncodingException e)
    {
      // this should not happen
      return term;
    }
  }

  /**
   * Returns the full relative action URI for this action handler. This is the concatenation of all parent handler
   * action names going back to the handler root separated by a slash. For example, if this action handler's action name
   * is <code>languageSettings</code>, with its parent's action name <code>configuration</code> and the next parent's
   * action name <code>admin</code> then the action URI for this handler evaluates to
   * 
   * <pre>
   * /admin/configuration/languageSettings.
   * </pre>
   * 
   * This String is needed for generating fully configured URLs (this URI together with the corresponding parameter
   * values) which can be used for rendering links pointing to this action handler.
   * 
   * @return the action URI for this action handler (such as <code>/admin/configuration/languageSettings</code> if this
   *         action handler's action name is <code>languageSettings</code>).
   */
  public String getActionURI ()
  {
    return mActionURI;
  }

  /**
   * Sets the parent action handler for this object. An action handler can only be added as sub-handler to one action
   * handler. In other words, an action handler can only have one parent. This parent relationship is established when
   * adding a sub-handler to an action handler with
   * {@link AbstractURLActionHandler#addSubHandler(AbstractURLActionHandler)}.
   * 
   * @param parent
   *          the parent handler for this action handler
   */
  protected final void setParent (AbstractURLActionHandler parent)
  {
    mParentHandler = parent;
  }

  protected IURLProvider getContextURLProvider ()
  {
    if (mParentHandler != null)
    {
      return mParentHandler.getContextURLProvider ();
    } else
    {
      throw new IllegalStateException ("Unable to provide IURLProvider object: this URL "
          + "action handler or one of its ancestors hasn't yet been added to a " + URLActionDispatcher.class.getName ());
    }
  }

  public URL getParameterizedActionURL (boolean clearAfterwards)
  {
    return getParameterizedActionURL (clearAfterwards, ParameterMode.QUERY);
  }

  public URL getParameterizedActionURL (boolean clearAfterwards, ParameterMode parameterMode)
  {
    return getParameterizedActionURL (clearAfterwards, parameterMode, false);
  }

  public URL getParameterizedActionURL (boolean clearAfterwards, ParameterMode parameterMode, boolean addHashMark)
  {
    return getParameterizedActionURL (clearAfterwards, parameterMode, addHashMark, null);
  }

  public URL getParameterizedActionURL (boolean clearAfterwards, ParameterMode parameterMode, boolean addHashMark,
      URL baseURL)
  {
    StringBuilder buf = new StringBuilder ();
    if (addHashMark)
    {
      buf.append ('#');
      if (mUseHashExclamationMarkNotation)
      {
        buf.append ('!');
      }
      buf.append (getActionURI ().substring (1));
    } else
    {
      buf.append (getActionURI ());
    }

    boolean removeLastCharacter = false;
    if (mActionArgumentMap != null && !mActionArgumentMap.isEmpty ())
    {
      if (parameterMode == ParameterMode.QUERY)
      {
        buf.append ('?');
        for (String argument : mActionArgumentOrder)
        {
          for (Serializable value : mActionArgumentMap.get (argument))
          {
            buf.append (urlEncode (argument)).append ('=').append (urlEncode (value.toString ()));
            buf.append ('&');
            removeLastCharacter = true;
          }
        }
      } else
      {
        buf.append ('/');
        for (String argument : mActionArgumentOrder)
        {
          for (Serializable value : mActionArgumentMap.get (argument))
          {
            if (parameterMode == ParameterMode.DIRECTORY_WITH_NAMES)
            {
              buf.append (urlEncode (argument)).append ('/');
            }
            buf.append (urlEncode (value.toString ()));
            buf.append ('/');
            removeLastCharacter = true;
          }
        }
      }
    }

    if (removeLastCharacter)
      buf.setLength (buf.length () - 1);

    try
    {
      URL url = baseURL == null ? getContextURLProvider ().getURL () : baseURL;
      return new URL (url, buf.toString ());
    } catch (MalformedURLException e)
    {
      throw new RuntimeException ("Unable to create URL object.", e);
    } finally
    {
      if (clearAfterwards)
      {
        clearActionArguments ();
      }
    }
  }

  public final void addDefaultCommandForCondition (AbstractURLActionCommand command, AbstractCondition condition)
  {
    CheckForNull.check (command, condition);
    if (mCommandsForCondition == null)
      mCommandsForCondition = new LinkedList<CommandForCondition> ();
    CommandForCondition cfc = new CommandForCondition ();
    cfc.mDefaultCommandForCondition = command;
    cfc.mCondition = condition;
    mCommandsForCondition.add (cfc);
  }

  public void addToHandlerChain (IURLActionHandler handler)
  {
    CheckForNull.check (handler);
    if (mHandlerChain == null)
    {
      mHandlerChain = new LinkedList<IURLActionHandler> ();
    }
    mHandlerChain.add (handler);
  }

  /**
   * <code>null</code> argument values are ignored.
   * 
   * @param argumentName
   * @param argumentValues
   */
  public void addActionArgument (String argumentName, Serializable... argumentValues)
  {
    CheckForNull.check (argumentName);
    if (mActionArgumentMap == null)
    {
      mActionArgumentMap = new HashMap<String, List<Serializable>> (4);
      mActionArgumentOrder = new LinkedList<String> ();
    }

    List<Serializable> valueList = mActionArgumentMap.get (argumentName);
    if (valueList == null)
    {
      valueList = new LinkedList<Serializable> ();
      mActionArgumentMap.put (argumentName, valueList);
    }
    for (Serializable value : argumentValues)
    {
      if (value != null)
        valueList.add (value);
    }
    if (valueList.isEmpty ())
    {
      mActionArgumentMap.remove (argumentName);
    } else if (!mActionArgumentOrder.contains (argumentName))
    {
      mActionArgumentOrder.add (argumentName);
    }
  }

  public void clearActionArguments ()
  {
    if (mActionArgumentMap != null)
    {
      mActionArgumentMap.clear ();
      mActionArgumentOrder.clear ();
    }
  }

  public final void clearDefaultCommands ()
  {
    mCommandsForCondition.clear ();
  }

  private static class CommandForCondition implements Serializable
  {
    private static final long        serialVersionUID = 2090692709855753816L;

    private AbstractURLActionCommand mDefaultCommandForCondition;

    private AbstractCondition        mCondition;
  }

  public void getActionURLOverview (List<String> targetList)
  {
    StringBuilder buf = new StringBuilder ();
    buf.append (getActionURI ());

    if (mURLParameters != null && mURLParameters.size () > 0)
    {
      buf.append (" ? ");
      for (IURLParameter<?> parameter : mURLParameters)
      {
        buf.append (parameter).append (", ");
      }
      buf.setLength (buf.length () - 2);
    }
    if (buf.length () > 0)
      targetList.add (buf.toString ());
    for (AbstractURLActionHandler subHandler : getSubHandlerMap ().values ())
    {
      subHandler.getActionURLOverview (targetList);
    }
  }

  /**
   * Returns a map of all registered sub-handlers for this URL action handler. This method is only implemented by
   * {@link DispatchingURLActionHandler} since this is the only URL action handler implementation in the framework which
   * can have sub-handlers. All other subclasses of {@link AbstractURLActionHandler} return an empty map.
   * 
   * @return map containing a mapping of URI tokens on the corresponding sub-handlers that handle these tokens.
   */
  protected Map<String, AbstractURLActionHandler> getSubHandlerMap ()
  {
    return Collections.emptyMap ();
  }

  public boolean hasSubHandlers ()
  {
    return !getSubHandlerMap ().isEmpty ();
  }

  protected void setSubhandlersActionURI (AbstractURLActionHandler subHandler)
  {
    subHandler.setActionURI (String.format ("%s%s%s", getActionURI (), "/", urlEncode (subHandler.mActionName)));
    if (subHandler.hasSubHandlers ())
    {
      subHandler.updateActionURIs ();
    }
  }

  protected void updateActionURIs ()
  {
    setActionURI (mParentHandler.getActionURI () + "/" + mActionName);
    for (AbstractURLActionHandler subHandler : getSubHandlerMap ().values ())
    {
      setSubhandlersActionURI (subHandler);
    }
  }

  public void setLocale (Locale locale)
  {
    mLocale = locale;
  }

  public Locale getLocale ()
  {
    return mLocale == null ? Locale.getDefault () : mLocale;
  }

  protected void setActionURI (String actionURI)
  {
    mActionURI = actionURI;
  }

  @Override
  public String toString ()
  {
    return String.format ("%s='%s'", getClass ().getSimpleName (), mActionName);
  }
}
