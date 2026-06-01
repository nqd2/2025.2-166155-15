# Đặc tả Use Case — PlanImportAllocation

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
