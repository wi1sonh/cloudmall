package fun.cmgraph.controller.user;

import fun.cmgraph.constant.StatusConstant;
import fun.cmgraph.entity.Product;
import fun.cmgraph.result.Result;
import fun.cmgraph.service.ProductService;
import fun.cmgraph.vo.ProductVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("userDishController")
@RequestMapping("/user/dish")
@Slf4j
public class ProductController {

    @Autowired
    private ProductService productService;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 根据分类id查询该分类下的所有商品
     *
     * @return
     */
    @GetMapping("/list/{id}")
    public Result<List<ProductVO>> getDishList(@PathVariable Integer id) {
        log.info("要查询当前的分类categoryId下的所有商品：{}", id);
        // 构造redis中的key，规则：dish_分类id
        String key = "dish_" + id;
        // 查询redis中是否存在商品数据
        List<ProductVO> dishes = (List<ProductVO>) redisTemplate.opsForValue().get(key);
        if (dishes != null && !dishes.isEmpty()) {
            //如果存在，直接返回，无须查询数据库
            return Result.success(dishes);
        }
        // 用户端除了分类条件限制，且只能展示起售中的商品，因此还有status限制
        Product product = new Product();
        product.setCategoryId(id);
        product.setStatus(StatusConstant.ENABLE);
        // 如果不存在，查询数据库，将查询到的数据放入redis中
        dishes = productService.getProductsById(product);
        redisTemplate.opsForValue().set(key, dishes);
        return Result.success(dishes);
    }

    /**
     * 根据商品id查询商品详情
     *
     * @param id
     * @return
     */
    @GetMapping("/dish/{id}")
    public Result<ProductVO> getDish(@PathVariable Integer id) {
        log.info("用户根据商品id查询商品详情：{}", id);
        ProductVO productVO = productService.getProductById(id);
        return Result.success(productVO);
    }

}
