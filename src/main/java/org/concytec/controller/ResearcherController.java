package org.concytec.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/research")
public class ResearcherController {
    @GetMapping("/researchers/{researcherId}/requests")
    void findAllRequestResearchByResearcherId(){
    }


}
