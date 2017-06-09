package com.bi.oranj.service.oranj;

import com.bi.oranj.constant.Constants;
import com.bi.oranj.controller.bi.resp.RestResponse;
import com.bi.oranj.model.bi.*;
import com.bi.oranj.model.bi.Aum;
import com.bi.oranj.model.oranj.OranjGoal;
import com.bi.oranj.repository.bi.*;
import com.bi.oranj.repository.oranj.OranjAUMRepository;
import com.bi.oranj.repository.oranj.OranjGoalRepository;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by harshavardhanpatil on 5/30/17.
 */
@Service
public class OranjService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    HttpServletResponse response;

    @Autowired
    private OranjGoalRepository oranjGoalRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AdvisorRepository advisorRepository;

    @Autowired
    private FirmRepository firmRepository;

    @Autowired
    private GoalRepository goalRepository;

    @Autowired
    AumRepository aumRepository;

    @Autowired
    PositionRepository positionRepository;


    @Autowired
    OranjAUMRepository oranjAUMRepository;


    @Autowired
    @Qualifier ("biEntityManager")
    EntityManager entityManager;

    /**
     *
     * @return
     * @throws ParseException
     */
    public void fetchAUMData () throws ParseException {

        List<Object[]> aumRows = oranjAUMRepository.fetchAUMData();
        DateFormat dateFormat1 = new SimpleDateFormat("yyy-MM-dd HH:mm:ss", Locale.US);

        for (Object[] o : aumRows){

            Client client = entityManager.find(Client.class, ((BigInteger) o[2]).longValue());
            if (client == null){
                saveResults(oranjGoalRepository.findByClientId((BigInteger) o[2]));
            }

            Aum aum = new Aum();
            aum.setPortfolioId((BigInteger) o[1]);
            aum.setClientId((BigInteger) o[2]);
            aum.setCreationDate((Date) o[8]);
            aum.setUpdatedOn(dateFormat1.parse((o[9]).toString()));
            aum.setIsInactive((Boolean) o[10]);
            aum.setAccountId((BigInteger) o[11]);
            aumRepository.save (aum);

            Position position = new Position();
            position.setPositionId((BigInteger) o[0]);
            position.setPortfolioId((BigInteger) o[1]);
            position.setTickerName((String) o[3]);
            position.setAssetClass((String) o[4]);
            position.setPrice((BigDecimal) o[5]);
            position.setQuantity((Double) o[6]);
            position.setAmount((BigDecimal) o[7]);
            position.setCurrencyCode("USD");
            position.setCreationDate(aum.getCreationDate());
            position.setUpdatedOn(aum.getUpdatedOn());

            positionRepository.save(position);

        }

    }


    public void fetchAUMHistory (){

    }



    /**
     * This method gets all the goals created between startDate and endDate
     * @param date
     * @return
     */
    public RestResponse getGoals(String date){

        String startDate = date + Constants.SPACE + Constants.START_SECOND_OF_THE_DAY;
        String endDate = date + Constants.SPACE + Constants.LAST_SECOND_OF_THE_DAY;
        try{
            saveResults(oranjGoalRepository.FindByCreationDate(startDate, endDate));
        }catch (Exception e){
            log.error("Error in fecthing goals from Oranj." + e);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return RestResponse.error("Error in fecthing Goals from Oranj DB");
        }
        return RestResponse.success(date + " has been saved");
    }

    public void saveResults (List<OranjGoal> oranjGoalList) {

        for (int i = 0; i < oranjGoalList.size(); i++) {

            Firm firm = new Firm();
            firm.setId(oranjGoalList.get(i).getFirmId());
            firm.setFirmName(oranjGoalList.get(i).getFirmName());
            firmRepository.save(firm);

            Advisor advisor = new Advisor();
            advisor.setId(oranjGoalList.get(i).getAdvisorId());
            advisor.setAdvisorFirstName(oranjGoalList.get(i).getAdvisorFirstName());
            advisor.setAdvisorLastName(oranjGoalList.get(i).getAdvisorLastName());
            advisor.setFirmId(oranjGoalList.get(i).getFirmId());
            advisorRepository.save(advisor);

            Client client = new Client();
            client.setId(oranjGoalList.get(i).getUser());
            client.setClientFirstName(oranjGoalList.get(i).getUserLastName());
            client.setClientLastName(oranjGoalList.get(i).getUserLastName());
            client.setAdvisorId(oranjGoalList.get(i).getAdvisorId());
            client.setFirmId(oranjGoalList.get(i).getFirmId());
            clientRepository.save(client);

            BiGoal biGoal = new BiGoal();
            biGoal.setId(oranjGoalList.get(i).getId());
            biGoal.setName(oranjGoalList.get(i).getName());
            biGoal.setType(oranjGoalList.get(i).getType());
            biGoal.setCreationDate(oranjGoalList.get(i).getCreationDate());
            biGoal.setDeleted(oranjGoalList.get(i).isDeleted());
            biGoal.setFirmId(oranjGoalList.get(i).getFirmId());
            biGoal.setAdvisorId(oranjGoalList.get(i).getAdvisorId());
            biGoal.setClientId(oranjGoalList.get(i).getUser());
            goalRepository.save(biGoal);

        }
    }
}
