# Phân tích yêu cầu hệ thống quản lý xuất nhập khẩu hàng hóa

Nhóm 15

- Nguyễn Quý Đức 20235682
- Vũ Đức Hoàng Anh 20235658
- Trần Đăng Sinh 20235821
- Phan Khánh Vũ 20235880
- Lương Quốc Khánh 20235754
- Nguyễn Hữu Nhân 20235798

## 1. Biểu đồ use case

### 1.1. Biểu đồ use case tổng quan

```mermaid
graph LR
  SalesDepartment["SalesDepartment"]
  InternationalOrderingDepartment["InternationalOrderingDepartment"]
  OverseasImportSite["OverseasImportSite"]
  WarehouseManagementDepartment["WarehouseManagementDepartment"]
  SystemAdministrator["SystemAdministrator"]

  subgraph ImportAssignmentSystem ["ImportAssignmentSystem"]
    ManageImportRequests(["ManageImportRequests"])
    ManageSiteInformation(["ManageSiteInformation"])
    CollectInventoryData(["CollectInventoryData"])
    PlanImportAllocation(["PlanImportAllocation"])
    PlaceOverseasOrders(["PlaceOverseasOrders"])
    ReceiveAndReconcileGoods(["ReceiveAndReconcileGoods"])
    ManageSystem(["ManageSystem"])
  end

  SalesDepartment --> ManageImportRequests
  InternationalOrderingDepartment --> ManageImportRequests
  InternationalOrderingDepartment --> ManageSiteInformation
  InternationalOrderingDepartment --> CollectInventoryData
  InternationalOrderingDepartment --> PlanImportAllocation
  InternationalOrderingDepartment --> PlaceOverseasOrders
  OverseasImportSite --> ManageSiteInformation
  OverseasImportSite --> CollectInventoryData
  OverseasImportSite --> PlaceOverseasOrders
  WarehouseManagementDepartment --> ReceiveAndReconcileGoods
  SystemAdministrator --> ManageSystem

  PlanImportAllocation -.->|"<<include>>"| CollectInventoryData
  PlaceOverseasOrders -.->|"<<include>>"| PlanImportAllocation
  ReceiveAndReconcileGoods -.->|"<<include>>"| PlaceOverseasOrders
```

**Giải thích về các tác nhân**

- **Bộ phận bán hàng (`SalesDepartment`)**: lập và gửi danh sách yêu cầu nhập hàng (mã hàng, số lượng, đơn vị, ngày nhận mong muốn), theo dõi kết quả xử lý yêu cầu.
- **Bộ phận đặt hàng quốc tế (`InternationalOrderingDepartment`)**: tiếp nhận yêu cầu từ Sales, tìm Site phù hợp, thu thập thông tin tồn kho, ra quyết định phân bổ và gửi đơn đặt hàng đến Site.
- **Site nhập khẩu (`OverseasImportSite`)**: cung cấp thông tin tồn kho, thông tin thời gian vận chuyển (tàu/hàng không), tiếp nhận và xử lý đơn đặt hàng.
- **Bộ phận quản lý kho (`WarehouseManagementDepartment`)**: tiếp nhận hàng thực tế, kiểm hàng và đối soát với đơn đặt, cập nhật hệ thống kho nội bộ.
- **Quản trị hệ thống (`SystemAdministrator`)**: quản lý tài khoản và phân quyền actor, quản trị dữ liệu dùng chung, giám sát vận hành và sao lưu/khôi phục dữ liệu.

**Giải thích về các use case tổng quan**

Các use case mức tổng quan trong hệ thống gồm:

- **ManageImportRequests**: quản lý yêu cầu nhập hàng từ khâu tạo, gửi, theo dõi trạng thái đến điều chỉnh/hủy yêu cầu.
- **ManageSiteInformation**: quản lý thông tin Site và dữ liệu phục vụ đặt hàng (danh mục mặt hàng kinh doanh, thời gian vận chuyển theo phương thức).
- **CollectInventoryData**: gửi yêu cầu kiểm tra tồn kho tới các Site và ghi nhận phản hồi tồn kho theo từng mặt hàng.
- **PlanImportAllocation**: xác định phương án nhập hàng theo tiêu chí ưu tiên (tàu trước hàng không, tồn kho lớn hơn, số Site ít nhất) và kiểm tra khả năng đáp ứng ngày nhận mong muốn.
- **PlaceOverseasOrders**: tạo và gửi đơn đặt hàng chính thức tới các Site được chọn.
- **ReceiveAndReconcileGoods**: kiểm hàng thực nhận tại kho, đối soát với đơn đặt, ghi nhận sai lệch và cập nhật tồn kho.
- **ManageSystem**: quản trị người dùng, phân quyền, dữ liệu dùng chung, nhật ký vận hành và sao lưu/khôi phục dữ liệu.

### 1.2. Biểu đồ use case phân rã "ManageImportRequests"

```mermaid
graph LR
  SalesDepartment["SalesDepartment"]
  InternationalOrderingDepartment["InternationalOrderingDepartment"]

  subgraph ImportAssignmentSystem ["ImportAssignmentSystem"]
    ManageImportRequests(["ManageImportRequests"])
    CreateImportRequest(["CreateImportRequest"])
    ValidateImportRequestInput(["ValidateImportRequestInput"])
    SubmitImportRequest(["SubmitImportRequest"])
    TrackImportRequestStatus(["TrackImportRequestStatus"])
    UpdateImportRequest(["UpdateImportRequest"])
    CancelImportRequest(["CancelImportRequest"])
  end

  SalesDepartment --> ManageImportRequests
  InternationalOrderingDepartment --> TrackImportRequestStatus

  ManageImportRequests -.->|"<<include>>"| CreateImportRequest
  ManageImportRequests -.->|"<<include>>"| SubmitImportRequest
  ManageImportRequests -.->|"<<include>>"| TrackImportRequestStatus
  CreateImportRequest -.->|"<<include>>"| ValidateImportRequestInput
  SubmitImportRequest -.->|"<<include>>"| ValidateImportRequestInput
  UpdateImportRequest -.->|"<<extend>>"| ManageImportRequests
  CancelImportRequest -.->|"<<extend>>"| ManageImportRequests

  SalesDepartment --> UpdateImportRequest
  SalesDepartment --> CancelImportRequest
```

### 1.3. Biểu đồ use case phân rã "ManageSiteInformation"

```mermaid
graph LR
  OverseasImportSite["OverseasImportSite"]
  InternationalOrderingDepartment["InternationalOrderingDepartment"]

  subgraph ImportAssignmentSystem ["ImportAssignmentSystem"]
    ManageSiteInformation(["ManageSiteInformation"])
    UpdateSiteProfile(["UpdateSiteProfile"])
    UpdateDeliveryTimesByShipAndAir(["UpdateDeliveryTimesByShipAndAir"])
    UpdateSiteMerchandiseCatalog(["UpdateSiteMerchandiseCatalog"])
    NotifyOrderingDepartmentOfSiteChanges(["NotifyOrderingDepartmentOfSiteChanges"])
  end

  OverseasImportSite --> ManageSiteInformation
  InternationalOrderingDepartment --> ManageSiteInformation

  ManageSiteInformation -.->|"<<include>>"| UpdateSiteProfile
  ManageSiteInformation -.->|"<<include>>"| UpdateDeliveryTimesByShipAndAir
  ManageSiteInformation -.->|"<<include>>"| UpdateSiteMerchandiseCatalog
  ManageSiteInformation -.->|"<<include>>"| NotifyOrderingDepartmentOfSiteChanges
  NotifyOrderingDepartmentOfSiteChanges -.->|"<<include>>"| UpdateSiteProfile
  NotifyOrderingDepartmentOfSiteChanges -.->|"<<include>>"| UpdateDeliveryTimesByShipAndAir
  NotifyOrderingDepartmentOfSiteChanges -.->|"<<include>>"| UpdateSiteMerchandiseCatalog
```

