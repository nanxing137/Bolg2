package net.bittreasury.bo;

import lombok.Data;
import net.bittreasury.entity.Classification;

import javax.persistence.Column;

@Data
public class ClassificationBO {
	private Long id;
	private String name;

	public ClassificationBO(Classification classification){
		this.setId(classification.getId());
		this.setName(classification.getName());
	}
}
