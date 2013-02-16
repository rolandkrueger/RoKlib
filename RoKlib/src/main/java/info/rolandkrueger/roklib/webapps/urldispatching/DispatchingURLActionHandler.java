/*
 * $Id: DispatchingURLActionHandler.java 181 2010-11-01 09:39:13Z roland $ Copyright (C) 2007 - 2010
 * Roland Krueger Created on 11.02.2010
 * 
 * Author: Roland Krueger (www.rolandkrueger.info)
 * 
 * This file is part of RoKlib.
 * 
 * This library is free software; you can redistribute it and/or modify it under the terms of the
 * GNU Lesser General Public License as published by the Free Software Foundation; either version
 * 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with this library;
 * if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA
 * 02111-1307 USA
 */
package info.rolandkrueger.roklib.webapps.urldispatching;

import info.rolandkrueger.roklib.util.helper.CheckForNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author Roland Krüger
 */
public class DispatchingURLActionHandler extends AbstractURLActionHandler
{
  private static final long                     serialVersionUID = -777810072366030611L;

  private AbstractURLActionCommand              mRootCommand;

  private Map<String, AbstractURLActionHandler> mSubHandlers;

  /**
   * Create a dispatching action handler with the provided action name. The action name is the part
   * of the URL that is handled by this action handler.
   * 
   * @param actionName
   *          the action name for this dispatching action handler
   */
  public DispatchingURLActionHandler (String actionName)
  {
    super (actionName);
  }

  /**
   * Sets the root command for this dispatching action handler. This is the
   * {@link AbstractURLActionCommand} which will be returned when the token list to be interpreted
   * by this handler is empty. This is the case when a URL is being interpreted that directly points
   * to a {@link DispatchingURLActionHandler}. For example, if the following URL is passed to the
   * URL action handling framework
   * 
   * <pre>
   * http://www.example.com/myapp/home/
   * </pre>
   * 
   * where the URL action handler for token <code>home</code> is a
   * {@link DispatchingURLActionHandler}, then this handler's root command is used as the outcome of
   * the URL interpretation. This command could then provide some default logic for the interpreted
   * URL, such as redirecting to the correct home screen for the currently signed in user. If
   * instead the following URL is interpreted
   * 
   * <pre>
   * http://www.example.com/myapp/home/manager
   * </pre>
   * 
   * then the <code>home</code> dispatcher will pass the URL token handling to its sub-handler which
   * is responsible for the <code>manager</code> token.
   * 
   * @param rootCommand
   *          action command to be used when interpreting a URL pointing directly to this action
   *          handler. May be <code>null</code>.
   */
  public void setRootCommand (AbstractURLActionCommand rootCommand)
  {
    mRootCommand = rootCommand;
  }

  @Override
  protected AbstractURLActionCommand handleURLImpl (List<String> uriTokens, Map<String, List<String>> parameters,
      ParameterMode parameterMode)
  {
    if (uriTokens == null || uriTokens.isEmpty () || "".equals (uriTokens.get (0))) { return mRootCommand; }
    String currentActionName = uriTokens.remove (0);
    return forwardToSubHandler (currentActionName, uriTokens, parameters, parameterMode);
  }

  private final AbstractURLActionCommand forwardToSubHandler (String currentActionName, List<String> uriTokens,
      Map<String, List<String>> parameters, ParameterMode parameterMode)
  {
    AbstractURLActionHandler subHandler = getResponsibleSubHandlerForActionName (currentActionName);
    if (subHandler == null) { return getDefaultCommand (); }

    return subHandler.handleURL (uriTokens, parameters, parameterMode);
  }

