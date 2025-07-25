package com.example.workflow;

import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.history.HistoricActivityInstance;
import org.camunda.bpm.engine.history.HistoricProcessInstance;
import org.camunda.bpm.engine.history.HistoricTaskInstance;
import org.camunda.bpm.engine.repository.Deployment;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.runtime.ActivityInstance;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.engine.variable.VariableMap;
import org.camunda.bpm.engine.variable.Variables;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
public class ProcessTests {

    @Resource
    private RuntimeService runtimeService;

    @Resource
    private RepositoryService repositoryService;

    @Resource
    private HistoryService historyService;

    @Resource
    private TaskService taskService;

    @Test
    void deployProcess(){
//        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().singleResult();
//        System.out.println(processDefinition);
//        System.out.println(processDefinition.getName());
//        Deployment deployment = repositoryService.createDeploymentQuery().singleResult();
//        System.out.println(deployment);
//        runtimeService.startProcessInstanceByKey(processDefinition.getName());
    }

    @Test
    void startProcess(){ //启动流程一定要初始化stater
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionName("process_simple").singleResult();
        System.out.println(processDefinition);
//        Deployment deployment = repositoryService.createDeploymentQuery().singleResult();
//        System.out.println(deployment);
        final VariableMap variables = Variables.createVariables();
        //会签-默认
        variables.putValue("starter", "zhangsan"); //占位符名和人员要传进来
//        variables.putValue("starter", "lisi"); //占位符名和人员要传进来

        runtimeService.startProcessInstanceByKey(processDefinition.getName(),"会签方式", variables);

    }

    @Test
    void deleteDefinition(){
        List<Deployment> deployments =  repositoryService.createDeploymentQuery().list();
        System.out.println(deployments);
        for (Deployment pd : deployments) { //删除部署的deployment
            repositoryService.deleteDeployment(pd.getId());
        }

        List<ProcessDefinition> processDefinitions =  repositoryService.createProcessDefinitionQuery().list();
        System.out.println(processDefinitions);
        for (ProcessDefinition pd : processDefinitions) {
            repositoryService.deleteProcessDefinition(pd.getId());
        }
    }

    @Test
    void deleteProcess(){ //流程撤销
        List<ProcessInstance> processInstance = runtimeService.createProcessInstanceQuery().list();
        System.out.println(processInstance);
        for (ProcessInstance p : processInstance) {
            runtimeService.deleteProcessInstance(p.getId(), "test");
        }
//        ProcessInstance processInstance =
//                runtimeService.createProcessInstanceQuery().processDefinitionKey("dynamic_choose").singleResult();
//        System.out.println(processInstance);
//        runtimeService.deleteProcessInstance(processInstance.getId(), "test");
    }

    @Test
    void addAssign() {//增加会签
        ProcessInstance processInstance =
                runtimeService.createProcessInstanceQuery().active().processDefinitionKey("dynamic_choose").singleResult();
        System.out.println(processInstance);
        List<String> ids = runtimeService.getActiveActivityIds(processInstance.getId());
//        for (String s : ids) {
//            System.out.println(s);
//        }
        runtimeService.createProcessInstanceModification(processInstance.getId())
                .startBeforeActivity(ids.get(0))
                .setVariable("starter","lisi")
                .execute();
    }

    @Test
    void suspendProcess(){ //挂起流程
        ProcessInstance processInstance =
                runtimeService.createProcessInstanceQuery().active().processDefinitionKey("dynamic_choose").processInstanceBusinessKey("会签方式").singleResult();
        System.out.println(processInstance);
        runtimeService.suspendProcessInstanceById(processInstance.getId());
        //另外一种方式
//        runtimeService.updateProcessInstanceSuspensionState().byProcessDefinitionKey("dynamic_choose").suspend();

    }

    @Test
    void activeProcess(){ //激活流程
//        ProcessInstance processInstance =
//                runtimeService.createProcessInstanceQuery().processDefinitionKey("dynamic_choose").processInstanceBusinessKey("会签方式").singleResult();
//        System.out.println(processInstance);
//        runtimeService.activateProcessInstanceById(processInstance.getId());
        // 另外一种方式
        runtimeService.updateProcessInstanceSuspensionState().byProcessDefinitionKey("dynamic_choose").activate();
    }

