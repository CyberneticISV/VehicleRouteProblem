package com.cybernetic.mapquest.constants;

public class CommonURLParameters {

    /* Use this parameter to set the strategy for resolving ambiguous location names.
    ignore - the Routing Service will simply use the first location found for an address.
    check - the Routing Service will return a full list of the possible location matches in the collections attribute
    of the response.
    Default = 'ignore'*/
    public static final String AMBIGUITIES = "ambiguities";

    /* Specifies the format of the request. If this parameter is not supplied, the input format is assumed to be
    JSON-formatted text. The JSON-formatted input text must be supplied as either the "json" parameter of an HTTP GET,
    or as the BODY of an HTTP POST. If this parameter is "xml", the XML-formatted input text must be supplied as either
    the "xml" parameter of an HTTP GET, or as the BODY of an HTTP POST.
    Must be one of the following, if supplied:
      - json
      - xml
    Defaults to 'json' when using POST; otherwise it determines which to use based on parameters given. */
    public static final String IN_FORMAT = "inFormat";

    /* This parameter, if present, should contain the JSON-formatted text of the request. Use this parameter if you want
    to submit your request in JSON format, but do not want to use an HTTP POST to submit body text.
    Note: Remember to URL-escape the text in this parameter! */
    public static final String JSON = "json";

    /* This parameter, if present, should contain the XML-formatted text of the request. Use this parameter if you want
    to submit your request in XML format, but do not want to use an HTTP POST to submit body text.
    Note: Remember to URL-escape the text in this parameter!*/
    public static final String XML = "xml";

    /* Specifies the format of the response. Must be one of the following, if supplied:
       - json
       - xml
    Default = 'json'*/
    public static final String OUT_FORMAT = "outFormat";

    /* A JavaScript function name. The JSON-formatted response will be wrapped in a call to the supplied callback
    function name to provide JSONP functionality. This functionality might be needed to do cross-site scripting.
    See the Wikipedia.org entry for JSON for more details.*/
    public static final String CALLBACK = "callback";
}
