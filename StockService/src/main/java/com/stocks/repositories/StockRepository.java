package com.stocks.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.stocks.domain.Stock;

/**
 * The Repository interface to perform DAO operations on {@link Stock}.
 * 
 * @author Jalal
 * @since 20190403
 * @version 1.0
 */
@Repository
public interface StockRepository extends CrudRepository<Stock, Long> {

}
