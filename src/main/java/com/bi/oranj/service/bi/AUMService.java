package com.bi.oranj.service.bi;

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

    public RestResponse getAUMForAdmin(Integer pageNumber, String previousDate, String currentDate) {

        boolean validDate = validateInputDate(previousDate, currentDate);
        if (validDate == false) {
            return RestResponse.error(DATE_VALIDATION_ERROR);
        }

        boolean validPageNumber = validateInputPageNumber(pageNumber);
        if(validPageNumber == false){
            return RestResponse.error(PAGE_NUMBER_VALIDATION_ERROR);
        }

        try {
            AUMForAdmin aumForAdmin = new AUMForAdmin();
            List<FirmAUM> firmAUMList = new ArrayList<>();
            Page<Firm> firmList = firmRepository.findByActiveTrue(new PageRequest(pageNumber, 100, Sort.Direction.ASC, "firmName"));
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
            aumForAdmin.setHasNext(firmList.hasNext());
            aumForAdmin.setPage(pageNumber);
            aumForAdmin.setCount(firmAUMList.size());
            return RestResponse.successWithoutMessage(aumForAdmin);
        } catch (Exception e) {
            log.error("Error in fecthing AUMs" + e);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return RestResponse.error("Error in fetching AUMs");
        }
    }

    public RestResponse getAUMForFirm(Long firmId, String previousDate, String currentDate, Integer pageNumber) {

        boolean validDate = validateInputDate(previousDate, currentDate);
        if (validDate == false) {
            return RestResponse.error(DATE_VALIDATION_ERROR);
        }

        boolean validPageNumber = validateInputPageNumber(pageNumber);
        if(validPageNumber == false){
            return RestResponse.error(PAGE_NUMBER_VALIDATION_ERROR);
        }

        try {
            AUMForFirm aumForFirm = new AUMForFirm();
            List<AdvisorAUM> advisorAUMList = new ArrayList<>();
            Page<Advisor> advisorList = advisorRepository.findByFirmIdAndActiveTrue(firmId, new PageRequest(pageNumber, 100, Sort.Direction.ASC, "advisorFirstName"));
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
            aumForFirm.setHasNext(advisorList.hasNext());
            aumForFirm.setPage(0);
            aumForFirm.setCount(advisorAUMList.size());
            return RestResponse.successWithoutMessage(aumForFirm);
        } catch (Exception e) {
            log.error("Error in fecthing AUMs" + e);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return RestResponse.error("Error in fetching AUMs");
        }
    }

    public RestResponse getAUMForAdvisor(Long advisorId, String previousDate, String currentDate, Integer pageNumber) {

        boolean validDate = validateInputDate(previousDate, currentDate);
        if (validDate == false){
            return RestResponse.error(DATE_VALIDATION_ERROR);
        }

        boolean validPageNumber = validateInputPageNumber(pageNumber);
        if(validPageNumber == false){
            return RestResponse.error(PAGE_NUMBER_VALIDATION_ERROR);
        }

        try {
            AUMForAdvisor aumForAdvisor = new AUMForAdvisor();
            List<ClientAUM> clientAUMList = new ArrayList<>();
            Page<Client> clientList = clientRepository.findByAdvisorIdAndActiveTrue(advisorId, new PageRequest(pageNumber, 100, Sort.Direction.ASC, "clientFirstName"));
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
            aumForAdvisor.setHasNext(clientList.hasNext());
            aumForAdvisor.setPage(pageNumber);
            aumForAdvisor.setCount(clientAUMList.size());
            return RestResponse.successWithoutMessage(aumForAdvisor);
        } catch (Exception e) {
            log.error("Error in fecthing AUMs" + e);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return RestResponse.error("Error in fetching AUMs");
        }
    }

    public AumDiff getAUM(Long id, String date, String aumFor){

        AumDiff aumDiff = new AumDiff();
        aumDiff.setDate(date);
        Map<String, BigDecimal> assetClass = new HashMap<>();
        aumDiff.setTotal(new BigDecimal(0));
        List<Object[]> positionsResultSet = null;

        switch (aumFor){
            case "client":
                positionsResultSet = aumRepository.findAUMsForClient(id, date);
                break;
            case "advisor":
                positionsResultSet = aumRepository.findAUMsForAdvisor(id, date);
                break;
            case "firm":
                positionsResultSet = aumRepository.findAUMsForFirm(id, date);
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

    public RestResponse getAUMSummary() {

        try {
            List<AumDiff> aumDiffList = new ArrayList<AumDiff>();
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
            log.error("Error in fecthing AUMs" + e);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return RestResponse.error("Error in fetching AUMs");
        }
    }

    public boolean validateInputDate(String previousDate, String currentDate){

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date previous = sdf.parse(previousDate);
            Date current = sdf.parse(currentDate);
        } catch (Exception e){
            log.error(DATE_VALIDATION_ERROR);
            return false;
        }
        return true;
    }

    public boolean validateInputPageNumber(Integer pageNumber) {

        if(pageNumber >= 0){
            return true;
        }else {
            return false;
        }
    }

    public List<String> getQuarterFirstDates(){

        List<String> dateList = new ArrayList<>();
        long totalNumberOfQuarters = IsoFields.QUARTER_YEARS.between(
                        LocalDate.of(START_YEAR, START_MONTH, START_DAY),
                        LocalDate.of(Calendar.getInstance().get(Calendar.YEAR),
                                     (Calendar.getInstance().get(Calendar.MONTH)+1),
                                      Calendar.getInstance().get(Calendar.DAY_OF_MONTH)))+1;
        log.info("totalNumberOfQuarters ::: " + totalNumberOfQuarters);

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


