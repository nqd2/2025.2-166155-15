# KỊCH BẢN THUYẾT TRÌNH BẢO VỆ BÀI TẬP LỚN ITSS
## Đề tài: Hệ thống Đặt hàng Nhập khẩu (Import Ordering Management System)
**Nhóm thực hiện:** Nhóm 15
**Thời lượng dự kiến:** 20 phút (Thuyết trình) + 10 phút (Hỏi đáp & Demo)

---

### PHÂN PHỐI THỜI GIAN & VAI TRÒ
*   **Nguyễn Quý Đức (Trưởng nhóm):** Dẫn dắt, đặt vấn đề, UC002, điều phối Demo, kết luận. (Tổng: ~5.5 phút)
*   **Vũ Đức Hoàng Anh:** Tổng quan Use Case, UC001, UC007 (Phân quyền & Quản trị). (Tổng: ~3.5 phút)
*   **Trần Đăng Sinh:** Class Diagram (Phân tích/Thiết kế), Kiến trúc Gói, UC003. (Tổng: ~3.5 phút)
*   **Phan Khánh Vũ:** UC004 (Thuật toán Phân bổ). (Tổng: ~2.5 phút)
*   **Lương Quốc Khánh:** UC005 (Lập & Phát hành đơn đặt hàng). (Tổng: ~2.5 phút)
*   **Nguyễn Hữu Nhân:** UC006 (Nhận hàng & Đối soát), Nguyên lý & Mẫu thiết kế, Testing. (Tổng: ~2.5 phút)

---

## PHẦN 1: ĐẶT VẤN ĐỀ & TỔNG QUAN HỆ THỐNG (Slide 1 - 9)
**Người trình bày:** **Nguyễn Quý Đức** (Trưởng nhóm)
**Thời lượng:** 3.5 phút

### Slide 1: Cover Slide (HUST)
*   **Lời thoại:** "Kính chào thầy cô và các bạn. Chúng em là Nhóm 15, hôm nay chúng em xin phép được trình bày báo cáo bảo vệ bài tập lớn môn Phân tích và Thiết kế Hệ thống Thông tin (ITSS) về đề tài: **Hệ thống Đặt hàng Nhập khẩu (Import Ordering Management System)**."

### Slide 2: Tên đề tài & Nhóm thực hiện
*   **Lời thoại:** "Bài tập lớn lần này là kết quả hợp tác của cả 6 thành viên trong nhóm 15. Sau quá trình khảo sát và phân tích nghiệp vụ, nhóm đã xây dựng một giải pháp phần mềm hoàn chỉnh cả về mặt tài liệu phân tích thiết kế lẫn mã nguồn sản phẩm thực tế chạy trên nền Java 22 và JavaFX."

### Slide 3: Danh sách thành viên Nhóm 15 & Cách phân công
*   **Lời thoại:** "Đây là danh sách 6 thành viên nhóm em cùng với các Use Case phụ trách chính. Để đảm bảo tính đồng bộ, nhóm đã thiết kế các chức năng liên kết chặt chẽ thành một luồng nghiệp vụ xuyên suốt: Bắt đầu từ khi phòng Sales tạo yêu cầu nhập hàng, hệ thống tự động kiểm tra tồn kho tại các site nước ngoài, lập phương án phân bổ tối ưu, phát hành đơn đặt hàng chính thức và cuối cùng là đối soát số lượng khi hàng cập kho nội bộ."

### Slide 4: Nội dung trình bày
*   **Lời thoại:** "Bài thuyết trình của nhóm gồm 5 phần chính: Thứ nhất là Đặt vấn đề và Tổng quan hệ thống; Thứ hai là Chi tiết Class Diagram mức Phân tích và Thiết kế; Thứ ba là Sơ đồ phụ thuộc Gói và Subsystem; Thứ tư là Các kỹ thuật và nguyên lý thiết kế được áp dụng; và cuối cùng là phần Demo thực tế cùng Kết luận."

