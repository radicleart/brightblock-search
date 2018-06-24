package org.brightblock.lightening.web;

import org.brightblock.lightening.service.LndRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LndSyncController {

	@Autowired private LndRepository lndRepo;

	@RequestMapping("/lnd/{id}")
    String customer(@PathVariable Integer id) {
        return "Hi";
    }
}
