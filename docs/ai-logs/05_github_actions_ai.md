# NHẬT KÝ ỨNG DỤNG AI #05: THIẾT KẾ CI/CD PIPELINE VỚI GITHUB ACTIONS
> **Giai đoạn:** Tuần 5 (Y5)  
> **Mục tiêu:** Cấu hình đường ống tự động hóa kiểm thử liên tục (CI/CD) trên GitHub Actions, chạy trên Ubuntu runner, đảm bảo pipeline luôn xanh (Pass) và xuất báo cáo tự động vào Artifacts.  
> **Sinh viên:** Nguyễn Thành Đạt - MSSV: TY00327 - Lớp: SD21308  

---

## 1. PROMPT GỬI CHO AI (INPUT PROMPT)
```text
Tôi cần cấu hình một file workflow GitHub Actions (.github/workflows/test.yml) cho dự án kiểm thử SOF309.
Yêu cầu kỹ thuật:
1. Trigger: Tự động chạy khi có sự kiện push hoặc pull_request vào nhánh main/master.
2. Môi trường chạy: ubuntu-latest với Java JDK 17 (Temurin distribution, có cache maven).
3. Câu lệnh chạy kiểm thử: Chạy Unit Test Controller (JUnit 5) với lệnh 'mvn test -Dtest=*ControllerTest'.
4. Xuất Artifacts: Sử dụng actions/upload-artifact@v4 để tải lên toàn bộ báo cáo target/surefire-reports/ và ảnh chụp lỗi trong screenshots/ (nếu có) ngay cả khi test fail (dùng if: always()).
Hãy viết file YAML chuẩn xác nhất.
```

---

## 2. KẾT QUẢ CẤU HÌNH DO AI CUNG CẤP
File cấu hình hoàn chỉnh tại `.github/workflows/test.yml`:
```yaml
name: CI Automation Test Suite

on:
  push:
    branches: [ main, master ]
  pull_request:
    branches: [ main, master ]
  workflow_dispatch:

jobs:
  test:
    runs-on: ubuntu-latest

    steps:
    - name: Checkout Source Code
      uses: actions/checkout@v4

    - name: Set up JDK 17 LTS (Temurin)
      uses: actions/setup-java@v4
      with:
        java-version: '17'
        distribution: 'temurin'
        cache: maven

    - name: Run Controller Unit Tests (JUnit 5)
      run: mvn test -Dtest=*ControllerTest

    - name: Upload Test Execution Reports & Screenshots
      if: always()
      uses: actions/upload-artifact@v4
      with:
        name: test-execution-results
        path: |
          target/surefire-reports/
          screenshots/
```

---

## 3. ĐÁNH GIÁ CỦA SINH VIÊN & KẾT QUẢ THỰC TẾ
* **Tính ổn định (Reliability):** Việc chạy Unit Test Controller trên môi trường CI không phụ thuộc vào cơ sở dữ liệu bên ngoài giúp pipeline luôn đạt 100% Pass và sáng đèn xanh (Success).
* **Quản lý Artifacts:** Toàn bộ log XML và HTML của Surefire được đóng gói thành file zip `test-execution-results.zip` trong tab Actions, thỏa mãn hoàn toàn tiêu chí nghiệm thu Tuần 5.
