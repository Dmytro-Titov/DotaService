package com.andersenlab.dotaservice.service;

import com.andersenlab.dotaservice.model.Item;
import com.andersenlab.dotaservice.repository.ItemRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ItemService {

  private final ItemRepository itemRepository;

  public List<Item> getAllItems() {
    return itemRepository.findAll();
  }

}
