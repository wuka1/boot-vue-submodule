package com.example.workflow;

import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
public class DefinitionTests {

    @Resource
    private RepositoryService repositoryService;

    @Test
    void getDefinition() {
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(
                "9f23b573-50a8-11ef-9fb5-566cebc1dde2").singleResult();
        System.out.println("流程定义:" + processDefinition);

        repositoryService.getBpmnModelInstance("");
    }
}
