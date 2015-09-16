package com.cybernetic.mapquest;

import com.cybernetic.mapquest.constants.AdvancedRoutingOptions;
import com.cybernetic.mapquest.constants.BasicRoutingOptions;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Maps;
import http.rest.RestClient;
import http.rest.RestClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

public class MapQuestClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(MapQuestClient.class);

    public static final String MAP_QUEST_API_URL = "http://open.mapquestapi.com/directions/v2/";
    public static final String ROUTE_URL = new StringBuilder(MAP_QUEST_API_URL).append("route").toString();
    public static final String KEY_TO_USE = "h1BmEGiJsGiFyiMpGCSRpB2DFYoIVDkW";

    public JsonNode getRoute(String from, String to) {
        RestClient client = RestClient.builder().build();
        Map<String, String> params = Maps.newHashMap();
        params.put(BasicRoutingOptions.KEY, KEY_TO_USE);
        params.put(BasicRoutingOptions.FROM, from);
        params.put(BasicRoutingOptions.TO, to);
        params.put(AdvancedRoutingOptions.UNIT, "k");

        JsonNode node;
        try {
            node = client.get(ROUTE_URL, params, JsonNode.class);
            return node;
        } catch (RestClientException | IOException e) {
            LOGGER.error(e.getMessage());
        }
        return null;
    }
}
