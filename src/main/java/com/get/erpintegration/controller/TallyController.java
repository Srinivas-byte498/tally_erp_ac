package com.get.erpintegration.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.get.erpintegration.service.TallyService;

@RestController
@RequestMapping("/api/tally")
public class TallyController {

    private final TallyService tallyService;

    @Autowired
    public TallyController(TallyService tallyService) {
        this.tallyService = tallyService;
    }

    @PostMapping("/update-email")
    public String updateCompanyEmail(@RequestParam String companyName, @RequestParam String newEmail) {
        try {
            tallyService.updateEmail(companyName, newEmail);
            return "Email update request sent for company: " + companyName;
        } catch (Exception e) {
            return "Error updating email: " + e.getMessage();
        }
    }
}
