package ptithcm.controller;

import java.io.IOException;
import java.security.spec.InvalidKeySpecException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.StoredProcedureQuery;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.Transaction;
import javax.transaction.Transactional;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.w3c.dom.Document;

import ptithcm.bean.Mailer;
import ptithcm.bean.Verify;
import ptithcm.entity.ProductEntity;
import ptithcm.entity.UserEntity;
import ptithcm.ultils.PasswordUltils;

@Controller
public class RegisterController {
	@Autowired
	SessionFactory factory;
	@Autowired
	Mailer mailer;
	@Transactional
	@RequestMapping("register")
	public String register(ModelMap model) {
		model.addAttribute("user", new UserEntity());
		model.addAttribute("frmCtrlLastName", "form-control form-control-lg");
		model.addAttribute("frmCtrlFirstName", "form-control form-control-lg");
		model.addAttribute("frmCtrlEmail", "form-control form-control-lg");
		model.addAttribute("frmCtrlPhone", "form-control form-control-lg");
		model.addAttribute("frmCtrlAdd", "form-control form-control-lg");
		model.addAttribute("frmCtrlPsw", "form-control form-control-lg");
		model.addAttribute("frmCtrlRePsw", "form-control form-control-lg");
		model.addAttribute("frmCtrlCaptcha", "form-control form-control-lg");
		model.addAttribute("frmCtrlBD", "form-control form-control-lg");
		return "register";
	}

	public Integer saveAccount(UserEntity user) {
		Session session = factory.openSession();
		org.hibernate.Transaction t = session.beginTransaction();
		int ret = 0;
		try {
			session.save(user);
			t.commit();
		} catch (Exception e) {
		
			t.rollback();
			ret = -1;
		} finally {
			session.close();
		}
		return ret;

	}

	public List<UserEntity> getUser(String email) {
		Session session = factory.openSession();
		String hql = "FROM UserEntity u WHERE u.email = :email";
		Query query = session.createQuery(hql);
		query.setParameter("email", email);
		return query.list();
	}

