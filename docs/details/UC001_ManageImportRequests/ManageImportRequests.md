# Đặc tả Use Case — ManageImportRequests

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
