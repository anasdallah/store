package com.challenge.store.dto;

import java.util.List;

import com.challenge.store.dao.entity.Item;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@AllArgsConstructor
@Data
@NoArgsConstructor
public class BillRequest {
    @NotNull
    @Size(min = 1)
    private List<String> itemIds;
}
