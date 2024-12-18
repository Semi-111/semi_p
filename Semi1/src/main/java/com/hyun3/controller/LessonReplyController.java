package com.hyun3.controller;

import java.io.IOException;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.hyun3.dao.lesson.LessonDAO;
import com.hyun3.domain.SessionInfo;
import com.hyun3.domain.lesson.LessonReplyDTO;
import com.hyun3.mvc.annotation.Controller;
import com.hyun3.mvc.annotation.RequestMapping;
import com.hyun3.util.MyUtil;
import com.hyun3.util.MyUtilBootstrap;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class LessonReplyController {
    
    // 댓글 등록
    @RequestMapping(value = "/lessonBoard/insertReply")
    public void insertReply(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LessonDAO dao = new LessonDAO();
        HttpSession session = req.getSession();
        SessionInfo info = (SessionInfo) session.getAttribute("member");
        
        String state = "true";
        
        try {
        	
        	LessonReplyDTO dto = new LessonReplyDTO();
            
            dto.setCm_num(Integer.parseInt(req.getParameter("cm_num")));
            dto.setContent(req.getParameter("content"));
            dto.setMb_num(info.getMb_Num());
            
            dao.insertReply(dto);
            
        } catch (Exception e) {
            state = "false";
        }
        
        // JSON 응답 직접 처리
        resp.setContentType("application/json; charset=utf-8");
        JSONObject job = new JSONObject();
        job.put("state", state);
        resp.getWriter().write(job.toString());
    }
    
    // 댓글 리스트
    @RequestMapping(value = "/lessonBoard/listReply")
    public void listReply(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LessonDAO dao = new LessonDAO();
        MyUtil util = new MyUtilBootstrap();
        
        try {
            int cm_num = Integer.parseInt(req.getParameter("cm_num"));
            String pageNo = req.getParameter("pageNo");
            int current_page = 1;
            if (pageNo != null) {
                current_page = Integer.parseInt(pageNo);
            }
            
            int size = 5;
            int total_page = 0;
            int dataCount = 0;
            
            dataCount = dao.dataCount(cm_num);
            total_page = util.pageCount(dataCount, size);
            if (current_page > total_page) {
                current_page = total_page;
            }
            
            int offset = (current_page - 1) * size;
            if(offset < 0) offset = 0;
            
            List<LessonReplyDTO> list = dao.listReply(cm_num, offset, size);
            
            // JSON으로 변환
            JSONObject job = new JSONObject();
            job.put("dataCount", dataCount);
            job.put("total_page", total_page);
            job.put("pageNo", current_page);
            
            JSONArray jarr = new JSONArray();
            for(LessonReplyDTO dto : list) {
                JSONObject ob = new JSONObject();
                
                ob.put("co_num", dto.getCo_num());
                ob.put("content", dto.getContent());
                ob.put("mb_num", dto.getMb_num());
                ob.put("reg_date", dto.getReg_date());
                ob.put("nickName", dto.getNickName());
                
                jarr.put(ob);
            }
            
            job.put("list", jarr);
            
            resp.setContentType("application/json; charset=utf-8");
            resp.getWriter().write(job.toString());
            
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendError(500);
        }
    }
    
    // 댓글 삭제
    @RequestMapping(value = "/lessonBoard/deleteReply")
    public void deleteReply(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LessonDAO dao = new LessonDAO();
        
        HttpSession session = req.getSession();
        SessionInfo info = (SessionInfo) session.getAttribute("member");
        
        String state = "true";
        
        try {
            int co_num = Integer.parseInt(req.getParameter("co_num"));
            dao.deleteReply(co_num, info.getMb_Num());
        } catch (Exception e) {
            state = "false";
        }
        
        resp.setContentType("application/json; charset=utf-8");
        JSONObject job = new JSONObject();
        job.put("state", state);
        resp.getWriter().write(job.toString());
    }
    
    // 댓글 수정
 // 댓글 수정
    @RequestMapping(value = "/lessonBoard/updateReply")
    public void updateReply(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LessonDAO dao = new LessonDAO();
        
        HttpSession session = req.getSession();
        SessionInfo info = (SessionInfo) session.getAttribute("member");
        
        String state = "false";
        
        try {
            LessonReplyDTO dto = new LessonReplyDTO();
            
            dto.setCo_num(Integer.parseInt(req.getParameter("co_num")));
            dto.setContent(req.getParameter("content"));
            dto.setMb_num(info.getMb_Num());
            
            dao.updateReply(dto);
            
            state = "true";
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        resp.setContentType("application/json; charset=utf-8");
        JSONObject job = new JSONObject();
        job.put("state", state);
        
        resp.getWriter().write(job.toString());
    }
}
