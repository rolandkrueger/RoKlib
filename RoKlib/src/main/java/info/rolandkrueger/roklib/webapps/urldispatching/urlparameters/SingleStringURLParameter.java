package info.rolandkrueger.roklib.webapps.urldispatching.urlparameters;

import info.rolandkrueger.roklib.webapps.urldispatching.AbstractURLActionCommand;

import java.util.List;
import java.util.Map;

public class SingleStringURLParameter extends AbstractSingleURLParameter<String>
{
  private static final long serialVersionUID = -9010093565464929620L;

  public SingleStringURLParameter (String parameterName)
  {
    super (parameterName);
  }
  
  public SingleStringURLParameter (String parameterName, String defaultString)
  {
    super (parameterName);
    setDefaultValue (defaultString);
  }
  
  protected boolean consumeImpl (Map<String, List<String>> parameters)
  {
    List<String> valueList = parameters.remove (getParameterName ());
    if (valueList != null)
    {
      setValue (valueList.get (0));
      return true;
    }
    return false;
  }
  
  @Override
  protected boolean consumeListImpl (String[] values)
  {
    if (values == null || values.length == 0)
      return false;
    setValue (values[0]);
    return true;
  }

  public AbstractURLActionCommand getErrorCommandIfInvalid ()
  {
    return null;
  }
}
