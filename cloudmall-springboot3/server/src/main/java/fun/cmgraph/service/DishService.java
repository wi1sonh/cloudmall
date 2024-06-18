package fun.cmgraph.service;

import fun.cmgraph.dto.DishDTO;
import fun.cmgraph.dto.DishPageDTO;
import fun.cmgraph.entity.Dish;
import fun.cmgraph.result.PageResult;
import fun.cmgraph.vo.DishVO;

import java.util.List;

public interface DishService {
    void addDishWithFlavor(DishDTO dishDTO);

    PageResult getPageList(DishPageDTO dishPageDTO);

    DishVO getDishWithFlavorById(Integer id);

    void updateDishWithFlavor(DishDTO dishDTO);

    void deleteBatch(List<Integer> ids);

    void onOff(Integer id);

    List<DishVO> getDishesWithFlavorById(Dish dish);
}
