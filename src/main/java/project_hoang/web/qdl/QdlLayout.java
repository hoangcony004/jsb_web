package project_hoang.web.qdl;


import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import project_hoang.web.admin.caidat.DvlCaiDat;



// Lớp này dùng để khởi tạo các dữ liệu toàn cục
// được chia sẻ chung bởi các trang *.html
// ví dụ: email, phone, web name, ...
@ControllerAdvice
// public class QdlGlobal
public class QdlLayout
{
    // Global
    @Autowired
    DvlCaiDat dvlCaiDat;

    @ModelAttribute("testGlobalText")
    public String getTest(){
        return "THIS IS A GLOBAL TEXT for All *.html(s), really cool ";
    }

    @ModelAttribute("caiDatTinh")
    public HashMap<String, String> getCaiDatTinh() {
        var map = new HashMap<String, String>();
        map.put("staticAppNam", "Web Shop Blog");
        map.put("staticTel", "19001010");

        return map;
    }

     @ModelAttribute("caidat")
    public HashMap<String, String> getCaiDat() {
        var map = new HashMap<String, String>();

        var list = dvlCaiDat.duyệtCaiDat();
        for(var obj : list){
            map.put(obj.getKhoa(), obj.getGiaTri());
        }
        return map;
    }

    /**
     * Kiểm tra xem nhân viên đăng nhập hay chưa ?
     * (Id của NhanVien không tồn tại trong Session)
     * Đánh dấu địa chỉ đường dẫn URI mà người dùng đang cố truy cập.
     * 
     * @param session
     * @param request
     * @return
     */
    public static Boolean 
    neuNhanVienChuaDangNhap(HttpSession session, HttpServletRequest request) 
    {
        if// nếu danh tính không xác định
        (session.getAttribute("NhanVien_Id") == null) 
        {
            // thì Đánh dấu nội trang mà người dùng muốn truy cập nhưng bị chặn
            request.getSession().setAttribute("URI_BEFORE_LOGIN", request.getRequestURI());

            // rồi điều hướng sang trang đăng nhập
            return true;

        }

        return false;
    }

    // Không nhận được Session
    // neuNhanVienChuaDangNhap(HttpServletRequest request) 
    // {
    //     if// nếu danh tính không xác định
    //     (request.getSession().getAttribute("NhanVien_Id") == null) 
    //     {
    //         System.out.println("QdlLayout chua dang nhap........");
    //         // thì Đánh dấu nội trang mà người dùng muốn truy cập nhưng bị chặn
    //         request.getSession().setAttribute("URI_BEFORE_LOGIN", request.getRequestURI());

    //         // rồi điều hướng sang trang đăng nhập
    //         return true;

    //     }

    //     return false;
    // }

      /**
     * Kiểm tra xem nhân viên đăng nhập hay chưa ?
     * (Id của NhanVien không tồn tại trong Session)
     * Đánh dấu địa chỉ đường dẫn URI mà người dùng đang cố truy cập.
     * 
     * @param session
     * @param request
     * @return
     */
    public static Boolean 
    neuDanhTinhNhanVienKhongXacDinh(HttpServletRequest request) 
    {
        if// nếu danh tính không xác định
        (request.getSession().getAttribute("NhanVien_Id") == null) 
        {
            // thì Đánh dấu nội trang mà người dùng muốn truy cập nhưng bị chặn
            request.getSession().setAttribute("URI_BEFORE_LOGIN", request.getRequestURI());

            // rồi điều hướng sang trang đăng nhập
            return true;

        }

        return false;
    }


}
// layout.phtml
//<div th:text="testGlobal"></div>
