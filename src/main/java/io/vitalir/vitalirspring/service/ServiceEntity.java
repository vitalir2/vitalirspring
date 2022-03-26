package io.vitalir.vitalirspring.service;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "services")
@Getter
@Setter
@NoArgsConstructor
public class ServiceEntity {
    @Id
    @Column(name = "title")
    String title;
}
