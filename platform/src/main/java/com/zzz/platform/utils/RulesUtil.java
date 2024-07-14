package com.zzz.platform.utils;

import com.zzz.platform.common.enumeration.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: zuoming yan
 * @version: v1.0.0
 * @date: 2024/7/14
 */
public class RulesUtil {
    public static final String PREFIX = "rules=";

    public static List<RulesEnum> splitRules(String rules) {
        if (StringUtils.isBlank(rules)) {
            return null;
        }
        List<RulesEnum> rulesEnums = new ArrayList<>();
        if (!rules.contains(",")) {
            RulesEnum enumByValue = EnumUtil.getEnumByValue(rules, RulesEnum.class);
            rulesEnums.add(enumByValue);
            return rulesEnums;
        }
        String[] rulesArray = rules.split(",");
        for (String rule : rulesArray) {
            if (StringUtils.isNotBlank(rule)) {
                RulesEnum rulesEnum = EnumUtil.getEnumByValue(rule, RulesEnum.class);
                rulesEnums.add(rulesEnum);
            }
        }
        return rulesEnums;
    }

    public static String concatUrl(List<RulesEnum> list) {
        StringBuilder params = new StringBuilder();
        for (RulesEnum rulesEnum : list) {
            params.append(PREFIX).append(rulesEnum.getValue()).append("&");
        }
        return params.toString();
    }

    @Getter
    @AllArgsConstructor
    public enum RulesEnum implements BaseEnum {
        AUNZ_PEPPOL_1_0_10("AUNZ_PEPPOL_1_0_10", "Aussie rules"),
        AUNZ_PEPPOL_SB_1_0_10("AUNZ_PEPPOL_SB_1_0_10", "Aussie rules"),
        AUNZ_UBL_1_0_10("AUNZ_UBL_1_0_10", "Aussie rules"),
        ;
        private final String value;
        private final String desc;
    }
}
