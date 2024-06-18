package fun.cmgraph.mapper;

import com.github.pagehelper.Page;
import fun.cmgraph.annotation.AutoFill;
import fun.cmgraph.dto.DishDTO;
import fun.cmgraph.dto.DishPageDTO;
import fun.cmgraph.entity.Dish;
import fun.cmgraph.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface DishMapper {
    @AutoFill(OperationType.INSERT)
    void addDish(Dish dish);

    Page<Dish> getPageList(DishPageDTO dishPageDTO);

    @Select("select * from dish where id = #{id}")
    Dish getById(Integer id);

    @AutoFill(OperationType.UPDATE)
    void update(Dish dish);

    void deleteBatch(List<Integer> ids);

    @Update("update dish set status = IF(status = 0, 1, 0) where id = #{id}")
    void onOff(Integer id);

    List<Dish> getList(Dish dish);

    @Select("select count(id) from dish where status = #{i}")
    Integer getByStatus(int i);
}
