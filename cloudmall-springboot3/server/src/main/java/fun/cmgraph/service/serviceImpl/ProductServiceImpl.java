package fun.cmgraph.service.serviceImpl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import fun.cmgraph.constant.MessageConstant;
import fun.cmgraph.constant.StatusConstant;
import fun.cmgraph.dto.ProductDTO;
import fun.cmgraph.dto.ProductPageDTO;
import fun.cmgraph.entity.Product;
import fun.cmgraph.entity.DishFlavor;
import fun.cmgraph.exception.DeleteNotAllowedException;
import fun.cmgraph.mapper.DishFlavorMapper;
import fun.cmgraph.mapper.ProductMapper;
import fun.cmgraph.mapper.BundleProductMapper;
import fun.cmgraph.result.PageResult;
import fun.cmgraph.service.ProductService;
import fun.cmgraph.vo.ProductVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;
    @Autowired
    private BundleProductMapper bundleProductMapper;

    /**
     * 新增商品
     * @param productDTO
     */
    public void addProduct(ProductDTO productDTO) {

        Product product = new Product();
        BeanUtils.copyProperties(productDTO, product);
        product.setStatus(1);
        productMapper.addProduct(product);
        System.out.println("新增商品成功！");
        // 由于在动态sql中，用了useGeneralKeys=true，因此会在插入数据后自动返回该行数据的主键id，
        // 并且使用keyProperty="id"，表示将返回的主键值赋值给dish的id属性，下面就可以dish.getId()获取到id
        Integer dishId = product.getId();

        List<DishFlavor> flavorList = productDTO.getFlavors();
        if (flavorList != null && !flavorList.isEmpty()) {
            flavorList.forEach(dishFlavor -> dishFlavor.setDishId(dishId));
            dishFlavorMapper.insertBatch(flavorList);
        }
    }

    /**
     * 根据条件page信息分页查询商品
     * @param productPageDTO 商品分页查询条件
     * @return PageResult
     */
    public PageResult getPageList(ProductPageDTO productPageDTO) {
        PageHelper.startPage(productPageDTO.getPage(), productPageDTO.getPageSize());
        Page<Product> productList = productMapper.getPageList(productPageDTO);
        return new PageResult(productList.getTotal(), productList.getResult());
    }

    /**
     * 根据id查询对应的商品
     * @param id
     * @return
     */
    public ProductVO getProductById(Integer id) {
        // 使用 useGenerateKey 和 keyProperty 来返回对应的id
        Product product = productMapper.getById(id);

        List<DishFlavor> dishFlavors = dishFlavorMapper.getByDishId(id);
        ProductVO productVO = new ProductVO();
        BeanUtils.copyProperties(product, productVO);
        productVO.setFlavors(dishFlavors);
        return productVO;
    }

    /**
     * 更新商品
     * @param productDTO
     */
    public void updateProduct(ProductDTO productDTO) {
        Product product = new Product();
        BeanUtils.copyProperties(productDTO, product);
        // 先根据id更新商品数据
        productMapper.update(product);
        // 再根据where dishId=商品id
        Integer dishId = productDTO.getId();
        // 1. 根据dishId批量删除
        dishFlavorMapper.deleteByDishId(dishId);
        List<DishFlavor> flavorList = productDTO.getFlavors();
        if (flavorList != null && !flavorList.isEmpty()){
            flavorList.forEach(dishFlavor -> dishFlavor.setDishId(dishId));
            // 2. 再批量插入口味数据
            dishFlavorMapper.insertBatch(flavorList);
        }
    }

    /**
     * 根据商品id列表，批量删除商品
     * @param ids
     */
    public void deleteBatch(List<Integer> ids) {
        // 1. 遍历所有商品，如果有任何一个在起售，则抛异常表示不能删除
        for (Integer id : ids){
            Product product = productMapper.getById(id);
            if (product.getStatus() == StatusConstant.ENABLE){
                throw new DeleteNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
        }
        // 2. 遍历所有商品，如果有关联套餐也不能删除
        List<Integer> setmealIds = bundleProductMapper.getSetmealIdsByDishIds(ids);
        if (setmealIds != null && !setmealIds.isEmpty()){
            throw new DeleteNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }
        // 可以批量删除商品和对应口味数据了
        productMapper.deleteBatch(ids);
        dishFlavorMapper.deleteBatch(ids);
    }

    /**
     * 根据id修改起售停售状态
     * @param id
     */
    public void onOff(Integer id) {
        productMapper.onOff(id);
    }

    /**
     * 获取对应分类下的所有商品，包括对应口味
     * @param product
     * @return
     */
    public List<ProductVO> getProductsById(Product product) {
        List<Product> productList = productMapper.getList(product);
        List<ProductVO> productVOList = new ArrayList<>();
        // 对商品列表的每个商品都加上对应口味，分别封装成DishVO再返回
        for (Product d : productList){
            ProductVO productVO = new ProductVO();
            BeanUtils.copyProperties(d, productVO);
            List<DishFlavor> flavors = dishFlavorMapper.getByDishId(d.getId());
            productVO.setFlavors(flavors);
            productVOList.add(productVO);
        }
        return productVOList;
    }

}
