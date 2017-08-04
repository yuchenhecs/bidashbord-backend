package com.bi.oranj.service.bi;

import com.bi.oranj.model.bi.Grid;
import com.bi.oranj.model.bi.GridContainer;
import com.bi.oranj.model.bi.GridEntity;
import com.bi.oranj.repository.bi.GridRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.StringTokenizer;

@Service
public class GridConfigService {

    @Autowired
    private GridRepository gridRepository;

    private Logger log = LoggerFactory.getLogger(this.getClass());

    public ResponseEntity<Object> insertOrUpdateIfExits (GridContainer gridContainer){
        try{
            if (gridContainer.getUserId() == null)
                return new ResponseEntity<>("I need a userId !", HttpStatus.BAD_REQUEST);

            gridRepository.insertOrUpdateIfExists(
                    gridContainer.getUserId(),
                    convertGridToString(gridContainer.getGoals()),
                    convertGridToString(gridContainer.getAum()),
                    convertGridToString(gridContainer.getNetWorth()),
                    convertGridToString(gridContainer.getLogins()));
        } catch (Exception e){
            log.error("Error occurred while insert/updating grid config", e);
            return new ResponseEntity<>("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("Successfully inserted", HttpStatus.OK);
    }


    public ResponseEntity<Object> getGridConfig (Long userId){

        GridContainer gridContainer = new GridContainer();
        try{
            GridEntity gridConfig = gridRepository.getGridConfig(userId);

            if (gridConfig == null) return new ResponseEntity<>("null", HttpStatus.OK);

            gridContainer.setUserId(userId);
            gridContainer.setGoals(convertStringToGrid(gridConfig.getGoals()));
            gridContainer.setAum(convertStringToGrid(gridConfig.getAum()));
            gridContainer.setNetWorth(convertStringToGrid(gridConfig.getNetWorth()));
            gridContainer.setLogins(convertStringToGrid(gridConfig.getLogins()));
        } catch (Exception e) {
            log.error("Error occurred while getting grid config", e);
            return new ResponseEntity<>("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(gridContainer, HttpStatus.OK);
    }

// x:3,y:3,height:3,width:4
    private Grid convertStringToGrid (String stringConfig) throws Exception{
        if (stringConfig == null) return null;

        StringTokenizer stringTokenizer = new StringTokenizer(stringConfig, ",");

        Grid grid = new Grid();
        while (stringTokenizer.hasMoreTokens()){
            String[] vals = stringTokenizer.nextToken().split(":");
            if (vals[0].equalsIgnoreCase("x")) grid.setX(vals[1].equals("null") ? null : Integer.parseInt(vals[1].trim()));
            else if (vals[0].equalsIgnoreCase("y")) grid.setY(vals[1].equals("null") ? null : Integer.parseInt(vals[1].trim()));
            else if (vals[0].equalsIgnoreCase("height")) grid.setHeight(vals[1].equals("null") ? null : Integer.parseInt(vals[1].trim()));
            else if (vals[0].equalsIgnoreCase("width")) grid.setWidth(vals[1].equals("null") ? null : Integer.parseInt(vals[1].trim()));
        }
        return grid;
    }

    private String convertGridToString (Grid grid){
        if (grid == null) return null;

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("x:").append(grid.getX()).append(",")
                .append("y:").append(grid.getY()).append(",")
                .append("height:").append(grid.getHeight()).append(",")
                .append("width:").append(grid.getWidth());
        return stringBuilder.toString();
    }

}