package ru.practicum.shareit.request;

import lombok.*;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * TODO Sprint add-item-requests.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
@Entity
@Table(name = "item_requests")
public class ItemRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id; //id запроса
    @Column(length = 20, nullable = false)
    private String description; //текст запрса
    @ManyToOne(optional = false)
    private User requester; //кто запрашивает
    @Column(nullable = false)
    private LocalDate created;
}