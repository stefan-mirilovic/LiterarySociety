package com.paymentconcentrator.cp.model;

import com.paymentconcentrator.cp.enumeration.TransactionStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column
	private Long id;

	@Column
	private LocalDateTime timestamp;

	@Column
	private Double amount;

	@Column
	private TransactionStatus status;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "merchant", referencedColumnName = "id")
	private Merchant merchant;

	@Column
	private Long acquirerOrderId;

	@Column
	private LocalDateTime acquirerTimestamp;

	@Column
	private Long paymentId;
}
