package com.cloudshare.dao;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cloudshare.useroperations.bean.UserBean;
import com.cloudshare.useroperations.dto.RegistrationDTO;
import com.cloudshare.util.DBConfig;

public class CommonDAO implements Serializable {
	private static final long serialVersionUID = 8562224930520844251L;

	Logger logger = LoggerFactory.getLogger(CommonDAO.class);

	public static String USER_DONOT_EXISTS = "User not present";

	private static CommonDAO SINGLETON;

	public static CommonDAO getInstance() throws Exception {
		if (SINGLETON == null)
			SINGLETON = new CommonDAO();

		return SINGLETON;
	}

	private CommonDAO() throws Exception {
		if (SINGLETON != null) {
			throw new Exception(CommonDAO.class.getName());
		}
	}

	@SuppressWarnings("unchecked")
	public UserBean searchByEmailId(String emailId) throws Exception {
		UserBean user = null;
		try {
			Session session = DBConfig.getSessionFactory().openSession();

			session.beginTransaction();

			Criteria crit = session.createCriteria(UserBean.class);
			Criterion emailAddressRestriction = Restrictions.eq("emailAddress",
					emailId);
			System.out.println(emailId);
			Criterion activeRestriction = Restrictions.eq("active", "Y");

			LogicalExpression andExp = Restrictions.and(
					emailAddressRestriction, activeRestriction);
			crit.add(andExp);

			List<UserBean> famedenUserList = ((List<UserBean>) crit.list());

			if (famedenUserList != null && famedenUserList.size() > 0) {
				user = famedenUserList.get(0);
			} else {
				throw new Exception(USER_DONOT_EXISTS);
			}

			session.getTransaction().commit();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw e;
		}
		return user;
	}

	@SuppressWarnings("unchecked")
	public UserBean searchByExternalUserId(int externalUserId) throws Exception {
		UserBean user = null;
		try {
			Session session = DBConfig.getSessionFactory().openSession();

			session.beginTransaction();

			Criteria crit = session.createCriteria(UserBean.class);
			Criterion emailAddressRestriction = Restrictions.eq("userId",
					externalUserId);

			Criterion activeRestriction = Restrictions.eq("active", "Y");

			LogicalExpression andExp = Restrictions.and(
					emailAddressRestriction, activeRestriction);
			crit.add(andExp);

			List<UserBean> famedenUserList = ((List<UserBean>) crit.list());

			if (famedenUserList != null && famedenUserList.size() > 0) {
				user = famedenUserList.get(0);
			} else {
				throw new Exception(USER_DONOT_EXISTS);
			}

			session.getTransaction().commit();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw e;
		}
		return user;
	}

	@SuppressWarnings("unchecked")
	public UserBean searchByEmailIdAndMobileNumber(String emailId)
			throws Exception {
		UserBean user = null;
		try {
			Session session = DBConfig.getSessionFactory().openSession();

			session.beginTransaction();

			Criteria crit = session.createCriteria(UserBean.class);
			Criterion emailAddressRestriction = Restrictions.eq("emailAddress",
					emailId);

			Criterion activeRestriction = Restrictions.eq("active", "Y");
			LogicalExpression andExp = Restrictions.and(
					emailAddressRestriction, activeRestriction);

			crit.add(andExp);

			List<UserBean> famedenUserList = ((List<UserBean>) crit.list());

			if (famedenUserList != null && famedenUserList.size() > 0) {
				user = famedenUserList.get(0);
			}

			session.getTransaction().commit();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw e;
		}
		return user;
	}

	public UserBean getVerifiedUserInfoByEmailId(String emailId)
			throws Exception {
		UserBean user = null;

		user = searchByEmailId(emailId);

		if (user != null) {
			if (user.getIsVerified() != null
					&& !user.getIsVerified().equals("Y")) {
				throw new Exception("User not verified");
			}
		} else {
			throw new Exception(USER_DONOT_EXISTS);
		}
		return user;
	}

	public int registerUser(RegistrationDTO dto) throws Exception {
		Session session = null;
		int externalId = 0;
		try {
			session = DBConfig.getSessionFactory().openSession();

			session.beginTransaction();
			UserBean bean = new UserBean();
			bean.setActive("Y");
			bean.setCreationDate(Calendar.getInstance().getTime());
			bean.setEmailAddress(dto.getUserEmailAddress());
			bean.setIsVerified("N");
			bean.setPassword(dto.getUserPassword());
			bean.setUserName(dto.getUserFullName());
			session.save(bean);
			externalId = bean.getUserId();
			session.getTransaction().commit();
		} catch (Exception e) {
			if (session != null) {
				session.getTransaction().rollback();
			}
			logger.error(e.getMessage(), e);
			throw e;
		}
		return externalId;
	}

	@SuppressWarnings("unchecked")
	public String verifyEmailAddress(int externalId) throws Exception {
		String fullName = null;
		try {
			Session session = DBConfig.getSessionFactory().openSession();

			session.beginTransaction();

			Criteria crit = session.createCriteria(UserBean.class);

			Criterion activeRestriction = Restrictions.eq("active", "Y");
			Criterion isVerifiedRestriction = Restrictions
					.eq("isVerified", "N");

			Criterion emailAddressRestriction = Restrictions.eq("userId",
					externalId);

			LogicalExpression andExp = Restrictions.and(
					emailAddressRestriction, activeRestriction);

			LogicalExpression andExp1 = Restrictions.and(isVerifiedRestriction,
					andExp);
			crit.add(andExp1);

			List<UserBean> famedenUserList = ((List<UserBean>) crit.list());

			if (famedenUserList != null && famedenUserList.size() > 0) {

				UserBean user = famedenUserList.get(0);
				user.setIsVerified("Y");
				fullName = user.getUserName();
			}

			session.getTransaction().commit();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw e;
		}
		return fullName;
	}

}
