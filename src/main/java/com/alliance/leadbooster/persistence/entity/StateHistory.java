package com.alliance.leadbooster.persistence.entity;

import com.alliance.leadbooster.model.enums.DealState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "state_history")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StateHistory {

    @Id
    @Column(name = "uuid")
    private String uuid = UUID.randomUUID().toString();

    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    private DealState state;

    @Column(name = "deal_uuid")
    private String dealUuid;

    @Column(name = "from_date")
    private LocalDateTime fromDate;

    @CreationTimestamp
    @Column(name = "to_date", insertable = false, updatable = false)
    private LocalDateTime toDate;

}
