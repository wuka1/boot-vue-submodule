package com.example.workflow.controller;

import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.history.HistoricProcessInstance;
import org.camunda.bpm.engine.repository.Deployment;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.variable.VariableMap;
import org.camunda.bpm.engine.variable.Variables;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;


@RestController
public class BPMNController {

    @Resource
    private RepositoryService repositoryService;
    @Resource
    private IdentityService identityService;
    @Resource
    private RuntimeService runtimeService;

    @Resource
    private HistoryService historyService;


    @PostMapping("/deploy/{processKey}")
    public String deployFlow(@PathVariable("processKey") String processKey) {
        // 启动加载
        Deployment deployment = repositoryService.createDeployment()
                .name(processKey)
                .source("system")
//                .addDeploymentResourceByName("", "")  //重新部署
                .addClasspathResource("BPMN/" + processKey.concat(".bpmn"))  //processKey为.bpmn的名称
                .deploy();
        return deployment.getId() + ":" + deployment.getName();
    }

    @PostMapping("/start/{processKey}")
    public void start(@PathVariable(value = "processKey") String processKey) {

        // 获取已部署的process定义
        List<ProcessDefinition> definitinList =
                repositoryService.createProcessDefinitionQuery().latestVersion().list();
        for (ProcessDefinition dl : definitinList) {
            System.out.println("已部署的process有：" + dl.getId());
        }
        // 查询流程实例
//        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().singleResult();

        //通过登录用户来设置
        identityService.setAuthenticatedUserId("zhangsan");

        final VariableMap variables = Variables.createVariables();
        variables.putValue("starter", "zhangsan");  //获取登录用户

        runtimeService.startProcessInstanceByKey(processKey, variables);
        // 这里执行execution?
//        runtimeService.startProcessInstanceById("062f647e-48ee-11ef-88ac-566cebc1dde2");
        //如何结合外部用户
    }

    @PostMapping("/restart/{processKey}")
    public void restart(@PathVariable(value = "processKey") String processKey) {

        // 获取已部署的process定义
//        List<ProcessDefinition> definitinList =
//                repositoryService.createProcessDefinitionQuery().latestVersion().list();
//        for (ProcessDefinition dl : definitinList) {
//            System.out.println("已部署的process有：" + dl.getId());
//        }

        final HistoricProcessInstance hpi =
                historyService.createHistoricProcessInstanceQuery().processInstanceId(processKey).singleResult();
        // 查询流程实例
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().singleResult();


        //通过登录用户来设置
        identityService.setAuthenticatedUserId("zhangsan");

        final VariableMap variables = Variables.createVariables();
        variables.putValue("starter", "zhangsan");  //获取登录用户

        runtimeService.startProcessInstanceByKey(processKey, variables);
        // 这里执行execution?
//        runtimeService.startProcessInstanceById("062f647e-48ee-11ef-88ac-566cebc1dde2");
        //如何结合外部用户
    }

}
