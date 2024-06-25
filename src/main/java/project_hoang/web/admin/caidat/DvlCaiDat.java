package project_hoang.web.admin.caidat;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DvlCaiDat
{
    @Autowired private KdlCaiDat kdl;// kho dữ liệu;

    public List<CaiDat> dsCaiDat() // getAllCaiDat()
    {
  
        // return null;

        // mã bởi lập trình viên:
        return kdl.findAll();
    }

    public List<CaiDat>  duyệtCaiDat() 
    {
        return kdl.findAll();
    }

    public CaiDat  tìmCaiDatTheoId(int id)// 
    {
        // TODO Auto-generated method stub
        // return null;

        // return kdl.findById(id);

        CaiDat dl = null;

        Optional<CaiDat> optional = kdl.findById(id);

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

    public CaiDat xemCaiDat(int id)// 
    {

        CaiDat dl = null;

        Optional<CaiDat> optional = kdl.findById(id);

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

    
    public void lưuCaiDat(CaiDat dl)
    {
        // TODO Auto-generated method stub
        this.kdl.save(dl);
    }


    public void xóaCaiDat(int id)
    {
        // TODO Auto-generated method stub
        this.kdl.deleteById(id);
    }

}
