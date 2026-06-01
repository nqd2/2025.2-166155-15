# AGENTS.md

## Mục tiêu repository

- Đây là repo tài liệu phân tích yêu cầu cho bài tập môn ITSS về hệ thống đặt hàng nhập khẩu.
- Trọng tâm tài liệu nằm trong `docs/`; final project Homework07 triển khai code trong `Homework07/Group/`.

## Về từng bài tập

- Với tất cả các bài tập, luôn luôn phải có file doc, pdf, và các file ảnh trong thư mục của bài tập đó. Ví dụ sinh viên Nguyễn Văn A có MSSV 20001234 nộp bài tập sô 6 thì phải nộp vào Homework06/20001234-NguyenVanA/ tại nhánh hw06/20001234-NguyenVanA. Sau đó tạo pull request từ nhánh hw06/20001234-NguyenVanA sang nhánh main. Bài tập gộp chung của nhóm thì sẽ được trưởng nhóm thu thập lại từ các thành viên trong nhóm sau đó tổng hợp vào Homework06/Group/ thẳng ở main luôn.
- Nhóm có 6 thành viên
  - Nguyễn Hữu Nhân (<nguyenhuunhanht2005@gmail.com>)
  - Vũ Đức Hoàng Anh (<incuksuk123@gmail.com>)
  - Trần Đăng Sinh (<ctan4991@gmail.com>)
  - Phan Khánh Vũ (<hi3sirin@gmail.com>)
  - Lương Quốc Khánh (<khanhlq.hust.work@gmail.com>)
  - Nguyễn Quý Đức (<nguyenquyduc_hsgs20@hus.edu.vn>) = trưởng nhóm

## Phân công use case hiện tại

| Thành viên | Email | Use case phụ trách |
| --- | --- | --- |
| Nguyễn Hữu Nhân | <nguyenhuunhanht2005@gmail.com> | UC006 - `ReceiveAndReconcileGoods` |
| Vũ Đức Hoàng Anh | <incuksuk123@gmail.com> | UC001 - `ManageImportRequests`; UC007 - `ManageSystem` |
| Trần Đăng Sinh | <ctan4991@gmail.com> | UC003 - `CollectInventoryData` |
| Phan Khánh Vũ | <hi3sirin@gmail.com> | UC004 - `PlanImportAllocation` |
| Lương Quốc Khánh | <khanhlq.hust.work@gmail.com> | UC005 - `PlaceOverseasOrders` |
| Nguyễn Quý Đức | <nguyenquyduc_hsgs20@hus.edu.vn> | UC002 - `ManageSiteInformation`; trưởng nhóm, phụ trách tích hợp và rà soát đồng bộ |

## Quy ước đặt tên branch

- Branch bài tập cá nhân: `hw<so-bai-2-chu-so>/<MSSV>-<HoTenKhongDau>`. Ví dụ: `hw06/20235798-NguyenHuuNhan`.
- Branch làm tài liệu use case theo phase: `docs/phase<1-4>/<uc-id>-<use-case-slug>-<member-slug>`. Ví dụ: `docs/phase2/uc006-receive-and-reconcile-goods-nguyen-huu-nhan`.
- Branch tích hợp nhóm: `docs/integration/phase<1-4>-group` hoặc `docs/integration/final-group`.
- Branch sửa lỗi tài liệu nhỏ: `fix/docs-<noi-dung-ngan>`. Ví dụ: `fix/docs-uc004-allocation-sequence`.
- Tất cả branch dùng chữ thường cho phần slug, không dấu, không khoảng trắng; dùng dấu gạch ngang `-` trong slug và dấu gạch chéo `/` để tách nhóm branch.
- Mỗi branch chỉ nên sửa một use case và một phase. Riêng Vũ Đức Hoàng Anh tách UC001 và UC007 thành hai branch khác nhau để dễ review.
- Nếu quy định bài tập yêu cầu branch dạng `hw<so-bai>/<MSSV>-<HoTenKhongDau>`, quy định bài tập được ưu tiên hơn branch `docs/phase*`.

## Final project Homework07

