# BỘ TEST CASE CHI TIẾT DỰ ÁN H.N.D.D BIDA (TEST CASES)
> **Kỹ thuật thiết kế:** Phân vùng tương đương (Equivalence Partitioning) & Phân tích giá trị biên (Boundary Value Analysis)  
> **Sinh viên:** Nguyễn Thành Đạt - MSSV: TY00327 - Lớp: SD21308  

---

## BẢNG 1: BỘ TEST CASE XÁC THỰC & ĐĂNG NHẬP (AUTHENTICATION CONTROLLER)

| Test Case ID | Tên ca kiểm thử | Dữ liệu đầu vào (Input) | Kỹ thuật kiểm thử | Kết quả mong đợi (Expected Output) | Loại test |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **TC_AUTH_01** | Đăng nhập thành công với tài khoản Admin | `username="admin"`, `password="admin123"` | Giá trị hợp lệ | Đăng nhập thành công, chuyển hướng tới `/admin/quanli.jsp` | Smoke |
| **TC_AUTH_02** | Đăng nhập thành công với tài khoản Khách hàng | `username="dat123456"`, `password="admin123"` | Giá trị hợp lệ | Đăng nhập thành công, chuyển hướng tới `/user/trangchunguoidung` | Smoke |
| **TC_AUTH_03** | Đăng nhập thất bại do để trống Username | `username=""`, `password="admin123"` | Phân tích biên (Độ dài = 0) | Báo lỗi: *"Vui lòng nhập đầy đủ tài khoản và mật khẩu"* | Regression |
| **TC_AUTH_04** | Đăng nhập thất bại do để trống Password | `username="admin"`, `password=""` | Phân tích biên (Độ dài = 0) | Báo lỗi: *"Vui lòng nhập đầy đủ tài khoản và mật khẩu"* | Regression |
| **TC_AUTH_05** | Đăng nhập thất bại khi tài khoản không tồn tại | `username="khong_ton_tai_999"`, `password="123456"` | Phân vùng không hợp lệ | Báo lỗi: *"Tài khoản không tồn tại"* | Regression |
| **TC_AUTH_06** | Đăng nhập thất bại khi sai Mật khẩu | `username="admin"`, `password="sai_mat_khau"` | Phân vùng không hợp lệ | Báo lỗi: *"Mật khẩu không đúng"* | Regression |
| **TC_AUTH_07** | Đăng ký thành công tài khoản mới | `user="thanhdat_vip"`, `pass="Dat@12345"`, `email="thanhdat@gmail.com"`, `phone="0912345678"` | Giá trị hợp lệ | Đăng ký thành công, lưu vào database với `role="Khách hàng"` | Smoke |
| **TC_AUTH_08** | Đăng ký thất bại khi Username dưới 5 ký tự | `user="abc"`, `pass="Dat@12345"`, `email="test@gmail.com"` | Phân tích biên (Độ dài = 3 < 5) | Báo lỗi: *"Tên tài khoản phải từ 5 đến 20 ký tự"* | Regression |
| **TC_AUTH_09** | Đăng ký thất bại khi Username trên 20 ký tự | `user="nguyenthanhdat_sieu_dai_qua_muc"`, `pass="Dat@123"` | Phân tích biên (Độ dài = 31 > 20) | Báo lỗi: *"Tên tài khoản không được vượt quá 20 ký tự"* | Regression |
| **TC_AUTH_10** | Đăng ký thất bại khi Email sai định dạng | `user="datuser"`, `email="datsaidinhdang@.com"` | Phân vùng không hợp lệ | Báo lỗi: *"Email không hợp lệ"* | Regression |
| **TC_AUTH_11** | Đăng ký thất bại khi trùng Username trong DB | `user="admin"` | Bắt lỗi trùng lặp (Exception) | Ném ngoại lệ hoặc báo lỗi: *"Tài khoản đã tồn tại trong hệ thống"* | Regression |

---

## BẢNG 2: BỘ TEST CASE QUẢN LÝ BÀN BIDA (TABLE BIDA CONTROLLER & CRUD)

| Test Case ID | Tên ca kiểm thử | Dữ liệu đầu vào (Input) | Kỹ thuật kiểm thử | Kết quả mong đợi (Expected Output) |
| :--- | :--- | :--- | :--- | :--- |
| **TC_TABLE_01** | Thêm mới bàn bida thành công | `name="Bàn VIP Lỗ 08"`, `type_id=1`, `status="Trống"` | Giá trị hợp lệ | Thêm mới thành công, hiển thị trong danh sách bàn |
| **TC_TABLE_02** | Thêm bàn thất bại khi tên bàn để trống | `name=""`, `type_id=1` | Phân tích biên (Độ dài = 0) | Ném ngoại lệ `IllegalArgumentException` hoặc báo lỗi |
| **TC_TABLE_03** | Thêm bàn thất bại khi giá loại bàn âm | `price = -50000` | Phân tích biên ($Price < 0$) | Hệ thống từ chối lưu, ném ngoại lệ giá không hợp lệ |
| **TC_TABLE_04** | Cập nhật trạng thái bàn sang "Đang sử dụng" | `tableId=1`, `newStatus="Đang sử dụng"` | Giá trị hợp lệ | Trạng thái bàn được cập nhật thành công |
| **TC_TABLE_05** | Xóa mềm bàn bida (Soft Delete) | `tableId=1` | Nghiệp vụ an toàn dữ liệu | Bàn được đánh dấu `is_deleted = 1`, không xuất hiện trên giao diện khách |
| **TC_TABLE_06** | Kiểm tra Timeout khi truy vấn danh sách bàn | Query danh sách 50 bàn | Timeout Testing ($\le 2$ giây) | Truy vấn hoàn thành trong thời gian cho phép không bị treo |

---

## BẢNG 3: BỘ TEST CASE AUTOMATION UI SELENIUM (PAGE OBJECT MODEL)

| Test Case ID | Page Object | Thao tác kịch bản (Steps) | Kiểm tra kết quả (Assertions) | Group |
| :--- | :--- | :--- | :--- | :--- |
| **TC_UI_01** | `LoginPage` | Mở `/dangnhap` $\rightarrow$ Nhập `admin` / `admin123` $\rightarrow$ Bấm nút "ĐĂNG NHẬP" | URL chuyển hướng chứa `/admin/quanli` hoặc navbar có tên Quản trị | `smoke` |
| **TC_UI_02** | `LoginPage` | Mở `/dangnhap` $\rightarrow$ Nhập `admin` / `saiPass` $\rightarrow$ Bấm "ĐĂNG NHẬP" | Xuất hiện thông báo màu đỏ: *"Mật khẩu không đúng"* | `regression` |
| **TC_UI_03** | `LoginPage` | Mở `/dangnhap` $\rightarrow$ Để trống cả 2 ô $\rightarrow$ Bấm "ĐĂNG NHẬP" | Xuất hiện cảnh báo yêu cầu nhập đầy đủ thông tin | `regression` |
| **TC_UI_04** | `RegisterPage` | Mở `/dangky` $\rightarrow$ Nhập form đăng ký $\rightarrow$ Bấm "ĐĂNG KÝ" | Chuyển hướng sang trang xác nhận OTP hoặc báo thành công | `smoke` |
| **TC_UI_05** | `TableBidaPage` | Mở `/admin/quanliTableBida.jsp` $\rightarrow$ Bấm Thêm bàn mới $\rightarrow$ Điền tên bàn $\rightarrow$ Lưu | Danh sách bàn tăng thêm 1 hàng chứa tên bàn vừa tạo | `smoke` |
