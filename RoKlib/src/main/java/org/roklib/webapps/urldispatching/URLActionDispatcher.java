/*
 * Copyright (C) 2007 Roland Krueger
 * 
 * Created on 10.02.2010
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

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.roklib.util.helper.CheckForNull;
import org.roklib.util.net.IURLProvider;
import org.roklib.webapps.data.DownloadInfo;
import org.roklib.webapps.urldispatching.IURLActionHandler.ParameterMode;

/**
 * <p>
 * The central dispatcher which provides the main entry point for the URL action handling framework. The action
 * dispatcher manages one internal root URL action handler which dispatches to its sub-handlers. When a visited URL has
 * to be interpreted, this URL is passed to methdo {@link #handleURIAction(URL, String)} or
 * {@link #handleURIAction(URL, String, ParameterMode)}, respectively. There, the URL is split into a token list to be
 * recursively interpreted by the registered action handlers. For example, if the following URL is to be interpreted
 * 
 * <pre>
 * http://www.example.com/myapp/user/home/messages
 * </pre>
 * 
 * with the web application installed under context <code>http://www.example.com/myapp/</code> the URI fragment to be
 * interpreted is <code>/user/home/messages</code>. This is split into three individual tokens <code>user</code>,
 * <code>home</code>, and <code>messages</code> in that order. To interpret these tokens, the root action handler passes
 * them to the sub-handler which has been registered as handler for the first token <code>user</code>. If no such
 * handler has been registered, the dispatcher will do nothing more or return the default action command that has been
 * registered with {@link #set404FileNotFoundCommand(AbstractURLActionCommand)}. It thus indicates, that the URL could
 * not successfully be interpreted.
 * </p>
 * <p>
 * Note that this class is not thread-safe, i.e. it must not be used to handle access to several URLs in parallel. You
 * should use one action dispatcher per HTTP session.
 * </p>
 * 
 * @author Roland Krueger
 */
public class URLActionDispatcher implements Serializable
{
  private static final long            serialVersionUID = 7151587763812706383L;
  private static final Logger          LOG              = null;                // FIXME
  private Map<String, List<String>>    mCurrentParameters;
  private URL                          mContextOriginal;
  private String                       mRelativeUriOriginal;
  private Map<String, String[]>        mCurrentParametersOriginalValues;
  private AbstractURLActionCommand     m404FileNotFoundCommand;
  private DispatchingURLActionHandler  mRootDispatcher;
  private IURLActionDispatcherListener mListener;
  private ParameterMode                mParameterMode   = ParameterMode.QUERY;
  private IURLProvider                 mURLProvider;
  private boolean                      mIgnoreExclamationMark;

  public URLActionDispatcher (IURLProvider urlProvider, boolean useCaseSensitiveURLs)
  {
    CheckForNull.check (urlProvider);
    mURLProvider = urlProvider;

    if (useCaseSensitiveURLs)
    {
      mCurrentParameters = new HashMap<String, List<String>> ();
    } else
    {
      mCurrentParameters = new TreeMap<String, List<String>> (String.CASE_INSENSITIVE_ORDER);
    }
    mRootDispatcher = new DispatchingURLActionHandler ("");
    mRootDispatcher.setCaseSensitive (useCaseSensitiveURLs);
    mRootDispatcher.setParent (new AbstractURLActionHandler ("")
    {
      private static final long serialVersionUID = 3744506992900879054L;

      @Override
      protected IURLProvider getContextURLProvider ()
      {
        return mURLProvider;
      }

      protected AbstractURLActionCommand handleURLImpl (List<String> uriTokens, Map<String, List<String>> parameters,
          ParameterMode parameterMode)
      {
        return null;
      }

      @Override
      protected boolean isResponsibleForToken (String uriToken)
      {
        throw new UnsupportedOperationException ();
      }
    });
  }

