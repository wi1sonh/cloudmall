package fun.cmgraph.service.serviceImpl;

import fun.cmgraph.context.BaseContext;
import fun.cmgraph.dto.CartDTO;
import fun.cmgraph.entity.Cart;
import fun.cmgraph.entity.Product;
import fun.cmgraph.entity.Bundle;
import fun.cmgraph.mapper.CartMapper;
import fun.cmgraph.mapper.ProductMapper;
import fun.cmgraph.mapper.BundleMapper;
import fun.cmgraph.service.CartService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartMapper cartMapper;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private BundleMapper bundleMapper;

    /**
     * 添加进购物车
     *
     * @param cartDTO
     */
    public void add(CartDTO cartDTO) {
        // cart表示当前用户要加入购物车的一条数据
        Cart cart = new Cart();
        BeanUtils.copyProperties(cartDTO, cart);
        cart.setUserId(BaseContext.getCurrentId());
        // 查询该用户自己购物车的所有商品和套餐，看看有没有和要加入的cart一样的？
        List<Cart> cartList = cartMapper.list(cart);
        // 1、存在，无需再insert，直接数量+1就行
        if (cartList != null && cartList.size() == 1) {
            cart = cartList.get(0);
            cart.setNumber(cart.getNumber() + 1);
            cartMapper.updateNumberById(cart);
        }
        // 2、不存在，需要新增（商品/套餐），数量为1
        else {
            // 判断当前添加到购物车的是商品还是套餐
            Integer dishId = cartDTO.getDishId();
            if (dishId != null) {
                // 添加到购物车的是商品
                Product product = productMapper.getById(dishId);
                cart.setName(product.getName());
                cart.setPic(product.getPic());
                cart.setAmount(product.getPrice());
            } else {
                // 添加到购物车的是套餐
                Bundle bundle = bundleMapper.getBundleById(cartDTO.getSetmealId());
                cart.setName(bundle.getName());
                cart.setPic(bundle.getPic());
                cart.setAmount(bundle.getPrice());
            }
            cart.setNumber(1);
            cart.setCreateTime(LocalDateTime.now());
            cartMapper.insert(cart);
        }
    }

    /**
     * 在购物车中的对应商品/套餐数量减一
     *
     * @param cartDTO
     */
    public void sub(CartDTO cartDTO) {
        // cart表示当前用户要减少购物车商品/套餐数量的一条数据
        Cart cart = new Cart();
        BeanUtils.copyProperties(cartDTO, cart);
        cart.setUserId(BaseContext.getCurrentId());
        // 查询该商品/套餐详细信息（必有，而且只有一条）
        List<Cart> cartList = cartMapper.list(cart);
        cart = cartList.get(0);
        // 1、数量-1后为0，直接把这条记录删除
        if (cart.getNumber() == 1) {
            if (cart.getDishId() != null) {
                // 不能只根据dishId删除，还要考虑到一个商品可能用户选择了多个口味，对应多个商品
                cartMapper.deleteByDishId(cart.getDishId(), cart.getDishFlavor());
            } else {
                cartMapper.deleteBySetmealId(cart.getSetmealId());
            }
        }
        // 2、直接数量-1就行
        else {
            cart.setNumber(cart.getNumber() - 1);
            cartMapper.updateNumberById(cart);
        }

    }

    /**
     * 根据userid，获取当前用户的购物车列表
     *
     * @return
     */
    public List<Cart> getList() {
        return cartMapper.list(Cart.builder().
                userId(BaseContext.getCurrentId()).
                build());
    }

    /**
     * 根据userid，清空其购物车
     */
    public void clean() {
        cartMapper.delete(BaseContext.getCurrentId());
    }

}
