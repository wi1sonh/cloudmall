package fun.cmgraph.service;

import fun.cmgraph.dto.SetmealDTO;
import fun.cmgraph.dto.SetmealPageDTO;
import fun.cmgraph.entity.Setmeal;
import fun.cmgraph.result.PageResult;
import fun.cmgraph.vo.DishItemVO;
import fun.cmgraph.vo.SetmealVO;
import fun.cmgraph.vo.SetmealWithPicVO;

import java.util.List;

public interface SetmealService {
    void addSetmeal(SetmealDTO setmealDTO);

    PageResult getPageList(SetmealPageDTO setmealPageDTO);

    SetmealVO getSetmealById(Integer id);

    void onOff(Integer id);

    void update(SetmealDTO setmealDTO);

    void deleteBatch(List<Integer> ids);

    List<Setmeal> getList(Integer categoryId);

    List<DishItemVO> getSetmealDishesById(Integer id);

    SetmealWithPicVO getSetmealWithPic(Integer id);
}
