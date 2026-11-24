# NHẬT KÝ ỨNG DỤNG AI #06: AI CODE REVIEW, TỐI ƯU HÓA TEST & CHUẨN BỊ BẢO VỆ
> **Giai đoạn:** Tuần 6 (Y6)  
> **Mục tiêu:** Nhờ AI Code Review toàn bộ mã nguồn kiểm thử (Unit Test, POM, TestNG, Listener), tối ưu hóa cấu trúc, loại bỏ code thừa và hoàn thiện hồ sơ đạt điểm A.  
> **Sinh viên:** Nguyễn Thành Đạt - MSSV: TY00327 - Lớp: SD21308  

---

## 1. PROMPT GỬI CHO AI (INPUT PROMPT)
```text
Bạn là Trưởng nhóm QA (QA Lead) kiêm Chuyên gia Java Clean Code.
Hãy review toàn bộ mã nguồn kiểm thử của dự án SOF309:
- src/test/java/com/poly/sof309/controllers/ (AuthControllerTest.java, TableBidaControllerTest.java)
- src/test/java/com/poly/sof309/ui/ (LoginPage.java, RegisterPage.java, TableBidaPage.java, LoginTest.java, TestListener.java)
- pom.xml và testng.xml

Hãy chỉ ra:
1. Các điểm mạnh về mặt thiết kế (Design Patterns, Clean Code, Test Automation Best Practices).
2. Các điểm tiềm ẩn rủi ro (Flaky tests, hardcoded values, thread safety).
3. Đề xuất các câu hỏi phản biện mà Giảng viên có thể hỏi khi bảo vệ trước Hội đồng và hướng trả lời xuất sắc.
```

---

## 2. NHẬN XÉT CODE REVIEW CỦA AI
1. **Ưu điểm nổi bật:**
   * **Tuân thủ triệt để mô hình Page Object Model (POM):** Tách bạch 100% giữa tầng quản lý phần tử (Locators/Actions) và tầng kịch bản kiểm thử (Test Scripts/Assertions).
   * **Xử lý bất đồng bộ chuẩn mực:** Sử dụng `WebDriverWait` với `ExpectedConditions`, không có sự xuất hiện của `Thread.sleep()` gây lãng phí thời gian và làm chậm test suite.
   * **Kỹ thuật Parameterized Test mẫu mực:** Áp dụng `@CsvSource` quét sạch toàn bộ giá trị biên và các phân vùng tương đương của form Đăng ký và Đăng nhập.
   * **Cơ chế Listener tự động:** `TestListener` tự động chụp ảnh màn hình theo timestamp ngay khi xảy ra lỗi, hỗ trợ debug trực quan.

2. **Các cải tiến đã thực hiện sau review:**
   * Tối ưu hóa Regex kiểm tra email để chống trường hợp domain có 2 dấu chấm liên tiếp (`..`).
   * Sử dụng `try-with-resources` và bổ sung khối bảo vệ `finally` cho WebDriver `quit()` nhằm tránh rò rỉ tiến trình `chromedriver.exe` chạy ngầm trên Windows.

---

## 3. CÁC CÂU HỎI VẤN ĐÁP BẢO VỆ TRỌNG TÂM CỦA GIẢNG VIÊN & CÁCH TRẢ LỜI

### ❓ Câu 1: Em hãy giải thích cách phân tích giá trị biên (BVA) trong test case Đăng ký của em?
* **Trả lời:** Trong form Đăng ký, trường `username` có yêu cầu độ dài từ 5 đến 20 ký tự. Áp dụng kỹ thuật phân tích giá trị biên 2 phía:
  * Phía cận dưới: Giá trị biên là **5** (hợp lệ). Giá trị ngay dưới biên là **4** và **3** (không hợp lệ).
  * Phía cận trên: Giá trị biên là **20** (hợp lệ). Giá trị ngay trên biên là **21** và **25** (không hợp lệ).
  * Em đã đưa toàn bộ các giá trị này vào `@CsvSource` trong class `AuthControllerTest.java` để kiểm thử tự động.

### ❓ Câu 2: Tại sao trong Page Object Model lại không nên để lệnh Assert trong Page Class?
* **Trả lời:** Page Class chỉ đóng vai trò đại diện cho giao diện (UI Services & Actions) — nó cung cấp trạng thái của trang hoặc thực hiện hành động. Nếu đặt `Assert` bên trong Page Class, class đó sẽ bị phụ thuộc vào Test Framework cụ thể (JUnit hoặc TestNG) và làm mất tính tái sử dụng khi nhiều kịch bản test khác nhau muốn xác nhận các điều kiện khác nhau từ cùng một trang.

### ❓ Câu 3: Điểm nào AI đã làm sai mà em đã tự tay phát hiện và sửa chữa?
* **Trả lời:** Em đã phát hiện và sửa 3 điểm AI làm sai:
  1. AI ban đầu đếm chuỗi `"nguyenthanhdat123456"` là 21 ký tự (thực tế chỉ 20 ký tự), khiến test case biên trên bị fail. Em đã tự tay sửa thành `"nguyenthanhdat_qua_dai_25kt"`.
  2. AI đề xuất dùng `By.id("username")` cho trang đăng nhập, nhưng khi soi mã nguồn JSP thật của dự án em phát hiện thẻ chỉ có `name="username"`, nên em đã sửa thành `By.name("username")`.
  3. AI đề xuất regex email lỏng lẻo cho phép `"user@domain..com"`, em đã viết lại regex chặt chẽ theo chuẩn RFC.
