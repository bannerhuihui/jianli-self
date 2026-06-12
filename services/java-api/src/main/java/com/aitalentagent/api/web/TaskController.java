package com.aitalentagent.api.web;

import com.aitalentagent.api.auth.AuthContext;
import com.aitalentagent.api.common.ApiResponse;
import com.aitalentagent.api.domain.AsyncTask;
import com.aitalentagent.api.service.TaskService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getTask(
            @PathVariable String taskId,
            HttpServletRequest request
    ) {
        AsyncTask task = taskService.getTask(taskId, AuthContext.requireUser().userId());
        return ResponseEntity.ok(ApiResponse.of(ApiMapper.task(task), requestId(request)));
    }

    private String requestId(HttpServletRequest request) {
        return request.getAttribute("requestId").toString();
    }
}
