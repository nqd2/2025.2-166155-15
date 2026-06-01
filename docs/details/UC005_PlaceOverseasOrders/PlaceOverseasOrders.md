# Đặc tả Use Case — PlaceOverseasOrders

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