### 1.4. Biểu đồ use case phân rã "CollectInventoryData"

```mermaid
graph LR
  InternationalOrderingDepartment["InternationalOrderingDepartment"]
  OverseasImportSite["OverseasImportSite"]

  subgraph ImportAssignmentSystem ["ImportAssignmentSystem"]
    CollectInventoryData(["CollectInventoryData"])
    FindCandidateSitesForMerchandise(["FindCandidateSitesForMerchandise"])
    FilterMerchandiseListPerSite(["FilterMerchandiseListPerSite"])
    SendStockInquiryToSite(["SendStockInquiryToSite"])
    ReceiveStockResponseFromSite(["ReceiveStockResponseFromSite"])
    PersistInventoryInformationFile(["PersistInventoryInformationFile"])
  end

  InternationalOrderingDepartment --> CollectInventoryData
  OverseasImportSite --> ReceiveStockResponseFromSite

  CollectInventoryData -.->|"<<include>>"| FindCandidateSitesForMerchandise
  FindCandidateSitesForMerchandise -.->|"<<include>>"| FilterMerchandiseListPerSite
  FilterMerchandiseListPerSite -.->|"<<include>>"| SendStockInquiryToSite
  SendStockInquiryToSite -.->|"<<include>>"| ReceiveStockResponseFromSite
  CollectInventoryData -.->|"<<include>>"| PersistInventoryInformationFile
  PersistInventoryInformationFile -.->|"<<include>>"| ReceiveStockResponseFromSite
```

### 1.5. Biểu đồ use case phân rã "PlanImportAllocation"

```mermaid
graph LR
  InternationalOrderingDepartment["InternationalOrderingDepartment"]

  subgraph ImportAssignmentSystem ["ImportAssignmentSystem"]
    PlanImportAllocation(["PlanImportAllocation"])
    ProcessEachMerchandiseIndependently(["ProcessEachMerchandiseIndependently"])
    CheckDesiredDeliveryFeasibility(["CheckDesiredDeliveryFeasibility"])
    RankSitesShipFirstHigherStockFewerSites(["RankSitesShipFirstHigherStockFewerSites"])
    SplitQuantityAcrossSelectedSites(["SplitQuantityAcrossSelectedSites"])
    ReportInsufficientSupplyError(["ReportInsufficientSupplyError"])
  end

  InternationalOrderingDepartment --> PlanImportAllocation

  PlanImportAllocation -.->|"<<include>>"| ProcessEachMerchandiseIndependently
  ProcessEachMerchandiseIndependently -.->|"<<include>>"| CheckDesiredDeliveryFeasibility
  CheckDesiredDeliveryFeasibility -.->|"<<include>>"| RankSitesShipFirstHigherStockFewerSites
  RankSitesShipFirstHigherStockFewerSites -.->|"<<include>>"| SplitQuantityAcrossSelectedSites
  ReportInsufficientSupplyError -.->|"<<extend>>"| PlanImportAllocation
```

### 1.6. Biểu đồ use case phân rã "PlaceOverseasOrders"

```mermaid
graph LR
  InternationalOrderingDepartment["InternationalOrderingDepartment"]
  OverseasImportSite["OverseasImportSite"]

  subgraph ImportAssignmentSystem ["ImportAssignmentSystem"]
    PlaceOverseasOrders(["PlaceOverseasOrders"])
    ComposeOrderLines(["ComposeOrderLines"])
    SetDeliveryMeansShipOrAir(["SetDeliveryMeansShipOrAir"])
    TransmitOrdersToSelectedSites(["TransmitOrdersToSelectedSites"])
    AcknowledgeOrderAtSite(["AcknowledgeOrderAtSite"])
  end

  InternationalOrderingDepartment --> PlaceOverseasOrders
  OverseasImportSite --> AcknowledgeOrderAtSite

  PlaceOverseasOrders -.->|"<<include>>"| ComposeOrderLines
  PlaceOverseasOrders -.->|"<<include>>"| TransmitOrdersToSelectedSites
  ComposeOrderLines -.->|"<<include>>"| SetDeliveryMeansShipOrAir
  TransmitOrdersToSelectedSites -.->|"<<include>>"| SetDeliveryMeansShipOrAir
  TransmitOrdersToSelectedSites -.->|"<<include>>"| AcknowledgeOrderAtSite
```

### 1.7. Biểu đồ use case phân rã "ReceiveAndReconcileGoods"

```mermaid
graph LR
  WarehouseManagementDepartment["WarehouseManagementDepartment"]
  InternationalOrderingDepartment["InternationalOrderingDepartment"]

  subgraph ImportAssignmentSystem ["ImportAssignmentSystem"]
    ReceiveAndReconcileGoods(["ReceiveAndReconcileGoods"])
    LoadExpectedOrderQuantities(["LoadExpectedOrderQuantities"])
    RecordGoodsReceipt(["RecordGoodsReceipt"])
    CompareReceivedVsOrdered(["CompareReceivedVsOrdered"])
    RecordQuantityDiscrepancy(["RecordQuantityDiscrepancy"])
    UpdateInternalWarehouseStock(["UpdateInternalWarehouseStock"])
  end

  WarehouseManagementDepartment --> ReceiveAndReconcileGoods
  InternationalOrderingDepartment --> LoadExpectedOrderQuantities

  ReceiveAndReconcileGoods -.->|"<<include>>"| LoadExpectedOrderQuantities
  ReceiveAndReconcileGoods -.->|"<<include>>"| RecordGoodsReceipt
  ReceiveAndReconcileGoods -.->|"<<include>>"| CompareReceivedVsOrdered
  ReceiveAndReconcileGoods -.->|"<<include>>"| UpdateInternalWarehouseStock
  CompareReceivedVsOrdered -.->|"<<include>>"| LoadExpectedOrderQuantities
  UpdateInternalWarehouseStock -.->|"<<include>>"| CompareReceivedVsOrdered
  RecordQuantityDiscrepancy -.->|"<<extend>>"| CompareReceivedVsOrdered

  WarehouseManagementDepartment --> RecordGoodsReceipt
  WarehouseManagementDepartment --> RecordQuantityDiscrepancy
  WarehouseManagementDepartment --> UpdateInternalWarehouseStock
```

### 1.8. Biểu đồ use case phân rã "ManageSystem"

```mermaid
graph LR
  SystemAdministrator["SystemAdministrator"]

  subgraph ImportAssignmentSystem ["ImportAssignmentSystem"]
    ManageSystem(["ManageSystem"])
    ManageUsersAndActorRoles(["ManageUsersAndActorRoles"])
    ManageSharedMasterData(["ManageSharedMasterData"])
    ViewCrossDepartmentOperationLogs(["ViewCrossDepartmentOperationLogs"])
    BackupAndRestoreBusinessData(["BackupAndRestoreBusinessData"])
  end

  SystemAdministrator --> ManageSystem
  ManageSystem -.->|"<<include>>"| ManageUsersAndActorRoles
  ManageSystem -.->|"<<include>>"| ManageSharedMasterData
  ManageSystem -.->|"<<include>>"| ViewCrossDepartmentOperationLogs
  ManageSystem -.->|"<<include>>"| BackupAndRestoreBusinessData
```

## 2. Đặc tả Use case

Các đặc tả chi tiết được lập theo mẫu bảng HTML thống nhất trong `AGENTS.md`. Quy ước mã use case từ `UC-001` đến `UC-007` tương ứng với các use case tổng quan của hệ thống.

