#

# Hệ thống phần mềm đặt hàng nhập khẩu

1. # **Mô tả hệ thống**

Dưới đây là mô tả hiện trạng của *Hệ thống đặt hàng nhập khẩu* (Import assignment system) của Công ty kinh doanh hàng nhập ngoại (a merchandise importing and sales company).

(1) Mỗi khi có nhu cầu nhập hàng, Bộ phận bán hàng (Sales department) gửi thông tin về danh sách các mặt hàng cần đặt[^1] cho Bộ phận đặt hàng quốc tế (Overseas order placement department). Thông tin này bao gồm: mã hàng (merchandise code), số lượng (quantity ordered), đơn vị (unit), và ngày nhận mong muốn (delivery date desired). Định dạng thông tin chi tiết như sau:

| Merchandise code | Quantity ordered | Unit | Desired delivery date |  |  |
| :---: | :---: | :---: | :---: | :---: | :---: |
|  |  |  | **Year** | **Month** | **Date** |

(2) Nhận được thông tin trên, Bộ phận đặt hàng quốc tế trước tiên tìm các Site nhập khẩu[^2] ở nước ngoài (Overseas Import Sites) có kinh doanh ít nhất một trong các mặt hàng cần đặt. Với mỗi Site **S** tìm được, bộ phận này lọc từ danh sách ban đầu ra danh sách các mặt hàng mà Site **S** kinh doanh. Tiếp theo, bộ phận này gửi danh sách lọc được cho Site **S** để hỏi về số lượng trong kho (in-stock quantity) của từng mặt hàng trong danh sách.

(3) Trong bước tiếp theo, các Site trả về cho Bộ phận đặt hàng quốc tế thông tin số lượng trong kho của các mặt hàng được yêu cầu. Mặt hàng Site không còn, số lượng sẽ có giá trị là 0\. Bộ phận đặt hàng quốc tế ghi lại các thông tin này vào Tệp thông tin kho, có định dạng như sau:

| Site code | Merchandise code | In-stock quantity | Unit |
| :---: | :---: | :---: | :---: |

(4) Bộ phận đặt hàng quốc tế đã tạo và lưu sẵn Tệp thông tin site, chứa thông tin vận chuyển chi tiết của từng Site. Để vận chuyển hàng hóa từ các Site tới Bộ phận bán hàng, có thể dùng phương tiện tàu (delivery by ship) hoặc hàng không (delivery by air). Số ngày vận chuyển hàng hóa (number of days for delivery) thay đổi theo Site và theo loại hình vận chuyển. Các thông tin này được cung cấp bởi mỗi Site. Khi có thay đổi, các Site sẽ thông báo cho Bộ phận đặt hàng quốc tế. Định dạng thông tin này trong Tệp thông tin site như sau:

| Site code | Import site name | Number of days for delivery by ship | Number of days for delivery by air | Other information |
| :---: | :---: | :---: | :---: | :---: |

(5) Dựa trên thông tin về lượng hàng trong kho và số ngày vận chuyển của mỗi Site, Bộ phận đặt hàng quốc tế sẽ quyết định nhập về số lượng mặt hàng cụ thể từ các Site. Bộ phận đặt hàng quốc tế sẽ xử lý **từng mặt hàng một cách** **độc lập** như sau. Với mỗi mặt hàng, tìm danh sách các Site đáp ứng được ngày nhận mong muốn. Nếu một Site không cung cấp đủ mặt hàng đó, phải chấp nhận nhập hàng từ nhiều Site. Nếu không thể đạt được số lượng nhập khẩu như yêu cầu, cần đưa ra thông báo lỗi. Các Site được lựa chọn theo các tiêu chí với mức độ ưu tiên giảm dần như sau:

1. Ưu tiên phương tiện tàu hơn hàng không  
   2. Ưu tiên Site có lượng hàng trong kho lớn  
   3. Số lượng các Site được chọn nhỏ nhất có thể

(6) Trong bước cuối cùng, Bộ phận đặt hàng quốc tế gửi thông tin đặt hàng tới các Site đã chọn. Định dạng của thông tin này như sau (Trong đó phương tiện vận chuyển – Delivery means – được lưu là “ship delivery” hoặc “air delivery”)

| Site code | Merchandise code | Quantity ordered | Unit | Delivery means |
| :---: | :---: | :---: | :---: | :---: |

(7) Khi hàng hóa được vận chuyển tới nơi, Bộ phận quản lý kho sẽ kiểm hàng, so sánh hàng về thực tế với hàng đặt trong danh sách, rồi lưu vào hệ thống quản lý kho của riêng họ.

Nhiệm vụ: cần tin học hóa *Hệ thống đặt hàng nhập khẩu* nói trên

**Lưu ý: Với tất cả các bài tập, luôn luôn phải có file doc, pdf, và các file ảnh**

**Bài tập 2**

Vẽ biểu đồ use case (bao gồm biểu đồ tổng quan và các biểu đồ phân rã nếu có)

