package fun.cmgraph.mapper;

import fun.cmgraph.entity.BundleProduct;
import fun.cmgraph.entity.BundleProductWithPic;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface BundleProductMapper {
    void insertBatch(List<BundleProduct> bundleProducts);

    @Select("select * from setmeal_dish where setmeal_id = #{id}")
    List<BundleProduct> getDishesBySetmealId(Integer id);

    @Delete("delete from setmeal_dish where setmeal_id = #{setmealId}")
    void deleteBySetmealId(Integer setmealId);

    void deleteBatch(List<Integer> ids);

    @Select("select sd.*, d.pic from setmeal_dish sd left join dish d on sd.dish_id = d.id where sd.setmeal_id = #{id}")
    List<BundleProductWithPic> getDishesWithPic(Integer id);

    List<Integer> getSetmealIdsByDishIds(List<Integer> ids);
}
