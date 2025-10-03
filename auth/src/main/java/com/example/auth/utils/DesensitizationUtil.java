package com.example.auth.utils;

/**
 * *@Description 日志脱模实现工具
 * *@Author wuka
 * *@Date 2025/8/4
 * *@Version 1.0
 **/
public class DesensitizationUtil {

    // 通用脱敏方法（兼容 Java 8）
    public static String desensitize(String input, int prefixLen, int suffixLen, char maskChar) {
        if (input == null || input.length() < prefixLen + suffixLen) {
            return input;
        }

        int maskLength = input.length() - prefixLen - suffixLen;
        StringBuilder masked = new StringBuilder(input.substring(0, prefixLen));

        // 手动构建掩码字符串
        for (int i = 0; i < maskLength; i++) {
            masked.append(maskChar);
        }

        masked.append(input.substring(input.length() - suffixLen));
        return masked.toString();
    }

    // 手机号脱敏
    public static String maskPhone(String phone) {
        if (phone == null || phone.length() < 7) return phone;
        return desensitize(phone, 3, 4, '*');
    }

    // 身份证脱敏
    public static String maskIdCard(String idCard) {
        if (idCard == null || idCard.length() < 10) return idCard;
        return desensitize(idCard, 3, 4, '*');
    }

    // 银行卡脱敏
    public static String maskBankCard(String bankCard) {
        if (bankCard == null || bankCard.length() < 10) return bankCard;
        return desensitize(bankCard, 6, 4, '*');
    }

    // 姓名脱敏
    public static String maskName(String name) {
        if (name == null || name.isEmpty()) return name;
        if (name.length() == 1) return "*";
        if (name.length() == 2) return name.charAt(0) + "*";
        return name.charAt(0) + "*" + name.charAt(name.length() - 1);
    }

    // 邮箱脱敏
    public static String maskEmail(String email) {
        if (email == null || !email.contains("@")) return email;

        String[] parts = email.split("@", 2);
        String localPart = parts[0];
        String domain = parts[1];

        if (localPart.length() <= 1) {
            return "*@" + domain;
        }

        return localPart.charAt(0) + "****" + localPart.charAt(localPart.length() - 1) + "@" + domain;
    }

    // 地址脱敏
    public static String maskAddress(String address) {
        if (address == null || address.length() < 4) return address;

        // 保留前4个字符（通常是省市信息）
        int keepLength = Math.min(4, address.length() / 2);
        return address.substring(0, keepLength) + "****" +
                (address.length() > keepLength + 4 ? address.substring(address.length() - 4) : "");
    }

}
