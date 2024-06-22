package fun.cmgraph.mapper;

import com.github.pagehelper.Page;
import fun.cmgraph.annotation.AutoFill;
import fun.cmgraph.dto.BundlePageDTO;
import fun.cmgraph.entity.Bundle;
import fun.cmgraph.enumeration.OperationType;
import fun.cmgraph.vo.ProductItemVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface BundleMapper {

    @AutoFill(OperationType.INSERT)
    void addSetmeal(Bundle bundle);

    Page<Bundle> getPageList(BundlePageDTO bundlePageDTO);

    @Select("select * from setmeal where id = #{id}")
    Bundle getSetmealById(Integer id);

    @Update("update setmeal set status = IF(status = 1, 0, 1) where id = #{id}")
    void onOff(Integer id);

    @AutoFill(OperationType.UPDATE)
    void update(Bundle bundle);

    void deleteBatch(List<Integer> ids);

    List<Bundle> getList(Bundle bundle);

    @Select("select s.name, s.pic, s.detail, sd.copies from " +
            "setmeal s left join setmeal_dish sd on s.id = sd.dish_id " +
            "where sd.setmeal_id = #{id}")
    List<ProductItemVO> getSetmealDishesById(Integer id);

    @Select("select count(id) from setmeal where status = #{i}")
    Integer getByStatus(int i);
}