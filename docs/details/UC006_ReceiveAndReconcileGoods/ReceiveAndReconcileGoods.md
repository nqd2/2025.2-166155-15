# Đặc tả Use Case — ReceiveAndReconcileGoods

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
