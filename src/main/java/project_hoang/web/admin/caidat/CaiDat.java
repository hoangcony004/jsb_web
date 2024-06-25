package project_hoang.web.admin.caidat;

//package congty.tdl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class CaiDat {
  @Id // Khóa chính
  @GeneratedValue(strategy = GenerationType.IDENTITY) // Tăng tự động từ 1,2,3,...
  private int Id;

  private String khoa;
  private String giaTri;

  public Boolean KhongHopLe() {
    var khl = false;

    // if (this.Ten.length() < 2) {
    // khl = true;
    // System.out.print("\n Lỗi->Tên phải từ 2 kí tự trở lên: ");
    // }

    // if (this.Ten.length() > 22) {
    // khl = true;
    // System.out.print("\n Lỗi->Tên phải không quá 22 kí tự. ");
    // }

    return khl;
  }

}// end class