### Slide 5: 1.1 Khảo sát bài toán: Bối cảnh nghiệp vụ
*   **Lời thoại:** "Về bối cảnh nghiệp vụ, một doanh nghiệp nhập khẩu quy mô vừa và lớn cần quản lý hoạt động mua hàng từ nhiều Overseas Import Site (các kho/site nước ngoài). Quy trình này liên quan đến nhiều bộ phận trong và ngoài doanh nghiệp bao gồm: Sales, mua hàng quốc tế, các kho nước ngoài, thủ kho nội địa và admin hệ thống. Dữ liệu đòi hỏi phải được kế thừa và đi xuyên suốt từ yêu cầu ban đầu cho tới lúc hàng về kho."

### Slide 6: 1.2 Khó khăn khi xử lý thủ công
*   **Lời thoại:** "Qua khảo sát, nhóm nhận thấy nếu xử lý thủ công sẽ gặp 4 khó khăn lớn: Một là dữ liệu rời rạc, khó biết trạng thái xử lý của từng yêu cầu. Hai là rất dễ xảy ra sai lệch số lượng giữa yêu cầu đặt hàng của Sales, đơn đặt thực tế với đối tác và số lượng thực nhận tại kho. Ba là việc lựa chọn Site cung ứng rất cảm tính, khó tối ưu hóa theo thời gian vận chuyển hay lượng tồn kho của từng kho. Và cuối cùng là thiếu cơ chế Audit Log để truy vết khi xảy ra sự cố."

### Slide 7: 1.3 Ý tưởng giải quyết
*   **Lời thoại:** "Để giải quyết triệt để vấn đề này, nhóm hướng tới mục tiêu: Tin học hóa toàn bộ quy trình đặt hàng; theo dõi chặt chẽ trạng thái từ đầu đến cuối; tự động hóa việc tính toán phân bổ đơn hàng tối ưu và ghi nhật ký thao tác chi tiết để dễ dàng kiểm toán nghiệp vụ."

### Slide 8: 1.4 Mục tiêu hệ thống
*   **Lời thoại:** "Về mặt nghiệp vụ, hệ thống giải quyết trọn vẹn luồng công việc từ tạo yêu cầu, hỏi tồn kho, phân bổ nguồn hàng, đặt hàng và đối soát hàng lỗi/thiếu. Về mặt kỹ thuật, hệ thống phải đảm bảo phân quyền người dùng nghiêm ngặt, lưu trữ dữ liệu tập trung qua Supabase, áp dụng kiến trúc phân lớp Clean Architecture và có cơ chế Mock lưu trữ (InMemoryStore) độc lập để phục vụ kiểm thử tự động một cách dễ dàng."

### Slide 9: 1.5 Actors trong hệ thống
*   **Lời thoại:** "Hệ thống bao gồm các tác nhân nội bộ (Internal Actors) là Sales Department, International Ordering Department, Warehouse Management Department và System Administrator. Tác nhân bên ngoài (External Actor) là các Overseas Import Site. Trong đó, phòng Mua hàng quốc tế (International Ordering) đóng vai trò trung tâm, kết nối các tác nhân thông qua các thực thể dữ liệu chính."

---

## PHẦN 2: THIẾT KẾ KIẾN TRÚC & CHI TIẾT USE CASE (Slide 10 - 23)
**Người trình bày:** **Vũ Đức Hoàng Anh** (Slide 10-12, 17, 23) & **Trần Đăng Sinh** (Slide 13-16, 19) & **Nguyễn Quý Đức** (Slide 18) & **Phan Khánh Vũ** (Slide 20) & **Lương Quốc Khánh** (Slide 21) & **Nguyễn Hữu Nhân** (Slide 22)
**Thời lượng:** 6.5 phút

### Slide 10: 1.6 Use case overview (Sơ đồ tổng quan)
*   **Người trình bày:** **Vũ Đức Hoàng Anh** (0.5 phút)
*   **Lời thoại:** "Xin kính chào thầy cô. Tiếp theo, em xin phép trình bày về sơ đồ Use Case tổng quan. Sơ đồ này thể hiện rõ ranh giới hệ thống cùng các mối quan hệ `<<include>>` giữa các chức năng cốt lõi. Chẳng hạn, chức năng `PlanImportAllocation` sẽ bao gồm việc thu thập tồn kho (`CollectInventoryData`), và kết quả của nó là tiền đề bắt buộc để chạy chức năng `PlaceOverseasOrders`."