### 2.1. Use case "ManageImportRequests" (UC-001)

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
    <td>UC-001</td>
    <td><strong>Tên UseCase</strong></td>
    <td>ManageImportRequests</td>
  </tr>
  <tr>
    <td><strong>Tác nhân</strong></td>
    <td colspan="3">SalesDepartment, InternationalOrderingDepartment</td>
  </tr>
  <tr>
    <td><strong>Tiền điều kiện</strong></td>
    <td colspan="3">SalesDepartment đã đăng nhập và có quyền tạo yêu cầu nhập; danh mục mặt hàng và đơn vị tính đã tồn tại trong hệ thống.</td>
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
          <td>SalesDepartment</td>
          <td>Tạo yêu cầu nhập hàng mới và nhập danh sách mặt hàng cần đặt.</td>
        </tr>
        <tr>
          <td>2</td>
          <td>System</td>
          <td>Kiểm tra hợp lệ dữ liệu đầu vào (mã hàng, số lượng, đơn vị, ngày nhận mong muốn).</td>
        </tr>
        <tr>
          <td>3</td>
          <td>SalesDepartment</td>
          <td>Xác nhận gửi yêu cầu nhập sang InternationalOrderingDepartment.</td>
        </tr>
        <tr>
          <td>4</td>
          <td>System</td>
          <td>Ghi nhận yêu cầu ở trạng thái Submitted và phát sinh mã yêu cầu.</td>
        </tr>
        <tr>
          <td>5</td>
          <td>InternationalOrderingDepartment</td>
          <td>Tra cứu và theo dõi trạng thái xử lý của yêu cầu nhập.</td>
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
          <td>2a</td>
          <td>System</td>
          <td>Dữ liệu không hợp lệ, hệ thống hiển thị lỗi và yêu cầu nhập lại trước khi gửi.</td>
        </tr>
        <tr>
          <td>4a</td>
          <td>SalesDepartment</td>
          <td>Cập nhật yêu cầu khi trạng thái chưa bị khóa xử lý.</td>
        </tr>
        <tr>
          <td>4b</td>
          <td>SalesDepartment</td>
          <td>Hủy yêu cầu khi nhu cầu thay đổi, hệ thống chuyển trạng thái Cancelled.</td>
        </tr>
      </table>
    </td>
  </tr>
  <tr>
    <td><strong>Hậu điều kiện</strong></td>
    <td colspan="3">Yêu cầu nhập được lưu với mã định danh và trạng thái phù hợp (Submitted/Updated/Cancelled), sẵn sàng cho các bước xử lý tiếp theo.</td>
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
          <td>merchandiseCode</td>
          <td>Mã hàng cần nhập</td>
          <td>Có</td>
          <td>Tồn tại trong danh mục mặt hàng</td>
          <td>MH-001</td>
        </tr>
        <tr>
          <td>2</td>
          <td>quantityOrdered</td>
          <td>Số lượng cần nhập</td>
          <td>Có</td>
          <td>Số nguyên dương lớn hơn 0</td>
          <td>500</td>
        </tr>
        <tr>
          <td>3</td>
          <td>unit</td>
          <td>Đơn vị tính của số lượng nhập</td>
          <td>Có</td>
          <td>Thuộc danh mục đơn vị hợp lệ</td>
          <td>box</td>
        </tr>
        <tr>
          <td>4</td>
          <td>desiredDeliveryDate</td>
          <td>Ngày nhận mong muốn (year/month/date)</td>
          <td>Có</td>
          <td>Là ngày hợp lệ và không nhỏ hơn ngày hiện tại</td>
          <td>2026-05-20</td>
        </tr>
      </table>
    </td>
  </tr>
</table>

### 2.2. Use case "ManageSiteInformation" (UC-002)

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
    <td>UC-002</td>
    <td><strong>Tên UseCase</strong></td>
    <td>ManageSiteInformation</td>
  </tr>
  <tr>
    <td><strong>Tác nhân</strong></td>
    <td colspan="3">OverseasImportSite, InternationalOrderingDepartment</td>
  </tr>
  <tr>
    <td><strong>Tiền điều kiện</strong></td>
    <td colspan="3">Thông tin Site đã được khởi tạo trên hệ thống và OverseasImportSite có quyền cập nhật dữ liệu Site.</td>
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
          <td>OverseasImportSite</td>
          <td>Mở chức năng quản lý thông tin Site và chọn hồ sơ Site cần cập nhật.</td>
        </tr>
        <tr>
          <td>2</td>
          <td>OverseasImportSite</td>
          <td>Cập nhật hồ sơ Site (tên Site, thông tin vận hành liên quan).</td>
        </tr>
        <tr>
          <td>3</td>
          <td>OverseasImportSite</td>
          <td>Cập nhật thời gian vận chuyển theo tàu và hàng không.</td>
        </tr>
        <tr>
          <td>4</td>
          <td>OverseasImportSite</td>
          <td>Cập nhật danh mục mặt hàng mà Site đang kinh doanh.</td>
        </tr>
        <tr>
          <td>5</td>
          <td>System</td>
          <td>Lưu thay đổi và gửi thông báo cập nhật cho InternationalOrderingDepartment.</td>
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
          <td>3a</td>
          <td>System</td>
          <td>Thời gian vận chuyển không hợp lệ, hệ thống từ chối lưu và yêu cầu nhập lại.</td>
        </tr>
        <tr>
          <td>4a</td>
          <td>System</td>
          <td>Mặt hàng cập nhật không thuộc danh mục cho phép, hệ thống cảnh báo và giữ dữ liệu cũ.</td>
        </tr>
      </table>
    </td>
  </tr>
  <tr>
    <td><strong>Hậu điều kiện</strong></td>
    <td colspan="3">Thông tin Site, thời gian vận chuyển và danh mục hàng kinh doanh được cập nhật đồng bộ, InternationalOrderingDepartment nhận được thông báo thay đổi.</td>
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
          <td>siteCode</td>
          <td>Mã định danh Site</td>
          <td>Có</td>
          <td>Tồn tại và còn hiệu lực trong hệ thống</td>
          <td>SITE-SEA-01</td>
        </tr>
        <tr>
          <td>2</td>
          <td>deliveryDaysByShip</td>
          <td>Số ngày vận chuyển bằng tàu</td>
          <td>Có</td>
          <td>Số nguyên dương</td>
          <td>18</td>
        </tr>
        <tr>
          <td>3</td>
          <td>deliveryDaysByAir</td>
          <td>Số ngày vận chuyển bằng hàng không</td>
          <td>Có</td>
          <td>Số nguyên dương</td>
          <td>5</td>
        </tr>
        <tr>
          <td>4</td>
          <td>merchandiseCatalog</td>
          <td>Danh sách mặt hàng Site kinh doanh</td>
          <td>Có</td>
          <td>Mỗi mặt hàng có mã hợp lệ, không trùng lặp trong danh sách</td>
          <td>MH-001, MH-005, MH-014</td>
        </tr>
      </table>
    </td>
  </tr>
</table>