  /**
   * <p>
   * If <code>true</code> is passed, the dispatcher is instructed to ignore a possible exclamation mark at the beginning
   * of the relative path when handling the URI action. That is, e. g., a relative path <code>!user/login</code> will be
   * handled as if the path <code>user/login</code> was given for interpretation. This is useful if your application is
   * a rich internet application that uses URI fragments for making pages bookmarkable. In the example above, the full
   * URL might look like <code>http://www.example.com/#!user/login</code>.
   * </p>
   * <p>
   * Using an exclamation mark after the hash is a scheme proposed by Google to make an AJAX application whose HTML-code
   * is generated by JavaScript crawlable for a web crawler. See <a
   * href="https://developers.google.com/webmasters/ajax-crawling">https://developers.google.com/
   * webmasters/ajax-crawling</a> for more information on this.
   * </p>
   * <p>
   * By ignoring the exclamation mark, you can have URLs using this particular scheme without having to explicitly deal
   * with the exclamation mark.
   * </p>
   * 
   * @param ignoreExclamationMark
   *          <code>true</code> if an exclamation mark at the beginning of the relative path to be handled should be
   *          ignored
   */
  public void setIgnoreExclamationMark (boolean ignoreExclamationMark)
  {
    mIgnoreExclamationMark = ignoreExclamationMark;
  }

  public boolean isCaseSensitive ()
  {
    return mRootDispatcher.isCaseSensitive ();
  }

  public void setCaseSensitive (boolean caseSensitive)
  {
    mRootDispatcher.setCaseSensitive (caseSensitive);
  }

  /**
   * Returns the root dispatching handler that is the entry point of the URL interpretation chain. This is a special
   * action handler as the URL token it is responsible for (its <em>action name</em>) is the empty String. Thus, if a
   * visited URL is to be interpreted by this action dispatcher, this URL is first passed to that root dispatching
   * handler. All URL action handlers that are responsible for the first directory level of a URL have to be added to
   * this root handler as sub-handlers. To do that, you can also use the delegate method
   * {@link #addHandler(AbstractURLActionHandler)}.
   * 
   * @return the root dispatching handler for this action dispatcher
   * @see #addHandler(AbstractURLActionHandler)
   */
  public DispatchingURLActionHandler getRootActionHandler ()
  {
    return mRootDispatcher;
  }

  public void setURLActionDispatcherListener (IURLActionDispatcherListener listener)
  {
    mListener = listener;
  }

  /**
   * Sets the action command to be executed each time when no responsible action handler could be found for some
   * particular relative URI. If set to <code>null</code> no particular action is performed when an unknown relative URI
   * is handled.
   * 
   * @param fileNotFoundCommand
   *          command to be executed for an unknown relative URI, may be <code>null</code>
   */
  public void set404FileNotFoundCommand (AbstractURLActionCommand fileNotFoundCommand)
  {
    m404FileNotFoundCommand = fileNotFoundCommand;
  }

  /**
   * Returns the set of parameters that belong to the currently handled URL and have been set with
   * {@link #handleParameters(Map)}.
   * 
   * @return
   */
  protected Map<String, List<String>> getParameters ()
  {
    return mCurrentParameters;
  }

  /**
   * Clears the set of parameter values that has been set with {@link #handleParameters(Map)}.
   */
  public void clearParameters ()
  {
    mCurrentParameters.clear ();
  }

  public void handleParameters (Map<String, String[]> parameters)
  {
    if (parameters == null)
      return;
    mCurrentParameters.clear ();
    mCurrentParametersOriginalValues = parameters;
    for (String key : parameters.keySet ())
    {
      List<String> params = new ArrayList<String> (Arrays.asList (parameters.get (key)));
      if (!params.isEmpty ())
        mCurrentParameters.put (key, params);
    }
  }

  /**
   * Set the parameter mode to be used for interpreting the visited URLs.
   * 
   * @param parameterMode
   *          {@link ParameterMode} which will be used by {@link #handleURIAction(URL, String)}
   */
  public void setParameterMode (ParameterMode parameterMode)
  {
    mParameterMode = parameterMode;
  }

  /**
   * Passes the given relative URI to the URL action handler chain and interprets all parameters with the
   * {@link ParameterMode} defined with {@link #setParameterMode(ParameterMode)}.
   * 
   * @see #handleURIAction(URL, String, ParameterMode)
   */
  public DownloadInfo handleURIAction (URL context, String relativeUri)
  {
    return handleURIAction (context, relativeUri, mParameterMode);
  }