### Slide 11: 1.5 Danh sách use case chính
*   **Người trình bày:** **Vũ Đức Hoàng Anh**
*   **Lời thoại:** "Hệ thống được module hóa thành 7 Use Case chính từ UC001 đến UC007 tương ứng với chức năng của từng bộ phận. Sự phân chia rõ ràng này giúp chúng em dễ dàng thiết kế phân quyền truy cập chức năng theo vai trò người dùng (Role-based Access Control - RBAC)."

### Slide 12: 1.6 Luồng nghiệp vụ tổng quát
*   **Người trình bày:** **Vũ Đức Hoàng Anh**
*   **Lời thoại:** "Đây là luồng dữ liệu nghiệp vụ tổng quát: Có yêu cầu nhập hàng (`ImportRequest`) thì mới tiến hành thu thập tồn kho; có tồn kho (`InventoryRecord`) mới chạy thuật toán lập phương án phân bổ (`AllocationPlan`); có phương án phân bổ được phê duyệt mới tạo đơn đặt hàng (`OverseasOrder`); và khi hàng về thì tiến hành nhận hàng và đối soát (`GoodsReceipt`). Tiếp theo, bạn Trần Đăng Sinh sẽ trình bày về Class Diagram của hệ thống."

### Slide 13: 2.1 Class diagram mức phân tích
*   **Người trình bày:** **Trần Đăng Sinh** (0.5 phút)
*   **Lời thoại:** "Em xin chào thầy cô. Đây là Class Diagram ở mức phân tích. Tại giai đoạn này, nhóm tập trung hoàn toàn vào việc mô tả các khái niệm nghiệp vụ lõi (Domain Entities) và mối quan hệ giữa chúng, chưa quan tâm đến các công nghệ như JavaFX hay Supabase Database. Các thực thể chính gồm có: `ImportRequest`, `ImportSite`, `InventoryRecord`, `AllocationPlan`, `OverseasOrder` và `GoodsReceipt`."

### Slide 14: 2.2 Class diagram mức thiết kế
*   **Người trình bày:** **Trần Đăng Sinh**
*   **Lời thoại:** "Khi chuyển sang mức thiết kế, chúng em đã chi tiết hóa cách thức triển khai bằng code. Hệ thống áp dụng kiến trúc đa lớp (Layered Architecture): Tách rõ ràng phần giao diện người dùng (`app.ui`), tầng dịch vụ điều phối nghiệp vụ (`application`), tầng lõi domain nghiệp vụ (`domain`), và tầng cổng kết nối dữ liệu (`application.port` / `infrastructure.persistence`). Mỗi Use Case giờ đây được kiểm soát bởi một Application Service tương ứng."

### Slide 15: 2.3 Analysis vs Design: Khác biệt chính
*   **Người trình bày:** **Trần Đăng Sinh**
*   **Lời thoại:** "Điểm khác biệt lớn nhất giữa hai mức độ là mức Phân tích chỉ mô tả nghiệp vụ cốt lõi, trong khi mức Thiết kế đã chi tiết hóa cách hệ thống chạy trong thực tế. Chúng em đã bổ sung thêm các thành phần giao diện JavaFX như `RequestView`, `OrderView`, bổ sung giao tiếp cơ sở dữ liệu qua `SupabaseStore` và quy định rõ ràng chiều phụ thuộc giữa các package để tránh lỗi vòng lặp phụ thuộc (circular dependency)."

### Slide 16: 2.4 Mức thiết kế đã chi tiết thêm gì?
*   **Người trình bày:** **Trần Đăng Sinh**
*   **Lời thoại:** "Cụ thể, ở mức thiết kế, chúng em bổ sung: 1. Lớp View cho từng màn hình JavaFX; 2. Lớp Service tương ứng cho từng Use Case; 3. Cổng giao tiếp `Store` để áp dụng nguyên lý Dependency Inversion; 4. Hai bộ adapter là `SupabaseStore` cho môi trường thật và `InMemoryStore` cho kiểm thử tự động độc lập; và 5. Các thực thể bổ trợ quản trị hệ thống như `UserAccount`, `Role`, `OperationLog`."

