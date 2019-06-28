package tki.bigdata.pojo;

import lombok.Data;

@Data
public class MyValueContainer {
   private Cashflow value1;
   private Contract value2;
   
   public MyValueContainer() {

	}
   
   public MyValueContainer(Cashflow value1, Contract value2) {
	   this.value1 = value1;
	   this.value2 = value2;
   }


}
