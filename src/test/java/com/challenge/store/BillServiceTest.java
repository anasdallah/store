package com.challenge.store;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.List;

import com.challenge.store.dao.entity.Item;
import com.challenge.store.dao.repository.ItemRepository;
import com.challenge.store.dto.BillRequest;
import com.challenge.store.dto.BillResponse;
import com.challenge.store.security.model.UserDetailsImpl;
import com.challenge.store.service.BillService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class BillServiceTest {

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private BillService billService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCalculateBill_withEmployeeDiscount() {
        // Arrange
        List<String> itemIds = Arrays.asList("1", "2", "3");
        List<Item> items = Arrays.asList(
                new Item("1", "Item 1", new BigDecimal("100"), "OTHERS"),
                new Item("2", "Item 2", new BigDecimal("200"), "OTHERS"),
                new Item("3", "Item 3", new BigDecimal("100"), "GROCERY")
        );
        BillRequest billRequest = new BillRequest(itemIds);
        UserDetailsImpl userDetails = new UserDetailsImpl("1", "user1", "password", Instant.now(),
                                                          List.of(new SimpleGrantedAuthority("ROLE_EMPLOYEE")));

        when(itemRepository.findAllById(itemIds)).thenReturn(items);

        // Act
        BillResponse response = billService.calculateBill(billRequest, userDetails);

        // Assert
        assertEquals(new BigDecimal("400"), response.getTotalAmount());
        assertEquals(new BigDecimal("290.00"), response.getNetPayableAmount());
    }

    @Test
    public void testCalculateBill_withAffiliateDiscount() {
        // Arrange
        List<String> itemIds = Arrays.asList("1", "2", "3");
        List<Item> items = Arrays.asList(
                new Item("1", "Item 1", new BigDecimal("100"), "OTHERS"),
                new Item("2", "Item 2", new BigDecimal("200"), "OTHERS"),
                new Item("3", "Item 3", new BigDecimal("100"), "GROCERY")
        );
        BillRequest billRequest = new BillRequest(itemIds);
        UserDetailsImpl userDetails = new UserDetailsImpl("2", "user2", "password", Instant.now(),
                                                          List.of(new SimpleGrantedAuthority("ROLE_AFFILIATE")));

        when(itemRepository.findAllById(itemIds)).thenReturn(items);

        // Act
        BillResponse response = billService.calculateBill(billRequest, userDetails);

        // Assert
        assertEquals(new BigDecimal("400"), response.getTotalAmount());
        assertEquals(new BigDecimal("350.00"), response.getNetPayableAmount());
    }

    @Test
    public void testCalculateBill_withCustomerLoyaltyDiscount() {
        // Arrange
        List<String> itemIds = Arrays.asList("1", "2", "3");
        List<Item> items = Arrays.asList(
                new Item("1", "Item 1", new BigDecimal("100"), "OTHERS"),
                new Item("2", "Item 2", new BigDecimal("200"), "OTHERS"),
                new Item("3", "Item 3", new BigDecimal("100"), "GROCERY")
        );
        BillRequest billRequest = new BillRequest(itemIds);
        Instant createdDate = Instant.now().atZone(ZoneOffset.UTC).minusYears(2).toInstant();
        UserDetailsImpl userDetails = new UserDetailsImpl("3", "user3", "password", createdDate,
                                                          List.of(new SimpleGrantedAuthority("ROLE_CUSTOMER")));

        when(itemRepository.findAllById(itemIds)).thenReturn(items);

        // Act
        BillResponse response = billService.calculateBill(billRequest, userDetails);

        // Assert
        assertEquals(new BigDecimal("400"), response.getTotalAmount());
        assertEquals(new BigDecimal("365.00"), response.getNetPayableAmount());
    }

    @Test
    public void testCalculateBill_withNoDiscount() {
        // Arrange
        List<String> itemIds = Arrays.asList("1", "2", "3");
        List<Item> items = Arrays.asList(
                new Item("1", "Item 1", new BigDecimal("100"), "GROCERY"),
                new Item("2", "Item 2", new BigDecimal("200"), "GROCERY"),
                new Item("3", "Item 3", new BigDecimal("100"), "GROCERY")
        );
        BillRequest billRequest = new BillRequest(itemIds);

        UserDetailsImpl userDetails = new UserDetailsImpl("4", "user4", "password", Instant.now(),
                                                          List.of(new SimpleGrantedAuthority("ROLE_CUSTOMER")));

        when(itemRepository.findAllById(itemIds)).thenReturn(items);

        // Act
        BillResponse response = billService.calculateBill(billRequest, userDetails);

        // Assert
        assertEquals(new BigDecimal("400"), response.getTotalAmount());
        assertEquals(new BigDecimal("380"), response.getNetPayableAmount());
    }

}
