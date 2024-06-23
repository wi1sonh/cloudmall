package fun.cmgraph.service.serviceImpl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import fun.cmgraph.constant.MessageConstant;
import fun.cmgraph.constant.StatusConstant;
import fun.cmgraph.dto.BundleDTO;
import fun.cmgraph.dto.BundlePageDTO;
import fun.cmgraph.entity.Bundle;
import fun.cmgraph.entity.BundleProduct;
import fun.cmgraph.entity.BundleProductWithPic;
import fun.cmgraph.exception.DeleteNotAllowedException;
import fun.cmgraph.mapper.BundleProductMapper;
import fun.cmgraph.mapper.BundleMapper;
import fun.cmgraph.result.PageResult;
import fun.cmgraph.service.BundleService;
import fun.cmgraph.vo.ProductItemVO;
import fun.cmgraph.vo.BundleVO;
import fun.cmgraph.vo.SetmealWithPicVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BundleServiceImpl implements BundleService {

    @Autowired
    private BundleMapper bundleMapper;
    @Autowired
    private BundleProductMapper bundleProductMapper;

    /**
     * 新增套餐
     * @param bundleDTO
     */
    public void addBundle(BundleDTO bundleDTO) {
        Bundle bundle = new Bundle();
        BeanUtils.copyProperties(bundleDTO, bundle);
        bundle.setStatus(1);  // 默认启用套餐
        bundleMapper.addBundle(bundle);
        // 套餐包含的商品批量插入
        Integer setmealId = bundle.getId();
        // 1. 遍历setmealDTO中的商品列表，每个商品都为其setmealId字段赋值
        List<BundleProduct> bundleProducts = bundleDTO.getBundleProducts();
        if (bundleProducts != null && !bundleProducts.isEmpty()) {
            bundleProducts.forEach(setmealDish -> setmealDish.setSetmealId(setmealId));
            bundleProductMapper.insertBatch(bundleProducts);
        }
    }

    /**
     * 套餐条件分页查询
     * @param bundlePageDTO
     * @return
     */
    public PageResult getPageList(BundlePageDTO bundlePageDTO) {
        PageHelper.startPage(bundlePageDTO.getPage(), bundlePageDTO.getPageSize());
        Page<Bundle> bundleList = bundleMapper.getPageList(bundlePageDTO);
        return new PageResult(bundleList.getTotal(), bundleList.getResult());
    }

    /**
     * 根据套餐id查询套餐，包括商品信息
     * @param id
     * @return
     */
    public BundleVO getBundleById(Integer id) {
        Bundle bundle = bundleMapper.getBundleById(id);
        List<BundleProduct> bundleProducts = bundleProductMapper.getDishesBySetmealId(id);
        // 组成SetmealVO后返回
        BundleVO bundleVO = new BundleVO();
        BeanUtils.copyProperties(bundle, bundleVO);
        bundleVO.setBundleProducts(bundleProducts);
        return bundleVO;
    }

    /**
     * 根据套餐名称查询套餐
     * @param name
     * @return
     */
    public BundleVO getBundleByName(String name) {
        Bundle bundle = bundleMapper.getBundleByName(name);
        List<BundleProduct> bundleProducts = bundleProductMapper.getDishesBySetmealId(bundle.getId());
        // 组成SetmealVO后返回
        BundleVO bundleVO = new BundleVO();
        BeanUtils.copyProperties(bundle, bundleVO);
        bundleVO.setBundleProducts(bundleProducts);
        return bundleVO;
    }

    /**
     * 起售停售套餐
     * @param id
     */
    public void onOff(Integer id) {
        bundleMapper.onOff(id);
    }

    /**
     * 修改套餐
     * @param bundleDTO
     */
    public void update(BundleDTO bundleDTO) {
        Bundle bundle = new Bundle();
        BeanUtils.copyProperties(bundleDTO, bundle);
        // 先修改套餐setmeal
        bundleMapper.update(bundle);
        // 再修改套餐下的商品setmealDish
        // 由于行数据可能不同，因此需要先根据setmealId批量删除，再批量插入
        Integer setmealId = bundleDTO.getId();
        bundleProductMapper.deleteBySetmealId(setmealId);
        List<BundleProduct> bundleProducts = bundleDTO.getBundleProducts();
        bundleProducts.forEach(setmealDish -> setmealDish.setSetmealId(setmealId));
        bundleProductMapper.insertBatch(bundleProducts);
    }

    /**
     * 批量删除套餐
     * @param ids
     */
    public void deleteBatch(List<Integer> ids) {
        // 遍历要删除的所有套餐，如果但凡有一个在起售就抛异常
        for(Integer id : ids){
            Bundle bundle = bundleMapper.getBundleById(id);
            if (bundle.getStatus() == StatusConstant.ENABLE){
                throw new DeleteNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
            }
        }
        bundleMapper.deleteBatch(ids);
        bundleProductMapper.deleteBatch(ids);
    }

    /**
     * 根据分类id查询所有套餐
     * @return
     */
    public List<Bundle> getList(Integer categoryId) {
        // 还有一个条件：起售的套餐才能展示在客户端
        Bundle bundle = new Bundle();
        bundle.setCategoryId(categoryId);
        bundle.setStatus(StatusConstant.ENABLE);
        List<Bundle> bundleList = bundleMapper.getList(bundle);
        return bundleList;
    }

    /**
     * 根据套餐id查询所有商品
     * @param id
     * @return
     */
    public List<ProductItemVO> getBundleProductById(Integer id) {
        List<ProductItemVO> productItemVOList = bundleMapper.getBundleProductsById(id);
        return productItemVOList;
    }

    /**
     * 根据套餐id获取套餐详情，其中商品都要有pic图片信息
     * @param id
     * @return
     */
    public SetmealWithPicVO getBundleWithPic(Integer id) {
        Bundle bundle = bundleMapper.getBundleById(id);
        // 该套餐下的每个商品都需要加上pic字段
        List<BundleProductWithPic> dishWithPics = bundleProductMapper.getDishesWithPic(id);
        // 组成setmealWithPicVO后返回
        SetmealWithPicVO setmealWithPicVO = new SetmealWithPicVO();
        BeanUtils.copyProperties(bundle, setmealWithPicVO);
        setmealWithPicVO.setSetmealDishes(dishWithPics);
        return setmealWithPicVO;
    }

}
