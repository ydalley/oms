package com.cogito.oms.service.implementation;

import java.util.Date;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.shiro.authc.credential.DefaultPasswordService;
import org.apache.shiro.authc.credential.PasswordService;
import org.apache.shiro.crypto.hash.format.Shiro1CryptFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.NoSuchMessageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cogito.oms.data.model.User;
import com.cogito.oms.data.repository.UserRepository;
import com.cogito.oms.service.ApplicationException;
import com.cogito.oms.service.UserService;
import com.cogito.oms.util.Messages;


@Service
@Transactional
public class UserServiceImpl implements UserService {
	private static final Logger log = LogManager
			.getLogger(UserServiceImpl.class);
	@Autowired
	private UserRepository userRepo;
	@Autowired
	Messages messages;
	@Autowired
	PasswordService passwordService;
	
	@Autowired
	JavaMailSender emailSender;


	@Override
	public User getUser(long id) {
		return userRepo.findOne(id);
	}




	@Override
	public User getUserForLoginByName(String userId) {
		//return userRepo.findByLoginIdAndStatus(userId,"E");
		return userRepo.findFirstByLoginId(userId);
	}


	public String modify(User user) throws ApplicationException {
		String result = "" ;
		try{
			userRepo.save(user);
			result= messages.get("user.update.success");
		}catch(Exception ex){
			result= messages.get("user.update.error");
			log.error(result, ex);
			throw new ApplicationException(result);
		}
		return result;
	}

	
	@Override
	public String add(User user) throws ApplicationException {
		String result = "" ;
		try{
			userRepo.save(user);
			result= messages.get("user.create.success");
		}catch(Exception ex){
			result= messages.get("user.create.error");
			log.error(result, ex);
			throw new ApplicationException(result);
		}
		return result;
	}


	public static void main(String[] args) throws ApplicationException {
		UserServiceImpl u = new UserServiceImpl();
//		String encrypt = u.encrypt("yd", "YD");
//		String encrypt2 = u.encrypt256("yd", "YD");
		//String encrypt2 = u.apacheEncrypt("yemi", "YDALLEY");
//		String encrypt3 = u.generateSHAdigest("yd");
//		System.out.println(encrypt);
//		System.out.println(encrypt2);
//		System.out.println(encrypt3);
		System.out.println("-------------------------------------------------------------------------------");
//		for(int i=0; i<100;i++){
//			String lgid = "tst" + i;
//			String pssd = u.generateSHAdigest(lgid);
//			String s = String.format("insert into user (id,first_name,last_name,login_id,profile_id,password) values (%4$d,'test%1$d','test%1$d','%2$s' ,2,'%3$s');", i,lgid,pssd,1000+i);
//			System.out.println(s);
//		}
//		System.out.println("-------------------------------------------------------------------------------");
	DefaultPasswordService dps = new DefaultPasswordService();
	System.out.println(dps.encryptPassword("yd"));
	dps.setHashFormat(new Shiro1CryptFormat());
	System.out.println(dps.encryptPassword("yd"));
	}

	@Override
	public void setUserPassword(User user, String oldPassword,
			String newPassword) throws ApplicationException {
		if (validatePassword(user, oldPassword, newPassword)) {
			String passwd = passwordService.encryptPassword(newPassword);
			user = userRepo.findOne(user.getId());
			user.setPassword(passwd);
			user.setExpiryDate(passwordExpiryDate());
			userRepo.save(user);
		}else{
			throw new ApplicationException("Password format is invalid");
		}

	}

	@Override
	public void setUserPassword(User user, String newPassword)
			throws ApplicationException {
		String passwd = passwordService.encryptPassword(newPassword);
		user.setPassword(passwd);
		user.setExpiryDate(new Date());
		userRepo.save(user);
	}

	@Override
	public void genearteUserPassword(User user) throws ApplicationException {
		String randomPassword = generateRandomPassword();
		log.debug("setting password for " + user + " to [" + randomPassword
				+ "]");
		setUserPassword(user, randomPassword);
		sendPasswordEmail(user, randomPassword);
	}

	private String generateRandomPassword() {
		String alphanumeric = RandomStringUtils.randomAlphanumeric(8);
		return alphanumeric;
	}

	private boolean validatePassword(User user, String oldPassword,
			String newPassword) {
		User tempagent = userRepo.findOne(user.getId());
		if (passwordService.passwordsMatch(oldPassword, tempagent.getPassword())) {
			return true;
		}
		return false;
	}

	private Date passwordExpiryDate() {
		return DateUtils.addDays(new Date(), 30);
	}

	@Override
	public String enable(User user) throws ApplicationException {
		user.setStatus("E");
		userRepo.save(user);
		return "";
	}

	@Async
	private void sendPasswordEmail(User user, String password) throws ApplicationException {
		MimeMessage message = emailSender.createMimeMessage();

		try {
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setTo(user.getEmail());
			helper.setText(messages.get("email.message", user.getLoginId(), password), true);
			helper.setSubject(messages.get("email.subject"));
			emailSender.send(message);
		} catch (NoSuchMessageException | MessagingException e1) {
			new ApplicationException("Error sending email, try again later");
		}
		
	}
	
	@Override
	public String disable(User user) throws ApplicationException {
		user.setStatus("D");
		userRepo.save(user);
		return "";
	}

	public String toggleStatus(User user) throws ApplicationException {
		String status = user.getStatus();
		String response = "";
		if (StringUtils.isEmpty(status)) {
			status = "D";
			response = "User is now disabled";
		} else if (status.equals("E")) {
			status = "D";
			response = "User is now disabled";
		} else if (status.equals("D")) {
			status = "E";
			response = "User is now enabled";
		}
		user.setStatus(status);
		Object o = null;
		try {
			o = userRepo.save(user);
		} catch (Exception e) {
			o = e.getMessage();
		}
		if(o instanceof String) 
			return (String)o;
			
		return response;
	}




	@Override
	public Page<User> getAllUsers(Pageable pageDetails) {
		return userRepo.findAll(pageDetails);
	}




	@Override
	public Page<User> findUser(String pattern, Pageable pageDetails) {
		
		return userRepo.findUsingPattern(pattern, pageDetails);
	}
}
