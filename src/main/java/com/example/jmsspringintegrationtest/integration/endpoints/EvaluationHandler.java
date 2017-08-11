package com.example.jmsspringintegrationtest.integration.endpoints;

import java.util.Map;

import com.example.jmsspringintegrationtest.service.EvaluationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.dsl.support.GenericHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EvaluationHandler implements GenericHandler<String> {

    private final EvaluationService evaluationService;

    public EvaluationHandler(EvaluationService evaluationService) {
        this.evaluationService = evaluationService;
    }

    @Override
    public String handle(String event, Map<String, Object> headers) {
        return evaluationService.evaluate(event);
    }
}
