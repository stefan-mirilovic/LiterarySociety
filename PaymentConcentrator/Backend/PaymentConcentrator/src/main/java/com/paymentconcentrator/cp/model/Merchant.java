package com.paymentconcentrator.cp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Data
@Table
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Merchant {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column
	private Long id;

	@Column
	private String email;

	@Column
	private UUID merchantId;

	@Column
	private String merchantPassword;

	@Column
	private String bankUrl;

	@OneToMany(mappedBy = "merchant", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Transaction> transactions;

	@ManyToMany
	@JoinTable(name = "merchant_payment", joinColumns = @JoinColumn(name = "payment_id"),inverseJoinColumns = @JoinColumn(name = "merchant_id"))
	private List<PaymentType> payments;
}