***Bài tập cá nhân***: Mỗi thành viên chọn một use case nghiệp vụ (bỏ qua các use case đăng nhập đăng ký) để thực hiện. Cần chỉ rõ ai làm use case nào. Trừ khi không còn use case, sinh viên không được phép chọn các use case dễ (ví dụ use case xóa dữ liệu, xem 1 bản ghi dữ liệu đơn giản). Use case được chọn phải có giao diện người dùng (GUI). Mỗi thành viên sẽ làm việc xuyên suốt với use case này. Trong bài tập này, mỗi thành viên cần đặc tả use case mình phụ trách. Trong đặc tả có thêm biểu đồ hoạt động (activity diagram) cho 1 luồng nào đó. Mỗi cá nhân nộp tài liệu SRS theo mẫu trong thư mục Google Drive.

***Bài tập nhóm***: Tập hợp các kết quả của các thành viên thành tại liệu đặc tả yêu cầu phần mềm SRS (Sofware Requirement Specification) cho cả nhóm

Lưu ý trong thư mục group và thư mục cá nhân cần có: file doc SRS, file pdf SRS, file project Astah, file ảnh biểu đồ export từ Astah.

**Bài tập 3 (Phân tích use case)**

***Bài tập cá nhân***: Vẽ các biểu đồ trình tự (mức phân tích) trong use case mình phụ trách. Có thể cần vẽ nhiều biểu đồ, mỗi biểu đồ ứng với một scenario (luồng sự kiện) trong use case. Cần chọn ít nhất 1 biểu đồ trình tự đã vẽ và vẽ thêm biểu đồ giao tiếp biểu diễn tương đương với biểu đồ trình tự này.

Dựa trên các biểu đồ trình tự đã vẽ, vẽ biểu đồ lớp (mức phân tích) cho use case mình phụ trách.

***Bài tập nhóm***: Vẽ biểu đồ lớp (mức phân tích) cho cả nhóm. Lưu ý: các lớp cùng chức năng ở nhiều use case phải dùng thống nhất cùng 1 tên. Các hành vi giống nhau trong các use case khác nhau cũng phải có cùng tên.

**Bài tập 4 (Thiết kế giao diện)**

***Bài tập nhóm***: Vẽ sơ đồ chuyển đổi màn hình (screen transition diagram), từ màn hình login đến tất cả các màn hình chi tiết trong các use case nhóm phụ trách.

***Bài tập cá nhân***: Vẽ sơ đồ chuyển đổi màn hình (screen transition diagram), từ màn hình home đến tất cả các màn hình chi tiết trong các use case mà mình phụ trách.

Thiết kế tất cả các màn hình dẫn từ trang home tới use case của mình.

Đặc tả tất cả các màn hình trong use case của mình.

Thiết kế từng subsystem (nếu có) trong use case của mình (vẽ biểu đồ trình tự cho từng hành vi mà subsystem cung cấp như minh họa trong hình dưới đây, mỗi hành vi một biểu đồ trình tự). Vẽ biểu đồ lớp cho từng subsystem

![A screenshot of a computer screenDescription automatically generated][image1]

**Bài tập 5 (Thiết kế chi tiết lớp)**

***Bài tập cá nhân***: Vẽ các biểu đồ trình tự (mức thiết kế) trong use case mình phụ trách. Có thể cần vẽ nhiều biểu đồ, mỗi biểu đồ ứng với một scenario trong use case. Sau đó tìm các hành vi và thuộc tính cho các lớp thiết kế và vẽ biểu đồ lớp (mức thiết kế) cho use case mình phụ trách.

Lưu ý: cần dùng interaction ref để biểu đồ trình tự không bị quá phức tạp trong 1 hình vẽ

Vẽ biểu đồ phụ thuộc gói

***Bài tập nhóm***: Gộp lại các biểu đồ lớp (mức thiết kế) của mỗi thành viên, tổ chức thành các package cho hợp lý, thống nhất cách thức đặt tên.

Sau đó, vẽ biểu đồ phụ thuộc package cho toàn nhóm và cho từng cá nhân (cần phân thành các tầng)

**Bài tập 6**

Chỉnh sửa lại thiết kế, áp dụng các nguyên lý thiết kế và các mẫu thiết kế đã học. Phân tích cách chỉnh sửa và ý nghĩa đem lại.

**Bài tập 7 (Lập trình và kiểm thử)**

Ngoài file Readme.txt phân công và thư mục từng thành viên để ngoài cùng, trong Homework07 cần có thư mục Projects, trong đó chứa các Eclipse projects của nhóm. **Mã nguồn phải khớp với thiết kế** đã làm và thoả mãn các yêu cầu đã đặc tả.

Mỗi thành viên cần chọn một module bất kỳ của mình (phương thức/lớp) và **dùng lần lượt kỹ thuật kiểm thử hộp đen và kỹ thuật kiểm thử hộp trắng (dùng độ đo c1)** để thiết kế test case cho module đó. Lưu ý phải dùng **lần lượt 2** kỹ thuật hộp đen và hộp trắng. Mỗi bạn cần viết file tài liệu mô tả phương thức/lớp mà mình muốn kiểm thử. Sau đó, trình bày phân tích kỹ thuật kiểm thử mình đã áp dụng như thế nào để thiết kế được các test case như vậy. Đồng thời, cần mô tả các test case đã thiết kế được khi áp dụng kỹ thuật đó.

