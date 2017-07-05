package com.bi.oranj.utils;


import com.bi.oranj.model.bi.Advisor;
import com.bi.oranj.model.bi.Client;
import com.bi.oranj.model.bi.Firm;
import com.bi.oranj.repository.bi.AdvisorRepository;
import com.bi.oranj.repository.bi.ClientRepository;
import com.bi.oranj.repository.bi.FirmRepository;
import org.fluttercode.datafactory.impl.DataFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by jaloliddinbakirov on 6/16/17.
 */
@Service
public class DummyService {

    @Autowired
    FirmRepository firmRepository;

    @Autowired
    AdvisorRepository advisorRepository;

    @Autowired
    ClientRepository clientRepository;


    private DataFactory dataFactory = new DataFactory();

    public void createData (int numberOfFirms, int numberOfAdvisors, Set<Long> clientIds){
        if (clientIds.isEmpty()) return;

        Random random = new Random();
        List<Firm> firms = new ArrayList<>();
        List<Advisor> advisors = new ArrayList<>();
        List<Client> clients = new ArrayList<>();

        for (int i = 0; i < numberOfFirms; i++){
            Firm firm = new Firm();
            firm.setId(Long.valueOf(dataFactory.getNumberBetween(1, 4000)));
            firm.setFirmName(dataFactory.getRandomWord());
            firm.setActive(true);
            firms.add(firm);
        }
        firmRepository.save(firms);

        for (int i = 0; i < numberOfAdvisors; i++){
            Advisor advisor = new Advisor();
            advisor.setId(Long.valueOf(dataFactory.getNumberBetween(1, 4000)));
            advisor.setAdvisorFirstName(dataFactory.getFirstName());
            advisor.setAdvisorLastName(dataFactory.getLastName());
            advisor.setFirmId(firms.get(random.nextInt(firms.size())).getId());
            advisor.setActive(true);
            advisors.add(advisor);
        }
        advisorRepository.save(advisors);

        for (Long id : clientIds){
            Client client = new Client();
            client.setId(id);
            client.setClientFirstName(dataFactory.getFirstName());
            client.setClientLastName(dataFactory.getLastName());
            client.setAdvisorId(advisors.get(random.nextInt(advisors.size())).getId());
            client.setFirmId(firms.get(random.nextInt(firms.size())).getId());
            client.setActive(true);
            clients.add(client);
        }


        clientRepository.save(clients);

    }

}
