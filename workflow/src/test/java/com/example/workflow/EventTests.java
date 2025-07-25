package com.example.workflow;

import org.camunda.bpm.engine.*;
import org.camunda.bpm.engine.runtime.EventSubscription;
import org.camunda.bpm.engine.runtime.MessageCorrelationResult;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.engine.variable.VariableMap;
import org.camunda.bpm.engine.variable.Variables;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
class EventTests {

    @Resource
    private RepositoryService repositoryService;
    @Resource
    private IdentityService identityService;
    @Resource
    private RuntimeService runtimeService;

    @Resource
    private TaskService taskService;

    @Resource
    private HistoryService historyService;

    @Test
    void startMessageEvent() { //基于事件启动
        final VariableMap variables = Variables.createVariables();
        variables.putValue("starter", "wangwu"); //占位符名和人员要传进来
        runtimeService.startProcessInstanceByMessage("sendMessage", "this is a test", variables);
    }

    @Test
    void midMessageEvent() { //基于事件流转
//        List<Task> tasks = taskService.createTaskQuery().active().list();
//        for (Task t : tasks) {
//            System.out.println(t);
//        }
//        taskService.complete("3f9818f3-4fd7-11ef-9b41-566cebc1dde2");
        ProcessInstance pi = runtimeService.startProcessInstanceByKey("dynamic_choose");

        EventSubscription subscription = runtimeService.createEventSubscriptionQuery()
                .processInstanceId(pi.getId()).eventType("message").singleResult();
        MessageCorrelationResult result = runtimeService.createMessageCorrelation("midMessage").correlateWithResult();
        System.out.println(result);

    }

    @Test
    void startTask() { // 启动任务，其实无需这个用户要在流程引擎里面
        Task task = taskService.createTaskQuery().taskAssignee("zhangsan").singleResult();
        System.out.println(task.getId());
        taskService.complete(task.getId());
    }

}
