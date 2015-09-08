package com.cybernetic.mapquest.constants;

public class BasicRoutingOptions {

    /* A unique key to authorize use of the Routing Service ()
    *  This is a required parameter!
    *  String */
    public static final String KEY = "key";

    /* When the input format is Key/Value pairs, the starting location of a Route Request. Exactly one "from" parameter is allowed. This is used for locations only.
     * Location */
    public static final String FROM = "from";

    /* When the input format is Key/Value pairs, the ending location(s) of a Route Request. More than one "to" parameter may be supplied. This is used for locations only.
     * Location */
    public static final String TO = "to";
}
