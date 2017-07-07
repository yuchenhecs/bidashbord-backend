package com.bi.oranj.service.bi;

import com.bi.oranj.controller.bi.resp.RestResponse;
import com.bi.oranj.model.bi.*;
import com.bi.oranj.repository.bi.AdvisorRepository;
import com.bi.oranj.repository.bi.AumRepository;
import com.bi.oranj.repository.bi.ClientRepository;
import com.bi.oranj.repository.bi.FirmRepository;
import com.bi.oranj.utils.InputValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.temporal.IsoFields;
import java.util.*;

import static com.bi.oranj.constant.Constants.*;
import static java.time.temporal.TemporalAdjusters.firstDayOfYear;

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

    @Autowired
    private InputValidator inputValidator;

    public RestResponse getAUMForAdmin(Integer pageNumber, String previousDate, String currentDate) {

        if (!inputValidator.validateInputDate(previousDate, currentDate)) {
            return RestResponse.error(ERROR_DATE_VALIDATION);
        }

        if(!inputValidator.validateInputPageNumber(pageNumber)){
            return RestResponse.error(ERROR_PAGE_NUMBER_VALIDATION);
        }

        try {
            Map<Long, FirmAUM> map = new HashMap<>();
            AUMForAdmin aumForAdmin = new AUMForAdmin();
            List<FirmAUM> firmAUMList = new ArrayList<>();
            List<Firm> firmList = firmRepository.findByActiveTrueOrderByFirmNameAsc();
            for (int i=0; i<firmList.size(); i++){

                FirmAUM firmAUM = new FirmAUM();
                firmAUM.setFirmId(firmList.get(i).getId());
                firmAUM.setName(firmList.get(i).getFirmName());
                firmAUM.setPrevious(new AumDiff(previousDate, BigDecimal.ZERO, new HashMap<String, BigDecimal>()));
                firmAUM.setCurrent(new AumDiff(currentDate, BigDecimal.ZERO, new HashMap<String, BigDecimal>()));
                firmAUMList.add(firmAUM);
                map.put(firmAUM.getFirmId(), firmAUM);
            }

            List<Object[]> positionsResultSet = aumRepository.findAUMsForAdmin(previousDate);
            for (Object[] resultSet : positionsResultSet){
                FirmAUM firmAUM = map.get(((BigInteger) resultSet[0]).longValue());
                AumDiff aumDiff = firmAUM.getPrevious();
                Map<String, BigDecimal> assetClass = aumDiff.getAssetClass();
                assetClass.put(resultSet[1].toString(), (BigDecimal) resultSet[2]);
                aumDiff.setTotal(aumDiff.getTotal().add((BigDecimal) resultSet[2]));
                aumDiff.setAssetClass(assetClass);
                firmAUM.setPrevious(aumDiff);
            }

            List<Object[]> positionsResultSet2 = aumRepository.findAUMsForAdmin(currentDate);
            for (Object[] resultSet : positionsResultSet2){
                FirmAUM firmAUM = map.get(((BigInteger) resultSet[0]).longValue());
                AumDiff aumDiff = firmAUM.getCurrent();
                Map<String, BigDecimal> assetClass = aumDiff.getAssetClass();
                assetClass.put(resultSet[1].toString(), (BigDecimal) resultSet[2]);
                aumDiff.setTotal(aumDiff.getTotal().add((BigDecimal) resultSet[2]));
                aumDiff.setAssetClass(assetClass);
                firmAUM.setCurrent(aumDiff);
            }
            aumForAdmin.setFirms(firmAUMList);
            aumForAdmin.setTotalFirms(firmList.size());
            aumForAdmin.setHasNext(false);
            aumForAdmin.setPage(pageNumber);
            aumForAdmin.setCount(firmAUMList.size());
            return RestResponse.successWithoutMessage(aumForAdmin);
        } catch (Exception e) {
            log.error(ERROR_IN_GETTING_AUM + e);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return RestResponse.error(ERROR_IN_GETTING_AUM);
        }
    }

    public RestResponse getAUMForFirm(Long firmId, String previousDate, String currentDate, Integer pageNumber) {

        if (!inputValidator.validateInputDate(previousDate, currentDate)) {
            return RestResponse.error(ERROR_DATE_VALIDATION);
        }

        if(!inputValidator.validateInputPageNumber(pageNumber)){
            return RestResponse.error(ERROR_PAGE_NUMBER_VALIDATION);
        }

        try {
            Map<Long, AdvisorAUM> map = new HashMap<>();
            AUMForFirm aumForFirm = new AUMForFirm();
            List<AdvisorAUM> advisorAUMList = new ArrayList<>();
            List<Advisor> advisorList = advisorRepository.findByFirmIdAndActiveTrueOrderByAdvisorFirstNameAsc(firmId);
            for (int i=0; i<advisorList.size(); i++){

                AdvisorAUM advisorAUM = new AdvisorAUM();
                advisorAUM.setAdvisorId(advisorList.get(i).getId());
                advisorAUM.setName(advisorList.get(i).getAdvisorFirstName() + " " + advisorList.get(i).getAdvisorLastName());
                advisorAUM.setPrevious(new AumDiff(previousDate, BigDecimal.ZERO, new HashMap<String, BigDecimal>()));
                advisorAUM.setCurrent(new AumDiff(currentDate, BigDecimal.ZERO, new HashMap<String, BigDecimal>()));
                advisorAUMList.add(advisorAUM);
                map.put(advisorAUM.getAdvisorId(), advisorAUM);
            }

            List<Object[]> positionsResultSet = aumRepository.findAUMsForFirm(firmId, previousDate);
            for (Object[] resultSet : positionsResultSet){
                AdvisorAUM advisorAUM = map.get(((BigInteger) resultSet[0]).longValue());
                AumDiff aumDiff = advisorAUM.getPrevious();
                Map<String, BigDecimal> assetClass = aumDiff.getAssetClass();
                assetClass.put(resultSet[1].toString(), (BigDecimal) resultSet[2]);
                aumDiff.setTotal(aumDiff.getTotal().add((BigDecimal) resultSet[2]));
                aumDiff.setAssetClass(assetClass);
                advisorAUM.setPrevious(aumDiff);
            }

            List<Object[]> positionsResultSet2 = aumRepository.findAUMsForFirm(firmId, currentDate);
            for (Object[] resultSet : positionsResultSet2){
                AdvisorAUM advisorAUM = map.get(((BigInteger) resultSet[0]).longValue());
                AumDiff aumDiff = advisorAUM.getCurrent();
                Map<String, BigDecimal> assetClass = aumDiff.getAssetClass();
                assetClass.put(resultSet[1].toString(), (BigDecimal) resultSet[2]);
                aumDiff.setTotal(aumDiff.getTotal().add((BigDecimal) resultSet[2]));
                aumDiff.setAssetClass(assetClass);
                advisorAUM.setCurrent(aumDiff);
            }

            aumForFirm.setAdvisors(advisorAUMList);
            aumForFirm.setTotalAdvisors(advisorList.size());
            aumForFirm.setHasNext(false);
            aumForFirm.setPage(0);
            aumForFirm.setCount(advisorAUMList.size());
            return RestResponse.successWithoutMessage(aumForFirm);
        } catch (Exception e) {
            log.error(ERROR_IN_GETTING_AUM + e);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return RestResponse.error(ERROR_IN_GETTING_AUM);
        }
    }

    public RestResponse getAUMForAdvisor(Long advisorId, String previousDate, String currentDate, Integer pageNumber) {

        if (!inputValidator.validateInputDate(previousDate, currentDate)){
            return RestResponse.error(ERROR_DATE_VALIDATION);
        }

        if(!inputValidator.validateInputPageNumber(pageNumber)){
            return RestResponse.error(ERROR_PAGE_NUMBER_VALIDATION);
        }

        try {
            Map<Long, ClientAUM> map = new HashMap<>();
            AUMForAdvisor aumForAdvisor = new AUMForAdvisor();
            List<ClientAUM> clientAUMList = new ArrayList<>();
            List<Client> clientList = clientRepository.findByAdvisorIdAndActiveTrueOrderByClientFirstNameAsc(advisorId);
            for (int i=0; i<clientList.size(); i++){

                ClientAUM clientAUM = new ClientAUM();
                clientAUM.setClientId(clientList.get(i).getId());
                clientAUM.setName(clientList.get(i).getClientFirstName() + " " + clientList.get(i).getClientLastName());
                clientAUM.setPrevious(new AumDiff(previousDate, BigDecimal.ZERO, new HashMap<String, BigDecimal>()));
                clientAUM.setCurrent(new AumDiff(currentDate, BigDecimal.ZERO, new HashMap<String, BigDecimal>()));
                clientAUMList.add(clientAUM);
                map.put(clientAUM.getClientId(), clientAUM);
            }

            List<Object[]> positionsResultSet = aumRepository.findAUMsForAdvisor(advisorId, previousDate);
            for (Object[] resultSet : positionsResultSet){
                ClientAUM clientAUM = map.get(((BigInteger) resultSet[0]).longValue());
                AumDiff aumDiff = clientAUM.getPrevious();
                Map<String, BigDecimal> assetClass = aumDiff.getAssetClass();
                assetClass.put(resultSet[1].toString(), (BigDecimal) resultSet[2]);
                aumDiff.setTotal(aumDiff.getTotal().add((BigDecimal) resultSet[2]));
                aumDiff.setAssetClass(assetClass);
                clientAUM.setPrevious(aumDiff);
            }

            List<Object[]> positionsResultSet2 = aumRepository.findAUMsForAdvisor(advisorId, currentDate);
            for (Object[] resultSet : positionsResultSet2){
                ClientAUM clientAUM = map.get(((BigInteger) resultSet[0]).longValue());
                AumDiff aumDiff = clientAUM.getCurrent();
                Map<String, BigDecimal> assetClass = aumDiff.getAssetClass();
                assetClass.put(resultSet[1].toString(), (BigDecimal) resultSet[2]);
                aumDiff.setTotal(aumDiff.getTotal().add((BigDecimal) resultSet[2]));
                aumDiff.setAssetClass(assetClass);
                clientAUM.setCurrent(aumDiff);
            }

            aumForAdvisor.setClients(clientAUMList);
            aumForAdvisor.setTotalClients(clientList.size());
            aumForAdvisor.setHasNext(false);
            aumForAdvisor.setPage(pageNumber);
            aumForAdvisor.setCount(clientAUMList.size());
            return RestResponse.successWithoutMessage(aumForAdvisor);
        } catch (Exception e) {
            log.error(ERROR_IN_GETTING_AUM + e);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return RestResponse.error(ERROR_IN_GETTING_AUM);
        }
    }

    public RestResponse getAUMSummary() {

        try {
            List<AumDiff> aumDiffList = new ArrayList<>();
            List<String> dateList = getQuarterFirstDates();
            for (int i=0; i<dateList.size(); i++){

                List<Object[]> aumSummaryResultSet = aumRepository.findAUMsSummary(dateList.get(i));

                AumDiff aumDiff = new AumDiff();
                aumDiff.setDate(dateList.get(i));
                aumDiff.setTotal(new BigDecimal(0));
                Map<String, BigDecimal> assetClass = new HashMap<>();

                for (Object[] resultSet : aumSummaryResultSet){
                    assetClass.put(resultSet[0].toString(), (BigDecimal) resultSet[1]);
                    aumDiff.setTotal(aumDiff.getTotal().add((BigDecimal) resultSet[1]));
                }
                aumDiff.setAssetClass(assetClass);
                aumDiffList.add(aumDiff);
            }
            return RestResponse.successWithoutMessage(aumDiffList);
        } catch (Exception e) {
            log.error(ERROR_IN_GETTING_AUM + e);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return RestResponse.error(ERROR_IN_GETTING_AUM);
        }
    }

    public List<String> getQuarterFirstDates(){

        List<String> dateList = new ArrayList<>();
        long totalNumberOfQuarters = IsoFields.QUARTER_YEARS.between(
                        LocalDate.of(START_YEAR, START_MONTH, START_DAY),
                        LocalDate.of(Calendar.getInstance().get(Calendar.YEAR),
                                     (Calendar.getInstance().get(Calendar.MONTH)+1),
                                      Calendar.getInstance().get(Calendar.DAY_OF_MONTH)))+1;
        log.info("totalNumberOfQuarters ::: {}", totalNumberOfQuarters);

        LocalDate beginningYear = LocalDate.parse(START_YEAR + "-0" + START_MONTH + "-0" + START_DAY);
        LocalDate firstQuarter = beginningYear.with(firstDayOfYear());

        int monthsToAdd=0;
        for(int i=0; i<totalNumberOfQuarters; i++){
            dateList.add(firstQuarter.plusMonths(monthsToAdd).toString());
            monthsToAdd = monthsToAdd + 3;
        }
        dateList.add(LocalDate.now().toString());
        return dateList;
    }
}


