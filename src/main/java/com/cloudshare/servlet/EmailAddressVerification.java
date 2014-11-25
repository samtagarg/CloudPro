package com.cloudshare.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cloudshare.dao.CommonDAO;

@WebServlet("/EmailAddressVerification")
public class EmailAddressVerification extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public EmailAddressVerification() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String emailAddressVerificationCode = request
				.getParameter("emailAddressVerificationCode");
		PrintWriter out = null;
		try {
			CommonDAO dao = CommonDAO.getInstance();
			String result = dao.verifyEmailAddress(Integer
					.parseInt(emailAddressVerificationCode));

			out = response.getWriter();
			if (result != null) {
				out.println("<h1> Welcome " + result + "</h1>");
				out.println("<h1> Email Verified </h1>");
			} else {
				throw new Exception();
			}

		} catch (Exception e) {
			out.println("<h1> Oops!! Something went wrong</h1>");
		}
	}

}