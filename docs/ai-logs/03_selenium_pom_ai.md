# NHẬT KÝ ỨNG DỤNG AI #03: XÂY DỰNG SELENIUM POM & TESTNG FRAMEWORK
> **Giai đoạn:** Tuần 3 & Tuần 4 (Y4)  
> **Mục tiêu:** Thiết kế khung kiểm thử giao diện tự động theo mô hình Page Object Model (POM), nâng cấp TestNG Suite với `@DataProvider`, phân nhóm `smoke/regression` và tự động chụp ảnh màn hình lỗi.  
> **Sinh viên:** Nguyễn Thành Đạt - MSSV: TY00327 - Lớp: SD21308  

---

## 1. PROMPT GỬI CHO AI (INPUT PROMPT)
```text
Tôi đang xây dựng khung Selenium UI Automation Test cho dự án Quản Lý Bida H.N.D.D.
Dưới đây là mã nguồn HTML/JSP của trang đăng nhập (dangnhap.jsp) và trang đăng ký (dangky.jsp).

Yêu cầu thực hiện:
1. Tạo Page Classes theo mô hình POM: LoginPage.java, RegisterPage.java, TableBidaPage.java.
   - Quản lý Locator rõ ràng với By.
   - Tuyệt đối không dùng Thread.sleep(), bắt buộc dùng WebDriverWait xử lý bất đồng bộ.
2. Viết TestNG Test Suite (LoginTest.java):
   - Phân nhóm: @Test(groups = "smoke") cho luồng đăng nhập admin thành công.
   - Ràng buộc luồng: Dùng dependsOnMethods = {"testLoginAdminSuccess"}.
   - Data-Driven Testing: Sử dụng @DataProvider nạp danh sách các tài khoản không hợp lệ.
3. Tạo TestListener implements ITestListener: Tự động chụp ảnh màn hình lưu vào thư mục screenshots/ mỗi khi test fail.
4. Tạo file cấu hình testng.xml.
```

---

## 2. KẾT QUẢ MÃ NGUỒN ĐẠT ĐƯỢC
* Thư mục `src/test/java/com/poly/sof309/ui/pages/`:
  * `LoginPage.java`
  * `RegisterPage.java`
  * `TableBidaPage.java`
* Thư mục `src/test/java/com/poly/sof309/ui/listeners/`:
  * `TestListener.java` (Lắng nghe sự kiện `onTestFailure` và dùng `TakesScreenshot` xuất file `.png` theo timestamp).
* Thư mục `src/test/java/com/poly/sof309/ui/tests/`:
  * `LoginTest.java` (Hỗ trợ cả chế độ local và `--headless=new` trên CI).
* File `testng.xml` ở thư mục gốc của dự án.

---

## 3. CAN THIỆP CHỈNH SỬA CỦA SINH VIÊN
* AI ban đầu dùng `By.id("username")`, nhưng trong form thật của web chỉ có `name="username"`. Mình đã đọc mã nguồn JSP thật và chỉnh sửa lại thành `By.name("username")`, `By.name("password")` để Selenium tìm đúng phần tử DOM.
* Bổ sung cơ chế kiểm tra môi trường `CI` để tự động bật cờ Chrome Headless khi chạy trên GitHub Actions.
