package project_hoang.web.admin.caidat;

// Thư viện chuẩn: Java Standard (JavaSE)
import java.util.List;

// Thư viện web: Java Spring
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import project_hoang.web.qdl.Qdl;

/**
 * User performs Actions on UI/View
 * Client Computer sends Http Requets -> Server Computer
 * (Http Requets: GET, POST, PUT, DELETE)
 * Server uses Url Routes Mapping Table to forward
 * requests to Spring Controllers
 * Each Controller itself forward requets to its Actions
 * Client có nhiều yêu cầu & truy vấn thông qua giao thức Http Get/Post
 * Server có nhiều Controller phục vụ, đáp ứng
 * Lập trình viên cần định tuyến cho rõ ràng:
 * Controller (Action) nào phục vụ Request(Get/Post) nào ?
 * Đừng thay đổi @Mapping, mà chỉ nên thay đổi Action Name
 * 
 * View Actions: List, Add, Delete, Edit, Details (LADE)
 * Data Actions: List, Create, Delete, Update, Read (CRUD)
 * SQL Actions: Select, Insert, Delete, Update (SIDU)
 * 
 * spring boot controller autowired HttpServletRequest
 * https://stackoverflow.com/questions/3320674/spring-how-do-i-inject-an-httpservletrequest-into-a-request-scoped-bean
 */

@Controller
public class QdlCaiDat {
    @Autowired
    private DvlCaiDat dvl; // cung cấp các dịch vụ thao tác dữ liệu

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private HttpSession session;

    // Các biến autowired như là request và session giúp giảm bớt sự dư thừa mã
    // trước đây các hàm Action() của Controller() cứ phải khai báo các tham số này,
    // nhìn rối & phức tạp

    /*
     * 
     * APPLICATION FAILED TO START
     ***************************
     * 
     * Description:
     * 
     * Field redirectAtt in spring.jsb_web_shop.caidat.QdlCaiDat required a bean of
     * type 'org.springframework.web.servlet.mvc.support.RedirectAttributes' that
     * could not be found.
     * 
     * The injection point has the following annotations:
     * - @org.springframework.beans.factory.annotation.Autowired(required=true)
     * 
     * 
     * Action:
     * 
     * Consider defining a bean of type
     * 'org.springframework.web.servlet.mvc.support.RedirectAttributes' in your
     * configuration.
     */
    // @Autowired
    // private RedirectAttributes redirectAtt;

    // @Autowired
    // private DvlBảngNgoại dvlBangNgoai; // cung cấp các dịch vụ thao tác dữ liệu

    // Không cần khai báo tham số HttpSession session,
    // trong các hàm action() của controller nữa vì có thể truy cập vào nó
    // thông qua: request.getSession()

    @Controller
    @RequestMapping("/admin")
    public class adminController {

        @GetMapping({
                "/cai-dat",
                "/cai-dat/duyet"
        })
        public String getDuyet(Model model) {
            if (Qdl.NhanVienChuaDangNhap(request))
                return "redirect:/admin/dang-nhap";

            // Đọc dữ liệu bảng rồi chứa vào biến tạm
            List<CaiDat> list = dvl.duyệtCaiDat();

            // Gửi danh sách sang giao diện View HTML
            model.addAttribute("ds", list);

            model.addAttribute("title", "Quản Lý Cài Đặt");

            // Nội dung riêng của trang...
            model.addAttribute("content", "admin/caidat/duyet.html"); // duyet.html

            // ...được đặt vào bố cục chung của toàn website
            // return "layout.html";
            return "layouts/layout-admin.html";
        }

