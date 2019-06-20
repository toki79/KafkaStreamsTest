package tki.bigdata.pojo;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data 
@ToString(includeFieldNames=true)
public class Contract {
	private int id;
    private String name; 

}
