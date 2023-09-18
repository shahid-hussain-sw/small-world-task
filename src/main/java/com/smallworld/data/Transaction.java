package com.smallworld.data;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Builder
@NoArgsConstructor
@Data
public class Transaction {
    private Long mtn;
    private Double amount;
    private String senderFullName;
    private Integer senderAge;
    private String beneficiaryFullName;
    private Integer beneficiaryAge;
    private Long issueId;
    private Boolean issueSolved;
    private String issueMessage;
}
