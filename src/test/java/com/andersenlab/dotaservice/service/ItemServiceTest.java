package com.andersenlab.dotaservice.service;

import com.andersenlab.dotaservice.model.Item;
import com.andersenlab.dotaservice.repository.ItemRepository;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ItemServiceTest {

  @Mock
  private ItemRepository itemRepository;

  @InjectMocks
  private ItemService itemService;

  private Item item;

  @BeforeEach
  void setUp() {
    item = new Item(1L, "Blink Dagger", 2250, 30, 100, "Active: Blink");
  }

  @Test
  void getAllItems_ShouldReturnItemList() {
    when(itemRepository.findAll()).thenReturn(Arrays.asList(item));

    List<Item> result = itemService.getAllItems();

    assertEquals(1, result.size());
    assertEquals("Blink Dagger", result.get(0).getName());
  }

  @Test
  void getAllItems_ShouldReturnEmptyList_WhenNoItems() {
    when(itemRepository.findAll()).thenReturn(Collections.emptyList());

    List<Item> result = itemService.getAllItems();

    assertTrue(result.isEmpty());
  }

  @Test
  void getItemById_ShouldReturnItem_WhenExists() {
    when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

    Item result = itemService.getItemById(1L);

    assertEquals(item, result);
  }

  @Test
  void getItemById_ShouldThrowException_WhenNotFound() {
    when(itemRepository.findById(1L)).thenReturn(Optional.empty());

    ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> itemService.getItemById(1L));

    assertEquals(404, exception.getStatusCode().value());
    assertTrue(exception.getReason().contains("1"));
  }
}
