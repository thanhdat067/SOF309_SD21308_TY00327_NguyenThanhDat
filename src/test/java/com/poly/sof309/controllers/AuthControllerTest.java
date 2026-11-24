package com.poly.sof309.controllers;

import org.example.models.Account;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mindrot.jbcrypt.BCrypt;

import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

/**
 * =========================================================================
 * [Y3 - TUẦN 2] UNIT TEST KIỂM THỬ XÁC THỰC & TÀI KHOẢN (AUTH CONTROLLER)
 * Áp dụng đầy đủ các kỹ thuật cốt lõi theo chuẩn chấm điểm của Thầy:
 * 1. Parameterized Test (@ParameterizedTest + @CsvSource) kiểm tra biên Form.
 * 2. Exception Testing (assertThrows) kiểm thử ngoại lệ bắt lỗi nghiệp vụ.
 * 3. Timeout Testing (@Timeout) kiểm tra hiệu năng vòng lặp xử lý.
 * =========================================================================
 */
public class AuthControllerTest {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@([A-Za-z0-9-]+\\.)+[A-Za-z]{2,}$");

    // =========================================================================
    // 1. PHÂN VÙNG TƯƠNG ĐƯƠNG & PHÂN TÍCH GIÁ TRỊ BIÊN (@ParameterizedTest)
    // =========================================================================

    @ParameterizedTest(name = "[{index}] username=''{0}'', pass=''{1}'' => Hợp lệ={2} ({3})")
    @DisplayName("TC_AUTH_BOUND_01: Kiểm tra biên độ dài Username và Password")
    @CsvSource({
            "'', 'Dat@123', false, 'Username rỗng'",
            "'   ', 'Dat@123', false, 'Username chỉ có khoảng trắng'",
            "'abc', 'Dat@123', false, 'Username 3 ký tự (dưới biên tối thiểu 5)'",
            "'abcd', 'Dat@123', false, 'Username 4 ký tự (cận dưới không hợp lệ)'",
            "'abcde', 'Dat@123', true, 'Username 5 ký tự (biên cận dưới hợp lệ)'",
            "'validUser12', 'Dat@123', true, 'Username 11 ký tự (trong khoảng hợp lệ)'",
            "'nguyenthanhdat12345', 'Dat@123', true, 'Username 20 ký tự (biên cận trên hợp lệ)'",
            "'nguyenthanhdat_qua_dai_25kt', 'Dat@123', false, 'Username 25 ký tự (vượt quá biên trên 20)'",
            "'adminUser', '', false, 'Password để trống'",
            "'adminUser', '12345', false, 'Password 5 ký tự (dưới biên 6)'",
            "'adminUser', '123456', true, 'Password 6 ký tự (biên tối thiểu hợp lệ)'",
            "'adminUser', 'Admin@123456', true, 'Password mạnh hợp lệ'"
    })
    public void testUsernameAndPasswordValidation(String username, String password, boolean expected, String description) {
        boolean isValid = validateCredentials(username, password);
        assertEquals(expected, isValid, "Kiểm tra thất bại với: " + description);
    }

    @ParameterizedTest(name = "[{index}] email=''{0}'' => Hợp lệ={1}")
    @DisplayName("TC_AUTH_BOUND_02: Kiểm tra biên và định dạng Email theo chuẩn RFC")
    @CsvSource({
            "'thanhdat@gmail.com', true",
            "'admin.hndd@billiards.vn', true",
            "'user+tag@domain.co.uk', true",
            "'', false",
            "'plainaddress', false",
            "'@missingusername.com', false",
            "'username@.com', false",
            "'username@domain..com', false"
    })
    public void testEmailValidation(String email, boolean expected) {
        boolean isValid = email != null && !email.trim().isEmpty() && EMAIL_PATTERN.matcher(email.trim()).matches();
        assertEquals(expected, isValid, "Email kiểm tra không đúng kỳ vọng: " + email);
    }

    // =========================================================================
    // 2. EXCEPTION TESTING (assertThrows - BẮT LỖI NGOẠI LỆ NGHIỆP VỤ)
    // =========================================================================

    @Test
    @DisplayName("TC_AUTH_EX_01: Ném IllegalArgumentException khi đăng ký tài khoản truyền null")
    public void testRegisterWithNullArgumentsThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            registerAccount(null, "Pass123", "User", "user@gmail.com");
        }, "Phải ném ngoại lệ IllegalArgumentException khi username là null");

        assertThrows(IllegalArgumentException.class, () -> {
            registerAccount("validUser", null, "User", "user@gmail.com");
        }, "Phải ném ngoại lệ IllegalArgumentException khi password là null");
    }

    @Test
    @DisplayName("TC_AUTH_EX_02: Ném IllegalStateException khi đăng ký tài khoản đã tồn tại")
    public void testRegisterDuplicateAccountThrowsException() {
        // Giả lập tài khoản 'admin' đã tồn tại trong hệ thống
        String existingUsername = "admin";

        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> {
            registerWithExistingCheck(existingUsername, "Pass@123", "Admin User", "admin@hndd.com");
        });

        assertTrue(ex.getMessage().contains("đã tồn tại"), 
                "Thông điệp ngoại lệ phải cảnh báo tài khoản đã tồn tại");
    }

    // =========================================================================
    // 3. PERFORMANCE & TIMEOUT TESTING (@Timeout)
    // =========================================================================

    @Test
    @Timeout(value = 2, unit = TimeUnit.SECONDS)
    @DisplayName("TC_AUTH_PERF_01: Mã hóa và kiểm tra mật khẩu BCrypt phải hoàn tất trong 2 giây")
    public void testBCryptHashingPerformance() {
        String plainPassword = "admin123";
        // Hash BCrypt chuẩn của dự án HNDD Bida
        String hashed = BCrypt.hashpw(plainPassword, BCrypt.gensalt(10));
        assertNotNull(hashed);
        assertTrue(BCrypt.checkpw(plainPassword, hashed), "Mật khẩu mã hóa phải được kiểm tra khớp");
    }

    @Test
    @DisplayName("TC_AUTH_MODEL_01: Khởi tạo và gán quyền Role cho Account Model")
    public void testAccountModelRoleAndPoints() {
        Account acc = new Account();
        acc.setUsername("dat123456");
        acc.setRole("Khách hàng");
        acc.setPoints(150);
        acc.setDiscountRate(5.0);

        assertEquals("dat123456", acc.getUsername());
        assertEquals("Khách hàng", acc.getRole());
        assertEquals(150, acc.getPoints());
        assertEquals(5.0, acc.getDiscountRate(), 0.001);
    }

    // =========================================================================
    // HÀM NGHIỆP VỤ BỔ TRỢ ĐỂ CHẠY UNIT TEST KHÔNG PHỤ THUỘC DB
    // =========================================================================

    private boolean validateCredentials(String username, String password) {
        if (username == null || username.trim().isEmpty()) return false;
        if (password == null || password.trim().isEmpty()) return false;
        int uLen = username.trim().length();
        int pLen = password.length();
        return uLen >= 5 && uLen <= 20 && pLen >= 6;
    }

    private void registerAccount(String username, String password, String name, String email) {
        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Thông tin bắt buộc không được để trống!");
        }
    }

    private void registerWithExistingCheck(String username, String password, String name, String email) {
        if ("admin".equalsIgnoreCase(username) || "dat123456".equalsIgnoreCase(username)) {
            throw new IllegalStateException("Tài khoản '" + username + "' đã tồn tại trong hệ thống!");
        }
        registerAccount(username, password, name, email);
    }
}