- Final project là Homework07, toàn bộ 6 thành viên cùng làm trong `Homework07/Group/`.
- Nhánh tích hợp chính của Homework07 là `develop`; mọi nhánh cá nhân phải tách từ `develop`, không tách trực tiếp từ `main`.
- Quy ước branch Homework07: `hw07/<MSSV>-<HoTenKhongDau>/<type>/<short-topic>`.
- `type` hợp lệ: `feature`, `fix`, `hotfix`, `refactor`, `test`, `docs`, `chore`.
- Ví dụ branch:
  - `hw07/20235798-NguyenHuuNhan/feature/receive-reconcile-goods`
  - `hw07/20235658-VuDucHoangAnh/feature/manage-import-requests`
  - `hw07/20235658-VuDucHoangAnh/feature/manage-system`
  - `hw07/20235821-TranDangSinh/feature/collect-inventory-data`
  - `hw07/20235880-PhanKhanhVu/feature/plan-import-allocation`
  - `hw07/20235754-LuongQuocKhanh/feature/place-overseas-orders`
  - `hw07/20235682-NguyenQuyDuc/feature/manage-site-information`
- Luồng merge: member branch -> pull request vào `develop`; trưởng nhóm kiểm tra build/test; `develop` ổn định mới merge về `main`.
- Công nghệ bắt buộc: Maven (`mvn`) và JDK 22.
- Trước khi tạo PR, chạy tối thiểu: `mvn -version`, `mvn clean test`, và nếu có đóng gói ứng dụng thì chạy thêm `mvn clean package`.
- Cấu hình Maven phải đặt `maven.compiler.release` là `22`; không dùng JDK thấp hơn 22 cho Homework07.
- Kế hoạch chi tiết Homework07 nằm tại `docs/superpowers/plans/2026-06-01-homework07-final-project-plan.md`.

## Ánh xạ nghiệp vụ theo ghi chú giảng viên

- Số thứ tự 1-7 trong ghi chú nghiệp vụ không thay thế mã `UC00x`; khi cập nhật tài liệu phải bám theo mã use case trong `docs/details/UC00x_*`.
- Ghi chú nghiệp vụ 1 "Tạo danh sách yêu cầu nhập hàng" thuộc UC001 - `ManageImportRequests`.
- Ghi chú nghiệp vụ 2 "Lọc Site và gửi yêu cầu kiểm tra tồn kho" thuộc UC003 - `CollectInventoryData`.
- Ghi chú nghiệp vụ 3 "Cập nhật số lượng tồn kho từ Site" thuộc UC003 - `CollectInventoryData`.
- Ghi chú nghiệp vụ 4 "Cập nhật thời gian vận chuyển của Site" thuộc UC002 - `ManageSiteInformation`.
- Ghi chú nghiệp vụ 5 "Phân bổ phương án đặt hàng cho mặt hàng" thuộc UC004 - `PlanImportAllocation`.
- Ghi chú nghiệp vụ 6 "Lập và xuất đơn đặt hàng chính thức" thuộc UC005 - `PlaceOverseasOrders`.
- Ghi chú nghiệp vụ 7 "Đối chiếu và ghi nhận hàng nhập kho" thuộc UC006 - `ReceiveAndReconcileGoods`.
- UC007 - `ManageSystem` là use case quản trị hệ thống, dữ liệu master, phân quyền, log và sao lưu/khôi phục; đây là use case hỗ trợ toàn hệ thống, không phải một bước nghiệp vụ nhập hàng tuyến tính.

## Kế hoạch 4 phase thực hiện Homework07

- Homework07 là giai đoạn code final project; phần phân tích/thiết kế coi như đã chốt, không dùng 4 phase này để chuẩn hóa lại diagram.
- Kế hoạch chi tiết để thực hiện từng phase nằm tại `docs/superpowers/plans/2026-06-01-homework07-final-project-plan.md`.

### Phase 1 - Khởi tạo project và nền kiến trúc code

- Trưởng nhóm tạo/cập nhật nhánh `develop` từ `main`; mọi thành viên fetch `develop` trước khi tách nhánh cá nhân.
- Khởi tạo Maven project trong `Homework07/Group/`, dùng JDK 22, cấu hình `maven.compiler.release=22`.
- Thống nhất package, module/layer code, coding convention, cấu trúc test và cách chạy app.
- Tạo skeleton domain/service/repository/controller hoặc UI theo thiết kế đã có; chưa viết lan man nghiệp vụ chi tiết.
- Đầu ra phase 1: `Homework07/Group/pom.xml`, cấu trúc `src/main/java`, `src/test/java`, `README.md`, build `mvn clean test` chạy được trên JDK 22.

