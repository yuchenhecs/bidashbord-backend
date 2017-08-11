package com.bi.oranj.model.bi;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * Created by jaloliddinbakirov on 8/11/17.
 */
@Embeddable
public class GridEntityId implements Serializable{
    @Column (name = "user_id")
    Long userId;
    @Column (name = "tile_type")
    String tileType;
}
