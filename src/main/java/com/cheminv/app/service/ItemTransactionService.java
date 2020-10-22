package com.cheminv.app.service;

import com.cheminv.app.domain.ItemTransaction;
import com.cheminv.app.repository.ItemTransactionRepository;
import com.cheminv.app.service.dto.ItemTransactionDTO;
import com.cheminv.app.service.mapper.ItemTransactionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link ItemTransaction}.
 */
@Service
@Transactional
public class ItemTransactionService {

    private final Logger log = LoggerFactory.getLogger(ItemTransactionService.class);

    private final ItemTransactionRepository itemTransactionRepository;

    private final ItemTransactionMapper itemTransactionMapper;

    public ItemTransactionService(ItemTransactionRepository itemTransactionRepository, ItemTransactionMapper itemTransactionMapper) {
        this.itemTransactionRepository = itemTransactionRepository;
        this.itemTransactionMapper = itemTransactionMapper;
    }

    /**
     * Save a itemTransaction.
     *
     * @param itemTransactionDTO the entity to save.
     * @return the persisted entity.
     */
    public ItemTransactionDTO save(ItemTransactionDTO itemTransactionDTO) {
        log.debug("Request to save ItemTransaction : {}", itemTransactionDTO);
        ItemTransaction itemTransaction = itemTransactionMapper.toEntity(itemTransactionDTO);
        itemTransaction = itemTransactionRepository.save(itemTransaction);
        return itemTransactionMapper.toDto(itemTransaction);
    }

    /**
     * Get all the itemTransactions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ItemTransactionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ItemTransactions");
        return itemTransactionRepository.findAll(pageable)
            .map(itemTransactionMapper::toDto);
    }


    /**
     * Get one itemTransaction by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ItemTransactionDTO> findOne(Long id) {
        log.debug("Request to get ItemTransaction : {}", id);
        return itemTransactionRepository.findById(id)
            .map(itemTransactionMapper::toDto);
    }

    /**
     * Delete the itemTransaction by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ItemTransaction : {}", id);
        itemTransactionRepository.deleteById(id);
    }
}
