package com.bi.oranj.model.bi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "gamification_advisor")
@JsonIgnoreProperties(ignoreUnknown = true)
public class GamificationAdvisor {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "advisor_id")
    private Long advisorId;

    @Column(name = "points")
    private Long points;

    @Column(name = "percentile_state")
    private BigDecimal percentileState;

    @Column(name = "percentile_overall")
    private BigDecimal percentileOverall;

    @Column(name = "percentile_firm")
    private BigDecimal percentileFirm;

    @Column(name = "updated_on")
    private Timestamp updatedOn;

    public GamificationAdvisor(){
    }

}
