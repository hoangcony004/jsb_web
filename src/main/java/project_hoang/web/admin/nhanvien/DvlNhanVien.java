package project_hoang.web.admin.nhanvien;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;



@Service
public class DvlNhanVien 
{
    @Autowired private KdlNhanVien kdl;// kho dữ liệu;

    @Autowired
    private KdlNhanVien nhanVienRepository;

    public Page<NhanVien> duyệtNhanVien(Pageable pageable) {
        return nhanVienRepository.findAll(pageable);
    }

    public NhanVien timNhanVienTheoId(int id) {
        return kdl.findById(id).orElse(null);
    }

    public List<NhanVien> timKiemTheoTen(String ten) {
        return kdl.findByTenDayDuContainingIgnoreCase(ten);
    }

    public List<NhanVien> timKiemTheoEmail(String email) {
        return kdl.findByEmailContainingIgnoreCase(email);
    }

    public List<NhanVien> timKiemTheoDienThoai(String dienThoai) {
        return kdl.findByDienThoaiContainingIgnoreCase(dienThoai);
    }

    public List<NhanVien> dsNhanVien() // getAllThucThe()
    {
  
        // return null;

        // mã bởi lập trình viên:
        return kdl.findAll();
    }

    public List<NhanVien>  duyệtNhanVien() 
    {
        return kdl.findAll();
    }

    public NhanVien  tìmNhanVienTheoId(int id)// 
    {
        // TODO Auto-generated method stub
        // return null;

        // return kdl.findById(id);

        NhanVien dl = null;

        Optional<NhanVien> optional = kdl.findById(id);

        if// nếu
        (optional.isPresent()) // tìm thấy bản ghi trong kho
        {
            dl = optional.get();
        } else// ngược lại
        {
            //throw new RuntimeException("Không tìm thấy thú cưng ! Ko tim thay thu cung !");
        }

        return dl;

    }

    public NhanVien xemNhanVien(int id)// 
    {

        NhanVien dl = null;

        Optional<NhanVien> optional = kdl.findById(id);

        if// nếu
        (optional.isPresent()) // tìm thấy bản ghi trong kho
        {
            dl = optional.get();
        } else// ngược lại
        {
            //throw new RuntimeException("Không tìm thấy thú cưng ! Ko tim thay thu cung !");
        }

        return dl;

    }

        public void lưuNhanVien(NhanVien dl)
    {
        // TODO Auto-generated method stub
        this.kdl.save(dl);
    }

        public void xóaNhanVien(int id)
    {
        // TODO Auto-generated method stub
        this.kdl.deleteById(id);
    }

        public NhanVien tìmNhanVienTheoTenDangNhap(String tdn)
    {
 // TODO Auto-generated method stub
        // return null;

        // return kdl.findById(id);

        NhanVien dl = null;

        // List<NhanVien> list = kdl.findByTenDangNhap(tdn);
        // if(list!=null && list.size()>0)
        //     dl = list.get(0);

        // if(kdl.existsByTenDangNhap(tdn))
        // {
        //     dl = kdl.findByTenDangNhap(tdn).get(0);
        // }

        dl = kdl.findOneByTenDangNhap(tdn);// OK

        // if// nếu
        // (dl==null) // tìm thấy bản ghi trong kho
        // {
            
        // } else// ngược lại
        // {
        //     //throw new RuntimeException("Không tìm thấy thú cưng ! Ko tim thay thu cung !");
        // }

        return dl;
    }

    
        public Boolean tồnTạiTênĐăngNhập(String tdn)
    {
        return kdl.existsByTenDangNhap(tdn);
    }



}
