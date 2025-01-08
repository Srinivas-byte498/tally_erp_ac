package com.get.erpintegration.controller;

import com.get.erpintegration.model.Request;
import com.get.erpintegration.service.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/requests")
public class RequestController {

    @Autowired
    private RequestService requestService;

    @GetMapping("/status/{status}")
    public List<Request> getRequestsByStatus(@PathVariable String status) {
        return requestService.getRequestsByStatus(status);
    }

    @PostMapping
    public Request saveRequest(@RequestBody Request request) {
        return requestService.saveRequest(request);
    }
}