package com.challenge.store.service;

import static com.challenge.store.constant.RoleEnum.ROLE_AFFILIATE;
import static com.challenge.store.constant.RoleEnum.ROLE_CUSTOMER;
import static com.challenge.store.constant.RoleEnum.ROLE_EMPLOYEE;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.challenge.store.dao.entity.Item;
import com.challenge.store.dao.repository.ItemRepository;
import com.challenge.store.dto.BillRequest;
import com.challenge.store.dto.BillResponse;
import com.challenge.store.security.model.UserDetailsImpl;
import com.challenge.store.strategy.AffiliateDiscountStrategy;
import com.challenge.store.strategy.CustomerLoyaltyDiscountStrategy;
import com.challenge.store.strategy.DiscountStrategy;
import com.challenge.store.strategy.EmployeeDiscountStrategy;
import com.challenge.store.strategy.NoDiscountStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BillService {

    private final ItemRepository itemRepository;

    public BillResponse calculateBill(BillRequest billRequest, UserDetailsImpl userDetails) {

        // Query items from the database
        List<Item> items = itemRepository.findAllById(billRequest.getItemIds());


        // Calculate grocery items amount
        BigDecimal groceryItemsAmount = items.stream()
                .filter(item -> item.getCategory().equalsIgnoreCase("GROCERY"))
                .map(Item::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        //Calculate non-grocery items
        BigDecimal nonGroceryItemsAmount = items.stream()
                .filter(item -> !item.getCategory().equalsIgnoreCase("GROCERY"))
                .map(Item::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        //Total amount:
        BigDecimal totalAmount = groceryItemsAmount.add(nonGroceryItemsAmount);

        // Apply discount only on non-grocery items
        DiscountStrategy discountStrategy = getDiscountStrategy(userDetails);
        BigDecimal discountOnNonGrocery = discountStrategy.applyDiscount(nonGroceryItemsAmount);

        BigDecimal nonGroceryPriceAfterDiscount = nonGroceryItemsAmount.subtract(discountOnNonGrocery);
        // Calculate net amount after discount
        BigDecimal netAmountAfterDiscount = nonGroceryPriceAfterDiscount.add(groceryItemsAmount);

        // Apply additional discount for every $100 spent
        BigDecimal additionalDiscount = calculateAdditionalDiscount(totalAmount);
        BigDecimal netPayableAmount = netAmountAfterDiscount.subtract(additionalDiscount);

        return new BillResponse(totalAmount,
                                netPayableAmount, userDetails.getId());
    }

    public static BigDecimal calculateAdditionalDiscount(BigDecimal billAmount) {
        BigDecimal numberOfHundreds = billAmount.divide(new BigDecimal("100"), 0, RoundingMode.DOWN);
        return numberOfHundreds.multiply(new BigDecimal("5"));
    }

    private DiscountStrategy getDiscountStrategy(UserDetailsImpl userDetails) {

        Set<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());

        if (roles.contains(ROLE_EMPLOYEE.name())) {
            return new EmployeeDiscountStrategy();
        } else if (roles.contains(ROLE_AFFILIATE.name())) {
            return new AffiliateDiscountStrategy();
        } else if (roles.contains(ROLE_CUSTOMER.name()) && customerLoyaltyCheck(userDetails.getCreatedDate())) {
            return new CustomerLoyaltyDiscountStrategy();
        }
        return new NoDiscountStrategy();
    }


    /**
     * if the user has been customer for over two years:
     */
    private boolean customerLoyaltyCheck(Instant createdDate) {
        return createdDate.isBefore(Instant.now().atZone(ZoneOffset.UTC).minusYears(2).toInstant());
    }
}