### 2.3. Use case "CollectInventoryData" (UC-003)

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
    <td>UC-003</td>
    <td><strong>Tên UseCase</strong></td>
    <td>CollectInventoryData</td>
  </tr>
  <tr>
    <td><strong>Tác nhân</strong></td>
    <td colspan="3">InternationalOrderingDepartment, OverseasImportSite</td>
  </tr>
  <tr>
    <td><strong>Tiền điều kiện</strong></td>
    <td colspan="3">Yêu cầu nhập đã được gửi hợp lệ; dữ liệu Site và danh mục mặt hàng kinh doanh của từng Site đã sẵn có.</td>
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
          <td>InternationalOrderingDepartment</td>
          <td>Tìm các Site có kinh doanh ít nhất một mặt hàng trong danh sách yêu cầu.</td>
        </tr>
        <tr>
          <td>2</td>
          <td>System</td>
          <td>Lọc danh sách mặt hàng theo từng Site để tạo gói truy vấn tồn kho riêng.</td>
        </tr>
        <tr>
          <td>3</td>
          <td>InternationalOrderingDepartment</td>
          <td>Gửi yêu cầu kiểm tra tồn kho đến từng OverseasImportSite.</td>
        </tr>
        <tr>
          <td>4</td>
          <td>OverseasImportSite</td>
          <td>Phản hồi số lượng tồn kho theo từng mặt hàng được yêu cầu.</td>
        </tr>
        <tr>
          <td>5</td>
          <td>System</td>
          <td>Ghi dữ liệu phản hồi vào tệp thông tin kho theo cấu trúc chuẩn.</td>
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
          <td>1a</td>
          <td>System</td>
          <td>Không tìm thấy Site phù hợp cho mặt hàng, hệ thống ghi nhận mặt hàng chưa có nguồn cung.</td>
        </tr>
        <tr>
          <td>4a</td>
          <td>OverseasImportSite</td>
          <td>Site không có hàng, phản hồi inStockQuantity = 0 cho mặt hàng tương ứng.</td>
        </tr>
        <tr>
          <td>4b</td>
          <td>System</td>
          <td>Site phản hồi chậm, hệ thống ghi log timeout và cho phép gửi lại yêu cầu.</td>
        </tr>
      </table>
    </td>
  </tr>
  <tr>
    <td><strong>Hậu điều kiện</strong></td>
    <td colspan="3">Tệp thông tin kho được cập nhật với dữ liệu tồn kho mới nhất theo từng Site và từng mặt hàng để phục vụ phân bổ nhập hàng.</td>
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
          <td>siteCode</td>
          <td>Mã Site nhận yêu cầu tồn kho</td>
          <td>Có</td>
          <td>Tồn tại trong tệp thông tin Site</td>
          <td>SITE-SEA-01</td>
        </tr>
        <tr>
          <td>2</td>
          <td>merchandiseCode</td>
          <td>Mã mặt hàng cần kiểm tra tồn kho</td>
          <td>Có</td>
          <td>Tồn tại trong danh mục mặt hàng</td>
          <td>MH-001</td>
        </tr>
        <tr>
          <td>3</td>
          <td>inStockQuantity</td>
          <td>Số lượng tồn kho Site phản hồi</td>
          <td>Có</td>
          <td>Số nguyên không âm</td>
          <td>320</td>
        </tr>
        <tr>
          <td>4</td>
          <td>unit</td>
          <td>Đơn vị của số lượng tồn kho</td>
          <td>Có</td>
          <td>Khớp đơn vị chuẩn của mặt hàng</td>
          <td>box</td>
        </tr>
      </table>
    </td>
  </tr>
</table>

### 2.4. Use case "PlanImportAllocation" (UC-004)

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
    <td>UC-004</td>
    <td><strong>Tên UseCase</strong></td>
    <td>PlanImportAllocation</td>
  </tr>
  <tr>
    <td><strong>Tác nhân</strong></td>
    <td colspan="3">InternationalOrderingDepartment</td>
  </tr>
  <tr>
    <td><strong>Tiền điều kiện</strong></td>
    <td colspan="3">Đã có dữ liệu yêu cầu nhập và dữ liệu tồn kho theo Site; thông tin thời gian vận chuyển ship/air của Site đã cập nhật.</td>
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
          <td>InternationalOrderingDepartment</td>
          <td>Khởi tạo lập kế hoạch phân bổ cho yêu cầu nhập hiện tại.</td>
        </tr>
        <tr>
          <td>2</td>
          <td>System</td>
          <td>Xử lý từng mặt hàng một cách độc lập.</td>
        </tr>
        <tr>
          <td>3</td>
          <td>System</td>
          <td>Kiểm tra Site có thể đáp ứng ngày nhận mong muốn theo thời gian vận chuyển.</td>
        </tr>
        <tr>
          <td>4</td>
          <td>System</td>
          <td>Xếp hạng Site theo ưu tiên: ship trước, tồn kho lớn hơn, số Site ít hơn.</td>
        </tr>
        <tr>
          <td>5</td>
          <td>System</td>
          <td>Phân bổ số lượng đặt cho một hoặc nhiều Site đến khi đáp ứng đủ nhu cầu.</td>
        </tr>
        <tr>
          <td>6</td>
          <td>InternationalOrderingDepartment</td>
          <td>Xem và xác nhận phương án phân bổ nhập hàng.</td>
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
          <td>3a</td>
          <td>System</td>
          <td>Không có Site nào đáp ứng ngày nhận mong muốn, hệ thống đánh dấu mặt hàng rủi ro trễ hạn.</td>
        </tr>
        <tr>
          <td>5a</td>
          <td>System</td>
          <td>Tổng tồn kho khả dụng nhỏ hơn nhu cầu, hệ thống phát sinh lỗi thiếu nguồn cung.</td>
        </tr>
        <tr>
          <td>6a</td>
          <td>InternationalOrderingDepartment</td>
          <td>Không chấp nhận phương án, điều chỉnh tham số ưu tiên và chạy lại phân bổ.</td>
        </tr>
      </table>
    </td>
  </tr>
  <tr>
    <td><strong>Hậu điều kiện</strong></td>
    <td colspan="3">Phương án phân bổ được lưu theo từng mặt hàng và từng Site, kèm phương thức vận chuyển dự kiến; các lỗi thiếu nguồn cung được ghi nhận nếu có.</td>
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
          <td>desiredDeliveryDate</td>
          <td>Ngày nhận mong muốn của từng mặt hàng</td>
          <td>Có</td>
          <td>Là ngày hợp lệ</td>
          <td>2026-06-01</td>
        </tr>
        <tr>
          <td>2</td>
          <td>availableSites</td>
          <td>Danh sách Site có hàng cho mặt hàng</td>
          <td>Có</td>
          <td>Mỗi Site có siteCode hợp lệ và tồn kho đi kèm</td>
          <td>SITE-SEA-01, SITE-EU-04</td>
        </tr>
        <tr>
          <td>3</td>
          <td>inStockQuantityBySite</td>
          <td>Số lượng tồn kho theo từng Site</td>
          <td>Có</td>
          <td>Số nguyên không âm</td>
          <td>320, 150</td>
        </tr>
        <tr>
          <td>4</td>
          <td>deliveryDaysByShipOrAir</td>
          <td>Số ngày vận chuyển theo từng phương thức của Site</td>
          <td>Có</td>
          <td>Số nguyên dương</td>
          <td>ship=18, air=5</td>
        </tr>
      </table>
    </td>
  </tr>
</table>

