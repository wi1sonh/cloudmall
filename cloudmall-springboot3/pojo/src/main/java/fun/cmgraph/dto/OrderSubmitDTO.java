package fun.cmgraph.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrderSubmitDTO implements Serializable {

    private Integer addressId; // 地址簿id
    private int payMethod; // 付款方式
    private String remark; // 备注
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime estimatedDeliveryTime; // 预计送达时间
    private Integer deliveryStatus; // 运送状态  1立即送出  0选择具体时间
    private Integer tablewareNumber; // 购物袋数量
    private Integer tablewareStatus; // 购物袋数量状态  1按商品数量提供  0选择具体数量
    private Integer packAmount; // 打包费
    private BigDecimal amount; // 总金额
}