        @GetMapping("/cai-dat/them")
        public String getThem(Model model) {
            if (Qdl.NhanVienChuaDangNhap(request))
                return "redirect:/qdl/nhanvien/dangnhap";

            CaiDat dl = new CaiDat();

            // Gửi đối tượng dữ liệu sang bên view
            // để còn ràng buộc vào html form
            model.addAttribute("dl", dl);

            // Nội dung riêng của trang...
            model.addAttribute("content", "caidat/them.html");
            // model.addAttribute("dsBangNgoai", this.dvlBangNgoai.dsBangNgoai());

            // ...được đặt vào bố cục chung của toàn website
            return "layout/layout-admin.html";
        }

        @PostMapping("/cai-dat/them")
        public String postThem(@ModelAttribute("CaiDat") CaiDat dl) {
            if (Qdl.NhanVienChuaDangNhap(request))
                return "redirect:/qdl/nhanvien/dangnhap";

            // System.out.print("save action...");
            dvl.lưuCaiDat(dl);

            // Gửi thông báo thành công từ view Add/Edit sang view List
            session.setAttribute("message", "Đã hoàn tất việc thêm mới !");

            return "redirect:/qdl/caidat/duyet";
        }

    }

    // @GetMapping("/qdl/caidat/sua/{id}")
    @GetMapping("/qdl/caidat/sua")
    public String getSua(Model model, @RequestParam("id") int id) {
        if (Qdl.NhanVienChuaDangNhap(request))
            return "redirect:/qdl/nhanvien/dangnhap";

        // trangSua(Model model, @PathVariable(value = "id") int id) {
        // Lấy ra bản ghi theo id
        CaiDat dl = dvl.xemCaiDat(id);

        // Gửi đối tượng dữ liệu sang bên view
        model.addAttribute("dl", dl);
        // model.addAttribute("dsBangNgoai", this.dvlBangNgoai.dsBangNgoai());

        // Hiển thị giao diện view html
        // Nội dung riêng của trang...
        model.addAttribute("content", "caidat/sua.html"); // sua.html

        // ...được đặt vào bố cục chung của toàn website
        return "layout/layout-admin.html";
    }

    // @GetMapping("/qdl/caidat/xoa/{id}")
    // public String // Giao diện xác nhận xoá
    // trangXoa(Model model, @PathVariable(value = "id") int id) {
    @GetMapping("/qdl/caidat/xoa")
    public String getXoa(Model model, @RequestParam(value = "id") int id) {
        if (Qdl.NhanVienChuaDangNhap(request))
            return "redirect:/qdl/nhanvien/dangnhap";

        // Lấy ra bản ghi theo id
        CaiDat dl = dvl.tìmCaiDatTheoId(id);

        // Gửi đối tượng dữ liệu sang bên view
        model.addAttribute("dl", dl);

        // Hiển thị view giao diện
        // Nội dung riêng của trang...
        model.addAttribute("content", "caidat/xoa.html"); // xoa.html

        // ...được đặt vào bố cục chung của toàn website
        return "layout/layout-admin.html";
    }

    @GetMapping("/qdl/caidat/copy")
    public String getCopy(Model model, @RequestParam("id") int id, RedirectAttributes redirectAttributes) {
        if (Qdl.NhanVienChuaDangNhap(request))
            return "redirect:/qdl/nhanvien/dangnhap";

        // Lấy ra thông tin của bản ghi cũ để sao chép
        CaiDat dl = dvl.xemCaiDat(id);

        // Khởi tạo bản ghi mới
        CaiDat dl_copy = new CaiDat();

        // Sao chép dữ liệu từ bản ghi cũ sang bản ghi mới
        dl_copy.setKhoa(dl.getKhoa());
        dl_copy.setGiaTri(dl.getGiaTri());

        // Lưu bản ghi mới copy vào trong csdl
        dvl.lưuCaiDat(dl_copy);

        // Gửi thông báo thành công sang bên View
        redirectAttributes.addFlashAttribute("THONG_BAO_OK", "Đã hoàn tất việc sao chép!");

        // Điều hướn sang trang danh sách
        return "redirect:/qdl/caidat/duyet";
    }