### Slide 17: 2.5 UC001 – Manage Import Requests
*   **Người trình bày:** **Vũ Đức Hoàng Anh** (0.5 phút)
*   **Lời thoại:** "Em xin phép quay lại trình bày về UC001 - Quản lý yêu cầu nhập hàng. Chức năng này dành cho phòng Kinh doanh (Sales Department) tạo các yêu cầu mua hàng. Input đầu vào là danh sách mặt hàng, số lượng và ngày nhận mong muốn. Hệ thống sẽ validate dữ liệu này thông qua `ImportRequestService` và lưu lại yêu cầu với trạng thái ban đầu là `Submitted`."

### Slide 18: 2.6 UC002 – Manage Site Information
*   **Người trình bày:** **Nguyễn Quý Đức** (0.5 phút)
*   **Lời thoại:** "Em xin trình bày về UC002 - Quản lý thông tin Site do em phụ trách. Chức năng này cho phép cập nhật hồ sơ năng lực của từng kho đối tác nước ngoài bao gồm: Tên site, thời gian vận chuyển bằng đường tàu/hàng không, và danh mục mặt hàng kinh doanh mà site hỗ trợ. Việc cập nhật chính xác các thông số này là điều kiện tiên quyết để thuật toán phân bổ ở các bước sau chạy chính xác."

### Slide 9: 2.7 UC003 – Collect Inventory Data
*   **Người trình bày:** **Trần Đăng Sinh** (0.5 phút)
*   **Lời thoại:** "Đối với UC003 - Thu thập dữ liệu tồn kho do em phụ trách. Khi có yêu cầu nhập hàng, nhân viên đặt hàng sẽ lọc các Site kinh doanh mặt hàng tương ứng và gửi truy vấn hỏi số lượng tồn kho khả dụng. Số lượng tồn kho phản hồi từ các Site sẽ được lưu trữ lại dưới dạng thực thể `InventoryRecord` để chuẩn bị cho quá trình lập phương án phân bổ."

### Slide 20: 2.8 UC004 – Plan Import Allocation
*   **Người trình bày:** **Phan Khánh Vũ** (0.5 phút)
*   **Lời thoại:** "Em xin chào thầy cô. Em phụ trách thiết kế UC004 - Lập phương án phân bổ hàng nhập khẩu. Đây là phần xử lý thuật toán chính của hệ thống. Dựa trên yêu cầu nhập hàng và dữ liệu tồn kho thu thập được từ UC003, `AllocationService` kết hợp với `AllocationPolicy` để tự động chọn ra phương án phân bổ tối ưu nhất: Gom đơn vào ít Site nhất, ưu tiên phương thức vận chuyển đường tàu giá rẻ nếu kịp deadline nhận hàng, hoặc chuyển sang đường hàng không nếu gấp. Hệ thống cũng cho phép người dùng điều chỉnh thủ công nếu cần thiết."

### Slide 21: 2.9 UC005 – Place Overseas Orders
*   **Người trình bày:** **Lương Quốc Khánh** (0.5 phút)
*   **Lời thoại:** "Em xin chào thầy cô. Em phụ trách thiết kế UC005 - Lập và xuất đơn đặt hàng chính thức. Sau khi phương án phân bổ ở UC004 được phê duyệt, `OrderService` sẽ tự động gom các dòng phân bổ theo từng Site để lập các đơn đặt hàng quốc tế (`OverseasOrder`) tương ứng. Đơn đặt hàng sau đó được xuất ra dưới dạng cấu trúc dữ liệu chuẩn để gửi tới các Site nước ngoài và chờ xác nhận (Acknowledgement)."

