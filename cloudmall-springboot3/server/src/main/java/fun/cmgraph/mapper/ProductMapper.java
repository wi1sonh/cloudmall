package fun.cmgraph.mapper;

import com.github.pagehelper.Page;
import fun.cmgraph.annotation.AutoFill;
import fun.cmgraph.dto.ProductPageDTO;
import fun.cmgraph.entity.Product;
import fun.cmgraph.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ProductMapper {
    @AutoFill(OperationType.INSERT)
    void addDish(Product product);

    Page<Product> getPageList(ProductPageDTO productPageDTO);

    @Select("select * from dish where id = #{id}")
    Product getById(Integer id);

    @AutoFill(OperationType.UPDATE)
    void update(Product product);

    void deleteBatch(List<Integer> ids);

    @Update("update dish set status = IF(status = 0, 1, 0) where id = #{id}")
    void onOff(Integer id);

    List<Product> getList(Product product);

    @Select("select count(id) from dish where status = #{i}")
    Integer getByStatus(int i);
}
