# AGENTS.md

## Mục tiêu repository

- Đây là repo tài liệu phân tích yêu cầu cho bài tập môn ITSS về hệ thống đặt hàng nhập khẩu.
- Trọng tâm hiện tại là các file trong `docs/` (SRS, phân tích actor/use case, PlantUML).

## Về từng bài tập

-  Với tất cả các bài tập, luôn luôn phải có file doc, pdf, và các file ảnh trong thư mục của bài tập đó. Ví dụ sinh viên Nguyễn Văn A có MSSV 20001234 nộp bài tập sô 6 thì phải nộp vào Homework06/20001234-NguyenVanA/ tại nhánh hw06/20001234-NguyenVanA. Sau đó tạo pull request từ nhánh hw06/20001234-NguyenVanA sang nhánh main. Bài tập gộp chung của nhóm thì sẽ được trưởng nhóm thu thập lại từ các thành viên trong nhóm sau đó tổng hợp vào Homework06/Group/ thẳng ở main luôn.
- Nhóm có 6 thành viên 
  - Nguyễn Hữu Nhân (<nguyenhuunhanht2005@gmail.com>)
  - Vũ Đức Hoàng Anh (<incuksuk123@gmail.com>)
  - Trần Đăng Sinh (<ctan4991@gmail.com>)
  - Phan Khánh Vũ (<hi3sirin@gmail.com>)
  - Lương Quốc Khánh (<khanhlq.hust.work@gmail.com>)
  - Nguyễn Quý Đức (<nguyenquyduc_hsgs20@hus.edu.vn>) = trưởng nhóm


## Phạm vi chỉnh sửa

- Được phép chỉnh sửa: `docs/**`, `.vscode/settings.json`, `.gitignore`, `AGENTS.md`.
- Không chỉnh sửa nội dung trong `docs/do-not-edit/**` trừ khi có yêu cầu rõ ràng từ người dùng.
- Không tự ý đổi tên file đang được giảng viên/nhóm tham chiếu.

## Quy ước nội dung

- Ngôn ngữ chính: tiếng Việt.
- Tên use case nên nhất quán theo tiếng Anh ngắn gọn (ví dụ: `ManageImportRequests`).
- Nội dung phải bám sát đề bài trong `docs/Project BTL - HeThongDatHangNhapKhau 20252.docx.md`.
- Tránh đưa thêm tính năng ngoài phạm vi bài toán nếu chưa có yêu cầu.

## Quy ước PlantUML

- Tất cả sơ đồ `.puml` đặt trong `docs/puml/`.
- Không dùng file style `!include` riêng trong nguồn `.puml`.
- Với tài liệu markdown trong `docs/`, nhúng sơ đồ bằng: `!include ./puml/<ten-file>.puml`
- Biểu đồ phân rã use case (nhóm ITSS): `usecase_manage_import_requests.puml`, `usecase_manage_site_information.puml`, `usecase_collect_inventory_data.puml`, `usecase_plan_import_allocation.puml`, `usecase_place_overseas_orders.puml`, `usecase_receive_and_reconcile_goods.puml`, `usecase_manage_system.puml`.

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
  <tr>
    <td><strong>Dữ liệu đầu vào</strong></td>
    <td colspan="3">
      <table border="1" cellpadding="4" cellspacing="0" style="border-collapse: collapse; width: 100%;">
        <tr>
          <th>STT</th>
          <th>Trường dữ liệu</th>
          <th>Mô tả</th>
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



từ toàn bộ các plan (nhìn trong folder details, các file UC00x_[UC_NAME]_all.puml) và toàn bộ các kế hoach cụ thể cho các use case (file markdown [UC_NAME].md)

lập kế hoạch 4 phase cho 6 người làm. 
cụ thể như sau:

  - Nguyễn Hữu Nhân (<nguyenhuunhanht2005@gmail.com>): UC 6 
  - Vũ Đức Hoàng Anh (<incuksuk123@gmail.com>): UC 1 + 7 
  - Trần Đăng Sinh (<ctan4991@gmail.com>): UC 3 
  - Phan Khánh Vũ (<hi3sirin@gmail.com>): UC 4 
  - Lương Quốc Khánh (<khanhlq.hust.work@gmail.com>): UC 5 
  - Nguyễn Quý Đức (<nguyenquyduc_hsgs20@hus.edu.vn>) = trưởng nhóm: UC 2

trong đó:
UC001 (ManageImportRequests)
UC002 (ManageSiteInformation)
UC003 (CollectInventoryData)
UC004 (PlanImportAllocation)
UC005 (PlaceOverseasOrders)
UC006 (ReceiveAndReconcileGoods)
UC007 (ManageSystem)