### 2.5. Use case "PlaceOverseasOrders" (UC-005)

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
    <td>UC-005</td>
    <td><strong>Tên UseCase</strong></td>
    <td>PlaceOverseasOrders</td>
  </tr>
  <tr>
    <td><strong>Tác nhân</strong></td>
    <td colspan="3">InternationalOrderingDepartment, OverseasImportSite</td>
  </tr>
  <tr>
    <td><strong>Tiền điều kiện</strong></td>
    <td colspan="3">Phương án phân bổ nhập hàng đã được xác nhận và có danh sách Site được chọn cho từng mặt hàng.</td>
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
          <td>InternationalOrderingDepartment</td>
          <td>Tạo đơn đặt hàng quốc tế từ kết quả phân bổ.</td>
        </tr>
        <tr>
          <td>2</td>
          <td>System</td>
          <td>Tạo các dòng đặt hàng theo từng Site và từng mặt hàng.</td>
        </tr>
        <tr>
          <td>3</td>
          <td>System</td>
          <td>Gán phương thức vận chuyển cho từng dòng (ship delivery hoặc air delivery).</td>
        </tr>
        <tr>
          <td>4</td>
          <td>InternationalOrderingDepartment</td>
          <td>Xác nhận gửi đơn đặt hàng đến các Site đã chọn.</td>
        </tr>
        <tr>
          <td>5</td>
          <td>OverseasImportSite</td>
          <td>Nhận đơn và xác nhận tiếp nhận xử lý đơn đặt hàng.</td>
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
          <td>3a</td>
          <td>System</td>
          <td>Không xác định được phương thức vận chuyển hợp lệ, hệ thống chặn gửi đơn và yêu cầu kiểm tra lại phân bổ.</td>
        </tr>
        <tr>
          <td>5a</td>
          <td>OverseasImportSite</td>
          <td>Site từ chối đơn do thay đổi năng lực cung ứng, hệ thống chuyển đơn sang trạng thái Rejected và báo về bộ phận đặt hàng.</td>
        </tr>
      </table>
    </td>
  </tr>
  <tr>
    <td><strong>Hậu điều kiện</strong></td>
    <td colspan="3">Đơn đặt hàng quốc tế được ghi nhận, gửi đến Site và cập nhật trạng thái tiếp nhận/từ chối theo phản hồi thực tế.</td>
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
          <td>siteCode</td>
          <td>Mã Site nhận đơn đặt hàng</td>
          <td>Có</td>
          <td>Nằm trong danh sách Site đã chọn từ bước phân bổ</td>
          <td>SITE-EU-04</td>
        </tr>
        <tr>
          <td>2</td>
          <td>merchandiseCode</td>
          <td>Mã mặt hàng được đặt</td>
          <td>Có</td>
          <td>Khớp với yêu cầu nhập đã duyệt</td>
          <td>MH-005</td>
        </tr>
        <tr>
          <td>3</td>
          <td>quantityOrdered</td>
          <td>Số lượng đặt theo từng dòng đơn</td>
          <td>Có</td>
          <td>Số nguyên dương và không vượt quá phân bổ đã duyệt</td>
          <td>150</td>
        </tr>
        <tr>
          <td>4</td>
          <td>deliveryMeans</td>
          <td>Phương thức vận chuyển</td>
          <td>Có</td>
          <td>Chỉ nhận ship delivery hoặc air delivery</td>
          <td>ship delivery</td>
        </tr>
      </table>
    </td>
  </tr>
</table>

### 2.6. Use case "ReceiveAndReconcileGoods" (UC-006)

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
    <td>UC-006</td>
    <td><strong>Tên UseCase</strong></td>
    <td>ReceiveAndReconcileGoods</td>
  </tr>
  <tr>
    <td><strong>Tác nhân</strong></td>
    <td colspan="3">WarehouseManagementDepartment, InternationalOrderingDepartment</td>
  </tr>
  <tr>
    <td><strong>Tiền điều kiện</strong></td>
    <td colspan="3">Đơn đặt hàng đã phát hành cho Site; WarehouseManagementDepartment có quyền tiếp nhận và kiểm hàng.</td>
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
          <td>InternationalOrderingDepartment</td>
          <td>Cung cấp dữ liệu số lượng đặt kỳ vọng cho WarehouseManagementDepartment.</td>
        </tr>
        <tr>
          <td>2</td>
          <td>WarehouseManagementDepartment</td>
          <td>Ghi nhận thông tin lô hàng thực tế khi hàng về kho.</td>
        </tr>
        <tr>
          <td>3</td>
          <td>System</td>
          <td>So sánh số lượng thực nhận với số lượng đã đặt theo từng mặt hàng.</td>
        </tr>
        <tr>
          <td>4</td>
          <td>WarehouseManagementDepartment</td>
          <td>Xác nhận kết quả kiểm nhận sau khi đối soát.</td>
        </tr>
        <tr>
          <td>5</td>
          <td>System</td>
          <td>Cập nhật tồn kho nội bộ theo số lượng thực nhận đã xác nhận.</td>
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
          <td>3a</td>
          <td>System</td>
          <td>Phát hiện sai lệch giữa thực nhận và đặt hàng, hệ thống mở bước ghi nhận discrepancy.</td>
        </tr>
        <tr>
          <td>3b</td>
          <td>WarehouseManagementDepartment</td>
          <td>Ghi chi tiết sai lệch (thiếu/thừa/hư hỏng) và nguyên nhân quan sát được.</td>
        </tr>
        <tr>
          <td>5a</td>
          <td>System</td>
          <td>Nếu sai lệch vượt ngưỡng, hệ thống gửi cảnh báo cho InternationalOrderingDepartment để xử lý tiếp.</td>
        </tr>
      </table>
    </td>
  </tr>
  <tr>
    <td><strong>Hậu điều kiện</strong></td>
    <td colspan="3">Kết quả kiểm nhận và đối soát được lưu đầy đủ; tồn kho nội bộ được cập nhật; các sai lệch được ghi nhận và cảnh báo theo quy định.</td>
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
          <td>orderReference</td>
          <td>Mã đơn đặt hàng cần kiểm nhận</td>
          <td>Có</td>
          <td>Tồn tại và ở trạng thái đã giao/đang giao</td>
          <td>PO-2026-0008</td>
        </tr>
        <tr>
          <td>2</td>
          <td>receivedQuantity</td>
          <td>Số lượng thực nhận theo mặt hàng</td>
          <td>Có</td>
          <td>Số nguyên không âm</td>
          <td>145</td>
        </tr>
        <tr>
          <td>3</td>
          <td>orderedQuantity</td>
          <td>Số lượng đã đặt theo đơn</td>
          <td>Có</td>
          <td>Số nguyên dương</td>
          <td>150</td>
        </tr>
        <tr>
          <td>4</td>
          <td>discrepancyNote</td>
          <td>Ghi chú sai lệch nếu có</td>
          <td>Không</td>
          <td>Bắt buộc khi receivedQuantity khác orderedQuantity</td>
          <td>Thiếu 5 đơn vị do hư hỏng vận chuyển</td>
        </tr>
      </table>
    </td>
  </tr>
</table>

### 2.7. Use case "ManageSystem" (UC-007)

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
    <td>UC-007</td>
    <td><strong>Tên UseCase</strong></td>
    <td>ManageSystem</td>
  </tr>
  <tr>
    <td><strong>Tác nhân</strong></td>
    <td colspan="3">SystemAdministrator</td>
  </tr>
  <tr>
    <td><strong>Tiền điều kiện</strong></td>
    <td colspan="3">SystemAdministrator đã đăng nhập với quyền quản trị cao nhất.</td>
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
          <td>SystemAdministrator</td>
          <td>Quản lý người dùng và vai trò cho các actor nghiệp vụ.</td>
        </tr>
        <tr>
          <td>2</td>
          <td>SystemAdministrator</td>
          <td>Quản lý dữ liệu master dùng chung (mặt hàng, đơn vị, Site, trạng thái nghiệp vụ).</td>
        </tr>
        <tr>
          <td>3</td>
          <td>SystemAdministrator</td>
          <td>Theo dõi nhật ký vận hành và thao tác liên phòng ban để truy vết.</td>
        </tr>
        <tr>
          <td>4</td>
          <td>SystemAdministrator</td>
          <td>Thiết lập lịch sao lưu dữ liệu nghiệp vụ.</td>
        </tr>
        <tr>
          <td>5</td>
          <td>System</td>
          <td>Thực thi sao lưu/khôi phục theo yêu cầu và ghi log kết quả.</td>
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
          <td>1a</td>
          <td>System</td>
          <td>Dữ liệu tài khoản hoặc phân quyền không hợp lệ, hệ thống từ chối cập nhật và báo lỗi chi tiết.</td>
        </tr>
        <tr>
          <td>5a</td>
          <td>System</td>
          <td>Khôi phục thất bại do bản sao lưu lỗi, hệ thống giữ nguyên trạng thái hiện tại và cảnh báo quản trị.</td>
        </tr>
      </table>
    </td>
  </tr>
  <tr>
    <td><strong>Hậu điều kiện</strong></td>
    <td colspan="3">Cấu hình hệ thống, dữ liệu master, phân quyền và bản ghi vận hành được cập nhật đầy đủ; hoạt động sao lưu/khôi phục có nhật ký truy vết.</td>
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
          <td>userAccount</td>
          <td>Thông tin tài khoản người dùng nội bộ</td>
          <td>Có</td>
          <td>Tên đăng nhập duy nhất, email hợp lệ, trạng thái rõ ràng</td>
          <td>iord.nguyenvana</td>
        </tr>
        <tr>
          <td>2</td>
          <td>actorRole</td>
          <td>Vai trò nghiệp vụ được gán cho tài khoản</td>
          <td>Có</td>
          <td>Nằm trong tập vai trò đã định nghĩa</td>
          <td>InternationalOrderingDepartment</td>
        </tr>
        <tr>
          <td>3</td>
          <td>sharedMasterData</td>
          <td>Dữ liệu danh mục dùng chung</td>
          <td>Có</td>
          <td>Bản ghi không trùng khóa và đúng định dạng</td>
          <td>unit=box, merchandiseCode=MH-001</td>
        </tr>
        <tr>
          <td>4</td>
          <td>backupSchedule</td>
          <td>Lịch sao lưu dữ liệu</td>
          <td>Có</td>
          <td>Có tần suất và thời điểm hợp lệ</td>
          <td>Daily 23:00</td>
        </tr>
      </table>
    </td>
  </tr>
