# Hệ Thống Quản Lý Xuất Nhập Khẩu + Hệ Thống Quản Lý Kho

Tài liệu mô tả các chức năng và quy trình nghiệp vụ cho các bộ phận trong hệ thống quản lý kho.

---

## 1. Bộ Phận Bán Hàng (Sales Department)

*   **Lập danh sách yêu cầu nhập hàng**:
    *   Tạo yêu cầu nhập với các trường bắt buộc: mã hàng, số lượng, đơn vị, ngày nhận mong muốn.
    *   Hỗ trợ tạo nhiều danh sách yêu cầu tại cùng một thời điểm.
    *   Kiểm tra hợp lệ dữ liệu đầu vào (đủ trường, số lượng > 0, ngày nhận hợp lệ).
*   **Gửi và theo dõi yêu cầu nhập hàng**:
    *   Gửi danh sách yêu cầu sang Bộ phận đặt hàng quốc tế để xử lý.
    *   Tra cứu yêu cầu theo mặt hàng, thời gian, trạng thái.
    *   Theo dõi kết quả xử lý: đã phân bổ đủ, phân bổ một phần, hoặc không thể đáp ứng.
*   **Điều chỉnh yêu cầu trước khi xử lý**:
    *   Cập nhật thông tin yêu cầu khi chưa bị khóa xử lý.
    *   Hủy yêu cầu khi nhu cầu thay đổi.

## 2. Bộ Phận Đặt Hàng Quốc Tế (International Ordering)

*   **Tiếp nhận và phân tích yêu cầu nhập**:
    *   Nhận danh sách mặt hàng cần đặt từ Bộ phận bán hàng.
    *   Xử lý từng mặt hàng độc lập theo bài toán nghiệp vụ.
*   **Tìm Site phù hợp và yêu cầu tồn kho**:
    *   Tìm các Site có kinh doanh ít nhất một mặt hàng trong danh sách yêu cầu.
    *   Lọc danh sách mặt hàng theo từng Site rồi gửi yêu cầu kiểm tra tồn kho.
    *   Nhận phản hồi tồn kho từ Site (hết hàng thì số lượng bằng 0).
*   **Quản lý dữ liệu Site và dữ liệu kho**:
    *   Quản lý tệp thông tin kho theo cấu trúc: Site code, Merchandise code, In-stock quantity, Unit.
    *   Quản lý tệp thông tin Site gồm tên Site, thời gian giao bằng tàu, thời gian giao bằng hàng không, và thông tin khác.
    *   Cập nhật dữ liệu khi Site thông báo thay đổi.
*   **Ra quyết định phân bổ đặt hàng**:
    *   Chỉ chọn Site đáp ứng ngày nhận mong muốn.
    *   Cho phép nhập từ nhiều Site nếu một Site không đủ số lượng.
    *   Cảnh báo lỗi khi tổng nguồn cung không đủ nhu cầu.
    *   Áp dụng tiêu chí ưu tiên theo thứ tự: tàu trước hàng không, tồn kho lớn hơn, số Site chọn ít nhất.
*   **Tạo và gửi đơn đặt hàng**:
    *   Sinh dữ liệu đặt hàng theo định dạng: Site code, Merchandise code, Quantity ordered, Unit, Delivery means.
    *   Gửi đơn đặt hàng tới các Site đã chọn.

## 3. Bộ Phận Quản Lý Kho (Warehouse Management)

*   **Tiếp nhận hàng về kho**:
    *   Nhận thông tin các đơn đã đặt từ Bộ phận đặt hàng quốc tế.
    *   Ghi nhận từng đợt hàng thực tế về kho.
*   **Kiểm hàng và đối soát**:
    *   So sánh số lượng thực nhận với số lượng đặt theo từng mặt hàng.
    *   Ghi nhận sai lệch (thiếu/thừa/hư hỏng) để xử lý tiếp theo.
*   **Cập nhật hệ thống kho nội bộ**:
    *   Lưu kết quả kiểm hàng vào hệ thống quản lý kho riêng.
    *   Cập nhật tồn kho thực tế sau kiểm nhận.

## 4. Site Layout & Operations

*   **Quản lý thông tin Site**:
    *   Cập nhật thông tin Site code, tên Site, và thông tin vận hành liên quan.
    *   Cập nhật thời gian vận chuyển theo 2 phương thức: tàu và hàng không.
    *   Cập nhật danh sách mặt hàng mà Site đang kinh doanh.
*   **Phản hồi tồn kho theo yêu cầu**:
    *   Nhận danh sách mặt hàng cần kiểm tra từ Bộ phận đặt hàng quốc tế.
    *   Trả về số lượng tồn kho theo từng mặt hàng; mặt hàng không có thì trả 0.
*   **Tiếp nhận và thực hiện đơn đặt hàng**:
    *   Nhận đơn đặt hàng chính thức từ Bộ phận đặt hàng quốc tế.
    *   Xử lý giao hàng theo phương thức vận chuyển được chỉ định.

## 5. Quản Trị Hệ Thống (Administration)

*   **Quản trị người dùng và phân quyền actor**:
    *   Quản lý tài khoản cho các actor nghiệp vụ: Sales, International Ordering, Site, Warehouse.
    *   Cấp quyền truy cập theo đúng phạm vi chức năng của từng bộ phận.
*   **Quản trị dữ liệu dùng chung**:
    *   Quản lý danh mục chuẩn: mã hàng, đơn vị tính, danh sách Site.
    *   Thiết lập quy tắc kiểm tra dữ liệu đầu vào cho các biểu mẫu nghiệp vụ.
*   **Giám sát và vận hành hệ thống**:
    *   Theo dõi nhật ký thao tác liên phòng ban để phục vụ truy vết.
    *   Quản lý sao lưu/khôi phục dữ liệu tệp thông tin kho, tệp thông tin Site, và dữ liệu đơn đặt hàng.

---

## Ghi Chú Kỹ Thuật

*   **Định nghĩa hành động**: Các thao tác **Xem / Tìm kiếm / Tra cứu / Xử lý** là sự kết hợp của các hành động CRUD cơ bản (Create, Read, Update, Delete) cùng với các logic nghiệp vụ đặc thù.
*   **Kiến trúc Use Case**: Khuyến nghị sử dụng **Abstract Actor** cho các Use Case có nhiều loại Actor cùng thực hiện một chức năng chung để tối ưu hóa thiết kế hệ thống.