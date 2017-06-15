package com.bi.oranj.service.bi;

import com.bi.oranj.constant.Constants;
import com.bi.oranj.controller.bi.resp.RestResponse;
import com.bi.oranj.model.bi.*;
import com.bi.oranj.repository.bi.AdvisorRepository;
import com.bi.oranj.repository.bi.AumRepository;
import com.bi.oranj.repository.bi.ClientRepository;
import com.bi.oranj.repository.bi.FirmRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by harshavardhanpatil on 6/9/17.
 */
@Service
public class AUMService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    HttpServletResponse response;

    @Autowired
    private AumRepository aumRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AdvisorRepository advisorRepository;

    @Autowired
    private FirmRepository firmRepository;

    public RestResponse getAUMForAdmin(Integer pageNumber, String previousDate, String currentDate) {

        boolean validDate = validateInputDate(previousDate, currentDate);
        if (validDate == false) {
            return RestResponse.error("Date should be in 'yyyy-MM-dd' format");
        }
        try {
            AUMForAdmin aumForAdmin = new AUMForAdmin();
            List<FirmAUM> firmAUMList = new ArrayList<>();
            Page<Firm> firmList;
            firmList = firmRepository.findAll(new PageRequest(pageNumber, 100, Sort.Direction.ASC, "firmName"));
            for (int i=0; i<firmList.getContent().size(); i++){

                FirmAUM firmAUM = new FirmAUM();
                firmAUM.setFirmId(firmList.getContent().get(i).getId());
                firmAUM.setName(firmList.getContent().get(i).getFirmName());
                firmAUM.setPrevious(getAUM(firmList.getContent().get(i).getId(), previousDate, "firm"));
                firmAUM.setCurrent(getAUM(firmList.getContent().get(i).getId(), currentDate, "firm"));
                firmAUMList.add(firmAUM);
            }
            aumForAdmin.setFirms(firmAUMList);
            aumForAdmin.setTotalFirms(firmList.getTotalElements());
            aumForAdmin.setLast(!firmList.hasNext());
            aumForAdmin.setPage(pageNumber);
            aumForAdmin.setCount(firmAUMList.size());
            return RestResponse.successWithoutMessage(aumForAdmin);
        } catch (Exception e) {
            log.error("Error in fecthing AUMs" + e);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return RestResponse.error("Error in fetching AUMs from Oranj DB");
        }
    }

    public RestResponse getAUMForFirm(Long firmId, String previousDate, String currentDate, Integer pageNumber) {

        boolean validDate = validateInputDate(previousDate, currentDate);
        if (validDate == false) {
            return RestResponse.error("Date should be in 'yyyy-MM-dd' format");
        }
        try {
            AUMForFirm aumForFirm = new AUMForFirm();
            List<AdvisorAUM> advisorAUMList = new ArrayList<>();
            Page<Advisor> advisorList;
            advisorList = advisorRepository.findByFirmId(firmId, new PageRequest(pageNumber, 100, Sort.Direction.ASC, "advisorFirstName"));
            for (int i=0; i<advisorList.getContent().size(); i++){

                AdvisorAUM advisorAUM = new AdvisorAUM();
                advisorAUM.setAdvisorId(advisorList.getContent().get(i).getId());
                advisorAUM.setName(advisorList.getContent().get(i).getAdvisorFirstName() + " " + advisorList.getContent().get(i).getAdvisorLastName());
                advisorAUM.setPrevious(getAUM(advisorList.getContent().get(i).getId(), previousDate, "advisor"));
                advisorAUM.setCurrent(getAUM(advisorList.getContent().get(i).getId(), currentDate, "advisor"));
                advisorAUMList.add(advisorAUM);
            }
            aumForFirm.setAdvisors(advisorAUMList);
            aumForFirm.setTotalAdvisors(advisorList.getTotalElements());
            aumForFirm.setLast(!advisorList.hasNext());
            aumForFirm.setPage(0);
            aumForFirm.setCount(advisorAUMList.size());
            return RestResponse.successWithoutMessage(aumForFirm);
        } catch (Exception e) {
            log.error("Error in fecthing AUMs" + e);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return RestResponse.error("Error in fetching AUMs from Oranj DB");
        }
    }

    public RestResponse getAUMForAdvisor(Long advisorId, String previousDate, String currentDate, Integer pageNumber) {

        boolean validDate = validateInputDate(previousDate, currentDate);
        if (validDate == false){
            return RestResponse.error("Date should be in 'yyyy-MM-dd' format");
        }
        try {
            AUMForAdvisor aumForAdvisor = new AUMForAdvisor();
            List<ClientAUM> clientAUMList = new ArrayList<>();
            Page<Client> clientList;
            clientList = clientRepository.findByAdvisorId(advisorId, new PageRequest(pageNumber, 100, Sort.Direction.ASC, "clientFirstName"));
            for (int i=0; i<clientList.getContent().size(); i++){

                ClientAUM clientAUM = new ClientAUM();
                clientAUM.setClientId(clientList.getContent().get(i).getId());
                clientAUM.setName(clientList.getContent().get(i).getClientFirstName() + " " + clientList.getContent().get(i).getClientLastName());
                clientAUM.setPrevious(getAUM(clientList.getContent().get(i).getId(), previousDate, "client"));
                clientAUM.setCurrent(getAUM(clientList.getContent().get(i).getId(), currentDate, "client"));
                clientAUMList.add(clientAUM);
            }
            aumForAdvisor.setClients(clientAUMList);
            aumForAdvisor.setTotalClients(clientList.getTotalElements());
            aumForAdvisor.setLast(!clientList.hasNext());
            aumForAdvisor.setPage(pageNumber);
            aumForAdvisor.setCount(clientAUMList.size());
            return RestResponse.successWithoutMessage(aumForAdvisor);
        } catch (Exception e) {
            log.error("Error in fecthing AUMs" + e);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return RestResponse.error("Error in fetching AUMs from Oranj DB");
        }
    }

    public AumDiff getAUM(Long id, String date, String aumFor){

        String startDate = date + Constants.SPACE + Constants.START_SECOND_OF_THE_DAY;
        String endDate = date + Constants.SPACE + Constants.LAST_SECOND_OF_THE_DAY;

        AumDiff aumDiff = new AumDiff();
        aumDiff.setDate(date);
        Map<String, BigDecimal> assetClass = new HashMap<>();
        aumDiff.setTotal(new BigDecimal(0));
        List<Object[]> positionsResultSet = null;

        switch (aumFor){
            case "client":
                positionsResultSet = aumRepository.findAUMsForClient(id, startDate, endDate);
                break;
            case "advisor":
                positionsResultSet = aumRepository.findAUMsForAdvisor(id, startDate, endDate);
                break;
            case "firm":
                positionsResultSet = aumRepository.findAUMsForFirm(id, startDate, endDate);
                break;
            default:
                break;
        }

        for (Object[] resultSet : positionsResultSet){
            assetClass.put(resultSet[3].toString(), (BigDecimal) resultSet[4]);
            aumDiff.setTotal(aumDiff.getTotal().add((BigDecimal) resultSet[4]));
        }
        aumDiff.setAssetClass(assetClass);
        return aumDiff;
    }

    public boolean validateInputDate(String previousDate, String currentDate){

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date previous = sdf.parse(previousDate);
            Date current = sdf.parse(currentDate);
        } catch (Exception e){
            log.error("Date should be in 'yyyy-MM-dd' format");
            return false;
        }
        return true;
    }
}


