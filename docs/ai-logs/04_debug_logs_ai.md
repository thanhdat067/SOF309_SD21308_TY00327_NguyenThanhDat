# NHẬT KÝ ỨNG DỤNG AI #04: PHÂN TÍCH STACK TRACE & DEBUG CÁC LỖI HỆ THỐNG
> **Giai đoạn:** Tuần 5 (Y6)  
> **Mục tiêu:** Thu thập các stack trace lỗi thực tế trong quá trình phát triển và kiểm thử, sử dụng AI để phân tích nguyên nhân gốc rễ (Root Cause) và đưa ra giải pháp khắc phục triệt để.  
> **Sinh viên:** Nguyễn Thành Đạt - MSSV: TY00327 - Lớp: SD21308  

---

## 1. SỰ CỐ #01: LỖI KẾT NỐI DATABASE VÀ THÔNG BÁO "TÀI KHOẢN KHÔNG TỒN TẠI"
* **Triệu chứng lỗi:** Khi người dùng nhập đúng tài khoản `admin` / `admin123` trên form web, hệ thống luôn trả về: *"Tài khoản không tồn tại"* hoặc *"Có lỗi xảy ra khi tạo tài khoản (kết nối cơ sở dữ liệu thất bại)"*.
* **Stack trace thu thập được:**
  ```text
  com.microsoft.sqlserver.jdbc.SQLServerException: The TCP/IP connection to the host localhost, port 1433 has failed.
  Error: "Connection refused: connect. Verify the connection properties. Make sure that an instance of SQL Server is running on the host and accepting TCP/IP connections at the port."
  ```
* **Prompt gửi cho AI:**
  > *"Ứng dụng Java Servlet trên Tomcat 10 kết nối tới SQL Server LocalDB (MSSQLLocalDB) qua JDBC báo lỗi Connection refused trên port 1433. Tại sao và cách xử lý thế nào?"*
* **AI phân tích nguyên nhân gốc rễ:**
  * SQL Server LocalDB là bản rút gọn dành cho lập trình cá nhân, nó chỉ giao tiếp qua cơ chế *Windows Named Pipe* (`\\.\pipe\LOCALDB#...`) và **hoàn toàn không hỗ trợ giao thức mạng TCP/IP**. Driver JDBC của Microsoft lại chỉ hỗ trợ kết nối qua TCP/IP.
* **Giải pháp khắc phục:**
  1. Cài đặt bản **SQL Server 2022 Developer Edition** chính thức của Microsoft.
  2. Bật giao thức mạng TCP/IP và cấu hình cố định cổng `1433` trong registry / SQL Server Configuration Manager.
  3. Bật Mixed Mode Authentication và kích hoạt user `sa` với mật khẩu `123456`.
  4. Chạy script `setup_database.sql` tạo database `QLBida5` và các bảng mẫu. Kết nối thành công 100%!

---

## 2. SỰ CỐ #02: LỖI HTTP 500 (ClassNotFoundException TRÊN TRANG JSP)
* **Triệu chứng lỗi:** Đăng nhập thành công chuyển hướng sang `/user/trangchunguoidung.jsp` thì trình duyệt báo lỗi:
  ```text
  HTTP Status 500 - Internal Server Error
  org.apache.jasper.JasperException: java.lang.ClassNotFoundException: org.apache.jsp.user.trangchunguoidung_jsp
  at org.apache.jasper.servlet.JspServletWrapper.getServlet(JspServletWrapper.java:167)
  ```
* **Prompt gửi cho AI:**
  > *"Tomcat 10.1 báo lỗi JasperException ClassNotFoundException khi truy cập trang JSP có dùng thẻ JSTL c:choose và c:if. Nguyên nhân và cách fix?"*
* **AI phân tích:**
  * Tomcat 10 sử dụng chuẩn **Jakarta EE 10** (`jakarta.servlet.*`), trong khi file JSP đang dùng URI cũ của Java EE (`http://java.sun.com/jsp/jstl/core`). Ngoài ra, thư mục `WEB-INF/lib` chưa có 2 file jar của JSTL 3.0.
* **Giải pháp khắc phục:**
  1. Thêm dependency `jakarta.servlet.jsp.jstl-api:3.0.0` và `org.glassfish.web:jakarta.servlet.jsp.jstl:3.0.1` vào `pom.xml`.
  2. Đặt 2 file jar JSTL vào `src/main/web/WEB-INF/lib`.
  3. Đổi URI ở đầu các file JSP thành `<%@ taglib prefix="c" uri="jakarta.tags.core" %>`. Trang chủ người dùng hiển thị mượt mà ngay lập tức.
