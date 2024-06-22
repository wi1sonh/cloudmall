package fun.cmgraph.service;

import fun.cmgraph.vo.BusinessDataVO;
import fun.cmgraph.vo.ProductOverViewVO;
import fun.cmgraph.vo.OrderOverViewVO;
import fun.cmgraph.vo.BundleOverViewVO;

import java.time.LocalDateTime;

public interface WorkSpaceService {
    BusinessDataVO getBusinessData(LocalDateTime begin, LocalDateTime end);

    OrderOverViewVO getOrderOverView();

    ProductOverViewVO getDishOverView();

    BundleOverViewVO getSetmealOverView();
}