    /**
     * ví dụ: http://localhost:6868/qdl/caidat/xem/3
     * nó khác so với phần sửa & xóa:
     * http://localhost:6868/qdl/caidat/xoa?id=3
     * 
     * @param model
     * @param id
     * @return
     */
    @GetMapping("/qdl/caidat/xem/{id}")
    public String getXem(Model model, @PathVariable(value = "id") int id) {
        if (Qdl.NhanVienChuaDangNhap(request))
            return "redirect:/qdl/nhanvien/dangnhap";

        // Lấy ra bản ghi theo id
        CaiDat dl = dvl.xemCaiDat(id);

        // Gửi đối tượng dữ liệu sang bên view
        model.addAttribute("dl", dl);

        // Hiển thị view giao diện
        // Nội dung riêng của trang...
        model.addAttribute("content", "caidat/xem.html");

        // ...được đặt vào bố cục chung của toàn website
        return "layout/layout-admin.html";
    }

    // nên là createAction()
    // https://javarevisited.blogspot.com/2022/04/how-to-use-session-attributes-in-spring.html#axzz89tnfpu78
    // https://stackoverflow.com/questions/55882706/how-to-remove-attribute-from-session-using-thymeleaf
    // https://stackoverflow.com/questions/46744586/thymeleaf-show-a-success-message-after-clicking-on-submit-button
    // @PostMapping("/qdl/caidat/add") why not ??? it's okay right ?

    // How to send success message to List View
    // https://www.appsloveworld.com/springmvc/100/17/how-to-add-success-notification-after-form-submit

    @PostMapping("/qdl/caidat/sua")
    public String postSua(@ModelAttribute("CaiDat") CaiDat dl) {
        if (Qdl.NhanVienChuaDangNhap(request))
            return "redirect:/qdl/nhanvien/dangnhap";

        dvl.lưuCaiDat(dl);

        // Gửi thông báo thành công từ view Add/Edit sang view List
        session.setAttribute("message", "Đã hoàn tất việc cập nhật !");

        return "redirect:/qdl/caidat/duyet";
    }

    // @PostMapping("/qdl/caidat/xoabo/{id}")
    // public String // Hàm xử lý yêu cầu xoá 1 bản ghi
    // xoabo(Model model, @PathVariable(value = "id") int id)
    // Sử dụng Session để gửi thông báo thành công từ Controller sang View
    // Sử dụng Session Helper để xóa thông báo hiện trên View
    @PostMapping("/qdl/caidat/xoa")
    public String postXoa(Model model, @RequestParam("Id") int id, RedirectAttributes redirectAttributes)
    // public String postXoa(Model model, @RequestParam("Id") int id)
    {
        if (Qdl.NhanVienChuaDangNhap(request))
            return "redirect:/qdl/nhanvien/dangnhap";

        // Xoá dữ liệu
        this.dvl.xóaCaiDat(id);

        // Lỗi:
        // org.springframework.expression.spel.SpelEvaluationException: EL1004E: Method
        // call: Method removeAttribute(java.lang.String) cannot be found on type
        // org.thymeleaf.context.WebEngineContext$SessionAttributeMap
        // request.getSession().setAttribute("THONG_BAO_OK_SS", "Đã xóa thành công !");
        // <!-- <th:block th:inline="text">
        // [[${session.removeAttribute('THONG_BAO_OK_SS')}]]</th:block> -->

        // <th:block th:text="${@sessionHelper.removeMessageFromSession()}"></th:block>

        // request.getSession().setAttribute("message", "Đã xóa thành công !");
        // session.setAttribute("message", "Đã xóa thành công, ahihi!");

        // redirectAtt.addFlashAttribute("THONG_BAO_OK", "Đã hoàn tất việc xóa !");
        redirectAttributes.addFlashAttribute("THONG_BAO_OK", "Đã hoàn tất việc xóa !");

        // Điều hướng sang trang giao diện
        return "redirect:/qdl/caidat/duyet";
    }

}// end class
