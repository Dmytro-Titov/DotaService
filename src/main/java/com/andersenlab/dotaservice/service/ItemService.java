package com.andersenlab.dotaservice.service;

import com.andersenlab.dotaservice.model.Item;
import com.andersenlab.dotaservice.repository.ItemRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import static com.andersenlab.dotaservice.errors.ErrorMessages.ITEM_NOT_FOUNT_ERROR;

@Service
@RequiredArgsConstructor
public class ItemService {

  private final ItemRepository itemRepository;

  public List<Item> getAllItems() {
    return itemRepository.findAll();
  }

  public Item getItemById(Long id) {
    return itemRepository.findById(id).orElseThrow(
        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, ITEM_NOT_FOUNT_ERROR + id));
  }

}
