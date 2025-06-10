package com.yanhuo.platform.unit.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

/**
 * 🔧 字符串工具类单元测试示例
 * 
 * 这是一个纯静态方法的单元测试示例，展示：
 * ✅ 无需Mock的纯逻辑测试
 * ✅ 参数化测试
 * ✅ 边界条件测试
 * ✅ 异常场景测试
 */
@DisplayName("🔧 字符串工具类测试")
class StringUtilTest {

    /**
     * 🧪 测试：判断字符串是否为空
     */
    @Test
    @DisplayName("✅ 应该能正确判断字符串是否为空")
    void shouldCheckIfStringIsEmpty() {
        // 测试空字符串
        assertThat(StringUtil.isEmpty("")).isTrue();
        assertThat(StringUtil.isEmpty(null)).isTrue();
        assertThat(StringUtil.isEmpty("   ")).isTrue(); // 只有空格
        
        // 测试非空字符串
        assertThat(StringUtil.isEmpty("hello")).isFalse();
        assertThat(StringUtil.isEmpty(" hello ")).isFalse();
        assertThat(StringUtil.isEmpty("123")).isFalse();
    }

    /**
     * 🧪 测试：判断字符串是否不为空
     */
    @Test
    @DisplayName("✅ 应该能正确判断字符串是否不为空")
    void shouldCheckIfStringIsNotEmpty() {
        // 测试非空字符串
        assertThat(StringUtil.isNotEmpty("hello")).isTrue();
        assertThat(StringUtil.isNotEmpty(" hello ")).isTrue();
        assertThat(StringUtil.isNotEmpty("123")).isTrue();
        
        // 测试空字符串
        assertThat(StringUtil.isNotEmpty("")).isFalse();
        assertThat(StringUtil.isNotEmpty(null)).isFalse();
        assertThat(StringUtil.isNotEmpty("   ")).isFalse();
    }

    /**
     * 🧪 参数化测试：测试字符串去除空格
     */
    @ParameterizedTest
    @DisplayName("✂️ 应该能正确去除字符串两端空格")
    @MethodSource("provideTrimTestCases")
    void shouldTrimStringCorrectly(String input, String expected) {
        String result = StringUtil.trim(input);
        assertThat(result).isEqualTo(expected);
    }

    /**
     * 🧪 参数化测试：测试字符串长度验证
     */
    @ParameterizedTest
    @DisplayName("📏 应该能正确验证字符串长度")
    @ValueSource(strings = {"", "a", "ab", "abc", "abcd", "abcde"})
    void shouldValidateStringLength(String input) {
        boolean result = StringUtil.isValidLength(input, 1, 5);
        
        if (input.length() >= 1 && input.length() <= 5) {
            assertThat(result).isTrue();
        } else {
            assertThat(result).isFalse();
        }
    }

    /**
     * 🧪 测试：字符串格式化
     */
    @Test
    @DisplayName("📝 应该能正确格式化字符串")
    void shouldFormatStringCorrectly() {
        // 测试普通格式化
        String result = StringUtil.format("Hello {}, welcome to {}!", "张三", "烟火平台");
        assertThat(result).isEqualTo("Hello 张三, welcome to 烟火平台!");
        
        // 测试数字格式化
        String numberResult = StringUtil.format("今天是{}年{}月{}日", 2024, 1, 15);
        assertThat(numberResult).isEqualTo("今天是2024年1月15日");
    }

    /**
     * 🧪 测试：字符串掩码
     */
    @Test
    @DisplayName("🎭 应该能正确对敏感信息进行掩码处理")
    void shouldMaskSensitiveInformation() {
        // 测试手机号掩码
        assertThat(StringUtil.maskPhone("13812345678")).isEqualTo("138****5678");
        assertThat(StringUtil.maskPhone("1381234567")).isEqualTo("138****567"); // 10位
        
        // 测试邮箱掩码
        assertThat(StringUtil.maskEmail("test@example.com")).isEqualTo("t***@example.com");
        assertThat(StringUtil.maskEmail("a@b.com")).isEqualTo("*@b.com"); // 短邮箱
        
        // 测试身份证掩码
        assertThat(StringUtil.maskIdCard("123456789012345678")).isEqualTo("123456****345678");
    }

    /**
     * 🧪 测试：字符串转换
     */
    @Test
    @DisplayName("🔄 应该能正确进行字符串转换")
    void shouldConvertStringCorrectly() {
        // 测试转驼峰命名
        assertThat(StringUtil.toCamelCase("user_name")).isEqualTo("userName");
        assertThat(StringUtil.toCamelCase("first_name_last")).isEqualTo("firstNameLast");
        assertThat(StringUtil.toCamelCase("single")).isEqualTo("single");
        
        // 测试转下划线命名
        assertThat(StringUtil.toSnakeCase("userName")).isEqualTo("user_name");
        assertThat(StringUtil.toSnakeCase("firstName")).isEqualTo("first_name");
        assertThat(StringUtil.toSnakeCase("XMLHttpRequest")).isEqualTo("xml_http_request");
    }

