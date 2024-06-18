package fun.cmgraph.controller.user;

import fun.cmgraph.entity.Setmeal;
import fun.cmgraph.entity.SetmealDish;
import fun.cmgraph.entity.SetmealDishWithPic;
import fun.cmgraph.result.Result;
import fun.cmgraph.service.SetmealService;
import fun.cmgraph.vo.DishItemVO;
import fun.cmgraph.vo.SetmealVO;
import fun.cmgraph.vo.SetmealWithPicVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController("userSetmealController")
@RequestMapping("/user/setmeal")
@Slf4j
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    /**
     * 根据分类id查询所有套餐
     * @param id
     * @return
     */
    @GetMapping("/list/{id}")
    @Cacheable(cacheNames = "setmealCache", key = "#id")
    public Result<List<Setmeal>> getSetmealList(@PathVariable Integer id){
        log.info("要查询的套餐分类id:{}", id);
        List<Setmeal> setmealList = setmealService.getList(id);
        return Result.success(setmealList);
    }

    /**
     * 根据套餐id查询套餐
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<SetmealWithPicVO> getSetmeal(@PathVariable Integer id){
        log.info("根据套餐id查询该套餐：{}", id);
        SetmealWithPicVO setmealWithPicVO = setmealService.getSetmealWithPic(id);
        return Result.success(setmealWithPicVO);
    }

    /**
     * 根据套餐查询包含的菜品
     * @return
     */
    @GetMapping("/dish/{id}")
    public Result<List<DishItemVO>> getSetmealDishes(@PathVariable Integer id){
        log.info("套餐id:{}", id);
        List<DishItemVO> dishes = setmealService.getSetmealDishesById(id);
        return Result.success(dishes);
    }
}
