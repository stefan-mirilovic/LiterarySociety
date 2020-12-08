package com.paymentconcentrator.cp.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Data
@Table
@Entity
public class Merchant {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column
	private Long id;

	@Column
	private UUID merchantId;

	@Column
	private String merchantPassword;

	@Column
	private String bankUrl;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "literarySociety", referencedColumnName = "id")
	private LiterarySociety literarySociety;

	@OneToMany(mappedBy = "merchant", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Transaction> transactions;
}
