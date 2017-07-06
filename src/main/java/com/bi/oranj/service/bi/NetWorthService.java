package com.bi.oranj.service.bi;

import com.bi.oranj.controller.bi.resp.RestResponse;
import com.bi.oranj.model.bi.*;
import com.bi.oranj.repository.bi.AdvisorRepository;
import com.bi.oranj.repository.bi.ClientRepository;
import com.bi.oranj.repository.bi.FirmRepository;
import com.bi.oranj.repository.bi.NetWorthRepository;
import com.bi.oranj.scheduler.ScheduledTasks;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.bi.oranj.constant.Constants.ERROR_IN_GETTING_NET_WORTH;
import static com.bi.oranj.constant.Constants.YEAR_MONTH_DAY_FORMAT;

/**
 * Created by robertyuan on 6/21/17.
 */
@Service
public class NetWorthService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final SimpleDateFormat dateFormat = new SimpleDateFormat(YEAR_MONTH_DAY_FORMAT);
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

    public RestResponse getNetWorthForAdmin(Integer pageNumber) {
        try {
            NetWorthAdmin netWorthAdmin = new NetWorthAdmin();
            List<NetWorthForAdmin> networthList = new ArrayList<>();
            Page<Firm> firmList = firmRepository.findByActiveTrue(new PageRequest(pageNumber, 500, Sort.Direction.ASC, "firmName"));
            for (int i = 0; i < firmList.getContent().size(); i++) {
                NetWorthForAdmin netWorthForAdmin = new NetWorthForAdmin();
                netWorthForAdmin.setFirmId(firmList.getContent().get(i).getId());
                netWorthForAdmin.setName(firmList.getContent().get(i).getFirmName());
                List<Object[]> netWorthCalc = networthRepository.findNetWorthForAdmin(firmList.getContent().get(i).getId());
                log.info("netWorthCalc.length::: ", netWorthCalc);
                for (Object[] resultSet : netWorthCalc) {
                    log.info("resultSet.length:: ", resultSet.length);
                    if (resultSet[1] != null) {
                        netWorthForAdmin.setAbsNet((BigDecimal) resultSet[1]);
                        BigDecimal count = new BigDecimal((BigInteger) resultSet[0]);
                        netWorthForAdmin.setAvgNet(netWorthForAdmin.getAbsNet().divide(count, 2, RoundingMode.HALF_UP));
                    }
                }
                networthList.add(netWorthForAdmin);
            }
            netWorthAdmin.setHasNext(firmList.hasNext());
            netWorthAdmin.setPage(pageNumber);
            netWorthAdmin.setFirms(networthList);
            return RestResponse.successWithoutMessage(netWorthAdmin);
        } catch (Exception e) {
            log.error(ERROR_IN_GETTING_NET_WORTH, e);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return RestResponse.error(ERROR_IN_GETTING_NET_WORTH);
        }
    }

    public RestResponse getNetWorthForFirm(Long firmId, Integer pageNumber) {
        try {
            NetWorthFirm netWorthFirm = new NetWorthFirm();
            List<NetWorthForFirm> networthList = new ArrayList<>();
            Page<Advisor> advisorList = advisorRepository.findByFirmIdAndActiveTrue(firmId, new PageRequest(pageNumber, 500, Sort.Direction.ASC, "advisorFirstName"));
            for (int i = 0; i < advisorList.getContent().size(); i++) {
                NetWorthForFirm netWorthForFirm = new NetWorthForFirm();
                netWorthForFirm.setAdvisorId(advisorList.getContent().get(i).getId());
                netWorthForFirm.setFirstName(advisorList.getContent().get(i).getAdvisorFirstName());
                netWorthForFirm.setLastName(advisorList.getContent().get(i).getAdvisorLastName());
                List<Object[]> netWorthCalc = networthRepository.findNetWorthForFirm(advisorList.getContent().get(i).getId());
                for (Object[] resultSet : netWorthCalc) {
                    if (resultSet[1] != null) {
                        netWorthForFirm.setAbsNet((BigDecimal) resultSet[1]);
                        BigDecimal count = new BigDecimal((BigInteger) resultSet[0]);
                        netWorthForFirm.setAvgNet(netWorthForFirm.getAbsNet().divide(count, 2, RoundingMode.HALF_UP));
                    }
                }
                networthList.add(netWorthForFirm);
            }
            netWorthFirm.setHasNext(advisorList.hasNext());
            netWorthFirm.setPage(pageNumber);
            netWorthFirm.setAdvisors(networthList);
            return RestResponse.successWithoutMessage(netWorthFirm);
        } catch (Exception e) {
            log.error(ERROR_IN_GETTING_NET_WORTH, e);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return RestResponse.error(ERROR_IN_GETTING_NET_WORTH);
        }
    }

    public RestResponse getNetWorthForAdvisor(Long advisorId, Integer pageNumber) {
        try {
            NetWorthAdvisor netWorthAdvisor = new NetWorthAdvisor();
            List<NetWorthForAdvisor> networthList = new ArrayList<>();
            Page<Client> clientList = clientRepository.findByAdvisorIdAndActiveTrue(advisorId, new PageRequest(pageNumber, 500, Sort.Direction.ASC, "clientFirstName"));
            for (int i = 0; i < clientList.getContent().size(); i++) {
                NetWorthForAdvisor netWorthForAdvisor = new NetWorthForAdvisor();
                netWorthForAdvisor.setClientId(clientList.getContent().get(i).getId());
                netWorthForAdvisor.setFirstName(clientList.getContent().get(i).getClientFirstName());
                netWorthForAdvisor.setLastName(clientList.getContent().get(i).getClientLastName());
                String yesterday = dateFormat.format(scheduledTasks.yesterday());
                BigDecimal clientAbsNet = networthRepository.findNetWorthForAdvisor(clientList.getContent().get(i).getId(), yesterday);
                netWorthForAdvisor.setAbsNet(clientAbsNet);
                networthList.add(netWorthForAdvisor);
            }
            List<Object[]> advisorCalc = networthRepository.findNetWorthForFirm(advisorId);
            for (Object[] resultSet : advisorCalc) {
                if (resultSet[1] != null) {
                    BigDecimal denominator = new BigDecimal((BigInteger) resultSet[0]);
                    netWorthAdvisor.setAvgAdvisor(((BigDecimal) resultSet[1]).divide(denominator, 2, RoundingMode.HALF_UP));
                }
            }


            List<Object[]> firmCalc = networthRepository.findNetWorthForAdmin(clientList.getContent().get(0).getFirmId());
            for (Object[] resultSet : firmCalc) {
                if (resultSet[1] != null) {
                    BigDecimal denominator = new BigDecimal((BigInteger) resultSet[0]);
                    netWorthAdvisor.setAvgFirm(((BigDecimal) resultSet[1]).divide(denominator, 2, RoundingMode.HALF_UP));
                }
            }

            netWorthAdvisor.setHasNext(clientList.hasNext());
            netWorthAdvisor.setPage(pageNumber);
            netWorthAdvisor.setClients(networthList);
            return RestResponse.successWithoutMessage(netWorthAdvisor);
        } catch (Exception e) {
            log.error(ERROR_IN_GETTING_NET_WORTH, e);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return RestResponse.error(ERROR_IN_GETTING_NET_WORTH);
        }
    }

    public RestResponse getNetWorthSummary() {
        try {
            NetWorthSummary netWorthSummary = new NetWorthSummary();
            List<NetWorthForSummary> networthList = new ArrayList<>();
            List<String> monthList = getDateList();
            BigDecimal numClientsBefore = BigDecimal.valueOf(0);

            for(int i=0; i<monthList.size();i++) {
                NetWorthForSummary netWorthForSummary = new NetWorthForSummary();
                netWorthForSummary.setDate(monthList.get(i));
                List<Object[]> monthData = networthRepository.findNetWorthForSummary(monthList.get(i));
                for (Object[] resultSet : monthData) {
                    if (i == 0) {
                        netWorthForSummary.setClientsDiff(numClientsBefore);
                        numClientsBefore = new BigDecimal((BigInteger) resultSet[0]);
                        netWorthForSummary.setAbsNet((BigDecimal) resultSet[1]);
                        networthList.add(netWorthForSummary);
                    } else {
                        BigDecimal numClientsNow = new BigDecimal((BigInteger) resultSet[0]);
                        netWorthForSummary.setClientsDiff(numClientsNow.subtract(numClientsBefore));
                        numClientsBefore = numClientsNow;
                        netWorthForSummary.setAbsNet((BigDecimal) resultSet[1]);
                        networthList.add(netWorthForSummary);
                    }
                }
            }

            netWorthSummary.setSummary(networthList);

            return RestResponse.successWithoutMessage(netWorthSummary);

        } catch (Exception e) {
            log.error(ERROR_IN_GETTING_NET_WORTH, e);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return RestResponse.error(ERROR_IN_GETTING_NET_WORTH);
        }
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
        monthList.add(currentDate.toString());

        return monthList;
    }
}
