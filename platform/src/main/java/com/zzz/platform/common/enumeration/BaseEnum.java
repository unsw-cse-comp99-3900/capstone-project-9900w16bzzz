package com.zzz.platform.common.enumeration;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONAware;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.CaseFormat;
import lombok.Data;

import java.util.LinkedHashMap;
import java.util.Objects;

/**
 * @author: zuoming yan
 * @version: v1.0.0
 * @date: 2024/6/19
 */
public interface BaseEnum {


    /**
     * get enum value
     *
     * @return
     */
    Object getValue();

    /**
     * get description from enum
     *
     * @return String
     */
    String getDesc();

    /**
     * compare enum value
     *
     * @param value
     * @return boolean
     */
    default boolean equalsValue(Object value) {
        return Objects.equals(getValue(), value);
    }

    /**
     * compare enum class
     *
     * @param baseEnum
     * @return boolean
     */
    default boolean equals(BaseEnum baseEnum) {
        return Objects.equals(getValue(), baseEnum.getValue()) && Objects.equals(getDesc(), baseEnum.getDesc());
    }

    /**
     * return enum info
     *
     * @param clazz
     * @return
     */
    static String getInfo(Class<? extends BaseEnum> clazz) {
        BaseEnum[] enums = clazz.getEnumConstants();
        LinkedHashMap<String, JSONObject> json = new LinkedHashMap<>(enums.length);
        for (BaseEnum e : enums) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("value", new DeletedQuotationAware(e.getValue()));
            jsonObject.put("desc", new DeletedQuotationAware(e.getDesc()));
            json.put(e.toString(), jsonObject);
        }

        String enumJson = JSON.toJSONString(json, true);
        enumJson = enumJson.replaceAll("\"", "");
        enumJson = enumJson.replaceAll("\t", "&nbsp;&nbsp;");
        enumJson = enumJson.replaceAll("\n", "<br>");
        String prefix = "  <br>  export const " + CaseFormat.UPPER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, clazz.getSimpleName() + " = <br> ");
        return prefix + enumJson + " <br>";
    }
    @Data
    class DeletedQuotationAware implements JSONAware {

        private String value;

        public DeletedQuotationAware(Object value) {
            if (value instanceof String) {
                this.value = "'" + value + "'";
            } else {
                this.value = value.toString();
            }
        }

        @Override
        public String toJSONString() {
            return value;
        }
    }
}
