# Đặc tả Use Case — ManageSiteInformation

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
