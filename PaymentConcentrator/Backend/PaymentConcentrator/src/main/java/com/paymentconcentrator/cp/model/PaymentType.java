package com.paymentconcentrator.cp.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table
public class PaymentType {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column
	private Long id;

	@Column
	private String name;

	@Column
	private String url;

	@ManyToMany(mappedBy = "payments")
	private List<Merchant> merchantList;
}
