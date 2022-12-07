package com.alliance.leadbooster.service;

import com.alliance.leadbooster.exceptions.EntityNotFoundException;
import com.alliance.leadbooster.model.UpdateStateRequest;
import com.alliance.leadbooster.persistence.entity.StateDictionary;
import com.alliance.leadbooster.persistence.repository.StateDictionaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StatesService {
    private final StateDictionaryRepository stateDictionaryRepository;

    @Transactional(readOnly = true)
    public List<StateDictionary> getAllStates() {
        return stateDictionaryRepository.findAll();
    }

    @Transactional
    public StateDictionary updateStateDictionaryItem(UpdateStateRequest updateStateRequest) {
        StateDictionary stateDictionary = stateDictionaryRepository.findById(updateStateRequest.getName()).orElseThrow(EntityNotFoundException::new);

        if (updateStateRequest.getColor() != null) {
            stateDictionary.setColor(updateStateRequest.getColor());
        }
        if (updateStateRequest.getTitle() != null) {
            stateDictionary.setTitle(updateStateRequest.getTitle());
        }
        if (updateStateRequest.getDaysToComplete() != null) {
            stateDictionary.setDaysToComplete(updateStateRequest.getDaysToComplete());
        }

        return stateDictionaryRepository.save(stateDictionary);
    }
}
