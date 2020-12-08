package com.paymentconcentrator.cp.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table
public class LiterarySociety {

	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;

	@Column
	private String name;

	@Column
	private String url;

	@Column
	private String successUrl;

	@Column
	private String failedUrl;

	@Column
	private String errorUrl;

	@OneToMany(mappedBy = "literarySociety", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Merchant> merchants;

	@ManyToMany
	@JoinTable(name = "literary_payment", joinColumns = @JoinColumn(name = "payment_id"),inverseJoinColumns = @JoinColumn(name = "literary_id"))
	private List<PaymentType> payments;

}
