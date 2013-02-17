/*
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


import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.roklib.util.helper.CheckForNull;

/**
 * <p>
 * Special dispatching URL action handler which is not only responsible for handling one particular URI token but
 * handles all tokens matching a predefined regular expression. So instead of only handling URI tokens such as
 * <code>user</code> it could handle all tokens matching the regex <code>user_(\d+)</code>, i. e. user_1, user_17,
 * user_23, and so on.
 * </p>
 * <p>
 * A {@link RegexURLActionHandler} is itself a {@link DispatchingURLActionHandler}, that is, it can have its own
 * sub-handlers to which the responsibility to interpret part of a URL can be passed. To set the action command for this
 * {@link RegexURLActionHandler} in case there are no more URI tokens to be passed to sub-handlers (i. e. the currently
 * interpreted URL directly points to this handler), you use method {@link #setRootCommand(AbstractURLActionCommand)}.
 * </p>
 * <h1>Capturing Groups</h1>
 * <p>
 * The regular expression for this action handler can contain capturing groups in order to capture parts or all of the
 * currently interpreted URI token. The captured values for these capturing groups can be obtained with
 * {@link #getMatchedTokenFragments()}. The set of matched token fragments is updated after each call to
 * {@link #isResponsibleForToken(String)} by the parent handler. This usually happens while in the process of
 * interpreting a visited URL by the {@link URLActionDispatcher}. Note that the array of matched token fragments does
 * not contain {@link Matcher}'s first capturing group holding the entire pattern.
 * </p>
 * <h1>Generating Parameterized Action URLs</h1>
 * <p>
 * When you are generating parameterized action URLs with the {@link #getParameterizedActionURL(boolean)} methods, you
 * have to provide a value for the URI token used to represent this {@link RegexURLActionHandler}. This is done with
 * {@link #setURIToken(String)}. The token set with this method must be able to be successfully matched against the
 * regular expression of this handler. Otherwise, an exception is thrown. If you generate a parameterized action URL
 * without setting the URI token first, the regular expression pattern itself is used as the token verbatim.
 * </p>
 * <p>
 * For example, if you have defined the following pattern to be used by this handler: <code>user_(\d+)</code> and this
 * handler is registered as a sub-handler for another action handler with the action name <code>profile</code>. Calling
 * {@link #getParameterizedActionURL(boolean)} without setting the URI token first will then yield the following URL
 * <code>http://www.example.com/profile/user_(\d+)</code>. Setting this handler's URI token to <code>user_123</code>
 * will instead yield the URL <code>http://www.example.com/profile/user_123</code>. The URI token has to be defined for
 * every {@link RegexURLActionHandler} that is found on the path from the action handler tree's root to some action
 * handler for which a parameterized action URL is to be generated.
 * </p>
 * 
 * @author Roland Krüger
 * @since 1.1.0
 */
public class RegexURLActionHandler extends DispatchingURLActionHandler
{
  private static final long serialVersionUID = 4435578380164414638L;

  /**
   * If the regular expression contains any capturing groups the captured values are contained in this array. By
   * convention, the first capturing group of a matcher is the entire pattern. This first group is excluded from the
   * matched token fragments.
   * 
   * @see Matcher
   */
  protected String[]        mMatchedTokenFragments;

  /**
   * The pattern object of this {@link RegexURLActionHandler}. It is compiled in the constructor and each time the case
   * sensitivity is changed.
   */
  private Pattern           mPattern;

  /**
   * Creates a new {@link RegexURLActionHandler} with the provided regular expression. This regex will be applied to the
   * URI tokens passed in to {@link #isResponsibleForToken(String)} to determine if this object is responsible for
   * handling the given token.
   * 
   * @param regex
   *          regular expression which shall be applied by this action handler on the interpreted URI token
   * @throws IllegalArgumentException
   *           when the regular exception is the empty String or consists of only whitespaces
   * @throws PatternSyntaxException
   *           when the regular exception could not be compiled
   */
  public RegexURLActionHandler (String regex)
  {
    super (regex);
    if ("".equals (regex.trim ()))
    {
      throw new IllegalArgumentException ("regex must not be the empty string or all whitespaces");
    }
    mPattern = Pattern.compile (regex);
  }

