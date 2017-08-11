package com.example.jmsspringintegrationtest.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by patrik.mihalcin on 5.6.2017.
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class EvaluationServiceImpl implements EvaluationService {

    private final CarRepository cars;

    @Override
    public String evaluate(String event) {

        log.debug("All cars: {}", this.cars.findAll());

        return event;
    }
}
