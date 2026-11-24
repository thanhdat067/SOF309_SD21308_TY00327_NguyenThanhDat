# ĐẶC TẢ NGHIỆP VỤ HỆ THỐNG QUẢN LÝ BIDA H.N.D.D (SPECIFICATIONS)
> **Dự án:** H.N.D.D Premium Billiards Management System  
> **Môn học:** SOF309 - Kiểm thử nâng cao cùng AI  
> **Sinh viên thực hiện:** Nguyễn Thành Đạt  
> **Mã số sinh viên (MSSV):** TY00327  
> **Lớp:** SD21308  
> **Giảng viên hướng dẫn:** ThS. Trần Quang Bình (binhtq7)  

---

## 1. TỔNG QUAN HỆ THỐNG VÀ KIẾN TRÚC KỸ THUẬT
* **Tên hệ thống:** Hệ thống Quản lý và Đặt bàn Câu lạc bộ Bida Cao cấp H.N.D.D
* **Nền tảng công nghệ:**
  * **Ngôn ngữ & Runtime:** Java 17 LTS (Microsoft OpenJDK 17)
  * **Kiến trúc ứng dụng:** Jakarta EE 10 / MVC (Model - View - Controller), Servlet 6.0, JSP 3.1, JSTL 3.0
  * **Hệ quản trị cơ sở dữ liệu:** Microsoft SQL Server 2022 Developer Edition (TCP Port: 1433, Database: `QLBida5`)
  * **Mã hóa bảo mật:** jBCrypt 0.4 (Mã hóa mật khẩu một chiều với Salt ngẫu nhiên)
  * **Máy chủ ứng dụng (Web Server):** Apache Tomcat 10.1.54 (SmartTomcat Plugin)
  * **Giao diện người dùng:** HTML5, TailwindCSS, FontAwesome 6, Google Fonts
  * **URL kiểm thử tại máy Local:** `http://localhost:8080`

---

## 2. QUY TẮC NGHIỆP VỤ CHI TIẾT (BUSINESS RULES)

### 📌 Phân hệ 1: Xác thực & Tài khoản (Authentication & Authorization)
* **URL chức năng:**
  * Đăng nhập: `/dangnhap`
  * Đăng ký: `/dangky`
  * Quên mật khẩu & OTP: `/quenmatkhau`, `/xacNhanOTP`
  * Đăng xuất: `/dangnhap?action=logout`

* **Quy tắc kiểm tra Form Đăng ký:**
  1. `username`: Bắt buộc nhập, độ dài từ 5 đến 20 ký tự, không chứa dấu cách hoặc ký tự đặc biệt, không được trùng lặp với tài khoản đã tồn tại trong DB (`daTonTaiUserName == false`).
  2. `email`: Bắt buộc nhập, phải đúng định dạng chuẩn RFC 5322 (ví dụ: `name@domain.com`), không được trùng lặp trong hệ thống (`daTonTaiEmail == false`).
  3. `password`: Tối thiểu 6 ký tự, khuyến nghị có chữ hoa, chữ thường và chữ số.
  4. `phone`: Chuỗi số từ 10 đến 11 chữ số, bắt đầu bằng đầu số hợp lệ của Việt Nam (03, 05, 07, 08, 09).
  5. `role`: Mặc định khi khách hàng tự đăng ký là `Khách hàng` (hoặc quyền `admin`, `Nhân viên` do quản trị tạo).

* **Quy tắc kiểm tra Form Đăng nhập:**
  1. Kiểm tra để trống: Cả `username` và `password` không được để trống hoặc chỉ chứa khoảng trắng.
  2. Kiểm tra tồn tại: Nếu `username` không tồn tại trong hệ thống $\rightarrow$ Báo lỗi: *"Tài khoản không tồn tại"*.
  3. Kiểm tra mật khẩu: So sánh mật khẩu người dùng nhập với hash trong database bằng `BCrypt.checkpw(password, hash)` $\rightarrow$ Nếu sai báo lỗi: *"Mật khẩu không đúng"*.
  4. Phân quyền điều hướng sau đăng nhập:
     * `role == "admin"` $\rightarrow$ Chuyển hướng tới `/admin/quanli.jsp`.
     * `role == "Nhân viên"` $\rightarrow$ Chuyển hướng tới `/employee/trangchunhanvien`.
     * `role == "Khách hàng"` $\rightarrow$ Chuyển hướng tới `/user/trangchunguoidung`.

---

### 📌 Phân hệ 2: Quản lý Bàn Bida (Table Bida Management - CRUD)
* **URL chức năng:**
  * Danh sách & Quản lý bàn: `/admin/quanliTableBida.jsp` (Controller: `TableBidaServlet`)
  * Thêm bàn mới: `ThemTableBidaServlet` (POST)
  * Sửa / Đổi trạng thái: `TableBidaServlet?action=update`
  * Xóa bàn: `TableBidaServlet?action=delete&id=...` (Xóa mềm: gán `is_deleted = 1`)

* **Ràng buộc nghiệp vụ:**
  1. `name` (Tên bàn): Bắt buộc nhập, độ dài từ 2 đến 100 ký tự (ví dụ: *"Bàn VIP 01"*, *"Bàn Lỗ 02"*).
  2. `type_id` (Loại bàn): Khóa ngoại tham chiếu bảng `TableType(id)`. Phải chọn loại bàn hợp lệ (Bàn Lỗ, Bàn Carom 3 Băng, Bàn Snooker).
  3. `status` (Trạng thái bàn): Chỉ chấp nhận một trong các trạng thái: `Trống`, `Đang sử dụng`, `Bảo trì`, `Đã đặt trước`.
  4. Không được xóa vĩnh viễn dữ liệu nếu bàn đang gắn với hóa đơn chưa thanh toán; thực hiện xóa mềm `is_deleted = 1`.

---

### 📌 Phân hệ 3: Quản lý Dịch vụ & Menu (Service Menu Management - CRUD)
* **URL chức năng:**
  * Quản lý dịch vụ: `ServiceServlet`
  * Thêm dịch vụ mới: `ThemServiceServlet`

* **Ràng buộc nghiệp vụ:**
  1. `name`: Bắt buộc nhập, từ 2 đến 150 ký tự, không trùng lặp trong cùng một danh mục.
  2. `price`: Giá bán phải là số thực lớn hơn 0 (`price > 0`). Giá trị biên: 1,000 VNĐ - 10,000,000 VNĐ. Nếu nhập giá $\le 0$ phải bắt ngoại lệ và báo lỗi.
  3. `category`: Thuộc danh mục hợp lệ (`Nước uống`, `Đồ uống có cồn`, `Đồ ăn`, `Thuốc lá`, `Khác`).

---

### 📌 Phân hệ 4: Đặt Bàn Trước (Booking Flow)
* **URL chức năng:** `/datban`, `/xacnhanbooking`
* **Ràng buộc nghiệp vụ:**
  1. `start_time`: Thời gian đặt phải sau thời gian hiện tại ít nhất 30 phút.
  2. Bàn được chọn phải có trạng thái `Trống` trong khung giờ khách yêu cầu (không bị trùng với bất kỳ `Booking` nào chưa kết thúc).
  3. Trạng thái booking ban đầu: `Chờ duyệt` $\rightarrow$ Nhân viên duyệt chuyển thành `Đã duyệt` $\rightarrow$ Bàn chuyển sang `Đã đặt trước`.
