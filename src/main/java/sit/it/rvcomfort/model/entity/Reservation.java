package sit.it.rvcomfort.model.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "reservation")
public class Reservation implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    @EqualsAndHashCode.Exclude
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "room_id")
    @EqualsAndHashCode.Exclude
    private Room room;

    @Column(name = "check_in_date")
    private ZonedDateTime checkInDate;

    @Column(name = "check_out_date")
    private ZonedDateTime checkOutDate;

    @Column(name = "reserved_name")
    private String reservedName;

    @Column(name = "num_of_guest")
    private Integer numOfGuest;

    @Column(name = "total_price")
    private BigDecimal totalPrice;

    @Column(name = "status")
    private String status;

    @Column(name = "created_at")
    private ZonedDateTime createdAt;

    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;

}
