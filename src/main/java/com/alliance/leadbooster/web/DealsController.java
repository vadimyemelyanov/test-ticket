package com.alliance.leadbooster.web;

import com.alliance.leadbooster.model.MoveTicketRequest;
import com.alliance.leadbooster.model.UpdateDealRequest;
import com.alliance.leadbooster.persistence.entity.Deal;
import com.alliance.leadbooster.service.DealsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/deals")
@Slf4j
public class DealsController {
    private final DealsService dealsService;

    public DealsController(DealsService dealsService) {
        this.dealsService = dealsService;
    }

    @GetMapping
    public List<Deal> getAllDeals(@RequestParam(required = false) String product) {
        log.info("[API] get all chats request");
        return dealsService.getAllDeals(product);
    }

    @PutMapping
    public void updateDeal(@RequestBody UpdateDealRequest request) {
        log.info("[API] update chat [{}]", request);
        dealsService.updateDeal(request);
    }

    @PatchMapping("/{uuid}/move")
    public void moveDeal(@PathVariable String uuid,
                         @RequestBody MoveTicketRequest moveTicketRequest) {
        log.info("[API] move ticket [{}] to  [{}]", uuid, moveTicketRequest);
        dealsService.moveDealToState(uuid, moveTicketRequest.getTargetState());
    }

}
