
package com.get.erpintegration.service;

import com.get.erpintegration.model.Request;
import com.get.erpintegration.repository.RequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RequestService {

    @Autowired
    private RequestRepository requestRepository;

    public List<Request> getRequestsByStatus(String status) {
        return requestRepository.findByStatus(status);
    }

    public Request saveRequest(Request request) {
        return requestRepository.save(request);
    }
}
