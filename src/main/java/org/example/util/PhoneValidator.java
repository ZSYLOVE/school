package org.example.util;

import java.util.regex.Pattern;

/**
 * 手机号码校验工具类
 */
public class PhoneValidator {
    
    /**
     * 中国大陆手机号码的正则表达式
     * 支持的号段：
     * 移动：134, 135, 136, 137, 138, 139, 147, 150, 151, 152, 157, 158, 159, 170, 178, 182, 183, 184, 187, 188, 198
     * 联通：130, 131, 132, 145, 155, 156, 166, 170, 171, 175, 176, 185, 186
     * 电信：133, 149, 153, 170, 173, 177, 180, 181, 189, 199
     */
    private static final String PHONE_PATTERN = 
        "^1(3[0-9]|4[579]|5[0-35-9]|6[6]|7[01235-8]|8[0-9]|9[189])\\d{8}$";
    
    /**
     * 编译后的正则表达式模式，提高性能
     */
    private static final Pattern PATTERN = Pattern.compile(PHONE_PATTERN);
    
    /**
     * 校验手机号码格式是否正确
     * 
     * @param phone 手机号码
     * @return true: 格式正确, false: 格式错误
     */
    public static boolean isValidPhone(String phone) {
        // 检查是否为null或空
        if (phone == null || phone.isEmpty()) {
            return false;
        }
        
        // 检查长度是否为11位
        if (phone.length() != 11) {
            return false;
        }
        
        // 使用正则表达式校验格式
        return PATTERN.matcher(phone).matches();
    }

    /**
     * 手机号脱敏处理，保留前3位和后4位
     * 
     * @param phone 手机号
     * @return 脱敏后的手机号，如：138****1234
     */
    public static String maskPhone(String phone) {
        if (phone == null || phone.length() != 11) {
            return phone;
        }
        return phone.substring(0, 3) + "****" + phone.substring(7);
    }
}