### Phase 2 - Code module use case theo nhánh cá nhân

- Mỗi thành viên tách nhánh từ `develop` theo mẫu `hw07/<MSSV>-<HoTenKhongDau>/feature/<short-topic>`.
- Mỗi người code đúng use case đã phân công: domain logic, service, repository/mock data, controller/UI/API tương ứng và unit test.
- Dữ liệu và tên field phải bám thiết kế đã chốt: `requestId`, `orderReference`, `siteCode`, `merchandiseCode`, `quantity`, `unit`, `desiredDeliveryDate`, `deliveryMeans`, `status`, `discrepancyNote`.
- Trước khi mở PR về `develop`, từng nhánh phải chạy `mvn -version` và `mvn clean test`.
- Đầu ra phase 2: các PR feature vào `develop`, mỗi PR có code + test cho use case phụ trách, không commit trực tiếp vào `develop`.

### Phase 3 - Tích hợp luồng nghiệp vụ end-to-end

- Trưởng nhóm merge PR vào `develop` theo thứ tự phụ thuộc: UC002/UC007 nền dữ liệu và quyền, UC001, UC003, UC004, UC005, UC006.
- Tích hợp luồng chính: tạo yêu cầu nhập -> lọc Site/hỏi tồn kho -> phân bổ -> phát hành đơn -> nhận và đối chiếu hàng.
- Bổ sung integration test hoặc kịch bản demo chạy qua nhiều use case; sửa conflict bằng nhánh `hw07/<MSSV>-<HoTenKhongDau>/fix/<short-topic>`.
- Kiểm tra các lỗi nghiệp vụ quan trọng: dữ liệu nhập sai, thiếu tồn kho, phương thức vận chuyển không hợp lệ, Site từ chối đơn, lệch số lượng nhận hàng.
- Đầu ra phase 3: `develop` chạy được luồng demo chính, `mvn clean test` và `mvn clean package` pass trên JDK 22.

### Phase 4 - Ổn định, đóng gói và nộp bài

- Freeze feature mới; chỉ nhận `fix`, `hotfix`, `test`, `docs`, `chore` cần thiết.
- Hoàn thiện README hướng dẫn build/run, tài liệu mô tả chức năng, ảnh minh họa/demo, file doc/pdf nếu bài nộp yêu cầu.
- Chạy quality gate cuối: `mvn -version`, `mvn clean test`, `mvn clean package`, quét placeholder và rà soát file nộp trong `Homework07/Group/`.
- Sau khi `develop` ổn định, trưởng nhóm merge `develop` về `main`.
- Đầu ra phase 4: final project trong `Homework07/Group/`, build/package được bằng Maven + JDK 22, đủ tài liệu nộp bài.

## Nhiệm vụ trọng tâm theo người

- Nguyễn Hữu Nhân - UC006 `ReceiveAndReconcileGoods`: tập trung màn hình chọn mã đơn hàng, nhập số lượng thực nhận, đối chiếu thiếu/thừa, ghi chú sai lệch, cập nhật tồn kho nội bộ và cảnh báo cho bộ phận đặt hàng khi vượt ngưỡng.
- Vũ Đức Hoàng Anh - UC001 `ManageImportRequests`: tập trung màn hình tạo yêu cầu nhập, nhập/tải danh sách mặt hàng, validation mã hàng/số lượng/đơn vị/ngày nhận mong muốn và trạng thái Submitted/Updated/Cancelled.
- Vũ Đức Hoàng Anh - UC007 `ManageSystem`: tập trung quản lý tài khoản, vai trò, dữ liệu master, nhật ký vận hành và sao lưu/khôi phục; đảm bảo UC007 không lấn sang luồng nghiệp vụ nhập hàng.
- Trần Đăng Sinh - UC003 `CollectInventoryData`: tập trung lọc Site theo mặt hàng, gửi yêu cầu hỏi tồn kho, nhận phản hồi tồn kho, nhập số lượng 0 khi hết hàng, timeout/gửi lại và ghi vào tệp thông tin kho.
- Phan Khánh Vũ - UC004 `PlanImportAllocation`: tập trung thuật toán phân bổ từng mặt hàng, kiểm tra ngày nhận mong muốn, ưu tiên đường tàu, ưu tiên tồn kho lớn, gom ít Site nhất, cảnh báo thiếu nguồn cung và cho phép điều chỉnh tay.
- Lương Quốc Khánh - UC005 `PlaceOverseasOrders`: tập trung tổng hợp phân bổ theo Site, lập đơn chính thức, preview dòng đơn, phương thức vận chuyển ship/air, gửi đơn và xử lý xác nhận/từ chối từ Site.
- Nguyễn Quý Đức - UC002 `ManageSiteInformation`: tập trung hồ sơ Site, danh mục mặt hàng Site kinh doanh, cập nhật thời gian vận chuyển bằng tàu/hàng không và thông báo thay đổi cho bộ phận đặt hàng quốc tế.
- Nguyễn Quý Đức - trưởng nhóm: quản lý `develop`, review PR, kiểm tra dependency giữa UC001-UC006, rà soát UC007, chạy `mvn clean test`/`mvn clean package`, gom tài liệu nộp bài và merge final về `main`.

