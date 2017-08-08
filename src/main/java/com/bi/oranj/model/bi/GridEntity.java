package com.bi.oranj.model.bi;

import lombok.Data;
import org.hibernate.annotations.SQLInsert;
import org.springframework.context.annotation.Configuration;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by jaloliddinbakirov on 8/3/17.
 */
@Data
@Entity
@Table(name = "grid_config")
@SQLInsert(sql = "INSERT INTO grid_config (user_id, tile_type, settings) " +
        "values (?, ?, ?) " +
        "ON DUPLICATE KEY UPDATE tile_type = VALUES(tile_type), settings = VALUES(settings);")
public class GridEntity {

    @Id
    @Column(name = "user_id")
    private Long userId;

    @Column (name = "tile_type")
    private String tileType;
    private String settings;
}
