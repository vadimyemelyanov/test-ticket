package com.telegram.leadbooster.web;

import com.telegram.leadbooster.domain.Deal;
import com.telegram.leadbooster.dto.MoveDealRequest;
import com.telegram.leadbooster.dto.UpdateDealRequest;
import com.telegram.leadbooster.service.DealsService;
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
    public Deal updateDeal(@RequestBody UpdateDealRequest request) {
        log.info("[API] update chat [{}]", request);
        return dealsService.updateDeal(request);
    }

    @PatchMapping("/{uuid}/move")
    public Deal moveDeal(@PathVariable String uuid,
                         @RequestBody MoveDealRequest moveDealRequest) {
        log.info("[API] move ticket [{}] to [{}]", uuid, moveDealRequest);
        return dealsService.moveDealToState(uuid, moveDealRequest.getTargetState());
    }

}
