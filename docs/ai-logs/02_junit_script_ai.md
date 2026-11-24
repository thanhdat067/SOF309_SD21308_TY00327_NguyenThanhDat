# NHẬT KÝ ỨNG DỤNG AI #02: SINH MÃ UNIT TEST JUNIT 5 CHO CONTROLLER
> **Giai đoạn:** Tuần 2 (Y3)  
> **Mục tiêu:** Nhờ AI sinh mã nguồn kiểm thử Unit Test cho Controller bằng JUnit 5 nâng cao, tích hợp `@ParameterizedTest`, `@CsvSource`, `assertThrows()` và `@Timeout`.  
> **Sinh viên:** Nguyễn Thành Đạt - MSSV: TY00327 - Lớp: SD21308  

---

## 1. PROMPT GỬI CHO AI (INPUT PROMPT)
```text
Bạn là chuyên gia tự động hóa kiểm thử phần mềm bằng Java & JUnit 5.
Dựa vào Bộ Test Case trong docs/TEST_CASES.md, hãy viết mã nguồn Unit Test cho 2 class:
1. AuthControllerTest.java: Kiểm thử chức năng xác thực và đăng nhập.
   - Yêu cầu bắt buộc 1: Sử dụng @ParameterizedTest kết hợp @CsvSource để quét toàn bộ giá trị biên của username (3, 4, 5, 20, 21 ký tự) và email chuẩn RFC.
   - Yêu cầu bắt buộc 2: Sử dụng assertThrows() kiểm thử ngoại lệ IllegalArgumentException khi dữ liệu rỗng và IllegalStateException khi tài khoản đã tồn tại.
   - Yêu cầu bắt buộc 3: Áp dụng @Timeout(2s) kiểm tra hiệu năng mã hóa mật khẩu BCrypt.
2. TableBidaControllerTest.java: Kiểm thử logic quản lý bàn bida (tên bàn, giá bàn, trạng thái).
   - Yêu cầu: Kiểm tra giá trị biên giá bàn âm, tên bàn rỗng, trạng thái bàn hợp lệ.
Mã nguồn phải đạt chuẩn Pass 100% khi chạy 'mvn test'.
```

---

## 2. KẾT QUẢ MÃ NGUỒN DO AI SINH RA
AI đã tạo ra 2 class kiểm thử hoàn chỉnh:
* `src/test/java/com/poly/sof309/controllers/AuthControllerTest.java` (24 ca kiểm thử tham số hóa).
* `src/test/java/com/poly/sof309/controllers/TableBidaControllerTest.java` (18 ca kiểm thử tham số hóa).

---

## 3. CAN THIỆP CHỈNH SỬA CỦA SINH VIÊN (DEBUG & REFACTOR)
Khi chạy lần đầu qua lệnh `mvn test`, gặp 2 lỗi nhỏ do AI tính toán sai:
1. **Lỗi chuỗi 21 ký tự:** AI đếm chuỗi `"nguyenthanhdat123456"` là 21 ký tự nhưng thực tế chỉ có 20 ký tự $\rightarrow$ Mình đã sửa lại thành chuỗi `"nguyenthanhdat_qua_dai_25kt"`.
2. **Lỗi Regex Email:** AI sử dụng regex lỏng lẻo cho phép `"username@domain..com"` $\rightarrow$ Mình đã siết lại regex `^[A-Za-z0-9+_.-]+@([A-Za-z0-9-]+\\.)+[A-Za-z]{2,}$`.
3. **Kết quả sau sửa đổi:** Chạy lệnh `mvn test -Dtest=*ControllerTest` đạt **44/44 Test Pass 100% (0 Failure, 0 Error)**.
