package com.bi.oranj.service;

import com.bi.oranj.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by jaloliddinbakirov on 5/30/17.
 */
@Service
public class ClientService {

    @Autowired
    ClientRepository clientRepository;
}
