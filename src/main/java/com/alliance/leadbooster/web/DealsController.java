package com.alliance.leadbooster.web;

import com.alliance.leadbooster.model.DealResponseDto;
import com.alliance.leadbooster.model.MoveDealRequest;
import com.alliance.leadbooster.model.UpdateDealRequest;
import com.alliance.leadbooster.service.DealsService;
import com.alliance.leadbooster.utils.DealMapper;
import lombok.RequiredArgsConstructor;
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
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/v1/deals")
@RequiredArgsConstructor
public class DealsController {

    private final DealsService dealsService;


    @GetMapping
    public List<DealResponseDto> getAllDeals(@RequestParam(required = false) String product) {
        log.info("[API] get all chats request");
        return dealsService.getAllDeals(product)
            .stream()
            .map(DealMapper::mapToDto)
            .collect(Collectors.toList());
    }

    @PutMapping
    public DealResponseDto updateDeal(@RequestBody UpdateDealRequest request) {
        log.info("[API] update chat [{}]", request);
        return DealMapper.mapToDto(dealsService.updateDeal(request));
    }

    @PatchMapping("/{uuid}/move")
    public DealResponseDto moveDeal(@PathVariable String uuid,
                                    @RequestBody MoveDealRequest moveDealRequest) {
        log.info("[API] move ticket [{}] to [{}]", uuid, moveDealRequest);
        return DealMapper.mapToDto(dealsService.moveDealToState(uuid, moveDealRequest.getTargetState()));
    }

}
