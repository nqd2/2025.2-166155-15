# Đặc tả Use Case — ManageSystem

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
