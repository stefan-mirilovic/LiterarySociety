package com.paymentconcentrator.paypal.model;

import lombok.Data;

import javax.persistence.*;
import java.util.UUID;

@Data
@Table
@Entity
public class Account {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;

	@Column
	private UUID merchantId;

	@Column
	private String clientSecret;

	@Column
	private String clientId;
}
