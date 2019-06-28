package tki.bigdata.pojo;

import java.util.Date;

import lombok.Data;

@Data
public class Category {
	private int id;
    private String name;
    private String description;
    private String regex;
}
