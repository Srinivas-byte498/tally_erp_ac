package com.get.erpintegration.service;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.CamelContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.annotation.PostConstruct;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;

import com.get.erpintegration.model.Company;
import com.get.erpintegration.model.Ledger;
import com.get.erpintegration.repository.CompanyRepository;
import com.get.erpintegration.repository.LedgerRepository;

@Service
public class TallyService {

    @Value("${tally.server.url}")
    private String tallyServerUrl;

    @Value("${tally.timer.period}")
    private String timerPeriod;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private LedgerRepository ledgerRepository;

    @Autowired
    private CamelContext camelContext;

    @PostConstruct
    public void init() {
        System.out.println("Camel Context Initialized: " + camelContext);

        try {
            camelContext.addRoutes(tallyRoute());
        } catch (Exception e) {
            System.err.println("Failed to add Tally route: " + e.getMessage());
        }
    }

    public RouteBuilder tallyRoute() {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("timer:tally?period=" + timerPeriod)
                        .routeId("TallyCompaniesRoute")
                        .log("Timer triggered...")
                        .setHeader("Content-Type", constant("application/xml"))
                        .setBody(constant(getCompaniesXMLRequest()))
                        .to(tallyServerUrl)
                        .process(exchange -> {
                            System.out.println("Timer triggered: Sending request to Tally...");
                            String response = exchange.getIn().getBody(String.class);
                            parseCompanies(response);
                        })
                        .onException(Exception.class)
                        .log("Error occurred while fetching companies from Tally: ${exception.message}")
                        .handled(true);
                
                        from("timer:tally?period=" + timerPeriod)
                        .routeId("TallyLedgersRoute")
                        .log("Timer triggered...")
                        .setHeader("Content-Type", constant("application/xml"))
                        .setBody(constant(getCompanyLedgerXMLRequest()))
                        .to(tallyServerUrl)
                        .process(exchange -> {
                            System.out.println("Timer triggered: Sending ledger request to Tally...");
                            String response = exchange.getIn().getBody(String.class);
                            parseLedgerData(response);
                        })
                        .onException(Exception.class)
                        .log("Error occurred while fetching ledgers from Tally: ${exception.message}")
                        .handled(true);
            }
        };
    }

    // XML request to be sent to Tally
    private String getCompaniesXMLRequest() {
        return "<ENVELOPE>" +
                    "<HEADER>" +
                        "<VERSION>1</VERSION>" +
                        "<TALLYREQUEST>Export</TALLYREQUEST>" +
                        "<TYPE>Collection</TYPE>" +
                        "<ID>Company Contact Details</ID>" +
                    "</HEADER>" +
                    "<BODY>" +
                        "<DESC>" +
                            "<STATICVARIABLES>" +
                                "<SVCurrentCompany>YourCompanyName</SVCurrentCompany>" +
                            "</STATICVARIABLES>" +
                            "<TDL>" +
                                "<TDLMESSAGE>" +
                                    "<COLLECTION ISMODIFY=\"No\" ISFIXED=\"No\" ISINITIALIZE=\"Yes\" ISOPTION=\"No\" ISINTERNAL=\"No\" NAME=\"Company Contact Details\">" +
                                        "<TYPE>Company</TYPE>" +
                                        "<NATIVEMETHOD>Name</NATIVEMETHOD>" +
                                        "<NATIVEMETHOD>PhoneNumber</NATIVEMETHOD>" +
                                        "<NATIVEMETHOD>Email</NATIVEMETHOD>" +
                                        "<NATIVEMETHOD>Website</NATIVEMETHOD>" +
                                    "</COLLECTION>" +
                                "</TDLMESSAGE>" +
                            "</TDL>" +
                        "</DESC>" +
                    "</BODY>" +
                "</ENVELOPE>";
    }

    private String getCompanyLedgerXMLRequest() {
        return "<ENVELOPE>" +
               "    <HEADER>" +
               "        <TALLYREQUEST>Export Data</TALLYREQUEST>" +
               "    </HEADER>" +
               "    <BODY>" +
               "        <EXPORTDATA>" +
               "            <REQUESTDESC>" +
               "                <REPORTNAME>Trial Balance</REPORTNAME>" +
               "                <STATICVARIABLES>" +
               "                    <SVCURRENTCOMPANY>GSSPEC</SVCURRENTCOMPANY>" +
               "                    <ISLEDGERWISE>YES</ISLEDGERWISE>" +
               "                    <SVEXPORTFORMAT>$$SysName:XML</SVEXPORTFORMAT>" +
               "                </STATICVARIABLES>" +
               "            </REQUESTDESC>" +
               "        </EXPORTDATA>" +
               "    </BODY>" +
               "</ENVELOPE>";
    }
    

    private void parseCompanies(String xmlResponse) {
        if (xmlResponse == null || xmlResponse.isEmpty()) {
            System.out.println("Received an empty response from Tally.");
        } else {
            try {
                // Parse the XML response
                Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder()
                        .parse(new InputSource(new StringReader(xmlResponse)));
    
                // Extract company list from XML (assuming companies are listed under <NAME>)
                NodeList companyNodes = document.getElementsByTagName("NAME");
                NodeList phoneNodes = document.getElementsByTagName("PHONENUMBER");
                NodeList emailNodes = document.getElementsByTagName("EMAIL");
                NodeList websiteNodes = document.getElementsByTagName("WEBSITE");
    
                System.out.println("List of Companies:");
                for (int i = 0; i < companyNodes.getLength(); i++) {
                    String companyName = getNodeTextContent(companyNodes, i);
                    String phoneNumber = getNodeTextContent(phoneNodes, i);
                    String email = getNodeTextContent(emailNodes, i);
                    String website = getNodeTextContent(websiteNodes, i);
    
                    // If any field is null, set it to "N/A"
                    companyName = companyName != null ? companyName : "N/A";
                    phoneNumber = phoneNumber != null ? phoneNumber : "N/A";
                    email = email != null ? email : "N/A";
                    website = website != null ? website : "N/A";
    
                    System.out.println(companyName);
    
                    // Check if the company already exists
                    var existingCompany = companyRepository.findByName(companyName);
    
                    if (existingCompany.isEmpty()) {
                        // Save company name and other details to MongoDB only if it doesn't exist
                        Company company = new Company(companyName, phoneNumber, email, website);
                        companyRepository.save(company);
                        System.out.println("Company saved: " + companyName);
                    } else {
                        // Update existing company with new details
                        Company companyToUpdate = existingCompany.get();
                        companyToUpdate.setPhoneNumber(phoneNumber);
                        companyToUpdate.setEmail(email);
                        companyToUpdate.setWebsite(website);
    
                        // Save updated company information to MongoDB
                        companyRepository.save(companyToUpdate);
                        System.out.println("Company updated: " + companyName);
                    }
                }
            } catch (Exception e) {
                System.err.println("Error parsing Tally response: " + e.getMessage());
            }
        }
    }

    private void parseLedgerData(String xmlResponse) {
        if (xmlResponse == null || xmlResponse.isEmpty()) {
            System.out.println("Received an empty response from Tally.");
        } else {
            try {
                // Parse the XML response
                Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder()
                        .parse(new InputSource(new StringReader(xmlResponse)));

                // Extract company list from XML (assuming companies are listed under <DSPDISPNAME>)
                NodeList ledgerNameNodes = document.getElementsByTagName("DSPDISPNAME");
                NodeList ledgerCRNodes = document.getElementsByTagName("DSPCLCRAMTA");
                NodeList ledgerDRNodes = document.getElementsByTagName("DSPCLDRAMTA");
                // System.out.println("ledgerName"+ledgerName);
                for (int i = 0; i < ledgerNameNodes.getLength(); i++) {
                    String ledgerName = getNodeTextContent(ledgerNameNodes, i);
                    String ledgerCR = getNodeTextContent(ledgerCRNodes, i);
                    String ledgerDR = getNodeTextContent(ledgerDRNodes, i);
                    System.out.println("ledgerName "+ledgerName);
                    System.out.println("ledgerCR "+ledgerCR);
                    System.out.println("ledgerDR "+ledgerDR);

                    String companyName = "GSSPEC";
                    // If any field is null, set it to "N/A"
                    ledgerName = ledgerName != null ? ledgerName : "N/A";
                    ledgerCR = ledgerCR != null ? ledgerCR : "-";
                    ledgerDR = ledgerDR != null ? ledgerDR : "-";
    
                    System.out.println(companyName);
    
                    // Check if the company already exists
                    var existingLedger = ledgerRepository.findByName(ledgerName);
    
                    if (existingLedger.isEmpty()) {
                        // Save company name and other details to MongoDB only if it doesn't exist
                        Ledger ledger = new Ledger(companyName, ledgerName, ledgerCR, ledgerDR);
                        ledgerRepository.save(ledger);
                        System.out.println("Ledger saved: " + ledgerName);
                    } else {
                        // Update existing ledger with new details
                        Ledger ledgerToUpdate = existingLedger.get();
                        ledgerToUpdate.setCompany(companyName);
                        ledgerToUpdate.setCR(ledgerCR);
                        ledgerToUpdate.setDR(ledgerDR);
    
                        // Save updated ledger information to MongoDB
                        ledgerRepository.save(ledgerToUpdate);
                        System.out.println("Ledger updated: " + ledgerName);
                    }
                }

            } catch (Exception e) {
                System.err.println("Error parsing Tally response: " + e.getMessage());
            }
        }
    }
    
    
    // Utility method to safely retrieve text content from a NodeList
    private String getNodeTextContent(NodeList nodeList, int index) {
        if (nodeList != null && index >= 0 && index < nodeList.getLength()) {
            org.w3c.dom.Node node = nodeList.item(index);
            if (node != null) {
                return node.getTextContent().trim();
            }
        }
        return null;
    }

    private final UpdateTallyService updateTallyService;

    @Autowired
    public TallyService(UpdateTallyService updateTallyService) {
        this.updateTallyService = updateTallyService;
    }

    // Example method to trigger email update
    public void updateEmail(String companyName, String newEmail) {
        updateTallyService.updateCompanyEmail(companyName, newEmail);
    }
}
