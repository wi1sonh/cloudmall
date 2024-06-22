package fun.cmgraph.service;

import com.github.pagehelper.Page;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import fun.cmgraph.dto.*;
import fun.cmgraph.entity.Product;
import fun.cmgraph.mapper.ProductMapper;
import fun.cmgraph.result.PageResult;
import fun.cmgraph.vo.ProductVO;
import fun.cmgraph.service.serviceImpl.ProductServiceImpl;
import fun.cmgraph.mapper.DishFlavorMapper;

import java.math.BigDecimal;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductMapper productMapper;
    @Mock
    private DishFlavorMapper dishFlavorMapper;

    @InjectMocks
    private ProductServiceImpl productService;

    @Test
    void addProduct() {
        ProductDTO productDTO = new ProductDTO(1, "test", "pic", "detail", new BigDecimal(10), "status", 1, null);
        productService.addProduct(productDTO);
        verify(productMapper, times(1)).addProduct(any());
    }

    @Test
    void getPageList() {
        ProductPageDTO productPageDTO = new ProductPageDTO(1, 10, "name", 1, 1);
        when(productMapper.getPageList(productPageDTO)).thenReturn(new Page<Product>());

        PageResult pageResult = productService.getPageList(productPageDTO);
        assertEquals(pageResult, new PageResult(0L, (new Page<Product>()).getResult()));
    }

    @Test
    void getProductById() {
        when(productMapper.getById(1)).thenReturn(new Product(1, "test", "pic", "detail", new BigDecimal(10), 1, 1, 1, 1, null, null));
        when(dishFlavorMapper.getByDishId(1)).thenReturn(null);
        ProductVO productVO = productService.getProductById(1);
        assertEquals(productVO, new ProductVO(1, "test", "pic", "detail", new BigDecimal(10), null, 1, null, null));
    }

    @Test
    void updateProduct() {
        ProductDTO productDTO = new ProductDTO(1, "test", "pic", "detail", new BigDecimal(10), "status", 1, null);
        productService.updateProduct(productDTO);
        verify(productMapper, times(1)).update(any());
    }

    @Test
    void deleteBatch() {
    }

    @Test
    void onOff() {
        productService.onOff(1);
        verify(productMapper, times(1)).onOff(1);
    }

    @Test
    void getProductsById() {
        Product product = new Product(1, "test", "pic", "detail", new BigDecimal(10), 1, 1, 1, 1, null, null);
        when(productMapper.getList(product)).thenReturn(List.of(product));
        when(dishFlavorMapper.getByDishId(1)).thenReturn(null);
        List<ProductVO> productVOList = productService.getProductsById(product);
        assertEquals(productVOList, List.of(new ProductVO(1, "test", "pic", "detail", new BigDecimal(10), null, 1, null, null)));
    }
}