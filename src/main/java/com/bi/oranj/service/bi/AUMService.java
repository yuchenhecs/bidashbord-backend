package com.bi.oranj.service.bi;

import com.bi.oranj.constant.Constants;
import com.bi.oranj.controller.bi.resp.RestResponse;
import com.bi.oranj.model.bi.*;
import com.bi.oranj.repository.bi.AumRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.math.BigInteger;
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

    public RestResponse getAUMForAdmin(Integer pageNumber, String previousDate, String currentDate) {

        boolean validDate = validateInputDate(previousDate, currentDate);
        if (validDate == false){
            return RestResponse.error("Date should be in 'yyyy-MM-dd' format");
        } else {

            String currentStartDate = currentDate + Constants.SPACE + Constants.START_SECOND_OF_THE_DAY;
            String currentEndDate = currentDate + Constants.SPACE + Constants.LAST_SECOND_OF_THE_DAY;

            AUMForAdmin aumForAdmin = new AUMForAdmin();
            List<FirmAUM> firmsList = new ArrayList<>();
            HashMap<BigInteger, FirmAUM> mapOfFirmIdToFirmAum = new HashMap<>();
            try {
                List<Object[]> aumForAdminResultSet = aumRepository.findAUMsForAdmin(currentStartDate, currentEndDate);
                for (Object[] aumResultSet : aumForAdminResultSet) {

                    BigInteger readFirmId = (BigInteger) aumResultSet[0];
                    if(!mapOfFirmIdToFirmAum.containsKey(readFirmId)){

                        FirmAUM firmAUM = new FirmAUM();
                        firmAUM.setFirmId(readFirmId);
                        firmAUM.setName(aumResultSet[1].toString());

                        setAUMDiff(aumResultSet, firmAUM, previousDate, currentDate);

                        firmsList.add(firmAUM);
                        mapOfFirmIdToFirmAum.put(readFirmId, firmAUM);
                    } else {

                        FirmAUM firmAUM = mapOfFirmIdToFirmAum.get(readFirmId);
                        AumDiff aumDiffCurrent = firmAUM.getCurrent();
                        Map<String, BigDecimal> assetClassMap = aumDiffCurrent.getAssetClass();
                        assetClassMap.put(aumResultSet[2].toString(), (BigDecimal) aumResultSet[3]);
                        aumDiffCurrent.setTotal(aumDiffCurrent.getTotal().add((BigDecimal) aumResultSet[3]));
                    }
                }
                aumForAdmin.setFirms(firmsList);
                aumForAdmin.setTotalFirms(firmsList.size());
                aumForAdmin.setLast(true);
                aumForAdmin.setPage(0);
                aumForAdmin.setCount(firmsList.size());
            }catch (Exception e){
                log.error("Error in fecthing AUMs" + e);
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return RestResponse.error("Error in fetching AUMs from Oranj DB");
            }
            return RestResponse.successWithoutMessage(aumForAdmin);
        }
    }


    public RestResponse getAUMForFirm(Long firmId, Integer pageNumber) {

        AUMForFirm aumForFirm = new AUMForFirm();
        List<AdvisorAUM> advisorsList = new ArrayList<>();
        HashMap<BigInteger, AdvisorAUM> mapOfAdvisorIdToAdvisorAum = new HashMap<>();

        try {
            log.info("firmId :::"+firmId);
            List<Object[]> aumForFirmResultSet = aumRepository.findAUMsForFirm(firmId);
            for (Object[] aumResultSet : aumForFirmResultSet) {

                BigInteger readAdvisorId = (BigInteger) aumResultSet[0];
                if(!mapOfAdvisorIdToAdvisorAum.containsKey(readAdvisorId)){

                    AdvisorAUM advisorAUM = new AdvisorAUM();
                    advisorAUM.setAdvisorId(readAdvisorId);
                    advisorAUM.setName(aumResultSet[1].toString());

                    setAUMDiff(aumResultSet, advisorAUM);

                    advisorsList.add(advisorAUM);
                    mapOfAdvisorIdToAdvisorAum.put(readAdvisorId, advisorAUM);
                } else {
                    AdvisorAUM advisorAUM = mapOfAdvisorIdToAdvisorAum.get(readAdvisorId);
                    AumDiff aumDiffCurrent = advisorAUM.getCurrent();
                    Map<String, BigDecimal> assetClassMap = aumDiffCurrent.getAssetClass();
                    assetClassMap.put(aumResultSet[2].toString(), (BigDecimal) aumResultSet[3]);
                    aumDiffCurrent.setTotal(aumDiffCurrent.getTotal().add((BigDecimal) aumResultSet[3]));
                }
            }
            aumForFirm.setAdvisors(advisorsList);
            aumForFirm.setTotalAdvisors(advisorsList.size());
            aumForFirm.setLast(true);
            aumForFirm.setPage(0);
            aumForFirm.setCount(advisorsList.size());

        }catch (Exception e){
            log.error("Error in fecthing AUMs" + e);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return RestResponse.error("Error in fetching AUMs from Oranj DB");
        }
        return RestResponse.successWithoutMessage(aumForFirm);
    }


    public RestResponse getAUMForAdvisor(Long advisorId, Integer pageNumber) {

        AUMForAdvisor aumForAdvisor = new AUMForAdvisor();
        List<ClientAUM> clientList = new ArrayList<>();
        HashMap<BigInteger, ClientAUM> mapOfClientIdToClientAum = new HashMap<>();

        log.info("advisorId :::" + advisorId);
        try {
            List<Object[]> aumForFirmResultSet = aumRepository.findAUMsForAdvisor(advisorId);
            for (Object[] aumResultSet : aumForFirmResultSet) {

                BigInteger readClientId = (BigInteger) aumResultSet[0];
                if(!mapOfClientIdToClientAum.containsKey(readClientId)){

                    ClientAUM clientAUM = new ClientAUM();
                    clientAUM.setClientId(readClientId);
                    clientAUM.setName(aumResultSet[1].toString());

                    setAUMDiff(aumResultSet, clientAUM);

                    clientList.add(clientAUM);
                    mapOfClientIdToClientAum.put(readClientId, clientAUM);
                } else {
                    ClientAUM clientAUM = mapOfClientIdToClientAum.get(readClientId);
                    AumDiff aumDiffCurrent = clientAUM.getCurrent();
                    Map<String, BigDecimal> assetClassMap = aumDiffCurrent.getAssetClass();
                    assetClassMap.put(aumResultSet[2].toString(), (BigDecimal) aumResultSet[3]);
                    aumDiffCurrent.setTotal(aumDiffCurrent.getTotal().add((BigDecimal) aumResultSet[3]));
                }
            }
            aumForAdvisor.setClients(clientList);
            aumForAdvisor.setTotalClients(clientList.size());
            aumForAdvisor.setLast(true);
            aumForAdvisor.setPage(0);
            aumForAdvisor.setCount(clientList.size());

        }catch (Exception e){
            log.error("Error in fecthing AUMs" + e);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return RestResponse.error("Error in fetching AUMs from Oranj DB");
        }
        return RestResponse.successWithoutMessage(aumForAdvisor);
    }


    public void setAUMDiff(Object[] aumResultSet, AdvisorAUM advisorAUM){

        // Set Current values
        AumDiff aumDiffCurrent = new AumDiff();
        BigDecimal total = (BigDecimal) aumResultSet[3];
        aumDiffCurrent.setTotal(total);

        Map<String, BigDecimal> assetTypeMap = new HashMap<String, BigDecimal>();
        assetTypeMap.put(aumResultSet[2].toString(), (BigDecimal) aumResultSet[3]);
        aumDiffCurrent.setAssetClass(assetTypeMap);

        advisorAUM.setCurrent(aumDiffCurrent);

        // Set Source values
        AumDiff aumDiffSource = new AumDiff();
        // Fecth data from History table and add
        advisorAUM.setPrevious(aumDiffSource);
    }

    public void setAUMDiff(Object[] aumResultSet, ClientAUM clientAUM){

        // Set Current values
        AumDiff aumDiffCurrent = new AumDiff();
        BigDecimal total = (BigDecimal) aumResultSet[3];
        aumDiffCurrent.setTotal(total);

        Map<String, BigDecimal> assetTypeMap = new HashMap<String, BigDecimal>();
        assetTypeMap.put(aumResultSet[2].toString(), (BigDecimal) aumResultSet[3]);
        aumDiffCurrent.setAssetClass(assetTypeMap);

        clientAUM.setCurrent(aumDiffCurrent);

        // Set Source values
        AumDiff aumDiffSource = new AumDiff();
        // Fecth data from History table and add
        clientAUM.setPrevious(aumDiffSource);
    }

    public void setAUMDiff(Object[] aumResultSet, FirmAUM firmAUM, String sourceDate, String comparisonDate){

        // Set Current values
        AumDiff aumDiffCurrent = new AumDiff();
        aumDiffCurrent.setDate(comparisonDate);
        BigDecimal total = (BigDecimal) aumResultSet[3];
        aumDiffCurrent.setTotal(total);

        Map<String, BigDecimal> assetTypeMap = new HashMap<String, BigDecimal>();
        assetTypeMap.put(aumResultSet[2].toString(), (BigDecimal) aumResultSet[3]);
        aumDiffCurrent.setAssetClass(assetTypeMap);

        firmAUM.setCurrent(aumDiffCurrent);

        // Set Source values
        AumDiff aumDiffSource = new AumDiff();
        aumDiffSource.setDate(sourceDate);
        // Fecth data from History table and add
        firmAUM.setPrevious(aumDiffSource);
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


