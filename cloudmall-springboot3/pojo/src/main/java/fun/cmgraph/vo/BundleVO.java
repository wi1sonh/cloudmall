package fun.cmgraph.vo;

import fun.cmgraph.entity.BundleProduct;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BundleVO implements Serializable {

    private Integer id;
    private String name;
    private String pic;
    private String detail;
    private BigDecimal price;
    private String status;
    private Integer categoryId;
    // 修改事件要记录，就比DTO多了这个字段而已
    private LocalDateTime updateTime;
    // 当前套餐包含的多种商品
    private List<BundleProduct> bundleProducts = new ArrayList<>();

}