    @Test
    void restartProcess(){ //重新激活一个已完成的任务--追加放款
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(
                "9f23b573-50a8-11ef-9fb5-566cebc1dde2").singleResult();
        System.out.println("流程定义:" + processDefinition);
        HistoricProcessInstance historicProcessInstance =
                historyService.createHistoricProcessInstanceQuery().completed().processDefinitionId(processDefinition.getId()).singleResult();
        System.out.println("流程实例:" + historicProcessInstance.getId());
        runtimeService.restartProcessInstances(processDefinition.getId()) //definitioinID
                .startTransition("Flow_0xsfbcd") //连线 --如何获取连线这些信息？
//                .startBeforeActivity(historicProcessInstance.getStartActivityId()) //从指定activity
                .processInstanceIds(historicProcessInstance.getId())
                .execute();
    }

    @Test
    void rejectProcess() { //驳回流程
        // 获取流程定义
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(
                "9f23b573-50a8-11ef-9fb5-566cebc1dde2").singleResult();
        System.out.println("流程定义:" + processDefinition);

        // 获取流程实例
        List<ProcessInstance> processInstances =
                runtimeService.createProcessInstanceQuery().processDefinitionKey(processDefinition.getKey()).list();
        for (ProcessInstance p : processInstances) {
            System.out.println("流程实例:" + p);
        }

        //获取task
        Task task = taskService.createTaskQuery().processInstanceId(processInstances.get(0).getId()).singleResult();
        System.out.println("任务实例:" + task);
        System.out.println("任务节点:" + task.getTaskDefinitionKey()); //获取当前流程状态

        // 获取activity
        ActivityInstance activity = runtimeService.getActivityInstance(processInstances.get(0).getProcessInstanceId());
        System.out.println(activity);

        // 驳回流程到指定位置，
        runtimeService.createProcessInstanceModification(processInstances.get(0).getProcessInstanceId()) //processInstanceId
                .cancelActivityInstance(activity.getId())//activityInstanceId
                .setAnnotation("流程驳回")
                .startTransition("Flow_0xsfbcd")//线，驳回的位置
                .setVariable("starter", "zhangsan")
                .execute();

    }

    @Test
    void recallProcess(){ //流程撤回
// 获取流程定义
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(
                "9f23b573-50a8-11ef-9fb5-566cebc1dde2").singleResult();
        System.out.println("流程定义:" + processDefinition);

        // 获取流程实例
        List<ProcessInstance> processInstances =
                runtimeService.createProcessInstanceQuery().processDefinitionKey(processDefinition.getKey()).list();
//        for (ProcessInstance p : processInstances) {
//            System.out.println("流程实例:" + p);
//        }
        processInstances.forEach(System.out::println);

        //获取task
        Task task = taskService.createTaskQuery().processInstanceId(processInstances.get(0).getId()).singleResult();
        System.out.println("任务实例:" + task);
        System.out.println("任务节点:" + task.getTaskDefinitionKey()); //获取当前流程状态

//        //获取已走完的节点
//        List<HistoricTaskInstance> historicTaskInstances = historyService.createHistoricTaskInstanceQuery().processInstanceId(
//                processInstances.get(0).getId()).processUnfinished().taskDefinitionKey(task.getTaskDefinitionKey()).list();
//        for (HistoricTaskInstance htt : historicTaskInstances) {
//            System.out.println(htt);
//
//        }

        // 获取activity
        ActivityInstance activity = runtimeService.getActivityInstance(processInstances.get(0).getProcessInstanceId());
        System.out.println(activity);

        // 撤回到流程的发起人位置
        runtimeService.createProcessInstanceModification(processInstances.get(0).getProcessInstanceId()) //processInstanceId
                .cancelActivityInstance(activity.getId())//activityInstanceId
                .setAnnotation("流程撤回")
                .startBeforeActivity("Activity_10i5qq4")
                .setVariable("starter", "zhangsan") //可以放初始化的变量
                .execute();
    }
}