  /**
   * Returns the set of token fragments that were captured by this action handler's regex capturing groups. This set is
   * recalculated each time the parent handler calls {@link #isResponsibleForToken(String)} during the URL
   * interpretation process.
   * 
   * @return the set of captured token fragments or <code>null</code> if this {@link RegexURLActionHandler} is not
   *         responsible for handling any of the tokens of the currently interpreted URL.
   */
  public String[] getMatchedTokenFragments ()
  {
    return mMatchedTokenFragments;
  }

  /**
   * Returns the number of token fragments that have been captured by the capturing groups of this regex handler's
   * pattern. The array of token fragments can be obtained with {@link #getMatchedTokenFragments()}.
   * 
   * @return size of the matched token fragment array
   * @see #getMatchedTokenFragments()
   */
  public int getMatchedTokenFragmentCount ()
  {
    return mMatchedTokenFragments == null ? 0 : mMatchedTokenFragments.length;
  }

  /**
   * {@inheritDoc}
   * <p>
   * If the case sensitivity is changed the regular expression pattern will be recompiled so that the pattern is in case
   * insensitive matching mode.
   * </p>
   */
  @Override
  protected void setCaseSensitive (boolean caseSensitive)
  {
    if (caseSensitive == isCaseSensitive ())
    {
      return;
    }
    super.setCaseSensitive (caseSensitive);
    mPattern = Pattern.compile (mActionName, caseSensitive ? 0 : Pattern.CASE_INSENSITIVE);
  }

  /**
   * Checks if this {@link RegexURLActionHandler} is responsible for handling the given URI token. It does so by
   * checking whether the token matches the assigned regular expression. If that is the case <code>true</code> is
   * returned. At the same time, it also retrieves all values from the regular expression's capturing group, if there
   * are any, and puts them into an array for later reference with {@link #getMatchedTokenFragments()}.
   * 
   * @return <code>true</code> if the given URI token will be handled by this action handler
   */
  @Override
  protected boolean isResponsibleForToken (String uriToken)
  {
    mMatchedTokenFragments = null;
    Matcher matcher = mPattern.matcher (uriToken);
    if (matcher.matches ())
    {
      identifyMatchedTokenFragments (matcher);
      return true;
    }
    return false;
  }

  /**
   * Retrieves the matched values from the capturing groups of this {@link RegexURLActionHandler}'s regular expression.
   */
  private void identifyMatchedTokenFragments (Matcher matcher)
  {
    mMatchedTokenFragments = new String[matcher.groupCount ()];
    for (int index = 1; index < matcher.groupCount () + 1; ++index)
    {
      mMatchedTokenFragments[index - 1] = matcher.group (index);
    }
  }

  /**
   * Since the action name of the {@link RegexURLActionHandler} is a regular expression, it should not be converted to
   * lower case as is done in the implementation of {@link #getCaseInsensitiveActionName()} in the super class.
   * {@inheritDoc}
   */
  @Override
  public String getCaseInsensitiveActionName ()
  {
    return getActionName ();
  }

  /**
   * <p>
   * Sets the URI token to be used for this action handler when generating URLs with
   * {@link #getParameterizedActionURL(boolean)}. Note that this token must be able to be successfully matched against
   * the pattern for this action handler. Otherwise, this {@link RegexURLActionHandler} would not be able to interpret
   * that token when the generated action URL is later evaluated by the action dispatcher.
   * </p>
   * <p>
   * Note that if you want to generate a parameterized action URL for some action handler, you have to set a specific
   * URI token for every {@link RegexURLActionHandler} that can be found on the path from this action handler back to
   * the root of the action handler tree via its parent handlers.
   * </p>
   * 
   * @param uriToken
   *          URI token to be used for this action handler when generating parameterized action URLs
   * @throws IllegalArgumentException
   *           if the given argument can not be matched against the regular expression of this
   *           {@link RegexURLActionHandler}
   */
  public void setURIToken (String uriToken)
  {
    CheckForNull.check (uriToken);
    if (!mPattern.matcher (uriToken).matches ())
    {
      throw new IllegalArgumentException ("action URI must match with the regular expression of this action handler");
    }
    mActionName = uriToken;
    updateActionURIs ();
  }
}
