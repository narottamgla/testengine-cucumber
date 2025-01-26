package com.web.utils;

import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.web.executiondata.ExecutionConfig;
import com.web.executiondata.Global;

public class GenericUtil {

    public static Object parseValue(final String value) throws ScriptException {
        if (value == null || value.equalsIgnoreCase("null")) {
            return null;
        } else if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false")) {
            return Boolean.parseBoolean(value);
        } else if (value.contains("EXPR:") || value.contains("HALF_UP:") || value.contains("TIME:")) {
            String expression = value.split(":")[1].replaceAll("[a-zA-Z]", "");
            Object object = new ScriptEngineManager(null).getEngineByName("JavaScript").eval(expression);
            if (object instanceof Number) {
                RoundingMode roundingMode = value.contains("HALF_UP:") ? RoundingMode.HALF_UP : RoundingMode.HALF_EVEN;
                int scale = value.contains("TIME:") ? 2 : 0;
                return BigDecimal.valueOf(Double.parseDouble(object.toString()))
                        .setScale(scale, roundingMode)
                        .doubleValue();
            }
            return object;
        } else if (value.contains("LIST:")) {
            List<String> list = Arrays.asList(value.split("LIST:")[1].split(","));
            List<Object> list2 = new ArrayList<>();
            for (String x : list) {
                list2.add(parseValue(x));
            }
            return list2;
        } else if (value.contains("STR:")) {
            return Global.getData(value.split("STR:")[1].replaceAll("\\s", ""));
        } else {
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException e) {
                if (value.contains(".")) {
                    return Double.parseDouble(value);
                }
                return value;
            }
        }
    }
}
