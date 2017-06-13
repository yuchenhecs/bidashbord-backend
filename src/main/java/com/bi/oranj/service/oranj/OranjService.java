package com.bi.oranj.service.oranj;

import com.bi.oranj.constant.Constants;
import com.bi.oranj.controller.bi.resp.RestResponse;
import com.bi.oranj.model.bi.*;
import com.bi.oranj.model.bi.Aum;
import com.bi.oranj.model.oranj.OranjClient;
import com.bi.oranj.model.oranj.OranjGoal;
import com.bi.oranj.repository.bi.*;
import com.bi.oranj.repository.oranj.*;
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
import java.util.*;

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
    OranjPositionsHistoryRepository oranjPositionsHistoryRepository;

    @Autowired
    @Qualifier ("biEntityManager")
    EntityManager entityManager;

    @Autowired
    OranjClientRepository oranjClientRepository;

    @Autowired
    OranjPositionsRepository oranjPositionsRepository;


    public void fetchAUMData(){
        List<Object[]> mapping = oranjAUMRepository.fetchPortfolioClientMapping();
        List<Aum> aums = new ArrayList<>();

        for (Object[] o : mapping){

            Client client = entityManager.find(Client.class, ((BigInteger) o[1]).longValue());
            if (client == null){
                saveClients(oranjClientRepository.findByClientId((BigInteger) o[1]));
            }

            Aum aum = new Aum();
            aum.setPortfolioId((BigInteger) o[0]);
            aum.setClientId((BigInteger) o[1]);
            aums.add(aum);
        }

        aumRepository.save(aums);

    }


    public void fetchPositionData () {

        List<Object[]> positionRows = oranjPositionsRepository.fetchPositionsData();
        DateFormat dateFormat1 = new SimpleDateFormat("yyy-MM-dd HH:mm:ss", Locale.US);

        List<Position> positions = new ArrayList<>();

        try{
            for (Object[] o : positionRows){

                Client client = entityManager.find(Client.class, ((BigInteger) o[2]).longValue());
                if (client == null){
                    saveClients(oranjClientRepository.findByClientId((BigInteger) o[2]));
                }

                Position position = new Position();
                position.setPositionId((BigInteger) o[0]);
                position.setPortfolioId((BigInteger) o[1]);
                position.setTickerName((String) o[2]);
                position.setAssetClass((String) o[3]);
                position.setPrice((BigDecimal) o[4]);
                position.setQuantity((Double) o[5]);
                position.setAmount((BigDecimal) o[6]);
                position.setCurrencyCode("USD");
                position.setCreationDate((Date) o[7]);
                position.setUpdatedOn(dateFormat1.parse((o[8]).toString()));
                positions.add(position);

            }
        } catch (Exception ex){
            ex.printStackTrace();
        }

        positionRepository.save(positions);
    }

//    public void fetchAUMHistory (long limitNum)  {
//        DateFormat dateFormat1 = new SimpleDateFormat("yyy-MM-dd HH:mm:ss", Locale.US);
//        List<Object[]> history = null;
//
//        if (limitNum == 0) history = oranjAumHistoryRepository.fetchAumHistory();
//        else history = oranjAumHistoryRepository.fetchAumHistoryWithLimit(limitNum);
//        saveAumHistory(history, dateFormat1);
//    }

    public void fetchPositionsHistory (long limitNum){
        DateFormat dateFormat1 = new SimpleDateFormat("yyy-MM-dd HH:mm:ss", Locale.US);
        List<Object[]> history = null;

        if (limitNum == 0) history = oranjPositionsHistoryRepository.fetchPositionsHistory();
        else history = oranjPositionsHistoryRepository.fetchPositionsHistoryWithLimit(limitNum);
        savePositionsHistory(history, dateFormat1);
    }

    private void savePositionsHistory (List<Object[]> history, DateFormat dateFormat1){
        List<Position> posHis = new ArrayList<>();
        try {
            for (Object[] o : history) {

                Client client = entityManager.find(Client.class, ((BigInteger) o[2]).longValue());
                if (client == null) saveClients(oranjClientRepository.findByClientId((BigInteger) o[2]));

                Position positionHistory = new Position();
                positionHistory.setPositionId((BigInteger) o[0]);
                positionHistory.setPortfolioId((BigInteger) o[1]);
                positionHistory.setTickerName((String) o[2]);
                positionHistory.setAssetClass((String) o[3]);
                positionHistory.setPrice((BigDecimal) o[4]);
                positionHistory.setQuantity((Double) o[5]);
                positionHistory.setAmount((BigDecimal) o[6]);
                positionHistory.setCurrencyCode("USD");
                positionHistory.setCreationDate(dateFormat1.parse((o[7]).toString()));
                positionHistory.setUpdatedOn(dateFormat1.parse((o[8]).toString()));
                posHis.add(positionHistory);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        positionRepository.save(posHis);
    }

//    private void saveAumHistory (List<Object[]> history, DateFormat dateFormat1){
//        List<Aum> aumHis = new ArrayList<>();
//        try {
//            for (Object[] o : history) {
//                Client client = entityManager.find(Client.class, ((BigInteger) o[2]).longValue());
//                if (client == null) saveClients(oranjClientRepository.findByClientId((BigInteger) o[2]));
//
//                Aum aum = new Aum();
//                aum.setPortfolioId((BigInteger) o[1]);
//                aum.setClientId((BigInteger) o[2]);
//                aum.setIsInactive((Boolean) o[4]);
//                aum.setAccountId((BigInteger) o[0]);
//                aum.setUpdatedOn(dateFormat1.parse((o[5]).toString()));
//                aum.setAmount((BigDecimal) o[3]);
//                aumHis.add(aum);
//            }
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        aumRepository.save(aumHis);
//    }

    private void saveClients (List<OranjClient> clients){
        for (OranjClient oranjClient : clients) {

            Firm firm = new Firm();
            firm.setId(oranjClient.getFirmId());
            firm.setFirmName(oranjClient.getFirmName());
            firmRepository.save(firm);

            Advisor advisor = new Advisor();
            advisor.setId(oranjClient.getAdvisorId());
            advisor.setAdvisorFirstName(oranjClient.getAdvisorFirstName());
            advisor.setAdvisorLastName(oranjClient.getAdvisorLastName());
            advisor.setFirmId(oranjClient.getFirmId());
            advisorRepository.save(advisor);

            Client client = new Client();
            client.setId(oranjClient.getId());
            client.setClientFirstName(oranjClient.getClientFirstName());
            client.setClientLastName(oranjClient.getClientLastName());
            client.setAdvisorId(oranjClient.getAdvisorId());
            client.setFirmId(oranjClient.getFirmId());
            clientRepository.save(client);

        }
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