### Slide 22: 2.10 UC006 – Receive and Reconcile Goods
*   **Người trình bày:** **Nguyễn Hữu Nhân** (0.5 phút)
*   **Lời thoại:** "Em xin chào thầy cô. Em phụ trách thiết kế UC006 - Đối chiếu và ghi nhận hàng nhập kho. Khi hàng về tới kho nội địa, thủ kho sẽ sử dụng màn hình `ReceivingView` chọn đơn hàng gốc để nhập số lượng thực tế nhận được. Hệ thống sẽ tự động đối chiếu số lượng đặt và thực nhận, tính toán sai lệch (Discrepancy) và yêu cầu nhập ghi chú nếu phát hiện thiếu/thừa hàng trước khi cộng dồn vào tồn kho nội địa."

### Slide 23: 2.11 UC007 – Manage System
*   **Người trình bày:** **Vũ Đức Hoàng Anh** (0.5 phút)
*   **Lời thoại:** "Cuối cùng là UC007 - Quản trị hệ thống. Đây là chức năng hỗ trợ quản trị viên (`SystemAdministrator`) quản lý tài khoản người dùng, phân quyền các bộ phận theo vai trò nghiệp vụ (RBAC), cấu hình lịch sao lưu tự động và giám sát hệ thống thông qua việc truy vấn các bản ghi `OperationLog`."

---

## PHẦN 3: KIẾN TRÚC GÓI & THIẾT KẾ CHI TIẾT (Slide 24 - 38)
**Người trình bày:** **Trần Đăng Sinh** (Slide 24) & Các thành viên lần lượt trình bày Subsystem của mình (Slide 25 - 38)
**Thời lượng:** 4 phút

### Slide 24: 3.1 Biểu đồ phụ thuộc gói (Package dependency)
*   **Người trình bày:** **Trần Đăng Sinh** (0.5 phút)
*   **Lời thoại:** "Tiếp theo là sơ đồ phụ thuộc gói. Nhóm đã hiện thực hóa kiến trúc phân lớp sạch (Clean/Layered Architecture) trong code Java. Lớp giao diện `app.ui` chỉ phụ thuộc vào `application` thông qua Facade. Tầng `application` phụ thuộc vào `domain` và chỉ phụ thuộc vào cổng `application.port`. Nhờ vậy, tầng giao diện hay tầng logic nghiệp vụ hoàn toàn độc lập với các công nghệ lưu trữ dữ liệu (Supabase, MySQL hay File)."

### Slide 25 & 32: UC001 Subsystem & Design Diagram
*   **Người trình bày:** **Vũ Đức Hoàng Anh** (0.5 phút)
*   **Lời thoại:** "Ở UC001, chúng em thiết kế `ValidationSubsystem` tích hợp trong `ImportRequestService` để kiểm tra tính hợp lệ của mã mặt hàng, số lượng và ngày giao hàng. Luồng thiết kế chi tiết thể hiện rõ các đối tượng UI, Service, Entity và Port phối hợp nhịp nhàng để hoàn tất việc gửi yêu cầu."

### Slide 26 & 33: UC002 Subsystem & Design Diagram
*   **Người trình bày:** **Nguyễn Quý Đức** (0.5 phút)
*   **Lời thoại:** "Đối với UC002, `SiteCatalogSubsystem` đảm nhiệm việc đóng gói logic kiểm tra danh mục sản phẩm của Site. Thiết kế đảm bảo rằng khi cập nhật thông tin Site, hệ thống không chỉ lưu vào bảng `import_sites` mà còn đồng bộ hóa danh mục mặt hàng kinh doanh liên quan tại bảng `site_catalog` thông qua cơ chế Transaction."

### Slide 27 & 34: UC003 Subsystem & Design Diagram
*   **Người trình bày:** **Trần Đăng Sinh** (0.5 phút)
*   **Lời thoại:** "Ở UC003, `StockQuerySubsystem` đóng gói logic kết nối và truy vấn tồn kho từ các Site nước ngoài. Nếu Site phản hồi hết hàng hoặc không có kết nối, hệ thống sẽ tự động chuyển đổi và lưu số lượng tồn kho bằng `0` thay vì báo lỗi hệ thống, giúp quy trình phân bổ phía sau không bị gián đoạn."

