package tki.bigdata.pojo;

import java.util.Date;

import lombok.Data;

@Data
public class Cashflow {
	private String date;
	private float amount;
	private String postingText;
	private int contractId;
    private Contract contract;
    private Category category;
     
}