## Phạm vi chỉnh sửa

- Được phép chỉnh sửa: `docs/**`, `Homework07/**`, `.vscode/settings.json`, `.gitignore`, `AGENTS.md`.
- Không chỉnh sửa nội dung trong `docs/do-not-edit/**` trừ khi có yêu cầu rõ ràng từ người dùng.
- Không tự ý đổi tên file đang được giảng viên/nhóm tham chiếu.

## Quy ước nội dung

- Ngôn ngữ chính: tiếng Việt.
- Tên use case nên nhất quán theo tiếng Anh ngắn gọn (ví dụ: `ManageImportRequests`).
- Nội dung phải bám sát đề bài trong `docs/Project BTL - HeThongDatHangNhapKhau 20252.docx.md`.
- Tránh đưa thêm tính năng ngoài phạm vi bài toán nếu chưa có yêu cầu.

## Quy ước PlantUML

- Sơ đồ tổng quan dùng chung có thể đặt trong `docs/puml/` hoặc `docs/details/overview/` theo cấu trúc hiện có.
- Sơ đồ chi tiết theo use case đặt trong `docs/details/UC00x_<UseCaseName>/`.
- Không dùng file style `!include` riêng trong nguồn `.puml`.
- Với tài liệu markdown trong `docs/`, nhúng sơ đồ bằng đường dẫn tương đối tới file `.puml` cần dùng, ví dụ `!include ./puml/<ten-file>.puml` hoặc `!include ./details/UC001_ManageImportRequests/usecase_manage_import_requests.puml`.
- Biểu đồ phân rã use case (nhóm ITSS): `usecase_manage_import_requests.puml`, `usecase_manage_site_information.puml`, `usecase_collect_inventory_data.puml`, `usecase_plan_import_allocation.puml`, `usecase_place_overseas_orders.puml`, `usecase_receive_and_reconcile_goods.puml`, `usecase_manage_system.puml`.
- Mỗi thư mục `docs/details/UC00x_<UseCaseName>/` hiện có bộ 11 diagram nguồn: `usecase`, `activity`, `class_analysis`, `communication_analysis`, `sequence_analysis`, `class_design`, `package_design`, `sequence_design`, `class_subsystem`, `sequence_subsystem`, `screen_transition`.
- File `UC00x_<UseCaseName>_all.puml` là file merge để đọc/rà soát toàn bộ diagram của một use case; không chỉnh tay nội dung trong file merge nếu thay đổi có thể thực hiện ở diagram nguồn.
- Khi có cả `.puml` và `.mermaid`, nội dung hai bản phải tương đương về actor, class, message, trạng thái và luồng chính.

## Checklist trước khi kết thúc chỉnh sửa

- Đảm bảo các mục actor/use case đồng bộ giữa `docs/Analysis.md` và `docs/RequirementAnalysis-VN.docx.md`.
- Không để placeholder kiểu `...`, `xxx`, `TODO` ở phần đã yêu cầu hoàn thiện.

## Naming convention

