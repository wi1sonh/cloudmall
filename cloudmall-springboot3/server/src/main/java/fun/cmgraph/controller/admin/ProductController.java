package fun.cmgraph.controller.admin;

import fun.cmgraph.dto.ProductDTO;
import fun.cmgraph.dto.ProductPageDTO;
import fun.cmgraph.result.PageResult;
import fun.cmgraph.result.Result;
import fun.cmgraph.service.ProductService;
import fun.cmgraph.vo.ProductVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/admin/dish")
@Slf4j
public class ProductController {

    @Autowired
    private ProductService productService;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 添加商品
     * @return
     */
    @PostMapping
    public Result addProduct(@RequestBody ProductDTO productDTO){
        log.info("要添加的商品信息：{}", productDTO);
        productService.addProduct(productDTO);
        // 清理缓存数据
        String key = "dish_" + productDTO.getCategoryId();
        cleanCache(key);
        return Result.success();
    }

    /**
     * 商品条件分页查询
     * @param productPageDTO
     * @return
     */
    @GetMapping("/page")
    public Result<PageResult> getPageList(ProductPageDTO productPageDTO){
        log.info("商品dish分页条件page：{}", productPageDTO);
        PageResult pageResult = productService.getPageList(productPageDTO);
        return Result.success(pageResult);
    }

    /**
     * 根据id查询商品
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<ProductVO> getProductById(@PathVariable Integer id){
        log.info("根据id查询商品：{}", id);
        ProductVO productVO = productService.getProductById(id);
        return Result.success(productVO);
    }

    /**
     * 根据商品名称查询商品
     * @param name 商品名称
     * @return
     */
    @GetMapping("/name/{name}")
    public Result<ProductVO> getProductByName(@PathVariable String name){
        log.info("根据商品名称查询商品：{}", name);
        ProductVO productVO = productService.getProductByName(name);
        return Result.success(productVO);
    }

    /**
     * 根据id修改起售停售状态
     * @param id
     * @return
     */
    @PutMapping("/status/{id}")
    public Result onOff(@PathVariable Integer id){
        log.info("根据id修改状态，{}", id);
        productService.onOff(id);
        // 将所有的商品缓存数据清理掉，所有以dish_开头的key
        cleanCache("dish_*");
        return Result.success();
    }

    /**
     * 更新商品以及对应口味
     * @param productDTO
     * @return
     */
    @PutMapping
    public Result updateProduct(@RequestBody ProductDTO productDTO){
        log.info("用户传过来的新商品数据，{}", productDTO);
        productService.updateProduct(productDTO);
        // 将所有的商品缓存数据清理掉，所有以dish_开头的key
        cleanCache("dish_*");
        return Result.success();
    }

    /**
     * 根据ids批量删除商品数据
     * @RequestParam 表示必须要ids参数，否则会报错
     * @param ids
     * @return
     */
    @DeleteMapping
    public Result deleteBatch(@RequestParam List<Integer> ids){
        log.info("要删除的商品id列表，{}", ids);
        productService.deleteBatch(ids);
        // 将所有的商品缓存数据清理掉，所有以dish_开头的key
        cleanCache("dish_*");
        return Result.success();
    }

    /**
     * 清理缓存数据
     * @param pattern
     */
    public void cleanCache(String pattern){
        Set keys = redisTemplate.keys(pattern);
        redisTemplate.delete(keys);
    }
}
