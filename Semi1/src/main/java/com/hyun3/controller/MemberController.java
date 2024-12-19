package com.hyun3.controller;

import static com.hyun3.mvc.annotation.RequestMethod.GET;
import static com.hyun3.mvc.annotation.RequestMethod.POST;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.hyun3.dao.MemberDAO;
import com.hyun3.domain.SessionInfo;
import com.hyun3.domain.member.MemberDTO;
import com.hyun3.mvc.annotation.Controller;
import com.hyun3.mvc.annotation.RequestMapping;
import com.hyun3.mvc.annotation.ResponseBody;
import com.hyun3.mvc.view.ModelAndView;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class MemberController {
	@RequestMapping(value = "/member/login", method = GET)
	public ModelAndView loginForm(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		// 로그인 폼
		ModelAndView mav = new ModelAndView("member/login");
		return mav;
	}

	@RequestMapping(value = "/member/login", method = POST)
	public ModelAndView loginSubmit(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// 로그인 처리

		// 세션 객체
		HttpSession session = req.getSession();

		MemberDAO dao = new MemberDAO();

		// 클라이언트가 보낸 아이디/패스워드
		// req.setCharacterEncoding("UTF-8"); // 요청 데이터의 인코딩 설정
		String userId = req.getParameter("userId");
		String pwd = req.getParameter("pwd");

		MemberDTO dto = dao.loginMember(userId, pwd);

		if (dto != null) {
			// 로그인 성공한 경우
			// 세션에 아이디, 이름, 권한등을 저장

			// 세션 유지시간을 20분으로 설정 : 톰켓 기본은 30분
			session.setMaxInactiveInterval(20 * 60);

			// 세션에 저장할 정보
			SessionInfo info = new SessionInfo();
			info.setMb_Num(dto.getMb_Num());
			info.setUserId(dto.getUserId());
			info.setName(dto.getName());
			info.setRole(dto.getRole());
			info.setNickName(dto.getNickName());
			info.setLessonNum(dto.getLessonNum()); // 이 부분 추가 - 선웅

			// 세션에 member 라는 이름으로 로그인 정보를 저장
			session.setAttribute("member", info);

			// 로그인 전 주소가 존재하는 경우
			String preLoginURI = (String) session.getAttribute("preLoginURI");
			session.removeAttribute("preLoginURI");
			if (preLoginURI != null) {
				return new ModelAndView(preLoginURI);
			}

			// 메인 화면으로 리다이렉트
			return new ModelAndView("redirect:/");
		}

		// 아이디 또는 패스워드가 일치하지 않은 경우
		// 다시 로그인 폼으로 포워딩

		ModelAndView mav = new ModelAndView("member/login");
		mav.addObject("message", "아이디 또는 패스워드가 일치하지 않습니다.");
		return mav;
	}

	@RequestMapping(value = "/member/logout", method = GET)
	public ModelAndView logout(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 로그 아웃
		HttpSession session = req.getSession();

		// 세션에 저장된 정보 지우기
		session.removeAttribute("member");

		// 세션에 저장된 모든 내용을 지우고 세션을 초기화
		session.invalidate();

		// 메인화면으로 리다이렉트
		return new ModelAndView("redirect:/");
	}

	// 마이페이지
	@RequestMapping(value = "/member/myPage", method = GET)
	public ModelAndView myPageForm(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		HttpSession session = req.getSession();

		// 세션에서 사용자 정보 가져오기
		SessionInfo info = (SessionInfo) session.getAttribute("member");

		// 사용자 정보를 ModelAndView에 추가
		ModelAndView mav = new ModelAndView("member/myPage");
		mav.addObject("memberInfo", info);

		return mav;
	}

	// 비밀번호 변경 폼
	@RequestMapping(value = "/member/changePwd", method = GET)
	public ModelAndView changePasswordForm(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		HttpSession session = req.getSession();
		
		// 세션에서 사용자 정보 가져오기
		SessionInfo info = (SessionInfo) session.getAttribute("member");

		// 비밀번호 변경 폼
		ModelAndView mav = new ModelAndView("member/changePwd");
		mav.addObject("memberInfo", info);
		/* mav.addObject("message", "새 비밀번호와 확인 비밀번호가 일치하지 않습니다."); */

		return mav;
	}

	// 비밀번호 변경 처리
	@RequestMapping(value = "/member/changePwd", method = POST)
	public ModelAndView changePasswordSubmit(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException, SQLException {
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");

		if (info == null) {
			return new ModelAndView("redirect:/member/login");
		}

		String pwd = req.getParameter("pwd");
		String newPwd = req.getParameter("newPwd");
		String confirmPwd = req.getParameter("confirmPwd");

		MemberDAO dao = new MemberDAO();
		MemberDTO dto = dao.findById(info.getUserId());

		if (dto == null || !dto.getPwd().equals(pwd)) {
			ModelAndView mav = new ModelAndView("member/changePwd");
			mav.addObject("message", "현재 비밀번호가 일치하지 않습니다.");
			return mav;
		}

		if (!newPwd.equals(confirmPwd)) {
			ModelAndView mav = new ModelAndView("member/changePwd");
			mav.addObject("message", "새 비밀번호와 확인 비밀번호가 일치하지 않습니다.");
			return mav;
		}

		// 비밀번호 변경 처리
		dto.setPwd(newPwd);
		dao.updateMember(dto);

		ModelAndView mav = new ModelAndView("redirect:/member/myPage");
		return mav;
	}

	// 이메일 변경 폼
	@RequestMapping(value = "/member/changeEmail", method = GET)
	public ModelAndView changeEmailForm(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// 이메일 변경 폼
		ModelAndView mav = new ModelAndView("member/changeEmail");
		return mav;
	}

	// 이메일 변경 처리
	@RequestMapping(value = "/member/changeEmail", method = POST)
	public ModelAndView changeEmailSubmit(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException, SQLException {
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");

		if (info == null) {
			return new ModelAndView("redirect:/member/login");
		}

		String newEmail = req.getParameter("newEmail");

		MemberDAO dao = new MemberDAO();
		MemberDTO dto = dao.findById(info.getUserId());

		if (dto == null) {
			ModelAndView mav = new ModelAndView("member/changeEmail");
			mav.addObject("message", "회원 정보를 찾을 수 없습니다.");
			return mav;
		}

		// 이메일 변경 처리
		dto.setEmail(newEmail);
		dao.updateMember(dto);

		ModelAndView mav = new ModelAndView("redirect:/member/myPage");
		return mav;
	}

	///

	@RequestMapping(value = "/member/noAuthorized", method = GET)
	public ModelAndView noAuthorized(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// 권한이 없는 경우
		return new ModelAndView("member/noAuthorized");
	}

	@RequestMapping(value = "/member/member", method = GET)
	public ModelAndView memberForm(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// 회원가입 폼
		ModelAndView mav = new ModelAndView("member/member");

		mav.addObject("title", "회원 가입");
		mav.addObject("mode", "member");

		return mav;
	}

	@RequestMapping(value = "/member/member", method = POST)
	public ModelAndView memberSubmit(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// 회원가입 처리
		MemberDAO dao = new MemberDAO();
		HttpSession session = req.getSession();

		String message = "";
		try {
			MemberDTO dto = new MemberDTO();
			dto.setUserId(req.getParameter("userId"));
			dto.setPwd(req.getParameter("pwd"));
			dto.setName(req.getParameter("name"));
			System.out.println(req.getParameter("name"));
			dto.setNickName(req.getParameter("nickName"));
			dto.setBirth(req.getParameter("birth"));
			dto.setEmail(req.getParameter("email"));
			dto.setTel(req.getParameter("tel"));
			dto.setStudentNum(Integer.parseInt(req.getParameter("studentNum")));
			dao.insertMember(dto);

			session.setAttribute("mode", "insert");
			session.setAttribute("Name", dto.getName());

			return new ModelAndView("redirect:/member/complete");
		} catch (SQLException e) {
			if (e.getErrorCode() == 1) {
				message = "아이디 중복으로 회원 가입이 실패 했습니다.";
			} else if (e.getErrorCode() == 1400) {
				message = "필수 사항을 입력하지 않았습니다.";
			} else if (e.getErrorCode() == 1840 || e.getErrorCode() == 1861) {
				message = "날짜 형식이 일치하지 않습니다.";
			} else {
				e.printStackTrace();
				message = "회원 가입이 실패 했습니다.";
				// 기타 - 2291:참조키 위반, 12899:폭보다 문자열 입력 값이 큰경우
			}
		} catch (Exception e) {
			message = "회원 가입이 실패 했습니다.";
			e.printStackTrace();
		}

		ModelAndView mav = new ModelAndView("member/member");

		mav.addObject("title", "회원 가입");
		mav.addObject("mode", "member");
		mav.addObject("message", message);

		return mav;
	}

	@RequestMapping(value = "/member/pwd", method = GET)
	public ModelAndView pwdForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 패스워드 확인 폼
		ModelAndView mav = new ModelAndView("member/pwd");

		String mode = req.getParameter("mode");
		mav.addObject("mode", mode);

		return mav;
	}

	@RequestMapping(value = "/member/pwd", method = POST)
	public ModelAndView pwdSubmit(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// 패스워드 확인
		MemberDAO dao = new MemberDAO();
		HttpSession session = req.getSession();

		try {
			SessionInfo info = (SessionInfo) session.getAttribute("member");

			// DB에서 해당 회원 정보 가져오기
			MemberDTO dto = dao.findById(info.getUserId());
			if (dto == null) {
				session.invalidate();
				return new ModelAndView("redirect:/");
			}

			String pwd = req.getParameter("pwd");
			String mode = req.getParameter("mode");
			if (!dto.getPwd().equals(pwd)) {
				ModelAndView mav = new ModelAndView("member/pwd");

				mav.addObject("mode", mode);
				mav.addObject("message", "패스워드가 일치하지 않습니다.");

				return mav;
			}

			if (mode.equals("delete")) {
				// 회원탈퇴
				dao.deleteMember(info.getUserId());

				session.removeAttribute("member");
				session.invalidate();

				return new ModelAndView("redirect:/");
			}

			// 회원정보수정 - 회원수정폼으로 이동
			ModelAndView mav = new ModelAndView("member/member");

			mav.addObject("title", "회원 정보 수정");
			mav.addObject("dto", dto);
			mav.addObject("mode", "update");

			return mav;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ModelAndView("redirect:/");
	}

	@RequestMapping(value = "/member/update", method = POST)
	public ModelAndView updateSubmit(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// 회원정보 수정 완료
		MemberDAO dao = new MemberDAO();
		HttpSession session = req.getSession();

		try {
			MemberDTO dto = new MemberDTO();

			dto.setUserId(req.getParameter("userId"));
			dto.setPwd(req.getParameter("pwd"));
			dto.setName(req.getParameter("name"));
			dto.setNickName(req.getParameter("nickName"));
			dto.setBirth(req.getParameter("birth"));
			dto.setEmail(req.getParameter("email"));
			dto.setTel(req.getParameter("tel"));
			dto.setLessonNum(Integer.parseInt(req.getParameter("lessonNum")));

			dao.updateMember(dto);

			session.setAttribute("mode", "update");
			session.setAttribute("Name", dto.getName());

			return new ModelAndView("redirect:/member/complete");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ModelAndView("redirect:/");
	}

	@RequestMapping(value = "/member/complete", method = GET)
	public ModelAndView complete(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		HttpSession session = req.getSession();
		String mode = (String) session.getAttribute("mode");
		String Name = (String) session.getAttribute("Name");

		session.removeAttribute("mode");
		session.removeAttribute("Name");

		if (mode == null) {
			return new ModelAndView("redirect:/");
		}

		String title;
		String message = "<b>" + Name + "</b>님 ";
		if (mode.equals("insert")) {
			title = "회원 가입";
			message += "회원가입이 완료 되었습니다.<br>로그인 하시면 정보를 이용하실수 있습니다.";
		} else {
			title = "회원 정보 수정";
			message += "회원정보가 수정 되었습니다.<br>메인 화면으로 이동하시기 바랍니다.";
		}

		ModelAndView mav = new ModelAndView("member/complete");

		mav.addObject("title", title);
		mav.addObject("message", message);

		return mav;
	}

	@ResponseBody // AJAX : JSON으로 반환
	@RequestMapping(value = "/member/userIdCheck", method = POST)
	public Map<String, Object> userIdCheck(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// 아이디 중복 검사
		MemberDAO dao = new MemberDAO();

		String userId = req.getParameter("userId");
		MemberDTO dto = dao.findById(userId);
		String passed = "false";

		if (dto == null) {
			passed = "true";
		}

		Map<String, Object> model = new HashMap<>();

		model.put("passed", passed);

		return model;
	}

}