- Tên file markdown: `<ten-use-case>.md` (ví dụ: `ManageImportRequests.md`).
- Tên file PlantUML: `<ten-diagram>.puml` (ví dụ: `usecase_overview.puml`).
- Đặt tên trong biểu đồ và trường dữ liệu: tiếng Anh, ưu tiên chuẩn dạng identifier dễ đọc.
  - Tên actor: `PascalCase` (ví dụ: `SalesDepartment`, `SystemAdministrator`).
  - Tên use case: `PascalCase` theo cụm động từ (ví dụ: `ManageImportRequests`, `PlanImportAllocation`).
  - Tên class (nếu có trong diagram phân tích): `PascalCase` (ví dụ: `ImportRequest`, `Site`, `InventoryRecord`).
  - Tên method/operation (nếu có trong diagram phân tích): `camelCase` (ví dụ: `calculateAllocation()`, `reconcileGoods()`).
  - Tên biến (nếu có): `camelCase` (ví dụ: `requestedQty`, `inStockQty`).
  - Tên diagram (tên file): `snake_case` (ví dụ: `usecase_overview.puml`, `sequence_manage_site.puml`).

## Cách biểu diễn đặc tả use case

### json

```{
  "useCase": {
    "Mã UseCase": ${useCaseId},
    "Tên UseCase": "${useCaseName}",
    "Tác nhân": [
      ${actor1}, ${actor2}, ...
    ],
    "Tiên điều kiện": "${preconditions}",
    "Luồng sự kiện chính": [
      {
        "STT": "1",
        "Thực hiện bởi": "${actorPerformingAction1}",
        "Hành động": "${action1}"
      },
      {
        "STT": "2",
        "Thực hiện bởi": "${actorPerformingAction2}",
        "Hành động": "${action2}"
      },
      {
        "STT": "3",
        "Thực hiện bởi": "${actorPerformingAction3}",
        "Hành động": "${action3}"
      },
      {
        "STT": "4",
        "Thực hiện bởi": "${actorPerformingAction4}",
        "Hành động": "${action4}"
      },
      {
        "STT": "5",
        "Thực hiện bởi": "${actorPerformingAction5}",
        "Hành động": "${action5}"
      }
    ],
    "luongSuKienThayThe": [
      {
        "STT": "5a",
        "Thực hiện bởi": "${actorPerformingAction5a}",
        "Hành động": "${action5a}"
      }
    ],
    "hauDieuKien": "${alternativePreconditions}",
    "Dữ liệu đầu vào": [
      {
        "STT": "1",
        "Trường dữ liệu": "${inputData1}",
        "Mô tả": "${inputData1Description}",
        "Bắt buộc": boolean,
        "Điều kiện hợp lệ": "${inputData1ValidCondition}",
        "Ví dụ": "${inputData1Example}"
      },
      {
        "STT": "2",
        "Trường dữ liệu": "${inputData2}",
        "Mô tả": "${inputData2Description}",
        "Bắt buộc": boolean,
        "Điều kiện hợp lệ": "${inputData2ValidCondition}",
        "Ví dụ": "${inputData2Example}"
      },
      {
        "STT": "3",
        "Trường dữ liệu": "${inputData3}",
        "Mô tả": "${inputData3Description}",
        "Bắt buộc": boolean,
        "Điều kiện hợp lệ": "${inputData3ValidCondition}",
        "Ví dụ": "${inputData3Example}"
      }
    ]
  }
}
```

### HTML bảng lồng nhau

