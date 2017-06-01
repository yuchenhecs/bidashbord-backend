package com.bi.oranj.controller;

import com.bi.oranj.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by jaloliddinbakirov on 5/30/17.
 */
@RestController
@RequestMapping("/goals")
public class ClientController {

    @Autowired
    ClientService clientService;
}
