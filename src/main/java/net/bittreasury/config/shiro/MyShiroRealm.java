package net.bittreasury.config.shiro;

import java.util.HashSet;
import java.util.Set;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import net.bittreasury.entity.User;
import net.bittreasury.repository.ArticleRepository;
import net.bittreasury.repository.UserRepository;
import net.bittreasury.service.UserService;

@Service
public class MyShiroRealm extends AuthorizingRealm {

	@Autowired
	private UserService userService;

	/**
	 * 授权用户权限
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		// 获取用户
		User user = (User) SecurityUtils.getSubject().getPrincipal();
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		// 获取用户角色
		Set<String> roleSet = new HashSet<String>();
		roleSet.add("100002");
		info.setRoles(roleSet);

		// 获取用户权限
		Set<String> permissionSet = new HashSet<String>();
		permissionSet.add("权限添加");
		permissionSet.add("权限删除");
		info.setStringPermissions(permissionSet);

		return info;
	}

	/**
	 * 验证用户身份
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken)
			throws AuthenticationException {

		UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
		String username = token.getUsername();
		String password = String.valueOf(token.getPassword());
		System.out.println(username);
		System.out.println(password);
		User user = userService.findByUsername(username);

		// Map<String, Object> map = new HashMap<String, Object>();
		// map.put("nickname", username);
		// //密码进行加密处理 明文为 password+name
		// String paw = password+username;
		// String pawDES = MyDES.encryptBasedDes(paw);
		// map.put("pswd", pawDES);

		// user.setId("112222");
		// user.setUsername(username);
		// user.setPassword(pawDES);
		// System.out.println(user);
		if (null == user || !user.getPassword().equals(password)) {
			throw new AuthenticationException();
		}
		userService.updateDate(user);
		return new SimpleAuthenticationInfo(user, password, getName());
	}

}