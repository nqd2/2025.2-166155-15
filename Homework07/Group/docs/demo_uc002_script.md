# Kịch bản Demo & Hướng dẫn sử dụng Use Case 2: ManageSiteInformation (Quản lý thông tin Site)

Tài liệu này cung cấp kịch bản demo (storyboard), dữ liệu thử nghiệm, các bước thao tác bấm nút trên giao diện ứng dụng (JavaFX) và cách kiểm tra kết quả lưu trữ trong cơ sở dữ liệu đối với **Use Case 2: ManageSiteInformation**.

---

## 1. Tổng quan Nghiệp vụ Use Case 2

Mục đích của Use Case 2 là quản lý và cập nhật hồ sơ năng lực của các Kho/Site nhập khẩu ở nước ngoài (**Overseas Import Site**). Thông tin này đóng vai trò quan trọng làm dữ liệu đầu vào cho các thuật toán phân bổ hàng nhập khẩu (UC004) và lập đơn hàng chính thức (UC005).

### Các trường dữ liệu chính (Input Data):
*   `siteCode` (Mã định danh Site): Bắt buộc, ví dụ `SITE-SEA-01`.
*   `siteName` (Tên Site): Bắt buộc, ví dụ `Singapore Sea Site`.
*   `deliveryDaysByShip` (Thời gian vận chuyển bằng tàu - số ngày): Bắt buộc, số nguyên dương.
*   `deliveryDaysByAir` (Thời gian vận chuyển bằng hàng không - số ngày): Bắt buộc, số nguyên dương.
*   `merchandiseCatalog` (Danh sách mặt hàng Site kinh doanh): Bắt buộc, danh sách mã hàng hợp lệ (ví dụ: `MH-001`, `MH-002`, `MH-005`).

---

## 2. Kịch bản Chuẩn bị Dữ liệu & Môi trường