  /**
   * This method is the central entry point for the URL action handling framework.
   * 
   * @param context
   *          URL containing the server context. If the web application runs at the address
   *          <code>http://www.example.com/myapp</code> this is the context passed in through this parameter. This value
   *          may be <code>null</code>.
   * @param relativeUri
   *          relative URI to be interpreted by the URL action handling framework. This may be an URI such as
   *          <code>/admin/configuration/settings/language/de</code>
   * @param parameterMode
   *          {@link ParameterMode} to be used for interpreting possible parameter values contained in the given
   *          relative URI
   * @return an object containing all necessary data for a file download. This return value is optional and may be
   *         <code>null</code>. If a {@link DownloadInfo} object is returned, the contained {@link InputStream} should
   *         be written to the {@link OutputStream} of the HTTP response.
   */
  public DownloadInfo handleURIAction (URL context, String relativeUri, ParameterMode parameterMode)
  {
    mContextOriginal = context;
    try
    {
      mRelativeUriOriginal = URLDecoder.decode (relativeUri, "UTF-8");
    } catch (UnsupportedEncodingException ueExc)
    {
      throw new RuntimeException ("UTF-8 encoding not supported", ueExc);
    }

    ignoreSlashAtBeginningOfRelativeURI ();
    ignoreExclamationMarkIfNecessary ();

    List<String> uriTokens = new ArrayList<String> (Arrays.asList (mRelativeUriOriginal.split ("/")));

    if (LOG != null && LOG.isTraceEnabled ())
    {
      LOG.trace (String.format ("Dispatching URI: '%s', params: '%s'", mRelativeUriOriginal, mCurrentParameters));
    }

    AbstractURLActionCommand action = mRootDispatcher.handleURL (uriTokens, mCurrentParameters, parameterMode);
    if (action == null)
    {
      // execute 404 File Not Found command
      LOG.info ("404 File Not Found: " + mRelativeUriOriginal + "?" + mCurrentParameters);
      if (m404FileNotFoundCommand != null)
      {
        m404FileNotFoundCommand.execute ();
      }
      return null;
    } else
    {
      action.execute ();
      if (mListener != null)
        mListener.handleURLActionCommand (action);
      return action.getDownloadStream ();
    }
  }

  private void ignoreExclamationMarkIfNecessary ()
  {
    if (mIgnoreExclamationMark && mRelativeUriOriginal.startsWith ("!"))
    {
      mRelativeUriOriginal = mRelativeUriOriginal.substring (1);
    }
  }

  private void ignoreSlashAtBeginningOfRelativeURI ()
  {
    if (mRelativeUriOriginal.startsWith ("/"))
    {
      mRelativeUriOriginal = mRelativeUriOriginal.substring (1);
    }
  }

  /**
   * Returns the relative URI that is currently being handled by this dispatcher. This URI is set each time
   * {@link #handleURIAction(URL, String, ParameterMode)} is called.
   */
  public String getCurrentlyHandledURI ()
  {
    return mRelativeUriOriginal;
  }

  public DownloadInfo replayCurrentAction ()
  {
    handleParameters (mCurrentParametersOriginalValues);
    return handleURIAction (mContextOriginal, mRelativeUriOriginal);
  }

  /**
   * Adds a new sub-handler to the root action handler of this dispatcher. For example, if this method is called three
   * times with action handlers for the fragments <code>admin</code>, <code>main</code>, and <code>login</code> on a web
   * application running in context <code>http://www.example.com/myapp</code> this dispatcher will be able to interpret
   * the following URLS:
   * 
   * <pre>
   * http://www.example.com/myapp/admin
   * http://www.example.com/myapp/main
   * http://www.example.com/myapp/login
   * </pre>
   * 
   * @param subHandler
   *          the new action handler
   * @throws IllegalArgumentException
   *           if the given sub-handler has already been added to another parent handler
   */
  public final void addHandler (AbstractURLActionHandler subHandler)
  {
    getRootActionHandler ().addSubHandler (subHandler);
  }
}
