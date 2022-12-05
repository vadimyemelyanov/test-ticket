package com.telegram.leadbooster.repository;

import com.telegram.leadbooster.domain.Deal;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DealsRepository extends JpaRepository<Deal, String> {

    @EntityGraph(
        value = "deal-with-notes-and-history",
        type = EntityGraph.EntityGraphType.LOAD
    )
    @Query(value = "from Deal where (product =:product or product is null) and currentState <> 'CLOSED' ")
    List<Deal> findAllByProduct(String product);

    @EntityGraph(
        value = "deal-with-notes-and-history",
        type = EntityGraph.EntityGraphType.LOAD
    )
    @Query(value = "from Deal where currentState <> 'CLOSED'")
    List<Deal> findAllByCurrentStateIsNotClosed();


    @Query(nativeQuery = true, value = "select * from deals where telegram_chat_id = ?1 and current_state not in ('CLOSED', 'COMPLETED')")
    List<Deal> findAllByChatIdAndCurrentStateNotFinal(Long chatId);
}
