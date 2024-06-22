package fun.cmgraph.service;

import fun.cmgraph.dto.ProductDTO;
import fun.cmgraph.dto.ProductPageDTO;
import fun.cmgraph.entity.Product;
import fun.cmgraph.result.PageResult;
import fun.cmgraph.vo.ProductVO;

import java.util.List;

public interface ProductService {
    void addProduct(ProductDTO productDTO);

    PageResult getPageList(ProductPageDTO productPageDTO);

    ProductVO getProductById(Integer id);

    void updateProduct(ProductDTO productDTO);

    void deleteBatch(List<Integer> ids);

    void onOff(Integer id);

    List<ProductVO> getProductsById(Product product);
}