### Slide 28 & 35: UC004 Subsystem & Design Diagram
*   **Người trình bày:** **Phan Khánh Vũ** (0.5 phút)
*   **Lời thoại:** "Tại UC004, chúng em thiết kế `AllocationEngineSubsystem` để bóc tách riêng thuật toán phân bổ. Thuật toán này sử dụng mẫu Strategy (`AllocationPolicy`) để lọc và sắp xếp các Site khả dụng theo thứ tự ưu tiên: Ưu tiên đường tàu biển trước nếu còn đủ thời gian giao hàng, gom đơn vào ít Site nhất để tiết kiệm chi phí thủ tục hải quan."

### Slide 29 & 36: UC005 Subsystem & Design Diagram
*   **Người trình bày:** **Lương Quốc Khánh** (0.5 phút)
*   **Lời thoại:** "Với UC005, `OrderTransmissionSubsystem` quản lý việc lập và phát hành đơn. Nó gom các dòng phân bổ thành đơn đặt hàng, kiểm tra tính hợp lệ của phương thức vận chuyển (chỉ chấp nhận `SHIP` hoặc `AIR`), sau đó gọi lưu trữ qua cổng `Store`."

### Slide 30 & 37: UC006 Subsystem & Design Diagram
*   **Người trình bày:** **Nguyễn Hữu Nhân** (0.5 phút)
*   **Lời thoại:** "Tại UC006, `ReconciliationSubsystem` chịu trách nhiệm đối chiếu số lượng. Khi phát hiện chênh lệch giữa thực tế nhận và hóa đơn đặt hàng gốc, nó bắt buộc người dùng nhập lý do sai lệch (Discrepancy Note) trước khi cho phép cập nhật vào kho hàng hóa tổng của doanh nghiệp."

### Slide 31 & 38: UC007 Subsystem & Design Diagram
*   **Người trình bày:** **Vũ Đức Hoàng Anh** (0.5 phút)
*   **Lời thoại:** "Cuối cùng, `SecuritySubsystem` trong UC007 quản lý phiên đăng nhập và phân quyền. Mọi hành động nhạy cảm đều được so khớp quyền truy cập của Account đang lưu trong AppSession trước khi chuyển tiếp xử lý tại Application Service."

---

## PHẦN 4: NGUYÊN LÝ & MẪU THIẾT KẾ ĐÃ ÁP DỤNG (Slide 39 - 41)
**Người trình bày:** **Nguyễn Hữu Nhân**
**Thời lượng:** 2 phút

### Slide 39: 4.1 Design principles nhóm đã áp dụng
*   **Lời thoại:** "Để đảm bảo chất lượng phần mềm, nhóm đã áp dụng triệt để các nguyên lý thiết kế SOLID:
    *   **Single Responsibility (SRP):** Mỗi Service chỉ đảm nhận đúng 1 Use Case nghiệp vụ cụ thể.
    *   **Dependency Inversion (DIP):** Lớp Service không phụ thuộc trực tiếp vào Supabase mà thông qua Interface `Store`.
    *   **Least Privilege:** Phân quyền chặt chẽ, người dùng chỉ được thực hiện chức năng được cấp quyền trong vai trò của mình."

### Slide 40: 4.2 Design patterns và kỹ thuật triển khai
*   **Lời thoại:** "Về các mẫu thiết kế (Design Patterns), nhóm đã áp dụng:
    *   **Facade Pattern:** `ImportOrderingFacade` đóng vai trò là điểm truy cập duy nhất, che giấu sự phức tạp của các Service bên dưới đối với tầng giao diện.
    *   **Strategy Pattern:** Dùng cho thuật toán phân bổ `AllocationPolicy` giúp dễ dàng thay đổi quy tắc chọn Site trong tương lai mà không ảnh hưởng tới các lớp gọi nó.
    *   **Repository Pattern:** Đóng gói thông qua Interface `Store` giúp che giấu cách thức lưu trữ dữ liệu thật."

