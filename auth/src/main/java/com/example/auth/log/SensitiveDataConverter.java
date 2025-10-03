package com.example.auth.log;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import com.example.auth.utils.DesensitizationUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * *@Description TODO
 * *@Author wuka
 * *@Date 2025/8/4
 * *@Version 1.0
 **/
public class SensitiveDataConverter extends ClassicConverter {

    // 敏感数据正则表达式
    private static final Pattern PHONE_PATTERN = Pattern.compile("\\b(1[3-9]\\d{9})\\b");
    private static final Pattern ID_PATTERN = Pattern.compile("\\b(\\d{6})(\\d{8})(\\d{4}[\\dXx]?)\\b");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("\\b([a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+)\\b");

    @Override
    public String convert(ILoggingEvent event) {
        String message = event.getFormattedMessage();
        return desensitize(message);
    }

    private String desensitize(String message) {
        if (message == null) return null;

        // 脱敏手机号
        message = replaceAll(message, PHONE_PATTERN, m -> DesensitizationUtil.maskPhone(m.group()));
        // 脱敏身份证
        message = replaceAll(message, ID_PATTERN, m -> DesensitizationUtil.maskIdCard(m.group()));
        // 脱敏邮箱
        message = replaceAll(message, EMAIL_PATTERN, m->DesensitizationUtil.maskEmail(m.group()));

        return message;
    }

    // 通用替换工具
    private String replaceAll(String input, Pattern pattern, java.util.function.Function<Matcher, String> maskFunction) {
        Matcher matcher = pattern.matcher(input);
        StringBuffer sb = new StringBuffer();

        while (matcher.find()) {
            String replacement = maskFunction.apply(matcher);
            matcher.appendReplacement(sb, Matcher.quoteReplacement(replacement));
        }

        matcher.appendTail(sb);
        return sb.toString();
    }
}
