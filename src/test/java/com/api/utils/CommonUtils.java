package com.api.utils;

import com.api.dto.Matcher;
import com.api.dto.Wait;
import com.web.executiondata.Global;
import com.web.utils.GenericUtil;
import io.cucumber.datatable.DataTable;
import io.restassured.path.json.JsonPath;
import lombok.extern.log4j.Log4j2;
import org.hamcrest.Matchers;
import org.hamcrest.core.AnyOf;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.script.ScriptException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static com.web.utils.GenericUtil.parseValue;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.containsString;
import com.web.utils.JSON;


@Log4j2
public class CommonUtils {

    protected static final String FILEPATH = "src/test/resources/req-res/";



    public static void validateResponsePath(final Matcher matcher, final DataTable dataTable, final JsonPath finalJsonPath) {
        getModifiableDataList(dataTable)
                .forEach(
                        y -> {
                            y.forEach(
                                    (key, value) -> {
                                        try {
                                            switch (matcher) {
                                                case equalTo:
                                                    assertThat(key, finalJsonPath.get(key), equalTo(parseValue(value)));
                                                    break;
                                                case notNull:
                                                    assertThat(key, finalJsonPath.get(key), is(notNullValue()));
                                                    break;
                                                case contains:
                                                    assertThat(key, finalJsonPath.get(key), contains(parseValue(value)));
                                                    break;
                                                case hasSize:
                                                    assertThat(
                                                            key, finalJsonPath.get(key), hasSize(Integer.parseInt(value)));
                                                    break;
                                                case hasItem:
                                                    assertThat(key, finalJsonPath.get(key), hasItem(parseValue(value)));
                                                    break;
                                                case isBlank:
                                                    assertThat(key, finalJsonPath.get(key), is(blankString()));
                                                    break;
                                                case containsInAnyOrder:
                                                    assertThat(
                                                            key,
                                                            finalJsonPath.get(key),
                                                            containsInAnyOrder(((ArrayList) parseValue(value)).toArray()));
                                                    break;
                                                case containsString:
                                                    assertThat(
                                                            key,
                                                            finalJsonPath.getString(key),
                                                            containsString(value));
                                                    break;
                                                case anyOf:
                                                    assertThat(key, finalJsonPath.get(key), AnyOf.anyOf(
                                                            Arrays.stream(value.split(",")).map(Matchers::is)
                                                                    .collect(Collectors.toList())));
                                                    break;
                                                default:
                                                    break;
                                            }
                                        } catch (ScriptException sce) {
                                            throw new RuntimeException(sce.getMessage());
                                        }
                                    });
                        });
    }

    public static List<Map<String, String>> getModifiableDataList(final DataTable dataTable) {
        List<Map<String, String>> mapList = new ArrayList<>();
        dataTable
                .asMaps()
                .forEach(
                        x -> {
                            HashMap<String, String> map = new HashMap<>();
                            x.forEach(
                                    (k, v) -> {
                                        String value = String.valueOf(Global.getData(v));
                                        if (v.equalsIgnoreCase("null")) v = null;
                                        map.put(k, v);
                                    });
                            mapList.add(map);
                        });
        return mapList;
    }

    public static <K, V> String buildRequestBody(final String filepath, final Map<K, V> map) {
        JSONObject json = null;

        if (filepath.isEmpty() || filepath.length() < 2) {
            return null;
        }

        String fullPath = FILEPATH + filepath.replaceAll("^\"|\"$", "") + ".json";

        try {
            json = readFile(fullPath);
        } catch (Exception e) {
            return null;
        }
        JSONObject finalJson = json;
        map.forEach(
                (k, v) -> {
                    if (v == null) {
                        replace(finalJson, k, v);
                    } else {
                        replace(finalJson, k, v.toString());
                    }
                });
        return json.toJSONString();
    }


    private static JSONObject readFile(final String filepath) throws Exception {
        JSONParser jsonParser = new JSONParser();
        try (FileReader reader = new FileReader(filepath)) {
            Object obj = jsonParser.parse(reader);
            return (JSONObject) obj;
        } catch (ParseException | IOException exception) {
            exception.printStackTrace();
            throw exception;
        }
    }

    private static <K, V> void replace(final JSONObject json, final K k, final V v) {
        if (k.toString().contains(".")) {
            com.jayway.jsonpath.JsonPath.parse(json).set(String.valueOf(k), v);

        } else if (json.containsKey(k)) {
            try {
                json.put(k, parseValue(String.valueOf(v)));
            } catch (ScriptException e) {
                e.printStackTrace();
            }
            if (v instanceof String && ((String) v).equalsIgnoreCase("$UUID")) {
                json.put(k, UUID.randomUUID().toString());
            }
        } else {
            for (String key : (Set<String>) json.keySet()) {
                Object value = json.get(key);
                if (value instanceof JSONObject) {
                    replace((JSONObject) value, k, v);
                } else if (value instanceof JSONArray && !((JSONArray) value).isEmpty()) {
                    ((JSONArray) value).forEach(x -> replace((JSONObject) x, k, v));
                }
            }
        }

    }
    public static void waitDynamically(final Wait maxWait, final Integer retryCount) {
        try {
            int waitInterval = maxWait.getTimeout().intValue() / retryCount;
            log.info("Wait interval(in second):" + waitInterval);
            Thread.sleep(waitInterval * 1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
