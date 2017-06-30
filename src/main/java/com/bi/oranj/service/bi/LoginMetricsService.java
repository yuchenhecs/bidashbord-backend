package com.bi.oranj.service.bi;

import com.bi.oranj.controller.bi.resp.RestResponse;
import com.bi.oranj.model.bi.AUMForAdmin;
import com.bi.oranj.model.bi.Firm;
import com.bi.oranj.model.bi.FirmAUM;
import com.bi.oranj.utils.InputValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;

import java.util.ArrayList;
import java.util.List;

import static com.bi.oranj.constant.Constants.*;

@Service
public class LoginMetricsService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    HttpServletResponse response;

    @Autowired
    private InputValidator inputValidator;

    public RestResponse getLoginMetricsForAdmin(Integer pageNumber, String user, String range){

        if (!inputValidator.validateInputPageNumber(pageNumber)) {
            return RestResponse.error(ERROR_PAGE_NUMBER_VALIDATION);
        }

        if (!inputValidator.validateInputUserType(user)) {
            return RestResponse.error(ERROR_USER_TYPE_VALIDATION);
        }

        if (!inputValidator.validateInputRangeType(range)) {
            return RestResponse.error(ERROR_RANGE_TYPE_VALIDATION);
        }

        try {
            AUMForAdmin AUMForAdmin = new AUMForAdmin();
            List<FirmAUM> firmAUMList = new ArrayList<>();
            Page<Firm> firmList = firmRepository.findByActiveTrue(new PageRequest(pageNumber, 100, Sort.Direction.ASC, "firmName"));
            for (int i=0; i<firmList.getContent().size(); i++){

                FirmAUM firmAUM = new FirmAUM();
                firmAUM.setFirmId(firmList.getContent().get(i).getId());
                firmAUM.setName(firmList.getContent().get(i).getFirmName());
                firmAUM.setPrevious(getAUM(firmList.getContent().get(i).getId(), previousDate, FIRM));
                firmAUM.setCurrent(getAUM(firmList.getContent().get(i).getId(), currentDate, FIRM));
                firmAUMList.add(firmAUM);
            }
            aumForAdmin.setFirms(firmAUMList);
            aumForAdmin.setTotalFirms(firmList.getTotalElements());
            aumForAdmin.setHasNext(firmList.hasNext());
            aumForAdmin.setPage(pageNumber);
            aumForAdmin.setCount(firmAUMList.size());
            return RestResponse.successWithoutMessage(aumForAdmin);
        } catch (Exception e) {
            log.error(ERROR_IN_GETTING_AUM + e);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return RestResponse.error(ERROR_IN_GETTING_AUM);
        }
    }
}
