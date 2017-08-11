package com.bi.oranj.service.bi;

import com.bi.oranj.model.bi.*;
import com.bi.oranj.repository.bi.AdvisorRepository;
import com.bi.oranj.repository.bi.ClientRepository;
import com.bi.oranj.repository.bi.FirmRepository;
import com.bi.oranj.repository.bi.NetWorthRepository;
import com.bi.oranj.scheduler.ScheduledTasks;
import com.bi.oranj.utils.ApiResponseMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by robertyuan on 6/21/17.
 */
@Service
public class NetWorthService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    HttpServletResponse response;


    @Autowired
    private NetWorthRepository networthRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AdvisorRepository advisorRepository;

    @Autowired
    private FirmRepository firmRepository;

    @Autowired
    ScheduledTasks scheduledTasks;

    @Autowired
    private AuthorizationService authorizationService;

    @Value("${page.size}")
    private Integer pageSize;


    public ResponseEntity<Object> getNetWorthForAdmin(Integer pageNumber) {
        Integer totalFirms = firmRepository.findDistinctFromFirm();
        Double maxPage = Math.ceil(totalFirms/pageSize);
        if (pageNumber > maxPage) {
            return new ResponseEntity<>(new ApiResponseMessage("Data not found"), HttpStatus.BAD_REQUEST);
        }
        try {
            NetWorthAdmin netWorthAdmin = new NetWorthAdmin();
            List<NetWorthForAdmin> networthList = new ArrayList<>();
            String yesterday = dateFormat.format(scheduledTasks.yesterday());
            List<Object[]> dataList = networthRepository.findNetWorthForAdmin(pageNumber * pageSize, yesterday, pageSize);
            for (Object[] networth : dataList) {
                NetWorthForAdmin netWorthForAdmin = new NetWorthForAdmin();
                netWorthForAdmin.setFirmId(((BigInteger) networth[0]).longValue());
                netWorthForAdmin.setName((String) networth[1]);
                if (networth[2] == null) {
                    netWorthForAdmin.setAbsNet(new BigDecimal(0));
                    netWorthForAdmin.setAvgNet(new BigDecimal(0));
                } else {
                    netWorthForAdmin.setAbsNet((BigDecimal) networth[2]);
                    netWorthForAdmin.setAvgNet((BigDecimal) networth[3]);
                }
                networthList.add(netWorthForAdmin);
            }
            if (pageNumber < maxPage) {
                netWorthAdmin.setHasNext(true);
            } else {
                netWorthAdmin.setHasNext(false);
            }
            netWorthAdmin.setPage(pageNumber);
            netWorthAdmin.setFirms(networthList);
            return new ResponseEntity<>(netWorthAdmin, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error in fetching net worth", e);
            return new ResponseEntity<>(new ApiResponseMessage("Error in fetching net worth"), HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<Object> getNetWorthForFirm(Long firmId, Integer pageNumber) {
        try {
            if (firmId == null){
                Client client = clientRepository.findById(authorizationService.getUserId());
                firmId = client.getFirmId();
            }

            Integer totalAdvisors = advisorRepository.findDistinctByFirm(firmId);
            Double maxPage = Math.ceil(totalAdvisors/pageSize);
            if (pageNumber > maxPage) {
                return new ResponseEntity<>(new ApiResponseMessage("Data not found"), HttpStatus.BAD_REQUEST);
            }

            NetWorthFirm netWorthFirm = new NetWorthFirm();
            List<NetWorthForFirm> networthList = new ArrayList<>();
            String yesterday = dateFormat.format(scheduledTasks.yesterday());
            List<Object[]> dataList = networthRepository.findNetWorthForFirm(firmId, pageNumber * pageSize, yesterday, pageSize);
            for (Object[] networth : dataList) {
                NetWorthForFirm netWorthForFirm = new NetWorthForFirm();
                netWorthForFirm.setAdvisorId(((BigInteger) networth[0]).longValue());
                netWorthForFirm.setName((String) networth[1] + " " + networth[2]);
                if (networth[3] == null) {
                    netWorthForFirm.setAbsNet(new BigDecimal(0));
                    netWorthForFirm.setAvgNet(new BigDecimal(0));
                } else {
                    netWorthForFirm.setAbsNet((BigDecimal) networth[3]);
                    netWorthForFirm.setAvgNet((BigDecimal) networth[4]);
                }
                networthList.add(netWorthForFirm);
            }
            if (pageNumber < maxPage) {
                netWorthFirm.setHasNext(true);
            } else {
                netWorthFirm.setHasNext(false);
            }
            netWorthFirm.setPage(pageNumber);
            netWorthFirm.setAdvisors(networthList);
            return new ResponseEntity<>(netWorthFirm, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error in fetching net worth", e);
            return new ResponseEntity<>(new ApiResponseMessage("Error in fetching net worth"), HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<Object> getNetWorthForAdvisor(Long advisorId, Integer pageNumber) {

        try {
            if (advisorId == null){
                Client client = clientRepository.findById(authorizationService.getUserId());
                advisorId = client.getAdvisorId();
            }

            Integer totalAdvisors = advisorRepository.findDistinctByFirm(advisorId);
            Double maxPage = Math.ceil(totalAdvisors/pageSize);
            if (pageNumber > maxPage) {
                return new ResponseEntity<>(new ApiResponseMessage("Data not found"), HttpStatus.BAD_REQUEST);
            }

            NetWorthAdvisor netWorthAdvisor = new NetWorthAdvisor();
            List<NetWorthForAdvisor> networthList = new ArrayList<>();
            String yesterday = dateFormat.format(scheduledTasks.yesterday());
            List<Object[]> dataList = networthRepository.findNetWorthForAdvisor(advisorId, pageNumber * pageSize, yesterday, pageSize);
            for (Object[] networth : dataList) {
                NetWorthForAdvisor netWorthForAdvisor = new NetWorthForAdvisor();
                netWorthForAdvisor.setClientId(((BigInteger) networth[0]).longValue());
                netWorthForAdvisor.setName((String) networth[1] + " " + networth[2]);
                if (networth[3] == null) {
                    netWorthForAdvisor.setAbsNet(new BigDecimal(0));
                } else {
                    netWorthForAdvisor.setAbsNet((BigDecimal) networth[3]);
                }
                networthList.add(netWorthForAdvisor);
            }
            BigDecimal advisorAvg = networthRepository.findAdvisorAverage(advisorId, yesterday);
            netWorthAdvisor.setAvgAdvisor(advisorAvg);
            BigDecimal firmAvg = networthRepository.findFirmAverage(advisorId, yesterday);
            netWorthAdvisor.setAvgFirm(firmAvg);
            if (pageNumber < maxPage) {
                netWorthAdvisor.setHasNext(true);
            } else {
                netWorthAdvisor.setHasNext(false);
            }
            netWorthAdvisor.setPage(pageNumber);
            netWorthAdvisor.setClients(networthList);
            return new ResponseEntity<>(netWorthAdvisor, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error in fetching net worth", e);
            return new ResponseEntity<>(new ApiResponseMessage("Error in fetching net worth"), HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<Object> getNetWorthSummary() {

        try {
            List<NetWorthForSummary> netWorthSummary = null;
            if (authorizationService.isSuperAdmin()){
                netWorthSummary = getAuthorizedData(null, "SuperAdmin");
            } else if (authorizationService.isAdmin()) {
                Client client = clientRepository.findById(authorizationService.getUserId());
                netWorthSummary = getAuthorizedData(client.getFirmId(), "FirmAdmin");
            } else if (authorizationService.isAdvisor()){
                Client client = clientRepository.findById(authorizationService.getUserId());
                netWorthSummary = getAuthorizedData(client.getAdvisorId(), "Advisor");
            } else {
                return new ResponseEntity<>("FORBIDDEN", HttpStatus.FORBIDDEN);
            }


            return new ResponseEntity<>(netWorthSummary, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error in fetching net worth", e);
            return new ResponseEntity<>(new ApiResponseMessage("Error in fetching net worth"), HttpStatus.BAD_REQUEST);
        }
    }

    private List<NetWorthForSummary> getAuthorizedData(Long userId, String userType) throws Exception {
        List<NetWorthForSummary> networthList = new ArrayList<>();
        List<String> monthList = getDateList();

        for(int i=0; i<monthList.size();i++) {

            List<Object[]> monthData = null;
            switch (userType){
                case "SuperAdmin":
                    monthData = networthRepository.findNetWorthForSummary(monthList.get(i));
                    break;
                case "FirmAdmin":
                    monthData = networthRepository.findNetWorthForFirmSummary(userId, monthList.get(i));
                    break;
                case "Advisor":
                    monthData = networthRepository.findNetWorthForAdvisorSummary(userId, monthList.get(i));
                    break;
                default:
                    break;
            }

            NetWorthForSummary netWorthForSummary = new NetWorthForSummary();
            netWorthForSummary.setDate(monthList.get(i));


            for (Object[] resultSet : monthData) {
                if (i == 0) {
                    netWorthForSummary.setClientsDiff(new BigDecimal((BigInteger) resultSet[0]));
                    netWorthForSummary.setAbsNet((BigDecimal) resultSet[1]);
                    networthList.add(netWorthForSummary);
                } else {
                    netWorthForSummary.setClientsDiff(new BigDecimal((BigInteger) resultSet[0]));
                    netWorthForSummary.setAbsNet((BigDecimal) resultSet[1]);
                    networthList.add(netWorthForSummary);
                }
            }
        }
        return networthList;
    }

    public List<String> getDateList() {
        List<String> monthList = new ArrayList<>();
        LocalDate currentDate = LocalDate.now();
        LocalDate firstDayDate = currentDate.minusMonths(5);
        int dayToSubtract = (currentDate.getDayOfMonth()-1);
        firstDayDate = firstDayDate.minusDays(dayToSubtract);
        for (int i=0; i<5 ;i++) {
            monthList.add(firstDayDate.toString());
            firstDayDate = firstDayDate.plusMonths(1);
        }
        monthList.add(currentDate.minusDays(1).toString());
        return monthList;
    }
}