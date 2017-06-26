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
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Null;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
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

    public RestResponse getNetWorthForAdmin(Integer pageNumber) {
        try {
            //go through the firms table and get all firms with active === 1, then find the clients under those firms
            //and add up all the client's net worth for one firm and take the average, repeat this for all the firms
            NetWorthAdmin netWorthAdmin = new NetWorthAdmin();
            List<NetWorthForAdmin> networthList = new ArrayList<>();
            Page<Firm> firmList = firmRepository.findByActiveTrue(new PageRequest(pageNumber, 100, Sort.Direction.ASC, "firmName"));
            for (int i = 0; i < firmList.getContent().size(); i++) {
                NetWorthForAdmin netWorthForAdmin = new NetWorthForAdmin();
                netWorthForAdmin.setFirmId(firmList.getContent().get(i).getId());
                netWorthForAdmin.setName(firmList.getContent().get(i).getFirmName());
                List<Object[]> netWorthCalc = networthRepository.findNetWorthForAdmin(firmList.getContent().get(i).getId());
                log.info("netWorthCalc.length::: " + netWorthCalc);
                for (Object[] resultSet : netWorthCalc) {
                    log.info("resultSet.length::" + resultSet.length);
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
            log.error("Error in fetching net worth" + e);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return RestResponse.error("Error in fetching net worth");
        }
    }

    public RestResponse getNetWorthForFirm(Long firmId, Integer pageNumber) {
        try {
            //find the appropriate firm using firmId, then for each advisor under that firm, find clients under that advisor
            //then add up all the client's net worth for one advisor  and take the average
            NetWorthFirm netWorthFirm = new NetWorthFirm();
            List<NetWorthForFirm> networthList = new ArrayList<>();
            Page<Advisor> advisorList = advisorRepository.findByFirmIdAndActiveTrue(firmId, new PageRequest(pageNumber, 100, Sort.Direction.ASC, "advisorFirstName"));
            for (int i = 0; i < advisorList.getContent().size(); i++) {
                NetWorthForFirm netWorthForFirm = new NetWorthForFirm();
                netWorthForFirm.setId(advisorList.getContent().get(i).getId());
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
            log.error("Error in fetching net worth" + e);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return RestResponse.error("Error in fetching net worth");
        }
    }

    public RestResponse getNetWorthForAdvisor(Long advisorId, Integer pageNumber) {
        try {
            //go through client table and get all clients with advisorId and active === 1,

            NetWorthAdvisor netWorthAdvisor = new NetWorthAdvisor();
            List<NetWorthForAdvisor> networthList = new ArrayList<>();
            Page<Client> clientList = clientRepository.findByAdvisorIdAndActiveTrue(advisorId, new PageRequest(pageNumber, 100, Sort.Direction.ASC, "clientFirstName"));
            for (int i = 0; i < clientList.getContent().size(); i++) {
                NetWorthForAdvisor netWorthForAdvisor = new NetWorthForAdvisor();
                netWorthForAdvisor.setClientId(clientList.getContent().get(i).getId());
                netWorthForAdvisor.setFirstName(clientList.getContent().get(i).getClientFirstName());
                netWorthForAdvisor.setLastName(clientList.getContent().get(i).getClientLastName());
                String yesterday = dateFormat.format(scheduledTasks.yesterday());
               // String yesterday = "2017-04-01";
                log.info(yesterday);
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
            log.error("Error in fetching net worth" + e);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return RestResponse.error("Error in fetching net worth");
        }
    }

    public RestResponse getNetWorthSummary(Integer PageNumber) {

    }
}

//added files NetWorth/Admin/Firm/Advisor to replace the files NetWorth/Firms/Advisors/Clients to make it consistent within the networth feature