package com.alliance.leadbooster.web;

import com.alliance.leadbooster.model.UpdateStateRequest;
import com.alliance.leadbooster.persistence.entity.StateDictionary;
import com.alliance.leadbooster.service.StatesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api/v1/states")
@RestController
@Slf4j
@RequiredArgsConstructor
public class StatesController {
    private final StatesService statesService;

    @GetMapping
    public List<StateDictionary> getAllStates() {
        log.debug("[API] get all states request");
        return statesService.getAllStates();
    }

    @PutMapping
    public StateDictionary updateStateDictionaryItem(@RequestBody UpdateStateRequest request) {
        log.debug("[API] get all states request");
        return statesService.updateStateDictionaryItem(request);
    }
}
