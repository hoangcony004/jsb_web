package project_hoang.web.admin.nhanvien;

import java.time.LocalDate;
import java.util.ArrayList;

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

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

// gọi thư viện mã hóa mật khẩu cho dự án
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// import org.springframework.security.crypto.password.PasswordEncoder;

// Gọi thư viện mã hóa độc lập Spring
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.LoggerFactory;

// Thue viện Session
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import project_hoang.web.qdl.Qdl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class QdlNhanVien {

    @Autowired
    private HttpServletRequest request;

    // đối tượng chịu trách nhiệm mã hóa
    // @Autowired
    // private PasswordEncoder passwordEncoder;
    // @Autowired
    // private DvlBảngNgoại dvlBangNgoai; // cung cấp các dịch vụ thao tác dữ liệu

    @Controller
    @RequestMapping("/admin")
    public class adminController {
        @Autowired
        private DvlNhanVien dvl;

        // @Autowired
        // private KdlNhanVien DvlNhanVien;

        @Autowired
        private DvlNhanVien dvlNhanVien;

        private static final Logger logger = LoggerFactory.getLogger(QdlNhanVien.class);

        @GetMapping({
                "/nhan-vien",
                "/nhan-vien/duyet"
        })
        public String getDuyet(Model model,
                @RequestParam(defaultValue = "0") int page,
                @RequestParam(defaultValue = "5") int pageSize,
                @RequestParam(defaultValue = "tenDayDu") String sort,
                @RequestParam(defaultValue = "asc") String direction) {

            if (Qdl.NhanVienChuaDangNhap(request))
                return "redirect:/admin/dang-nhap";

            Sort.Direction sortDirection = direction.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
            Pageable pageable = PageRequest.of(page, pageSize, Sort.by(sortDirection, sort));

            Page<NhanVien> nhanVienPage = dvlNhanVien.duyệtNhanVien(pageable);
            List<NhanVien> list = nhanVienPage.getContent();
            int currentPage = 1;
            int totalPages = 10;
            model.addAttribute("currentPage", currentPage);
            model.addAttribute("totalPages", totalPages);

            model.addAttribute("ds", list);
            model.addAttribute("page", nhanVienPage);
            model.addAttribute("dl", new NhanVien());

            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", nhanVienPage.getTotalPages());
            model.addAttribute("pageSize", pageSize);

            model.addAttribute("sort", sort);
            model.addAttribute("direction", direction);
            model.addAttribute("sortDirection", direction.equals("asc") ? "asc" : "desc");

            model.addAttribute("action", "/admin/nhan-vien/them");
            model.addAttribute("title_body", "Thêm Nhân Viên");
            model.addAttribute("title_sm", "Thêm mới");
            model.addAttribute("title", "Quản Lý Nhân Viên");
            model.addAttribute("content", "admin/nhanvien/duyet.html");

            return "layouts/layout-admin.html";
        }

        @GetMapping("/nhan-vien/them")
        public String getThem(Model model) {
            // NhanVien dl = new NhanVien();
            var dl = new NhanVien();

            model.addAttribute("dl", dl);
            model.addAttribute("title", "Thêm Nhân Viên");
            model.addAttribute("title_sm", "Thêm mới");

            model.addAttribute("action", "/admin/nhan-vien/them");

            // Nội dung riêng của trang...
            // model.addAttribute("content", "admin/nhanvien/form-bs4.html");
            model.addAttribute("content", "admin/nhanvien/them.html");

            return "layouts/layout-admin.html";
            // return "admin/nhanvien/form-bs4.html";
        }

        @PostMapping("/nhan-vien/them")
        public String postThem(@ModelAttribute("NhanVien") NhanVien dl, RedirectAttributes redirectAttributes) {

            // Cách làm hệ thống: Spring Security
            // mã hóa mật khẩu trước khi lưu
            // var password_encoded = passwordEncoder.encode(dl.getMatKhau());
            // dl.setMatKhau(password_encoded);
            // dl.setMatKhau(passwordEncoder.encode(dl.getMatKhau()));

            // var password = "123abc";
            // Lấy mật khẩu trên HTML Form
            var inputPassword = dl.getMatKhau();
            // Mã hóa
            var hash = BCrypt.hashpw(inputPassword, BCrypt.gensalt(12));
            dl.setMatKhau(hash);

            dl.setNgayTao(LocalDate.now());
            dl.setNgaySua(LocalDate.now());

            // rồi mới lưu vào csdl
            dvl.lưuNhanVien(dl);

            // Gửi thông báo thành công từ view Add/Edit sang view List
            redirectAttributes.addFlashAttribute("THONG_BAO", "Đã thêm mới thành công !");

            return "redirect:/admin/nhan-vien/duyet";
        }

        // @GetMapping("/nhanvien/sua/{id}")
        @GetMapping("/nhan-vien/sua")
        public String getSua(Model model, @RequestParam("id") int id) {
            // trangSua(Model model, @PathVariable(value = "id") int id) {
            // Lấy ra bản ghi theo id
            NhanVien dl = dvl.xemNhanVien(id);

            // Gửi đối tượng dữ liệu sang bên view
            model.addAttribute("dl", dl);
            // model.addAttribute("dsBangNgoai", this.dvlBangNgoai.dsBangNgoai());

            // Hiển thị giao diện view html
            // Nội dung riêng của trang...
            model.addAttribute("content", "admin/nhanvien/sua.html"); // sua.html

            return "layouts/layout-admin.html";
        }

        @GetMapping("/nhan-vien/sua-ajax")
        public String getSuaAjax(Model model, @RequestParam("id") int id) {
            if (Qdl.NhanVienChuaDangNhap(request))
                return "redirect:/admin/dang-nhap";

            // NhanVien dl = dvl.xemNhanVien(id);
            var dl = dvl.xemNhanVien(id);
            model.addAttribute("title_body", "Sửa Nhân Viên");
            model.addAttribute("title_sm", "Cập nhật");
            // Gửi đối tượng dữ liệu sang bên view
            model.addAttribute("dl", dl);
            model.addAttribute("action", "/admin/nhan-vien/sua");

            return "admin/nhanvien/form-bs4.html";

        }

        @PostMapping("/nhan-vien/sua")
        public String postSua(@ModelAttribute("NhanVien") NhanVien dl, RedirectAttributes redirectAttributes) {

            dl.setMatKhau(BCrypt.hashpw(dl.getMatKhau(), BCrypt.gensalt(12)));

            dvl.lưuNhanVien(dl);

            // Gửi thông báo thành công từ view Add/Edit sang view List
            redirectAttributes.addFlashAttribute("THONG_BAO", "Đã sửa thành công !");

            return "redirect:/admin/nhan-vien/duyet";
        }

        @GetMapping("/nhan-vien/xoa")
        public String getXoa(Model model, @RequestParam(value = "id") int id) {
            // Lấy ra bản ghi theo id
            NhanVien dl = dvl.tìmNhanVienTheoId(id);

            // Gửi đối tượng dữ liệu sang bên view
            model.addAttribute("dl", dl);

            // Hiển thị view giao diện
            // Nội dung riêng của trang...
            model.addAttribute("content", "admin/nhanvien/xoa.html"); // xoa.html

            return "layouts/layout-admin.html";
        }

        @PostMapping("/nhan-vien/xoa")
        public String postXoa(@RequestParam("id") int id, RedirectAttributes redirectAttributes) {
            System.out.println("ID nhận được trong controller: " + id);

            // Xoá dữ liệu
            try {
                this.dvl.xóaNhanVien(id);
                redirectAttributes.addFlashAttribute("THONG_BAO", "Đã xóa thành công !");
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("THONG_BAO_ERROR",
                        "Không thể xóa nhân viên. Lỗi: " + e.getMessage());
            }

            // Điều hướng sang trang giao diện
            return "redirect:/admin/nhan-vien/duyet";
        }

        @GetMapping("/nhan-vien/xem")
        public String getXem(Model model, @RequestParam(value = "id") int id) {
            // Lấy ra bản ghi theo id
            NhanVien dl = dvl.xemNhanVien(id);
            // Gửi đối tượng dữ liệu sang bên view
            model.addAttribute("dl", dl);

            // Nội dung riêng của trang...
            model.addAttribute("content", "admin/nhanvien/xem.html");

            return "layouts/layout-admin.html";
        }

    @GetMapping("/nhan-vien/tim-kiem")
    public String getTimKiem(Model model, @RequestParam("criteria") String criteria, @RequestParam("query") String query) {
        List<NhanVien> results = new ArrayList<>();
        
        switch (criteria) {
            case "Id":
                try {
                    int id = Integer.parseInt(query);
                    NhanVien nv = dvl.timNhanVienTheoId(id);
                    if (nv != null) results.add(nv);
                } catch (NumberFormatException e) {
                    model.addAttribute("THONG_BAO", "ID phải là số nguyên.");
                }
                break;
            case "tenDayDu":
                results = dvl.timKiemTheoTen(query);
                break;
            case "email":
                results = dvl.timKiemTheoEmail(query);
                break;
            case "dienThoai":
                results = dvl.timKiemTheoDienThoai(query);
                break;
            default:
                model.addAttribute("THONG_BAO", "Tiêu chí tìm kiếm không hợp lệ.");
                break;
        }

        model.addAttribute("ds", results);
        model.addAttribute("dl", new NhanVien());
        model.addAttribute("action", "/admin/nhan-vien/them");
        model.addAttribute("title_body", "Thêm Nhân Viên");
        model.addAttribute("title_sm", "Thêm mới");
        model.addAttribute("title", "Quản Lý Nhân Viên");

        // Nội dung riêng của trang...
        model.addAttribute("content", "admin/nhanvien/duyet.html");

        return "layouts/layout-admin.html";
    }

        // Register User Account (Đăng ký tài khoản, có mật khẩu)
        // Sign In, Sign Out, Sign Up (user)
        // Log In , Log Out, Register (customer)

        @GetMapping("/dang-nhap")
        public String getDangNhap(Model model, HttpSession session) {

            System.out.println("\n uri before login: " + (String) session.getAttribute("URI_BEFORE_LOGIN"));
            model.addAttribute("dl", new NhanVien());
            // Lấy ra bản ghi theo id
            model.addAttribute("content", "admin/nhanvien/dangnhap.html");

            // ...được đặt vào bố cục chung của toàn website
            // return "layout.html";
            return "layouts/layout-admin-login.html";
        }

        @PostMapping("/dang-nhap")
        public String postDangNhap(Model model,
                RedirectAttributes redirectAttributes,
                @RequestParam("TenDangNhap") String TenDangNhap,
                @RequestParam("MatKhau") String MatKhau,
                HttpServletRequest request,
                HttpSession session) {

            // đừng viết là dl.getMatKhau() vì nó sẽ chết !

            String old_password = null;
            // var old_dl = dvl.tìmNhanVienTheoTenDangNhap(TenDangNhap);
            if// Nếu tồn tại tên đăng nhập trong csdl
              // (old_dl!=null)// chạy OK
            (dvl.tồnTạiTênĐăngNhập(TenDangNhap)) {
                var old_dl = dvl.tìmNhanVienTheoTenDangNhap(TenDangNhap);
                System.out.println(old_dl.getTenDangNhap());
                old_password = old_dl.getMatKhau();
                // So sánh mật khẩu trên Form và trong MySQL
                // xem có khớp không
                var dung_mat_khau = BCrypt.checkpw(MatKhau, old_password);

                if// nếu
                (dung_mat_khau) {
                    System.out.println("\n Đúng tài khoản, đăng nhập thành công");

                    request.getSession().setAttribute("NhanVien_Id",
                            old_dl.getId());
                    request.getSession().setAttribute("NhanVien_TenDayDu",
                            old_dl.getTenDayDu());

                } else {
                    System.out.println("\n Sai mật khẩu");
                    // Gửi thông báo thành công từ view Add/Edit sang view List
                    redirectAttributes.addFlashAttribute("THONG_BAO", "Sai mật khẩu !");
                    return "redirect:/admin/dang-nhap";
                }
            } else {
                System.out.println("\n Không tồn tại tên đăng nhập");
                // Gửi thông báo thành công từ view Add/Edit sang view List
                redirectAttributes.addFlashAttribute("THONG_BAO", "Sai tên đăng nhập hoặc mật khẩu !");
                return "redirect:/admin/dang-nhap";
            }

            var uriBeforeLogin = (String) session.getAttribute("URI_BEFORE_LOGIN");
            if (uriBeforeLogin == null)
                uriBeforeLogin = "/admin/nhan-vien";
            return "redirect:" + uriBeforeLogin;
        }

        @GetMapping("/dang-xuat")
        public String getDangThoat(HttpServletRequest request) {

            request.getSession().invalidate();
            return "redirect:/admin/dang-nhap";
        }

    }

}
// end class
