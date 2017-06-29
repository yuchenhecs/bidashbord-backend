package com.bi.oranj.service.bi;

import com.bi.oranj.controller.bi.resp.RestResponse;
import com.bi.oranj.utils.InputValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;

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

        return null;
    }
}
