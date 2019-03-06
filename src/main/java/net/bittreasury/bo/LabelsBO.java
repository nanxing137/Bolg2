package net.bittreasury.bo;

import lombok.Data;
import net.bittreasury.entity.Label;

import javax.persistence.Column;

@Data
public class LabelsBO {
	private Long id;
	private String name;

	public LabelsBO(Label label){
		this.setId(label.getId());
		this.setName(label.getName());
	}
}