<table border="1" cellpadding="6" cellspacing="0" style="border-collapse: collapse; width: 100%;">
  <colgroup>
    <col style="width: 16%;">
    <col style="width: 34%;">
    <col style="width: 16%;">
    <col style="width: 34%;">
  </colgroup>
  <tr>
    <th colspan="4">Đặc tả Use Case</th>
  </tr>
  <tr>
    <td><strong>Mã UseCase</strong></td>
    <td>${useCaseId}</td>
    <td><strong>Tên UseCase</strong></td>
    <td>${useCaseName}</td>
  </tr>
  <tr>
    <td><strong>Tác nhân</strong></td>
    <td colspan="3">${actor1}, ${actor2}, ...</td>
  </tr>
  <tr>
    <td><strong>Tiền điều kiện</strong></td>
    <td colspan="3">${preconditions}</td>
  </tr>
  <tr>
    <td><strong>Luồng sự kiện chính</strong></td>
    <td colspan="3">
      <table border="1" cellpadding="4" cellspacing="0" style="border-collapse: collapse; width: 100%;">
        <tr>
          <th>STT</th>
          <th>Thực hiện bởi</th>
          <th>Hành động</th>
        </tr>
        <tr>
          <td>1</td>
          <td>${actorPerformingAction1}</td>
          <td>${action1}</td>
        </tr>
        <tr>
          <td>2</td>
          <td>${actorPerformingAction2}</td>
          <td>${action2}</td>
        </tr>
        <tr>
          <td>3</td>
          <td>${actorPerformingAction3}</td>
          <td>${action3}</td>
        </tr>
        <tr>
          <td>4</td>
          <td>${actorPerformingAction4}</td>
          <td>${action4}</td>
        </tr>
        <tr>
          <td>5</td>
          <td>${actorPerformingAction5}</td>
          <td>${action5}</td>
        </tr>
      </table>
    </td>
  </tr>
  <tr>
    <td><strong>Luồng sự kiện thay thế</strong></td>
    <td colspan="3">
      <table border="1" cellpadding="4" cellspacing="0" style="border-collapse: collapse; width: 100%;">
        <tr>
          <th>STT</th>
          <th>Thực hiện bởi</th>
          <th>Hành động</th>
        </tr>
        <tr>
          <td>5a</td>
          <td>${actorPerformingAction5a}</td>
          <td>${action5a}</td>
        </tr>
      </table>
    </td>
  </tr>
  <tr>
    <td><strong>Hậu điều kiện</strong></td>
    <td colspan="3">${alternativePreconditions}</td>
  </tr>
</table>
<br/>

**Dữ liệu đầu vào:**
<table border="1" cellpadding="6" cellspacing="0" style="border-collapse: collapse; width: 100%;">
  <tr>
    <td><strong>STT</strong></td>
    <td><strong>Trường dữ liệu</strong></td>
    <td><strong>Mô tả</strong></td>
          <th>Bắt buộc</th>
          <th>Điều kiện hợp lệ</th>
          <th>Ví dụ</th>
        </tr>
        <tr>
          <td>1</td>
          <td>${inputData1}</td>
          <td>${inputData1Description}</td>
          <td>${inputData1Required}</td>
          <td>${inputData1ValidCondition}</td>
          <td>${inputData1Example}</td>
        </tr>
        <tr>
          <td>2</td>
          <td>${inputData2}</td>
          <td>${inputData2Description}</td>
          <td>${inputData2Required}</td>
          <td>${inputData2ValidCondition}</td>
          <td>${inputData2Example}</td>
        </tr>
        <tr>
          <td>3</td>
          <td>${inputData3}</td>
          <td>${inputData3Description}</td>
          <td>${inputData3Required}</td>
          <td>${inputData3ValidCondition}</td>
          <td>${inputData3Example}</td>
        </tr>
      </table>
    </td>
  </tr>
</table>

## Screen Transition Diagram

<!-- gitnexus:start -->
# GitNexus — Code Intelligence

This project is indexed by GitNexus as **2025.2-166155-15** (1617 symbols, 4348 relationships, 116 execution flows). Use the GitNexus MCP tools to understand code, assess impact, and navigate safely.

> If any GitNexus tool warns the index is stale, run `npx gitnexus analyze` in terminal first.

## Always Do

- **MUST run impact analysis before editing any symbol.** Before modifying a function, class, or method, run `gitnexus_impact({target: "symbolName", direction: "upstream"})` and report the blast radius (direct callers, affected processes, risk level) to the user.
- **MUST run `gitnexus_detect_changes()` before committing** to verify your changes only affect expected symbols and execution flows.
- **MUST warn the user** if impact analysis returns HIGH or CRITICAL risk before proceeding with edits.
- When exploring unfamiliar code, use `gitnexus_query({query: "concept"})` to find execution flows instead of grepping. It returns process-grouped results ranked by relevance.
- When you need full context on a specific symbol — callers, callees, which execution flows it participates in — use `gitnexus_context({name: "symbolName"})`.

## When Debugging

1. `gitnexus_query({query: "<error or symptom>"})` — find execution flows related to the issue
2. `gitnexus_context({name: "<suspect function>"})` — see all callers, callees, and process participation
3. `READ gitnexus://repo/2025.2-166155-15/process/{processName}` — trace the full execution flow step by step
4. For regressions: `gitnexus_detect_changes({scope: "compare", base_ref: "main"})` — see what your branch changed

