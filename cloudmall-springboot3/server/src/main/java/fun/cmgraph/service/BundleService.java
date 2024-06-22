package fun.cmgraph.service;

import fun.cmgraph.dto.BundleDTO;
import fun.cmgraph.dto.BundlePageDTO;
import fun.cmgraph.entity.Bundle;
import fun.cmgraph.result.PageResult;
import fun.cmgraph.vo.ProductItemVO;
import fun.cmgraph.vo.BundleVO;
import fun.cmgraph.vo.SetmealWithPicVO;

import java.util.List;

public interface BundleService {
    void addBundle(BundleDTO bundleDTO);

    PageResult getPageList(BundlePageDTO bundlePageDTO);

    BundleVO getBundleById(Integer id);

    BundleVO getBundleByName(String name);

    void onOff(Integer id);

    void update(BundleDTO bundleDTO);

    void deleteBatch(List<Integer> ids);

    List<Bundle> getList(Integer categoryId);

    List<ProductItemVO> getBundleProductById(Integer id);

    SetmealWithPicVO getBundleWithPic(Integer id);
}
