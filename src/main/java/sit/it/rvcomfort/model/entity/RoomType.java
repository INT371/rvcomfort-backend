package sit.it.rvcomfort.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "room_type")
public class RoomType implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "type_id")
    private Integer typeId;

    @Column(name = "type_name")
    private String typeName;

    private String description;

    private BigDecimal price;

    @Column(name = "max_capacity")
    private Integer maxCapacity;

    private String policy;

    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "type", fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    private List<RoomTypeImage> images;

    @Column(name = "created_at")
    private ZonedDateTime createdAt;

    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;

    @OneToMany(mappedBy = "roomType", cascade = CascadeType.MERGE, orphanRemoval = true)
    @JsonBackReference
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @EqualsAndHashCode.Exclude
    private List<Room> rooms;

}