</table>

## 3. Thiết kế màn hình

### 3.1. UC-001 - ManageImportRequests

<table border="1" cellpadding="6" cellspacing="0" style="border-collapse: collapse; width: 100%;">
  <tr>
    <th>STT</th>
    <th>Mã màn hình</th>
    <th>Tên màn hình</th>
    <th>Chức năng chính</th>
  </tr>
  <tr><td>1</td><td>SC-001</td><td>ManageImportRequestsScreen</td><td>Màn hình tổng hợp để tạo/sửa/hủy yêu cầu nhập và theo dõi trạng thái xử lý.</td></tr>
  <tr><td>2</td><td>SC-001-01</td><td>CreateImportRequestScreen</td><td>Nhập thông tin yêu cầu nhập theo mặt hàng, số lượng, ngày nhận mong muốn.</td></tr>
  <tr><td>3</td><td>SC-001-02</td><td>ValidateImportRequestInputDialog</td><td>Kiểm tra hợp lệ dữ liệu và cảnh báo lỗi trước khi gửi yêu cầu.</td></tr>
  <tr><td>4</td><td>SC-001-03</td><td>SubmitImportRequestConfirmation</td><td>Xác nhận gửi yêu cầu và hiển thị mã yêu cầu sau khi gửi thành công.</td></tr>
  <tr><td>5</td><td>SC-001-04</td><td>TrackImportRequestStatusScreen</td><td>Theo dõi trạng thái yêu cầu nhập theo từng mốc xử lý.</td></tr>
  <tr><td>6</td><td>SC-001-05</td><td>UpdateImportRequestScreen</td><td>Cập nhật thông tin yêu cầu nhập còn hiệu lực.</td></tr>
  <tr><td>7</td><td>SC-001-06</td><td>CancelImportRequestDialog</td><td>Hủy yêu cầu nhập và ghi nhận lý do hủy.</td></tr>
</table>

### 3.2. UC-002 - ManageSiteInformation

<table border="1" cellpadding="6" cellspacing="0" style="border-collapse: collapse; width: 100%;">
  <tr>
    <th>STT</th>
    <th>Mã màn hình</th>
    <th>Tên màn hình</th>
    <th>Chức năng chính</th>
  </tr>
  <tr><td>1</td><td>SC-002</td><td>ManageSiteInformationScreen</td><td>Quản lý thông tin Site nhập khẩu, danh mục hàng và tham số vận chuyển.</td></tr>
  <tr><td>2</td><td>SC-002-01</td><td>UpdateSiteProfileScreen</td><td>Cập nhật hồ sơ Site gồm thông tin định danh, liên hệ, khu vực, trạng thái hoạt động.</td></tr>
  <tr><td>3</td><td>SC-002-02</td><td>UpdateDeliveryTimesByShipAndAirScreen</td><td>Cập nhật thời gian vận chuyển theo phương thức ship/air cho từng Site.</td></tr>
  <tr><td>4</td><td>SC-002-03</td><td>UpdateSiteMerchandiseCatalogScreen</td><td>Cập nhật danh mục mặt hàng Site có thể cung cấp.</td></tr>
  <tr><td>5</td><td>SC-002-04</td><td>NotifyOrderingDepartmentOfSiteChangesDialog</td><td>Gửi thông báo thay đổi thông tin Site tới bộ phận đặt hàng quốc tế.</td></tr>
</table>

### 3.3. UC-003 - CollectInventoryData

<table border="1" cellpadding="6" cellspacing="0" style="border-collapse: collapse; width: 100%;">
  <tr>
    <th>STT</th>
    <th>Mã màn hình</th>
    <th>Tên màn hình</th>
    <th>Chức năng chính</th>
  </tr>
  <tr><td>1</td><td>SC-003</td><td>CollectInventoryDataScreen</td><td>Điều phối quá trình thu thập tồn kho từ nhiều Site cho danh sách mặt hàng cần nhập.</td></tr>
  <tr><td>2</td><td>SC-003-01</td><td>FindCandidateSitesForMerchandiseScreen</td><td>Lọc và hiển thị danh sách Site ứng viên theo từng mặt hàng cần nhập.</td></tr>
  <tr><td>3</td><td>SC-003-02</td><td>FilterMerchandiseListPerSiteScreen</td><td>Phân nhóm mặt hàng gửi hỏi tồn kho theo từng Site phù hợp.</td></tr>
  <tr><td>4</td><td>SC-003-03</td><td>SendStockInquiryToSiteScreen</td><td>Tạo và gửi yêu cầu xác nhận tồn kho đến các Site đã chọn.</td></tr>
  <tr><td>5</td><td>SC-003-04</td><td>ReceiveStockResponseFromSiteScreen</td><td>Nhận phản hồi tồn kho từ Site và hiển thị trạng thái phản hồi.</td></tr>
  <tr><td>6</td><td>SC-003-05</td><td>PersistInventoryInformationFileScreen</td><td>Lưu dữ liệu tồn kho tổng hợp phục vụ bước phân bổ.</td></tr>
</table>

### 3.4. UC-004 - PlanImportAllocation

<table border="1" cellpadding="6" cellspacing="0" style="border-collapse: collapse; width: 100%;">
  <tr>
    <th>STT</th>
    <th>Mã màn hình</th>
    <th>Tên màn hình</th>
    <th>Chức năng chính</th>
  </tr>
  <tr><td>1</td><td>SC-004</td><td>PlanImportAllocationScreen</td><td>Lập và xem phương án phân bổ nhập hàng theo ưu tiên giao hàng và năng lực Site.</td></tr>
  <tr><td>2</td><td>SC-004-01</td><td>ProcessEachMerchandiseIndependentlyPanel</td><td>Xử lý phân bổ độc lập theo từng mặt hàng.</td></tr>
  <tr><td>3</td><td>SC-004-02</td><td>CheckDesiredDeliveryFeasibilityPanel</td><td>Đánh giá khả năng đáp ứng ngày nhận mong muốn.</td></tr>
  <tr><td>4</td><td>SC-004-03</td><td>RankSitesPanel</td><td>Xếp hạng Site theo tiêu chí ship trước, tồn kho cao hơn, ít Site hơn.</td></tr>
  <tr><td>5</td><td>SC-004-04</td><td>SplitQuantityAcrossSelectedSitesPanel</td><td>Phân tách số lượng đặt sang các Site được chọn.</td></tr>
  <tr><td>6</td><td>SC-004-05</td><td>InsufficientSupplyErrorDialog</td><td>Thông báo lỗi thiếu nguồn cung khi tổng tồn kho không đủ.</td></tr>