## When Refactoring

- **Renaming**: MUST use `gitnexus_rename({symbol_name: "old", new_name: "new", dry_run: true})` first. Review the preview — graph edits are safe, text_search edits need manual review. Then run with `dry_run: false`.
- **Extracting/Splitting**: MUST run `gitnexus_context({name: "target"})` to see all incoming/outgoing refs, then `gitnexus_impact({target: "target", direction: "upstream"})` to find all external callers before moving code.
- After any refactor: run `gitnexus_detect_changes({scope: "all"})` to verify only expected files changed.

## Never Do

- NEVER edit a function, class, or method without first running `gitnexus_impact` on it.
- NEVER ignore HIGH or CRITICAL risk warnings from impact analysis.
- NEVER rename symbols with find-and-replace — use `gitnexus_rename` which understands the call graph.
- NEVER commit changes without running `gitnexus_detect_changes()` to check affected scope.

## Tools Quick Reference

| Tool | When to use | Command |
|------|-------------|---------|
| `query` | Find code by concept | `gitnexus_query({query: "auth validation"})` |
| `context` | 360-degree view of one symbol | `gitnexus_context({name: "validateUser"})` |
| `impact` | Blast radius before editing | `gitnexus_impact({target: "X", direction: "upstream"})` |
| `detect_changes` | Pre-commit scope check | `gitnexus_detect_changes({scope: "staged"})` |
| `rename` | Safe multi-file rename | `gitnexus_rename({symbol_name: "old", new_name: "new", dry_run: true})` |
| `cypher` | Custom graph queries | `gitnexus_cypher({query: "MATCH ..."})` |

## Impact Risk Levels

| Depth | Meaning | Action |
|-------|---------|--------|
| d=1 | WILL BREAK — direct callers/importers | MUST update these |
| d=2 | LIKELY AFFECTED — indirect deps | Should test |
| d=3 | MAY NEED TESTING — transitive | Test if critical path |

## Resources

| Resource | Use for |
|----------|---------|
| `gitnexus://repo/2025.2-166155-15/context` | Codebase overview, check index freshness |
| `gitnexus://repo/2025.2-166155-15/clusters` | All functional areas |
| `gitnexus://repo/2025.2-166155-15/processes` | All execution flows |
| `gitnexus://repo/2025.2-166155-15/process/{name}` | Step-by-step execution trace |

## Self-Check Before Finishing

Before completing any code modification task, verify:
1. `gitnexus_impact` was run for all modified symbols
2. No HIGH/CRITICAL risk warnings were ignored
3. `gitnexus_detect_changes()` confirms changes match expected scope
4. All d=1 (WILL BREAK) dependents were updated

## Keeping the Index Fresh

After committing code changes, the GitNexus index becomes stale. Re-run analyze to update it:

```bash
npx gitnexus analyze
```

If the index previously included embeddings, preserve them by adding `--embeddings`:

```bash
npx gitnexus analyze --embeddings
```

To check whether embeddings exist, inspect `.gitnexus/meta.json` — the `stats.embeddings` field shows the count (0 means no embeddings). **Running analyze without `--embeddings` will delete any previously generated embeddings.**

> Claude Code users: A PostToolUse hook handles this automatically after `git commit` and `git merge`.

## CLI

| Task | Read this skill file |
|------|---------------------|
| Understand architecture / "How does X work?" | `.claude/skills/gitnexus/gitnexus-exploring/SKILL.md` |
| Blast radius / "What breaks if I change X?" | `.claude/skills/gitnexus/gitnexus-impact-analysis/SKILL.md` |
| Trace bugs / "Why is X failing?" | `.claude/skills/gitnexus/gitnexus-debugging/SKILL.md` |
| Rename / extract / split / refactor | `.claude/skills/gitnexus/gitnexus-refactoring/SKILL.md` |
| Tools, resources, schema reference | `.claude/skills/gitnexus/gitnexus-guide/SKILL.md` |
| Index, status, clean, wiki CLI commands | `.claude/skills/gitnexus/gitnexus-cli/SKILL.md` |

<!-- gitnexus:end -->
