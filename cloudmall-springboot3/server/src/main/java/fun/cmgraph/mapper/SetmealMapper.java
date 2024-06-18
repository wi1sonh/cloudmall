package fun.cmgraph.mapper;

import com.github.pagehelper.Page;
import fun.cmgraph.annotation.AutoFill;
import fun.cmgraph.dto.SetmealDTO;
import fun.cmgraph.dto.SetmealPageDTO;
import fun.cmgraph.entity.Setmeal;
import fun.cmgraph.enumeration.OperationType;
import fun.cmgraph.vo.DishItemVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper
public interface SetmealMapper {

    @AutoFill(OperationType.INSERT)
    void addSetmeal(Setmeal setmeal);

    Page<Setmeal> getPageList(SetmealPageDTO setmealPageDTO);

    @Select("select * from setmeal where id = #{id}")
    Setmeal getSetmealById(Integer id);

    @Update("update setmeal set status = IF(status = 1, 0, 1) where id = #{id}")
    void onOff(Integer id);

    @AutoFill(OperationType.UPDATE)
    void update(Setmeal setmeal);

    void deleteBatch(List<Integer> ids);

    List<Setmeal> getList(Setmeal setmeal);

    @Select("select s.name, s.pic, s.detail, sd.copies from " +
            "setmeal s left join setmeal_dish sd on s.id = sd.dish_id " +
            "where sd.setmeal_id = #{id}")
    List<DishItemVO> getSetmealDishesById(Integer id);

    @Select("select count(id) from setmeal where status = #{i}")
    Integer getByStatus(int i);
}