Sau đó, mỗi thành viên cần cài đặt **chương trình kiểm thử tự động** các test case đó, sử dụng JUnit framework. Đồng thời chỉ rõ tên đầy đủ (full name) của các Class kiểm thử tự động trong tài liệu.

Bên cạnh kiểm thử đơn vị, mỗi thành viên còn cần thực hiện kiểm thử use case mình phụ trách (các scenarios là gì, từ đó cần thiết kế bao nhiêu test case, các test case là gì, và kết quả test) (SV tự tìm template)

**Bài tập 8 \- Final (ghép lại lần cuối để bảo vệ)**  
Tạo thư mục Final trong repo của nhóm. Trong thư mục Final, có các thư mục/file sau:

* Report nhóm: cần có report chung cho toàn nhóm bao gồm (1) Tổng % đóng góp của các thành viên trong nhóm, (2) Tài liệu đặc tả phần mềm SRS, (3) Tài liệu phân tích use case, (4) Sơ đồ chuyển đổi màn hình, đặc tả màn hình, các thiết kế subsystems, (5) Mô tả thiết kế chi tiết phần mềm: các biểu đồ lớp, biểu đồ gói chung trong toàn nhóm, chú ý phân thành các tầng, giải thích ý nghĩa từng package, (6) Các nguyên lý thiết kế và mẫu thiết kế nhóm đã áp dụng, kèm phân tích giải thích lợi ích đem lại, (7) Một số ảnh giao diện phần mềm (cho từng use case của các thành viên trong nhóm), (8) Nhật ký làm việc nhóm.
* Report cá nhân: Mỗi cá nhân cần có file report riêng là kết quả các bài tập 2-7. File report cá nhân chỉ chứa những thông tin làm việc bởi chính cá nhân đó, liên quan tới use case cá nhân đó phụ trách.  
* Projects: Chứa mã nguồn cuối cùng, bao gồm cả mã nguồn cho chương trình chính kèm code kiểm thử đơn vị  
* Readme.txt: Tổng kết toàn bộ phân công việc từ đầu đến cuối, chỉ rõ tổng % đóng góp của các thành viên trong nhóm vào kết quả chung

Công việc cần làm trước buổi bảo vệ đầu tiên: upload lên thư mục Google Drive (do GV cung cấp)

* Mỗi nhóm một thư mục, đặt tên đúng quy cách. Ví dụ: Group 01, Group 04, Group 13  
* Phần kết quả chung: upload vào thư mục Group, là thư mục con của thư mục Group XX  
* Phần kết quả riêng:  
  * upload vào thư mục đặt theo tên SV (ví dụ: Nguyen Van Nam)  
  * **mỗi bạn tạo 1 video (1) demo các tính năng trong use case của mình \+ (2) chạy phần kiểm thử tự động**  
  * **Video không quá 50 MB, ảnh rõ nét**

Cách thuyết trình:

* Tất cả các thành viên trong nhóm phải bật cam khi trình bày trên Teams  
* Mỗi nhóm 20 phút thuyết trình và demo \+ 10 phút hỏi đáp  
* Thuyết trình chung cho toàn nhóm, không thuyết trình cá nhân  
* Gợi ý nội dung:  
  * Trình bày nhanh biểu đồ lớp ở mức phân tích, mức thiết kế. Phân tích những khác biệt ở mức thiết kế và mức phân tích, mức thiết kế đã chi tiết thêm những gì  
    * Chỉ rõ từng use case của mỗi bạn cần dùng những lớp nào  
  * Biểu đồ phụ thuộc gói  
    * Chỉ rõ từng use case của mỗi bạn cần dùng những package nào  
  * Các subsystem  
    * Chỉ rõ từng use case của mỗi bạn cần dùng những subsystem nào  
  * Các kỹ thuật, nguyên lý thiết kế nhóm đã áp dụng  
  * Demo (thiết kế luồng demo sao cho tối ưu nhất, lần lượt những use case nào sẽ được trình bày cho tối ưu, vd phải tạo yêu cầu nhập hàng trước rồi mới tới xử lý yêu cầu nhập hàng, ...)

[^1]:  Tại cùng một thời điểm, Bộ phận bán hàng có thể có nhiều danh sách yêu cầu như vậy.

[^2]:  Hiện tại, 50 Site nhập khẩu trên toàn thế giới đã được kết nối với Bộ phận đặt hàng quốc tế. Mỗi Site kinh doanh nhiều mặt hàng khác nhau; các Site khác nhau có thể cùng kinh doanh một số mặt hàng giống nhau. Khi thay đổi danh sách các mặt hàng kinh doanh, mỗi Site sẽ gửi lại danh sách mới cho Bộ phận đặt hàng quốc tế.
