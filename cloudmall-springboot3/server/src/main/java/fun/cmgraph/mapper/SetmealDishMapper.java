package fun.cmgraph.mapper;

import fun.cmgraph.entity.SetmealDish;
import fun.cmgraph.entity.SetmealDishWithPic;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetmealDishMapper {
    void insertBatch(List<SetmealDish> setmealDishes);

    @Select("select * from setmeal_dish where setmeal_id = #{id}")
    List<SetmealDish> getDishesBySetmealId(Integer id);

    @Delete("delete from setmeal_dish where setmeal_id = #{setmealId}")
    void deleteBySetmealId(Integer setmealId);

    void deleteBatch(List<Integer> ids);

    @Select("select sd.*, d.pic from setmeal_dish sd left join dish d on sd.dish_id = d.id where sd.setmeal_id = #{id}")
    List<SetmealDishWithPic> getDishesWithPic(Integer id);

    List<Integer> getSetmealIdsByDishIds(List<Integer> ids);
}
