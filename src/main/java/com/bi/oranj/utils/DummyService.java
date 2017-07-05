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

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

    public void createData (int numberOfFirms, int numberOfAdvisors, Set<Long> clientIds) throws ParseException {
        if (clientIds.isEmpty()) return;

        Random random = new Random();
        List<Firm> firms = new ArrayList<>();
        List<Advisor> advisors = new ArrayList<>();
        List<Client> clients = new ArrayList<>();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String startDate = "2016-01-01";
        String endDate = "2017-06-20";

        for (int i = 0; i < numberOfFirms; i++){
            Firm firm = new Firm();
            firm.setId(Long.valueOf(dataFactory.getNumberBetween(1, 4000)));
            firm.setFirmName(dataFactory.getRandomWord());
            firm.setActive(true);
            firm.setCreatedOn(new Timestamp(dataFactory.getDateBetween(simpleDateFormat.parse(startDate), simpleDateFormat.parse(endDate)).getTime()));
            firms.add(firm);
        }
        firmRepository.save(firms);

        for (int i = 0; i < numberOfAdvisors; i++){
            Advisor advisor = new Advisor();
            advisor.setId(Long.valueOf(dataFactory.getNumberBetween(1, 4000)));
            advisor.setAdvisorFirstName(dataFactory.getFirstName());
            advisor.setAdvisorLastName(dataFactory.getLastName());
            advisor.setFirmId(firms.get(random.nextInt(firms.size())).getId());
            advisor.setCreatedOn(new Timestamp(dataFactory.getDateBetween(simpleDateFormat.parse(startDate), simpleDateFormat.parse(endDate)).getTime()));
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
            client.setCreatedOn(new Timestamp(dataFactory.getDateBetween(simpleDateFormat.parse(startDate), simpleDateFormat.parse(endDate)).getTime()));
            client.setActive(true);
            clients.add(client);
        }


        clientRepository.save(clients);

    }

}
