package com.example.workflow.demos.controller;

import com.example.workflow.demos.model.TaskDto;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.repository.Deployment;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.engine.variable.VariableMap;
import org.camunda.bpm.engine.variable.Variables;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
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
    private TaskService taskService;

    @PostMapping("/deploy/{processKey}")
    public String deployFlow(@PathVariable("processKey") String processKey) {

        Deployment deployment = repositoryService.createDeployment()
                .name("process")
                .addClasspathResource(processKey.concat(".bpmn"))  //processKey为.bpmn的名称
                .deploy();
        return deployment.getId() + ":" + deployment.getName();
    }

    @GetMapping("/start/{processKey}")
    public void start(@PathVariable(value = "processKey") String processKey) {

        // 获取已部署的process
//        List<ProcessDefinition> definitinList =
//                repositoryService.createProcessDefinitionQuery().latestVersion().list();

        identityService.setAuthenticatedUserId("zhangsan");

        final VariableMap variables = Variables.createVariables();
        variables.putValue("starter", "zhangsan");  //获取登录用户

        runtimeService.startProcessInstanceByKey(processKey, variables);
//        runtimeService.startProcessInstanceById("062f647e-48ee-11ef-88ac-566cebc1dde2");
    }

    @PostMapping("/task/list/{userId}")
    public List<TaskDto> taskList(@PathVariable("userId") String userId) {
        final List<Task> list = taskService.createTaskQuery()
                .taskAssignee(userId).list();

        List<TaskDto> dtoList = new ArrayList<>();
        for (Task task : list) {
            TaskDto dto = new TaskDto();
            dto.setId(task.getId());
            dto.setName(task.getName());
            dto.setAssignee(task.getAssignee());
            dto.setCreateTime(task.getCreateTime());
            dtoList.add(dto);
        }
        return dtoList;
    }

}
