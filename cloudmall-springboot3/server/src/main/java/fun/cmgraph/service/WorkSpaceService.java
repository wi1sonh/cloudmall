package fun.cmgraph.service;

import fun.cmgraph.vo.BusinessDataVO;
import fun.cmgraph.vo.DishOverViewVO;
import fun.cmgraph.vo.OrderOverViewVO;
import fun.cmgraph.vo.SetmealOverViewVO;

import java.time.LocalDateTime;

public interface WorkSpaceService {
    BusinessDataVO getBusinessData(LocalDateTime begin, LocalDateTime end);

    OrderOverViewVO getOrderOverView();

    DishOverViewVO getDishOverView();

    SetmealOverViewVO getSetmealOverView();
}