    /**
     * 🧪 测试：异常场景
     */
    @Test
    @DisplayName("❌ 应该在异常情况下正确处理")
    void shouldHandleExceptionalCases() {
        // 测试null值处理
        assertThat(StringUtil.format(null, "test")).isEqualTo("");
        assertThat(StringUtil.maskPhone(null)).isNull();
        assertThat(StringUtil.toCamelCase(null)).isNull();
        
        // 测试无效参数
        assertThatThrownBy(() -> StringUtil.isValidLength("test", -1, 5))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("最小长度不能小于0");
        
        assertThatThrownBy(() -> StringUtil.isValidLength("test", 5, 3))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("最大长度不能小于最小长度");
    }

    /**
     * 🧪 测试：性能测试
     */
    @Test
    @DisplayName("⚡ 字符串操作应该有良好的性能")
    void shouldHaveGoodPerformance() {
        // 测试大量字符串操作的性能
        long startTime = System.currentTimeMillis();
        
        for (int i = 0; i < 10000; i++) {
            StringUtil.isEmpty("test" + i);
            StringUtil.trim("  test" + i + "  ");
            StringUtil.format("Hello {}", i);
        }
        
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        
        // 10000次操作应该在1秒内完成
        assertThat(duration).isLessThan(1000);
    }

    /**
     * 🔧 测试数据提供者：trim测试用例
     */
    private static Stream<Arguments> provideTrimTestCases() {
        return Stream.of(
            Arguments.of("  hello  ", "hello"),
            Arguments.of("hello", "hello"),
            Arguments.of("", ""),
            Arguments.of("   ", ""),
            Arguments.of("\t\n hello \t\n", "hello"),
            Arguments.of(null, null)
        );
    }

    /**
     * 📋 静态内部类：模拟被测试的工具类
     * 实际项目中这应该是独立的工具类
     */
    static class StringUtil {
        
        public static boolean isEmpty(String str) {
            return str == null || str.trim().isEmpty();
        }
        
        public static boolean isNotEmpty(String str) {
            return !isEmpty(str);
        }
        
        public static String trim(String str) {
            return str == null ? null : str.trim();
        }
        
        public static boolean isValidLength(String str, int minLength, int maxLength) {
            if (minLength < 0) {
                throw new IllegalArgumentException("最小长度不能小于0");
            }
            if (maxLength < minLength) {
                throw new IllegalArgumentException("最大长度不能小于最小长度");
            }
            if (str == null) {
                return false;
            }
            int length = str.length();
            return length >= minLength && length <= maxLength;
        }
        
        public static String format(String template, Object... args) {
            if (template == null) {
                return "";
            }
            String result = template;
            for (Object arg : args) {
                result = result.replaceFirst("\\{\\}", String.valueOf(arg));
            }
            return result;
        }
        
        public static String maskPhone(String phone) {
            if (phone == null || phone.length() < 7) {
                return phone;
            }
            int length = phone.length();
            if (length == 11) {
                return phone.substring(0, 3) + "****" + phone.substring(7);
            } else {
                return phone.substring(0, 3) + "****" + phone.substring(length - 3);
            }
        }
        
        public static String maskEmail(String email) {
            if (email == null || !email.contains("@")) {
                return email;
            }
            String[] parts = email.split("@");
            String localPart = parts[0];
            if (localPart.length() <= 1) {
                return "*@" + parts[1];
            }
            return localPart.charAt(0) + "***@" + parts[1];
        }
        
        public static String maskIdCard(String idCard) {
            if (idCard == null || idCard.length() < 8) {
                return idCard;
            }
            return idCard.substring(0, 6) + "****" + idCard.substring(idCard.length() - 6);
        }
        
        public static String toCamelCase(String str) {
            if (str == null || str.isEmpty()) {
                return str;
            }
            String[] parts = str.split("_");
            StringBuilder result = new StringBuilder(parts[0]);
            for (int i = 1; i < parts.length; i++) {
                if (!parts[i].isEmpty()) {
                    result.append(Character.toUpperCase(parts[i].charAt(0)))
                          .append(parts[i].substring(1));
                }
            }
            return result.toString();
        }
        
        public static String toSnakeCase(String str) {
            if (str == null || str.isEmpty()) {
                return str;
            }
            return str.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
        }
    }
} 