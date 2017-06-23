package com.bi.oranj.service.bi;

import com.bi.oranj.controller.bi.resp.RestResponse;
import com.bi.oranj.model.bi.*;
import com.bi.oranj.repository.bi.AdvisorRepository;
import com.bi.oranj.repository.bi.ClientRepository;
import com.bi.oranj.repository.bi.FirmRepository;
import com.bi.oranj.repository.bi.NetWorthRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Null;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by robertyuan on 6/21/17.
 */
@Service
public class NetWorthService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    HttpServletResponse response;

    @Autowired
    private NetWorthRepository networthRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AdvisorRepository advisorRepository;

    @Autowired
    private FirmRepository firmRepository;

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
            log.error("Error in fecthing AUMs" + e);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return RestResponse.error("Error in fetching AUMs");
        }
    }

    public RestResponse getNetWorthForFirm(Long firmId, Integer pageNumber) {
        try {
            //go through the advisors table and get all advisors with active === 1, then find the clients under those advisors
            //and add up all the client's net worth for one advisor and take the average, repeat this for all the advisors
            NetWorthFirm netWorthFirm = new NetWorthFirm();
            List<NetWorthForFirm> networthList = new ArrayList<>();
            Page<Advisor> advisorList = advisorRepository.findByFirmIdAndActiveTrue(firmId, new PageRequest(pageNumber, 100, Sort.Direction.ASC, "firmName"));
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
            log.error("Error in fecthing AUMs" + e);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return RestResponse.error("Error in fetching AUMs");
        }
    }

}





//added files NetWorth/Admin/Firm/Advisor to replace the files NetWorth/Firms/Advisors/Clients to make it consistent within the networth feature