</table>

### 3.5. UC-005 - PlaceOverseasOrders

<table border="1" cellpadding="6" cellspacing="0" style="border-collapse: collapse; width: 100%;">
  <tr>
    <th>STT</th>
    <th>Mã màn hình</th>
    <th>Tên màn hình</th>
    <th>Chức năng chính</th>
  </tr>
  <tr><td>1</td><td>SC-005</td><td>PlaceOverseasOrdersScreen</td><td>Tạo và gửi đơn đặt hàng quốc tế dựa trên phương án phân bổ đã xác nhận.</td></tr>
  <tr><td>2</td><td>SC-005-01</td><td>ComposeOrderLinesScreen</td><td>Soạn các dòng đơn hàng theo mặt hàng, số lượng, Site nhận đơn.</td></tr>
  <tr><td>3</td><td>SC-005-02</td><td>SetDeliveryMeansShipOrAirDialog</td><td>Chọn phương thức giao hàng ship/air cho từng dòng đơn.</td></tr>
  <tr><td>4</td><td>SC-005-03</td><td>TransmitOrdersToSelectedSitesScreen</td><td>Gửi đơn tới các Site được chọn và theo dõi trạng thái gửi.</td></tr>
  <tr><td>5</td><td>SC-005-04</td><td>AcknowledgeOrderAtSiteScreen</td><td>Tiếp nhận xác nhận từ Site về đơn đã nhận xử lý.</td></tr>
</table>

### 3.6. UC-006 - ReceiveAndReconcileGoods

<table border="1" cellpadding="6" cellspacing="0" style="border-collapse: collapse; width: 100%;">
  <tr>
    <th>STT</th>
    <th>Mã màn hình</th>
    <th>Tên màn hình</th>
    <th>Chức năng chính</th>
  </tr>
  <tr><td>1</td><td>SC-006</td><td>ReceiveAndReconcileGoodsScreen</td><td>Tiếp nhận hàng, đối soát với đơn đặt hàng và cập nhật tồn kho nội bộ.</td></tr>
  <tr><td>2</td><td>SC-006-01</td><td>LoadExpectedOrderQuantitiesScreen</td><td>Nạp dữ liệu số lượng dự kiến theo đơn hàng để làm chuẩn đối soát.</td></tr>
  <tr><td>3</td><td>SC-006-02</td><td>RecordGoodsReceiptScreen</td><td>Ghi nhận số lượng thực nhận theo từng mặt hàng/đợt nhận hàng.</td></tr>
  <tr><td>4</td><td>SC-006-03</td><td>CompareReceivedVsOrderedScreen</td><td>So sánh số lượng thực nhận với số lượng đặt để xác định chênh lệch.</td></tr>
  <tr><td>5</td><td>SC-006-04</td><td>RecordQuantityDiscrepancyDialog</td><td>Ghi nhận chi tiết chênh lệch thiếu/thừa và nguyên nhân.</td></tr>
  <tr><td>6</td><td>SC-006-05</td><td>UpdateInternalWarehouseStockScreen</td><td>Cập nhật tồn kho nội bộ sau khi hoàn tất đối soát.</td></tr>
</table>

### 3.7. UC-007 - ManageSystem

<table border="1" cellpadding="6" cellspacing="0" style="border-collapse: collapse; width: 100%;">
  <tr>
    <th>STT</th>
    <th>Mã màn hình</th>
    <th>Tên màn hình</th>
    <th>Chức năng chính</th>
  </tr>
  <tr><td>1</td><td>SC-007</td><td>ManageSystemScreen</td><td>Trang điều phối quản trị hệ thống cho quản trị viên.</td></tr>
  <tr><td>2</td><td>SC-007-01</td><td>ManageUsersAndActorRolesScreen</td><td>Quản lý tài khoản người dùng, vai trò actor và phân quyền.</td></tr>
  <tr><td>3</td><td>SC-007-02</td><td>ManageSharedMasterDataScreen</td><td>Quản trị danh mục dữ liệu dùng chung cho hệ thống.</td></tr>
  <tr><td>4</td><td>SC-007-03</td><td>ViewCrossDepartmentOperationLogsScreen</td><td>Tra cứu nhật ký thao tác liên phòng ban.</td></tr>
  <tr><td>5</td><td>SC-007-04</td><td>BackupAndRestoreBusinessDataScreen</td><td>Thiết lập sao lưu và phục hồi dữ liệu nghiệp vụ.</td></tr>
</table>

## 4. Thiết kế sơ đồ chuyển màn hình

### 4.1. UC-001 - ManageImportRequests

```mermaid
graph LR
  SC001["ManageImportRequestsScreen (SC001)"]
  SC00101["CreateImportRequestScreen (SC00101)"]
  SC00102["ValidateImportRequestInputDialog (SC00102)"]
  SC00103["SubmitImportRequestConfirmation (SC00103)"]
  SC00104["TrackImportRequestStatusScreen (SC00104)"]
  SC00105["UpdateImportRequestScreen (SC00105)"]
  SC00106["CancelImportRequestDialog (SC00106)"]

  SC001 -->|create request| SC00101
  SC00101 -->|validate input| SC00102
  SC00102 -->|invalid data| SC00101
  SC00102 -->|valid data| SC00103
  SC00103 -->|done| SC001
  SC001 -->|track status| SC00104
  SC001 -->|edit request| SC00105
  SC00105 -->|re-validate| SC00102
  SC001 -->|cancel request| SC00106
  SC00106 -->|done| SC001
```

### 4.2. UC-002 - ManageSiteInformation

```mermaid
graph LR
  SC002["ManageSiteInformationScreen (SC002)"]
  SC00201["UpdateSiteProfileScreen (SC00201)"]
  SC00202["UpdateDeliveryTimesByShipAndAirScreen (SC00202)"]
  SC00203["UpdateSiteMerchandiseCatalogScreen (SC00203)"]
  SC00204["NotifyOrderingDepartmentOfSiteChangesDialog (SC00204)"]

  SC002 -->|update profile| SC00201
  SC002 -->|update delivery times| SC00202
  SC002 -->|update catalog| SC00203
  SC00201 -->|notify change| SC00204
  SC00202 -->|notify change| SC00204
  SC00203 -->|notify change| SC00204
  SC00204 -->|done| SC002
```

### 4.3. UC-003 - CollectInventoryData

```mermaid
graph LR
  SC003["CollectInventoryDataScreen (SC003)"]
  SC00301["FindCandidateSitesForMerchandiseScreen (SC00301)"]
  SC00302["FilterMerchandiseListPerSiteScreen (SC00302)"]
  SC00303["SendStockInquiryToSiteScreen (SC00303)"]
  SC00304["ReceiveStockResponseFromSiteScreen (SC00304)"]
  SC00305["PersistInventoryInformationFileScreen (SC00305)"]

  SC003 -->|start collection| SC00301
  SC00301 -->|shortlist sites| SC00302
  SC00302 -->|prepare inquiries| SC00303
  SC00303 -->|wait responses| SC00304
  SC00304 -->|consolidate data| SC00305
  SC00305 -->|done| SC003
```

### 4.4. UC-004 - PlanImportAllocation

