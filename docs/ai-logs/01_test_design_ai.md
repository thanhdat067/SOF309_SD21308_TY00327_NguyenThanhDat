# NHẬT KÝ ỨNG DỤNG AI #01: THIẾT KẾ TEST PLAN & TEST CASES BẰNG KỸ THUẬT RAG
> **Giai đoạn:** Tuần 1 (Y2)  
> **Mục tiêu:** Sử dụng kỹ thuật AI RAG kết hợp tài liệu đặc tả nghiệp vụ `SPECIFICATIONS.md` để sinh Master Test Plan và Bộ Test Case chi tiết theo kỹ thuật Phân tích giá trị biên (BVA) và Phân vùng tương đương (EP).  
> **Sinh viên:** Nguyễn Thành Đạt - MSSV: TY00327 - Lớp: SD21308  

---

## 1. BỐI CẢNH & PHẠM VI NGHIỆP VỤ (CONTEXT PROMPT)
* **Mô tả bài toán:** Hệ thống Quản lý và Đặt bàn Bida H.N.D.D cần xây dựng kế hoạch kiểm thử tự động toàn diện cho các phân hệ trọng yếu:
  1. Xác thực (Đăng ký, Đăng nhập, Phân quyền Role).
  2. Quản lý Bàn Bida `TableBida` (CRUD, đổi trạng thái, xóa mềm).
  3. Quản lý Menu & Dịch vụ (Kiểm tra giá bán, danh mục).
  4. Đặt bàn trước (Xung đột thời gian bàn).

---

## 2. PROMPT GỬI CHO AI (INPUT PROMPT)
```text
Bạn là một Chuyên gia Kiểm thử phần mềm (QA Lead) giàu kinh nghiệm.
Dưới đây là tài liệu đặc tả nghiệp vụ chi tiết của dự án Web Quản Lý Bida H.N.D.D (viết bằng Java Servlet, JSP, SQL Server):

[Nạp toàn bộ nội dung từ file docs/SPECIFICATIONS.md]

Yêu cầu thực hiện:
1. Áp dụng kỹ thuật Phân vùng tương đương (Equivalence Partitioning) và Phân tích giá trị biên (Boundary Value Analysis) để thiết kế bộ Test Case cho chức năng Đăng ký, Đăng nhập và CRUD Bàn Bida.
2. Với mỗi Test Case, hãy chỉ định rõ: Test Case ID, Mục đích kiểm thử, Dữ liệu đầu vào (Input), Kỹ thuật kiểm thử áp dụng, Kết quả mong đợi (Expected Result) và Phân nhóm (Smoke / Regression).
3. Lập Kế hoạch kiểm thử tổng thể (Master Test Plan) theo chuẩn IEEE 829.
```

---

## 3. PHẢN HỒI CỦA AI & KẾT QUẢ ĐẦU RA (AI OUTPUT)
* AI đã phân tích và phân loại các trường dữ liệu thành các phân vùng hợp lệ và không hợp lệ:
  * **Độ dài Username:** $Length < 5$ (Không hợp lệ), $5 \le Length \le 20$ (Hợp lệ), $Length > 20$ (Không hợp lệ). Giá trị biên: 4, 5, 20, 21.
  * **Độ dài Mật khẩu:** $Length < 6$ (Không hợp lệ), $Length \ge 6$ (Hợp lệ). Giá trị biên: 5, 6.
  * **Giá bàn bida:** $Price \le 0$ (Không hợp lệ), $Price > 0$ (Hợp lệ).
* Kết quả sinh ra được lưu trữ chính thức vào 2 file:
  * [TEST_PLAN.md](../TEST_PLAN.md)
  * [TEST_CASES.md](../TEST_CASES.md)

---

## 4. ĐÁNH GIÁ CỦA SINH VIÊN & CHỈNH SỬA THỦ CÔNG (HUMAN IN THE LOOP)
* **Điểm AI làm tốt:** AI phân tích giá trị biên rất nhanh và chuẩn xác, phân chia nhóm Smoke và Regression hợp lý.
* **Điểm AI chưa chuẩn xác cần can thiệp:** 
  1. AI ban đầu đề xuất mật khẩu tối thiểu 8 ký tự kèm ký tự đặc biệt, nhưng thực tế hệ thống HNDD Bida quy định tối thiểu 6 ký tự. Mình đã hiệu chỉnh lại bộ test case để khớp với logic Controller thật.
  2. Bổ sung thêm trường hợp tài khoản đã tồn tại trong database để phục vụ Exception Testing `assertThrows()`.