	@RequestMapping(value = "validate", method = RequestMethod.POST)
	public String validate(ModelMap model, @Validated @ModelAttribute("user") UserEntity user, BindingResult errors,
			@RequestParam("psw_repeat") String psw_repeat,
			@RequestParam("birthday") @DateTimeFormat(pattern = "yyyy-MM-dd") Date birthday, HttpServletRequest request,
			HttpSession ss, HttpServletResponse response) {
		
		boolean err = false;
		user.setEmail(user.getEmail().trim());
		if (user.getLastname().isBlank()) {
			model.addAttribute("frmCtrlLastName", "form-control form-control-lg is-invalid");
			model.addAttribute("fbLastName", "invalid-feedback");
			model.addAttribute("contentFBLN", "Kh??ng ???????c ????? tr???ng h???");
			err = true;
		} else {
			model.addAttribute("frmCtrlLastName", "form-control form-control-lg is-valid");
		
		}
		if(birthday==null)
		{
			model.addAttribute("frmCtrlBD", "form-control form-control-lg is-invalid");
			model.addAttribute("fbBD", "invalid-feedback");
			model.addAttribute("contentFBBD", "Kh??ng ???????c ????? tr???ng ng??y sinh");
			err = true;
		}
		else
		{
			model.addAttribute("frmCtrlBD", "form-control form-control-lg is-valid");
			user.setBirthday(birthday);
		}
		if (user.getAddress().isBlank()) {
			model.addAttribute("frmCtrlAdd", "form-control form-control-lg is-invalid");
			model.addAttribute("fbAdd", "invalid-feedback");
			model.addAttribute("contentFBAdd", "Kh??ng ???????c ????? tr???ng ?????a ch???");
			err = true;
		} else {
			model.addAttribute("frmCtrlAdd", "form-control form-control-lg is-valid");
			
		}

		if (user.getFirstname().isBlank()) {
			model.addAttribute("frmCtrlFirstName", "form-control form-control-lg is-invalid");
			model.addAttribute("fbFirstName", "invalid-feedback");
			model.addAttribute("contentFBFN", "Kh??ng ???????c ????? tr???ng t??n");
			err = true;

		} else {
			model.addAttribute("frmCtrlFirstName", "form-control form-control-lg is-valid");
		
		}

		Pattern patternEmail = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
		if (user.getEmail().isBlank()) {
			model.addAttribute("frmCtrlEmail", "form-control form-control-lg is-invalid");
			model.addAttribute("fbEmail", "invalid-feedback");
			model.addAttribute("contentFBEmail", "Kh??ng ???????c ????? tr???ng email");
			err = true;

		} else {

			Matcher matcher = patternEmail.matcher(user.getEmail());
			boolean match = matcher.matches();
			if (!match) {
				model.addAttribute("frmCtrlEmail", "form-control form-control-lg is-invalid");
				model.addAttribute("fbEmail", "invalid-feedback");
				model.addAttribute("contentFBEmail", "?????a ch??? email kh??ng h???p l???");
				err = true;
			} else if (getUser(user.getEmail()).size() != 0) {
				model.addAttribute("frmCtrlEmail", "form-control form-control-lg is-invalid");
				model.addAttribute("fbEmail", "invalid-feedback");
				model.addAttribute("contentFBEmail", "?????a ch??? email ???????c ???????c ????ng k?? b???i t??i kho???n kh??c");
				err = true;
			} else {
				model.addAttribute("frmCtrlEmail", "form-control form-control-lg is-valid");
				
			}
		}

		

		String regexPhone = "(84|0[3|5|7|8|9])+([0-9]{8})";
		Pattern patternPhone = Pattern.compile(regexPhone);
		if (user.getPhone().isBlank()) {
			model.addAttribute("frmCtrlPhone", "form-control form-control-lg is-invalid");
			model.addAttribute("fbPhone", "invalid-feedback");
			model.addAttribute("contentFBPhone", "S??? ??i???n tho???i kh??ng ???????c ????? tr???ng");
			err = true;
		} else if (!user.getPhone().isBlank()) {
			Matcher matcher = patternPhone.matcher(user.getPhone());
			boolean match = matcher.matches();
			if (!match) {
				model.addAttribute("frmCtrlPhone", "form-control form-control-lg is-invalid");
				model.addAttribute("fbPhone", "invalid-feedback");
				model.addAttribute("contentFBPhone", "S??? ??i???n tho???i kh??ng h???p l???");
				err = true;
			} else {
				model.addAttribute("frmCtrlPhone", "form-control form-control-lg is-valid");
				
			}
		}
		String regexPass = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$";
		Pattern patternPass = Pattern.compile(regexPass);
		boolean matchPass = true;
		if (user.getPassword().isBlank()) {
			model.addAttribute("frmCtrlPsw", "form-control form-control-lg is-invalid");
			model.addAttribute("fbPsw", "invalid-feedback");
			model.addAttribute("contentFBPsw", "Kh??ng ???????c ????? tr???ng m???t kh???u");
			err = true;
		}

		else if (!user.getPassword().isBlank()) {
			Matcher matcher = patternPass.matcher(user.getPassword());
			matchPass = matcher.matches();
			if (!matchPass) {
				model.addAttribute("frmCtrlPsw", "form-control form-control-lg is-invalid");
				model.addAttribute("fbPsw", "invalid-feedback");
				model.addAttribute("contentFBPsw",
						"M???t kh???u ph???i ch???a t???i thi???u 8 k?? t???, bao g???m ch??? c??i, ch??? s???, k?? t??? ?????c bi???t");
				err = true;
			} else {
				model.addAttribute("frmCtrlPsw", "form-control form-control-lg is-valid");
				
			}
		}

		if (psw_repeat.isBlank()) {
			model.addAttribute("frmCtrlRePsw", "form-control form-control-lg is-invalid");
			model.addAttribute("fbRePsw", "invalid-feedback");
			model.addAttribute("contentFBRePsw", "Kh??ng ???????c ????? tr???ng");
			err = true;
		} else if (matchPass && !psw_repeat.equals(user.getPassword())) {
			model.addAttribute("frmCtrlRePsw", "form-control form-control-lg is-invalid");
			model.addAttribute("fbRePsw", "invalid-feedback");
			model.addAttribute("contentFBRePsw", "M???t kh???u kh??ng kh???p");
			err = true;
		} else {
			model.addAttribute("frmCtrlRePsw", "form-control form-control-lg");

		}

		String captcha = ss.getAttribute("captcha_security").toString();
		String verifyCaptcha = request.getParameter("captcha");
		boolean verify = false;

		if (captcha.equals(verifyCaptcha)) {

			verify = true;
		} else {
			verify = false;
		}

		if (!verify) {
			model.addAttribute("frmCtrlCaptcha", "form-control form-control-lg is-invalid");
			model.addAttribute("fbCaptcha", "invalid-feedback");
			model.addAttribute("contentFBCaptcha", "M???i b???n nh???p l???i captcha");
			return "register";
		}
		else
		{
			model.addAttribute("frmCtrlCaptcha", "form-control form-control-lg");
		}

		if (err == false && verify) {
			model.addAttribute("message", "B???n ???? nh???p th??nh c??ng");
			String salt = PasswordUltils.getSalt(30);
			user.setSalt(salt);
			String yourPass = "";
			try {
				yourPass = PasswordUltils.generateSecurePassword(user.getPassword(), salt);
			} catch (InvalidKeySpecException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			user.setPassword(yourPass);
			if(user.getSex()==0)
			{
				user.setAvatar("men.jpg");
			}
			else
			{
				user.setAvatar("women.jpg");
			}
			if (saveAccount(user) == 0) {
				ss.setAttribute("userVerify", user);
				Verify verifyCode= new Verify();
				verifyCode.codeVerify=PasswordUltils.generateRandomPassword();
				mailer.sendVerifyCode(verifyCode.codeVerify, user.getEmail());
				return "redirect:/user/verify.htm";
			} else {
				model.addAttribute("message", "T???o t??i kho???n th???t b???i");
			}
		}
		return "register";
	}

}
