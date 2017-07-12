package com.bi.oranj.service.oranj;

import com.bi.oranj.controller.bi.resp.RestResponse;
import com.bi.oranj.model.bi.*;
import com.bi.oranj.model.oranj.OranjGoal;
import com.bi.oranj.model.oranj.OranjPositions;
import com.bi.oranj.repository.bi.*;
import com.bi.oranj.repository.oranj.OranjAUMRepository;
import com.bi.oranj.repository.oranj.OranjGoalRepository;
import com.bi.oranj.repository.oranj.*;
import com.bi.oranj.utils.date.DateValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.*;

import static com.bi.oranj.constant.Constants.ERROR_IN_GETTING_GOALS_FROM_ORANJ;

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
    private PositionRepository positionRepository;

    @Autowired
    private OranjAUMRepository oranjAUMRepository;

    @Autowired
    private OranjClientRepository oranjClientRepository;

    @Autowired
    private OranjPositionsRepository oranjPositionsRepository;

    @Autowired
    private OranjNetWorthRepository oranjNetWorthRepository;

    @Autowired
    private NetWorthRepository netWorthRepository;

    @Autowired
    private DateValidator dateValidator;


    public RestResponse fetchPositionsData () {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date1 = simpleDateFormat.format(new Date());

        try{
            positionRepository.deleteAllAfterDate(date1);

            List<Object[]> positionRows = oranjPositionsRepository.fetchPositionsData();
            DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);

            savePositions(positionRows, dateFormat1);
        } catch (Exception ex){
            RestResponse.error("Error while fetching positions data" + ex);
        }

        return RestResponse.success();
    }

    /**
     *
     * @param limitNum - is how many rows of history to fetch from ORANJ DB
     */
    public RestResponse fetchPositionsHistory (long limitNum){
        DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        List<Object[]> history = null;

        try{
            if (limitNum == 0) history = oranjPositionsRepository.fetchPositionsHistory();
            else history = oranjPositionsRepository.fetchPositionsHistoryWithLimit(limitNum);
            savePositions(history, dateFormat1);
        }catch (Exception ex){
            return RestResponse.error("Error while fetching positions history" + ex);
        }

        return RestResponse.success();
    }

    public RestResponse fetchPositionsDataByDate(String date) {
        if (!dateValidator.validate(date)) return RestResponse.error("Date is not valid");

        List<OranjPositions> positions = oranjPositionsRepository.fetchPositionsHistoryByDate(date);
        if (positions.isEmpty()){
            log.info("Empty data set for the given date -> {}", date);
            return RestResponse.error(String.format("Empty data set for the given date %s", date));
        }

        return RestResponse.success();
    }

    private void savePositions (List<Object[]> history, DateFormat dateFormat1) throws ParseException {

        List<Position> posHis = new ArrayList<>();
        for (Object[] o : history) {

            String assetClass =  (String) o[4];
            if (assetClass == null) assetClass = getRandomAssetClass(); // ! this is for dummy data

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

        positionRepository.save(posHis);
    }

    public RestResponse getGoals(String date){

        try{
            List<OranjGoal> oranjGoalList = oranjGoalRepository.findByCreationDate(date);
            saveGoals(oranjGoalList);
        }catch (Exception e){
            log.error(ERROR_IN_GETTING_GOALS_FROM_ORANJ, e);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return RestResponse.error(ERROR_IN_GETTING_GOALS_FROM_ORANJ);
        }
        return RestResponse.success("Goals created on " + date + " have been saved");
    }

    public RestResponse getGoalsTillDate(String date){

        try{
            List<OranjGoal> oranjGoalList = oranjGoalRepository.findGoalsTillDate(date);
            saveGoals(oranjGoalList);
        }catch (Exception e){
            log.error("Error in fetching goals from Oranj." + e);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return RestResponse.error("Error in fetching Goals from Oranj DB");
        }
        return RestResponse.success("Goals created till " + date + " have been saved");
    }

    private void saveGoals(List<OranjGoal> oranjGoalList){
        try{
            for(int i=0; i<oranjGoalList.size(); i++){

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
                biGoal.setGoalCreationDate(oranjGoalList.get(i).getCreationDate());
                biGoal.setDeleted(oranjGoalList.get(i).isDeleted());
                biGoal.setFirmId(oranjGoalList.get(i).getFirmId());
                biGoal.setAdvisorId(oranjGoalList.get(i).getAdvisorId());
                biGoal.setClientId(oranjGoalList.get(i).getUser());
                goalRepository.save(biGoal);
            }
        }catch (Exception e){
            log.error("Error in storing goals in BI DB", e);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    public RestResponse getAllFirms(){
        try{
            List<Object[]> oranjFirmList = oranjGoalRepository.findAllFirms();
            for (Object[] firmResultSet : oranjFirmList) {
                Firm firm = new Firm();
                firm.setId(Long.parseLong(firmResultSet[0].toString()));
                firm.setFirmName(firmResultSet[1].toString());
                firm.setCreatedOn((Timestamp) firmResultSet[2]);
                firm.setActive((Boolean) firmResultSet[3]);
                firmRepository.save(firm);
            }
        }catch (Exception e){
            log.error("Error in fetching Firms from Oranj." + e);
            return RestResponse.error("Error in fecthing Firms from Oranj DB");
        }
        return RestResponse.success("All Firms have been saved");
    }

    public RestResponse getAllAdvisors(){
        try{
            List<Object[]> oranjAdvisorsList = oranjGoalRepository.findAllAdvisors();
            for (Object[] advisorsResultSet : oranjAdvisorsList) {
                Advisor advisor = new Advisor();
                advisor.setId(Long.parseLong(advisorsResultSet[0].toString()));
                advisor.setAdvisorFirstName(advisorsResultSet[1].toString());
                advisor.setAdvisorLastName(advisorsResultSet[2].toString());
                advisor.setFirmId(Long.parseLong(advisorsResultSet[3].toString()));
                advisor.setCreatedOn((Timestamp) advisorsResultSet[4]);
                advisor.setActive((Boolean) advisorsResultSet[5]);
                advisorRepository.save(advisor);
            }
        }catch (Exception e){
            log.error("Error in fetching Advisors from Oranj." + e);
            return RestResponse.error("Error in fecthing Advisors from Oranj DB");
        }
        return RestResponse.success("All Advisors have been saved");
    }

    public RestResponse getAllClients(){
        try{
            List<Object[]> oranjClientsList = oranjGoalRepository.findAllClients();
            for (Object[] clientsResultSet : oranjClientsList) {
                Client client = new Client();
                client.setId(Long.parseLong(clientsResultSet[0].toString()));
                client.setClientFirstName(clientsResultSet[1].toString());
                client.setClientLastName(clientsResultSet[2].toString());
                client.setAdvisorId(Long.parseLong(clientsResultSet[3].toString()));
                client.setFirmId(Long.parseLong(clientsResultSet[4].toString()));
                client.setCreatedOn((Timestamp) clientsResultSet[5]);
                client.setActive((Boolean) clientsResultSet[6]);
                clientRepository.save(client);
            }
//            List<Object[]> oranjClientsWhoAreAdvisorsList = oranjGoalRepository.findAllClientsWhoAreAdvisors();
//            for (Object[] clientsResultSet : oranjClientsWhoAreAdvisorsList) {
//                Client client = new Client();
//                client.setId(Long.parseLong(clientsResultSet[0].toString()));
//                client.setClientFirstName(clientsResultSet[1].toString());
//                client.setClientLastName(clientsResultSet[2].toString());
//                client.setAdvisorId(Long.parseLong(clientsResultSet[3].toString()));
//                client.setFirmId(Long.parseLong(clientsResultSet[4].toString()));
//                client.setCreatedOn((Timestamp) clientsResultSet[5]);
//                client.setActive((Boolean) clientsResultSet[6]);
//                clientRepository.save(client);
//            }
        }catch (Exception e){
            log.error("Error in fetching Clients from Oranj." + e);
            return RestResponse.error("Error in fecthing Clients from Oranj DB");
        }
        return RestResponse.success("All Clients have been saved");
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

    public RestResponse getNetWorthTillDate(String date){
        try{
            List<Object[]> oranjNetWorthList = oranjNetWorthRepository.findNetWorthTillDate(date);
            saveNetWorth(oranjNetWorthList);
        }catch (Exception e){
            log.error("Error in fetching net worth from Oranj." + e);
            return RestResponse.error("Error in fetching net worth from Oranj DB. "+ e);
        }
        return RestResponse.success("Net worth till " + date + " have been saved");
    }

    public RestResponse getNetWorth(String date){
        try{
            long index = 0;
            long step = 1000;
            while(true){
                List<Object[]> oranjNetWorthList = oranjNetWorthRepository.findNetWorthTillDateByStep(date, index, step);
                saveNetWorth(oranjNetWorthList);

                if(oranjNetWorthList.size() < step) break;
                index+=step;
                oranjNetWorthList.clear();
            }

        }catch (Exception e){
            log.error("Error in fetching goals from Oranj." + e);
            return RestResponse.error("Error in fetching Goals from Oranj DB");
        }
        return RestResponse.success("Goals created on " + date + " have been saved");

    }

    private void saveNetWorth(List<Object[]> oranjNetWorthList){
        List<NetWorth> netWorthList = new ArrayList<>();
        for (Object[] obj : oranjNetWorthList) {
            NetWorth netWorth = new NetWorth();
            netWorth.setId((BigInteger)obj[0]);
            netWorth.setDate((Timestamp)obj[1]);
            netWorth.setValue( BigDecimal.valueOf((double)obj[2]) );
            netWorth.setUserId((BigInteger)obj[3]);
            netWorth.setAssetValue(BigDecimal.valueOf((double)obj[4]));
            netWorth.setLiabilityValue(BigDecimal.valueOf((double)obj[5]));
            netWorthList.add(netWorth);
        }

        netWorthRepository.save(netWorthList);
        netWorthList.clear();
    }
}
