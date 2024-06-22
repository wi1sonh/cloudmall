package fun.cmgraph.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import fun.cmgraph.context.BaseContext;
import fun.cmgraph.dto.*;
import fun.cmgraph.entity.*;
import fun.cmgraph.mapper.*;
import fun.cmgraph.service.serviceImpl.*;
import org.springframework.beans.BeanUtils;
import fun.cmgraph.entity.AddressBook;
import fun.cmgraph.vo.OrderSubmitVO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderMapper orderMapper;
    @Mock
    private AddressBookMapper addressBookMapper;
    @Mock
    private CartMapper cartMapper;
    @Mock
    private OrderDetailMapper orderDetailMapper;

    @InjectMocks
    private OrderServiceImpl orderServiceImpl;

    @BeforeEach
    void setUp() {
        BaseContext.setCurrentId(1);
    }

    @Test
    void submit() {
        AddressBook addressBook = new AddressBook(1, 1, "huang", "12345678910", 1, "44", "广东省", "01", "广州市", "001", "天河区", "detail", "home", 1);
        when(addressBookMapper.getById(1)).thenReturn(addressBook);
        Cart queryCart = new Cart();
        queryCart.setUserId(1);
        List<Cart> cartList = new ArrayList<>();
        cartList.add(new Cart(1, "name", 1, 1, null, null, 1, new BigDecimal(10), "pic", null));
        when(cartMapper.list(queryCart)).thenReturn(cartList);
        OrderSubmitDTO orderSubmitDTO = new OrderSubmitDTO(1, 1, "remark", null, 1, 1, 1, 6, new BigDecimal(10));
        Order order = new Order();
        BeanUtils.copyProperties(orderSubmitDTO, order);
        order.setAddressBookId(orderSubmitDTO.getAddressId());
        order.setPhone(addressBook.getPhone());
        order.setAddress(addressBook.getDetail());
        order.setConsignee(addressBook.getConsignee());
        // 利用时间戳来生成当前订单的编号
        order.setNumber(String.valueOf(System.currentTimeMillis()));
        order.setUserId(1);
        order.setStatus(Order.PENDING_PAYMENT); // 刚下单提交，此时是待付款状态
        order.setPayStatus(Order.UN_PAID); // 未支付
        order.setOrderTime(LocalDateTime.now());

        List<OrderDetail> orderDetailList = new ArrayList<>();
        for (Cart c: cartList) {
            OrderDetail orderDetail = new OrderDetail();
            BeanUtils.copyProperties(c, orderDetail);
            orderDetail.setOrderId(order.getId());
            orderDetailList.add(orderDetail);
        }

        doNothing().when(cartMapper).delete(1);

        orderServiceImpl.submit(orderSubmitDTO);
        verify(orderMapper, times(1)).insert(any(Order.class));
        verify(orderDetailMapper, times(1)).insertBatch(orderDetailList);
    }
}