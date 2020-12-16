package com.cheminv.app.service;

import com.cheminv.app.domain.ItemStock;
import com.cheminv.app.domain.Transaction;
import com.cheminv.app.repository.ItemStockRepository;
import com.cheminv.app.repository.TransactionRepository;
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
 * Service Implementation for managing {@link Transaction}.
 */
@Service
@Transactional
public class TransactionService {

    private final Logger log = LoggerFactory.getLogger(TransactionService.class);

    private final TransactionRepository transactionRepository;

    private final ItemTransactionMapper itemTransactionMapper;

    private final ItemStockRepository itemStockRepository;

    public TransactionService(TransactionRepository transactionRepository, ItemTransactionMapper itemTransactionMapper, ItemStockRepository itemStockRepository) {
        this.transactionRepository = transactionRepository;
        this.itemTransactionMapper = itemTransactionMapper;
        this.itemStockRepository = itemStockRepository;
    }

    /**
     * Save a itemTransaction.
     *
     * @param itemTransactionDTO the entity to save.
     * @return the persisted entity.
     */
    public ItemTransactionDTO save(ItemTransactionDTO itemTransactionDTO) {
        log.debug("Request to save Transaction : {}", itemTransactionDTO);
        Transaction transaction = itemTransactionMapper.toEntity(itemTransactionDTO);
        transaction = transactionRepository.save(transaction);
        adjustTotalQuantity(transaction);
        return itemTransactionMapper.toDto(transaction);
    }

    public void adjustTotalQuantity(Transaction transaction){
        ItemStock itemStock = itemStockRepository.getOne(transaction.getItemStock().getId());
        Float newTotal = itemStock.getTotalQuantity() + transaction.getQuantity();
        itemStock.setTotalQuantity(newTotal);
        itemStockRepository.save(itemStock);
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
        return transactionRepository.findAll(pageable)
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
        log.debug("Request to get Transaction : {}", id);
        return transactionRepository.findById(id)
            .map(itemTransactionMapper::toDto);
    }

    /**
     * Delete the itemTransaction by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Transaction : {}", id);
        transactionRepository.deleteById(id);
    }
}