  /**
   * Tries to find the next action handler in line which is responsible for handling the current URL
   * token. If such a handler is found, the responsibility for interpreting the current URL is
   * passed to this handler. Note that a specific precedence rule applies to the registered
   * sub-handlers as described in the class description.
   * 
   * @param currentActionName
   *          the currently interpreted URL token
   * @return {@link AbstractURLActionHandler} that is responsible for handling the current URL token
   *         or <code>null</code> if no such handler could be found.
   */
  private AbstractURLActionHandler getResponsibleSubHandlerForActionName (String currentActionName)
  {
    String actionName = isCaseSensitive () ? currentActionName : currentActionName.toLowerCase (getLocale ());

    AbstractURLActionHandler responsibleSubHandler = getSubHandlerMap ().get (actionName);
    if (responsibleSubHandler != null) { return responsibleSubHandler; }

    for (AbstractURLActionHandler subhandler : getSubHandlerMap ().values ())
    {
      if (subhandler.isResponsibleForToken (actionName)) { return subhandler; }
    }
    return null;
  }

  /**
   * <p>
   * Registers a sub-handler to this {@link DispatchingURLActionHandler}. Sub-handlers form the
   * links of the URL interpretation chain in that each of them is responsible for interpreting one
   * particular fragment of a URL.
   * </p>
   * <p>
   * For example, if a web application offers the following two valid URLs
   * 
   * <pre>
   * http://www.example.com/myapp/articles/list
   * http://www.example.com/myapp/articles/showArticle
   * </pre>
   * 
   * then the URL action handler for fragment <code>articles</code> has to be a
   * {@link DispatchingURLActionHandler} since it needs two sub-handlers for <code>list</code> and
   * <code>showArticle</code>. These two fragments may be handled by
   * {@link DispatchingURLActionHandler}s themselves if they in turn allow sub-directories in the
   * URL structure. They could also be {@link SimpleURLActionHandler}s that simply return an
   * {@link AbstractURLActionCommand} when being evaluated.
   * </p>
   * <p>
   * The case sensitivity of this action handler is inherited to the sub-handler.
   * </p>
   * 
   * @param subHandler
   *          the sub-handler to be added to this {@link DispatchingURLActionHandler}
   * @throws IllegalArgumentException
   *           if the passed action handler alread has been added as sub-handler to another
   *           {@link DispatchingURLActionHandler}. In other words, if the passed sub-handler
   *           already has a parent handler.
   */
  public final void addSubHandler (AbstractURLActionHandler subHandler)
  {
    CheckForNull.check (subHandler);
    if (subHandler.mParentHandler != null)
      throw new IllegalArgumentException (String.format ("This sub-handler instance has "
          + "already been added to another action handler. This handler = '%s'; sub-handler = '%s'", mActionName,
          subHandler.mActionName));
    subHandler.mParentHandler = this;
    setSubhandlersActionURI (subHandler);
    getSubHandlerMap ().put (subHandler.mActionName, subHandler);
    subHandler.setCaseSensitive (isCaseSensitive ());
  }

  /**
   * <p>
   * {@inheritDoc}
   * </p>
   */
  @Override
  protected void setCaseSensitive (boolean caseSensitive)
  {
    super.setCaseSensitive (caseSensitive);
    if (isCaseSensitive () & mSubHandlers != null)
    {
      rebuildSubhandlerMap (caseSensitive);
      for (AbstractURLActionHandler subhandler : mSubHandlers.values ())
      {
        subhandler.setCaseSensitive (caseSensitive);
      }
    }
  }

  private void rebuildSubhandlerMap (boolean caseSensitive)
  {
    Map<String, AbstractURLActionHandler> subHandlers = mSubHandlers;
    mSubHandlers = null;
    mSubHandlers = getSubHandlerMap ();

    for (AbstractURLActionHandler subHandler : subHandlers.values ())
    {
      String actionName = caseSensitive ? subHandler.getActionName () : subHandler.getCaseInsensitiveActionName ();
      mSubHandlers.put (actionName, subHandler);
    }
  }

  /**
   * {@inheritDoc}
   */
  protected Map<String, AbstractURLActionHandler> getSubHandlerMap ()
  {
    if (mSubHandlers == null)
    {
      if (!isCaseSensitive ())
      {
        mSubHandlers = new TreeMap<String, AbstractURLActionHandler> (String.CASE_INSENSITIVE_ORDER);
      } else
      {
        mSubHandlers = new HashMap<String, AbstractURLActionHandler> (4);
      }
    }
    return mSubHandlers;
  }
}
