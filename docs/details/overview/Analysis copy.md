# Hệ Thống Quản Lý Xuất Nhập Khẩu + Hệ Thống Quản Lý Kho

Tài liệu mô tả các chức năng và quy trình nghiệp vụ cho các bộ phận trong hệ thống quản lý kho.

---

## 1. Bộ Phận Bán Hàng (Sales Department)

*   **Quản lý yêu cầu nhập hàng**:
    *   Xem danh sách yêu cầu.
    *   Tìm kiếm và tra cứu yêu cầu.
    *   Thực hiện các thao tác CRUD (Thêm, Xem, Sửa, Xóa).
    *   Hủy yêu cầu nhập hàng.
*   **Quản lý mặt hàng**:
    *   Tạo mới, cập nhật và xem thông tin chi tiết mặt hàng.
    *   Xem danh sách các mặt hàng đang kinh doanh.
    *   Xóa mềm (Soft delete) các mặt hàng không còn kinh doanh.

## 2. Bộ Phận Đặt Hàng Quốc Tế (International Ordering)

*   **Quản lý yêu cầu nhập hàng**:
    *   Xem danh sách và chi tiết các yêu cầu nhập hàng từ bộ phận bán hàng.
    *   Xử lý yêu cầu nhập hàng:
        *   Tạo đơn hàng mới.
        *   Xác nhận đơn hàng.
        *   Phân chia sản phẩm theo yêu cầu.
        *   Hủy đơn hàng nếu cần thiết.
*   **Quản lý đơn hàng**:
    *   Xem danh sách và chi tiết các đơn hàng quốc tế.
    *   Cập nhật/Sửa đổi thông tin đơn hàng.
    *   Hủy đơn hàng (Cập nhật trạng thái thành `Cancelled`).
    *   Xử lý các đơn hàng đã bị hủy.
*   **Quản lý Site (CRU)**:
    *   **Create**: Tạo mới thông tin site.
    *   **Read**: Xem danh sách và chi tiết các site.
    *   **Update**: Cập nhật trạng thái và thông tin site.

## 3. Bộ Phận Quản Lý Kho (Warehouse Management)

*   **Theo dõi đơn hàng**:
    *   Xem danh sách các đơn hàng liên quan đến kho.
    *   Xem chi tiết đơn hàng (Chế độ xem đặc thù cho quản lý kho, khác với bộ phận đặt hàng quốc tế).

## 4. Site Layout & Operations

*   **Quản lý thông tin Site**:
    *   Cập nhật thông tin cơ bản của site.
    *   Quản lý danh mục mặt hàng kinh doanh tại site.
    *   Cập nhật số lượng hàng tồn kho thực tế.
*   **Xử lý đơn hàng tại Site**:
    *   Xem danh sách và chi tiết các đơn hàng của site.
    *   Xác nhận đơn hàng đã nhận hoặc đã xử lý.

## 5. Quản Trị Hệ Thống (Administration)

*   **Quản lý người dùng & tài khoản**:
    *   Xem danh sách và tra cứu người dùng theo phòng ban, vai trò, trạng thái.
    *   Tạo mới tài khoản cho nhân sự nội bộ.
    *   Cập nhật thông tin hồ sơ người dùng (họ tên, email, số điện thoại, đơn vị).
    *   Khóa/Mở khóa tài khoản khi phát hiện rủi ro hoặc người dùng nghỉ việc.
    *   Đặt lại mật khẩu và buộc đổi mật khẩu ở lần đăng nhập tiếp theo.
*   **Quản lý vai trò & phân quyền (RBAC)**:
    *   Tạo/Sửa/Xóa vai trò hệ thống.
    *   Gán/Thu hồi vai trò cho từng người dùng hoặc nhóm người dùng.
    *   Cấu hình quyền theo module/chức năng (Yêu cầu nhập hàng, Đơn hàng, Site, Báo cáo...).
    *   Xem ma trận quyền và kiểm tra xung đột quyền.
*   **Quản lý danh mục và tham số hệ thống**:
    *   Cấu hình tham số nghiệp vụ dùng chung (đơn vị tiền tệ, chính sách làm tròn, ngưỡng cảnh báo).
    *   Quản lý danh mục chuẩn (nhà cung cấp, phương thức vận chuyển, loại mặt hàng, trạng thái đơn hàng).
    *   Cấu hình luồng phê duyệt và các quy tắc ràng buộc dữ liệu.
*   **Giám sát, nhật ký và kiểm soát tuân thủ**:
    *   Theo dõi nhật ký đăng nhập/đăng xuất và lịch sử thao tác của người dùng.
    *   Tra cứu nhật ký thay đổi dữ liệu quan trọng (ai sửa, sửa gì, khi nào).
    *   Xuất báo cáo audit phục vụ kiểm tra nội bộ và đối soát.
*   **Quản trị tích hợp & vận hành**:
    *   Quản lý kết nối tích hợp với hệ thống ngoài (ERP, email, thông báo).
    *   Kiểm tra trạng thái đồng bộ dữ liệu và xử lý tác vụ lỗi.
    *   Cấu hình lịch chạy tác vụ nền (đồng bộ, gửi báo cáo định kỳ).
*   **Sao lưu, khôi phục và an toàn dữ liệu**:
    *   Thiết lập chính sách sao lưu định kỳ.
    *   Thực hiện khôi phục dữ liệu trong tình huống sự cố.
    *   Quản lý chính sách lưu trữ và vòng đời dữ liệu (retention/purge).
*   **Báo cáo quản trị**:
    *   Xem dashboard tổng quan vận hành hệ thống.
    *   Thống kê người dùng theo vai trò, mức độ sử dụng chức năng, tần suất đăng nhập.
    *   Theo dõi cảnh báo bất thường (đăng nhập thất bại liên tiếp, truy cập ngoài phạm vi quyền).

---

## Ghi Chú Kỹ Thuật

*   **Định nghĩa hành động**: Các thao tác **Xem / Tìm kiếm / Tra cứu / Xử lý** là sự kết hợp của các hành động CRUD cơ bản (Create, Read, Update, Delete) cùng với các logic nghiệp vụ đặc thù.
*   **Kiến trúc Use Case**: Khuyến nghị sử dụng **Abstract Actor** cho các Use Case có nhiều loại Actor cùng thực hiện một chức năng chung để tối ưu hóa thiết kế hệ thống.