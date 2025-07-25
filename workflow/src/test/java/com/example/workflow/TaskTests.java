package com.example.workflow;

import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.history.HistoricTaskInstance;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
public class TaskTests {

    @Resource
    private TaskService taskService;

    @Resource
    private HistoryService historyService;

    @Test
    void completeTask() { //任务执行
        List<Task> tasks =
                taskService.createTaskQuery().processDefinitionName("process_simple").taskAssignee("lisi").list();
        for(Task t : tasks){
            System.out.println(t);
        }

        taskService.complete(tasks.get(0).getId());
    }

    @Test
    void getTaskStatus() { //获取当前任务状态，配合前端显示--活动动图
        List<Task> tasks = taskService.createTaskQuery().processDefinitionName("process_simple").list();
        for (Task t : tasks) {
            System.out.println(t.getTaskDefinitionKey());
        }

    }

    @Test
    void toDoTaskList() { // 待办事项
        List<Task> taskList = taskService.createTaskQuery().taskAssignee("lisi").list();
        System.out.println("代办事项:");
        for (Task t : taskList) {
            System.out.println(t);
        }
    }

    @Test
    void doneTaskList() { // 已办事项
        List<HistoricTaskInstance> historicTaskInstances =
                historyService.createHistoricTaskInstanceQuery().taskAssignee("zhangsan").finished().list();
        System.out.println("已办事项:");
        for (HistoricTaskInstance htt : historicTaskInstances) {
            System.out.println(htt);
        }
    }

    @Test
    void redirectAssigneeTask() { //转办--待办任务转给他人
        Task task = taskService.createTaskQuery().taskAssignee("lisi").singleResult();
        taskService.delegateTask(task.getId(), "wangwu");
    }

}
