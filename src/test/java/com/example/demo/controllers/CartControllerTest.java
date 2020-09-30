package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CartControllerTest {
    private CartController cartController;

    private CartRepository cartRepository = mock(CartRepository.class);
    private UserRepository userRepository = mock(UserRepository.class);
    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setup() throws IllegalAccessException {
        this.cartController = new CartController();
        TestUtils.injectObjects(cartController, "cartRepository", cartRepository);
        TestUtils.injectObjects(cartController, "userRepository", userRepository);
        TestUtils.injectObjects(cartController, "itemRepository", itemRepository);
    }

    @Test
    public void addToCart() {
        ModifyCartRequest cartRequest = new ModifyCartRequest();
        cartRequest.setUsername("test");
        cartRequest.setItemId(1);
        cartRequest.setQuantity(1);

        User user = new User();
        user.setId(1);
        user.setUsername("test");
        Cart newCart = new Cart();
        user.setCart(newCart);

        Item item = new Item();
        item.setId(1L);
        item.setName("testItem");
        item.setDescription("testDescription");
        item.setPrice(BigDecimal.ONE);

        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));
        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);

        ResponseEntity<Cart> response = cartController.addTocart(cartRequest);
        Cart cart = response.getBody();

        Assert.assertEquals(200, response.getStatusCodeValue());
        Assert.assertEquals("testItem", cart.getItems().get(0).getName());
        Assert.assertEquals("testDescription", cart.getItems().get(0).getDescription());
    }

}
