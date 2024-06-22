package fun.cmgraph.controller.admin;

import fun.cmgraph.dto.BundleDTO;
import fun.cmgraph.dto.BundlePageDTO;
import fun.cmgraph.result.PageResult;
import fun.cmgraph.result.Result;
import fun.cmgraph.service.BundleService;
import fun.cmgraph.vo.BundleVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/setmeal")
@Slf4j
public class BundleController {

    @Autowired
    private BundleService bundleService;

    /**
     * 新增套餐
     * @param bundleDTO
     * @return
     */
    @PostMapping
    @CacheEvict(cacheNames = "setmealCache", key = "#bundleDTO.categoryId")
    public Result addBundle(@RequestBody BundleDTO bundleDTO){
        log.info("新增套餐的信息：{}", bundleDTO);
        bundleService.addBundle(bundleDTO);
        return Result.success();
    }

    /**
     * 套餐条件分页查询
     * @param bundlePageDTO
     * @return
     */
    @GetMapping("/page")
    public Result<PageResult> getPageList(BundlePageDTO bundlePageDTO){
        log.info("条件分页查询：{}", bundlePageDTO);
        PageResult pageResult = bundleService.getPageList(bundlePageDTO);
        return Result.success(pageResult);
    }

    /**
     * 根据id查询套餐
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<BundleVO> getBundleById(@PathVariable Integer id){
        log.info("要查询的套餐id：{}", id);
        BundleVO bundleVO = bundleService.getBundleById(id);
        return Result.success(bundleVO);
    }

    /**
     * 根据套餐名查询套餐
     * @param name
     * @return
     */
    @GetMapping("/name/{name}")
    public Result<BundleVO> getBundleByName(@PathVariable String name){
        log.info("要查询的套餐名：{}", name);
        BundleVO bundleVO = bundleService.getBundleByName(name);
        return Result.success(bundleVO);
    }

    /**
     * 根据id起售停售套餐
     * @param id
     * @return
     */
    @PutMapping("/status/{id}")
    @CacheEvict(cacheNames = "setmealCache", allEntries = true)
    public Result onOff(@PathVariable Integer id){
        log.info("套餐id:{}", id);
        bundleService.onOff(id);
        return Result.success();
    }

    /**
     * 修改套餐
     * @param bundleDTO
     * @return
     */
    @PutMapping
    @CacheEvict(cacheNames = "setmealCache", allEntries = true)
    public Result update(@RequestBody BundleDTO bundleDTO){
        log.info("修改后的套餐信息：{}", bundleDTO);
        bundleService.update(bundleDTO);
        return Result.success();
    }

    /**
     * 批量删除套餐
     * @param ids
     * @return
     */
    @DeleteMapping
    @CacheEvict(cacheNames = "setmealCache", allEntries = true)
    public Result deleteBatch(@RequestParam List<Integer> ids){
        log.info("批量删除套餐的套餐id集合：{}", ids);
        bundleService.deleteBatch(ids);
        return Result.success();
    }

}
