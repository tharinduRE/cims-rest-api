package com.cheminv.app.service;

import com.cheminv.app.domain.ItemTransaction;
import com.cheminv.app.repository.ItemTransactionRepository;
import com.cheminv.app.repository.search.ItemTransactionSearchRepository;
import com.cheminv.app.service.dto.ItemTransactionDTO;
import com.cheminv.app.service.mapper.ItemTransactionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link ItemTransaction}.
 */
@Service
@Transactional
public class ItemTransactionService {

    private final Logger log = LoggerFactory.getLogger(ItemTransactionService.class);

    private final ItemTransactionRepository itemTransactionRepository;

    private final ItemTransactionMapper itemTransactionMapper;

    private final ItemTransactionSearchRepository itemTransactionSearchRepository;

    public ItemTransactionService(ItemTransactionRepository itemTransactionRepository, ItemTransactionMapper itemTransactionMapper, ItemTransactionSearchRepository itemTransactionSearchRepository) {
        this.itemTransactionRepository = itemTransactionRepository;
        this.itemTransactionMapper = itemTransactionMapper;
        this.itemTransactionSearchRepository = itemTransactionSearchRepository;
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
        ItemTransactionDTO result = itemTransactionMapper.toDto(itemTransaction);
        itemTransactionSearchRepository.save(itemTransaction);
        return result;
    }

    /**
     * Get all the itemTransactions.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ItemTransactionDTO> findAll() {
        log.debug("Request to get all ItemTransactions");
        return itemTransactionRepository.findAll().stream()
            .map(itemTransactionMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
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
        itemTransactionSearchRepository.deleteById(id);
    }

    /**
     * Search for the itemTransaction corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ItemTransactionDTO> search(String query) {
        log.debug("Request to search ItemTransactions for query {}", query);
        return StreamSupport
            .stream(itemTransactionSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(itemTransactionMapper::toDto)
        .collect(Collectors.toList());
    }
}
