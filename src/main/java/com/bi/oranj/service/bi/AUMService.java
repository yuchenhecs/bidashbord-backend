package com.bi.oranj.service.bi;

import com.bi.oranj.model.bi.*;
import com.bi.oranj.repository.bi.AdvisorRepository;
import com.bi.oranj.repository.bi.AumRepository;
import com.bi.oranj.repository.bi.ClientRepository;
import com.bi.oranj.repository.bi.FirmRepository;
import com.bi.oranj.utils.ApiResponseMessage;
import com.bi.oranj.utils.InputValidator;
import com.bi.oranj.utils.date.DateUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

import static com.bi.oranj.constant.Constants.*;

@Service
public class AUMService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    HttpServletResponse response;

    @Autowired
    private AumRepository aumRepository;

    @Autowired
    private AdvisorRepository advisorRepository;

    @Autowired
    private FirmRepository firmRepository;

    @Autowired
    private InputValidator inputValidator;

    @Autowired
    private AuthorizationService authorizationService;

    @Autowired
    DateUtility dateUtility;

    @Autowired
    ClientRepository clientRepository;

    public ResponseEntity<Object> getAUMForAdmin(Integer pageNumber, String previousDate, String currentDate) {
        try {
            if (!inputValidator.validateInputDate(previousDate, currentDate)) {
                return new ResponseEntity<>(new ApiResponseMessage(ERROR_DATE_VALIDATION), HttpStatus.BAD_REQUEST);
            }

            if(!inputValidator.validateInputPageNumber(pageNumber)){
                return new ResponseEntity<>(new ApiResponseMessage(ERROR_PAGE_NUMBER_VALIDATION), HttpStatus.BAD_REQUEST);
            }
            Map<Long, FirmAUM> map = new HashMap<>();
            AUMForAdmin aumForAdmin = new AUMForAdmin();
            List<FirmAUM> firmAUMList = new ArrayList<>();
            List<Firm> firmList = firmRepository.findByActiveTrueOrderByFirmNameAsc();
            for (int i=0; i<firmList.size(); i++){

                FirmAUM firmAUM = new FirmAUM();
                firmAUM.setFirmId(firmList.get(i).getId());
                firmAUM.setName(firmList.get(i).getFirmName());
                Map<String, BigDecimal> previousAssetClass = new HashMap<>();
                previousAssetClass.put("Cash", BigDecimal.ZERO);
                previousAssetClass.put("US Bond", BigDecimal.ZERO);
                previousAssetClass.put("Other", BigDecimal.ZERO);
                previousAssetClass.put("Non US Stock", BigDecimal.ZERO);
                previousAssetClass.put("US Stock", BigDecimal.ZERO);
                Map<String, BigDecimal> currentAssetClass = new HashMap<>();
                currentAssetClass.put("Cash", BigDecimal.ZERO);
                currentAssetClass.put("US Bond", BigDecimal.ZERO);
                currentAssetClass.put("Other", BigDecimal.ZERO);
                currentAssetClass.put("Non US Stock", BigDecimal.ZERO);
                currentAssetClass.put("US Stock", BigDecimal.ZERO);
                firmAUM.setPrevious(new AumDiff(previousDate, BigDecimal.ZERO, previousAssetClass));
                firmAUM.setCurrent(new AumDiff(currentDate, BigDecimal.ZERO, currentAssetClass));
                firmAUMList.add(firmAUM);
                map.put(firmAUM.getFirmId(), firmAUM);
            }

            List<Object[]> positionsResultSet = aumRepository.findAUMsForAdmin(previousDate);
            for (Object[] resultSet : positionsResultSet){
                FirmAUM firmAUM = map.get(((BigInteger) resultSet[0]).longValue());
                AumDiff aumDiff = firmAUM.getPrevious();
                Map<String, BigDecimal> assetClass = aumDiff.getAssetClass();
                assetClass.put(resultSet[1].toString(), assetClass.get(resultSet[1].toString()).add((BigDecimal) resultSet[2]));
                aumDiff.setTotal(aumDiff.getTotal().add((BigDecimal) resultSet[2]));
                aumDiff.setAssetClass(assetClass);
                firmAUM.setPrevious(aumDiff);
            }

            List<Object[]> positionsResultSet2 = aumRepository.findAUMsForAdmin(currentDate);
            for (Object[] resultSet : positionsResultSet2){
                FirmAUM firmAUM = map.get(((BigInteger) resultSet[0]).longValue());
                AumDiff aumDiff = firmAUM.getCurrent();
                Map<String, BigDecimal> assetClass = aumDiff.getAssetClass();
                assetClass.put(resultSet[1].toString(), assetClass.get(resultSet[1].toString()).add((BigDecimal) resultSet[2]));
                aumDiff.setTotal(aumDiff.getTotal().add((BigDecimal) resultSet[2]));
                aumDiff.setAssetClass(assetClass);
                firmAUM.setCurrent(aumDiff);
            }
            aumForAdmin.setFirms(firmAUMList);
            aumForAdmin.setTotalFirms(firmList.size());
            aumForAdmin.setHasNext(false);
            aumForAdmin.setPage(pageNumber);
            aumForAdmin.setCount(firmAUMList.size());
            return new ResponseEntity<>(aumForAdmin, HttpStatus.OK);
        } catch (Exception e) {
            log.error(ERROR_IN_GETTING_AUM, e);
            return new ResponseEntity<>(new ApiResponseMessage(ERROR_IN_GETTING_AUM), HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<Object> getAUMForFirm(Long firmId, String previousDate, String currentDate, Integer pageNumber) {

        try {
            if (!inputValidator.validateInputDate(previousDate, currentDate)) {
                return new ResponseEntity<>(new ApiResponseMessage(ERROR_DATE_VALIDATION), HttpStatus.BAD_REQUEST);
            }

            if(!inputValidator.validateInputPageNumber(pageNumber)){
                return new ResponseEntity<>(new ApiResponseMessage(ERROR_PAGE_NUMBER_VALIDATION), HttpStatus.BAD_REQUEST);
            }

            if(firmId == null){
                Client client = clientRepository.findById(authorizationService.getUserId());
                firmId = client.getFirmId();
            }

            Map<Long, AdvisorAUM> map = new HashMap<>();
            AUMForFirm aumForFirm = new AUMForFirm();
            List<AdvisorAUM> advisorAUMList = new ArrayList<>();
            List<Advisor> advisorList = advisorRepository.findByFirmIdAndActiveTrueOrderByAdvisorFirstNameAsc(firmId);
            for (int i=0; i<advisorList.size(); i++){

                AdvisorAUM advisorAUM = new AdvisorAUM();
                advisorAUM.setAdvisorId(advisorList.get(i).getId());
                advisorAUM.setName(advisorList.get(i).getAdvisorFirstName() + " " + advisorList.get(i).getAdvisorLastName());
                Map<String, BigDecimal> previousAssetClass = new HashMap<>();
                previousAssetClass.put("Cash", BigDecimal.ZERO);
                previousAssetClass.put("US Bond", BigDecimal.ZERO);
                previousAssetClass.put("Other", BigDecimal.ZERO);
                previousAssetClass.put("Non US Stock", BigDecimal.ZERO);
                previousAssetClass.put("US Stock", BigDecimal.ZERO);
                Map<String, BigDecimal> currentAssetClass = new HashMap<>();
                currentAssetClass.put("Cash", BigDecimal.ZERO);
                currentAssetClass.put("US Bond", BigDecimal.ZERO);
                currentAssetClass.put("Other", BigDecimal.ZERO);
                currentAssetClass.put("Non US Stock", BigDecimal.ZERO);
                currentAssetClass.put("US Stock", BigDecimal.ZERO);
                advisorAUM.setPrevious(new AumDiff(previousDate, BigDecimal.ZERO, previousAssetClass));
                advisorAUM.setCurrent(new AumDiff(currentDate, BigDecimal.ZERO, currentAssetClass));
                advisorAUMList.add(advisorAUM);
                map.put(advisorAUM.getAdvisorId(), advisorAUM);
            }

            List<Object[]> positionsResultSet = aumRepository.findAUMsForFirm(firmId, previousDate);
            for (Object[] resultSet : positionsResultSet){
                AdvisorAUM advisorAUM = map.get(((BigInteger) resultSet[0]).longValue());
                AumDiff aumDiff = advisorAUM.getPrevious();
                Map<String, BigDecimal> assetClass = aumDiff.getAssetClass();
                assetClass.put(resultSet[1].toString(), assetClass.get(resultSet[1].toString()).add((BigDecimal) resultSet[2]));
                aumDiff.setTotal(aumDiff.getTotal().add((BigDecimal) resultSet[2]));
                aumDiff.setAssetClass(assetClass);
                advisorAUM.setPrevious(aumDiff);
            }

            List<Object[]> positionsResultSet2 = aumRepository.findAUMsForFirm(firmId, currentDate);
            for (Object[] resultSet : positionsResultSet2){
                AdvisorAUM advisorAUM = map.get(((BigInteger) resultSet[0]).longValue());
                AumDiff aumDiff = advisorAUM.getCurrent();
                Map<String, BigDecimal> assetClass = aumDiff.getAssetClass();
                assetClass.put(resultSet[1].toString(), assetClass.get(resultSet[1].toString()).add((BigDecimal) resultSet[2]));
                aumDiff.setTotal(aumDiff.getTotal().add((BigDecimal) resultSet[2]));
                aumDiff.setAssetClass(assetClass);
                advisorAUM.setCurrent(aumDiff);
            }

            aumForFirm.setAdvisors(advisorAUMList);
            aumForFirm.setTotalAdvisors(advisorList.size());
            aumForFirm.setHasNext(false);
            aumForFirm.setPage(0);
            aumForFirm.setCount(advisorAUMList.size());
            return new ResponseEntity<>(aumForFirm, HttpStatus.OK);
        } catch (Exception e) {
            log.error(ERROR_IN_GETTING_AUM, e);
            return new ResponseEntity<>(new ApiResponseMessage(ERROR_IN_GETTING_AUM), HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<Object> getAUMForAdvisor(Long advisorId, String previousDate, String currentDate, Integer pageNumber) {

        try {
            if (!inputValidator.validateInputDate(previousDate, currentDate)){
                return new ResponseEntity<>(new ApiResponseMessage(ERROR_DATE_VALIDATION), HttpStatus.BAD_REQUEST);
            }

            if(!inputValidator.validateInputPageNumber(pageNumber)){
                return new ResponseEntity<>(new ApiResponseMessage(ERROR_PAGE_NUMBER_VALIDATION), HttpStatus.BAD_REQUEST);
            }

            if(advisorId == null){
                Client client = clientRepository.findById(authorizationService.getUserId());
                advisorId = client.getAdvisorId();
            }

            Map<Long, ClientAUM> map = new HashMap<>();
            AUMForAdvisor aumForAdvisor = new AUMForAdvisor();
            List<ClientAUM> clientAUMList = new ArrayList<>();
            List<Client> clientList = clientRepository.findByAdvisorIdAndActiveTrueOrderByClientFirstNameAsc(advisorId);
            for (int i=0; i<clientList.size(); i++){

                ClientAUM clientAUM = new ClientAUM();
                clientAUM.setClientId(clientList.get(i).getId());
                clientAUM.setName(clientList.get(i).getClientFirstName() + " " + clientList.get(i).getClientLastName());
                Map<String, BigDecimal> previousAssetClass = new HashMap<>();
                previousAssetClass.put("Cash", BigDecimal.ZERO);
                previousAssetClass.put("US Bond", BigDecimal.ZERO);
                previousAssetClass.put("Other", BigDecimal.ZERO);
                previousAssetClass.put("Non US Stock", BigDecimal.ZERO);
                previousAssetClass.put("US Stock", BigDecimal.ZERO);
                Map<String, BigDecimal> currentAssetClass = new HashMap<>();
                currentAssetClass.put("Cash", BigDecimal.ZERO);
                currentAssetClass.put("US Bond", BigDecimal.ZERO);
                currentAssetClass.put("Other", BigDecimal.ZERO);
                currentAssetClass.put("Non US Stock", BigDecimal.ZERO);
                currentAssetClass.put("US Stock", BigDecimal.ZERO);
                clientAUM.setPrevious(new AumDiff(previousDate, BigDecimal.ZERO, previousAssetClass));
                clientAUM.setCurrent(new AumDiff(currentDate, BigDecimal.ZERO, currentAssetClass));
                clientAUMList.add(clientAUM);
                map.put(clientAUM.getClientId(), clientAUM);
            }

            List<Object[]> positionsResultSet = aumRepository.findAUMsForAdvisor(advisorId, previousDate);
            for (Object[] resultSet : positionsResultSet){
                ClientAUM clientAUM = map.get(((BigInteger) resultSet[0]).longValue());
                AumDiff aumDiff = clientAUM.getPrevious();
                Map<String, BigDecimal> assetClass = aumDiff.getAssetClass();
                assetClass.put(resultSet[1].toString(), assetClass.get(resultSet[1].toString()).add((BigDecimal) resultSet[2]));
                aumDiff.setTotal(aumDiff.getTotal().add((BigDecimal) resultSet[2]));
                aumDiff.setAssetClass(assetClass);
                clientAUM.setPrevious(aumDiff);
            }

            List<Object[]> positionsResultSet2 = aumRepository.findAUMsForAdvisor(advisorId, currentDate);
            for (Object[] resultSet : positionsResultSet2){
                ClientAUM clientAUM = map.get(((BigInteger) resultSet[0]).longValue());
                AumDiff aumDiff = clientAUM.getCurrent();
                Map<String, BigDecimal> assetClass = aumDiff.getAssetClass();
                assetClass.put(resultSet[1].toString(), assetClass.get(resultSet[1].toString()).add((BigDecimal) resultSet[2]));
                aumDiff.setTotal(aumDiff.getTotal().add((BigDecimal) resultSet[2]));
                aumDiff.setAssetClass(assetClass);
                clientAUM.setCurrent(aumDiff);
            }

            aumForAdvisor.setClients(clientAUMList);
            aumForAdvisor.setTotalClients(clientList.size());
            aumForAdvisor.setHasNext(false);
            aumForAdvisor.setPage(pageNumber);
            aumForAdvisor.setCount(clientAUMList.size());
            return new ResponseEntity<>(aumForAdvisor, HttpStatus.OK);
        } catch (Exception e) {
            log.error(ERROR_IN_GETTING_AUM, e);
            return new ResponseEntity<>(new ApiResponseMessage(ERROR_IN_GETTING_AUM), HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<Object> getAUMSummary() {

        try {
            List<AumDiff> aumForSummary = null;
            if (authorizationService.isSuperAdmin()) {
                aumForSummary = getAuthorizedData(null, "SuperAdmin");
            } else if (authorizationService.isAdmin()){
                Client client = clientRepository.findById(authorizationService.getUserId());
                if (client == null) return new ResponseEntity<>("User does not exist", HttpStatus.BAD_REQUEST);
                aumForSummary = getAuthorizedData(client.getFirmId(), "FirmAdmin");
            } else if (authorizationService.isAdvisor()) {
                Client client = clientRepository.findById(authorizationService.getUserId());
                if (client == null) return new ResponseEntity<>("User does not exist", HttpStatus.BAD_REQUEST);
                aumForSummary = getAuthorizedData(client.getAdvisorId(), "Advisor");
            } else {
                new ResponseEntity<>(ACCESS_DENIED, HttpStatus.FORBIDDEN);
            }

            return new ResponseEntity<>(aumForSummary, HttpStatus.OK);
        } catch (Exception e) {

            log.error(ERROR_IN_GETTING_AUM_SUMMARY, e);
            return new ResponseEntity<>(new ApiResponseMessage(ERROR_IN_GETTING_AUM_SUMMARY), HttpStatus.BAD_REQUEST);
        }
    }

    private List<AumDiff> getAuthorizedData (Long userId, String userType) throws Exception{
        List<AumDiff> aumDiffList = new ArrayList<>();
        List<String> dateList = dateUtility.getQuarterFirstDates();
        for (int i=0; i<dateList.size(); i++){

            List<Object[]> aumSummaryResultSet = null;
            switch (userType){
                case "SuperAdmin":
                    aumSummaryResultSet = aumRepository.findAUMsSummary(dateList.get(i));
                    break;
                case "FirmAdmin":
                    aumSummaryResultSet = aumRepository.findAUMsSummaryForFirm(userId, dateList.get(i));
                    break;
                case "Advisor":
                    aumSummaryResultSet = aumRepository.findAUMsSummaryForAdvisor(userId, dateList.get(i));
                    break;

                default:
                    break;
            }

            AumDiff aumDiff = new AumDiff();
            aumDiff.setDate(dateList.get(i));
            aumDiff.setTotal(new BigDecimal(0));
            Map<String, BigDecimal> assetClass = new HashMap<>();
            assetClass.put("Cash", BigDecimal.ZERO);
            assetClass.put("US Bond", BigDecimal.ZERO);
            assetClass.put("Other", BigDecimal.ZERO);
            assetClass.put("Non US Stock", BigDecimal.ZERO);
            assetClass.put("US Stock", BigDecimal.ZERO);

            for (Object[] resultSet : aumSummaryResultSet){
                assetClass.put(resultSet[0].toString(), assetClass.get(resultSet[0].toString()).add((BigDecimal) resultSet[1]));
                aumDiff.setTotal(aumDiff.getTotal().add((BigDecimal) resultSet[1]));
            }
            aumDiff.setAssetClass(assetClass);
            aumDiffList.add(aumDiff);
        }

        return aumDiffList;
    }

}


