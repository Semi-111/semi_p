package com.hyun3.controller;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import com.hyun3.dao.MyCalendarDAO;
import com.hyun3.domain.MyCalendarDTO;
import com.hyun3.domain.SessionInfo;
import com.hyun3.mvc.annotation.Controller;
import com.hyun3.mvc.annotation.RequestMapping;
import com.hyun3.mvc.annotation.RequestMethod;
import com.hyun3.mvc.annotation.ResponseBody;
import com.hyun3.mvc.view.ModelAndView;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class MyCalendarController {
	@RequestMapping(value = "/myCalendar/myCal", method = RequestMethod.GET)
	public ModelAndView main(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + 1; // 0 ~ 11
		int date = cal.get(Calendar.DATE);
		
		String today = String.format("%04d%02d%02d", year, month, date);
		
		ModelAndView mav = new ModelAndView("myCalendar/myCal");
		mav.addObject("today", today);
		return mav;
	}
	
	// AJAX - Text
	@RequestMapping(value = "/myCalendar/month", method = RequestMethod.GET)
	public ModelAndView monthMyCalendar(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		MyCalendarDAO dao = new MyCalendarDAO();
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");

		ModelAndView mav = new ModelAndView("myCalendar/month");
		
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + 1; // 0 ~ 11
		int todayYear = year;
		int todayMonth = month;
		int todayDate = cal.get(Calendar.DATE);
		
		try {
			String y = req.getParameter("year");
			String m = req.getParameter("month");

			if (y != null) {
				year = Integer.parseInt(y);
			}
			if (m != null) {
				month = Integer.parseInt(m);
			}

			// year년 month월 1일의 요일
			cal.set(year, month - 1, 1);
			year = cal.get(Calendar.YEAR);
			month = cal.get(Calendar.MONTH) + 1;
			int week = cal.get(Calendar.DAY_OF_WEEK); // 1~7

			// 첫주의 year년도 month월 1일 이전 날짜
			Calendar scal = (Calendar) cal.clone();
			scal.add(Calendar.DATE, -(week - 1));
			int syear = scal.get(Calendar.YEAR);
			int smonth = scal.get(Calendar.MONTH) + 1;
			int sdate = scal.get(Calendar.DATE);

			// 마지막주의 year년도 month월 말일주의 토요일 날짜
			Calendar ecal = (Calendar) cal.clone();
			// year년도 month월 말일
			ecal.add(Calendar.DATE, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
			// year년도 month월 말일주의 토요일
			ecal.add(Calendar.DATE, 7 - ecal.get(Calendar.DAY_OF_WEEK));
			int eyear = ecal.get(Calendar.YEAR);
			int emonth = ecal.get(Calendar.MONTH) + 1;
			int edate = ecal.get(Calendar.DATE);

			// 스케쥴 가져오기
			String startDay = String.format("%04d%02d%02d", syear, smonth, sdate);
			String endDay = String.format("%04d%02d%02d", eyear, emonth, edate);
			List<MyCalendarDTO> list = dao.listMonth(startDay, endDay, info.getMemberIdx());

			String s;
			String[][] days = new String[cal.getActualMaximum(Calendar.WEEK_OF_MONTH)][7];

			// 1일 앞의 전달 날짜 및 일정 출력
			// startDay ~ endDay 까지 처리
			int cnt;
			for (int i = 1; i < week; i++) {
				s = String.format("%04d%02d%02d", syear, smonth, sdate);
				days[0][i - 1] = "<span class='textDate preMonthDate' data-date='" + s + "' >" + sdate + "</span>";

				cnt = 0;
				for (MyCalendarDTO dto : list) {
					int sd8 = Integer.parseInt(dto.getSday());
					int sd4 = Integer.parseInt(dto.getSday().substring(4));
					int ed8 = -1;
					if (dto.getEday() != null) {
						ed8 = Integer.parseInt(dto.getEday());
					}
					int cn8 = Integer.parseInt(s);
					int cn4 = Integer.parseInt(s.substring(4));

					if (cnt == 4) {
						days[0][i - 1] += "<span class='scheduleMore' data-date='" + s + "' >" + "more..." + "</span>";
						break;
					}

					if ((dto.getRepeat() == 0 && (sd8 == cn8 || sd8 <= cn8 && ed8 >= cn8))
							|| (dto.getRepeat() == 1 && sd4 == cn4)) {
						days[0][i - 1] += "<span class='scheduleSubject' data-date='" + s + "' data-num='" + dto.getNum()
								+ "' >" + dto.getSubject() + "</span>";
						cnt++;
					} else if ((dto.getRepeat() == 0 && (sd8 > cn8 && ed8 < cn8)) || (dto.getRepeat() == 1 && sd4 > cn4)) {
						break;
					}
				}

				sdate++;
			}

			// year년도 month월 날짜 및 일정 출력
			int row, n = 0;

			jump: for (row = 0; row < days.length; row++) {
				for (int i = week - 1; i < 7; i++) {
					n++;
					s = String.format("%04d%02d%02d", year, month, n);

					if (i == 0) {
						days[row][i] = "<span class='textDate sundayDate' data-date='" + s + "' >" + n + "</span>";
					} else if (i == 6) {
						days[row][i] = "<span class='textDate saturdayDate' data-date='" + s + "' >" + n + "</span>";
					} else {
						days[row][i] = "<span class='textDate nowDate' data-date='" + s + "' >" + n + "</span>";
					}

					cnt = 0;
					for (MyCalendarDTO dto : list) {
						int sd8 = Integer.parseInt(dto.getSday());
						int sd4 = Integer.parseInt(dto.getSday().substring(4));
						int ed8 = -1;
						if (dto.getEday() != null) {
							ed8 = Integer.parseInt(dto.getEday());
						}
						int cn8 = Integer.parseInt(s);
						int cn4 = Integer.parseInt(s.substring(4));

						if (cnt == 4) {
							days[row][i] += "<span class='scheduleMore' data-date='" + s + "' >" + "more..." + "</span>";
							break;
						}

						if ((dto.getRepeat() == 0 && (sd8 == cn8 || sd8 <= cn8 && ed8 >= cn8))
								|| (dto.getRepeat() == 1 && sd4 == cn4)) {
							days[row][i] += "<span class='scheduleSubject' data-date='" + s + "' data-num='" + dto.getNum()
									+ "' >" + dto.getSubject() + "</span>";
							cnt++;
						} else if ((dto.getRepeat() == 0 && (sd8 > cn8 && ed8 < cn8))
								|| (dto.getRepeat() == 1 && sd4 > cn4)) {
							break;
						}
					}

					if (n == cal.getActualMaximum(Calendar.DATE)) {
						week = i + 1;
						break jump;
					}
				}
				week = 1;
			}

			// year년도 month월 마지막 날짜 이후 일정 출력
			if (week != 7) {
				n = 0;
				for (int i = week; i < 7; i++) {
					n++;
					s = String.format("%04d%02d%02d", eyear, emonth, n);
					days[row][i] = "<span class='textDate nextMonthDate' data-date='" + s + "' >" + n + "</span>";

					cnt = 0;
					for (MyCalendarDTO dto : list) {
						int sd8 = Integer.parseInt(dto.getSday());
						int sd4 = Integer.parseInt(dto.getSday().substring(4));
						int ed8 = -1;
						if (dto.getEday() != null) {
							ed8 = Integer.parseInt(dto.getEday());
						}
						int cn8 = Integer.parseInt(s);
						int cn4 = Integer.parseInt(s.substring(4));

						if (cnt == 4) {
							days[row][i] += "<span class='scheduleMore' data-date='" + s + "' >" + "more..." + "</span>";
							break;
						}

						if ((dto.getRepeat() == 0 && (sd8 == cn8 || sd8 <= cn8 && ed8 >= cn8))
								|| (dto.getRepeat() == 1 && sd4 == cn4)) {
							days[row][i] += "<span class='scheduleSubject' data-date='" + s + "' data-num='" + dto.getNum()
									+ "' >" + dto.getSubject() + "</span>";
							cnt++;
						} else if ((dto.getRepeat() == 0 && (sd8 > cn8 && ed8 < cn8))
								|| (dto.getRepeat() == 1 && sd4 > cn4)) {
							break;
						}
					}

				}
			}

			String today = String.format("%04d%02d%02d", todayYear, todayMonth, todayDate);

			mav.addObject("year", year);
			mav.addObject("month", month);
			mav.addObject("todayYear", todayYear);
			mav.addObject("todayMonth", todayMonth);
			mav.addObject("todayDate", todayDate);
			mav.addObject("today", today);
			mav.addObject("days", days);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return mav;
	}

	// AJAX - Text
	@RequestMapping(value = "/MyCalendare/day", method = RequestMethod.GET)
	public ModelAndView dayMyCalendar(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		MyCalendarDAO dao = new MyCalendarDAO();
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");
		
		ModelAndView mav = new ModelAndView("myCalendar/day");
		
		try {
			String date = req.getParameter("date");
			String snum = req.getParameter("num");

			Calendar cal = Calendar.getInstance();

			// 오늘날짜
			String today = String.format("%04d%02d%02d", 
					cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DATE));
			if (date == null || !Pattern.matches("^\\d{8}$", date)) {
				date = today;
			}
			
			// 일일 일정을 출력할 년, 월, 일
			int year = Integer.parseInt(date.substring(0, 4));
			int month = Integer.parseInt(date.substring(4, 6));
			int day = Integer.parseInt(date.substring(6));

			cal.set(year, month - 1, day);
			year = cal.get(Calendar.YEAR);
			month = cal.get(Calendar.MONTH) + 1;
			day = cal.get(Calendar.DATE);

			cal.set(year, month - 1, 1);
			int week = cal.get(Calendar.DAY_OF_WEEK);

			// 테이블에서 일일 전체일정 리스트 가져오기
			date = String.format("%04d%02d%02d", year, month, day);
			List<MyCalendarDTO> list = dao.listDay(date, info.getMemberIdx());

			long num = 0;
			MyCalendarDTO dto = null;
			if (snum != null) {
				num = Long.parseLong(snum);
				dto = dao.readMyCalendar(num);
			}
			if (dto == null && list.size() > 0) {
				dto = dao.readMyCalendar(list.get(0).getNum());
			}

			// 이전달과 다음달 1일의 날짜
			Calendar cal2 = (Calendar) cal.clone();
			cal2.add(Calendar.MONTH, -1);
			cal2.set(Calendar.DATE, 1);
			String preMonth = String.format("%04d%02d%02d", 
					cal2.get(Calendar.YEAR), cal2.get(Calendar.MONTH) + 1, cal2.get(Calendar.DATE));

			cal2.add(Calendar.MONTH, 2);
			String nextMonth = String.format("%04d%02d%02d", 
					cal2.get(Calendar.YEAR), cal2.get(Calendar.MONTH) + 1, cal2.get(Calendar.DATE));

			// 첫주의 year년도 month월 1일 이전 날짜
			Calendar scal = (Calendar) cal.clone();
			scal.add(Calendar.DATE, -(week - 1));
			int syear = scal.get(Calendar.YEAR);
			int smonth = scal.get(Calendar.MONTH) + 1;
			int sdate = scal.get(Calendar.DATE);

			// 마지막주의 year년도 month월 말일주의 토요일 날짜
			Calendar ecal = (Calendar) cal.clone();
			// year년도 month월 말일
			ecal.add(Calendar.DATE, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
			// year년도 month월 말일주의 토요일
			ecal.add(Calendar.DATE, 7 - ecal.get(Calendar.DAY_OF_WEEK));
			int eyear = ecal.get(Calendar.YEAR);
			int emonth = ecal.get(Calendar.MONTH) + 1;

			String s;
			String[][] days = new String[cal.getActualMaximum(Calendar.WEEK_OF_MONTH)][7];

			// 1일 앞의 전달 날짜
			for (int i = 1; i < week; i++) {
				s = String.format("%04d%02d%02d", syear, smonth, sdate);
				days[0][i - 1] = "<span class='textDate preMonthDate' data-date='" + s + "' >" + sdate + "</span>";
				sdate++;
			}

			// year년도 month월 날짜
			int row, n = 0;
			
			jump:
			for (row = 0; row < days.length; row++) {
				for (int i = week - 1; i < 7; i++) {
					n++;
					s = String.format("%04d%02d%02d", year, month, n);

					if (i == 0) {
						days[row][i] = "<span class='textDate sundayDate' data-date='" + s + "' >" + n + "</span>";
					} else if (i == 6) {
						days[row][i] = "<span class='textDate saturdayDate' data-date='" + s + "' >" + n + "</span>";
					} else {
						days[row][i] = "<span class='textDate nowDate' data-date='" + s + "' >" + n + "</span>";
					}

					if (n == cal.getActualMaximum(Calendar.DATE)) {
						week = i + 1;
						break jump;
					}
				}
				week = 1;
			}

			// year년도 month월 마지막 날짜 이후
			if (week != 7) {
				n = 0;
				for (int i = week; i < 7; i++) {
					n++;
					s = String.format("%04d%02d%02d", eyear, emonth, n);
					days[row][i] = "<span class='textDate nextMonthDate' data-date='" + s + "' >" + n + "</span>";
				}
			}

			mav.addObject("year", year);
			mav.addObject("month", month);
			mav.addObject("day", day);
			mav.addObject("date", date);
			mav.addObject("today", today);
			mav.addObject("preMonth", preMonth);
			mav.addObject("nextMonth", nextMonth);

			mav.addObject("days", days);
			mav.addObject("dto", dto);
			mav.addObject("list", list);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return mav;
	}

	// AJAX - Text
	@RequestMapping(value = "/myCalendar/year", method = RequestMethod.GET)
	public ModelAndView yearMyCalendar(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ModelAndView mav = new ModelAndView("myCalendar/year");
		
		try {
			String syear = req.getParameter("year");
			
			Calendar cal = Calendar.getInstance();
			int year = cal.get(Calendar.YEAR);

			int todayYear = cal.get(Calendar.YEAR);
			String today = String.format("%04d%02d%02d", 
					cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DATE));

			if (syear != null) {
				year = Integer.parseInt(syear);
			}
			if (year < 1900) {
				year = cal.get(Calendar.YEAR);
			}

			String days[][][] = new String[12][6][7];

			int row, col, month_of_day;
			String s;
			for (int m = 1; m <= 12; m++) {
				cal.set(year, m - 1, 1);
				row = 0;
				col = cal.get(Calendar.DAY_OF_WEEK) - 1;
				month_of_day = cal.getActualMaximum(Calendar.DATE);
				for (int i = 1; i <= month_of_day; i++) {
					s = String.format("%04d%02d%02d", year, m, i);

					if (col == 0) {
						days[m - 1][row][col] = "<span class='textDate sundayDate' data-date='" + s + "' >" + i + "</span>";
					} else if (col == 6) {
						days[m - 1][row][col] = "<span class='textDate saturdayDate' data-date='" + s + "' >" + i
								+ "</span>";
					} else {
						days[m - 1][row][col] = "<span class='textDate nowDate' data-date='" + s + "' >" + i + "</span>";
					}

					col++;
					if (col > 6) {
						col = 0;
						row++;
					}
				}
			}

			req.setAttribute("year", year);

			req.setAttribute("todayYear", todayYear);
			req.setAttribute("today", today);
			req.setAttribute("days", days);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return mav;
	}

	// AJAX - JSON
	@ResponseBody
	@RequestMapping(value = "/myCalendar/insert", method = RequestMethod.POST)
	public Map<String, Object> insertSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		MyCalendarDAO dao = new MyCalendarDAO();
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");
		
		Map<String, Object> model = new HashMap<String, Object>();
		
		String state = "false";
		try {
			MyCalendarDAO dto = new MyCalendarDAO();
			
			dto.setMemberIdx(info.getMemberIdx());
			dto.setSubject(req.getParameter("subject"));
			dto.setColor(req.getParameter("color"));
			dto.setSday(req.getParameter("sday").replaceAll("-", ""));
			dto.setEday(req.getParameter("eday").replaceAll("-", ""));
			dto.setStime(req.getParameter("stime").replaceAll(":", ""));
			dto.setEtime(req.getParameter("etime").replaceAll(":", ""));
			if (req.getParameter("allDay") != null) {
				dto.setStime("");
				dto.setEtime("");
			}

			if (dto.getStime().length() == 0 && dto.getEtime().length() == 0 && dto.getSday().equals(dto.getEday()))
				dto.setEday("");

			dto.setRepeat(Integer.parseInt(req.getParameter("repeat")));
			if (req.getParameter("repeat_cycle").length() != 0) {
				dto.setRepeat_cycle(Integer.parseInt(req.getParameter("repeat_cycle")));

				dto.setEday("");
				dto.setStime("");
				dto.setEtime("");
			}
			dto.setMemo(req.getParameter("memo"));

			dao.insertMyCalendar(dto);
			state = "true";
		} catch (Exception e) {
			e.printStackTrace();
		}

		model.put("state", state);

		return model;
	}

	// AJAX-JSON
	@ResponseBody
	@RequestMapping(value = "/myCalendar/update", method = RequestMethod.POST)
	public Map<String, Object> updateSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		MyCalendarDAO dao = new MyCalendarDAO();
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");

		Map<String, Object> model = new HashMap<String, Object>();
		
		String state = "false";
		try {
			MyCalendarDTO dto = new MyCalendarDTO();
			
			dto.setMemberIdx(info.getMemberIdx());
			dto.setNum(Long.parseLong(req.getParameter("num")));
			dto.setSubject(req.getParameter("subject"));
			dto.setColor(req.getParameter("color"));
			dto.setSday(req.getParameter("sday").replaceAll("-", ""));
			dto.setEday(req.getParameter("eday").replaceAll("-", ""));
			dto.setStime(req.getParameter("stime").replaceAll(":", ""));
			dto.setEtime(req.getParameter("etime").replaceAll(":", ""));
			
			if (req.getParameter("allDay") != null) {
				dto.setStime("");
				dto.setEtime("");
			}

			if (dto.getStime().length() == 0 && dto.getEtime().length() == 0 && dto.getSday().equals(dto.getEday())) {
				dto.setEday("");
			}

			dto.setRepeat(Integer.parseInt(req.getParameter("repeat")));
			if (req.getParameter("repeat_cycle").length() != 0) {
				dto.setRepeat_cycle(Integer.parseInt(req.getParameter("repeat_cycle")));

				dto.setEday("");
				dto.setStime("");
				dto.setEtime("");
			}
			dto.setMemo(req.getParameter("memo"));

			dao.updateMyCalendar(dto);
			state = "true";
		} catch (Exception e) {
			e.printStackTrace();
		}

		model.put("state", state);

		return model;
	}

	// AJAX-JSON
	@ResponseBody
	@RequestMapping(value = "/myCalendar/delete", method = RequestMethod.POST)
	public Map<String, Object> deleteSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		MyCalendarDAO dao = new MyCalendarDAO();
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");

		Map<String, Object> model = new HashMap<String, Object>();
		
		String state = "false";
		try {
			long num = Long.parseLong(req.getParameter("num"));

			dao.deleteMyCalendar(num, info.getMemberIdx());
			state = "true"; 
		} catch (Exception e) {
			e.printStackTrace();
		}

		model.put("state", state);

		return model;
	}
}
