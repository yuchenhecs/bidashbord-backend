package com.bi.oranj.service.oranj;

import com.bi.oranj.constant.Constants;
import com.bi.oranj.controller.bi.resp.RestResponse;
import com.bi.oranj.model.bi.*;
import com.bi.oranj.model.oranj.OranjClient;
import com.bi.oranj.model.oranj.OranjGoal;
import com.bi.oranj.model.oranj.OranjPositions;
import com.bi.oranj.repository.bi.*;
import com.bi.oranj.repository.oranj.*;
import com.bi.oranj.utils.DateValidator;
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
import java.sql.Timestamp;
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

    @Autowired
    DateValidator dateValidator;

//    public void fetchAUMData(){
//        List<Object[]> mapping = oranjAUMRepository.fetchPortfolioClientMapping();
//        List<Aum> aums = new ArrayList<>();
//
//        try{
//            for (Object[] o : mapping){
//                Client client = entityManager.find(Client.class, ((BigInteger) o[1]).longValue());
//                if (client == null) {
//                    List<OranjClient> clients =oranjClientRepository.findByClientId((BigInteger) o[1]);
//                    if (clients.size() != 0) saveClients(clients);
//                    else continue;
//                }
//
//                Aum aum = new Aum();
//                aum.setPortfolioId((BigInteger) o[0]);
//                aum.setClientId((BigInteger) o[1]);
//                aums.add(aum);
//            }
//            aumRepository.save(aums);
//        }catch (Exception ex){
//            ex.printStackTrace();
//        }
//    }

    public void fetchPositionsData () {

        List<Object[]> positionRows = oranjPositionsRepository.fetchPositionsData();
        DateFormat dateFormat1 = new SimpleDateFormat("yyy-MM-dd HH:mm:ss", Locale.US);

        savePositions(positionRows, dateFormat1);
    }

    public void fetchPositionsHistory (long limitNum){
        DateFormat dateFormat1 = new SimpleDateFormat("yyy-MM-dd HH:mm:ss", Locale.US);
        List<Object[]> history = null;

        if (limitNum == 0) history = oranjPositionsHistoryRepository.fetchPositionsHistory();
        else history = oranjPositionsHistoryRepository.fetchPositionsHistoryWithLimit(limitNum);
        savePositions(history, dateFormat1);
    }

    public void fetchPositionsDataByDate(String date) {
        if (!dateValidator.validate(date)) return;

        List<OranjPositions> positions = oranjPositionsHistoryRepository.fetchPositionsHistoryByDate(date);
        if (positions.size() == 0) log.info("Empty data set for the given data -> {}", date);

    }

    private void savePositions (List<Object[]> history, DateFormat dateFormat1){

        List<Position> posHis = new ArrayList<>();
        try {
            for (Object[] o : history) {

                String assetClass =  (String) o[4];
                if (assetClass == null) assetClass = getRandomAssetClass();

                String date = "";
                if (o[8].getClass() == Timestamp.class) date = ((Timestamp) o[8]).toString();
                else date = ((Date) o[8]).toString();

                if (date.length() <= 10) date += " 00:00:00";

                Position positionHistory = new Position();
                positionHistory.setPositionId((BigInteger) o[0]);
                positionHistory.setPortfolioId((BigInteger) o[1]);
                positionHistory.setClientId((BigInteger) o[2]);
                positionHistory.setTickerName((String) o[3]);
                positionHistory.setAssetClass(assetClass);
                positionHistory.setPrice((BigDecimal) o[5]);
                positionHistory.setQuantity((Double) o[6]);
                positionHistory.setAmount((BigDecimal) o[7]);
                positionHistory.setCurrencyCode("USD");
                positionHistory.setCreationDate(dateFormat1.parse(date));
                positionHistory.setUpdatedOn(dateFormat1.parse((o[9]).toString()));
                posHis.add(positionHistory);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        positionRepository.save(posHis);
    }

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

    private String getRandomAssetClass (){
        List<String> assetClasses = new ArrayList<>();
        assetClasses.add("Cash");
        assetClasses.add("US Stock");
        assetClasses.add("Non US Stock");
        assetClasses.add("US Bond");
        assetClasses.add("Other");

        return assetClasses.get(new Random().nextInt(assetClasses.size()));
    }
}
