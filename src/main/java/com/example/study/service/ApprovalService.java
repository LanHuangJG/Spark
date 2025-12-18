//package com.example.study.service;
//
//import lombok.RequiredArgsConstructor;
//import org.flowable.engine.RuntimeService;
//import org.flowable.engine.TaskService;
//import org.flowable.engine.runtime.ProcessInstance;
//import org.flowable.task.api.Task;
//import org.springframework.stereotype.Service;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@Service
//@RequiredArgsConstructor
//public class ApprovalService {
//    private final RuntimeService runtimeService;
//    private final TaskService taskService;
//
//    public ProcessInstance startProcess(String applicant, String approver) {
//        Map<String, Object> variables = new HashMap<>();
//        variables.put("applicant", applicant);
//        variables.put("approver", approver);
//        return runtimeService.startProcessInstanceByKey("simpleApproval", variables);
//    }
//
//    public List<Task> getUserTasks(String userId) {
//        return taskService.createTaskQuery()
//                .taskAssignee(userId)
//                .list();
//    }
//    public void completeApplyTask(String taskId,Map<String,Object> variables){
//        taskService.complete(taskId,variables);
//    }
//    public void completeApproveTask(String taskId,boolean approved){
//        Map<String,Object> variables = new HashMap<>();
//        variables.put("approved",approved);
//        taskService.complete(taskId,variables);
//    }
//}
