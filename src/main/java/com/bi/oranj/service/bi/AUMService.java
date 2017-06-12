package com.bi.oranj.service.bi;

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

    public RestResponse getAUMForAdmin(Integer pageNumber) {

        AUMForAdmin aumForAdmin = new AUMForAdmin();
        List<FirmAUM> firmsList = new ArrayList<>();
        HashMap<BigInteger, FirmAUM> mapOfFirmIdToFirmAum = new HashMap<>();

        try {
            List<Object[]> aumForAdminResultSet = aumRepository.findAUMsForAdmin();
            for (Object[] aumResultSet : aumForAdminResultSet) {


                log.info("Firm ID ::: " + aumResultSet[0]);
                BigInteger readFirmId = (BigInteger) aumResultSet[0];
                if(!mapOfFirmIdToFirmAum.containsKey(readFirmId)){

                    FirmAUM firmAUM = new FirmAUM();
                    firmAUM.setFirmId(readFirmId);
                    firmAUM.setName(aumResultSet[1].toString());

                    // Set Current values
                    AumDiff aumDiffCurrent = new AumDiff();
                    BigDecimal total = (BigDecimal) aumResultSet[3];
                    aumDiffCurrent.setTotal(total);

                    Map<String, BigDecimal> assetTypeMap = new HashMap<String, BigDecimal>();
                    assetTypeMap.put(aumResultSet[2].toString(), (BigDecimal) aumResultSet[3]);
                    aumDiffCurrent.setAssetClass(assetTypeMap);

                    firmAUM.setCurrent(aumDiffCurrent);

                    // Set Source values
                    AumDiff aumDiffSource = new AumDiff();
                    // Fecth data from History table and add
                    firmAUM.setSource(aumDiffSource);

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
