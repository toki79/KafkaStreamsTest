package tki.bigdata.pojo;

import lombok.Data;

@Data
public class MyValueContainer {
   private Object value1;
   private Object value2;
   
   public MyValueContainer(Object value1, Object value2) {
	   this.value1 = value1;
	   this.value2 = value2;
   }
}
