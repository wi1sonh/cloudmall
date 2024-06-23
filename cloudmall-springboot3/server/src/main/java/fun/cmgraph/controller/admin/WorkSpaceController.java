package fun.cmgraph.controller.admin;

import fun.cmgraph.result.Result;
import fun.cmgraph.service.WorkSpaceService;
import fun.cmgraph.vo.BusinessDataVO;
import fun.cmgraph.vo.ProductOverViewVO;
import fun.cmgraph.vo.OrderOverViewVO;
import fun.cmgraph.vo.BundleOverViewVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.LocalTime;

@RestController
@RequestMapping("/admin/workspace")
@Slf4j
public class WorkSpaceController {

    @Autowired
    private WorkSpaceService workSpaceService;

    /**
     * 工作台今日数据查询
     * @return
     */
    @GetMapping("/businessData")
    public Result<BusinessDataVO> businessData(){
        LocalDateTime begin = LocalDateTime.now().with(LocalTime.MIN);
        LocalDateTime end = LocalDateTime.now().with(LocalTime.MAX);
        BusinessDataVO businessDataVO = workSpaceService.getBusinessData(begin, end);
        return Result.success(businessDataVO);
    }

    /**
     * 查询订单管理数据
     * @return
     */
    @GetMapping("/overviewOrders")
    public Result<OrderOverViewVO> orderOverView(){
        OrderOverViewVO orderOverViewVO = workSpaceService.getOrderOverView();
        return Result.success(orderOverViewVO);
    }

    /**
     * 查询商品总览
     * @return
     */
    @GetMapping("/overviewDishes")
    public Result<ProductOverViewVO> dishOverView(){
        ProductOverViewVO productOverViewVO = workSpaceService.getDishOverView();
        return Result.success(productOverViewVO);
    }

    /**
     * 查询套餐总览
     * @return
     */
    @GetMapping("/overviewSetmeals")
    public Result<BundleOverViewVO> setmealOverView(){
        BundleOverViewVO bundleOverViewVO = workSpaceService.getSetmealOverView();
        return Result.success(bundleOverViewVO);
    }
}
