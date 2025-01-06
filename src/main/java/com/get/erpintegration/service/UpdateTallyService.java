package com.get.erpintegration.service;

import org.apache.camel.CamelContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class UpdateTallyService {

    @Value("${tally.server.url}")
    private String tallyServerUrl;

    private final CamelContext camelContext;

    public UpdateTallyService(CamelContext camelContext) {
        this.camelContext = camelContext;
    }

    // Method to update a company's email in Tally
    public void updateCompanyEmail(String companyName, String newEmail) {
        String updateXml = getUpdateCompanyEmailXml(companyName, newEmail);

        try {
            // Send the XML request to Tally
            String response = camelContext.createProducerTemplate()
                    .requestBody(tallyServerUrl, updateXml, String.class);

            System.out.println("Response from Tally: " + response);

            if (response.contains("RESPONSE") && response.contains("SUCCESS")) {
                System.out.println("Email updated successfully for company: " + companyName);
            } else {
                System.err.println("Failed to update email in Tally: " + response);
            }
        } catch (Exception e) {
            System.err.println("Error updating email in Tally: " + e.getMessage());
        }
    }

    // Helper method to generate the XML request
    private String getUpdateCompanyEmailXml(String companyName, String newEmail) {
        return "<ENVELOPE>" +
                "<HEADER>" +
                "<VERSION>1</VERSION>" +
                "<TALLYREQUEST>Import</TALLYREQUEST>" +
                "<TYPE>Data</TYPE>" +
                "<ID>All Masters</ID>" +
                "</HEADER>" +
                "<BODY>" +
                "<DESC>" +
                "<STATICVARIABLES>" +
                "<IMPORTDUPS>Modify</IMPORTDUPS>" +
                "</STATICVARIABLES>" +
                "<DATA>" +
                "<TALLYMESSAGE>" +
                "<COMPANY NAME=\"" + companyName + "\">" +
                "<EMAIL>" + newEmail + "</EMAIL>" +
                "</COMPANY>" +
                "</TALLYMESSAGE>" +
                "</DATA>" +
                "</DESC>" +
                "</BODY>" +
                "</ENVELOPE>";
    }
}