Trước khi thực hiện Demo, cơ sở dữ liệu đã được khởi tạo qua tệp [schema.sql](file:///home/nqd2/code/ITSSVN/2025.2-166155-15/Homework07/Group/src/main/resources/db/schema.sql) với các thông tin mặc định:
*   Mã mặt hàng hợp lệ trong hệ thống: `MH-001`, `MH-002`, `MH-003`, `MH-004`, `MH-005`, `MH-014`.
*   Danh sách Site ban đầu:
    1.  `SITE-SEA-01` ("Singapore Sea Site"): Vận chuyển tàu: 18 ngày, hàng không: 5 ngày. Mặt hàng kinh doanh: `MH-001`, `MH-002`, `MH-005`.
    2.  `SITE-EU-04` ("Europe Import Site"): Vận chuyển tàu: 28 ngày, hàng không: 7 ngày. Mặt hàng kinh doanh: `MH-001`, `MH-003`, `MH-005`, `MH-014`.
    3.  `SITE-AIR-02` ("Air Express Site"): Vận chuyển tàu: 35 ngày, hàng không: 4 ngày. Mặt hàng kinh doanh: `MH-002`, `MH-004`, `MH-014`.

---

## 3. Kịch bản Demo Chi tiết (Storyboards)

### Kịch bản 1: Luồng Nghiệp vụ Chính (Happy Path)
> **Bối cảnh:** Đối tác vận tải đường biển và hàng không của chi nhánh **Singapore Sea Site (`SITE-SEA-01`)** vừa tối ưu hóa lộ trình logistics. Họ thông báo thời gian vận chuyển bằng tàu biển giảm từ **18 ngày xuống còn 14 ngày**, và đường hàng không giảm từ **5 ngày xuống 3 ngày**. Đồng thời, chi nhánh này mở rộng kinh doanh thêm mặt hàng **`MH-003`** (đã được phê duyệt trong danh mục chung).
*   **Người thực hiện:** Nhân viên Phòng Đặt hàng Quốc tế (`InternationalOrderingDepartment`).
*   **Kết quả mong muốn:** Hệ thống ghi nhận thông tin thời gian vận chuyển mới, bổ sung mã hàng `MH-003` vào danh mục kinh doanh của Site, ghi lại nhật ký hệ thống (Audit Log) và hiển thị thông báo thành công.

### Kịch bản 2: Các Luồng Nghiệp vụ Thay thế & Validate Lỗi (Alternative Paths)
> **Bối cảnh:** Người dùng vô tình nhập sai dữ liệu trong quá trình chỉnh sửa.
*   **Luồng 3a (Thời gian vận chuyển không hợp lệ):** Người dùng sửa thời gian vận chuyển bằng tàu biển thành `0` hoặc `-5`. Hệ thống từ chối lưu và đưa ra cảnh báo lỗi cụ thể.
*   **Luồng 4a (Cập nhật danh mục rỗng):** Người dùng bỏ chọn tất cả các mặt hàng kinh doanh của Site (danh mục trống). Hệ thống ngăn chặn việc lưu và đưa ra cảnh báo lỗi (mỗi Site phải giao dịch ít nhất 1 mặt hàng).

---

## 4. Hướng dẫn Bấm nút Từng bước (Step-by-step Guide)

### Bước 1: Khởi động Ứng dụng & Tạo tài khoản Quản trị viên
Nếu bạn đang chạy ứng dụng lần đầu tiên trên một database trống:
1.  Khởi động ứng dụng bằng lệnh:
    ```bash
    cd Homework07/Group
    mvn clean compile exec:java -Dexec.mainClass="vn.edu.hust.itss.group15.app.MainApp"
    ```
2.  Giao diện đầu tiên hiển thị là **"Create first admin"** (Khởi tạo tài khoản Admin hệ thống):
    *   Nhập **Username**: `admin`
    *   Nhập **Email**: `admin@exim.com`
    *   Nhập **Password**: `admin12345` (Hoặc mật khẩu bất kỳ từ 8 ký tự).
    *   Click nút **"Create admin"**. Bạn sẽ tự động đăng nhập vào trang chủ dưới quyền `SystemAdministrator`.

### Bước 2: Tạo Tài khoản Nhân viên Đặt hàng Quốc tế
Để demo đúng luồng nghiệp vụ của tác nhân phân quyền (`InternationalOrderingDepartment`):
1.  Sau khi đăng nhập bằng tài khoản `admin`, tại menu sidebar bên trái, chọn tab **"Admin"**.
2.  Tại form nhập thông tin bên phải màn hình **Administration**:
    *   **Username**: `order_staff`
    *   **Email**: `order_staff@exim.com`
    *   **Password**: `order12345`
    *   Tại mục **Roles**: Tích chọn checkbox `InternationalOrderingDepartment`.
    *   Click nút **"Create user"**.
    *   *Kết quả:* Thông báo "User created" xuất hiện ở góc dưới. Danh sách bảng **Users** cập nhật thêm dòng của `order_staff`.
3.  Click nút **"Logout"** ở góc trên bên phải để thoát tài khoản Admin.

### Bước 3: Đăng nhập dưới quyền Nhân viên Đặt hàng
1.  Tại màn hình đăng nhập:
    *   **Username**: `order_staff`
    *   **Password**: `order12345`
    *   Click **"Sign in"**.
2.  *Kết quả:* Bạn truy cập vào trang chủ. Lúc này menu bên trái chỉ hiển thị các tab mà vai trò này được phép truy cập (`Overview`, `Sites`, `Inventory`, `Allocation`, `Orders`), tab `Admin` và `Requests` đã bị ẩn/khóa.

### Bước 4: Thực hiện chỉnh sửa thông tin Site (Happy Path)
1.  Nhấp chọn tab **"Sites"** ở menu bên trái.
2.  Giao diện hiển thị bảng danh sách các Site ở dưới và Form chỉnh sửa ở trên.
3.  Click chuột chọn dòng có mã Site **`SITE-SEA-01`** (Singapore Sea Site) trong bảng dữ liệu.
4.  *Kết quả:* Các thông tin cũ của Site tự động hiển thị đầy đủ lên các trường nhập liệu trong form phía trên.
5.  Thực hiện thay đổi các thông tin sau trên Form:
    *   Sửa ô **Ship days** từ `18` thành `14`.
    *   Sửa ô **Air days** từ `5` thành `3`.
    *   Tại danh sách checkbox **Catalog**: Tích chọn thêm ô **`MH-003`** (Các ô `MH-001`, `MH-002`, `MH-005` giữ nguyên tích chọn).
6.  Click nút **"Update Site"**.
7.  *Kết quả:*
    *   Hộp thoại nhỏ (Toast) hiển thị thông báo **"Site saved"**.
    *   Bảng dữ liệu bên dưới lập tức cập nhật giá trị mới cho dòng `SITE-SEA-01`: Cột *Ship days* là `14`, *Air days* là `3`, cột *Catalog* bổ sung thêm `MH-003`.

### Bước 5: Validate dữ liệu đầu vào (Luồng Lỗi)
1.  **Thử nghiệm lỗi thời gian vận chuyển không hợp lệ (Luồng 3a):**
    *   Vẫn chọn dòng `SITE-SEA-01`.
    *   Sửa ô **Ship days** thành `0` hoặc `-5`.
    *   Click nút **"Update Site"**.
    *   *Kết quả:* Hộp thoại Toast báo lỗi đỏ: **"delivery days must be positive"**. Giá trị trong bảng và database không thay đổi.
    *   *Khôi phục:* Nhập lại `14` vào ô Ship days.
2.  **Thử nghiệm lỗi danh mục rỗng (Luồng 4a):**
    *   Vẫn chọn dòng `SITE-SEA-01`.
    *   Bỏ tích chọn toàn bộ các ô hàng hóa trong phần **Catalog** (`MH-001`, `MH-002`, `MH-003`, `MH-005` đều unchecked).
    *   Click nút **"Update Site"**.
    *   *Kết quả:* Hộp thoại Toast báo lỗi đỏ: **"merchandiseCatalog is required"**. Hệ thống ngăn chặn việc lưu danh mục trống.

### Bước 6: Kiểm tra Nhật ký Vận hành (Audit Trail)
1.  Thực hiện logout tài khoản `order_staff` bằng nút **"Logout"**.
2.  Đăng nhập lại bằng tài khoản admin:
    *   **Username**: `admin`
    *   **Password**: `admin12345`
3.  Click chọn tab **"Admin"** ở menu bên trái.
4.  Chuyển sang tab con **"Logs"** bên cạnh tab "Users".
5.  *Kết quả:* Bạn sẽ thấy dòng ghi nhật ký mới nhất ghi nhận sự kiện cập nhật vừa rồi:
    *   **Operator**: `system`
    *   **Action**: `UPDATE_SITE`
    *   **Details**: `SITE-SEA-01`
    *   **Time**: Thời gian thực tế vừa bấm nút.

---

## 5. Các File Code Liên Quan trong Hệ thống

Để kiểm chứng hoặc sửa đổi logic, bạn có thể tham khảo các tệp tin sau:
1.  **Giao diện hiển thị:** [SiteView.java](file:///home/nqd2/code/ITSSVN/2025.2-166155-15/Homework07/Group/src/main/java/vn/edu/hust/itss/group15/app/ui/SiteView.java) - Chứa định nghĩa Form nhập liệu, bảng dữ liệu, Toast thông báo và sự kiện kích hoạt khi bấm nút "Update Site".
2.  **Tầng Application Service:** [SiteService.java](file:///home/nqd2/code/ITSSVN/2025.2-166155-15/Homework07/Group/src/main/java/vn/edu/hust/itss/group15/application/SiteService.java) - Chứa logic nghiệp vụ xử lý nghiệp vụ cập nhật thông tin Site, validate dữ liệu đầu vào và lưu Audit Log.
3.  **Tầng Domain Model:** [ImportSite.java](file:///home/nqd2/code/ITSSVN/2025.2-166155-15/Homework07/Group/src/main/java/vn/edu/hust/itss/group15/domain/ImportSite.java) - Ràng buộc thực thể nghiệp vụ của Site, validate giá trị tại tầng Core Domain.
4.  **Tầng Persistence (Lưu trữ):** 
    *   [SupabaseStore.java](file:///home/nqd2/code/ITSSVN/2025.2-166155-15/Homework07/Group/src/main/java/vn/edu/hust/itss/group15/infrastructure/persistence/supabase/SupabaseStore.java) - Thực hiện đồng bộ dữ liệu thông qua các bảng `import_sites` và `site_catalog` trên Supabase Database.
    *   [InMemoryStore.java](file:///home/nqd2/code/ITSSVN/2025.2-166155-15/Homework07/Group/src/main/java/vn/edu/hust/itss/group15/infrastructure/persistence/memory/InMemoryStore.java) - Khởi tạo dữ liệu giả lập phục vụ cho Unit Test.
5.  **Tệp Kiểm thử Tự động:** [Uc002SiteProfileTest.java](file:///home/nqd2/code/ITSSVN/2025.2-166155-15/Homework07/Group/src/test/java/vn/edu/hust/itss/group15/usecases/Uc002SiteProfileTest.java) - Chứa các trường hợp kiểm thử tự động (Unit Test) cho luồng nghiệp vụ UC002.
