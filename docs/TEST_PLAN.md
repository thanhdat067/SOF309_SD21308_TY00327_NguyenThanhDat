# KẾ HOẠCH KIỂM THỬ TỔNG THỂ (MASTER TEST PLAN)
> **Dự án:** Hệ Thống Quản Lý Câu Lạc Bộ Bida H.N.D.D  
> **Sinh viên:** Nguyễn Thành Đạt - MSSV: TY00327 - Lớp: SD21308  
> **Tiêu chuẩn áp dụng:** IEEE 829 & Quy chuẩn kiểm thử môn học SOF309  

---

## 1. MỤC TIÊU KIỂM THỬ (TEST OBJECTIVES)
1. Đảm bảo tính đúng đắn của logic nghiệp vụ Backend (Authentication, Validation, Business Rules) thông qua hệ thống **Unit Test (JUnit 5)** với tỷ lệ Pass 100%.
2. Tự động hóa kiểm thử giao diện người dùng (End-to-End UI Testing) bằng **Selenium WebDriver** kết hợp **TestNG Framework** theo mô hình **Page Object Model (POM)**.
3. Áp dụng kỹ thuật kiểm thử theo hướng dữ liệu (**Data-Driven Testing**) để kiểm thử lặp với nhiều tập dữ liệu biên.
4. Tích hợp liên tục (**CI/CD**) với **GitHub Actions**: Tự động kích hoạt kiểm thử Headless Chrome mỗi khi đẩy code lên nhánh `main`/`master`, tự động upload báo cáo kiểm thử và chụp ảnh màn hình lỗi vào Artifacts.

---

## 2. PHẠM VI KIỂM THỬ (TEST SCOPE)

### 2.1. Trong phạm vi kiểm thử (In-Scope)
* **Phân hệ Xác thực & Người dùng:**
  * Kiểm thử luồng Đăng nhập (thành công, sai tài khoản, sai mật khẩu, để trống form).
  * Kiểm thử luồng Đăng ký (độ dài username, trùng username, định dạng email, độ dài mật khẩu).
  * Kiểm thử phân quyền truy cập theo Role (`admin`, `Nhân viên`, `Khách hàng`).
* **Phân hệ Quản lý Bàn Bida:**
  * Thao tác thêm mới bàn bida với các giá trị biên (tên bàn trống, giá bàn âm, giá hợp lệ).
  * Thao tác cập nhật trạng thái bàn và xóa mềm bàn bida.
* **Tự động hóa giao diện (Selenium UI Automation):**
  * Tự động tương tác form đăng nhập, đăng ký và xác nhận thông báo lỗi/thành công.
  * Tự động chụp ảnh màn hình khi có test fail và xuất báo cáo TestNG/Surefire.

### 2.2. Ngoài phạm vi kiểm thử (Out-of-Scope)
* Kiểm thử hiệu năng chịu tải cao (Load Testing > 10,000 concurrent users).
* Kiểm thử thâm nhập an ninh mạng chuyên sâu (Penetration Testing).

---

## 3. CHIẾN LƯỢC & CÔNG CỤ KIỂM THỬ (TEST STRATEGY & TOOLS)

| Hạng mục | Công nghệ / Công cụ áp dụng | Mục đích sử dụng |
| :--- | :--- | :--- |
| **Unit Testing** | JUnit 5 (`jupiter-api`, `jupiter-params`) | Viết Unit Test cho Controller & Service, `@ParameterizedTest` + `@CsvSource`, `assertThrows()`. |
| **UI Automation** | Selenium WebDriver 4.20.0 | Tự động hóa trình duyệt Chrome, tương tác DOM với `WebDriverWait`. |
| **Test Runner & Suite** | TestNG 7.9.0 | Quản lý Suite `testng.xml`, phân nhóm `groups="smoke"`, `dependsOnMethods`, `@DataProvider`. |
| **Chụp ảnh lỗi** | `ITestListener` + `TakesScreenshot` | Tự động chụp ảnh màn hình khi xảy ra lỗi lưu vào thư mục `screenshots/`. |
| **CI/CD Pipeline** | GitHub Actions (`ubuntu-latest`) | Chạy tự động ở chế độ `--headless=new` trên đám mây, upload báo cáo vào Artifacts. |
| **AI Assistants** | Gemini & Claude AI | Hỗ trợ RAG thiết kế Test Case, sinh code kiểm thử và phân tích stack trace lỗi. |

---

## 4. TIÊU CHÍ NGHIỆM THU (EXIT CRITERIA)
* [x] 100% các Unit Test Controller chạy lệnh `mvn test` đạt **BUILD SUCCESS (0 Failure, 0 Error)**.
* [x] Kịch bản Selenium POM tương tác chính xác với các thành phần giao diện không dùng `Thread.sleep()`.
* [x] File cấu hình `testng.xml` thực thi tuần tự và phân nhóm smoke/regression chuẩn chỉ.
* [x] Workflow GitHub Actions chạy thành công và sáng đèn **Xanh lá cây (Pass)**.
* [x] Đầy đủ 6 file nhật ký minh chứng ứng dụng AI trong `docs/ai-logs/`.