[3/17/2026 8:19 PM] Hai Son Bac: 1. Tạo danh sách yêu cầu nhập hàng
Tác nhân: Bộ phận bán hàng.
Mô tả UI & Nghiệp vụ: Nhân viên bán hàng mở màn hình "Tạo yêu cầu nhập", nhập hoặc tải lên danh sách các mặt hàng cần đặt. Giao diện yêu cầu nhập đầy đủ các trường: mã hàng, số lượng, đơn vị và ngày nhận mong muốn. Hệ thống kiểm tra tính hợp lệ của dữ liệu (ví dụ: ngày nhận mong muốn phải ở tương lai, số lượng > 0) trước khi nhân viên bấm "Gửi yêu cầu" sang Bộ phận đặt hàng quốc tế.
[3/17/2026 8:19 PM] Hai Son Bac: 2. Lọc Site và gửi yêu cầu kiểm tra tồn kho
Tác nhân: Bộ phận đặt hàng quốc tế.
Mô tả UI & Nghiệp vụ: Màn hình hiển thị danh sách các mặt hàng cần nhập. Nhân viên chọn một hoặc nhiều mặt hàng, hệ thống tự động tra cứu và gợi ý danh sách các Site nhập khẩu có kinh doanh mặt hàng đó. Nhân viên xem xét, tích chọn các Site phù hợp trên lưới dữ liệu và bấm "Gửi yêu cầu hỏi tồn kho". Hệ thống sẽ tạo thông điệp gửi đi.
[3/17/2026 8:20 PM] Hai Son Bac: 3. Cập nhật số lượng tồn kho từ Site
Tác nhân: Bộ phận đặt hàng quốc tế.
Mô tả UI & Nghiệp vụ: Khi các Site phản hồi về số lượng hàng còn trong kho, nhân viên mở màn hình "Cập nhật tồn kho", chọn mã Site và mã mặt hàng. Nhân viên nhập số lượng thực tế mà Site báo lại (nếu Site báo hết hàng thì nhập số 0). Sau khi lưu, hệ thống ghi nhận thông tin này vào Tệp thông tin kho.
[3/17/2026 8:20 PM] Hai Son Bac: 4. Cập nhật thời gian vận chuyển của Site
Tác nhân: Bộ phận đặt hàng quốc tế.
Mô tả UI & Nghiệp vụ: Khi có thông báo thay đổi từ Site, nhân viên mở hồ sơ vận chuyển của Site đó trên phần mềm. Nhân viên tiến hành chỉnh sửa "Số ngày vận chuyển bằng tàu" hoặc "Số ngày vận chuyển bằng hàng không". Use case này giải quyết một tác vụ cập nhật rất cụ thể thay vì một chức năng "Quản lý" chung chung.
[3/17/2026 8:20 PM] Hai Son Bac: 5. Phân bổ phương án đặt hàng cho mặt hàng
Tác nhân: Bộ phận đặt hàng quốc tế.
Mô tả UI & Nghiệp vụ: Nhân viên chọn một mặt hàng cụ thể để xử lý độc lập. Màn hình hiển thị thông tin số lượng cần nhập, ngày mong muốn và danh sách các Site đang có tồn kho. Hệ thống chạy thuật toán gợi ý chia nhỏ số lượng đặt cho từng Site dựa trên các quy tắc: đáp ứng ngày nhận , ưu tiên đường tàu , ưu tiên Site có tồn kho lớn , và gom vào ít Site nhất có thể. Nhân viên xem bảng đề xuất, có thể điều chỉnh lại bằng tay. Nếu tổng tồn kho của tất cả các Site không đủ đáp ứng yêu cầu, hệ thống sẽ bật popup cảnh báo lỗi. Nhân viên bấm "Chốt phương án" để lưu lại.
[3/17/2026 8:20 PM] Hai Son Bac: 6. Lập và xuất đơn đặt hàng chính thức
Tác nhân: Bộ phận đặt hàng quốc tế.
Mô tả UI & Nghiệp vụ: Sau khi đã chốt phương án cho các mặt hàng, nhân viên mở màn hình "Phát hành đơn đặt hàng". Hệ thống tổng hợp dữ liệu theo từng Site đã chọn. Nhân viên xem trước định dạng đơn hàng gồm: mã Site, mã hàng, số lượng, đơn vị, và phương tiện vận chuyển (tàu hoặc hàng không). Nhân viên bấm "Gửi đơn hàng" để hệ thống chính thức gửi thông tin đi.
[3/17/2026 8:20 PM] Hai Son Bac: 7. Đối chiếu và ghi nhận hàng nhập kho
Tác nhân: Bộ phận quản lý kho.
Mô tả UI & Nghiệp vụ: Khi hàng được chở tới, nhân viên kho mở hệ thống và chọn mã đơn hàng. Màn hình hiển thị danh sách các mặt hàng theo đơn đã đặt. Nhân viên kiểm đếm thực tế và nhập số lượng thực nhận vào ô tương ứng. Hệ thống tự động so sánh, bôi đỏ các dòng có sự chênh lệch (thiếu hoặc thừa) giữa hàng đặt và hàng về. Nhân viên ghi chú lý do (nếu có) và bấm "Lưu xác nhận nhập kho"

 (cập nhật thông tin mới vào  [AGENTS.md](AGENTS.md). ví dụ như các loại biểu đồ, quy ước, nhiệm vụ, phân công, etc)