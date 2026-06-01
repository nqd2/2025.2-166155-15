# Đặc tả Use Case — CollectInventoryData

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
