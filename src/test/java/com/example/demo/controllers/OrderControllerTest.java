package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {
    private OrderController orderController;
    private UserRepository userRepo = mock(UserRepository.class);
    private OrderRepository orderRepo = mock(OrderRepository.class);

    @Before
    public void setUp() throws IllegalAccessException {
        orderController = new OrderController();
        TestUtils.injectObjects(orderController, "userRepository", userRepo);
        TestUtils.injectObjects(orderController, "orderRepository", orderRepo);
    }

    @Test
    public void submitOrder() {
        User user = new User();
        user.setId(1L);
        user.setUsername("test");
        Cart cart = new Cart();

        Item item = new Item();
        item.setId(1L);
        item.setName("testItem");
        item.setDescription("testDescription");
        item.setPrice(BigDecimal.ZERO);

        List<Item> items = new ArrayList<>();
        items.add(item);
        cart.setItems(items);
        user.setCart(cart);
        when(userRepo.findByUsername(user.getUsername())).thenReturn(user);

        final ResponseEntity<UserOrder> response = orderController.submit(user.getUsername());
        assertNotNull(response);
        Assert.assertEquals(200, response.getStatusCodeValue());
        UserOrder order = response.getBody();
        assertNotNull(order);
        assertEquals("testItem", order.getItems().get(0).getName());
        assertEquals("testDescription", order.getItems().get(0).getDescription());
        assertEquals(BigDecimal.ZERO, order.getItems().get(0).getPrice());
    }

    @Test
    public void submitOrderFailure() {
        User user = new User();
        user.setId(1L);
        user.setUsername("test");
        Cart cart = new Cart();

        Item item = new Item();
        item.setId(1L);
        item.setName("testItem");
        item.setDescription("testDescription");
        item.setPrice(BigDecimal.ZERO);

        List<Item> items = new ArrayList<>();
        items.add(item);
        cart.setItems(items);
        user.setCart(cart);
        when(userRepo.findByUsername(user.getUsername())).thenReturn(user);

        final ResponseEntity<UserOrder> response = orderController.submit("test2");
        Assert.assertEquals(404, response.getStatusCodeValue());
    }
}