### Slide 41: 4.3 Technology stack và testing
*   **Lời thoại:** "Về công nghệ, nhóm sử dụng Java 22, thư viện JavaFX cho giao diện, Maven để quản lý thư viện và Supabase làm cơ sở dữ liệu lưu trữ tập trung qua REST API. Một điểm kỹ thuật quan trọng là chiến lược kiểm thử: Chúng em viết các Unit Test tự động chạy hoàn toàn trên bộ nhớ thông qua `InMemoryStore` để đảm bảo tốc độ chạy test nhanh (< 1 giây) và độc lập hoàn toàn với mạng Internet hay database thật."

---

## PHẦN 5: DEMO VÀ KẾT LUẬN (Slide 42 - 45)
**Người trình bày:** **Nguyễn Quý Đức**
**Thời lượng:** 4 phút (Thuyết trình & Điều phối chạy Demo)

### Slide 42: 5.1 Thiết kế luồng demo tối ưu
*   **Lời thoại:** "Để chứng minh hệ thống hoạt động trơn tru, chúng em thiết kế một luồng demo tối ưu gồm 9 bước liên tiếp: Từ khởi tạo tài khoản quản trị, tạo tài khoản phân quyền cho các bộ phận, cập nhật thông tin site, tạo yêu cầu nhập hàng, lập phương án phân bổ tự động, đặt hàng và kết thúc bằng việc nhận hàng đối soát sai lệch tại kho."

### Slide 43: 5.2 Kịch bản demo chi tiết & Backup Plan
*   **Lời thoại:** "Kịch bản demo được chia làm 3 phần rõ rệt như trên slide. Chúng em cũng đã chuẩn bị sẵn phương án dự phòng (Backup Plan): Nếu kết nối mạng tới Supabase gặp sự cố, chúng em có thể chuyển đổi cấu hình sang chạy hoàn toàn bằng dữ liệu giả lập `InMemoryStore` ngay trên máy cục bộ để buổi demo không bị gián đoạn."

### Slide 44: 5.3 Kết luận
*   **Lời thoại:** "Tóm lại, hệ thống của nhóm đã bao phủ toàn vẹn quy trình nghiệp vụ đặt hàng nhập khẩu của doanh nghiệp. Kiến trúc phân lớp sạch sẽ, tách biệt rõ ràng logic nghiệp vụ khỏi giao diện và cơ sở dữ liệu là điểm mạnh lớn nhất của thiết kế này. Hướng phát triển tiếp theo là hỗ trợ xuất nhập file Excel/CSV và tích hợp API đồng bộ thời gian thực với hệ thống của các Site nước ngoài."

### Slide 45: Thank you
*   **Lời thoại:** "Trên đây là toàn bộ phần trình bày lý thuyết thiết kế của Nhóm 15. Sau đây, nhóm em xin phép được trình bày phần chạy Demo trực tiếp ứng dụng để minh họa các chức năng. Kính mời thầy cô theo dõi."

---

### MẸO THUYẾT TRÌNH BẢO VỆ CHO NHÓM:
1.  **Chuyển giao mượt mà:** Khi kết thúc slide của mình, hãy nói câu dẫn dắt, ví dụ: *"Sau đây, bạn Trần Đăng Sinh sẽ tiếp tục trình bày về Class Diagram..."* để tạo tính gắn kết, tránh việc im lặng ngắt quãng.
2.  **Tập trung vào Thiết kế (ITSS):** Môn ITSS là môn Phân tích Thiết kế, thầy cô rất dị ứng với việc chỉ chiếu code hoặc chỉ nói về giao diện. Hãy nhấn mạnh vào **Kiến trúc đa lớp (Layered)**, **Sự tách biệt phụ thuộc gói (Package Dependency)** và cách áp dụng **DIP (Dependency Inversion)**.
3.  **Chuẩn bị Demo kỹ càng:** Hãy chạy sẵn ứng dụng JavaFX lên trước khi bắt đầu thuyết trình để khi đến phần Demo chỉ cần kéo cửa sổ phần mềm ra và thao tác theo đúng các bước trong kịch bản [demo_uc002_script.md](file:///home/nqd2/code/ITSSVN/2025.2-166155-15/Homework07/Group/docs/demo_uc002_script.md).