```mermaid
graph LR
  SC004["PlanImportAllocationScreen (SC004)"]
  SC00401["ProcessEachMerchandiseIndependentlyPanel (SC00401)"]
  SC00402["CheckDesiredDeliveryFeasibilityPanel (SC00402)"]
  SC00403["RankSitesPanel (SC00403)"]
  SC00404["SplitQuantityAcrossSelectedSitesPanel (SC00404)"]
  SC00405["InsufficientSupplyErrorDialog (SC00405)"]

  SC004 -->|run allocation| SC00401
  SC00401 -->|per item| SC00402
  SC00402 -->|feasible| SC00403
  SC00402 -->|infeasible| SC00405
  SC00403 -->|ranked| SC00404
  SC00404 -->|save plan| SC004
  SC00405 -->|acknowledge| SC004
```

### 4.5. UC-005 - PlaceOverseasOrders

```mermaid
graph LR
  SC005["PlaceOverseasOrdersScreen (SC005)"]
  SC00501["ComposeOrderLinesScreen (SC00501)"]
  SC00502["SetDeliveryMeansShipOrAirDialog (SC00502)"]
  SC00503["TransmitOrdersToSelectedSitesScreen (SC00503)"]
  SC00504["AcknowledgeOrderAtSiteScreen (SC00504)"]

  SC005 -->|compose order| SC00501
  SC00501 -->|set delivery means| SC00502
  SC00502 -->|transmit orders| SC00503
  SC00503 -->|site acknowledges| SC00504
  SC00504 -->|done| SC005
```

### 4.6. UC-006 - ReceiveAndReconcileGoods

```mermaid
graph LR
  SC006["ReceiveAndReconcileGoodsScreen (SC006)"]
  SC00601["LoadExpectedOrderQuantitiesScreen (SC00601)"]
  SC00602["RecordGoodsReceiptScreen (SC00602)"]
  SC00603["CompareReceivedVsOrderedScreen (SC00603)"]
  SC00604["RecordQuantityDiscrepancyDialog (SC00604)"]
  SC00605["UpdateInternalWarehouseStockScreen (SC00605)"]

  SC006 -->|load expected| SC00601
  SC00601 -->|receive goods| SC00602
  SC00602 -->|compare| SC00603
  SC00603 -->|has discrepancy| SC00604
  SC00603 -->|no discrepancy| SC00605
  SC00604 -->|finalize| SC00605
  SC00605 -->|done| SC006
```

### 4.7. UC-007 - ManageSystem

```mermaid
graph LR
  SC007["ManageSystemScreen (SC007)"]
  SC00701["ManageUsersAndActorRolesScreen (SC00701)"]
  SC00702["ManageSharedMasterDataScreen (SC00702)"]
  SC00703["ViewCrossDepartmentOperationLogsScreen (SC00703)"]
  SC00704["BackupAndRestoreBusinessDataScreen (SC00704)"]

  SC007 -->|users and roles| SC00701
  SC007 -->|master data| SC00702
  SC007 -->|operation logs| SC00703
  SC007 -->|backup/restore| SC00704
  SC00701 -->|done| SC007
  SC00702 -->|done| SC007
  SC00703 -->|done| SC007
  SC00704 -->|done| SC007
```

## 5. Từ điển thuật ngữ

Mục này thống nhất các thuật ngữ nghiệp vụ, tác nhân và dữ liệu được sử dụng xuyên suốt tài liệu.

### 5.1. Thuật ngữ nghiệp vụ và tác nhân

<table border="1" cellpadding="6" cellspacing="0" style="border-collapse: collapse; width: 100%;">
  <tr>
    <th>STT</th>
    <th>Thuật ngữ</th>
    <th>Mô tả</th>
  </tr>
  <tr><td>1</td><td>Import Request</td><td>Yêu cầu nhập hàng do `SalesDepartment` khởi tạo, làm đầu vào cho toàn bộ quy trình đặt hàng nhập khẩu.</td></tr>
  <tr><td>2</td><td>Import Allocation Plan</td><td>Phương án phân bổ số lượng đặt hàng theo từng Site và phương thức giao hàng.</td></tr>
  <tr><td>3</td><td>Overseas Import Site (Site)</td><td>Đơn vị/địa điểm cung ứng hàng ở nước ngoài tham gia xử lý đơn đặt hàng.</td></tr>
  <tr><td>4</td><td>SalesDepartment</td><td>Tác nhân tạo, cập nhật, hủy và theo dõi yêu cầu nhập hàng.</td></tr>
  <tr><td>5</td><td>InternationalOrderingDepartment</td><td>Tác nhân điều phối thu thập tồn kho, lập phân bổ và phát hành đơn đặt hàng quốc tế.</td></tr>
  <tr><td>6</td><td>WarehouseManagementDepartment</td><td>Tác nhân tiếp nhận hàng, đối soát số lượng và cập nhật tồn kho nội bộ.</td></tr>
  <tr><td>7</td><td>SystemAdministrator</td><td>Tác nhân quản trị người dùng, phân quyền, dữ liệu dùng chung, log vận hành và sao lưu/phục hồi.</td></tr>
  <tr><td>8</td><td>Merchandise</td><td>Mặt hàng được quản lý theo mã hàng, đơn vị tính và thông tin liên quan trong quy trình nhập khẩu.</td></tr>
  <tr><td>9</td><td>Desired Delivery Date</td><td>Ngày mong muốn nhận hàng của từng mặt hàng trong yêu cầu nhập.</td></tr>
  <tr><td>10</td><td>Stock Inquiry</td><td>Yêu cầu xác nhận tồn kho gửi từ hệ thống tới Site để thu thập khả năng cung ứng.</td></tr>
</table>

### 5.2. Thuật ngữ dữ liệu và vận hành hệ thống

<table border="1" cellpadding="6" cellspacing="0" style="border-collapse: collapse; width: 100%;">
  <tr>
    <th>STT</th>
    <th>Thuật ngữ</th>
    <th>Mô tả</th>
  </tr>
  <tr><td>1</td><td>In-Stock Quantity</td><td>Số lượng tồn kho khả dụng của một mặt hàng tại một Site ở thời điểm phản hồi.</td></tr>
  <tr><td>2</td><td>Delivery Days by Ship/Air</td><td>Số ngày vận chuyển dự kiến theo đường biển (`ship`) hoặc đường hàng không (`air`).</td></tr>
  <tr><td>3</td><td>Order Line</td><td>Dòng chi tiết trong đơn đặt hàng gồm mã hàng, số lượng, Site và phương thức giao hàng.</td></tr>
  <tr><td>4</td><td>Acknowledgement</td><td>Xác nhận của Site về việc đã nhận và tiếp nhận xử lý đơn đặt hàng.</td></tr>
  <tr><td>5</td><td>Goods Receipt</td><td>Nghiệp vụ ghi nhận số lượng hàng thực tế khi hàng được tiếp nhận vào kho.</td></tr>
  <tr><td>6</td><td>Quantity Discrepancy</td><td>Chênh lệch giữa số lượng đặt và số lượng thực nhận, có thể là thiếu hoặc thừa.</td></tr>
  <tr><td>7</td><td>Operation Log</td><td>Nhật ký thao tác và sự kiện vận hành phục vụ giám sát, truy vết và kiểm tra.</td></tr>
  <tr><td>8</td><td>Shared Master Data</td><td>Dữ liệu danh mục dùng chung (mã hàng, đơn vị tính, quy tắc cấu hình...) cho nhiều chức năng.</td></tr>
  <tr><td>9</td><td>Backup</td><td>Bản sao dữ liệu được tạo theo lịch hoặc thủ công để đảm bảo khả năng khôi phục.</td></tr>
  <tr><td>10</td><td>Restore</td><td>Quá trình phục hồi dữ liệu hệ thống từ bản sao lưu hợp lệ.</td></tr>
</table>

## 6. Đặc tả phụ trợ

### 6.1. Chức năng

Cxxx

### 6.2. Hiệu năng

Xxxx

### 6.3. Độ tin cậy

…
