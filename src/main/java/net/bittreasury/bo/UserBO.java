package net.bittreasury.bo;

import lombok.Data;
import net.bittreasury.entity.User;

@Data
public class UserBO {
	private Long id;
	private String username;
	private String nickName;

	public UserBO(User user){
		this.setId(user.getId());
		this.setUsername(user.getUsername());
		this.setNickName(user.getNickName());
	}
}
