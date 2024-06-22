package fun.cmgraph.service;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import fun.cmgraph.dto.*;
import fun.cmgraph.entity.Cart;
import fun.cmgraph.mapper.CartMapper;
import fun.cmgraph.service.serviceImpl.CartServiceImpl;
import fun.cmgraph.mapper.ProductMapper;
import fun.cmgraph.entity.Product;
import java.util.*;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @Mock
    private CartMapper cartMapper;
    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private CartServiceImpl cartService;

    @Test
    void add() {
        CartDTO cartDTO = new CartDTO();
        cartDTO.setDishId(1);
        Cart cart = new Cart();
        cart.setDishId(1);
        Cart returnCart = new Cart();
        returnCart.setDishId(1);
        returnCart.setNumber(1);
        // 1、存在，无需再insert，直接数量+1就行
        when(cartMapper.list(cart)).thenReturn(Collections.singletonList(returnCart));
        cartService.add(cartDTO);
        verify(cartMapper, times(1)).updateNumberById(any(Cart.class));
        // 2、不存在，需要新增（菜品/套餐），数量为1
        CartDTO cartDTO2 = new CartDTO();
        cartDTO2.setDishId(2);
        when(productMapper.getById(2)).thenReturn(new Product());
        cartService.add(cartDTO2);
        verify(cartMapper, times(1)).insert(any(Cart.class));
    }

    @Test
    void getList() {
        cartService.getList();
        verify(cartMapper, times(1)).list(any());
    }

    @Test
    void clean() {
        cartService.clean();
        verify(cartMapper, times(1)).delete(any());
    }

    @Test
    void sub() {
        CartDTO cartDTO = new CartDTO();
        cartDTO.setDishId(1);
        Cart cart = new Cart();
        cart.setDishId(1);
        Cart returnCart = new Cart();
        returnCart.setNumber(1);
        returnCart.setDishId(1);
        when(cartMapper.list(cart)).thenReturn(Collections.singletonList(returnCart));
        cartService.sub(cartDTO);
        verify(cartMapper, times(1)).deleteByDishId(anyInt(), any());
    }
}