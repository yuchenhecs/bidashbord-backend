package com.bi.oranj.model.bi;

import lombok.Data;
import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLInsert;
import org.springframework.context.annotation.Configuration;

import javax.persistence.*;

/**
 * Created by jaloliddinbakirov on 8/3/17.
 */
@Data
@Entity
@Table(name = "grid_config")
@IdClass(GridEntityId.class)
@SQLInsert(sql = "INSERT INTO grid_config (settings, tile_type, user_id) " +
        "values (?, ?, ?) " +
        "ON DUPLICATE KEY UPDATE settings = VALUES(settings);")
public class GridEntity {

    @Id
    @Column(name = "user_id")
    private Long userId;

    @Id
    @Column (name = "tile_type")
    private String tileType;

    @Column (name = "settings")
    private String settings;

    public GridEntity(){}

    public GridEntity (Long userId, String tileType, String settings){
        this.userId = userId;
        this.tileType = tileType;
        this.settings = settings;
    }
}
